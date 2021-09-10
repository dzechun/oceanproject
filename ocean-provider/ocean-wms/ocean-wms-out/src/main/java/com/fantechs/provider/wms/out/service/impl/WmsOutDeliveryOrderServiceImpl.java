package com.fantechs.provider.wms.out.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutTransferDeliveryOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrder;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrderDet;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtDeliveryOrder;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtDeliveryOrderDet;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutDeliveryOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.wms.out.mapper.WmsOutDeliveryOrderDetMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutDeliveryOrderMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutHtDeliveryOrderDetMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutHtDeliveryOrderMapper;
import com.fantechs.provider.wms.out.service.WmsOutDeliveryOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2021/05/07.
 */
@Service
public class WmsOutDeliveryOrderServiceImpl extends BaseService<WmsOutDeliveryOrder> implements WmsOutDeliveryOrderService {

    @Resource
    private WmsOutDeliveryOrderMapper wmsOutDeliveryOrderMapper;
    @Resource
    private WmsOutDeliveryOrderDetMapper wmsOutDeliveryOrderDetMapper;
    @Resource
    private WmsOutHtDeliveryOrderMapper wmsOutHtDeliveryOrderMapper;
    @Resource
    private WmsOutHtDeliveryOrderDetMapper wmsOutHtDeliveryOrderDetMapper;
    @Resource
    private InnerFeignApi innerFeignApi;
    @Resource
    private SecurityFeignApi securityFeignApi;

    @Override
    public List<WmsOutDeliveryOrderDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        map.put("orgId",user.getOrganizationId());
        List<WmsOutDeliveryOrderDto> wmsOutDeliveryOrderDtos = wmsOutDeliveryOrderMapper.findList(map);

