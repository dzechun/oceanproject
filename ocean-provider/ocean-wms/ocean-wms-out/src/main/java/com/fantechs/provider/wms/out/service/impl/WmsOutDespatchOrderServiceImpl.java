package com.fantechs.provider.wms.out.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderSummaryDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryLogDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDespatchOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDespatchOrderReJoReDetDto;
import com.fantechs.common.base.general.entity.eng.EngPackingOrder;
import com.fantechs.common.base.general.entity.om.OmOtherOutOrderDet;
import com.fantechs.common.base.general.entity.om.OmSalesOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryLog;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.out.*;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutDeliveryOrderDet;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutDespatchOrder;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutDespatchOrderReJoReDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.*;
import com.fantechs.provider.api.guest.eng.EngFeignApi;
import com.fantechs.provider.api.qms.OMFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.in.InFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.wms.out.mapper.*;
import com.fantechs.provider.wms.out.service.WmsOutDespatchOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * Created by Mr.Lei on 2021/05/10.
 */
@Service
public class WmsOutDespatchOrderServiceImpl extends BaseService<WmsOutDespatchOrder> implements WmsOutDespatchOrderService {

    @Resource
    private WmsOutDespatchOrderMapper wmsOutDespatchOrderMapper;
    @Resource
    private InnerFeignApi innerFeignApi;
    @Resource
    private WmsOutDespatchOrderReJoMapper wmsOutDespatchOrderReJoMapper;
    @Resource
    private WmsOutDespatchOrderReJoReDetMapper wmsOutDespatchOrderReJoReDetMapper;
    @Resource
    private WmsOutDeliveryOrderDetMapper wmsOutDeliveryOrderDetMapper;
    @Resource
    private OMFeignApi omFeignApi;
    @Resource
    private WmsOutDeliveryOrderMapper wmsOutDeliveryOrderMapper;
    @Resource
    private InFeignApi inFeignApi;
    @Resource
    private EngFeignApi engFeignApi;
    @Resource
    private SecurityFeignApi securityFeignApi;


    @Override
    public List<WmsOutDespatchOrderDto> findList(SearchWmsOutDespatchOrder searchWmsOutDespatchOrder) {
        SysUser sysUser = currentUser();
        searchWmsOutDespatchOrder.setOrgId(sysUser.getOrganizationId());
        return wmsOutDespatchOrderMapper.findList(searchWmsOutDespatchOrder);
    }

