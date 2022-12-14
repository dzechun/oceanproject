package com.fantechs.provider.guest.wanbao.service.impl;

import com.alibaba.fastjson.JSON;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseTabDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcBarcodeProcessDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcess;
import com.fantechs.common.base.general.dto.om.OmSalesCodeReSpcDto;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDetDto;
import com.fantechs.common.base.general.dto.om.SearchOmSalesOrderDetDto;
import com.fantechs.common.base.general.dto.wms.inner.*;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.*;
import com.fantechs.common.base.general.entity.om.search.SearchOmSalesCodeReSpc;
import com.fantechs.common.base.general.entity.wanbao.QmsInspectionOrder;
import com.fantechs.common.base.general.entity.wanbao.QmsInspectionOrderDet;
import com.fantechs.common.base.general.entity.wanbao.QmsInspectionOrderDetSample;
import com.fantechs.common.base.general.entity.wanbao.history.QmsHtInspectionOrder;
import com.fantechs.common.base.general.entity.wanbao.search.SearchQmsInspectionOrder;
import com.fantechs.common.base.general.entity.wanbao.search.SearchQmsInspectionOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrderDet;
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
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.in.InFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.guest.wanbao.mapper.QmsHtInspectionOrderMapper;
import com.fantechs.provider.guest.wanbao.mapper.QmsInspectionOrderDetMapper;
import com.fantechs.provider.guest.wanbao.mapper.QmsInspectionOrderDetSampleMapper;
import com.fantechs.provider.guest.wanbao.mapper.QmsInspectionOrderMapper;
import com.fantechs.provider.guest.wanbao.service.QmsInspectionOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by leifengzhi on 2021/05/25.
 */
