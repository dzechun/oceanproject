package com.fantechs.provider.wms.in.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysImportAndExportLog;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsIncomingInspectionOrderDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInPlanReceivingOrderDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInReceivingOrderBarcode;
import com.fantechs.common.base.general.dto.wms.in.WmsInReceivingOrderDetDto;
import com.fantechs.common.base.general.dto.wms.in.imports.WmsInPlanReceivingOrderImport;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeReOrderDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseOrderFlow;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrderFlow;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehouse;
import com.fantechs.common.base.general.entity.wms.in.*;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerMaterialBarcodeReOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.qms.QmsFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.wms.in.mapper.WmsInHtPlanReceivingOrderDetMapper;
import com.fantechs.provider.wms.in.mapper.WmsInHtPlanReceivingOrderMapper;
import com.fantechs.provider.wms.in.mapper.WmsInPlanReceivingOrderDetMapper;
import com.fantechs.provider.wms.in.mapper.WmsInPlanReceivingOrderMapper;
import com.fantechs.provider.wms.in.service.WmsInPlanReceivingOrderService;
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
public class WmsInPlanReceivingOrderServiceImpl extends BaseService<WmsInPlanReceivingOrder> implements WmsInPlanReceivingOrderService {

    @Resource
    private WmsInPlanReceivingOrderMapper wmsInPlanReceivingOrderMapper;
    @Resource
    private WmsInPlanReceivingOrderDetMapper wmsInPlanReceivingOrderDetMapper;
    @Resource
    private WmsInHtPlanReceivingOrderMapper wmsInHtPlanReceivingOrderMapper;
    @Resource
    private WmsInHtPlanReceivingOrderDetMapper wmsInHtPlanReceivingOrderDetMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private InnerFeignApi innerFeignApi;
    @Resource
    private WmsInReceivingOrderService wmsInReceivingOrderService;
    @Resource
    private QmsFeignApi qmsFeignApi;

    @Override
    public List<WmsInPlanReceivingOrderDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return wmsInPlanReceivingOrderMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(WmsInPlanReceivingOrder record) {
        if(StringUtils.isEmpty(record.getWarehouseId())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"仓库不能为空");
        }
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        record.setPlanReceivingOrderCode(CodeUtils.getId("IN-SPO"));
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrderStatus((byte)1);
        record.setOrgId(sysUser.getOrganizationId());
        record.setSysOrderTypeCode("IN-SPO");
        if(StringUtils.isEmpty(record.getSourceBigType())){
            record.setSourceBigType((byte)2);
        }
        int num = wmsInPlanReceivingOrderMapper.insertUseGeneratedKeys(record);

        //履历记录
        WmsInHtPlanReceivingOrder wmsInHtPlanReceivingOrder = new WmsInHtPlanReceivingOrder();
        BeanUtils.copyProperties(record,wmsInHtPlanReceivingOrder);
        wmsInHtPlanReceivingOrderMapper.insertSelective(wmsInHtPlanReceivingOrder);

        if(StringUtils.isNotEmpty(record.getInPlanReceivingOrderDets())){
            List<WmsInPlanReceivingOrderDet> detList = new ArrayList<>();
            List<WmsInHtPlanReceivingOrderDet> htdetList = new ArrayList<>();
            for (WmsInPlanReceivingOrderDet inPlanReceivingOrderDet : record.getInPlanReceivingOrderDets()) {
                if(StringUtils.isEmpty(inPlanReceivingOrderDet.getPlanQty()) || inPlanReceivingOrderDet.getPlanQty().compareTo(BigDecimal.ZERO)<1){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"计划数量必须大于0");
                }
                inPlanReceivingOrderDet.setCreateTime(new Date());
                inPlanReceivingOrderDet.setCreateUserId(sysUser.getUserId());
                inPlanReceivingOrderDet.setModifiedTime(new Date());
                inPlanReceivingOrderDet.setModifiedUserId(sysUser.getUserId());
                inPlanReceivingOrderDet.setPlanReceivingOrderId(record.getPlanReceivingOrderId());
                inPlanReceivingOrderDet.setOrgId(sysUser.getOrganizationId());
                inPlanReceivingOrderDet.setLineStatus((byte)1);
                inPlanReceivingOrderDet.setIfAllIssued((byte)0);
                detList.add(inPlanReceivingOrderDet);

                WmsInHtPlanReceivingOrderDet wmsInHtPlanReceivingOrderDet = new WmsInHtPlanReceivingOrderDet();
                BeanUtils.copyProperties(inPlanReceivingOrderDet,wmsInHtPlanReceivingOrderDet);
                htdetList.add(wmsInHtPlanReceivingOrderDet);
            }
            if(!detList.isEmpty()){
                num = wmsInPlanReceivingOrderDetMapper.insertList(detList);
                //新增履历
                wmsInHtPlanReceivingOrderDetMapper.insertList(htdetList);
            }
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(WmsInPlanReceivingOrder entity) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();

