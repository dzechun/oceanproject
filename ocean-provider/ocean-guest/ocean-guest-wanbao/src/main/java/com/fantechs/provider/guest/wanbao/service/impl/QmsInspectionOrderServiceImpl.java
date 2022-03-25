package com.fantechs.provider.guest.wanbao.service.impl;

import com.alibaba.fastjson.JSON;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseTabDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcBarcodeProcessDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcess;
import com.fantechs.common.base.general.dto.om.OmSalesCodeReSpcDto;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDetDto;
import com.fantechs.common.base.general.dto.om.SearchOmSalesOrderDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
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
 *
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
    private QmsInspectionOrderDetServiceImpl qmsInspectionOrderDetService;

    @Override
    public List<QmsInspectionOrder> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        List<QmsInspectionOrder> qmsInspectionOrders = qmsInspectionOrderMapper.findList(map);
        return qmsInspectionOrders;

        //新数据源
        /*SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        List<QmsInspectionOrder> resultList=new ArrayList<>();
        List<QmsInspectionOrder> qmsInspectionOrders = qmsInspectionOrderMapper.findList(map);
        for (QmsInspectionOrder qmsInspectionOrder : qmsInspectionOrders) {
            String orderCode=qmsInspectionOrder.getInspectionOrderCode();
            SearchWmsInnerJobOrder searchWmsInnerJobOrder=new SearchWmsInnerJobOrder();
            searchWmsInnerJobOrder.setRelatedOrderCode(orderCode);
            List<WmsInnerJobOrderDto> list=innerFeignApi.findList(searchWmsInnerJobOrder).getData();
            if(StringUtils.isNotEmpty(list) && list.size()>0){
                Long jobOrderId=list.get(0).getJobOrderId();
                SearchWmsInnerJobOrderDet sJobOrderDet=new SearchWmsInnerJobOrderDet();
                sJobOrderDet.setJobOrderId(jobOrderId);
                List<WmsInnerJobOrderDetDto> jobOrderDetDtos=innerFeignApi.findList(sJobOrderDet).getData();
                for (WmsInnerJobOrderDetDto jobOrderDetDto : jobOrderDetDtos) {
                    QmsInspectionOrder newQmsInspectionOrder=new QmsInspectionOrder();
                    BeanUtil.copyProperties(qmsInspectionOrder,newQmsInspectionOrder);
                    newQmsInspectionOrder.setOrderQty(jobOrderDetDto.getPlanQty());
                    newQmsInspectionOrder.setJobOrderDetId(jobOrderDetDto.getJobOrderDetId());
                    resultList.add(newQmsInspectionOrder);
                }
            }
        }
        return resultList;*/
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSampleQty(Long inspectionOrderId,BigDecimal sampleQty){
        int i = 0;
        Example example = new Example(QmsInspectionOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("inspectionOrderId",inspectionOrderId);
        List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetMapper.selectByExample(example);

        for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets){
            qmsInspectionOrderDet.setSampleQty(sampleQty);
            i += qmsInspectionOrderDetMapper.updateByPrimaryKeySelective(qmsInspectionOrderDet);
        }

        return i;
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
        for (QmsInspectionOrderDetSample DetSample : qmsInspectionOrderDetSamples) {
            DetSample.setBarcodeStatus((byte)1);
            DetSample.setModifiedUserId(user.getUserId());
            DetSample.setModifiedTime(new Date());
            qmsInspectionOrderDetSampleMapper.updateByPrimaryKeySelective(DetSample);
        }

        List<QmsInspectionOrderDetSample> inspectionOrderDetSampleList = new LinkedList<>();
        for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets){
            //明细
            qmsInspectionOrderDet.setBadnessQty(BigDecimal.ZERO);
            qmsInspectionOrderDet.setInspectionResult((byte)1);
            qmsInspectionOrderDetMapper.updateByPrimaryKeySelective(qmsInspectionOrderDet);

            //样本
            for(QmsInspectionOrderDetSample qmsInspectionOrderDetSample : qmsInspectionOrderDetSamples){
                QmsInspectionOrderDetSample inspectionOrderDetSample = new QmsInspectionOrderDetSample();
                inspectionOrderDetSample.setInspectionOrderDetId(qmsInspectionOrderDet.getInspectionOrderDetId());
                inspectionOrderDetSample.setBarcode(qmsInspectionOrderDetSample.getBarcode());
                inspectionOrderDetSample.setBarcodeStatus((byte)1);
                inspectionOrderDetSample.setSampleValue("OK");
                inspectionOrderDetSample.setOrgId(user.getOrganizationId());
                inspectionOrderDetSample.setInspectionOrderId(inspectionOrderId);
                inspectionOrderDetSampleList.add(inspectionOrderDetSample);
            }
        }
        if(StringUtils.isNotEmpty(inspectionOrderDetSampleList)) {
            qmsInspectionOrderDetSampleMapper.insertList(inspectionOrderDetSampleList);
        }

        qmsInspectionOrder.setInspectionStatus((byte)3);
        qmsInspectionOrder.setInspectionResult((byte)1);
        qmsInspectionOrder.setInspectionUserId(user.getUserId());
        int i = qmsInspectionOrderMapper.updateByPrimaryKeySelective(qmsInspectionOrder);

        //处理库存
        this.handleInventory(qmsInspectionOrder.getInspectionOrderCode(),qmsInspectionOrder.getInspectionResult());

        //生成移位单
        createJobOrderShift(qmsInspectionOrderDetSamples,qmsInspectionOrder,user);

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
            //明细
            if(StringUtils.isEmpty(qmsInspectionOrderDet.getInspectionResult())) {
                qmsInspectionOrderDet.setBadnessQty(BigDecimal.ZERO);
                qmsInspectionOrderDet.setInspectionResult((byte) 1);
                qmsInspectionOrderDet.setUnitName("台");
                qmsInspectionOrderDetMapper.updateByPrimaryKeySelective(qmsInspectionOrderDet);

                //样本
                for(QmsInspectionOrderDetSample qmsInspectionOrderDetSample : qmsInspectionOrderDetSamples){
                    QmsInspectionOrderDetSample inspectionOrderDetSample = new QmsInspectionOrderDetSample();
                    inspectionOrderDetSample.setInspectionOrderDetId(qmsInspectionOrderDet.getInspectionOrderDetId());
                    inspectionOrderDetSample.setBarcode(qmsInspectionOrderDetSample.getBarcode());
                    inspectionOrderDetSample.setSampleValue("OK");
                    inspectionOrderDetSample.setOrgId(user.getOrganizationId());
                    inspectionOrderDetSample.setInspectionOrderId(inspectionOrderId);
                    inspectionOrderDetSampleList.add(inspectionOrderDetSample);
                }
            }
        }
        if(StringUtils.isNotEmpty(inspectionOrderDetSampleList)) {
            qmsInspectionOrderDetSampleMapper.insertList(inspectionOrderDetSampleList);
        }


        List<QmsInspectionOrderDetSample> unQualifiedBarcodes = qmsInspectionOrderDetSamples.stream().filter(item -> item.getBarcodeStatus()!=null && item.getBarcodeStatus() == 0).collect(Collectors.toList());
        if(qmsInspectionOrderDetSamples.size() == unQualifiedBarcodes.size()){
            qmsInspectionOrder.setInspectionResult((byte)0);
        }else if(unQualifiedBarcodes.size() == 0){
            qmsInspectionOrder.setInspectionResult((byte)1);
        } else{
            qmsInspectionOrder.setInspectionResult((byte)2);
        }

        qmsInspectionOrder.setInspectionStatus((byte)3);
        qmsInspectionOrder.setInspectionUserId(user.getUserId());
        int i = qmsInspectionOrderMapper.updateByPrimaryKeySelective(qmsInspectionOrder);

        //处理库存
        if(qmsInspectionOrder.getInspectionResult() == (byte)0||qmsInspectionOrder.getInspectionResult() == (byte)1) {
            this.handleInventory(qmsInspectionOrder.getInspectionOrderCode(), qmsInspectionOrder.getInspectionResult());
        }else if(qmsInspectionOrder.getInspectionResult() == (byte)2){
            this.splitInventory(qmsInspectionOrder.getInspectionOrderCode(), unQualifiedBarcodes);
        }

        //生成移位单
        createJobOrderShift(qmsInspectionOrderDetSamples,qmsInspectionOrder,user);

        return i;
    }

    private int createJobOrderShift(List<QmsInspectionOrderDetSample> list,QmsInspectionOrder qmsInspectionOrder,SysUser user) {
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

        SearchBaseTab searchBaseTab=new SearchBaseTab();
        searchBaseTab.setMaterialId(materialId);
        List<BaseTabDto> tabList=baseFeignApi.findTabList(searchBaseTab).getData();

        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setRelatedOrderCode(orderCode);
        searchWmsInnerJobOrder.setOption1("qmsCommit");
        List<WmsInnerJobOrderDto> jobOrderDtoList = innerFeignApi.findList(searchWmsInnerJobOrder).getData();
        if (StringUtils.isNotEmpty(jobOrderDtoList) && jobOrderDtoList.size() > 0) {
            //复检提交 对已生成的质检移位单做处理
            List<QmsInspectionOrderDetSample> ngQualifiedBarcodes = list.stream().filter(item -> item.getBarcodeStatus() != null && item.getBarcodeStatus() == 0).collect(Collectors.toList());
            BigDecimal ngQty = new BigDecimal(ngQualifiedBarcodes.size());
            innerFeignApi.updateShit(jobOrderDtoList.get(0).getJobOrderId(),ngQty);

        } else {
            //找成品检验对应的质检移位单
            searchWmsInnerJobOrder.setOption1("qmsToInnerJobShift");
            jobOrderDtoList = innerFeignApi.findList(searchWmsInnerJobOrder).getData();
            if (StringUtils.isNotEmpty(jobOrderDtoList) && jobOrderDtoList.size() > 0) {
                SearchWmsInnerJobOrderDet sDet = new SearchWmsInnerJobOrderDet();
                sDet.setJobOrderId(jobOrderDtoList.get(0).getJobOrderId());
                List<WmsInnerJobOrderDetDto> detDtoList = innerFeignApi.findList(sDet).getData();
                if (StringUtils.isNotEmpty(detDtoList) && detDtoList.size() > 0) {
                    inStorageId = detDtoList.get(0).getOutStorageId();
                    outStorageId = detDtoList.get(0).getInStorageId();

                    SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
                    searchBaseStorage.setStorageId(inStorageId);
                    List<BaseStorage> storageDtoList = baseFeignApi.findList(searchBaseStorage).getData();
                    if (StringUtils.isNotEmpty(storageDtoList) && storageDtoList.size() > 0) {
                        warehouseId = storageDtoList.get(0).getWarehouseId();
                    }
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
            if(StringUtils.isNotEmpty(proLineId)) {
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

            SearchWmsInnerInventory searchWmsInnerInventory = new SearchWmsInnerInventory();
            searchWmsInnerInventory.setMaterialId(materialId);
            searchWmsInnerInventory.setStorageId(outStorageId);
            searchWmsInnerInventory.setLockStatus((byte) 0);
            searchWmsInnerInventory.setJobStatus((byte) 1);
            searchWmsInnerInventory.setInspectionOrderCode(orderCode);
            List<WmsInnerInventoryDto> inventoryDtos = innerFeignApi.findList(searchWmsInnerInventory).getData();
            if (StringUtils.isNotEmpty(inventoryDtos)) {
                List<WmsInnerInventoryDto> dtoList=inventoryDtos.stream().filter(item -> item.getPackingQty() != null && item.getPackingQty().compareTo(new BigDecimal(0))==1).collect(Collectors.toList());

                log.info("============= 库存数据" + JSON.toJSONString(dtoList));

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
                    if(dtoList.size()>0) {
                        wmsInnerJobOrderDet.setSourceDetId(dtoList.get(0).getInventoryId());
                    }
                    wmsInnerJobOrderDet.setOrderStatus((byte) 3);
                    if (statusList.size() > 0) {
                        wmsInnerJobOrderDet.setInventoryStatusId(statusList.get(0).getInventoryStatusId());
                    }
                    if(StringUtils.isNotEmpty(tabList) && tabList.size()>0){
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
                    if(dtoList.size()>0) {
                        wmsInnerJobOrderDet.setSourceDetId(dtoList.get(0).getInventoryId());
                    }
                    wmsInnerJobOrderDet.setOrderStatus((byte) 3);
                    if (statusList.size() > 0) {
                        wmsInnerJobOrderDet.setInventoryStatusId(statusList.get(0).getInventoryStatusId());
                    }
                    if(StringUtils.isNotEmpty(tabList) && tabList.size()>0){
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
            }

        }

        return i;
    }

    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int splitInventory(String inspectionOrderCode,List<QmsInspectionOrderDetSample> unQualifiedBarcodes){
        SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
        searchBaseInventoryStatus.setInventoryStatusName("不合格");
        searchBaseInventoryStatus.setNameQueryMark(1);
        List<BaseInventoryStatus> inventoryStatusList1 = baseFeignApi.findList(searchBaseInventoryStatus).getData();
        if(StringUtils.isEmpty(inventoryStatusList1)){
            throw new BizErrorException("未查询到对应库存状态");
        }

        searchBaseInventoryStatus.setInventoryStatusName("合格");
        List<BaseInventoryStatus> inventoryStatusList2 = baseFeignApi.findList(searchBaseInventoryStatus).getData();
        if(StringUtils.isEmpty(inventoryStatusList2)){
            throw new BizErrorException("未查询到对应库存状态");
        }

        //检验结果返写回库存明细
        int unQualifiedCount = 0;
        SearchWmsInnerInventoryDet  searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
        searchWmsInnerInventoryDet.setPageSize(999);
        searchWmsInnerInventoryDet.setInspectionOrderCode(inspectionOrderCode);
        List<WmsInnerInventoryDetDto> inventoryDetDtos = innerFeignApi.findList(searchWmsInnerInventoryDet).getData();
        if(StringUtils.isNotEmpty(inventoryDetDtos)) {
            for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : inventoryDetDtos) {
                wmsInnerInventoryDetDto.setInventoryStatusId(inventoryStatusList2.get(0).getInventoryStatusId());
                for (QmsInspectionOrderDetSample qmsInspectionOrderDetSample : unQualifiedBarcodes){
                    if(wmsInnerInventoryDetDto.getBarcode().equals(qmsInspectionOrderDetSample.getFactoryBarcode())){
                        wmsInnerInventoryDetDto.setInventoryStatusId(inventoryStatusList1.get(0).getInventoryStatusId());
                        unQualifiedCount++;
                    }
                }
                wmsInnerInventoryDetDto.setQcDate(new Date());
                innerFeignApi.update(wmsInnerInventoryDetDto);
            }
        }

        //检验结果返写回库存
        SearchWmsInnerInventory  searchWmsInnerInventory = new SearchWmsInnerInventory();
        searchWmsInnerInventory.setInspectionOrderCode(inspectionOrderCode);
        ResponseEntity<List<WmsInnerInventoryDto>> innerInventoryDtoList = innerFeignApi.findList(searchWmsInnerInventory);
        if(StringUtils.isEmpty(innerInventoryDtoList.getData())){
            throw new BizErrorException("未查询到对应库存信息");
        }
        WmsInnerInventoryDto wmsInnerInventoryDto = new WmsInnerInventoryDto();
        BeanUtils.copyProperties(innerInventoryDtoList.getData().get(0),wmsInnerInventoryDto);
        WmsInnerInventoryDto innerInventoryDto = new WmsInnerInventoryDto();
        BeanUtils.copyProperties(innerInventoryDtoList.getData().get(0),innerInventoryDto);

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
        innerInventoryDto.setQcLock((byte)0);
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

        handleInventory(qmsInspectionOrder.getInspectionOrderCode(),qmsInspectionOrder.getInspectionResult());

        qmsInspectionOrder.setIfThirdInspection(StringUtils.isEmpty(qmsInspectionOrder.getIfThirdInspection()) ? 1 :qmsInspectionOrder.getIfThirdInspection());
        qmsInspectionOrder.setInspectionStatus((byte)3);
        qmsInspectionOrder.setInspectionUserId(user.getUserId());
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
        int i = 0;
        //手动添加变更质检锁
        List<WmsInnerInventoryDetDto> wmsInnerInventoryDetDtos= new LinkedList<>();
        List<WmsInnerInventoryDto> wmsInnerInventoryDtos= new LinkedList<>();
        if(StringUtils.isNotEmpty(qmsInspectionOrder.getInventoryIds())){
            String[] ids = qmsInspectionOrder.getInventoryIds().split(",");
            for(String id : ids){
                SearchWmsInnerInventory  searchWmsInnerInventory = new SearchWmsInnerInventory();
                searchWmsInnerInventory.setInventoryId(Long.valueOf(id).longValue());
                ResponseEntity<List<WmsInnerInventoryDto>> innerInventoryDtoList = innerFeignApi.findList(searchWmsInnerInventory);
                if(StringUtils.isEmpty(innerInventoryDtoList.getData())) throw new BizErrorException("未查询到对应库存信息");
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
            if(StringUtils.isEmpty(wmsInnerInventoryDetDtos)){
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
                    for (WmsInnerInventoryDto wmsInnerInventory : wmsInnerInventoryDtos){
                        if(detDtoList.get(0).getStorageId().equals(wmsInnerInventory.getStorageId())
                        &&detDtoList.get(0).getInventoryStatusId().equals(wmsInnerInventory.getInventoryStatusId())
                        &&detDtoList.get(0).getMaterialId().equals(wmsInnerInventory.getMaterialId())){
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
        qmsInspectionOrder.setAuditStatus(StringUtils.isEmpty(qmsInspectionOrder.getAuditStatus())?0:qmsInspectionOrder.getAuditStatus());
        qmsInspectionOrder.setStatus(StringUtils.isEmpty(qmsInspectionOrder.getStatus())?1:qmsInspectionOrder.getStatus());
        qmsInspectionOrder.setInspectionStatus((byte)1);
        qmsInspectionOrder.setOrgId(user.getOrganizationId());
        int i = qmsInspectionOrderMapper.insertUseGeneratedKeys(qmsInspectionOrder);

        //履历
        QmsHtInspectionOrder qmsHtInspectionOrder = new QmsHtInspectionOrder();
        BeanUtils.copyProperties(qmsInspectionOrder, qmsHtInspectionOrder);
        qmsHtInspectionOrderMapper.insertSelective(qmsHtInspectionOrder);

        //明细
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

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int update(QmsInspectionOrder qmsInspectionOrder,Byte type) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //生成的质检移位单判断
        Long jobOrderId=null;
        if(type!=(byte)0) {
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
        int i=qmsInspectionOrderMapper.updateByPrimaryKeySelective(qmsInspectionOrder);

        //履历
        QmsHtInspectionOrder qmsHtInspectionOrder = new QmsHtInspectionOrder();
        BeanUtils.copyProperties(qmsInspectionOrder, qmsHtInspectionOrder);
        qmsHtInspectionOrderMapper.insertSelective(qmsHtInspectionOrder);

        //原来有的明细只更新
        ArrayList<Long> idList = new ArrayList<>();
        List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrder.getQmsInspectionOrderDets();
        if(StringUtils.isNotEmpty(qmsInspectionOrderDets)) {
            for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets) {
                if (StringUtils.isNotEmpty(qmsInspectionOrderDet.getInspectionOrderDetId())) {
                    qmsInspectionOrderDet.setSampleQty(StringUtils.isNotEmpty(qmsInspectionOrder.getSampleQty())?qmsInspectionOrder.getSampleQty():qmsInspectionOrderDet.getSampleQty());
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
                qmsInspectionOrderDet.setSampleQty(StringUtils.isNotEmpty(qmsInspectionOrder.getSampleQty())?qmsInspectionOrder.getSampleQty():qmsInspectionOrderDet.getSampleQty());
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
        if(StringUtils.isNotEmpty(jobOrderId) && StringUtils.isNotEmpty(qty) && type!=(byte)0) {
            innerFeignApi.reCreateInnerJobShift(jobOrderId,qty);
        }
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

        //计算明细项目合格数与不合格数
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
        //检验结果返写回库存
        SearchWmsInnerInventory  searchWmsInnerInventory = new SearchWmsInnerInventory();
        searchWmsInnerInventory.setInspectionOrderCode(inspectionOrderCode);
        //2022/03/23 查询所有质检锁的库存 整批合格要更新库存合格状态
        searchWmsInnerInventory.setQcLock((byte)1);
                
        ResponseEntity<List<WmsInnerInventoryDto>> innerInventoryDtoList = innerFeignApi.findList(searchWmsInnerInventory);
        if(StringUtils.isEmpty(innerInventoryDtoList.getData())){
            throw new BizErrorException("未查询到对应库存信息");
        }
//        WmsInnerInventoryDto wmsInnerInventoryDto = new WmsInnerInventoryDto();
//        BeanUtils.copyProperties(innerInventoryDtoList.getData().get(0),wmsInnerInventoryDto);

        SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
        searchBaseInventoryStatus.setInventoryStatusName(inspectionResult==0 ? "不合格" : "合格");
        searchBaseInventoryStatus.setNameQueryMark(1);
        List<BaseInventoryStatus> inventoryStatusList = baseFeignApi.findList(searchBaseInventoryStatus).getData();
        if(StringUtils.isEmpty(inventoryStatusList)){
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

        List<WmsInnerInventoryDto> inventoryDtoList=innerInventoryDtoList.getData();
        for (WmsInnerInventoryDto innerInventoryDto : inventoryDtoList) {
            innerInventoryDto.setQcLock((byte)0);
            innerInventoryDto.setInventoryStatusId(inventoryStatusList.get(0).getInventoryStatusId());
            innerInventoryDto.setModifiedTime(new Date());
            innerFeignApi.update(innerInventoryDto);
        }

        //检验结果返写回库存明细
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
            if(qmsInspectionOrder.getInspectionStatus()==(byte)2||qmsInspectionOrder.getInspectionStatus()==(byte)3){
                throw new BizErrorException("检验中或检验完成的单据不可删除");
            }
            QmsHtInspectionOrder qmsHtInspectionOrder = new QmsHtInspectionOrder();
            BeanUtils.copyProperties(qmsInspectionOrder, qmsHtInspectionOrder);
            list.add(qmsHtInspectionOrder);

            Example example1 = new Example(QmsInspectionOrderDet.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("inspectionOrderId", qmsInspectionOrder.getInspectionOrderId());
            List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetMapper.selectByExample(example1);
            for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets){
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
            if(StringUtils.isNotEmpty(inventoryDetDtos)) {
                for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : inventoryDetDtos) {
                    wmsInnerInventoryDetDto.setInspectionOrderCode("");
                    innerFeignApi.update(wmsInnerInventoryDetDto);
                }
            }

            SearchWmsInnerInventory searchWmsInnerInventory = new SearchWmsInnerInventory();
            searchWmsInnerInventory.setPageSize(999);
            searchWmsInnerInventory.setInspectionOrderCode(qmsInspectionOrder.getInspectionOrderCode());
            List<WmsInnerInventoryDto> innerInventoryDtos = innerFeignApi.findList(searchWmsInnerInventory).getData();
            if(StringUtils.isNotEmpty(innerInventoryDtos)){
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

   public Map<String, List<WmsInnerInventoryDetDto>> groupInventoryDet(List<WmsInnerInventoryDetDto> innerInventoryDetDtos){
        //分组：1、PO号不为空：PO号相同的同一组；
        // 2、PO号为空但销售订单号不为空：销售订单号相同的同一组；
        // 3、PO号和销售订单号为空：两者都为空的同一组；（option4为PO号，option3为销售编码）
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
    public int newAutoAdd() {
        System.out.println("===========自动生成成品检验单定时任务============");
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        //获取库存明细所有待检的条码
        SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
        searchBaseInventoryStatus.setOrgId(user.getOrganizationId());
    //    searchBaseInventoryStatus.setInventoryStatusName("待检");
        List<BaseInventoryStatus> inventoryStatus = baseFeignApi.findList(searchBaseInventoryStatus).getData();
        if (StringUtils.isEmpty(inventoryStatus))
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "未查询带待检的检验状态");
        Long wait = null;
        Long qualified = null;
        Long noQualified = null;
        for(BaseInventoryStatus status : inventoryStatus){
            if("待检".equals(status.getInventoryStatusName()))
                wait = status.getInventoryStatusId();
            if("合格".equals(status.getInventoryStatusName()))
                qualified = status.getInventoryStatusId();
            if("不合格".equals(status.getInventoryStatusName()))
                noQualified = status.getInventoryStatusId();
        }

        //只查询不属于三星仓的库存明细
        SearchWmsInnerInventoryDet searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
        searchWmsInnerInventoryDet.setInventoryStatusId(wait);
        searchWmsInnerInventoryDet.setBarcodeStatus("3");
        searchWmsInnerInventoryDet.setLogicCode("C149");
        searchWmsInnerInventoryDet.setNotEqualMark(1);
        List<WmsInnerInventoryDetDto> wmsInnerInventoryDetDtos = innerFeignApi.findList(searchWmsInnerInventoryDet).getData();

        //库存明细按PO和销售订单号和物料进行分组
        Map<String, List<WmsInnerInventoryDetDto>> collect = newGroupInventoryDet(wmsInnerInventoryDetDtos);

        if(StringUtils.isNotEmpty(collect)) {
            Set<String> codes = collect.keySet();
            for (String code : codes) {
                List<WmsInnerInventoryDetDto> detDtos = collect.get(code);
                QmsInspectionOrder qmsInspectionOrder = new QmsInspectionOrder();

                SearchWmsInnerInventoryDet searchQualifiedInventoryDet = new SearchWmsInnerInventoryDet();
                searchQualifiedInventoryDet.setMaterialCode(detDtos.get(0).getMaterialCode());

                SearchQmsInspectionOrder searchQmsInspectionOrder = new SearchQmsInspectionOrder();
                searchQmsInspectionOrder.setMaterialCode(detDtos.get(0).getMaterialCode());
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

                //入库数量（合格+待检数量）
                List<WmsInnerInventoryDetDto> qualifiedInventoryDetDtos = innerFeignApi.findList(searchQualifiedInventoryDet).getData();

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
                    this.update(qmsInspectionOrder, (byte) 0);
                } else {
                    //新建检验单
                    qmsInspectionOrder.setMaterialId(detDtos.get(0).getMaterialId());
                    if(StringUtils.isNotEmpty(detDtos.get(0).getOption4())) {
                        SearchOmSalesCodeReSpc searchOmSalesCodeReSpc = new SearchOmSalesCodeReSpc();
                        searchOmSalesCodeReSpc.setSamePackageCode(detDtos.get(0).getOption4());
                        List<OmSalesCodeReSpcDto> omSalesCodeReSpcDtos = oMFeignApi.findList(searchOmSalesCodeReSpc).getData();
                        if(StringUtils.isEmpty(omSalesCodeReSpcDtos))
                            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "未查询到对应的PO号:"+detDtos.get(0).getOption4());
                        qmsInspectionOrder.setOrderQty(omSalesCodeReSpcDtos.get(0).getSamePackageCodeQty());
                        qmsInspectionOrder.setInventoryQty(new BigDecimal(qualifiedInventoryDetDtos.size()));
                    }else if(StringUtils.isNotEmpty(detDtos.get(0).getOption3()) && StringUtils.isEmpty(detDtos.get(0).getOption4()) ) {
                        SearchOmSalesOrderDetDto searchOmSalesOrderDetDto = new SearchOmSalesOrderDetDto();
                        searchOmSalesOrderDetDto.setSalesCode(detDtos.get(0).getOption3());
                        List<OmSalesOrderDetDto> omSalesOrderDetDtos = oMFeignApi.findList(searchOmSalesOrderDetDto).getData();
                        if(StringUtils.isEmpty(omSalesOrderDetDtos))
                            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "未查询到对应的销售编码:"+detDtos.get(0).getOption3());
                        qmsInspectionOrder.setOrderQty(omSalesOrderDetDtos.get(0).getOrderQty());
                        qmsInspectionOrder.setInventoryQty(new BigDecimal(qualifiedInventoryDetDtos.size()));
                    }else{
                        qmsInspectionOrder.setOrderQty(new BigDecimal(detDtos.size()));
                        qmsInspectionOrder.setInventoryQty(new BigDecimal(detDtos.size()));
                    }
                    createQmsInspectionOrder(qmsInspectionOrder, detDtos);
                }

                //库存、库存明细写入检验单号
                writeInspectionOrderCode(qmsInspectionOrder, detDtos, qualified, noQualified);
            }
        }
        //处理三星仓库物料,统一按照无PO号和销售编码处理
        SearchWmsInnerInventoryDet searchWmsInnerInventoryDet1 = new SearchWmsInnerInventoryDet();
        searchWmsInnerInventoryDet1.setInventoryStatusId(wait);
        searchWmsInnerInventoryDet1.setBarcodeStatus("3");
        searchWmsInnerInventoryDet1.setLogicCode("C149");
        searchWmsInnerInventoryDet1.setNotEqualMark(0);
        searchWmsInnerInventoryDet1.setIfInspectionOrderCodeNull(1);
        List<WmsInnerInventoryDetDto> wmsInnerInventoryDetDtos1 = innerFeignApi.findList(searchWmsInnerInventoryDet1).getData();
        Map<String, List<WmsInnerInventoryDetDto>> collect1 = new HashMap<>();
        for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : wmsInnerInventoryDetDtos1) {
            List<WmsInnerInventoryDetDto> inventoryDetDtos = new LinkedList<>();
            if (collect1.containsKey(wmsInnerInventoryDetDto.getMaterialCode())) {
                inventoryDetDtos = collect1.get(wmsInnerInventoryDetDto.getMaterialCode());
            }
            inventoryDetDtos.add(wmsInnerInventoryDetDto);
            collect1.put(wmsInnerInventoryDetDto.getMaterialCode(), inventoryDetDtos);
        }
        if(StringUtils.isNotEmpty(collect1)) {
            Set<String> codes1 = collect1.keySet();
            for (String code : codes1) {
                List<WmsInnerInventoryDetDto> detDtos = collect1.get(code);
                QmsInspectionOrder qmsInspectionOrder1 = new QmsInspectionOrder();
                SearchQmsInspectionOrder searchQmsInspectionOrder = new SearchQmsInspectionOrder();
                searchQmsInspectionOrder.setMaterialCode(code);
                searchQmsInspectionOrder.setInspectionStatus((byte) 1);
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
                        this.update(qmsInspectionOrder1, (byte) 0);
                    } else {
                        //新建检验单
                        qmsInspectionOrder1.setMaterialId(detDtos.get(0).getMaterialId());
                        qmsInspectionOrder1.setOrderQty(new BigDecimal(detDtos.size()));
                        qmsInspectionOrder1.setInventoryQty(new BigDecimal(detDtos.size()));
                        createQmsInspectionOrder(qmsInspectionOrder1, detDtos);
                    }

                    //库存、库存明细写入检验单号
                    writeInspectionOrderCode(qmsInspectionOrder1, detDtos, qualified, noQualified);
                }else{
                    //新建检验单
                    qmsInspectionOrder1.setMaterialId(detDtos.get(0).getMaterialId());
                    qmsInspectionOrder1.setOrderQty(new BigDecimal(detDtos.size()));
                    qmsInspectionOrder1.setInventoryQty(new BigDecimal(detDtos.size()));
                    createQmsInspectionOrder(qmsInspectionOrder1, detDtos);

                }
            }
        }
        return 1;
    }

    public void createQmsInspectionOrder(QmsInspectionOrder qmsInspectionOrder,List<WmsInnerInventoryDetDto> detDtos){
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

    public void writeInspectionOrderCode(QmsInspectionOrder qmsInspectionOrder,List<WmsInnerInventoryDetDto> detDtos,Long qualified ,Long noQualified) {
        //对应的库存明细写入质检单号
        if (StringUtils.isNotEmpty(detDtos)) {
            for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : detDtos) {
                wmsInnerInventoryDetDto.setInspectionOrderCode(qmsInspectionOrder.getInspectionOrderCode());
                if(StringUtils.isNotEmpty(qmsInspectionOrder.getSalesCode()) || StringUtils.isNotEmpty(qmsInspectionOrder.getSamePackageCode())) {
                    if ("0".equals(qmsInspectionOrder.getInspectionResult())) {
                        wmsInnerInventoryDetDto.setInventoryStatusId(noQualified);
                    } else if ("1".equals(qmsInspectionOrder.getInspectionResult())) {
                        wmsInnerInventoryDetDto.setInventoryStatusId(qualified);
                    }
                }
                wmsInnerInventoryDetDto.setModifiedTime(new Date());
                ResponseEntity update = innerFeignApi.update(wmsInnerInventoryDetDto);
                if (StringUtils.isNotEmpty(update) && update.getCode() != 0)
                    throw new BizErrorException("更新库存明细失败");
            }
        }

        //锁定所有待检库存
        SearchWmsInnerInventory searchWmsInnerInventory = new SearchWmsInnerInventory();
        searchWmsInnerInventory.setStorageId(detDtos.get(0).getStorageId());
        searchWmsInnerInventory.setInventoryStatusId(detDtos.get(0).getInventoryStatusId());
        searchWmsInnerInventory.setMaterialId(detDtos.get(0).getMaterialId());
        searchWmsInnerInventory.setQcLock((byte) 0);
        searchWmsInnerInventory.setStockLock((byte) 0);
        List<WmsInnerInventoryDto> list = innerFeignApi.findList(searchWmsInnerInventory).getData();
        if (StringUtils.isNotEmpty(list)) {
            for (WmsInnerInventoryDto wmsInnerInventoryDto : list) {
                wmsInnerInventoryDto.setQcLock((byte) 1);
                wmsInnerInventoryDto.setModifiedTime(new Date());
                wmsInnerInventoryDto.setInspectionOrderCode(qmsInspectionOrder.getInspectionOrderCode());
                ResponseEntity update = innerFeignApi.update(wmsInnerInventoryDto);
                if (StringUtils.isNotEmpty(update) && update.getCode() != 0)
                    throw new BizErrorException("更新库存状态失败");
            }
        }
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
        int i=1;
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(ids.length()<=0){
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }
        //移入库位默认为质检专用库位
        Long inStorageId=null;
        Long warehouseId=null;
        String storageCode="Z-QC";
        SearchBaseStorage searchBaseStorage=new SearchBaseStorage();
        searchBaseStorage.setStorageCode(storageCode);
        List<BaseStorage> storageDtoList=baseFeignApi.findList(searchBaseStorage).getData();
        if(StringUtils.isEmpty(storageDtoList) || storageDtoList.size()<=0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"未找到质检专用库位");
        }
        inStorageId=storageDtoList.get(0).getStorageId();
        warehouseId=storageDtoList.get(0).getWarehouseId();

        SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
        searchBaseInventoryStatus.setWarehouseId(warehouseId);
        searchBaseInventoryStatus.setOrgId(user.getOrganizationId());
        List<BaseInventoryStatus> inventoryStatusList = baseFeignApi.findList(searchBaseInventoryStatus).getData();
        List<BaseInventoryStatus> statusList = inventoryStatusList.stream().filter(item -> item.getInventoryStatusName().equals("待检")).collect(Collectors.toList());
        if(StringUtils.isEmpty(statusList) || statusList.size()<=0){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"未找到仓库的待检状态");
        }

        String[] arrId = ids.split(",");
        for (String id : arrId) {
            QmsInspectionOrder qmsInspectionOrder=qmsInspectionOrderMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(qmsInspectionOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012005);
            }
            BigDecimal sampleQty=null;
            Example example = new Example(QmsInspectionOrderDet.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("inspectionOrderId", qmsInspectionOrder.getInspectionOrderId());
            List<QmsInspectionOrderDet> orderDetList=qmsInspectionOrderDetMapper.selectByExample(example);
            sampleQty=orderDetList.get(0).getSampleQty();
            if(StringUtils.isEmpty(sampleQty)){
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"样本数必须大于0");
            }

            String orderCode=qmsInspectionOrder.getInspectionOrderCode();
            SearchWmsInnerJobOrder searchWmsInnerJobOrder=new SearchWmsInnerJobOrder();
            searchWmsInnerJobOrder.setRelatedOrderCode(orderCode);
            List<WmsInnerJobOrderDto> list=innerFeignApi.findList(searchWmsInnerJobOrder).getData();
            if(StringUtils.isNotEmpty(list) && list.size()>0){
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"成品检验单-->"+orderCode+" 已经生成质检移位单!");
            }

            SearchWmsInnerInventoryDet searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
            searchWmsInnerInventoryDet.setMaterialId(qmsInspectionOrder.getMaterialId());
            searchWmsInnerInventoryDet.setInspectionOrderCode(qmsInspectionOrder.getInspectionOrderCode());
            List<WmsInnerInventoryDetDto> inventoryDetDtos = innerFeignApi.findList(searchWmsInnerInventoryDet).getData();
            if(StringUtils.isEmpty(inventoryDetDtos)){
                throw new BizErrorException("未找到成品检验单的条码明细信息-->"+orderCode);
            }

            Long outStorageId=inventoryDetDtos.get(0).getStorageId();

            //找物料库存
            // inventoryStatusId: 132
            //inventoryStatusName: "待检"
            Long materialId=qmsInspectionOrder.getMaterialId();
            SearchWmsInnerInventory searchWmsInnerInventory=new SearchWmsInnerInventory();
            searchWmsInnerInventory.setMaterialId(materialId);
            searchWmsInnerInventory.setStorageId(outStorageId);
            searchWmsInnerInventory.setLockStatus((byte)0);
            searchWmsInnerInventory.setJobStatus((byte)1);
            searchWmsInnerInventory.setInventoryStatusName("待检");
            List<WmsInnerInventoryDto> inventoryDtos=innerFeignApi.findList(searchWmsInnerInventory).getData();
            if(StringUtils.isEmpty(inventoryDtos) || inventoryDtos.size()<=0){
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"未找到待检库存");
            }
            BigDecimal qty=sampleQty;
            List<WmsInnerInventoryDto> dtoList = inventoryDtos.stream().filter(u -> (u.getPackingQty().compareTo(qty)>=0)).collect(Collectors.toList());
            if(StringUtils.isEmpty(dtoList) || dtoList.size()<=0){
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"未找到大于等于样板数的待检库存");
            }
            if(StringUtils.isNotEmpty(inventoryDtos) && inventoryDtos.size()>0){
                WmsInnerJobOrder wmsInnerJobOrder=new WmsInnerJobOrder();
                wmsInnerJobOrder.setJobOrderType((byte)2);
                wmsInnerJobOrder.setShiftType((byte)2);
                wmsInnerJobOrder.setWarehouseId(warehouseId);
                wmsInnerJobOrder.setStatus((byte)1);
                wmsInnerJobOrder.setOrderStatus((byte)3);
                wmsInnerJobOrder.setOrgId(user.getOrganizationId());
                wmsInnerJobOrder.setRelatedOrderCode(orderCode);
                wmsInnerJobOrder.setSourceOrderId(qmsInspectionOrder.getInspectionOrderId());
                wmsInnerJobOrder.setOption1("qmsToInnerJobShift");

                List<WmsInnerJobOrderDet> detList = new LinkedList<>();

                SearchBaseTab searchBaseTab=new SearchBaseTab();
                searchBaseTab.setMaterialId(materialId);
                List<BaseTabDto> tabList=baseFeignApi.findTabList(searchBaseTab).getData();

                WmsInnerJobOrderDet wmsInnerJobOrderDet=new WmsInnerJobOrderDet();
                wmsInnerJobOrderDet.setMaterialId(materialId);
                wmsInnerJobOrderDet.setPlanQty(qty);
                wmsInnerJobOrderDet.setDistributionQty(qty);
                wmsInnerJobOrderDet.setOutStorageId(outStorageId);
                wmsInnerJobOrderDet.setInStorageId(inStorageId);
                wmsInnerJobOrderDet.setSourceDetId(dtoList.get(0).getInventoryId());
                wmsInnerJobOrderDet.setOrderStatus((byte)3);
                wmsInnerJobOrderDet.setInventoryStatusId(statusList.get(0).getInventoryStatusId());
                if(StringUtils.isNotEmpty(tabList) && tabList.size()>0){
                    wmsInnerJobOrderDet.setPackingUnitName(tabList.get(0).getMainUnit());
                }

                detList.add(wmsInnerJobOrderDet);

                wmsInnerJobOrder.setWmsInPutawayOrderDets(detList);
                ResponseEntity responseEntity = innerFeignApi.add(wmsInnerJobOrder);
                if(responseEntity.getCode() != 0){
                    throw new BizErrorException("生成质检移位单失败");
                }
            }
        }

        return i;
    }
}
