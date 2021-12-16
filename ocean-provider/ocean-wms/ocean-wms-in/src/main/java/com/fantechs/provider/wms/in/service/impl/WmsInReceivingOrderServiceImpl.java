package com.fantechs.provider.wms.in.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysImportAndExportLog;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsIncomingInspectionOrderDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInReceivingOrderBarcode;
import com.fantechs.common.base.general.dto.wms.in.WmsInReceivingOrderDto;
import com.fantechs.common.base.general.dto.wms.in.imports.WmsInReceivingOrderImport;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseOrderFlow;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrderFlow;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehouse;
import com.fantechs.common.base.general.entity.wms.in.*;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerMaterialBarcodeReOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.qms.QmsFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.wms.in.mapper.*;
import com.fantechs.provider.wms.in.service.WmsInReceivingOrderService;
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
 * Created by mr.lei on 2021/12/13.
 */
@Service
public class WmsInReceivingOrderServiceImpl extends BaseService<WmsInReceivingOrder> implements WmsInReceivingOrderService {

    @Resource
    private WmsInReceivingOrderMapper wmsInReceivingOrderMapper;
    @Resource
    private WmsInReceivingOrderDetMapper wmsInReceivingOrderDetMapper;
    @Resource
    private WmsInPlanReceivingOrderMapper wmsInPlanReceivingOrderMapper;
    @Resource
    private WmsInPlanReceivingOrderDetMapper wmsInPlanReceivingOrderDetMapper;
    @Resource
    private InnerFeignApi innerFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private QmsFeignApi qmsFeignApi;
    @Resource
    private WmsInHtReceivingOrderMapper wmsInHtReceivingOrderMapper;
    @Resource
    private WmsInHtReceivingOrderDetMapper wmsInHtReceivingOrderDetMapper;

