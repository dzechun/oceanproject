package com.fantechs.provider.wms.inner.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrderDet;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.wms.out.OutFeignApi;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerJobOrderDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerJobOrderMapper;
import com.fantechs.provider.wms.inner.service.PickingOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author mr.lei
 * @Date 2021/5/10
 */
@Service
public class PickingOrderServiceImpl implements PickingOrderService {

    @Resource
    private WmsInnerJobOrderMapper wmsInnerJobOrderMapper;
    @Resource
    private WmsInnerJobOrderDetMapper wmsInnerJobOrderDetMapper;
    @Resource
    private WmsInnerInventoryMapper wmsInnerInventoryMapper;
    @Resource
    private OutFeignApi outFeignApi;
    @Resource
    private RedisUtil redisUtil;

    private String REDIS_KEY = "PICKINGID:";

    @Override
    public int scanAffirmQty(Long jobOrderId,String barCode) {
        SysUser sysUser = currentUser();

        //检验条码

        WmsInnerJobOrderDet wmsInnerJobOrderDet = wmsInnerJobOrderDetMapper.selectByPrimaryKey(jobOrderId);
        wmsInnerJobOrderDet.setActualQty(wmsInnerJobOrderDet.getActualQty().add(new BigDecimal("1")));
            wmsInnerJobOrderDet.setModifiedTime(new Date());
            wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
            int num =allocationInv(wmsInnerJobOrderDet);
        return num;
    }
    /**
     * 自动分配
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int autoDistribution(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        int num = 0;
        for (String id : arrId) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(id);
            if(wmsInnerJobOrder.getOrderStatus()==(byte)3){
                throw new BizErrorException("单据已分配完成");
            }
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId",wmsInnerJobOrder.getJobOrderId());
            List<WmsInnerJobOrderDet> list = wmsInnerJobOrderDetMapper.selectByExample(example);
            for (WmsInnerJobOrderDet wms : list) {
                if(StringUtils.isEmpty(wms)){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                }
                //推荐库位
                Long storageId = wmsInnerJobOrderMapper.findStorage(wms.getMaterialId(),wmsInnerJobOrder.getWarehouseId());
                storageId = storageId==null?wmsInnerJobOrderMapper.SelectStorage():storageId;
                if(StringUtils.isEmpty(storageId)){
                    throw new BizErrorException("位查询到推荐库位");
                }
                num+=wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(WmsInnerJobOrderDet.builder()
                        .jobOrderDetId(wms.getJobOrderDetId())
                        .outStorageId(storageId)
                        .distributionQty(wms.getPlanQty())
                        .modifiedUserId(sysUser.getUserId())
                        .modifiedTime(new Date())
                        .orderStatus((byte)3)
                        .build());
                //库位容量减1
                //baseFeignApi.minusSurplusCanPutSalver(wms.getInStorageId(),1);

                //num+= this.DistributionInventory(wmsInnerJobOrder,wms);
                WmsInnerJobOrderDet wmsInnerJobOrderDet = wmsInnerJobOrderDetMapper.selectByPrimaryKey(wms.getJobOrderDetId());
                //分配库存
                num += this.DistributionInventory(wmsInnerJobOrder, wmsInnerJobOrderDet);
            }

            wmsInnerJobOrder.setOrderStatus((byte)3);
            wmsInnerJobOrder.setModifiedTime(new Date());
            wmsInnerJobOrder.setModifiedUserId(sysUser.getUserId());
            wmsInnerJobOrderMapper.updateByPrimaryKeySelective(wmsInnerJobOrder);
        }
        return num;
    }

    /**
     * 手动分配
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int handDistribution(List<WmsInnerJobOrderDet> list) {
        SysUser sysUser = currentUser();
        int num=0;
        for (WmsInnerJobOrderDet wmsInPutawayOrderDet : list) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(wmsInPutawayOrderDet.getJobOrderId());
            if(!StringUtils.isEmpty(wmsInPutawayOrderDet.getDistributionQty()) && wmsInPutawayOrderDet.getDistributionQty().doubleValue()>wmsInPutawayOrderDet.getPlanQty().doubleValue()){
                throw new BizErrorException("分配数量不能大于计划数量");
            }
            WmsInnerJobOrderDet ss = wmsInnerJobOrderDetMapper.selectByPrimaryKey(wmsInPutawayOrderDet.getJobOrderDetId());
            if(ss.getOrderStatus()>2){
                throw new BizErrorException("产品已分配完成");
            }
            WmsInnerJobOrderDet ws = new WmsInnerJobOrderDet();
            if(StringUtils.isEmpty(wmsInPutawayOrderDet.getDistributionQty()) || wmsInPutawayOrderDet.getDistributionQty().compareTo(wmsInPutawayOrderDet.getPlanQty())==-1){
                //分配中
                WmsInnerJobOrderDet wms = new WmsInnerJobOrderDet();
                BeanUtil.copyProperties(wmsInPutawayOrderDet,wms);
                wms.setJobOrderDetId(null);
                wms.setPlanQty(wmsInPutawayOrderDet.getDistributionQty());
                wms.setDistributionQty(wmsInPutawayOrderDet.getDistributionQty());
                wms.setOrderStatus((byte)3);
                num+=wmsInnerJobOrderDetMapper.insertUseGeneratedKeys(wms);
                ws = wms;

                wmsInPutawayOrderDet.setOrderStatus((byte)1);
                wmsInPutawayOrderDet.setDistributionQty(null);
                wmsInPutawayOrderDet.setOutStorageId(null);
                wmsInPutawayOrderDet.setPlanQty(new BigDecimal(wmsInPutawayOrderDet.getPlanQty().doubleValue()-wms.getDistributionQty().doubleValue()));
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                num += wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
            }else if(wmsInPutawayOrderDet.getDistributionQty().compareTo(wmsInPutawayOrderDet.getPlanQty())==0){
                //分配完成
                wmsInPutawayOrderDet.setOrderStatus((byte)3);
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                num += wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
                ws = wmsInPutawayOrderDet;
            }
            SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
            searchWmsInnerJobOrderDet.setJobOrderId(wmsInPutawayOrderDet.getJobOrderId());
            List<WmsInnerJobOrderDetDto> dto = wmsInnerJobOrderDetMapper.findList(searchWmsInnerJobOrderDet);

            //分配库存
            num += this.DistributionInventory(wmsInnerJobOrder, ws);

            if(StringUtils.isEmpty(dto)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            if(dto.stream().filter(li->li.getOrderStatus()==(byte)3).collect(Collectors.toList()).size()==dto.size()){
                //更新表头状态
                wmsInnerJobOrderMapper.updateByPrimaryKeySelective(WmsInnerJobOrder.builder()
                        .jobOrderId(wmsInPutawayOrderDet.getJobOrderId())
                        .orderStatus((byte)3)
                        .build());
            }else{
                wmsInnerJobOrderMapper.updateByPrimaryKeySelective(WmsInnerJobOrder.builder()
                        .jobOrderId(wmsInPutawayOrderDet.getJobOrderId())
                        .orderStatus((byte)2)
                        .build());
            }
        }
        return num;
    }

    /**
     * 取消分配
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int cancelDistribution(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        int num = 0;
        for (String s : arrId) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(s);
            if(StringUtils.isEmpty(wmsInnerJobOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            if(wmsInnerJobOrder.getOrderStatus()==(byte)4 || wmsInnerJobOrder.getOrderStatus()==(byte)5){
                throw new BizErrorException("单据作业中，无法取消");
            }
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId",s);
            List<WmsInnerJobOrderDet> list = wmsInnerJobOrderDetMapper.selectByExample(example);
            Map<Long,List<WmsInnerJobOrderDet>> map = new HashMap<>();
            for (WmsInnerJobOrderDet wmsInnerJobOrderDet : list) {
                if(wmsInnerJobOrderDet.getOrderStatus()==(byte)4){
                    throw new BizErrorException("单据作业中 无法取消");
                }
                if(map.containsKey(wmsInnerJobOrderDet.getMaterialId())){
                    List<WmsInnerJobOrderDet> nm = new ArrayList<>();
                    for (WmsInnerJobOrderDet innerJobOrderDet : map.get(wmsInnerJobOrderDet.getMaterialId())) {
                        innerJobOrderDet.setPlanQty(innerJobOrderDet.getPlanQty().add(wmsInnerJobOrderDet.getPlanQty()));
                        nm.add(innerJobOrderDet);
                    }
                    map.put(wmsInnerJobOrderDet.getMaterialId(),nm);
                }else{
                    List<WmsInnerJobOrderDet> list1 = new ArrayList<>();
                    list1.add(wmsInnerJobOrderDet);
                    map.put(wmsInnerJobOrderDet.getMaterialId(),list1);
                }

                //恢复库存数量
                if(wmsInnerJobOrderDet.getOrderStatus()==(byte)3){
                    int res = this.reconver(wmsInnerJobOrderDet.getJobOrderDetId());
                }
                //删除分配库存
                Example example1 = new Example(WmsInnerInventory.class);
                example1.createCriteria().andEqualTo("jobOrderDetId",wmsInnerJobOrderDet.getJobOrderDetId()).andEqualTo("jobStatus",(byte)1).andEqualTo("relevanceOrderCode",wmsInnerJobOrder.getJobOrderCode());
                wmsInnerInventoryMapper.deleteByExample(example1);
            }
            wmsInnerJobOrderDetMapper.deleteByExample(example);
            for (List<WmsInnerJobOrderDet> value : map.values()) {
                for (WmsInnerJobOrderDet wmsInnerJobOrderDet : value) {
                    wmsInnerJobOrderDet.setDistributionQty(null);
                    wmsInnerJobOrderDet.setOutStorageId(null);
                    wmsInnerJobOrderDet.setOrderStatus((byte)1);
                    wmsInnerJobOrderDet.setModifiedTime(new Date());
                    wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
                    wmsInnerJobOrderDet.setOrderStatus((byte)1);
                    num +=wmsInnerJobOrderDetMapper.insertSelective(wmsInnerJobOrderDet);
                }
            }
            wmsInnerJobOrder.setOrderStatus((byte)1);
            num +=wmsInnerJobOrderMapper.updateByPrimaryKeySelective(wmsInnerJobOrder);
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int allReceiving(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        int num = 0;
        for (String id : arrId) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(id);
            if (wmsInnerJobOrder.getOrderStatus()<(byte) 3) {
                throw new BizErrorException("未分配完成,无法拣货");
            }
            double total = 0.00;
            if (wmsInnerJobOrder.getOrderStatus() == (byte) 5) {
                throw new BizErrorException("单据确认已完成");
            }
            Long jobOrderDetId = null;
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId", wmsInnerJobOrder.getJobOrderId());
            List<WmsInnerJobOrderDet> wmsInnerJobOrderDets = wmsInnerJobOrderDetMapper.selectByExample(example);
            for (WmsInnerJobOrderDet wmsInnerJobOrderDet : wmsInnerJobOrderDets) {
                if(wmsInnerJobOrderDet.getOrderStatus()==(byte)3) {
                    if (StringUtils.isEmpty(id)) {
                        throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                    }
                    SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
                    searchWmsInnerJobOrderDet.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
                    WmsInnerJobOrderDetDto oldDto = wmsInnerJobOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);

                    total += wmsInnerJobOrderDet.getDistributionQty().doubleValue();
                    num += wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(WmsInnerJobOrderDet.builder()
                            .jobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId())
                            .orderStatus((byte) 5)
                            .actualQty(wmsInnerJobOrderDet.getDistributionQty())
                            .modifiedUserId(sysUser.getUserId())
                            .modifiedTime(new Date())
                            .build());

                    //更改库存为正常状态
                    SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
                    searchWmsInnerJobOrder.setJobOrderId(wmsInnerJobOrderDet.getJobOrderId());
                    WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInnerJobOrderMapper.findList(searchWmsInnerJobOrder).get(0);
                    searchWmsInnerJobOrderDet.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
                    WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto = wmsInnerJobOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);

                    //拣货作业更改库存
                    num+= this.Inventory(oldDto,wmsInnerJobOrderDetDto);

                    wmsInnerJobOrderDet.setActualQty(wmsInnerJobOrderDet.getDistributionQty());
                    //反写出库单拣货数量
                    num += this.writeDeliveryOrderQty(wmsInnerJobOrderDet);

                    //清除redis
                    this.removeRedis(wmsInnerJobOrderDet.getJobOrderDetId());
                }
            }
            BigDecimal resultQty = wmsInnerJobOrderDets.stream()
                    .map(WmsInnerJobOrderDet::getDistributionQty)
                    .reduce(BigDecimal.ZERO,BigDecimal::add);
                //更改表头为作业完成状态
                wmsInnerJobOrderMapper.updateByPrimaryKeySelective(WmsInnerJobOrder.builder()
                        .orderStatus((byte) 5)
                        .jobOrderId(wmsInnerJobOrder.getJobOrderId())
                        .actualQty(resultQty)
                        .workStartTime(new Date())
                        .workEndtTime(new Date())
                        .build());
            }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int singleReceiving(List<WmsInnerJobOrderDet> wmsInPutawayOrderDets) {
        SysUser sysUser = currentUser();
        int num = 0;
        for (WmsInnerJobOrderDet wmsInPutawayOrderDet : wmsInPutawayOrderDets) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(wmsInPutawayOrderDet.getJobOrderId());
            if(wmsInnerJobOrder.getOrderStatus()==(byte)5){
                throw new BizErrorException("单据确认已完成");
            }

            SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
            searchWmsInnerJobOrderDet.setJobOrderDetId(wmsInPutawayOrderDet.getJobOrderDetId());
            WmsInnerJobOrderDetDto oldDto = wmsInnerJobOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);

            BigDecimal aqty = wmsInPutawayOrderDet.getActualQty();
            Long jobOrderDetId = null;
            if(wmsInPutawayOrderDet.getActualQty().compareTo(wmsInPutawayOrderDet.getDistributionQty())==-1){
                WmsInnerJobOrderDet wms = new WmsInnerJobOrderDet();
                BeanUtil.copyProperties(wmsInPutawayOrderDet,wms);
                wms.setJobOrderDetId(null);
                wms.setPlanQty(wmsInPutawayOrderDet.getActualQty());
                wms.setDistributionQty(wmsInPutawayOrderDet.getActualQty());
                wms.setOrderStatus((byte)5);
                num+=wmsInnerJobOrderDetMapper.insertUseGeneratedKeys(wms);
                jobOrderDetId = wms.getJobOrderDetId();

                wmsInPutawayOrderDet.setOrderStatus((byte)3);
                wmsInPutawayOrderDet.setInStorageId(null);
                wmsInPutawayOrderDet.setPlanQty(wmsInPutawayOrderDet.getPlanQty().subtract(wms.getPlanQty()));
                wmsInPutawayOrderDet.setDistributionQty(wmsInPutawayOrderDet.getDistributionQty().subtract(wms.getDistributionQty()));
                wmsInPutawayOrderDet.setActualQty(null);
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                num+=wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
            }else if(wmsInPutawayOrderDet.getDistributionQty().compareTo(wmsInPutawayOrderDet.getPlanQty())==0){
                //确认完成
                wmsInPutawayOrderDet.setOrderStatus((byte)5);
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                num += wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
                jobOrderDetId = wmsInPutawayOrderDet.getJobOrderDetId();
            }
            //更改库存
            SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
            searchWmsInnerJobOrder.setJobOrderId(wmsInPutawayOrderDet.getJobOrderId());
            WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInnerJobOrderMapper.findList(searchWmsInnerJobOrder).get(0);
            WmsInnerJobOrderDet Det = wmsInnerJobOrderDetMapper.selectByPrimaryKey(jobOrderDetId);

            //拣货作业更改库存
            searchWmsInnerJobOrderDet.setJobOrderDetId(jobOrderDetId);
            List<WmsInnerJobOrderDetDto> wmsInner = wmsInnerJobOrderDetMapper.findList(searchWmsInnerJobOrderDet);
            num+= this.Inventory(oldDto,wmsInner.get(0));

            //反写出库单拣货数量
            num+=this.writeDeliveryOrderQty(Det);
            //清除redis
            this.removeRedis(wmsInPutawayOrderDet.getJobOrderDetId());


            WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
            wmsInnerJobOrderDet.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
            int count = wmsInnerJobOrderDetMapper.selectCount(wmsInnerJobOrderDet);
            wmsInnerJobOrderDet.setOrderStatus((byte)5);
            int oCount = wmsInnerJobOrderDetMapper.selectCount(wmsInnerJobOrderDet);


            if(oCount==count){
                WmsInnerJobOrder ws = wmsInnerJobOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDto.getJobOrderId());
                ws.setOrderStatus((byte)5);
                if(StringUtils.isEmpty(ws.getActualQty())){
                    ws.setActualQty(new BigDecimal("0.00"));
                }
                ws.setActualQty(ws.getActualQty().add(aqty));
                ws.setModifiedUserId(sysUser.getUserId());
                ws.setModifiedTime(new Date());
                ws.setWorkEndtTime(new Date());
                num +=wmsInnerJobOrderMapper.updateByPrimaryKeySelective(ws);
            }else{
                WmsInnerJobOrder ws = wmsInnerJobOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDto.getJobOrderId());
                ws.setActualQty(wmsInPutawayOrderDet.getActualQty());
                ws.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
                if(StringUtils.isEmpty(ws.getActualQty())){
                    ws.setActualQty(new BigDecimal("0.00"));
                }
                ws.setActualQty(ws.getActualQty().add(aqty));
                ws.setOrderStatus((byte)4);
                ws.setModifiedUserId(sysUser.getUserId());
                ws.setModifiedTime(new Date());
                if(StringUtils.isEmpty(wmsInnerJobOrder.getWorkStartTime())){
                    ws.setWorkStartTime(new Date());
                }
                num +=wmsInnerJobOrderMapper.updateByPrimaryKeySelective(ws);
            }
        }
        return num;
    }

    @Override
    public List<WmsInnerJobOrderDto> findList(SearchWmsInnerJobOrder searchWmsInnerJobOrder) {
        return wmsInnerJobOrderMapper.findList(searchWmsInnerJobOrder);
    }

    /**
     * 确认收货
     * 判断确认时移出库位是否跟分配移出库位一致，不一致时新建一条库存，扣减分配库存数量
     * @param wmsInnerJobOrderDet
     * @return
     */
    private int allocationInv(WmsInnerJobOrderDet wmsInnerJobOrderDet){
        SysUser sysUser = currentUser();
        int num = 0;
        Example example = new Example(WmsInnerInventory.class);
        example.createCriteria().andEqualTo("warehouseId",wmsInnerJobOrderDet.getWarehouseId()).andEqualTo("storageId",wmsInnerJobOrderDet.getOutStorageId())
                .andEqualTo("materialId",wmsInnerJobOrderDet.getMaterialId()).andEqualTo("batchCode",wmsInnerJobOrderDet.getBatchCode());
        List<WmsInnerInventory> list = wmsInnerInventoryMapper.selectByExample(example);
        if(list.size()<1){
            throw new BizErrorException("未查询到库存");
        }
        //WmsInnerInventory oldInventory = list.get(0);

        WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDet.getJobOrderId());
        if(StringUtils.isEmpty(wmsInnerJobOrder)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        example.clear();
        example.createCriteria().andEqualTo("relevanceOrderCode",wmsInnerJobOrder.getJobOrderCode()).andEqualTo("warehouseName",wmsInnerJobOrderDet).andEqualTo("storageId",wmsInnerJobOrderDet.getOutStorageId())
                .andEqualTo("materialId",wmsInnerJobOrderDet.getMaterialId()).andEqualTo("batchCode",wmsInnerJobOrderDet.getBatchCode());
        List<WmsInnerInventory> newList = wmsInnerInventoryMapper.selectByExample(example);
        if(list.get(0).getPackingQty().compareTo(wmsInnerJobOrderDet.getActualQty())==-1){
            throw new BizErrorException("库存不足");
        }
        if(newList.size()<1){
            //新增代出库存
            WmsInnerInventory newInventory = new WmsInnerInventory();
            BeanUtil.copyProperties(list.get(0),newInventory);
            newInventory.setJobStatus((byte)3);
            newInventory.setInventoryId(null);
            newInventory.setCreateTime(new Date());
            newInventory.setCreateUserId(sysUser.getUserId());
            newInventory.setModifiedTime(new Date());
            newInventory.setPackingQty(wmsInnerJobOrderDet.getActualQty());
            newInventory.setModifiedUserId(sysUser.getUserId());
            num += wmsInnerInventoryMapper.insertSelective(newInventory);

            //扣除原库存
           WmsInnerInventory oldInventory = list.get(0);
           oldInventory.setPackingQty(oldInventory.getPackingQty().subtract(wmsInnerJobOrderDet.getActualQty()));
           wmsInnerInventoryMapper.updateByPrimaryKeySelective(oldInventory);
        }else{
            WmsInnerInventory newInventory = new WmsInnerInventory();
            newInventory.setInventoryId(newList.get(0).getInventoryId());
            newInventory.setPackingQty(wmsInnerJobOrderDet.getActualQty());
            newInventory.setModifiedUserId(sysUser.getUserId());
            newInventory.setModifiedTime(new Date());
            num+=wmsInnerInventoryMapper.updateByPrimaryKeySelective(newInventory);

            WmsInnerInventory oldInventory = list.get(0);
            oldInventory.setPackingQty(oldInventory.getPackingQty().subtract(wmsInnerJobOrderDet.getActualQty()));
            wmsInnerInventoryMapper.updateByPrimaryKeySelective(oldInventory);
        }
        return num;
    }

