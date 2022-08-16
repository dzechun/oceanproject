package com.fantechs.provider.wms.in.service.impl;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcBarcodeProcessRecordDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductCartonDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductPalletDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcessRecord;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcProductCarton;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcProductPallet;
import com.fantechs.common.base.general.dto.wms.in.WmsInAsnOrderDetDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInAsnOrderDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDetDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrderDet;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInAsnOrder;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInAsnOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerMaterialBarcode;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutDeliveryOrderDet;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.api.wms.out.OutFeignApi;
import com.fantechs.provider.wms.in.mapper.WmsInAsnOrderDetMapper;
import com.fantechs.provider.wms.in.mapper.WmsInAsnOrderMapper;
import com.fantechs.provider.wms.in.service.WmsInAsnOrderDetService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by Mr.Lei on 2021/04/29.
 */
@Service
public class WmsInAsnOrderDetServiceImpl extends BaseService<WmsInAsnOrderDet> implements WmsInAsnOrderDetService {

    @Resource
    private WmsInAsnOrderDetMapper wmsInAsnOrderDetMapper;
    @Resource
    private WmsInAsnOrderMapper wmsInAsnOrderMapper;
    @Resource
    private OutFeignApi outFeignApi;
    @Resource
    private InnerFeignApi innerFeignApi;
    @Resource
    private SFCFeignApi sfcFeignApi;
    @Resource
    private WmsInAsnOrderServiceImpl wmsInAsnOrderService;

    @Override
    public List<WmsInAsnOrderDetDto> findList(SearchWmsInAsnOrderDet searchWmsInAsnOrderDet) {
        return wmsInAsnOrderDetMapper.findList(searchWmsInAsnOrderDet);
    }

