package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcBarcodeProcessDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcess;
import com.fantechs.common.base.general.dto.wms.inner.*;
import com.fantechs.common.base.general.entity.wanbao.QmsInspectionOrder;
import com.fantechs.common.base.general.entity.wanbao.QmsInspectionOrderDetSample;
import com.fantechs.common.base.general.entity.wanbao.search.SearchQmsInspectionOrder;
import com.fantechs.common.base.general.entity.wanbao.search.SearchQmsInspectionOrderDetSample;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerBarcodeOperation;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;

import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.guest.wanbao.WanbaoFeignApi;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.wms.inner.mapper.WmsInnerBarcodeOperationMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerJobOrderDetMapper;
import com.fantechs.provider.wms.inner.service.*;
import com.fantechs.provider.wms.inner.util.InventoryLogUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2022/03/09.
 */
@Service
public class WmsInnerBarcodeOperationServiceImpl extends BaseService<WmsInnerBarcodeOperation> implements WmsInnerBarcodeOperationService {

    @Resource
    private WmsInnerBarcodeOperationMapper wmsInnerBarcodeOperationMapper;
    @Resource
    private WmsInnerInventoryDetMapper wmsInnerInventoryDetMapper;
    @Resource
    private WmsInnerJobOrderDetMapper wmsInnerJobOrderDetMapper;
    @Resource
    private WmsInnerInventoryMapper wmsInnerInventoryMapper;
    @Resource
    private WmsInnerInventoryDetService wmsInnerInventoryDetService;
    @Resource
    private WanbaoFeignApi wanbaoFeignApi;
    @Resource
    private SFCFeignApi sfcFeignApi;
    @Resource
    private WmsInnerJobOrderService wmsInnerJobOrderService;
    @Resource
    private WmsInnerJobOrderDetService wmsInnerJobOrderDetService;
    @Resource
    WmsInnerInventoryService wmsInnerInventoryService;

    @Override
    public List<WmsInnerBarcodeOperationDto> findList(Map<String, Object> map) {
        return wmsInnerBarcodeOperationMapper.findList(map);
    }

