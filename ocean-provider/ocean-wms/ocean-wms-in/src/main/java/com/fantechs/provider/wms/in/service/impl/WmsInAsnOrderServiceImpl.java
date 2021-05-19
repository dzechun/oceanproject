package com.fantechs.provider.wms.in.service.impl;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrderDet;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
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
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
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

    @Override
    public List<WmsInAsnOrderDto> findList(SearchWmsInAsnOrder searchWmsInAsnOrder) {
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


        WmsInAsnOrderDet oldWms = wmsInAsnOrderDetMapper.selectByPrimaryKey(wmsInAsnOrderDet.getAsnOrderDetId());
        BigDecimal countQty = wmsInAsnOrderDet.getActualQty().add(oldWms.getActualQty()!=null?oldWms.getActualQty():new BigDecimal("0"));
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
        wmsInAsnOrderDet.setActualQty(wmsInAsnOrderDet.getActualQty().add(wms.getActualQty()!=null?wms.getActualQty():new BigDecimal("0")));
        wmsInAsnOrderDetMapper.updateByPrimaryKeySelective(wmsInAsnOrderDet);
        //添加库存
        int num = this.addInventory(wmsInAsnOrderDet.getAsnOrderId(), wmsInAsnOrderDet.getAsnOrderDetId());

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

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int writeQty(WmsInAsnOrderDet wmsInAsnOrderDet) {
        WmsInAsnOrderDet wms = wmsInAsnOrderDetMapper.selectByPrimaryKey(wmsInAsnOrderDet.getAsnOrderDetId());
        int num = wmsInAsnOrderDetMapper.updateByPrimaryKeySelective(WmsInAsnOrderDet.builder()
                .asnOrderDetId(wmsInAsnOrderDet.getAsnOrderDetId())
                .putawayQty(wms.getPutawayQty()!=null?wms.getPutawayQty().add(wmsInAsnOrderDet.getPutawayQty()):wmsInAsnOrderDet.getPutawayQty())
                .build());
//        SearchWmsInAsnOrder searchWmsInAsnOrder = new SearchWmsInAsnOrder();
//        searchWmsInAsnOrder.setAsnOrderId(wms.getAsnOrderId());
//        List<WmsInAsnOrderDto> list = this.findList(searchWmsInAsnOrder);
//        for (WmsInAsnOrderDto wmsInAsnOrderDto : list) {
//            if(wmsInAsnOrderDto.getPutawayQty().doubleValue()==wmsInAsnOrderDto.getActualQty().doubleValue()){
//                wmsInAsnOrderMapper.updateByPrimaryKeySelective(WmsInAsnOrder.builder()
//                        .asnOrderId(wmsInAsnOrderDto.getAsnOrderId())
//                        .orderStatus((byte)3)
//                        .build());
//            }
//        }
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
            wmsInnerInventory.setMaterialOwnerName(wmsInAsnOrderDto.getMaterialOwnerName());
            wmsInnerInventory.setRelevanceOrderCode(wmsInAsnOrderDto.getAsnCode());
            wmsInnerInventory.setMaterialId(wmsInAsnOrderDetDto.getMaterialId());
            wmsInnerInventory.setMaterialCode(wmsInAsnOrderDetDto.getMaterialCode());
            wmsInnerInventory.setMaterialName(wmsInAsnOrderDetDto.getMaterialName());
            wmsInnerInventory.setWarehouseName(wmsInAsnOrderDetDto.getWarehouseName());
            wmsInnerInventory.setStorageName(wmsInAsnOrderDetDto.getStorageName());
            wmsInnerInventory.setReceivingDate(new Date());
            wmsInnerInventory.setProductionDate(wmsInAsnOrderDetDto.getProductionDate());
            wmsInnerInventory.setJobStatus((byte)1);
            wmsInnerInventory.setCreateTime(new Date());
            wmsInnerInventory.setCreateUserId(sysUser.getUserId());
            wmsInnerInventory.setModifiedTime(new Date());
            wmsInnerInventory.setModifiedUserId(sysUser.getUserId());
            ResponseEntity responseEntity = innerFeignApi.insertSelective(wmsInnerInventory);
            if(responseEntity.getCode()!=0){
                throw new BizErrorException("库存添加失败");
            }
        }
        return wmsInAsnOrder;
    }

    @Override
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
        map.put("warehouseName",wmsInAsnOrderDetDto.getWarehouseName());
        map.put("storageName",wmsInAsnOrderDetDto.getStorageName());
        WmsInnerInventory wmsInnerInventory = innerFeignApi.selectOneByExample(map).getData();
        if(StringUtils.isEmpty(wmsInnerInventory)){
            //添加库存
            wmsInnerInventory = new WmsInnerInventory();
            wmsInnerInventory.setInventoryStatusId(wmsInAsnOrderDetDto.getInventoryStatusId());
            wmsInnerInventory.setReceivingDate(wmsInAsnOrderDto.getEndReceivingDate());
            wmsInnerInventory.setPackingUnitName(wmsInAsnOrderDetDto.getPackingUnitName());
            wmsInnerInventory.setPackingQty(wmsInAsnOrderDetDto.getActualQty());
            wmsInnerInventory.setPalletCode(wmsInAsnOrderDetDto.getPalletCode());
            wmsInnerInventory.setMaterialOwnerName(wmsInAsnOrderDto.getMaterialOwnerName());
            wmsInnerInventory.setRelevanceOrderCode(wmsInAsnOrderDto.getAsnCode());
            wmsInnerInventory.setMaterialId(wmsInAsnOrderDetDto.getMaterialId());
            wmsInnerInventory.setMaterialCode(wmsInAsnOrderDetDto.getMaterialCode());
            wmsInnerInventory.setMaterialName(wmsInAsnOrderDetDto.getMaterialName());
            wmsInnerInventory.setWarehouseName(wmsInAsnOrderDetDto.getWarehouseName());
            wmsInnerInventory.setStorageName(wmsInAsnOrderDetDto.getStorageName());
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
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        int num = wmsInAsnOrderMapper.insertUseGeneratedKeys(record);
        for (WmsInAsnOrderDet wmsInAsnOrderDet : record.getWmsInAsnOrderDetList()) {
            MesPmWorkOrder mesPmWorkOrder = this.PmQty(wmsInAsnOrderDet.getPackingQty(),wmsInAsnOrderDet.getSourceOrderId());
            mesPmWorkOrder.setInventoryQty(StringUtils.isEmpty(mesPmWorkOrder.getInventoryQty())?wmsInAsnOrderDet.getPackingQty():mesPmWorkOrder.getInventoryQty().add(wmsInAsnOrderDet.getPackingQty()));
            mesPmWorkOrder.setModifiedTime(new Date());
            mesPmWorkOrder.setModifiedUserId(sysUser.getUserId());
            ResponseEntity rs = pmFeignApi.updateInventory(mesPmWorkOrder);
            if(rs.getCode()!=0){
                throw new BizErrorException(rs.getMessage());
            }
            wmsInAsnOrderDet.setAsnOrderId(record.getAsnOrderId());
            wmsInAsnOrderDet.setCreateTime(new Date());
            wmsInAsnOrderDet.setCreateUserId(sysUser.getUserId());
            wmsInAsnOrderDet.setModifiedTime(new Date());
            wmsInAsnOrderDet.setModifiedUserId(sysUser.getUserId());
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
        this.deleteQty(entity.getAsnOrderId());
        for (WmsInAsnOrderDet wmsInAsnOrderDet : entity.getWmsInAsnOrderDetList()) {
            wmsInAsnOrderDet.setAsnOrderDetId(null);
            MesPmWorkOrder mesPmWorkOrder = this.PmQty(wmsInAsnOrderDet.getPackingQty(),wmsInAsnOrderDet.getSourceOrderId());
            mesPmWorkOrder.setInventoryQty(StringUtils.isEmpty(mesPmWorkOrder.getInventoryQty())?wmsInAsnOrderDet.getPackingQty():mesPmWorkOrder.getInventoryQty().add(wmsInAsnOrderDet.getPackingQty()));
            mesPmWorkOrder.setModifiedTime(new Date());
            mesPmWorkOrder.setModifiedUserId(sysUser.getUserId());
            ResponseEntity rs = pmFeignApi.updateInventory(mesPmWorkOrder);
            if(rs.getCode()!=0){
                throw new BizErrorException(rs.getMessage());
            }
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
     * @param wmsInAsnOrderDet
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int palletAutoAsnOrder(WmsInAsnOrderDet wmsInAsnOrderDet) {
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
            Example example = new Example(WmsInAsnOrderDet.class);
            example.createCriteria().andEqualTo("asnOrderId",wmsInAsnOrder.getAsnOrderId()).andEqualTo("materialId",wmsInAsnOrderDet.getMaterialId()).andEqualTo("batchCode",wmsInAsnOrderDet.getBatchCode());
            WmsInAsnOrderDet wms = wmsInAsnOrderDetMapper.selectOneByExample(example);
            if(StringUtils.isNotEmpty(wms)){
                wms.setPackingQty(wms.getPackingQty().add(wmsInAsnOrderDet.getPackingQty()));
                wms.setPutawayQty(wms.getActualQty().add(wmsInAsnOrderDet.getPackingQty()));
            }
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
                    .build();
            int num = wmsInAsnOrderMapper.insertUseGeneratedKeys(wmsInAsnOrder);
            if(num<1){
                throw new BizErrorException("完工入库单生成失败");
            }
            wmsInAsnOrderDet.setAsnOrderId(wmsInAsnOrder.getAsnOrderId());
            wmsInAsnOrderDet.setCreateTime(new Date());
            wmsInAsnOrderDet.setCreateUserId(sysUser.getUserId());
            wmsInAsnOrderDet.setModifiedTime(new Date());
            wmsInAsnOrderDet.setModifiedUserId(sysUser.getUserId());

            //设置新redis 时效为24小时
            redisUtil.set("pallet_id",wmsInAsnOrder.getAsnOrderId());
            redisUtil.expire("pallet_id",getRemainSecondsOneDay(new Date()));
        }
        return 0;
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
