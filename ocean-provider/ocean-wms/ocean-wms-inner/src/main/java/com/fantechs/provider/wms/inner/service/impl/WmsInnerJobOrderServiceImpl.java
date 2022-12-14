package com.fantechs.provider.wms.inner.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseStorageRule;
import com.fantechs.common.base.general.dto.basic.BaseWorkerDto;
import com.fantechs.common.base.general.dto.wms.in.BarPODto;
import com.fantechs.common.base.general.dto.wms.in.WmsInAsnOrderDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.entity.basic.BaseInventoryStatus;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.BaseStorageCapacity;
import com.fantechs.common.base.general.entity.basic.search.*;
import com.fantechs.common.base.general.entity.wanbao.WanbaoStacking;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrderDet;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInAsnOrder;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInAsnOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.*;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.guest.wanbao.WanbaoFeignApi;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.in.InFeignApi;
import com.fantechs.provider.wms.inner.mapper.*;
import com.fantechs.provider.wms.inner.service.*;
import com.fantechs.provider.wms.inner.util.InBarcodeUtil;
import com.fantechs.provider.wms.inner.util.InventoryLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Mr.Lei on 2021/05/06.
 */
@Service
@Slf4j
public class WmsInnerJobOrderServiceImpl extends BaseService<WmsInnerJobOrder> implements WmsInnerJobOrderService {
    @Resource
    private WmsInnerJobOrderMapper wmsInPutawayOrderMapper;
    @Resource
    private WmsInnerJobOrderDetMapper wmsInPutawayOrderDetMapper;
    @Resource
    private WmsInnerJobOrderDetService wmsInnerJobOrderDetService;
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
    @Resource
    WmsInnerJobOrderDetBarcodeService wmsInnerJobOrderDetBarcodeService;
    @Resource
    private PickingOrderService pickingOrderService;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private WanbaoFeignApi wanbaoFeignApi;
    @Resource
    WmsInnerHtJobOrderDetBarcodeService wmsInnerHtJobOrderDetBarcodeService;

    @Override
    public List<WmsInnerJobOrderDto> findList(SearchWmsInnerJobOrder searchWmsInnerJobOrder) {
        SysUser sysUser = currentUser();
        if(StringUtils.isEmpty(searchWmsInnerJobOrder.getOrgId())) {
            searchWmsInnerJobOrder.setOrgId(sysUser.getOrganizationId());
        }
        if (StringUtils.isNotEmpty(searchWmsInnerJobOrder.getJobOrderType()) && searchWmsInnerJobOrder.getJobOrderType() == (byte) 4){
            searchWmsInnerJobOrder.setUserId(sysUser.getUserId());
        }
        return wmsInPutawayOrderMapper.findList(searchWmsInnerJobOrder);
    }

