package com.fantechs.provider.wms.in.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcBarcodeProcessRecordDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductCartonDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductPalletDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderBarcodeDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcessRecord;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcProductCarton;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcProductPallet;
import com.fantechs.common.base.general.dto.wms.in.WmsInAsnOrderDetDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInAsnOrderDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDetDto;
import com.fantechs.common.base.general.entity.mes.sfc.SearchMesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrder;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrderDet;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInAsnOrder;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInAsnOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutDeliveryOrderDet;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.api.wms.out.OutFeignApi;
import com.fantechs.provider.wms.in.mapper.WmsInAsnOrderDetMapper;
import com.fantechs.provider.wms.in.mapper.WmsInAsnOrderMapper;
import com.fantechs.provider.wms.in.service.WmsInAsnOrderDetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
        if (wmsInAsnOrderDetDto.getIsComingMaterial() != null && wmsInAsnOrderDetDto.getIsComingMaterial() == 1) {//是来料
            return wmsInAsnOrderDetDto.getDefaultQty();
        }else if (barcode.equals(asnOrderDetDto.getMaterialCode())) {//非来料且是物料编码
            return new BigDecimal(0);
        }else {//非来料且非物料编码
            //判断是否能入库
            if(asnOrderDto.getOrderTypeId()==3) {
                //调拨入库 : 调拨入库单--调拨出库单--拣货作业单--库存明细--是否有该条码
                SearchWmsOutDeliveryOrderDet searchWmsOutDeliveryOrderDet = new SearchWmsOutDeliveryOrderDet();
                searchWmsOutDeliveryOrderDet.setDeliveryOrderId(asnOrderDetDto.getSourceOrderId());
                List<WmsOutDeliveryOrderDetDto> wmsOutDeliveryOrderDetDtos = outFeignApi.findList(searchWmsOutDeliveryOrderDet).getData();

                SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
                searchWmsInnerJobOrder.setSourceOrderId(wmsOutDeliveryOrderDetDtos.get(0).getDeliveryOrderId());
                List<WmsInnerJobOrderDto> wmsInnerJobOrderDtos = innerFeignApi.findPickingOrderList(searchWmsInnerJobOrder).getData();

                SearchWmsInnerInventoryDet searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
                searchWmsInnerInventoryDet.setRelevanceOrderCode(wmsInnerJobOrderDtos.get(0).getJobOrderCode());
                List<WmsInnerInventoryDetDto> wmsInnerInventoryDetDtos = innerFeignApi.findList(searchWmsInnerInventoryDet).getData();

                boolean tag = false;
                for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto:wmsInnerInventoryDetDtos){
                    if(barcode.equals(wmsInnerInventoryDetDto.getBarcode())){
                        tag = true;
                    }
                }
                if(!tag){
                    throw new BizErrorException("调拨入库单：库存明细不存在该条码，不允许入库");
                }
            }else {
                //完工入库
                //判断在库存明细中是否存在（除调拨入库单外）
                WmsInnerInventoryDet inventoryDet = innerFeignApi.findByDet(barcode).getData();
                if (StringUtils.isNotEmpty(inventoryDet)) {
                    throw new BizErrorException("库存明细中已存在该条码");
                }

                SearchMesSfcBarcodeProcessRecord searchMesSfcBarcodeProcessRecord = new SearchMesSfcBarcodeProcessRecord();
                searchMesSfcBarcodeProcessRecord.setBarcode(barcode);
                List<MesSfcBarcodeProcessRecordDto> mesSfcBarcodeProcessRecordDtos = sfcFeignApi.findList(searchMesSfcBarcodeProcessRecord).getData();
                if(StringUtils.isEmpty(mesSfcBarcodeProcessRecordDtos)){
                    throw new BizErrorException("完工入库：过站记录不存在该条码，不允许入库");
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
            SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode = new SearchMesSfcWorkOrderBarcode();
            searchMesSfcWorkOrderBarcode.setBarcode(barcode);
            List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtos = sfcFeignApi.findList(searchMesSfcWorkOrderBarcode).getData();
            if(StringUtils.isNotEmpty(mesSfcWorkOrderBarcodeDtos)){
                return new BigDecimal(1);
            }else {
                throw new BizErrorException("该条码不合法");
            }
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
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(sysUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        SearchWmsInAsnOrder searchWmsInAsnOrder = new SearchWmsInAsnOrder();
        searchWmsInAsnOrder.setAsnOrderId(wmsInAsnOrderDetDto.getAsnOrderId());
        WmsInAsnOrderDto wmsInAsnOrderDto = wmsInAsnOrderMapper.findList(searchWmsInAsnOrder).get(0);

        //非物料编码，新增（修改）库存明细
        List<WmsInnerInventoryDet> wmsInnerInventoryDets = new ArrayList<>();
        List<String> barcodes = wmsInAsnOrderDetDto.getBarcodes();
        for(String barcode:barcodes){
            if(!barcode.equals(wmsInAsnOrderDetDto.getMaterialCode())&&wmsInAsnOrderDto.getOrderTypeId()!=3){
                WmsInnerInventoryDet wmsInnerInventoryDet = new WmsInnerInventoryDet();
                wmsInnerInventoryDet.setStorageId(wmsInAsnOrderDetDto.getStorageId());
                wmsInnerInventoryDet.setMaterialId(wmsInAsnOrderDetDto.getMaterialId());
                wmsInnerInventoryDet.setBarcode(barcode);
                wmsInnerInventoryDet.setMaterialQty(wmsInAsnOrderDetDto.getActualQty());
                wmsInnerInventoryDet.setExpirationDate(this.daysBetween(wmsInAsnOrderDetDto.getProductionDate(),wmsInAsnOrderDetDto.getExpiredDate()));
                wmsInnerInventoryDet.setProductionDate(wmsInAsnOrderDetDto.getProductionDate());
                wmsInnerInventoryDet.setProductionBatchCode(wmsInAsnOrderDetDto.getBatchCode());
                wmsInnerInventoryDet.setRelatedOrderCode(wmsInAsnOrderDto.getAsnCode());
                wmsInnerInventoryDets.add(wmsInnerInventoryDet);
                ResponseEntity responseEntity = innerFeignApi.add(wmsInnerInventoryDets);
                if(responseEntity.getCode()!=0){
                    throw new BizErrorException("新增库存明细失败");
                }
            }else if(!barcode.equals(wmsInAsnOrderDetDto.getMaterialCode())&&wmsInAsnOrderDto.getOrderTypeId()==3){
                WmsInnerInventoryDet inventoryDet = innerFeignApi.findByDet(barcode).getData();
                inventoryDet.setInTime(null);
                inventoryDet.setStorageId(wmsInAsnOrderDetDto.getStorageId());
                inventoryDet.setMaterialQty(inventoryDet.getMaterialQty().add(wmsInAsnOrderDetDto.getActualQty()));
                ResponseEntity responseEntity = innerFeignApi.update(inventoryDet);
                if(responseEntity.getCode()!=0){
                    throw new BizErrorException("修改库存明细失败");
                }
            }
        }

        //新增（更新）库存
        SearchWmsInnerInventory searchWmsInnerInventory = new SearchWmsInnerInventory();
        searchWmsInnerInventory.setMaterialId(wmsInAsnOrderDetDto.getMaterialId());
        searchWmsInnerInventory.setStorageId(wmsInAsnOrderDetDto.getStorageId());
        searchWmsInnerInventory.setRelevanceOrderCode(wmsInAsnOrderDto.getAsnCode());
        searchWmsInnerInventory.setInventoryStatusId(wmsInAsnOrderDetDto.getInventoryStatusId());
        List<WmsInnerInventoryDto> wmsInnerInventoryDtos = innerFeignApi.findList(searchWmsInnerInventory).getData();
        if(StringUtils.isNotEmpty(wmsInnerInventoryDtos)){
            //修改库存
            WmsInnerInventory wmsInnerInventory = new WmsInnerInventory();
            wmsInnerInventory.setInventoryId(wmsInnerInventoryDtos.get(0).getInventoryId());
            wmsInnerInventory.setInventoryTotalQty(wmsInnerInventory.getInventoryTotalQty()==null ? wmsInAsnOrderDetDto.getActualQty() : wmsInnerInventory.getInventoryTotalQty().add(wmsInAsnOrderDetDto.getActualQty()));
            ResponseEntity responseEntity = innerFeignApi.update(wmsInnerInventory);
            if(responseEntity.getCode()!=0){
                throw new BizErrorException("库存修改失败");
            }
        }else {
            //添加库存
            WmsInnerInventory wmsInnerInventory = new WmsInnerInventory();
            wmsInnerInventory.setInventoryStatusId(wmsInAsnOrderDetDto.getInventoryStatusId());
            wmsInnerInventory.setReceivingDate(wmsInAsnOrderDto.getEndReceivingDate());
            wmsInnerInventory.setPackingUnitName(wmsInAsnOrderDetDto.getPackingUnitName());
            wmsInnerInventory.setPackingQty(wmsInAsnOrderDetDto.getActualQty());
            wmsInnerInventory.setPalletCode(wmsInAsnOrderDetDto.getPalletCode());
            wmsInnerInventory.setMaterialOwnerId(wmsInAsnOrderDto.getMaterialOwnerId());
            wmsInnerInventory.setRelevanceOrderCode(wmsInAsnOrderDto.getAsnCode());
            wmsInnerInventory.setMaterialId(wmsInAsnOrderDetDto.getMaterialId());
            wmsInnerInventory.setWarehouseId(wmsInAsnOrderDetDto.getWarehouseId());
            wmsInnerInventory.setStorageId(wmsInAsnOrderDetDto.getStorageId());
            wmsInnerInventory.setJobStatus((byte)1);
            wmsInnerInventory.setCreateTime(new Date());
            wmsInnerInventory.setCreateUserId(sysUser.getUserId());
            wmsInnerInventory.setModifiedTime(new Date());
            wmsInnerInventory.setModifiedUserId(sysUser.getUserId());
            wmsInnerInventory.setOrgId(sysUser.getOrganizationId());
            ResponseEntity responseEntity =innerFeignApi.insertSelective(wmsInnerInventory);
            if(responseEntity.getCode()!=0){
                throw new BizErrorException("库存添加失败");
            }
        }

        wmsInAsnOrderDetDto.setModifiedUserId(sysUser.getUserId());
        wmsInAsnOrderDetDto.setModifiedTime(new Date());
        int i = wmsInAsnOrderDetMapper.updateByPrimaryKeySelective(wmsInAsnOrderDetDto);

        //修改收货状态
        wmsInAsnOrderMapper.updateByPrimaryKeySelective(WmsInAsnOrder.builder()
                .asnOrderId(wmsInAsnOrderDetDto.getAsnOrderId())
                .orderStatus((byte) 2)
                .modifiedTime(new Date())
                .modifiedUserId(sysUser.getUserId())
                .build());

        //判断明细是否全部收货完成
        Example example = new Example(WmsInAsnOrderDet.class);
        example.createCriteria().andEqualTo("asnOrderId",wmsInAsnOrderDetDto.getAsnOrderId());
        List<WmsInAsnOrderDet> list = wmsInAsnOrderDetMapper.selectByExample(example);
        int size = list.size();
        list = list.stream().filter(li->!StringUtils.isEmpty(li.getActualQty()) && li.getPackingQty().compareTo(li.getActualQty())==0).collect(Collectors.toList());
        if (list.size() == size) {
            //收货结束时间及收货状态
            wmsInAsnOrderMapper.updateByPrimaryKeySelective(WmsInAsnOrder.builder()
                    .asnOrderId(wmsInAsnOrderDetDto.getAsnOrderId())
                    .endReceivingDate(new Date())
                    .orderStatus((byte) 3)
                    .modifiedTime(new Date())
                    .modifiedUserId(sysUser.getUserId())
                    .build());
        }

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