    @Override
    public WmsInnerInventoryDetDto scanBarcode(String barcode) {
        SysUser sysUser=currentUser();
        if(StringUtils.isEmpty(barcode)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"??????????????????");
        }
        WmsInnerInventoryDetDto wmsInnerInventoryDetDto=this.judgeBarcode(barcode,sysUser);
        if(wmsInnerInventoryDetDto.getBarcodeStatus()!=(byte)4){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"???????????????????????????");
        }

        //????????????(1-????????????2-????????????3-?????????4-????????????5-????????????6-????????????7-?????????)

        return wmsInnerInventoryDetDto;
    }

    @Override
    public WmsInnerInventoryDetDto scanReplaceBarcode(String barcode,String replaceBarcode) {
        SysUser sysUser=currentUser();
        Long materialId=null;
        Long replaceMaterialId=null;
        WmsInnerInventoryDetDto result=new WmsInnerInventoryDetDto();
        if(StringUtils.isEmpty(barcode)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"??????????????????");
        }
        if(StringUtils.isEmpty(replaceBarcode)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"????????????????????????");
        }
        if(barcode.equals(replaceBarcode)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"???????????????????????????");
        }
        result=this.judgeBarcode(barcode,sysUser);
        if(StringUtils.isNotEmpty(result)){
            materialId=result.getMaterialId();
        }
        result=this.judgeBarcode(replaceBarcode,sysUser);
        if(StringUtils.isNotEmpty(result)){
            replaceMaterialId=result.getMaterialId();
        }
        if(materialId.longValue()!=replaceMaterialId.longValue()){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"???????????????????????????????????????");
        }
        if(result.getBarcodeStatus()!=(byte)3){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"?????????????????????????????????");
        }

        SearchQmsInspectionOrderDetSample sDetSample=new SearchQmsInspectionOrderDetSample();
        sDetSample.setBarcode(result.getBarcode());
        sDetSample.setOrgId(sysUser.getOrganizationId());
        List<QmsInspectionOrderDetSample> detSampleList=wanbaoFeignApi.findList(sDetSample).getData();
        if(StringUtils.isEmpty(detSampleList) || detSampleList.size()<=0){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"?????????????????????");
        }
        else{
            if(StringUtils.isNotEmpty(detSampleList.get(0).getBarcodeStatus())
             && detSampleList.get(0).getBarcodeStatus()==(byte)0){
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"??????????????????????????????");
            }
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int replaceCommit(String barcode, String replaceBarcode) {
        int i=0;
        SysUser sysUser=currentUser();
        // ?????????????????????????????????????????????????????????????????????????????????????????????????????????
        // ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        // ??????????????????????????????????????????????????????????????????/????????????????????????
        // ??????????????????
        Example example = new Example(WmsInnerInventoryDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("barcode",barcode);
        criteria.andEqualTo("orgId",sysUser.getOrganizationId());
        WmsInnerInventoryDet oldBarcode=wmsInnerInventoryDetMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(oldBarcode)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"?????????????????????-->"+barcode);
        }

        Example exampleNew = new Example(WmsInnerInventoryDet.class);
        Example.Criteria criteriaNew = exampleNew.createCriteria();
        criteriaNew.andEqualTo("barcode",replaceBarcode);
        criteriaNew.andEqualTo("orgId",sysUser.getOrganizationId());
        WmsInnerInventoryDet newBarcode=wmsInnerInventoryDetMapper.selectOneByExample(exampleNew);
        if(StringUtils.isEmpty(newBarcode)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"???????????????????????????-->"+replaceBarcode);
        }

        //?????????????????????
        oldBarcode.setBarcodeStatus((byte)3);//??????
        oldBarcode.setModifiedUserId(sysUser.getUserId());
        oldBarcode.setModifiedTime(new Date());
        i=wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(oldBarcode);

        // ???????????????+1
        Example inventotyExample = new Example(WmsInnerInventory.class);
        Example.Criteria inventotyCriteria = inventotyExample.createCriteria();
        inventotyCriteria.andEqualTo("materialId", oldBarcode.getMaterialId())
                .andEqualTo("storageId", oldBarcode.getStorageId())
                .andEqualTo("relevanceOrderCode", oldBarcode.getDeliveryOrderCode())
                .andEqualTo("stockLock", 0)
                .andEqualTo("lockStatus", 0);
        List<WmsInnerInventory> wmsInnerInventoryList_old = wmsInnerInventoryMapper.selectByExample(inventotyExample);
        WmsInnerInventory wmsInnerInventory_old_status0 = new WmsInnerInventory();
        WmsInnerInventory wmsInnerInventory_old_status1 = new WmsInnerInventory();
        for (WmsInnerInventory innerInventory : wmsInnerInventoryList_old){
            if (innerInventory.getJobStatus().equals((byte) 0)){
                wmsInnerInventory_old_status0 = innerInventory;
            }else {
                wmsInnerInventory_old_status1 = innerInventory;
            }
        }
        if (wmsInnerInventory_old_status0 != null) {
            if(StringUtils.isEmpty(wmsInnerInventory_old_status0.getPackingQty())){
                wmsInnerInventory_old_status0.setPackingQty(BigDecimal.ZERO);
            }
            wmsInnerInventory_old_status0.setPackingQty(wmsInnerInventory_old_status0.getPackingQty().add(BigDecimal.ONE));
            wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventory_old_status0);
        }else {
            wmsInnerInventory_old_status1.setInventoryId(null);
            wmsInnerInventory_old_status1.setJobStatus((byte) 0);
            wmsInnerInventoryMapper.insert(wmsInnerInventory_old_status1);
        }

        //??????????????????
        newBarcode.setBarcodeStatus((byte)4);//?????????
        newBarcode.setDeliveryOrderCode(oldBarcode.getDeliveryOrderCode());//??????????????????
        newBarcode.setModifiedUserId(sysUser.getUserId());
        newBarcode.setModifiedTime(new Date());
        i+=wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(newBarcode);

        // ???????????????-1
        example.clear();
        inventotyCriteria = inventotyExample.createCriteria();
        inventotyCriteria.andEqualTo("materialId", newBarcode.getMaterialId())
                .andEqualTo("storageId", newBarcode.getStorageId())
                .andEqualTo("relevanceOrderCode", newBarcode.getDeliveryOrderCode())
                .andEqualTo("jobStatus", (byte) 1)
                .andEqualTo("stockLock", 0)
                .andEqualTo("lockStatus", 0)
                .andGreaterThan("packingQty", 0);
        WmsInnerInventory wmsInnerInventory_new = wmsInnerInventoryMapper.selectOneByExample(example);
        wmsInnerInventory_new.setPackingQty(wmsInnerInventory_new.getPackingQty().subtract(BigDecimal.ONE));
        wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventory_new);

        //???????????????
        SearchWmsInnerJobOrder searchWmsInnerJobOrder=new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setJobOrderCode(oldBarcode.getDeliveryOrderCode());
        searchWmsInnerJobOrder.setOrgId(sysUser.getOrganizationId());
        List<WmsInnerJobOrderDto> jobOrderDtoList=wmsInnerJobOrderService.findList(searchWmsInnerJobOrder);

        //????????????????????????
        WmsInnerBarcodeOperation wmsInnerBarcodeOperation=new WmsInnerBarcodeOperation();
        wmsInnerBarcodeOperation.setBarcodeOperationId(null);
        wmsInnerBarcodeOperation.setBarcode(barcode);
        wmsInnerBarcodeOperation.setReplaceBarcode(replaceBarcode);
        wmsInnerBarcodeOperation.setRelatedOrderCode(oldBarcode.getDeliveryOrderCode());
        wmsInnerBarcodeOperation.setOperationType("??????");
        if(jobOrderDtoList.size()>0){
            wmsInnerBarcodeOperation.setOutPort(jobOrderDtoList.get(0).getPlatformName());
        }
        wmsInnerBarcodeOperation.setCreateUserId(sysUser.getUserId());
        wmsInnerBarcodeOperation.setCreateTime(new Date());
        wmsInnerBarcodeOperation.setModifiedUserId(sysUser.getUserId());
        wmsInnerBarcodeOperation.setModifiedTime(new Date());
        i+=wmsInnerBarcodeOperationMapper.insertUseGeneratedKeys(wmsInnerBarcodeOperation);

        return i;
    }

    @Override
    public WmsInnerInventoryDetDto scanCrapBarcode(String barcode) {
        SysUser sysUser=currentUser();
        WmsInnerInventoryDetDto wmsInnerInventoryDetDto=this.judgeBarcode(barcode,sysUser);
        return wmsInnerInventoryDetDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int crapCommit(String barcode) {
        int i=0;
        String orderCode=null;
        String platformName =null;
        SysUser sysUser=currentUser();
        Example example = new Example(WmsInnerInventoryDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("barcode",barcode);
        criteria.andEqualTo("orgId",sysUser.getOrganizationId());
        WmsInnerInventoryDet barcodeDet=wmsInnerInventoryDetMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(barcodeDet)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"???????????????????????????-->"+barcode);
        }

        //???????????????
        if(StringUtils.isNotEmpty(barcodeDet.getDeliveryOrderCode())){
            //??????????????? ???????????????????????????
            SearchWmsInnerJobOrder searchWmsInnerJobOrder=new SearchWmsInnerJobOrder();
            searchWmsInnerJobOrder.setJobOrderCode(barcodeDet.getDeliveryOrderCode());
            searchWmsInnerJobOrder.setOrgId(sysUser.getOrganizationId());
            List<WmsInnerJobOrderDto> jobOrderDtoList=wmsInnerJobOrderService.findList(searchWmsInnerJobOrder);
            if(StringUtils.isNotEmpty(jobOrderDtoList) && jobOrderDtoList.size()>0){
                orderCode=jobOrderDtoList.get(0).getJobOrderCode();
                platformName=jobOrderDtoList.get(0).getPlatformName();

                SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet=new SearchWmsInnerJobOrderDet();
                searchWmsInnerJobOrderDet.setJobOrderId(jobOrderDtoList.get(0).getJobOrderId());
                searchWmsInnerJobOrderDet.setMaterialId(barcodeDet.getMaterialId());
                searchWmsInnerJobOrderDet.setOrgId(sysUser.getOrganizationId());
                List<WmsInnerJobOrderDetDto> detDtoList=wmsInnerJobOrderDetService.findList(searchWmsInnerJobOrderDet);
                if(StringUtils.isNotEmpty(detDtoList) && detDtoList.size()>0){
                    WmsInnerJobOrderDet wmsInnerJobOrderDet=new WmsInnerJobOrderDet();
                    wmsInnerJobOrderDet.setJobOrderDetId(detDtoList.get(0).getJobOrderDetId());
                    wmsInnerJobOrderDet.setActualQty(detDtoList.get(0).getActualQty().subtract(new BigDecimal(1)));
                    wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
                    wmsInnerJobOrderDet.setModifiedTime(new Date());
                    i=wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInnerJobOrderDet);
                }
                if(detDtoList.get(0).getOrderStatus()==(byte)5){
                    // ???????????????????????? ????????????????????????
                    SearchWmsInnerInventory searchWmsInnerInventory=new SearchWmsInnerInventory();
                    searchWmsInnerInventory.setMaterialId(barcodeDet.getMaterialId());
                    searchWmsInnerInventory.setStorageId(detDtoList.get(0).getInStorageId());
                    searchWmsInnerInventory.setLockStatus((byte)0);
                    searchWmsInnerInventory.setStockLock((byte)0);
                    searchWmsInnerInventory.setQcLock((byte)0);
                    searchWmsInnerInventory.setJobStatus((byte)2);
                    List<WmsInnerInventoryDto> inventoryDtos=wmsInnerInventoryService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInventory));
                    if(StringUtils.isNotEmpty(inventoryDtos) && inventoryDtos.size()>0){
                        WmsInnerInventory wmsInnerInventory=new WmsInnerInventory();
                        wmsInnerInventory.setInventoryId(inventoryDtos.get(0).getInventoryId());
                        wmsInnerInventory.setPackingQty(inventoryDtos.get(0).getPackingQty().subtract(new BigDecimal(1)));
                        wmsInnerInventory.setModifiedUserId(sysUser.getUserId());
                        wmsInnerInventory.setModifiedTime(new Date());
                        i=wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventory);
                    }
                }
            }
        }
        else {
            //????????? ???????????????
            SearchWmsInnerInventory searchWmsInnerInventory=new SearchWmsInnerInventory();
            searchWmsInnerInventory.setMaterialId(barcodeDet.getMaterialId());
            searchWmsInnerInventory.setStorageId(barcodeDet.getStorageId());
            searchWmsInnerInventory.setLockStatus((byte)0);
            searchWmsInnerInventory.setStockLock((byte)0);
            searchWmsInnerInventory.setQcLock((byte)0);
            searchWmsInnerInventory.setJobStatus((byte)1);
            searchWmsInnerInventory.setInventoryStatusName("??????");
            List<WmsInnerInventoryDto> inventoryDtos=wmsInnerInventoryService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInventory));
            if(StringUtils.isNotEmpty(inventoryDtos) && inventoryDtos.size()>0){
                WmsInnerInventory wmsInnerInventory=new WmsInnerInventory();
                wmsInnerInventory.setInventoryId(inventoryDtos.get(0).getInventoryId());
                wmsInnerInventory.setPackingQty(inventoryDtos.get(0).getPackingQty().subtract(new BigDecimal(1)));
                wmsInnerInventory.setModifiedUserId(sysUser.getUserId());
                wmsInnerInventory.setModifiedTime(new Date());
                i=wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventory);
            }

            //???????????????
            SearchQmsInspectionOrderDetSample sDetSample=new SearchQmsInspectionOrderDetSample();
            sDetSample.setFactoryBarcode(barcode);
            sDetSample.setOrgId(sysUser.getOrganizationId());
            List<QmsInspectionOrderDetSample> detSampleList=wanbaoFeignApi.findList(sDetSample).getData();
            if(StringUtils.isNotEmpty(detSampleList) && detSampleList.size()>0){
                SearchQmsInspectionOrder searchQmsInspectionOrder=new SearchQmsInspectionOrder();
                searchQmsInspectionOrder.setInspectionOrderId(detSampleList.get(0).getInspectionOrderId());
                searchQmsInspectionOrder.setOrgId(sysUser.getOrganizationId());
                List<QmsInspectionOrder> inspectionOrders=wanbaoFeignApi.findList(searchQmsInspectionOrder).getData();
                if(StringUtils.isNotEmpty(inspectionOrders) && inspectionOrders.size()>0){
                    orderCode=inspectionOrders.get(0).getInspectionOrderCode();
                }
            }
        }

        //????????????????????? ?????????
        barcodeDet.setBarcodeStatus((byte)7);
        barcodeDet.setModifiedUserId(sysUser.getUserId());
        barcodeDet.setModifiedTime(new Date());
        i+=wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(barcodeDet);

        //????????????????????????
        WmsInnerBarcodeOperation wmsInnerBarcodeOperation=new WmsInnerBarcodeOperation();
        wmsInnerBarcodeOperation.setBarcodeOperationId(null);
        wmsInnerBarcodeOperation.setBarcode(barcode);
        wmsInnerBarcodeOperation.setRelatedOrderCode(orderCode);
        wmsInnerBarcodeOperation.setOperationType("??????");

        wmsInnerBarcodeOperation.setOutPort(platformName);
        wmsInnerBarcodeOperation.setCreateUserId(sysUser.getUserId());
        wmsInnerBarcodeOperation.setCreateTime(new Date());
        wmsInnerBarcodeOperation.setModifiedUserId(sysUser.getUserId());
        wmsInnerBarcodeOperation.setModifiedTime(new Date());
        i+=wmsInnerBarcodeOperationMapper.insertUseGeneratedKeys(wmsInnerBarcodeOperation);

        //????????????
        //InventoryLogUtil.addLog();

        return i;
    }

    private WmsInnerInventoryDetDto judgeBarcode(String barcode,SysUser sysUser){
        WmsInnerInventoryDetDto result=new WmsInnerInventoryDetDto();
        SearchWmsInnerInventoryDet sInnerInventoryDet=new SearchWmsInnerInventoryDet();
        sInnerInventoryDet.setBarcode(barcode);
        sInnerInventoryDet.setOrgId(sysUser.getOrganizationId());
        List<WmsInnerInventoryDetDto> detDtoList=wmsInnerInventoryDetService.findList(ControllerUtil.dynamicConditionByEntity(sInnerInventoryDet));
        if(StringUtils.isNotEmpty(detDtoList) && detDtoList.size()>0){
            result=detDtoList.get(0);
        }
        else {
            sInnerInventoryDet.setBarcode(null);
            sInnerInventoryDet.setSalesBarcode(barcode);
            detDtoList=wmsInnerInventoryDetService.findList(ControllerUtil.dynamicConditionByEntity(sInnerInventoryDet));
            if(StringUtils.isNotEmpty(detDtoList) && detDtoList.size()>0){
                result=detDtoList.get(0);
            }
            else {
                sInnerInventoryDet.setBarcode(null);
                sInnerInventoryDet.setSalesBarcode(null);
                sInnerInventoryDet.setCustomerBarcode(barcode);
                detDtoList=wmsInnerInventoryDetService.findList(ControllerUtil.dynamicConditionByEntity(sInnerInventoryDet));
                if(StringUtils.isNotEmpty(detDtoList) && detDtoList.size()>0){
                    result=detDtoList.get(0);
                }
            }
        }

        if(StringUtils.isEmpty(result.getInventoryDetId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"????????????????????????????????????-->"+barcode);
        }

        //?????????
        SearchMesSfcBarcodeProcess sBarcodeProcess=new SearchMesSfcBarcodeProcess();
        sBarcodeProcess.setBarcode(barcode);
        sBarcodeProcess.setOrgId(sysUser.getOrganizationId());
        List<MesSfcBarcodeProcessDto> processDtos=sfcFeignApi.findList(sBarcodeProcess).getData();
        if(StringUtils.isNotEmpty(processDtos) && processDtos.size()>0){
            result.setProName(processDtos.get(0).getProName());
        }

        result.setScanBarcode(barcode);
        return result;
    }

    /**
     * ????????????????????????
     *
     * @return
     */
    private SysUser currentUser() {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}