    @Override
    public List<WmsInnerJobOrderDto> findShiftList(Map<String, Object> map) {
        return wmsInPutawayOrderMapper.findShiftList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDeleteByShiftWork(String ids) {
        String[] arrId = ids.split(",");
        for (String s : arrId) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(s);
            if (wmsInnerJobOrder.getOrderStatus() >= (byte) 4) {
                throw new BizErrorException("?????????????????????????????????");
            }
            if (StringUtils.isEmpty(wmsInnerJobOrder)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId", s);
            List<WmsInnerJobOrderDet> jobOrderDetList = wmsInPutawayOrderDetMapper.selectByExample(example);
            for (WmsInnerJobOrderDet det : jobOrderDetList) {
                wmsInPutawayOrderDetMapper.delete(det);
                // ???????????????????????????
                Example exampleInventory = new Example(WmsInnerInventory.class);
                exampleInventory.createCriteria().andEqualTo("jobOrderDetId", det.getJobOrderDetId());
                WmsInnerInventory innerInventory = wmsInnerInventoryMapper.selectOneByExample(exampleInventory);
                // ????????????????????????????????????
                WmsInnerInventory sourceInnerInventory = wmsInnerInventoryMapper.selectByPrimaryKey(innerInventory.getParentInventoryId());
                sourceInnerInventory.setPackingQty(sourceInnerInventory.getPackingQty().add(innerInventory.getPackingQty()));
                // ???????????????
                wmsInnerInventoryService.update(sourceInnerInventory);
                // ??????????????????
                wmsInnerInventoryService.delete(innerInventory);
            }
        }
        return wmsInPutawayOrderMapper.deleteByIds(ids);
    }

    /**
     * ????????????
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int autoDistribution(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        int num = 0;
        int success = 0;
        for (String id : arrId) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(id);
            if (wmsInnerJobOrder.getOrderStatus() > (byte) 2) {
                throw new BizErrorException("?????????????????????");
            }
            if(StringUtils.isEmpty(wmsInnerJobOrder.getOption1(),wmsInnerJobOrder.getOption2())){
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"????????????????????????,?????????????????????????????????");
            }
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId", wmsInnerJobOrder.getJobOrderId()).andEqualTo("orderStatus",1);
            List<WmsInnerJobOrderDet> list = wmsInPutawayOrderDetMapper.selectByExample(example);
            for (WmsInnerJobOrderDet wms : list) {
                if (StringUtils.isEmpty(wms)) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                }
                BaseStorageRule baseStorageRule = new BaseStorageRule();
                baseStorageRule.setLogicId(Long.parseLong(wmsInnerJobOrder.getOption1()));
                baseStorageRule.setProLineId(Long.parseLong(wmsInnerJobOrder.getOption2()));
                baseStorageRule.setSalesBarcode(wmsInnerJobOrder.getOption3());
                baseStorageRule.setPoCode(wmsInnerJobOrder.getOption4());
                baseStorageRule.setQty(wms.getPlanQty());
                baseStorageRule.setMaterialId(wms.getMaterialId());
                baseStorageRule.setInventoryStatusId(wms.getInventoryStatusId());
                baseStorageRule.setWorkOrderQty(new BigDecimal(wmsInnerJobOrder.getOption5()));
                //??????????????????
                ResponseEntity<Long> responseEntity = baseFeignApi.inRule(baseStorageRule);
                if(responseEntity.getCode()!=0){
                    throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
                }

                if(StringUtils.isNotEmpty(responseEntity.getData())){
                    Long storageId = responseEntity.getData();
                    wms.setInStorageId(storageId);
                    wms.setDistributionQty(wms.getPlanQty());
                    wms.setModifiedTime(new Date());
                    wms.setOrderStatus((byte)3);
                    wms.setWorkStartTime(new Date());
                    wms.setWorkEndTime(new Date());
                    wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wms);

                    SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
                    searchWmsInnerJobOrder.setJobOrderId(wms.getJobOrderId());
                    WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInPutawayOrderMapper.findList(searchWmsInnerJobOrder).get(0);
                    SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
                    searchWmsInnerJobOrderDet.setJobOrderDetId(wms.getJobOrderDetId());
                    WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);

                    num += this.updateInventory(wmsInnerJobOrderDto, wmsInnerJobOrderDetDto);

                    success++;
                }


//                JobRuleDto jobRuleDto = new JobRuleDto();
//                jobRuleDto.setPackageQty(wms.getPlanQty());
//                jobRuleDto.setWarehouseId(wms.getWarehouseId());
//                jobRuleDto.setMaterialId(wms.getMaterialId());
//                jobRuleDto.setBatchCode(StringUtils.isEmpty(wms.getBatchCode())?null:wms.getBatchCode());
//                jobRuleDto.setProDate(StringUtils.isEmpty(wms.getProductionDate())?null:DateUtils.getDateString(wms.getProductionDate(),"yyyy-MM-dd"));
//                ResponseEntity<List<StorageRuleDto>> responseEntity = baseFeignApi.JobRule(jobRuleDto);
//                if(responseEntity.getCode()!=0){
//                    throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
//                }
//                List<StorageRuleDto> list1 = responseEntity.getData();
//                if(list1.size()<1){
//                    throw new BizErrorException("??????????????????");
//                }
//                BigDecimal totalQty = wms.getPlanQty();
//                WmsInnerJobOrderDet wmsInnerJobOrderDet =null;
//                for (StorageRuleDto storageRuleDto : list1) {
//                    if(totalQty.compareTo(BigDecimal.ZERO)==1){
//                            num += wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(WmsInnerJobOrderDet.builder()
//                                    .jobOrderDetId(wms.getJobOrderDetId())
//                                    .inStorageId(storageRuleDto.getStorageId())
//                                    .planQty(storageRuleDto.getPutawayQty())
//                                    .distributionQty(storageRuleDto.getPutawayQty())
//                                    .modifiedUserId(sysUser.getUserId())
//                                    .modifiedTime(new Date())
//                                    .orderStatus((byte) 3)
//                                            .workStartTime(new Date())
//                                            .workEndTime(new Date())
//                                    .build());
//                            wmsInnerJobOrder.setOrderStatus((byte)3);
//                            wmsInnerJobOrder.setWorkStartTime(new Date());
//                            wmsInnerJobOrder.setWorkEndtTime(new Date());
//                            SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
//                            searchWmsInnerJobOrder.setJobOrderId(wms.getJobOrderId());
//                            WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInPutawayOrderMapper.findList(searchWmsInnerJobOrder).get(0);
//                            SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
//                            searchWmsInnerJobOrderDet.setJobOrderDetId(wms.getJobOrderDetId());
//                            WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);
//                            //????????????
//                            num += this.updateInventory(wmsInnerJobOrderDto, wmsInnerJobOrderDetDto);
//
//                            //??????????????????????????????????????????????????????
//                            if(storageRuleDto.getPutawayQty().compareTo(totalQty)==-1){
//                                WmsInnerJobOrderDet wmsInnerJobOrderDet1 = new WmsInnerJobOrderDet();
//                                BeanUtil.copyProperties(wms,wmsInnerJobOrderDet1);
//                                wmsInnerJobOrderDet1.setJobOrderDetId(null);
//                                wmsInnerJobOrderDet1.setPlanQty(totalQty.subtract(storageRuleDto.getPutawayQty()));
//                                wmsInnerJobOrderDet1.setInStorageId(null);
//                                wmsInnerJobOrderDet1.setDistributionQty(BigDecimal.ZERO);
//                                wmsInnerJobOrderDet1.setOrderStatus((byte)1);
//                                num += wmsInPutawayOrderDetMapper.insertUseGeneratedKeys(wmsInnerJobOrderDet1);
//                                wms = wmsInnerJobOrderDet1;
//                                wmsInnerJobOrder.setOrderStatus((byte)2);
//                                wmsInnerJobOrder.setWorkEndtTime(null);
//                            }
//                        totalQty = totalQty.subtract(storageRuleDto.getPutawayQty());
//                    }
//                }
            }
            if(success==0){
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"????????????????????????");
            }else if(success==list.size()){
                wmsInnerJobOrder.setOrderStatus((byte)3);
                wmsInnerJobOrder.setWorkStartTime(new Date());
                wmsInnerJobOrder.setWorkEndtTime(new Date());
            }else {
                wmsInnerJobOrder.setOrderStatus((byte)2);
                wmsInnerJobOrder.setWorkStartTime(new Date());
            }
            //?????????
            SearchBaseWorker searchBaseWorker = new SearchBaseWorker();
            searchBaseWorker.setWarehouseId(wmsInnerJobOrder.getWarehouseId());
            searchBaseWorker.setUserId(sysUser.getUserId());
            List<BaseWorkerDto> workerDtos = baseFeignApi.findList(searchBaseWorker).getData();
            if (!workerDtos.isEmpty()) {
                wmsInnerJobOrder.setWorkerId(workerDtos.get(0).getWorkerId());
            }
            wmsInnerJobOrder.setModifiedTime(new Date());
            wmsInnerJobOrder.setModifiedUserId(sysUser.getUserId());
            wmsInPutawayOrderMapper.updateByPrimaryKeySelective(wmsInnerJobOrder);
        }
        return num;
    }

    /**
     * ????????????????????????????????????
     * @param wmsInnerJobOrder
     * @return
     */
    private int dis(WmsInnerJobOrder wmsInnerJobOrder){
        int num = 0;
        Example example = new Example(WmsInnerJobOrderDet.class);
        example.createCriteria().andEqualTo("jobOrderId", wmsInnerJobOrder.getJobOrderId()).andEqualTo("orderStatus",1);
        List<WmsInnerJobOrderDet> list = wmsInPutawayOrderDetMapper.selectByExample(example);
        int success=0;
        WmsInnerJobOrder wms = wmsInPutawayOrderMapper.selectByPrimaryKey(wmsInnerJobOrder.getJobOrderId());
        for (WmsInnerJobOrderDet det : list) {
            if (StringUtils.isEmpty(det)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //??????????????????
            ResponseEntity<Long> responseEntity = baseFeignApi.inRule(wmsInnerJobOrder.getBaseStorageRule());
            if (responseEntity.getCode() != 0) {
                throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
            }

            if (StringUtils.isNotEmpty(responseEntity.getData())) {
                Long storageId = responseEntity.getData();
                det.setInStorageId(storageId);
                det.setDistributionQty(det.getPlanQty());
                det.setModifiedTime(new Date());
                det.setOrderStatus((byte) 3);
                det.setWorkStartTime(new Date());
                det.setWorkEndTime(new Date());
                wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(det);
                //????????????

                WmsInnerJobOrderDto wmsInnerJobOrderDto = new WmsInnerJobOrderDto();
                BeanUtil.copyProperties(wms,wmsInnerJobOrderDto);
                WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto = new WmsInnerJobOrderDetDto();
                BeanUtil.copyProperties(det,wmsInnerJobOrderDetDto);
                num += this.updateInventory(wmsInnerJobOrderDto, wmsInnerJobOrderDetDto);
                success++;
            }
        }
        if(success==0){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"????????????????????????");
        }
        if(success==list.size()){
            //?????????
            wms.setOrderStatus((byte)3);
            wms.setWorkStartTime(new Date());
            wms.setWorkEndtTime(new Date());
        }else {
            wms.setOrderStatus((byte)2);
            wms.setWorkStartTime(new Date());
        }
        SearchBaseWorker searchBaseWorker = new SearchBaseWorker();
        searchBaseWorker.setWarehouseId(wmsInnerJobOrder.getWarehouseId());
        searchBaseWorker.setUserId(wmsInnerJobOrder.getCreateUserId());
        List<BaseWorkerDto> workerDtos = baseFeignApi.findList(searchBaseWorker).getData();
        if (!workerDtos.isEmpty()) {
            wms.setWorkerId(workerDtos.get(0).getWorkerId());
        }
        wms.setModifiedTime(new Date());
        wms.setModifiedUserId(wmsInnerJobOrder.getCreateUserId());
        num += wmsInPutawayOrderMapper.updateByPrimaryKeySelective(wms);
        logger.info("success:"+success+"              list:"+list.size());
        logger.info(wmsInnerJobOrder.toString());
        return num;
    }
    /**
     * ????????????
     *
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int handDistribution(List<WmsInnerJobOrderDet> list) {
        SysUser sysUser = currentUser();
        int num = 0;
        for (WmsInnerJobOrderDet wmsInPutawayOrderDet : list) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(wmsInPutawayOrderDet.getJobOrderId());
            if (!StringUtils.isEmpty(wmsInPutawayOrderDet.getDistributionQty()) && wmsInPutawayOrderDet.getDistributionQty().doubleValue() > wmsInPutawayOrderDet.getPlanQty().doubleValue()) {
                throw new BizErrorException("????????????????????????????????????");
            }
            Long id = null;
            BigDecimal distributionQty = BigDecimal.ZERO;
            WmsInnerJobOrderDet wms = new WmsInnerJobOrderDet();
            if(wmsInnerJobOrder.getJobOrderType()==2){
                if(wmsInPutawayOrderDet.getInStorageId().equals(wmsInPutawayOrderDet.getOutStorageId())){
                    throw new BizErrorException("???????????????????????????????????????");
                }
            }
            //?????????????????????????????????????????????????????????????????????????????????
            if (StringUtils.isEmpty(wmsInPutawayOrderDet.getDistributionQty()) || wmsInPutawayOrderDet.getDistributionQty().compareTo(wmsInPutawayOrderDet.getPlanQty()) == -1) {
                //?????????
                wms = new WmsInnerJobOrderDet();
                BeanUtil.copyProperties(wmsInPutawayOrderDet, wms);
                wms.setJobOrderDetId(null);
                wms.setPlanQty(wmsInPutawayOrderDet.getDistributionQty());
                wms.setDistributionQty(wmsInPutawayOrderDet.getDistributionQty());
                wms.setOrderStatus((byte) 3);
                num += wmsInPutawayOrderDetMapper.insertUseGeneratedKeys(wms);
                id = wms.getJobOrderDetId();

                wmsInPutawayOrderDet.setOrderStatus((byte) 1);
                distributionQty = wmsInPutawayOrderDet.getDistributionQty();
                wmsInPutawayOrderDet.setDistributionQty(null);
                wmsInPutawayOrderDet.setInStorageId(null);
                wmsInPutawayOrderDet.setPlanQty(wmsInPutawayOrderDet.getPlanQty().subtract(wms.getDistributionQty()));
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                num += wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
            } else if (wmsInPutawayOrderDet.getDistributionQty().compareTo(wmsInPutawayOrderDet.getPlanQty()) == 0) {
                //????????????
                wmsInPutawayOrderDet.setOrderStatus((byte) 3);
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                num += wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
                id = wmsInPutawayOrderDet.getJobOrderDetId();
                wms = wmsInPutawayOrderDet;
            }
            SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
            searchWmsInnerJobOrderDet.setJobOrderId(wmsInPutawayOrderDet.getJobOrderId());
            List<WmsInnerJobOrderDetDto> dto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet);

            SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
            searchWmsInnerJobOrder.setJobOrderId(wmsInPutawayOrderDet.getJobOrderId());
            WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInPutawayOrderMapper.findList(searchWmsInnerJobOrder).get(0);
            searchWmsInnerJobOrderDet.setJobOrderDetId(wmsInPutawayOrderDet.getJobOrderDetId());
            WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);
            //????????????
            if (wmsInnerJobOrderDto.getJobOrderType() != (byte) 2) {
                if(wmsInnerJobOrderDto.getOrderTypeId()==9L){
                    num+=this.takeDistribution(wmsInnerJobOrderDto,wms);
                }else {
                    num += this.updateInventory(wmsInnerJobOrderDto, wms);
                }
            } else {
                //???
                Example example = new Example(WmsInnerInventory.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("materialId", wmsInnerJobOrderDetDto.getMaterialId())
                        .andEqualTo("warehouseId", wmsInnerJobOrderDetDto.getWarehouseId())
                        .andEqualTo("storageId", wmsInnerJobOrderDetDto.getOutStorageId())
                        .andEqualTo("jobOrderDetId", wmsInPutawayOrderDet.getJobOrderDetId())
                        .andEqualTo("relevanceOrderCode", wmsInnerJobOrder.getJobOrderCode())
                        .andEqualTo("jobStatus", (byte) 2)
                        .andEqualTo("stockLock", 0)
                        .andEqualTo("qcLock", 0)
                        .andEqualTo("lockStatus", 0)
                        .andEqualTo("orgId",sysUser.getOrganizationId());
                WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
                if (StringUtils.isEmpty(wmsInnerInventory)) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                }
                if (StringUtils.isEmpty(wmsInnerInventory.getPackingQty()) || wmsInnerInventory.getPackingQty().compareTo(BigDecimal.ZERO) == -1) {
                    throw new BizErrorException("????????????,???????????????");
                }
                WmsInnerInventory wmsIn = new WmsInnerInventory();
                wmsIn.setInventoryId(wmsInnerInventory.getInventoryId());
                wmsIn.setPackingQty(wmsInnerInventory.getPackingQty().subtract(distributionQty));
                num += wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsIn);

                example = new Example(WmsInnerInventory.class);
                Example.Criteria criteria1 = example.createCriteria();
                criteria1.andEqualTo("materialId", wmsInnerJobOrderDetDto.getMaterialId())
                        .andEqualTo("warehouseId", wmsInnerJobOrderDto.getWarehouseId())
                        .andEqualTo("storageId", wmsInnerJobOrderDetDto.getOutStorageId())
                        .andEqualTo("relevanceOrderCode", wmsInnerJobOrder.getJobOrderCode())
                        .andEqualTo("jobOrderDetId", id)
                        .andEqualTo("jobStatus", (byte) 2)
                        .andEqualTo("stockLock", 0)
                        .andEqualTo("qcLock", 0)
                        .andEqualTo("lockStatus", 0)
                        .andEqualTo("orgId",sysUser.getOrganizationId());
                WmsInnerInventory wmsInnerInventorys = wmsInnerInventoryMapper.selectOneByExample(example);
                if (StringUtils.isEmpty(wmsInnerInventorys)) {
                    //????????????
                    WmsInnerInventory inv = new WmsInnerInventory();
                    BeanUtil.copyProperties(wmsInnerInventory, inv);
                    inv.setStorageId(wmsInnerJobOrderDetDto.getOutStorageId());
                    inv.setWarehouseId(wmsInnerJobOrderDetDto.getWarehouseId());
                    inv.setRelevanceOrderCode(wmsInnerJobOrderDto.getJobOrderCode());
                    inv.setPackingQty(distributionQty);
                    inv.setJobStatus((byte) 2);
                    inv.setInventoryId(null);
                    inv.setBatchCode(wmsInnerJobOrderDetDto.getBatchCode());
                    inv.setJobOrderDetId(id);
                    inv.setOrgId(sysUser.getOrganizationId());
                    inv.setCreateUserId(sysUser.getUserId());
                    inv.setCreateTime(new Date());
                    inv.setModifiedTime(new Date());
                    inv.setModifiedUserId(sysUser.getUserId());
                    num += wmsInnerInventoryMapper.insertSelective(inv);
                } else {
                    //?????????
                    wmsInnerInventorys.setPackingQty(wmsInPutawayOrderDet.getDistributionQty());
                    num += wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
                }
            }
            if (StringUtils.isEmpty(dto)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //??????????????????????????????????????????????????????????????????
            List<WmsInnerJobOrderDetDto> orderDetDtos = dto.stream().filter(li -> li.getOrderStatus() != null && li.getOrderStatus() == (byte) 3).collect(Collectors.toList());
            if (!orderDetDtos.isEmpty() && orderDetDtos.size() == dto.size()) {
                //??????????????????
                //????????????????????????????????? ??????????????????
                Byte status = 3;
//                if (wmsInnerJobOrder.getOrderTypeId() != null && wmsInnerJobOrder.getOrderTypeId() == 4) {
//                    status = 6;
//                }
                SearchBaseWorker searchBaseWorker = new SearchBaseWorker();
                searchBaseWorker.setWarehouseId(wmsInnerJobOrder.getWarehouseId());
                searchBaseWorker.setUserId(sysUser.getUserId());
                List<BaseWorkerDto> workerDtos = baseFeignApi.findList(searchBaseWorker).getData();
                wmsInPutawayOrderMapper.updateByPrimaryKeySelective(WmsInnerJobOrder.builder()
                        .jobOrderId(wmsInPutawayOrderDet.getJobOrderId())
                        //?????????
                        .orderStatus(status)
                        .workerId(!workerDtos.isEmpty()?workerDtos.get(0).getWorkerId():null)
                        .build());
            } else {
                wmsInPutawayOrderMapper.updateByPrimaryKeySelective(WmsInnerJobOrder.builder()
                        .jobOrderId(wmsInPutawayOrderDet.getJobOrderId())
                        .orderStatus((byte) 2)
                        .workerId(sysUser.getUserId())
                        .build());
            }
        }
        return num;
    }

    /**
     * ????????????
     *
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
            if (StringUtils.isEmpty(wmsInnerJobOrder)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            if (wmsInnerJobOrder.getOrderStatus() == (byte) 1) {
                throw new BizErrorException("???????????????????????????");
            }
            if (wmsInnerJobOrder.getOrderStatus() == (byte) 4 || wmsInnerJobOrder.getOrderStatus() == (byte) 5) {
                throw new BizErrorException("??????????????????????????????");
            }
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId", s);
            List<WmsInnerJobOrderDet> list = wmsInPutawayOrderDetMapper.selectByExample(example);

            //????????????????????????
            Map<Long, List<WmsInnerJobOrderDet>> map = new HashMap<>();
            for (WmsInnerJobOrderDet wmsInnerJobOrderDet : list) {
                if (wmsInnerJobOrderDet.getOrderStatus() == (byte) 4) {
                    throw new BizErrorException("??????????????? ????????????");
                }
                if (map.containsKey(wmsInnerJobOrderDet.getMaterialId())) {
                    List<WmsInnerJobOrderDet> nm = new ArrayList<>();
                    for (WmsInnerJobOrderDet innerJobOrderDet : map.get(wmsInnerJobOrderDet.getMaterialId())) {
                        innerJobOrderDet.setPlanQty(innerJobOrderDet.getPlanQty().add(wmsInnerJobOrderDet.getPlanQty()));
                        nm.add(innerJobOrderDet);
                    }
                    map.put(wmsInnerJobOrderDet.getMaterialId(), nm);
                } else {
                    List<WmsInnerJobOrderDet> list1 = new ArrayList<>();
                    list1.add(wmsInnerJobOrderDet);
                    map.put(wmsInnerJobOrderDet.getMaterialId(), list1);
                }
                if (wmsInnerJobOrder.getJobOrderType() != (byte) 2) {

                    Example example1 = new Example(WmsInnerInventory.class);
                    example1.createCriteria().andEqualTo("jobOrderDetId", wmsInnerJobOrderDet.getJobOrderDetId()).andEqualTo("jobStatus", (byte) 2).andEqualTo("relevanceOrderCode", wmsInnerJobOrder.getJobOrderCode());
                    List<WmsInnerInventory> wmsInnerInventory = wmsInnerInventoryMapper.selectByExample(example1);

                    //????????????????????????
                    //InventoryLogUtil.addLog(wmsInnerInventory.get(0),wmsInnerJobOrder,wmsInnerJobOrderDet,wmsInnerJobOrderDet.getDistributionQty(),wmsInnerJobOrderDet.getDistributionQty(),(byte)2,(byte)2);
                    //????????????
                    if(wmsInnerJobOrder.getOrderTypeId()==9L){
                        this.takeCancel(wmsInnerJobOrder,wmsInnerJobOrderDet);
                    }else {
                        int res = this.cancel(wmsInnerJobOrder,wmsInnerJobOrderDet);
                        if (res < 1) {
                            throw new BizErrorException("??????????????????");
                        }
                    }

                    //??????????????????
//                    Example example1 = new Example(WmsInnerInventory.class);
//                    example1.createCriteria().andEqualTo("jobOrderDetId", wmsInnerJobOrderDet.getJobOrderDetId()).andEqualTo("jobStatus", (byte) 2).andEqualTo("relevanceOrderCode", wmsInnerJobOrder.getJobOrderCode());
                    wmsInnerInventoryMapper.deleteByExample(example1);
                }else {
                    Example example1 = new Example(WmsInnerInventory.class);
                    Example.Criteria criteria = example1.createCriteria();
                    criteria.andEqualTo("materialId", wmsInnerJobOrderDet.getMaterialId())
                            .andEqualTo("warehouseId", wmsInnerJobOrderDet.getWarehouseId())
                            .andEqualTo("storageId", wmsInnerJobOrderDet.getOutStorageId())
                            .andEqualTo("relevanceOrderCode", wmsInnerJobOrder.getJobOrderCode())
                            .andEqualTo("jobStatus", (byte) 2)
                            .andEqualTo("stockLock", 0)
                            .andEqualTo("qcLock", 0)
                            .andEqualTo("lockStatus", 0)
                            .andEqualTo("orgId",sysUser.getOrganizationId());
                    List<WmsInnerInventory> inventories = wmsInnerInventoryMapper.selectByExample(example1);
                    if(inventories.isEmpty()){
                        throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                    }
                    //??????????????????
                    wmsInnerInventoryMapper.deleteByExample(example1);

                    WmsInnerInventory wmsIn = inventories.get(0);
                    BigDecimal packingQty = inventories.stream().map(WmsInnerInventory::getPackingQty).reduce(BigDecimal::add).get();
                    wmsIn.setPackingQty(packingQty);
                    wmsInnerInventoryMapper.insert(wmsIn);
                }
            }
            //????????????????????????
            wmsInPutawayOrderDetMapper.deleteByExample(example);
            for (List<WmsInnerJobOrderDet> value : map.values()) {
                for (WmsInnerJobOrderDet wmsInnerJobOrderDet : value) {
                    wmsInnerJobOrderDet.setDistributionQty(null);
                    wmsInnerJobOrderDet.setInStorageId(null);
                    wmsInnerJobOrderDet.setOrderStatus((byte) 1);
                    wmsInnerJobOrderDet.setModifiedTime(new Date());
                    wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
                    wmsInnerJobOrderDet.setOrderStatus((byte) 1);
                    num += wmsInPutawayOrderDetMapper.insertSelective(wmsInnerJobOrderDet);
                }
            }
            wmsInnerJobOrder.setOrderStatus((byte) 1);
            num += wmsInPutawayOrderMapper.updateByPrimaryKeySelective(wmsInnerJobOrder);
        }
        return num;
    }

    private int cancel(WmsInnerJobOrder wmsInnerJobOrder,WmsInnerJobOrderDet wmsInnerJobOrderDet) {
//            WmsInAsnOrderDetDto wmsInAsnOrderDetDto = inFeignApi.findDetList(SearchWmsInAsnOrderDet.builder()
//                    .asnOrderDetId(wmsInnerJobOrderDet.getSourceDetId())
//                    .build()).getData().get(0);
//
//            WmsInAsnOrderDto wmsInAsnOrderDto = inFeignApi.findList(SearchWmsInAsnOrder.builder()
//                    .asnOrderId(wmsInAsnOrderDetDto.getAsnOrderId())
//                    .build()).getData().get(0);
            //???
            Example example = new Example(WmsInnerInventory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("relevanceOrderCode", wmsInnerJobOrder.getRelatedOrderCode()).andEqualTo("materialId", wmsInnerJobOrderDet.getMaterialId()).andEqualTo("warehouseId", wmsInnerJobOrderDet.getWarehouseId()).andEqualTo("storageId", wmsInnerJobOrderDet.getOutStorageId()).andEqualTo("orgId", wmsInnerJobOrderDet.getOrgId());
            if (!StringUtils.isEmpty(wmsInnerJobOrderDet.getBatchCode())) {
                criteria.andEqualTo("batchCode", wmsInnerJobOrderDet.getBatchCode());
            }
            WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
            if (StringUtils.isEmpty(wmsInnerInventory)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            WmsInnerInventory wmsIn = new WmsInnerInventory();
            wmsIn.setInventoryId(wmsInnerInventory.getInventoryId());
            wmsIn.setPackingQty(wmsInnerInventory.getPackingQty().add(wmsInnerJobOrderDet.getDistributionQty()));
        //????????????????????????
        //InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrder,wmsInnerJobOrderDet,wmsInnerInventory.getPackingQty(),wmsInnerJobOrderDet.getDistributionQty(),(byte)2,(byte)1);
            int num = wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsIn);
        return num;
    }

    /**
     * ????????????????????????
     * @param wmsInnerJobOrderDet
     * @return
     */
    private int takeCancel(WmsInnerJobOrder wmsInnerJobOrder,WmsInnerJobOrderDet wmsInnerJobOrderDet){
        //????????????????????????
        Example example = new Example(WmsInnerInventory.class);
        example.createCriteria().andEqualTo("relevanceOrderCode", wmsInnerJobOrderDet.getOption1())
                .andEqualTo("materialId", wmsInnerJobOrderDet.getMaterialId())
                .andEqualTo("warehouseId", wmsInnerJobOrderDet.getWarehouseId())
                .andEqualTo("storageId", wmsInnerJobOrderDet.getOutStorageId())
                .andEqualTo("inventoryStatusId", wmsInnerJobOrderDet.getInventoryStatusId())
                .andEqualTo("orgId",wmsInnerJobOrderDet.getOrgId());
        WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
        if (StringUtils.isEmpty(wmsInnerInventory)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        WmsInnerInventory wmsIn = new WmsInnerInventory();
        wmsIn.setInventoryId(wmsInnerInventory.getInventoryId());
        wmsIn.setPackingQty(wmsInnerInventory.getPackingQty().add(wmsInnerJobOrderDet.getDistributionQty()));
        int num = wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsIn);
        //????????????????????????
        InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrder,wmsInnerJobOrderDet,wmsInnerInventory.getPackingQty(),wmsInnerJobOrderDet.getDistributionQty(),(byte)2,(byte)1);
        return num;
    }

