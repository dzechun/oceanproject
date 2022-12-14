package com.fantechs.provider.wms.out.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseMaterialOwnerDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutTransferDeliveryOrderDto;
import com.fantechs.common.base.general.dto.wms.out.imports.WmsOutDeliveryOrderImport;
import com.fantechs.common.base.general.dto.wms.out.imports.WmsSamsungOutDeliveryOrderImport;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.BaseSupplier;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.search.*;
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
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.guest.fivering.FiveringFeignApi;
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
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private FiveringFeignApi fiveringFeignApi;

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
            //???????????????
            BigDecimal packingSum = list.stream().map(WmsOutDeliveryOrderDetDto::getPackingQty).filter(e->e!=null).reduce(BigDecimal.ZERO,BigDecimal::add);
            wmsOutDeliveryOrderDto.setTotalPackingQty(packingSum);
            BigDecimal pickingSum = list.stream().map(WmsOutDeliveryOrderDetDto::getPickingQty).filter(e->e!=null).reduce(BigDecimal.ZERO,BigDecimal::add);
            wmsOutDeliveryOrderDto.setTotalPickingQty(pickingSum);
            BigDecimal dispatchQty = list.stream().map(WmsOutDeliveryOrderDetDto::getDispatchQty).filter(e->e!=null).reduce(BigDecimal.ZERO,BigDecimal::add);
            wmsOutDeliveryOrderDto.setTotalDispatchQty(dispatchQty);
            //??????????????????
            this.updateOrderStatus(wmsOutDeliveryOrderDto);
        }

        return wmsOutDeliveryOrderDtos;
    }

    /**
     * ?????????????????????
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
            //???????????????
            BigDecimal packingSum = list.stream().map(WmsOutDeliveryOrderDetDto::getPackingQty).reduce(BigDecimal.ZERO,BigDecimal::add);
            wmsOutDeliveryOrderDto.setTotalPackingQty(packingSum);
            BigDecimal pickingSum = list.stream().map(WmsOutDeliveryOrderDetDto::getPickingQty).filter(e->e!=null).reduce(BigDecimal.ZERO,BigDecimal::add);
            wmsOutDeliveryOrderDto.setTotalPickingQty(pickingSum);
            BigDecimal dispatchSum = list.stream().map(WmsOutDeliveryOrderDetDto::getDispatchQty).filter(e->e!=null).reduce(BigDecimal.ZERO,BigDecimal::add);
            //??????????????????
            this.updateOrderStatus(wmsOutDeliveryOrderDto);

            //??????
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
            //???????????????
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
            //???????????????
            BigDecimal packingSum = list.stream().map(WmsOutHtDeliveryOrderDet::getPackingQty).reduce(BigDecimal.ZERO,BigDecimal::add);
            wmsOutHtDeliveryOrder.setTotalPackingQty(packingSum);
            BigDecimal pickingSum = list.stream().map(WmsOutHtDeliveryOrderDet::getPickingQty).filter(e->e!=null).reduce(BigDecimal.ZERO,BigDecimal::add);
            BigDecimal dispatchSum = list.stream().map(WmsOutHtDeliveryOrderDet::getDispatchQty).filter(e->e!=null).reduce(BigDecimal.ZERO,BigDecimal::add);
            wmsOutHtDeliveryOrder.setTotalPickingQty(pickingSum);

            //??????
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

        Long warehouseId=null;
        SearchBaseWarehouse searchBaseWarehouse=new SearchBaseWarehouse();
        searchBaseWarehouse.setOrgId(user.getOrganizationId());
        List<BaseWarehouse> warehouseList=baseFeignApi.findList(searchBaseWarehouse).getData();
        if(StringUtils.isNotEmpty(warehouseList) && warehouseList.size()>0){
            warehouseId=warehouseList.get(0).getWarehouseId();
        }

        //?????????
        if(wmsOutDeliveryOrder.getOrderTypeId()==1){
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("wanbaoSyncData");
            List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            if (specItems.isEmpty()){
                wmsOutDeliveryOrder.setDeliveryOrderCode(CodeUtils.getId("XSCK-"));
            }else {
                JSONObject jsonObject = JSON.parseObject(specItems.get(0).getParaValue());
                if(!jsonObject.get("enable").equals(1) || StringUtils.isEmpty(wmsOutDeliveryOrder.getDeliveryOrderCode())){
                    wmsOutDeliveryOrder.setDeliveryOrderCode(CodeUtils.getId("XSCK-"));
                }
            }
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

        //???????????????
        WmsOutHtDeliveryOrder wmsOutHtDeliveryOrder = new WmsOutHtDeliveryOrder();
        BeanUtils.copyProperties(wmsOutDeliveryOrder,wmsOutHtDeliveryOrder);
        wmsOutHtDeliveryOrderMapper.insertSelective(wmsOutHtDeliveryOrder);

        //???????????????
        List<WmsOutHtDeliveryOrderDet> wmsOutHtDeliveryOrderDetList = new ArrayList<>();
        List<WmsOutDeliveryOrderDetDto> wmsOutDeliveryOrderDetList = wmsOutDeliveryOrder.getWmsOutDeliveryOrderDetList();
        if(StringUtils.isNotEmpty(wmsOutDeliveryOrderDetList)){
            for (WmsOutDeliveryOrderDet wmsOutDeliveryOrderDet : wmsOutDeliveryOrderDetList ) {
                if(StringUtils.isEmpty(wmsOutDeliveryOrderDet.getPackingQty()) || wmsOutDeliveryOrderDet.getPackingQty().compareTo(BigDecimal.ZERO)<1){
                    throw new BizErrorException("????????????????????????0");
                }
                //???????????????????????????
//                if(wmsOutDeliveryOrder.getOrderTypeId()==8){
//                    this.findInventory(wmsOutDeliveryOrderDet);
//                }
                wmsOutDeliveryOrderDet.setDeliveryOrderId(wmsOutDeliveryOrder.getDeliveryOrderId());
                wmsOutDeliveryOrderDet.setCreateTime(new Date());
                wmsOutDeliveryOrderDet.setCreateUserId(user.getUserId());
                wmsOutDeliveryOrderDet.setModifiedTime(new Date());
                wmsOutDeliveryOrderDet.setModifiedUserId(user.getUserId());
                wmsOutDeliveryOrderDet.setOrgId(user.getOrganizationId());
                //?????????????????????????????? 2022-03-16
                wmsOutDeliveryOrderDet.setSalesCode(wmsOutDeliveryOrderDet.getSalesCode());

                if(StringUtils.isEmpty(wmsOutDeliveryOrderDet.getWarehouseId())){
                    wmsOutDeliveryOrderDet.setWarehouseId(warehouseId);
                }
                //?????????????????????
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

        //???????????????????????????
        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setSourceOrderId(wmsOutDeliveryOrder.getDeliveryOrderId());
        searchWmsInnerJobOrder.setOrderTypeId(wmsOutDeliveryOrder.getOrderTypeId());
        List<WmsInnerJobOrderDto> wmsInnerJobOrderDtos = innerFeignApi.findList(searchWmsInnerJobOrder).getData();
        if(StringUtils.isNotEmpty(wmsInnerJobOrderDtos)){
            throw new BizErrorException("?????????????????????????????????,??????????????????????????????");
        }

        Long warehouseId=null;
        SearchBaseWarehouse searchBaseWarehouse=new SearchBaseWarehouse();
        searchBaseWarehouse.setOrgId(user.getOrganizationId());
        List<BaseWarehouse> warehouseList=baseFeignApi.findList(searchBaseWarehouse).getData();
        if(StringUtils.isNotEmpty(warehouseList) && warehouseList.size()>0){
            warehouseId=warehouseList.get(0).getWarehouseId();
        }

        //?????????
        wmsOutDeliveryOrder.setModifiedTime(new Date());
        wmsOutDeliveryOrder.setModifiedUserId(user.getUserId());
        wmsOutDeliveryOrder.setOrgId(user.getOrganizationId());
        int i = wmsOutDeliveryOrderMapper.updateByPrimaryKeySelective(wmsOutDeliveryOrder);

        //???????????????
        WmsOutHtDeliveryOrder wmsOutHtDeliveryOrder = new WmsOutHtDeliveryOrder();
        BeanUtils.copyProperties(wmsOutDeliveryOrder,wmsOutHtDeliveryOrder);
        wmsOutHtDeliveryOrderMapper.insertSelective(wmsOutHtDeliveryOrder);

        //???????????????
        List<WmsOutHtDeliveryOrderDet> wmsOutHtDeliveryOrderDetList = new ArrayList<>();

        Example example = new Example(WmsOutDeliveryOrder.class);
        example.createCriteria().andEqualTo("deliveryOrderId", wmsOutDeliveryOrder.getDeliveryOrderId());
        wmsOutDeliveryOrderDetMapper.deleteByExample(example);

        List<WmsOutDeliveryOrderDetDto> wmsOutDeliveryOrderDetList = wmsOutDeliveryOrder.getWmsOutDeliveryOrderDetList();
        if(StringUtils.isNotEmpty(wmsOutDeliveryOrderDetList)){
            for (WmsOutDeliveryOrderDet wmsOutDeliveryOrderDet : wmsOutDeliveryOrderDetList ) {
//                //???????????????????????????
//                if(wmsOutDeliveryOrder.getOrderTypeId()==8){
//                    this.findInventory(wmsOutDeliveryOrderDet);
//                }
                wmsOutDeliveryOrderDet.setModifiedTime(new Date());
                wmsOutDeliveryOrderDet.setModifiedUserId(user.getUserId());
                wmsOutDeliveryOrderDet.setOrgId(user.getOrganizationId());

                //?????????????????????????????? 2022-03-16
                wmsOutDeliveryOrderDet.setSalesCode(wmsOutDeliveryOrderDet.getSalesCode());

                if(StringUtils.isEmpty(wmsOutDeliveryOrderDet.getWarehouseId())){
                    wmsOutDeliveryOrderDet.setWarehouseId(warehouseId);
                }
                //?????????????????????
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
     * ???????????????????????????
     * @param wmsOutDeliveryOrderDet
     */
    private void findInventory(WmsOutDeliveryOrderDet wmsOutDeliveryOrderDet){
        //????????????
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
            throw new BizErrorException("????????????");
        }
       if(wmsInnerInventoryDtos.stream().map(WmsInnerInventory::getPackingQty).reduce(BigDecimal.ZERO,BigDecimal::add).compareTo(wmsOutDeliveryOrderDet.getPackingQty())==-1){
           throw new BizErrorException("???????????????");
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
            //???????????????
            WmsOutHtDeliveryOrder wmsOutHtDeliveryOrder = new WmsOutHtDeliveryOrder();
            BeanUtils.copyProperties(wmsOutDeliveryOrder,wmsOutHtDeliveryOrder);
            wmsOutHtDeliveryOrderMapper.insertSelective(wmsOutHtDeliveryOrder);

            //??????????????????????????????
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


    //@Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int createJobOrderOld(Long id,Long platformId) {
        Map<String, Object> map = new HashMap<>();
        map.put("deliveryOrderId", id);
        List<WmsOutDeliveryOrderDto> list = wmsOutDeliveryOrderMapper.findList(map);
        if (StringUtils.isEmpty(list)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003,"?????????");
        }
        WmsOutDeliveryOrderDto wmsOutDeliveryOrderDto = list.get(0);

        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("wmsOutDelivery");
        List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        if(!specItems.isEmpty() && "wb".equals(specItems.get(0).getParaValue())){
            if(wmsOutDeliveryOrderDto.getOrderTypeId().equals(1L) && (wmsOutDeliveryOrderDto.getAuditStatus() == null || wmsOutDeliveryOrderDto.getAuditStatus() != (byte) 1)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011023, "????????????????????????????????????????????????????????????");
            }
        }

        SearchWmsOutDeliveryOrderDet searchWmsOutDeliveryOrderDet = new SearchWmsOutDeliveryOrderDet();
        searchWmsOutDeliveryOrderDet.setDeliveryOrderId(id);
        List<WmsOutDeliveryOrderDetDto> wmsOutDeliveryOrderDetList = wmsOutDeliveryOrderDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutDeliveryOrderDet));
        if (StringUtils.isEmpty(wmsOutDeliveryOrderDetList)) {
            throw new BizErrorException("?????????????????????????????????????????????");
        }

        //???????????????????????????
        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setRelatedOrderCode(wmsOutDeliveryOrderDto.getDeliveryOrderCode());
        searchWmsInnerJobOrder.setCodeQueryMark(1);
        List<WmsInnerJobOrderDto> wmsInnerJobOrderDtos = innerFeignApi.findList(searchWmsInnerJobOrder).getData();
        //??????????????????????????????
        if (StringUtils.isEmpty(wmsInnerJobOrderDtos)) {
            Map<Long, List<WmsOutDeliveryOrderDetDto>> listMap = new HashMap<>();
            //????????????id??????(?????????????????????????????????)
            for (WmsOutDeliveryOrderDetDto wmsOutDeliveryOrderDetDto : wmsOutDeliveryOrderDetList) {
                //????????????
                BigDecimal totalInvQty = wmsOutDeliveryOrderMapper.totalInventoryQty(ControllerUtil.dynamicCondition("warehouseId",wmsOutDeliveryOrderDetDto.getWarehouseId(),"materialId",wmsOutDeliveryOrderDetDto.getMaterialId(),"batchCode",wmsOutDeliveryOrderDetDto.getBatchCode()));
                if(StringUtils.isEmpty(totalInvQty) || totalInvQty.compareTo(wmsOutDeliveryOrderDetDto.getPackingQty())==-1){
                    throw new BizErrorException("????????????,?????????????????????");
                }

                if (listMap.containsKey(wmsOutDeliveryOrderDetDto.getWarehouseId())) {
                    listMap.get(wmsOutDeliveryOrderDetDto.getWarehouseId()).add(wmsOutDeliveryOrderDetDto);
                } else {
                    List<WmsOutDeliveryOrderDetDto> wmsOutDeliveryOrderDetDtos = new ArrayList<>();
                    wmsOutDeliveryOrderDetDtos.add(wmsOutDeliveryOrderDetDto);
                    listMap.put(wmsOutDeliveryOrderDetDto.getWarehouseId(), wmsOutDeliveryOrderDetDtos);
                }
            }

            //??????map
            for (List<WmsOutDeliveryOrderDetDto> dtoList : listMap.values()) {
                WmsInnerJobOrder wmsInnerJobOrder = new WmsInnerJobOrder();
                wmsInnerJobOrder.setSourceOrderId(wmsOutDeliveryOrderDto.getDeliveryOrderId());
                wmsInnerJobOrder.setMaterialOwnerId(wmsOutDeliveryOrderDto.getMaterialOwnerId());
                wmsInnerJobOrder.setOrderTypeId(wmsOutDeliveryOrderDto.getOrderTypeId());
                wmsInnerJobOrder.setRelatedOrderCode(wmsOutDeliveryOrderDto.getDeliveryOrderCode());
                wmsInnerJobOrder.setJobOrderType((byte) 4);
                wmsInnerJobOrder.setOrderStatus((byte) 1);
                wmsInnerJobOrder.setWarehouseId(dtoList.get(0).getWarehouseId());
                //???????????????
                BigDecimal packingSum = dtoList.stream().map(WmsOutDeliveryOrderDet::getPackingQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                wmsInnerJobOrder.setPlanQty(packingSum);

                if(wmsInnerJobOrder.getOrderTypeId()==8){
                    if(dtoList.stream().filter(li->StringUtils.isEmpty(li.getPickingStorageId())).collect(Collectors.toList()).size()>0 && dtoList.stream().filter(li->StringUtils.isNotEmpty(li.getPickingStorageId())).collect(Collectors.toList()).size()>0){
                        throw new BizErrorException("???????????????????????????");
                    }
                }

                //???????????????
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
                    wmsInnerJobOrderDet.setPlatformId(dto.getPlatformId());
                    //????????????
                    wmsInnerJobOrderDet.setOption1(dto.getSalesCode());
                    //PO
                    wmsInnerJobOrderDet.setOption2(dto.getOption3());
                    wmsInnerJobOrderDets.add(wmsInnerJobOrderDet);
                }
                wmsInnerJobOrder.setPlatformId(platformId);
                wmsInnerJobOrder.setWmsInPutawayOrderDets(wmsInnerJobOrderDets);

                //???????????????
                ResponseEntity responseEntity = innerFeignApi.add(wmsInnerJobOrder);
                if (responseEntity.getCode() != 0) {
                    throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
                }
            }
            wmsOutDeliveryOrderDto.setIfCreatedJobOrder((byte)1);
            wmsOutDeliveryOrderDto.setPlatformId(platformId);
            wmsOutDeliveryOrderMapper.updateByPrimaryKeySelective(wmsOutDeliveryOrderDto);
        } else {
            throw new BizErrorException("????????????????????????");
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int createJobOrder(Long id,Long platformId) {
        Map<String, Object> map = new HashMap<>();
        map.put("deliveryOrderId", id);
        List<WmsOutDeliveryOrderDto> list = wmsOutDeliveryOrderMapper.findList(map);
        if (StringUtils.isEmpty(list)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003,"?????????");
        }
        WmsOutDeliveryOrderDto wmsOutDeliveryOrderDto = list.get(0);

        //??????????????????????????????????????????????????????
        /*SearchWmsInnerJobOrder sWmsInnerJobOrder=new SearchWmsInnerJobOrder();
        List<Byte> statusList=new ArrayList<>();
        statusList.add((byte)1);
        statusList.add((byte)2);
        statusList.add((byte)3);
        statusList.add((byte)4);
        statusList.add((byte)5);
        sWmsInnerJobOrder.setOrderStatusList(statusList);
        sWmsInnerJobOrder.setJobOrderType((byte)4);
        //sWmsInnerJobOrder.setOrderStatus((byte)4);
        sWmsInnerJobOrder.setPlatformId(platformId);

        List<WmsInnerJobOrderDto> innerJobOrderDtos = innerFeignApi.findList(sWmsInnerJobOrder).getData();
        if(StringUtils.isNotEmpty(innerJobOrderDtos) && innerJobOrderDtos.size()>0){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"???????????????????????????????????????"+innerJobOrderDtos.get(0).getJobOrderCode()+"????????? ?????????????????????");
        }

         */

        //???????????????
        SearchBaseStorage searchBaseStorage=new SearchBaseStorage();
        searchBaseStorage.setStorageType((byte)3);
        List<BaseStorage> storageList=baseFeignApi.findList(searchBaseStorage).getData();

        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("wmsOutDelivery");
        List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        if(!specItems.isEmpty() && "wb".equals(specItems.get(0).getParaValue())){
            if(wmsOutDeliveryOrderDto.getOrderTypeId().equals(1L) && (wmsOutDeliveryOrderDto.getAuditStatus() == null || wmsOutDeliveryOrderDto.getAuditStatus() != (byte) 1)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011023, "????????????????????????????????????????????????????????????");
            }
        }

        SearchWmsOutDeliveryOrderDet searchWmsOutDeliveryOrderDet = new SearchWmsOutDeliveryOrderDet();
        searchWmsOutDeliveryOrderDet.setDeliveryOrderId(id);
        List<WmsOutDeliveryOrderDetDto> wmsOutDeliveryOrderDetList = wmsOutDeliveryOrderDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutDeliveryOrderDet));
        if (StringUtils.isEmpty(wmsOutDeliveryOrderDetList)) {
            throw new BizErrorException("?????????????????????????????????????????????");
        }

        //???????????????????????????
        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setRelatedOrderCode(wmsOutDeliveryOrderDto.getDeliveryOrderCode());
        searchWmsInnerJobOrder.setCodeQueryMark(1);
        List<WmsInnerJobOrderDto> wmsInnerJobOrderDtos = innerFeignApi.findList(searchWmsInnerJobOrder).getData();
        //??????????????????????????????
        if (StringUtils.isEmpty(wmsInnerJobOrderDtos)) {
            Map<Long, List<WmsOutDeliveryOrderDetDto>> listMap = new HashMap<>();
            //????????????id??????(?????????????????????????????????)
            for (WmsOutDeliveryOrderDetDto wmsOutDeliveryOrderDetDto : wmsOutDeliveryOrderDetList) {
                if (listMap.containsKey(wmsOutDeliveryOrderDetDto.getWarehouseId())) {
                    listMap.get(wmsOutDeliveryOrderDetDto.getWarehouseId()).add(wmsOutDeliveryOrderDetDto);
                } else {
                    List<WmsOutDeliveryOrderDetDto> wmsOutDeliveryOrderDetDtos = new ArrayList<>();
                    wmsOutDeliveryOrderDetDtos.add(wmsOutDeliveryOrderDetDto);
                    listMap.put(wmsOutDeliveryOrderDetDto.getWarehouseId(), wmsOutDeliveryOrderDetDtos);
                }
            }

            //??????map
            for (List<WmsOutDeliveryOrderDetDto> dtoList : listMap.values()) {
                WmsInnerJobOrder wmsInnerJobOrder = new WmsInnerJobOrder();
                wmsInnerJobOrder.setSourceOrderId(wmsOutDeliveryOrderDto.getDeliveryOrderId());
                wmsInnerJobOrder.setMaterialOwnerId(wmsOutDeliveryOrderDto.getMaterialOwnerId());
                wmsInnerJobOrder.setOrderTypeId(wmsOutDeliveryOrderDto.getOrderTypeId());
                wmsInnerJobOrder.setRelatedOrderCode(wmsOutDeliveryOrderDto.getDeliveryOrderCode());
                wmsInnerJobOrder.setJobOrderType((byte) 4);
                wmsInnerJobOrder.setOrderStatus((byte) 1);
                wmsInnerJobOrder.setWarehouseId(dtoList.get(0).getWarehouseId());
                //???????????????
                BigDecimal packingSum = dtoList.stream().map(WmsOutDeliveryOrderDet::getPackingQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                wmsInnerJobOrder.setPlanQty(packingSum);

                if(wmsInnerJobOrder.getOrderTypeId()==8){
                    if(dtoList.stream().filter(li->StringUtils.isEmpty(li.getPickingStorageId())).collect(Collectors.toList()).size()>0 && dtoList.stream().filter(li->StringUtils.isNotEmpty(li.getPickingStorageId())).collect(Collectors.toList()).size()>0){
                        throw new BizErrorException("???????????????????????????");
                    }
                }

                //???????????????
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
                    if(StringUtils.isNotEmpty(dto.getStorageId())) {
                        wmsInnerJobOrderDet.setInStorageId(dto.getStorageId());
                    }
                    else if(StringUtils.isNotEmpty(storageList) && storageList.size()>0){
                        wmsInnerJobOrderDet.setInStorageId(storageList.get(0).getStorageId());
                    }
                    wmsInnerJobOrderDet.setInventoryStatusId(dto.getInventoryStatusId());
                    wmsInnerJobOrderDet.setMaterialId(dto.getMaterialId());
                    wmsInnerJobOrderDet.setPackingUnitName(dto.getPackingUnitName());
                    wmsInnerJobOrderDet.setPlanQty(dto.getPackingQty());
                    wmsInnerJobOrderDet.setOrderStatus((byte) 1);
                    wmsInnerJobOrderDet.setBatchCode(dto.getBatchCode());
                    wmsInnerJobOrderDet.setPlatformId(dto.getPlatformId());
                    //????????????
                    wmsInnerJobOrderDet.setOption2(dto.getSalesCode());
                    //PO
                    wmsInnerJobOrderDet.setOption3(dto.getOption3());
                    wmsInnerJobOrderDets.add(wmsInnerJobOrderDet);
                }
                wmsInnerJobOrder.setPlatformId(platformId);
                wmsInnerJobOrder.setWmsInPutawayOrderDets(wmsInnerJobOrderDets);

                //???????????????
                ResponseEntity responseEntity = innerFeignApi.add(wmsInnerJobOrder);
                if (responseEntity.getCode() != 0) {
                    throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
                }
            }
            wmsOutDeliveryOrderDto.setIfCreatedJobOrder((byte)1);
            wmsOutDeliveryOrderDto.setPlatformId(platformId);
            wmsOutDeliveryOrderMapper.updateByPrimaryKeySelective(wmsOutDeliveryOrderDto);
        } else {
            throw new BizErrorException("????????????????????????");
        }
        return 1;
    }

    /**
     * ???????????????????????????
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
        if(ids.size()>0){
            for (Long id : ids) {
                WmsOutDeliveryOrder deliveryOrder=wmsOutDeliveryOrderMapper.selectByPrimaryKey(id);
                if(StringUtils.isNotEmpty(deliveryOrder)){
                    if(deliveryOrder.getAuditStatus()==(byte)1){
                        throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"???????????????????????? ??????????????????-->"+deliveryOrder.getDeliveryOrderCode());
                    }
                }
            }
        }
        return wmsOutDeliveryOrderMapper.batchUpdateStatus(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveByApi(WmsOutDeliveryOrder wmsOutDeliveryOrder) {
        //??????????????????????????????
        SearchWmsInnerJobOrder searchWmsInnerJobOrder=new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setRelatedOrderCode(wmsOutDeliveryOrder.getDeliveryOrderCode());
        searchWmsInnerJobOrder.setOrgId(wmsOutDeliveryOrder.getOrgId());
        ResponseEntity<List<WmsInnerJobOrderDto>> responseEntityDto=innerFeignApi.findList(searchWmsInnerJobOrder);
        if(StringUtils.isNotEmpty(responseEntityDto.getData())){
            //????????????????????????????????? ?????? ?????????
            if(StringUtils.isNotEmpty(responseEntityDto.getData().get(0).getJobOrderCode()))
                return 0;
        }

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

            //??????
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
            wmsOutDeliveryOrder.setOrderStatus(outDeliveryOrder.getOrderStatus());
            wmsOutDeliveryOrderMapper.updateByPrimaryKeySelective(wmsOutDeliveryOrder);

            //??????
            List<WmsOutDeliveryOrderDetDto> wmsOutDeliveryOrderDetList = wmsOutDeliveryOrder.getWmsOutDeliveryOrderDetList();
            if(StringUtils.isNotEmpty(wmsOutDeliveryOrderDetList)){
                for (WmsOutDeliveryOrderDet wmsOutDeliveryOrderDet : wmsOutDeliveryOrderDetList ) {
                    //IDGUID??????????????????
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<WmsOutDeliveryOrderImport> wmsOutDeliveryOrderImports) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //??????????????????
        int success = 0;  //?????????????????????
        List<Integer> fail = new ArrayList<>();  //????????????????????????
        LinkedList<WmsOutDeliveryOrder> list = new LinkedList<>();
        LinkedList<WmsOutHtDeliveryOrder> htList = new LinkedList<>();
//        LinkedList<WmsOutDeliveryOrderImport> deliveryOrderImports = new LinkedList<>();

        // ????????????
        SearchBaseMaterialOwner searchBaseMaterialOwner = new SearchBaseMaterialOwner();
        searchBaseMaterialOwner.setOrgId(user.getOrganizationId());
        searchBaseMaterialOwner.setPageSize(9999);
        List<BaseMaterialOwnerDto> materialOwnerDtos = baseFeignApi.findList(searchBaseMaterialOwner).getData();
        if (materialOwnerDtos.isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.GL9999404, "?????????????????????????????????????????????");
        }

        // ????????????
        SearchBaseSupplier searchBaseSupplier = new SearchBaseSupplier();
        searchBaseSupplier.setOrgId(user.getOrganizationId());
        searchBaseSupplier.setSupplierType((byte)2);
        searchBaseSupplier.setPageSize(9999);
        List<BaseSupplier> baseSuppliers = baseFeignApi.findSupplierList(searchBaseSupplier).getData();
        if (StringUtils.isEmpty(baseSuppliers)){
            throw new BizErrorException(ErrorCodeEnum.GL9999404, "?????????????????????????????????????????????");
        }

//        // ?????????
//        SearchBaseConsignee searchBaseConsignee = new SearchBaseConsignee();
//        searchBaseConsignee.setOrgId(user.getOrganizationId());
//        searchBaseConsignee.setPageSize(9999);
//        List<BaseConsignee> baseConsignees = baseFeignApi.findList(searchBaseConsignee).getData();
//        if (StringUtils.isEmpty(baseConsignees)){
//            throw new BizErrorException(ErrorCodeEnum.GL9999404, "??????????????????????????????????????????");
//        }

        // ????????????
        SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
        searchBaseStorage.setOrgId(user.getOrganizationId());
        searchBaseStorage.setCodeQueryMark((byte)1);
        searchBaseStorage.setStorageType((byte) 3);
        searchBaseStorage.setPageSize(9999);
        List<BaseStorage> baseStorages = baseFeignApi.findList(searchBaseStorage).getData();
        if (baseStorages.isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.GL9999404, "?????????????????????????????????????????????");
        }

        for (int i = 0; i < wmsOutDeliveryOrderImports.size(); i++) {
            WmsOutDeliveryOrderImport wmsOutDeliveryOrderImport = wmsOutDeliveryOrderImports.get(i);

            Date planDespatchDate = wmsOutDeliveryOrderImport.getPlanDespatchDate();
            if (StringUtils.isEmpty(planDespatchDate)){
                fail.add(i+1);
                continue;
            }
            // ??????
            wmsOutDeliveryOrderImport.setMaterialOwnerId(materialOwnerDtos.get(0).getMaterialOwnerId());

            // ????????????????????????
            String customerCode = wmsOutDeliveryOrderImport.getCustomerCode();
            if(StringUtils.isNotEmpty(customerCode)){
                for (BaseSupplier supplier : baseSuppliers){
                    if (customerCode.equals(supplier.getSupplierCode())){
                        wmsOutDeliveryOrderImport.setCustomerId(supplier.getSupplierId());
                        break;
                    }
                }
            }
            /*else {
                //?????? ????????????????????? 1032050057
                List<BaseSupplier> baseSupplierList = baseSuppliers.stream().filter(u -> (u.getSupplierCode().equals("1032050057"))).collect(Collectors.toList());
                if(StringUtils.isNotEmpty(baseSupplierList) && baseSupplierList.size()>0){
                    wmsOutDeliveryOrderImport.setCustomerId(baseSupplierList.get(0).getSupplierId());
                }
            }*/

