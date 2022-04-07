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
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"条码不能为空");
        }
        WmsInnerInventoryDetDto wmsInnerInventoryDetDto=this.judgeBarcode(barcode,sysUser);
        if(wmsInnerInventoryDetDto.getBarcodeStatus()!=(byte)4){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"条码状态不是已拣货");
        }

        //条码状态(1-待收货、2-已收货、3-在库、4-已拣选、5-已复核、6-已出库、7-已取消)

        return wmsInnerInventoryDetDto;
    }

    @Override
    public WmsInnerInventoryDetDto scanReplaceBarcode(String barcode,String replaceBarcode) {
        SysUser sysUser=currentUser();
        Long materialId=null;
        Long replaceMaterialId=null;
        WmsInnerInventoryDetDto result=new WmsInnerInventoryDetDto();
        if(StringUtils.isEmpty(barcode)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"条码不能为空");
        }
        if(StringUtils.isEmpty(replaceBarcode)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"替换条码不能为空");
        }
        if(barcode.equals(replaceBarcode)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"条码和替换条码相同");
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
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"条码和替换条码不是同一物料");
        }
        if(result.getBarcodeStatus()!=(byte)3){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"替换条码的状态不是在库");
        }

        SearchQmsInspectionOrderDetSample sDetSample=new SearchQmsInspectionOrderDetSample();
        sDetSample.setBarcode(result.getBarcode());
        sDetSample.setOrgId(sysUser.getOrganizationId());
        List<QmsInspectionOrderDetSample> detSampleList=wanbaoFeignApi.findList(sDetSample).getData();
        if(StringUtils.isEmpty(detSampleList) || detSampleList.size()<=0){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"替换条码未质检");
        }
        else{
            if(StringUtils.isNotEmpty(detSampleList.get(0).getBarcodeStatus())
             && detSampleList.get(0).getBarcodeStatus()==(byte)0){
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"替换条码不是合格状态");
            }
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int replaceCommit(String barcode, String replaceBarcode) {
        int i=0;
        SysUser sysUser=currentUser();
        // 替换条码操作：点击提交以后，后台需要对两个条码的状态以及库存进行修改，
        // 如原条码是待出状态，替换条码是合格状态，需要更换为原条码为合格状态，替换条码为待出状态，
        // 并将替换条码反写到该拣货作业单上并反写到替换/报废条码管理页面
        // 增加条码日志
        Example example = new Example(WmsInnerInventoryDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("barcode",barcode);
        criteria.andEqualTo("orgId",sysUser.getOrganizationId());
        WmsInnerInventoryDet oldBarcode=wmsInnerInventoryDetMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(oldBarcode)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"提交的条码无效-->"+barcode);
        }

        Example exampleNew = new Example(WmsInnerInventoryDet.class);
        Example.Criteria criteriaNew = exampleNew.createCriteria();
        criteriaNew.andEqualTo("barcode",replaceBarcode);
        criteriaNew.andEqualTo("orgId",sysUser.getOrganizationId());
        WmsInnerInventoryDet newBarcode=wmsInnerInventoryDetMapper.selectOneByExample(exampleNew);
        if(StringUtils.isEmpty(newBarcode)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"提交的替换条码无效-->"+replaceBarcode);
        }

        //条码更新为在库
        oldBarcode.setBarcodeStatus((byte)3);//在库
        oldBarcode.setModifiedUserId(sysUser.getUserId());
        oldBarcode.setModifiedTime(new Date());
        i=wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(oldBarcode);

        //替换条码处理
        newBarcode.setBarcodeStatus((byte)4);//已拣选
        newBarcode.setDeliveryOrderCode(oldBarcode.getDeliveryOrderCode());//更新出库单号
        newBarcode.setModifiedUserId(sysUser.getUserId());
        newBarcode.setModifiedTime(new Date());
        i+=wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(newBarcode);

        //查询拣货单
        SearchWmsInnerJobOrder searchWmsInnerJobOrder=new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setJobOrderCode(oldBarcode.getDeliveryOrderCode());
        searchWmsInnerJobOrder.setOrgId(sysUser.getOrganizationId());
        List<WmsInnerJobOrderDto> jobOrderDtoList=wmsInnerJobOrderService.findList(searchWmsInnerJobOrder);

        //新增条码操作记录
        WmsInnerBarcodeOperation wmsInnerBarcodeOperation=new WmsInnerBarcodeOperation();
        wmsInnerBarcodeOperation.setBarcodeOperationId(null);
        wmsInnerBarcodeOperation.setBarcode(barcode);
        wmsInnerBarcodeOperation.setReplaceBarcode(replaceBarcode);
        wmsInnerBarcodeOperation.setRelatedOrderCode(oldBarcode.getDeliveryOrderCode());
        wmsInnerBarcodeOperation.setOperationType("替换");
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
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"提交的报废条码无效-->"+barcode);
        }

        //是否有出库
        if(StringUtils.isNotEmpty(barcodeDet.getDeliveryOrderCode())){
            //查询拣货单 减少拣货单拣货数量
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
            }
        }
        else {
            //无拣货 则库存减少
            SearchWmsInnerInventory searchWmsInnerInventory=new SearchWmsInnerInventory();
            searchWmsInnerInventory.setMaterialId(barcodeDet.getMaterialId());
            searchWmsInnerInventory.setStorageId(barcodeDet.getStorageId());
            searchWmsInnerInventory.setLockStatus((byte)0);
            searchWmsInnerInventory.setStockLock((byte)0);
            searchWmsInnerInventory.setQcLock((byte)0);
            searchWmsInnerInventory.setJobStatus((byte)1);
            searchWmsInnerInventory.setInventoryStatusName("合格");
            List<WmsInnerInventoryDto> inventoryDtos=wmsInnerInventoryService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInventory));
            if(StringUtils.isNotEmpty(inventoryDtos) && inventoryDtos.size()>0){
                WmsInnerInventory wmsInnerInventory=new WmsInnerInventory();
                wmsInnerInventory.setInventoryId(inventoryDtos.get(0).getInventoryId());
                wmsInnerInventory.setPackingQty(inventoryDtos.get(0).getPackingQty().subtract(new BigDecimal(1)));
                wmsInnerInventory.setModifiedUserId(sysUser.getUserId());
                wmsInnerInventory.setModifiedTime(new Date());
                i=wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventory);
            }

            //找质检单号
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

        //条码状态更新为 已取消
        barcodeDet.setBarcodeStatus((byte)7);
        barcodeDet.setModifiedUserId(sysUser.getUserId());
        barcodeDet.setModifiedTime(new Date());
        i+=wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(barcodeDet);

        //增加条码报废记录
        WmsInnerBarcodeOperation wmsInnerBarcodeOperation=new WmsInnerBarcodeOperation();
        wmsInnerBarcodeOperation.setBarcodeOperationId(null);
        wmsInnerBarcodeOperation.setBarcode(barcode);
        wmsInnerBarcodeOperation.setRelatedOrderCode(orderCode);
        wmsInnerBarcodeOperation.setOperationType("报废");

        wmsInnerBarcodeOperation.setOutPort(platformName);
        wmsInnerBarcodeOperation.setCreateUserId(sysUser.getUserId());
        wmsInnerBarcodeOperation.setCreateTime(new Date());
        wmsInnerBarcodeOperation.setModifiedUserId(sysUser.getUserId());
        wmsInnerBarcodeOperation.setModifiedTime(new Date());
        i+=wmsInnerBarcodeOperationMapper.insertUseGeneratedKeys(wmsInnerBarcodeOperation);

        //库存日志
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
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"库存条码明细信息无此条码-->"+barcode);
        }

        //找产线
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
     * 获取当前登录用户
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
