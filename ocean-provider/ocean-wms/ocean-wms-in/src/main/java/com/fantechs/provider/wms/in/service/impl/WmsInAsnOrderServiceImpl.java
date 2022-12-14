package com.fantechs.provider.wms.in.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseLabelMaterialDto;
import com.fantechs.common.base.general.dto.basic.BaseStorageRule;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductPalletDetDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcProductPalletDet;
import com.fantechs.common.base.general.dto.wms.in.*;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryLogDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabelMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.om.OmOtherInOrderDet;
import com.fantechs.common.base.general.entity.om.OmSalesReturnOrderDet;
import com.fantechs.common.base.general.entity.om.OmTransferOrder;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrder;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrderDet;
import com.fantechs.common.base.general.entity.wms.in.WmsInHtAsnOrder;
import com.fantechs.common.base.general.entity.wms.in.WmsInHtAsnOrderDet;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInAsnOrder;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInAsnOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.*;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.*;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.api.qms.OMFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.wms.in.mapper.WmsInAsnOrderDetMapper;
import com.fantechs.provider.wms.in.mapper.WmsInAsnOrderMapper;
import com.fantechs.provider.wms.in.mapper.WmsInHtAsnOrderDetMapper;
import com.fantechs.provider.wms.in.mapper.WmsInHtAsnOrderMapper;
import com.fantechs.provider.wms.in.service.WmsInAsnOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Mr.Lei on 2021/04/29.
 */
@Service
public class WmsInAsnOrderServiceImpl extends BaseService<WmsInAsnOrder> implements WmsInAsnOrderService {

    @Resource
    private WmsInAsnOrderMapper wmsInAsnOrderMapper;
    @Resource
    private WmsInAsnOrderDetMapper wmsInAsnOrderDetMapper;
    @Resource
    private InnerFeignApi innerFeignApi;
    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private SFCFeignApi sfcFeignApi;
    @Resource
    private OMFeignApi omFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private WmsInHtAsnOrderMapper wmsInHtAsnOrderMapper;
    @Resource
    private WmsInHtAsnOrderDetMapper wmsInHtAsnOrderDetMapper;

    @Override
    public List<WmsInAsnOrderDto> findList(SearchWmsInAsnOrder searchWmsInAsnOrder) {
        SysUser sysUser = currentUser();
        searchWmsInAsnOrder.setOrgId(sysUser.getOrganizationId());
        return wmsInAsnOrderMapper.findList(searchWmsInAsnOrder);
    }

