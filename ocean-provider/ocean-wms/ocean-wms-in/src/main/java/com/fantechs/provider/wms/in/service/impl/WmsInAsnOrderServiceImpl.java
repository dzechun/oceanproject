package com.fantechs.provider.wms.in.service.impl;

import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.response.ResponseEntity;
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
                    wmsInAsnOrderDet.setStorageId(storageId);
                    wmsInAsnOrderDet.setInventoryStatusId(inventoryStatusId);
                    wmsInAsnOrderDet.setActualQty(wmsInAsnOrderDet.getPackingQty());
                    wmsInAsnOrderDet.setModifiedTime(new Date());
                    wmsInAsnOrderDet.setModifiedUserId(sysUser.getUserId());

                    WmsInAsnOrderDetDto wmsInAsnOrderDetDto = wmsInAsnOrderDetMapper.findList(SearchWmsInAsnOrderDet.builder()
                            .asnOrderDetId(wmsInAsnOrderDet.getAsnOrderDetId())
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
                    wmsInnerInventory.setJobStatus((byte)1);
                    wmsInnerInventory.setCreateTime(new Date());
                    wmsInnerInventory.setCreateUserId(sysUser.getUserId());
                    wmsInnerInventory.setModifiedTime(new Date());
                    wmsInnerInventory.setModifiedUserId(sysUser.getUserId());
                    ResponseEntity responseEntity = innerFeignApi.insertSelective(wmsInnerInventory);
                    if(responseEntity.getCode()!=0)
                        throw new BizErrorException("库存创建失败");
                    wmsInAsnOrderDetMapper.updateByPrimaryKeySelective(wmsInAsnOrderDet);
                }catch (Exception e){
                    throw new BizErrorException("收货失败");
                }
            }
            wmsInAsnOrder.setStartReceivingDate(new Date());
            wmsInAsnOrder.setEndReceivingDate(new Date());
            wmsInAsnOrder.setModifiedTime(new Date());
            wmsInAsnOrder.setModifiedUserId(sysUser.getUserId());
            wmsInAsnOrder.setOrderStatus((byte)2);
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
        if(StringUtils.isEmpty(wmsInAsnOrderDet.getAsnOrderDetId())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"参数错误");
        }
        WmsInAsnOrderDet wms = wmsInAsnOrderDetMapper.selectByPrimaryKey(wmsInAsnOrderDet.getAsnOrderDetId());
        wmsInAsnOrderDet.setModifiedUserId(sysUser.getUserId());
        wmsInAsnOrderDet.setModifiedTime(new Date());

        //测试
        wmsInAsnOrderDet.setActualQty(wmsInAsnOrderDet.getPutawayQty());
        wmsInAsnOrderDet.setPutawayQty(null);

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
                    .modifiedUserId(sysUser.getUserId())
                    .build());
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
        list = list.stream().filter(li->li.getPackingQty().compareTo(li.getActualQty())==0).collect(Collectors.toList());
        if(list.size()==size){
            //收货结束时间及收货状态
            if(countQty.compareTo(wmsInAsnOrderDet.getPackingQty())==0){
                wmsInAsnOrderMapper.updateByPrimaryKeySelective(WmsInAsnOrder.builder()
                        .asnOrderId(wms.getAsnOrderId())
                        .endReceivingDate(new Date())
                        .orderStatus((byte)2)
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
        SearchWmsInAsnOrder searchWmsInAsnOrder = new SearchWmsInAsnOrder();
        searchWmsInAsnOrder.setAsnOrderId(wms.getAsnOrderId());
        List<WmsInAsnOrderDto> list = this.findList(searchWmsInAsnOrder);
        for (WmsInAsnOrderDto wmsInAsnOrderDto : list) {
            if(wmsInAsnOrderDto.getPutawayQty().doubleValue()==wmsInAsnOrderDto.getActualQty().doubleValue()){
                wmsInAsnOrderMapper.updateByPrimaryKeySelective(WmsInAsnOrder.builder()
                        .asnOrderId(wmsInAsnOrderDto.getAsnOrderId())
                        .orderStatus((byte)3)
                        .build());
            }
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
        Example example = new Example(WmsInAsnOrderDet.class);
        example.createCriteria().andEqualTo("asnOrderId",asnOrderId);
        List<WmsInAsnOrderDet> list = wmsInAsnOrderDetMapper.selectByExample(example);
        List<WmsInnerJobOrderDet> jobOrderDets = new ArrayList<>();
        BigDecimal bigDecimal = new BigDecimal("0");
        for (WmsInAsnOrderDet wms : list) {
            jobOrderDets.add(WmsInnerJobOrderDet.builder()
                    .sourceDetId(wms.getAsnOrderDetId())
                    .materialOwnerId(wmsInAsnOrder.getMaterialOwnerId())
                    .warehouseId(wms.getWarehouseId())
                    .inStorageId(wms.getStorageId())
                    .inventoryStatusId(wms.getInventoryStatusId())
                    .materialId(wms.getMaterialId())
                    .batchCode(wms.getBatchCode())
                    .packingUnitName(wms.getPackingUnitName())
                    .planQty(wms.getActualQty())
                    .palletCode(wms.getPalletCode())
                    .build());
            bigDecimal.add(wms.getActualQty());
        }
        WmsInnerJobOrder wmsInnerJobOrder = WmsInnerJobOrder.builder()
                .sourceOrderId(wmsInAsnOrder.getAsnOrderId())
                .materialOwnerId(wmsInAsnOrder.getMaterialOwnerId())
                .orderTypeId(wmsInAsnOrder.getOrderTypeId())
                .jobOrderType("3")
                .relatedOrderCode(wmsInAsnOrder.getAsnCode())
                .wmsInPutawayOrderDets(jobOrderDets)
                .planQty(bigDecimal)
                .build();
        ResponseEntity responseEntity = innerFeignApi.add(wmsInnerJobOrder);
        if(responseEntity.getCode()!=0){
            throw new BizErrorException("创建作业单失败");
        }
        return 1;
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
        WmsInnerInventory wmsInnerInventory = innerFeignApi.selectOneByExample(map).getData();
        if(StringUtils.isEmpty(wmsInnerInventory)){
            //添加库存
            wmsInnerInventory = new WmsInnerInventory();
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
            wmsInnerInventory = new WmsInnerInventory();
            wmsInnerInventory.setPackingQty(wmsInAsnOrderDetDto.getPackingQty());
            ResponseEntity responseEntity =  innerFeignApi.updateByExampleSelective(wmsInnerInventory,map);
            if(responseEntity.getCode()!=0){
                throw new BizErrorException("添加失败");
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

        //删除原有明细
        wmsInAsnOrderDetMapper.delete(WmsInAsnOrderDet.builder()
                .asnOrderId(entity.getAsnOrderId())
                .build());
        for (WmsInAsnOrderDet wmsInAsnOrderDet : entity.getWmsInAsnOrderDetList()) {
            wmsInAsnOrderDet.setAsnOrderId(entity.getAsnOrderId());
            wmsInAsnOrderDet.setModifiedTime(new Date());
            wmsInAsnOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInAsnOrderDetMapper.insert(wmsInAsnOrderDet);
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        String[] arrId = ids.split(",");
        for (String s : arrId) {
            WmsInAsnOrder wmsInAsnOrder = wmsInAsnOrderMapper.selectByPrimaryKey(s);
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