    /**
     * 发运
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int forwarding(String ids) {
        SysUser sysUser = currentUser();
        String[] arrIds = ids.split(",");
        int num = 0;
        for (String arrId : arrIds) {
            WmsOutDespatchOrder wmsOutDespatchOrder = wmsOutDespatchOrderMapper.selectByPrimaryKey(arrId);
            Example example = new Example(WmsOutDespatchOrderReJo.class);
            example.createCriteria().andEqualTo("despatchOrderId",arrId);
            List<WmsOutDespatchOrderReJo> list = wmsOutDespatchOrderReJoMapper.selectByExample(example);
            for (WmsOutDespatchOrderReJo wms : list) {
                SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
                searchWmsInnerJobOrder.setJobOrderId(wms.getJobOrderId());
                WmsInnerJobOrderDto wmsInnerJobOrder = innerFeignApi.findList(searchWmsInnerJobOrder).getData().get(0);

                Example example1 = new Example(WmsOutDespatchOrderReJoReDet.class);
                example1.createCriteria().andEqualTo("despatchOrderReJoId",wms.getDespatchOrderReJoId());
                List<WmsOutDespatchOrderReJoReDet> wmsOutDespatchOrderReJoReDetDtoList = wmsOutDespatchOrderReJoReDetMapper.selectByExample(example1);
                for (WmsOutDespatchOrderReJoReDet wmsOutDespatchOrderReJoReDet : wmsOutDespatchOrderReJoReDetDtoList) {
                    //明细
                    SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
                    searchWmsInnerJobOrderDet.setJobOrderDetId(wmsOutDespatchOrderReJoReDet.getJobOrderDetId());
                    WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto = innerFeignApi.findList(searchWmsInnerJobOrderDet).getData().get(0);
                    Map<String,Object> map = new HashMap<>();
                    map.put("relevanceOrderCode",wmsInnerJobOrder.getJobOrderCode());
                    map.put("warehouseId",wmsInnerJobOrder.getWarehouseId());
                    map.put("storageId",wmsInnerJobOrderDetDto.getInStorageId());
                    map.put("materialId",wmsInnerJobOrderDetDto.getMaterialId());
                    map.put("batchCode",wmsInnerJobOrderDetDto.getBatchCode());
                    map.put("jobOrderDetId",wmsInnerJobOrderDetDto.getJobOrderDetId());
                    map.put("jobStatus",(byte)2);
                    map.put("orgId",sysUser.getOrganizationId());
                    ResponseEntity<WmsInnerInventory> responseEntity = innerFeignApi.selectOneByExample(map);
                    if(responseEntity.getCode()!=0){
                        throw new BizErrorException(ErrorCodeEnum.GL9999404);
                    }
                    WmsInnerInventory wmsInnerInventory = responseEntity.getData();
                    if(StringUtils.isEmpty(wmsInnerInventory)){
                        throw new BizErrorException("未匹配到库存");
                    }
                    if(StringUtils.isEmpty(wmsInnerInventory.getPackingQty()) || wmsInnerJobOrderDetDto.getActualQty().compareTo(wmsInnerInventory.getPackingQty())==1){
                        throw new BizErrorException("库存不足");
                    }
                    wmsInnerInventory.setPackingQty(wmsInnerInventory.getPackingQty().subtract(wmsInnerJobOrderDetDto.getActualQty()));
                    ResponseEntity rs = innerFeignApi.updateByPrimaryKeySelective(wmsInnerInventory);
                    if(responseEntity.getCode()!=0){
                        throw new BizErrorException(rs.getMessage());
                    }
                    //数量反写销售订单
                    int i = this.writeDeliveryOrderQty(wmsInnerJobOrder,wmsInnerJobOrderDetDto);
                    if (i<1){
                        throw new BizErrorException("发运失败");
                    }
                    //反写拣货单状态
                    responseEntity = this.retrographyStatus(wmsInnerJobOrderDetDto);
                    if(responseEntity.getCode()!=0){
                        throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
                    }

                    WmsInnerInventoryDet wmsInnerInventoryDet = new WmsInnerInventoryDet();
                    wmsInnerInventoryDet.setDeliveryOrderCode(wmsInnerJobOrder.getJobOrderCode());
                    wmsInnerInventoryDet.setStorageId(wmsInnerJobOrderDetDto.getOutStorageId());
                    //wmsInnerInventoryDet.setMaterialQty(wmsInnerJobOrderDetDto.getActualQty());
                    wmsInnerInventoryDet.setBarcodeStatus((byte)6);
                    rs = innerFeignApi.subtract(wmsInnerInventoryDet);
                    if(responseEntity.getCode()!=0){
                        throw new BizErrorException("发运失败");
                    }

                    //添加库存日志
                    WmsInnerInventoryLog wmsInnerInventoryLog = new WmsInnerInventoryLogDto();
                    BeanUtil.copyProperties(wmsInnerInventory,wmsInnerInventoryLog);
//                    wmsInnerInventoryLog.setAsnCode(wmsInnerJobOrder.getRelatedOrderCode());
                    wmsInnerInventoryLog.setRelatedOrderCode(wmsInnerJobOrder.getJobOrderCode());
                    //发运
                    wmsInnerInventoryLog.setJobOrderType((byte)8);
                    wmsInnerInventoryLog.setAddOrSubtract((byte)2);
                    wmsInnerInventoryLog.setStorageId(wmsInnerJobOrderDetDto.getInStorageId());
                    wmsInnerInventoryLog.setWarehouseId(wmsInnerJobOrder.getWarehouseId());
                    wmsInnerInventoryLog.setMaterialId(wmsInnerJobOrderDetDto.getMaterialId());
                    wmsInnerInventoryLog.setProductionDate(wmsInnerJobOrderDetDto.getProductionDate());
//                    wmsInnerInventoryLog.setExpiredDate(wmsInnerJobOrderDetDto.getExpiredDate());
                    wmsInnerInventoryLog.setBatchCode(wmsInnerJobOrderDetDto.getBatchCode());
//                    wmsInnerInventoryLog.setPalletCode(wmsInnerJobOrderDetDto.getPalletCode());
                    wmsInnerInventoryLog.setInventoryStatusId(wmsInnerJobOrderDetDto.getInventoryStatusId());
                    wmsInnerInventoryLog.setInitialQty(wmsInnerJobOrderDetDto.getActualQty());
                    wmsInnerInventoryLog.setChangeQty(wmsInnerJobOrderDetDto.getActualQty());
//                    wmsInnerInventoryLog.setMaterialOwnerId(wmsInnerJobOrder.getMaterialOwnerId());
                    wmsInnerInventoryLog.setOrgId(sysUser.getOrganizationId());
                    innerFeignApi.add(wmsInnerInventoryLog);


                    //记录材料日志
                    //获取程序配置项
                    SearchSysSpecItem searchSysSpecItemFiveRing = new SearchSysSpecItem();
                    searchSysSpecItemFiveRing.setSpecCode("sendMaterialLogMessage");
                    List<SysSpecItem> itemListFiveRing = securityFeignApi.findSpecItemList(searchSysSpecItemFiveRing).getData();
                    if(itemListFiveRing.size()<1){
                        throw new BizErrorException("配置项 sendMaterialLogMessage 获取失败");
                    }
                    SysSpecItem sysSpecItem = itemListFiveRing.get(0);
                    if("1".equals(sysSpecItem.getParaValue())) {
                        List<EngPackingOrderSummaryDetDto> lists = new ArrayList<>();
                        EngPackingOrderSummaryDetDto engPackingOrderSummaryDetDto = new EngPackingOrderSummaryDetDto();
                        engPackingOrderSummaryDetDto.setContractCode(wmsInnerInventory.getContractCode());
                        engPackingOrderSummaryDetDto.setPurchaseReqOrderCode(wmsInnerInventory.getPurchaseReqOrderCode());
                        engPackingOrderSummaryDetDto.setLocationNum(wmsInnerInventory.getOption4());
                        engPackingOrderSummaryDetDto.setDeviceCode(wmsInnerInventory.getOption1());
                        engPackingOrderSummaryDetDto.setDominantTermCode(wmsInnerInventory.getOption2());
                        engPackingOrderSummaryDetDto.setMaterialCode(wmsInnerJobOrderDetDto.getMaterialCode());
                        engPackingOrderSummaryDetDto.setQty(wmsInnerJobOrderDetDto.getActualQty());
                        lists.add(engPackingOrderSummaryDetDto);
                        EngPackingOrder engPackingOrder = new EngPackingOrder();
//                        engPackingOrder.setPackingOrderCode(wmsInnerJobOrder.getRelatedOrderCode());
                        engPackingOrder.setSummaryDetList(lists);
                        engFeignApi.saveRecord(engPackingOrder,(byte)7,"出库");
                    }
                }
            }
            wmsOutDespatchOrder.setOrderStatus((byte)4);
            num = wmsOutDespatchOrderMapper.updateByPrimaryKeySelective(wmsOutDespatchOrder);
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int finishTruckloading(String ids) {
        String[] arrIds = ids.split(",");
        int num =0;
        for (String arrId : arrIds) {
            WmsOutDespatchOrder wmsOutDespatchOrder = wmsOutDespatchOrderMapper.selectByPrimaryKey(arrId);
            if(StringUtils.isEmpty(wmsOutDespatchOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            wmsOutDespatchOrder.setOrderStatus((byte)3);
            num +=wmsOutDespatchOrderMapper.updateByPrimaryKeySelective(wmsOutDespatchOrder);
        }
        return num;
    }

    @Override
    public List<WmsOutDespatchOrderReJoReDetDto> findDetList(SearchWmsOutDespatchOrderReJoReDet searchWmsOutDespatchOrderReJoReDet) {
        return wmsOutDespatchOrderReJoReDetMapper.findList(searchWmsOutDespatchOrderReJoReDet);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public String add(WmsOutDespatchOrder record) {
        SysUser sysUser = currentUser();
        record.setDespatchOrderCode(CodeUtils.getId("TRUCK"));
        if(StringUtils.isEmpty(record.getOrderStatus())){
            if(StringUtils.isEmpty(record.getWmsOutDespatchOrderReJo())){
                record.setOrderStatus((byte)1);
            }else{
                record.setOrderStatus((byte)2);
            }
        }
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrgId(sysUser.getOrganizationId());
        int num = wmsOutDespatchOrderMapper.insertUseGeneratedKeys(record);
        for (WmsOutDespatchOrderReJo wms : record.getWmsOutDespatchOrderReJo()) {
            SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
            searchWmsInnerJobOrderDet.setJobOrderId(wms.getJobOrderId());
            List<Byte> byteList = new ArrayList<>();
            byteList.add((byte)4);
            byteList.add((byte)5);
//            searchWmsInnerJobOrderDet.setOrderStatusList(byteList);
            ResponseEntity<List<WmsInnerJobOrderDetDto>> responseEntity = innerFeignApi.findList(searchWmsInnerJobOrderDet);
            if(responseEntity.getCode()!=0){
                throw new BizErrorException(ErrorCodeEnum.GL9999404);
            }
            List<WmsInnerJobOrderDetDto> list = responseEntity.getData();
            wms.setDespatchOrderId(record.getDespatchOrderId());
            wms.setCreateTime(new Date());
            wms.setCreateUserId(sysUser.getUserId());
            wms.setModifiedTime(new Date());
            wms.setModifiedUserId(sysUser.getUserId());
            wms.setOrgId(sysUser.getOrganizationId());
            wmsOutDespatchOrderReJoMapper.insertUseGeneratedKeys(wms);
            for (WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto : list) {
                if(StringUtils.isNotEmpty(wmsInnerJobOrderDetDto.getActualQty()) && wmsInnerJobOrderDetDto.getActualQty().compareTo(BigDecimal.ZERO)==1){
                    WmsOutDespatchOrderReJoReDet wmsOutDespatchOrderReJoReDet = new WmsOutDespatchOrderReJoReDetDto();
                    wmsOutDespatchOrderReJoReDet.setJobOrderDetId(wmsInnerJobOrderDetDto.getJobOrderDetId());
                    wmsOutDespatchOrderReJoReDet.setDespatchOrderReJoId(wms.getDespatchOrderReJoId());
                    wmsOutDespatchOrderReJoReDet.setCreateTime(new Date());
                    wmsOutDespatchOrderReJoReDet.setCreateUserId(sysUser.getUserId());
                    wmsOutDespatchOrderReJoReDet.setModifiedTime(new Date());
                    wmsOutDespatchOrderReJoReDet.setModifiedUserId(sysUser.getUserId());
                    wmsOutDespatchOrderReJoReDet.setOrgId(sysUser.getOrganizationId());
                    num +=wmsOutDespatchOrderReJoReDetMapper.insertSelective(wmsOutDespatchOrderReJoReDet);
                }
            }
        }
        return record.getDespatchOrderId().toString();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(WmsOutDespatchOrder entity) {
        SysUser sysUser = currentUser();

        int num = 0;

        Example example = new Example(WmsOutDespatchOrderReJo.class);
        example.createCriteria().andEqualTo("despatchOrderId",entity.getDespatchOrderId());
        wmsOutDespatchOrderReJoMapper.deleteByExample(example);
        for (WmsOutDespatchOrderReJo wms : entity.getWmsOutDespatchOrderReJo()) {
            SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
            searchWmsInnerJobOrderDet.setJobOrderId(wms.getJobOrderId());
            ResponseEntity<List<WmsInnerJobOrderDetDto>> responseEntity = innerFeignApi.findList(searchWmsInnerJobOrderDet);
            if(responseEntity.getCode()!=0){
                throw new BizErrorException(ErrorCodeEnum.GL9999404);
            }
            List<WmsInnerJobOrderDetDto> list = responseEntity.getData();
            wms.setDespatchOrderId(entity.getDespatchOrderId());
            wms.setCreateTime(new Date());
            wms.setCreateUserId(sysUser.getUserId());
            wms.setModifiedTime(new Date());
            wms.setModifiedUserId(sysUser.getUserId());
            wms.setOrgId(sysUser.getOrganizationId());
            wmsOutDespatchOrderReJoMapper.insertUseGeneratedKeys(wms);
            for (WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto : list) {
                WmsOutDespatchOrderReJoReDet wmsOutDespatchOrderReJoReDet = new WmsOutDespatchOrderReJoReDetDto();
                wmsOutDespatchOrderReJoReDet.setJobOrderDetId(wmsInnerJobOrderDetDto.getJobOrderDetId());
                wmsOutDespatchOrderReJoReDet.setDespatchOrderReJoId(wms.getDespatchOrderReJoId());
                wmsOutDespatchOrderReJoReDet.setCreateTime(new Date());
                wmsOutDespatchOrderReJoReDet.setCreateUserId(sysUser.getUserId());
                wmsOutDespatchOrderReJoReDet.setModifiedTime(new Date());
                wmsOutDespatchOrderReJoReDet.setModifiedUserId(sysUser.getUserId());
                wmsOutDespatchOrderReJoReDet.setOrgId(sysUser.getOrganizationId());
                num +=wmsOutDespatchOrderReJoReDetMapper.insertSelective(wmsOutDespatchOrderReJoReDet);
            }
        }
        if(StringUtils.isNotEmpty(entity.getOrderStatus()) && entity.getOrderStatus()==1){
            entity.setOrderStatus((byte)2);
        }
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(sysUser.getUserId());
        num+=wmsOutDespatchOrderMapper.updateByPrimaryKeySelective(entity);
        return num;
    }

    @Override
    public int batchDelete(String ids) {
        SysUser sysUser = currentUser();
        String[] arrIds = ids.split(",");
        for (String id : arrIds) {
            WmsOutDespatchOrder wmsOutDespatchOrder = wmsOutDespatchOrderMapper.selectByPrimaryKey(id);
            this.recoverStatus(wmsOutDespatchOrder.getDespatchOrderId());
            if(wmsOutDespatchOrder.getOrderStatus()==(4)){
                throw new BizErrorException("单据已完成，无法删除");
            }
            if(StringUtils.isEmpty(wmsOutDespatchOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
        }
        return super.batchDelete(ids);
    }

    /**
     * 恢复拣货作业单据装车状态
     * @param despatchOrderId
     */
    private void recoverStatus(Long despatchOrderId){
        Example example = new Example(WmsOutDespatchOrderReJo.class);
        example.createCriteria().andEqualTo("despatchOrderId",despatchOrderId);
        List<WmsOutDespatchOrderReJo> list = wmsOutDespatchOrderReJoMapper.selectByExample(example);
        for (WmsOutDespatchOrderReJo wms : list) {
            example = new Example(WmsOutDespatchOrderReJoReDet.class);
            example.createCriteria().andEqualTo("despatchOrderReJoId",wms.getDespatchOrderReJoId());
            List<WmsOutDespatchOrderReJoReDet> wmsOutDespatchOrderReJoReDets = wmsOutDespatchOrderReJoReDetMapper.selectByExample(example);
            for (WmsOutDespatchOrderReJoReDet wmsOutDespatchOrderReJoReDet : wmsOutDespatchOrderReJoReDets) {
                ResponseEntity responseEntity = innerFeignApi.retrographyStatus(WmsInnerJobOrderDet.builder()
                        .jobOrderDetId(wmsOutDespatchOrderReJoReDet.getJobOrderDetId())
                        .jobOrderId(wms.getJobOrderId())
//                        .orderStatus((byte)5)
                        .build());
                if(responseEntity.getCode()!=0){
                    throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
                }
            }
        }
    }