    @Override
    public List<WmsInReceivingOrderDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return wmsInReceivingOrderMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(WmsInReceivingOrder record) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(record.getWarehouseId())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"仓库不能为空");
        }
        record.setReceivingOrderCode(CodeUtils.getId("IN-SWK"));
        record.setCreateUserId(sysUser.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrgId(sysUser.getOrganizationId());
        if(StringUtils.isNotEmpty(record.getIsPdaCreate()) && record.getIsPdaCreate()==1){
            record.setOrderStatus((byte)3);
        }else {
            record.setOrderStatus((byte)1);
        }
        int num = wmsInReceivingOrderMapper.insertUseGeneratedKeys(record);

        //履历
        WmsInHtReceivingOrder wmsInHtReceivingOrder = new WmsInHtReceivingOrder();
        BeanUtils.copyProperties(record,wmsInHtReceivingOrder);
        wmsInHtReceivingOrderMapper.insertSelective(wmsInHtReceivingOrder);

        if(!record.getWmsInReceivingOrderDets().isEmpty()){
            int i = 0;
            for (WmsInReceivingOrderDet wmsInReceivingOrderDet : record.getWmsInReceivingOrderDets()) {
                i++;
                wmsInReceivingOrderDet.setReceivingOrderId(record.getReceivingOrderId());
                wmsInReceivingOrderDet.setLineNumber(i+"");
                wmsInReceivingOrderDet.setCreateUserId(sysUser.getUserId());
                wmsInReceivingOrderDet.setCreateTime(new Date());
                wmsInReceivingOrderDet.setModifiedTime(new Date());
                wmsInReceivingOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInReceivingOrderDet.setOrgId(sysUser.getOrganizationId());

                if(StringUtils.isNotEmpty(record.getIsPdaCreate()) && record.getIsPdaCreate()==1){
                    wmsInReceivingOrderDet.setOperatorUserId(sysUser.getUserId());
                    wmsInReceivingOrderDet.setPlanQty(wmsInReceivingOrderDet.getActualQty());
                    wmsInReceivingOrderDet.setLineStatus((byte)3);
                    wmsInReceivingOrderDetMapper.insertUseGeneratedKeys(wmsInReceivingOrderDet);
                    //条码记录

//                    List<WmsInnerMaterialBarcodeReOrder> wmsInnerMaterialBarcodeReOrders = new ArrayList<>();
//                    for (WmsInReceivingOrderBarcode wmsInReceivingOrderBarcode : wmsInReceivingOrderDet.getWmsInReceivingOrderBarcodeList()) {
//                        WmsInnerMaterialBarcodeReOrder wmsInnerMaterialBarcodeReOrder = new WmsInnerMaterialBarcodeReOrder();
//                        wmsInnerMaterialBarcodeReOrder.setMaterialBarcodeReOrderId(wmsInReceivingOrderBarcode.getMaterialBarcodeReOrderId());
//                        wmsInnerMaterialBarcodeReOrder.setScanStatus((byte)3);
//                        wmsInnerMaterialBarcodeReOrders.add(wmsInnerMaterialBarcodeReOrder);
//                    }
//                    if(!wmsInnerMaterialBarcodeReOrders.isEmpty()){
//                        ResponseEntity responseEntity = innerFeignApi.batchUpdate(wmsInnerMaterialBarcodeReOrders);
//                        if(responseEntity.getCode()!=0){
//                            throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
//                        }
//                    }
                }else {
                    if(StringUtils.isEmpty(wmsInReceivingOrderDet.getPlanQty()) || wmsInReceivingOrderDet.getPlanQty().compareTo(BigDecimal.ZERO)<1){
                        throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"计划数量必须大于0");
                    }
                    wmsInReceivingOrderDet.setLineStatus((byte)1);
                    wmsInReceivingOrderDetMapper.insertUseGeneratedKeys(wmsInReceivingOrderDet);
                    //获取条码记录
                    List<WmsInnerMaterialBarcodeReOrder> wmsInnerMaterialBarcodeReOrders = new ArrayList<>();
                    for (WmsInReceivingOrderBarcode wmsInReceivingOrderBarcode : wmsInReceivingOrderDet.getWmsInReceivingOrderBarcodeList()) {
                        WmsInnerMaterialBarcodeReOrder wmsInnerMaterialBarcodeReOrder = new WmsInnerMaterialBarcodeReOrder();
                        wmsInnerMaterialBarcodeReOrder.setOrderId(record.getReceivingOrderId());
                        wmsInnerMaterialBarcodeReOrder.setOrderCode(record.getReceivingOrderCode());
                        wmsInnerMaterialBarcodeReOrder.setMaterialBarcodeId(wmsInReceivingOrderBarcode.getMaterialBarcodeId());
                        wmsInnerMaterialBarcodeReOrder.setOrderDetId(wmsInReceivingOrderDet.getReceivingOrderDetId());
                        wmsInnerMaterialBarcodeReOrder.setOrderTypeCode("IN-SWK");
                        wmsInnerMaterialBarcodeReOrder.setScanStatus((byte)1);
                        wmsInnerMaterialBarcodeReOrders.add(wmsInnerMaterialBarcodeReOrder);
                    }
                    if(!wmsInnerMaterialBarcodeReOrders.isEmpty()){
                        ResponseEntity responseEntity = innerFeignApi.batchAdd(wmsInnerMaterialBarcodeReOrders);
                        if(responseEntity.getCode()!=0){
                            throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
                        }
                    }
                }
                WmsInHtReceivingOrderDet wmsInHtReceivingOrderDet = new WmsInHtReceivingOrderDet();
                BeanUtils.copyProperties(wmsInReceivingOrderDet,wmsInHtReceivingOrderDet);
                wmsInHtReceivingOrderDetMapper.insertSelective(wmsInHtReceivingOrderDet);
            }
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(WmsInReceivingOrder entity) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(sysUser.getUserId());
        Boolean isPDA = false;
        if(StringUtils.isNotEmpty(entity.getIsPdaCreate()) && entity.getIsPdaCreate()==1){
            isPDA=true;
        }else {
            //删除原有单据
            Example example = new Example(WmsInReceivingOrderDet.class);
            example.createCriteria().andEqualTo("receivingOrderId",entity.getReceivingOrderId());
            wmsInReceivingOrderDetMapper.deleteByExample(example);
        }
        int i=0;

        WmsInHtReceivingOrder wmsInHtReceivingOrder = new WmsInHtReceivingOrder();
        BeanUtils.copyProperties(entity,wmsInHtReceivingOrder);
        wmsInHtReceivingOrderMapper.insertSelective(wmsInHtReceivingOrder);

        for (WmsInReceivingOrderDet wmsInReceivingOrderDet : entity.getWmsInReceivingOrderDets()) {
            wmsInReceivingOrderDet.setModifiedTime(new Date());
            wmsInReceivingOrderDet.setModifiedUserId(sysUser.getUserId());
            if (isPDA) {
                //PDA更新数量 更新单据状态
                WmsInReceivingOrderDet inReceivingOrderDet = wmsInReceivingOrderDetMapper.selectByPrimaryKey(wmsInReceivingOrderDet.getReceivingOrderDetId());
                if (StringUtils.isEmpty(inReceivingOrderDet.getActualQty())) {
                    inReceivingOrderDet.setActualQty(BigDecimal.ZERO);
                }
                BigDecimal totalActualQty = inReceivingOrderDet.getActualQty().add(wmsInReceivingOrderDet.getActualQty());
                if (inReceivingOrderDet.getPlanQty().compareTo(totalActualQty) == 0) {
                    wmsInReceivingOrderDet.setLineStatus((byte) 3);
                } else {
                    wmsInReceivingOrderDet.setLineStatus((byte) 2);
                }
                wmsInReceivingOrderDet.setOperatorUserId(sysUser.getUserId());
                wmsInReceivingOrderDet.setActualQty(totalActualQty);
                wmsInReceivingOrderDetMapper.updateByPrimaryKeySelective(wmsInReceivingOrderDet);
                wmsInReceivingOrderDet.setSourceId(inReceivingOrderDet.getSourceId());
                //反写收货计划实收数量
                this.writeQty(wmsInReceivingOrderDet);

                //新增条码记录

            } else {
                i++;
                if(StringUtils.isEmpty(wmsInReceivingOrderDet.getPlanQty()) || wmsInReceivingOrderDet.getPlanQty().compareTo(BigDecimal.ZERO)<1){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"计划数量必须大于0");
                }
                wmsInReceivingOrderDet.setLineStatus((byte)1);
                wmsInReceivingOrderDet.setReceivingOrderId(entity.getReceivingOrderId());
                wmsInReceivingOrderDet.setLineNumber(i+"");
                wmsInReceivingOrderDet.setCreateUserId(sysUser.getUserId());
                wmsInReceivingOrderDet.setCreateTime(new Date());
                wmsInReceivingOrderDet.setOrgId(sysUser.getOrganizationId());
            }
        }
        if(!isPDA && !entity.getWmsInReceivingOrderDets().isEmpty()){
            wmsInReceivingOrderDetMapper.insertList(entity.getWmsInReceivingOrderDets());
        }else {
            Example example = new Example(WmsInReceivingOrderDet.class);
            example.createCriteria().andEqualTo("receivingOrderId",entity.getReceivingOrderId());
            List<WmsInReceivingOrderDet> wmsInReceivingOrderDets = wmsInReceivingOrderDetMapper.selectByExample(example);
            if(wmsInReceivingOrderDets.stream().filter(li->li.getLineStatus()==3).collect(Collectors.toList()).size()==wmsInReceivingOrderDets.size()){
                entity.setOrderStatus((byte)3);
            }else {
                entity.setOrderStatus((byte)2);
            }
        }
        return super.update(entity);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        String[] idArry = ids.split(",");
        for (String s : idArry) {
            WmsInReceivingOrder wmsInReceivingOrder = wmsInReceivingOrderMapper.selectByPrimaryKey(s);
            if(StringUtils.isEmpty(wmsInReceivingOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012000,s);
            }
            if(wmsInReceivingOrder.getOrderStatus()>1){
                throw new BizErrorException("已作业的单无法删除");
            }
            //删除明细
            Example example = new Example(WmsInReceivingOrderDet.class);
            example.createCriteria().andEqualTo("receivingOrderId",s);
            wmsInReceivingOrderDetMapper.deleteByExample(example);
        }
        return super.batchDelete(ids);
    }

    private void writeQty(WmsInReceivingOrderDet wmsInReceivingOrderDet){
        WmsInPlanReceivingOrderDet wmsInPlanReceivingOrderDet = wmsInPlanReceivingOrderDetMapper.selectByPrimaryKey(wmsInReceivingOrderDet.getSourceId());
        wmsInPlanReceivingOrderDet.setActualQty(wmsInPlanReceivingOrderDet.getActualQty());
        wmsInPlanReceivingOrderDet.setOperatorUserId(wmsInPlanReceivingOrderDet.getOperatorUserId());
        wmsInPlanReceivingOrderDet.setLineStatus(wmsInReceivingOrderDet.getLineStatus());
        wmsInPlanReceivingOrderDetMapper.updateByPrimaryKeySelective(wmsInPlanReceivingOrderDet);
        Example example = new Example(WmsInPlanReceivingOrderDet.class);
        example.createCriteria().andEqualTo("planReceivingOrderId");
        List<WmsInPlanReceivingOrderDet> list = wmsInPlanReceivingOrderDetMapper.selectByExample(example);
        WmsInPlanReceivingOrder wmsInPlanReceivingOrder = new WmsInPlanReceivingOrder();
        wmsInPlanReceivingOrder.setPlanReceivingOrderId(wmsInPlanReceivingOrderDet.getPlanReceivingOrderId());
        if(list.stream().filter(li->li.getLineStatus()==3).collect(Collectors.toList()).size()==list.size()){
            wmsInPlanReceivingOrder.setOrderStatus((byte)3);
        }else{
            wmsInPlanReceivingOrder.setOrderStatus((byte)2);
        }
        wmsInPlanReceivingOrderMapper.updateByPrimaryKeySelective(wmsInPlanReceivingOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<WmsInReceivingOrderImport> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        Map<String,String > map = new HashMap<>();
        List<Map<String,String>> failMap = new ArrayList<>();  //记录操作失败行数
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        int i=0;
        Map<String,List<WmsInReceivingOrderImport>> hashMap = list.stream().collect(Collectors.groupingBy(WmsInReceivingOrderImport::getWarehouseName));
        Set<String> set = hashMap.keySet();
        for (String s : set) {
            List<WmsInReceivingOrderImport> importList = hashMap.get(s);
            //判断非空
            String warehouseName = importList.get(0).getWarehouseName();
            if (StringUtils.isEmpty(
                    warehouseName
            )){
                map.put(s,"仓库名称、计划入库单号不鞥呢为空");
                failMap.add(map);
                fail.add(i++);
                break;
            }
            SearchBaseWarehouse searchBaseWarehouse = new SearchBaseWarehouse();
            searchBaseWarehouse.setWarehouseName(warehouseName);
            ResponseEntity<List<BaseWarehouse>> responseEntity = baseFeignApi.findList(searchBaseWarehouse);
            if(responseEntity.getCode()!=0){
                throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
            }
            if (StringUtils.isEmpty(responseEntity.getData()) || responseEntity.getData().size() != 1) {
                map.put(s,"未查询到仓库或查询出的仓库不唯一");
                failMap.add(map);
                fail.add(i++);
                break;
            }
            //表头
            WmsInReceivingOrder wmsInReceivingOrder = new WmsInReceivingOrder();
            wmsInReceivingOrder.setReceivingOrderCode(CodeUtils.getId("IN-SWK"));
            wmsInReceivingOrder.setWarehouseId(responseEntity.getData().get(0).getWarehouseId());
            wmsInReceivingOrder.setCreateTime(new Date());
            wmsInReceivingOrder.setCreateUserId(user.getUserId());
            wmsInReceivingOrder.setModifiedTime(new Date());
            wmsInReceivingOrder.setModifiedUserId(user.getUserId());
            wmsInReceivingOrder.setOrderStatus((byte)1);
            wmsInReceivingOrder.setOrgId(user.getOrganizationId());
            int num = wmsInReceivingOrderMapper.insertUseGeneratedKeys(wmsInReceivingOrder);
            List<WmsInReceivingOrderDet> wmsInReceivingOrderDets = new ArrayList<>();
            for (WmsInReceivingOrderImport wmsInReceivingOrderImport : importList) {
                WmsInReceivingOrderDet wmsInReceivingOrderDet = new WmsInReceivingOrderDet();
                wmsInReceivingOrderDet.setReceivingOrderId(wmsInReceivingOrder.getReceivingOrderId());

                SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
                searchBaseMaterial.setMaterialCode(wmsInReceivingOrderImport.getMaterialCode());
                searchBaseMaterial.setCodeQueryMark(1);
                ResponseEntity<List<BaseMaterial>> rs = baseFeignApi.findList(searchBaseMaterial);
                if(rs.getData().isEmpty()){
                    map.put(s,"未查询到物料基础信息");
                    failMap.add(map);
                    fail.add(i++);
                    break;
                }
                wmsInReceivingOrderDet.setMaterialId(rs.getData().get(0).getMaterialId());
                wmsInReceivingOrderDet.setBatchCode(wmsInReceivingOrderImport.getBatchCode());
                wmsInReceivingOrderDet.setPlanQty(wmsInReceivingOrderImport.getPlanQty());
                wmsInReceivingOrderDet.setActualQty(BigDecimal.ZERO);
                wmsInReceivingOrderDet.setProductionTime(wmsInReceivingOrderImport.getProductionTime());
                wmsInReceivingOrderDet.setCreateTime(new Date());
                wmsInReceivingOrderDet.setCreateUserId(user.getUserId());
                wmsInReceivingOrderDet.setModifiedTime(new Date());
                wmsInReceivingOrderDet.setModifiedUserId(user.getUserId());
                wmsInReceivingOrderDet.setLineStatus((byte)1);
                wmsInReceivingOrderDet.setIfAllIssued((byte)0);
                wmsInReceivingOrderDets.add(wmsInReceivingOrderDet);
            }
            if(wmsInReceivingOrderDets.size()<1){
                wmsInPlanReceivingOrderMapper.deleteByPrimaryKey(wmsInReceivingOrder);
                map.put(s,"无详情数据");
                failMap.add(map);
                fail.add(i++);
            }else {
                wmsInReceivingOrderDetMapper.insertList(wmsInReceivingOrderDets);
            }
        }

        //添加日志
        SysImportAndExportLog log = new SysImportAndExportLog();
        log.setSucceedCount(list.size() - fail.size());
        log.setFailCount(fail.size());
        log.setFailInfo(failMap.toString());
        log.setOperatorUserId(user.getUserId());
        log.setTotalCount(list.size());
        log.setType((byte)1);
        securityFeignApi.add(log);

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int pushDown(String ids) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        String[] idArry = ids.split(",");


        String sysOrderTypeCode =null;//当前单据类型编码
        String coreSourceSysOrderTypeCode = null;//核心单据类型编码
        //查当前单据的下游单据
        SearchBaseOrderFlow searchBaseOrderFlow = new SearchBaseOrderFlow();
        searchBaseOrderFlow.setBusinessType((byte)1);
        searchBaseOrderFlow.setOrderNode((byte)4);
        BaseOrderFlow baseOrderFlow = baseFeignApi.findOrderFlow(searchBaseOrderFlow).getData();
        if(StringUtils.isEmpty(baseOrderFlow)){
            throw new BizErrorException("未找到当前单据配置的下游单据");
        }

        List<WmsInReceivingOrderDet> wmsInReceivingOrderDets = new ArrayList<>();
        for (String id : idArry) {
            WmsInReceivingOrderDet wmsInReceivingOrderDet = wmsInReceivingOrderDetMapper.selectByPrimaryKey(id);
            wmsInReceivingOrderDets.add(wmsInReceivingOrderDet);
        }

        switch (baseOrderFlow.getNextOrderTypeCode()){
            case "QMS-MIIO":
                //来料检验
                List<QmsIncomingInspectionOrderDto> qmsIncomingInspectionOrders = new ArrayList<>();
                for (WmsInReceivingOrderDet wmsInReceivingOrderDet : wmsInReceivingOrderDets) {

                    WmsInReceivingOrder wmsInReceivingOrder = wmsInReceivingOrderMapper.selectByPrimaryKey(wmsInReceivingOrderDet.getReceivingOrderId());
                    sysOrderTypeCode = wmsInReceivingOrder.getSysOrderTypeCode();
                    coreSourceSysOrderTypeCode = wmsInReceivingOrder.getCoreSourceSysOrderTypeCode();

                    QmsIncomingInspectionOrderDto qmsIncomingInspectionOrder = new QmsIncomingInspectionOrderDto();
                    qmsIncomingInspectionOrder.setCoreSourceOrderCode(wmsInReceivingOrderDet.getCoreSourceOrderCode());
                    qmsIncomingInspectionOrder.setSourceOrderCode(wmsInReceivingOrder.getReceivingOrderCode());
                    qmsIncomingInspectionOrder.setSourceId(wmsInReceivingOrderDet.getReceivingOrderDetId());
                    qmsIncomingInspectionOrder.setMaterialId(wmsInReceivingOrderDet.getMaterialId());
                    qmsIncomingInspectionOrder.setOrderQty(wmsInReceivingOrderDet.getPlanQty());
                    qmsIncomingInspectionOrder.setInspectionStatus((byte)1);
                    qmsIncomingInspectionOrders.add(qmsIncomingInspectionOrder);
                }
                ResponseEntity responseEntity =qmsFeignApi.batchAdd(qmsIncomingInspectionOrders);
                if(responseEntity.getCode()!=0){
                    throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
                }
                break;
            case "IN-IWK":
                //上架作业
                //生成上架作业单
                List<WmsInnerJobOrderDet> detList = new LinkedList<>();
                int lineNumber = 1;
                for (WmsInReceivingOrderDet wmsInReceivingOrderDet : wmsInReceivingOrderDets) {
                    WmsInPlanReceivingOrder wmsInPlanReceivingOrder = wmsInPlanReceivingOrderMapper.selectByPrimaryKey(wmsInReceivingOrderDet.getReceivingOrderId());
                    sysOrderTypeCode = wmsInPlanReceivingOrder.getSysOrderTypeCode();
                    coreSourceSysOrderTypeCode = wmsInPlanReceivingOrder.getCoreSourceSysOrderTypeCode();
                    WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
                    wmsInnerJobOrderDet.setCoreSourceOrderCode(wmsInReceivingOrderDet.getCoreSourceOrderCode());
                    wmsInnerJobOrderDet.setSourceOrderCode(wmsInPlanReceivingOrder.getPlanReceivingOrderCode());
                    wmsInnerJobOrderDet.setSourceId(wmsInReceivingOrderDet.getReceivingOrderDetId());
                    wmsInnerJobOrderDet.setLineNumber(lineNumber+"");
                    wmsInnerJobOrderDet.setMaterialId(wmsInReceivingOrderDet.getMaterialId());
                    wmsInnerJobOrderDet.setPlanQty(wmsInReceivingOrderDet.getPlanQty());
                    wmsInnerJobOrderDet.setLineStatus((byte)1);
                    detList.add(wmsInnerJobOrderDet);
                    lineNumber++;
                }
                WmsInnerJobOrder wmsInnerJobOrder = new WmsInnerJobOrder();
                wmsInnerJobOrder.setSourceSysOrderTypeCode(sysOrderTypeCode);
                wmsInnerJobOrder.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                wmsInnerJobOrder.setJobOrderType((byte)1);
                wmsInnerJobOrder.setOrderStatus((byte)1);
                wmsInnerJobOrder.setCreateUserId(sysUser.getUserId());
                wmsInnerJobOrder.setCreateTime(new Date());
                wmsInnerJobOrder.setModifiedUserId(sysUser.getUserId());
                wmsInnerJobOrder.setModifiedTime(new Date());
                wmsInnerJobOrder.setStatus((byte)1);
                wmsInnerJobOrder.setOrgId(sysUser.getOrganizationId());
                wmsInnerJobOrder.setWmsInPutawayOrderDets(detList);

                ResponseEntity rs = innerFeignApi.add(wmsInnerJobOrder);
                if(rs.getCode() != 0){
                    throw new BizErrorException("下推生成上架作业单失败");
                }
                break;
            default:
                throw new BizErrorException("单据流配置错误");
        }
        int num = 0;
        for (WmsInReceivingOrderDet wmsInReceivingOrderDet : wmsInReceivingOrderDets) {
            wmsInReceivingOrderDet.setIfAllIssued((byte)1);
            wmsInReceivingOrderDet.setLineStatus((byte)2);
            wmsInReceivingOrderDetMapper.updateByPrimaryKeySelective(wmsInReceivingOrderDet);
            WmsInReceivingOrder wmsInReceivingOrder = new WmsInReceivingOrder();
            wmsInReceivingOrder.setOrderStatus((byte)2);
            num = wmsInReceivingOrderMapper.updateByPrimaryKeySelective(wmsInReceivingOrder);
        }
        return num;
    }
}
