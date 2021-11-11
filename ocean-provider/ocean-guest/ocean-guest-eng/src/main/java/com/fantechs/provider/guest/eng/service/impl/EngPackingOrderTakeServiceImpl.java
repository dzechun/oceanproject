package com.fantechs.provider.guest.eng.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseMaterialOwnerDto;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderDto;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderSummaryDetDto;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderSummaryDto;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderTakeCancel;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryLogDto;
import com.fantechs.common.base.general.entity.basic.BaseInventoryStatus;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInventoryStatus;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterialOwner;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehouse;
import com.fantechs.common.base.general.entity.eng.EngContractQtyOrder;
import com.fantechs.common.base.general.entity.eng.EngPackingOrder;
import com.fantechs.common.base.general.entity.eng.EngPackingOrderSummary;
import com.fantechs.common.base.general.entity.eng.EngPackingOrderSummaryDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryLog;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.guest.eng.mapper.EngContractQtyOrderMapper;
import com.fantechs.provider.guest.eng.mapper.EngPackingOrderMapper;
import com.fantechs.provider.guest.eng.mapper.EngPackingOrderSummaryDetMapper;
import com.fantechs.provider.guest.eng.mapper.EngPackingOrderSummaryMapper;
import com.fantechs.provider.guest.eng.service.EngPackingOrderService;
import com.fantechs.provider.guest.eng.service.EngPackingOrderTakeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author mr.lei
 * @Date 2021/9/8
 */
@Service
public class EngPackingOrderTakeServiceImpl implements EngPackingOrderTakeService {
    @Resource
    private EngPackingOrderMapper engPackingOrderMapper;
    @Resource
    private EngPackingOrderSummaryMapper engPackingOrderSummaryMapper;
    @Resource
    private EngPackingOrderSummaryDetMapper engPackingOrderSummaryDetMapper;
    @Resource
    private InnerFeignApi innerFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private EngContractQtyOrderMapper engContractQtyOrderMapper;
    @Resource
    private EngPackingOrderService engPackingOrderService;

