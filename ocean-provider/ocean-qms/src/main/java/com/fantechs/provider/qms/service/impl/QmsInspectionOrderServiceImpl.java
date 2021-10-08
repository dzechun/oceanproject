package com.fantechs.provider.qms.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDto;
import com.fantechs.common.base.general.dto.om.SearchOmSalesOrderDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInAsnOrderDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDto;
import com.fantechs.common.base.general.entity.basic.BaseInspectionStandard;
import com.fantechs.common.base.general.entity.basic.BaseInventoryStatus;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInspectionStandard;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInventoryStatus;
import com.fantechs.common.base.general.entity.qms.QmsInspectionOrder;
import com.fantechs.common.base.general.entity.qms.QmsInspectionOrderDet;
import com.fantechs.common.base.general.entity.qms.QmsInspectionOrderDetSample;
import com.fantechs.common.base.general.entity.qms.history.QmsHtInspectionOrder;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsInspectionOrderDet;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrderDet;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInAsnOrder;
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
    private QmsInspectionOrderDetServiceImpl qmsInspectionOrderDetService;
    @Resource
    private QmsInspectionOrderDetSampleServiceImpl qmsInspectionOrderDetSampleService;

    @Override
    public List<QmsInspectionOrder> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        map.put("orgId",user.getOrganizationId());

        List<QmsInspectionOrder> qmsInspectionOrders = qmsInspectionOrderMapper.findList(map);

        return qmsInspectionOrders;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchQualified(Long inspectionOrderId){
        QmsInspectionOrder qmsInspectionOrder = qmsInspectionOrderMapper.selectByPrimaryKey(inspectionOrderId);

        Example example = new Example(QmsInspectionOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("inspectionOrderId",inspectionOrderId);
        List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetMapper.selectByExample(example);

        for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets){
            //明细
            qmsInspectionOrderDet.setBadnessQty(BigDecimal.ZERO);
            qmsInspectionOrderDet.setInspectionResult((byte)1);
            qmsInspectionOrderDetMapper.updateByPrimaryKeySelective(qmsInspectionOrderDet);
        }

        qmsInspectionOrder.setInspectionStatus((byte)3);
        qmsInspectionOrder.setInspectionResult((byte)1);
        int i = qmsInspectionOrderMapper.updateByPrimaryKeySelective(qmsInspectionOrder);

        //处理库存
        this.handleInventory(qmsInspectionOrder.getInspectionOrderCode(),qmsInspectionOrder.getInspectionResult());

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchSubmit(Long inspectionOrderId){
        QmsInspectionOrder qmsInspectionOrder = qmsInspectionOrderMapper.selectByPrimaryKey(inspectionOrderId);

        Example example = new Example(QmsInspectionOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("inspectionOrderId",inspectionOrderId);
        List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetMapper.selectByExample(example);

        for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets){
            //明细
            if(StringUtils.isEmpty(qmsInspectionOrderDet.getInspectionResult())) {
                qmsInspectionOrderDet.setBadnessQty(BigDecimal.ZERO);
                qmsInspectionOrderDet.setInspectionResult((byte) 1);
                qmsInspectionOrderDetMapper.updateByPrimaryKeySelective(qmsInspectionOrderDet);
            }
        }

        List<QmsInspectionOrderDetSample> barcodes = qmsInspectionOrderDetSampleService.findBarcodes(inspectionOrderId);
        List<QmsInspectionOrderDetSample> unQualifiedBarcodes = barcodes.stream().filter(item -> item.getBarcodeStatus() == (byte) 0).collect(Collectors.toList());
        if(barcodes.size() == unQualifiedBarcodes.size()){
            qmsInspectionOrder.setInspectionResult((byte)0);
        }else {
            qmsInspectionOrder.setInspectionResult((byte)2);
        }

        qmsInspectionOrder.setInspectionStatus((byte)3);
        int i = qmsInspectionOrderMapper.updateByPrimaryKeySelective(qmsInspectionOrder);

        //处理库存
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

        //检验结果返写回库存
        SearchWmsInnerInventory  searchWmsInnerInventory = new SearchWmsInnerInventory();
        searchWmsInnerInventory.setInspectionOrderCode(inspectionOrderCode);
        ResponseEntity<List<WmsInnerInventoryDto>> innerInventoryDtoList = innerFeignApi.findList(searchWmsInnerInventory);
        if(StringUtils.isEmpty(innerInventoryDtoList.getData())){
            throw new BizErrorException("未查询到对应库存信息");
        }
        WmsInnerInventoryDto wmsInnerInventoryDto = innerInventoryDtoList.getData().get(0);
        WmsInnerInventoryDto innerInventoryDto = innerInventoryDtoList.getData().get(0);

        //不合格库存
        wmsInnerInventoryDto.setQcLock((byte)0);
        wmsInnerInventoryDto.setInventoryStatusId(inventoryStatusList1.get(0).getInventoryStatusId());
        wmsInnerInventoryDto.setPackingQty(new BigDecimal(unQualifiedCount));
        innerFeignApi.update(wmsInnerInventoryDto);

        //合格库存
        innerInventoryDto.setInventoryId(null);
        innerInventoryDto.setQcLock((byte)0);
        innerInventoryDto.setInventoryStatusId(inventoryStatusList2.get(0).getInventoryStatusId());
        innerInventoryDto.setPackingQty(innerInventoryDto.getPackingQty().subtract(new BigDecimal(unQualifiedCount)));
        innerFeignApi.add(innerInventoryDto);

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int audit(QmsInspectionOrder qmsInspectionOrder) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        qmsInspectionOrder.setModifiedUserId(user.getUserId());
        qmsInspectionOrder.setModifiedTime(new Date());
        return qmsInspectionOrderMapper.updateByPrimaryKeySelective(qmsInspectionOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int thirdInspection(QmsInspectionOrder qmsInspectionOrder) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        qmsInspectionOrder.setIfThirdInspection(StringUtils.isEmpty(qmsInspectionOrder.getIfThirdInspection()) ? 1 :qmsInspectionOrder.getIfThirdInspection());
        qmsInspectionOrder.setModifiedUserId(user.getUserId());
        qmsInspectionOrder.setModifiedTime(new Date());
        return qmsInspectionOrderMapper.updateByPrimaryKeySelective(qmsInspectionOrder);
    }

    @Override
    public QmsInspectionOrder selectByKey(Object key) {
        Map<String,Object> map = new HashMap<>();
        map.put("inspectionOrderId",key);
        QmsInspectionOrder qmsInspectionOrder = qmsInspectionOrderMapper.findList(map).get(0);
        SearchQmsInspectionOrderDet searchQmsInspectionOrderDet = new SearchQmsInspectionOrderDet();
        searchQmsInspectionOrderDet.setInspectionOrderId(qmsInspectionOrder.getInspectionOrderId());
        List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetMapper.findDetList(ControllerUtil.dynamicConditionByEntity(searchQmsInspectionOrderDet));
        qmsInspectionOrder.setQmsInspectionOrderDets(qmsInspectionOrderDets);

        return qmsInspectionOrder;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int save(QmsInspectionOrder qmsInspectionOrder) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        qmsInspectionOrder.setInspectionOrderCode(CodeUtils.getId("CPJY-"));
        qmsInspectionOrder.setCreateUserId(user.getUserId());
        qmsInspectionOrder.setCreateTime(new Date());
        qmsInspectionOrder.setModifiedUserId(user.getUserId());
        qmsInspectionOrder.setModifiedTime(new Date());
        qmsInspectionOrder.setStatus(StringUtils.isEmpty(qmsInspectionOrder.getStatus())?1:qmsInspectionOrder.getStatus());
        qmsInspectionOrder.setInspectionStatus((byte)1);
        qmsInspectionOrder.setOrgId(user.getOrganizationId());
        int i = qmsInspectionOrderMapper.insertUseGeneratedKeys(qmsInspectionOrder);

        //履历
        QmsHtInspectionOrder qmsHtInspectionOrder = new QmsHtInspectionOrder();
        BeanUtils.copyProperties(qmsInspectionOrder, qmsHtInspectionOrder);
        qmsHtInspectionOrderMapper.insert(qmsHtInspectionOrder);

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

        //手动添加变更质检锁
        if(StringUtils.isNotEmpty(qmsInspectionOrder.getInventoryIds())){
            String[] ids = qmsInspectionOrder.getInventoryIds().split(",");
            for(String id : ids){
                SearchWmsInnerInventory  searchWmsInnerInventory = new SearchWmsInnerInventory();
                searchWmsInnerInventory.setInventoryId(Long.valueOf(id).longValue());
                ResponseEntity<List<WmsInnerInventoryDto>> innerInventoryDtoList = innerFeignApi.findList(searchWmsInnerInventory);
                if(StringUtils.isEmpty(innerInventoryDtoList.getData())) throw new BizErrorException("未查询到对应库存信息");
                WmsInnerInventoryDto wmsInnerInventoryDto = innerInventoryDtoList.getData().get(0);
                wmsInnerInventoryDto.setQcLock((byte)1);
                wmsInnerInventoryDto.setInspectionOrderCode(qmsInspectionOrder.getInspectionOrderCode());
                innerFeignApi.update(wmsInnerInventoryDto);


                //对应的库存明细写入质检单号
                SearchWmsInnerInventoryDet searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
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
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        qmsInspectionOrder.setModifiedUserId(user.getUserId());
        qmsInspectionOrder.setModifiedTime(new Date());
        qmsInspectionOrder.setOrgId(user.getOrganizationId());
        int i=qmsInspectionOrderMapper.updateByPrimaryKeySelective(qmsInspectionOrder);

        //履历
        QmsHtInspectionOrder qmsHtInspectionOrder = new QmsHtInspectionOrder();
        BeanUtils.copyProperties(qmsInspectionOrder, qmsHtInspectionOrder);
        qmsHtInspectionOrderMapper.insert(qmsHtInspectionOrder);

        //原来有的明细只更新
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
        ResponseEntity<List<WmsInnerInventoryDto>> innerInventoryDtoList = innerFeignApi.findList(searchWmsInnerInventory);
        if(StringUtils.isEmpty(innerInventoryDtoList.getData())){
            throw new BizErrorException("未查询到对应库存信息");
        }
        WmsInnerInventoryDto wmsInnerInventoryDto = innerInventoryDtoList.getData().get(0);


        SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
        searchBaseInventoryStatus.setInventoryStatusName(inspectionResult==0 ? "不合格" : "合格");
        searchBaseInventoryStatus.setNameQueryMark(1);
        List<BaseInventoryStatus> inventoryStatusList = baseFeignApi.findList(searchBaseInventoryStatus).getData();
        if(StringUtils.isEmpty(inventoryStatusList)){
            throw new BizErrorException("未查询到对应库存状态");
        }

        wmsInnerInventoryDto.setQcLock((byte)0);
        wmsInnerInventoryDto.setInventoryStatusId(inventoryStatusList.get(0).getInventoryStatusId());
        innerFeignApi.update(wmsInnerInventoryDto);

        //合并库存
        SearchWmsInnerInventory searchInnerInventory = new SearchWmsInnerInventory();
        searchInnerInventory.setMaterialId(wmsInnerInventoryDto.getMaterialId());
        searchInnerInventory.setWarehouseId(wmsInnerInventoryDto.getWarehouseId());
        searchInnerInventory.setStorageId(wmsInnerInventoryDto.getStorageId());
        searchInnerInventory.setBatchCode(wmsInnerInventoryDto.getBatchCode());
        searchInnerInventory.setInventoryStatusId(wmsInnerInventoryDto.getInventoryStatusId());
        searchInnerInventory.setJobStatus((byte) 1);
        searchInnerInventory.setLockStatus((byte) 0);
        searchInnerInventory.setQcLock((byte) 0);
        searchInnerInventory.setStockLock((byte) 0);
        List<WmsInnerInventoryDto> innerInventoryDtos = innerFeignApi.findList(searchInnerInventory).getData();
        if (StringUtils.isNotEmpty(innerInventoryDtos)) {
            WmsInnerInventoryDto innerInventoryDto = innerInventoryDtos.get(0);
            innerInventoryDto.setPackingQty(innerInventoryDto.getPackingQty().add(wmsInnerInventoryDto.getPackingQty()));
            innerFeignApi.update(innerInventoryDto);
            wmsInnerInventoryDto.setPackingQty(BigDecimal.ZERO);
            innerFeignApi.update(wmsInnerInventoryDto);
        }

        //检验结果返写回库存明细
        SearchWmsInnerInventoryDet  searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
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
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

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
                //删除检验单明细样本
                Example example2 = new Example(QmsInspectionOrderDetSample.class);
                Example.Criteria criteria2 = example2.createCriteria();
                criteria2.andEqualTo("inspectionOrderDetId", qmsInspectionOrderDet.getInspectionOrderDetId());
                qmsInspectionOrderDetSampleMapper.deleteByExample(example2);
            }
            //删除检验单明细
            qmsInspectionOrderDetMapper.deleteByExample(example1);

        }
        //履历
        qmsHtInspectionOrderMapper.insertList(list);

        return qmsInspectionOrderMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int autoAdd() {

        //获取完工入库单
        SearchWmsInAsnOrder searchWmsInAsnOrder = new SearchWmsInAsnOrder();
        searchWmsInAsnOrder.setOrderStatus((byte)3);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -1);
        Date date = calendar.getTime();
        searchWmsInAsnOrder.setStartTime(DateUtils.getDateString(date));
        searchWmsInAsnOrder.setEndTime(DateUtils.getDateString(date));
        searchWmsInAsnOrder.setOrderTypeName("完工入库");
        ResponseEntity<List<WmsInAsnOrderDto>> wmsInAsnOrderList = inFeignApi.findList(searchWmsInAsnOrder);

        if(StringUtils.isNotEmpty(wmsInAsnOrderList.getData())){
            for(WmsInAsnOrderDto wmsInAsnOrderDto : wmsInAsnOrderList.getData()) {
                if (StringUtils.isNotEmpty(wmsInAsnOrderDto.getWmsInAsnOrderDetList())) {
                    //throw new BizErrorException("未查询到可生成检验单的入库信息");
                    for (WmsInAsnOrderDet wmsInAsnOrderDet : wmsInAsnOrderDto.getWmsInAsnOrderDetList()) {
                        SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
                        searchBaseInventoryStatus.setInventoryStatusId(wmsInAsnOrderDet.getInventoryStatusId());
                        List<BaseInventoryStatus> inventoryStatuses = baseFeignApi.findList(searchBaseInventoryStatus).getData();
                        if(StringUtils.isNotEmpty(inventoryStatuses)&&"待检".equals(inventoryStatuses.get(0).getInventoryStatusName())){

                            QmsInspectionOrder qmsInspectionOrder = new QmsInspectionOrder();
                            qmsInspectionOrder.setMaterialId(wmsInAsnOrderDet.getMaterialId());
                            qmsInspectionOrder.setOrderQty(wmsInAsnOrderDet.getActualQty());
                            qmsInspectionOrder.setInspectionWayId((long) 42);
                            qmsInspectionOrder.setInspectionStatus((byte) 1);

                            SearchOmSalesOrderDto searchOmSalesOrderDto = new SearchOmSalesOrderDto();
                            searchOmSalesOrderDto.setWorkOrderId(wmsInAsnOrderDet.getSourceOrderId());
                            ResponseEntity<List<OmSalesOrderDto>> salesOrderDtoList = oMFeignApi.findList(searchOmSalesOrderDto);
                            if (StringUtils.isNotEmpty(salesOrderDtoList.getData()))
                                qmsInspectionOrder.setCustomerId(salesOrderDtoList.getData().get(0).getSupplierId());

                            SearchBaseInspectionStandard searchBaseInspectionStandard = new SearchBaseInspectionStandard();
                            searchBaseInspectionStandard.setMaterialId(wmsInAsnOrderDet.getMaterialId());
                            searchBaseInspectionStandard.setSupplierId(salesOrderDtoList.getData().get(0).getSupplierId());
                            ResponseEntity<List<BaseInspectionStandard>> inspectionStandardList = baseFeignApi.findList(searchBaseInspectionStandard);
                            qmsInspectionOrder.setInspectionStandardId(inspectionStandardList.getData().get(0).getInspectionStandardId());

                            //明细
                            List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetService.showOrderDet(qmsInspectionOrder.getInspectionStandardId(), qmsInspectionOrder.getOrderQty());
                            qmsInspectionOrder.setQmsInspectionOrderDets(qmsInspectionOrderDets);
                            this.save(qmsInspectionOrder);

                            //修改质检锁
                            SearchWmsInnerInventory searchWmsInnerInventory = new SearchWmsInnerInventory();
                            searchWmsInnerInventory.setRelevanceOrderCode(wmsInAsnOrderDto.getAsnCode());
                            searchWmsInnerInventory.setMaterialId(wmsInAsnOrderDet.getMaterialId());
                            searchWmsInnerInventory.setQcLock((byte)0);
                            ResponseEntity<List<WmsInnerInventoryDto>> list = innerFeignApi.findList(searchWmsInnerInventory);
                            if (StringUtils.isEmpty(list.getData()))
                                throw new BizErrorException("未查询到质检锁为否的对应库存，asnCode为：" + wmsInAsnOrderDto.getAsnCode() + ",物料id为：" + wmsInAsnOrderDet.getMaterialId());
                            WmsInnerInventoryDto wmsInnerInventory = list.getData().get(0);
                            wmsInnerInventory.setQcLock((byte) 1);
                            wmsInnerInventory.setInspectionOrderCode(qmsInspectionOrder.getInspectionOrderCode());
                            innerFeignApi.update(wmsInnerInventory);

                            //对应的库存明细写入质检单号
                            SearchWmsInnerInventoryDet searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
                            searchWmsInnerInventoryDet.setStorageId(wmsInnerInventory.getStorageId());
                            searchWmsInnerInventoryDet.setMaterialId(wmsInnerInventory.getMaterialId());
                            searchWmsInnerInventoryDet.setInventoryStatusId(wmsInnerInventory.getInventoryStatusId());
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

                }
            }
        }
        return 1;
    }



}
