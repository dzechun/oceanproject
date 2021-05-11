package com.fantechs.provider.wms.out.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrder;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrderDet;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtDeliveryOrder;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtDeliveryOrderDet;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutDeliveryOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
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

    @Override
    public List<WmsOutHtDeliveryOrder> findHtList(Map<String, Object> map) {
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
    @Transactional(rollbackFor = Exception.class)
    public int save(WmsOutDeliveryOrder wmsOutDeliveryOrder) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //出库单
        wmsOutDeliveryOrder.setDeliveryOrderCode(CodeUtils.getId("XSCK-"));
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
        WmsOutDeliveryOrderDto wmsOutDeliveryOrder = new WmsOutDeliveryOrderDto();

        if(wmsOutDeliveryOrderDto.getTotalPickingQty().compareTo(new BigDecimal("0")) == 0){
            wmsOutDeliveryOrder.setOrderStatus((byte)1);
        }else if(wmsOutDeliveryOrderDto.getTotalPickingQty().compareTo(new BigDecimal("0")) == 1
                && wmsOutDeliveryOrderDto.getTotalPickingQty().compareTo(wmsOutDeliveryOrderDto.getTotalPackingQty()) == -1){
            wmsOutDeliveryOrder.setOrderStatus((byte)2);
        }else if(wmsOutDeliveryOrderDto.getTotalPickingQty().compareTo(wmsOutDeliveryOrderDto.getTotalPackingQty()) == 0
                || wmsOutDeliveryOrderDto.getTotalPickingQty().compareTo(wmsOutDeliveryOrderDto.getTotalPackingQty()) == 1){
            wmsOutDeliveryOrder.setOrderStatus((byte)3);
        }

        wmsOutDeliveryOrder.setDeliveryOrderId(wmsOutDeliveryOrderDto.getDeliveryOrderId());
        return wmsOutDeliveryOrderMapper.updateByPrimaryKeySelective(wmsOutDeliveryOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createJobOrder(Long id) {
        Map<String, Object> map = new HashMap<>();
        map.put("deliveryOrderId",id);
        List<WmsOutDeliveryOrderDto> list = this.findList(map);
        if(StringUtils.isNotEmpty(list)) {
            WmsOutDeliveryOrderDto wmsOutDeliveryOrderDto = list.get(0);
            List<WmsOutDeliveryOrderDetDto> wmsOutDeliveryOrderDetList = wmsOutDeliveryOrderDto.getWmsOutDeliveryOrderDetList();
            for (WmsOutDeliveryOrderDetDto wmsOutDeliveryOrderDetDto : wmsOutDeliveryOrderDetList) {
                WmsInnerJobOrder wmsInnerJobOrder = new WmsInnerJobOrder();
                wmsInnerJobOrder.setMaterialOwnerId(wmsOutDeliveryOrderDto.getMaterialOwnerId());
                wmsInnerJobOrder.setOrderTypeId(wmsOutDeliveryOrderDto.getOrderTypeId());
                wmsInnerJobOrder.setRelatedOrderCode(wmsOutDeliveryOrderDto.getDeliveryOrderCode());
                wmsInnerJobOrder.setWarehouseId(wmsOutDeliveryOrderDetDto.getWarehouseId());
                wmsInnerJobOrder.setPlanQty(wmsOutDeliveryOrderDetDto.getPackingQty());
                innerFeignApi.add(wmsInnerJobOrder);
            }
        }
        return 1;
    }
}