    /**
     * 废
     * @param wmsInnerJobOrderDet
     * @return
     */
    private int moveInventory(WmsInnerJobOrderDet wmsInnerJobOrderDet){
        SysUser sysUser = currentUser();
        int num = 0;
        String newStorageName = wmsInnerJobOrderMapper.findStorageName(wmsInnerJobOrderDet.getOutStorageId());
        WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDet.getJobOrderId());
        if(StringUtils.isEmpty(wmsInnerJobOrder)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        WmsInnerJobOrderDet oldDet = wmsInnerJobOrderDetMapper.selectByPrimaryKey(wmsInnerJobOrderDet.getJobOrderDetId());
        String oldStorageName = wmsInnerJobOrderMapper.findStorageName(oldDet.getOutStorageId());
        Example example = new Example(WmsInnerInventory.class);
        //分配库位
        example.createCriteria().andEqualTo("relevanceOrderCode",wmsInnerJobOrder.getJobOrderCode()).andEqualTo("warehouseName",wmsInnerJobOrderDet.getWarehouseId()).andEqualTo("storageName",oldStorageName)
                .andEqualTo("materialId",wmsInnerJobOrderDet.getMaterialId()).andEqualTo("batchCode",wmsInnerJobOrderDet.getBatchCode());
        List<WmsInnerInventory> oldlist = wmsInnerInventoryMapper.selectByExample(example);

        //确认库位
        example.clear();
        example.createCriteria().andEqualTo("relevanceOrderCode",wmsInnerJobOrder.getJobOrderCode()).andEqualTo("storageName",newStorageName)
                .andEqualTo("materialId",wmsInnerJobOrderDet.getMaterialId()).andEqualTo("batchCode",wmsInnerJobOrderDet.getBatchCode());
        List<WmsInnerInventory> newList = wmsInnerInventoryMapper.selectByExample(example);
        if(oldlist.get(0).getPackingQty().compareTo(wmsInnerJobOrderDet.getDistributionQty())==-1){
            throw new BizErrorException("库存不足");
        }
        if(newList.size()<1){
            //新增代出库存
            WmsInnerInventory newInventory = new WmsInnerInventory();
            BeanUtil.copyProperties(oldlist.get(0),newInventory);
            newInventory.setJobStatus((byte)3);
            newInventory.setInventoryId(null);
            newInventory.setStorageName(newStorageName);
            newInventory.setCreateTime(new Date());
            newInventory.setCreateUserId(sysUser.getUserId());
            newInventory.setModifiedTime(new Date());
            newInventory.setPackingQty(wmsInnerJobOrderDet.getDistributionQty());
            newInventory.setModifiedUserId(sysUser.getUserId());
            num += wmsInnerInventoryMapper.insertSelective(newInventory);

            //扣除原库存
            WmsInnerInventory oldInventory = oldlist.get(0);
            oldInventory.setPackingQty(oldInventory.getPackingQty().subtract(wmsInnerJobOrderDet.getDistributionQty()));
            wmsInnerInventoryMapper.updateByPrimaryKeySelective(oldInventory);
        }else{
            WmsInnerInventory newInventory = new WmsInnerInventory();
            newInventory.setInventoryId(newList.get(0).getInventoryId());
            newInventory.setPackingQty(wmsInnerJobOrderDet.getDistributionQty());
            newInventory.setModifiedUserId(sysUser.getUserId());
            newInventory.setModifiedTime(new Date());
            num+=wmsInnerInventoryMapper.updateByPrimaryKeySelective(newInventory);

            WmsInnerInventory oldInventory = oldlist.get(0);
            oldInventory.setPackingQty(oldInventory.getPackingQty().subtract(wmsInnerJobOrderDet.getDistributionQty()));
            wmsInnerInventoryMapper.updateByPrimaryKeySelective(oldInventory);
        }
        return num;
    }

