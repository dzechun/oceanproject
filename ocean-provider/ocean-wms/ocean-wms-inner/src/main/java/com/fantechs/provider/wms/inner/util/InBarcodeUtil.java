package com.fantechs.provider.wms.inner.util;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductPalletDetDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcProductPalletDet;
import com.fantechs.common.base.general.dto.wms.inner.BarcodeResultDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStockOrderDetBarcodeDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductPallet;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderReMspp;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerMaterialBarcode;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerStockOrderDetBarcode;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.api.wms.in.InFeignApi;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerJobOrderMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerJobOrderReMsppMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerMaterialBarcodeService;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 入库条码校验
 * @author mr.lei
 */
@Component
public class InBarcodeUtil {
    //Feign
    @Resource
    private SFCFeignApi sfcFeignApi;
    @Resource
    private WmsInnerInventoryDetMapper wmsInnerInventoryDetMapper;
    @Resource
    private WmsInnerJobOrderMapper wmsInnerJobOrderMapper;
    @Resource
    private WmsInnerJobOrderReMsppMapper wmsInnerJobOrderReMsppMapper;
    @Resource
    private InFeignApi inFeignApi;
    @Resource
    private WmsInnerMaterialBarcodeService wmsInnerMaterialBarcodeService;

    private static InBarcodeUtil inBarcodeUtil;

    //初始化
    @PostConstruct
    public void init(){
        inBarcodeUtil = this;
        inBarcodeUtil.sfcFeignApi = sfcFeignApi;
        inBarcodeUtil.wmsInnerInventoryDetMapper = wmsInnerInventoryDetMapper;
        inBarcodeUtil.wmsInnerJobOrderMapper = wmsInnerJobOrderMapper;
        inBarcodeUtil.wmsInnerJobOrderReMsppMapper = wmsInnerJobOrderReMsppMapper;
        inBarcodeUtil.inFeignApi = inFeignApi;
        inBarcodeUtil.wmsInnerMaterialBarcodeService=wmsInnerMaterialBarcodeService;
    }

    /**
     * 校验成品条码获取栈板数量
     * @param workOrderId
     * @param barCode
     * @return
     */
    public static BigDecimal checkBarCode(Long workOrderId, String barCode){
        MesSfcWorkOrderBarcode mesSfcWorkOrderBarcode = inBarcodeUtil.sfcFeignApi.findBarcode(barCode).getData();
        if(StringUtils.isEmpty(mesSfcWorkOrderBarcode)){
           throw new BizErrorException("不存在该条码");
        }
        //条码是否为工单条码且是否为对应工单
        if(mesSfcWorkOrderBarcode.getLabelCategoryId().equals(01) && mesSfcWorkOrderBarcode.getWorkOrderId()!=workOrderId){
            throw new BizErrorException("该条码不属于该工单");
        }
        //查询工单条码关联展板id
        SearchMesSfcProductPalletDet searchMesSfcProductPalletDet = new SearchMesSfcProductPalletDet();
        searchMesSfcProductPalletDet.setWorkOrderBarcodeId(mesSfcWorkOrderBarcode.getWorkOrderBarcodeId());
        ResponseEntity<List<MesSfcProductPalletDetDto>> responseEntity = inBarcodeUtil.sfcFeignApi.findList(searchMesSfcProductPalletDet);
        if(responseEntity.getCode()!=0){
            throw new BizErrorException("检验条码失败");
        }
        List<MesSfcProductPalletDetDto> mesSfcProductPalletDetDtos = responseEntity.getData();
        BigDecimal qty = BigDecimal.ZERO;
        //统计栈板数量
        for (MesSfcProductPalletDetDto mesSfcProductPalletDetDto : mesSfcProductPalletDetDtos) {
            MesSfcProductPallet mesSfcProductPallet = inBarcodeUtil.sfcFeignApi.detail(mesSfcProductPalletDetDto.getProductPalletId()).getData();
            if(!StringUtils.isEmpty(mesSfcProductPallet) && !StringUtils.isEmpty(mesSfcProductPallet.getNowPackageSpecQty())){
                qty.add(mesSfcProductPallet.getNowPackageSpecQty());
            }
        }
        //展板查询包装数量
        if(qty.compareTo(BigDecimal.ZERO)==0){
            throw new BizErrorException("暂无数量");
        }
        return qty;
    }

