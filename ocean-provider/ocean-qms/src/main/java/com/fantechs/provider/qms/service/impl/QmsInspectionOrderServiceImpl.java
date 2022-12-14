package com.fantechs.provider.qms.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDetDto;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDto;
import com.fantechs.common.base.general.dto.om.SearchOmSalesOrderDetDto;
import com.fantechs.common.base.general.dto.om.SearchOmSalesOrderDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInAsnOrderDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDto;
import com.fantechs.common.base.general.entity.basic.BaseInspectionStandard;
import com.fantechs.common.base.general.entity.basic.BaseInspectionWay;
import com.fantechs.common.base.general.entity.basic.BaseInventoryStatus;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInspectionStandard;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInspectionWay;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInventoryStatus;
import com.fantechs.common.base.general.entity.qms.QmsInspectionOrder;
import com.fantechs.common.base.general.entity.qms.QmsInspectionOrderDet;
import com.fantechs.common.base.general.entity.qms.QmsInspectionOrderDetSample;
import com.fantechs.common.base.general.entity.qms.history.QmsHtInspectionOrder;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsInspectionOrderDet;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrderDet;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInAsnOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventoryDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.DateUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.api.qms.OMFeignApi;
import com.fantechs.provider.api.wms.in.InFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.qms.mapper.QmsHtInspectionOrderMapper;
import com.fantechs.provider.qms.mapper.QmsInspectionOrderDetMapper;
import com.fantechs.provider.qms.mapper.QmsInspectionOrderDetSampleMapper;
import com.fantechs.provider.qms.mapper.QmsInspectionOrderMapper;
import com.fantechs.provider.qms.service.QmsInspectionOrderService;
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
 * Created by leifengzhi on 2021/05/25.
 */
@Service
public class QmsInspectionOrderServiceImpl extends BaseService<QmsInspectionOrder> implements QmsInspectionOrderService {

    @Resource
    private QmsInspectionOrderMapper qmsInspectionOrderMapper;
    @Resource
    private QmsInspectionOrderDetMapper qmsInspectionOrderDetMapper;
    @Resource
    private QmsHtInspectionOrderMapper qmsHtInspectionOrderMapper;
    @Resource
    private QmsInspectionOrderDetSampleMapper qmsInspectionOrderDetSampleMapper;
    @Resource
    private InFeignApi inFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private OMFeignApi oMFeignApi;
    @Resource
    private InnerFeignApi innerFeignApi;
    @Resource
    private SFCFeignApi sfcFeignApi;
    @Resource
    private QmsInspectionOrderDetServiceImpl qmsInspectionOrderDetService;
    @Resource
    private QmsInspectionOrderDetSampleServiceImpl qmsInspectionOrderDetSampleService;