    /**
     * 拣货分配库存
     * @param wmsInnerJobOrder
     * @param wmsInnerJobOrderDet
     */
    private int  DistributionInventory(WmsInnerJobOrder wmsInnerJobOrder,WmsInnerJobOrderDet wmsInnerJobOrderDet){
        SysUser sysUser = currentUser();
        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setJobOrderId(wmsInnerJobOrder.getJobOrderId());
        WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInnerJobOrderMapper.findList(searchWmsInnerJobOrder).get(0);
        //获取分配库存库存
        SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
        searchWmsInnerJobOrderDet.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
        List<WmsInnerJobOrderDetDto> list = wmsInnerJobOrderDetMapper.findList(searchWmsInnerJobOrderDet);
        if(list.size()<1){
            throw new BizErrorException(ErrorCodeEnum.GL9999404);
        }
        WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto = list.get(0);
        wmsInnerJobOrderDetDto.setDistributionQty(wmsInnerJobOrderDet.getDistributionQty());
        wmsInnerJobOrderDetDto.setInventoryStatusId(wmsInnerJobOrderDet.getInventoryStatusId());
        wmsInnerJobOrderDetDto.setWarehouseId(wmsInnerJobOrderDto.getWarehouseId());
        wmsInnerJobOrderDetDto.setWarehouseName(wmsInnerJobOrderDto.getWarehouseName());
        int num = 0;
        num+=subtract(wmsInnerJobOrderDetDto);
        if(num>0){
            plus(wmsInnerJobOrder,wmsInnerJobOrderDetDto);
        }else {
            throw new BizErrorException("库存分配失败");
        }
        return num;
    }