    /**
     * 反写销售出库单拣货数量
     * @param wmsInnerJobOrderDto
     * @param wmsInnerJobOrderDetDto
     * @return
     */
    private int writeDeliveryOrderQty(WmsInnerJobOrderDto wmsInnerJobOrderDto,WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto){
        SysUser sysUser = currentUser();
        WmsOutDeliveryOrderDet wmsOutDeliveryOrderDet = wmsOutDeliveryOrderDetMapper.selectByPrimaryKey(wmsInnerJobOrderDetDto.getSourceId());
        if(StringUtils.isEmpty(wmsOutDeliveryOrderDet)){
            throw new BizErrorException("未匹配到关联的出库单");
        }
        if(StringUtils.isEmpty(wmsOutDeliveryOrderDet.getDispatchQty())){
            wmsOutDeliveryOrderDet.setDispatchQty(BigDecimal.ZERO);
        }
        wmsOutDeliveryOrderDet.setDispatchQty(wmsOutDeliveryOrderDet.getDispatchQty().add(wmsInnerJobOrderDetDto.getActualQty()));
        //查询出库单下所有拣货作业
//        Example example = new Example(WmsInnerJobOrder.class);
//        example.createCriteria().andEqualTo("sourceOrderId",wmsOutDeliveryOrderDet.getDeliveryOrderId());
//        List<WmsInnerJobOrder> list =

        WmsOutDeliveryOrder wmsOutDeliveryOrder  =  wmsOutDeliveryOrderMapper.selectByPrimaryKey(null);
        if(StringUtils.isEmpty(wmsOutDeliveryOrder)){
            throw new BizErrorException(ErrorCodeEnum.GL9999404);
        }
        int num = 0;
        switch (wmsOutDeliveryOrder.getOrderTypeId().toString()){
            //销售订单
            case "1":
                if(StringUtils.isNotEmpty(wmsOutDeliveryOrder.getSourceOrderId()) && StringUtils.isNotEmpty(wmsOutDeliveryOrderDet.getSourceOrderId())){
                    //反写销售订单出库数量
                    OmSalesOrderDet omSalesOrderDet = new OmSalesOrderDet();
                    omSalesOrderDet.setIsWriteQty(1);
                    omSalesOrderDet.setSalesOrderId(wmsOutDeliveryOrderDet.getSourceOrderId());
                    omSalesOrderDet.setSalesOrderDetId(wmsOutDeliveryOrderDet.getOrderDetId());
                    omSalesOrderDet.setTotalOutboundQty(wmsInnerJobOrderDetDto.getActualQty());
//                    omSalesOrderDet.setActualDeliverDate(DateUtils.getDateString(new Date(),"yyyy-MM-dd"));
                    ResponseEntity responseEntity = omFeignApi.update(omSalesOrderDet);
                }
                num+=wmsOutDeliveryOrderDetMapper.updateByPrimaryKeySelective(wmsOutDeliveryOrderDet);
                break;
                //调拨and其他出库反写数量
            case "2":
                num+=wmsOutDeliveryOrderDetMapper.updateByPrimaryKeySelective(wmsOutDeliveryOrderDet);

                //生成调拨入库单
//                WmsInAsnOrder wmsInAsnOrder = new WmsInAsnOrder();
//                List<WmsInAsnOrderDet> wmsInAsnOrderDets = new ArrayList<>();
                //获取调拨出库单
                WmsOutDeliveryOrder res = wmsOutDeliveryOrderMapper.selectByPrimaryKey(wmsOutDeliveryOrder.getDeliveryOrderId());

                //获取调拨订单调入仓库
                Long warehouseId= wmsOutDespatchOrderMapper.findOmWarehouseId(res.getSourceOrderId());

                //获取收货库位
                Map<String,Object> map = new HashMap<>();
                map.put("orgId",sysUser.getOrganizationId());
                map.put("warehouseId",warehouseId);
                map.put("storageType",2);
                Long storageId = wmsOutDespatchOrderMapper.findStorageId(map);
                if(StringUtils.isEmpty(storageId)){
                    throw new BizErrorException("未获取到该仓库下的收货库位");
                }
//                wmsInAsnOrder.setSourceOrderId(res.getSourceOrderId());
//                wmsInAsnOrder.setMaterialOwnerId(res.getMaterialOwnerId());
//                wmsInAsnOrder.setSupplierId(res.getSupplierId());
//                //调拨订单号
//                wmsInAsnOrder.setRelatedOrderCode1(res.getRelatedOrderCode1());
//                //调拨出库单号
//                wmsInAsnOrder.setRelatedOrderCode2(res.getDeliveryOrderCode());
//                wmsInAsnOrder.setCustomerOrderCode(res.getCustomerOrderCode());
//                wmsInAsnOrder.setOrderDate(res.getOrderDate());
//                wmsInAsnOrder.setWarehouseId(warehouseId);
//                wmsInAsnOrder.setStorageId(storageId);
//                wmsInAsnOrder.setPlanAgoDate(new Date());
//                wmsInAsnOrder.setLinkManName(res.getLinkManName());
//                wmsInAsnOrder.setLinkManPhone(res.getLinkManPhone());
//                wmsInAsnOrder.setFaxNumber(res.getFaxNumber());
//                wmsInAsnOrder.setEMailAddress(res.getEmailAddress());
                SearchWmsOutDeliveryOrderDet searchWmsOutDeliveryOrderDet = new SearchWmsOutDeliveryOrderDet();
                searchWmsOutDeliveryOrderDet.setDeliveryOrderId(res.getDeliveryOrderId());
                int i = 0;
                List<WmsOutDeliveryOrderDetDto> wmsOutDeliveryOrderDets = wmsOutDeliveryOrderDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutDeliveryOrderDet));
                for (WmsOutDeliveryOrderDetDto dets : wmsOutDeliveryOrderDets) {
                    i++;
//                    WmsInAsnOrderDet wmsInAsnOrderDet = new WmsInAsnOrderDet();
//                    wmsInAsnOrderDet.setSourceOrderId(dets.getDeliveryOrderId());
//                    wmsInAsnOrderDet.setOrderDetId(dets.getDeliveryOrderDetId());
//                    wmsInAsnOrderDet.setWarehouseId(warehouseId);
//                    wmsInAsnOrderDet.setStorageId(storageId);
//                    wmsInAsnOrderDet.setLineNumber(i);
//                    wmsInAsnOrderDet.setInventoryStatusId(dets.getInventoryStatusId());
//                    wmsInAsnOrderDet.setMaterialId(dets.getMaterialId());
//                    wmsInAsnOrderDet.setPackingUnitName(dets.getPackingUnitName());
//                    wmsInAsnOrderDet.setPackingQty(dets.getPackingQty());
//                    wmsInAsnOrderDet.setBatchCode(dets.getBatchCode());
//                    wmsInAsnOrderDets.add(wmsInAsnOrderDet);
                }
//                wmsInAsnOrder.setWmsInAsnOrderDetList(wmsInAsnOrderDets);
//                ResponseEntity rer = inFeignApi.save(wmsInAsnOrder);
//                if(rer.getCode()!=0){
//                    throw new BizErrorException(rer.getMessage());
//                }

