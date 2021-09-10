package com.fantechs.provider.guest.eng.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderDto;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderSummaryDetDto;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderSummaryDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryLogDto;
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
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.guest.eng.mapper.EngPackingOrderMapper;
import com.fantechs.provider.guest.eng.mapper.EngPackingOrderSummaryDetMapper;
import com.fantechs.provider.guest.eng.mapper.EngPackingOrderSummaryMapper;
import com.fantechs.provider.guest.eng.service.EngPackingOrderTakeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            engPackingOrder.setOrderStatus((byte)2);
            engPackingOrder.setAgoConfirmTime(new Date());
            engPackingOrder.setAgoConfirmUserId(sysUser.getUserId());
            engPackingOrder.setModifiedTime(new Date());
            engPackingOrder.setModifiedUserId(sysUser.getUserId());
            num+=engPackingOrderMapper.updateByPrimaryKeySelective(engPackingOrder);
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
    public int allTask(List<Long> ids) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        List<WmsInnerInventory> wmsInnerInventories = new ArrayList<>();
        //获取收货库存信息
        Long warehouseId = engPackingOrderMapper.findWarehouse(ControllerUtil.dynamicCondition("orgId",sysUser.getOrganizationId()));
        if(StringUtils.isEmpty(warehouseId)){
            throw new BizErrorException("获取仓库信息失败");
        }