    @Override
    public List<QmsInspectionOrder> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        List<QmsInspectionOrder> qmsInspectionOrders = qmsInspectionOrderMapper.findList(map);
        return qmsInspectionOrders;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int recheck(Long inspectionOrderId){
        Example example = new Example(QmsInspectionOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("inspectionOrderId",inspectionOrderId);
        List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetMapper.selectByExample(example);

        List<Long> detIds = new LinkedList<>();
        for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets){
            detIds.add(qmsInspectionOrderDet.getInspectionOrderDetId());
        }

        Example example1 = new Example(QmsInspectionOrderDetSample.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andIn("inspectionOrderDetId",detIds);
        qmsInspectionOrderDetSampleMapper.deleteByExample(example1);

        return batchQualified(inspectionOrderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int batchQualified(Long inspectionOrderId){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        QmsInspectionOrder qmsInspectionOrder = qmsInspectionOrderMapper.selectByPrimaryKey(inspectionOrderId);

        Example example = new Example(QmsInspectionOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("inspectionOrderId",inspectionOrderId);
        List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetMapper.selectByExample(example);

        Example example1 = new Example(QmsInspectionOrderDetSample.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("inspectionOrderId",inspectionOrderId);
        List<QmsInspectionOrderDetSample> qmsInspectionOrderDetSamples = qmsInspectionOrderDetSampleMapper.selectByExample(example1);

        List<QmsInspectionOrderDetSample> inspectionOrderDetSampleList = new LinkedList<>();
        for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets){
            //??????
            qmsInspectionOrderDet.setBadnessQty(BigDecimal.ZERO);
            qmsInspectionOrderDet.setInspectionResult((byte)1);
            qmsInspectionOrderDetMapper.updateByPrimaryKeySelective(qmsInspectionOrderDet);

            //??????
            for(QmsInspectionOrderDetSample qmsInspectionOrderDetSample : qmsInspectionOrderDetSamples){
                QmsInspectionOrderDetSample inspectionOrderDetSample = new QmsInspectionOrderDetSample();
                inspectionOrderDetSample.setInspectionOrderDetId(qmsInspectionOrderDet.getInspectionOrderDetId());
                inspectionOrderDetSample.setBarcode(qmsInspectionOrderDetSample.getBarcode());
                inspectionOrderDetSample.setSampleValue("OK");
                inspectionOrderDetSample.setOrgId(user.getOrganizationId());
                inspectionOrderDetSampleList.add(inspectionOrderDetSample);
            }
        }
        if(StringUtils.isNotEmpty(inspectionOrderDetSampleList)) {
            qmsInspectionOrderDetSampleMapper.insertList(inspectionOrderDetSampleList);
        }

        qmsInspectionOrder.setInspectionStatus((byte)3);
        qmsInspectionOrder.setInspectionResult((byte)1);
        int i = qmsInspectionOrderMapper.updateByPrimaryKeySelective(qmsInspectionOrder);

        //????????????
        this.handleInventory(qmsInspectionOrder.getInspectionOrderCode(),qmsInspectionOrder.getInspectionResult());

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int batchSubmit(Long inspectionOrderId){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        QmsInspectionOrder qmsInspectionOrder = qmsInspectionOrderMapper.selectByPrimaryKey(inspectionOrderId);

        Example example = new Example(QmsInspectionOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("inspectionOrderId",inspectionOrderId);
        List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetMapper.selectByExample(example);

        Example example1 = new Example(QmsInspectionOrderDetSample.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("inspectionOrderId",inspectionOrderId);
        List<QmsInspectionOrderDetSample> qmsInspectionOrderDetSamples = qmsInspectionOrderDetSampleMapper.selectByExample(example1);

        List<QmsInspectionOrderDetSample> inspectionOrderDetSampleList = new LinkedList<>();
        for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets){
            //??????
            if(StringUtils.isEmpty(qmsInspectionOrderDet.getInspectionResult())) {
                qmsInspectionOrderDet.setBadnessQty(BigDecimal.ZERO);
                qmsInspectionOrderDet.setInspectionResult((byte) 1);
                qmsInspectionOrderDetMapper.updateByPrimaryKeySelective(qmsInspectionOrderDet);

                //??????
                for(QmsInspectionOrderDetSample qmsInspectionOrderDetSample : qmsInspectionOrderDetSamples){
                    QmsInspectionOrderDetSample inspectionOrderDetSample = new QmsInspectionOrderDetSample();
                    inspectionOrderDetSample.setInspectionOrderDetId(qmsInspectionOrderDet.getInspectionOrderDetId());
                    inspectionOrderDetSample.setBarcode(qmsInspectionOrderDetSample.getBarcode());
                    inspectionOrderDetSample.setSampleValue("OK");
                    inspectionOrderDetSample.setOrgId(user.getOrganizationId());
                    inspectionOrderDetSampleList.add(inspectionOrderDetSample);
                }
            }
        }
        if(StringUtils.isNotEmpty(inspectionOrderDetSampleList)) {
            qmsInspectionOrderDetSampleMapper.insertList(inspectionOrderDetSampleList);
        }

        List<QmsInspectionOrderDetSample> barcodes = qmsInspectionOrderDetSampleService.findBarcodes(inspectionOrderId);
        List<QmsInspectionOrderDetSample> unQualifiedBarcodes = barcodes.stream().filter(item -> item.getBarcodeStatus()!=null && item.getBarcodeStatus() == 0).collect(Collectors.toList());
        if(barcodes.size() == unQualifiedBarcodes.size()){
            qmsInspectionOrder.setInspectionResult((byte)0);
        }else {
            qmsInspectionOrder.setInspectionResult((byte)2);
        }

        qmsInspectionOrder.setInspectionStatus((byte)3);
        int i = qmsInspectionOrderMapper.updateByPrimaryKeySelective(qmsInspectionOrder);

        //????????????
        if(qmsInspectionOrder.getInspectionResult() == (byte)0) {
            this.handleInventory(qmsInspectionOrder.getInspectionOrderCode(), qmsInspectionOrder.getInspectionResult());
        }else if(qmsInspectionOrder.getInspectionResult() == (byte)2){
            this.splitInventory(qmsInspectionOrder.getInspectionOrderCode(), unQualifiedBarcodes);
        }

        return i;
    }

    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int splitInventory(String inspectionOrderCode,List<QmsInspectionOrderDetSample> unQualifiedBarcodes){
        SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
        searchBaseInventoryStatus.setInventoryStatusName("?????????");
        searchBaseInventoryStatus.setNameQueryMark(1);
        List<BaseInventoryStatus> inventoryStatusList1 = baseFeignApi.findList(searchBaseInventoryStatus).getData();
        if(StringUtils.isEmpty(inventoryStatusList1)){
            throw new BizErrorException("??????????????????????????????");
        }

        searchBaseInventoryStatus.setInventoryStatusName("??????");
        List<BaseInventoryStatus> inventoryStatusList2 = baseFeignApi.findList(searchBaseInventoryStatus).getData();
        if(StringUtils.isEmpty(inventoryStatusList2)){
            throw new BizErrorException("??????????????????????????????");
        }

        //?????????????????????????????????
        int unQualifiedCount = 0;
        SearchWmsInnerInventoryDet  searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
        searchWmsInnerInventoryDet.setPageSize(999);
        searchWmsInnerInventoryDet.setInspectionOrderCode(inspectionOrderCode);
        List<WmsInnerInventoryDetDto> inventoryDetDtos = innerFeignApi.findList(searchWmsInnerInventoryDet).getData();
        if(StringUtils.isNotEmpty(inventoryDetDtos)) {
            for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : inventoryDetDtos) {
                wmsInnerInventoryDetDto.setInventoryStatusId(inventoryStatusList2.get(0).getInventoryStatusId());
                for (QmsInspectionOrderDetSample qmsInspectionOrderDetSample : unQualifiedBarcodes){
                    if(wmsInnerInventoryDetDto.getBarcode().equals(qmsInspectionOrderDetSample.getBarcode())){
                        wmsInnerInventoryDetDto.setInventoryStatusId(inventoryStatusList1.get(0).getInventoryStatusId());
                        unQualifiedCount++;
                    }
                }
                wmsInnerInventoryDetDto.setQcDate(new Date());
                innerFeignApi.update(wmsInnerInventoryDetDto);
            }
        }

        //???????????????????????????
        SearchWmsInnerInventory  searchWmsInnerInventory = new SearchWmsInnerInventory();
        searchWmsInnerInventory.setInspectionOrderCode(inspectionOrderCode);
        ResponseEntity<List<WmsInnerInventoryDto>> innerInventoryDtoList = innerFeignApi.findList(searchWmsInnerInventory);
        if(StringUtils.isEmpty(innerInventoryDtoList.getData())){
            throw new BizErrorException("??????????????????????????????");
        }
        WmsInnerInventoryDto wmsInnerInventoryDto = new WmsInnerInventoryDto();
        BeanUtils.copyProperties(innerInventoryDtoList.getData().get(0),wmsInnerInventoryDto);
        WmsInnerInventoryDto innerInventoryDto = new WmsInnerInventoryDto();
        BeanUtils.copyProperties(innerInventoryDtoList.getData().get(0),innerInventoryDto);

        //?????????????????????
        Map<String,Object> map = new HashMap<>();
        map.put("materialId",wmsInnerInventoryDto.getMaterialId());
        map.put("warehouseId",wmsInnerInventoryDto.getWarehouseId());
        map.put("storageId",wmsInnerInventoryDto.getStorageId());
        map.put("batchCode",wmsInnerInventoryDto.getBatchCode());
        map.put("inventoryStatusId",inventoryStatusList1.get(0).getInventoryStatusId());
        map.put("jobStatus",1);
        map.put("lockStatus",0);
        map.put("qcLock",0);
        map.put("stockLock",0);
        WmsInnerInventory oldInventory = innerFeignApi.selectOneByExample(map).getData();
        if (StringUtils.isNotEmpty(oldInventory)) {
            WmsInnerInventoryDto inventoryDto = new WmsInnerInventoryDto();
            BeanUtils.copyProperties(oldInventory,inventoryDto);
            inventoryDto.setPackingQty(inventoryDto.getPackingQty().add(new BigDecimal(unQualifiedCount)));
            innerFeignApi.update(inventoryDto);
            wmsInnerInventoryDto.setPackingQty(BigDecimal.ZERO);
            innerFeignApi.update(wmsInnerInventoryDto);
        }else{
            wmsInnerInventoryDto.setQcLock((byte)0);
            wmsInnerInventoryDto.setInventoryStatusId(inventoryStatusList1.get(0).getInventoryStatusId());
            wmsInnerInventoryDto.setPackingQty(new BigDecimal(unQualifiedCount));
            innerFeignApi.update(wmsInnerInventoryDto);
        }

        //??????????????????
        BigDecimal qty = innerInventoryDto.getPackingQty().subtract(new BigDecimal(unQualifiedCount));
        map.put("inventoryStatusId",inventoryStatusList2.get(0).getInventoryStatusId());
        WmsInnerInventory oldInventory1 = innerFeignApi.selectOneByExample(map).getData();
        if (StringUtils.isNotEmpty(oldInventory1)) {
            WmsInnerInventoryDto inventoryDto = new WmsInnerInventoryDto();
            BeanUtils.copyProperties(oldInventory1,inventoryDto);
            inventoryDto.setPackingQty(inventoryDto.getPackingQty().add(qty));
            innerFeignApi.update(inventoryDto);
        }else{
            innerInventoryDto.setInventoryId(null);
            innerInventoryDto.setQcLock((byte)0);
            innerInventoryDto.setInventoryStatusId(inventoryStatusList2.get(0).getInventoryStatusId());
            innerInventoryDto.setPackingQty(qty);
            innerFeignApi.add(innerInventoryDto);
        }

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int audit(QmsInspectionOrder qmsInspectionOrder) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        qmsInspectionOrder.setModifiedUserId(user.getUserId());
        qmsInspectionOrder.setModifiedTime(new Date());
        return qmsInspectionOrderMapper.updateByPrimaryKeySelective(qmsInspectionOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int thirdInspection(QmsInspectionOrder qmsInspectionOrder) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        handleInventory(qmsInspectionOrder.getInspectionOrderCode(),qmsInspectionOrder.getInspectionResult());

        qmsInspectionOrder.setIfThirdInspection(StringUtils.isEmpty(qmsInspectionOrder.getIfThirdInspection()) ? 1 :qmsInspectionOrder.getIfThirdInspection());
        qmsInspectionOrder.setInspectionStatus((byte)3);
        qmsInspectionOrder.setModifiedUserId(user.getUserId());
        qmsInspectionOrder.setModifiedTime(new Date());
        return qmsInspectionOrderMapper.updateByPrimaryKeySelective(qmsInspectionOrder);
    }

    @Override
    public QmsInspectionOrder selectByKey(Long key) {
        Map<String,Object> map = new HashMap<>();
        map.put("inspectionOrderId",key);
        QmsInspectionOrder qmsInspectionOrder = qmsInspectionOrderMapper.findList(map).get(0);
        if(StringUtils.isNotEmpty(qmsInspectionOrder)) {
            SearchQmsInspectionOrderDet searchQmsInspectionOrderDet = new SearchQmsInspectionOrderDet();
            searchQmsInspectionOrderDet.setInspectionOrderId(qmsInspectionOrder.getInspectionOrderId());
            List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetMapper.findDetList(ControllerUtil.dynamicConditionByEntity(searchQmsInspectionOrderDet));
            qmsInspectionOrder.setQmsInspectionOrderDets(qmsInspectionOrderDets);
        }

        return qmsInspectionOrder;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int save(QmsInspectionOrder qmsInspectionOrder) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        qmsInspectionOrder.setInspectionOrderCode(CodeUtils.getId("CPJY-"));
        qmsInspectionOrder.setCreateUserId(user.getUserId());
        qmsInspectionOrder.setCreateTime(new Date());
        qmsInspectionOrder.setModifiedUserId(user.getUserId());
        qmsInspectionOrder.setModifiedTime(new Date());
        qmsInspectionOrder.setStatus(StringUtils.isEmpty(qmsInspectionOrder.getStatus())?1:qmsInspectionOrder.getStatus());
        qmsInspectionOrder.setInspectionStatus((byte)1);
        qmsInspectionOrder.setOrgId(user.getOrganizationId());
        int i = qmsInspectionOrderMapper.insertUseGeneratedKeys(qmsInspectionOrder);

        //??????
        QmsHtInspectionOrder qmsHtInspectionOrder = new QmsHtInspectionOrder();
        BeanUtils.copyProperties(qmsInspectionOrder, qmsHtInspectionOrder);
        qmsHtInspectionOrderMapper.insertSelective(qmsHtInspectionOrder);

        //??????
        List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrder.getQmsInspectionOrderDets();
        if(StringUtils.isNotEmpty(qmsInspectionOrderDets)){
            for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets) {
                qmsInspectionOrderDet.setInspectionOrderId(qmsInspectionOrder.getInspectionOrderId());
                qmsInspectionOrderDet.setCreateUserId(user.getUserId());
                qmsInspectionOrderDet.setCreateTime(new Date());
                qmsInspectionOrderDet.setModifiedUserId(user.getUserId());
                qmsInspectionOrderDet.setModifiedTime(new Date());
                qmsInspectionOrderDet.setStatus(StringUtils.isEmpty(qmsInspectionOrderDet.getStatus())?1:qmsInspectionOrderDet.getStatus());
                qmsInspectionOrderDet.setOrgId(user.getOrganizationId());
            }
            qmsInspectionOrderDetMapper.insertList(qmsInspectionOrderDets);
        }

        //???????????????????????????
        if(StringUtils.isNotEmpty(qmsInspectionOrder.getInventoryIds())){
            String[] ids = qmsInspectionOrder.getInventoryIds().split(",");
            for(String id : ids){
                SearchWmsInnerInventory  searchWmsInnerInventory = new SearchWmsInnerInventory();
                searchWmsInnerInventory.setInventoryId(Long.valueOf(id).longValue());
                ResponseEntity<List<WmsInnerInventoryDto>> innerInventoryDtoList = innerFeignApi.findList(searchWmsInnerInventory);
                if(StringUtils.isEmpty(innerInventoryDtoList.getData())) throw new BizErrorException("??????????????????????????????");
                WmsInnerInventoryDto wmsInnerInventoryDto = innerInventoryDtoList.getData().get(0);
                wmsInnerInventoryDto.setQcLock((byte)1);
                wmsInnerInventoryDto.setInspectionOrderCode(qmsInspectionOrder.getInspectionOrderCode());
                innerFeignApi.update(wmsInnerInventoryDto);


                //???????????????????????????????????????
                SearchWmsInnerInventoryDet searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
                searchWmsInnerInventoryDet.setPageSize(999);
                searchWmsInnerInventoryDet.setStorageId(wmsInnerInventoryDto.getStorageId());
                searchWmsInnerInventoryDet.setMaterialId(wmsInnerInventoryDto.getMaterialId());
                searchWmsInnerInventoryDet.setInventoryStatusId(wmsInnerInventoryDto.getInventoryStatusId());
                searchWmsInnerInventoryDet.setIfInspectionOrderCodeNull(1);
                List<WmsInnerInventoryDetDto> inventoryDetDtos = innerFeignApi.findList(searchWmsInnerInventoryDet).getData();

                if(StringUtils.isNotEmpty(inventoryDetDtos)) {
                    for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : inventoryDetDtos) {
                        wmsInnerInventoryDetDto.setInspectionOrderCode(qmsInspectionOrder.getInspectionOrderCode());
                        innerFeignApi.update(wmsInnerInventoryDetDto);
                    }
                }

            }
        }
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int update(QmsInspectionOrder qmsInspectionOrder) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        qmsInspectionOrder.setModifiedUserId(user.getUserId());
        qmsInspectionOrder.setModifiedTime(new Date());
        qmsInspectionOrder.setOrgId(user.getOrganizationId());
        int i=qmsInspectionOrderMapper.updateByPrimaryKeySelective(qmsInspectionOrder);

        //??????
        QmsHtInspectionOrder qmsHtInspectionOrder = new QmsHtInspectionOrder();
        BeanUtils.copyProperties(qmsInspectionOrder, qmsHtInspectionOrder);
        qmsHtInspectionOrderMapper.insertSelective(qmsHtInspectionOrder);

        //???????????????????????????
        ArrayList<Long> idList = new ArrayList<>();
        List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrder.getQmsInspectionOrderDets();
        if(StringUtils.isNotEmpty(qmsInspectionOrderDets)) {
            for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets) {
                if (StringUtils.isNotEmpty(qmsInspectionOrderDet.getInspectionOrderDetId())) {
                    qmsInspectionOrderDetMapper.updateByPrimaryKeySelective(qmsInspectionOrderDet);
                    idList.add(qmsInspectionOrderDet.getInspectionOrderDetId());
                }
            }
        }

        //???????????????
        Example example1 = new Example(QmsInspectionOrderDet.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("inspectionOrderId", qmsInspectionOrder.getInspectionOrderId());
        if (idList.size() > 0) {
            criteria1.andNotIn("inspectionOrderDetId", idList);
        }
        List<QmsInspectionOrderDet> qmsInspectionOrderDets1 = qmsInspectionOrderDetMapper.selectByExample(example1);
        if (StringUtils.isNotEmpty(qmsInspectionOrderDets1)) {
            for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets1) {
                //???????????????????????????
                Example example2 = new Example(QmsInspectionOrderDetSample.class);
                Example.Criteria criteria2 = example2.createCriteria();
                criteria2.andEqualTo("inspectionOrderDetId", qmsInspectionOrderDet.getInspectionOrderDetId());
                qmsInspectionOrderDetSampleMapper.deleteByExample(example2);
            }
        }
        //????????????????????????
        qmsInspectionOrderDetMapper.deleteByExample(example1);

        //???????????????????????????
        List<QmsInspectionOrderDet> qmsInspectionOrderDetList = qmsInspectionOrder.getQmsInspectionOrderDets();
        if (StringUtils.isNotEmpty(qmsInspectionOrderDetList)) {
            for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDetList) {
                if (idList.contains(qmsInspectionOrderDet.getInspectionOrderDetId())) {
                    continue;
                }
                qmsInspectionOrderDet.setInspectionOrderId(qmsInspectionOrder.getInspectionOrderId());
                qmsInspectionOrderDet.setCreateUserId(user.getUserId());
                qmsInspectionOrderDet.setCreateTime(new Date());
                qmsInspectionOrderDet.setModifiedUserId(user.getUserId());
                qmsInspectionOrderDet.setModifiedTime(new Date());
                qmsInspectionOrderDet.setOrgId(user.getOrganizationId());
                qmsInspectionOrderDetMapper.insert(qmsInspectionOrderDet);
            }
        }

        //?????????????????????????????????
        this.writeBack(qmsInspectionOrder.getInspectionOrderId());

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int writeBack(Long inspectionOrderId){
        Example example = new Example(QmsInspectionOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("inspectionOrderId",inspectionOrderId);
        List<QmsInspectionOrderDet> qmsInspectionOrderDetList = qmsInspectionOrderDetMapper.selectByExample(example);

        //??????????????????????????????????????????
        int i = 0;
        int qualifiedCount = 0;
        int unqualifiedCount = 0;
        int inspectionCount = 0;
        int mustInspectionCount = 0;
        for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDetList){
            if(StringUtils.isNotEmpty(qmsInspectionOrderDet.getInspectionResult())){
                if(qmsInspectionOrderDet.getInspectionResult()==(byte)0){
                    unqualifiedCount++;
                }else {
                    qualifiedCount++;
                }
            }

            if(qmsInspectionOrderDet.getIfMustInspection()==(byte)1){
                mustInspectionCount++;
            }

            if(StringUtils.isNotEmpty(qmsInspectionOrderDet.getInspectionResult())
                    &&qmsInspectionOrderDet.getIfMustInspection()==(byte)1){
                inspectionCount++;
            }
        }

        if(inspectionCount == mustInspectionCount){
            QmsInspectionOrder qmsInspectionOrder = new QmsInspectionOrder();
            qmsInspectionOrder.setInspectionOrderId(inspectionOrderId);
            qmsInspectionOrder.setInspectionStatus((byte) 3);
            qmsInspectionOrder.setInspectionResult(unqualifiedCount==0 ? (byte)1 : (byte)0);

            this.handleInventory(qmsInspectionOrder.getInspectionOrderCode(),qmsInspectionOrder.getInspectionResult());

            i = qmsInspectionOrderMapper.updateByPrimaryKeySelective(qmsInspectionOrder);
        }

        return i;
    }

    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int handleInventory(String inspectionOrderCode,Byte inspectionResult){
        //???????????????????????????
        SearchWmsInnerInventory  searchWmsInnerInventory = new SearchWmsInnerInventory();
        searchWmsInnerInventory.setInspectionOrderCode(inspectionOrderCode);
        ResponseEntity<List<WmsInnerInventoryDto>> innerInventoryDtoList = innerFeignApi.findList(searchWmsInnerInventory);
        if(StringUtils.isEmpty(innerInventoryDtoList.getData())){
            throw new BizErrorException("??????????????????????????????");
        }
        WmsInnerInventoryDto wmsInnerInventoryDto = new WmsInnerInventoryDto();
        BeanUtils.copyProperties(innerInventoryDtoList.getData().get(0),wmsInnerInventoryDto);


        SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
        searchBaseInventoryStatus.setInventoryStatusName(inspectionResult==0 ? "?????????" : "??????");
        searchBaseInventoryStatus.setNameQueryMark(1);
        List<BaseInventoryStatus> inventoryStatusList = baseFeignApi.findList(searchBaseInventoryStatus).getData();
        if(StringUtils.isEmpty(inventoryStatusList)){
            throw new BizErrorException("??????????????????????????????");
        }

        //????????????
        Map<String,Object> map = new HashMap<>();
        map.put("materialId",wmsInnerInventoryDto.getMaterialId());
        map.put("warehouseId",wmsInnerInventoryDto.getWarehouseId());
        map.put("storageId",wmsInnerInventoryDto.getStorageId());
        map.put("batchCode",wmsInnerInventoryDto.getBatchCode());
        map.put("inventoryStatusId",inventoryStatusList.get(0).getInventoryStatusId());
        map.put("jobStatus",1);
        map.put("lockStatus",0);
        map.put("qcLock",0);
        map.put("stockLock",0);
        WmsInnerInventory wmsInnerInventory = innerFeignApi.selectOneByExample(map).getData();
        if (StringUtils.isNotEmpty(wmsInnerInventory)) {
            WmsInnerInventoryDto innerInventoryDto = new WmsInnerInventoryDto();
            BeanUtils.copyProperties(wmsInnerInventory,innerInventoryDto);
            innerInventoryDto.setQcLock((byte)0);
            innerInventoryDto.setPackingQty(innerInventoryDto.getPackingQty().add(wmsInnerInventoryDto.getPackingQty()));
            innerFeignApi.update(innerInventoryDto);
            wmsInnerInventoryDto.setPackingQty(BigDecimal.ZERO);
            innerFeignApi.update(wmsInnerInventoryDto);
        }else{
            wmsInnerInventoryDto.setQcLock((byte)0);
            wmsInnerInventoryDto.setInventoryStatusId(inventoryStatusList.get(0).getInventoryStatusId());
            innerFeignApi.update(wmsInnerInventoryDto);
        }

        //?????????????????????????????????
        SearchWmsInnerInventoryDet  searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
        searchWmsInnerInventoryDet.setPageSize(999);
        searchWmsInnerInventoryDet.setInspectionOrderCode(inspectionOrderCode);
        List<WmsInnerInventoryDetDto> inventoryDetDtos = innerFeignApi.findList(searchWmsInnerInventoryDet).getData();
        if(StringUtils.isNotEmpty(inventoryDetDtos)) {
            for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : inventoryDetDtos) {
                wmsInnerInventoryDetDto.setInventoryStatusId(inventoryStatusList.get(0).getInventoryStatusId());
                wmsInnerInventoryDetDto.setQcDate(new Date());
                innerFeignApi.update(wmsInnerInventoryDetDto);
            }
        }

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<QmsHtInspectionOrder> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            QmsInspectionOrder qmsInspectionOrder = qmsInspectionOrderMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(qmsInspectionOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            QmsHtInspectionOrder qmsHtInspectionOrder = new QmsHtInspectionOrder();
            BeanUtils.copyProperties(qmsInspectionOrder, qmsHtInspectionOrder);
            list.add(qmsHtInspectionOrder);

            Example example1 = new Example(QmsInspectionOrderDet.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("inspectionOrderId", qmsInspectionOrder.getInspectionOrderId());
            List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetMapper.selectByExample(example1);
            for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets){
                //???????????????????????????
                Example example2 = new Example(QmsInspectionOrderDetSample.class);
                Example.Criteria criteria2 = example2.createCriteria();
                criteria2.andEqualTo("inspectionOrderDetId", qmsInspectionOrderDet.getInspectionOrderDetId());
                qmsInspectionOrderDetSampleMapper.deleteByExample(example2);
            }
            //?????????????????????
            qmsInspectionOrderDetMapper.deleteByExample(example1);

        }
        //??????
        qmsHtInspectionOrderMapper.insertList(list);

        return qmsInspectionOrderMapper.deleteByIds(ids);
    }

    public Map<String, List<WmsInnerInventoryDetDto>> groupInventoryDet(List<WmsInnerInventoryDetDto> innerInventoryDetDtos){
        //?????????1???PO???????????????PO????????????????????????
        // 2???PO???????????????????????????????????????????????????????????????????????????
        // 3???PO?????????????????????????????????????????????????????????
        Map<String, List<WmsInnerInventoryDetDto>> collect = new HashMap<>();
        for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : innerInventoryDetDtos) {
            if (StringUtils.isEmpty(wmsInnerInventoryDetDto.getOption4())) {
                boolean tag = false;
                if(StringUtils.isNotEmpty(wmsInnerInventoryDetDto.getOption3())) {
                    SearchOmSalesOrderDetDto searchOmSalesOrderDetDto = new SearchOmSalesOrderDetDto();
                    searchOmSalesOrderDetDto.setSalesCode(wmsInnerInventoryDetDto.getOption3());
                    List<OmSalesOrderDetDto> salesOrderDetDtos = oMFeignApi.findList(searchOmSalesOrderDetDto).getData();
                    if (StringUtils.isNotEmpty(salesOrderDetDtos)) {
                        wmsInnerInventoryDetDto.setSalesOrderCode(salesOrderDetDtos.get(0).getSalesOrderCode());
                        List<WmsInnerInventoryDetDto> inventoryDetDtos = new LinkedList<>();
                        if (collect.containsKey(wmsInnerInventoryDetDto.getSalesOrderCode())) {
                            inventoryDetDtos = collect.get(wmsInnerInventoryDetDto.getSalesOrderCode());
                        }
                        inventoryDetDtos.add(wmsInnerInventoryDetDto);
                        collect.put(wmsInnerInventoryDetDto.getSalesOrderCode(), inventoryDetDtos);
                    }else {
                        tag = true;
                    }
                }else {
                    tag = true;
                }

                if(tag) {
                    List<WmsInnerInventoryDetDto> inventoryDetDtos = new LinkedList<>();
                    if (collect.containsKey("null")) {
                        inventoryDetDtos = collect.get("null");
                    }
                    inventoryDetDtos.add(wmsInnerInventoryDetDto);
                    collect.put("null", inventoryDetDtos);
                }
            } else {
                List<WmsInnerInventoryDetDto> inventoryDetDtos = new LinkedList<>();
                if (collect.containsKey(wmsInnerInventoryDetDto.getOption4())) {
                    inventoryDetDtos = collect.get(wmsInnerInventoryDetDto.getOption4());
                }
                inventoryDetDtos.add(wmsInnerInventoryDetDto);
                collect.put(wmsInnerInventoryDetDto.getOption4(), inventoryDetDtos);
            }
        }

        return collect;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int autoAdd() {
        System.out.println("===========???????????????????????????????????????============");
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        //?????????????????????
        SearchWmsInAsnOrder searchWmsInAsnOrder = new SearchWmsInAsnOrder();
        searchWmsInAsnOrder.setOrderStatus((byte) 3);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -1);
        Date date = calendar.getTime();
        searchWmsInAsnOrder.setStartTime(DateUtils.getDateString(date));
        searchWmsInAsnOrder.setEndTime(DateUtils.getDateString(date));
        searchWmsInAsnOrder.setOrderTypeName("????????????");
        searchWmsInAsnOrder.setOrgId(user.getOrganizationId());
        List<WmsInAsnOrderDto> wmsInAsnOrderList = inFeignApi.findList(searchWmsInAsnOrder).getData();

        if (StringUtils.isNotEmpty(wmsInAsnOrderList)) {
            for (WmsInAsnOrderDto wmsInAsnOrderDto : wmsInAsnOrderList) {
                SearchWmsInnerInventoryDet searchInnerInventoryDet = new SearchWmsInnerInventoryDet();
                searchInnerInventoryDet.setPageSize(999);
                searchInnerInventoryDet.setAsnCode(wmsInAsnOrderDto.getAsnCode());
                List<WmsInnerInventoryDetDto> innerInventoryDetDtos = innerFeignApi.findList(searchInnerInventoryDet).getData();

                //???????????????PO????????????????????????
                Map<String, List<WmsInnerInventoryDetDto>> collect = groupInventoryDet(innerInventoryDetDtos);

                //???????????????
                if (StringUtils.isNotEmpty(wmsInAsnOrderDto.getWmsInAsnOrderDetList())) {
                    for (WmsInAsnOrderDet wmsInAsnOrderDet : wmsInAsnOrderDto.getWmsInAsnOrderDetList()) {
                        SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
                        searchBaseInventoryStatus.setInventoryStatusId(wmsInAsnOrderDet.getInventoryStatusId());
                        List<BaseInventoryStatus> inventoryStatuses = baseFeignApi.findList(searchBaseInventoryStatus).getData();
                        if (StringUtils.isNotEmpty(inventoryStatuses) && "??????".equals(inventoryStatuses.get(0).getInventoryStatusName())) {
                            Set<String> codes = collect.keySet();
                            for (String code : codes) {
                                List<WmsInnerInventoryDetDto> inventoryDetDtos = collect.get(code);
                                List<WmsInnerInventoryDetDto> detDtos = inventoryDetDtos.stream().filter(li -> li.getMaterialId().equals(wmsInAsnOrderDet.getMaterialId())).collect(Collectors.toList());

                                QmsInspectionOrder qmsInspectionOrder = new QmsInspectionOrder();
                                qmsInspectionOrder.setMaterialId(wmsInAsnOrderDet.getMaterialId());
                                qmsInspectionOrder.setOrderQty(new BigDecimal(detDtos.size()));
                                qmsInspectionOrder.setInspectionStatus((byte) 1);

                                SearchBaseInspectionWay searchBaseInspectionWay = new SearchBaseInspectionWay();
                                searchBaseInspectionWay.setInspectionWayDesc("??????");
                                searchBaseInspectionWay.setQueryMark(1);
                                List<BaseInspectionWay> inspectionWays = baseFeignApi.findList(searchBaseInspectionWay).getData();
                                if (StringUtils.isEmpty(inspectionWays)) {
                                    throw new BizErrorException("??????????????????????????????");
                                }
                                qmsInspectionOrder.setInspectionWayId(inspectionWays.get(0).getInspectionWayId());

                                SearchOmSalesOrderDto searchOmSalesOrderDto = new SearchOmSalesOrderDto();
                                searchOmSalesOrderDto.setWorkOrderId(wmsInAsnOrderDet.getSourceOrderId());
                                List<OmSalesOrderDto> salesOrderDtoList = oMFeignApi.findList(searchOmSalesOrderDto).getData();
                                if (StringUtils.isNotEmpty(salesOrderDtoList)) {
                                    qmsInspectionOrder.setCustomerId(salesOrderDtoList.get(0).getSupplierId());
                                }

                                SearchBaseInspectionStandard searchBaseInspectionStandard = new SearchBaseInspectionStandard();
                                searchBaseInspectionStandard.setMaterialId(wmsInAsnOrderDet.getMaterialId());
                                searchBaseInspectionStandard.setSupplierId(qmsInspectionOrder.getCustomerId());
                                List<BaseInspectionStandard> inspectionStandardList = baseFeignApi.findList(searchBaseInspectionStandard).getData();
                                if (StringUtils.isEmpty(inspectionStandardList)) {
                                    throw new BizErrorException("?????????????????????");
                                }
                                qmsInspectionOrder.setInspectionStandardId(inspectionStandardList.get(0).getInspectionStandardId());

                                //??????
                                List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetService.showOrderDet(qmsInspectionOrder.getInspectionStandardId(), qmsInspectionOrder.getOrderQty());
                                qmsInspectionOrder.setQmsInspectionOrderDets(qmsInspectionOrderDets);
                                this.save(qmsInspectionOrder);

                                //???????????????????????????????????????
                                if (StringUtils.isNotEmpty(detDtos)) {
                                    for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : detDtos) {
                                        wmsInnerInventoryDetDto.setInspectionOrderCode(qmsInspectionOrder.getInspectionOrderCode());
                                        innerFeignApi.update(wmsInnerInventoryDetDto);
                                    }
                                }

                                //?????????????????????????????????
                                SearchWmsInnerInventory searchWmsInnerInventory = new SearchWmsInnerInventory();
                                //searchWmsInnerInventory.setRelevanceOrderCode(wmsInAsnOrderDto.getAsnCode());
                                searchWmsInnerInventory.setStorageId(detDtos.get(0).getStorageId());
                                searchWmsInnerInventory.setInventoryStatusId(detDtos.get(0).getInventoryStatusId());
                                searchWmsInnerInventory.setMaterialId(detDtos.get(0).getMaterialId());
                                searchWmsInnerInventory.setQcLock((byte) 0);
                                List<WmsInnerInventoryDto> list = innerFeignApi.findList(searchWmsInnerInventory).getData();
                                if (StringUtils.isEmpty(list)) {
                                    throw new BizErrorException("????????????????????????");
                                }
                                WmsInnerInventoryDto wmsInnerInventory = list.get(0);

                                //?????????
                                WmsInnerInventoryDto newInnerInventory = new WmsInnerInventoryDto();
                                BeanUtils.copyProperties(wmsInnerInventory, newInnerInventory);
                                newInnerInventory.setInventoryId(null);
                                newInnerInventory.setPackingQty(new BigDecimal(detDtos.size()));
                                newInnerInventory.setQcLock((byte) 1);
                                newInnerInventory.setInspectionOrderCode(qmsInspectionOrder.getInspectionOrderCode());
                                innerFeignApi.add(newInnerInventory);

                                wmsInnerInventory.setPackingQty(wmsInnerInventory.getPackingQty().subtract(new BigDecimal(detDtos.size())));
                                innerFeignApi.update(wmsInnerInventory);
                            }
                        }
                    }
                }
            }
        }
        return 1;
    }



}