        for (WmsOutDeliveryOrderDto wmsOutDeliveryOrderDto : wmsOutDeliveryOrderDtos) {
            SearchWmsOutDeliveryOrderDet searchWmsOutDeliveryOrderDet = new SearchWmsOutDeliveryOrderDet();
            searchWmsOutDeliveryOrderDet.setDeliveryOrderId(wmsOutDeliveryOrderDto.getDeliveryOrderId());
            List<WmsOutDeliveryOrderDetDto> list = wmsOutDeliveryOrderDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutDeliveryOrderDet));
            wmsOutDeliveryOrderDto.setWmsOutDeliveryOrderDetList(list);
            //计算总数量
            BigDecimal packingSum = list.stream().map(WmsOutDeliveryOrderDetDto::getPackingQty).filter(e->e!=null).reduce(BigDecimal.ZERO,BigDecimal::add);
            wmsOutDeliveryOrderDto.setTotalPackingQty(packingSum);
            BigDecimal pickingSum = list.stream().map(WmsOutDeliveryOrderDetDto::getPickingQty).filter(e->e!=null).reduce(BigDecimal.ZERO,BigDecimal::add);
            wmsOutDeliveryOrderDto.setTotalPickingQty(pickingSum);
            BigDecimal dispatchQty = list.stream().map(WmsOutDeliveryOrderDetDto::getDispatchQty).filter(e->e!=null).reduce(BigDecimal.ZERO,BigDecimal::add);
            wmsOutDeliveryOrderDto.setTotalDispatchQty(dispatchQty);
            //修改单据状态
            this.updateOrderStatus(wmsOutDeliveryOrderDto);
        }

        return wmsOutDeliveryOrderDtos;
    }

    /**
     * 查询调拨出库单
     * @param map
     * @return
     */
    @Override
    public List<WmsOutTransferDeliveryOrderDto> transferFindList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        map.put("orderTypeId",2);
        map.put("orgId",user.getOrganizationId());
        List<WmsOutDeliveryOrderDto> wmsOutDeliveryOrderDtos = wmsOutDeliveryOrderMapper.findList(map);

        List<WmsOutTransferDeliveryOrderDto> wmsOutTransferDeliveryOrderDtos = new ArrayList<>();
        for (WmsOutDeliveryOrderDto wmsOutDeliveryOrderDto : wmsOutDeliveryOrderDtos) {
            SearchWmsOutDeliveryOrderDet searchWmsOutDeliveryOrderDet = new SearchWmsOutDeliveryOrderDet();
            searchWmsOutDeliveryOrderDet.setDeliveryOrderId(wmsOutDeliveryOrderDto.getDeliveryOrderId());
            List<WmsOutDeliveryOrderDetDto> list = wmsOutDeliveryOrderDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutDeliveryOrderDet));
            wmsOutDeliveryOrderDto.setWmsOutDeliveryOrderDetList(list);
            //计算总数量
            BigDecimal packingSum = list.stream().map(WmsOutDeliveryOrderDetDto::getPackingQty).reduce(BigDecimal.ZERO,BigDecimal::add);
            wmsOutDeliveryOrderDto.setTotalPackingQty(packingSum);
            BigDecimal pickingSum = list.stream().map(WmsOutDeliveryOrderDetDto::getPickingQty).filter(e->e!=null).reduce(BigDecimal.ZERO,BigDecimal::add);
            wmsOutDeliveryOrderDto.setTotalPickingQty(pickingSum);
            BigDecimal dispatchSum = list.stream().map(WmsOutDeliveryOrderDetDto::getDispatchQty).filter(e->e!=null).reduce(BigDecimal.ZERO,BigDecimal::add);
            //修改单据状态
            this.updateOrderStatus(wmsOutDeliveryOrderDto);

            //赋值
            WmsOutTransferDeliveryOrderDto wmsOutTransferDeliveryOrderDto = new WmsOutTransferDeliveryOrderDto();
            BeanUtils.copyProperties(wmsOutDeliveryOrderDto,wmsOutTransferDeliveryOrderDto);
            wmsOutTransferDeliveryOrderDto.setTotalDispatchQty(dispatchSum);
            wmsOutTransferDeliveryOrderDtos.add(wmsOutTransferDeliveryOrderDto);
        }

        return wmsOutTransferDeliveryOrderDtos;
    }

    @Override
    public List<WmsOutHtDeliveryOrder> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        map.put("orgId",user.getOrganizationId());
        List<WmsOutHtDeliveryOrder> wmsOutHtDeliveryOrders = wmsOutHtDeliveryOrderMapper.findHtList(map);

        for (WmsOutHtDeliveryOrder wmsOutHtDeliveryOrder : wmsOutHtDeliveryOrders) {
            SearchWmsOutDeliveryOrderDet searchWmsOutDeliveryOrderDet = new SearchWmsOutDeliveryOrderDet();
            searchWmsOutDeliveryOrderDet.setDeliveryOrderId(wmsOutHtDeliveryOrder.getDeliveryOrderId());
            List<WmsOutHtDeliveryOrderDet> list = wmsOutHtDeliveryOrderDetMapper.findHtList(ControllerUtil.dynamicConditionByEntity(searchWmsOutDeliveryOrderDet));
            wmsOutHtDeliveryOrder.setWmsOutHtDeliveryOrderDets(list);
            //计算总数量
            BigDecimal packingSum = list.stream().map(WmsOutHtDeliveryOrderDet::getPackingQty).reduce(BigDecimal.ZERO,BigDecimal::add);
            wmsOutHtDeliveryOrder.setTotalPackingQty(packingSum);
            BigDecimal pickingSum = list.stream().map(WmsOutHtDeliveryOrderDet::getPickingQty).filter(e->e!=null).reduce(BigDecimal.ZERO,BigDecimal::add);
            wmsOutHtDeliveryOrder.setTotalPickingQty(pickingSum);
        }

        return wmsOutHtDeliveryOrders;
    }

    @Override
    public List<WmsOutTransferDeliveryOrderDto> transferFindHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        map.put("orderTypeId",2);
        map.put("orgId",user.getOrganizationId());
        List<WmsOutHtDeliveryOrder> wmsOutHtDeliveryOrders = wmsOutHtDeliveryOrderMapper.findHtList(map);

        List<WmsOutTransferDeliveryOrderDto> wmsOutTransferDeliveryOrderDtos = new ArrayList<>();
        for (WmsOutHtDeliveryOrder wmsOutHtDeliveryOrder : wmsOutHtDeliveryOrders) {
            SearchWmsOutDeliveryOrderDet searchWmsOutDeliveryOrderDet = new SearchWmsOutDeliveryOrderDet();
            searchWmsOutDeliveryOrderDet.setDeliveryOrderId(wmsOutHtDeliveryOrder.getDeliveryOrderId());
            List<WmsOutHtDeliveryOrderDet> list = wmsOutHtDeliveryOrderDetMapper.findHtList(ControllerUtil.dynamicConditionByEntity(searchWmsOutDeliveryOrderDet));
            wmsOutHtDeliveryOrder.setWmsOutHtDeliveryOrderDets(list);
            //计算总数量
            BigDecimal packingSum = list.stream().map(WmsOutHtDeliveryOrderDet::getPackingQty).reduce(BigDecimal.ZERO,BigDecimal::add);
            wmsOutHtDeliveryOrder.setTotalPackingQty(packingSum);
            BigDecimal pickingSum = list.stream().map(WmsOutHtDeliveryOrderDet::getPickingQty).filter(e->e!=null).reduce(BigDecimal.ZERO,BigDecimal::add);
            BigDecimal dispatchSum = list.stream().map(WmsOutHtDeliveryOrderDet::getDispatchQty).filter(e->e!=null).reduce(BigDecimal.ZERO,BigDecimal::add);
            wmsOutHtDeliveryOrder.setTotalPickingQty(pickingSum);

            //赋值
            WmsOutTransferDeliveryOrderDto wmsOutTransferDeliveryOrderDto = new WmsOutTransferDeliveryOrderDto();
            BeanUtils.copyProperties(wmsOutHtDeliveryOrder,wmsOutTransferDeliveryOrderDto);
            wmsOutTransferDeliveryOrderDto.setTotalDispatchQty(dispatchSum);
            wmsOutTransferDeliveryOrderDtos.add(wmsOutTransferDeliveryOrderDto);
        }

        return wmsOutTransferDeliveryOrderDtos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(WmsOutDeliveryOrder wmsOutDeliveryOrder) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //出库单
        if(wmsOutDeliveryOrder.getOrderTypeId()==1){
            wmsOutDeliveryOrder.setDeliveryOrderCode(CodeUtils.getId("XSCK-"));
        }else if(wmsOutDeliveryOrder.getOrderTypeId()==2){
            wmsOutDeliveryOrder.setDeliveryOrderCode(CodeUtils.getId("DBCK-"));
        }else if(wmsOutDeliveryOrder.getOrderTypeId()==7){
            wmsOutDeliveryOrder.setDeliveryOrderCode(CodeUtils.getId("OTCK-"));
        }else if(wmsOutDeliveryOrder.getOrderTypeId()==8){
            wmsOutDeliveryOrder.setDeliveryOrderCode(CodeUtils.getId("LLCK-"));
        }
        wmsOutDeliveryOrder.setAuditStatus((byte) 0);
        wmsOutDeliveryOrder.setCreateTime(new Date());
        wmsOutDeliveryOrder.setCreateUserId(user.getUserId());
        wmsOutDeliveryOrder.setModifiedTime(new Date());
        wmsOutDeliveryOrder.setModifiedUserId(user.getUserId());
        wmsOutDeliveryOrder.setOrgId(user.getOrganizationId());
        wmsOutDeliveryOrder.setOrderStatus((byte)1);
        int i = wmsOutDeliveryOrderMapper.insertUseGeneratedKeys(wmsOutDeliveryOrder);

        //出库单履历
        WmsOutHtDeliveryOrder wmsOutHtDeliveryOrder = new WmsOutHtDeliveryOrder();
        BeanUtils.copyProperties(wmsOutDeliveryOrder,wmsOutHtDeliveryOrder);
        wmsOutHtDeliveryOrderMapper.insertSelective(wmsOutHtDeliveryOrder);

        //出库单明细
        List<WmsOutHtDeliveryOrderDet> wmsOutHtDeliveryOrderDetList = new ArrayList<>();
        List<WmsOutDeliveryOrderDetDto> wmsOutDeliveryOrderDetList = wmsOutDeliveryOrder.getWmsOutDeliveryOrderDetList();
        if(StringUtils.isNotEmpty(wmsOutDeliveryOrderDetList)){
            for (WmsOutDeliveryOrderDet wmsOutDeliveryOrderDet : wmsOutDeliveryOrderDetList ) {
                //领料出库单查询库存
//                if(wmsOutDeliveryOrder.getOrderTypeId()==8){
//                    this.findInventory(wmsOutDeliveryOrderDet);
//                }
                wmsOutDeliveryOrderDet.setDeliveryOrderId(wmsOutDeliveryOrder.getDeliveryOrderId());
                wmsOutDeliveryOrderDet.setCreateTime(new Date());
                wmsOutDeliveryOrderDet.setCreateUserId(user.getUserId());
                wmsOutDeliveryOrderDet.setModifiedTime(new Date());
                wmsOutDeliveryOrderDet.setModifiedUserId(user.getUserId());
                wmsOutDeliveryOrderDet.setOrgId(user.getOrganizationId());
                //出库单明细履历
                WmsOutHtDeliveryOrderDet wmsOutHtDeliveryOrderDet = new WmsOutHtDeliveryOrderDet();
                BeanUtils.copyProperties(wmsOutDeliveryOrderDet,wmsOutHtDeliveryOrderDet);
                wmsOutHtDeliveryOrderDetList.add(wmsOutHtDeliveryOrderDet);
            }
            wmsOutDeliveryOrderDetMapper.insertList(wmsOutDeliveryOrderDetList);
            wmsOutHtDeliveryOrderDetMapper.insertList(wmsOutHtDeliveryOrderDetList);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(WmsOutDeliveryOrder wmsOutDeliveryOrder) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //查询是否创建作业单
        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setSourceOrderId(wmsOutDeliveryOrder.getDeliveryOrderId());
        searchWmsInnerJobOrder.setOrderTypeId(wmsOutDeliveryOrder.getOrderTypeId());
        List<WmsInnerJobOrderDto> wmsInnerJobOrderDtos = innerFeignApi.findList(searchWmsInnerJobOrder).getData();
        if(StringUtils.isNotEmpty(wmsInnerJobOrderDtos)){
            throw new BizErrorException("对应的拣货作业单已存在,该出库单不允许修改！");
        }

        //出库单
        wmsOutDeliveryOrder.setModifiedTime(new Date());
        wmsOutDeliveryOrder.setModifiedUserId(user.getUserId());
        wmsOutDeliveryOrder.setOrgId(user.getOrganizationId());
        int i = wmsOutDeliveryOrderMapper.updateByPrimaryKeySelective(wmsOutDeliveryOrder);

        //出库单履历
        WmsOutHtDeliveryOrder wmsOutHtDeliveryOrder = new WmsOutHtDeliveryOrder();
        BeanUtils.copyProperties(wmsOutDeliveryOrder,wmsOutHtDeliveryOrder);
        wmsOutHtDeliveryOrderMapper.insertSelective(wmsOutHtDeliveryOrder);

        //出库单明细
        List<WmsOutHtDeliveryOrderDet> wmsOutHtDeliveryOrderDetList = new ArrayList<>();

        Example example = new Example(WmsOutDeliveryOrder.class);
        example.createCriteria().andEqualTo("deliveryOrderId", wmsOutDeliveryOrder.getDeliveryOrderId());
        wmsOutDeliveryOrderDetMapper.deleteByExample(example);

        List<WmsOutDeliveryOrderDetDto> wmsOutDeliveryOrderDetList = wmsOutDeliveryOrder.getWmsOutDeliveryOrderDetList();
        if(StringUtils.isNotEmpty(wmsOutDeliveryOrderDetList)){
            for (WmsOutDeliveryOrderDet wmsOutDeliveryOrderDet : wmsOutDeliveryOrderDetList ) {
//                //领料出库单查询库存
//                if(wmsOutDeliveryOrder.getOrderTypeId()==8){
//                    this.findInventory(wmsOutDeliveryOrderDet);
//                }
                wmsOutDeliveryOrderDet.setModifiedTime(new Date());
                wmsOutDeliveryOrderDet.setModifiedUserId(user.getUserId());
                wmsOutDeliveryOrderDet.setOrgId(user.getOrganizationId());

                //出库单明细履历
                WmsOutHtDeliveryOrderDet wmsOutHtDeliveryOrderDet = new WmsOutHtDeliveryOrderDet();
                BeanUtils.copyProperties(wmsOutDeliveryOrderDet,wmsOutHtDeliveryOrderDet);
                wmsOutHtDeliveryOrderDetList.add(wmsOutHtDeliveryOrderDet);
            }
            wmsOutDeliveryOrderDetMapper.insertList(wmsOutDeliveryOrderDetList);
            wmsOutHtDeliveryOrderDetMapper.insertList(wmsOutHtDeliveryOrderDetList);
        }

        return i;
    }

    /**
     * 领料拣货单匹配库存
     * @param wmsOutDeliveryOrderDet
     */
    private void findInventory(WmsOutDeliveryOrderDet wmsOutDeliveryOrderDet){
        //查询库存
        SearchWmsInnerInventory searchWmsInnerInventory = new SearchWmsInnerInventory();
        searchWmsInnerInventory.setWarehouseId(wmsOutDeliveryOrderDet.getWarehouseId());
        searchWmsInnerInventory.setStorageId(wmsOutDeliveryOrderDet.getStorageId());
        searchWmsInnerInventory.setMaterialId(wmsOutDeliveryOrderDet.getMaterialId());
        searchWmsInnerInventory.setBatchCode(wmsOutDeliveryOrderDet.getBatchCode());
        searchWmsInnerInventory.setJobStatus((byte)1);
        ResponseEntity<List<WmsInnerInventoryDto>> responseEntity = innerFeignApi.findList(searchWmsInnerInventory);
        if(responseEntity.getCode()!=0){
            throw new BizErrorException(ErrorCodeEnum.GL9999404);
        }
        List<WmsInnerInventoryDto> wmsInnerInventoryDtos = responseEntity.getData();
        if(StringUtils.isEmpty(wmsInnerInventoryDtos)){
            throw new BizErrorException("无库存！");
        }
       if(wmsInnerInventoryDtos.stream().map(WmsInnerInventory::getPackingQty).reduce(BigDecimal.ZERO,BigDecimal::add).compareTo(wmsOutDeliveryOrderDet.getPackingQty())==-1){
           throw new BizErrorException("库存不足！");
       }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idsArr  = ids.split(",");

        for (String id:idsArr) {
            WmsOutDeliveryOrder wmsOutDeliveryOrder = wmsOutDeliveryOrderMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(wmsOutDeliveryOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //出库单履历
            WmsOutHtDeliveryOrder wmsOutHtDeliveryOrder = new WmsOutHtDeliveryOrder();
            BeanUtils.copyProperties(wmsOutDeliveryOrder,wmsOutHtDeliveryOrder);
            wmsOutHtDeliveryOrderMapper.insertSelective(wmsOutHtDeliveryOrder);

            //删除相应的出库单明细
            Example example = new Example(WmsOutDeliveryOrderDet.class);
            example.createCriteria().andEqualTo("deliveryOrderId", id);
            wmsOutDeliveryOrderDetMapper.deleteByExample(example);
        }

        return wmsOutDeliveryOrderMapper.deleteByIds(ids);
    }

    public int updateOrderStatus(WmsOutDeliveryOrderDto wmsOutDeliveryOrderDto){
        Byte orderStatus = wmsOutDeliveryOrderDto.getOrderStatus();
        if(wmsOutDeliveryOrderDto.getOrderStatus()==4 || wmsOutDeliveryOrderDto.getOrderStatus()==5){
            return 1;
        }
        if(wmsOutDeliveryOrderDto.getTotalPickingQty().compareTo(new BigDecimal("0")) == 0){
            wmsOutDeliveryOrderDto.setOrderStatus((byte)1);
        }else if(wmsOutDeliveryOrderDto.getTotalPickingQty().compareTo(new BigDecimal("0")) == 1
                && wmsOutDeliveryOrderDto.getTotalPickingQty().compareTo(wmsOutDeliveryOrderDto.getTotalPackingQty()) == -1){
            wmsOutDeliveryOrderDto.setOrderStatus((byte)2);
        }else if(wmsOutDeliveryOrderDto.getTotalPickingQty().compareTo(wmsOutDeliveryOrderDto.getTotalPackingQty()) == 0
                || wmsOutDeliveryOrderDto.getTotalPickingQty().compareTo(wmsOutDeliveryOrderDto.getTotalPackingQty()) == 1){
            wmsOutDeliveryOrderDto.setOrderStatus((byte)3);
        }

        int i = 0;
        if(!wmsOutDeliveryOrderDto.getOrderStatus().equals(orderStatus)){
            i = wmsOutDeliveryOrderMapper.updateByPrimaryKeySelective(wmsOutDeliveryOrderDto);
        }

        return i;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createJobOrder(Long id) {
        Map<String, Object> map = new HashMap<>();
        map.put("deliveryOrderId", id);
        List<WmsOutDeliveryOrderDto> list = wmsOutDeliveryOrderMapper.findList(map);
        if (StringUtils.isEmpty(list)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003,"无数据");
        }
        WmsOutDeliveryOrderDto wmsOutDeliveryOrderDto = list.get(0);

        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("wmsOutDelivery");
        List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        if(!specItems.isEmpty() && "wb".equals(specItems.get(0).getParaValue())){
            if(wmsOutDeliveryOrderDto.getOrderTypeId().equals(1L) && (wmsOutDeliveryOrderDto.getAuditStatus() == null || wmsOutDeliveryOrderDto.getAuditStatus() != (byte) 1)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011023, "此销售出库单尚未审核，不允许创建作业单！");
            }
        }

        SearchWmsOutDeliveryOrderDet searchWmsOutDeliveryOrderDet = new SearchWmsOutDeliveryOrderDet();
        searchWmsOutDeliveryOrderDet.setDeliveryOrderId(id);
        List<WmsOutDeliveryOrderDetDto> wmsOutDeliveryOrderDetList = wmsOutDeliveryOrderDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutDeliveryOrderDet));
        if (StringUtils.isEmpty(wmsOutDeliveryOrderDetList)) {
            throw new BizErrorException("出库单明细为空时无法创建作业单");
        }

        //查询是否创建作业单
        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setRelatedOrderCode(wmsOutDeliveryOrderDto.getDeliveryOrderCode());
        searchWmsInnerJobOrder.setCodeQueryMark(1);
        List<WmsInnerJobOrderDto> wmsInnerJobOrderDtos = innerFeignApi.findList(searchWmsInnerJobOrder).getData();
        //未创建过作业单则新建
        if (StringUtils.isEmpty(wmsInnerJobOrderDtos)) {
            Map<Long, List<WmsOutDeliveryOrderDetDto>> listMap = new HashMap<>();
            //根据仓库id分组(一个仓库对应一个作业单)
            for (WmsOutDeliveryOrderDetDto wmsOutDeliveryOrderDetDto : wmsOutDeliveryOrderDetList) {
                if (listMap.containsKey(wmsOutDeliveryOrderDetDto.getWarehouseId())) {
                    listMap.get(wmsOutDeliveryOrderDetDto.getWarehouseId()).add(wmsOutDeliveryOrderDetDto);
                } else {
                    List<WmsOutDeliveryOrderDetDto> wmsOutDeliveryOrderDetDtos = new ArrayList<>();
                    wmsOutDeliveryOrderDetDtos.add(wmsOutDeliveryOrderDetDto);
                    listMap.put(wmsOutDeliveryOrderDetDto.getWarehouseId(), wmsOutDeliveryOrderDetDtos);
                }
            }

            //遍历map
            for (List<WmsOutDeliveryOrderDetDto> dtoList : listMap.values()) {
                WmsInnerJobOrder wmsInnerJobOrder = new WmsInnerJobOrder();
                wmsInnerJobOrder.setSourceOrderId(wmsOutDeliveryOrderDto.getDeliveryOrderId());
                wmsInnerJobOrder.setMaterialOwnerId(wmsOutDeliveryOrderDto.getMaterialOwnerId());
                wmsInnerJobOrder.setOrderTypeId(wmsOutDeliveryOrderDto.getOrderTypeId());
                wmsInnerJobOrder.setRelatedOrderCode(wmsOutDeliveryOrderDto.getDeliveryOrderCode());
                wmsInnerJobOrder.setJobOrderType((byte) 4);
                wmsInnerJobOrder.setOrderStatus((byte) 1);
                wmsInnerJobOrder.setWarehouseId(dtoList.get(0).getWarehouseId());
                //计算总数量
                BigDecimal packingSum = dtoList.stream().map(WmsOutDeliveryOrderDet::getPackingQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                wmsInnerJobOrder.setPlanQty(packingSum);

                if(wmsInnerJobOrder.getOrderTypeId()==8){
                    if(dtoList.stream().filter(li->StringUtils.isEmpty(li.getPickingStorageId())).collect(Collectors.toList()).size()>0 && dtoList.stream().filter(li->StringUtils.isNotEmpty(li.getPickingStorageId())).collect(Collectors.toList()).size()>0){
                        throw new BizErrorException("维护所有的拣货库位");
                    }
                }

                //作业单明细
                List<WmsInnerJobOrderDet> wmsInnerJobOrderDets = new ArrayList<>();
                for (WmsOutDeliveryOrderDetDto dto : dtoList) {
                    WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
                    if(wmsInnerJobOrder.getOrderTypeId()==8 && StringUtils.isNotEmpty(dto.getPickingStorageId())){
                        wmsInnerJobOrder.setType(1);
                        wmsInnerJobOrderDet.setOutStorageId(dto.getPickingStorageId());
                        wmsInnerJobOrderDet.setDistributionQty(dto.getPackingQty());
                    }else {
                        wmsInnerJobOrder.setType(0);
                    }
                    wmsInnerJobOrderDet.setSourceDetId(dto.getDeliveryOrderDetId());
                    wmsInnerJobOrderDet.setInStorageId(dto.getStorageId());
                    wmsInnerJobOrderDet.setInventoryStatusId(dto.getInventoryStatusId());
                    wmsInnerJobOrderDet.setMaterialId(dto.getMaterialId());
                    wmsInnerJobOrderDet.setPackingUnitName(dto.getPackingUnitName());
                    wmsInnerJobOrderDet.setPlanQty(dto.getPackingQty());
                    wmsInnerJobOrderDet.setOrderStatus((byte) 1);
                    wmsInnerJobOrderDet.setBatchCode(dto.getBatchCode());
                    wmsInnerJobOrderDets.add(wmsInnerJobOrderDet);
                }
                wmsInnerJobOrder.setWmsInPutawayOrderDets(wmsInnerJobOrderDets);

                //创建作业单
                ResponseEntity responseEntity = innerFeignApi.add(wmsInnerJobOrder);
                if (responseEntity.getCode() != 0) {
                    throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
                }
            }
        } else {
            throw new BizErrorException("拣货作业单已存在");
        }
        return 1;
    }

    /**
     * 发运反写出库单状态
     * @param deliverOrderId
     * @param orderStatus
     * @return
     */
    @Override
    public int forwardingStatus(Long deliverOrderId, Byte orderStatus) {
        SysUser sysUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(sysUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        WmsOutDeliveryOrder wms = wmsOutDeliveryOrderMapper.selectByPrimaryKey(deliverOrderId);
        if(StringUtils.isEmpty(wms)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012000,deliverOrderId);
        }
        wms.setOrderStatus(orderStatus);
        wms.setModifiedTime(new Date());
        wms.setModifiedUserId(sysUser.getUserId());
        return wmsOutDeliveryOrderMapper.updateByPrimaryKeySelective(wms);
    }

    @Override
    public int updateStatus(List<Long> ids) {
        return wmsOutDeliveryOrderMapper.batchUpdateStatus(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveByApi(WmsOutDeliveryOrder wmsOutDeliveryOrder) {
        Example example = new Example(WmsOutDeliveryOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("deliveryOrderCode",wmsOutDeliveryOrder.getDeliveryOrderCode());
        criteria.andEqualTo("orgId",wmsOutDeliveryOrder.getOrgId());
        WmsOutDeliveryOrder outDeliveryOrder = wmsOutDeliveryOrderMapper.selectOneByExample(example);
        int i= 0;
        if(StringUtils.isEmpty(outDeliveryOrder)) {
            wmsOutDeliveryOrder.setCreateTime(new Date());
            wmsOutDeliveryOrder.setCreateUserId((long) 1);
            wmsOutDeliveryOrder.setModifiedUserId((long) 1);
            wmsOutDeliveryOrder.setModifiedTime(new Date());
            wmsOutDeliveryOrder.setIsDelete((byte) 1);
            i = wmsOutDeliveryOrderMapper.insertUseGeneratedKeys(wmsOutDeliveryOrder);

            //明细
            List<WmsOutDeliveryOrderDetDto> wmsOutDeliveryOrderDetList = wmsOutDeliveryOrder.getWmsOutDeliveryOrderDetList();
            if(StringUtils.isNotEmpty(wmsOutDeliveryOrderDetList)){
                for (WmsOutDeliveryOrderDet wmsOutDeliveryOrderDet : wmsOutDeliveryOrderDetList ) {
                    wmsOutDeliveryOrderDet.setDeliveryOrderId(wmsOutDeliveryOrder.getDeliveryOrderId());
                    wmsOutDeliveryOrderDet.setCreateTime(new Date());
                    wmsOutDeliveryOrderDet.setCreateUserId(1L);
                }
                wmsOutDeliveryOrderDetMapper.insertList(wmsOutDeliveryOrderDetList);
            }

        }else{
            wmsOutDeliveryOrder.setDeliveryOrderId(outDeliveryOrder.getDeliveryOrderId());
            wmsOutDeliveryOrder.setModifiedTime(new Date());
            wmsOutDeliveryOrderMapper.updateByPrimaryKeySelective(wmsOutDeliveryOrder);

            //明细
            List<WmsOutDeliveryOrderDetDto> wmsOutDeliveryOrderDetList = wmsOutDeliveryOrder.getWmsOutDeliveryOrderDetList();
            if(StringUtils.isNotEmpty(wmsOutDeliveryOrderDetList)){
                for (WmsOutDeliveryOrderDet wmsOutDeliveryOrderDet : wmsOutDeliveryOrderDetList ) {
                    //IDGUID判断是否重复
                    Example exampleDet = new Example(WmsOutDeliveryOrderDet.class);
                    Example.Criteria criteriaDet = exampleDet.createCriteria();
                    criteriaDet.andEqualTo("option1",wmsOutDeliveryOrderDet.getOption1());
                    WmsOutDeliveryOrderDet outDeliveryOrderDet = wmsOutDeliveryOrderDetMapper.selectOneByExample(exampleDet);
                    if(StringUtils.isEmpty(outDeliveryOrderDet)){
                        wmsOutDeliveryOrderDet.setCreateUserId(1L);
                        wmsOutDeliveryOrderDet.setCreateTime(new Date());
                        wmsOutDeliveryOrderDetMapper.insertSelective(wmsOutDeliveryOrderDet);
                    }
                    else{
                        wmsOutDeliveryOrderDet.setDeliveryOrderDetId(outDeliveryOrderDet.getDeliveryOrderDetId());
                        wmsOutDeliveryOrderDet.setModifiedTime(new Date());
                        wmsOutDeliveryOrderDetMapper.updateByPrimaryKeySelective(wmsOutDeliveryOrderDet);
                    }

                }
            }
        }
        return i;
    }
}