//            //?????????????????????
//            String consigneeCode = wmsOutDeliveryOrderImport.getConsigneeCode();
//            if(StringUtils.isNotEmpty(consigneeCode)){
//                for (BaseConsignee baseConsignee : baseConsignees){
//                    if (consigneeCode.equals(baseConsignee.getConsigneeCode())){
//                        wmsOutDeliveryOrderImport.setConsignee(baseConsignees.get(0).getConsigneeName());
//                        wmsOutDeliveryOrderImport.setLinkManName(baseConsignees.get(0).getLinkManName());
//                        wmsOutDeliveryOrderImport.setLinkManPhone(baseConsignees.get(0).getLinkManPhone());
//                        wmsOutDeliveryOrderImport.setFaxNumber(baseConsignees.get(0).getFaxNumber());
//                        wmsOutDeliveryOrderImport.setEmailAddress(baseConsignees.get(0).getEmailAddress());
//                        wmsOutDeliveryOrderImport.setDetailedAddress(baseConsignees.get(0).getAddress());
//                        break;
//                    }
//                }
//            }

            //---------??????-----------
            //????????????????????????
            String storageCode = wmsOutDeliveryOrderImport.getStorageCode();
            if(StringUtils.isNotEmpty(storageCode)){
                for (BaseStorage baseStorage : baseStorages){
                    if (storageCode.equals(baseStorage.getStorageCode())){
                        wmsOutDeliveryOrderImport.setStorageId(baseStorage.getStorageId());
                        wmsOutDeliveryOrderImport.setWarehouseId(baseStorage.getWarehouseId());
                        break;
                    }
                }
            }

            //????????????
            String materialCode = wmsOutDeliveryOrderImport.getMaterialCode();
            if(StringUtils.isNotEmpty(materialCode)) {
                SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
                searchBaseMaterial.setMaterialCode(materialCode);
                searchBaseMaterial.setOrgId(user.getOrganizationId());
                List<BaseMaterial> baseMaterials = baseFeignApi.findList(searchBaseMaterial).getData();
                if (StringUtils.isEmpty(baseMaterials)){
                    fail.add(i+1);
                    continue;
                }
                wmsOutDeliveryOrderImport.setMaterialId(baseMaterials.get(0).getMaterialId());
            }