    /**
     * 拣货作业匹配条码
     * @param inventoryStatusId
     * @param materialId
     * @param barCode
     * @return
     */
    public static BigDecimal pickCheckBarCode(Long inventoryStatusId, Long materialId,String barCode){
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        //查询库存明细是否存在改条码
        Example example = new Example(WmsInnerInventoryDet.class);
        example.createCriteria().andEqualTo("materialId",materialId).andEqualTo("barcode",barCode).andEqualTo("orgId",sysUser.getOrganizationId())
                .andEqualTo("barcodeStatus",3);
        List<WmsInnerInventoryDet> list = inBarcodeUtil.wmsInnerInventoryDetMapper.selectByExample(example);
        if(list.size()<1){
            throw new BizErrorException("条码不存在");
        }
        if (StringUtils.isNotEmpty(inventoryStatusId)){
            list = list.stream().filter(li->li.getInventoryStatusId().equals(inventoryStatusId)).collect(Collectors.toList());
        }
/*        BigDecimal totalQty =list.stream()
                .map(WmsInnerInventoryDet::getMaterialQty)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        if(totalQty.compareTo(BigDecimal.ZERO)==0){
            throw new BizErrorException("暂无入库数量");
        }
        return totalQty;*/
        return BigDecimal.ZERO;
    }