    /**
     * PDA收货作业条码检查
     * @param wmsInAsnOrderDetDto 必传：AsnOrderDetId、AsnOrderId、Barcode
     * @return
     */
    @Override
    public BigDecimal checkBarcode(WmsInAsnOrderDetDto wmsInAsnOrderDetDto) {
        SearchWmsInAsnOrderDet searchWmsInAsnOrderDet = new SearchWmsInAsnOrderDet();
        searchWmsInAsnOrderDet.setAsnOrderDetId(wmsInAsnOrderDetDto.getAsnOrderDetId());
        WmsInAsnOrderDetDto asnOrderDetDto = wmsInAsnOrderDetMapper.findList(searchWmsInAsnOrderDet).get(0);

        SearchWmsInAsnOrder searchWmsInAsnOrder = new SearchWmsInAsnOrder();
        searchWmsInAsnOrder.setAsnOrderId(wmsInAsnOrderDetDto.getAsnOrderId());
        WmsInAsnOrderDto asnOrderDto = wmsInAsnOrderMapper.findList(searchWmsInAsnOrder).get(0);

        String barcode = wmsInAsnOrderDetDto.getBarcode();

        //判断在库存明细中是否存在（除调拨入库单外）
        if(!barcode.equals(asnOrderDetDto.getMaterialCode())&&asnOrderDto.getOrderTypeId()!=3) {
            SearchWmsInnerInventoryDet searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
            searchWmsInnerInventoryDet.setBarcode(barcode);
            List<WmsInnerInventoryDetDto> wmsInnerInventoryDetDtos = innerFeignApi.findList(searchWmsInnerInventoryDet).getData();
            if (StringUtils.isNotEmpty(wmsInnerInventoryDetDtos)) {
                throw new BizErrorException("该条码已入库，不可重复入库");
            }
        }

        if (wmsInAsnOrderDetDto.getIsComingMaterial() != null && wmsInAsnOrderDetDto.getIsComingMaterial() == 1) {//是来料
            return wmsInAsnOrderDetDto.getDefaultQty();
        }else if (barcode.equals(asnOrderDetDto.getMaterialCode())) {//非来料且是物料编码
            return BigDecimal.ZERO;
        }else {//非来料且非物料编码
            if(asnOrderDto.getOrderTypeId()==3) {
                //调拨入库

                SearchWmsOutDeliveryOrderDet searchWmsOutDeliveryOrderDet = new SearchWmsOutDeliveryOrderDet();
                searchWmsOutDeliveryOrderDet.setDeliveryOrderId(asnOrderDetDto.getSourceOrderId());
                List<WmsOutDeliveryOrderDetDto> wmsOutDeliveryOrderDetDtos = outFeignApi.findList(searchWmsOutDeliveryOrderDet).getData();

                //判断是否能入库
                SearchWmsInnerInventoryDet searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
                searchWmsInnerInventoryDet.setNotEqualMark(0);
                searchWmsInnerInventoryDet.setStorageId(wmsOutDeliveryOrderDetDtos.get(0).getStorageId());
                searchWmsInnerInventoryDet.setBarcode(barcode);
                List<WmsInnerInventoryDetDto> inventoryDetDtos1 = innerFeignApi.findList(searchWmsInnerInventoryDet).getData();
                if (StringUtils.isEmpty(inventoryDetDtos1)) {
                    throw new BizErrorException("调拨入库单：库存明细不存在该条码，不允许入库");
                }

                //判断条码是否重复
                searchWmsInnerInventoryDet.setNotEqualMark(1);
                List<WmsInnerInventoryDetDto> inventoryDetDtos2 = innerFeignApi.findList(searchWmsInnerInventoryDet).getData();
                if (StringUtils.isNotEmpty(inventoryDetDtos2)) {
                    throw new BizErrorException("该条码已入库，不可重复入库");
                }

            }else if(asnOrderDto.getOrderTypeId()==4){
                //完工入库
                SearchMesSfcBarcodeProcessRecord searchMesSfcBarcodeProcessRecord = new SearchMesSfcBarcodeProcessRecord();
                searchMesSfcBarcodeProcessRecord.setBarcode(barcode);
                List<MesSfcBarcodeProcessRecordDto> mesSfcBarcodeProcessRecordDtos = sfcFeignApi.findList(searchMesSfcBarcodeProcessRecord).getData();
                if(StringUtils.isEmpty(mesSfcBarcodeProcessRecordDtos)){
                    throw new BizErrorException("完工入库：过站记录不存在该条码，不允许入库");
                }
            }else {
                //剩余类型入库单
                SearchWmsInnerMaterialBarcode searchWmsInnerMaterialBarcode = new SearchWmsInnerMaterialBarcode();
                searchWmsInnerMaterialBarcode.setBarcode(barcode);
                List<WmsInnerMaterialBarcodeDto> wmsInnerMaterialBarcodeDtos = innerFeignApi.findList(searchWmsInnerMaterialBarcode).getData();
                if(StringUtils.isEmpty(wmsInnerMaterialBarcodeDtos)){
                    throw new BizErrorException("来料打印不存在该条码，不允许入库");
                }
            }



            //通过条码带出数量
            //1、是否栈板条码
            SearchMesSfcProductPallet searchMesSfcProductPallet = new SearchMesSfcProductPallet();
            searchMesSfcProductPallet.setPalletCode(barcode);
            List<MesSfcProductPalletDto> mesSfcProductPalletDtos = sfcFeignApi.findProductPalletList(searchMesSfcProductPallet).getData();
            if(StringUtils.isNotEmpty(mesSfcProductPalletDtos)){
                return mesSfcProductPalletDtos.get(0).getNowPackageSpecQty();
            }

            //2、是否包箱条码
            SearchMesSfcProductCarton searchMesSfcProductCarton = new SearchMesSfcProductCarton();
            searchMesSfcProductCarton.setCartonCode(barcode);
            List<MesSfcProductCartonDto> mesSfcProductCartonDtos = sfcFeignApi.findProductCartonList(searchMesSfcProductCarton).getData();
            if(StringUtils.isNotEmpty(mesSfcProductCartonDtos)){
                return mesSfcProductCartonDtos.get(0).getNowPackageSpecQty();
            }

            //3、是否产品条码
            /*SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode = new SearchMesSfcWorkOrderBarcode();
            searchMesSfcWorkOrderBarcode.setBarcode(barcode);
            List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtos = sfcFeignApi.findList(searchMesSfcWorkOrderBarcode).getData();
            if(StringUtils.isNotEmpty(mesSfcWorkOrderBarcodeDtos)){
                return BigDecimal.ONE;
            }*/

            return BigDecimal.ONE;
        }

    }