//            deliveryOrderImports.add(wmsOutDeliveryOrderImport);
        }
        if(StringUtils.isNotEmpty(wmsOutDeliveryOrderImports)){
            // ???????????????
            Map<String, List<WmsOutDeliveryOrderImport>> collect = wmsOutDeliveryOrderImports.stream().collect(Collectors.groupingBy(WmsOutDeliveryOrderImport::getGenRule));
            // ?????????
            Map<String, List<WmsOutDeliveryOrderImport>> single = collect.get("1").stream().collect(Collectors.groupingBy(WmsOutDeliveryOrderImport::getLIDCode));
            // ?????????
            Map<String, List<WmsOutDeliveryOrderImport>> twin = collect.get("2").stream().collect(Collectors.groupingBy(WmsOutDeliveryOrderImport::getLIDCode));
            if (!twin.isEmpty()){
                single.putAll(twin);
            }
            //???????????????????????????
            Set<String> codeList = single.keySet();
            for (String code : codeList) {
                List<WmsOutDeliveryOrderImport> wmsOutDeliveryOrderImports1 = single.get(code);
                WmsOutDeliveryOrder wmsOutDeliveryOrder = new WmsOutDeliveryOrder();
                //??????????????????
                BeanUtils.copyProperties(wmsOutDeliveryOrderImports1.get(0), wmsOutDeliveryOrder);
                wmsOutDeliveryOrder.setDeliveryOrderCode(CodeUtils.getId("XSCK-"));
                wmsOutDeliveryOrder.setCreateTime(new Date());
                wmsOutDeliveryOrder.setCreateUserId(user.getUserId());
                wmsOutDeliveryOrder.setModifiedUserId(user.getUserId());
                wmsOutDeliveryOrder.setModifiedTime(new Date());
                wmsOutDeliveryOrder.setOrgId(user.getOrganizationId());
                wmsOutDeliveryOrder.setStatus((byte)1);
                wmsOutDeliveryOrder.setAuditStatus((byte) 0);
                wmsOutDeliveryOrder.setOrderStatus((byte)1);
                wmsOutDeliveryOrder.setOrderTypeId((long)1);
                wmsOutDeliveryOrder.setIfCreatedJobOrder((byte)0);
                success += wmsOutDeliveryOrderMapper.insertUseGeneratedKeys(wmsOutDeliveryOrder);

                //??????
                WmsOutHtDeliveryOrder wmsOutHtDeliveryOrder = new WmsOutHtDeliveryOrder();
                BeanUtils.copyProperties(wmsOutDeliveryOrder, wmsOutHtDeliveryOrder);
                htList.add(wmsOutHtDeliveryOrder);

                //??????????????????
                LinkedList<WmsOutDeliveryOrderDet> detList = new LinkedList<>();
                for (WmsOutDeliveryOrderImport wmsOutDeliveryOrderImport : wmsOutDeliveryOrderImports1) {
                    WmsOutDeliveryOrderDet wmsOutDeliveryOrderDet = new WmsOutDeliveryOrderDet();
                    BeanUtils.copyProperties(wmsOutDeliveryOrderImport, wmsOutDeliveryOrderDet);
                    wmsOutDeliveryOrderDet.setDeliveryOrderId(wmsOutDeliveryOrder.getDeliveryOrderId());
                    wmsOutDeliveryOrderDet.setStatus((byte) 1);
                    wmsOutDeliveryOrderDet.setOption1(wmsOutDeliveryOrderImport.getLIDCode());
                    wmsOutDeliveryOrderDet.setOption2(wmsOutDeliveryOrderImport.getBRCode());
                    wmsOutDeliveryOrderDet.setOption3(wmsOutDeliveryOrder.getOption3());
                    detList.add(wmsOutDeliveryOrderDet);
                }
                wmsOutDeliveryOrderDetMapper.insertList(detList);
            }
            wmsOutHtDeliveryOrderMapper.insertList(htList);
        }

        resultMap.put("??????????????????",success);
        resultMap.put("??????????????????",fail);
        return resultMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importSamsungExcel(List<WmsSamsungOutDeliveryOrderImport> wmsSamsungOutDeliveryOrderImports) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        Long customerId=null;
        SearchBaseSupplier searchBaseSupplier=new SearchBaseSupplier();
        searchBaseSupplier.setSupplierCode("1032050057");
        searchBaseSupplier.setCodeQueryMark((byte)1);
        searchBaseSupplier.setSupplierType((byte)2);
        List<BaseSupplier> supplierList=baseFeignApi.findSupplierList(searchBaseSupplier).getData();
        if(StringUtils.isNotEmpty(supplierList) && supplierList.size()>0){
            customerId=supplierList.get(0).getSupplierId();
        }
        Map<String, Object> resultMap = new HashMap<>();  //??????????????????
        int success = 0;  //?????????????????????
        List<Integer> fail = new ArrayList<>();  //????????????????????????
        LinkedList<WmsSamsungOutDeliveryOrderImport> samsungOutDeliveryOrderImports = new LinkedList<>();

        for (int i = 0; i < wmsSamsungOutDeliveryOrderImports.size(); i++) {
            WmsSamsungOutDeliveryOrderImport wmsSamsungOutDeliveryOrderImport = wmsSamsungOutDeliveryOrderImports.get(i);
            String LIDCode = wmsSamsungOutDeliveryOrderImport.getOption1();
            //String deliveryOrderCode = "SM"+LIDCode;
            String deliveryOrderCode = LIDCode;
            if (StringUtils.isEmpty(LIDCode)){
                fail.add(i+4);
                continue;
            }

            Example example = new Example(WmsOutDeliveryOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("deliveryOrderCode",deliveryOrderCode)
                    .andEqualTo("orgId",user.getOrganizationId());
            WmsOutDeliveryOrder wmsOutDeliveryOrder = wmsOutDeliveryOrderMapper.selectOneByExample(example);
            if(StringUtils.isNotEmpty(wmsOutDeliveryOrder)){
                fail.add(i+4);
                continue;
            }

            //????????????
            String materialCode = wmsSamsungOutDeliveryOrderImport.getMaterialCode();
            if(StringUtils.isNotEmpty(materialCode)) {
                SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
                searchBaseMaterial.setMaterialCode(materialCode);
                searchBaseMaterial.setOrgId(user.getOrganizationId());
                List<BaseMaterial> baseMaterials = baseFeignApi.findList(searchBaseMaterial).getData();
                if (StringUtils.isEmpty(baseMaterials)){
                    fail.add(i+4);
                    continue;
                }
                wmsSamsungOutDeliveryOrderImport.setMaterialId(baseMaterials.get(0).getMaterialId());
            }

            samsungOutDeliveryOrderImports.add(wmsSamsungOutDeliveryOrderImport);
        }

        if(StringUtils.isNotEmpty(samsungOutDeliveryOrderImports)){
            HashMap<String, List<WmsSamsungOutDeliveryOrderImport>> collect = samsungOutDeliveryOrderImports.stream().collect(Collectors.groupingBy(WmsSamsungOutDeliveryOrderImport::getOption1, HashMap::new, Collectors.toList()));
            Set<String> set = collect.keySet();

            for(String s : set){
                List<WmsSamsungOutDeliveryOrderImport> wmsSamsungOutDeliveryOrderImports1 = collect.get(s);
                WmsOutDeliveryOrder wmsOutDeliveryOrder = new WmsOutDeliveryOrder();
                //??????????????????
                //wmsOutDeliveryOrder.setDeliveryOrderCode("SM"+wmsSamsungOutDeliveryOrderImports1.get(0).getOption1());
                wmsOutDeliveryOrder.setDeliveryOrderCode(wmsSamsungOutDeliveryOrderImports1.get(0).getOption1());
                wmsOutDeliveryOrder.setPlanDespatchDate(wmsSamsungOutDeliveryOrderImports1.get(0).getPlanDespatchDate());
                wmsOutDeliveryOrder.setRelatedOrderCode1(wmsSamsungOutDeliveryOrderImports1.get(0).getOption3());
                wmsOutDeliveryOrder.setCreateTime(new Date());
                wmsOutDeliveryOrder.setCreateUserId(user.getUserId());
                wmsOutDeliveryOrder.setModifiedUserId(user.getUserId());
                wmsOutDeliveryOrder.setModifiedTime(new Date());
                wmsOutDeliveryOrder.setOrgId(user.getOrganizationId());
                wmsOutDeliveryOrder.setStatus((byte)1);
                wmsOutDeliveryOrder.setAuditStatus((byte) 0);
                wmsOutDeliveryOrder.setOrderStatus((byte)1);
                wmsOutDeliveryOrder.setOrderTypeId((long)1);
                wmsOutDeliveryOrder.setCustomerId(customerId);
                wmsOutDeliveryOrder.setIfCreatedJobOrder((byte)0);
                success += wmsOutDeliveryOrderMapper.insertUseGeneratedKeys(wmsOutDeliveryOrder);

                //??????????????????
                LinkedList<WmsOutDeliveryOrderDet> detList = new LinkedList<>();
                for (WmsSamsungOutDeliveryOrderImport wmsSamsungOutDeliveryOrderImport : wmsSamsungOutDeliveryOrderImports1) {
                    WmsOutDeliveryOrderDet wmsOutDeliveryOrderDet = new WmsOutDeliveryOrderDet();
                    BeanUtils.copyProperties(wmsSamsungOutDeliveryOrderImport, wmsOutDeliveryOrderDet);
                    wmsOutDeliveryOrderDet.setDeliveryOrderId(wmsOutDeliveryOrder.getDeliveryOrderId());
                    wmsOutDeliveryOrderDet.setStatus((byte) 1);
                    wmsOutDeliveryOrderDet.setOption3(null);
                    detList.add(wmsOutDeliveryOrderDet);
                }
                wmsOutDeliveryOrderDetMapper.insertList(detList);
            }
        }

        resultMap.put("??????????????????",success);
        resultMap.put("??????????????????",fail);
        return resultMap;
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int sealOrder(Long outDeliveryOrderId) {
        WmsOutDeliveryOrder wmsOutDeliveryOrder  = wmsOutDeliveryOrderMapper.selectByPrimaryKey(outDeliveryOrderId);
        if(StringUtils.isEmpty(wmsOutDeliveryOrder)){
            throw new BizErrorException("????????????????????????");
        }
        Example example = new Example(WmsOutDeliveryOrderDet.class);
        example.createCriteria().andEqualTo("deliveryOrderId",outDeliveryOrderId);
        List<WmsOutDeliveryOrderDet> wmsOutDeliveryOrderDets = wmsOutDeliveryOrderDetMapper.selectByExample(example);
        for (WmsOutDeliveryOrderDet wmsOutDeliveryOrderDet : wmsOutDeliveryOrderDets) {
            if(StringUtils.isEmpty(wmsOutDeliveryOrderDet.getDispatchQty())){
                wmsOutDeliveryOrderDet.setDispatchQty(BigDecimal.ZERO);
            }
            wmsOutDeliveryOrderDet.setPickingQty(wmsOutDeliveryOrderDet.getDispatchQty());
            wmsOutDeliveryOrderDetMapper.updateByPrimaryKeySelective(wmsOutDeliveryOrderDet);
        }
        wmsOutDeliveryOrder.setOrderStatus((byte)5);
        int num = wmsOutDeliveryOrderMapper.updateByPrimaryKeySelective(wmsOutDeliveryOrder);
        return num;
    }

    @Override
    /**
     * create by: Dylan
     * description: ??????????????????
     * create time:
     * @return
     */
    public int overIssue(Long deliveryOrderId) {

        String ISGUID="";
        String userName="";
        //??????????????????????????????
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        userName=user.getUserName();

        SearchWmsOutDeliveryOrderDet searchWmsOutDeliveryOrderDet = new SearchWmsOutDeliveryOrderDet();
        searchWmsOutDeliveryOrderDet.setDeliveryOrderId(deliveryOrderId);
        List<WmsOutDeliveryOrderDetDto> wmsOutDeliveryOrderDetList = wmsOutDeliveryOrderDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutDeliveryOrderDet));
        if(wmsOutDeliveryOrderDetList.size()>0){

            ISGUID=wmsOutDeliveryOrderDetList.get(0).getOption2();
            fiveringFeignApi.overIssue(ISGUID,userName);
        }

        return 0;
    }
}