    /**
     * ????????????
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int allReceiving(String ids, Long storageId) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        int num = 0;
        for (String s : arrId) {
            WmsInAsnOrder wmsInAsnOrder = wmsInAsnOrderMapper.selectByPrimaryKey(s);
            if (wmsInAsnOrder.getOrderStatus() == (byte) 3) {
                throw new BizErrorException("??????????????????");
            }
            if (StringUtils.isEmpty(wmsInAsnOrder)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            List<WmsInAsnOrderDet> list = wmsInAsnOrderDetMapper.select(WmsInAsnOrderDet.builder()
                    .asnOrderId(Long.parseLong(s))
                    .build());
            for (WmsInAsnOrderDet wmsInAsnOrderDet : list) {
                try {
                    if (StringUtils.isEmpty(wmsInAsnOrderDet.getActualQty()) || wmsInAsnOrderDet.getActualQty().compareTo(BigDecimal.ZERO) == 0) {

                        Long statusId = wmsInAsnOrderMapper.findDefaultStatus(ControllerUtil.dynamicCondition("salesCode", null, "poCode", null));
                        wmsInAsnOrderDet.setInventoryStatusId(statusId);
                        wmsInAsnOrderDet.setStorageId(wmsInAsnOrderDet.getStorageId());
                        wmsInAsnOrderDet.setActualQty(wmsInAsnOrderDet.getPackingQty());
                        wmsInAsnOrderDet.setModifiedTime(new Date());
                        wmsInAsnOrderDet.setModifiedUserId(sysUser.getUserId());

                        //????????????
                        wmsInAsnOrderDetMapper.updateByPrimaryKeySelective(wmsInAsnOrderDet);
                        num += addInventory(wmsInAsnOrder.getAsnOrderId(), wmsInAsnOrderDet.getAsnOrderDetId(), wmsInAsnOrderDet.getActualQty(), (byte) 1);
                    }
                } catch (Exception e) {
                    throw new BizErrorException("????????????");
                }
            }
            wmsInAsnOrder.setWmsInAsnOrderDetList(list);
            wmsInAsnOrder.setStartReceivingDate(new Date());
            wmsInAsnOrder.setEndReceivingDate(new Date());
            wmsInAsnOrder.setModifiedTime(new Date());
            wmsInAsnOrder.setModifiedUserId(sysUser.getUserId());
            wmsInAsnOrder.setOrderStatus((byte) 3);
            num += wmsInAsnOrderMapper.updateByPrimaryKeySelective(wmsInAsnOrder);

            //????????????
            this.addHt(wmsInAsnOrder);
        }
        return num;
    }

    /**
     * ????????????
     *
     * @param wmsInAsnOrderDet
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int singleReceiving(WmsInAsnOrderDet wmsInAsnOrderDet) {
        SysUser sysUser = currentUser();

        WmsInAsnOrder wmsInAsnOrder = wmsInAsnOrderMapper.selectByPrimaryKey(wmsInAsnOrderDet.getAsnOrderId());
        if (wmsInAsnOrder.getOrderStatus() == (byte) 3) {
            throw new BizErrorException("??????????????????");
        }

        if (StringUtils.isEmpty(wmsInAsnOrderDet.getAsnOrderDetId())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "????????????");
        }
        WmsInAsnOrderDet wms = wmsInAsnOrderDetMapper.selectByPrimaryKey(wmsInAsnOrderDet.getAsnOrderDetId());
        wmsInAsnOrderDet.setModifiedUserId(sysUser.getUserId());
        wmsInAsnOrderDet.setModifiedTime(new Date());


        //WmsInAsnOrderDet oldWms = wmsInAsnOrderDetMapper.selectByPrimaryKey(wmsInAsnOrderDet.getAsnOrderDetId());
        BigDecimal countQty = wmsInAsnOrderDet.getActualQty().add(wms.getActualQty() != null ? wms.getActualQty() : new BigDecimal("0"));
        if (countQty.compareTo(wmsInAsnOrderDet.getPackingQty()) == 1) {
            throw new BizErrorException("????????????????????????????????????");
        }
        //??????????????????
        if (wms.getActualQty() == null) {
            wmsInAsnOrderMapper.updateByPrimaryKeySelective(WmsInAsnOrder.builder()
                    .asnOrderId(wms.getAsnOrderId())
                    .startReceivingDate(new Date())
                    .modifiedTime(new Date())
                    .orderStatus((byte) 2)
                    .modifiedUserId(sysUser.getUserId())
                    .build());
            wmsInAsnOrderDet.setReceivingDate(new Date());
        }
        Long storageId = wms.getStorageId();
        Long warehouseId = wms.getWarehouseId();
        if (wmsInAsnOrderDet.getActualQty().compareTo(wmsInAsnOrderDet.getPackingQty()) == -1) {
            //if((wms.getInventoryStatusId()!=null || wms.getStorageId()!=null) && wmsInAsnOrderDet.getActualQty().compareTo(wmsInAsnOrderDet.getPackingQty())==-1){
            //if(!wmsInAsnOrderDet.getInventoryStatusId().equals(wms.getInventoryStatusId()) || !wmsInAsnOrderDet.getStorageId().equals(wms.getStorageId())){
            wms = new WmsInAsnOrderDet();
            BeanUtil.copyProperties(wmsInAsnOrderDet, wms);
            wms.setAsnOrderDetId(null);
            wms.setPackingQty(wmsInAsnOrderDet.getActualQty());
            wms.setLineNumber(wmsInAsnOrderDetMapper.findLineNumber(wmsInAsnOrder.getAsnOrderId()) + 1);
            int num = wmsInAsnOrderDetMapper.insertUseGeneratedKeys(wms);

            wmsInAsnOrderDet.setPackingQty(wmsInAsnOrderDet.getPackingQty().subtract(wmsInAsnOrderDet.getActualQty()));
            wmsInAsnOrderDet.setActualQty(BigDecimal.ZERO);
            wmsInAsnOrderDet.setWarehouseId(warehouseId);
            wmsInAsnOrderDet.setStorageId(storageId);
            wmsInAsnOrderDet.setInventoryStatusId(null);
            //}
        } else {
            wmsInAsnOrderDet.setActualQty(wmsInAsnOrderDet.getActualQty().add(wms.getActualQty() != null ? wms.getActualQty() : new BigDecimal("0")));
            wms.setActualQty(wmsInAsnOrderDet.getActualQty());
        }
        //wmsInAsnOrderDet.setActualQty(wmsInAsnOrderDet.getActualQty().add(wms.getActualQty()!=null?wms.getActualQty():new BigDecimal("0")));
        wmsInAsnOrderDetMapper.updateByPrimaryKey(wmsInAsnOrderDet);
        wmsInAsnOrderDet = wms;
        //????????????
        int num = this.addInventory(wmsInAsnOrderDet.getAsnOrderId(), wmsInAsnOrderDet.getAsnOrderDetId(), wmsInAsnOrderDet.getActualQty(), (byte) 1);
        if (num < 1) {
            throw new BizErrorException("????????????");
        }
        //????????????????????????????????????
        Example example = new Example(WmsInAsnOrderDet.class);
        example.createCriteria().andEqualTo("asnOrderId", wmsInAsnOrderDet.getAsnOrderId());
        List<WmsInAsnOrderDet> list = wmsInAsnOrderDetMapper.selectByExample(example);
        int size = list.size();
        list = list.stream().filter(li -> !StringUtils.isEmpty(li.getActualQty()) && li.getPackingQty().compareTo(li.getActualQty()) == 0).collect(Collectors.toList());
        if (list.size() == size) {
            //?????????????????????????????????
            if (countQty.compareTo(wmsInAsnOrderDet.getPackingQty()) == 0) {
                wmsInAsnOrderMapper.updateByPrimaryKeySelective(WmsInAsnOrder.builder()
                        .asnOrderId(wms.getAsnOrderId())
                        .endReceivingDate(new Date())
                        .orderStatus((byte) 3)
                        .modifiedTime(new Date())
                        .modifiedUserId(sysUser.getUserId())
                        .build());
            }
        }
        return num;
    }

    /**
     * ???????????????????????????
     *
     * @param wmsInAsnOrderDet
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int writeQty(WmsInAsnOrderDet wmsInAsnOrderDet) {
        WmsInAsnOrderDet wms = wmsInAsnOrderDetMapper.selectByPrimaryKey(wmsInAsnOrderDet.getAsnOrderDetId());
        WmsInAsnOrder wmsInAsnOrder = wmsInAsnOrderMapper.selectByPrimaryKey(wms.getAsnOrderId());
        int num = wmsInAsnOrderDetMapper.updateByPrimaryKeySelective(WmsInAsnOrderDet.builder()
                .asnOrderDetId(wmsInAsnOrderDet.getAsnOrderDetId())
                .putawayQty(wms.getPutawayQty() != null ? wms.getPutawayQty().add(wmsInAsnOrderDet.getPutawayQty()) : wmsInAsnOrderDet.getPutawayQty())
                .build());
        if (StringUtils.isEmpty(wms.getSourceOrderId(), wms.getOrderDetId(), wmsInAsnOrder.getSourceOrderId())) {
            return num;
        }
        //??????????????????
        switch (wmsInAsnOrder.getOrderTypeId().toString()) {
            //??????
            case "3":
                //????????????????????????
                //??????????????????????????????????????????
                Example example = new Example(WmsInAsnOrderDet.class);
                example.createCriteria().andEqualTo("asnOrderId", wmsInAsnOrder.getAsnOrderId());
                List<WmsInAsnOrderDet> wmsInAsnOrderDets = wmsInAsnOrderDetMapper.selectByExample(example);
                BigDecimal actualQty = wmsInAsnOrderDets.stream()
                        .map(WmsInAsnOrderDet::getActualQty)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal putawayQty = wmsInAsnOrderDets.stream()
                        .map(WmsInAsnOrderDet::getPutawayQty)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                //??????????????????????????????????????????????????????
                if (actualQty.compareTo(putawayQty) == 0) {
                    OmTransferOrder omTransferOrder = new OmTransferOrder();
                    omTransferOrder.setTransferOrderId(wmsInAsnOrder.getSourceOrderId());
                    omTransferOrder.setOrderStatus((byte) 3);
                    ResponseEntity responseEntity = omFeignApi.updateStatus(omTransferOrder);
                    if (responseEntity.getCode() != 0) {
                        throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
                    }
                }
                break;
            //???????????????
            case "4":
                break;
            //???????????????
            case "5":
                OmSalesReturnOrderDet omSalesReturnOrderDet = new OmSalesReturnOrderDet();
                omSalesReturnOrderDet.setSalesReturnOrderId(wms.getSourceOrderId());
                omSalesReturnOrderDet.setSalesReturnOrderDetId(wms.getOrderDetId());
                omSalesReturnOrderDet.setReceivingQty(wmsInAsnOrderDet.getPutawayQty());
                ResponseEntity responseEntity = omFeignApi.writeQty(omSalesReturnOrderDet);
                if (responseEntity.getCode() != 0) {
                    throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
                }
                break;
            //???????????????
            case "6":
                OmOtherInOrderDet omOtherInOrderDet = new OmOtherInOrderDet();
                omOtherInOrderDet.setOtherInOrderId(wms.getSourceOrderId());
                omOtherInOrderDet.setOtherInOrderDetId(wms.getOrderDetId());
                omOtherInOrderDet.setReceivingQty(wmsInAsnOrderDet.getPutawayQty());
                ResponseEntity responseEntity1 = omFeignApi.writeQtyToIn(omOtherInOrderDet);
                if (responseEntity1.getCode() != 0) {
                    throw new BizErrorException(responseEntity1.getCode(), responseEntity1.getMessage());
                }
                break;
        }
        return num;
    }

    /**
     * ?????????????????????-??????????????????
     *
     * @param wmsInAsnOrder
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public WmsInAsnOrder packageAutoAdd(WmsInAsnOrder wmsInAsnOrder) {
        SysUser sysUser = currentUser();
        wmsInAsnOrder.setAsnCode(CodeUtils.getId("ASN-"));
        wmsInAsnOrder.setCreateTime(new Date());
        wmsInAsnOrder.setCreateUserId(sysUser.getUserId());
        wmsInAsnOrder.setModifiedUserId(sysUser.getUserId());
        wmsInAsnOrder.setModifiedTime(new Date());
        wmsInAsnOrder.setOrgId(sysUser.getOrganizationId());
        int num = wmsInAsnOrderMapper.insertUseGeneratedKeys(wmsInAsnOrder);
        WmsInAsnOrderDto wmsInAsnOrderDto = wmsInAsnOrderMapper.findList(SearchWmsInAsnOrder.builder()
                .asnOrderId(wmsInAsnOrder.getAsnOrderId())
                .build()).get(0);
        for (WmsInAsnOrderDet wmsInAsnOrderDet : wmsInAsnOrder.getWmsInAsnOrderDetList()) {
            wmsInAsnOrderDet.setAsnOrderId(wmsInAsnOrder.getAsnOrderId());
            wmsInAsnOrderDet.setCreateTime(new Date());
            wmsInAsnOrderDet.setCreateUserId(sysUser.getUserId());
            wmsInAsnOrderDet.setModifiedTime(new Date());
            wmsInAsnOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInAsnOrderDet.setOrgId(sysUser.getOrganizationId());
            wmsInAsnOrderDetMapper.insertUseGeneratedKeys(wmsInAsnOrderDet);

            WmsInAsnOrderDetDto wmsInAsnOrderDetDto = wmsInAsnOrderDetMapper.findList(SearchWmsInAsnOrderDet.builder()
                    .asnOrderDetId(wmsInAsnOrderDet.getOrderDetId())
                    .build()).get(0);
            //????????????
            WmsInnerInventory wmsInnerInventory = new WmsInnerInventory();
            wmsInnerInventory.setInventoryStatusId(wmsInAsnOrderDetDto.getInventoryStatusId());
            wmsInnerInventory.setReceivingDate(wmsInAsnOrderDto.getEndReceivingDate());
            wmsInnerInventory.setPackingUnitName(wmsInAsnOrderDetDto.getPackingUnitName());
            wmsInnerInventory.setPackingQty(wmsInAsnOrderDetDto.getPackingQty());
            wmsInnerInventory.setPalletCode(wmsInAsnOrderDetDto.getPalletCode());
            wmsInnerInventory.setMaterialOwnerId(wmsInAsnOrderDto.getMaterialOwnerId());
            wmsInnerInventory.setRelevanceOrderCode(wmsInAsnOrderDto.getAsnCode());
            wmsInnerInventory.setMaterialId(wmsInAsnOrderDetDto.getMaterialId());
            wmsInnerInventory.setWarehouseId(wmsInAsnOrderDetDto.getWarehouseId());
            wmsInnerInventory.setStorageId(wmsInAsnOrderDetDto.getStorageId());
            wmsInnerInventory.setReceivingDate(new Date());
            wmsInnerInventory.setProductionDate(wmsInAsnOrderDetDto.getProductionDate());
            wmsInnerInventory.setJobStatus((byte) 1);
            wmsInnerInventory.setCreateTime(new Date());
            wmsInnerInventory.setCreateUserId(sysUser.getUserId());
            wmsInnerInventory.setModifiedTime(new Date());
            wmsInnerInventory.setModifiedUserId(sysUser.getUserId());
            wmsInnerInventory.setOrgId(sysUser.getOrganizationId());
            ResponseEntity responseEntity = innerFeignApi.insertSelective(wmsInnerInventory);
            if (responseEntity.getCode() != 0) {
                throw new BizErrorException("??????????????????");
            }
        }
        return wmsInAsnOrder;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int createInnerJobOrder(Long asnOrderId) {
        WmsInAsnOrder wmsInAsnOrder = wmsInAsnOrderMapper.selectByPrimaryKey(asnOrderId);
        if (StringUtils.isEmpty(wmsInAsnOrder)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        if (wmsInAsnOrder.getOrderStatus() != (byte) 3) {
            throw new BizErrorException("??????????????????????????????");
        }
        Example example = new Example(WmsInAsnOrderDet.class);
        example.createCriteria().andEqualTo("asnOrderId", asnOrderId);
        List<WmsInAsnOrderDet> list = wmsInAsnOrderDetMapper.selectByExample(example);


        //???????????????????????????
        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setSourceOrderId(asnOrderId);
        searchWmsInnerJobOrder.setOrderTypeId(wmsInAsnOrder.getOrderTypeId());
        List<WmsInnerJobOrderDto> wmsInnerJobOrderDtos = innerFeignApi.findList(searchWmsInnerJobOrder).getData();
        if (wmsInnerJobOrderDtos.size() > 0) {
            throw new BizErrorException("????????????????????????");
        }
        int num = 0;
        Map<Long, List<WmsInAsnOrderDet>> listMap = new HashMap<>();
        //????????????id??????(?????????????????????????????????)
        for (WmsInAsnOrderDet wms : list) {
            if (listMap.containsKey(wms.getWarehouseId())) {
                listMap.get(wms.getWarehouseId()).add(wms);
            } else {
                List<WmsInAsnOrderDet> wmsInAsnOrderDets = new ArrayList<>();
                wmsInAsnOrderDets.add(wms);
                listMap.put(wms.getWarehouseId(), wmsInAsnOrderDets);
            }
        }

        for (List<WmsInAsnOrderDet> dtoList : listMap.values()) {
            WmsInnerJobOrder wmsInnerJobOrder = WmsInnerJobOrder.builder()
                    .sourceOrderId(wmsInAsnOrder.getAsnOrderId())
                    .materialOwnerId(wmsInAsnOrder.getMaterialOwnerId())
                    .orderTypeId(wmsInAsnOrder.getOrderTypeId())
                    .jobOrderType((byte) 3)
                    .relatedOrderCode(wmsInAsnOrder.getAsnCode())
                    .warehouseId(dtoList.get(0).getWarehouseId())
                    .planQty(dtoList.stream().map(WmsInAsnOrderDet::getActualQty).reduce(BigDecimal.ZERO, BigDecimal::add))
                    .orderStatus((byte) 1)
                    .orderTypeId(wmsInAsnOrder.getOrderTypeId())
                    .actualQty(new BigDecimal("0"))
                    .build();
            List<WmsInnerJobOrderDet> wmsInnerJobOrderDets = new ArrayList<>();
            for (WmsInAsnOrderDet wms : dtoList) {
                wmsInnerJobOrderDets.add(WmsInnerJobOrderDet.builder()
                        .sourceDetId(wms.getAsnOrderDetId())
                        .materialOwnerId(wmsInAsnOrder.getMaterialOwnerId())
                        .outStorageId(wms.getStorageId())
                        .inventoryStatusId(wms.getInventoryStatusId())
                        .materialId(wms.getMaterialId())
                        .batchCode(wms.getBatchCode())
                        .warehouseId(wms.getWarehouseId())
                        .packingUnitName(wms.getPackingUnitName())
                        .planQty(wms.getActualQty())
                        .palletCode(wms.getPalletCode())
                        .orderStatus((byte) 1)
                        .build());
            }
            wmsInnerJobOrder.setWmsInPutawayOrderDets(wmsInnerJobOrderDets);
            ResponseEntity responseEntity = innerFeignApi.add(wmsInnerJobOrder);
            if (responseEntity.getCode() != 0) {
                throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
            }
            num++;
        }
        return num;
    }

    private int addInventory(Long asnOrderId, Long asnOrderDetId, BigDecimal qty, Byte type) {
        SysUser sysUser = currentUser();
        WmsInAsnOrderDto wmsInAsnOrderDto = wmsInAsnOrderMapper.findList(SearchWmsInAsnOrder.builder()
                .asnOrderId(asnOrderId)
                .build()).get(0);

        WmsInAsnOrderDetDto wmsInAsnOrderDetDto = wmsInAsnOrderDetMapper.findList(SearchWmsInAsnOrderDet.builder()
                .asnOrderDetId(asnOrderDetId)
                .build()).get(0);

        Map<String, Object> map = new HashMap<>();
        map.put("relevanceOrderCode", wmsInAsnOrderDto.getAsnCode());
        map.put("materialId", wmsInAsnOrderDetDto.getMaterialId());
        map.put("batchCode", wmsInAsnOrderDetDto.getBatchCode());
        map.put("actualQty", wmsInAsnOrderDetDto.getActualQty());
        map.put("warehouseId", wmsInAsnOrderDetDto.getWarehouseId());
        map.put("storageId", wmsInAsnOrderDetDto.getStorageId());
        map.put("inventoryStatusId", wmsInAsnOrderDetDto.getInventoryStatusId());
        map.put("jobOrderDetId",wmsInAsnOrderDetDto.getAsnOrderDetId());
        WmsInnerInventory wmsInnerInventory = innerFeignApi.selectOneByExample(map).getData();
        if (StringUtils.isEmpty(wmsInnerInventory)) {
            //????????????
            wmsInnerInventory = new WmsInnerInventory();
            if (wmsInAsnOrderDto.getOrderTypeId() == (byte) 4) {
                wmsInnerInventory.setWorkOrderCode(wmsInAsnOrderDetDto.getWorkOrderCode());
            }
            wmsInnerInventory.setWorkOrderCode(wmsInAsnOrderDetDto.getWorkOrderCode());
            wmsInnerInventory.setInventoryStatusId(wmsInAsnOrderDetDto.getInventoryStatusId());
            wmsInnerInventory.setMaterialOwnerId(wmsInAsnOrderDto.getMaterialOwnerId());
            wmsInnerInventory.setPackingUnitName(wmsInAsnOrderDetDto.getPackingUnitName());
            wmsInnerInventory.setPackingQty(qty);
            wmsInnerInventory.setPalletCode(wmsInAsnOrderDetDto.getPalletCode());
            wmsInnerInventory.setRelevanceOrderCode(wmsInAsnOrderDto.getAsnCode());
            wmsInnerInventory.setMaterialId(wmsInAsnOrderDetDto.getMaterialId());
            wmsInnerInventory.setWarehouseId(wmsInAsnOrderDetDto.getWarehouseId());
            wmsInnerInventory.setStorageId(wmsInAsnOrderDetDto.getStorageId());
            wmsInnerInventory.setBatchCode(wmsInAsnOrderDetDto.getBatchCode());
            wmsInnerInventory.setJobOrderDetId(wmsInAsnOrderDetDto.getAsnOrderDetId());
            wmsInnerInventory.setReceivingDate(new Date());
            wmsInnerInventory.setPackingUnitName(wmsInAsnOrderDetDto.getPackingUnitName());
            wmsInnerInventory.setJobStatus(type);
            wmsInnerInventory.setCreateTime(new Date());
            wmsInnerInventory.setCreateUserId(sysUser.getUserId());
            wmsInnerInventory.setModifiedTime(new Date());
            wmsInnerInventory.setModifiedUserId(sysUser.getUserId());
            wmsInnerInventory.setOrgId(sysUser.getOrganizationId());

            //2022-03-18 ??????????????????
            wmsInnerInventory.setStockLock((byte) 0);
            wmsInnerInventory.setLockStatus((byte) 0);
            wmsInnerInventory.setQcLock((byte) 0);

            ResponseEntity responseEntity = innerFeignApi.insertSelective(wmsInnerInventory);
            if (responseEntity.getCode() != 0) {
                throw new BizErrorException("??????????????????");
            }
            //??????????????????
            this.addLog(wmsInnerInventory, wmsInAsnOrderDto, wmsInAsnOrderDetDto, BigDecimal.ZERO, qty);
        } else {
            //?????????
            //BigDecimal qty = wmsInAsnOrderDetDto.getActualQty().subtract(wmsInnerInventory.getPackingQty());
            BigDecimal initQty = wmsInnerInventory.getPackingQty();
            wmsInnerInventory.setPackingQty(wmsInnerInventory.getPackingQty().add(qty));
            ResponseEntity responseEntity = innerFeignApi.updateByPrimaryKeySelective(wmsInnerInventory);
            if (responseEntity.getCode() != 0) {
                throw new BizErrorException("????????????");
            }
            //??????????????????
            this.addLog(wmsInnerInventory, wmsInAsnOrderDto, wmsInAsnOrderDetDto, initQty, qty);
        }
        //??????????????????
        return 1;
    }

    private void addLog(WmsInnerInventory wmsInnerInventory, WmsInAsnOrder wmsInAsnOrder, WmsInAsnOrderDet wmsInAsnOrderDet, BigDecimal initQty, BigDecimal changQty) {
        WmsInnerInventoryLog wmsInnerInventoryLog = new WmsInnerInventoryLogDto();
        BeanUtil.copyProperties(wmsInnerInventory, wmsInnerInventoryLog);
        wmsInnerInventoryLog.setAsnCode(wmsInAsnOrder.getAsnCode());
        //??????
        wmsInnerInventoryLog.setJobOrderType((byte) 1);
        wmsInnerInventoryLog.setAddOrSubtract((byte) 1);
        wmsInnerInventoryLog.setSupplierId(wmsInAsnOrder.getSupplierId());
        wmsInnerInventoryLog.setWarehouseId(wmsInAsnOrderDet.getWarehouseId());
        wmsInnerInventoryLog.setStorageId(wmsInAsnOrderDet.getStorageId());
        wmsInnerInventoryLog.setMaterialId(wmsInAsnOrderDet.getMaterialId());
        wmsInnerInventoryLog.setRelatedOrderCode(wmsInAsnOrder.getAsnCode());
        wmsInnerInventoryLog.setProductionDate(wmsInAsnOrderDet.getProductionDate());
        wmsInnerInventoryLog.setExpiredDate(wmsInAsnOrderDet.getExpiredDate());
        wmsInnerInventoryLog.setBatchCode(wmsInAsnOrderDet.getBatchCode());
        wmsInnerInventoryLog.setPalletCode(wmsInAsnOrderDet.getPalletCode());
        wmsInnerInventoryLog.setInventoryStatusId(wmsInAsnOrderDet.getInventoryStatusId());
        wmsInnerInventoryLog.setInitialQty(initQty);
        wmsInnerInventoryLog.setChangeQty(changQty);
        wmsInnerInventoryLog.setSupplierId(wmsInAsnOrder.getSupplierId());
        wmsInnerInventoryLog.setMaterialOwnerId(wmsInAsnOrder.getMaterialOwnerId());
        wmsInnerInventoryLog.setOrgId(wmsInAsnOrder.getOrgId());
        ResponseEntity responseEntity = innerFeignApi.add(wmsInnerInventoryLog);
        if (responseEntity.getCode() != 0) {
            throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(WmsInAsnOrder record) {
        SysUser sysUser = currentUser();
        record.setOrderStatus((byte) 1);
        record.setAsnCode(CodeUtils.getId("ASN-"));
        record.setCreateTime(new Date());
        if (StringUtils.isEmpty(record.getOrderTypeId())) {
            record.setOrderTypeId((long) 4);
        }
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setOrgId(sysUser.getOrganizationId());
        int num = wmsInAsnOrderMapper.insertUseGeneratedKeys(record);

        //????????????????????????
        SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
        searchBaseStorage.setStorageCode("????????????");
        searchBaseStorage.setCodeQueryMark((byte) 1);
        List<BaseStorage> baseStorageList = baseFeignApi.findList(searchBaseStorage).getData();
        for (WmsInAsnOrderDet wmsInAsnOrderDet : record.getWmsInAsnOrderDetList()) {
            if (StringUtils.isEmpty(wmsInAsnOrderDet.getPackingQty()) || wmsInAsnOrderDet.getPackingQty().compareTo(BigDecimal.ZERO) < 1) {
                throw new BizErrorException("????????????????????????0");
            }
            if (record.getOrderTypeId() == 4) {
                MesPmWorkOrder mesPmWorkOrder = this.PmQty(wmsInAsnOrderDet.getPackingQty(), wmsInAsnOrderDet.getSourceOrderId());
                mesPmWorkOrder.setInventoryQty(StringUtils.isEmpty(mesPmWorkOrder.getInventoryQty()) ? wmsInAsnOrderDet.getPackingQty() : mesPmWorkOrder.getInventoryQty().add(wmsInAsnOrderDet.getPackingQty()));
                mesPmWorkOrder.setModifiedTime(new Date());
                mesPmWorkOrder.setModifiedUserId(sysUser.getUserId());
                ResponseEntity rs = pmFeignApi.updateInventory(mesPmWorkOrder);
                if (rs.getCode() != 0) {
                    throw new BizErrorException(rs.getMessage());
                }
            }
            wmsInAsnOrderDet.setStorageId(baseStorageList.get(0).getStorageId());
            wmsInAsnOrderDet.setWarehouseId(baseStorageList.get(0).getWarehouseId());
            Long statusId = wmsInAsnOrderMapper.findDefaultStatus(ControllerUtil.dynamicCondition("salesCode", null, "poCode", null));
            wmsInAsnOrderDet.setInventoryStatusId(statusId);
            wmsInAsnOrderDet.setAsnOrderId(record.getAsnOrderId());
            wmsInAsnOrderDet.setCreateTime(new Date());
            wmsInAsnOrderDet.setCreateUserId(sysUser.getUserId());
            wmsInAsnOrderDet.setModifiedTime(new Date());
            wmsInAsnOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInAsnOrderDet.setOrgId(sysUser.getOrganizationId());
            wmsInAsnOrderDetMapper.insert(wmsInAsnOrderDet);
        }

        this.addHt(record);
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(WmsInAsnOrder entity) {
        SysUser sysUser = currentUser();
        entity.setModifiedUserId(sysUser.getUserId());
        entity.setModifiedTime(new Date());
        int num = wmsInAsnOrderMapper.updateByPrimaryKeySelective(entity);
        if (entity.getOrderStatus() == (3)) {
            throw new BizErrorException("???????????????????????????????????????");
        }
        //??????????????????
        if (entity.getOrderTypeId() == 4) {
            this.deleteQty(entity.getAsnOrderId());
        } else {
            Example example = new Example(WmsInAsnOrderDet.class);
            example.createCriteria().andEqualTo("asnOrderId", entity.getAsnOrderId());
            wmsInAsnOrderDetMapper.deleteByExample(example);
        }
        for (WmsInAsnOrderDet wmsInAsnOrderDet : entity.getWmsInAsnOrderDetList()) {
            wmsInAsnOrderDet.setAsnOrderDetId(null);
            if (entity.getOrderTypeId() == 4) {
                MesPmWorkOrder mesPmWorkOrder = this.PmQty(wmsInAsnOrderDet.getPackingQty(), wmsInAsnOrderDet.getSourceOrderId());
                mesPmWorkOrder.setInventoryQty(StringUtils.isEmpty(mesPmWorkOrder.getInventoryQty()) ? wmsInAsnOrderDet.getPackingQty() : mesPmWorkOrder.getInventoryQty().add(wmsInAsnOrderDet.getPackingQty()));
                mesPmWorkOrder.setModifiedTime(new Date());
                mesPmWorkOrder.setModifiedUserId(sysUser.getUserId());
                ResponseEntity rs = pmFeignApi.updateInventory(mesPmWorkOrder);
                if (rs.getCode() != 0) {
                    throw new BizErrorException(rs.getMessage());
                }
            }
            wmsInAsnOrderDet.setCreateUserId(sysUser.getUserId());
            wmsInAsnOrderDet.setCreateTime(new Date());
            wmsInAsnOrderDet.setAsnOrderId(entity.getAsnOrderId());
            wmsInAsnOrderDet.setModifiedTime(new Date());
            wmsInAsnOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInAsnOrderDetMapper.insert(wmsInAsnOrderDet);
        }
        this.addHt(entity);
        return num;
    }

    /**
     * ??????????????????????????????
     *
     * @param asnOrderId
     */
    private void deleteQty(Long asnOrderId) {
        Example example = new Example(WmsInAsnOrderDet.class);
        example.createCriteria().andEqualTo("asnOrderId", asnOrderId);
        List<WmsInAsnOrderDet> list = wmsInAsnOrderDetMapper.selectByExample(example);
        for (WmsInAsnOrderDet wms : list) {
            //??????????????????????????????
            ResponseEntity<MesPmWorkOrder> responseEntity = pmFeignApi.workOrderDetail(wms.getSourceOrderId());
            if (responseEntity.getCode() != 0) {
                throw new BizErrorException("????????????????????????");
            }
            MesPmWorkOrder mesPmWorkOrder = responseEntity.getData();
            mesPmWorkOrder.setInventoryQty(mesPmWorkOrder.getInventoryQty().subtract(wms.getPackingQty()));
            ResponseEntity rs = pmFeignApi.updateInventory(mesPmWorkOrder);
            if (rs.getCode() != 0) {
                throw new BizErrorException(rs.getMessage());
            }

            wmsInAsnOrderDetMapper.deleteByPrimaryKey(wms.getAsnOrderDetId());
        }
    }