    /**
     * PDA收货作业提交
     * @param wmsInAsnOrderDetDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(WmsInAsnOrderDetDto wmsInAsnOrderDetDto) {

        SearchWmsInAsnOrder searchWmsInAsnOrder = new SearchWmsInAsnOrder();
        searchWmsInAsnOrder.setAsnOrderId(wmsInAsnOrderDetDto.getAsnOrderId());
        WmsInAsnOrderDto wmsInAsnOrderDto = wmsInAsnOrderMapper.findList(searchWmsInAsnOrder).get(0);

        //非物料编码，新增（修改）库存明细
        List<String> barcodes = wmsInAsnOrderDetDto.getBarcodes();
        for(String barcode:barcodes){
            if(!barcode.equals(wmsInAsnOrderDetDto.getMaterialCode())&&wmsInAsnOrderDto.getOrderTypeId()!=3){
                List<WmsInnerInventoryDet> wmsInnerInventoryDets = new ArrayList<>();
                WmsInnerInventoryDet wmsInnerInventoryDet = new WmsInnerInventoryDet();
                wmsInnerInventoryDet.setStorageId(wmsInAsnOrderDetDto.getStorageId());
                wmsInnerInventoryDet.setMaterialId(wmsInAsnOrderDetDto.getMaterialId());
                wmsInnerInventoryDet.setBarcode(barcode);
                wmsInnerInventoryDet.setMaterialQty(BigDecimal.ONE);
                wmsInnerInventoryDet.setProductionDate(wmsInAsnOrderDetDto.getProductionDate());
                wmsInnerInventoryDet.setProductionBatchCode(wmsInAsnOrderDetDto.getBatchCode());
                wmsInnerInventoryDet.setAsnCode(wmsInAsnOrderDto.getAsnCode());
                wmsInnerInventoryDet.setReceivingDate(new Date());
                wmsInnerInventoryDet.setBarcodeStatus((byte)2);
                wmsInnerInventoryDets.add(wmsInnerInventoryDet);
                ResponseEntity responseEntity = innerFeignApi.add(wmsInnerInventoryDets);
                if(responseEntity.getCode()!=0){
                    throw new BizErrorException("新增库存明细失败");
                }
            }else if(!barcode.equals(wmsInAsnOrderDetDto.getMaterialCode())&&wmsInAsnOrderDto.getOrderTypeId()==3){
                WmsInnerInventoryDet inventoryDet = innerFeignApi.findByDet(barcode).getData();
                inventoryDet.setStorageId(wmsInAsnOrderDetDto.getStorageId());
                inventoryDet.setMaterialQty(BigDecimal.ONE);
                inventoryDet.setAsnCode(wmsInAsnOrderDto.getAsnCode());
                inventoryDet.setReceivingDate(new Date());
                inventoryDet.setBarcodeStatus((byte)2);
                ResponseEntity responseEntity = innerFeignApi.update(inventoryDet);
                if(responseEntity.getCode()!=0){
                    throw new BizErrorException("修改库存明细失败");
                }
            }
        }

        WmsInAsnOrderDet wmsInAsnOrderDet = new WmsInAsnOrderDet();
        BeanUtils.copyProperties(wmsInAsnOrderDetDto,wmsInAsnOrderDet);
        int i = wmsInAsnOrderService.singleReceiving(wmsInAsnOrderDet);

        return i;
    }

    /**
     * 计算两个日期之间相差的天数
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static Integer daysBetween(Date smdate, Date bdate) {
        if(!StringUtils.isNotEmpty(smdate,bdate)){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long between_days = 0;
        try {
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bdate);
            long time2 = cal.getTimeInMillis();
            between_days = (time2 - time1) / (1000 * 3600 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(String.valueOf(between_days));
    }
}
