package com.fantechs.provider.wms.inner.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.in.WmsInAsnOrderDetDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInAsnOrderDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrder;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrderDet;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInAsnOrder;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInAsnOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.*;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.api.wms.in.InFeignApi;
import com.fantechs.provider.wms.inner.mapper.*;
import com.fantechs.provider.wms.inner.service.WmsInnerInventoryService;
import com.fantechs.provider.wms.inner.service.WmsInnerJobOrderService;
import com.fantechs.provider.wms.inner.util.InBarcodeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by Mr.Lei on 2021/05/06.
 */
@Service
public class WmsInnerJobOrderServiceImpl extends BaseService<WmsInnerJobOrder> implements WmsInnerJobOrderService {
    @Resource
    private WmsInnerJobOrderMapper wmsInPutawayOrderMapper;
    @Resource
    private WmsInnerJobOrderDetMapper wmsInPutawayOrderDetMapper;
    @Resource
    private InFeignApi inFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private WmsInnerInventoryMapper wmsInnerInventoryMapper;
    @Resource
    private WmsInnerJobOrderReMsppMapper wmsInnerJobOrderReMsppMapper;
    @Resource
    private SFCFeignApi sfcFeignApi;
    @Resource
    private WmsInnerInventoryDetMapper wmsInnerInventoryDetMapper;
    @Resource
    WmsInnerInventoryService wmsInnerInventoryService;

    @Override
    public List<WmsInnerJobOrderDto> findList(SearchWmsInnerJobOrder searchWmsInnerJobOrder) {
        SysUser sysUser = currentUser();
        searchWmsInnerJobOrder.setOrgId(sysUser.getOrganizationId());
        return wmsInPutawayOrderMapper.findList(searchWmsInnerJobOrder);
    }

    @Override
    public List<WmsInnerJobOrderDto> findShiftList(Map<String, Object> map) {
        return wmsInPutawayOrderMapper.findShiftList(map);
    }

