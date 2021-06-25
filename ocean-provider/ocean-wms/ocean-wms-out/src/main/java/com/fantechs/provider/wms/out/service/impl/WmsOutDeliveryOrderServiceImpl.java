package com.fantechs.provider.wms.out.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutTransferDeliveryOrderDto;
import com.fantechs.common.base.general.entity.om.OmSalesOrderDet;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
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
import com.fantechs.provider.api.qms.OMFeignApi;
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
            BigDecimal packingSum = list.stream().map(WmsOutDeliveryOrderDetDto::getPackingQty).reduce(BigDecimal.ZERO,BigDecimal::add);
            wmsOutDeliveryOrderDto.setTotalPackingQty(packingSum);
            BigDecimal pickingSum = list.stream().map(WmsOutDeliveryOrderDetDto::getPickingQty).filter(e->e!=null).reduce(BigDecimal.ZERO,BigDecimal::add);
            wmsOutDeliveryOrderDto.setTotalPickingQty(pickingSum);
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

        map.put("orderTypeId",1);
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
        if(StringUtils.isEmpty(wmsOutDeliveryOrder.getDeliveryOrderCode())){
            wmsOutDeliveryOrder.setDeliveryOrderCode(CodeUtils.getId("XSCK-"));
        }
        wmsOutDeliveryOrder.setCreateTime(new Date());
        wmsOutDeliveryOrder.setCreateUserId(user.getUserId());
        wmsOutDeliveryOrder.setModifiedTime(new Date());
        wmsOutDeliveryOrder.setModifiedUserId(user.getUserId());
        wmsOutDeliveryOrder.setOrgId(user.getOrganizationId());
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
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        WmsOutDeliveryOrderDto wmsOutDeliveryOrderDto = list.get(0);

        SearchWmsOutDeliveryOrderDet searchWmsOutDeliveryOrderDet = new SearchWmsOutDeliveryOrderDet();
        searchWmsOutDeliveryOrderDet.setDeliveryOrderId(id);
        List<WmsOutDeliveryOrderDetDto> wmsOutDeliveryOrderDetList = wmsOutDeliveryOrderDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutDeliveryOrderDet));
        if (StringUtils.isEmpty(wmsOutDeliveryOrderDetList)) {
            throw new BizErrorException("出库单明细为空时无法创建作业单");
        }

        //查询是否创建作业单
        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setSourceOrderId(id);
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

                //作业单明细
                List<WmsInnerJobOrderDet> wmsInnerJobOrderDets = new ArrayList<>();
                for (WmsOutDeliveryOrderDetDto dto : dtoList) {
                    WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
                    wmsInnerJobOrderDet.setSourceDetId(dto.getDeliveryOrderDetId());
                    wmsInnerJobOrderDet.setInStorageId(dto.getStorageId());
                    wmsInnerJobOrderDet.setInventoryStatusId(dto.getInventoryStatusId());
                    wmsInnerJobOrderDet.setMaterialId(dto.getMaterialId());
                    wmsInnerJobOrderDet.setPackingUnitName(dto.getPackingUnitName());
                    wmsInnerJobOrderDet.setPlanQty(dto.getPackingQty());
                    wmsInnerJobOrderDet.setOrderStatus((byte) 1);
                    wmsInnerJobOrderDets.add(wmsInnerJobOrderDet);
                }
                wmsInnerJobOrder.setWmsInPutawayOrderDets(wmsInnerJobOrderDets);

                //创建作业单
                ResponseEntity responseEntity = innerFeignApi.add(wmsInnerJobOrder);
                if (responseEntity.getCode() != 0) {
                    throw new BizErrorException("创建拣货作业单失败");
                }
            }
        } else {
            throw new BizErrorException("拣货作业单已存在");
        }
        return 1;
    }
}