                break;
            case "7":
                //其他出库
                if(StringUtils.isNotEmpty(wmsOutDeliveryOrder.getSourceOrderId(),wmsOutDeliveryOrderDet.getOrderDetId())){
                    OmOtherOutOrderDet omOtherOutOrderDet = new OmOtherOutOrderDet();
                    omOtherOutOrderDet.setOtherOutOrderId(wmsOutDeliveryOrder.getSourceOrderId());
                    omOtherOutOrderDet.setOtherOutOrderDetId(wmsOutDeliveryOrderDet.getOrderDetId());
//                    omOtherOutOrderDet.setDispatchQty(wmsInnerJobOrderDetDto.getActualQty());
                    ResponseEntity responseEntity = omFeignApi.writeQtyToOut(omOtherOutOrderDet);
                }
                num+=wmsOutDeliveryOrderDetMapper.updateByPrimaryKeySelective(wmsOutDeliveryOrderDet);
                break;
            case "8":
                num+=wmsOutDeliveryOrderDetMapper.updateByPrimaryKeySelective(wmsOutDeliveryOrderDet);
                break;
        }
        //查询出库单对应拣货单是否都已经发运完成
        if(wmsOutDeliveryOrder.getOrderTypeId()==2) {
            Integer total1 = wmsOutDespatchOrderReJoMapper.findCountQty(wmsOutDeliveryOrder.getDeliveryOrderId(), wmsOutDeliveryOrder.getOrderTypeId());
            Integer totalCount1 = wmsOutDespatchOrderReJoMapper.findCount(wmsOutDeliveryOrder.getDeliveryOrderId(), wmsOutDeliveryOrder.getOrderTypeId());
            if (total1.equals(totalCount1)) {
                //更新调拨出库单状态
                num += wmsOutDespatchOrderReJoMapper.writeOutQty((byte) 5, wmsOutDeliveryOrder.getDeliveryOrderId());
                //领料出库回传接口（五环） 在拣货作业单完成时回传
//            if(wmsOutDeliveryOrder.getOrderTypeId().toString().equals("8")){
//                engFeignApi.reportDeliveryOrderOrder(wmsOutDeliveryOrder);
//            }

            } else {
                num += wmsOutDespatchOrderReJoMapper.writeOutQty((byte) 4, wmsOutDeliveryOrder.getDeliveryOrderId());
            }
        }else {
            Example example = new Example(WmsOutDeliveryOrderDet.class);
            example.createCriteria().andEqualTo("deliveryOrderId",wmsOutDeliveryOrder.getDeliveryOrderId());
            List<WmsOutDeliveryOrderDet> list = wmsOutDeliveryOrderDetMapper.selectByExample(example);
            BigDecimal totalQty =list.stream().map(i->{
                if (StringUtils.isNotEmpty(i.getPackingQty())) {
                    return i.getPackingQty();
                }else {
                    return BigDecimal.ZERO;
                }
            }).reduce(BigDecimal.ZERO,BigDecimal::add);
            BigDecimal totalDisQty = list.stream().map(i->{
                if(StringUtils.isNotEmpty(i.getDispatchQty())){
                    return i.getDispatchQty();
                }else {
                    return BigDecimal.ZERO;
                }
            }).reduce(BigDecimal.ZERO,BigDecimal::add);
            //BigDecimal totalQty = list.stream().map(WmsOutDeliveryOrderDet::getPackingQty).reduce(BigDecimal.ZERO,BigDecimal::add);
            //BigDecimal totalDisQty = list.stream().map(WmsOutDeliveryOrderDet::getDispatchQty).reduce(BigDecimal.ZERO,BigDecimal::add);
            WmsOutDeliveryOrder wms =new WmsOutDeliveryOrder();
            wms.setDeliveryOrderId(wmsOutDeliveryOrder.getDeliveryOrderId());
            if(totalQty.compareTo(totalDisQty)==0){
                wms.setOrderStatus((byte)5);

            }else {
                wms.setOrderStatus((byte)4);
            }
            wmsOutDeliveryOrderMapper.updateByPrimaryKeySelective(wms);
        }
       return num;
    }

    /**
     * 更新拣货单状态
     * @param wmsInnerJobOrderDetDto
     * @return
     */
    private ResponseEntity retrographyStatus(WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto){
        ResponseEntity responseEntity = innerFeignApi.retrographyStatus(WmsInnerJobOrderDet.builder()
                .jobOrderDetId(wmsInnerJobOrderDetDto.getJobOrderDetId())
                .jobOrderId(wmsInnerJobOrderDetDto.getJobOrderId())
//                .orderStatus((byte)6)
                .build());
        if(responseEntity.getCode()!=0){
            throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
        }
        return responseEntity;
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