@Service
@Slf4j
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
    private SecurityFeignApi securityFeignApi;
    @Resource
    private QmsInspectionOrderDetServiceImpl qmsInspectionOrderDetService;

    @Override
    public List<QmsInspectionOrder> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        List<QmsInspectionOrder> qmsInspectionOrders = qmsInspectionOrderMapper.findList(map);
        if(StringUtils.isNotEmpty(qmsInspectionOrders)&&!"0".equals(map.get("ifContainDet"))){
            SearchQmsInspectionOrderDet searchQmsInspectionOrderDet = new SearchQmsInspectionOrderDet();
            for (QmsInspectionOrder qmsInspectionOrder : qmsInspectionOrders){
                searchQmsInspectionOrderDet.setInspectionOrderId(qmsInspectionOrder.getInspectionOrderId());
                List<QmsInspectionOrderDet> detList = qmsInspectionOrderDetMapper.findDetList(ControllerUtil.dynamicConditionByEntity(searchQmsInspectionOrderDet));
                qmsInspectionOrder.setQmsInspectionOrderDets(detList);
            }
        }

        return qmsInspectionOrders;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int exemption(QmsInspectionOrder qmsInspectionOrder) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(QmsInspectionOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("inspectionOrderId", qmsInspectionOrder.getInspectionOrderId());
        List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetMapper.selectByExample(example);

        Example example1 = new Example(QmsInspectionOrderDetSample.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("inspectionOrderId", qmsInspectionOrder.getInspectionOrderId());
        List<QmsInspectionOrderDetSample> qmsInspectionOrderDetSamples = qmsInspectionOrderDetSampleMapper.selectByExample(example1);
        for (QmsInspectionOrderDetSample DetSample : qmsInspectionOrderDetSamples) {
            DetSample.setBarcodeStatus((byte) 1);
            DetSample.setModifiedUserId(user.getUserId());
            DetSample.setModifiedTime(new Date());
            qmsInspectionOrderDetSampleMapper.updateByPrimaryKeySelective(DetSample);
        }

        List<QmsInspectionOrderDetSample> inspectionOrderDetSampleList = new LinkedList<>();
        for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets) {
            //??????
            qmsInspectionOrderDet.setBadnessQty(BigDecimal.ZERO);
            qmsInspectionOrderDet.setInspectionResult((byte) 1);
            qmsInspectionOrderDetMapper.updateByPrimaryKeySelective(qmsInspectionOrderDet);

            //??????
            /*for(QmsInspectionOrderDetSample qmsInspectionOrderDetSample : qmsInspectionOrderDetSamples){
                QmsInspectionOrderDetSample inspectionOrderDetSample = new QmsInspectionOrderDetSample();
                inspectionOrderDetSample.setInspectionOrderDetId(qmsInspectionOrderDet.getInspectionOrderDetId());
                inspectionOrderDetSample.setBarcode(qmsInspectionOrderDetSample.getBarcode());
                inspectionOrderDetSample.setBarcodeStatus((byte)1);
                inspectionOrderDetSample.setSampleValue("OK");
                inspectionOrderDetSample.setOrgId(user.getOrganizationId());
                inspectionOrderDetSampleList.add(inspectionOrderDetSample);
            }*/
        }
        if (StringUtils.isNotEmpty(inspectionOrderDetSampleList)) {
            qmsInspectionOrderDetSampleMapper.insertList(inspectionOrderDetSampleList);
        }

        qmsInspectionOrder.setInspectionStatus((byte) 3);
        qmsInspectionOrder.setInspectionResult((byte) 1);
        qmsInspectionOrder.setInspectionUserId(user.getUserId());
        qmsInspectionOrder.setInspectionType((byte) 1);
        int i = qmsInspectionOrderMapper.updateByPrimaryKeySelective(qmsInspectionOrder);

        //????????????:????????????????????????
        SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
        searchBaseInventoryStatus.setInventoryStatusName("??????");
        searchBaseInventoryStatus.setNameQueryMark(1);
        List<BaseInventoryStatus> inventoryStatus = baseFeignApi.findList(searchBaseInventoryStatus).getData();
        if (StringUtils.isEmpty(inventoryStatus))
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "??????????????????????????????");

        //????????????
        SearchWmsInnerInventoryDet searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
        searchWmsInnerInventoryDet.setPageSize(999);
        searchWmsInnerInventoryDet.setInspectionOrderCode(qmsInspectionOrder.getInspectionOrderCode());
        List<WmsInnerInventoryDetDto> inventoryDetDtos = innerFeignApi.findList(searchWmsInnerInventoryDet).getData();
        if (StringUtils.isNotEmpty(inventoryDetDtos)) {
            for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : inventoryDetDtos) {
                wmsInnerInventoryDetDto.setInventoryStatusId(inventoryStatus.get(0).getInventoryStatusId());
                wmsInnerInventoryDetDto.setQcDate(new Date());
                innerFeignApi.update(wmsInnerInventoryDetDto);
            }
        }

        //??????
        SearchWmsInnerInventory searchWmsInnerInventory = new SearchWmsInnerInventory();
        searchWmsInnerInventory.setInspectionOrderCode(qmsInspectionOrder.getInspectionOrderCode());
        searchWmsInnerInventory.setQcLock((byte) 1);
        List<WmsInnerInventoryDto> innerInventoryDtoList = innerFeignApi.findList(searchWmsInnerInventory).getData();
        if (StringUtils.isEmpty(innerInventoryDtoList)) {
            throw new BizErrorException("??????????????????????????????");
        }
        WmsInnerInventoryDto wmsInnerInventoryDto = innerInventoryDtoList.get(0);

        //????????????
        SearchWmsInnerInventory sWmsInnerInventory = new SearchWmsInnerInventory();
        sWmsInnerInventory.setMaterialId(wmsInnerInventoryDto.getMaterialId());
        sWmsInnerInventory.setWarehouseId(wmsInnerInventoryDto.getWarehouseId());
        sWmsInnerInventory.setStorageId(wmsInnerInventoryDto.getStorageId());
        sWmsInnerInventory.setBatchCode(wmsInnerInventoryDto.getBatchCode());
        sWmsInnerInventory.setInventoryStatusId(inventoryStatus.get(0).getInventoryStatusId());
        sWmsInnerInventory.setJobStatus((byte) 1);
        sWmsInnerInventory.setLockStatus((byte) 0);
        sWmsInnerInventory.setQcLock((byte) 0);
        sWmsInnerInventory.setStockLock((byte) 0);
        List<WmsInnerInventoryDto> inventoryDtos = innerFeignApi.findList(sWmsInnerInventory).getData();

        /*Map<String,Object> map = new HashMap<>();
        map.put("materialId",wmsInnerInventoryDto.getMaterialId());
        map.put("warehouseId",wmsInnerInventoryDto.getWarehouseId());
        map.put("storageId",wmsInnerInventoryDto.getStorageId());
        map.put("batchCode",wmsInnerInventoryDto.getBatchCode());
        map.put("inventoryStatusId",inventoryStatus.get(0).getInventoryStatusId());
        map.put("jobStatus",1);
        map.put("lockStatus",0);
        map.put("qcLock",0);
        map.put("stockLock",0);
        WmsInnerInventory wmsInnerInventory = innerFeignApi.selectOneByExample(map).getData();*/
        if (StringUtils.isNotEmpty(inventoryDtos)) {
            WmsInnerInventoryDto wmsInnerInventory = inventoryDtos.get(0);
            WmsInnerInventoryDto innerInventoryDto = new WmsInnerInventoryDto();
            BeanUtils.copyProperties(wmsInnerInventory, innerInventoryDto);
            innerInventoryDto.setQcLock((byte) 0);
            innerInventoryDto.setPackingQty(innerInventoryDto.getPackingQty().add(wmsInnerInventoryDto.getPackingQty()));
            innerFeignApi.update(innerInventoryDto);
            wmsInnerInventoryDto.setPackingQty(BigDecimal.ZERO);
            innerFeignApi.update(wmsInnerInventoryDto);
        } else {
            wmsInnerInventoryDto.setQcLock((byte) 0);
            wmsInnerInventoryDto.setInventoryStatusId(inventoryStatus.get(0).getInventoryStatusId());
            innerFeignApi.update(wmsInnerInventoryDto);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSampleQty(Long inspectionOrderId, BigDecimal sampleQty) {
        int i = 0;
        Example example = new Example(QmsInspectionOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("inspectionOrderId", inspectionOrderId);
        List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetMapper.selectByExample(example);

        for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets) {
            qmsInspectionOrderDet.setSampleQty(sampleQty);
            i += qmsInspectionOrderDetMapper.updateByPrimaryKeySelective(qmsInspectionOrderDet);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int recheck(Long inspectionOrderId) {
        QmsInspectionOrder qmsInspectionOrder = qmsInspectionOrderMapper.selectByPrimaryKey(inspectionOrderId);
        qmsInspectionOrder.setRecheckStatus((byte) 2);
        qmsInspectionOrderMapper.updateByPrimaryKeySelective(qmsInspectionOrder);

        Example example = new Example(QmsInspectionOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("inspectionOrderId", inspectionOrderId);
        List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetMapper.selectByExample(example);

        List<Long> detIds = new LinkedList<>();
        for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets) {
            detIds.add(qmsInspectionOrderDet.getInspectionOrderDetId());
        }

        Example example1 = new Example(QmsInspectionOrderDetSample.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andIn("inspectionOrderDetId", detIds);
        qmsInspectionOrderDetSampleMapper.deleteByExample(example1);

        //return batchQualified(inspectionOrderId);
        return recheckBatchQualified(inspectionOrderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int batchQualified(Long inspectionOrderId) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        QmsInspectionOrder qmsInspectionOrder = qmsInspectionOrderMapper.selectByPrimaryKey(inspectionOrderId);

        Example example = new Example(QmsInspectionOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("inspectionOrderId", inspectionOrderId);
        List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetMapper.selectByExample(example);
        //log.info("============= ??????????????????" + JSON.toJSONString(qmsInspectionOrderDets));

        Example example1 = new Example(QmsInspectionOrderDetSample.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("inspectionOrderId", inspectionOrderId);
        List<QmsInspectionOrderDetSample> qmsInspectionOrderDetSamples = qmsInspectionOrderDetSampleMapper.selectByExample(example1);
        log.info("============= ??????????????????" + JSON.toJSONString(qmsInspectionOrderDetSamples));

        for (QmsInspectionOrderDetSample DetSample : qmsInspectionOrderDetSamples) {
            DetSample.setBarcodeStatus((byte) 1);
            DetSample.setModifiedUserId(user.getUserId());
            DetSample.setModifiedTime(new Date());
            qmsInspectionOrderDetSampleMapper.updateByPrimaryKeySelective(DetSample);
        }

        List<QmsInspectionOrderDetSample> inspectionOrderDetSampleList = new LinkedList<>();
        for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets) {
            //??????
            qmsInspectionOrderDet.setUnitName("???");
            qmsInspectionOrderDet.setBadnessQty(BigDecimal.ZERO);
            qmsInspectionOrderDet.setInspectionResult((byte) 1);
            qmsInspectionOrderDetMapper.updateByPrimaryKeySelective(qmsInspectionOrderDet);

            //??????
            for (QmsInspectionOrderDetSample qmsInspectionOrderDetSample : qmsInspectionOrderDetSamples) {
                QmsInspectionOrderDetSample inspectionOrderDetSample = new QmsInspectionOrderDetSample();
                inspectionOrderDetSample.setInspectionOrderDetId(qmsInspectionOrderDet.getInspectionOrderDetId());
                inspectionOrderDetSample.setBarcode(qmsInspectionOrderDetSample.getBarcode());
                inspectionOrderDetSample.setBarcodeStatus((byte) 1);
                inspectionOrderDetSample.setSampleValue("OK");
                inspectionOrderDetSample.setOrgId(user.getOrganizationId());
                inspectionOrderDetSampleList.add(inspectionOrderDetSample);
            }
        }
        if (StringUtils.isNotEmpty(inspectionOrderDetSampleList)) {
            qmsInspectionOrderDetSampleMapper.insertList(inspectionOrderDetSampleList);
        }

        qmsInspectionOrder.setInspectionStatus((byte) 3);
        qmsInspectionOrder.setInspectionResult((byte) 1);
        qmsInspectionOrder.setInspectionUserId(user.getUserId());
        int i = qmsInspectionOrderMapper.updateByPrimaryKeySelective(qmsInspectionOrder);

        //????????????
        this.handleInventory(qmsInspectionOrder.getInspectionOrderCode(), qmsInspectionOrder.getInspectionResult());

        //???????????????
        createJobOrderShift(qmsInspectionOrderDetSamples, qmsInspectionOrder, user);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int recheckBatchQualified(Long inspectionOrderId) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        Long warehouseId = null;
        QmsInspectionOrder qmsInspectionOrder = qmsInspectionOrderMapper.selectByPrimaryKey(inspectionOrderId);
        String inspectionOrderCode = qmsInspectionOrder.getInspectionOrderCode();

        Example example = new Example(QmsInspectionOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("inspectionOrderId", inspectionOrderId);
        List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetMapper.selectByExample(example);

        Example example1 = new Example(QmsInspectionOrderDetSample.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("inspectionOrderId", inspectionOrderId);
        List<QmsInspectionOrderDetSample> qmsInspectionOrderDetSamples = qmsInspectionOrderDetSampleMapper.selectByExample(example1);
        for (QmsInspectionOrderDetSample DetSample : qmsInspectionOrderDetSamples) {
            DetSample.setBarcodeStatus((byte) 1);
            DetSample.setModifiedUserId(user.getUserId());
            DetSample.setModifiedTime(new Date());
            qmsInspectionOrderDetSampleMapper.updateByPrimaryKeySelective(DetSample);
        }

        List<QmsInspectionOrderDetSample> inspectionOrderDetSampleList = new LinkedList<>();
        for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets) {
            //??????
            qmsInspectionOrderDet.setBadnessQty(BigDecimal.ZERO);
            qmsInspectionOrderDet.setInspectionResult((byte) 1);
            qmsInspectionOrderDetMapper.updateByPrimaryKeySelective(qmsInspectionOrderDet);

            //??????
            for (QmsInspectionOrderDetSample qmsInspectionOrderDetSample : qmsInspectionOrderDetSamples) {
                QmsInspectionOrderDetSample inspectionOrderDetSample = new QmsInspectionOrderDetSample();
                inspectionOrderDetSample.setInspectionOrderDetId(qmsInspectionOrderDet.getInspectionOrderDetId());
                inspectionOrderDetSample.setBarcode(qmsInspectionOrderDetSample.getBarcode());
                inspectionOrderDetSample.setBarcodeStatus((byte) 1);
                inspectionOrderDetSample.setSampleValue("OK");
                inspectionOrderDetSample.setOrgId(user.getOrganizationId());
                inspectionOrderDetSampleList.add(inspectionOrderDetSample);
            }
        }
        if (StringUtils.isNotEmpty(inspectionOrderDetSampleList)) {
            qmsInspectionOrderDetSampleMapper.insertList(inspectionOrderDetSampleList);
        }

        qmsInspectionOrder.setInspectionStatus((byte) 3);
        qmsInspectionOrder.setInspectionResult((byte) 1);
        qmsInspectionOrder.setInspectionUserId(user.getUserId());
        int i = qmsInspectionOrderMapper.updateByPrimaryKeySelective(qmsInspectionOrder);

        SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
        searchBaseInventoryStatus.setOrgId(user.getOrganizationId());
        searchBaseInventoryStatus.setInventoryStatusName("??????");
        searchBaseInventoryStatus.setNameQueryMark(1);
        List<BaseInventoryStatus> inventoryStatus = baseFeignApi.findList(searchBaseInventoryStatus).getData();
        if (StringUtils.isEmpty(inventoryStatus))
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "??????????????????????????????");

        SearchWmsInnerInventoryDet searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
        searchWmsInnerInventoryDet.setPageSize(999);
        searchWmsInnerInventoryDet.setInspectionOrderCode(inspectionOrderCode);
        List<WmsInnerInventoryDetDto> inventoryDetDtos = innerFeignApi.findList(searchWmsInnerInventoryDet).getData();
        if (StringUtils.isNotEmpty(inventoryDetDtos)) {
            for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : inventoryDetDtos) {
                wmsInnerInventoryDetDto.setInventoryStatusId(inventoryStatus.get(0).getInventoryStatusId());
                wmsInnerInventoryDetDto.setQcDate(new Date());
                innerFeignApi.update(wmsInnerInventoryDetDto);
            }
        }

        //???????????????????????????
        SearchWmsInnerInventory searchWmsInnerInventory = new SearchWmsInnerInventory();
        searchWmsInnerInventory.setInspectionOrderCode(inspectionOrderCode);
        searchWmsInnerInventory.setQcLock((byte) 1);
        ResponseEntity<List<WmsInnerInventoryDto>> innerInventoryDtoList = innerFeignApi.findList(searchWmsInnerInventory);
        if (StringUtils.isEmpty(innerInventoryDtoList.getData())) {
            throw new BizErrorException("??????????????????????????????");
        }

        List<WmsInnerInventoryDto> inventoryDtoList = innerInventoryDtoList.getData();
        for (WmsInnerInventoryDto innerInventoryDto : inventoryDtoList) {
            innerInventoryDto.setQcLock((byte) 0);
            innerInventoryDto.setInventoryStatusId(inventoryStatus.get(0).getInventoryStatusId());
            innerInventoryDto.setModifiedTime(new Date());
            innerFeignApi.update(innerInventoryDto);
        }

        //???????????????
        createJobOrderShift(qmsInspectionOrderDetSamples, qmsInspectionOrder, user);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int batchSubmit(Long inspectionOrderId) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        QmsInspectionOrder qmsInspectionOrder = qmsInspectionOrderMapper.selectByPrimaryKey(inspectionOrderId);

        Example example = new Example(QmsInspectionOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("inspectionOrderId", inspectionOrderId);
        List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetMapper.selectByExample(example);

        Example example1 = new Example(QmsInspectionOrderDetSample.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("inspectionOrderId", inspectionOrderId);
        List<QmsInspectionOrderDetSample> qmsInspectionOrderDetSamples = qmsInspectionOrderDetSampleMapper.selectByExample(example1);
        for (QmsInspectionOrderDetSample qmsInspectionOrderDetSample : qmsInspectionOrderDetSamples) {
            //???barcodeStatus????????????????????????
            if (StringUtils.isEmpty(qmsInspectionOrderDetSample.getBarcodeStatus())) {
                qmsInspectionOrderDetSample.setBarcodeStatus((byte) 1);
                qmsInspectionOrderDetSampleMapper.updateByPrimaryKeySelective(qmsInspectionOrderDetSample);
            }
        }

        List<QmsInspectionOrderDetSample> inspectionOrderDetSampleList = new LinkedList<>();
        for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets) {
            //??????
            qmsInspectionOrderDet.setUnitName("???");//??????????????????????????????
            if (StringUtils.isEmpty(qmsInspectionOrderDet.getInspectionResult())) {
                qmsInspectionOrderDet.setBadnessQty(BigDecimal.ZERO);
                qmsInspectionOrderDet.setInspectionResult((byte) 1);
                qmsInspectionOrderDetMapper.updateByPrimaryKeySelective(qmsInspectionOrderDet);

                //??????
                for (QmsInspectionOrderDetSample qmsInspectionOrderDetSample : qmsInspectionOrderDetSamples) {
                    QmsInspectionOrderDetSample inspectionOrderDetSample = new QmsInspectionOrderDetSample();
                    inspectionOrderDetSample.setInspectionOrderDetId(qmsInspectionOrderDet.getInspectionOrderDetId());
                    inspectionOrderDetSample.setBarcode(qmsInspectionOrderDetSample.getBarcode());
                    inspectionOrderDetSample.setSampleValue("OK");
                    inspectionOrderDetSample.setOrgId(user.getOrganizationId());
                    inspectionOrderDetSampleList.add(inspectionOrderDetSample);
                }
            }
        }
        if (StringUtils.isNotEmpty(inspectionOrderDetSampleList)) {
            qmsInspectionOrderDetSampleMapper.insertList(inspectionOrderDetSampleList);
        }


        List<QmsInspectionOrderDetSample> unQualifiedBarcodes = qmsInspectionOrderDetSamples.stream().filter(item -> item.getBarcodeStatus() != null && item.getBarcodeStatus() == 0).collect(Collectors.toList());
        if (qmsInspectionOrderDetSamples.size() == unQualifiedBarcodes.size()) {
            qmsInspectionOrder.setInspectionResult((byte) 0);
        } else if (unQualifiedBarcodes.size() == 0) {
            qmsInspectionOrder.setInspectionResult((byte) 1);
        } else {
            qmsInspectionOrder.setInspectionResult((byte) 2);
        }

        qmsInspectionOrder.setInspectionStatus((byte) 3);
        qmsInspectionOrder.setInspectionUserId(user.getUserId());
        int i = qmsInspectionOrderMapper.updateByPrimaryKeySelective(qmsInspectionOrder);

        //????????????
        if (qmsInspectionOrder.getInspectionResult() == (byte) 0 || qmsInspectionOrder.getInspectionResult() == (byte) 1) {
            this.handleInventory(qmsInspectionOrder.getInspectionOrderCode(), qmsInspectionOrder.getInspectionResult());
        } else if (qmsInspectionOrder.getInspectionResult() == (byte) 2) {
            this.splitInventory(qmsInspectionOrder.getInspectionOrderCode(), unQualifiedBarcodes);
        }

        //???????????????
        createJobOrderShift(qmsInspectionOrderDetSamples, qmsInspectionOrder, user);

        return i;
    }

    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int createJobOrderShift(List<QmsInspectionOrderDetSample> list, QmsInspectionOrder qmsInspectionOrder, SysUser user) {
        int i = 1;
        String barcode = list.get(0).getFactoryBarcode();
        Long materialId = qmsInspectionOrder.getMaterialId();
        String proCode = null;
        Long inStorageId = null;
        Long outStorageId = null;
        Long warehouseId = null;
        Long proLineId = null;
        Byte shiftType = 2;
        String orderCode = qmsInspectionOrder.getInspectionOrderCode();

        SearchBaseTab searchBaseTab = new SearchBaseTab();
        searchBaseTab.setMaterialId(materialId);
        List<BaseTabDto> tabList = baseFeignApi.findTabList(searchBaseTab).getData();

        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setRelatedOrderCode(orderCode);
        searchWmsInnerJobOrder.setOption1("qmsCommit");
        List<WmsInnerJobOrderDto> jobOrderDtoList = innerFeignApi.findList(searchWmsInnerJobOrder).getData();
        if (StringUtils.isNotEmpty(jobOrderDtoList) && jobOrderDtoList.size() > 0) {
            //???????????? ???????????????????????????????????????
            List<QmsInspectionOrderDetSample> ngQualifiedBarcodes = list.stream().filter(item -> item.getBarcodeStatus() != null && item.getBarcodeStatus() == 0).collect(Collectors.toList());
            BigDecimal ngQty = new BigDecimal(ngQualifiedBarcodes.size());
            innerFeignApi.updateShit(jobOrderDtoList.get(0).getJobOrderId(), ngQty);

            log.info("============= ?????????????????????==========================");

        } else {
            //??????????????????????????????
            SaveShiftWorkDetDto saveShiftWorkDetDto = new SaveShiftWorkDetDto();
            List<String> barcodes = new ArrayList<>();
            Long sourceDetId = null;//????????????????????????????????????????????????ID
            for (QmsInspectionOrderDetSample item : list) {
                barcodes.add(item.getBarcode());
            }
            saveShiftWorkDetDto.setBarcodes(barcodes);
            saveShiftWorkDetDto.setMaterialQty(new BigDecimal(list.size()));

            //????????????????????????
            SaveShiftJobOrderDto saveShiftJobOrderDto = new SaveShiftJobOrderDto();

            //???????????????????????????????????????
            searchWmsInnerJobOrder.setOption1("qmsToInnerJobShift");
            jobOrderDtoList = innerFeignApi.findList(searchWmsInnerJobOrder).getData();
            if (StringUtils.isNotEmpty(jobOrderDtoList) && jobOrderDtoList.size() > 0) {
                saveShiftWorkDetDto.setJobOrderId(jobOrderDtoList.get(0).getJobOrderId());

                SearchWmsInnerJobOrderDet sDet = new SearchWmsInnerJobOrderDet();
                sDet.setJobOrderId(jobOrderDtoList.get(0).getJobOrderId());
                List<WmsInnerJobOrderDetDto> detDtoList = innerFeignApi.findList(sDet).getData();
                if (StringUtils.isNotEmpty(detDtoList) && detDtoList.size() > 0) {
                    inStorageId = detDtoList.get(0).getOutStorageId();
                    outStorageId = detDtoList.get(0).getInStorageId();
                    sourceDetId = detDtoList.get(0).getSourceDetId();

                    saveShiftWorkDetDto.setJobOrderDetId(detDtoList.get(0).getJobOrderDetId());
                    saveShiftWorkDetDto.setMaterialId(detDtoList.get(0).getMaterialId());
                    saveShiftWorkDetDto.setStorageId(detDtoList.get(0).getOutStorageId());

                    saveShiftJobOrderDto.setJobOrderDetId(detDtoList.get(0).getJobOrderDetId());
                    saveShiftJobOrderDto.setStorageId(detDtoList.get(0).getInStorageId());

                    SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
                    searchBaseStorage.setStorageId(inStorageId);
                    List<BaseStorage> storageDtoList = baseFeignApi.findList(searchBaseStorage).getData();
                    if (StringUtils.isNotEmpty(storageDtoList) && storageDtoList.size() > 0) {
                        warehouseId = storageDtoList.get(0).getWarehouseId();

                        saveShiftWorkDetDto.setWarehouseId(warehouseId);
                    }

                    log.info("============= ???????????????????????????==========================");
                }
            }
            //????????????
            SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
            searchBaseInventoryStatus.setWarehouseId(warehouseId);
            searchBaseInventoryStatus.setOrgId(user.getOrganizationId());
            List<BaseInventoryStatus> inventoryStatusList = baseFeignApi.findList(searchBaseInventoryStatus).getData();

            SearchMesSfcBarcodeProcess sBarcodeProcess = new SearchMesSfcBarcodeProcess();
            sBarcodeProcess.setBarcode(barcode);
            sBarcodeProcess.setOrgId(user.getOrganizationId());
            List<MesSfcBarcodeProcessDto> processDtos = sfcFeignApi.findList(sBarcodeProcess).getData();
            if (StringUtils.isNotEmpty(processDtos) && processDtos.size() > 0) {
                //proCode=processDtos.get(0).getProCode();
                proLineId = processDtos.get(0).getProLineId();
            }
            if (StringUtils.isNotEmpty(proLineId)) {
                BaseProLine baseProLine = baseFeignApi.getProLineDetail(proLineId).getData();
                if (StringUtils.isNotEmpty(baseProLine)) {
                    proCode = baseProLine.getProCode();
                }
            }

            if (StringUtils.isNotEmpty(proCode) && proCode.contains("A")) {
                String storageCode = "Z-SX";
                SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
                searchBaseStorage.setStorageCode(storageCode);
                List<BaseStorage> storageDtoList = baseFeignApi.findList(searchBaseStorage).getData();
                if (StringUtils.isEmpty(storageDtoList) || storageDtoList.size() <= 0) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "?????????????????????????????????");
                }
                inStorageId = storageDtoList.get(0).getStorageId();
                warehouseId = storageDtoList.get(0).getWarehouseId();
                shiftType = 3;
            }

            //??????????????? ????????????????????? ??????????????????????????????
            if (StringUtils.isNotEmpty(sourceDetId) && qmsInspectionOrder.getInspectionResult() == (byte) 2) {
                List<BaseInventoryStatus> statusList = inventoryStatusList.stream().filter(item -> item.getInventoryStatusName().equals("??????")).collect(Collectors.toList());
                SearchWmsInnerInventory searchWmsInnerInventory = new SearchWmsInnerInventory();
                searchWmsInnerInventory.setInventoryId(sourceDetId);
                List<WmsInnerInventoryDto> innerInventoryDtoList = innerFeignApi.findList(searchWmsInnerInventory).getData();
                WmsInnerInventoryDto wmsInnerInventoryDto = new WmsInnerInventoryDto();
                if (StringUtils.isNotEmpty(innerInventoryDtoList) && innerInventoryDtoList.size() > 0) {
                    wmsInnerInventoryDto = innerInventoryDtoList.get(0);
                    SearchWmsInnerInventory searchWmsInnerInventory1 = new SearchWmsInnerInventory();
                    searchWmsInnerInventory1.setWarehouseId(wmsInnerInventoryDto.getWarehouseId());
                    searchWmsInnerInventory1.setStorageId(wmsInnerInventoryDto.getStorageId());
                    searchWmsInnerInventory1.setMaterialId(wmsInnerInventoryDto.getMaterialId());
                    searchWmsInnerInventory1.setBatchCode(wmsInnerInventoryDto.getBatchCode());
                    searchWmsInnerInventory1.setJobStatus((byte) 1);
                    searchWmsInnerInventory1.setStockLock((byte) 0);
                    searchWmsInnerInventory1.setQcLock((byte) 0);
                    searchWmsInnerInventory1.setLockStatus((byte) 0);
                    searchWmsInnerInventory1.setInspectionOrderCode(wmsInnerInventoryDto.getInspectionOrderCode());
                    searchWmsInnerInventory1.setInventoryStatusId(wmsInnerInventoryDto.getInventoryStatusId());
                    List<WmsInnerInventoryDto> innerInventoryDtoList1 = innerFeignApi.findList(searchWmsInnerInventory1).getData();
                    if (StringUtils.isNotEmpty(innerInventoryDtoList1) && innerInventoryDtoList1.size() > 0) {
                        WmsInnerInventoryDto wmsInnerInventoryDto1 = new WmsInnerInventoryDto();
                        wmsInnerInventoryDto1 = innerInventoryDtoList1.get(0);
                        wmsInnerInventoryDto1.setPackingQty(wmsInnerInventoryDto1.getPackingQty() != null ? wmsInnerInventoryDto1.getPackingQty().add(wmsInnerInventoryDto.getPackingQty()) : wmsInnerInventoryDto.getPackingQty());
                        wmsInnerInventoryDto1.setModifiedTime(new Date());
                        innerFeignApi.update(wmsInnerInventoryDto1);

                        //
                        wmsInnerInventoryDto.setPackingQty(BigDecimal.ZERO);
                        wmsInnerInventoryDto.setModifiedTime(new Date());
                        wmsInnerInventoryDto.setInspectionOrderCode(null);
                        innerFeignApi.update(wmsInnerInventoryDto);
                    } else {
                        wmsInnerInventoryDto.setInventoryStatusId(statusList.get(0).getInventoryStatusId());
                        wmsInnerInventoryDto.setQcLock((byte) 0);
                        wmsInnerInventoryDto.setModifiedTime(new Date());
                        innerFeignApi.update(wmsInnerInventoryDto);
                    }

                }

            }

            //????????????????????????????????? ????????????
            Long inventoryId = null;
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("autoConfirmShift");
            List<SysSpecItem> sysSpecItemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            log.info("============= ?????????????????????" + JSON.toJSONString(sysSpecItemList));

            if (StringUtils.isNotEmpty(sysSpecItemList) && sysSpecItemList.size() > 0) {
                SysSpecItem sysSpecItem = sysSpecItemList.get(0);
                if (sysSpecItem.getParaValue().equals("1") && jobOrderDtoList.get(0).getOrderStatus() == (byte) 3) {
                    log.info("============= ?????????????????????????????????==================" + JSON.toJSONString(saveShiftWorkDetDto));
                    innerFeignApi.saveShiftWorkDetBarcode(saveShiftWorkDetDto);
                    log.info("============= ???????????????????????????==================" + JSON.toJSONString(saveShiftJobOrderDto));
                    //innerFeignApi.saveJobOrder(saveShiftJobOrderDto);
                    ResponseEntity<Long> entityLong = innerFeignApi.saveJobOrderReturnId(saveShiftJobOrderDto);
                    log.info("===================????????????================" + entityLong.toString());
                    if (entityLong.getCode() != 0) {
                        throw new BizErrorException(entityLong.getMessage());
                    }
                    inventoryId = entityLong.getData();
                }
            }


            /*SearchWmsInnerInventory searchWmsInnerInventory = new SearchWmsInnerInventory();
            searchWmsInnerInventory.setMaterialId(materialId);
            searchWmsInnerInventory.setStorageId(outStorageId);
            searchWmsInnerInventory.setLockStatus((byte) 0);
            searchWmsInnerInventory.setJobStatus((byte) 1);
            searchWmsInnerInventory.setInspectionOrderCode(orderCode);
            List<WmsInnerInventoryDto> inventoryDtos = innerFeignApi.findList(searchWmsInnerInventory).getData();
            log.info("============= ????????????????????????" + JSON.toJSONString(searchWmsInnerInventory));
            log.info("============= ??????????????????" + JSON.toJSONString(inventoryDtos));*/

            if (StringUtils.isNotEmpty(inventoryId)) {
                //List<WmsInnerInventoryDto> dtoList=inventoryDtos.stream().filter(item -> item.getPackingQty() != null && item.getPackingQty().compareTo(new BigDecimal(0))==1).collect(Collectors.toList());

                //log.info("============= ????????????????????????????????????" + JSON.toJSONString(dtoList));
                log.info("============= ?????????????????????????????????ID-->" + inventoryId.toString());

                log.info("======================== ????????????????????????????????????=========================");

                //???????????????????????????????????????
                List<QmsInspectionOrderDetSample> ngQualifiedBarcodes = list.stream().filter(item -> item.getBarcodeStatus() != null && item.getBarcodeStatus() == 0).collect(Collectors.toList());
                List<QmsInspectionOrderDetSample> goodQualifiedBarcodes = list.stream().filter(item -> item.getBarcodeStatus() == null || item.getBarcodeStatus() != 0).collect(Collectors.toList());
                BigDecimal ngQty = new BigDecimal(ngQualifiedBarcodes.size());
                BigDecimal goodQty = new BigDecimal(goodQualifiedBarcodes.size());

                WmsInnerJobOrder wmsInnerJobOrder = new WmsInnerJobOrder();
                wmsInnerJobOrder.setOrderStatus((byte) 3);
                wmsInnerJobOrder.setJobOrderType((byte) 2);
                wmsInnerJobOrder.setShiftType(shiftType);
                wmsInnerJobOrder.setWarehouseId(warehouseId);
                wmsInnerJobOrder.setStatus((byte) 1);
                wmsInnerJobOrder.setOrgId(user.getOrganizationId());
                wmsInnerJobOrder.setRelatedOrderCode(orderCode);
                wmsInnerJobOrder.setSourceOrderId(qmsInspectionOrder.getInspectionOrderId());
                wmsInnerJobOrder.setOption1("qmsCommit");

                List<WmsInnerJobOrderDet> detList = new LinkedList<>();
                if (ngQty.compareTo(new BigDecimal(0)) == 1) {
                    List<BaseInventoryStatus> statusList = inventoryStatusList.stream().filter(item -> item.getInventoryStatusName().equals("?????????")).collect(Collectors.toList());
                    WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
                    wmsInnerJobOrderDet.setMaterialId(materialId);
                    wmsInnerJobOrderDet.setPlanQty(ngQty);
                    wmsInnerJobOrderDet.setDistributionQty(ngQty);
                    wmsInnerJobOrderDet.setOutStorageId(outStorageId);
                    wmsInnerJobOrderDet.setInStorageId(inStorageId);
//                    if(dtoList.size()>0) {
//                        wmsInnerJobOrderDet.setSourceDetId(dtoList.get(0).getInventoryId());
//                    }
                    wmsInnerJobOrderDet.setSourceDetId(inventoryId);
                    wmsInnerJobOrderDet.setOrderStatus((byte) 3);
                    if (statusList.size() > 0) {
                        wmsInnerJobOrderDet.setInventoryStatusId(statusList.get(0).getInventoryStatusId());
                    }
                    if (StringUtils.isNotEmpty(tabList) && tabList.size() > 0) {
                        wmsInnerJobOrderDet.setPackingUnitName(tabList.get(0).getMainUnit());
                    }

                    detList.add(wmsInnerJobOrderDet);

                    log.info("============= ????????????" + JSON.toJSONString(ngQty));
                }
                if (goodQty.compareTo(new BigDecimal(0)) == 1) {
                    List<BaseInventoryStatus> statusList = inventoryStatusList.stream().filter(item -> item.getInventoryStatusName().equals("??????")).collect(Collectors.toList());
                    WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
                    wmsInnerJobOrderDet.setMaterialId(materialId);
                    wmsInnerJobOrderDet.setPlanQty(goodQty);
                    wmsInnerJobOrderDet.setDistributionQty(goodQty);
                    wmsInnerJobOrderDet.setOutStorageId(outStorageId);
                    wmsInnerJobOrderDet.setInStorageId(inStorageId);
//                    if(dtoList.size()>0) {
//                        wmsInnerJobOrderDet.setSourceDetId(dtoList.get(0).getInventoryId());
//                    }
                    wmsInnerJobOrderDet.setSourceDetId(inventoryId);
                    wmsInnerJobOrderDet.setOrderStatus((byte) 3);
                    if (statusList.size() > 0) {
                        wmsInnerJobOrderDet.setInventoryStatusId(statusList.get(0).getInventoryStatusId());
                    }
                    if (StringUtils.isNotEmpty(tabList) && tabList.size() > 0) {
                        wmsInnerJobOrderDet.setPackingUnitName(tabList.get(0).getMainUnit());
                    }

                    detList.add(wmsInnerJobOrderDet);

                    log.info("============= ????????????" + JSON.toJSONString(goodQty));
                }

                log.info("============= ????????????" + JSON.toJSONString(detList));

                wmsInnerJobOrder.setWmsInPutawayOrderDets(detList);
                ResponseEntity responseEntity = innerFeignApi.add(wmsInnerJobOrder);
                if (responseEntity.getCode() != 0) {
                    throw new BizErrorException("???????????????????????????");
                }

                log.info("======================== ????????????????????????????????????=========================");


            }
        }

        return i;
    }

    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int splitInventory(String inspectionOrderCode, List<QmsInspectionOrderDetSample> unQualifiedBarcodes) {
        SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
        searchBaseInventoryStatus.setInventoryStatusName("?????????");
        searchBaseInventoryStatus.setNameQueryMark(1);
        List<BaseInventoryStatus> inventoryStatusList1 = baseFeignApi.findList(searchBaseInventoryStatus).getData();
        if (StringUtils.isEmpty(inventoryStatusList1)) {
            throw new BizErrorException("??????????????????????????????");
        }

        searchBaseInventoryStatus.setInventoryStatusName("??????");
        List<BaseInventoryStatus> inventoryStatusList2 = baseFeignApi.findList(searchBaseInventoryStatus).getData();
        if (StringUtils.isEmpty(inventoryStatusList2)) {
            throw new BizErrorException("??????????????????????????????");
        }

        //?????????????????????????????????
        int unQualifiedCount = 0;
        SearchWmsInnerInventoryDet searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
        searchWmsInnerInventoryDet.setPageSize(999);
        searchWmsInnerInventoryDet.setInspectionOrderCode(inspectionOrderCode);
        List<WmsInnerInventoryDetDto> inventoryDetDtos = innerFeignApi.findList(searchWmsInnerInventoryDet).getData();
        if (StringUtils.isNotEmpty(inventoryDetDtos)) {
            for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : inventoryDetDtos) {
                wmsInnerInventoryDetDto.setInventoryStatusId(inventoryStatusList2.get(0).getInventoryStatusId());
                for (QmsInspectionOrderDetSample qmsInspectionOrderDetSample : unQualifiedBarcodes) {
                    if (wmsInnerInventoryDetDto.getBarcode().equals(qmsInspectionOrderDetSample.getFactoryBarcode())) {
                        wmsInnerInventoryDetDto.setInventoryStatusId(inventoryStatusList1.get(0).getInventoryStatusId());
                        unQualifiedCount++;
                    }
                }
                wmsInnerInventoryDetDto.setQcDate(new Date());
                innerFeignApi.update(wmsInnerInventoryDetDto);
            }
        }

        //???????????????????????????
        SearchWmsInnerInventory searchWmsInnerInventory = new SearchWmsInnerInventory();
        searchWmsInnerInventory.setJobStatus((byte) 1);
        searchWmsInnerInventory.setInspectionOrderCode(inspectionOrderCode);
        ResponseEntity<List<WmsInnerInventoryDto>> innerInventoryDtoList = innerFeignApi.findList(searchWmsInnerInventory);
        if (StringUtils.isEmpty(innerInventoryDtoList.getData())) {
            throw new BizErrorException("??????????????????????????????");
        }
        WmsInnerInventoryDto wmsInnerInventoryDto = new WmsInnerInventoryDto();
        BeanUtils.copyProperties(innerInventoryDtoList.getData().get(0), wmsInnerInventoryDto);
        WmsInnerInventoryDto innerInventoryDto = new WmsInnerInventoryDto();
        BeanUtils.copyProperties(innerInventoryDtoList.getData().get(0), innerInventoryDto);

        //?????????????????????
        /*Map<String,Object> map = new HashMap<>();
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
        }*/

        //????????????????????????
        /*wmsInnerInventoryDto.setQcLock((byte)0);
        wmsInnerInventoryDto.setInventoryStatusId(inventoryStatusList1.get(0).getInventoryStatusId());
        wmsInnerInventoryDto.setPackingQty(new BigDecimal(unQualifiedCount));
        innerFeignApi.update(wmsInnerInventoryDto);*/

        //??????????????????
        BigDecimal qty = innerInventoryDto.getPackingQty().subtract(new BigDecimal(unQualifiedCount));
        /*map.put("inventoryStatusId",inventoryStatusList2.get(0).getInventoryStatusId());
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
        }*/

        innerInventoryDto.setInventoryId(null);
        innerInventoryDto.setQcLock((byte) 0);
        innerInventoryDto.setInventoryStatusId(inventoryStatusList2.get(0).getInventoryStatusId());
        //innerInventoryDto.setPackingQty(qty);
        //??????????????? ????????????????????????????????????
        innerInventoryDto.setPackingQty(BigDecimal.ZERO);
        innerFeignApi.add(innerInventoryDto);

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int audit(QmsInspectionOrder qmsInspectionOrder) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        qmsInspectionOrder.setAuditUserId(user.getUserId());
        qmsInspectionOrder.setAuditDeptId(user.getDeptId());
        qmsInspectionOrder.setModifiedUserId(user.getUserId());
        qmsInspectionOrder.setModifiedTime(new Date());
        return qmsInspectionOrderMapper.updateByPrimaryKeySelective(qmsInspectionOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int thirdInspection(QmsInspectionOrder qmsInspectionOrder) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //handleInventory(qmsInspectionOrder.getInspectionOrderCode(),qmsInspectionOrder.getInspectionResult());

        qmsInspectionOrder.setIfThirdInspection(StringUtils.isEmpty(qmsInspectionOrder.getIfThirdInspection()) ? 1 : qmsInspectionOrder.getIfThirdInspection());
        //qmsInspectionOrder.setInspectionStatus((byte)3);
        qmsInspectionOrder.setInspectionType((byte) 2);
        //qmsInspectionOrder.setInspectionUserId(user.getUserId());
        qmsInspectionOrder.setModifiedUserId(user.getUserId());
        qmsInspectionOrder.setModifiedTime(new Date());
        return qmsInspectionOrderMapper.updateByPrimaryKeySelective(qmsInspectionOrder);
    }

    @Override
    public QmsInspectionOrder selectByKey(Long key) {
        Map<String, Object> map = new HashMap<>();
        map.put("inspectionOrderId", key);
        QmsInspectionOrder qmsInspectionOrder = qmsInspectionOrderMapper.findList(map).get(0);
        if (StringUtils.isNotEmpty(qmsInspectionOrder)) {
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
        int i = 0;
        //???????????????????????????
        List<WmsInnerInventoryDetDto> wmsInnerInventoryDetDtos = new LinkedList<>();
        List<WmsInnerInventoryDto> wmsInnerInventoryDtos = new LinkedList<>();
        if (StringUtils.isNotEmpty(qmsInspectionOrder.getInventoryIds())) {
            String[] ids = qmsInspectionOrder.getInventoryIds().split(",");
            for (String id : ids) {
                SearchWmsInnerInventory searchWmsInnerInventory = new SearchWmsInnerInventory();
                searchWmsInnerInventory.setInventoryId(Long.valueOf(id).longValue());
                ResponseEntity<List<WmsInnerInventoryDto>> innerInventoryDtoList = innerFeignApi.findList(searchWmsInnerInventory);
                if (StringUtils.isEmpty(innerInventoryDtoList.getData())) throw new BizErrorException("??????????????????????????????");
                WmsInnerInventoryDto wmsInnerInventoryDto = innerInventoryDtoList.getData().get(0);
                wmsInnerInventoryDtos.add(wmsInnerInventoryDto);

                //???????????????????????????????????????
                SearchWmsInnerInventoryDet searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
                searchWmsInnerInventoryDet.setPageSize(999);
                searchWmsInnerInventoryDet.setStorageId(wmsInnerInventoryDto.getStorageId());
                searchWmsInnerInventoryDet.setMaterialId(wmsInnerInventoryDto.getMaterialId());
                searchWmsInnerInventoryDet.setInventoryStatusId(wmsInnerInventoryDto.getInventoryStatusId());
                searchWmsInnerInventoryDet.setIfInspectionOrderCodeNull(1);
                List<WmsInnerInventoryDetDto> inventoryDetDtos = innerFeignApi.findList(searchWmsInnerInventoryDet).getData();
                wmsInnerInventoryDetDtos.addAll(inventoryDetDtos);
            }
            if (StringUtils.isEmpty(wmsInnerInventoryDetDtos)) {
                throw new BizErrorException("??????????????????????????????");
            }

            //???????????????PO????????????????????????
            Map<String, List<WmsInnerInventoryDetDto>> collect = groupInventoryDet(wmsInnerInventoryDetDtos);
            Set<String> codes = collect.keySet();
            for (String code : codes) {
                List<WmsInnerInventoryDetDto> detDtos = collect.get(code);
                qmsInspectionOrder.setOrderQty(new BigDecimal(detDtos.size()));
                //??????
                List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetService.showOrderDet(qmsInspectionOrder.getInspectionStandardId(), qmsInspectionOrder.getOrderQty());
                qmsInspectionOrder.setQmsInspectionOrderDets(qmsInspectionOrderDets);
                i += this.add(qmsInspectionOrder);

                //???????????????????????????????????????
                if (StringUtils.isNotEmpty(detDtos)) {
                    for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : detDtos) {
                        wmsInnerInventoryDetDto.setInspectionOrderCode(qmsInspectionOrder.getInspectionOrderCode());
                        innerFeignApi.update(wmsInnerInventoryDetDto);
                    }
                }

                //???????????????
                Map<String, List<WmsInnerInventoryDetDto>> map = new HashMap<>();
                for (WmsInnerInventoryDetDto detDto : detDtos) {
                    String groupCode = detDto.getMaterialId() + "_" + detDto.getStorageId() + "_" + detDto.getInventoryStatusId();
                    List<WmsInnerInventoryDetDto> inventoryDetDtoList = new LinkedList<>();
                    if (map.containsKey(groupCode)) {
                        inventoryDetDtoList = map.get(groupCode);
                    }
                    inventoryDetDtoList.add(detDto);
                    map.put(groupCode, inventoryDetDtoList);
                }

                Set<String> keySet = map.keySet();
                for (String s : keySet) {
                    List<WmsInnerInventoryDetDto> detDtoList = map.get(s);

                    //?????????????????????????????????
                    for (WmsInnerInventoryDto wmsInnerInventory : wmsInnerInventoryDtos) {
                        if (detDtoList.get(0).getStorageId().equals(wmsInnerInventory.getStorageId())
                                && detDtoList.get(0).getInventoryStatusId().equals(wmsInnerInventory.getInventoryStatusId())
                                && detDtoList.get(0).getMaterialId().equals(wmsInnerInventory.getMaterialId())) {
                            //?????????
                            WmsInnerInventoryDto newInnerInventory = new WmsInnerInventoryDto();
                            BeanUtils.copyProperties(wmsInnerInventory, newInnerInventory);
                            newInnerInventory.setInventoryId(null);
                            newInnerInventory.setPackingQty(new BigDecimal(detDtoList.size()));
                            newInnerInventory.setQcLock((byte) 1);
                            newInnerInventory.setInspectionOrderCode(qmsInspectionOrder.getInspectionOrderCode());
                            innerFeignApi.add(newInnerInventory);

                            wmsInnerInventory.setPackingQty(wmsInnerInventory.getPackingQty().subtract(new BigDecimal(detDtoList.size())));
                            innerFeignApi.update(wmsInnerInventory);
                        }
                    }
                }
            }

        }

        return i;
    }


    @Transactional(rollbackFor = Exception.class)
    public int add(QmsInspectionOrder qmsInspectionOrder) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        qmsInspectionOrder.setInspectionOrderCode(CodeUtils.getId("CPJY-"));
        qmsInspectionOrder.setCreateUserId(user.getUserId());
        qmsInspectionOrder.setCreateTime(new Date());
        qmsInspectionOrder.setModifiedUserId(user.getUserId());
        qmsInspectionOrder.setModifiedTime(new Date());
        qmsInspectionOrder.setAuditStatus(StringUtils.isEmpty(qmsInspectionOrder.getAuditStatus()) ? 0 : qmsInspectionOrder.getAuditStatus());
        qmsInspectionOrder.setStatus(StringUtils.isEmpty(qmsInspectionOrder.getStatus()) ? 1 : qmsInspectionOrder.getStatus());
        qmsInspectionOrder.setInspectionStatus((byte) 1);
        qmsInspectionOrder.setOrgId(user.getOrganizationId());
        int i = qmsInspectionOrderMapper.insertUseGeneratedKeys(qmsInspectionOrder);

        //??????
        QmsHtInspectionOrder qmsHtInspectionOrder = new QmsHtInspectionOrder();
        BeanUtils.copyProperties(qmsInspectionOrder, qmsHtInspectionOrder);
        qmsHtInspectionOrderMapper.insertSelective(qmsHtInspectionOrder);

        //??????
        List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrder.getQmsInspectionOrderDets();
        if (StringUtils.isNotEmpty(qmsInspectionOrderDets)) {
            for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets) {
                qmsInspectionOrderDet.setInspectionOrderId(qmsInspectionOrder.getInspectionOrderId());
                qmsInspectionOrderDet.setCreateUserId(user.getUserId());
                qmsInspectionOrderDet.setCreateTime(new Date());
                qmsInspectionOrderDet.setModifiedUserId(user.getUserId());
                qmsInspectionOrderDet.setModifiedTime(new Date());
                qmsInspectionOrderDet.setStatus(StringUtils.isEmpty(qmsInspectionOrderDet.getStatus()) ? 1 : qmsInspectionOrderDet.getStatus());
                qmsInspectionOrderDet.setOrgId(user.getOrganizationId());
            }
            qmsInspectionOrderDetMapper.insertList(qmsInspectionOrderDets);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int update(QmsInspectionOrder qmsInspectionOrder, Byte type) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        //??????????????????????????????
        Long jobOrderId = null;
        if (type != (byte) 0) {
            String inspectionOrderCode = qmsInspectionOrder.getInspectionOrderCode();
            SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
            searchWmsInnerJobOrder.setRelatedOrderCode(inspectionOrderCode);
            searchWmsInnerJobOrder.setCodeQueryMark(1);
            List<WmsInnerJobOrderDto> orderDtoList = innerFeignApi.findList(searchWmsInnerJobOrder).getData();
            if (StringUtils.isNotEmpty(orderDtoList) && orderDtoList.size() > 0) {
                if (orderDtoList.get(0).getOrderStatus() >= 4) {
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "???????????????????????????????????? ????????????-->" + orderDtoList.get(0).getJobOrderCode());
                }

                jobOrderId = orderDtoList.get(0).getJobOrderId();
            }
        }
        qmsInspectionOrder.setModifiedUserId(user.getUserId());
        qmsInspectionOrder.setModifiedTime(new Date());
        qmsInspectionOrder.setOrgId(user.getOrganizationId());
        int i = qmsInspectionOrderMapper.updateByPrimaryKeySelective(qmsInspectionOrder);

        //??????
        QmsHtInspectionOrder qmsHtInspectionOrder = new QmsHtInspectionOrder();
        BeanUtils.copyProperties(qmsInspectionOrder, qmsHtInspectionOrder);
        qmsHtInspectionOrderMapper.insertSelective(qmsHtInspectionOrder);

        //???????????????????????????
        ArrayList<Long> idList = new ArrayList<>();
        List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrder.getQmsInspectionOrderDets();
        if (StringUtils.isNotEmpty(qmsInspectionOrderDets)) {
            for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets) {
                if (StringUtils.isNotEmpty(qmsInspectionOrderDet.getInspectionOrderDetId())) {
                    qmsInspectionOrderDet.setSampleQty(StringUtils.isNotEmpty(qmsInspectionOrder.getSampleQty()) ? qmsInspectionOrder.getSampleQty() : qmsInspectionOrderDet.getSampleQty());
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
                qmsInspectionOrderDet.setSampleQty(StringUtils.isNotEmpty(qmsInspectionOrder.getSampleQty()) ? qmsInspectionOrder.getSampleQty() : qmsInspectionOrderDet.getSampleQty());
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
        /*if(type != 0) {
            this.writeBack(qmsInspectionOrder.getInspectionOrderId());
        }*/

        //??????????????? ???????????????????????????
        BigDecimal qty = qmsInspectionOrderDets.get(0).getSampleQty();
        if (StringUtils.isNotEmpty(jobOrderId) && StringUtils.isNotEmpty(qty) && type != (byte) 0) {
            innerFeignApi.reCreateInnerJobShift(jobOrderId, qty);
        }
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int writeBack(Long inspectionOrderId) {
        Example example = new Example(QmsInspectionOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("inspectionOrderId", inspectionOrderId);
        List<QmsInspectionOrderDet> qmsInspectionOrderDetList = qmsInspectionOrderDetMapper.selectByExample(example);

        //??????????????????????????????????????????
        int i = 0;
        int qualifiedCount = 0;
        int unqualifiedCount = 0;
        int inspectionCount = 0;
        int mustInspectionCount = 0;
        for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDetList) {
            if (StringUtils.isNotEmpty(qmsInspectionOrderDet.getInspectionResult())) {
                if (qmsInspectionOrderDet.getInspectionResult() == (byte) 0) {
                    unqualifiedCount++;
                } else {
                    qualifiedCount++;
                }
            }

            if (qmsInspectionOrderDet.getIfMustInspection() == (byte) 1) {
                mustInspectionCount++;
            }

            if (StringUtils.isNotEmpty(qmsInspectionOrderDet.getInspectionResult())
                    && qmsInspectionOrderDet.getIfMustInspection() == (byte) 1) {
                inspectionCount++;
            }
        }

        if (inspectionCount == mustInspectionCount) {
            QmsInspectionOrder qmsInspectionOrder = new QmsInspectionOrder();
            qmsInspectionOrder.setInspectionOrderId(inspectionOrderId);
            qmsInspectionOrder.setInspectionStatus((byte) 3);
            qmsInspectionOrder.setInspectionResult(unqualifiedCount == 0 ? (byte) 1 : (byte) 0);

            this.handleInventory(qmsInspectionOrder.getInspectionOrderCode(), qmsInspectionOrder.getInspectionResult());

            i = qmsInspectionOrderMapper.updateByPrimaryKeySelective(qmsInspectionOrder);
        }

        return i;
    }

    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int handleInventory(String inspectionOrderCode, Byte inspectionResult) {
        //???????????????????????????
        SearchWmsInnerInventory searchWmsInnerInventory = new SearchWmsInnerInventory();
        searchWmsInnerInventory.setInspectionOrderCode(inspectionOrderCode);
        //2022/03/23 ?????????????????????????????? ???????????????????????????????????????
        searchWmsInnerInventory.setQcLock((byte) 1);

        ResponseEntity<List<WmsInnerInventoryDto>> innerInventoryDtoList = innerFeignApi.findList(searchWmsInnerInventory);
        if (StringUtils.isEmpty(innerInventoryDtoList.getData())) {
            throw new BizErrorException("??????????????????????????????");
        }
//        WmsInnerInventoryDto wmsInnerInventoryDto = new WmsInnerInventoryDto();
//        BeanUtils.copyProperties(innerInventoryDtoList.getData().get(0),wmsInnerInventoryDto);

        SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
        searchBaseInventoryStatus.setInventoryStatusName(inspectionResult == 0 ? "?????????" : "??????");
        searchBaseInventoryStatus.setNameQueryMark(1);
        List<BaseInventoryStatus> inventoryStatusList = baseFeignApi.findList(searchBaseInventoryStatus).getData();
        if (StringUtils.isEmpty(inventoryStatusList)) {
            throw new BizErrorException("??????????????????????????????");
        }

        //????????????
        /*Map<String,Object> map = new HashMap<>();
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
        }*/

        List<WmsInnerInventoryDto> inventoryDtoList = innerInventoryDtoList.getData();
        for (WmsInnerInventoryDto innerInventoryDto : inventoryDtoList) {
            innerInventoryDto.setQcLock((byte) 0);
            innerInventoryDto.setInventoryStatusId(inventoryStatusList.get(0).getInventoryStatusId());
            innerInventoryDto.setModifiedTime(new Date());
            innerFeignApi.update(innerInventoryDto);
            log.info("???????????????id???" + innerInventoryDto.getInventoryId() + ",????????????id???" + innerInventoryDto.getInventoryStatusId());
        }

        //?????????????????????????????????
        SearchWmsInnerInventoryDet searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
        searchWmsInnerInventoryDet.setPageSize(999);
        searchWmsInnerInventoryDet.setInspectionOrderCode(inspectionOrderCode);
        List<WmsInnerInventoryDetDto> inventoryDetDtos = innerFeignApi.findList(searchWmsInnerInventoryDet).getData();
        if (StringUtils.isNotEmpty(inventoryDetDtos)) {
            for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : inventoryDetDtos) {
                wmsInnerInventoryDetDto.setInventoryStatusId(inventoryStatusList.get(0).getInventoryStatusId());
                wmsInnerInventoryDetDto.setQcDate(new Date());
                innerFeignApi.update(wmsInnerInventoryDetDto);
                log.info("????????????????????????" + wmsInnerInventoryDetDto.getBarcode() + ",????????????id???" + wmsInnerInventoryDetDto.getInventoryStatusId());
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
            if (StringUtils.isEmpty(qmsInspectionOrder)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            if (qmsInspectionOrder.getInspectionStatus() == (byte) 2 || qmsInspectionOrder.getInspectionStatus() == (byte) 3) {
                throw new BizErrorException("?????????????????????????????????????????????");
            }
            QmsHtInspectionOrder qmsHtInspectionOrder = new QmsHtInspectionOrder();
            BeanUtils.copyProperties(qmsInspectionOrder, qmsHtInspectionOrder);
            list.add(qmsHtInspectionOrder);

            Example example1 = new Example(QmsInspectionOrderDet.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("inspectionOrderId", qmsInspectionOrder.getInspectionOrderId());
            List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetMapper.selectByExample(example1);
            for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets) {
                //???????????????????????????
                Example example2 = new Example(QmsInspectionOrderDetSample.class);
                Example.Criteria criteria2 = example2.createCriteria();
                criteria2.andEqualTo("inspectionOrderDetId", qmsInspectionOrderDet.getInspectionOrderDetId());
                qmsInspectionOrderDetSampleMapper.deleteByExample(example2);
            }
            //?????????????????????
            qmsInspectionOrderDetMapper.deleteByExample(example1);

            //??????????????????????????????
            SearchWmsInnerInventoryDet searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
            searchWmsInnerInventoryDet.setPageSize(999);
            searchWmsInnerInventoryDet.setInspectionOrderCode(qmsInspectionOrder.getInspectionOrderCode());
            List<WmsInnerInventoryDetDto> inventoryDetDtos = innerFeignApi.findList(searchWmsInnerInventoryDet).getData();
            if (StringUtils.isNotEmpty(inventoryDetDtos)) {
                for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : inventoryDetDtos) {
                    wmsInnerInventoryDetDto.setInspectionOrderCode("");
                    innerFeignApi.update(wmsInnerInventoryDetDto);
                }
            }

            SearchWmsInnerInventory searchWmsInnerInventory = new SearchWmsInnerInventory();
            searchWmsInnerInventory.setPageSize(999);
            searchWmsInnerInventory.setInspectionOrderCode(qmsInspectionOrder.getInspectionOrderCode());
            List<WmsInnerInventoryDto> innerInventoryDtos = innerFeignApi.findList(searchWmsInnerInventory).getData();
            if (StringUtils.isNotEmpty(innerInventoryDtos)) {
                for (WmsInnerInventoryDto wmsInnerInventoryDto : innerInventoryDtos) {
                    wmsInnerInventoryDto.setInspectionOrderCode("");
                    innerFeignApi.update(wmsInnerInventoryDto);
                }
            }
        }
        //??????
        qmsHtInspectionOrderMapper.insertList(list);

        return qmsInspectionOrderMapper.deleteByIds(ids);
    }

    public Map<String, List<WmsInnerInventoryDetDto>> groupInventoryDet(List<WmsInnerInventoryDetDto> innerInventoryDetDtos) {
        //?????????1???PO???????????????PO????????????????????????
        // 2???PO???????????????????????????????????????????????????????????????????????????
        // 3???PO???????????????????????????????????????????????????????????????option4???PO??????option3??????????????????
        Map<String, List<WmsInnerInventoryDetDto>> collect = new HashMap<>();
        for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : innerInventoryDetDtos) {
            if (StringUtils.isEmpty(wmsInnerInventoryDetDto.getOption4())) {
                boolean tag = false;
                if (StringUtils.isNotEmpty(wmsInnerInventoryDetDto.getOption3())) {
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
                    } else {
                        tag = true;
                    }
                } else {
                    tag = true;
                }

                if (tag) {
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
    //   @LcnTransaction
    public int newAutoAdd() {
        long start = System.currentTimeMillis();
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        //???????????????????????????????????????
        SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
        searchBaseInventoryStatus.setOrgId(user.getOrganizationId());
        //    searchBaseInventoryStatus.setInventoryStatusName("??????");
        List<BaseInventoryStatus> inventoryStatus = baseFeignApi.findList(searchBaseInventoryStatus).getData();
        if (StringUtils.isEmpty(inventoryStatus)) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "?????????????????????????????????");
        }
        Long wait = null;
        Long qualified = null;
        Long noQualified = null;
        for (BaseInventoryStatus status : inventoryStatus) {
            if ("??????".equals(status.getInventoryStatusName())) {
                wait = status.getInventoryStatusId();
            }
            if ("??????".equals(status.getInventoryStatusName())) {
                qualified = status.getInventoryStatusId();
            }
            if ("?????????".equals(status.getInventoryStatusName())) {
                noQualified = status.getInventoryStatusId();
            }
        }

        long inventoryStatusTime = System.currentTimeMillis();
        log.info("============= ???????????????????????????" + (inventoryStatusTime - start));

        //??????????????????????????????????????????
        Map<String, Object> map = new HashMap<>();
        map.put("inventoryStatusId", wait);
        map.put("barcodeStatus", "3");
        map.put("logicCode", "C149");
        map.put("notEqualMark", 1);
        map.put("ifInspectionOrderCodeNull", 1);
        List<WmsInnerInventoryDetDto> wmsInnerInventoryDetDtos = qmsInspectionOrderMapper.findInventoryDetList(map);

        long inventoryDetsTime = System.currentTimeMillis();
        log.info("============= ???????????????????????????" + (inventoryDetsTime - inventoryStatusTime));

        //List<WmsInnerInventoryDetDto> innerInventoryDetDtos=wmsInnerInventoryDetDtos.stream().filter(item -> item.getInspectionOrderCode()==null).collect(Collectors.toList());
        //???????????????PO???????????????????????????????????????
        Map<String, List<WmsInnerInventoryDetDto>> collect = newGroupInventoryDet(wmsInnerInventoryDetDtos);

        long collectTime = System.currentTimeMillis();
        log.info("============= ???????????????PO????????????????????????????????????????????????" + (collectTime - inventoryDetsTime));

        if (StringUtils.isNotEmpty(collect)) {
            Set<String> codes = collect.keySet();
            for (String code : codes) {
                long earchTime = System.currentTimeMillis();
                List<WmsInnerInventoryDetDto> detDtos = collect.get(code);
                QmsInspectionOrder qmsInspectionOrder = new QmsInspectionOrder();
                if (StringUtils.isNotEmpty(detDtos)) {
                    SearchWmsInnerInventoryDet searchQualifiedInventoryDet = new SearchWmsInnerInventoryDet();
                    searchQualifiedInventoryDet.setMaterialCode(detDtos.get(0).getMaterialCode());
                    searchQualifiedInventoryDet.setStorageType((byte) 1);
                    searchQualifiedInventoryDet.setPageSize(99999);
                    SearchQmsInspectionOrder searchQmsInspectionOrder = new SearchQmsInspectionOrder();
                    searchQmsInspectionOrder.setMaterialCode(detDtos.get(0).getMaterialCode());
                    searchQmsInspectionOrder.setPageSize(99999);
                    if (StringUtils.isNotEmpty(detDtos.get(0).getOption3())) {
                        searchQmsInspectionOrder.setSalesCode(detDtos.get(0).getOption3());
                        searchQualifiedInventoryDet.setOption3(detDtos.get(0).getOption3());
                    }
                    if (StringUtils.isNotEmpty(detDtos.get(0).getOption4())) {
                        searchQmsInspectionOrder.setSamePackageCode(detDtos.get(0).getOption4());
                        searchQualifiedInventoryDet.setOption4(detDtos.get(0).getOption4());
                    }
                    if (StringUtils.isEmpty(detDtos.get(0).getOption3()) && StringUtils.isEmpty(detDtos.get(0).getOption4())) {
                        searchQmsInspectionOrder.setQueryType(0);
                        searchQmsInspectionOrder.setInspectionStatus((byte) 1);
                    }
                    List<QmsInspectionOrder> qmsInspectionOrderList = qmsInspectionOrderMapper.findList(ControllerUtil.dynamicConditionByEntity(searchQmsInspectionOrder));
                    long qmsInspectionOrderTime = System.currentTimeMillis();
                    log.info("============= ??????qmsInspectionOrder?????????" + (qmsInspectionOrderTime - earchTime));
                    //?????????????????????+???????????????
                    List<WmsInnerInventoryDetDto> qualifiedInventoryDetDtos = innerFeignApi.findList(searchQualifiedInventoryDet).getData();
                    long qualifiedInventoryDetTime = System.currentTimeMillis();
                    log.info("============= ???????????????????????????" + (qualifiedInventoryDetTime - qmsInspectionOrderTime));

                    if (StringUtils.isNotEmpty(qmsInspectionOrderList)) {
                        qmsInspectionOrder = qmsInspectionOrderList.get(0);
                        if (StringUtils.isNotEmpty(detDtos.get(0).getOption4())) {
                            qmsInspectionOrder.setInventoryQty(new BigDecimal(qualifiedInventoryDetDtos.size()));
                        } else if (StringUtils.isNotEmpty(detDtos.get(0).getOption3()) && StringUtils.isEmpty(detDtos.get(0).getOption4())) {
                            qmsInspectionOrder.setInventoryQty(new BigDecimal(qualifiedInventoryDetDtos.size()));
                        } else {
                            qmsInspectionOrder.setOrderQty(new BigDecimal(detDtos.size()));
                            qmsInspectionOrder.setInventoryQty(new BigDecimal(detDtos.size()));
                            List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetService.showOrderDet(qmsInspectionOrder.getInspectionStandardId(), qmsInspectionOrder.getOrderQty());
                            qmsInspectionOrder.setQmsInspectionOrderDets(qmsInspectionOrderDets);
                        }
                        qmsInspectionOrderMapper.updateByPrimaryKeySelective(qmsInspectionOrder);
                        //    this.update(qmsInspectionOrder, (byte) 0);
                    } else {
                        //???????????????
                        qmsInspectionOrder.setMaterialId(detDtos.get(0).getMaterialId());
                        if (StringUtils.isNotEmpty(detDtos.get(0).getOption4())) {
                            SearchOmSalesCodeReSpc searchOmSalesCodeReSpc = new SearchOmSalesCodeReSpc();
                            searchOmSalesCodeReSpc.setSamePackageCode(detDtos.get(0).getOption4());
                            searchOmSalesCodeReSpc.setPageSize(99999);
                            List<OmSalesCodeReSpcDto> omSalesCodeReSpcDtos = oMFeignApi.findList(searchOmSalesCodeReSpc).getData();
                            if (StringUtils.isEmpty(omSalesCodeReSpcDtos)) {
//                                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "?????????????????????PO???:" + detDtos.get(0).getOption4());
                                 continue;
                            }
                            qmsInspectionOrder.setOrderQty(omSalesCodeReSpcDtos.get(0).getSamePackageCodeQty());
                            qmsInspectionOrder.setInventoryQty(new BigDecimal(qualifiedInventoryDetDtos.size()));
                        } else if (StringUtils.isNotEmpty(detDtos.get(0).getOption3()) && StringUtils.isEmpty(detDtos.get(0).getOption4())) {
                            SearchOmSalesOrderDetDto searchOmSalesOrderDetDto = new SearchOmSalesOrderDetDto();
                            searchOmSalesOrderDetDto.setSalesCode(detDtos.get(0).getOption3());
                            List<OmSalesOrderDetDto> omSalesOrderDetDtos = oMFeignApi.findList(searchOmSalesOrderDetDto).getData();
                            if (StringUtils.isEmpty(omSalesOrderDetDtos)){
//                                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "?????????????????????????????????:" + detDtos.get(0).getOption3());
                                continue;
                            }
                            qmsInspectionOrder.setOrderQty(omSalesOrderDetDtos.get(0).getOrderQty());
                            qmsInspectionOrder.setInventoryQty(new BigDecimal(qualifiedInventoryDetDtos.size()));
                        } else {
                            qmsInspectionOrder.setOrderQty(new BigDecimal(detDtos.size()));
                            qmsInspectionOrder.setInventoryQty(new BigDecimal(detDtos.size()));
                        }
                        createQmsInspectionOrder(qmsInspectionOrder, detDtos);
                    }

                    long createQmsInspectionOrderTime = System.currentTimeMillis();
                    log.info("============= ????????????????????????" + (createQmsInspectionOrderTime - qualifiedInventoryDetTime));

                    //???????????????????????????????????????
                    writeInspectionOrderCode(qmsInspectionOrder, detDtos, qualified, noQualified);

                    long writeInspectionOrderCodeTime = System.currentTimeMillis();
                    log.info("============= ????????????????????????????????????????????????" + (writeInspectionOrderCodeTime - createQmsInspectionOrderTime));
                }
            }
        }

        long sanxingTime = System.currentTimeMillis();
        //????????????????????????,???????????????PO????????????????????????
        map.clear();
        map.put("inventoryStatusId", wait);
        map.put("barcodeStatus", "3");
        map.put("logicCode", "C149");
        map.put("notEqualMark", 0);
        map.put("storageType", (byte) 1);
        map.put("ifInspectionOrderCodeNull", 1);
        List<WmsInnerInventoryDetDto> wmsInnerInventoryDetDtos1 = qmsInspectionOrderMapper.findInventoryDetList(map);
        Map<String, List<WmsInnerInventoryDetDto>> collect1 = new HashMap<>();
        for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : wmsInnerInventoryDetDtos1) {
            List<WmsInnerInventoryDetDto> inventoryDetDtos = new LinkedList<>();
            if (collect1.containsKey(wmsInnerInventoryDetDto.getMaterialCode())) {
                inventoryDetDtos = collect1.get(wmsInnerInventoryDetDto.getMaterialCode());
            }
            inventoryDetDtos.add(wmsInnerInventoryDetDto);
            collect1.put(wmsInnerInventoryDetDto.getMaterialCode(), inventoryDetDtos);
        }

        long sanxingInnerInventoryDetTime = System.currentTimeMillis();
        log.info("============= ?????????????????????????????????" + (sanxingInnerInventoryDetTime - sanxingTime));

        if (StringUtils.isNotEmpty(collect1)) {
            Set<String> codes1 = collect1.keySet();
            for (String code : codes1) {
                List<WmsInnerInventoryDetDto> detDtos = collect1.get(code);
                if (StringUtils.isNotEmpty(detDtos)) {
                    QmsInspectionOrder qmsInspectionOrder1 = new QmsInspectionOrder();
                    SearchQmsInspectionOrder searchQmsInspectionOrder = new SearchQmsInspectionOrder();
                    searchQmsInspectionOrder.setMaterialCode(code);
                    searchQmsInspectionOrder.setInspectionStatus((byte) 1);
                    searchQmsInspectionOrder.setPageSize(99999);
                    List<QmsInspectionOrder> qmsInspectionOrderList = qmsInspectionOrderMapper.findList(ControllerUtil.dynamicConditionByEntity(searchQmsInspectionOrder));

                    if (StringUtils.isNotEmpty(qmsInspectionOrderList)) {
                        qmsInspectionOrder1 = qmsInspectionOrderList.get(0);
                        String qmsInspectionOrderTime = DateUtils.getDateString(qmsInspectionOrder1.getCreateTime(), "yyyy-MM-dd");
                        String newDate = DateUtils.getDateString(new Date(), "yyyy-MM-dd");
                        if (newDate.equals(qmsInspectionOrderTime)) {
                            qmsInspectionOrder1.setOrderQty(new BigDecimal(detDtos.size()));
                            qmsInspectionOrder1.setInventoryQty(new BigDecimal(detDtos.size()));
                            List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetService.showOrderDet(qmsInspectionOrder1.getInspectionStandardId(), qmsInspectionOrder1.getOrderQty());
                            qmsInspectionOrder1.setQmsInspectionOrderDets(qmsInspectionOrderDets);
//                        this.update(qmsInspectionOrder1, (byte) 0);
                            qmsInspectionOrderMapper.updateByPrimaryKeySelective(qmsInspectionOrder1);
                        } else {
                            //???????????????
                            qmsInspectionOrder1.setMaterialId(detDtos.get(0).getMaterialId());
                            qmsInspectionOrder1.setOrderQty(new BigDecimal(detDtos.size()));
                            qmsInspectionOrder1.setInventoryQty(new BigDecimal(detDtos.size()));
                            createQmsInspectionOrder(qmsInspectionOrder1, detDtos);
                        }

                        //???????????????????????????????????????
                        //     writeInspectionOrderCode(qmsInspectionOrder1, detDtos, qualified, noQualified);
                    } else {
                        //???????????????
                        qmsInspectionOrder1.setMaterialId(detDtos.get(0).getMaterialId());
                        qmsInspectionOrder1.setOrderQty(new BigDecimal(detDtos.size()));
                        qmsInspectionOrder1.setInventoryQty(new BigDecimal(detDtos.size()));
                        createQmsInspectionOrder(qmsInspectionOrder1, detDtos);
                    }
                    //???????????????????????????????????????
                    writeInspectionOrderCode(qmsInspectionOrder1, detDtos, qualified, noQualified);
                }
            }

            long sanxingxunhuanTime = System.currentTimeMillis();
            log.info("============= ???????????????????????????" + (sanxingxunhuanTime - sanxingInnerInventoryDetTime));
        }
        long end = System.currentTimeMillis();
        log.info("============= ????????????" + (end - start));
        return 1;
    }

    public void createQmsInspectionOrder(QmsInspectionOrder qmsInspectionOrder, List<WmsInnerInventoryDetDto> detDtos) {
        qmsInspectionOrder.setInspectionStatus((byte) 1);
        SearchBaseInspectionWay searchBaseInspectionWay = new SearchBaseInspectionWay();
        searchBaseInspectionWay.setInspectionWayDesc("??????????????????");
        searchBaseInspectionWay.setQueryMark(1);
        List<BaseInspectionWay> inspectionWays = baseFeignApi.findList(searchBaseInspectionWay).getData();
        if (StringUtils.isEmpty(inspectionWays)) {
            throw new BizErrorException("??????????????????????????????");
        }
        qmsInspectionOrder.setInspectionWayId(inspectionWays.get(0).getInspectionWayId());

        if (StringUtils.isNotEmpty(detDtos.get(0).getOption3())) {
            SearchOmSalesOrderDetDto searchOmSalesOrderDetDto = new SearchOmSalesOrderDetDto();
            searchOmSalesOrderDetDto.setSalesCode(detDtos.get(0).getOption3());
            List<OmSalesOrderDetDto> salesOrderDetDtoList = oMFeignApi.findList(searchOmSalesOrderDetDto).getData();
            if (StringUtils.isNotEmpty(salesOrderDetDtoList)) {
                qmsInspectionOrder.setCustomerId(salesOrderDetDtoList.get(0).getSupplierId());
            }
        }

        SearchBaseInspectionStandard searchBaseInspectionStandard = new SearchBaseInspectionStandard();
        searchBaseInspectionStandard.setMaterialId(detDtos.get(0).getMaterialId());
        searchBaseInspectionStandard.setSupplierId(qmsInspectionOrder.getCustomerId());
        searchBaseInspectionStandard.setInspectionWayId(inspectionWays.get(0).getInspectionWayId());
        List<BaseInspectionStandard> inspectionStandardList = baseFeignApi.findList(searchBaseInspectionStandard).getData();
        if (StringUtils.isEmpty(inspectionStandardList)) {
            //???????????????????????????
            SearchBaseInspectionStandard searchBaseInspectionStandard1 = new SearchBaseInspectionStandard();
            searchBaseInspectionStandard1.setMaterialId((long) 0);
            searchBaseInspectionStandard1.setInspectionType((byte) 2);
            inspectionStandardList = baseFeignApi.findList(searchBaseInspectionStandard1).getData();
            if (StringUtils.isEmpty(inspectionStandardList))
                throw new BizErrorException("?????????????????????");
        }

        qmsInspectionOrder.setInspectionStandardId(inspectionStandardList.get(0).getInspectionStandardId());
        qmsInspectionOrder.setSalesCode(detDtos.get(0).getOption3());
        qmsInspectionOrder.setSamePackageCode(detDtos.get(0).getOption4());
        //??????
        List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetService.showOrderDet(qmsInspectionOrder.getInspectionStandardId(), qmsInspectionOrder.getOrderQty());
        qmsInspectionOrder.setQmsInspectionOrderDets(qmsInspectionOrderDets);
        this.add(qmsInspectionOrder);

    }

    public void writeInspectionOrderCode(QmsInspectionOrder qmsInspectionOrder, List<WmsInnerInventoryDetDto> detDtos, Long qualified, Long noQualified) {
        long start = System.currentTimeMillis();
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        //???????????????????????????????????????
        if (StringUtils.isNotEmpty(detDtos)) {
            log.info("============= detDtos?????????" + detDtos.size());
            List<WmsInnerInventoryDet> list = new ArrayList<>();
            for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : detDtos) {
                wmsInnerInventoryDetDto.setInspectionOrderCode(qmsInspectionOrder.getInspectionOrderCode());
                if (StringUtils.isNotEmpty(qmsInspectionOrder.getSalesCode()) || StringUtils.isNotEmpty(qmsInspectionOrder.getSamePackageCode())) {
                    if ("0".equals(qmsInspectionOrder.getInspectionResult())) {
                        wmsInnerInventoryDetDto.setInventoryStatusId(noQualified);
                    } else if ("1".equals(qmsInspectionOrder.getInspectionResult())) {
                        wmsInnerInventoryDetDto.setInventoryStatusId(qualified);
                    }
                }
                wmsInnerInventoryDetDto.setModifiedTime(new Date());
                wmsInnerInventoryDetDto.setModifiedUserId(user.getUserId());
                WmsInnerInventoryDet det = new WmsInnerInventoryDet();
                BeanUtils.copyProperties(wmsInnerInventoryDetDto, det);
                list.add(det);
            }
            long eachInventoryDetTime = System.currentTimeMillis();
            log.info("============= ??????detDtos?????????" + (eachInventoryDetTime - start));
            if (StringUtils.isNotEmpty(list)){
                ResponseEntity update = innerFeignApi.batchUpdate(list);
                if (StringUtils.isNotEmpty(update) && update.getCode() != 0) {
                    throw new BizErrorException("????????????????????????");
                }
            }
            long inventoryDetTime = System.currentTimeMillis();
            log.info("============= ?????????????????????????????????" + (inventoryDetTime - eachInventoryDetTime));
        }
        long updateInventoryDetTime = System.currentTimeMillis();
        log.info("============= ???????????????????????????????????????" + (updateInventoryDetTime - start));

        //????????????????????????
        //????????????????????????????????????????????????
        BigDecimal totalQty = BigDecimal.ZERO;
        BigDecimal lastQty = BigDecimal.ZERO;
        SearchWmsInnerInventory searchInventory = new SearchWmsInnerInventory();
        searchInventory.setStorageId(detDtos.get(0).getStorageId());
        searchInventory.setInventoryStatusId(detDtos.get(0).getInventoryStatusId());
        searchInventory.setMaterialId(detDtos.get(0).getMaterialId());
        searchInventory.setQcLock((byte) 1);
        searchInventory.setStockLock((byte) 0);
        searchInventory.setLockStatus((byte) 0);
        searchInventory.setInspectionOrderCode(qmsInspectionOrder.getInspectionOrderCode());
        List<WmsInnerInventoryDto> dtoList = innerFeignApi.findList(searchInventory).getData();
        if (StringUtils.isNotEmpty(dtoList) && dtoList.size() > 0) {
            lastQty = dtoList.get(0).getPackingQty();
        }
        long dtoListTime = System.currentTimeMillis();
        log.info("============= ???????????????????????????" + (dtoListTime - updateInventoryDetTime));

        SearchWmsInnerInventory searchWmsInnerInventory = new SearchWmsInnerInventory();
        searchWmsInnerInventory.setStorageId(detDtos.get(0).getStorageId());
        searchWmsInnerInventory.setInventoryStatusId(detDtos.get(0).getInventoryStatusId());
        searchWmsInnerInventory.setMaterialId(detDtos.get(0).getMaterialId());
        searchWmsInnerInventory.setJobStatus((byte) 1);
        searchWmsInnerInventory.setQcLock((byte) 0);
        searchWmsInnerInventory.setStockLock((byte) 0);
        List<WmsInnerInventoryDto> list = innerFeignApi.findList(searchWmsInnerInventory).getData();
        long listTime = System.currentTimeMillis();
        log.info("============= ???????????????????????????" + (listTime - dtoListTime));

        List<WmsInnerInventory> updateList = new ArrayList<>();
        if (StringUtils.isNotEmpty(list) && lastQty.compareTo(new BigDecimal(0)) == 0) {
            for (WmsInnerInventoryDto wmsInnerInventoryDto : list) {
                WmsInnerInventory inventory = new WmsInnerInventory();
                BeanUtils.copyProperties(wmsInnerInventoryDto, inventory);
                inventory.setQcLock((byte) 1);
                inventory.setModifiedTime(new Date());
                inventory.setModifiedUserId(user.getModifiedUserId());
                inventory.setInspectionOrderCode(qmsInspectionOrder.getInspectionOrderCode());
                updateList.add(inventory);
            }
        } else if (StringUtils.isNotEmpty(list) && lastQty.compareTo(new BigDecimal(0)) == 1) {
            for (WmsInnerInventoryDto wmsInnerInventoryDto : list) {
                totalQty = totalQty.add(wmsInnerInventoryDto.getPackingQty());
                WmsInnerInventory inventory = new WmsInnerInventory();
                BeanUtils.copyProperties(wmsInnerInventoryDto, inventory);
                inventory.setPackingQty(new BigDecimal(0));
                inventory.setQcLock((byte) 1);
                inventory.setModifiedTime(new Date());
                inventory.setModifiedUserId(user.getModifiedUserId());
                updateList.add(inventory);
            }

            WmsInnerInventoryDto upDto = dtoList.get(0);
            upDto.setPackingQty(upDto.getPackingQty().add(totalQty));
            upDto.setRelevanceOrderCode(list.get(0).getRelevanceOrderCode());
            upDto.setModifiedTime(new Date());
            ResponseEntity update = innerFeignApi.update(upDto);
            if (StringUtils.isNotEmpty(update) && update.getCode() != 0) {
                throw new BizErrorException("????????????????????????");
            }
        }
        if (StringUtils.isNotEmpty(updateList)){
            // ??????????????????
            ResponseEntity responseEntity = innerFeignApi.batchUpdateInventory(updateList);
            if (StringUtils.isNotEmpty(responseEntity) && responseEntity.getCode() != 0) {
                throw new BizErrorException("????????????????????????");
            }
        }

        long end = System.currentTimeMillis();
        log.info("============= ?????????????????????" + (end - listTime));
        log.info("============= writeInspectionOrderCode????????????" + (end - start));
    }

    public Map<String, List<WmsInnerInventoryDetDto>> newGroupInventoryDet(List<WmsInnerInventoryDetDto> innerInventoryDetDtos) {
        //?????????1???PO???????????????PO????????????????????????
        // 2???PO???????????????????????????????????????????????????????????????????????????
        // 3???PO?????????????????????????????????????????????????????????  //Option3?????????????????????Option4???PO???
        Map<String, List<WmsInnerInventoryDetDto>> collect = new HashMap<>();
        for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : innerInventoryDetDtos) {
            if (StringUtils.isEmpty(wmsInnerInventoryDetDto.getOption4())) {

                List<WmsInnerInventoryDetDto> inventoryDetDtos = new LinkedList<>();
                boolean tag = false;
                if (StringUtils.isNotEmpty(wmsInnerInventoryDetDto.getOption3())) {
                    //PO?????????????????????????????????????????????
                    if (collect.containsKey(wmsInnerInventoryDetDto.getOption3())) {
                        inventoryDetDtos = collect.get(wmsInnerInventoryDetDto.getOption3());
                    }
                    inventoryDetDtos.add(wmsInnerInventoryDetDto);
                    collect.put(wmsInnerInventoryDetDto.getOption3(), inventoryDetDtos);
                } else {
                    tag = true;
                }

                if (tag) {
                    //PO??????????????????????????????????????????
                    if (collect.containsKey(wmsInnerInventoryDetDto.getMaterialCode())) {
                        inventoryDetDtos = collect.get(wmsInnerInventoryDetDto.getMaterialCode());
                    }
                    inventoryDetDtos.add(wmsInnerInventoryDetDto);
                    collect.put(wmsInnerInventoryDetDto.getMaterialCode(), inventoryDetDtos);
                }
            } else {

                //PO?????????????????????
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
    public int qmsInspectToInnerJobShift(String ids) {
        int i = 1;
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (ids.length() <= 0) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }
        //???????????????????????????????????????
        Long inStorageId = null;
        Long warehouseId = null;
        String storageCode = "Z-QC";
        SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
        searchBaseStorage.setStorageCode(storageCode);
        List<BaseStorage> storageDtoList = baseFeignApi.findList(searchBaseStorage).getData();
        if (StringUtils.isEmpty(storageDtoList) || storageDtoList.size() <= 0) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "???????????????????????????");
        }
        inStorageId = storageDtoList.get(0).getStorageId();
        warehouseId = storageDtoList.get(0).getWarehouseId();

        SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
        searchBaseInventoryStatus.setWarehouseId(warehouseId);
        searchBaseInventoryStatus.setOrgId(user.getOrganizationId());
        List<BaseInventoryStatus> inventoryStatusList = baseFeignApi.findList(searchBaseInventoryStatus).getData();
        List<BaseInventoryStatus> statusList = inventoryStatusList.stream().filter(item -> item.getInventoryStatusName().equals("??????")).collect(Collectors.toList());
        if (StringUtils.isEmpty(statusList) || statusList.size() <= 0) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "??????????????????????????????");
        }

        String[] arrId = ids.split(",");
        for (String id : arrId) {
            QmsInspectionOrder qmsInspectionOrder = qmsInspectionOrderMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(qmsInspectionOrder)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012005);
            }
            BigDecimal sampleQty = null;
            Example example = new Example(QmsInspectionOrderDet.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("inspectionOrderId", qmsInspectionOrder.getInspectionOrderId());
            List<QmsInspectionOrderDet> orderDetList = qmsInspectionOrderDetMapper.selectByExample(example);
            sampleQty = orderDetList.get(0).getSampleQty();
            if (StringUtils.isEmpty(sampleQty)) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "?????????????????????0");
            }

            String orderCode = qmsInspectionOrder.getInspectionOrderCode();
            SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
            searchWmsInnerJobOrder.setRelatedOrderCode(orderCode);
            List<WmsInnerJobOrderDto> list = innerFeignApi.findList(searchWmsInnerJobOrder).getData();
            if (StringUtils.isNotEmpty(list) && list.size() > 0) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "???????????????-->" + orderCode + " ???????????????????????????!");
            }

            SearchWmsInnerInventoryDet searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
            searchWmsInnerInventoryDet.setMaterialId(qmsInspectionOrder.getMaterialId());
            searchWmsInnerInventoryDet.setInspectionOrderCode(qmsInspectionOrder.getInspectionOrderCode());
            List<WmsInnerInventoryDetDto> inventoryDetDtos = innerFeignApi.findList(searchWmsInnerInventoryDet).getData();
            if (StringUtils.isEmpty(inventoryDetDtos)) {
                throw new BizErrorException("?????????????????????????????????????????????-->" + orderCode);
            }

            Long outStorageId = inventoryDetDtos.get(0).getStorageId();

            //???????????????
            // inventoryStatusId: 132
            //inventoryStatusName: "??????"
            Long materialId = qmsInspectionOrder.getMaterialId();
            SearchWmsInnerInventory searchWmsInnerInventory = new SearchWmsInnerInventory();
            searchWmsInnerInventory.setMaterialId(materialId);
            searchWmsInnerInventory.setStorageId(outStorageId);
            searchWmsInnerInventory.setLockStatus((byte) 0);
            searchWmsInnerInventory.setJobStatus((byte) 1);
            searchWmsInnerInventory.setInventoryStatusName("??????");
            List<WmsInnerInventoryDto> inventoryDtos = innerFeignApi.findList(searchWmsInnerInventory).getData();
            if (StringUtils.isEmpty(inventoryDtos) || inventoryDtos.size() <= 0) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "?????????????????????");
            }
            BigDecimal qty = sampleQty;
            List<WmsInnerInventoryDto> dtoList = inventoryDtos.stream().filter(u -> (u.getPackingQty().compareTo(qty) >= 0)).collect(Collectors.toList());
            if (StringUtils.isEmpty(dtoList) || dtoList.size() <= 0) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "?????????????????????????????????????????????");
            }
            if (StringUtils.isNotEmpty(inventoryDtos) && inventoryDtos.size() > 0) {
                WmsInnerJobOrder wmsInnerJobOrder = new WmsInnerJobOrder();
                wmsInnerJobOrder.setJobOrderType((byte) 2);
                wmsInnerJobOrder.setShiftType((byte) 2);
                wmsInnerJobOrder.setWarehouseId(warehouseId);
                wmsInnerJobOrder.setStatus((byte) 1);
                wmsInnerJobOrder.setOrderStatus((byte) 3);
                wmsInnerJobOrder.setOrgId(user.getOrganizationId());
                wmsInnerJobOrder.setRelatedOrderCode(orderCode);
                wmsInnerJobOrder.setSourceOrderId(qmsInspectionOrder.getInspectionOrderId());
                wmsInnerJobOrder.setOption1("qmsToInnerJobShift");

                List<WmsInnerJobOrderDet> detList = new LinkedList<>();

                SearchBaseTab searchBaseTab = new SearchBaseTab();
                searchBaseTab.setMaterialId(materialId);
                List<BaseTabDto> tabList = baseFeignApi.findTabList(searchBaseTab).getData();

                WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
                wmsInnerJobOrderDet.setMaterialId(materialId);
                wmsInnerJobOrderDet.setPlanQty(qty);
                wmsInnerJobOrderDet.setDistributionQty(qty);
                wmsInnerJobOrderDet.setOutStorageId(outStorageId);
                wmsInnerJobOrderDet.setInStorageId(inStorageId);
                wmsInnerJobOrderDet.setSourceDetId(dtoList.get(0).getInventoryId());
                wmsInnerJobOrderDet.setOrderStatus((byte) 3);
                wmsInnerJobOrderDet.setInventoryStatusId(statusList.get(0).getInventoryStatusId());
                if (StringUtils.isNotEmpty(tabList) && tabList.size() > 0) {
                    wmsInnerJobOrderDet.setPackingUnitName(tabList.get(0).getMainUnit());
                }

                detList.add(wmsInnerJobOrderDet);

                wmsInnerJobOrder.setWmsInPutawayOrderDets(detList);
                ResponseEntity responseEntity = innerFeignApi.add(wmsInnerJobOrder);
                if (responseEntity.getCode() != 0) {
                    throw new BizErrorException("???????????????????????????");
                }
            }
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int recheckByBarcode(String barcode) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        log.info("===================== recheckByBarcode???????????? =====================");
        Map<String, Object> map = new HashMap<>();
        map.put("factoryBarcode", barcode);
        map.put("barcodeStatus", 0);
        List<QmsInspectionOrderDetSample> orderDetSamples = qmsInspectionOrderDetSampleMapper.findList(map);
        log.info("===================== orderDetSamples??? " + JSON.toJSONString(orderDetSamples));
        if (orderDetSamples.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "???????????????????????????");
        }
        // ???????????????
        Long inspectionOrderId = null;
        List<Long> inspectionOrderDetIds = new ArrayList<>();
        for (QmsInspectionOrderDetSample orderDetSample : orderDetSamples) {
            orderDetSample.setBarcodeStatus((byte) 1);
            if (StringUtils.isNotEmpty(orderDetSample.getSampleValue()) && StringUtils.isNotEmpty(orderDetSample.getBadnessPhenotypeId())) {
                orderDetSample.setSampleValue("OK");
                orderDetSample.setBadnessPhenotypeId(null);
                orderDetSample.setBadnessPhenotypeDesc(null);
                inspectionOrderDetIds.add(orderDetSample.getInspectionOrderDetId());
            } else {
                inspectionOrderId = orderDetSample.getInspectionOrderId();
            }
            orderDetSample.setModifiedUserId(user.getUserId());
            orderDetSample.setModifiedTime(new Date());
        }
        int num = qmsInspectionOrderDetSampleMapper.batchUpdate(orderDetSamples);

        //??????????????????????????????????????????????????????????????????
        map.clear();
        map.put("list", inspectionOrderDetIds);
        List<QmsInspectionOrderDet> detList = qmsInspectionOrderDetMapper.findDetList(map);
        for (QmsInspectionOrderDet det : detList) {
            BigDecimal subtract = det.getBadnessQty().subtract(BigDecimal.ONE);
            if (subtract.compareTo(BigDecimal.valueOf(det.getAcValue())) < 1) {
                det.setInspectionResult((byte) 1);
            }
            det.setBadnessQty(subtract);
            det.setModifiedTime(new Date());
            det.setModifiedUserId(user.getUserId());
        }
        qmsInspectionOrderDetMapper.batchUpdate(detList);


        log.info("===================== inspectionOrderId: " + inspectionOrderId);
        // ?????????????????????????????????
        Example example = new Example(QmsInspectionOrderDetSample.class);
        example.createCriteria().andEqualTo("sampleValue", "NG").andIn("inspectionOrderDetId", inspectionOrderDetIds);
        int count = qmsInspectionOrderDetSampleMapper.selectCountByExample(example);
        QmsInspectionOrder qmsInspectionOrder = qmsInspectionOrderMapper.selectByPrimaryKey(inspectionOrderId);

        log.info("===================== ?????? " + (count - 1));
        if (count == 0) {
            qmsInspectionOrder.setRecheckStatus((byte) 2);
            qmsInspectionOrder.setInspectionResult((byte) 1);
            qmsInspectionOrderMapper.updateByPrimaryKey(qmsInspectionOrder);
        }
        if (count > 0) {
            qmsInspectionOrder.setInspectionResult((byte) 2);
            qmsInspectionOrder.setRecheckStatus((byte) 2);
            qmsInspectionOrderMapper.updateByPrimaryKey(qmsInspectionOrder);
        }
        innerFeignApi.autoRecheck(qmsInspectionOrder.getInspectionOrderCode());
        log.info("===================== recheckByBarcode?????? =====================");
        return num;
    }
}