    /**
     * 减库存
     * @param wmsInnerJobOrderDetDto
     * @return
     */
    private int subtract(WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto){
        Example example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialId",wmsInnerJobOrderDetDto.getMaterialId());
        if(!StringUtils.isNotEmpty(wmsInnerJobOrderDetDto.getDistributionQty())){
            criteria.andEqualTo("batchCode",wmsInnerJobOrderDetDto.getBatchCode());
        }
        criteria.andEqualTo("jobStatus",(byte)1);
        criteria.andEqualTo("storageName",wmsInnerJobOrderDetDto.getOutStorageName()).andEqualTo("warehouseName",wmsInnerJobOrderDetDto.getWarehouseName()).andGreaterThan("packingQty",0);
        List<WmsInnerInventory> wmsInnerInventory = wmsInnerInventoryMapper.selectByExample(example);
        if(StringUtils.isEmpty(wmsInnerInventory)){
            throw new BizErrorException("未匹配到库存");
        }
        BigDecimal acuQty = wmsInnerJobOrderDetDto.getDistributionQty();
        //库存数量
        BigDecimal countQty = wmsInnerInventory.stream()
                .map(WmsInnerInventory::getPackingQty)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        if(acuQty.compareTo(countQty)==1){
            throw new BizErrorException("库存不足");
        }
        int num = 0;
        Map<Long,BigDecimal> bigDecimalMap = new HashMap<>();
        for (WmsInnerInventory innerInventory : wmsInnerInventory) {
            if(innerInventory.getPackingQty().compareTo(acuQty)==-1){
                acuQty = acuQty.subtract(innerInventory.getPackingQty());
                innerInventory.setPackingQty(innerInventory.getPackingQty().subtract(innerInventory.getPackingQty()));
                if(bigDecimalMap.containsKey(innerInventory.getInventoryId())){
                    bigDecimalMap.put(innerInventory.getInventoryId(), bigDecimalMap.get(innerInventory.getInventoryId()).add(acuQty));
                }else{
                    bigDecimalMap.put(innerInventory.getInventoryId(),acuQty);
                }
            }else{
                innerInventory.setPackingQty(innerInventory.getPackingQty().subtract(acuQty));
                if(bigDecimalMap.containsKey(innerInventory.getInventoryId())){
                    bigDecimalMap.put(innerInventory.getInventoryId(), bigDecimalMap.get(innerInventory.getInventoryId()).add(acuQty));
                }else{
                    bigDecimalMap.put(innerInventory.getInventoryId(),acuQty);
                }
                acuQty = acuQty.subtract(acuQty);
            }
            num +=wmsInnerInventoryMapper.updateByPrimaryKeySelective(innerInventory);
        }
        redisUtil.set(this.REDIS_KEY+wmsInnerJobOrderDetDto.getJobOrderDetId().toString(),bigDecimalMap);
        return num;
    }