    /**
     * ??????????????????
     *
     * @param PackingQty
     * @param sourceOrderId
     */
    private MesPmWorkOrder PmQty(BigDecimal PackingQty, Long sourceOrderId) {
        Example example = new Example(WmsInAsnOrderDet.class);
        example.createCriteria().andEqualTo("sourceOrderId", sourceOrderId);
        List<WmsInAsnOrderDet> list = wmsInAsnOrderDetMapper.selectByExample(example);

        BigDecimal resultQty = list.stream()
                .map(WmsInAsnOrderDet::getPackingQty)
                .reduce(BigDecimal.ZERO, BigDecimal::add).add(PackingQty);

        //??????????????????????????????
        ResponseEntity<MesPmWorkOrder> responseEntity = pmFeignApi.workOrderDetail(sourceOrderId);
        if (responseEntity.getCode() != 0) {
            throw new BizErrorException("????????????????????????");
        }
        MesPmWorkOrder mesPmWorkOrder = responseEntity.getData();
        if (StringUtils.isEmpty(mesPmWorkOrder.getInventoryQty())) {
            mesPmWorkOrder.setInventoryQty(BigDecimal.ZERO);
        }
        if (PackingQty.compareTo(mesPmWorkOrder.getProductionQty()) == 1 || mesPmWorkOrder.getInventoryQty().add(PackingQty).compareTo(mesPmWorkOrder.getProductionQty()) == 1) {
            throw new BizErrorException("????????????????????????");
        }
        return mesPmWorkOrder;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        String[] arrId = ids.split(",");
        for (String s : arrId) {
            WmsInAsnOrder wmsInAsnOrder = wmsInAsnOrderMapper.selectByPrimaryKey(s);
            if (wmsInAsnOrder.getOrderStatus() >= (byte) 2) {
                throw new BizErrorException("?????????????????????????????????");
            }
            if (StringUtils.isEmpty(wmsInAsnOrder)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            Example example = new Example(WmsInAsnOrderDet.class);
            example.createCriteria().andEqualTo("asnOrderId", s);
            wmsInAsnOrderDetMapper.deleteByExample(example);
        }
        return wmsInAsnOrderMapper.deleteByIds(ids);
    }

    /**
     * ?????????????????????????????????
     *
     * @param palletAutoAsnDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int palletAutoAsnOrder(PalletAutoAsnDto palletAutoAsnDto) {
        SysUser sysUser = currentUser();

        //1-?????? 2-?????? 3-??????
        String materialType = "0";
        //???????????????????????? ???????????????????????????
        SearchBaseLabelMaterial searchBaseLabelMaterial = new SearchBaseLabelMaterial();
        searchBaseLabelMaterial.setMaterialId(palletAutoAsnDto.getMaterialId().toString());
        searchBaseLabelMaterial.setLabelCategoryId("56");
        ResponseEntity<List<BaseLabelMaterialDto>> listResponseEntity = baseFeignApi.findLabelMaterialList(searchBaseLabelMaterial);
        if (listResponseEntity.getCode() != 0) {
            throw new BizErrorException(listResponseEntity.getCode(), listResponseEntity.getMessage());
        }

        //????????????????????????
        SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
        searchBaseStorage.setStorageCode("????????????");
        searchBaseStorage.setCodeQueryMark((byte) 1);
        List<BaseStorage> baseStorageList = baseFeignApi.findList(searchBaseStorage).getData();

        if (!listResponseEntity.getData().isEmpty()) {
            BaseLabelMaterialDto baseLabelMaterialDto = listResponseEntity.getData().get(0);
            switch (baseLabelMaterialDto.getLabelCode()) {
                case "CN01":
                    materialType = "1";
                    break;
                case "CN02":
                    materialType = "2";
                    break;
                case "SEC":
                    materialType = "3";
                    break;
                default:
                    materialType = "4";
            }
        } else {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "????????????????????????");
        }
        //??????redis??????????????????????????????
        Boolean hasKey = redisUtil.hasKey("pallet_id");
        Map<String, Map<String, String>> palletMap = new HashMap<>();
        Map<String, String> asnMap = new HashMap<>();
        //true
        boolean isExist = false;
        if (hasKey) {
            palletMap = (Map<String, Map<String, String>>) redisUtil.get("pallet_id");
            if (palletMap.containsKey(materialType)) {
                asnMap = palletMap.get(materialType);
                isExist = true;
            }
        }
        //???????????????????????????
        if (isExist && asnMap.containsKey(sysUser.getOrganizationId().toString())) {
            Long asnOrderId = Long.parseLong(asnMap.get(sysUser.getOrganizationId().toString()));
            WmsInAsnOrder wmsInAsnOrder = wmsInAsnOrderMapper.selectByPrimaryKey(asnOrderId);
            if (StringUtils.isEmpty(wmsInAsnOrder)) {
                isExist = false;
            }
        }
        if (isExist) {
            Long asnOrderId = Long.parseLong(asnMap.get(sysUser.getOrganizationId().toString()));
            WmsInAsnOrder wmsInAsnOrder = wmsInAsnOrderMapper.selectByPrimaryKey(asnOrderId);
            if (StringUtils.isEmpty(wmsInAsnOrder)) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404);
            }
            wmsInAsnOrder.setProductPalletId(palletAutoAsnDto.getStackingId());
            Example example = new Example(WmsInAsnOrderDet.class);
            example.createCriteria().andEqualTo("asnOrderId", wmsInAsnOrder.getAsnOrderId())
                    .andEqualTo("materialId", palletAutoAsnDto.getMaterialId())
                    .andEqualTo("batchCode", palletAutoAsnDto.getBatchCode())
                    .andEqualTo("sourceOrderId", palletAutoAsnDto.getSourceOrderId());
            WmsInAsnOrderDet wms = wmsInAsnOrderDetMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(wms)) {
                wms.setPackingQty(wms.getPackingQty().add(palletAutoAsnDto.getPackingQty()));
                wms.setActualQty(wms.getActualQty().add(palletAutoAsnDto.getPackingQty()));
                Long statusId = wmsInAsnOrderMapper.findDefaultStatus(ControllerUtil.dynamicCondition("salesCode", palletAutoAsnDto.getSalesOrderCode(), "poCode", palletAutoAsnDto.getSamePackageCode(), "materialId", palletAutoAsnDto.getMaterialId()));
                wms.setInventoryStatusId(statusId);
                wmsInAsnOrderDetMapper.updateByPrimaryKeySelective(wms);
            } else {
                wms = palletAutoAsnDto;
                wms.setWarehouseId(baseStorageList.get(0).getWarehouseId());
                wms.setStorageId(baseStorageList.get(0).getStorageId());
                Long statusId = wmsInAsnOrderMapper.findDefaultStatus(ControllerUtil.dynamicCondition("salesCode", palletAutoAsnDto.getSalesOrderCode(), "poCode", palletAutoAsnDto.getSamePackageCode(), "materialId", palletAutoAsnDto.getMaterialId()));
                wms.setInventoryStatusId(statusId);
                wms.setAsnOrderId(asnOrderId);
                wms.setCreateTime(new Date());
                wms.setCreateUserId(sysUser.getUserId());
                wms.setModifiedTime(new Date());
                wms.setModifiedUserId(sysUser.getUserId());
                wms.setReceivingDate(new Date());
                wms.setLineNumber(wmsInAsnOrderMapper.findLineNumber(wmsInAsnOrder.getAsnOrderId()) + 1);
                wmsInAsnOrderDetMapper.insertUseGeneratedKeys(wms);
            }
            //????????????
            int res = this.addInventory(wmsInAsnOrder.getAsnOrderId(), wms.getAsnOrderDetId(), palletAutoAsnDto.getPackingQty(), (byte) 2);
            wms.setActualQty(palletAutoAsnDto.getActualQty());
            //??????????????????
            res = this.addInventoryDet(palletAutoAsnDto, wmsInAsnOrder.getAsnCode(), wms);
            BaseStorageRule baseStorageRule = new BaseStorageRule();
            baseStorageRule.setLogicId(palletAutoAsnDto.getLogicId());
            baseStorageRule.setMaterialId(palletAutoAsnDto.getMaterialId());
            baseStorageRule.setProLineId(palletAutoAsnDto.getProLineId());
            baseStorageRule.setSalesBarcode(palletAutoAsnDto.getSalesOrderCode());
            baseStorageRule.setPoCode(palletAutoAsnDto.getSamePackageCode());
            baseStorageRule.setQty(palletAutoAsnDto.getPackingQty());
            baseStorageRule.setWorkOrderQty(palletAutoAsnDto.getWorkOrderQty());
            baseStorageRule.setInventoryStatusId(wms.getInventoryStatusId());
            wmsInAsnOrder.setBaseStorageRule(baseStorageRule);
            //?????????????????????
            res = this.createJobOrder(wmsInAsnOrder, wms, palletAutoAsnDto.getBarCodeList());
            return 1;
        } else {
            //???????????????????????????
            WmsInAsnOrder wmsInAsnOrder = WmsInAsnOrder.builder()
                    .materialOwnerId(palletAutoAsnDto.getMaterialOwnerId())
                    .asnCode(CodeUtils.getId("ASN-"))
                    .orderDate(new Date())
                    .modifiedUserId(sysUser.getUserId())
                    .modifiedTime(new Date())
                    .createTime(new Date())
                    .createUserId(sysUser.getUserId())
                    .orderStatus((byte) 3)
                    .orderTypeId((long) 4)
                    .startReceivingDate(new Date())
                    .endReceivingDate(new Date())
                    .productPalletId(palletAutoAsnDto.getStackingId())
                    .orgId(sysUser.getOrganizationId())
                    .remark(DateUtils.getDateString(new Date(), "yyyy-MM-dd"))
                    .materialType(Byte.valueOf(materialType))
                    .build();
            int num = wmsInAsnOrderMapper.insertUseGeneratedKeys(wmsInAsnOrder);
            if (num < 1) {
                throw new BizErrorException("???????????????????????????");
            }
            WmsInAsnOrderDet wmsInAsnOrderDet = new WmsInAsnOrderDet();
            BeanUtil.copyProperties(palletAutoAsnDto, wmsInAsnOrderDet);
            wmsInAsnOrderDet.setWarehouseId(baseStorageList.get(0).getWarehouseId());
            wmsInAsnOrderDet.setStorageId(baseStorageList.get(0).getStorageId());
            Long statusId = wmsInAsnOrderMapper.findDefaultStatus(ControllerUtil.dynamicCondition("salesCode", palletAutoAsnDto.getSalesOrderCode(), "poCode", palletAutoAsnDto.getSamePackageCode(), "materialId", palletAutoAsnDto.getMaterialId()));
            wmsInAsnOrderDet.setInventoryStatusId(statusId);
            wmsInAsnOrderDet.setAsnOrderId(wmsInAsnOrder.getAsnOrderId());
            wmsInAsnOrderDet.setCreateTime(new Date());
            wmsInAsnOrderDet.setCreateUserId(sysUser.getUserId());
            wmsInAsnOrderDet.setModifiedTime(new Date());
            wmsInAsnOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInAsnOrderDet.setReceivingDate(new Date());
            wmsInAsnOrderDet.setOrgId(sysUser.getOrganizationId());
            wmsInAsnOrderDet.setLineNumber(1);
            wmsInAsnOrderDetMapper.insertUseGeneratedKeys(wmsInAsnOrderDet);
            //????????????
            int res = this.addInventory(wmsInAsnOrder.getAsnOrderId(), wmsInAsnOrderDet.getAsnOrderDetId(), palletAutoAsnDto.getPackingQty(), (byte) 2);
            if (res < 1) {
                throw new BizErrorException("??????????????????");
            }
            //??????????????????
            res = this.addInventoryDet(palletAutoAsnDto, wmsInAsnOrder.getAsnCode(), wmsInAsnOrderDet);
            //?????????redis ?????????24??????
            asnMap.put(sysUser.getOrganizationId().toString(), wmsInAsnOrder.getAsnOrderId().toString());
            palletMap.put(materialType, asnMap);
            redisUtil.set("pallet_id", palletMap);
            redisUtil.expire("pallet_id", getRemainSecondsOneDay(new Date()));
            //?????????????????????

            BaseStorageRule baseStorageRule = new BaseStorageRule();
            baseStorageRule.setLogicId(palletAutoAsnDto.getLogicId());
            baseStorageRule.setMaterialId(palletAutoAsnDto.getMaterialId());
            baseStorageRule.setProLineId(palletAutoAsnDto.getProLineId());
            baseStorageRule.setSalesBarcode(palletAutoAsnDto.getSalesOrderCode());
            baseStorageRule.setPoCode(palletAutoAsnDto.getSamePackageCode());
            baseStorageRule.setQty(palletAutoAsnDto.getPackingQty());
            baseStorageRule.setWorkOrderQty(palletAutoAsnDto.getWorkOrderQty());
            baseStorageRule.setInventoryStatusId(wmsInAsnOrderDet.getInventoryStatusId());
            wmsInAsnOrder.setBaseStorageRule(baseStorageRule);
            res = this.createJobOrder(wmsInAsnOrder, wmsInAsnOrderDet, palletAutoAsnDto.getBarCodeList());
            return 1;
        }
    }

    /**
     * ???????????????
     *
     * @param wmsInAsnOrder
     * @param wmsInAsnOrderDet
     * @param barCodeList
     * @return
     */
    private int createJobOrder(WmsInAsnOrder wmsInAsnOrder, WmsInAsnOrderDet wmsInAsnOrderDet, List<BarPODto> barCodeList) {
        WmsInnerJobOrder wmsInnerJobOrder = WmsInnerJobOrder.builder()
                .sourceOrderId(wmsInAsnOrder.getAsnOrderId())
                .materialOwnerId(wmsInAsnOrder.getMaterialOwnerId())
                .orderTypeId(wmsInAsnOrder.getOrderTypeId())
                .jobOrderType((byte) 3)
                .relatedOrderCode(wmsInAsnOrder.getAsnCode())
                .warehouseId(wmsInAsnOrderDet.getWarehouseId())
                .planQty(wmsInAsnOrderDet.getActualQty())
                .orderStatus((byte) 1)
                .orderTypeId(wmsInAsnOrder.getOrderTypeId())
                .actualQty(new BigDecimal("0"))
                .productPalletId(wmsInAsnOrder.getProductPalletId())
                .build();
        if (wmsInAsnOrder.getBaseStorageRule() != null) {
            wmsInnerJobOrder.setBaseStorageRule(wmsInAsnOrder.getBaseStorageRule());
            wmsInnerJobOrder.setOption1(wmsInAsnOrder.getBaseStorageRule().getLogicId() != null ? wmsInAsnOrder.getBaseStorageRule().getLogicId().toString() : null);
            wmsInnerJobOrder.setOption2(wmsInAsnOrder.getBaseStorageRule().getProLineId() != null ? wmsInAsnOrder.getBaseStorageRule().getProLineId().toString() : null);
            wmsInnerJobOrder.setOption3(wmsInAsnOrder.getBaseStorageRule().getSalesBarcode());
            wmsInnerJobOrder.setOption4(wmsInAsnOrder.getBaseStorageRule().getPoCode());
            wmsInnerJobOrder.setOption5(wmsInAsnOrder.getBaseStorageRule().getWorkOrderQty().toString());
        }
        List<WmsInnerJobOrderDet> list = new ArrayList<>();
        WmsInnerJobOrderDet orderDet = WmsInnerJobOrderDet.builder()
                .sourceDetId(wmsInAsnOrderDet.getAsnOrderDetId())
                .materialOwnerId(wmsInAsnOrder.getMaterialOwnerId())
                .outStorageId(wmsInAsnOrderDet.getStorageId())
                .inventoryStatusId(wmsInAsnOrderDet.getInventoryStatusId())
                .materialId(wmsInAsnOrderDet.getMaterialId())
                .batchCode(wmsInAsnOrderDet.getBatchCode())
                .warehouseId(wmsInAsnOrderDet.getWarehouseId())
                .packingUnitName(wmsInAsnOrderDet.getPackingUnitName())
                .planQty(wmsInAsnOrderDet.getActualQty())
                .palletCode(wmsInAsnOrderDet.getPalletCode())
                .orderStatus((byte) 1)
                .option1(StringUtils.isNotEmpty(wmsInAsnOrderDet.getSourceOrderId()) ? wmsInAsnOrderDet.getSourceOrderId().toString() : null)
                .build();
        if (wmsInAsnOrder.getBaseStorageRule() != null) {
            orderDet.setOption2(wmsInAsnOrder.getBaseStorageRule().getSalesBarcode());
            orderDet.setOption3(wmsInAsnOrder.getBaseStorageRule().getPoCode());
        }
        list.add(orderDet);
        wmsInnerJobOrder.setWmsInPutawayOrderDets(list);
        wmsInnerJobOrder.setBarCodeList(barCodeList);
        ResponseEntity responseEntity = innerFeignApi.add(wmsInnerJobOrder);
        if (responseEntity.getCode() != 0) {
            throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
        }
        return 1;
    }

    /**
     * ?????????
     * @param currentDate
     * @return
     */
    private Integer getRemainSecondsOneDay(Date currentDate) {
        //??????plusDays?????????????????????1???????????????????????????0
        LocalDateTime midnight = LocalDateTime.ofInstant(currentDate.toInstant(),
                ZoneId.systemDefault()).plusDays(1).withHour(0).withMinute(0)
                .withSecond(0).withNano(0);
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(currentDate.toInstant(),
                ZoneId.systemDefault());
        //??????ChronoUnit.SECONDS.between?????????????????????LocalDateTime?????????????????????????????????
        long seconds = ChronoUnit.SECONDS.between(currentDateTime, midnight);
        return (int) seconds;
    }

    /**
     * ??????????????????
     *
     * @param productPalletId
     * @return
     */
    private List<String> checkBarcode(Long productPalletId) {
        SearchMesSfcProductPalletDet searchMesSfcProductPalletDet = new SearchMesSfcProductPalletDet();
        searchMesSfcProductPalletDet.setProductPalletId(productPalletId);
//        ResponseEntity<MesSfcProductPallet> responseEntity = sfcFeignApi.detail(productPalletId);
//        if(responseEntity.getCode()!=0){
//            throw new BizErrorException("??????????????????");
//        }
//        String barcode = responseEntity.getData().getPalletCode();
        ResponseEntity<List<MesSfcProductPalletDetDto>> responseEntity = sfcFeignApi.findList(searchMesSfcProductPalletDet);
        if (responseEntity.getCode() != 0) {
            throw new BizErrorException("??????????????????");
        }
        List<MesSfcProductPalletDetDto> mesSfcProductPalletDetDtos = responseEntity.getData();
        //??????????????????
        List<String> barcodeList = new ArrayList<>();
        for (MesSfcProductPalletDetDto mesSfcProductPalletDetDto : mesSfcProductPalletDetDtos) {
            String barcode = wmsInAsnOrderMapper.findBarCode(mesSfcProductPalletDetDtos.get(0).getWorkOrderBarcodeId());
            if (StringUtils.isEmpty(barcode)) {
                throw new BizErrorException("????????????");
            }
            barcodeList.add(barcode);
        }

        if (StringUtils.isEmpty(barcodeList)) {
            throw new BizErrorException("????????????????????????");
        }
        return barcodeList;
    }

    /**
     * ??????????????????????????????
     *
     * @param orderCode
     * @param wmsInAsnOrderDet
     * @return
     */
    private int addInventoryDet(PalletAutoAsnDto palletAutoAsnDto, String orderCode, WmsInAsnOrderDet wmsInAsnOrderDet) {
        //??????????????????
        //List<String> barcodes = this.checkBarcode(productPalletId);
        //String barCode = this.checkBarcode(productPalletId);
        int num = 0;
        for (BarPODto barcode : palletAutoAsnDto.getBarCodeList()) {
            //?????????????????????????????????
            WmsInnerInventoryDet wmsInnerInventoryDet = innerFeignApi.findByDet(barcode.getBarCode()).getData();
            if (StringUtils.isNotEmpty(wmsInnerInventoryDet)) {
                throw new BizErrorException("????????????");
            }
            List<WmsInnerInventoryDet> wmsInnerInventoryDets = new ArrayList<>();
            wmsInnerInventoryDet = new WmsInnerInventoryDet();
            wmsInnerInventoryDet.setStorageId(wmsInAsnOrderDet.getStorageId());
            wmsInnerInventoryDet.setMaterialId(wmsInAsnOrderDet.getMaterialId());
            wmsInnerInventoryDet.setBarcode(barcode.getBarCode());
            wmsInnerInventoryDet.setSalesBarcode(barcode.getSalesBarcode());
            wmsInnerInventoryDet.setCustomerBarcode(barcode.getCutsomerBarcode());
            wmsInnerInventoryDet.setMaterialQty(BigDecimal.ONE);
            wmsInnerInventoryDet.setProductionDate(wmsInAsnOrderDet.getProductionDate());
            wmsInnerInventoryDet.setProductionBatchCode(wmsInnerInventoryDet.getProductionBatchCode());
            wmsInnerInventoryDet.setAsnCode(orderCode);
            wmsInnerInventoryDet.setReceivingDate(new Date());
            wmsInnerInventoryDet.setBarcodeStatus((byte) 2);
            wmsInnerInventoryDet.setOrgId(wmsInAsnOrderDet.getOrgId());
            wmsInnerInventoryDet.setInventoryStatusId(wmsInAsnOrderDet.getInventoryStatusId());
            wmsInnerInventoryDet.setOption1(palletAutoAsnDto.getCustomerName());
            wmsInnerInventoryDet.setOption2(palletAutoAsnDto.getSalesManName());
            wmsInnerInventoryDet.setOption3(palletAutoAsnDto.getSalesOrderCode());
            wmsInnerInventoryDet.setOption4(barcode.getPOCode());
            wmsInnerInventoryDet.setOption5(palletAutoAsnDto.getWorkOrderCode());

            wmsInnerInventoryDet.setIfStockLock((byte) 0);
            wmsInnerInventoryDet.setLockStatus((byte)0);
            wmsInnerInventoryDets.add(wmsInnerInventoryDet);
            ResponseEntity responseEntity = innerFeignApi.add(wmsInnerInventoryDets);
            if (responseEntity.getCode() != 0) {
                throw new BizErrorException("????????????????????????");
            }
            num++;
        }

        return num;
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

    /**
     * ????????????
     *
     * @param map
     * @return
     */
    @Override
    public List<WmsInHtAsnOrderDto> findHtList(Map<String, Object> map) {
        SysUser sysUser = currentUser();
        map.put("orgId", sysUser.getOrganizationId());
        return wmsInHtAsnOrderMapper.findHtList(map);
    }

    /**
     * ??????????????????
     *
     * @param wmsInAsnOrder
     * @return
     */
    private void addHt(WmsInAsnOrder wmsInAsnOrder) {
        WmsInHtAsnOrder wmsInHtAsnOrder = new WmsInHtAsnOrder();
        BeanUtil.copyProperties(wmsInAsnOrder, wmsInHtAsnOrder);
        wmsInHtAsnOrderMapper.insertUseGeneratedKeys(wmsInHtAsnOrder);
        if (StringUtils.isNotEmpty(wmsInAsnOrder.getWmsInAsnOrderDetList())) ;
        for (WmsInAsnOrderDet wmsInAsnOrderDet : wmsInAsnOrder.getWmsInAsnOrderDetList()) {
            WmsInHtAsnOrderDet wmsInHtAsnOrderDet = new WmsInHtAsnOrderDet();
            BeanUtil.copyProperties(wmsInAsnOrderDet, wmsInHtAsnOrderDet);
            wmsInHtAsnOrderDetMapper.insertSelective(wmsInHtAsnOrderDet);
        }
    }
}