        if(entity.getSourceBigType()==1){
            throw new BizErrorException(ErrorCodeEnum.OPT20012004.getCode(),"下推单据,无法修改");
        }

        if(StringUtils.isEmpty(entity.getWarehouseId())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"仓库不能为空");
        }
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(sysUser.getUserId());

        //履历记录
        WmsInHtPlanReceivingOrder wmsInHtPlanReceivingOrder = new WmsInHtPlanReceivingOrder();
        BeanUtils.copyProperties(entity,wmsInHtPlanReceivingOrder);
        wmsInHtPlanReceivingOrderMapper.insertSelective(wmsInHtPlanReceivingOrder);

        //删除原有明细
        Example example = new Example(WmsInPlanReceivingOrderDet.class);
        example.createCriteria().andEqualTo("planReceivingOrderId",entity.getPlanReceivingOrderId());
        wmsInPlanReceivingOrderDetMapper.deleteByExample(example);

        List<WmsInHtPlanReceivingOrderDet> htdetList = new ArrayList<>();
        for (WmsInPlanReceivingOrderDet inPlanReceivingOrderDet : entity.getInPlanReceivingOrderDets()) {
            if(StringUtils.isEmpty(inPlanReceivingOrderDet.getPlanQty()) || inPlanReceivingOrderDet.getPlanQty().compareTo(BigDecimal.ZERO)<1){
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"计划数量必须大于0");
            }
            inPlanReceivingOrderDet.setPlanReceivingOrderId(entity.getPlanReceivingOrderId());
            inPlanReceivingOrderDet.setCreateUserId(entity.getCreateUserId());
            inPlanReceivingOrderDet.setCreateTime(entity.getCreateTime());
            inPlanReceivingOrderDet.setModifiedTime(new Date());
            inPlanReceivingOrderDet.setModifiedUserId(sysUser.getUserId());
            inPlanReceivingOrderDet.setOrgId(sysUser.getOrganizationId());

            WmsInHtPlanReceivingOrderDet wmsInHtPlanReceivingOrderDet = new WmsInHtPlanReceivingOrderDet();
            BeanUtils.copyProperties(inPlanReceivingOrderDet,wmsInHtPlanReceivingOrderDet);
            htdetList.add(wmsInHtPlanReceivingOrderDet);
        }
        if(!entity.getInPlanReceivingOrderDets().isEmpty()){
            wmsInPlanReceivingOrderDetMapper.insertList(entity.getInPlanReceivingOrderDets());
            wmsInHtPlanReceivingOrderDetMapper.insertList(htdetList);
        }
        return super.update(entity);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            WmsInPlanReceivingOrder wmsInPlanReceivingOrder = wmsInPlanReceivingOrderMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(wmsInPlanReceivingOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012000,id);
            }
            if(wmsInPlanReceivingOrder.getSourceBigType()==1){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004.getCode(),"下推单据,无法删除");
            }
            if(wmsInPlanReceivingOrder.getOrderStatus()>1){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }
            //删除相关明细
            Example example = new Example(WmsInPlanReceivingOrderDet.class);
            example.createCriteria().andEqualTo("planReceivingOrderId",wmsInPlanReceivingOrder.getPlanReceivingOrderId());
            wmsInPlanReceivingOrderDetMapper.deleteByExample(example);
        }
        return super.batchDelete(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<WmsInPlanReceivingOrderImport> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        Map<String,String > map = new HashMap<>();
        List<Map<String,String>> failMap = new ArrayList<>();  //记录操作失败行数
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        int i=0;
        Map<String,List<WmsInPlanReceivingOrderImport>> hashMap = list.stream().collect(Collectors.groupingBy(WmsInPlanReceivingOrderImport::getWarehouseName));
        Set<String> set = hashMap.keySet();
        for (String s : set) {
            List<WmsInPlanReceivingOrderImport> importList = hashMap.get(s);
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
            WmsInPlanReceivingOrder wmsInPlanReceivingOrder = new WmsInPlanReceivingOrder();
            wmsInPlanReceivingOrder.setPlanReceivingOrderCode(CodeUtils.getId("IN-SPO"));
            wmsInPlanReceivingOrder.setWarehouseId(responseEntity.getData().get(0).getWarehouseId());
            wmsInPlanReceivingOrder.setCreateTime(new Date());
            wmsInPlanReceivingOrder.setCreateUserId(user.getUserId());
            wmsInPlanReceivingOrder.setModifiedTime(new Date());
            wmsInPlanReceivingOrder.setModifiedUserId(user.getUserId());
            wmsInPlanReceivingOrder.setOrderStatus((byte)1);
            wmsInPlanReceivingOrder.setOrgId(user.getOrganizationId());
            int num = wmsInPlanReceivingOrderMapper.insertUseGeneratedKeys(wmsInPlanReceivingOrder);
            List<WmsInPlanReceivingOrderDet> wmsInPlanReceivingOrderDets = new ArrayList<>();
            for (WmsInPlanReceivingOrderImport wmsInPlanReceivingOrderImport : importList) {
                WmsInPlanReceivingOrderDet wmsInPlanReceivingOrderDet = new WmsInPlanReceivingOrderDet();
                wmsInPlanReceivingOrderDet.setPlanReceivingOrderId(wmsInPlanReceivingOrder.getPlanReceivingOrderId());

                SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
                searchBaseMaterial.setMaterialCode(wmsInPlanReceivingOrderImport.getMaterialCode());
                searchBaseMaterial.setCodeQueryMark(1);
                ResponseEntity<List<BaseMaterial>> rs = baseFeignApi.findList(searchBaseMaterial);
                if(rs.getData().isEmpty()){
                    map.put(s,"未查询到物料基础信息");
                    failMap.add(map);
                    fail.add(i++);
                    break;
                }
                wmsInPlanReceivingOrderDet.setMaterialId(rs.getData().get(0).getMaterialId());
                wmsInPlanReceivingOrderDet.setBatchCode(wmsInPlanReceivingOrderImport.getBatchCode());
                wmsInPlanReceivingOrderDet.setPlanQty(wmsInPlanReceivingOrderImport.getPlanQty());
                wmsInPlanReceivingOrderDet.setActualQty(BigDecimal.ZERO);
                wmsInPlanReceivingOrderDet.setProductionTime(wmsInPlanReceivingOrderDet.getProductionTime());
                wmsInPlanReceivingOrderDet.setCreateTime(new Date());
                wmsInPlanReceivingOrderDet.setCreateUserId(user.getUserId());
                wmsInPlanReceivingOrderDet.setModifiedTime(new Date());
                wmsInPlanReceivingOrderDet.setModifiedUserId(user.getUserId());
                wmsInPlanReceivingOrderDet.setLineStatus((byte)1);
                wmsInPlanReceivingOrderDet.setIfAllIssued((byte)0);
                wmsInPlanReceivingOrderDets.add(wmsInPlanReceivingOrderDet);
            }
            if(wmsInPlanReceivingOrderDets.size()<1){
                wmsInPlanReceivingOrderMapper.deleteByPrimaryKey(wmsInPlanReceivingOrder);
                map.put(s,"无详情数据");
                failMap.add(map);
                fail.add(i++);
            }else {
                wmsInPlanReceivingOrderDetMapper.insertList(wmsInPlanReceivingOrderDets);
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
        List<WmsInPlanReceivingOrderDet> wmsInPlanReceivingOrderDets = wmsInPlanReceivingOrderDetMapper.selectByIds(ids);
        if(StringUtils.isEmpty(wmsInPlanReceivingOrderDets)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"请选择下推的物料");
        }
        Map<String,List<WmsInPlanReceivingOrderDet>> map = new HashMap<>();
        //赛选出是否存在按物料维度下推的单据
        for (WmsInPlanReceivingOrderDet wmsInPlanReceivingOrderDet : wmsInPlanReceivingOrderDets) {
            if(wmsInPlanReceivingOrderDet.getIfAllIssued()==1){
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"重复下推");
            }
            //查当前单据的下游单据
            SearchBaseOrderFlow searchBaseOrderFlow = new SearchBaseOrderFlow();
            searchBaseOrderFlow.setOrderTypeCode("IN-SPO");
            searchBaseOrderFlow.setMaterialId(wmsInPlanReceivingOrderDet.getMaterialId());
            BaseOrderFlow baseOrderFlow = baseFeignApi.findOrderFlow(searchBaseOrderFlow).getData();
            if(StringUtils.isEmpty(baseOrderFlow)){
                throw new BizErrorException("未找到当前单据配置的下游单据");
            }else
            if(map.containsKey(baseOrderFlow.getNextOrderTypeCode())){
                List<WmsInPlanReceivingOrderDet> list = map.get(baseOrderFlow.getNextOrderTypeCode());
                list.add(wmsInPlanReceivingOrderDet);
                map.put(baseOrderFlow.getNextOrderTypeCode(),list);
            }else {
                List<WmsInPlanReceivingOrderDet> list = new ArrayList<>();
                list.add(wmsInPlanReceivingOrderDet);
                map.put(baseOrderFlow.getNextOrderTypeCode(),list);
            }
        }
//        //查当前单据的下游单据
//        SearchBaseOrderFlow searchBaseOrderFlow = new SearchBaseOrderFlow();
//        searchBaseOrderFlow.setOrderTypeCode("IN-SPO");
//        BaseOrderFlow baseOrderFlow = baseFeignApi.findOrderFlow(searchBaseOrderFlow).getData();
//        if(StringUtils.isEmpty(baseOrderFlow)){
//            throw new BizErrorException("未找到当前单据配置的下游单据");
//        }
        Set<String> set = map.keySet();
        for (String s : set) {
            List<WmsInPlanReceivingOrderDet> list = map.get(s);
            WmsInPlanReceivingOrder wmsInPlanReceivingOrder = wmsInPlanReceivingOrderMapper.selectByPrimaryKey(list.get(0).getPlanReceivingOrderId());
            String sysOrderTypeCode =null;//当前单据类型编码
            String coreSourceSysOrderTypeCode = null;//核心单据类型编码
            switch (s){
                case "IN-SWK":
                    //收获作业
                    List<WmsInReceivingOrderDetDto> receivingOrderDets = new ArrayList<>();
                    for (WmsInPlanReceivingOrderDet wmsInPlanReceivingOrderDet : list) {
                        sysOrderTypeCode = wmsInPlanReceivingOrder.getSysOrderTypeCode();
                        coreSourceSysOrderTypeCode = wmsInPlanReceivingOrder.getCoreSourceSysOrderTypeCode();
                        WmsInReceivingOrderDetDto wmsInReceivingOrderDet = new WmsInReceivingOrderDetDto();
                        wmsInReceivingOrderDet.setCoreSourceOrderCode(wmsInPlanReceivingOrderDet.getCoreSourceOrderCode());
                        wmsInReceivingOrderDet.setSourceOrderCode(wmsInPlanReceivingOrder.getPlanReceivingOrderCode());
                        wmsInReceivingOrderDet.setSourceId(wmsInPlanReceivingOrderDet.getPlanReceivingOrderDetId());
                        wmsInReceivingOrderDet.setLineStatus((byte)1);
                        wmsInReceivingOrderDet.setLineNumber(wmsInPlanReceivingOrderDet.getLineNumber());
                        wmsInReceivingOrderDet.setPlanQty(wmsInPlanReceivingOrderDet.getPlanQty());
                        wmsInReceivingOrderDet.setMaterialId(wmsInPlanReceivingOrderDet.getMaterialId());
                        wmsInReceivingOrderDet.setBatchCode(wmsInReceivingOrderDet.getBatchCode());
                        wmsInReceivingOrderDet.setProductionTime(wmsInPlanReceivingOrderDet.getProductionTime());
                        wmsInReceivingOrderDet.setActualQty(BigDecimal.ZERO);
                        wmsInReceivingOrderDet.setCreateTime(new Date());
                        wmsInReceivingOrderDet.setCreateUserId(sysUser.getUserId());
                        wmsInReceivingOrderDet.setModifiedTime(new Date());
                        wmsInReceivingOrderDet.setModifiedUserId(sysUser.getUserId());
                        wmsInReceivingOrderDet.setOrgId(sysUser.getOrganizationId());
                        wmsInReceivingOrderDet.setIfAllIssued((byte)0);

                        //查找是否有条码
                        SearchWmsInnerMaterialBarcodeReOrder searchWmsInnerMaterialBarcodeReOrder = new SearchWmsInnerMaterialBarcodeReOrder();
                        searchWmsInnerMaterialBarcodeReOrder.setOrderId(wmsInPlanReceivingOrder.getPlanReceivingOrderId());
                        searchWmsInnerMaterialBarcodeReOrder.setOrderDetId(wmsInPlanReceivingOrderDet.getPlanReceivingOrderDetId());
                        searchWmsInnerMaterialBarcodeReOrder.setOrderTypeCode("IN-SPO");
                        ResponseEntity<List<WmsInnerMaterialBarcodeReOrderDto>> listResponseEntity = innerFeignApi.findList(searchWmsInnerMaterialBarcodeReOrder);
                        if(listResponseEntity.getCode()!=0){
                            throw new BizErrorException(listResponseEntity.getCode(),listResponseEntity.getMessage());
                        }
                        List<WmsInReceivingOrderBarcode> barcodeList = new ArrayList<>();
                        for (WmsInnerMaterialBarcodeReOrderDto datum : listResponseEntity.getData()) {
                            WmsInReceivingOrderBarcode wmsInReceivingOrderBarcode = new WmsInReceivingOrderBarcode();
                            wmsInReceivingOrderBarcode.setMaterialBarcodeReOrderId(datum.getMaterialBarcodeReOrderId());
                            wmsInReceivingOrderBarcode.setMaterialBarcodeId(datum.getMaterialBarcodeId());
                            barcodeList.add(wmsInReceivingOrderBarcode);
                        }
                        wmsInReceivingOrderDet.setWmsInReceivingOrderBarcodeList(barcodeList);

                        //更新表头为执行中
                        wmsInPlanReceivingOrder.setOrderStatus((byte)2);

                        receivingOrderDets.add(wmsInReceivingOrderDet);
                    }
                    WmsInReceivingOrder wmsInReceivingOrder = new WmsInReceivingOrder();
                    wmsInReceivingOrder.setSourceBigType((byte)1);
                    wmsInReceivingOrder.setSourceSysOrderTypeCode(sysOrderTypeCode);
                    wmsInReceivingOrder.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                    wmsInReceivingOrder.setCreateUserId(sysUser.getUserId());
                    wmsInReceivingOrder.setCreateTime(new Date());
                    wmsInReceivingOrder.setWarehouseId(wmsInPlanReceivingOrder.getWarehouseId());
                    wmsInReceivingOrder.setModifiedUserId(sysUser.getUserId());
                    wmsInReceivingOrder.setModifiedTime(new Date());
                    wmsInReceivingOrder.setStatus((byte)1);
                    wmsInReceivingOrder.setOrgId(sysUser.getOrganizationId());
                    wmsInReceivingOrder.setOrderStatus((byte)1);
                    wmsInReceivingOrder.setWmsInReceivingOrderDets(receivingOrderDets);
                    wmsInReceivingOrderService.save(wmsInReceivingOrder);
                    break;
                case "QMS-MIIO":
                    //来料检验
                    List<QmsIncomingInspectionOrderDto> qmsIncomingInspectionOrders = new ArrayList<>();
                    for (WmsInPlanReceivingOrderDet wmsInPlanReceivingOrderDet : list) {

                        //WmsInPlanReceivingOrder wmsInPlanReceivingOrder = wmsInPlanReceivingOrderMapper.selectByPrimaryKey(wmsInPlanReceivingOrderDet.getPlanReceivingOrderId());
                        sysOrderTypeCode = wmsInPlanReceivingOrder.getSysOrderTypeCode();
                        coreSourceSysOrderTypeCode = wmsInPlanReceivingOrder.getCoreSourceSysOrderTypeCode();

                        QmsIncomingInspectionOrderDto qmsIncomingInspectionOrder = new QmsIncomingInspectionOrderDto();
                        qmsIncomingInspectionOrder.setCoreSourceOrderCode(wmsInPlanReceivingOrderDet.getCoreSourceOrderCode());
                        qmsIncomingInspectionOrder.setSourceOrderCode(wmsInPlanReceivingOrder.getPlanReceivingOrderCode());
                        qmsIncomingInspectionOrder.setSourceId(wmsInPlanReceivingOrderDet.getPlanReceivingOrderDetId());
                        qmsIncomingInspectionOrder.setWarehouseId(wmsInPlanReceivingOrder.getWarehouseId());
                        qmsIncomingInspectionOrder.setMaterialId(wmsInPlanReceivingOrderDet.getMaterialId());
                        qmsIncomingInspectionOrder.setOrderQty(wmsInPlanReceivingOrderDet.getPlanQty());
                        qmsIncomingInspectionOrder.setSourceSysOrderTypeCode(sysOrderTypeCode);
                        qmsIncomingInspectionOrder.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
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

                    //获取默认收货库位
                    SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
                    searchBaseStorage.setWarehouseId(wmsInPlanReceivingOrder.getWarehouseId());
                    searchBaseStorage.setStorageType((byte)2);
                    List<BaseStorage> storageList = baseFeignApi.findList(searchBaseStorage).getData();
                    if(storageList.size()<1){
                        throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"未维护仓库收货库位");
                    }

                    List<WmsInnerJobOrderDet> detList = new LinkedList<>();
                    int lineNumber = 1;
                    for (WmsInPlanReceivingOrderDet wmsInPlanReceivingOrderDet : list) {
                        //WmsInPlanReceivingOrder wmsInPlanReceivingOrder = wmsInPlanReceivingOrderMapper.selectByPrimaryKey(wmsInPlanReceivingOrderDets.get(0).getPlanReceivingOrderId());
                        sysOrderTypeCode = wmsInPlanReceivingOrder.getSysOrderTypeCode();
                        coreSourceSysOrderTypeCode = wmsInPlanReceivingOrder.getCoreSourceSysOrderTypeCode();
                        WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
                        wmsInnerJobOrderDet.setCoreSourceOrderCode(wmsInPlanReceivingOrderDet.getCoreSourceOrderCode());
                        wmsInnerJobOrderDet.setSourceOrderCode(wmsInPlanReceivingOrder.getPlanReceivingOrderCode());
                        wmsInnerJobOrderDet.setSourceId(wmsInPlanReceivingOrderDet.getPlanReceivingOrderDetId());
                        wmsInnerJobOrderDet.setLineNumber(lineNumber+"");
                        wmsInnerJobOrderDet.setMaterialId(wmsInPlanReceivingOrderDet.getMaterialId());
                        wmsInnerJobOrderDet.setPlanQty(wmsInPlanReceivingOrderDet.getPlanQty());
                        wmsInnerJobOrderDet.setLineStatus((byte)1);
                        wmsInnerJobOrderDet.setOutStorageId(storageList.get(0).getStorageId());

                        //查找是否有条码
//                    SearchWmsInnerMaterialBarcodeReOrder searchWmsInnerMaterialBarcodeReOrder = new SearchWmsInnerMaterialBarcodeReOrder();
//                    searchWmsInnerMaterialBarcodeReOrder.setOrderId(wmsInPlanReceivingOrder.getPlanReceivingOrderId());
//                    searchWmsInnerMaterialBarcodeReOrder.setOrderDetId(wmsInPlanReceivingOrderDet.getPlanReceivingOrderDetId());
//                    searchWmsInnerMaterialBarcodeReOrder.setOrderTypeCode("IN-SPO");
//                    ResponseEntity<List<WmsInnerMaterialBarcodeReOrderDto>> listResponseEntity = innerFeignApi.findList(searchWmsInnerMaterialBarcodeReOrder);
//                    if(listResponseEntity.getCode()!=0){
//                        throw new BizErrorException(listResponseEntity.getCode(),listResponseEntity.getMessage());
//                    }
//                    List<WmsInReceivingOrderBarcode> barcodeList = new ArrayList<>();
//                    for (WmsInnerMaterialBarcodeReOrderDto datum : listResponseEntity.getData()) {
//                        WmsInReceivingOrderBarcode wmsInReceivingOrderBarcode = new WmsInReceivingOrderBarcode();
//                        wmsInReceivingOrderBarcode.setMaterialBarcodeReOrderId(datum.getMaterialBarcodeReOrderId());
//                        wmsInReceivingOrderBarcode.setMaterialBarcodeId(datum.getMaterialBarcodeId());
//                        barcodeList.add(wmsInReceivingOrderBarcode);
//                    }
                        detList.add(wmsInnerJobOrderDet);
                        lineNumber++;
                    }
                    WmsInnerJobOrder wmsInnerJobOrder = new WmsInnerJobOrder();
                    wmsInnerJobOrder.setSourceSysOrderTypeCode(sysOrderTypeCode);
                    wmsInnerJobOrder.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                    wmsInnerJobOrder.setWarehouseId(wmsInPlanReceivingOrder.getWarehouseId());
                    wmsInnerJobOrder.setJobOrderType((byte)1);
                    wmsInnerJobOrder.setOrderStatus((byte)1);
                    wmsInnerJobOrder.setCreateUserId(sysUser.getUserId());
                    wmsInnerJobOrder.setCreateTime(new Date());
                    wmsInnerJobOrder.setModifiedUserId(sysUser.getUserId());
                    wmsInnerJobOrder.setModifiedTime(new Date());
                    wmsInnerJobOrder.setStatus((byte)1);
                    wmsInnerJobOrder.setOrgId(sysUser.getOrganizationId());
                    wmsInnerJobOrder.setSourceBigType((byte)1);
                    wmsInnerJobOrder.setWmsInPutawayOrderDets(detList);

                    ResponseEntity rs = innerFeignApi.add(wmsInnerJobOrder);
                    if(rs.getCode() != 0){
                        throw new BizErrorException(rs.getCode(),rs.getMessage());
                    }
                    break;
                default:
                    throw new BizErrorException("单据流配置错误");
            }
        }
        int num = 0;
        if(set.size()>0){
            for (WmsInPlanReceivingOrderDet wmsInPlanReceivingOrderDet : wmsInPlanReceivingOrderDets) {
                wmsInPlanReceivingOrderDet.setIfAllIssued((byte)1);
                wmsInPlanReceivingOrderDet.setLineStatus((byte)2);
                num = wmsInPlanReceivingOrderDetMapper.updateByPrimaryKeySelective(wmsInPlanReceivingOrderDet);
                WmsInPlanReceivingOrder wms = new WmsInPlanReceivingOrder();
                wms.setPlanReceivingOrderId(wmsInPlanReceivingOrderDet.getPlanReceivingOrderId());
                wms.setOrderStatus((byte)2);
                wmsInPlanReceivingOrderMapper.updateByPrimaryKeySelective(wms);
            }
        }
        return num;
    }
}