//        //获取库位信息
//        Long storageId = engPackingOrderMapper.findStorage(ControllerUtil.dynamicCondition("orgId",user.getOrganizationId(),"warehouseId",warehouseId));
//        if(StringUtils.isEmpty(storageId)){
//            throw new BizErrorException("获取库位信息失败");
//        }
        //获取库存状态信息
        Long inventoryStatus = engPackingOrderMapper.findInventoryStatus(ControllerUtil.dynamicCondition("orgId",sysUser.getOrganizationId(),"warehouseId",warehouseId));
        if(StringUtils.isEmpty(inventoryStatus)){
            throw new BizErrorException("获取库位信息失败");
        }
        int num = 0;
        for (Long id : ids) {
            EngPackingOrder engPackingOrder  = engPackingOrderMapper.selectByPrimaryKey(id);
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
                    wmsInnerInventories.add(wmsInnerInventory);

                    //收货数量
                    engPackingOrderSummaryDetDto.setReceivingQty(engPackingOrderSummaryDetDto.getQty());
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
    public int boxTask(List<EngPackingOrderSummaryDto> engPackingOrderSummaryDtos) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        List<WmsInnerInventory> wmsInnerInventories = new ArrayList<>();
        //获取收货库存信息
        Long warehouseId = engPackingOrderMapper.findWarehouse(ControllerUtil.dynamicCondition("orgId",sysUser.getOrganizationId()));
        if(StringUtils.isEmpty(warehouseId)){
            throw new BizErrorException("获取仓库信息失败");
        }
        //获取库存状态信息
        Long inventoryStatus = engPackingOrderMapper.findInventoryStatus(ControllerUtil.dynamicCondition("orgId",sysUser.getOrganizationId(),"warehouseId",warehouseId));
        if(StringUtils.isEmpty(inventoryStatus)){
            throw new BizErrorException("获取库位信息失败");
        }
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
                wmsInnerInventories.add(wmsInnerInventory);

                //收货数量
                engPackingOrderSummaryDetDto.setReceivingQty(engPackingOrderSummaryDetDto.getQty());
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
    public int onlyTask(EngPackingOrderSummaryDetDto engPackingOrderSummaryDetDto) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        List<WmsInnerInventory> wmsInnerInventories = new ArrayList<>();
        //获取收货库存信息
        Long warehouseId = engPackingOrderMapper.findWarehouse(ControllerUtil.dynamicCondition("orgId",sysUser.getOrganizationId()));
        if(StringUtils.isEmpty(warehouseId)){
            throw new BizErrorException("获取仓库信息失败");
        }
        //获取库存状态信息
        Long inventoryStatus = engPackingOrderMapper.findInventoryStatus(ControllerUtil.dynamicCondition("orgId",sysUser.getOrganizationId(),"warehouseId",warehouseId));
        if(StringUtils.isEmpty(inventoryStatus)){
            throw new BizErrorException("获取库位信息失败");
        }
        EngPackingOrderSummaryDet engPackingOrderSummaryDet = engPackingOrderSummaryDetMapper.selectByPrimaryKey(engPackingOrderSummaryDetDto.getPackingOrderSummaryDetId());
        EngPackingOrderSummary engPackingOrderSummary = engPackingOrderSummaryMapper.selectByPrimaryKey(engPackingOrderSummaryDetDto.getPackingOrderSummaryId());
        EngPackingOrder engPackingOrder = engPackingOrderMapper.selectByPrimaryKey(engPackingOrderSummary.getPackingOrderId());

        if(StringUtils.isEmpty(engPackingOrderSummaryDetDto.getCancelQty())){
            if(StringUtils.isEmpty(engPackingOrderSummaryDet.getReceivingQty()) || (engPackingOrderSummaryDet.getReceivingQty().subtract(engPackingOrderSummaryDetDto.getCancelQty()).compareTo(BigDecimal.ZERO))==-1){
                throw new BizErrorException("取消数量不能大于实收数量");
            }
            engPackingOrderSummaryDetDto.setReceivingQty(engPackingOrderSummaryDetDto.getCancelQty().negate());
        }
        if(StringUtils.isNotEmpty(engPackingOrderSummaryDetDto.getReceivingQty())){
            //创建收货库存
            WmsInnerInventory wmsInnerInventory = new WmsInnerInventory();
            wmsInnerInventory.setInventoryStatusId(inventoryStatus);
            wmsInnerInventory.setPackingQty(engPackingOrderSummaryDetDto.getReceivingQty());
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
            wmsInnerInventory.setOrgId(sysUser.getOrganizationId());
            wmsInnerInventories.add(wmsInnerInventory);
        }

        //收货数量
        if(StringUtils.isNotEmpty(engPackingOrderSummaryDet.getReceivingQty())){
            engPackingOrderSummaryDetDto.setReceivingQty(engPackingOrderSummaryDet.getReceivingQty().add(engPackingOrderSummaryDetDto.getReceivingQty()));
        }
        engPackingOrderSummaryDetDto.setModifiedTime(new Date());
        engPackingOrderSummaryDetDto.setModifiedUserId(sysUser.getUserId());
        engPackingOrderSummaryDetMapper.updateByPrimaryKeySelective(engPackingOrderSummaryDetDto);

        //判断收货类型 1-确认并可以继续收货 2-收货完成 不能再收货
        if(engPackingOrderSummaryDetDto.getButtonType()==1){
            //收货中
            engPackingOrderSummaryDetDto.setSummaryDetStatus((byte)2);
            engPackingOrderSummary.setSummaryStatus((byte)2);
        }else {
            //查询是否已经全部收货完成
            List<EngPackingOrderSummaryDetDto> list = engPackingOrderSummaryDetMapper.findList(ControllerUtil.dynamicCondition("packingOrderSummaryId",engPackingOrderSummary.getPackingOrderSummaryId()));
            int count = list.stream().filter(li->li.getSummaryDetStatus()==3).collect(Collectors.toList()).size();
            if(count<list.size()){
                //收货中
                engPackingOrderSummary.setSummaryStatus((byte)2);
            }else  if(count==list.size()){
                //待上架
                engPackingOrderSummary.setSummaryStatus((byte)3);
            }

            //查询包箱明细
            List<EngPackingOrderSummaryDto> eng = engPackingOrderSummaryMapper.findList(ControllerUtil.dynamicCondition("packingOrderId",engPackingOrder.getPackingOrderId()));
            count = eng.stream().filter(li->li.getSummaryStatus()==3).collect(Collectors.toList()).size();
            if(count<eng.size()){
                //收货中
                engPackingOrder.setOrderStatus((byte)3);
            }else if(count==eng.size()){
                //待上架
                engPackingOrder.setOrderStatus((byte)4);
            }
        }
        //更新包装清单明细
        engPackingOrderSummary.setModifiedTime(new Date());
        engPackingOrderSummary.setModifiedUserId(sysUser.getUserId());
        engPackingOrderSummaryMapper.updateByPrimaryKeySelective(engPackingOrderSummary);

        //更新包装清单
        engPackingOrder.setModifiedTime(new Date());
        engPackingOrder.setModifiedUserId(sysUser.getUserId());
        engPackingOrderMapper.updateByPrimaryKeySelective(engPackingOrder);

        //增减库存
        int num = this.addInveentory(wmsInnerInventories,engPackingOrder);
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int createInnerJobOrder(List<Long> ids) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        List<WmsInnerJobOrder> list = new ArrayList<>();
        //获取收货库存信息
        Long warehouseId = engPackingOrderMapper.findWarehouse(ControllerUtil.dynamicCondition("orgId",sysUser.getOrganizationId()));
        if(StringUtils.isEmpty(warehouseId)){
            throw new BizErrorException("获取仓库信息失败");
        }
        //获取库存状态信息
        Long inventoryStatus = engPackingOrderMapper.findInventoryStatus(ControllerUtil.dynamicCondition("orgId",sysUser.getOrganizationId(),"warehouseId",warehouseId));
        if(StringUtils.isEmpty(inventoryStatus)){
            throw new BizErrorException("获取库位信息失败");
        }
        for (Long id : ids) {
            EngPackingOrder engPackingOrder = engPackingOrderMapper.selectByPrimaryKey(id);
//            if(engPackingOrder.getOrderStatus()!=4){
//                throw new BizErrorException("单据收货中，无法整单创建上架单");
//            }
            BigDecimal sumQty = BigDecimal.ZERO;
            List<EngPackingOrderSummaryDto> engPackingOrderSummaryDtos = engPackingOrderSummaryMapper.findList(ControllerUtil.dynamicCondition("packingOrderId", id));
            List<WmsInnerJobOrderDet> wmsInnerJobOrderDets = new ArrayList<>();
            if (engPackingOrder.getOrderStatus() == 3 || engPackingOrder.getOrderStatus() == 4) {
                for (EngPackingOrderSummaryDto engPackingOrderSummaryDto : engPackingOrderSummaryDtos) {
                    //收货中及待上架才能进行创建上架单
                    if (engPackingOrderSummaryDto.getSummaryStatus() == 3 || engPackingOrderSummaryDto.getSummaryStatus() == 4) {
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
                                            .option1(engPackingOrderSummaryDto.getCartonCode())
                                            .orgId(sysUser.getOrganizationId())
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
                if(engPackingOrder.getOrderStatus()==3 && (engPackingOrderSummaryDtos.size()==engPackingOrderSummaryDtos.stream().filter(li->li.getSummaryStatus()==4).collect(Collectors.toList()).size())){
                    engPackingOrder.setOrderStatus((byte)5);
                    engPackingOrder.setModifiedUserId(sysUser.getUserId());
                    engPackingOrder.setModifiedTime(new Date());
                    engPackingOrderMapper.updateByPrimaryKeySelective(engPackingOrder);
                }
            }
            if(wmsInnerJobOrderDets.size()>0){
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
                        .build();
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
    public int pdaCreateInnerJobOrder(List<EngPackingOrderSummaryDetDto> engPackingOrderSummaryDetDtos) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();

        //获取收货库存信息
        Long warehouseId = engPackingOrderMapper.findWarehouse(ControllerUtil.dynamicCondition("orgId",sysUser.getOrganizationId()));
        if(StringUtils.isEmpty(warehouseId)){
            throw new BizErrorException("获取仓库信息失败");
        }
        //获取库存状态信息
        Long inventoryStatus = engPackingOrderMapper.findInventoryStatus(ControllerUtil.dynamicCondition("orgId",sysUser.getOrganizationId(),"warehouseId",warehouseId));
        if(StringUtils.isEmpty(inventoryStatus)){
            throw new BizErrorException("获取库位信息失败");
        }
        EngPackingOrderSummary engPackingOrderSummary = engPackingOrderSummaryMapper.selectByPrimaryKey(engPackingOrderSummaryDetDtos.get(0).getPackingOrderSummaryId());
        EngPackingOrder engPackingOrder = engPackingOrderMapper.selectByPrimaryKey(engPackingOrderSummary.getPackingOrderId());
        List<WmsInnerJobOrder> wmsInnerJobOrders = new ArrayList<>();
        List<WmsInnerJobOrderDet> wmsInnerJobOrderDets = new ArrayList<>();
        BigDecimal sumQty = BigDecimal.ZERO;
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
        if(engPackingOrder.getOrderStatus()==3 && (engPackingOrderSummaryDtos.size()==engPackingOrderSummaryDtos.stream().filter(li->li.getSummaryStatus()==4).collect(Collectors.toList()).size())){
            engPackingOrder.setOrderStatus((byte)5);
            engPackingOrder.setModifiedUserId(sysUser.getUserId());
            engPackingOrder.setModifiedTime(new Date());
            engPackingOrderMapper.updateByPrimaryKeySelective(engPackingOrder);
        }
        if(wmsInnerJobOrderDets.size()>0){
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
            if(StringUtils.isEmpty(wmsInnerInventory)){
                //添加库存
                list.add(wms);
            }else{
                //原库存
                wmsInnerInventory.setPackingQty(wmsInnerInventory.getPackingQty().add(wms.getPackingQty()));
                ResponseEntity responseEntity =  innerFeignApi.updateByPrimaryKeySelective(wmsInnerInventory);
                if(responseEntity.getCode()!=0){
                    throw new BizErrorException("确认失败");
                }
            }
            //添加库存日志
            this.addLog(wmsInnerInventory,engPackingOrder);
        }
        //批量新增库存明细
        ResponseEntity responseEntity =innerFeignApi.insertList(list);
        if(responseEntity.getCode()!=0){
            throw new BizErrorException("库存添加失败");
        }
        return 1;
    }

    /**
     * 库存日志
     * @param wmsInnerInventory
     * @param engPackingOrder
     */
    private void addLog(WmsInnerInventory wmsInnerInventory,EngPackingOrder engPackingOrder){
        WmsInnerInventoryLog wmsInnerInventoryLog = new WmsInnerInventoryLogDto();
        wmsInnerInventoryLog.setPurchaseReqOrderCode(wmsInnerInventory.getPurchaseReqOrderCode());
        wmsInnerInventoryLog.setSpec(wmsInnerInventory.getSpec());
        wmsInnerInventoryLog.setContractCode(wmsInnerInventory.getContractCode());
        //收货
        wmsInnerInventoryLog.setJobOrderType((byte)1);
        if(wmsInnerInventory.getPackingQty().signum()==-1){
            wmsInnerInventoryLog.setAddOrSubtract((byte)1);
        }else {
            wmsInnerInventoryLog.setAddOrSubtract((byte)2);
        }
        wmsInnerInventoryLog.setSupplierId(engPackingOrder.getSupplierId());
        wmsInnerInventoryLog.setWarehouseId(wmsInnerInventory.getWarehouseId());
        wmsInnerInventoryLog.setStorageId(wmsInnerInventory.getStorageId());
        wmsInnerInventoryLog.setMaterialId(wmsInnerInventory.getMaterialId());
        wmsInnerInventoryLog.setInventoryStatusId(wmsInnerInventory.getInventoryStatusId());
        wmsInnerInventoryLog.setChangeQty(wmsInnerInventory.getPackingQty());
        ResponseEntity responseEntity = innerFeignApi.add(wmsInnerInventoryLog);
        if(responseEntity.getCode()!=0){
            throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
        }
    }
}
