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
            //明细
            qmsInspectionOrderDet.setBadnessQty(BigDecimal.ZERO);
            qmsInspectionOrderDet.setInspectionResult((byte) 1);
            qmsInspectionOrderDetMapper.updateByPrimaryKeySelective(qmsInspectionOrderDet);

            //样本
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

        //处理库存:库存状态改为合格
        SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
        searchBaseInventoryStatus.setInventoryStatusName("合格");
        searchBaseInventoryStatus.setNameQueryMark(1);
        List<BaseInventoryStatus> inventoryStatus = baseFeignApi.findList(searchBaseInventoryStatus).getData();
        if (StringUtils.isEmpty(inventoryStatus))
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "未找到库存的合格状态");

        //库存明细
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

        //库存
        SearchWmsInnerInventory searchWmsInnerInventory = new SearchWmsInnerInventory();
        searchWmsInnerInventory.setInspectionOrderCode(qmsInspectionOrder.getInspectionOrderCode());
        searchWmsInnerInventory.setQcLock((byte) 1);
        List<WmsInnerInventoryDto> innerInventoryDtoList = innerFeignApi.findList(searchWmsInnerInventory).getData();
        if (StringUtils.isEmpty(innerInventoryDtoList)) {
            throw new BizErrorException("未查询到对应库存信息");
        }
        WmsInnerInventoryDto wmsInnerInventoryDto = innerInventoryDtoList.get(0);

        //合并库存
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
        //log.info("============= 检验项目数据" + JSON.toJSONString(qmsInspectionOrderDets));

        Example example1 = new Example(QmsInspectionOrderDetSample.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("inspectionOrderId", inspectionOrderId);
        List<QmsInspectionOrderDetSample> qmsInspectionOrderDetSamples = qmsInspectionOrderDetSampleMapper.selectByExample(example1);
        log.info("============= 扫码条码数据" + JSON.toJSONString(qmsInspectionOrderDetSamples));

        for (QmsInspectionOrderDetSample DetSample : qmsInspectionOrderDetSamples) {
            DetSample.setBarcodeStatus((byte) 1);
            DetSample.setModifiedUserId(user.getUserId());
            DetSample.setModifiedTime(new Date());
            qmsInspectionOrderDetSampleMapper.updateByPrimaryKeySelective(DetSample);
        }

        List<QmsInspectionOrderDetSample> inspectionOrderDetSampleList = new LinkedList<>();
        for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets) {
            //明细
            qmsInspectionOrderDet.setUnitName("台");
            qmsInspectionOrderDet.setBadnessQty(BigDecimal.ZERO);
            qmsInspectionOrderDet.setInspectionResult((byte) 1);
            qmsInspectionOrderDetMapper.updateByPrimaryKeySelective(qmsInspectionOrderDet);

            //样本
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

        //处理库存
        this.handleInventory(qmsInspectionOrder.getInspectionOrderCode(), qmsInspectionOrder.getInspectionResult());

        //生成移位单
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
            //明细
            qmsInspectionOrderDet.setBadnessQty(BigDecimal.ZERO);
            qmsInspectionOrderDet.setInspectionResult((byte) 1);
            qmsInspectionOrderDetMapper.updateByPrimaryKeySelective(qmsInspectionOrderDet);

            //样本
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
        searchBaseInventoryStatus.setInventoryStatusName("合格");
        searchBaseInventoryStatus.setNameQueryMark(1);
        List<BaseInventoryStatus> inventoryStatus = baseFeignApi.findList(searchBaseInventoryStatus).getData();
        if (StringUtils.isEmpty(inventoryStatus))
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "未找到库存的合格状态");

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

        //检验结果返写回库存
        SearchWmsInnerInventory searchWmsInnerInventory = new SearchWmsInnerInventory();
        searchWmsInnerInventory.setInspectionOrderCode(inspectionOrderCode);
        searchWmsInnerInventory.setQcLock((byte) 1);
        ResponseEntity<List<WmsInnerInventoryDto>> innerInventoryDtoList = innerFeignApi.findList(searchWmsInnerInventory);
        if (StringUtils.isEmpty(innerInventoryDtoList.getData())) {
            throw new BizErrorException("未查询到对应库存信息");
        }

        List<WmsInnerInventoryDto> inventoryDtoList = innerInventoryDtoList.getData();
        for (WmsInnerInventoryDto innerInventoryDto : inventoryDtoList) {
            innerInventoryDto.setQcLock((byte) 0);
            innerInventoryDto.setInventoryStatusId(inventoryStatus.get(0).getInventoryStatusId());
            innerInventoryDto.setModifiedTime(new Date());
            innerFeignApi.update(innerInventoryDto);
        }

        //生成移位单
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
            //若barcodeStatus为空，则认为合格
            if (StringUtils.isEmpty(qmsInspectionOrderDetSample.getBarcodeStatus())) {
                qmsInspectionOrderDetSample.setBarcodeStatus((byte) 1);
                qmsInspectionOrderDetSampleMapper.updateByPrimaryKeySelective(qmsInspectionOrderDetSample);
            }
        }

        List<QmsInspectionOrderDetSample> inspectionOrderDetSampleList = new LinkedList<>();
        for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets) {
            //明细
            qmsInspectionOrderDet.setUnitName("台");//万宝：提交时加上单位
            if (StringUtils.isEmpty(qmsInspectionOrderDet.getInspectionResult())) {
                qmsInspectionOrderDet.setBadnessQty(BigDecimal.ZERO);
                qmsInspectionOrderDet.setInspectionResult((byte) 1);
                qmsInspectionOrderDetMapper.updateByPrimaryKeySelective(qmsInspectionOrderDet);

                //样本
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

        //处理库存
        if (qmsInspectionOrder.getInspectionResult() == (byte) 0 || qmsInspectionOrder.getInspectionResult() == (byte) 1) {
            this.handleInventory(qmsInspectionOrder.getInspectionOrderCode(), qmsInspectionOrder.getInspectionResult());
        } else if (qmsInspectionOrder.getInspectionResult() == (byte) 2) {
            this.splitInventory(qmsInspectionOrder.getInspectionOrderCode(), unQualifiedBarcodes);
        }

        //生成移位单
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
            //复检提交 对已生成的质检移位单做处理
            List<QmsInspectionOrderDetSample> ngQualifiedBarcodes = list.stream().filter(item -> item.getBarcodeStatus() != null && item.getBarcodeStatus() == 0).collect(Collectors.toList());
            BigDecimal ngQty = new BigDecimal(ngQualifiedBarcodes.size());
            innerFeignApi.updateShit(jobOrderDtoList.get(0).getJobOrderId(), ngQty);

            log.info("============= 复检移位单处理==========================");

        } else {
            //移位作业扫码提交参数
            SaveShiftWorkDetDto saveShiftWorkDetDto = new SaveShiftWorkDetDto();
            List<String> barcodes = new ArrayList<>();
            Long sourceDetId = null;//第一阶段质检移位单移出库位的库存ID
            for (QmsInspectionOrderDetSample item : list) {
                barcodes.add(item.getBarcode());
            }
            saveShiftWorkDetDto.setBarcodes(barcodes);
            saveShiftWorkDetDto.setMaterialQty(new BigDecimal(list.size()));

            //移位作业上架参数
            SaveShiftJobOrderDto saveShiftJobOrderDto = new SaveShiftJobOrderDto();

            //找成品检验对应的质检移位单
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

                    log.info("============= 第一阶段移位单处理==========================");
                }
            }
            //库存状态
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
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "未找到三星质检专用库位");
                }
                inStorageId = storageDtoList.get(0).getStorageId();
                warehouseId = storageDtoList.get(0).getWarehouseId();
                shiftType = 3;
            }

            //部分不合格 除样本数数量外 原库位库存更新为合格
            if (StringUtils.isNotEmpty(sourceDetId) && qmsInspectionOrder.getInspectionResult() == (byte) 2) {
                List<BaseInventoryStatus> statusList = inventoryStatusList.stream().filter(item -> item.getInventoryStatusName().equals("合格")).collect(Collectors.toList());
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

            //质检移位单自动扫码提交 上架提交
            Long inventoryId = null;
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("autoConfirmShift");
            List<SysSpecItem> sysSpecItemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            log.info("============= 程序配置项信息" + JSON.toJSONString(sysSpecItemList));

            if (StringUtils.isNotEmpty(sysSpecItemList) && sysSpecItemList.size() > 0) {
                SysSpecItem sysSpecItem = sysSpecItemList.get(0);
                if (sysSpecItem.getParaValue().equals("1") && jobOrderDtoList.get(0).getOrderStatus() == (byte) 3) {
                    log.info("============= 质检移位单扫码提交参数==================" + JSON.toJSONString(saveShiftWorkDetDto));
                    innerFeignApi.saveShiftWorkDetBarcode(saveShiftWorkDetDto);
                    log.info("============= 质检移位单上架参数==================" + JSON.toJSONString(saveShiftJobOrderDto));
                    //innerFeignApi.saveJobOrder(saveShiftJobOrderDto);
                    ResponseEntity<Long> entityLong = innerFeignApi.saveJobOrderReturnId(saveShiftJobOrderDto);
                    log.info("===================返回结果================" + entityLong.toString());
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
            log.info("============= 查询库存数据参数" + JSON.toJSONString(searchWmsInnerInventory));
            log.info("============= 原始库存数据" + JSON.toJSONString(inventoryDtos));*/

            if (StringUtils.isNotEmpty(inventoryId)) {
                //List<WmsInnerInventoryDto> dtoList=inventoryDtos.stream().filter(item -> item.getPackingQty() != null && item.getPackingQty().compareTo(new BigDecimal(0))==1).collect(Collectors.toList());

                //log.info("============= 开始生成二阶段质检移位单" + JSON.toJSONString(dtoList));
                log.info("============= 质检移位单上架返回库存ID-->" + inventoryId.toString());

                log.info("======================== 开始生成二阶段质检移位单=========================");

                //存在合格的库存才生成移位单
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
                    List<BaseInventoryStatus> statusList = inventoryStatusList.stream().filter(item -> item.getInventoryStatusName().equals("不合格")).collect(Collectors.toList());
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

                    log.info("============= 不良数量" + JSON.toJSONString(ngQty));
                }
                if (goodQty.compareTo(new BigDecimal(0)) == 1) {
                    List<BaseInventoryStatus> statusList = inventoryStatusList.stream().filter(item -> item.getInventoryStatusName().equals("合格")).collect(Collectors.toList());
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

                    log.info("============= 良品数量" + JSON.toJSONString(goodQty));
                }

                log.info("============= 明细信息" + JSON.toJSONString(detList));

                wmsInnerJobOrder.setWmsInPutawayOrderDets(detList);
                ResponseEntity responseEntity = innerFeignApi.add(wmsInnerJobOrder);
                if (responseEntity.getCode() != 0) {
                    throw new BizErrorException("生成质检移位单失败");
                }

                log.info("======================== 生成二阶段质检移位单结束=========================");


            }
        }

        return i;
    }

    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int splitInventory(String inspectionOrderCode, List<QmsInspectionOrderDetSample> unQualifiedBarcodes) {
        SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
        searchBaseInventoryStatus.setInventoryStatusName("不合格");
        searchBaseInventoryStatus.setNameQueryMark(1);
        List<BaseInventoryStatus> inventoryStatusList1 = baseFeignApi.findList(searchBaseInventoryStatus).getData();
        if (StringUtils.isEmpty(inventoryStatusList1)) {
            throw new BizErrorException("未查询到对应库存状态");
        }

        searchBaseInventoryStatus.setInventoryStatusName("合格");
        List<BaseInventoryStatus> inventoryStatusList2 = baseFeignApi.findList(searchBaseInventoryStatus).getData();
        if (StringUtils.isEmpty(inventoryStatusList2)) {
            throw new BizErrorException("未查询到对应库存状态");
        }

        //检验结果返写回库存明细
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

        //检验结果返写回库存
        SearchWmsInnerInventory searchWmsInnerInventory = new SearchWmsInnerInventory();
        searchWmsInnerInventory.setJobStatus((byte) 1);
        searchWmsInnerInventory.setInspectionOrderCode(inspectionOrderCode);
        ResponseEntity<List<WmsInnerInventoryDto>> innerInventoryDtoList = innerFeignApi.findList(searchWmsInnerInventory);
        if (StringUtils.isEmpty(innerInventoryDtoList.getData())) {
            throw new BizErrorException("未查询到对应库存信息");
        }
        WmsInnerInventoryDto wmsInnerInventoryDto = new WmsInnerInventoryDto();
        BeanUtils.copyProperties(innerInventoryDtoList.getData().get(0), wmsInnerInventoryDto);
        WmsInnerInventoryDto innerInventoryDto = new WmsInnerInventoryDto();
        BeanUtils.copyProperties(innerInventoryDtoList.getData().get(0), innerInventoryDto);

        //合并不合格库存
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

        //不做合并库存操作
        /*wmsInnerInventoryDto.setQcLock((byte)0);
        wmsInnerInventoryDto.setInventoryStatusId(inventoryStatusList1.get(0).getInventoryStatusId());
        wmsInnerInventoryDto.setPackingQty(new BigDecimal(unQualifiedCount));
        innerFeignApi.update(wmsInnerInventoryDto);*/

        //合并合格库存
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
        //质检移位单 作业完成后再移入合格库存
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
        //手动添加变更质检锁
        List<WmsInnerInventoryDetDto> wmsInnerInventoryDetDtos = new LinkedList<>();
        List<WmsInnerInventoryDto> wmsInnerInventoryDtos = new LinkedList<>();
        if (StringUtils.isNotEmpty(qmsInspectionOrder.getInventoryIds())) {
            String[] ids = qmsInspectionOrder.getInventoryIds().split(",");
            for (String id : ids) {
                SearchWmsInnerInventory searchWmsInnerInventory = new SearchWmsInnerInventory();
                searchWmsInnerInventory.setInventoryId(Long.valueOf(id).longValue());
                ResponseEntity<List<WmsInnerInventoryDto>> innerInventoryDtoList = innerFeignApi.findList(searchWmsInnerInventory);
                if (StringUtils.isEmpty(innerInventoryDtoList.getData())) throw new BizErrorException("未查询到对应库存信息");
                WmsInnerInventoryDto wmsInnerInventoryDto = innerInventoryDtoList.getData().get(0);
                wmsInnerInventoryDtos.add(wmsInnerInventoryDto);

                //对应的库存明细写入质检单号
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
                throw new BizErrorException("未查询到对应库存明细");
            }

            //库存明细按PO和销售订单号分组
            Map<String, List<WmsInnerInventoryDetDto>> collect = groupInventoryDet(wmsInnerInventoryDetDtos);
            Set<String> codes = collect.keySet();
            for (String code : codes) {
                List<WmsInnerInventoryDetDto> detDtos = collect.get(code);
                qmsInspectionOrder.setOrderQty(new BigDecimal(detDtos.size()));
                //明细
                List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetService.showOrderDet(qmsInspectionOrder.getInspectionStandardId(), qmsInspectionOrder.getOrderQty());
                qmsInspectionOrder.setQmsInspectionOrderDets(qmsInspectionOrderDets);
                i += this.add(qmsInspectionOrder);

                //对应的库存明细写入质检单号
                if (StringUtils.isNotEmpty(detDtos)) {
                    for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : detDtos) {
                        wmsInnerInventoryDetDto.setInspectionOrderCode(qmsInspectionOrder.getInspectionOrderCode());
                        innerFeignApi.update(wmsInnerInventoryDetDto);
                    }
                }

                //按库存分组
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

                    //对应的库存写入质检单号
                    for (WmsInnerInventoryDto wmsInnerInventory : wmsInnerInventoryDtos) {
                        if (detDtoList.get(0).getStorageId().equals(wmsInnerInventory.getStorageId())
                                && detDtoList.get(0).getInventoryStatusId().equals(wmsInnerInventory.getInventoryStatusId())
                                && detDtoList.get(0).getMaterialId().equals(wmsInnerInventory.getMaterialId())) {
                            //拆库存
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

        //履历
        QmsHtInspectionOrder qmsHtInspectionOrder = new QmsHtInspectionOrder();
        BeanUtils.copyProperties(qmsInspectionOrder, qmsHtInspectionOrder);
        qmsHtInspectionOrderMapper.insertSelective(qmsHtInspectionOrder);

        //明细
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
        //生成的质检移位单判断
        Long jobOrderId = null;
        if (type != (byte) 0) {
            String inspectionOrderCode = qmsInspectionOrder.getInspectionOrderCode();
            SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
            searchWmsInnerJobOrder.setRelatedOrderCode(inspectionOrderCode);
            searchWmsInnerJobOrder.setCodeQueryMark(1);
            List<WmsInnerJobOrderDto> orderDtoList = innerFeignApi.findList(searchWmsInnerJobOrder).getData();
            if (StringUtils.isNotEmpty(orderDtoList) && orderDtoList.size() > 0) {
                if (orderDtoList.get(0).getOrderStatus() >= 4) {
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "质检移位单已作业或已完成 不能修改-->" + orderDtoList.get(0).getJobOrderCode());
                }

                jobOrderId = orderDtoList.get(0).getJobOrderId();
            }
        }
        qmsInspectionOrder.setModifiedUserId(user.getUserId());
        qmsInspectionOrder.setModifiedTime(new Date());
        qmsInspectionOrder.setOrgId(user.getOrganizationId());
        int i = qmsInspectionOrderMapper.updateByPrimaryKeySelective(qmsInspectionOrder);

        //履历
        QmsHtInspectionOrder qmsHtInspectionOrder = new QmsHtInspectionOrder();
        BeanUtils.copyProperties(qmsInspectionOrder, qmsHtInspectionOrder);
        qmsHtInspectionOrderMapper.insertSelective(qmsHtInspectionOrder);

        //原来有的明细只更新
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

        //删除原明细
        Example example1 = new Example(QmsInspectionOrderDet.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("inspectionOrderId", qmsInspectionOrder.getInspectionOrderId());
        if (idList.size() > 0) {
            criteria1.andNotIn("inspectionOrderDetId", idList);
        }
        List<QmsInspectionOrderDet> qmsInspectionOrderDets1 = qmsInspectionOrderDetMapper.selectByExample(example1);
        if (StringUtils.isNotEmpty(qmsInspectionOrderDets1)) {
            for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets1) {
                //删除检验单明细样本
                Example example2 = new Example(QmsInspectionOrderDetSample.class);
                Example.Criteria criteria2 = example2.createCriteria();
                criteria2.andEqualTo("inspectionOrderDetId", qmsInspectionOrderDet.getInspectionOrderDetId());
                qmsInspectionOrderDetSampleMapper.deleteByExample(example2);
            }
        }
        //删除原检验单明细
        qmsInspectionOrderDetMapper.deleteByExample(example1);

        //新增新的检验单明细
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

        //返写检验状态与检验结果
        /*if(type != 0) {
            this.writeBack(qmsInspectionOrder.getInspectionOrderId());
        }*/

        //改了样本数 从新生成质检移位单
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

        //计算明细项目合格数与不合格数
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
        //检验结果返写回库存
        SearchWmsInnerInventory searchWmsInnerInventory = new SearchWmsInnerInventory();
        searchWmsInnerInventory.setInspectionOrderCode(inspectionOrderCode);
        //2022/03/23 查询所有质检锁的库存 整批合格要更新库存合格状态
        searchWmsInnerInventory.setQcLock((byte) 1);

        ResponseEntity<List<WmsInnerInventoryDto>> innerInventoryDtoList = innerFeignApi.findList(searchWmsInnerInventory);
        if (StringUtils.isEmpty(innerInventoryDtoList.getData())) {
            throw new BizErrorException("未查询到对应库存信息");
        }
//        WmsInnerInventoryDto wmsInnerInventoryDto = new WmsInnerInventoryDto();
//        BeanUtils.copyProperties(innerInventoryDtoList.getData().get(0),wmsInnerInventoryDto);

        SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
        searchBaseInventoryStatus.setInventoryStatusName(inspectionResult == 0 ? "不合格" : "合格");
        searchBaseInventoryStatus.setNameQueryMark(1);
        List<BaseInventoryStatus> inventoryStatusList = baseFeignApi.findList(searchBaseInventoryStatus).getData();
        if (StringUtils.isEmpty(inventoryStatusList)) {
            throw new BizErrorException("未查询到对应库存状态");
        }

        //合并库存
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
            log.info("修改的库存id：" + innerInventoryDto.getInventoryId() + ",库存状态id：" + innerInventoryDto.getInventoryStatusId());
        }

        //检验结果返写回库存明细
        SearchWmsInnerInventoryDet searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
        searchWmsInnerInventoryDet.setPageSize(999);
        searchWmsInnerInventoryDet.setInspectionOrderCode(inspectionOrderCode);
        List<WmsInnerInventoryDetDto> inventoryDetDtos = innerFeignApi.findList(searchWmsInnerInventoryDet).getData();
        if (StringUtils.isNotEmpty(inventoryDetDtos)) {
            for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : inventoryDetDtos) {
                wmsInnerInventoryDetDto.setInventoryStatusId(inventoryStatusList.get(0).getInventoryStatusId());
                wmsInnerInventoryDetDto.setQcDate(new Date());
                innerFeignApi.update(wmsInnerInventoryDetDto);
                log.info("修改的库存条码：" + wmsInnerInventoryDetDto.getBarcode() + ",库存状态id：" + wmsInnerInventoryDetDto.getInventoryStatusId());
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
                throw new BizErrorException("检验中或检验完成的单据不可删除");
            }
            QmsHtInspectionOrder qmsHtInspectionOrder = new QmsHtInspectionOrder();
            BeanUtils.copyProperties(qmsInspectionOrder, qmsHtInspectionOrder);
            list.add(qmsHtInspectionOrder);

            Example example1 = new Example(QmsInspectionOrderDet.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("inspectionOrderId", qmsInspectionOrder.getInspectionOrderId());
            List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetMapper.selectByExample(example1);
            for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets) {
                //删除检验单明细样本
                Example example2 = new Example(QmsInspectionOrderDetSample.class);
                Example.Criteria criteria2 = example2.createCriteria();
                criteria2.andEqualTo("inspectionOrderDetId", qmsInspectionOrderDet.getInspectionOrderDetId());
                qmsInspectionOrderDetSampleMapper.deleteByExample(example2);
            }
            //删除检验单明细
            qmsInspectionOrderDetMapper.deleteByExample(example1);

            //将库存的检验单号置空
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
        //履历
        qmsHtInspectionOrderMapper.insertList(list);

        return qmsInspectionOrderMapper.deleteByIds(ids);
    }

    public Map<String, List<WmsInnerInventoryDetDto>> groupInventoryDet(List<WmsInnerInventoryDetDto> innerInventoryDetDtos) {
        //分组：1、PO号不为空：PO号相同的同一组；
        // 2、PO号为空但销售订单号不为空：销售订单号相同的同一组；
        // 3、PO号和销售订单号为空：两者都为空的同一组；（option4为PO号，option3为销售编码）
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
        //获取库存明细所有待检的条码
        SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
        searchBaseInventoryStatus.setOrgId(user.getOrganizationId());
        //    searchBaseInventoryStatus.setInventoryStatusName("待检");
        List<BaseInventoryStatus> inventoryStatus = baseFeignApi.findList(searchBaseInventoryStatus).getData();
        if (StringUtils.isEmpty(inventoryStatus)) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "未查询带待检的检验状态");
        }
        Long wait = null;
        Long qualified = null;
        Long noQualified = null;
        for (BaseInventoryStatus status : inventoryStatus) {
            if ("待检".equals(status.getInventoryStatusName())) {
                wait = status.getInventoryStatusId();
            }
            if ("合格".equals(status.getInventoryStatusName())) {
                qualified = status.getInventoryStatusId();
            }
            if ("不合格".equals(status.getInventoryStatusName())) {
                noQualified = status.getInventoryStatusId();
            }
        }

        long inventoryStatusTime = System.currentTimeMillis();
        log.info("============= 查询库存状态耗时：" + (inventoryStatusTime - start));

        //只查询不属于三星仓的库存明细
        Map<String, Object> map = new HashMap<>();
        map.put("inventoryStatusId", wait);
        map.put("barcodeStatus", "3");
        map.put("logicCode", "C149");
        map.put("notEqualMark", 1);
        map.put("ifInspectionOrderCodeNull", 1);
        List<WmsInnerInventoryDetDto> wmsInnerInventoryDetDtos = qmsInspectionOrderMapper.findInventoryDetList(map);

        long inventoryDetsTime = System.currentTimeMillis();
        log.info("============= 查询库存明细耗时：" + (inventoryDetsTime - inventoryStatusTime));

        //List<WmsInnerInventoryDetDto> innerInventoryDetDtos=wmsInnerInventoryDetDtos.stream().filter(item -> item.getInspectionOrderCode()==null).collect(Collectors.toList());
        //库存明细按PO和销售订单号和物料进行分组
        Map<String, List<WmsInnerInventoryDetDto>> collect = newGroupInventoryDet(wmsInnerInventoryDetDtos);

        long collectTime = System.currentTimeMillis();
        log.info("============= 库存明细按PO和销售订单号和物料进行分组耗时：" + (collectTime - inventoryDetsTime));

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
                    log.info("============= 查询qmsInspectionOrder耗时：" + (qmsInspectionOrderTime - earchTime));
                    //入库数量（合格+待检数量）
                    List<WmsInnerInventoryDetDto> qualifiedInventoryDetDtos = innerFeignApi.findList(searchQualifiedInventoryDet).getData();
                    long qualifiedInventoryDetTime = System.currentTimeMillis();
                    log.info("============= 查询库存明细耗时：" + (qualifiedInventoryDetTime - qmsInspectionOrderTime));

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
                        //新建检验单
                        qmsInspectionOrder.setMaterialId(detDtos.get(0).getMaterialId());
                        if (StringUtils.isNotEmpty(detDtos.get(0).getOption4())) {
                            SearchOmSalesCodeReSpc searchOmSalesCodeReSpc = new SearchOmSalesCodeReSpc();
                            searchOmSalesCodeReSpc.setSamePackageCode(detDtos.get(0).getOption4());
                            searchOmSalesCodeReSpc.setPageSize(99999);
                            List<OmSalesCodeReSpcDto> omSalesCodeReSpcDtos = oMFeignApi.findList(searchOmSalesCodeReSpc).getData();
                            if (StringUtils.isEmpty(omSalesCodeReSpcDtos)) {
//                                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "未查询到对应的PO号:" + detDtos.get(0).getOption4());
                                 continue;
                            }
                            qmsInspectionOrder.setOrderQty(omSalesCodeReSpcDtos.get(0).getSamePackageCodeQty());
                            qmsInspectionOrder.setInventoryQty(new BigDecimal(qualifiedInventoryDetDtos.size()));
                        } else if (StringUtils.isNotEmpty(detDtos.get(0).getOption3()) && StringUtils.isEmpty(detDtos.get(0).getOption4())) {
                            SearchOmSalesOrderDetDto searchOmSalesOrderDetDto = new SearchOmSalesOrderDetDto();
                            searchOmSalesOrderDetDto.setSalesCode(detDtos.get(0).getOption3());
                            List<OmSalesOrderDetDto> omSalesOrderDetDtos = oMFeignApi.findList(searchOmSalesOrderDetDto).getData();
                            if (StringUtils.isEmpty(omSalesOrderDetDtos)){
//                                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "未查询到对应的销售编码:" + detDtos.get(0).getOption3());
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
                    log.info("============= 检验单处理耗时：" + (createQmsInspectionOrderTime - qualifiedInventoryDetTime));

                    //库存、库存明细写入检验单号
                    writeInspectionOrderCode(qmsInspectionOrder, detDtos, qualified, noQualified);

                    long writeInspectionOrderCodeTime = System.currentTimeMillis();
                    log.info("============= 库存、库存明细写入检验单号耗时：" + (writeInspectionOrderCodeTime - createQmsInspectionOrderTime));
                }
            }
        }

        long sanxingTime = System.currentTimeMillis();
        //处理三星仓库物料,统一按照无PO号和销售编码处理
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
        log.info("============= 查询三星库存明细耗时：" + (sanxingInnerInventoryDetTime - sanxingTime));

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
                            //新建检验单
                            qmsInspectionOrder1.setMaterialId(detDtos.get(0).getMaterialId());
                            qmsInspectionOrder1.setOrderQty(new BigDecimal(detDtos.size()));
                            qmsInspectionOrder1.setInventoryQty(new BigDecimal(detDtos.size()));
                            createQmsInspectionOrder(qmsInspectionOrder1, detDtos);
                        }

                        //库存、库存明细写入检验单号
                        //     writeInspectionOrderCode(qmsInspectionOrder1, detDtos, qualified, noQualified);
                    } else {
                        //新建检验单
                        qmsInspectionOrder1.setMaterialId(detDtos.get(0).getMaterialId());
                        qmsInspectionOrder1.setOrderQty(new BigDecimal(detDtos.size()));
                        qmsInspectionOrder1.setInventoryQty(new BigDecimal(detDtos.size()));
                        createQmsInspectionOrder(qmsInspectionOrder1, detDtos);
                    }
                    //库存、库存明细写入检验单号
                    writeInspectionOrderCode(qmsInspectionOrder1, detDtos, qualified, noQualified);
                }
            }

            long sanxingxunhuanTime = System.currentTimeMillis();
            log.info("============= 三星循环处理耗时：" + (sanxingxunhuanTime - sanxingInnerInventoryDetTime));
        }
        long end = System.currentTimeMillis();
        log.info("============= 总耗时：" + (end - start));
        return 1;
    }

    public void createQmsInspectionOrder(QmsInspectionOrder qmsInspectionOrder, List<WmsInnerInventoryDetDto> detDtos) {
        qmsInspectionOrder.setInspectionStatus((byte) 1);
        SearchBaseInspectionWay searchBaseInspectionWay = new SearchBaseInspectionWay();
        searchBaseInspectionWay.setInspectionWayDesc("万宝检验方式");
        searchBaseInspectionWay.setQueryMark(1);
        List<BaseInspectionWay> inspectionWays = baseFeignApi.findList(searchBaseInspectionWay).getData();
        if (StringUtils.isEmpty(inspectionWays)) {
            throw new BizErrorException("未维护正常的检验方式");
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
            //查询通用的检验方式
            SearchBaseInspectionStandard searchBaseInspectionStandard1 = new SearchBaseInspectionStandard();
            searchBaseInspectionStandard1.setMaterialId((long) 0);
            searchBaseInspectionStandard1.setInspectionType((byte) 2);
            inspectionStandardList = baseFeignApi.findList(searchBaseInspectionStandard1).getData();
            if (StringUtils.isEmpty(inspectionStandardList))
                throw new BizErrorException("未查到检验标准");
        }

        qmsInspectionOrder.setInspectionStandardId(inspectionStandardList.get(0).getInspectionStandardId());
        qmsInspectionOrder.setSalesCode(detDtos.get(0).getOption3());
        qmsInspectionOrder.setSamePackageCode(detDtos.get(0).getOption4());
        //明细
        List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetService.showOrderDet(qmsInspectionOrder.getInspectionStandardId(), qmsInspectionOrder.getOrderQty());
        qmsInspectionOrder.setQmsInspectionOrderDets(qmsInspectionOrderDets);
        this.add(qmsInspectionOrder);

    }

    public void writeInspectionOrderCode(QmsInspectionOrder qmsInspectionOrder, List<WmsInnerInventoryDetDto> detDtos, Long qualified, Long noQualified) {
        long start = System.currentTimeMillis();
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        //对应的库存明细写入质检单号
        if (StringUtils.isNotEmpty(detDtos)) {
            log.info("============= detDtos数量：" + detDtos.size());
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
            log.info("============= 循环detDtos耗时：" + (eachInventoryDetTime - start));
            if (StringUtils.isNotEmpty(list)){
                ResponseEntity update = innerFeignApi.batchUpdate(list);
                if (StringUtils.isNotEmpty(update) && update.getCode() != 0) {
                    throw new BizErrorException("更新库存明细失败");
                }
            }
            long inventoryDetTime = System.currentTimeMillis();
            log.info("============= 库存明细批量修改耗时：" + (inventoryDetTime - eachInventoryDetTime));
        }
        long updateInventoryDetTime = System.currentTimeMillis();
        log.info("============= 库存明细写入质检单号耗时：" + (updateInventoryDetTime - start));

        //锁定所有待检库存
        //同一检验单号是否有待检状态的库存
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
        log.info("============= 查询质检库存耗时：" + (dtoListTime - updateInventoryDetTime));

        SearchWmsInnerInventory searchWmsInnerInventory = new SearchWmsInnerInventory();
        searchWmsInnerInventory.setStorageId(detDtos.get(0).getStorageId());
        searchWmsInnerInventory.setInventoryStatusId(detDtos.get(0).getInventoryStatusId());
        searchWmsInnerInventory.setMaterialId(detDtos.get(0).getMaterialId());
        searchWmsInnerInventory.setJobStatus((byte) 1);
        searchWmsInnerInventory.setQcLock((byte) 0);
        searchWmsInnerInventory.setStockLock((byte) 0);
        List<WmsInnerInventoryDto> list = innerFeignApi.findList(searchWmsInnerInventory).getData();
        long listTime = System.currentTimeMillis();
        log.info("============= 查询正常库存耗时：" + (listTime - dtoListTime));

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
                throw new BizErrorException("质检合并库存失败");
            }
        }
        if (StringUtils.isNotEmpty(updateList)){
            // 批量修改库存
            ResponseEntity responseEntity = innerFeignApi.batchUpdateInventory(updateList);
            if (StringUtils.isNotEmpty(responseEntity) && responseEntity.getCode() != 0) {
                throw new BizErrorException("更新库存明细失败");
            }
        }

        long end = System.currentTimeMillis();
        log.info("============= 循环修改耗时：" + (end - listTime));
        log.info("============= writeInspectionOrderCode总耗时：" + (end - start));
    }

    public Map<String, List<WmsInnerInventoryDetDto>> newGroupInventoryDet(List<WmsInnerInventoryDetDto> innerInventoryDetDtos) {
        //分组：1、PO号不为空：PO号相同的同一组；
        // 2、PO号为空但销售订单号不为空：销售订单号相同的同一组；
        // 3、PO号和销售订单号为空：两者都为空的同一组  //Option3是销售订单号、Option4是PO号
        Map<String, List<WmsInnerInventoryDetDto>> collect = new HashMap<>();
        for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : innerInventoryDetDtos) {
            if (StringUtils.isEmpty(wmsInnerInventoryDetDto.getOption4())) {

                List<WmsInnerInventoryDetDto> inventoryDetDtos = new LinkedList<>();
                boolean tag = false;
                if (StringUtils.isNotEmpty(wmsInnerInventoryDetDto.getOption3())) {
                    //PO号为空的情况、销售订单号不为空
                    if (collect.containsKey(wmsInnerInventoryDetDto.getOption3())) {
                        inventoryDetDtos = collect.get(wmsInnerInventoryDetDto.getOption3());
                    }
                    inventoryDetDtos.add(wmsInnerInventoryDetDto);
                    collect.put(wmsInnerInventoryDetDto.getOption3(), inventoryDetDtos);
                } else {
                    tag = true;
                }

                if (tag) {
                    //PO号为空的情况、销售订单号为空
                    if (collect.containsKey(wmsInnerInventoryDetDto.getMaterialCode())) {
                        inventoryDetDtos = collect.get(wmsInnerInventoryDetDto.getMaterialCode());
                    }
                    inventoryDetDtos.add(wmsInnerInventoryDetDto);
                    collect.put(wmsInnerInventoryDetDto.getMaterialCode(), inventoryDetDtos);
                }
            } else {

                //PO号不为空的情况
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
        //移入库位默认为质检专用库位
        Long inStorageId = null;
        Long warehouseId = null;
        String storageCode = "Z-QC";
        SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
        searchBaseStorage.setStorageCode(storageCode);
        List<BaseStorage> storageDtoList = baseFeignApi.findList(searchBaseStorage).getData();
        if (StringUtils.isEmpty(storageDtoList) || storageDtoList.size() <= 0) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "未找到质检专用库位");
        }
        inStorageId = storageDtoList.get(0).getStorageId();
        warehouseId = storageDtoList.get(0).getWarehouseId();

        SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
        searchBaseInventoryStatus.setWarehouseId(warehouseId);
        searchBaseInventoryStatus.setOrgId(user.getOrganizationId());
        List<BaseInventoryStatus> inventoryStatusList = baseFeignApi.findList(searchBaseInventoryStatus).getData();
        List<BaseInventoryStatus> statusList = inventoryStatusList.stream().filter(item -> item.getInventoryStatusName().equals("待检")).collect(Collectors.toList());
        if (StringUtils.isEmpty(statusList) || statusList.size() <= 0) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "未找到仓库的待检状态");
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
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "样本数必须大于0");
            }

            String orderCode = qmsInspectionOrder.getInspectionOrderCode();
            SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
            searchWmsInnerJobOrder.setRelatedOrderCode(orderCode);
            List<WmsInnerJobOrderDto> list = innerFeignApi.findList(searchWmsInnerJobOrder).getData();
            if (StringUtils.isNotEmpty(list) && list.size() > 0) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "成品检验单-->" + orderCode + " 已经生成质检移位单!");
            }

            SearchWmsInnerInventoryDet searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
            searchWmsInnerInventoryDet.setMaterialId(qmsInspectionOrder.getMaterialId());
            searchWmsInnerInventoryDet.setInspectionOrderCode(qmsInspectionOrder.getInspectionOrderCode());
            List<WmsInnerInventoryDetDto> inventoryDetDtos = innerFeignApi.findList(searchWmsInnerInventoryDet).getData();
            if (StringUtils.isEmpty(inventoryDetDtos)) {
                throw new BizErrorException("未找到成品检验单的条码明细信息-->" + orderCode);
            }

            Long outStorageId = inventoryDetDtos.get(0).getStorageId();

            //找物料库存
            // inventoryStatusId: 132
            //inventoryStatusName: "待检"
            Long materialId = qmsInspectionOrder.getMaterialId();
            SearchWmsInnerInventory searchWmsInnerInventory = new SearchWmsInnerInventory();
            searchWmsInnerInventory.setMaterialId(materialId);
            searchWmsInnerInventory.setStorageId(outStorageId);
            searchWmsInnerInventory.setLockStatus((byte) 0);
            searchWmsInnerInventory.setJobStatus((byte) 1);
            searchWmsInnerInventory.setInventoryStatusName("待检");
            List<WmsInnerInventoryDto> inventoryDtos = innerFeignApi.findList(searchWmsInnerInventory).getData();
            if (StringUtils.isEmpty(inventoryDtos) || inventoryDtos.size() <= 0) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "未找到待检库存");
            }
            BigDecimal qty = sampleQty;
            List<WmsInnerInventoryDto> dtoList = inventoryDtos.stream().filter(u -> (u.getPackingQty().compareTo(qty) >= 0)).collect(Collectors.toList());
            if (StringUtils.isEmpty(dtoList) || dtoList.size() <= 0) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "未找到大于等于样板数的待检库存");
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
                    throw new BizErrorException("生成质检移位单失败");
                }
            }
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int recheckByBarcode(String barcode) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        log.info("===================== recheckByBarcode开始执行 =====================");
        Map<String, Object> map = new HashMap<>();
        map.put("factoryBarcode", barcode);
        map.put("barcodeStatus", 0);
        List<QmsInspectionOrderDetSample> orderDetSamples = qmsInspectionOrderDetSampleMapper.findList(map);
        log.info("===================== orderDetSamples： " + JSON.toJSONString(orderDetSamples));
        if (orderDetSamples.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "此条码不是质检条码");
        }
        // 修改样本值
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

        //扣减该条码所有检验项目不良数量以及变更其状态
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
        // 修改成品检验单表头状态
        Example example = new Example(QmsInspectionOrderDetSample.class);
        example.createCriteria().andEqualTo("sampleValue", "NG").andIn("inspectionOrderDetId", inspectionOrderDetIds);
        int count = qmsInspectionOrderDetSampleMapper.selectCountByExample(example);
        QmsInspectionOrder qmsInspectionOrder = qmsInspectionOrderMapper.selectByPrimaryKey(inspectionOrderId);

        log.info("===================== 数量 " + (count - 1));
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
        log.info("===================== recheckByBarcode结束 =====================");
        return num;
    }
}