    /**
     * ????????????
     * @param wmsInnerJobOrderDet
     * @return
     */
    private int takeDistribution(WmsInnerJobOrder wmsInnerJobOrder,WmsInnerJobOrderDet wmsInnerJobOrderDet){
        //??????????????????
        Example example = new Example(WmsInnerInventory.class);
        example.createCriteria().andEqualTo("relevanceOrderCode", wmsInnerJobOrderDet.getOption1())
                .andEqualTo("materialId", wmsInnerJobOrderDet.getMaterialId())
                .andEqualTo("warehouseId", wmsInnerJobOrderDet.getWarehouseId())
                .andEqualTo("storageId", wmsInnerJobOrderDet.getOutStorageId())
                .andEqualTo("inventoryStatusId", wmsInnerJobOrderDet.getInventoryStatusId())
                .andEqualTo("orgId",wmsInnerJobOrderDet.getOrgId());
        WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(wmsInnerInventory)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        if (StringUtils.isEmpty(wmsInnerInventory.getPackingQty()) || wmsInnerInventory.getPackingQty().compareTo(BigDecimal.ZERO) == -1) {
            throw new BizErrorException("????????????,???????????????");
        }
        WmsInnerInventory wmsIn = new WmsInnerInventory();
        wmsIn.setInventoryId(wmsInnerInventory.getInventoryId());
        wmsIn.setPackingQty(wmsInnerInventory.getPackingQty().subtract(wmsInnerJobOrderDet.getDistributionQty()));
        wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsIn);

