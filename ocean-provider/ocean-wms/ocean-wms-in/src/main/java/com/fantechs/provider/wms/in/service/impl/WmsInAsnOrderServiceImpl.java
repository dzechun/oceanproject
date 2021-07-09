package com.fantechs.provider.wms.in.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductPalletDetDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcProductPalletDet;
import com.fantechs.common.base.general.dto.wms.in.PalletAutoAsnDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductPallet;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.om.OmSalesReturnOrderDet;
import com.fantechs.common.base.general.entity.om.OmTransferOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrderDet;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.*;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.api.qms.OMFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.in.WmsInAsnOrderDetDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInAsnOrderDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrderDet;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrder;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInAsnOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInAsnOrder;
import com.fantechs.provider.wms.in.mapper.WmsInAsnOrderDetMapper;
import com.fantechs.provider.wms.in.mapper.WmsInAsnOrderMapper;
import com.fantechs.provider.wms.in.service.WmsInAsnOrderService;
import org.springframework.stereotype.Service;
import com.fantechs.common.base.support.BaseService;
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
 *
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

    @Override
    public List<WmsInAsnOrderDto> findList(SearchWmsInAsnOrder searchWmsInAsnOrder) {
        SysUser sysUser = currentUser();
        searchWmsInAsnOrder.setOrgId(sysUser.getOrganizationId());
        return wmsInAsnOrderMapper.findList(searchWmsInAsnOrder);
    }

    /**
     * 整单收货
     * @param ids
     * @param inventoryStatusId
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int allReceiving(String ids,Long storageId,Long inventoryStatusId) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        int num = 0;
        for (String s : arrId) {
            WmsInAsnOrder wmsInAsnOrder = wmsInAsnOrderMapper.selectByPrimaryKey(s);
            if(wmsInAsnOrder.getOrderStatus()==(byte)3){
                throw new BizErrorException("单据已经收货");
            }
            if(StringUtils.isEmpty(wmsInAsnOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            List<WmsInAsnOrderDet> list = wmsInAsnOrderDetMapper.select(WmsInAsnOrderDet.builder()
                    .asnOrderId(Long.parseLong(s))
                    .build());
            WmsInAsnOrderDto wmsInAsnOrderDto = wmsInAsnOrderMapper.findList(SearchWmsInAsnOrder.builder()
                    .asnOrderId(wmsInAsnOrder.getAsnOrderId())
                    .build()).get(0);
            for (WmsInAsnOrderDet wmsInAsnOrderDet : list) {
                try {
                    wmsInAsnOrderDet.setStorageId(wmsInAsnOrderDet.getStorageId());
                    wmsInAsnOrderDet.setInventoryStatusId(inventoryStatusId);
                    wmsInAsnOrderDet.setActualQty(wmsInAsnOrderDet.getPackingQty());
                    wmsInAsnOrderDet.setModifiedTime(new Date());
                    wmsInAsnOrderDet.setModifiedUserId(sysUser.getUserId());

                    WmsInAsnOrderDetDto wmsInAsnOrderDetDto = wmsInAsnOrderDetMapper.findList(SearchWmsInAsnOrderDet.builder()
                            .asnOrderDetId(wmsInAsnOrderDet.getAsnOrderDetId())
                            .build()).get(0);
                    //添加库存
                    wmsInAsnOrderDetMapper.updateByPrimaryKeySelective(wmsInAsnOrderDet);
                    num+=addInventory(wmsInAsnOrder.getAsnOrderId(), wmsInAsnOrderDet.getAsnOrderDetId());
                }catch (Exception e){
                    throw new BizErrorException("收货失败");
                }
            }
            wmsInAsnOrder.setStartReceivingDate(new Date());
            wmsInAsnOrder.setEndReceivingDate(new Date());
            wmsInAsnOrder.setModifiedTime(new Date());
            wmsInAsnOrder.setModifiedUserId(sysUser.getUserId());
            wmsInAsnOrder.setOrderStatus((byte)3);
            num+=wmsInAsnOrderMapper.updateByPrimaryKeySelective(wmsInAsnOrder);
        }
        return num;
    }

    /**
     * 单一收货
     * @param wmsInAsnOrderDet
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int singleReceiving(WmsInAsnOrderDet wmsInAsnOrderDet) {
        SysUser sysUser = currentUser();

        WmsInAsnOrder wmsInAsnOrder = wmsInAsnOrderMapper.selectByPrimaryKey(wmsInAsnOrderDet.getAsnOrderId());
        if(wmsInAsnOrder.getOrderStatus()==(byte)3){
            throw new BizErrorException("单据已经收货");
        }

        if(StringUtils.isEmpty(wmsInAsnOrderDet.getAsnOrderDetId())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"参数错误");
        }
        WmsInAsnOrderDet wms = wmsInAsnOrderDetMapper.selectByPrimaryKey(wmsInAsnOrderDet.getAsnOrderDetId());
        wmsInAsnOrderDet.setModifiedUserId(sysUser.getUserId());
        wmsInAsnOrderDet.setModifiedTime(new Date());


        //WmsInAsnOrderDet oldWms = wmsInAsnOrderDetMapper.selectByPrimaryKey(wmsInAsnOrderDet.getAsnOrderDetId());
        BigDecimal countQty = wmsInAsnOrderDet.getActualQty().add(wms.getActualQty()!=null?wms.getActualQty():new BigDecimal("0"));
        if(countQty.compareTo(wmsInAsnOrderDet.getPackingQty())==1){
            throw new BizErrorException("收货数量不能大于计划数量");
        }
        //收货开始时间
        if(wms.getActualQty()==null){
            wmsInAsnOrderMapper.updateByPrimaryKeySelective(WmsInAsnOrder.builder()
                    .asnOrderId(wms.getAsnOrderId())
                    .startReceivingDate(new Date())
                    .modifiedTime(new Date())
                    .orderStatus((byte)2)
                    .modifiedUserId(sysUser.getUserId())
                    .build());
            wmsInAsnOrderDet.setReceivingDate(new Date());
        }
        Long storageId = wms.getStorageId();
        Long warehouseId = wms.getWarehouseId();
        if(wmsInAsnOrderDet.getActualQty().compareTo(wmsInAsnOrderDet.getPackingQty())==-1){
       //if((wms.getInventoryStatusId()!=null || wms.getStorageId()!=null) && wmsInAsnOrderDet.getActualQty().compareTo(wmsInAsnOrderDet.getPackingQty())==-1){
           //if(!wmsInAsnOrderDet.getInventoryStatusId().equals(wms.getInventoryStatusId()) || !wmsInAsnOrderDet.getStorageId().equals(wms.getStorageId())){
               wms = new WmsInAsnOrderDet();
               BeanUtil.copyProperties(wmsInAsnOrderDet,wms);
               wms.setAsnOrderDetId(null);
               wms.setPackingQty(wmsInAsnOrderDet.getActualQty());
               wms.setLineNumber(wmsInAsnOrderDet.getLineNumber()+1);
               int num = wmsInAsnOrderDetMapper.insertUseGeneratedKeys(wms);

               wmsInAsnOrderDet.setPackingQty(wmsInAsnOrderDet.getPackingQty().subtract(wmsInAsnOrderDet.getActualQty()));
               wmsInAsnOrderDet.setActualQty(BigDecimal.ZERO);
               wmsInAsnOrderDet.setWarehouseId(warehouseId);
               wmsInAsnOrderDet.setStorageId(storageId);
               wmsInAsnOrderDet.setInventoryStatusId(null);
           //}
       }else{
           wmsInAsnOrderDet.setActualQty(wmsInAsnOrderDet.getActualQty().add(wms.getActualQty()!=null?wms.getActualQty():new BigDecimal("0")));
       }
        //wmsInAsnOrderDet.setActualQty(wmsInAsnOrderDet.getActualQty().add(wms.getActualQty()!=null?wms.getActualQty():new BigDecimal("0")));
        wmsInAsnOrderDetMapper.updateByPrimaryKey(wmsInAsnOrderDet);
       wmsInAsnOrderDet = wms;
        //添加库存
        int num = this.addInventory(wmsInAsnOrderDet.getAsnOrderId(), wmsInAsnOrderDet.getAsnOrderDetId());
        if(num<1){
            throw new BizErrorException("收货失败");
        }
        //判断明细是否全部收货完成
        Example example = new Example(WmsInAsnOrderDet.class);
        example.createCriteria().andEqualTo("asnOrderId",wmsInAsnOrderDet.getAsnOrderId());
        List<WmsInAsnOrderDet> list = wmsInAsnOrderDetMapper.selectByExample(example);
        int size = list.size();
        list = list.stream().filter(li->!StringUtils.isEmpty(li.getActualQty()) && li.getPackingQty().compareTo(li.getActualQty())==0).collect(Collectors.toList());
        if(list.size()==size){
            //收货结束时间及收货状态
            if(countQty.compareTo(wmsInAsnOrderDet.getPackingQty())==0){
                wmsInAsnOrderMapper.updateByPrimaryKeySelective(WmsInAsnOrder.builder()
                        .asnOrderId(wms.getAsnOrderId())
                        .endReceivingDate(new Date())
                        .orderStatus((byte)3)
                        .modifiedTime(new Date())
                        .modifiedUserId(sysUser.getUserId())
                        .build());
            }
        }
        return num;
    }

    /**
     * 单据源收货数量反写
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
                .putawayQty(wms.getPutawayQty()!=null?wms.getPutawayQty().add(wmsInAsnOrderDet.getPutawayQty()):wmsInAsnOrderDet.getPutawayQty())
                .build());
        if(StringUtils.isEmpty(wms.getSourceOrderId(),wmsInAsnOrder.getSourceOrderId())){
            return num;
        }
        //订单数量反写
        switch (wmsInAsnOrder.getOrderTypeId().toString())
        {
            //调拨
            case "3":
                //反写调拨订单状态
                //查询调拨订单下发的所有入库单
                Example example = new Example(WmsInAsnOrderDet.class);
                example.createCriteria().andEqualTo("asnOrderId",wmsInAsnOrder.getAsnOrderId());
                List<WmsInAsnOrderDet> wmsInAsnOrderDets = wmsInAsnOrderDetMapper.selectByExample(example);
                BigDecimal actualQty = wmsInAsnOrderDets.stream()
                        .map(WmsInAsnOrderDet::getActualQty)
                        .reduce(BigDecimal.ZERO,BigDecimal::add);
                BigDecimal putawayQty = wmsInAsnOrderDets.stream()
                        .map(WmsInAsnOrderDet::getPutawayQty)
                        .reduce(BigDecimal.ZERO,BigDecimal::add);
                //数量相等时更改调拨订单状态已完成状态
                if(actualQty.compareTo(putawayQty)==0){
                    OmTransferOrder omTransferOrder = new OmTransferOrder();
                    omTransferOrder.setTransferOrderId(wmsInAsnOrder.getSourceOrderId());
                    omTransferOrder.setOrderStatus((byte)3);
                    ResponseEntity responseEntity = omFeignApi.updateStatus(omTransferOrder);
                    if(responseEntity.getCode()!=0){
                        throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
                    }
                }
                break;
                //完工入库单
            case "4":
                break;
                //销退入库单
            case "5":
                OmSalesReturnOrderDet omSalesReturnOrderDet = new OmSalesReturnOrderDet();
                omSalesReturnOrderDet.setSalesReturnOrderId(wms.getSourceOrderId());
                omSalesReturnOrderDet.setSalesReturnOrderDetId(wms.getOrderDetId());
                omSalesReturnOrderDet.setReceivingQty(wmsInAsnOrderDet.getActualQty());
                ResponseEntity responseEntity = omFeignApi.writeQty(omSalesReturnOrderDet);
                if(responseEntity.getCode()!=0){
                    throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
                }
                break;
                //其他入库单
            case "6":
                break;
        }
        return num;
    }

    /**
     * 生成完工入库单-自动整单确认
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
            //添加库存
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
            wmsInnerInventory.setJobStatus((byte)1);
            wmsInnerInventory.setCreateTime(new Date());
            wmsInnerInventory.setCreateUserId(sysUser.getUserId());
            wmsInnerInventory.setModifiedTime(new Date());
            wmsInnerInventory.setModifiedUserId(sysUser.getUserId());
            wmsInnerInventory.setOrgId(sysUser.getOrganizationId());
            ResponseEntity responseEntity = innerFeignApi.insertSelective(wmsInnerInventory);
            if(responseEntity.getCode()!=0){
                throw new BizErrorException("库存添加失败");
            }
        }
        return wmsInAsnOrder;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int createInnerJobOrder(Long asnOrderId) {
        WmsInAsnOrder wmsInAsnOrder = wmsInAsnOrderMapper.selectByPrimaryKey(asnOrderId);
        if(StringUtils.isEmpty(wmsInAsnOrder)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        if(wmsInAsnOrder.getOrderStatus()!=(byte)3){
            throw new BizErrorException("完工入库单未完成收货");
        }
        Example example = new Example(WmsInAsnOrderDet.class);
        example.createCriteria().andEqualTo("asnOrderId",asnOrderId);
        List<WmsInAsnOrderDet> list = wmsInAsnOrderDetMapper.selectByExample(example);


        //查询是否创建作业单
        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setSourceOrderId(asnOrderId);
        List<WmsInnerJobOrderDto> wmsInnerJobOrderDtos = innerFeignApi.findList(searchWmsInnerJobOrder).getData();
        if(wmsInnerJobOrderDtos.size()>0){
            throw new BizErrorException("已有上架作业单");
        }
        int num = 0;
        Map<Long, List<WmsInAsnOrderDet>> listMap = new HashMap<>();
        //根据仓库id分组(一个仓库对应一个作业单)
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
                    .jobOrderType((byte)3)
                    .relatedOrderCode(wmsInAsnOrder.getAsnCode())
                    .warehouseId(dtoList.get(0).getWarehouseId())
                    .planQty(dtoList.stream().map(WmsInAsnOrderDet::getActualQty).reduce(BigDecimal.ZERO,BigDecimal::add))
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
                        .orderStatus((byte)1)
                        .build());
            }
            wmsInnerJobOrder.setWmsInPutawayOrderDets(wmsInnerJobOrderDets);
            ResponseEntity responseEntity = innerFeignApi.add(wmsInnerJobOrder);
            if (responseEntity.getCode() != 0) {
                throw new BizErrorException("创建作业单失败");
            }
            num++;
        }
        return num;
    }

    private int addInventory(Long asnOrderId,Long asnOrderDetId){
        SysUser sysUser = currentUser();
        WmsInAsnOrderDto wmsInAsnOrderDto = wmsInAsnOrderMapper.findList(SearchWmsInAsnOrder.builder()
                .asnOrderId(asnOrderId)
                .build()).get(0);

        WmsInAsnOrderDetDto wmsInAsnOrderDetDto = wmsInAsnOrderDetMapper.findList(SearchWmsInAsnOrderDet.builder()
                .asnOrderDetId(asnOrderDetId)
                .build()).get(0);

        Map<String,Object> map = new HashMap<>();
        map.put("relevanceOrderCode",wmsInAsnOrderDto.getAsnCode());
        map.put("materialId",wmsInAsnOrderDetDto.getMaterialId());
        map.put("batchCode",wmsInAsnOrderDetDto.getBatchCode());
        map.put("actualQty",wmsInAsnOrderDetDto.getActualQty());
        map.put("warehouseId",wmsInAsnOrderDetDto.getWarehouseId());
        map.put("storageId",wmsInAsnOrderDetDto.getStorageId());
        map.put("inventoryStatusId",wmsInAsnOrderDetDto.getInventoryStatusId());
        WmsInnerInventory wmsInnerInventory = innerFeignApi.selectOneByExample(map).getData();
        if(StringUtils.isEmpty(wmsInnerInventory)){
            //添加库存
            wmsInnerInventory = new WmsInnerInventory();
            if(wmsInAsnOrderDto.getOrderTypeId()==(byte)4){
                wmsInnerInventory.setWorkOrderCode(wmsInAsnOrderDetDto.getWorkOrderCode());
            }
            wmsInnerInventory.setWorkOrderCode(wmsInAsnOrderDetDto.getWorkOrderCode());
            wmsInnerInventory.setInventoryStatusId(wmsInAsnOrderDetDto.getInventoryStatusId());
            wmsInnerInventory.setMaterialOwnerId(wmsInAsnOrderDto.getMaterialOwnerId());
            wmsInnerInventory.setReceivingDate(wmsInAsnOrderDto.getEndReceivingDate());
            wmsInnerInventory.setPackingUnitName(wmsInAsnOrderDetDto.getPackingUnitName());
            wmsInnerInventory.setPackingQty(wmsInAsnOrderDetDto.getActualQty());
            wmsInnerInventory.setPalletCode(wmsInAsnOrderDetDto.getPalletCode());
            wmsInnerInventory.setMaterialOwnerId(wmsInAsnOrderDto.getMaterialOwnerId());
            wmsInnerInventory.setRelevanceOrderCode(wmsInAsnOrderDto.getAsnCode());
            wmsInnerInventory.setMaterialId(wmsInAsnOrderDetDto.getMaterialId());
            wmsInnerInventory.setWarehouseId(wmsInAsnOrderDetDto.getWarehouseId());
            wmsInnerInventory.setStorageId(wmsInAsnOrderDetDto.getStorageId());
            wmsInnerInventory.setBatchCode(wmsInAsnOrderDetDto.getBatchCode());
            wmsInnerInventory.setPackingUnitName(wmsInAsnOrderDetDto.getPackingUnitName());
            wmsInnerInventory.setJobStatus((byte)1);
            wmsInnerInventory.setCreateTime(new Date());
            wmsInnerInventory.setCreateUserId(sysUser.getUserId());
            wmsInnerInventory.setModifiedTime(new Date());
            wmsInnerInventory.setModifiedUserId(sysUser.getUserId());
            ResponseEntity responseEntity =innerFeignApi.insertSelective(wmsInnerInventory);
            if(responseEntity.getCode()!=0){
                throw new BizErrorException("库存添加失败");
            }
            return 1;
        }else{
            //原库存
            BigDecimal qty = wmsInAsnOrderDetDto.getActualQty().subtract(wmsInnerInventory.getPackingQty());
            wmsInnerInventory.setPackingQty(wmsInnerInventory.getPackingQty().add(qty));
            ResponseEntity responseEntity =  innerFeignApi.updateByPrimaryKeySelective(wmsInnerInventory);
            if(responseEntity.getCode()!=0){
                throw new BizErrorException("确认失败");
            }
            return 1;
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(WmsInAsnOrder record) {
        SysUser sysUser = currentUser();
        record.setOrderStatus((byte)1);
        record.setAsnCode(CodeUtils.getId("ASN-"));
        record.setCreateTime(new Date());
        if(StringUtils.isEmpty(record.getOrderTypeId())){
            record.setOrderTypeId((long)4);
        }
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setOrgId(sysUser.getOrganizationId());
        int num = wmsInAsnOrderMapper.insertUseGeneratedKeys(record);
        for (WmsInAsnOrderDet wmsInAsnOrderDet : record.getWmsInAsnOrderDetList()) {
            if(record.getOrderTypeId()==4){
                MesPmWorkOrder mesPmWorkOrder = this.PmQty(wmsInAsnOrderDet.getPackingQty(),wmsInAsnOrderDet.getSourceOrderId());
                mesPmWorkOrder.setInventoryQty(StringUtils.isEmpty(mesPmWorkOrder.getInventoryQty())?wmsInAsnOrderDet.getPackingQty():mesPmWorkOrder.getInventoryQty().add(wmsInAsnOrderDet.getPackingQty()));
                mesPmWorkOrder.setModifiedTime(new Date());
                mesPmWorkOrder.setModifiedUserId(sysUser.getUserId());
                ResponseEntity rs = pmFeignApi.updateInventory(mesPmWorkOrder);
                if(rs.getCode()!=0){
                    throw new BizErrorException(rs.getMessage());
                }
            }
            wmsInAsnOrderDet.setAsnOrderId(record.getAsnOrderId());
            wmsInAsnOrderDet.setCreateTime(new Date());
            wmsInAsnOrderDet.setCreateUserId(sysUser.getUserId());
            wmsInAsnOrderDet.setModifiedTime(new Date());
            wmsInAsnOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInAsnOrderDet.setOrgId(sysUser.getOrganizationId());
            wmsInAsnOrderDetMapper.insert(wmsInAsnOrderDet);
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(WmsInAsnOrder entity) {
        SysUser sysUser = currentUser();
        entity.setModifiedUserId(sysUser.getUserId());
        entity.setModifiedTime(new Date());
        int num = wmsInAsnOrderMapper.updateByPrimaryKeySelective(entity);
        if(entity.getOrderStatus()==(3)){
            throw new BizErrorException("单据已经完成收货，无法修改");
        }
        //删除原有明细
        if(entity.getOrderTypeId()==4){
            this.deleteQty(entity.getAsnOrderId());
        }else{
            Example example = new Example(WmsInAsnOrderDet.class);
            example.createCriteria().andEqualTo("asnOrderId",entity.getAsnOrderId());
            wmsInAsnOrderDetMapper.deleteByExample(example);
        }
        for (WmsInAsnOrderDet wmsInAsnOrderDet : entity.getWmsInAsnOrderDetList()) {
            wmsInAsnOrderDet.setAsnOrderDetId(null);
            if(entity.getOrderTypeId()==4){
                MesPmWorkOrder mesPmWorkOrder = this.PmQty(wmsInAsnOrderDet.getPackingQty(),wmsInAsnOrderDet.getSourceOrderId());
                mesPmWorkOrder.setInventoryQty(StringUtils.isEmpty(mesPmWorkOrder.getInventoryQty())?wmsInAsnOrderDet.getPackingQty():mesPmWorkOrder.getInventoryQty().add(wmsInAsnOrderDet.getPackingQty()));
                mesPmWorkOrder.setModifiedTime(new Date());
                mesPmWorkOrder.setModifiedUserId(sysUser.getUserId());
                ResponseEntity rs = pmFeignApi.updateInventory(mesPmWorkOrder);
                if(rs.getCode()!=0){
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
        return num;
    }

    /**
     * 删除明细恢复工单数量
     * @param asnOrderId
     */
    private void deleteQty(Long asnOrderId){
        Example example = new Example(WmsInAsnOrderDet.class);
        example.createCriteria().andEqualTo("asnOrderId",asnOrderId);
        List<WmsInAsnOrderDet> list = wmsInAsnOrderDetMapper.selectByExample(example);
        for (WmsInAsnOrderDet wms : list) {
            //查询工单反写工单数量
            ResponseEntity<MesPmWorkOrder> responseEntity = pmFeignApi.workOrderDetail(wms.getSourceOrderId());
            if(responseEntity.getCode()!=0){
                throw new BizErrorException("未查询到工单信息");
            }
            MesPmWorkOrder mesPmWorkOrder = responseEntity.getData();
            mesPmWorkOrder.setInventoryQty(mesPmWorkOrder.getInventoryQty().subtract(wms.getPackingQty()));
            ResponseEntity rs = pmFeignApi.updateInventory(mesPmWorkOrder);
            if(rs.getCode()!=0){
                throw new BizErrorException(rs.getMessage());
            }

            wmsInAsnOrderDetMapper.deleteByPrimaryKey(wms.getAsnOrderDetId());
        }
    }

    /**
     * 工单数量检测
     * @param PackingQty
     * @param sourceOrderId
     */
    private MesPmWorkOrder PmQty(BigDecimal PackingQty,Long sourceOrderId){
        Example example = new Example(WmsInAsnOrderDet.class);
        example.createCriteria().andEqualTo("sourceOrderId",sourceOrderId);
        List<WmsInAsnOrderDet> list = wmsInAsnOrderDetMapper.selectByExample(example);

        BigDecimal resultQty = list.stream()
                .map(WmsInAsnOrderDet::getPackingQty)
                .reduce(BigDecimal.ZERO,BigDecimal::add).add(PackingQty);

        //查询工单反写工单数量
        ResponseEntity<MesPmWorkOrder> responseEntity = pmFeignApi.workOrderDetail(sourceOrderId);
        if(responseEntity.getCode()!=0){
            throw new BizErrorException("未查询到工单信息");
        }
        MesPmWorkOrder mesPmWorkOrder = responseEntity.getData();
        if(StringUtils.isEmpty(mesPmWorkOrder.getInventoryQty())){
            mesPmWorkOrder.setInventoryQty(BigDecimal.ZERO);
        }
        if(PackingQty.compareTo(mesPmWorkOrder.getWorkOrderQty())==1 || resultQty.compareTo(mesPmWorkOrder.getWorkOrderQty())==1 ||mesPmWorkOrder.getInventoryQty().add(PackingQty).compareTo(mesPmWorkOrder.getWorkOrderQty())==1){
            throw new BizErrorException("超出工单数量范围");
        }
        return mesPmWorkOrder;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        String[] arrId = ids.split(",");
        for (String s : arrId) {
            WmsInAsnOrder wmsInAsnOrder = wmsInAsnOrderMapper.selectByPrimaryKey(s);
            if(wmsInAsnOrder.getOrderStatus()>=(byte)2){
                throw new BizErrorException("单据已经收货，无法删除");
            }
            if(StringUtils.isEmpty(wmsInAsnOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            Example example = new Example(WmsInAsnOrderDet.class);
            example.createCriteria().andEqualTo("asnOrderId",s);
            wmsInAsnOrderDetMapper.deleteByExample(example);
        }
        return wmsInAsnOrderMapper.deleteByIds(ids);
    }

    /**
     * 栈板作业生成完工入库单
     * @param palletAutoAsnDto
     * @return
     */
    @Override
    public int palletAutoAsnOrder(PalletAutoAsnDto palletAutoAsnDto) {
        SysUser sysUser = currentUser();
        //查询redis是否存储今日入库单号
        Boolean hasKey = redisUtil.hasKey("pallet_id");
        //true
        if(hasKey){
            Long asnOrderId = Long.parseLong(redisUtil.get("pallet_id").toString());
            WmsInAsnOrder wmsInAsnOrder = wmsInAsnOrderMapper.selectByPrimaryKey(asnOrderId);
            if(StringUtils.isEmpty(wmsInAsnOrder)){
                throw new BizErrorException(ErrorCodeEnum.GL9999404);
            }
            wmsInAsnOrder.setProductPalletId(palletAutoAsnDto.getProductPalletId());
            Example example = new Example(WmsInAsnOrderDet.class);
            example.createCriteria().andEqualTo("asnOrderId",wmsInAsnOrder.getAsnOrderId())
                    .andEqualTo("materialId",palletAutoAsnDto.getMaterialId())
                    .andEqualTo("batchCode",palletAutoAsnDto.getBatchCode())
                    .andEqualTo("sourceOrderId", palletAutoAsnDto.getSourceOrderId());
            WmsInAsnOrderDet wms = wmsInAsnOrderDetMapper.selectOneByExample(example);
            if(StringUtils.isNotEmpty(wms)){
                wms.setPackingQty(wms.getPackingQty().add(palletAutoAsnDto.getPackingQty()));
                wms.setActualQty(wms.getActualQty().add(palletAutoAsnDto.getPackingQty()));
                wmsInAsnOrderDetMapper.updateByPrimaryKeySelective(wms);
            }else{
                wms = palletAutoAsnDto;
                wms.setAsnOrderId(asnOrderId);
                wms.setCreateTime(new Date());
                wms.setCreateUserId(sysUser.getUserId());
                wms.setModifiedTime(new Date());
                wms.setModifiedUserId(sysUser.getUserId());
                wms.setReceivingDate(new Date());
                wmsInAsnOrderDetMapper.insertUseGeneratedKeys(wms);
            }
            //更新库存
            int res = this.addInventory(wmsInAsnOrder.getAsnOrderId(),wms.getAsnOrderDetId());
            wms.setActualQty(palletAutoAsnDto.getActualQty());
            //新增库存明细
            res = this.addInventoryDet(wmsInAsnOrder.getProductPalletId(),wmsInAsnOrder.getAsnCode(),wms);
            //新增上架作业单
            res = this.createJobOrder(wmsInAsnOrder,wms);
            return 1;
        }else{
            //生成完工入库单单号
            WmsInAsnOrder wmsInAsnOrder = WmsInAsnOrder.builder()
                    .asnCode(CodeUtils.getId("ASN-"))
                    .orderDate(new Date())
                    .modifiedUserId(sysUser.getUserId())
                    .modifiedTime(new Date())
                    .createTime(new Date())
                    .createUserId(sysUser.getUserId())
                    .orderStatus((byte)3)
                    .orderTypeId((long)4)
                    .startReceivingDate(new Date())
                    .endReceivingDate(new Date())
                    .productPalletId(palletAutoAsnDto.getProductPalletId())
                    .orgId(sysUser.getOrganizationId())
                    .build();
            int num = wmsInAsnOrderMapper.insertUseGeneratedKeys(wmsInAsnOrder);
            if(num<1){
                throw new BizErrorException("完工入库单生成失败");
            }
            WmsInAsnOrderDet wmsInAsnOrderDet = new WmsInAsnOrderDet();
            BeanUtil.copyProperties(palletAutoAsnDto,wmsInAsnOrderDet);
            wmsInAsnOrderDet.setAsnOrderId(wmsInAsnOrder.getAsnOrderId());
            wmsInAsnOrderDet.setCreateTime(new Date());
            wmsInAsnOrderDet.setCreateUserId(sysUser.getUserId());
            wmsInAsnOrderDet.setModifiedTime(new Date());
            wmsInAsnOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInAsnOrderDet.setReceivingDate(new Date());
            wmsInAsnOrderDet.setOrgId(sysUser.getOrganizationId());
            wmsInAsnOrderDetMapper.insertUseGeneratedKeys(wmsInAsnOrderDet);
            //新增库存
            int res = this.addInventory(wmsInAsnOrder.getAsnOrderId(),wmsInAsnOrderDet.getAsnOrderDetId());
            if(res<1){
                throw new BizErrorException("库存添加失败");
            }

            //新增库存明细
            res = this.addInventoryDet(wmsInAsnOrder.getProductPalletId(),wmsInAsnOrder.getAsnCode(),wmsInAsnOrderDet);

            //新增上级作业单
            res = this.createJobOrder(wmsInAsnOrder,wmsInAsnOrderDet);
            //设置新redis 时效为24小时
            redisUtil.set("pallet_id",wmsInAsnOrder.getAsnOrderId());
            redisUtil.expire("pallet_id",getRemainSecondsOneDay(new Date()));

            return 1;
        }
    }

    /**
     * 创建作业单
     * @param wmsInAsnOrder
     * @param wmsInAsnOrderDet
     * @return
     */
    private int  createJobOrder(WmsInAsnOrder wmsInAsnOrder,WmsInAsnOrderDet wmsInAsnOrderDet){
        WmsInnerJobOrder wmsInnerJobOrder = WmsInnerJobOrder.builder()
                .sourceOrderId(wmsInAsnOrder.getAsnOrderId())
                .materialOwnerId(wmsInAsnOrder.getMaterialOwnerId())
                .orderTypeId(wmsInAsnOrder.getOrderTypeId())
                .jobOrderType((byte)3)
                .relatedOrderCode(wmsInAsnOrder.getAsnCode())
                .warehouseId(wmsInAsnOrderDet.getWarehouseId())
                .planQty(wmsInAsnOrderDet.getActualQty())
                .orderStatus((byte) 1)
                .orderTypeId(wmsInAsnOrder.getOrderTypeId())
                .actualQty(new BigDecimal("0"))
                .productPalletId(wmsInAsnOrder.getProductPalletId())
                .build();
        List<WmsInnerJobOrderDet> list = new ArrayList<>();
        list.add(WmsInnerJobOrderDet.builder()
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
                .orderStatus((byte)1)
                .build());
        wmsInnerJobOrder.setWmsInPutawayOrderDets(list);
        ResponseEntity responseEntity = innerFeignApi.add(wmsInnerJobOrder);
        if(responseEntity.getCode()!=0){
            throw new BizErrorException("上架作业单生成失败");
        }
        return 1;
    }

    /**
     * 时间戳
     * @param currentDate
     * @return
     */
    private Integer getRemainSecondsOneDay(Date currentDate) {
        //使用plusDays加传入的时间加1天，将时分秒设置成0
        LocalDateTime midnight = LocalDateTime.ofInstant(currentDate.toInstant(),
                ZoneId.systemDefault()).plusDays(1).withHour(0).withMinute(0)
                .withSecond(0).withNano(0);
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(currentDate.toInstant(),
                ZoneId.systemDefault());
        //使用ChronoUnit.SECONDS.between方法，传入两个LocalDateTime对象即可得到相差的秒数
        long seconds = ChronoUnit.SECONDS.between(currentDateTime, midnight);
        return (int) seconds;
    }

    /**
     * 获取工单条码
     * @param productPalletId
     * @return
     */
    private String checkBarcode(Long productPalletId){
//        SearchMesSfcProductPalletDet searchMesSfcProductPalletDet = new SearchMesSfcProductPalletDet();
//        searchMesSfcProductPalletDet.setProductPalletId(productPalletId);
        ResponseEntity<MesSfcProductPallet> responseEntity = sfcFeignApi.detail(productPalletId);
        if(responseEntity.getCode()!=0){
            throw new BizErrorException("检验条码失败");
        }
        String barcode = responseEntity.getData().getPalletCode();
//        ResponseEntity<List<MesSfcProductPalletDetDto>> responseEntity = sfcFeignApi.findList(searchMesSfcProductPalletDet);
//        if(responseEntity.getCode()!=0){
//            throw new BizErrorException("检验条码失败");
//        }
//        List<MesSfcProductPalletDetDto> mesSfcProductPalletDetDtos = responseEntity.getData();
        //获取工单条码
        //String barcode = wmsInAsnOrderMapper.findBarCode(mesSfcProductPalletDetDtos.get(0).getWorkOrderBarcodeId());
        if(StringUtils.isEmpty(barcode)){
            throw new BizErrorException("获取工单条码失败");
        }
        return barcode;
    }

    /**
     * 收货确认新增库存明细
     * @param productPalletId
     * @param orderCode
     * @param wmsInAsnOrderDet
     * @return
     */
    private int addInventoryDet(Long productPalletId,String orderCode,WmsInAsnOrderDet wmsInAsnOrderDet){
        //获取工单条码
        String barCode = this.checkBarcode(productPalletId);
        //按条码查询是否存在库存
        WmsInnerInventoryDet wmsInnerInventoryDet = innerFeignApi.findByDet(barCode).getData();
        if(StringUtils.isNotEmpty(wmsInnerInventoryDet)){
            throw new BizErrorException("重复入库");
        }
        List<WmsInnerInventoryDet> wmsInnerInventoryDets = new ArrayList<>();
        wmsInnerInventoryDet= new WmsInnerInventoryDet();
        wmsInnerInventoryDet.setStorageId(wmsInAsnOrderDet.getStorageId());
        wmsInnerInventoryDet.setMaterialId(wmsInAsnOrderDet.getMaterialId());
        wmsInnerInventoryDet.setBarcode(barCode);
        wmsInnerInventoryDet.setMaterialQty(wmsInAsnOrderDet.getActualQty());
        wmsInnerInventoryDet.setInTime(new Date());
        wmsInnerInventoryDet.setProductionDate(wmsInAsnOrderDet.getProductionDate());
        wmsInnerInventoryDet.setProductionBatchCode(wmsInnerInventoryDet.getProductionBatchCode());
        wmsInnerInventoryDet.setRelatedOrderCode(orderCode);
        wmsInnerInventoryDets.add(wmsInnerInventoryDet);
        ResponseEntity responseEntity = innerFeignApi.add(wmsInnerInventoryDets);
        if(responseEntity.getCode()!=0){
            throw new BizErrorException("生成库存明细失败");
        }
        return 1;
    }

    /**
     * 获取当前登录用户
     * @return
     */
    private SysUser currentUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}