    @Override
    public List<EngPackingOrderDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        map.put("auditStatus",3);
        return engPackingOrderMapper.findList(map);
    }

    /**
     * 到货登记
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int register(List<Long> ids) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        int num = 0;
        for (Long id : ids) {
            EngPackingOrder engPackingOrder = engPackingOrderMapper.selectByPrimaryKey(id);
            if(engPackingOrder.getOrderStatus()>1){
                throw new BizErrorException("重复操作");
            }
            engPackingOrder.setOrderStatus((byte)2);
            engPackingOrder.setArrivalTime(new Date());
            engPackingOrder.setAgoConfirmUserId(sysUser.getUserId());
            engPackingOrder.setModifiedTime(new Date());
            engPackingOrder.setModifiedUserId(sysUser.getUserId());
            num+=engPackingOrderMapper.updateByPrimaryKeySelective(engPackingOrder);

            //到场记录材料跟踪日志
            engPackingOrderService.saveRecord(engPackingOrder,(byte)4,"到场");
        }
        return num;
    }

    /**
     * 整单收货
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int allTask(List<Long> ids) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        List<WmsInnerInventory> wmsInnerInventories = new ArrayList<>();
        //获取收货库存信息
        List<BaseWarehouse> warehouseList = baseFeignApi.findList(new SearchBaseWarehouse()).getData();
        if(StringUtils.isEmpty(warehouseList)){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"获取仓库信息失败");
        }
        Long warehouseId = warehouseList.get(0).getWarehouseId();

        //获取库存状态信息
        SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
        searchBaseInventoryStatus.setWarehouseId(warehouseId);
        List<BaseInventoryStatus> inventoryStatusList = baseFeignApi.findList(searchBaseInventoryStatus).getData();
        if(StringUtils.isEmpty(inventoryStatusList)){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"获取库位信息失败");
        }
        Long inventoryStatus = inventoryStatusList.get(0).getInventoryStatusId();

        //获取货主信息
        List<BaseMaterialOwnerDto> materialOwnerDtoList = baseFeignApi.findList(new SearchBaseMaterialOwner()).getData();
        if(StringUtils.isEmpty(materialOwnerDtoList)){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"获取货主信息失败");
        }
        Long materialOwnerId = materialOwnerDtoList.get(0).getMaterialOwnerId();
        int num = 0;
        for (Long id : ids) {
            EngPackingOrder engPackingOrder  = engPackingOrderMapper.selectByPrimaryKey(id);
            if(engPackingOrder.getOrderStatus()==5){
                throw new BizErrorException("单据已完成");
            }
            if(engPackingOrder.getOrderStatus()==4){
                throw new BizErrorException("收货已完成，重复操作");
            }
            if(engPackingOrder.getOrderStatus()==3){
                throw new BizErrorException("收货中，无法整单收货");
            }
            //查询包箱明细
            List<EngPackingOrderSummaryDto> engPackingOrderSummaryDtos = engPackingOrderSummaryMapper.findList(ControllerUtil.dynamicCondition("packingOrderId",engPackingOrder.getPackingOrderId()));
            for (EngPackingOrderSummaryDto engPackingOrderSummaryDto : engPackingOrderSummaryDtos) {
                //查询包箱货品明细
                List<EngPackingOrderSummaryDetDto> engPackingOrderSummaryDetDtos = engPackingOrderSummaryDetMapper.findList(ControllerUtil.dynamicCondition("packingOrderSummaryId",engPackingOrderSummaryDto.getPackingOrderSummaryId()));
                for (EngPackingOrderSummaryDetDto engPackingOrderSummaryDetDto : engPackingOrderSummaryDetDtos) {
                    //创建收货库存
                    WmsInnerInventory wmsInnerInventory = new WmsInnerInventory();
                    wmsInnerInventory.setMaterialOwnerId(materialOwnerId);
                    wmsInnerInventory.setInventoryStatusId(inventoryStatus);
                    wmsInnerInventory.setPackingQty(engPackingOrderSummaryDetDto.getQty());
                    wmsInnerInventory.setRelevanceOrderCode(engPackingOrderSummaryDetDto.getCartonCode());
                    wmsInnerInventory.setPurchaseReqOrderCode(engPackingOrderSummaryDto.getPurchaseReqOrderCode());
                    wmsInnerInventory.setContractCode(engPackingOrderSummaryDto.getContractCode());
                    wmsInnerInventory.setSpec(engPackingOrderSummaryDetDto.getSpec());
                    wmsInnerInventory.setMaterialId(engPackingOrderSummaryDetDto.getMaterialId());
                    wmsInnerInventory.setWarehouseId(warehouseId);
                    //收货库位
                    wmsInnerInventory.setStorageId(engPackingOrderSummaryDetDto.getReceivingStorageId());
                    wmsInnerInventory.setReceivingDate(new Date());
                    wmsInnerInventory.setJobStatus((byte)1);
                    wmsInnerInventory.setCreateTime(new Date());
                    wmsInnerInventory.setCreateUserId(sysUser.getUserId());
                    wmsInnerInventory.setModifiedTime(new Date());
                    wmsInnerInventory.setModifiedUserId(sysUser.getUserId());
                    wmsInnerInventory.setOrgId(sysUser.getOrganizationId());
                    wmsInnerInventory.setLockStatus((byte)0);
                    wmsInnerInventory.setQcLock((byte)0);
                    wmsInnerInventory.setStockLock((byte)0);
                    wmsInnerInventory.setPackingUnitName(engPackingOrderSummaryDetDto.getUnitName());
                    wmsInnerInventory.setSupplierId(engPackingOrder.getSupplierId());
                    wmsInnerInventory.setOption1(engPackingOrderSummaryDetDto.getDeviceCode());
                    wmsInnerInventory.setOption2(engPackingOrderSummaryDetDto.getDominantTermCode());
                    wmsInnerInventory.setOption3(engPackingOrderMapper.findMaterialPurpose(ControllerUtil.dynamicCondition("contractCode",engPackingOrderSummaryDto.getContractCode(),
                            "materialCode",engPackingOrderSummaryDetDto.getMaterialCode())));
                    wmsInnerInventory.setOption4(engPackingOrderSummaryDetDto.getLocationNum());
                    wmsInnerInventories.add(wmsInnerInventory);

                    //收货数量
                    engPackingOrderSummaryDetDto.setReceivingQty(engPackingOrderSummaryDetDto.getQty());
                    engPackingOrderSummaryDetDto.setSummaryDetStatus((byte)3);
                    engPackingOrderSummaryDetDto.setModifiedTime(new Date());
                    engPackingOrderSummaryDetDto.setModifiedUserId(sysUser.getUserId());
                    engPackingOrderSummaryDetMapper.updateByPrimaryKeySelective(engPackingOrderSummaryDetDto);
                }
                engPackingOrderSummaryDto.setSummaryStatus((byte)3);
                engPackingOrderSummaryDto.setModifiedTime(new Date());
                engPackingOrderSummaryDto.setModifiedUserId(sysUser.getUserId());
                engPackingOrderSummaryMapper.updateByPrimaryKeySelective(engPackingOrderSummaryDto);
            }
            engPackingOrder.setOrderStatus((byte)4);
            engPackingOrder.setModifiedUserId(sysUser.getUserId());
            engPackingOrder.setModifiedTime(new Date());
            engPackingOrderMapper.updateByPrimaryKeySelective(engPackingOrder);

            //新增收货库存
            num = this.addInveentory(wmsInnerInventories,engPackingOrder);
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int boxTask(List<EngPackingOrderSummaryDto> engPackingOrderSummaryDtos) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        List<WmsInnerInventory> wmsInnerInventories = new ArrayList<>();
        //获取收货库存信息
        List<BaseWarehouse> warehouseList = baseFeignApi.findList(new SearchBaseWarehouse()).getData();
        if(StringUtils.isEmpty(warehouseList)){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"获取仓库信息失败");
        }
        Long warehouseId = warehouseList.get(0).getWarehouseId();

        //获取库存状态信息
        SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
        searchBaseInventoryStatus.setWarehouseId(warehouseId);
        List<BaseInventoryStatus> inventoryStatusList = baseFeignApi.findList(searchBaseInventoryStatus).getData();
        if(StringUtils.isEmpty(inventoryStatusList)){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"获取库位信息失败");
        }
        Long inventoryStatus = inventoryStatusList.get(0).getInventoryStatusId();

        //获取货主信息
        List<BaseMaterialOwnerDto> materialOwnerDtoList = baseFeignApi.findList(new SearchBaseMaterialOwner()).getData();
        if(StringUtils.isEmpty(materialOwnerDtoList)){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"获取货主信息失败");
        }
        Long materialOwnerId = materialOwnerDtoList.get(0).getMaterialOwnerId();


        EngPackingOrder engPackingOrder = engPackingOrderMapper.selectByPrimaryKey(engPackingOrderSummaryDtos.get(0).getPackingOrderId());
        if(engPackingOrder.getOrderStatus()==(4)){
            throw new BizErrorException("单据已收货完成");
        }
        for (EngPackingOrderSummaryDto engPackingOrderSummaryDto : engPackingOrderSummaryDtos) {
            if(engPackingOrderSummaryDto.getSummaryStatus()==3){
                throw new BizErrorException(engPackingOrderSummaryDto.getCartonCode()+"已经收货完成");
            }
            if(engPackingOrderSummaryDto.getSummaryStatus()==2){
                throw new BizErrorException(engPackingOrderSummaryDto.getCartonCode()+"已经部分收货,无法按箱收货");
            }
            //查询包箱货品明细
            List<EngPackingOrderSummaryDetDto> engPackingOrderSummaryDetDtos = engPackingOrderSummaryDetMapper.findList(ControllerUtil.dynamicCondition("packingOrderSummaryId",engPackingOrderSummaryDto.getPackingOrderSummaryId()));
            for (EngPackingOrderSummaryDetDto engPackingOrderSummaryDetDto : engPackingOrderSummaryDetDtos) {
                //创建收货库存
                WmsInnerInventory wmsInnerInventory = new WmsInnerInventory();
                wmsInnerInventory.setMaterialOwnerId(materialOwnerId);
                wmsInnerInventory.setInventoryStatusId(inventoryStatus);
                wmsInnerInventory.setPackingQty(engPackingOrderSummaryDetDto.getQty());
                wmsInnerInventory.setRelevanceOrderCode(engPackingOrderSummaryDetDto.getCartonCode());
                wmsInnerInventory.setMaterialId(engPackingOrderSummaryDetDto.getMaterialId());
                wmsInnerInventory.setWarehouseId(warehouseId);
                //收货库位
                wmsInnerInventory.setStorageId(engPackingOrderSummaryDetDto.getReceivingStorageId());
                wmsInnerInventory.setReceivingDate(new Date());
                wmsInnerInventory.setJobStatus((byte)1);
                wmsInnerInventory.setCreateTime(new Date());
                wmsInnerInventory.setCreateUserId(sysUser.getUserId());
                wmsInnerInventory.setModifiedTime(new Date());
                wmsInnerInventory.setModifiedUserId(sysUser.getUserId());
                wmsInnerInventory.setPurchaseReqOrderCode(engPackingOrderSummaryDto.getPurchaseReqOrderCode());
                wmsInnerInventory.setSpec(engPackingOrderSummaryDetDto.getSpec());
                wmsInnerInventory.setContractCode(engPackingOrderSummaryDto.getContractCode());
                wmsInnerInventory.setOrgId(sysUser.getOrganizationId());
                wmsInnerInventory.setLockStatus((byte)0);
                wmsInnerInventory.setQcLock((byte)0);
                wmsInnerInventory.setStockLock((byte)0);
                wmsInnerInventory.setPackingUnitName(engPackingOrderSummaryDetDto.getUnitName());
                wmsInnerInventory.setSupplierId(engPackingOrder.getSupplierId());
                wmsInnerInventory.setOption1(engPackingOrderSummaryDetDto.getDeviceCode());
                wmsInnerInventory.setOption2(engPackingOrderSummaryDetDto.getDominantTermCode());
                wmsInnerInventory.setOption3(engPackingOrderMapper.findMaterialPurpose(ControllerUtil.dynamicCondition("contractCode",engPackingOrderSummaryDto.getContractCode(),
                        "materialCode",engPackingOrderSummaryDetDto.getMaterialCode())));
                wmsInnerInventory.setOption4(engPackingOrderSummaryDetDto.getLocationNum());
                wmsInnerInventories.add(wmsInnerInventory);

                //收货数量
                engPackingOrderSummaryDetDto.setReceivingQty(engPackingOrderSummaryDetDto.getQty());
                engPackingOrderSummaryDetDto.setSummaryDetStatus((byte)3);
                engPackingOrderSummaryDetDto.setModifiedTime(new Date());
                engPackingOrderSummaryDetDto.setModifiedUserId(sysUser.getUserId());
                engPackingOrderSummaryDetMapper.updateByPrimaryKeySelective(engPackingOrderSummaryDetDto);
            }
            engPackingOrderSummaryDto.setSummaryStatus((byte)3);
            engPackingOrderSummaryDto.setModifiedTime(new Date());
            engPackingOrderSummaryDto.setModifiedUserId(sysUser.getUserId());
            engPackingOrderSummaryMapper.updateByPrimaryKeySelective(engPackingOrderSummaryDto);
        }
        //查询是否已经全部收货完成
        //查询包箱明细
        List<EngPackingOrderSummaryDto> eng = engPackingOrderSummaryMapper.findList(ControllerUtil.dynamicCondition("packingOrderId",engPackingOrder.getPackingOrderId()));
        int count = eng.stream().filter(li->li.getSummaryStatus()==3).collect(Collectors.toList()).size();
        if(count<eng.size()){
            engPackingOrder.setOrderStatus((byte)3);
        }else if(count==eng.size()){
            engPackingOrder.setOrderStatus((byte)4);
        }
        engPackingOrder.setModifiedTime(new Date());
        engPackingOrder.setModifiedUserId(sysUser.getUserId());
        engPackingOrderMapper.updateByPrimaryKeySelective(engPackingOrder);

        //新增收货库存
        int num = this.addInveentory(wmsInnerInventories,engPackingOrder);
        return  num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int onlyTask(EngPackingOrderSummaryDetDto engPackingOrderSummaryDetDto) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        List<WmsInnerInventory> wmsInnerInventories = new ArrayList<>();
        //获取收货库存信息
        List<BaseWarehouse> warehouseList = baseFeignApi.findList(new SearchBaseWarehouse()).getData();
        if(StringUtils.isEmpty(warehouseList)){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"获取仓库信息失败");
        }
        Long warehouseId = warehouseList.get(0).getWarehouseId();

        //获取库存状态信息
        SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
        searchBaseInventoryStatus.setWarehouseId(warehouseId);
        List<BaseInventoryStatus> inventoryStatusList = baseFeignApi.findList(searchBaseInventoryStatus).getData();
        if(StringUtils.isEmpty(inventoryStatusList)){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"获取库位信息失败");
        }
        Long inventoryStatus = inventoryStatusList.get(0).getInventoryStatusId();

        //获取货主信息
        List<BaseMaterialOwnerDto> materialOwnerDtoList = baseFeignApi.findList(new SearchBaseMaterialOwner()).getData();
        if(StringUtils.isEmpty(materialOwnerDtoList)){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"获取货主信息失败");
        }
        Long materialOwnerId = materialOwnerDtoList.get(0).getMaterialOwnerId();
        //取消收货实体
        List<EngPackingOrderTakeCancel> engPackingOrderTakeCancelList = new ArrayList<>();
        EngPackingOrderSummaryDet engPackingOrderSummaryDet = engPackingOrderSummaryDetMapper.selectByPrimaryKey(engPackingOrderSummaryDetDto.getPackingOrderSummaryDetId());
        EngPackingOrderSummary engPackingOrderSummary = engPackingOrderSummaryMapper.selectByPrimaryKey(engPackingOrderSummaryDetDto.getPackingOrderSummaryId());
        EngPackingOrder engPackingOrder = engPackingOrderMapper.selectByPrimaryKey(engPackingOrderSummary.getPackingOrderId());
        if(StringUtils.isNotEmpty(engPackingOrderSummaryDetDto.getIsCal()) && engPackingOrderSummaryDetDto.getIsCal()==1){
            engPackingOrderSummaryDetDto.setCancelQty(engPackingOrderSummaryDet.getReceivingQty());
        }
        BigDecimal calQty = engPackingOrderSummaryDetDto.getCancelQty();
        if(StringUtils.isEmpty(engPackingOrderSummaryDetDto.getCancelQty()) && engPackingOrderSummaryDet.getSummaryDetStatus()>=3){
            throw new BizErrorException("收货完成，重复收货");
        }
        if(StringUtils.isNotEmpty(engPackingOrderSummaryDetDto.getCancelQty())){

            if(StringUtils.isEmpty(engPackingOrderSummaryDet.getReceivingQty()) || (engPackingOrderSummaryDet.getReceivingQty().subtract(engPackingOrderSummaryDetDto.getCancelQty()).compareTo(BigDecimal.ZERO))==-1){
                throw new BizErrorException("取消数量不能大于实收数量");
            }
            if(StringUtils.isNotEmpty(engPackingOrderSummaryDet.getDistributionQty())){
                //可上架数量 = (实收数量-分配数量)
                BigDecimal totalQty = engPackingOrderSummaryDet.getReceivingQty().subtract(engPackingOrderSummaryDet.getDistributionQty());
                //判断取消数量是否大于可收货数量同时
                if(engPackingOrderSummaryDetDto.getCancelQty().compareTo(totalQty)==1){
                    if(StringUtils.isEmpty(engPackingOrderSummaryDet.getPutawayQty())) {
                        engPackingOrderSummaryDet.setPutawayQty(BigDecimal.ZERO);
                    }
                    //可取消数量=（收货数量-上架数量）
                    //判断取消数量数否大于可取消数量
                    BigDecimal canQty = (engPackingOrderSummaryDet.getReceivingQty().subtract(engPackingOrderSummaryDet.getPutawayQty()));
                    if(canQty.compareTo(engPackingOrderSummaryDetDto.getCancelQty())>-1){
                        //计算可取消的分配数量=(分配数量-上架数量)
                        //BigDecimal disQty = engPackingOrderSummaryDet.getDistributionQty().subtract(engPackingOrderSummaryDet.getPutawayQty());
                        EngPackingOrderTakeCancel engPackingOrderTakeCancel = new EngPackingOrderTakeCancel();
                        engPackingOrderTakeCancel = new EngPackingOrderTakeCancel();
                        engPackingOrderTakeCancel.setPackingOrderId(engPackingOrder.getPackingOrderId());
                        engPackingOrderTakeCancel.setPackingOrderSummaryDetId(engPackingOrderSummaryDet.getPackingOrderSummaryDetId());
                        engPackingOrderTakeCancel.setPackingOrderCode(engPackingOrder.getPackingOrderCode());
                        //计算需要扣减分配的数量=(分配数量-(实收数量-取消数量))
                        engPackingOrderTakeCancel.setQty(engPackingOrderSummaryDet.getDistributionQty().subtract((engPackingOrderSummaryDet.getReceivingQty().subtract(engPackingOrderSummaryDetDto.getCancelQty()))));
                        if(StringUtils.isNotEmpty(engPackingOrderTakeCancel.getQty()) && engPackingOrderTakeCancel.getQty().compareTo(BigDecimal.ZERO)>-1){
                            engPackingOrderTakeCancelList.add(engPackingOrderTakeCancel);
                            engPackingOrderSummaryDetDto.setDistributionQty(engPackingOrderSummaryDet.getDistributionQty().subtract(engPackingOrderTakeCancel.getQty()));

                            //计算需要扣减的收货数量=(总取消数量-已减分配数量)
                            engPackingOrderSummaryDetDto.setCancelQty(engPackingOrderSummaryDetDto.getCancelQty().subtract(engPackingOrderTakeCancel.getQty()));
                        }
                    }else{
                        //
                        throw new BizErrorException("可取消收货数量不足,无法取消");
                    }
                }
            }
            engPackingOrderSummaryDetDto.setReceivingQty(engPackingOrderSummaryDetDto.getCancelQty().negate());
        }
        if(StringUtils.isNotEmpty(engPackingOrderSummaryDetDto.getReceivingQty()) && (engPackingOrderSummaryDetDto.getReceivingQty().compareTo(BigDecimal.ZERO)==-1 || engPackingOrderSummaryDetDto.getReceivingQty().compareTo(BigDecimal.ZERO)==1)){
            //创建收货库存
            WmsInnerInventory wmsInnerInventory = new WmsInnerInventory();
            wmsInnerInventory.setMaterialOwnerId(materialOwnerId);
            wmsInnerInventory.setInventoryStatusId(inventoryStatus);
            wmsInnerInventory.setPackingQty(engPackingOrderSummaryDetDto.getReceivingQty());
            wmsInnerInventory.setRelevanceOrderCode(engPackingOrderSummaryDet.getCartonCode());
            wmsInnerInventory.setMaterialId(engPackingOrderSummaryDetDto.getMaterialId());
            wmsInnerInventory.setWarehouseId(warehouseId);
            //收货库位
            wmsInnerInventory.setStorageId(engPackingOrderSummaryDetDto.getReceivingStorageId());
            wmsInnerInventory.setReceivingDate(new Date());
            wmsInnerInventory.setJobStatus((byte)1);
            wmsInnerInventory.setCreateTime(new Date());
            wmsInnerInventory.setCreateUserId(sysUser.getUserId());
            wmsInnerInventory.setModifiedTime(new Date());
            wmsInnerInventory.setModifiedUserId(sysUser.getUserId());
            wmsInnerInventory.setOrgId(sysUser.getOrganizationId());
            wmsInnerInventory.setPurchaseReqOrderCode(engPackingOrderSummary.getPurchaseReqOrderCode());
            wmsInnerInventory.setSpec(engPackingOrderSummaryDetDto.getSpec());
            wmsInnerInventory.setContractCode(engPackingOrderSummary.getContractCode());
            wmsInnerInventory.setLockStatus((byte)0);
            wmsInnerInventory.setQcLock((byte)0);
            wmsInnerInventory.setStockLock((byte)0);
            wmsInnerInventory.setPackingUnitName(engPackingOrderSummaryDetDto.getUnitName());
            wmsInnerInventory.setSupplierId(engPackingOrder.getSupplierId());
            wmsInnerInventory.setOption1(engPackingOrderSummaryDet.getDeviceCode());
            wmsInnerInventory.setOption2(engPackingOrderSummaryDet.getDominantTermCode());
            wmsInnerInventory.setOption3(engPackingOrderMapper.findMaterialPurpose(ControllerUtil.dynamicCondition("contractCode",engPackingOrderSummary.getContractCode(),
                    "materialCode",engPackingOrderSummaryDetDto.getMaterialCode())));
            wmsInnerInventory.setOption4(engPackingOrderSummaryDet.getLocationNum());
            wmsInnerInventories.add(wmsInnerInventory);
        }
        if(StringUtils.isNotEmpty(calQty)){
            engPackingOrderSummaryDetDto.setReceivingQty(calQty.negate());
        }
        //收货数量
        if(StringUtils.isNotEmpty(engPackingOrderSummaryDet.getReceivingQty()) && StringUtils.isNotEmpty(engPackingOrderSummaryDetDto.getReceivingQty())){
            engPackingOrderSummaryDetDto.setReceivingQty(engPackingOrderSummaryDet.getReceivingQty().add(engPackingOrderSummaryDetDto.getReceivingQty()));
        }
        engPackingOrderSummaryDetDto.setModifiedTime(new Date());
        engPackingOrderSummaryDetDto.setModifiedUserId(sysUser.getUserId());

        //判断收货类型 1-确认并可以继续收货 2-收货完成 不能再收货
        if(StringUtils.isEmpty(engPackingOrderSummaryDetDto.getButtonType())){
            engPackingOrderSummaryDetDto.setButtonType((byte)1);
        }
        if(engPackingOrderSummaryDetDto.getButtonType()==1){
            //实收数量为0时状态更改为待收货
            if(StringUtils.isNotEmpty(engPackingOrderSummaryDetDto.getCancelQty())) {
                if (engPackingOrderSummaryDetDto.getReceivingQty().compareTo(BigDecimal.ZERO) == 0) {
                    engPackingOrderSummaryDetDto.setSummaryDetStatus((byte) 1);
                } else {
                    //收货中
                    engPackingOrderSummaryDetDto.setSummaryDetStatus((byte) 2);
                }
            }else {
                //收货中
                if(engPackingOrderSummaryDetDto.getReceivingQty().compareTo(engPackingOrderSummaryDet.getQty())>-1){
                    engPackingOrderSummaryDetDto.setSummaryDetStatus((byte)3);
                }else {
                    engPackingOrderSummaryDetDto.setSummaryDetStatus((byte) 2);
                }
            }
        }else if(engPackingOrderSummaryDetDto.getButtonType()==2){
            //判断分配数量是否等于收货数量
            if(StringUtils.isNotEmpty(engPackingOrderSummaryDetDto.getReceivingQty(),engPackingOrderSummaryDetDto.getDistributionQty()) && engPackingOrderSummaryDetDto.getReceivingQty().compareTo(engPackingOrderSummaryDet.getDistributionQty())==0){
                engPackingOrderSummaryDetDto.setSummaryDetStatus((byte)4);
            }else {
                engPackingOrderSummaryDetDto.setSummaryDetStatus((byte)3);
            }
        }
        int num = engPackingOrderSummaryDetMapper.updateByPrimaryKeySelective(engPackingOrderSummaryDetDto);
            //查询是否已经全部收货完成
            List<EngPackingOrderSummaryDetDto> list = engPackingOrderSummaryDetMapper.findList(ControllerUtil.dynamicCondition("packingOrderSummaryId",engPackingOrderSummary.getPackingOrderSummaryId()));
            int count = list.stream().filter(li->li.getSummaryDetStatus()==3).collect(Collectors.toList()).size();
            if(list.stream().filter(li->li.getSummaryDetStatus()==1).collect(Collectors.toList()).size()==list.size()){
                engPackingOrderSummary.setSummaryStatus((byte)1);
            }else if(count<list.size()){
                //收货中
                engPackingOrderSummary.setSummaryStatus((byte)2);
            }else  if(count==list.size()){
                //待上架
                engPackingOrderSummary.setSummaryStatus((byte)3);
            }else if(list.size()==list.stream().filter(li->li.getSummaryDetStatus()==4).collect(Collectors.toList()).size()){
                //完成
                engPackingOrderSummary.setSummaryStatus((byte)4);
            }
        //更新包装清单明细
        engPackingOrderSummary.setModifiedTime(new Date());
        engPackingOrderSummary.setModifiedUserId(sysUser.getUserId());
        engPackingOrderSummaryMapper.updateByPrimaryKeySelective(engPackingOrderSummary);

            //查询包箱明细
            List<EngPackingOrderSummaryDto> eng = engPackingOrderSummaryMapper.findList(ControllerUtil.dynamicCondition("packingOrderId",engPackingOrder.getPackingOrderId()));
            count = eng.stream().filter(li->li.getSummaryStatus()==3).collect(Collectors.toList()).size();
            if(eng.stream().filter(li->li.getSummaryStatus()==1).collect(Collectors.toList()).size()==eng.size()){
                engPackingOrder.setOrderStatus((byte)2);
            }else if(count<eng.size()){
                //收货中
                engPackingOrder.setOrderStatus((byte)3);
            }else if(count==eng.size()){
                //待上架
                engPackingOrder.setOrderStatus((byte)4);
            }else if(eng.size()==eng.stream().filter(en->en.getSummaryStatus()==4).collect(Collectors.toList()).size()){
                engPackingOrder.setOrderStatus((byte)5);
            }

        //更新包装清单
        engPackingOrder.setModifiedTime(new Date());
        engPackingOrder.setModifiedUserId(sysUser.getUserId());
        engPackingOrderMapper.updateByPrimaryKeySelective(engPackingOrder);

        //增减库存
        if(wmsInnerInventories.size()>0){
             num+=this.addInveentory(wmsInnerInventories,engPackingOrder);
        }

        if(StringUtils.isNotEmpty(engPackingOrderTakeCancelList)){
            //调用扣减上架作业单数量
            ResponseEntity responseEntity = innerFeignApi.cancelJobOrder(engPackingOrderTakeCancelList);
            if(responseEntity.getCode()!=0){
                throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
            }
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int createInnerJobOrder(List<Long> ids) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        List<WmsInnerJobOrder> list = new ArrayList<>();

        //获取收货库存信息
        List<BaseWarehouse> warehouseList = baseFeignApi.findList(new SearchBaseWarehouse()).getData();
        if(StringUtils.isEmpty(warehouseList)){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"获取仓库信息失败");
        }
        Long warehouseId = warehouseList.get(0).getWarehouseId();
        //获取库存状态信息
        SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
        searchBaseInventoryStatus.setWarehouseId(warehouseId);
        List<BaseInventoryStatus> inventoryStatusList = baseFeignApi.findList(searchBaseInventoryStatus).getData();
        if(StringUtils.isEmpty(inventoryStatusList)){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"获取库位信息失败");
        }
        Long inventoryStatus = inventoryStatusList.get(0).getInventoryStatusId();

        //获取货主信息
        List<BaseMaterialOwnerDto> materialOwnerDtoList = baseFeignApi.findList(new SearchBaseMaterialOwner()).getData();
        if(StringUtils.isEmpty(materialOwnerDtoList)){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"获取货主信息失败");
        }
        Long materialOwnerId = materialOwnerDtoList.get(0).getMaterialOwnerId();


        for (Long id : ids) {
            EngPackingOrder engPackingOrder = engPackingOrderMapper.selectByPrimaryKey(id);
            if(engPackingOrder.getOrderStatus()==5){
                throw new BizErrorException("单据已完成");
            }
//            if(engPackingOrder.getOrderStatus()!=4){
//                throw new BizErrorException("单据收货中，无法整单创建上架单");
//            }
            BigDecimal sumQty = BigDecimal.ZERO;
            List<EngPackingOrderSummaryDto> engPackingOrderSummaryDtos = engPackingOrderSummaryMapper.findList(ControllerUtil.dynamicCondition("packingOrderId", id));
            List<WmsInnerJobOrderDet> wmsInnerJobOrderDets = new ArrayList<>();
            if (engPackingOrder.getOrderStatus() == 3 || engPackingOrder.getOrderStatus() == 4) {
                for (EngPackingOrderSummaryDto engPackingOrderSummaryDto : engPackingOrderSummaryDtos) {
                    //收货中及待上架才能进行创建上架单
                    if (engPackingOrderSummaryDto.getSummaryStatus() == 3 || engPackingOrderSummaryDto.getSummaryStatus() == 2) {
                        //查询包箱货品明细
                        List<EngPackingOrderSummaryDetDto> engPackingOrderSummaryDetDtos = engPackingOrderSummaryDetMapper.findList(ControllerUtil.dynamicCondition("packingOrderSummaryId", engPackingOrderSummaryDto.getPackingOrderSummaryId()));
                        for (EngPackingOrderSummaryDetDto engPackingOrderSummaryDetDto : engPackingOrderSummaryDetDtos) {
                            //边上边收 收货中及待上架状态
                            if (StringUtils.isNotEmpty(engPackingOrderSummaryDetDto.getReceivingQty()) && (engPackingOrderSummaryDetDto.getSummaryDetStatus() == 2 || engPackingOrderSummaryDetDto.getSummaryDetStatus() == 3)) {
                                //可上架数量=(收货数量-分配数量)
                                BigDecimal totalQty = engPackingOrderSummaryDetDto.getReceivingQty();
                                if (StringUtils.isNotEmpty(engPackingOrderSummaryDetDto.getDistributionQty())) {
                                    totalQty = (engPackingOrderSummaryDetDto.getReceivingQty().subtract(engPackingOrderSummaryDetDto.getDistributionQty()));

                                    //分配数量.add（可上架数量）
                                    engPackingOrderSummaryDetDto.setDistributionQty(engPackingOrderSummaryDetDto.getDistributionQty().add(totalQty));
                                } else {
                                    engPackingOrderSummaryDetDto.setDistributionQty(totalQty);
                                }
                                //可上架数量大于0创建上架单明细
                                if (totalQty.compareTo(BigDecimal.ZERO) == 1) {
                                    //上架明细单
                                    WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
                                    wmsInnerJobOrderDet.setSourceDetId(engPackingOrderSummaryDetDto.getPackingOrderSummaryDetId());
                                    wmsInnerJobOrderDet.setWarehouseId(warehouseId);
                                    wmsInnerJobOrderDet.setOutStorageId(engPackingOrderSummaryDetDto.getReceivingStorageId());
                                    wmsInnerJobOrderDet.setInStorageId(engPackingOrderSummaryDetDto.getPutawayStorageId());
                                    wmsInnerJobOrderDet.setMaterialId(engPackingOrderSummaryDetDto.getMaterialId());
                                    wmsInnerJobOrderDet.setPlanQty(totalQty);
                                    wmsInnerJobOrderDet.setDistributionQty(totalQty);
                                    wmsInnerJobOrderDet.setOrderStatus((byte) 3);
                                    wmsInnerJobOrderDet.setInventoryStatusId(inventoryStatus);
                                    wmsInnerJobOrderDet.setOption1(engPackingOrderSummaryDto.getCartonCode());
                                    wmsInnerJobOrderDet.setOrgId(sysUser.getOrganizationId());
                                    wmsInnerJobOrderDet.setPackingUnitName(engPackingOrderSummaryDetDto.getUnitName());
                                    wmsInnerJobOrderDets.add(wmsInnerJobOrderDet);

                                    //状态为待上架且分配数量等于收货数量时 更新状态为完成
                                    if (engPackingOrderSummaryDetDto.getSummaryDetStatus() == 3 && engPackingOrderSummaryDetDto.getDistributionQty().compareTo(engPackingOrderSummaryDetDto.getReceivingQty()) == 0) {
                                        engPackingOrderSummaryDetDto.setSummaryDetStatus((byte) 4);
                                    }
                                    engPackingOrderSummaryDetDto.setModifiedUserId(sysUser.getUserId());
                                    engPackingOrderSummaryDetDto.setModifiedTime(new Date());
                                    engPackingOrderSummaryDetMapper.updateByPrimaryKeySelective(engPackingOrderSummaryDetDto);
                                    sumQty = sumQty.add(wmsInnerJobOrderDet.getPlanQty());
                                }
                            }
                        }
                        //判断是否已经收货完成
                        if (engPackingOrderSummaryDto.getSummaryStatus() == 3 && (engPackingOrderSummaryDetDtos.stream().filter(li -> li.getSummaryDetStatus() == 4).collect(Collectors.toList()).size() == engPackingOrderSummaryDetDtos.size())) {
                            engPackingOrderSummaryDto.setSummaryStatus((byte) 4);
                            engPackingOrderSummaryDto.setModifiedUserId(sysUser.getUserId());
                            engPackingOrderSummaryDto.setModifiedTime(new Date());
                            engPackingOrderSummaryMapper.updateByPrimaryKeySelective(engPackingOrderSummaryDto);
                        }
                    }
                }
                //更新表头状态
                if(engPackingOrder.getOrderStatus()==4 && (engPackingOrderSummaryDtos.size()==engPackingOrderSummaryDtos.stream().filter(li->li.getSummaryStatus()==4).collect(Collectors.toList()).size())){
                    engPackingOrder.setOrderStatus((byte)5);
                    engPackingOrder.setModifiedUserId(sysUser.getUserId());
                    engPackingOrder.setModifiedTime(new Date());
                    engPackingOrderMapper.updateByPrimaryKeySelective(engPackingOrder);
                }
            }
            if(wmsInnerJobOrderDets.size()>0){
                WmsInnerJobOrder wmsInnerJobOrder = new WmsInnerJobOrder();
                wmsInnerJobOrder.setSourceOrderId(engPackingOrder.getPackingOrderId());
                wmsInnerJobOrder.setOrderTypeId(Long.parseLong("9"));
                wmsInnerJobOrder.setJobOrderType((byte)3);
                wmsInnerJobOrder.setRelatedOrderCode(engPackingOrder.getPackingOrderCode());
                wmsInnerJobOrder.setWarehouseId(warehouseId);
                wmsInnerJobOrder.setPlanQty(sumQty);
                wmsInnerJobOrder .setOrderStatus((byte) 3);
                wmsInnerJobOrder .setActualQty(new BigDecimal("0"));
                wmsInnerJobOrder .setWmsInPutawayOrderDets(wmsInnerJobOrderDets);
                wmsInnerJobOrder .setOrgId(sysUser.getOrganizationId());
                wmsInnerJobOrder .setMaterialOwnerId(materialOwnerId);
                list.add(wmsInnerJobOrder);
            }
        }
        if(list.size()>0){
            ResponseEntity responseEntity = innerFeignApi.addList(list);
            if(responseEntity.getCode()!=0){
                throw new BizErrorException("创建上架单失败");
            }
            return 1;
        }else {
            throw new BizErrorException("没有可上架的货品");
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int pdaCreateInnerJobOrder(List<EngPackingOrderSummaryDetDto> engPackingOrderSummaryDetDtos) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();

        //获取收货库存信息
        List<BaseWarehouse> warehouseList = baseFeignApi.findList(new SearchBaseWarehouse()).getData();
        if(StringUtils.isEmpty(warehouseList)){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"获取仓库信息失败");
        }
        Long warehouseId = warehouseList.get(0).getWarehouseId();

        //获取库存状态信息
        SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
        searchBaseInventoryStatus.setWarehouseId(warehouseId);
        List<BaseInventoryStatus> inventoryStatusList = baseFeignApi.findList(searchBaseInventoryStatus).getData();
        if(StringUtils.isEmpty(inventoryStatusList)){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"获取库位信息失败");
        }
        Long inventoryStatus = inventoryStatusList.get(0).getInventoryStatusId();

        //获取货主信息
        List<BaseMaterialOwnerDto> materialOwnerDtoList = baseFeignApi.findList(new SearchBaseMaterialOwner()).getData();
        if(StringUtils.isEmpty(materialOwnerDtoList)){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"获取货主信息失败");
        }
        Long materialOwnerId = materialOwnerDtoList.get(0).getMaterialOwnerId();


        List<WmsInnerJobOrderDet> wmsInnerJobOrderDets = new ArrayList<>();
        BigDecimal sumQty = BigDecimal.ZERO;
        EngPackingOrder engPackingOrder = null;
        for (EngPackingOrderSummaryDetDto engPackingOrderSummaryDetDto : engPackingOrderSummaryDetDtos) {
            EngPackingOrderSummary engPackingOrderSummary = engPackingOrderSummaryMapper.selectByPrimaryKey(engPackingOrderSummaryDetDto.getPackingOrderSummaryId());
            engPackingOrder = engPackingOrderMapper.selectByPrimaryKey(engPackingOrderSummary.getPackingOrderId());
                //边上边收 收货中及待上架状态
                if (StringUtils.isNotEmpty(engPackingOrderSummaryDetDto.getReceivingQty()) && (engPackingOrderSummaryDetDto.getSummaryDetStatus() == 2 || engPackingOrderSummaryDetDto.getSummaryDetStatus() == 3)) {
                    //可上架数量=(收货数量-分配数量)
                    BigDecimal totalQty = engPackingOrderSummaryDetDto.getReceivingQty();
                    if (StringUtils.isNotEmpty(engPackingOrderSummaryDetDto.getDistributionQty())) {
                        totalQty = (engPackingOrderSummaryDetDto.getReceivingQty().subtract(engPackingOrderSummaryDetDto.getDistributionQty()));

                        //分配数量.add（可上架数量）
                        engPackingOrderSummaryDetDto.setDistributionQty(engPackingOrderSummaryDetDto.getDistributionQty().add(totalQty));
                    } else {
                        engPackingOrderSummaryDetDto.setDistributionQty(totalQty);
                    }
                    //可上架数量大于0创建上架单明细
                    if (totalQty.compareTo(BigDecimal.ZERO) == 1) {
                        //上架明细单
                        WmsInnerJobOrderDet wmsInnerJobOrderDet = WmsInnerJobOrderDet.builder()
                                .sourceDetId(engPackingOrderSummaryDetDto.getPackingOrderSummaryDetId())
                                .warehouseId(warehouseId)
                                .outStorageId(engPackingOrderSummaryDetDto.getReceivingStorageId())
                                .inStorageId(engPackingOrderSummaryDetDto.getPutawayStorageId())
                                .materialId(engPackingOrderSummaryDetDto.getMaterialId())
                                .planQty(totalQty)
                                .distributionQty(totalQty)
                                .orderStatus((byte) 3)
                                .inventoryStatusId(inventoryStatus)
                                .option1(engPackingOrderSummary.getCartonCode())
                                .orgId(sysUser.getOrganizationId())
                                .packingUnitName(engPackingOrderSummaryDetDto.getUnitName())
                                .build();
                        wmsInnerJobOrderDets.add(wmsInnerJobOrderDet);

                        //状态为待上架且分配数量等于收货数量时 更新状态为完成
                        if (engPackingOrderSummaryDetDto.getSummaryDetStatus() == 3 && engPackingOrderSummaryDetDto.getDistributionQty().compareTo(engPackingOrderSummaryDetDto.getReceivingQty()) == 0) {
                            engPackingOrderSummaryDetDto.setSummaryDetStatus((byte) 4);
                        }
                        engPackingOrderSummaryDetDto.setModifiedUserId(sysUser.getUserId());
                        engPackingOrderSummaryDetDto.setModifiedTime(new Date());
                        engPackingOrderSummaryDetMapper.updateByPrimaryKeySelective(engPackingOrderSummaryDetDto);
                        sumQty = sumQty.add(totalQty);
                    }
                }
            //查询是否整箱收货完成
            List<EngPackingOrderSummaryDetDto> list = engPackingOrderSummaryDetMapper.findList(ControllerUtil.dynamicCondition("packingOrderSummaryId",engPackingOrderSummary.getPackingOrderSummaryId()));
            //判断是否已经收货完成
            if (engPackingOrderSummary.getSummaryStatus() == 3 && (list.stream().filter(li -> li.getSummaryDetStatus() == 4).collect(Collectors.toList()).size() == list.size())) {
                engPackingOrderSummary.setSummaryStatus((byte) 4);
                engPackingOrderSummary.setModifiedUserId(sysUser.getUserId());
                engPackingOrderSummary.setModifiedTime(new Date());
                engPackingOrderSummaryMapper.updateByPrimaryKeySelective(engPackingOrderSummary);
            }
            List<EngPackingOrderSummaryDto> engPackingOrderSummaryDtos = engPackingOrderSummaryMapper.findList(ControllerUtil.dynamicCondition("packingOrderId",engPackingOrder.getPackingOrderId()));
            //更新表头状态
            if(engPackingOrder.getOrderStatus()==4 && (engPackingOrderSummaryDtos.size()==engPackingOrderSummaryDtos.stream().filter(li->li.getSummaryStatus()==4).collect(Collectors.toList()).size())){
                engPackingOrder.setOrderStatus((byte)5);
                engPackingOrder.setModifiedUserId(sysUser.getUserId());
                engPackingOrder.setModifiedTime(new Date());
                engPackingOrderMapper.updateByPrimaryKeySelective(engPackingOrder);
            }
        }
        if(wmsInnerJobOrderDets.size()>0){
            List<WmsInnerJobOrder> wmsInnerJobOrders = new ArrayList<>();
            WmsInnerJobOrder wmsInnerJobOrder = WmsInnerJobOrder.builder()
                    .sourceOrderId(engPackingOrder.getPackingOrderId())
                    .orderTypeId(Long.parseLong("9"))
                    .jobOrderType((byte)3)
                    .relatedOrderCode(engPackingOrder.getPackingOrderCode())
                    .warehouseId(warehouseId)
                    .planQty(sumQty)
                    .orderStatus((byte) 3)
                    .actualQty(new BigDecimal("0"))
                    .wmsInPutawayOrderDets(wmsInnerJobOrderDets)
                    .orgId(sysUser.getOrganizationId())
                    .materialOwnerId(materialOwnerId)
                    .build();
            wmsInnerJobOrders.add(wmsInnerJobOrder);
            ResponseEntity responseEntity = innerFeignApi.addList(wmsInnerJobOrders);
            if(responseEntity.getCode()!=0){
                throw new BizErrorException("创建上架单失败");
            }
        }else {
            throw new BizErrorException("改货品暂无可上架数量");
        }
        return 1;
    }

    /**
     * 整单取消
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int cancelAll(List<Long> ids) {
        SysUser sysUser  = CurrentUserInfoUtils.getCurrentUserInfo();
        int num = 0;
        for (Long id : ids) {
            EngPackingOrder engPackingOrder = engPackingOrderMapper.selectByPrimaryKey(id);
            List<EngPackingOrderSummaryDto> engPackingOrderSummaryDtos = engPackingOrderSummaryMapper.findList(ControllerUtil.dynamicCondition("packingOrderId",engPackingOrder.getPackingOrderId()));
            for (EngPackingOrderSummaryDto engPackingOrderSummaryDto : engPackingOrderSummaryDtos) {
                List<EngPackingOrderSummaryDetDto> engPackingOrderSummaryDetDtos = engPackingOrderSummaryDetMapper.findList(ControllerUtil.dynamicCondition("packingOrderSummaryId", engPackingOrderSummaryDto.getPackingOrderSummaryId()));
                for (EngPackingOrderSummaryDetDto engPackingOrderSummaryDetDto : engPackingOrderSummaryDetDtos) {
                    engPackingOrderSummaryDetDto.setCancelQty(engPackingOrderSummaryDetDto.getReceivingQty());
                    engPackingOrderSummaryDetDto.setButtonType((byte)1);
                    num+=this.onlyTask(engPackingOrderSummaryDetDto);
                }
            }
        }
        return num;
    }

    /**
     * 单一取消收货
     * @param engPackingOrderSummaryDetDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int onlyCancel(EngPackingOrderSummaryDetDto engPackingOrderSummaryDetDto) {
//        engPackingOrderSummaryDetDto.setCancelQty(engPackingOrderSummaryDetDto.getCancelQty());
        engPackingOrderSummaryDetDto.setButtonType((byte)1);
        return this.onlyTask(engPackingOrderSummaryDetDto);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int writeQty(Long id, BigDecimal qty,Byte type) {
        EngPackingOrderSummaryDet engPackingOrderSummaryDet = engPackingOrderSummaryDetMapper.selectByPrimaryKey(id);
        if(StringUtils.isEmpty(engPackingOrderSummaryDet.getPutawayQty())){
            engPackingOrderSummaryDet.setPutawayQty(BigDecimal.ZERO);
        }
        if(type==1){
            engPackingOrderSummaryDet.setPutawayQty(engPackingOrderSummaryDet.getPutawayQty().add(qty));
        }else {
            engPackingOrderSummaryDet.setDistributionQty(engPackingOrderSummaryDet.getDistributionQty().add(qty));
        }

        //返写合同量单
        Example qtyExample = new Example(EngContractQtyOrder.class);
        Example.Criteria qtyCriteria = qtyExample.createCriteria();
        qtyCriteria.andEqualTo("contractCode",engPackingOrderSummaryDet.getCartonCode());
        qtyCriteria.andEqualTo("dominantTermCode",engPackingOrderSummaryDet.getDominantTermCode());
        qtyCriteria.andEqualTo("deviceCode",engPackingOrderSummaryDet.getDeviceCode());
        qtyCriteria.andEqualTo("materialCode",engPackingOrderSummaryDet.getMaterialId());
        List<EngContractQtyOrder> engContractQtyOrders = engContractQtyOrderMapper.selectByExample(qtyExample);
        if(StringUtils.isNotEmpty(engContractQtyOrders)){
            //throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"未查询到对应的合同量单");
            EngContractQtyOrder order = engContractQtyOrders.get(0);
            if(StringUtils.isEmpty(order.getAgoQty())){
                order.setAgoQty(qty);
            }else{
                order.setAgoQty(order.getAgoQty().add(qty));
            }
            engContractQtyOrderMapper.updateByPrimaryKeySelective(order);
        }
        return engPackingOrderSummaryDetMapper.updateByPrimaryKeySelective(engPackingOrderSummaryDet);
    }

    /**
     * 增减库存
     * @param wmsInnerInventories
     * @param engPackingOrder
     * @return
     */
    private int addInveentory(List<WmsInnerInventory> wmsInnerInventories,EngPackingOrder engPackingOrder){
        List<WmsInnerInventory> list = new ArrayList<>();
        for (WmsInnerInventory wms : wmsInnerInventories) {
            Map<String,Object> map = new HashMap<>();
            map.put("relevanceOrderCode",wms.getRelevanceOrderCode());
            map.put("materialId",wms.getMaterialId());
            map.put("warehouseId",wms.getWarehouseId());
            map.put("storageId",wms.getStorageId());
            map.put("inventoryStatusId",wms.getInventoryStatusId());
            WmsInnerInventory wmsInnerInventory = innerFeignApi.selectOneByExample(map).getData();
            BigDecimal qty = BigDecimal.ZERO;
            if((wms.getPackingQty().signum()==-1)){
                if(StringUtils.isEmpty(wmsInnerInventory) || wmsInnerInventory.getPackingQty().compareTo(BigDecimal.ZERO)<1){
                    throw new BizErrorException("未查询到库存");
                }
            }
            if(StringUtils.isEmpty(wmsInnerInventory)){
                //添加库存
                list.add(wms);
            }else{
                //原库存
                qty = wmsInnerInventory.getPackingQty();
                wmsInnerInventory.setOption1(wms.getOption1());
                wmsInnerInventory.setOption2(wms.getOption2());
                wmsInnerInventory.setOption3(wms.getOption3());
                wmsInnerInventory.setOption4(wms.getOption4());
                wmsInnerInventory.setPackingQty(wmsInnerInventory.getPackingQty().add(wms.getPackingQty()));
                ResponseEntity responseEntity =  innerFeignApi.updateByPrimaryKeySelective(wmsInnerInventory);
                if(responseEntity.getCode()!=0){
                    throw new BizErrorException("确认失败");
                }
            }
            //添加库存日志
            Byte addOrSub = 1;
            BigDecimal chaQty = BigDecimal.ZERO;
            if(StringUtils.isNotEmpty(wmsInnerInventory) && wms.getPackingQty().signum()==-1){
                //减
                addOrSub = ((byte)2);
                chaQty = qty.subtract(wmsInnerInventory.getPackingQty());
            }else {
                //加
                addOrSub = ((byte)1);
                chaQty = wms.getPackingQty();
            }
            this.addLog(engPackingOrder,wms,qty,chaQty,addOrSub);
        }
        if(list.size()>0){
            //批量新增库存明细
            ResponseEntity responseEntity =innerFeignApi.insertList(list);
            if(responseEntity.getCode()!=0){
                throw new BizErrorException("库存添加失败");
            }
        }
        return 1;
    }

    /**
     * 库存日志
     * @param wmsInnerInventory
     * @param engPackingOrder
     */
    private void addLog(EngPackingOrder engPackingOrder,WmsInnerInventory wmsInnerInventory,BigDecimal initQty,BigDecimal chQty,Byte addOrSub){
        WmsInnerInventoryLog wmsInnerInventoryLog = new WmsInnerInventoryLogDto();
        BeanUtil.copyProperties(wmsInnerInventory,wmsInnerInventoryLog);
//        wmsInnerInventoryLog.setPurchaseReqOrderCode(wmsInnerInventory.getPurchaseReqOrderCode());
//        wmsInnerInventoryLog.setSpec(wmsInnerInventory.getSpec());
//        wmsInnerInventoryLog.setContractCode(wmsInnerInventory.getContractCode());
        //收货
        wmsInnerInventoryLog.setJobOrderType((byte)1);
        wmsInnerInventoryLog.setAddOrSubtract(addOrSub);
        wmsInnerInventoryLog.setRelatedOrderCode(wmsInnerInventory.getRelevanceOrderCode());
        wmsInnerInventoryLog.setAsnCode(engPackingOrder.getPackingOrderCode());
        wmsInnerInventoryLog.setSupplierId(engPackingOrder.getSupplierId());
        wmsInnerInventoryLog.setWarehouseId(wmsInnerInventory.getWarehouseId());
        wmsInnerInventoryLog.setStorageId(wmsInnerInventory.getStorageId());
        wmsInnerInventoryLog.setMaterialId(wmsInnerInventory.getMaterialId());
        wmsInnerInventoryLog.setInventoryStatusId(wmsInnerInventory.getInventoryStatusId());
        wmsInnerInventoryLog.setPackingUnitName(wmsInnerInventory.getPackingUnitName());
        wmsInnerInventoryLog.setInitialQty(initQty);
        wmsInnerInventoryLog.setChangeQty(chQty);
        ResponseEntity responseEntity = innerFeignApi.add(wmsInnerInventoryLog);
        if(responseEntity.getCode()!=0){
            throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
        }
    }

//    private void check(EngPackingOrder engPackingOrder,EngPackingOrderSummary engPackingOrderSummary,EngPackingOrderSummaryDet engPackingOrderSummaryDet,Byte type){
//        switch (type){
//            //到场登记
//            case 1:
//                if(engPackingOrder.getOrderStatus()>1){
//                    throw new BizErrorException("单据已登记，重复操作");
//                }
//                break;
//                //收货
//            case 2:
//                if(engPackingOrder.getOrderStatus()<2){
//                    throw new BizErrorException("单据未登记");
//                }
//                if(engPackingOrder.getOrderStatus()>=4){
//                    throw new BizErrorException("单据已收货完成");
//                }
//                if(engPackingOrderSummary.getSummaryStatus()>3){
//                    throw new BizErrorException("装箱收货已完成");
//                }
//                if(engPackingOrderSummaryDet.getSummaryDetStatus()>3){
//                    throw new BizErrorException("货品收货已完成");
//                }
//                break;
//                //创建上架作业单
//            case 3:
//                if(engPackingOrder.getOrderStatus()==5){
//
//                }
//        }
//        if(engPackingOrder.getOrderStatus()==4){
//            throw new BizErrorException("单据收货已完成");
//        }
//    }
}