    @Override
    public List<WmsInnerJobOrderDto> pdaFindShiftList(Map<String, Object> map) {
        return wmsInPutawayOrderMapper.findShiftList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDeleteByShiftWork(String ids) {
        String[] arrId = ids.split(",");
        for (String s : arrId) {
            WmsInnerJobOrder wmsInnerJobOrder =  wmsInPutawayOrderMapper.selectByPrimaryKey(s);
            if(wmsInnerJobOrder.getOrderStatus()>=(byte)4){
                throw new BizErrorException("单据已经作业，无法删除");
            }
            if(StringUtils.isEmpty(wmsInnerJobOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId",s);
            wmsInPutawayOrderDetMapper.deleteByExample(example);

            // 查询明细对应的库存
            Example exampleInventory = new Example(WmsInnerInventory.class);
            exampleInventory.createCriteria().andEqualTo("jobOrderDetId", s);
            WmsInnerInventory innerInventory = wmsInnerInventoryMapper.selectOneByExample(exampleInventory);
            // 查询明细库存对应的原库存
            WmsInnerInventory sourceInnerInventory = wmsInnerInventoryMapper.selectByPrimaryKey(innerInventory.getParentInventoryId());
            sourceInnerInventory.setPackingQty(sourceInnerInventory.getPackingQty().add(innerInventory.getPackingQty()));
            // 批量修改原库存
            wmsInnerInventoryService.update(sourceInnerInventory);
            // 删除明细库存
            wmsInnerInventoryService.delete(innerInventory);
        }
        return wmsInPutawayOrderMapper.deleteByIds(ids);
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
            WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(id);
            if(wmsInnerJobOrder.getOrderStatus()>(byte)2){
                throw new BizErrorException("单据已分配完成");
            }
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId",wmsInnerJobOrder.getJobOrderId());
            List<WmsInnerJobOrderDet> list = wmsInPutawayOrderDetMapper.selectByExample(example);
            for (WmsInnerJobOrderDet wms : list) {
                if(StringUtils.isEmpty(wms)){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                }
                //推荐库位
                Long storageId = wmsInPutawayOrderMapper.findStorage(wms.getMaterialId(),wmsInnerJobOrder.getWarehouseId());
                storageId = storageId==null?wmsInPutawayOrderMapper.SelectStorage():storageId;
                if(StringUtils.isEmpty(storageId)){
                    throw new BizErrorException("未查询到推荐库位");
                }
                num+=wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(WmsInnerJobOrderDet.builder()
                        .jobOrderDetId(wms.getJobOrderDetId())
                        .inStorageId(storageId)
                        .distributionQty(wms.getPlanQty())
                        .modifiedUserId(sysUser.getUserId())
                        .modifiedTime(new Date())
                        .orderStatus((byte)3)
                        .build());
                //库位容量减1
                baseFeignApi.minusSurplusCanPutSalver(wms.getInStorageId(),1);

                SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
                searchWmsInnerJobOrder.setJobOrderId(wms.getJobOrderId());
                WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInPutawayOrderMapper.findList(searchWmsInnerJobOrder).get(0);
                SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
                searchWmsInnerJobOrderDet.setJobOrderDetId(wms.getJobOrderDetId());
                WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);
                //分配库存
                num+=this.updateInventory(wmsInnerJobOrderDto,wmsInnerJobOrderDetDto);
            }
            //待激活
            wmsInnerJobOrder.setWorkerId(sysUser.getUserId());
            if(wmsInnerJobOrder.getOrderTypeId()==4){
                wmsInnerJobOrder.setOrderStatus((byte)6);
            }else{
                wmsInnerJobOrder.setOrderStatus((byte)3);
            }
            wmsInnerJobOrder.setModifiedTime(new Date());
            wmsInnerJobOrder.setModifiedUserId(sysUser.getUserId());
            wmsInPutawayOrderMapper.updateByPrimaryKeySelective(wmsInnerJobOrder);
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
            WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(wmsInPutawayOrderDet.getJobOrderId());
            if(!StringUtils.isEmpty(wmsInPutawayOrderDet.getDistributionQty()) && wmsInPutawayOrderDet.getDistributionQty().doubleValue()>wmsInPutawayOrderDet.getPlanQty().doubleValue()){
                throw new BizErrorException("分配数量不能大于计划数量");
            }
            Long id = null;
            //当货品分配时未全部分配完时新增一条剩余待分配数量的记录
            if(StringUtils.isEmpty(wmsInPutawayOrderDet.getDistributionQty()) || wmsInPutawayOrderDet.getDistributionQty().compareTo(wmsInPutawayOrderDet.getPlanQty())==-1){
                //分配中
                WmsInnerJobOrderDet wms = new WmsInnerJobOrderDet();
                BeanUtil.copyProperties(wmsInPutawayOrderDet,wms);
                wms.setJobOrderDetId(null);
                wms.setPlanQty(wmsInPutawayOrderDet.getDistributionQty());
                wms.setDistributionQty(wmsInPutawayOrderDet.getDistributionQty());
                wms.setOrderStatus((byte)3);
                num+=wmsInPutawayOrderDetMapper.insertUseGeneratedKeys(wms);
                id = wms.getJobOrderDetId();

                wmsInPutawayOrderDet.setOrderStatus((byte)1);
                wmsInPutawayOrderDet.setDistributionQty(null);
                wmsInPutawayOrderDet.setInStorageId(null);
                wmsInPutawayOrderDet.setPlanQty(new BigDecimal(wmsInPutawayOrderDet.getPlanQty().doubleValue()-wms.getDistributionQty().doubleValue()));
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                num += wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
            }else if(wmsInPutawayOrderDet.getDistributionQty().compareTo(wmsInPutawayOrderDet.getPlanQty())==0){
                //分配完成
                wmsInPutawayOrderDet.setOrderStatus((byte)3);
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                num += wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
                id = wmsInPutawayOrderDet.getJobOrderDetId();
            }
            SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
            searchWmsInnerJobOrderDet.setJobOrderId(wmsInPutawayOrderDet.getJobOrderId());
            List<WmsInnerJobOrderDetDto> dto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet);

            SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
            searchWmsInnerJobOrder.setJobOrderId(wmsInPutawayOrderDet.getJobOrderId());
            WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInPutawayOrderMapper.findList(searchWmsInnerJobOrder).get(0);
            searchWmsInnerJobOrderDet.setJobOrderDetId(id);
            WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);
            //分配库存
            num+=this.updateInventory(wmsInnerJobOrderDto,wmsInnerJobOrderDetDto);

            if(StringUtils.isEmpty(dto)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //如果货品全部分配完成更改表头状态为待作业状态
            if(dto.stream().filter(li->li.getOrderStatus()==(byte)3).collect(Collectors.toList()).size()==dto.size()){
                //更新表头状态
                //完工入库单需要激活状态 其他则不需要
                Byte status = 3;
                if(wmsInnerJobOrder.getOrderTypeId()==4){
                    status=6;
                }
                wmsInPutawayOrderMapper.updateByPrimaryKeySelective(WmsInnerJobOrder.builder()
                        .jobOrderId(wmsInPutawayOrderDet.getJobOrderId())
                        //待激活
                        .orderStatus(status)
                        .workerId(sysUser.getUserId())
                        .build());
            }else{
                wmsInPutawayOrderMapper.updateByPrimaryKeySelective(WmsInnerJobOrder.builder()
                        .jobOrderId(wmsInPutawayOrderDet.getJobOrderId())
                        .orderStatus((byte)2)
                        .workerId(sysUser.getUserId())
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
            WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(s);
            if(StringUtils.isEmpty(wmsInnerJobOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            if(wmsInnerJobOrder.getOrderStatus()==(byte)1){
                throw new BizErrorException("单据处于未分配状态");
            }
            if(wmsInnerJobOrder.getOrderStatus()==(byte)4 || wmsInnerJobOrder.getOrderStatus()==(byte)5){
                throw new BizErrorException("单据作业中，无法取消");
            }
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId",s);
            List<WmsInnerJobOrderDet> list = wmsInPutawayOrderDetMapper.selectByExample(example);

            //合并同货品的记录
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
                //恢复库存
                int res = this.cancel(wmsInnerJobOrderDet.getSourceDetId(),wmsInnerJobOrderDet.getDistributionQty());
                if(res<1){
                    throw new BizErrorException("库存恢复失败");
                }
                //删除分配库存
                Example example1 = new Example(WmsInnerInventory.class);
                example1.createCriteria().andEqualTo("jobOrderDetId",wmsInnerJobOrderDet.getJobOrderDetId()).andEqualTo("jobStatus",(byte)2).andEqualTo("relevanceOrderCode",wmsInnerJobOrder.getJobOrderCode());
                wmsInnerInventoryMapper.deleteByExample(example1);
            }
            //删除全部明细数据
            wmsInPutawayOrderDetMapper.deleteByExample(example);
            for (List<WmsInnerJobOrderDet> value : map.values()) {
                for (WmsInnerJobOrderDet wmsInnerJobOrderDet : value) {
                    wmsInnerJobOrderDet.setDistributionQty(null);
                    wmsInnerJobOrderDet.setInStorageId(null);
                    wmsInnerJobOrderDet.setOrderStatus((byte)1);
                    wmsInnerJobOrderDet.setModifiedTime(new Date());
                    wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
                    wmsInnerJobOrderDet.setOrderStatus((byte)1);
                    num +=wmsInPutawayOrderDetMapper.insertSelective(wmsInnerJobOrderDet);
                }
            }
            wmsInnerJobOrder.setOrderStatus((byte)1);
            num +=wmsInPutawayOrderMapper.updateByPrimaryKeySelective(wmsInnerJobOrder);
        }
        return num;
    }

    private int cancel(Long sourceDetId,BigDecimal qty){
        if(StringUtils.isEmpty(qty)){
            qty = BigDecimal.ZERO;
        }
        WmsInAsnOrderDetDto wmsInAsnOrderDetDto = inFeignApi.findDetList(SearchWmsInAsnOrderDet.builder()
                .asnOrderDetId(sourceDetId)
                .build()).getData().get(0);

        WmsInAsnOrderDto wmsInAsnOrderDto = inFeignApi.findList(SearchWmsInAsnOrder.builder()
                .asnOrderId(wmsInAsnOrderDetDto.getAsnOrderId())
                .build()).getData().get(0);
        //旧
        Example example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("relevanceOrderCode",wmsInAsnOrderDto.getAsnCode()).andEqualTo("materialId",wmsInAsnOrderDetDto.getMaterialId()).andEqualTo("warehouseId",wmsInAsnOrderDetDto.getWarehouseId()).andEqualTo("storageId",wmsInAsnOrderDetDto.getStorageId());
        if(!StringUtils.isEmpty(wmsInAsnOrderDetDto.getBatchCode())){
            criteria.andEqualTo("batchCode",wmsInAsnOrderDetDto.getBatchCode());
        }
        WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(wmsInnerInventory)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        WmsInnerInventory wmsIn = new WmsInnerInventory();
        wmsIn.setInventoryId(wmsInnerInventory.getInventoryId());
        wmsIn.setPackingQty(wmsInnerInventory.getPackingQty().add(qty));
        int num = wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsIn);
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int allReceiving(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        int num = 0;
        for (String id : arrId) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(id);
            if(wmsInnerJobOrder.getOrderStatus()==6){
                wmsInnerJobOrder.setOrderStatus((byte)3);
            }
            if(wmsInnerJobOrder.getOrderStatus()<(byte)3){
                throw new BizErrorException("未分配完成,无法全部上架");
            }
            double total = 0.00;
            if(wmsInnerJobOrder.getOrderStatus()==(byte)5){
                throw new BizErrorException("单据确认已完成");
            }
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId",wmsInnerJobOrder.getJobOrderId());
            List<WmsInnerJobOrderDet> wmsInnerJobOrderDets = wmsInPutawayOrderDetMapper.selectByExample(example);

            for (WmsInnerJobOrderDet wmsInnerJobOrderDet : wmsInnerJobOrderDets) {
                if(wmsInnerJobOrderDet.getOrderStatus()==(byte)3) {
                    if (StringUtils.isEmpty(id)) {
                        throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                    }
                    SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
                    searchWmsInnerJobOrderDet.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
                    WmsInnerJobOrderDetDto oldDto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);

                    total += wmsInnerJobOrderDet.getDistributionQty().doubleValue();
                    num += wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(WmsInnerJobOrderDet.builder()
                            .jobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId())
                            .orderStatus((byte) 5)
                            .actualQty(wmsInnerJobOrderDet.getDistributionQty())
                            .modifiedUserId(sysUser.getUserId())
                            .modifiedTime(new Date())
                            .build());

                    //更改库存为正常状态
                    SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
                    searchWmsInnerJobOrder.setJobOrderId(wmsInnerJobOrderDet.getJobOrderId());
                    WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInPutawayOrderMapper.findList(searchWmsInnerJobOrder).get(0);
                    WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);

                    //上架作业更改库存
                    if (wmsInnerJobOrder.getJobOrderType() == (byte) 3) {
                        num = this.Inventory(oldDto,wmsInnerJobOrderDetDto);
                    }

                    //反写完工入库单
                    inFeignApi.writeQty(WmsInAsnOrderDet.builder()
                            .putawayQty(wmsInnerJobOrderDet.getDistributionQty())
                            .asnOrderDetId(wmsInnerJobOrderDet.getSourceDetId())
                            .build());
                }
            }
            BigDecimal resultQty = wmsInnerJobOrderDets.stream()
                    .map(WmsInnerJobOrderDet::getDistributionQty)
                    .reduce(BigDecimal.ZERO,BigDecimal::add);
            //更改表头为作业完成状态
            wmsInPutawayOrderMapper.updateByPrimaryKeySelective(WmsInnerJobOrder.builder()
                    .orderStatus((byte)5)
                    .workerId(sysUser.getUserId())
                    .jobOrderId(wmsInnerJobOrder.getJobOrderId())
                    .actualQty(resultQty)
                    .workStartTime(new Date())
                    .workEndtTime(new Date())
                    .build());
        }
        return num;
    }

    private int ReceivingInventory(WmsInnerJobOrder wmsInnerJobOrder,WmsInnerJobOrderDet wmsInnerJobOrderDet){
        return 1;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int singleReceiving(List<WmsInnerJobOrderDet> wmsInPutawayOrderDets) {
        SysUser sysUser = currentUser();
        int num = 0;
        for (WmsInnerJobOrderDet wmsInPutawayOrderDet : wmsInPutawayOrderDets) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(wmsInPutawayOrderDet.getJobOrderId());
            if(wmsInnerJobOrder.getOrderStatus()==6){
                wmsInnerJobOrder.setOrderStatus((byte)3);
            }
            if(wmsInnerJobOrder.getOrderStatus()==(byte)5){
                throw new BizErrorException("单据确认已完成");
            }
            BigDecimal aqty = wmsInPutawayOrderDet.getActualQty();

            SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
            searchWmsInnerJobOrderDet.setJobOrderDetId(wmsInPutawayOrderDet.getJobOrderDetId());
            WmsInnerJobOrderDetDto oldDto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);

            Long jobOrderDetId = null;
            if(wmsInPutawayOrderDet.getActualQty().compareTo(wmsInPutawayOrderDet.getDistributionQty())==-1){
                WmsInnerJobOrderDet wms = new WmsInnerJobOrderDet();
                BeanUtil.copyProperties(wmsInPutawayOrderDet,wms);
                wms.setJobOrderDetId(null);
                wms.setPlanQty(wmsInPutawayOrderDet.getActualQty());
                wms.setDistributionQty(wmsInPutawayOrderDet.getActualQty());
                wms.setOrderStatus((byte)5);
                num+=wmsInPutawayOrderDetMapper.insertUseGeneratedKeys(wms);
                jobOrderDetId = wms.getJobOrderDetId();

                wmsInPutawayOrderDet.setOrderStatus((byte)3);
                wmsInPutawayOrderDet.setInStorageId(null);
                wmsInPutawayOrderDet.setPlanQty(wmsInPutawayOrderDet.getPlanQty().subtract(wms.getPlanQty()));
                wmsInPutawayOrderDet.setDistributionQty(wmsInPutawayOrderDet.getDistributionQty().subtract(wms.getDistributionQty()));
                wmsInPutawayOrderDet.setActualQty(null);
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                num+=wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
            }else if(wmsInPutawayOrderDet.getDistributionQty().compareTo(wmsInPutawayOrderDet.getPlanQty())==0){
                //确认完成
                wmsInPutawayOrderDet.setOrderStatus((byte)5);
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                num += wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
                jobOrderDetId = wmsInPutawayOrderDet.getJobOrderDetId();
            }
            //更改库存
            SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
            searchWmsInnerJobOrder.setJobOrderId(wmsInPutawayOrderDet.getJobOrderId());
            WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInPutawayOrderMapper.findList(searchWmsInnerJobOrder).get(0);
            searchWmsInnerJobOrderDet.setJobOrderDetId(jobOrderDetId);
            List<WmsInnerJobOrderDetDto> wmsInner = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet);

            //更改库存
            WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto = wmsInner.get(0);
            //num = this.updateInventory(wmsInnerJobOrderDto,wmsInnerJobOrderDetDto);
            num = this.Inventory(oldDto,wmsInnerJobOrderDetDto);

            WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
            wmsInnerJobOrderDet.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
            int count = wmsInPutawayOrderDetMapper.selectCount(wmsInnerJobOrderDet);
            wmsInnerJobOrderDet.setOrderStatus((byte)5);
            int oCount = wmsInPutawayOrderDetMapper.selectCount(wmsInnerJobOrderDet);


            if(oCount==count){
                WmsInnerJobOrder ws = wmsInPutawayOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDto.getJobOrderId());
                ws.setOrderStatus((byte)5);
                if(StringUtils.isEmpty(ws.getActualQty())){
                    ws.setActualQty(new BigDecimal("0.00"));
                }
                ws.setWorkerId(sysUser.getUserId());
                ws.setActualQty(ws.getActualQty().add(aqty));
                ws.setModifiedUserId(sysUser.getUserId());
                ws.setModifiedTime(new Date());
                ws.setWorkEndtTime(new Date());
                num +=wmsInPutawayOrderMapper.updateByPrimaryKeySelective(ws);
            }else{
                WmsInnerJobOrder ws = wmsInPutawayOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDto.getJobOrderId());
                ws.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
                if(StringUtils.isEmpty(ws.getActualQty())){
                    ws.setActualQty(new BigDecimal("0.00"));
                }
                ws.setWorkerId(sysUser.getUserId());
                ws.setActualQty(ws.getActualQty().add(aqty));
                ws.setOrderStatus((byte)4);
                ws.setModifiedUserId(sysUser.getUserId());
                ws.setModifiedTime(new Date());
                if(StringUtils.isEmpty(wmsInnerJobOrder.getWorkStartTime())){
                    ws.setWorkStartTime(new Date());
                }
                num +=wmsInPutawayOrderMapper.updateByPrimaryKeySelective(ws);
            }

            //反写完工入库单
            inFeignApi.writeQty(WmsInAsnOrderDet.builder()
                    .putawayQty(wmsInnerJobOrderDetDto.getActualQty())
                    .asnOrderDetId(wmsInnerJobOrderDetDto.getSourceDetId())
                    .build());
        }
        return num;
    }

    /**
     * 校验条码
     * @param barCode
     * @return 包装数量
     */
    @Override
    public Map<String,Object> checkBarcode(String barCode,Long jobOrderDetId) {
        WmsInnerJobOrderDet wmsInnerJobOrderDet = wmsInPutawayOrderDetMapper.selectByPrimaryKey(jobOrderDetId);
        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isEmpty(wmsInnerJobOrderDet)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        //获取完工入库记录的工单
        WmsInAsnOrderDet wms = inFeignApi.findDetList(SearchWmsInAsnOrderDet.builder()
                .asnOrderDetId(wmsInnerJobOrderDet.getSourceDetId())
                .build()).getData().get(0);
        String materialCode = wmsInPutawayOrderMapper.findMaterialCode(wms.getMaterialId());
        if(StringUtils.isNotEmpty(materialCode) && materialCode.equals(barCode)){
            map.put("SN","false");
            return map;
        }else{
            BigDecimal qty = InBarcodeUtil.getInventoryDetQty(wmsInnerJobOrderDet.getMaterialId(),barCode);
            map.put("SN","true");
            map.put("qty",qty);
        }
        return map;
    }

    /**
     * PDA扫码上架新增库存明细
     * @return
     */
    private int addInventoryDet(Long asnOrderId,WmsInnerJobOrderDet wmsInnerJobOrderDet){
        //获取完工入库单单号
        String asnOrderCode = wmsInPutawayOrderMapper.findAsnCode(asnOrderId);
        Example example = new Example(WmsInnerInventoryDet.class);
        //获取绑定上架单的栈板码
        String barCode = wmsInPutawayOrderDetMapper.findPalletCode(wmsInnerJobOrderDet.getJobOrderId());
        if(StringUtils.isEmpty(barCode)){
            throw new BizErrorException("获取栈板信息失败");
        }
        example.createCriteria().andEqualTo("relatedOrderCode",asnOrderCode).andEqualTo("storageId",wmsInnerJobOrderDet.getOutStorageId()).andEqualTo("materialId",wmsInnerJobOrderDet.getMaterialId()).andEqualTo("barcode",barCode);
        WmsInnerInventoryDet wmsInnerInventoryDet = wmsInnerInventoryDetMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(wmsInnerInventoryDet)){
            throw new BizErrorException("未查询到收货条码");
        }
        wmsInnerInventoryDet.setInTime(new Date());
        wmsInnerInventoryDet.setStorageId(wmsInnerJobOrderDet.getInStorageId());
        return wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(wmsInnerInventoryDet);
    }

    /**
     * PDA上架作业扫描库位上架
     * @param storageCode
     * @param jobOrderDetId
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public WmsInnerJobOrderDet scanStorageBackQty(String storageCode, Long jobOrderDetId,BigDecimal qty) {
        SysUser sysUser = currentUser();
        if(StringUtils.isEmpty(qty)){
            throw new BizErrorException("上架数量不能小于1");
        }
        //通过储位编码查询储位id
        ResponseEntity<List<BaseStorage>> list = baseFeignApi.findList(SearchBaseStorage.builder()
                .storageCode(storageCode)
                .codeQueryMark((byte)1)
                .build());
        if(StringUtils.isEmpty(list.getData())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"库位查询失败");
        }

        WmsInnerJobOrderDet wmsInnerJobOrderDet = wmsInPutawayOrderDetMapper.selectByPrimaryKey(jobOrderDetId);
//        if(StringUtils.isEmpty(wmsInnerJobOrderDet.getActualQty())){
//            wmsInnerJobOrderDet.setActualQty(wmsInnerJobOrderDet.getDistributionQty());
//        }
        if(StringUtils.isEmpty(wmsInnerJobOrderDet.getActualQty())){
            wmsInnerJobOrderDet.setActualQty(BigDecimal.ZERO);
        }
        if(wmsInnerJobOrderDet.getActualQty().add(qty).compareTo(wmsInnerJobOrderDet.getDistributionQty())==1){
            throw new BizErrorException("上架数量不能大于分配数量");
        }
        BaseStorage baseStorage = list.getData().get(0);

        SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
        searchWmsInnerJobOrderDet.setJobOrderDetId(jobOrderDetId);
        WmsInnerJobOrderDetDto oldDto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);
        Byte status = 4;
        if(wmsInnerJobOrderDet.getActualQty().add(qty).compareTo(wmsInnerJobOrderDet.getDistributionQty())==0){
            status = (byte)5;
        }
        WmsInnerJobOrderDet wms =WmsInnerJobOrderDet.builder()
                .jobOrderDetId(jobOrderDetId)
                .inStorageId(baseStorage.getStorageId())
                .actualQty(qty)
                .orderStatus(status)
                .modifiedTime(new Date())
                .modifiedUserId(sysUser.getUserId())
                .build();
        int num = wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wms);
        if(num==0){
            throw new BizErrorException("上架失败");
        }


        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setJobOrderId(wmsInnerJobOrderDet.getJobOrderId());
        WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInPutawayOrderMapper.findList(searchWmsInnerJobOrder).get(0);
        List<WmsInnerJobOrderDetDto> wmsInnerJobOrderDetDto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet);

        BigDecimal resQty = wmsInnerJobOrderDetDto.stream()
                .map(WmsInnerJobOrderDetDto::getActualQty)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        //更改库存
        num = this.Inventory(oldDto,wmsInnerJobOrderDetDto.get(0));

        //更新库存明细
        num+=this.addInventoryDet(wmsInnerJobOrderDto.getSourceOrderId(),wmsInnerJobOrderDet);


        wms= new WmsInnerJobOrderDet();
        wms.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
        int count = wmsInPutawayOrderDetMapper.selectCount(wms);
        wms.setOrderStatus((byte)5);
        int oCount = wmsInPutawayOrderDetMapper.selectCount(wms);


        if(oCount==count){
            WmsInnerJobOrder ws = new WmsInnerJobOrder();
            ws.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
            ws.setOrderStatus((byte)5);
            ws.setActualQty(resQty);
            ws.setModifiedUserId(sysUser.getUserId());
            ws.setModifiedTime(new Date());
            ws.setWorkEndtTime(new Date());
            ws.setWorkerId(sysUser.getUserId());
            num +=wmsInPutawayOrderMapper.updateByPrimaryKeySelective(ws);
        }else{
            WmsInnerJobOrder ws = new WmsInnerJobOrder();
            ws.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
            ws.setOrderStatus((byte)4);
            ws.setActualQty(resQty);
            ws.setModifiedUserId(sysUser.getUserId());
            ws.setModifiedTime(new Date());
            ws.setWorkerId(sysUser.getUserId());
            if(StringUtils.isEmpty(wmsInnerJobOrderDto.getWorkStartTime())){
                ws.setWorkStartTime(new Date());
            }
            num +=wmsInPutawayOrderMapper.updateByPrimaryKeySelective(ws);
        }
        //反写完工入库单
        inFeignApi.writeQty(WmsInAsnOrderDet.builder()
                .putawayQty(qty)
                .asnOrderDetId(wmsInnerJobOrderDet.getSourceDetId())
                .build());
        return wmsInnerJobOrderDet;
    }

    @Override
    public WmsInnerJobOrder packageAutoAdd(WmsInnerJobOrder wmsInnerJobOrder) {
        SysUser sysUser = currentUser();
        wmsInnerJobOrder.setJobOrderCode(CodeUtils.getId("WORK-"));
        wmsInnerJobOrder.setCreateTime(new Date());
        wmsInnerJobOrder.setCreateUserId(sysUser.getUserId());
        wmsInnerJobOrder.setModifiedTime(new Date());
        wmsInnerJobOrder.setModifiedUserId(sysUser.getUserId());
        int num = wmsInPutawayOrderMapper.insertUseGeneratedKeys(wmsInnerJobOrder);
        for (WmsInnerJobOrderDet wmsInPutawayOrderDet : wmsInnerJobOrder.getWmsInPutawayOrderDets()) {
            wmsInPutawayOrderDetMapper.insertUseGeneratedKeys(wmsInPutawayOrderDet.builder()
                    .jobOrderId(wmsInnerJobOrder.getJobOrderId())
                    .createTime(new Date())
                    .createUserId(sysUser.getUserId())
                    .modifiedTime(new Date())
                    .modifiedUserId(sysUser.getUserId())
                    .build());

            WmsInAsnOrderDto wmsInAsnOrderDto = inFeignApi.findList(SearchWmsInAsnOrder.builder()
                    .asnOrderId(wmsInnerJobOrder.getSourceOrderId())
                    .build()).getData().get(0);
            Example example = new Example(WmsInnerInventory.class);
            example.createCriteria().andEqualTo("relevanceOrderCode",wmsInAsnOrderDto.getAsnCode()).andEqualTo("materialId",wmsInPutawayOrderDet.getMaterialId()).andEqualTo("batchCode",wmsInPutawayOrderDet.getBatchCode());
            WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
            if(StringUtils.isEmpty(wmsInnerInventory)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            WmsInnerInventory wmsInnerInventorys = new WmsInnerInventory();
            wmsInnerInventorys.setInventoryId(wmsInnerInventory.getInventoryId());
            wmsInnerInventorys.setJobStatus((byte)2);
            wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
        }
        return wmsInnerJobOrder;
    }

    /**
     * PDA激活关闭栈板
     * @param jobOrderId
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int activation(Long jobOrderId) {
        WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(jobOrderId);
        if(StringUtils.isEmpty(wmsInnerJobOrder)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        Example example = new Example(WmsInnerJobOrderReMspp.class);
        example.createCriteria().andEqualTo("jobOrderId",jobOrderId);
        WmsInnerJobOrderReMspp wmsInnerJobOrderReMspp  = wmsInnerJobOrderReMsppMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(wmsInnerJobOrderReMspp)){
            throw new BizErrorException("未匹配到上架单关联栈板关系");
        }
        //更新栈板状态
        ResponseEntity responseEntity = sfcFeignApi.updateMoveStatus(wmsInnerJobOrderReMspp.getProductPalletId());
        if(responseEntity.getCode()!=0){
            throw new BizErrorException("激活失败");
        }
        //更新待作业状态
        wmsInnerJobOrder.setOrderStatus((byte)3);
        int num = wmsInPutawayOrderMapper.updateByPrimaryKeySelective(wmsInnerJobOrder);
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(WmsInnerJobOrder record) {
        SysUser sysUser = currentUser();
        if(record.getJobOrderType()==(byte)3){
            //上架单
            record.setJobOrderCode(CodeUtils.getId("PUT-"));
        }else if(record.getJobOrderType()==(byte)4){
            //拣货单
            record.setJobOrderCode(CodeUtils.getId("PICK-"));
        }else if(record.getJobOrderType()==(byte)2){
            //移位单
            record.setJobOrderCode(CodeUtils.getId("SHIFT-"));
        }
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrgId(sysUser.getOrganizationId());
        int num = wmsInPutawayOrderMapper.insertUseGeneratedKeys(record);
        for (WmsInnerJobOrderDet wmsInPutawayOrderDet : record.getWmsInPutawayOrderDets()) {
            wmsInPutawayOrderDet.setJobOrderId(record.getJobOrderId());
            wmsInPutawayOrderDet.setCreateTime(new Date());
            wmsInPutawayOrderDet.setCreateUserId(sysUser.getUserId());
            wmsInPutawayOrderDet.setModifiedTime(new Date());
            wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInPutawayOrderDet.setOrgId(sysUser.getOrganizationId());
            if(record.getJobOrderType()==(byte)2){
                wmsInPutawayOrderDet.setShiftStorageStatus((byte) 2);
            }
            wmsInPutawayOrderDetMapper.insertUseGeneratedKeys(wmsInPutawayOrderDet);
            if(record.getJobOrderType()==(byte)3) {
                WmsInAsnOrderDto wmsInAsnOrderDto = inFeignApi.findList(SearchWmsInAsnOrder.builder()
                        .asnOrderId(record.getSourceOrderId())
                        .build()).getData().get(0);
                Example example = new Example(WmsInnerInventory.class);
                example.createCriteria().andEqualTo("relevanceOrderCode", wmsInAsnOrderDto.getAsnCode())
                        .andEqualTo("materialId", wmsInPutawayOrderDet.getMaterialId())
                        .andEqualTo("batchCode", wmsInPutawayOrderDet.getBatchCode())
                        .andEqualTo("warehouseId", record.getWarehouseId())
                        .andEqualTo("storageId", wmsInPutawayOrderDet.getOutStorageId());
                WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
                if (StringUtils.isEmpty(wmsInnerInventory)) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                }
                WmsInnerInventory wmsInnerInventorys = new WmsInnerInventory();
                wmsInnerInventorys.setInventoryId(wmsInnerInventory.getInventoryId());
                wmsInnerInventorys.setJobStatus((byte) 2);
                wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
            }else if(record.getJobOrderType()==(byte)2){
                // 生成库存，扣减原库存
                WmsInnerInventory innerInventory = wmsInnerInventoryService.selectByKey(wmsInPutawayOrderDet.getSourceDetId());
                if (innerInventory.getPackingQty().compareTo(wmsInPutawayOrderDet.getPlanQty()) < -1){
                    throw new BizErrorException(ErrorCodeEnum.PDA5001012);
                }
                WmsInnerInventory newInnerInventory = new WmsInnerInventory();
                BeanUtil.copyProperties(innerInventory, newInnerInventory);
                newInnerInventory.setJobStatus((byte) 3);
                newInnerInventory.setJobOrderDetId(wmsInPutawayOrderDet.getJobOrderDetId());
                newInnerInventory.setOrgId(sysUser.getOrganizationId());
                newInnerInventory.setCreateTime(new Date());
                newInnerInventory.setCreateUserId(sysUser.getUserId());
                wmsInnerInventoryService.save(newInnerInventory);
                // 变更减少原库存
                innerInventory.setPackingQty(innerInventory.getPackingQty().subtract(wmsInPutawayOrderDet.getPlanQty()));
                wmsInnerInventoryService.update(innerInventory);
            }
        }
        if(StringUtils.isNotEmpty(record.getProductPalletId())){
            //生成上架单绑定栈板关联关系
            WmsInnerJobOrderReMspp wmsInnerJobOrderReMspp = WmsInnerJobOrderReMspp.builder()
                    .jobOrderId(record.getJobOrderId())
                    .productPalletId(record.getProductPalletId())
                    .createTime(new Date())
                    .createUserId(sysUser.getUserId())
                    .modifiedTime(new Date())
                    .modifiedUserId(sysUser.getUserId())
                    .orgId(sysUser.getOrganizationId())
                    .build();
            int res = wmsInnerJobOrderReMsppMapper.insertSelective(wmsInnerJobOrderReMspp);
            if(res<=0){
                throw new BizErrorException("上架单关联栈板失败");
            }
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(WmsInnerJobOrder record){
        SysUser sysUser = currentUser();
        if(record.getOrderStatus() != (byte) 1){
            throw new BizErrorException("移位单已分配，不可变更修改");
        }
        int i = wmsInPutawayOrderMapper.updateByPrimaryKey(record);
        if(record.getJobOrderType()==(byte)2){
            // 查询明细
            SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
            searchWmsInnerJobOrderDet.setJobOrderId(record.getJobOrderId());
            List<WmsInnerJobOrderDetDto> orderDetMapperList = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet);
            // 查询明细对应的库存
            Example example = new Example(WmsInnerInventory.class);
            example.createCriteria().andIn("jobOrderDetId", orderDetMapperList.stream().map(WmsInnerJobOrderDet::getJobOrderDetId).collect(Collectors.toList()));
            List<WmsInnerInventory> innerInventories = wmsInnerInventoryMapper.selectByExample(example);
            // 查询明细库存对应的原库存
            example.clear();
            example.createCriteria().andIn("inventoryId", innerInventories.stream().map(WmsInnerInventory::getParentInventoryId).collect(Collectors.toList()));
            List<WmsInnerInventory> sourceInnerInventories = wmsInnerInventoryMapper.selectByExample(example);
            for (WmsInnerInventory source : sourceInnerInventories){
                for (WmsInnerInventory innerInventory : sourceInnerInventories){
                    if(innerInventory.getParentInventoryId().equals(source.getInventoryId())){
                        source.setPackingQty(source.getPackingQty().add(innerInventory.getPackingQty()));
                    }
                }
            }
            // 批量修改原库存
            wmsInnerInventoryService.batchUpdate(sourceInnerInventories);
            // 删除明细库存
            wmsInnerInventoryService.batchDelete(innerInventories);
            // 删除明细
            wmsInPutawayOrderDetMapper.insertList(orderDetMapperList);


            for (WmsInnerJobOrderDet wmsInPutawayOrderDet : record.getWmsInPutawayOrderDets()) {
                wmsInPutawayOrderDet.setJobOrderId(record.getJobOrderId());
                wmsInPutawayOrderDet.setCreateTime(new Date());
                wmsInPutawayOrderDet.setCreateUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setOrgId(sysUser.getOrganizationId());
                wmsInPutawayOrderDetMapper.insertUseGeneratedKeys(wmsInPutawayOrderDet);

                // 生成库存，扣减原库存
                WmsInnerInventory innerInventory = wmsInnerInventoryService.selectByKey(wmsInPutawayOrderDet.getSourceDetId());
                if (innerInventory.getPackingQty().compareTo(wmsInPutawayOrderDet.getPlanQty()) < -1){
                    throw new BizErrorException(ErrorCodeEnum.PDA5001012);
                }
                WmsInnerInventory newInnerInventory = new WmsInnerInventory();
                BeanUtil.copyProperties(innerInventory, newInnerInventory);
                newInnerInventory.setJobStatus((byte) 3);
                newInnerInventory.setJobOrderDetId(wmsInPutawayOrderDet.getJobOrderDetId());
                newInnerInventory.setOrgId(sysUser.getOrganizationId());
                newInnerInventory.setCreateTime(new Date());
                newInnerInventory.setCreateUserId(sysUser.getUserId());
                wmsInnerInventoryService.save(newInnerInventory);
                // 变更减少原库存
                innerInventory.setPackingQty(innerInventory.getPackingQty().subtract(wmsInPutawayOrderDet.getPlanQty()));
                wmsInnerInventoryService.update(innerInventory);
            }
        }
        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        String[] arrId = ids.split(",");
        for (String s : arrId) {
            WmsInnerJobOrder wmsInnerJobOrder =  wmsInPutawayOrderMapper.selectByPrimaryKey(s);
            if(wmsInnerJobOrder.getOrderStatus()>=(byte)4){
                throw new BizErrorException("单据已经作业，无法删除");
            }
            if(StringUtils.isEmpty(wmsInnerJobOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId",s);
            wmsInPutawayOrderDetMapper.deleteByExample(example);
        }
        return wmsInPutawayOrderMapper.deleteByIds(ids);
    }

    /**
     * 分配库存
     * @param wmsInnerJobOrderDto
     * @param wmsInnerJobOrderDetDto
     * @return
     */
    private int updateInventory(WmsInnerJobOrderDto wmsInnerJobOrderDto,WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto){
        SysUser sysUser = currentUser();
        WmsInAsnOrderDto wmsInAsnOrderDto = inFeignApi.findList(SearchWmsInAsnOrder.builder()
                .asnOrderId(wmsInnerJobOrderDto.getSourceOrderId())
                .build()).getData().get(0);

        WmsInAsnOrderDetDto wmsInAsnOrderDetDto = inFeignApi.findDetList(SearchWmsInAsnOrderDet.builder()
                .asnOrderDetId(wmsInnerJobOrderDetDto.getSourceDetId())
                .build()).getData().get(0);

        //旧
        Example example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("relevanceOrderCode",wmsInAsnOrderDto.getAsnCode()).andEqualTo("materialId",wmsInAsnOrderDetDto.getMaterialId()).andEqualTo("warehouseId",wmsInAsnOrderDetDto.getWarehouseId()).andEqualTo("storageId",wmsInAsnOrderDetDto.getStorageId());
        if(!StringUtils.isEmpty(wmsInAsnOrderDetDto.getBatchCode())){
            criteria.andEqualTo("batchCode",wmsInAsnOrderDetDto.getBatchCode());
        }
        WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(wmsInnerInventory)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        WmsInnerInventory wmsIn = new WmsInnerInventory();
        wmsIn.setInventoryId(wmsInnerInventory.getInventoryId());
        wmsIn.setPackingQty(wmsInnerInventory.getPackingQty().subtract(wmsInnerJobOrderDetDto.getDistributionQty()));
        int num = wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsIn);

        example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("relevanceOrderCode",wmsInnerJobOrderDto.getJobOrderCode()).andEqualTo("materialId",wmsInAsnOrderDetDto.getMaterialId()).andEqualTo("warehouseId",wmsInnerJobOrderDto.getWarehouseId()).andEqualTo("storageId",wmsInnerJobOrderDetDto.getOutStorageId());
        if(!StringUtils.isEmpty(wmsInAsnOrderDetDto.getBatchCode())){
            criteria1.andEqualTo("batchCode",wmsInAsnOrderDetDto.getBatchCode());
        }
        criteria1.andEqualTo("jobOrderDetId",wmsInnerJobOrderDetDto.getJobOrderDetId());
        criteria1.andEqualTo("jobStatus",(byte)2);
        WmsInnerInventory wmsInnerInventorys = wmsInnerInventoryMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(wmsInnerInventorys)){
            //添加库存
            WmsInnerInventory inv = new WmsInnerInventory();
            BeanUtil.copyProperties(wmsInnerInventory,inv);
            inv.setStorageId(wmsInnerJobOrderDetDto.getOutStorageId());
            inv.setWarehouseId(wmsInnerJobOrderDetDto.getWarehouseId());
            inv.setRelevanceOrderCode(wmsInnerJobOrderDto.getJobOrderCode());
            inv.setPackingQty(wmsInnerJobOrderDetDto.getDistributionQty());
            inv.setJobStatus((byte)2);
            inv.setInventoryId(null);
            inv.setBatchCode(wmsInnerJobOrderDetDto.getBatchCode());
            inv.setJobOrderDetId(wmsInnerJobOrderDetDto.getJobOrderDetId());
            inv.setOrgId(sysUser.getOrganizationId());
            inv.setCreateUserId(sysUser.getUserId());
            inv.setCreateTime(new Date());
            inv.setModifiedTime(new Date());
            inv.setModifiedUserId(sysUser.getUserId());
            return wmsInnerInventoryMapper.insertSelective(inv);
        }else{
            //原库存
            wmsInnerInventorys.setPackingQty(wmsInnerInventorys.getPackingQty().add(wmsInnerJobOrderDetDto.getDistributionQty()));
            return wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
        }
    }

    /**
     * 库存
     * @return
     */
    private int Inventory(WmsInnerJobOrderDetDto oldDto,WmsInnerJobOrderDetDto newDto){
        SysUser sysUser = currentUser();
        WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(oldDto.getJobOrderId());
        //旧
        Example example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("relevanceOrderCode",wmsInnerJobOrder.getJobOrderCode()).andEqualTo("materialId",oldDto.getMaterialId()).andEqualTo("warehouseId",oldDto.getWarehouseId()).andEqualTo("storageId",oldDto.getOutStorageId());
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
        criteria1.andEqualTo("materialId",newDto.getMaterialId()).andEqualTo("warehouseId",newDto.getWarehouseId()).andEqualTo("storageId",newDto.getInStorageId());
        if(!StringUtils.isEmpty(newDto.getBatchCode())){
            criteria1.andEqualTo("batchCode",newDto.getBatchCode());
        }
        criteria.andEqualTo("jobOrderDetId",newDto.getJobOrderDetId());
        criteria1.andEqualTo("jobStatus",(byte)1);
        WmsInnerInventory wmsInnerInventorys = wmsInnerInventoryMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(wmsInnerInventorys)){
            //添加库存
            WmsInnerInventory inv = new WmsInnerInventory();
            BeanUtil.copyProperties(wmsInnerInventory,inv);
            inv.setStorageId(newDto.getInStorageId());
            inv.setWarehouseId(newDto.getWarehouseId());
            inv.setPackingQty(newDto.getActualQty());
            inv.setJobStatus((byte)1);
            inv.setBatchCode(newDto.getBatchCode());
            inv.setJobOrderDetId(newDto.getJobOrderDetId());
            inv.setInventoryId(null);
            inv.setCreateUserId(sysUser.getUserId());
            inv.setCreateTime(new Date());
            inv.setModifiedTime(new Date());
            inv.setModifiedUserId(sysUser.getUserId());
            inv.setRelevanceOrderCode(wmsInnerJobOrder.getJobOrderCode());
            return wmsInnerInventoryMapper.insertSelective(inv);
        }else{
            //原库存
            wmsInnerInventorys.setPackingQty(wmsInnerInventorys.getPackingQty().add(newDto.getActualQty()));
            wmsInnerInventorys.setRelevanceOrderCode(wmsInnerJobOrder.getJobOrderCode());
            return wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
        }
    }
//    private void checkBarCode(){
//        //1、判断是否展板自动收货还是PDA收货
//        //2、栈板自动收货匹配栈板条码还是成品条码
//        //一个成品条码数量为1、一个栈板返回栈板数量
//        //如果收到收货直接匹配库存明细
//    }
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