        //????????????????????????
        example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("relevanceOrderCode", wmsInnerJobOrder.getJobOrderCode())
                .andEqualTo("materialId", wmsInnerJobOrderDet.getMaterialId())
                .andEqualTo("warehouseId", wmsInnerJobOrderDet.getWarehouseId())
                .andEqualTo("storageId", wmsInnerJobOrderDet.getOutStorageId())
                .andEqualTo("inventoryStatusId", wmsInnerJobOrderDet.getInventoryStatusId())
                .andEqualTo("jobOrderDetId", wmsInnerJobOrderDet.getJobOrderDetId())
                .andEqualTo("jobStatus", (byte) 2)
                .andEqualTo("orgId",wmsInnerJobOrderDet.getOrgId());
        WmsInnerInventory wmsInnerInventorys = wmsInnerInventoryMapper.selectOneByExample(example);
        int num=0;
        if (StringUtils.isEmpty(wmsInnerInventorys)) {
            //????????????
            WmsInnerInventory inv = new WmsInnerInventory();
            BeanUtil.copyProperties(wmsInnerInventory, inv);
            inv.setStorageId(wmsInnerJobOrderDet.getOutStorageId());
            inv.setWarehouseId(wmsInnerJobOrderDet.getWarehouseId());
            inv.setRelevanceOrderCode(wmsInnerJobOrder.getJobOrderCode());
            inv.setPackingQty(wmsInnerJobOrderDet.getDistributionQty());
            inv.setJobStatus((byte) 2);
            inv.setInventoryId(null);
            inv.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
            inv.setOrgId(wmsInnerJobOrderDet.getOrgId());
            inv.setCreateUserId(wmsInnerJobOrderDet.getCreateUserId());
            inv.setCreateTime(new Date());
            inv.setModifiedTime(new Date());
            inv.setModifiedUserId(wmsInnerJobOrderDet.getCreateUserId());
            num = wmsInnerInventoryMapper.insertSelective(inv);
        } else {
            //?????????
            wmsInnerInventorys.setPackingQty(wmsInnerInventorys.getPackingQty().add(wmsInnerJobOrderDet.getDistributionQty()));
            num = wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int allReceiving(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        int num = 0;
        for (String id : arrId) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(id);
            if (wmsInnerJobOrder.getOrderStatus() == 6) {
                wmsInnerJobOrder.setOrderStatus((byte) 3);
            }
            if (wmsInnerJobOrder.getOrderStatus() < (byte) 3) {
                throw new BizErrorException("???????????????,??????????????????");
            }
            double total = 0.00;
            if (wmsInnerJobOrder.getOrderStatus() == (byte) 5) {
                throw new BizErrorException("?????????????????????");
            }
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId", wmsInnerJobOrder.getJobOrderId());
            List<WmsInnerJobOrderDet> wmsInnerJobOrderDets = wmsInPutawayOrderDetMapper.selectByExample(example);

            for (WmsInnerJobOrderDet wmsInnerJobOrderDet : wmsInnerJobOrderDets) {
                if (wmsInnerJobOrderDet.getOrderStatus() == (byte) 3) {
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
                                    .workStartTime(new Date())
                                    .workEndTime(new Date())
                            .modifiedUserId(sysUser.getUserId())
                            .modifiedTime(new Date())
                            .build());

                    //???????????????????????????
                    SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
                    searchWmsInnerJobOrder.setJobOrderId(wmsInnerJobOrderDet.getJobOrderId());
                    WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInPutawayOrderMapper.findList(searchWmsInnerJobOrder).get(0);
                    WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);

                    //????????????????????????
                    if (wmsInnerJobOrder.getJobOrderType() == (byte) 3) {
                        num = this.Inventory(wmsInnerJobOrderDto,oldDto, wmsInnerJobOrderDetDto);
                    }

                    if (wmsInnerJobOrder.getJobOrderType() == (byte) 2) {
                        // ????????????
                        Example example1 = new Example(WmsInnerInventory.class);
                        Example.Criteria criteria = example1.createCriteria();
                        criteria.andEqualTo("materialId", oldDto.getMaterialId())
                                .andEqualTo("warehouseId", oldDto.getWarehouseId())
                                .andEqualTo("storageId", oldDto.getOutStorageId())
                                .andEqualTo("jobOrderDetId", oldDto.getJobOrderDetId())
                                .andEqualTo("jobStatus", (byte) 2)
                                .andEqualTo("stockLock", 0)
                                .andEqualTo("qcLock", 0)
                                .andEqualTo("lockStatus", 0)
                                .andEqualTo("orgId",sysUser.getOrganizationId());
                        WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example1);
                        example1.clear();
                        Example.Criteria criteria1 = example1.createCriteria().andEqualTo("materialId", oldDto.getMaterialId())
                                .andEqualTo("warehouseId", oldDto.getWarehouseId())
                                .andEqualTo("storageId", oldDto.getInStorageId())
                                .andEqualTo("jobStatus", (byte) 1)
                                .andEqualTo("stockLock", 0)
                                .andEqualTo("qcLock", 0)
                                .andEqualTo("lockStatus", 0)
                                .andEqualTo("orgId",sysUser.getOrganizationId());
                        if (StringUtils.isNotEmpty(wmsInnerInventory)){
                            criteria1.andEqualTo("inventoryStatusId", wmsInnerInventory.getInventoryStatusId());
                        }

                        //??????????????????
                        WmsInnerInventory innerInventory = wmsInnerInventoryMapper.selectByPrimaryKey(wmsInnerInventory.getParentInventoryId());
                        if(StringUtils.isEmpty(innerInventory.getPackingQty())){
                            innerInventory.setPackingQty(BigDecimal.ZERO);
                        }

                        WmsInnerInventory wmsInnerInventory_old = wmsInnerInventoryMapper.selectOneByExample(example1);
                        BigDecimal packageQty = BigDecimal.ZERO;
                        if (StringUtils.isEmpty(wmsInnerInventory_old)) {
                            if (StringUtils.isEmpty(wmsInnerInventory)) {
                                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                            }
                            wmsInnerInventory.setJobStatus((byte) 1);
                            wmsInnerInventory.setStorageId(oldDto.getInStorageId());
                            wmsInnerInventoryService.updateByPrimaryKeySelective(wmsInnerInventory);
                        } else {
                            packageQty = wmsInnerInventory_old.getPackingQty();
                            wmsInnerInventory_old.setPackingQty(wmsInnerInventory_old.getPackingQty() != null ? wmsInnerInventory_old.getPackingQty().add(wmsInnerInventory.getPackingQty()) : wmsInnerInventory.getPackingQty());
                            wmsInnerInventory_old.setRelevanceOrderCode(wmsInnerInventory.getRelevanceOrderCode());
                            wmsInnerInventoryService.updateByPrimaryKeySelective(wmsInnerInventory_old);
                            wmsInnerInventoryService.delete(wmsInnerInventory);
                        }
                        InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrder,oldDto,innerInventory.getPackingQty().add(wmsInnerInventory.getPackingQty()),wmsInnerInventory.getPackingQty(),(byte)3,(byte)2);
                        InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrder,oldDto,packageQty,wmsInnerInventory.getPackingQty(),(byte)3,(byte)1);
                        //??????????????????
                        Example example2 = new Example(WmsInnerJobOrderDetBarcode.class);
                        example2.createCriteria().andEqualTo("jobOrderDetId", oldDto.getJobOrderDetId());
                        List<WmsInnerJobOrderDetBarcode> orderDetBarcodeList = wmsInnerJobOrderDetBarcodeService.selectByExample(example2);
                        if (!orderDetBarcodeList.isEmpty()) {
                            for (WmsInnerJobOrderDetBarcode jobOrderDetBarcode : orderDetBarcodeList) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("storageId", oldDto.getOutStorageId());
                                map.put("barcode", jobOrderDetBarcode.getBarcode());
                                List<WmsInnerInventoryDetDto> inventoryDetDtos = wmsInnerInventoryDetMapper.findList(map);
                                if (inventoryDetDtos.isEmpty()) {
                                    throw new BizErrorException(ErrorCodeEnum.PDA5001004);
                                }
                                WmsInnerInventoryDetDto inventoryDetDto = inventoryDetDtos.get(0);
                                inventoryDetDto.setStorageId(oldDto.getInStorageId());
                                wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(inventoryDetDto);
                            }
                        }
                    } else {
                        if(wmsInnerJobOrder.getJobOrderType()!=2){
                            //?????????????????????
                            ResponseEntity responseEntity = inFeignApi.writeQty(WmsInAsnOrderDet.builder()
                                    .putawayQty(wmsInnerJobOrderDet.getDistributionQty())
                                    .asnOrderDetId(wmsInnerJobOrderDet.getSourceDetId())
                                    .build());
                            if (responseEntity.getCode() != 0) {
                                throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
                            }
                        }
                    }
                }
            }
            BigDecimal resultQty = wmsInnerJobOrderDets.stream()
                    .map(WmsInnerJobOrderDet::getDistributionQty)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            WmsInnerJobOrder innerJobOrder = WmsInnerJobOrder.builder()
                    .orderStatus((byte) 5)
                    .jobOrderId(wmsInnerJobOrder.getJobOrderId())
                    .actualQty(resultQty)
                    .workStartTime(new Date())
                    .workEndtTime(new Date())
                    .build();
            SearchBaseWorker searchBaseWorker = new SearchBaseWorker();
            searchBaseWorker.setWarehouseId(wmsInnerJobOrder.getWarehouseId());
            searchBaseWorker.setUserId(sysUser.getUserId());
            List<BaseWorkerDto> workerDtos = baseFeignApi.findList(searchBaseWorker).getData();
            if (!workerDtos.isEmpty()) {
                innerJobOrder.setWorkerId(workerDtos.get(0).getWorkerId());
            }

            //?????????????????????????????????
            wmsInPutawayOrderMapper.updateByPrimaryKeySelective(innerJobOrder);

            // 2022-03-09 ???????????? - ???????????????????????????
            Example example1 = new Example(WmsInnerJobOrderReMspp.class);
            example1.createCriteria().andEqualTo("jobOrderId", wmsInnerJobOrder.getJobOrderId());
            List<WmsInnerJobOrderReMspp> jobOrderReMspps = wmsInnerJobOrderReMsppMapper.selectByExample(example1);
            if (!jobOrderReMspps.isEmpty()){
                WanbaoStacking stacking = wanbaoFeignApi.detail(jobOrderReMspps.get(0).getProductPalletId()).getData();
                stacking.setUsageStatus((byte) 1);
                wanbaoFeignApi.updateAndClearBarcode(stacking);
            }
        }

        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int singleReceiving(List<WmsInnerJobOrderDet> wmsInPutawayOrderDets) {
        SysUser sysUser = currentUser();
        int num = 0;
        for (WmsInnerJobOrderDet wmsInPutawayOrderDet : wmsInPutawayOrderDets) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(wmsInPutawayOrderDet.getJobOrderId());
            if (wmsInnerJobOrder.getOrderStatus() == 6) {
                wmsInnerJobOrder.setOrderStatus((byte) 3);
            }
            if (wmsInnerJobOrder.getOrderStatus() == (byte) 5) {
                throw new BizErrorException("?????????????????????");
            }
            BigDecimal aqty = wmsInPutawayOrderDet.getActualQty();

            SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
            searchWmsInnerJobOrderDet.setJobOrderDetId(wmsInPutawayOrderDet.getJobOrderDetId());
            WmsInnerJobOrderDetDto oldDto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);
            BigDecimal actualQty = BigDecimal.ZERO;
            Long jobOrderDetId = null;
            if (wmsInPutawayOrderDet.getActualQty().compareTo(wmsInPutawayOrderDet.getDistributionQty()) == -1) {
                WmsInnerJobOrderDet wms = new WmsInnerJobOrderDet();
                BeanUtil.copyProperties(wmsInPutawayOrderDet, wms);
                wms.setJobOrderDetId(null);
                wms.setPlanQty(wmsInPutawayOrderDet.getActualQty());
                wms.setDistributionQty(wmsInPutawayOrderDet.getActualQty());
                wms.setOrderStatus((byte) 5);
                wms.setWorkStartTime(new Date());
                wms.setWorkEndTime(new Date());
                wms.setWorkEndTime(new Date());
                wms.setOption3("finish");
                num += wmsInPutawayOrderDetMapper.insertUseGeneratedKeys(wms);
                jobOrderDetId = wms.getJobOrderDetId();

                wmsInPutawayOrderDet.setOrderStatus((byte) 3);
                wmsInPutawayOrderDet.setInStorageId(null);
                wmsInPutawayOrderDet.setPlanQty(wmsInPutawayOrderDet.getPlanQty().subtract(wms.getPlanQty()));
                wmsInPutawayOrderDet.setDistributionQty(wmsInPutawayOrderDet.getDistributionQty().subtract(wms.getDistributionQty()));
                actualQty = wmsInPutawayOrderDet.getActualQty();
                wmsInPutawayOrderDet.setActualQty(null);
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                num += wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
            } else if (wmsInPutawayOrderDet.getDistributionQty().compareTo(wmsInPutawayOrderDet.getPlanQty()) == 0) {
                //????????????
                wmsInPutawayOrderDet.setOrderStatus((byte) 5);
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                wmsInPutawayOrderDet.setWorkStartTime(new Date());
                wmsInPutawayOrderDet.setWorkEndTime(new Date());
                wmsInPutawayOrderDet.setOption3("finish");
                num += wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
                jobOrderDetId = wmsInPutawayOrderDet.getJobOrderDetId();
            }

            //????????????
            SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
            searchWmsInnerJobOrder.setJobOrderId(wmsInPutawayOrderDet.getJobOrderId());
            WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInPutawayOrderMapper.findList(searchWmsInnerJobOrder).get(0);
            searchWmsInnerJobOrderDet.setJobOrderDetId(jobOrderDetId);
            List<WmsInnerJobOrderDetDto> wmsInner = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet);
            WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto = wmsInner.get(0);

            if (wmsInnerJobOrder.getJobOrderType() == (byte) 2) {
                // ????????????
                Example example = new Example(WmsInnerInventory.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("materialId", oldDto.getMaterialId())
                        .andEqualTo("warehouseId", oldDto.getWarehouseId())
                        .andEqualTo("storageId", oldDto.getOutStorageId())
                        .andEqualTo("jobOrderDetId", oldDto.getJobOrderDetId())
                        .andEqualTo("relevanceOrderCode", wmsInnerJobOrder.getJobOrderCode())
                        .andEqualTo("jobStatus", (byte) 2)
                        .andEqualTo("stockLock", 0)
                        .andEqualTo("qcLock", 0)
                        .andEqualTo("lockStatus", 0)
                        .andGreaterThan("packingQty", 0)
                        .andEqualTo("orgId",sysUser.getOrganizationId());
                WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
                example.clear();
                Example.Criteria criteria1 = example.createCriteria().andEqualTo("materialId", oldDto.getMaterialId())
                        .andEqualTo("warehouseId", oldDto.getWarehouseId())
                        .andEqualTo("storageId", oldDto.getInStorageId())
//                        .andEqualTo("relevanceOrderCode", wmsInnerJobOrder.getJobOrderCode())
                        .andEqualTo("jobStatus", (byte) 1)
                        .andEqualTo("stockLock", 0)
                        .andEqualTo("qcLock", 0)
                        .andEqualTo("lockStatus", 0)
                        .andEqualTo("orgId",sysUser.getOrganizationId());
                if (StringUtils.isNotEmpty(wmsInnerInventory)){
                    criteria1.andEqualTo("inventoryStatusId", wmsInnerInventory.getInventoryStatusId());
                }
                WmsInnerInventory wmsInnerInventory_old = wmsInnerInventoryMapper.selectOneByExample(example);

                //??????????????????
                WmsInnerInventory innerInventory = wmsInnerInventoryMapper.selectByPrimaryKey(wmsInnerInventory.getParentInventoryId());
                if(StringUtils.isEmpty(innerInventory.getPackingQty())){
                    innerInventory.setPackingQty(BigDecimal.ZERO);
                }
                if (StringUtils.isEmpty(wmsInnerInventory_old)) {
                    //????????????
                    BigDecimal packingQty = wmsInnerInventory.getPackingQty();
                    WmsInnerInventory inv = new WmsInnerInventory();
                    BeanUtil.copyProperties(wmsInnerInventory, inv);
                    inv.setStorageId(oldDto.getInStorageId());
                    inv.setWarehouseId(oldDto.getWarehouseId());
                    inv.setPackingQty(aqty);
                    inv.setJobStatus((byte) 1);
                    inv.setBatchCode(oldDto.getBatchCode());
                    inv.setJobOrderDetId(oldDto.getJobOrderDetId());
                    inv.setInventoryId(null);
                    inv.setCreateUserId(sysUser.getUserId());
                    inv.setCreateTime(new Date());
                    inv.setModifiedTime(new Date());
                    inv.setModifiedUserId(sysUser.getUserId());
                    inv.setOrgId(sysUser.getOrganizationId());
                    inv.setRelevanceOrderCode(wmsInnerJobOrder.getJobOrderCode());
                    inv.setJobOrderDetId(null);
                    wmsInnerInventoryMapper.insertSelective(inv);
                    wmsInnerInventory.setPackingQty(wmsInnerInventory.getPackingQty().subtract(aqty));
                    wmsInnerInventoryService.updateByPrimaryKeySelective(wmsInnerInventory);

                    BigDecimal initQty = innerInventory.getPackingQty().add(packingQty);
                    InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrder,oldDto,initQty,aqty,(byte)3,(byte)2);
                    InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrder,oldDto,BigDecimal.ZERO,aqty,(byte)3,(byte)1);
                } else {
                    BigDecimal packingQty = innerInventory.getPackingQty().add(wmsInnerInventory.getPackingQty());
                    BigDecimal initQty = wmsInnerInventory_old.getPackingQty();
                    wmsInnerInventory_old.setPackingQty(wmsInnerInventory_old.getPackingQty() != null ? wmsInnerInventory_old.getPackingQty().add(wmsInnerInventory.getPackingQty()) : wmsInnerInventory.getPackingQty());
                    wmsInnerInventory_old.setRelevanceOrderCode(wmsInnerInventory.getRelevanceOrderCode());
                    wmsInnerInventoryService.updateByPrimaryKeySelective(wmsInnerInventory_old);
                    wmsInnerInventory.setPackingQty(BigDecimal.ZERO);
                    wmsInnerInventoryService.updateByPrimaryKeySelective(wmsInnerInventory);

                    InventoryLogUtil.addLog(wmsInnerInventory_old,wmsInnerJobOrder,oldDto,packingQty,aqty,(byte)3,(byte)2);
                    InventoryLogUtil.addLog(wmsInnerInventory_old,wmsInnerJobOrder,oldDto,initQty,aqty,(byte)3,(byte)1);
                }
                //??????????????????
                Example example1 = new Example(WmsInnerJobOrderDetBarcode.class);
                example1.createCriteria().andEqualTo("jobOrderDetId", oldDto.getJobOrderDetId());
                List<WmsInnerJobOrderDetBarcode> orderDetBarcodeList = wmsInnerJobOrderDetBarcodeService.selectByExample(example1);
                if (!orderDetBarcodeList.isEmpty()) {
                    for (WmsInnerJobOrderDetBarcode jobOrderDetBarcode : orderDetBarcodeList) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("storageId", oldDto.getOutStorageId());
                        map.put("barcode", jobOrderDetBarcode.getBarcode());
                        List<WmsInnerInventoryDetDto> inventoryDetDtos = wmsInnerInventoryDetMapper.findList(map);
                        if (inventoryDetDtos.isEmpty()) {
                            throw new BizErrorException(ErrorCodeEnum.PDA5001004);
                        }
                        WmsInnerInventoryDetDto inventoryDetDto = inventoryDetDtos.get(0);
                        inventoryDetDto.setStorageId(oldDto.getInStorageId());
                        wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(inventoryDetDto);
                    }
                }
            } else {
                //????????????
                num = this.Inventory(wmsInnerJobOrderDto,oldDto, wmsInnerJobOrderDetDto);
            }

            WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
            wmsInnerJobOrderDet.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
            int count = wmsInPutawayOrderDetMapper.selectCount(wmsInnerJobOrderDet);
            wmsInnerJobOrderDet.setOrderStatus((byte) 5);
            int oCount = wmsInPutawayOrderDetMapper.selectCount(wmsInnerJobOrderDet);

            SearchBaseWorker searchBaseWorker = new SearchBaseWorker();
            searchBaseWorker.setWarehouseId(wmsInnerJobOrderDto.getWarehouseId());
            searchBaseWorker.setUserId(sysUser.getUserId());
            List<BaseWorkerDto> workerDtos = baseFeignApi.findList(searchBaseWorker).getData();

            if (oCount == count) {
                WmsInnerJobOrder ws = wmsInPutawayOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDto.getJobOrderId());
                ws.setOrderStatus((byte) 5);
                if (StringUtils.isEmpty(ws.getActualQty())) {
                    ws.setActualQty(new BigDecimal("0.00"));
                }
                if (!workerDtos.isEmpty()) {
                    ws.setWorkerId(workerDtos.get(0).getWorkerId());
                }
                ws.setActualQty(ws.getActualQty().add(aqty));
                ws.setModifiedUserId(sysUser.getUserId());
                ws.setModifiedTime(new Date());
                if (StringUtils.isEmpty(ws.getWorkStartTime())) {
                    ws.setWorkStartTime(new Date());
                }
                ws.setWorkEndtTime(new Date());
                num += wmsInPutawayOrderMapper.updateByPrimaryKeySelective(ws);
            } else {
                WmsInnerJobOrder ws = wmsInPutawayOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDto.getJobOrderId());
                ws.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
                if (StringUtils.isEmpty(ws.getActualQty())) {
                    ws.setActualQty(new BigDecimal("0.00"));
                }
                if (!workerDtos.isEmpty()) {
                    ws.setWorkerId(workerDtos.get(0).getWorkerId());
                }
                ws.setActualQty(ws.getActualQty().add(aqty));
                ws.setOrderStatus((byte) 4);
                ws.setModifiedUserId(sysUser.getUserId());
                ws.setModifiedTime(new Date());
                if (StringUtils.isEmpty(wmsInnerJobOrder.getWorkStartTime())) {
                    ws.setWorkStartTime(new Date());
                }
                num += wmsInPutawayOrderMapper.updateByPrimaryKeySelective(ws);
            }

            if (wmsInnerJobOrder.getJobOrderType() != (byte) 2) {
                //?????????????????????
                ResponseEntity responseEntity = inFeignApi.writeQty(WmsInAsnOrderDet.builder()
                        .putawayQty(wmsInnerJobOrderDetDto.getActualQty())
                        .asnOrderDetId(wmsInnerJobOrderDetDto.getSourceDetId())
                        .build());
                if (responseEntity.getCode() != 0) {
                    throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
                }
            }
        }
        // 2022-03-09 ???????????? - ???????????????????????????
        /*if (!wmsInPutawayOrderDets.isEmpty()){
            Example example = new Example(WmsInnerJobOrderReMspp.class);
            example.createCriteria().andEqualTo("jobOrderId", wmsInPutawayOrderDets.get(0).getJobOrderId());
            List<WmsInnerJobOrderReMspp> jobOrderReMspps = wmsInnerJobOrderReMsppMapper.selectByExample(example);
            if (!jobOrderReMspps.isEmpty()){
                WanbaoStacking stacking = wanbaoFeignApi.detail(jobOrderReMspps.get(0).getProductPalletId()).getData();
                stacking.setUsageStatus((byte) 1);
                wanbaoFeignApi.updateAndClearBarcode(stacking);
            }
        }*/

        return num;
    }

    /**
     * ????????????
     *
     * @param barCode
     * @return ????????????
     */
    @Override
    public Map<String, Object> checkBarcode(String barCode, Long jobOrderDetId) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        WmsInnerJobOrderDet wmsInnerJobOrderDet = wmsInPutawayOrderDetMapper.selectByPrimaryKey(jobOrderDetId);
        WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDet.getJobOrderId());
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(wmsInnerJobOrderDet)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        //?????????????????????????????????
        Long materialId = null;
        WmsInAsnOrderDet wms = null;
        if(wmsInnerJobOrder.getOrderTypeId()==9L){
            materialId = wmsInPutawayOrderDetMapper.findEngMaterial(wmsInnerJobOrderDet.getSourceDetId());
        }else {
             wms = inFeignApi.findDetList(SearchWmsInAsnOrderDet.builder()
                    .asnOrderDetId(wmsInnerJobOrderDet.getSourceDetId())
                    .build()).getData().get(0);
            materialId = wms.getMaterialId();
        }

        SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
        searchBaseMaterial.setMaterialId(materialId);
        List<BaseMaterial> baseMaterialList = baseFeignApi.findList(searchBaseMaterial).getData();
        String materialCode = null;
        if(StringUtils.isNotEmpty(baseMaterialList)){
            materialCode = baseMaterialList.get(0).getMaterialCode();
        }
        if (StringUtils.isNotEmpty(materialCode) && materialCode.equals(barCode)) {
            map.put("SN", "false");
            return map;
        } else if(StringUtils.isNotEmpty(wms)){
            BigDecimal qty = InBarcodeUtil.getInventoryDetQty(wms.getAsnOrderId(), wmsInnerJobOrderDet.getMaterialId(), barCode);
            map.put("SN", "true");
            map.put("qty", qty);
        }else{
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"??????????????????????????????");
        }
        return map;
    }

    /**
     * PDA??????????????????????????????
     *
     * @return
     */
    private int addInventoryDet(String relatedOrderCode, WmsInnerJobOrderDet wmsInnerJobOrderDet, String barcode) {
        //???????????????????????????
        Example example = new Example(WmsInnerInventoryDet.class);
        example.createCriteria().andEqualTo("asnCode", relatedOrderCode).andEqualTo("storageId", wmsInnerJobOrderDet.getOutStorageId()).andEqualTo("materialId", wmsInnerJobOrderDet.getMaterialId()).andEqualTo("barcode", barcode).andEqualTo("barcodeStatus",2).andEqualTo("orgId",wmsInnerJobOrderDet.getOrgId());
        WmsInnerInventoryDet wmsInnerInventoryDet = wmsInnerInventoryDetMapper.selectOneByExample(example);
        if (StringUtils.isEmpty(wmsInnerInventoryDet)) {
            throw new BizErrorException("????????????????????????");
        }
        //wmsInnerInventoryDet.setAsnCode(jobOrderCode);
        wmsInnerInventoryDet.setStorageId(wmsInnerJobOrderDet.getInStorageId());
        wmsInnerInventoryDet.setReceivingDate(new Date());
        wmsInnerInventoryDet.setBarcodeStatus((byte)3);
        wmsInnerInventoryDet.setLockStatus((byte)0);
        return wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(wmsInnerInventoryDet);
    }

    /**
     * PDA??????????????????????????????
     *
     * @param storageCode
     * @param jobOrderDetId
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public WmsInnerJobOrderDet scanStorageBackQty(String storageCode, Long jobOrderDetId, BigDecimal qty, String barcode) {
        SysUser sysUser = currentUser();
        if (StringUtils.isEmpty(qty)) {
            throw new BizErrorException("????????????????????????1");
        }
        //??????????????????????????????id
        ResponseEntity<List<BaseStorage>> list = baseFeignApi.findList(SearchBaseStorage.builder()
                .storageCode(storageCode)
                .codeQueryMark((byte) 1)
                .build());
        if (StringUtils.isEmpty(list.getData())) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "??????????????????");
        }
        BaseStorage baseStorage = list.getData().get(0);

        WmsInnerJobOrderDet wmsInnerJobOrderDet = wmsInPutawayOrderDetMapper.selectByPrimaryKey(jobOrderDetId);
        if (StringUtils.isEmpty(wmsInnerJobOrderDet.getActualQty())) {
            wmsInnerJobOrderDet.setActualQty(BigDecimal.ZERO);
        }
        if (wmsInnerJobOrderDet.getActualQty().add(qty).compareTo(wmsInnerJobOrderDet.getDistributionQty()) == 1) {
            throw new BizErrorException("????????????????????????????????????");
        }

        // ????????????????????????
        WmsInnerJobOrder innerJobOrder = this.selectByKey(wmsInnerJobOrderDet.getJobOrderId());
        if (StringUtils.isEmpty(innerJobOrder.getReleaseUserId())){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "????????????????????????????????????");
        }

        SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
        searchWmsInnerJobOrderDet.setJobOrderDetId(jobOrderDetId);
        WmsInnerJobOrderDetDto oldDto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);
        int num = 0;
        if (wmsInnerJobOrderDet.getActualQty().add(qty).compareTo(wmsInnerJobOrderDet.getDistributionQty()) == -1) {
            WmsInnerJobOrderDet wmss = new WmsInnerJobOrderDet();
            BeanUtil.copyProperties(wmsInnerJobOrderDet, wmss);
            wmss.setJobOrderDetId(null);
            wmss.setInStorageId(baseStorage.getStorageId());
            wmss.setActualQty(qty);
            wmss.setPlanQty(qty);
            wmss.setDistributionQty(qty);
            wmss.setOrderStatus((byte) 5);
            wmss.setWorkStartTime(new Date());
            wmss.setWorkEndTime(new Date());
            wmss.setOrgId(sysUser.getOrganizationId());
            wmss.setOption3("finish");
            num += wmsInPutawayOrderDetMapper.insertUseGeneratedKeys(wmss);
            jobOrderDetId = wmss.getJobOrderDetId();

            wmsInnerJobOrderDet.setOrderStatus((byte) 3);
            wmsInnerJobOrderDet.setInStorageId(null);
            wmsInnerJobOrderDet.setPlanQty(wmsInnerJobOrderDet.getPlanQty().subtract(wmss.getPlanQty()));
            wmsInnerJobOrderDet.setDistributionQty(wmsInnerJobOrderDet.getDistributionQty().subtract(wmss.getDistributionQty()));
            wmsInnerJobOrderDet.setActualQty(null);
            wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInnerJobOrderDet.setModifiedTime(new Date());
            wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wmsInnerJobOrderDet);
            wmsInnerJobOrderDet.setInStorageId(wmss.getInStorageId());
        } else if (wmsInnerJobOrderDet.getDistributionQty().compareTo(wmsInnerJobOrderDet.getPlanQty()) == 0) {
            //????????????
            wmsInnerJobOrderDet.setActualQty(qty);
            wmsInnerJobOrderDet.setInStorageId(baseStorage.getStorageId());
            wmsInnerJobOrderDet.setOrderStatus((byte) 5);
            wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInnerJobOrderDet.setModifiedTime(new Date());
            wmsInnerJobOrderDet.setWorkStartTime(new Date());
            wmsInnerJobOrderDet.setWorkEndTime(new Date());
            num += wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wmsInnerJobOrderDet);
        }
        if (num == 0) {
            throw new BizErrorException("????????????");
        }


        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setJobOrderId(wmsInnerJobOrderDet.getJobOrderId());
        WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInPutawayOrderMapper.findList(searchWmsInnerJobOrder).get(0);
        searchWmsInnerJobOrderDet.setJobOrderDetId(jobOrderDetId);
        List<WmsInnerJobOrderDetDto> wmsInnerJobOrderDetDto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet);
        //????????????
        num = this.Inventory(wmsInnerJobOrderDto,oldDto, wmsInnerJobOrderDetDto.get(0));
        //??????????????????
        if(wmsInnerJobOrderDto.getOrderTypeId()!=9L) {
            if (StringUtils.isEmpty(barcode)) {
                barcode = InBarcodeUtil.getWorkBarCodeList(jobOrderDetId);
            }
            String[] code = barcode.split(",");
            for (String s : code) {
                //??????????????????
                num += this.addInventoryDet(wmsInnerJobOrderDto.getRelatedOrderCode(), wmsInnerJobOrderDet, s);
            }
        }

        WmsInnerJobOrderDet wms = new WmsInnerJobOrderDet();
        wms.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
        int count = wmsInPutawayOrderDetMapper.selectCount(wms);
        wms.setOrderStatus((byte) 5);
        int oCount = wmsInPutawayOrderDetMapper.selectCount(wms);

        if (StringUtils.isEmpty(wmsInnerJobOrderDto.getActualQty())) {
            wmsInnerJobOrderDto.setActualQty(BigDecimal.ZERO);
        }
        SearchBaseWorker searchBaseWorker = new SearchBaseWorker();
        searchBaseWorker.setWarehouseId(wmsInnerJobOrderDto.getWarehouseId());
        searchBaseWorker.setUserId(sysUser.getUserId());
        List<BaseWorkerDto> workerDtos = baseFeignApi.findList(searchBaseWorker).getData();

        if (oCount == count) {
            WmsInnerJobOrder ws = new WmsInnerJobOrder();
            ws.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
            ws.setOrderStatus((byte) 5);
            ws.setActualQty(wmsInnerJobOrderDto.getActualQty().add(qty));
            ws.setModifiedUserId(sysUser.getUserId());
            ws.setModifiedTime(new Date());
            ws.setWorkEndtTime(new Date());
            if (!workerDtos.isEmpty()) {
                ws.setWorkerId(workerDtos.get(0).getWorkerId());
            }
            if (StringUtils.isEmpty(wmsInnerJobOrderDto.getWorkStartTime())) {
                ws.setWorkStartTime(new Date());
            }
            ws.setWorkEndtTime(new Date());
            num += wmsInPutawayOrderMapper.updateByPrimaryKeySelective(ws);

        } else {
            WmsInnerJobOrder ws = new WmsInnerJobOrder();
            ws.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
            ws.setOrderStatus((byte) 4);
            ws.setActualQty(wmsInnerJobOrderDto.getActualQty().add(qty));
            ws.setModifiedUserId(sysUser.getUserId());
            ws.setModifiedTime(new Date());
            if (!workerDtos.isEmpty()) {
                ws.setWorkerId(workerDtos.get(0).getWorkerId());
            }
            if (StringUtils.isEmpty(wmsInnerJobOrderDto.getWorkStartTime())) {
                ws.setWorkStartTime(new Date());
            }
            num += wmsInPutawayOrderMapper.updateByPrimaryKeySelective(ws);
        }
        //?????????????????????
        ResponseEntity responseEntity = inFeignApi.writeQty(WmsInAsnOrderDet.builder()
                .putawayQty(qty)
                .asnOrderDetId(oldDto.getSourceDetId())
                .build());
        if (responseEntity.getCode() != 0) {
            throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
        }

        // 2022-03-09 ???????????? - ???????????????????????????
//        Example example = new Example(WmsInnerJobOrderReMspp.class);
//        example.createCriteria().andEqualTo("jobOrderId", wmsInnerJobOrderDto.getJobOrderId());
//        List<WmsInnerJobOrderReMspp> jobOrderReMspps = wmsInnerJobOrderReMsppMapper.selectByExample(example);
//        if (!jobOrderReMspps.isEmpty()){
//            WanbaoStacking stacking = wanbaoFeignApi.detail(jobOrderReMspps.get(0).getProductPalletId()).getData();
//            stacking.setUsageStatus((byte) 1);
//            wanbaoFeignApi.updateAndClearBarcode(stacking);
//        }
        return wmsInnerJobOrderDet;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
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
                    .orgId(sysUser.getOrganizationId())
                    .build());

            WmsInAsnOrderDto wmsInAsnOrderDto = inFeignApi.findList(SearchWmsInAsnOrder.builder()
                    .asnOrderId(wmsInnerJobOrder.getSourceOrderId())
                    .build()).getData().get(0);
            Example example = new Example(WmsInnerInventory.class);
            example.createCriteria().andEqualTo("relevanceOrderCode", wmsInAsnOrderDto.getAsnCode()).andEqualTo("materialId", wmsInPutawayOrderDet.getMaterialId()).andEqualTo("batchCode", wmsInPutawayOrderDet.getBatchCode()).andEqualTo("orgId",sysUser.getOrganizationId());
            WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
            if (StringUtils.isEmpty(wmsInnerInventory)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            WmsInnerInventory wmsInnerInventorys = new WmsInnerInventory();
            wmsInnerInventorys.setInventoryId(wmsInnerInventory.getInventoryId());
            wmsInnerInventorys.setJobStatus((byte) 2);
            wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
        }
        return wmsInnerJobOrder;
    }

    /**
     * PDA??????????????????
     *
     * @param jobOrderId
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int activation(Long jobOrderId) {
        WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(jobOrderId);
        if (StringUtils.isEmpty(wmsInnerJobOrder)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        Example example = new Example(WmsInnerJobOrderReMspp.class);
        example.createCriteria().andEqualTo("jobOrderId", jobOrderId);
        WmsInnerJobOrderReMspp wmsInnerJobOrderReMspp = wmsInnerJobOrderReMsppMapper.selectOneByExample(example);
        if (StringUtils.isEmpty(wmsInnerJobOrderReMspp)) {
            throw new BizErrorException("???????????????????????????????????????");
        }
        //??????????????????
        ResponseEntity responseEntity = sfcFeignApi.updateMoveStatus(wmsInnerJobOrderReMspp.getProductPalletId());
        if (responseEntity.getCode() != 0) {
            throw new BizErrorException("????????????");
        }
        //?????????????????????
        wmsInnerJobOrder.setOrderStatus((byte) 3);
        int num = wmsInPutawayOrderMapper.updateByPrimaryKeySelective(wmsInnerJobOrder);
        return num;
    }

    /**
     * ?????????????????????????????????
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int addList(List<WmsInnerJobOrder> list){
        SysUser sysUser = currentUser();
        for (WmsInnerJobOrder wmsInnerJobOrder : list) {
            wmsInnerJobOrder.setJobOrderCode(CodeUtils.getId("PUT-"));
            wmsInnerJobOrder.setCreateTime(new Date());
            wmsInnerJobOrder.setCreateUserId(sysUser.getUserId());
            wmsInnerJobOrder.setModifiedTime(new Date());
            wmsInnerJobOrder.setModifiedUserId(sysUser.getUserId());
            wmsInnerJobOrder.setOrgId(sysUser.getOrganizationId());
            wmsInnerJobOrder.setIsDelete((byte) 1);
            wmsInPutawayOrderMapper.insertUseGeneratedKeys(wmsInnerJobOrder);
            for (WmsInnerJobOrderDet wmsInPutawayOrderDet : wmsInnerJobOrder.getWmsInPutawayOrderDets()) {
                wmsInPutawayOrderDet.setJobOrderId(wmsInnerJobOrder.getJobOrderId());
                wmsInPutawayOrderDet.setCreateTime(new Date());
                wmsInPutawayOrderDet.setCreateUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setOrgId(sysUser.getOrganizationId());
                wmsInPutawayOrderDetMapper.insertUseGeneratedKeys(wmsInPutawayOrderDet);

                //??????????????????
                Example example = new Example(WmsInnerInventory.class);
                example.createCriteria().andEqualTo("relevanceOrderCode", wmsInPutawayOrderDet.getOption1())
                        .andEqualTo("materialId", wmsInPutawayOrderDet.getMaterialId())
                        .andEqualTo("warehouseId", wmsInPutawayOrderDet.getWarehouseId())
                        .andEqualTo("storageId", wmsInPutawayOrderDet.getOutStorageId())
                        .andEqualTo("inventoryStatusId", wmsInPutawayOrderDet.getInventoryStatusId())
                        .andEqualTo("orgId",wmsInPutawayOrderDet.getOrgId());
                WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
                if(StringUtils.isEmpty(wmsInnerInventory)){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                }
                if (StringUtils.isEmpty(wmsInnerInventory.getPackingQty()) || wmsInnerInventory.getPackingQty().compareTo(BigDecimal.ZERO) == -1) {
                    throw new BizErrorException("????????????,???????????????");
                }
                WmsInnerInventory wmsIn = new WmsInnerInventory();
                wmsIn.setInventoryId(wmsInnerInventory.getInventoryId());
                wmsIn.setPackingQty(wmsInnerInventory.getPackingQty().subtract(wmsInPutawayOrderDet.getDistributionQty()));
                wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsIn);

                //????????????????????????
                example = new Example(WmsInnerInventory.class);
                Example.Criteria criteria1 = example.createCriteria();
                criteria1.andEqualTo("relevanceOrderCode", wmsInnerJobOrder.getJobOrderCode())
                        .andEqualTo("materialId", wmsInPutawayOrderDet.getMaterialId())
                        .andEqualTo("warehouseId", wmsInPutawayOrderDet.getWarehouseId())
                        .andEqualTo("storageId", wmsInPutawayOrderDet.getOutStorageId())
                        .andEqualTo("inventoryStatusId", wmsInPutawayOrderDet.getInventoryStatusId())
                        .andEqualTo("jobOrderDetId", wmsInPutawayOrderDet.getJobOrderDetId())
                        .andEqualTo("jobStatus", (byte) 2)
                        .andEqualTo("orgId",sysUser.getOrganizationId());
                WmsInnerInventory wmsInnerInventorys = wmsInnerInventoryMapper.selectOneByExample(example);
                if (StringUtils.isEmpty(wmsInnerInventorys)) {
                    //????????????
                    WmsInnerInventory inv = new WmsInnerInventory();
                    BeanUtil.copyProperties(wmsInnerInventory, inv);
                    inv.setStorageId(wmsInPutawayOrderDet.getOutStorageId());
                    inv.setWarehouseId(wmsInPutawayOrderDet.getWarehouseId());
                    inv.setRelevanceOrderCode(wmsInnerJobOrder.getJobOrderCode());
                    inv.setPackingQty(wmsInPutawayOrderDet.getDistributionQty());
                    inv.setJobStatus((byte) 2);
                    inv.setInventoryId(null);
                    inv.setJobOrderDetId(wmsInPutawayOrderDet.getJobOrderDetId());
                    inv.setOrgId(sysUser.getOrganizationId());
                    inv.setCreateUserId(sysUser.getUserId());
                    inv.setCreateTime(new Date());
                    inv.setModifiedTime(new Date());
                    inv.setModifiedUserId(sysUser.getUserId());
                    wmsInnerInventoryMapper.insertSelective(inv);
                } else {
                    //?????????
                    wmsInnerInventorys.setPackingQty(wmsInnerInventorys.getPackingQty().add(wmsInPutawayOrderDet.getDistributionQty()));
                    wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
                }

            }
        }
        return 1;
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int save(WmsInnerJobOrder record) {
        SysUser sysUser = currentUser();
        if (record.getJobOrderType() == (byte) 3) {
            //?????????
            record.setJobOrderCode(CodeUtils.getId("PUT-"));
        } else if (record.getJobOrderType() == (byte) 4) {
            //?????????
            record.setJobOrderCode(CodeUtils.getId("PICK-"));
        } else if (record.getJobOrderType() == (byte) 2) {
            //?????????
            record.setJobOrderCode(CodeUtils.getId("SHIFT-"));
            if(StringUtils.isEmpty(record.getShiftType())){
                record.setShiftType((byte)1);//???????????????
            }
        }
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrgId(sysUser.getOrganizationId());
        record.setIsDelete((byte) 1);
        if(StringUtils.isEmpty(record.getOrderStatus())){
            record.setOrderStatus((byte) 1);
        }

        record.setPlanQty(record.getWmsInPutawayOrderDets().stream().map(WmsInnerJobOrderDet::getPlanQty).reduce(BigDecimal.ZERO, BigDecimal::add));
        int num = wmsInPutawayOrderMapper.insertUseGeneratedKeys(record);
        for (WmsInnerJobOrderDet wmsInPutawayOrderDet : record.getWmsInPutawayOrderDets()) {
            wmsInPutawayOrderDet.setJobOrderId(record.getJobOrderId());
            wmsInPutawayOrderDet.setCreateTime(new Date());
            wmsInPutawayOrderDet.setCreateUserId(sysUser.getUserId());
            wmsInPutawayOrderDet.setModifiedTime(new Date());
            wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInPutawayOrderDet.setOrgId(sysUser.getOrganizationId());
            if (record.getJobOrderType() == (byte) 2) {
                wmsInPutawayOrderDet.setShiftStorageStatus((byte) 2);
            }
            wmsInPutawayOrderDetMapper.insertUseGeneratedKeys(wmsInPutawayOrderDet);
            if (record.getJobOrderType() == (byte) 3) {
                if(StringUtils.isEmpty(wmsInPutawayOrderDet.getOption1())){
                    Example example = new Example(WmsInnerInventory.class);
                    example.createCriteria().andEqualTo("relevanceOrderCode", record.getRelatedOrderCode())
                            .andEqualTo("materialId", wmsInPutawayOrderDet.getMaterialId())
                            .andEqualTo("batchCode", wmsInPutawayOrderDet.getBatchCode())
                            .andEqualTo("warehouseId", record.getWarehouseId())
                            .andEqualTo("storageId", wmsInPutawayOrderDet.getOutStorageId())
                            .andEqualTo("inventoryStatusId", wmsInPutawayOrderDet.getInventoryStatusId())
                            .andEqualTo("orgId",sysUser.getOrganizationId());
                    WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
                    if (StringUtils.isEmpty(wmsInnerInventory)) {
                        throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                    }
                    WmsInnerInventory wmsInnerInventorys = new WmsInnerInventory();
                    wmsInnerInventorys.setInventoryId(wmsInnerInventory.getInventoryId());
                    wmsInnerInventorys.setJobStatus((byte) 2);
                    wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
                }
            } else if (record.getJobOrderType() == (byte) 2) {
                // ??????????????????????????????
                WmsInnerInventory innerInventory = wmsInnerInventoryService.selectByKey(wmsInPutawayOrderDet.getSourceDetId());
                log.info("============= ???????????????" + JSON.toJSONString(innerInventory));
                log.info("============= ??????????????????" + JSON.toJSONString(wmsInPutawayOrderDet));
                if (innerInventory.getPackingQty().compareTo(wmsInPutawayOrderDet.getPlanQty()) < 0) {
                    throw new BizErrorException(ErrorCodeEnum.PDA5001012);
                }
                WmsInnerInventory newInnerInventory = new WmsInnerInventory();
                BeanUtil.copyProperties(innerInventory, newInnerInventory);
                newInnerInventory.setPackingQty(wmsInPutawayOrderDet.getPlanQty());
                newInnerInventory.setJobStatus((byte) 2);
                newInnerInventory.setJobOrderDetId(wmsInPutawayOrderDet.getJobOrderDetId());
                newInnerInventory.setOrgId(sysUser.getOrganizationId());
                newInnerInventory.setCreateTime(new Date());
                newInnerInventory.setCreateUserId(sysUser.getUserId());
                newInnerInventory.setParentInventoryId(innerInventory.getInventoryId());
                newInnerInventory.setRelevanceOrderCode(record.getJobOrderCode());
                newInnerInventory.setInventoryStatusId(wmsInPutawayOrderDet.getInventoryStatusId());
                wmsInnerInventoryService.save(newInnerInventory);
                // ?????????????????????
                innerInventory.setPackingQty(innerInventory.getPackingQty().subtract(wmsInPutawayOrderDet.getPlanQty()));
                wmsInnerInventoryService.update(innerInventory);
            }
        }
        if (StringUtils.isNotEmpty(record.getProductPalletId()) && record.getJobOrderType()==3) {
            //???????????????????????????????????????
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
            if (res <= 0) {
                throw new BizErrorException("???????????????????????????");
            }
            /**
             * 2022-03-24
             * ????????????
             * ????????????????????????????????????
             * ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
             */
            if (!record.getBarCodeList().isEmpty()){
                List<WmsInnerJobOrderDetBarcode> jobOrderDetBarcodeList = new ArrayList<>();
                List<WmsInnerHtJobOrderDetBarcode> htJobOrderDetBarcodes = new ArrayList<>();
                WmsInnerJobOrderDet wmsInnerJobOrderDet = record.getWmsInPutawayOrderDets().get(0);
                for (BarPODto barPODto : record.getBarCodeList()){
                    WmsInnerJobOrderDetBarcode detBarcode = new WmsInnerJobOrderDetBarcode();
                    detBarcode.setBarcode(barPODto.getBarCode());
                    detBarcode.setCustomerBarcode(barPODto.getCutsomerBarcode());
                    detBarcode.setSalesBarcode(barPODto.getSalesBarcode());
                    detBarcode.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
                    detBarcode.setStatus((byte) 1);
                    detBarcode.setOrgId(sysUser.getOrganizationId());
                    detBarcode.setCreateTime(new Date());
                    detBarcode.setCreateUserId(sysUser.getUserId());
                    detBarcode.setIsDelete((byte) 1);
                    jobOrderDetBarcodeList.add(detBarcode);
                    WmsInnerHtJobOrderDetBarcode innerHtJobOrderDetBarcode = new WmsInnerHtJobOrderDetBarcode();
                    BeanUtil.copyProperties(detBarcode, innerHtJobOrderDetBarcode);
                    htJobOrderDetBarcodes.add(innerHtJobOrderDetBarcode);
                }
                if (jobOrderDetBarcodeList.size() > 0) {
                    wmsInnerJobOrderDetBarcodeService.batchSave(jobOrderDetBarcodeList);
                }
                if (htJobOrderDetBarcodes.size() > 0) {
                    wmsInnerHtJobOrderDetBarcodeService.batchSave(htJobOrderDetBarcodes);
                }
            }

            //??????????????????
            SearchSysSpecItem searchSysSpecItemFiveRing = new SearchSysSpecItem();
            searchSysSpecItemFiveRing.setSpecCode("Automatic");
            List<SysSpecItem> itemListFiveRing = securityFeignApi.findSpecItemList(searchSysSpecItemFiveRing).getData();
            if (itemListFiveRing.size() < 1) {
                throw new BizErrorException("????????? Automatic ????????????");
            }
            SysSpecItem sysSpecItem = itemListFiveRing.get(0);

            //??????????????????
            if ("1".equals(sysSpecItem.getParaValue()) && record.getJobOrderType()==3) {
                //????????????
                try {
                    this.dis(record);
                }catch (Exception e){
                    //throw new BizErrorException(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        //?????? ????????????
        //?????????????????????????????????
        //1?????????????????????????????????????????????????????? ???????????????????????? ???????????????????????????
        if((StringUtils.isNotEmpty(record.getOrderTypeId())&&record.getOrderTypeId()==8) && record.getJobOrderType()==4 && (StringUtils.isNotEmpty(record.getType())&&record.getType()==1)){
            //??????????????????????????????
            int i = pickingOrderService.handDistribution(record.getWmsInPutawayOrderDets());
            if(i<1){
                throw new BizErrorException("?????????????????????");
            }
        }

        //???????????????????????????????????????????????????????????? 2022-03-24
        if((StringUtils.isNotEmpty(record.getOrderTypeId()) && record.getOrderTypeId()==1) && record.getJobOrderType()==4){
            //??????????????????????????????
            int i = pickingOrderService.autoDistribution(record.getJobOrderId().toString());
            /*if(i<1){
                throw new BizErrorException("?????????????????????");
            }*/
        }

        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(WmsInnerJobOrder record) {
        SysUser sysUser = currentUser();
        if (record.getOrderStatus() != (byte) 1) {
            throw new BizErrorException("???????????????????????????????????????");
        }

        if (record.getJobOrderType() == (byte) 2) {
            // ????????????
            SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
            searchWmsInnerJobOrderDet.setJobOrderId(record.getJobOrderId());
            List<WmsInnerJobOrderDetDto> orderDetMapperList = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet);
            if (!orderDetMapperList.isEmpty()) {
                // ???????????????????????????
                Example example = new Example(WmsInnerInventory.class);
                example.createCriteria().andIn("jobOrderDetId", orderDetMapperList.stream().map(WmsInnerJobOrderDet::getJobOrderDetId).collect(Collectors.toList()));
                List<WmsInnerInventory> innerInventories = wmsInnerInventoryMapper.selectByExample(example);
                // ????????????????????????????????????
                example.clear();
                example.createCriteria().andIn("inventoryId", innerInventories.stream().map(WmsInnerInventory::getParentInventoryId).collect(Collectors.toList()));
                List<WmsInnerInventory> sourceInnerInventories = wmsInnerInventoryMapper.selectByExample(example);
                for (WmsInnerInventory source : sourceInnerInventories) {
                    for (WmsInnerInventory innerInventory : innerInventories) {
                        if (innerInventory.getParentInventoryId().equals(source.getInventoryId())) {
                            source.setPackingQty(source.getPackingQty().add(innerInventory.getPackingQty()));
                        }
                    }
                }

                if (!sourceInnerInventories.isEmpty()) {
                    // ?????????????????????
                    wmsInnerInventoryService.batchUpdate(sourceInnerInventories);
                }
                if (!innerInventories.isEmpty()) {
                    // ??????????????????
                    wmsInnerInventoryService.batchDelete(innerInventories);
                }
                if (!innerInventories.isEmpty()) {
                    // ????????????
                    List<WmsInnerJobOrderDet> detList = new ArrayList<>();
                    for (WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto : orderDetMapperList) {
                        WmsInnerJobOrderDet det = wmsInnerJobOrderDetDto;
                        detList.add(det);
                    }
                    wmsInnerJobOrderDetService.batchDelete(detList);
                }
            }

            for (WmsInnerJobOrderDet wmsInPutawayOrderDet : record.getWmsInPutawayOrderDets()) {
                wmsInPutawayOrderDet.setJobOrderId(record.getJobOrderId());
                wmsInPutawayOrderDet.setCreateTime(new Date());
                wmsInPutawayOrderDet.setCreateUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setOrgId(sysUser.getOrganizationId());
                wmsInPutawayOrderDetMapper.insertUseGeneratedKeys(wmsInPutawayOrderDet);

                // ??????????????????????????????
                WmsInnerInventory innerInventory = wmsInnerInventoryService.selectByKey(wmsInPutawayOrderDet.getSourceDetId());
                if (innerInventory.getPackingQty().compareTo(wmsInPutawayOrderDet.getPlanQty()) < 0) {
                    throw new BizErrorException(ErrorCodeEnum.PDA5001012);
                }
                WmsInnerInventory newInnerInventory = new WmsInnerInventory();
                BeanUtil.copyProperties(innerInventory, newInnerInventory);
                newInnerInventory.setJobStatus((byte) 2);
                newInnerInventory.setJobOrderDetId(wmsInPutawayOrderDet.getJobOrderDetId());
                newInnerInventory.setOrgId(sysUser.getOrganizationId());
                newInnerInventory.setRelevanceOrderCode(record.getJobOrderCode());
                newInnerInventory.setCreateTime(new Date());
                newInnerInventory.setCreateUserId(sysUser.getUserId());
                newInnerInventory.setParentInventoryId(innerInventory.getInventoryId());
                newInnerInventory.setPackingQty(wmsInPutawayOrderDet.getPlanQty());
                wmsInnerInventoryService.save(newInnerInventory);
                // ?????????????????????
                innerInventory.setPackingQty(innerInventory.getPackingQty().subtract(wmsInPutawayOrderDet.getPlanQty()));
                wmsInnerInventoryService.update(innerInventory);
            }
            record.setPlanQty(record.getWmsInPutawayOrderDets().stream().map(WmsInnerJobOrderDet::getPlanQty).reduce(BigDecimal.ZERO, BigDecimal::add));
        }
        int i = wmsInPutawayOrderMapper.updateByPrimaryKey(record);
        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        String[] arrId = ids.split(",");
        for (String s : arrId) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(s);
            if (wmsInnerJobOrder.getOrderStatus() >= (byte) 4) {
                throw new BizErrorException("?????????????????????????????????");
            }
            if (StringUtils.isEmpty(wmsInnerJobOrder)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId", s);
            wmsInPutawayOrderDetMapper.deleteByExample(example);
        }
        return wmsInPutawayOrderMapper.deleteByIds(ids);
    }

    /**
     * ????????????
     *
     * @param wmsInnerJobOrderDto
     * @param wmsInnerJobOrderDet
     * @return
     */
    private int updateInventory(WmsInnerJobOrderDto wmsInnerJobOrderDto, WmsInnerJobOrderDet wmsInnerJobOrderDet) {
        SysUser sysUser = currentUser();
        //???
        Example example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("relevanceOrderCode", wmsInnerJobOrderDto.getRelatedOrderCode()).andEqualTo("materialId", wmsInnerJobOrderDet.getMaterialId())
                .andEqualTo("warehouseId", wmsInnerJobOrderDet.getWarehouseId()).andEqualTo("storageId", wmsInnerJobOrderDet.getOutStorageId())
                .andEqualTo("inventoryStatusId", wmsInnerJobOrderDet.getInventoryStatusId());
        if (StringUtils.isNotEmpty(wmsInnerJobOrderDet.getBatchCode())) {
            criteria.andEqualTo("batchCode", wmsInnerJobOrderDet.getBatchCode());
        }
        criteria.andEqualTo("jobOrderDetId",wmsInnerJobOrderDet.getSourceDetId());
        criteria.andEqualTo("orgId",sysUser.getOrganizationId());
        WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
        if (StringUtils.isEmpty(wmsInnerInventory)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        if (StringUtils.isEmpty(wmsInnerInventory.getPackingQty()) || wmsInnerInventory.getPackingQty().compareTo(BigDecimal.ZERO) == -1) {
            throw new BizErrorException("????????????,???????????????");
        }
        WmsInnerInventory wmsIn = new WmsInnerInventory();
        wmsIn.setInventoryId(wmsInnerInventory.getInventoryId());
        wmsIn.setPackingQty(wmsInnerInventory.getPackingQty().subtract(wmsInnerJobOrderDet.getDistributionQty()));
        int num = wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsIn);

//        //??????????????????
//        InventoryLogUtil.addLog(wmsInnerJobOrderDto,wmsInnerJobOrderDet,wmsInnerInventory.getPackingQty(),wmsInnerJobOrderDet.getDistributionQty(),(byte)2,(byte)2);

        example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("relevanceOrderCode", wmsInnerJobOrderDto.getJobOrderCode())
                .andEqualTo("materialId", wmsInnerJobOrderDet.getMaterialId())
                .andEqualTo("warehouseId", wmsInnerJobOrderDto.getWarehouseId())
                .andEqualTo("storageId", wmsInnerJobOrderDet.getOutStorageId())
                .andEqualTo("inventoryStatusId", wmsInnerJobOrderDet.getInventoryStatusId());
        if (!StringUtils.isEmpty(wmsInnerJobOrderDet.getBatchCode())) {
            criteria1.andEqualTo("batchCode", wmsInnerJobOrderDet.getBatchCode());
        }
        criteria1.andEqualTo("jobOrderDetId", wmsInnerJobOrderDet.getJobOrderDetId());
        criteria1.andEqualTo("jobStatus", (byte) 2);
        criteria1.andEqualTo("orgId",sysUser.getOrganizationId());
        WmsInnerInventory wmsInnerInventorys = wmsInnerInventoryMapper.selectOneByExample(example);
        if (StringUtils.isEmpty(wmsInnerInventorys)) {
            //????????????
            WmsInnerInventory inv = new WmsInnerInventory();
            BeanUtil.copyProperties(wmsInnerInventory, inv);
            inv.setStorageId(wmsInnerJobOrderDet.getOutStorageId());
            inv.setWarehouseId(wmsInnerJobOrderDet.getWarehouseId());
            inv.setRelevanceOrderCode(wmsInnerJobOrderDto.getJobOrderCode());
            inv.setPackingQty(wmsInnerJobOrderDet.getDistributionQty());
            inv.setJobStatus((byte) 2);
            inv.setInventoryId(null);
            inv.setBatchCode(wmsInnerJobOrderDet.getBatchCode());
            inv.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
            inv.setOrgId(sysUser.getOrganizationId());
            inv.setCreateUserId(sysUser.getUserId());
            inv.setCreateTime(new Date());
            inv.setModifiedTime(new Date());
            inv.setModifiedUserId(sysUser.getUserId());

            return wmsInnerInventoryMapper.insertSelective(inv);
        } else {
            //?????????
            wmsInnerInventorys.setPackingQty(wmsInnerInventorys.getPackingQty().add(wmsInnerJobOrderDet.getDistributionQty()));
            return wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
        }
    }

    /**
     * ??????
     *
     * @return
     */
    private int Inventory(WmsInnerJobOrderDto wmsInnerJobOrderDto,WmsInnerJobOrderDetDto oldDto, WmsInnerJobOrderDetDto newDto) {
        SysUser sysUser = currentUser();
        WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(oldDto.getJobOrderId());
        //???
        Example example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("relevanceOrderCode", wmsInnerJobOrder.getJobOrderCode()).andEqualTo("materialId", oldDto.getMaterialId()).andEqualTo("warehouseId", oldDto.getWarehouseId()).andEqualTo("storageId", oldDto.getOutStorageId());
        if (!StringUtils.isEmpty(oldDto.getBatchCode())) {
            criteria.andEqualTo("batchCode", oldDto.getBatchCode());
        }
        criteria.andEqualTo("jobOrderDetId", oldDto.getJobOrderDetId());
        criteria.andEqualTo("jobStatus", (byte) 2);
        criteria.andEqualTo("inventoryStatusId", oldDto.getInventoryStatusId());
        criteria.andEqualTo("orgId",sysUser.getOrganizationId());
        WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
        if (StringUtils.isEmpty(wmsInnerInventory)) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"?????????????????????");
        }
        WmsInnerInventory wmsIn = new WmsInnerInventory();
        wmsIn.setInventoryId(wmsInnerInventory.getInventoryId());
        wmsIn.setPackingQty(wmsInnerInventory.getPackingQty().subtract(newDto.getActualQty()));
        int num = wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsIn);

        //????????????????????????
        oldDto.setInStorageId(oldDto.getOutStorageId());
        InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrder,oldDto,wmsInnerInventory.getPackingQty(),newDto.getActualQty(),(byte)2,(byte)2);


        example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("materialId", newDto.getMaterialId()).andEqualTo("warehouseId", newDto.getWarehouseId()).andEqualTo("storageId", newDto.getInStorageId());
        if (!StringUtils.isEmpty(newDto.getBatchCode())) {
            criteria1.andEqualTo("batchCode", newDto.getBatchCode());
        }
        criteria1.andEqualTo("jobStatus", (byte) 1);
        criteria1.andEqualTo("inventoryStatusId", newDto.getInventoryStatusId());
        criteria1.andEqualTo("stockLock", 0).andEqualTo("qcLock", 0).andEqualTo("lockStatus", 0);
        criteria1.andEqualTo("orgId",sysUser.getOrganizationId());
        WmsInnerInventory wmsInnerInventorys = wmsInnerInventoryMapper.selectOneByExample(example);
        BigDecimal qty = BigDecimal.ZERO;
        if (StringUtils.isEmpty(wmsInnerInventorys)) {
            //????????????
            WmsInnerInventory inv = new WmsInnerInventory();
            BeanUtil.copyProperties(wmsInnerInventory, inv);
            inv.setStorageId(newDto.getInStorageId());
            inv.setWarehouseId(newDto.getWarehouseId());
            inv.setPackingQty(newDto.getActualQty());
            inv.setJobStatus((byte) 1);
            inv.setBatchCode(newDto.getBatchCode());
            inv.setJobOrderDetId(newDto.getJobOrderDetId());
            inv.setInventoryId(null);
            inv.setCreateUserId(sysUser.getUserId());
            inv.setCreateTime(new Date());
            inv.setOrgId(sysUser.getOrganizationId());
            inv.setModifiedTime(new Date());
            inv.setModifiedUserId(sysUser.getUserId());
            inv.setRelevanceOrderCode(wmsInnerJobOrder.getJobOrderCode());
            inv.setJobOrderDetId(null);
            num+=wmsInnerInventoryMapper.insertSelective(inv);
        } else {
            qty = wmsInnerInventorys.getPackingQty();
            if(StringUtils.isEmpty(wmsInnerInventorys.getPackingQty()) || wmsInnerInventorys.getPackingQty().compareTo(BigDecimal.ZERO)==0){
                wmsInnerInventorys.setReceivingDate(wmsInnerInventory.getReceivingDate());
            }
            //?????????
            wmsInnerInventorys.setPackingQty(wmsInnerInventorys.getPackingQty().add(newDto.getActualQty()));
            wmsInnerInventorys.setWorkOrderCode(wmsInnerInventory.getWorkOrderCode());
            wmsInnerInventorys.setRelevanceOrderCode(wmsInnerJobOrder.getJobOrderCode());
            wmsInnerInventorys.setModifiedTime(new Date());
            wmsInnerInventorys.setModifiedUserId(sysUser.getUserId());
            num+= wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
        }
        //??????????????????
        InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrder,newDto,qty,newDto.getActualQty(),(byte)2,(byte)1);

        return num;
    }

    /**
     * ??????????????????????????????
     * @param materialId
     * @param storageId
     * @param qty
     * @return
     */
    @Override
    public Boolean storageCapacity(Long materialId,Long storageId,BigDecimal qty){
        SysUser sysUser = currentUser();
        Boolean isSuccess = true;
        //???????????????
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("capacity");
        List<SysSpecItem> itemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        if(itemList.size()>0){

            //????????????
            SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
            searchBaseStorage.setStorageId(storageId);
            List<BaseStorage> baseStorages = baseFeignApi.findList(searchBaseStorage).getData();
            if(baseStorages.size()<1){
                throw new BizErrorException("????????????????????????");
            }
            if(StringUtils.isEmpty(baseStorages.get(0).getMaterialStoreType())){
                throw new BizErrorException("?????????????????????????????????");
            }

            //??????????????????????????????????????????
            if(itemList.get(0).getParaValue().equals("base_storage_capacity")){
                //????????????
                SearchBaseStorageCapacity searchBaseStorageCapacity = new SearchBaseStorageCapacity();
                searchBaseStorageCapacity.setMaterialId(materialId);
                List<BaseStorageCapacity> baseStorageCapacities = baseFeignApi.findList(searchBaseStorageCapacity).getData();
                if(baseStorageCapacities.size()<1){
                    throw new BizErrorException("???????????????????????????");
                }
                Example example = new Example(WmsInnerInventory.class);
                example.createCriteria().andEqualTo("storageId",storageId).andEqualTo("materialId",materialId).andEqualTo("orgId",sysUser.getOrganizationId());
                List<WmsInnerInventory> inventories = wmsInnerInventoryMapper.selectByExample(example);
                BigDecimal totalQty = inventories.stream()
                        .map(WmsInnerInventory::getPackingQty)
                        .reduce(BigDecimal.ZERO,BigDecimal::add);
                switch (baseStorages.get(0).getMaterialStoreType()) {
                    case 1:
                        if (StringUtils.isEmpty(baseStorageCapacities.get(0).getTypeACapacity())) {
                            throw new BizErrorException("?????????A?????????");
                        }
                        totalQty = baseStorageCapacities.get(0).getTypeACapacity().subtract(totalQty);
                        if(totalQty.compareTo(qty)==-1){
                            isSuccess = false;
                        }
                        break;
                    case 2:
                        if (StringUtils.isEmpty(baseStorageCapacities.get(0).getTypeBCapacity())) {
                            throw new BizErrorException("?????????B?????????");
                        }
                        totalQty = baseStorageCapacities.get(0).getTypeBCapacity().subtract(totalQty);
                        if(totalQty.compareTo(qty)==-1){
                            isSuccess = false;
                        }
                        break;
                    case 3:
                        if (StringUtils.isEmpty(baseStorageCapacities.get(0).getTypeCCapacity())) {
                            throw new BizErrorException("?????????C?????????");
                        }
                        totalQty = baseStorageCapacities.get(0).getTypeCCapacity().subtract(totalQty);
                        if(totalQty.compareTo(qty)==-1){
                            isSuccess = false;
                        }
                        break;
                    case 4:
                        if (StringUtils.isEmpty(baseStorageCapacities.get(0).getTypeDCapacity())) {
                            throw new BizErrorException("?????????D?????????");
                        }
                        totalQty = baseStorageCapacities.get(0).getTypeDCapacity().subtract(totalQty);
                        if(totalQty.compareTo(qty)==-1){
                            isSuccess = false;
                        }
                        break;
                }
            }
        }
        return isSuccess;
    }

    @Override
    @Transactional
    public int reCreateInnerJobShift(Long jobOrderId, BigDecimal qty) {
        int i=0;
        SysUser sysUser=currentUser();
        WmsInnerJobOrder wmsInnerJobOrder=wmsInPutawayOrderMapper.selectByPrimaryKey(jobOrderId);
        Example example = new Example(WmsInnerJobOrderDet.class);
        example.createCriteria().andEqualTo("jobOrderId",jobOrderId);
        List<WmsInnerJobOrderDet> jobOrderDetList = wmsInPutawayOrderDetMapper.selectByExample(example);
        if(jobOrderDetList.size()>0){
            if(qty.compareTo(jobOrderDetList.get(0).getPlanQty())==0){
                return 1;
            }
            //????????????
            this.cancelDistribution(jobOrderId.toString());

            WmsInnerJobOrderDet wmsInnerJobOrderDet=jobOrderDetList.get(0);
            wmsInnerJobOrderDet.setPlanQty(qty);
            wmsInnerJobOrderDet.setDistributionQty(qty);
            wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInnerJobOrderDet.setModifiedTime(new Date());
            i=wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wmsInnerJobOrderDet);

            SearchWmsInnerInventory searchWmsInnerInventory=new SearchWmsInnerInventory();
            searchWmsInnerInventory.setMaterialId(wmsInnerJobOrderDet.getMaterialId());
            searchWmsInnerInventory.setStorageId(wmsInnerJobOrderDet.getOutStorageId());
            searchWmsInnerInventory.setLockStatus((byte)0);
            searchWmsInnerInventory.setInventoryStatusName("??????");
            List<WmsInnerInventoryDto> inventoryDtos=wmsInnerInventoryService.findList(ControllerUtil.dynamicCondition(searchWmsInnerInventory));
            if(StringUtils.isEmpty(inventoryDtos) || inventoryDtos.size()<=0){
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"?????????????????????");
            }

            List<WmsInnerInventoryDto> dtoList = inventoryDtos.stream().filter(u -> (u.getPackingQty().compareTo(qty)>=0)).collect(Collectors.toList());
            if(StringUtils.isEmpty(dtoList) || dtoList.size()<=0){
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"?????????????????????????????????????????????");
            }
            //????????????
            WmsInnerInventory innerInventory=dtoList.get(0);
            WmsInnerInventory newInnerInventory = new WmsInnerInventory();
            BeanUtil.copyProperties(innerInventory, newInnerInventory);
            newInnerInventory.setPackingQty(qty);
            newInnerInventory.setJobStatus((byte) 2);
            newInnerInventory.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
            newInnerInventory.setOrgId(sysUser.getOrganizationId());
            newInnerInventory.setCreateTime(new Date());
            newInnerInventory.setCreateUserId(sysUser.getUserId());
            newInnerInventory.setParentInventoryId(innerInventory.getInventoryId());
            newInnerInventory.setRelevanceOrderCode(wmsInnerJobOrder.getJobOrderCode());
            i+=wmsInnerInventoryService.save(newInnerInventory);
            // ?????????????????????
            innerInventory.setPackingQty(innerInventory.getPackingQty().subtract(qty));
            i+=wmsInnerInventoryService.update(innerInventory);

        }

        return i;
    }

    @Override
    @Transactional
    @LcnTransaction
    public int updateShit(Long jobOrderId, BigDecimal ngQty) {
        int i=0;
        SysUser sysUser=currentUser();
        WmsInnerJobOrder wmsInnerJobOrder=wmsInPutawayOrderMapper.selectByPrimaryKey(jobOrderId);
        Long warehouseId=wmsInnerJobOrder.getWarehouseId();
        SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
        searchBaseInventoryStatus.setWarehouseId(warehouseId);
        searchBaseInventoryStatus.setOrgId(sysUser.getOrganizationId());
        List<BaseInventoryStatus> inventoryStatusList = baseFeignApi.findList(searchBaseInventoryStatus).getData();
        List<BaseInventoryStatus> statusList = inventoryStatusList.stream().filter(item -> item.getInventoryStatusName().equals("?????????")).collect(Collectors.toList());
        if(StringUtils.isEmpty(statusList) || statusList.size()<=0){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"?????????????????????????????????");
        }

        List<BaseInventoryStatus> statusOKList = inventoryStatusList.stream().filter(item -> item.getInventoryStatusName().equals("??????")).collect(Collectors.toList());
        if(StringUtils.isEmpty(statusOKList) || statusOKList.size()<=0){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"??????????????????????????????");
        }

        Example example = new Example(WmsInnerJobOrderDet.class);
        example.createCriteria().andEqualTo("jobOrderId",jobOrderId)
                                .andEqualTo("inventoryStatusId",statusList.get(0).getInventoryStatusId());
        List<WmsInnerJobOrderDet> jobOrderDetList = wmsInPutawayOrderDetMapper.selectByExample(example);
        if(jobOrderDetList.size()>0){
            if(ngQty.compareTo(new BigDecimal(0))==1 && ngQty.compareTo(jobOrderDetList.get(0).getPlanQty())==-1){
                WmsInnerJobOrderDet wmsInnerJobOrderDet=jobOrderDetList.get(0);
                wmsInnerJobOrderDet.setPlanQty(ngQty);
                wmsInnerJobOrderDet.setDistributionQty(ngQty);
                wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInnerJobOrderDet.setModifiedTime(new Date());
                i=wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wmsInnerJobOrderDet);

                //????????????????????????
                BigDecimal newQty=wmsInnerJobOrderDet.getPlanQty().subtract(ngQty);
                WmsInnerJobOrderDet newDet=new WmsInnerJobOrderDet();
                BeanUtil.copyProperties(jobOrderDetList.get(0),newDet);
                newDet.setJobOrderDetId(null);
                newDet.setPlanQty(newQty);
                newDet.setDistributionQty(newQty);
                newDet.setInventoryStatusId(statusOKList.get(0).getInventoryStatusId());
                newDet.setCreateUserId(sysUser.getUserId());
                newDet.setCreateTime(new Date());
                i+=wmsInPutawayOrderDetMapper.insertUseGeneratedKeys(newDet);

                /*SearchWmsInnerInventory searchWmsInnerInventory=new SearchWmsInnerInventory();
                searchWmsInnerInventory.setMaterialId(wmsInnerJobOrderDet.getMaterialId());
                searchWmsInnerInventory.setStorageId(wmsInnerJobOrderDet.getOutStorageId());
                searchWmsInnerInventory.setLockStatus((byte)0);
                searchWmsInnerInventory.setInventoryStatusName("??????");
                List<WmsInnerInventoryDto> inventoryDtos=wmsInnerInventoryService.findList(ControllerUtil.dynamicCondition(searchWmsInnerInventory));
                if(StringUtils.isEmpty(inventoryDtos) || inventoryDtos.size()<=0){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"?????????????????????");
                }

                List<WmsInnerInventoryDto> dtoList = inventoryDtos.stream().filter(u -> (u.getPackingQty().compareTo(newQty)>=0)).collect(Collectors.toList());
                if(StringUtils.isEmpty(dtoList) || dtoList.size()<=0){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"?????????????????????????????????????????????");
                }
                //????????????
                WmsInnerInventory innerInventory=dtoList.get(0);
                WmsInnerInventory newInnerInventory = new WmsInnerInventory();
                BeanUtil.copyProperties(innerInventory, newInnerInventory);
                newInnerInventory.setPackingQty(newQty);
                newInnerInventory.setJobStatus((byte) 2);
                newInnerInventory.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
                newInnerInventory.setOrgId(sysUser.getOrganizationId());
                newInnerInventory.setCreateTime(new Date());
                newInnerInventory.setCreateUserId(sysUser.getUserId());
                newInnerInventory.setParentInventoryId(innerInventory.getInventoryId());
                newInnerInventory.setRelevanceOrderCode(wmsInnerJobOrder.getJobOrderCode());
                i+=wmsInnerInventoryService.save(newInnerInventory);
                // ?????????????????????
                innerInventory.setPackingQty(innerInventory.getPackingQty().subtract(newQty));
                i+=wmsInnerInventoryService.update(innerInventory);*/

            }
            else if(ngQty.compareTo(new BigDecimal(0))==0){
                WmsInnerJobOrderDet wmsInnerJobOrderDet=jobOrderDetList.get(0);
                wmsInnerJobOrderDet.setInventoryStatusId(statusOKList.get(0).getInventoryStatusId());
                wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInnerJobOrderDet.setModifiedTime(new Date());
                i=wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wmsInnerJobOrderDet);
            }
            else {
                //???????????????????????????????????????????????????
                return 1;
            }

        }

        return i;
    }

    @Override
    public int releaseStacking(Long jobOrderId) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        // ????????????
        Example example = new Example(WmsInnerJobOrderReMspp.class);
        example.createCriteria().andEqualTo("jobOrderId", jobOrderId);
        List<WmsInnerJobOrderReMspp> jobOrderReMspps = wmsInnerJobOrderReMsppMapper.selectByExample(example);
        if (!jobOrderReMspps.isEmpty()){
            WanbaoStacking stacking = wanbaoFeignApi.detail(jobOrderReMspps.get(0).getProductPalletId()).getData();
            if(StringUtils.isEmpty(stacking)){
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "???????????????????????????????????????");
            }
            stacking.setUsageStatus((byte) 1);
            wanbaoFeignApi.updateAndClearBarcode(stacking);
        }
        // ?????????????????????????????????
        WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(jobOrderId);
        if(!wmsInnerJobOrder.getJobOrderType().equals((byte) 3)){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "???????????????????????????????????????");
        }
        wmsInnerJobOrder.setReleaseUserId(user.getUserId());
        wmsInnerJobOrder.setReleaseTime(new Date());
        return wmsInPutawayOrderMapper.updateByPrimaryKey(wmsInnerJobOrder);
    }

    @Override
    public int autoRecheck(String relatedOrderCode) {
        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setRelatedOrderCode(relatedOrderCode);
        searchWmsInnerJobOrder.setOption1("qmsCommit");
        List<WmsInnerJobOrderDto> wmsInnerJobOrderDtos = wmsInPutawayOrderMapper.findList(searchWmsInnerJobOrder);
        if (wmsInnerJobOrderDtos.isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "??????????????????????????????????????????");
        }
        WmsInnerJobOrderDto innerJobOrderDto = wmsInnerJobOrderDtos.get(0);

        List<BaseInventoryStatus> inventoryStatusList = baseFeignApi.findList(new SearchBaseInventoryStatus()).getData();
        Long ok_inventoryStatusId = null;
        Long ng_inventoryStatusId = null;
        for (BaseInventoryStatus inventoryStatus : inventoryStatusList){
            if (inventoryStatus.getInventoryStatusName().equals("?????????")){
                ng_inventoryStatusId = inventoryStatus.getInventoryStatusId();
            }
            if (inventoryStatus.getInventoryStatusName().equals("??????")){
                ok_inventoryStatusId = inventoryStatus.getInventoryStatusId();
            }
        }
        Example example = new Example(WmsInnerJobOrderDet.class);
        example.createCriteria().andEqualTo("jobOrderId", innerJobOrderDto.getJobOrderId());
        List<WmsInnerJobOrderDet> jobOrderDetList = wmsInPutawayOrderDetMapper.selectByExample(example);
        WmsInnerJobOrderDet ng_jobOrderDet = new WmsInnerJobOrderDet();
        WmsInnerJobOrderDet ok_jobOrderDet = new WmsInnerJobOrderDet();
        for (WmsInnerJobOrderDet item : jobOrderDetList){
            if (item.getInventoryStatusId().equals(ng_inventoryStatusId)){
                ng_jobOrderDet = item;
            }
            if (item.getInventoryStatusId().equals(ok_inventoryStatusId) && item.getActualQty() == null){
                ok_jobOrderDet = item;
            }
        }
        int num = 0;
        if (ok_jobOrderDet.getJobOrderDetId() != null){
            ok_jobOrderDet.setPlanQty(ok_jobOrderDet.getPlanQty().add(BigDecimal.ONE));
            ok_jobOrderDet.setDistributionQty(ok_jobOrderDet.getDistributionQty().add(BigDecimal.ONE));
            num += wmsInPutawayOrderDetMapper.updateByPrimaryKey(ok_jobOrderDet);
        }else {
            WmsInnerJobOrderDet newJobOrderDet = new WmsInnerJobOrderDet();
            BeanUtil.copyProperties(ng_jobOrderDet, newJobOrderDet);
            newJobOrderDet.setJobOrderDetId(null);
            newJobOrderDet.setPlanQty(BigDecimal.ONE);
            newJobOrderDet.setDistributionQty(BigDecimal.ONE);
            num += wmsInPutawayOrderDetMapper.insert(newJobOrderDet);
        }
        if (ng_jobOrderDet.getPlanQty().compareTo(BigDecimal.ONE) == 0){
            num += wmsInPutawayOrderDetMapper.delete(ng_jobOrderDet);
        }else {
            ng_jobOrderDet.setPlanQty(ng_jobOrderDet.getPlanQty().subtract(BigDecimal.ONE));
            ng_jobOrderDet.setDistributionQty(ng_jobOrderDet.getDistributionQty().subtract(BigDecimal.ONE));
            num += wmsInPutawayOrderDetMapper.updateByPrimaryKey(ng_jobOrderDet);
        }
        return num>0?1:0;
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
}