    /**
     * 获取栈板绑定工单条码信息
     * @param jobOrderId
     * @return
     */
    public static String getWorkBarCodeList(Long jobOrderId){
        Example example = new Example(WmsInnerJobOrderReMspp.class);
        example.createCriteria().andEqualTo("jobOrderId",jobOrderId);
        WmsInnerJobOrderReMspp wmsInnerJobOrderReMspp = inBarcodeUtil.wmsInnerJobOrderReMsppMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(wmsInnerJobOrderReMspp)){
            throw new BizErrorException("信息匹配失败");
        }
        //获取栈板绑定
        List<String> barCodeList = inBarcodeUtil.wmsInnerJobOrderMapper.workBarCodeList(wmsInnerJobOrderReMspp.getProductPalletId());
        if(barCodeList.size()<1){
            throw new BizErrorException("条码信息匹配失败");
        }
        String barCode = Joiner.on(",").join(barCodeList);
        return barCode;
    }

    /**
     * 扫描条码返回条码类型 最少包装单位数量
     * @param barcode
     * @return BarcodeResultDto 条码信息
     */
    public static BarcodeResultDto scanBarcode(String barcode) {
        BarcodeResultDto barcodeResultDto=new BarcodeResultDto();
        if(StringUtils.isEmpty(barcode)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"条码不能为空");
        }

        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();

        //条码判断
        List<WmsInnerMaterialBarcodeDto> barcodeDtos=new ArrayList<>();
        SearchWmsInnerMaterialBarcode searchWmsInnerMaterialBarcode=new SearchWmsInnerMaterialBarcode();
        searchWmsInnerMaterialBarcode.setBarcode(barcode);
        searchWmsInnerMaterialBarcode.setOrgId(sysUser.getOrganizationId());
        barcodeDtos=inBarcodeUtil.wmsInnerMaterialBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerMaterialBarcode));
        if(barcodeDtos.size()>0){
            //SN码
            if(barcodeDtos.get(0).getBarcodeStatus()>=(byte)5){
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"条码已扫描 请勿重复扫码-->"+barcode);
            }
            barcodeResultDto.setBarcodeType((byte)1);
            barcodeResultDto.setMaterialQty(barcodeDtos.get(0).getMaterialQty());
            barcodeResultDto.setBarcode(barcode);
            barcodeResultDto.setMaterialBarcodeId(barcodeDtos.get(0).getMaterialBarcodeId());
            barcodeResultDto.setMaterialId(barcodeDtos.get(0).getMaterialId());
        }
        else {
            //彩盒
            searchWmsInnerMaterialBarcode.setBarcode(null);
            searchWmsInnerMaterialBarcode.setColorBoxCode(barcode);
            barcodeDtos=inBarcodeUtil.wmsInnerMaterialBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerMaterialBarcode));
            if(barcodeDtos.size()>0){
                List<WmsInnerMaterialBarcodeDto> barcodeListOne = barcodeDtos.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode())?"":u.getBarcode())=="")).collect(Collectors.toList());
                if(barcodeListOne.get(0).getBarcodeStatus()>=(byte)5){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"条码已扫描 请勿重复扫码-->"+barcode);
                }
                barcodeResultDto.setBarcodeType((byte)2);
                barcodeResultDto.setMaterialQty(barcodeDtos.get(0).getMaterialQty());
                barcodeResultDto.setBarcode(barcode);
                barcodeResultDto.setMaterialBarcodeId(barcodeDtos.get(0).getMaterialBarcodeId());
                barcodeResultDto.setMaterialId(barcodeDtos.get(0).getMaterialId());
            }
            else {
                //箱码
                searchWmsInnerMaterialBarcode.setBarcode(null);
                searchWmsInnerMaterialBarcode.setColorBoxCode(null);
                searchWmsInnerMaterialBarcode.setCartonCode(barcode);
                barcodeDtos=inBarcodeUtil.wmsInnerMaterialBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerMaterialBarcode));
                if(barcodeDtos.size()>0){
                    List<WmsInnerMaterialBarcodeDto> barcodeListOne = barcodeDtos.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode())?"":u.getBarcode())=="")).collect(Collectors.toList());
                    if(barcodeListOne.get(0).getBarcodeStatus()>=(byte)5){
                        throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"条码已扫描 请勿重复扫码-->"+barcode);
                    }
                    List<WmsInnerMaterialBarcodeDto> barcodeList = barcodeDtos.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode())?"":u.getBarcode())!="")).collect(Collectors.toList());
                    BigDecimal totalQty=barcodeList.stream().map(WmsInnerMaterialBarcodeDto::getMaterialQty).reduce(BigDecimal.ZERO,BigDecimal::add);
                    barcodeResultDto.setBarcodeType((byte)3);
                    barcodeResultDto.setMaterialQty(totalQty);
                    barcodeResultDto.setBarcode(barcode);
                    barcodeResultDto.setMaterialBarcodeId(barcodeDtos.get(0).getMaterialBarcodeId());
                    barcodeResultDto.setMaterialId(barcodeDtos.get(0).getMaterialId());
                }
                else {
                    //栈板
                    searchWmsInnerMaterialBarcode.setBarcode(null);
                    searchWmsInnerMaterialBarcode.setColorBoxCode(null);
                    searchWmsInnerMaterialBarcode.setCartonCode(null);
                    searchWmsInnerMaterialBarcode.setPalletCode(barcode);
                    barcodeDtos=inBarcodeUtil.wmsInnerMaterialBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerMaterialBarcode));
                    if(barcodeDtos.size()>0){
                        List<WmsInnerMaterialBarcodeDto> barcodeListOne = barcodeDtos.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode())?"":u.getBarcode())=="")).collect(Collectors.toList());
                        if(barcodeListOne.get(0).getBarcodeStatus()>=(byte)5){
                            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"条码已扫描 请勿重复扫码-->"+barcode);
                        }
                        List<WmsInnerMaterialBarcodeDto> barcodeList = barcodeDtos.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode())?"":u.getBarcode())!="")).collect(Collectors.toList());
                        BigDecimal totalQty=barcodeList.stream().map(WmsInnerMaterialBarcodeDto::getMaterialQty).reduce(BigDecimal.ZERO,BigDecimal::add);
                        barcodeResultDto.setBarcodeType((byte)4);
                        barcodeResultDto.setMaterialQty(totalQty);
                        barcodeResultDto.setBarcode(barcode);
                        barcodeResultDto.setMaterialBarcodeId(barcodeDtos.get(0).getMaterialBarcodeId());
                        barcodeResultDto.setMaterialId(barcodeDtos.get(0).getMaterialId());
                    }
                }
            }
        }

        if(StringUtils.isEmpty(barcodeResultDto.getBarcodeType())){
            barcodeResultDto.setBarcodeType((byte)5);
            barcodeResultDto.setBarcode("");
            barcodeResultDto.setMaterialQty(new BigDecimal(0));
        }

        return barcodeResultDto;
    }
}