    /**
     * 添加数量
     * @param wmsInnerJobOrderDetDto
     * @return
     */
    private int plus(WmsInnerJobOrder wmsInnerJobOrder,WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto){
        SysUser sysUser = currentUser();
        Example example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria = example.createCriteria();
        int num = 0;
        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setJobOrderId(wmsInnerJobOrder.getJobOrderId());
        WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInnerJobOrderMapper.findList(searchWmsInnerJobOrder).get(0);
        criteria.andEqualTo("relevanceOrderCode",wmsInnerJobOrder.getJobOrderCode()).andEqualTo("materialId",wmsInnerJobOrderDetDto.getMaterialId());
        if(StringUtils.isNotEmpty(wmsInnerJobOrderDetDto.getDistributionQty())){
            criteria.andEqualTo("batchCode",wmsInnerJobOrderDetDto.getBatchCode());
        }
        criteria.andEqualTo("jobStatus",(byte)2);
        criteria.andEqualTo("jobOrderDetId",wmsInnerJobOrderDetDto.getJobOrderDetId());
        criteria.andEqualTo("storageName",wmsInnerJobOrderDetDto.getOutStorageName()).andEqualTo("warehouseName",wmsInnerJobOrderDetDto.getWarehouseName());
        WmsInnerInventory wmsInnerInventorys = wmsInnerInventoryMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(wmsInnerInventorys)){
            //新增一条分配库存
            WmsInnerInventory wms = new WmsInnerInventory();
            wms.setMaterialOwnerName(wmsInnerJobOrderDto.getMaterialOwnerName());
            wms.setWarehouseName(wmsInnerJobOrderDto.getWarehouseName());
            wms.setStorageName(wmsInnerJobOrderDetDto.getOutStorageName());
            wms.setRelevanceOrderCode(wmsInnerJobOrder.getJobOrderCode());
            wms.setPackingQty(wmsInnerJobOrderDetDto.getDistributionQty());
            wms.setPackingUnitName(wmsInnerJobOrderDetDto.getPackingUnitName());
            wms.setMaterialId(wmsInnerJobOrderDetDto.getMaterialId());
            wms.setMaterialCode(wmsInnerJobOrderDetDto.getMaterialCode());
            wms.setMaterialName(wmsInnerJobOrderDetDto.getMaterialName());
            wms.setInventoryStatusId(wmsInnerJobOrderDetDto.getInventoryStatusId());
            wms.setJobStatus((byte)2);
            wms.setJobOrderDetId(wmsInnerJobOrderDetDto.getJobOrderDetId());
            wms.setCreateTime(new Date());
            wms.setCreateUserId(sysUser.getUserId());
            wms.setModifiedUserId(sysUser.getUserId());
            wms.setModifiedTime(new Date());
            num = wmsInnerInventoryMapper.insertSelective(wms);
            if(num<1){
                throw new BizErrorException("库存分配失败");
            }
        }else{
            wmsInnerInventorys.setPackingQty(wmsInnerInventorys.getPackingQty().add(wmsInnerJobOrderDetDto.getDistributionQty()));
            wmsInnerInventorys.setModifiedTime(new Date());
            wmsInnerInventorys.setModifiedUserId(sysUser.getUserId());
            num = wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
            if(num<1){
                throw new BizErrorException("库存分配失败");
            }
        }
        return num;
    }

    /**
     * 库存
     * @return
     */
    private int Inventory(WmsInnerJobOrderDetDto oldDto,WmsInnerJobOrderDetDto newDto){
        SysUser sysUser = currentUser();
        WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(oldDto.getJobOrderId());
        String warehouseName = wmsInnerJobOrderMapper.findWarehouseName(wmsInnerJobOrder.getWarehouseId());
        //旧
        Example example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("relevanceOrderCode",wmsInnerJobOrder.getJobOrderCode()).andEqualTo("materialId",oldDto.getMaterialId()).andEqualTo("warehouseName",oldDto.getWarehouseName()).andEqualTo("storageName",oldDto.getOutStorageName());
        if(!StringUtils.isEmpty(oldDto.getBatchCode())){
            criteria.andEqualTo("batchCode",oldDto.getBatchCode());
        }
        criteria.andEqualTo("jobOrderDetId",oldDto.getJobOrderDetId());
        criteria.andEqualTo("jobStatus",(byte)2);
        WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(wmsInnerInventory)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        WmsInnerInventory wmsIn = new WmsInnerInventory();
        wmsIn.setInventoryId(wmsInnerInventory.getInventoryId());
        wmsIn.setPackingQty(wmsInnerInventory.getPackingQty().subtract(newDto.getActualQty()));
        int num = wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsIn);


        example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("relevanceOrderCode",wmsInnerJobOrder.getJobOrderCode()).andEqualTo("materialId",newDto.getMaterialId()).andEqualTo("warehouseName",newDto.getWarehouseName()).andEqualTo("storageName",newDto.getInStorageName());
        if(!StringUtils.isEmpty(newDto.getBatchCode())){
            criteria1.andEqualTo("batchCode",newDto.getBatchCode());
        }
        criteria1.andEqualTo("jobOrderDetId",newDto.getJobOrderDetId());
        criteria1.andEqualTo("jobStatus",(byte)2);
        WmsInnerInventory wmsInnerInventorys = wmsInnerInventoryMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(wmsInnerInventorys)){
            //添加库存
            WmsInnerInventory inv = new WmsInnerInventory();
            BeanUtil.copyProperties(wmsInnerInventory,inv);
            inv.setStorageName(newDto.getInStorageName());
            inv.setWarehouseName(warehouseName);
            inv.setRelevanceOrderCode(wmsInnerJobOrder.getJobOrderCode());
            inv.setPackingQty(newDto.getActualQty());
            inv.setJobStatus((byte)2);
            inv.setJobOrderDetId(newDto.getJobOrderDetId());
            inv.setInventoryId(null);
            return wmsInnerInventoryMapper.insertSelective(inv);
        }else{
            //原库存
            wmsInnerInventorys.setPackingQty(wmsInnerInventorys.getPackingQty().add(newDto.getActualQty()));
            return wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
        }
    }

    /**
     * 恢复库存
     * @return
     */
    private int  reconver(Long jobOrderDetId){
        //查询redis
        int num = 0;

        if(redisUtil.hasKey(this.REDIS_KEY+jobOrderDetId.toString())){
            Map<Long,BigDecimal> map = (Map<Long, BigDecimal>) redisUtil.get(this.REDIS_KEY+jobOrderDetId.toString());
            for (Map.Entry<Long, BigDecimal> m : map.entrySet()){
                WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectByPrimaryKey(m.getKey());
                if(StringUtils.isEmpty(wmsInnerInventory)){
                    throw new BizErrorException("恢复库存失败");
                }
                wmsInnerInventory.setPackingQty(wmsInnerInventory.getPackingQty().add(new BigDecimal(String.valueOf(m.getValue()))));
                num+=wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventory);
            }
            redisUtil.expire(this.REDIS_KEY+jobOrderDetId,3);
        }else{
            throw new BizErrorException("恢复占用库存失败");
        }
        return num;
    }

    /**
     * 清除redis
     */
    private void removeRedis(Long jobOrderDetId){
        Map<Long,BigDecimal> map = (Map<Long, BigDecimal>) redisUtil.get(this.REDIS_KEY+jobOrderDetId.toString());
        if(map.containsKey(jobOrderDetId)){
            //设置10秒后失效
            redisUtil.expire(this.REDIS_KEY+jobOrderDetId,3);
        }
    }

    /**
     * 反写销售出库单拣货数量
     * @param wmsInnerJobOrderDet
     * @return
     */
    private int writeDeliveryOrderQty(WmsInnerJobOrderDet wmsInnerJobOrderDet){
        WmsOutDeliveryOrderDet wmsOutDeliveryOrderDet = new WmsOutDeliveryOrderDet();
        wmsOutDeliveryOrderDet.setDeliveryOrderDetId(wmsInnerJobOrderDet.getSourceDetId());
        wmsOutDeliveryOrderDet.setPickingQty(wmsInnerJobOrderDet.getActualQty());
        ResponseEntity responseEntity = outFeignApi.update(wmsOutDeliveryOrderDet);
        if(responseEntity.getCode()!=0){
            throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
        }
        return 1;
    }

    /**
     * 改状态
     * @param wmsInnerJobOrderDet
     * @return
     */
    @Override
    public int retrographyStatus(WmsInnerJobOrderDet wmsInnerJobOrderDet){
        wmsInnerJobOrderMapper.updateByPrimaryKeySelective(WmsInnerJobOrder.builder()
                .jobOrderId(wmsInnerJobOrderDet.getJobOrderId())
                .orderStatus(wmsInnerJobOrderDet.getOrderStatus())
                .build());
        return wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInnerJobOrderDet);
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
