package com.fantechs.provider.om.service.impl;

import cn.hutool.core.date.DateTime;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.entity.security.SysImportAndExportLog;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrderFlowDto;
import com.fantechs.common.base.general.dto.om.OmPurchaseReturnOrderDetDto;
import com.fantechs.common.base.general.dto.om.OmPurchaseReturnOrderDto;
import com.fantechs.common.base.general.dto.om.imports.OmPurchaseReturnOrderImport;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryReqOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryReqOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanDeliveryOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanDeliveryOrderDto;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.*;
import com.fantechs.common.base.general.entity.om.*;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.api.wms.out.OutFeignApi;
import com.fantechs.provider.om.mapper.OmPurchaseReturnOrderDetMapper;
import com.fantechs.provider.om.mapper.OmPurchaseReturnOrderMapper;
import com.fantechs.provider.om.mapper.ht.OmHtPurchaseReturnOrderDetMapper;
import com.fantechs.provider.om.mapper.ht.OmHtPurchaseReturnOrderMapper;
import com.fantechs.provider.om.service.OmPurchaseReturnOrderService;
import com.fantechs.provider.om.util.OrderFlowUtil;
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
 * Created by leifengzhi on 2021/12/20.
 */
@Service
public class OmPurchaseReturnOrderServiceImpl extends BaseService<OmPurchaseReturnOrder> implements OmPurchaseReturnOrderService {

    @Resource
    private OmPurchaseReturnOrderMapper omPurchaseReturnOrderMapper;
    @Resource
    private OmPurchaseReturnOrderDetMapper omPurchaseReturnOrderDetMapper;
    @Resource
    private OmHtPurchaseReturnOrderMapper omHtPurchaseReturnOrderMapper;
    @Resource
    private OmHtPurchaseReturnOrderDetMapper omHtPurchaseReturnOrderDetMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private InnerFeignApi innerFeignApi;
    @Resource
    private OutFeignApi outFeignApi;

    @Override
    public List<OmPurchaseReturnOrderDto> findList(Map<String, Object> map) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",currentUserInfo.getOrganizationId());
        return omPurchaseReturnOrderMapper.findList(map);
    }

    @Override
    public List<OmHtPurchaseReturnOrder> findHtList(Map<String, Object> map) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",currentUserInfo.getOrganizationId());
        return omHtPurchaseReturnOrderMapper.findHtList(map);
    }

    @Override
    public int purchaseUpdatePickingQty(Long purchaseReturnOrderDetId, BigDecimal actualQty) {
        Example example = new Example(OmPurchaseReturnOrderDet.class);
        example.createCriteria().andEqualTo("purchaseReturnOrderDetId",purchaseReturnOrderDetId);
        List<OmPurchaseReturnOrderDet> omPurchaseReturnOrderDets = omPurchaseReturnOrderDetMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(omPurchaseReturnOrderDets) && omPurchaseReturnOrderDets.size() == 1) {
            OmPurchaseReturnOrderDet omPurchaseReturnOrderDet = omPurchaseReturnOrderDets.get(0);
            BigDecimal qty = omPurchaseReturnOrderDet.getActualQty();
            actualQty = actualQty.add((StringUtils.isNotEmpty(qty)?qty:new BigDecimal(0)));
            omPurchaseReturnOrderDet.setActualQty(actualQty);
            omPurchaseReturnOrderDetMapper.updateByPrimaryKeySelective(omPurchaseReturnOrderDet);
        }

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int pushDown(List<OmPurchaseReturnOrderDetDto> omPurchaseReturnOrderDetDtos) {
        int i;
        for (OmPurchaseReturnOrderDetDto omPurchaseReturnOrderDetDto : omPurchaseReturnOrderDetDtos){
            BigDecimal totalIssueQty = omPurchaseReturnOrderDetDto.getTotalIssueQty() == null ? BigDecimal.ZERO : omPurchaseReturnOrderDetDto.getTotalIssueQty();
            BigDecimal add = totalIssueQty.add(omPurchaseReturnOrderDetDto.getIssueQty());
            if(add.compareTo(omPurchaseReturnOrderDetDto.getOrderQty()) == 1){
                throw new BizErrorException("下发数量不能大于订单数量");
            }else if(add.compareTo(omPurchaseReturnOrderDetDto.getOrderQty()) == 0){
                omPurchaseReturnOrderDetDto.setIfAllIssued((byte)1);
            }
            omPurchaseReturnOrderDetDto.setTotalIssueQty(add);
        }
        i = omPurchaseReturnOrderDetMapper.batchUpdate(omPurchaseReturnOrderDetDtos);

        //查当前单据类型的所有单据流
        SearchBaseOrderFlow searchBaseOrderFlow = new SearchBaseOrderFlow();
        searchBaseOrderFlow.setOrderTypeCode("OUT-PRO");
        searchBaseOrderFlow.setStatus((byte)1);
        List<BaseOrderFlowDto> baseOrderFlowDtos = baseFeignApi.findAll(searchBaseOrderFlow).getData();
        if (StringUtils.isEmpty(baseOrderFlowDtos)) {
            throw new BizErrorException("未找到当前单据配置的单据流");
        }

        //不同单据流分组
        Map<String, List<OmPurchaseReturnOrderDetDto>> map = new HashMap<>();
        for (OmPurchaseReturnOrderDetDto omPurchaseReturnOrderDetDto : omPurchaseReturnOrderDetDtos) {
            //查当前单据的下游单据
            BaseOrderFlow baseOrderFlow = OrderFlowUtil.getOrderFlow(baseOrderFlowDtos, omPurchaseReturnOrderDetDto.getMaterialId(), omPurchaseReturnOrderDetDto.getSupplierId());

            String key = baseOrderFlow.getNextOrderTypeCode();
            if (map.get(key) == null) {
                List<OmPurchaseReturnOrderDetDto> diffOrderFlows = new LinkedList<>();
                diffOrderFlows.add(omPurchaseReturnOrderDetDto);
                map.put(key, diffOrderFlows);
            } else {
                List<OmPurchaseReturnOrderDetDto> diffOrderFlows = map.get(key);
                diffOrderFlows.add(omPurchaseReturnOrderDetDto);
                map.put(key, diffOrderFlows);
            }
        }

        Set<String> codes = map.keySet();
        for (String code : codes) {
            List<OmPurchaseReturnOrderDetDto> purchaseReturnOrderDetDtos = map.get(code);
            if ("OUT-DRO".equals(code)) {
                //出库通知单
                List<WmsOutDeliveryReqOrderDetDto> wmsOutDeliveryReqOrderDetDtos = new LinkedList<>();
                for (OmPurchaseReturnOrderDetDto omPurchaseReturnOrderDetDto : purchaseReturnOrderDetDtos) {
                    WmsOutDeliveryReqOrderDetDto wmsOutDeliveryReqOrderDetDto = new WmsOutDeliveryReqOrderDetDto();
                    wmsOutDeliveryReqOrderDetDto.setCoreSourceOrderCode(omPurchaseReturnOrderDetDto.getPurchaseReturnOrderCode());
                    wmsOutDeliveryReqOrderDetDto.setSourceOrderCode(omPurchaseReturnOrderDetDto.getPurchaseReturnOrderCode());
                    wmsOutDeliveryReqOrderDetDto.setCoreSourceId(omPurchaseReturnOrderDetDto.getPurchaseReturnOrderDetId());
                    wmsOutDeliveryReqOrderDetDto.setSourceId(omPurchaseReturnOrderDetDto.getPurchaseReturnOrderDetId());
                    wmsOutDeliveryReqOrderDetDto.setMaterialId(omPurchaseReturnOrderDetDto.getMaterialId());
                    wmsOutDeliveryReqOrderDetDto.setOrderQty(omPurchaseReturnOrderDetDto.getIssueQty());
                    wmsOutDeliveryReqOrderDetDto.setLineStatus((byte) 1);
                    wmsOutDeliveryReqOrderDetDtos.add(wmsOutDeliveryReqOrderDetDto);
                }
                WmsOutDeliveryReqOrderDto wmsOutDeliveryReqOrderDto = new WmsOutDeliveryReqOrderDto();
                wmsOutDeliveryReqOrderDto.setCoreSourceSysOrderTypeCode("OUT-PRO");
                wmsOutDeliveryReqOrderDto.setSourceSysOrderTypeCode("OUT-PRO");
                wmsOutDeliveryReqOrderDto.setSourceBigType((byte)1);
                wmsOutDeliveryReqOrderDto.setWarehouseId(purchaseReturnOrderDetDtos.get(0).getWarehouseId());
                wmsOutDeliveryReqOrderDto.setWmsOutDeliveryReqOrderDetDtos(wmsOutDeliveryReqOrderDetDtos);
                ResponseEntity responseEntity = outFeignApi.add(wmsOutDeliveryReqOrderDto);
                if (responseEntity.getCode() != 0) {
                    throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
                } else {
                    i++;
                }
            } else if ("OUT-PDO".equals(code)) {
                //出库计划
                List<WmsOutPlanDeliveryOrderDetDto> wmsOutPlanDeliveryOrderDetDtos = new LinkedList<>();
                for (OmPurchaseReturnOrderDetDto omPurchaseReturnOrderDetDto : purchaseReturnOrderDetDtos) {
                    WmsOutPlanDeliveryOrderDetDto wmsOutPlanDeliveryOrderDetDto = new WmsOutPlanDeliveryOrderDetDto();
                    wmsOutPlanDeliveryOrderDetDto.setCoreSourceOrderCode(omPurchaseReturnOrderDetDto.getPurchaseReturnOrderCode());
                    wmsOutPlanDeliveryOrderDetDto.setSourceOrderCode(omPurchaseReturnOrderDetDto.getPurchaseReturnOrderCode());
                    wmsOutPlanDeliveryOrderDetDto.setCoreSourceId(omPurchaseReturnOrderDetDto.getPurchaseReturnOrderDetId());
                    wmsOutPlanDeliveryOrderDetDto.setSourceId(omPurchaseReturnOrderDetDto.getPurchaseReturnOrderDetId());
                    wmsOutPlanDeliveryOrderDetDto.setMaterialId(omPurchaseReturnOrderDetDto.getMaterialId());
                    wmsOutPlanDeliveryOrderDetDto.setOrderQty(omPurchaseReturnOrderDetDto.getIssueQty());
                    wmsOutPlanDeliveryOrderDetDto.setLineStatus((byte) 1);
                    wmsOutPlanDeliveryOrderDetDtos.add(wmsOutPlanDeliveryOrderDetDto);
                }
                WmsOutPlanDeliveryOrderDto wmsOutPlanDeliveryOrderDto = new WmsOutPlanDeliveryOrderDto();
                wmsOutPlanDeliveryOrderDto.setCoreSourceSysOrderTypeCode("OUT-PRO");
                wmsOutPlanDeliveryOrderDto.setSourceSysOrderTypeCode("OUT-PRO");
                wmsOutPlanDeliveryOrderDto.setSourceBigType((byte)1);
                wmsOutPlanDeliveryOrderDto.setWarehouseId(purchaseReturnOrderDetDtos.get(0).getWarehouseId());
                wmsOutPlanDeliveryOrderDto.setWmsOutPlanDeliveryOrderDetDtos(wmsOutPlanDeliveryOrderDetDtos);
                ResponseEntity responseEntity = outFeignApi.add(wmsOutPlanDeliveryOrderDto);
                if (responseEntity.getCode() != 0) {
                    throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
                } else {
                    i++;
                }
            } else if ("OUT-IWK".equals(code)) {
                //拣货作业

                //查询发货库位
                SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
                searchBaseStorage.setWarehouseId(omPurchaseReturnOrderDetDtos.get(0).getWarehouseId());
                searchBaseStorage.setStorageType((byte)3);
                List<BaseStorage> baseStorages = baseFeignApi.findList(searchBaseStorage).getData();
                if(StringUtils.isEmpty(baseStorages)){
                    throw new BizErrorException("该仓库未找到发货库位");
                }
                Long inStorageId = baseStorages.get(0).getStorageId();

                int lineNumber = 1;
                List<WmsInnerJobOrderDet> wmsInnerJobOrderDets = new LinkedList<>();
                for (OmPurchaseReturnOrderDetDto omPurchaseReturnOrderDetDto : purchaseReturnOrderDetDtos) {
                    WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
                    wmsInnerJobOrderDet.setCoreSourceOrderCode(omPurchaseReturnOrderDetDto.getPurchaseReturnOrderCode());
                    wmsInnerJobOrderDet.setSourceOrderCode(omPurchaseReturnOrderDetDto.getPurchaseReturnOrderCode());
                    wmsInnerJobOrderDet.setCoreSourceId(omPurchaseReturnOrderDetDto.getPurchaseReturnOrderDetId());
                    wmsInnerJobOrderDet.setSourceId(omPurchaseReturnOrderDetDto.getPurchaseReturnOrderDetId());
                    wmsInnerJobOrderDet.setLineNumber(lineNumber + "");
                    lineNumber++;
                    wmsInnerJobOrderDet.setMaterialId(omPurchaseReturnOrderDetDto.getMaterialId());
                    wmsInnerJobOrderDet.setBatchCode(omPurchaseReturnOrderDetDto.getBatchCode());
                    wmsInnerJobOrderDet.setPlanQty(omPurchaseReturnOrderDetDto.getIssueQty());
                    wmsInnerJobOrderDet.setLineStatus((byte) 1);
                    wmsInnerJobOrderDet.setInStorageId(inStorageId);
                    wmsInnerJobOrderDets.add(wmsInnerJobOrderDet);
                }
                WmsInnerJobOrder wmsInnerJobOrder = new WmsInnerJobOrder();
                wmsInnerJobOrder.setSourceBigType((byte)1);
                wmsInnerJobOrder.setCoreSourceSysOrderTypeCode("OUT-PRO");
                wmsInnerJobOrder.setSourceSysOrderTypeCode("OUT-PRO");
                wmsInnerJobOrder.setWarehouseId(purchaseReturnOrderDetDtos.get(0).getWarehouseId());
                wmsInnerJobOrder.setJobOrderType((byte) 2);
                wmsInnerJobOrder.setWmsInPutawayOrderDets(wmsInnerJobOrderDets);
                ResponseEntity responseEntity = innerFeignApi.add(wmsInnerJobOrder);
                if (responseEntity.getCode() != 0) {
                    throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
                } else {
                    i++;
                }
            }else {
                throw new BizErrorException("单据流配置错误");
            }
        }

        //修改单据状态
        Byte orderStatus = (byte)3;
        OmPurchaseReturnOrder omPurchaseReturnOrder = omPurchaseReturnOrderMapper.selectByPrimaryKey(omPurchaseReturnOrderDetDtos.get(0).getPurchaseReturnOrderId());
        Example example = new Example(OmPurchaseReturnOrderDet.class);
        example.createCriteria().andEqualTo("purchaseReturnOrderId",omPurchaseReturnOrder.getPurchaseReturnOrderId());
        List<OmPurchaseReturnOrderDet> omPurchaseReturnOrderDets = omPurchaseReturnOrderDetMapper.selectByExample(example);
        for (OmPurchaseReturnOrderDet omPurchaseReturnOrderDet : omPurchaseReturnOrderDets){
            if(omPurchaseReturnOrderDet.getIfAllIssued()==null||omPurchaseReturnOrderDet.getIfAllIssued()!=(byte)1){
                orderStatus = (byte)2;
                break;
            }
        }
        omPurchaseReturnOrder.setOrderStatus(orderStatus);
        omPurchaseReturnOrderMapper.updateByPrimaryKeySelective(omPurchaseReturnOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(OmPurchaseReturnOrderDto record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        record.setPurchaseReturnOrderCode(CodeUtils.getId("OUT-PRO"));
        record.setOrgId(user.getOrganizationId());
        record.setCreateTime(new DateTime());
        record.setCreateUserId(user.getUserId());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new DateTime());
        int i = omPurchaseReturnOrderMapper.insertUseGeneratedKeys(record);

        //明细
        List<OmHtPurchaseReturnOrderDet> htList = new LinkedList<>();
        List<OmPurchaseReturnOrderDetDto> omPurchaseReturnOrderDetDtos = record.getOmPurchaseReturnOrderDetDtos();
        if(StringUtils.isNotEmpty(omPurchaseReturnOrderDetDtos)){
            for (OmPurchaseReturnOrderDetDto omPurchaseReturnOrderDetDto:omPurchaseReturnOrderDetDtos){
                omPurchaseReturnOrderDetDto.setPurchaseReturnOrderId(record.getPurchaseReturnOrderId());
                omPurchaseReturnOrderDetDto.setCreateUserId(user.getUserId());
                omPurchaseReturnOrderDetDto.setCreateTime(new Date());
                omPurchaseReturnOrderDetDto.setModifiedUserId(user.getUserId());
                omPurchaseReturnOrderDetDto.setModifiedTime(new Date());
                omPurchaseReturnOrderDetDto.setOrgId(user.getOrganizationId());

                OmHtPurchaseReturnOrderDet omHtPurchaseReturnOrderDet = new OmHtPurchaseReturnOrderDet();
                org.springframework.beans.BeanUtils.copyProperties(omPurchaseReturnOrderDetDto, omHtPurchaseReturnOrderDet);
                htList.add(omHtPurchaseReturnOrderDet);
            }
            omPurchaseReturnOrderDetMapper.insertList(omPurchaseReturnOrderDetDtos);
            omHtPurchaseReturnOrderDetMapper.insertList(htList);
        }

        //履历
        OmHtPurchaseReturnOrder omHtPurchaseReturnOrder = new OmHtPurchaseReturnOrder();
        org.springframework.beans.BeanUtils.copyProperties(record, omHtPurchaseReturnOrder);
        omHtPurchaseReturnOrderMapper.insertSelective(omHtPurchaseReturnOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(OmPurchaseReturnOrderDto entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        int i = omPurchaseReturnOrderMapper.updateByPrimaryKeySelective(entity);

        //原来有的明细只更新
        ArrayList<Long> idList = new ArrayList<>();
        List<OmPurchaseReturnOrderDetDto> omPurchaseReturnOrderDetDtos = entity.getOmPurchaseReturnOrderDetDtos();
        if(StringUtils.isNotEmpty(omPurchaseReturnOrderDetDtos)) {
            for (OmPurchaseReturnOrderDetDto omPurchaseReturnOrderDetDto : omPurchaseReturnOrderDetDtos) {
                if (StringUtils.isNotEmpty(omPurchaseReturnOrderDetDto.getPurchaseReturnOrderDetId())) {
                    omPurchaseReturnOrderDetMapper.updateByPrimaryKeySelective(omPurchaseReturnOrderDetDto);
                    idList.add(omPurchaseReturnOrderDetDto.getPurchaseReturnOrderDetId());
                }
            }
        }

        //删除原明细
        Example example = new Example(OmPurchaseReturnOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("purchaseReturnOrderId",entity.getPurchaseReturnOrderId());
        if (idList.size() > 0) {
            criteria.andNotIn("purchaseReturnOrderDetId", idList);
        }
        omPurchaseReturnOrderDetMapper.deleteByExample(example);

        //明细
        List<OmHtPurchaseReturnOrderDet> htList = new LinkedList<>();
        if(StringUtils.isNotEmpty(omPurchaseReturnOrderDetDtos)){
            List<OmPurchaseReturnOrderDetDto> addDetList = new LinkedList<>();
            for (OmPurchaseReturnOrderDetDto omPurchaseReturnOrderDetDto : omPurchaseReturnOrderDetDtos){
                OmHtPurchaseReturnOrderDet omHtPurchaseReturnOrderDet = new OmHtPurchaseReturnOrderDet();
                org.springframework.beans.BeanUtils.copyProperties(omPurchaseReturnOrderDetDto, omHtPurchaseReturnOrderDet);
                htList.add(omHtPurchaseReturnOrderDet);

                if (idList.contains(omPurchaseReturnOrderDetDto.getPurchaseReturnOrderDetId())) {
                    continue;
                }
                omPurchaseReturnOrderDetDto.setPurchaseReturnOrderId(entity.getPurchaseReturnOrderId());
                omPurchaseReturnOrderDetDto.setCreateUserId(user.getUserId());
                omPurchaseReturnOrderDetDto.setCreateTime(new Date());
                omPurchaseReturnOrderDetDto.setModifiedUserId(user.getUserId());
                omPurchaseReturnOrderDetDto.setModifiedTime(new Date());
                omPurchaseReturnOrderDetDto.setOrgId(user.getOrganizationId());
                addDetList.add(omPurchaseReturnOrderDetDto);
            }
            if(StringUtils.isNotEmpty(addDetList)) {
                omPurchaseReturnOrderDetMapper.insertList(addDetList);
            }
            if(StringUtils.isNotEmpty(htList)) {
                omHtPurchaseReturnOrderDetMapper.insertList(htList);
            }
        }

        //履历
        OmHtPurchaseReturnOrder omHtPurchaseReturnOrder = new OmHtPurchaseReturnOrder();
        org.springframework.beans.BeanUtils.copyProperties(entity, omHtPurchaseReturnOrder);
        omHtPurchaseReturnOrderMapper.insertSelective(omHtPurchaseReturnOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        //表头履历
        List<OmPurchaseReturnOrder> omPurchaseReturnOrders = omPurchaseReturnOrderMapper.selectByIds(ids);
        List<OmHtPurchaseReturnOrder> htList = new LinkedList<>();
        if(StringUtils.isNotEmpty(omPurchaseReturnOrders)) {
            for (OmPurchaseReturnOrder omPurchaseReturnOrder : omPurchaseReturnOrders) {
                OmHtPurchaseReturnOrder omHtPurchaseReturnOrder = new OmHtPurchaseReturnOrder();
                org.springframework.beans.BeanUtils.copyProperties(omPurchaseReturnOrder, omHtPurchaseReturnOrder);
                htList.add(omHtPurchaseReturnOrder);
            }
            omHtPurchaseReturnOrderMapper.insertList(htList);
        }

        //表体履历
        List<String> idList = Arrays.asList(ids.split(","));
        Example example = new Example(OmPurchaseReturnOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("purchaseReturnOrderId",idList);
        List<OmPurchaseReturnOrderDet> omPurchaseReturnOrderDets = omPurchaseReturnOrderDetMapper.selectByExample(example);
        List<OmHtPurchaseReturnOrderDet> htDetList = new LinkedList<>();
        if(StringUtils.isNotEmpty(omPurchaseReturnOrderDets)) {
            for (OmPurchaseReturnOrderDet omPurchaseReturnOrderDet : omPurchaseReturnOrderDets) {
                OmHtPurchaseReturnOrderDet omHtPurchaseReturnOrderDet = new OmHtPurchaseReturnOrderDet();
                org.springframework.beans.BeanUtils.copyProperties(omPurchaseReturnOrderDet, omHtPurchaseReturnOrderDet);
                htDetList.add(omHtPurchaseReturnOrderDet);
            }
            omHtPurchaseReturnOrderDetMapper.insertList(htDetList);
        }

        omPurchaseReturnOrderDetMapper.deleteByExample(example);

        return omPurchaseReturnOrderMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<OmPurchaseReturnOrderImport> omPurchaseReturnOrderImports) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<OmPurchaseReturnOrder> list = new LinkedList<>();
        LinkedList<OmHtPurchaseReturnOrder> htList = new LinkedList<>();
        LinkedList<OmPurchaseReturnOrderImport> purchaseReturnnOrderImports = new LinkedList<>();
        //日志记录
        StringBuilder succeedInfo = new StringBuilder();
        StringBuilder failInfo = new StringBuilder();
        Integer succeedCount = 0;
        Integer failCount = 0;

        for (int i = 0; i < omPurchaseReturnOrderImports.size(); i++) {
            OmPurchaseReturnOrderImport omPurchaseReturnOrderImport = omPurchaseReturnOrderImports.get(i);
            String supplierCode = omPurchaseReturnOrderImport.getSupplierCode();
            String makeOrderUserCode = omPurchaseReturnOrderImport.getMakeOrderUserCode();

            if (StringUtils.isEmpty(
                    supplierCode, makeOrderUserCode
            )) {
                failCount++;
                failInfo.append("必填项为空").append(",");
                fail.add(i + 4);
                continue;
            }

            //供应商是否存在
            SearchBaseSupplier searchBaseSupplier = new SearchBaseSupplier();
            searchBaseSupplier.setCodeQueryMark((byte)1);
            searchBaseSupplier.setSupplierCode(supplierCode);
            List<BaseSupplier> baseSuppliers = baseFeignApi.findSupplierList(searchBaseSupplier).getData();
            if (StringUtils.isEmpty(baseSuppliers)) {
                failCount++;
                failInfo.append("供应商不存在").append(",");
                fail.add(i + 4);
                continue;
            }
            omPurchaseReturnOrderImport.setSupplierId(baseSuppliers.get(0).getSupplierId());

            //制单人是否存在
            SearchSysUser searchSysUser = new SearchSysUser();
            searchSysUser.setUserCode(makeOrderUserCode);
            List<SysUser> sysUsers = securityFeignApi.selectUsers(searchSysUser).getData();
            if (StringUtils.isEmpty(sysUsers)) {
                failCount++;
                failInfo.append("制单人员不存在").append(",");
                fail.add(i + 4);
                continue;
            }
            omPurchaseReturnOrderImport.setMakeOrderUserId(sysUsers.get(0).getUserId());

            //退货部门
            String returnDeptCode = omPurchaseReturnOrderImport.getReturnDeptCode();
            if(StringUtils.isNotEmpty(returnDeptCode)){
                SearchBaseDept searchBaseDept = new SearchBaseDept();
                searchBaseDept.setDeptCode(returnDeptCode);
                searchBaseDept.setCodeQueryMark(1);
                List<BaseDept> baseDepts = baseFeignApi.selectDepts(searchBaseDept).getData();
                if (StringUtils.isEmpty(baseDepts)){
                    failCount++;
                    failInfo.append("退货部门不存在").append(",");
                    fail.add(i+4);
                    continue;
                }
                omPurchaseReturnOrderImport.setReturnDeptId(baseDepts.get(0).getDeptId());
            }

            //物料
            String materialCode = omPurchaseReturnOrderImport.getMaterialCode();
            if(StringUtils.isNotEmpty(materialCode)){
                SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
                searchBaseMaterial.setMaterialCode(materialCode);
                searchBaseMaterial.setCodeQueryMark(1);
                List<BaseMaterial> baseMaterials = baseFeignApi.findList(searchBaseMaterial).getData();
                if (StringUtils.isEmpty(baseMaterials)){
                    failCount++;
                    failInfo.append("物料编码不存在").append(",");
                    fail.add(i+4);
                    continue;
                }
                omPurchaseReturnOrderImport.setMaterialId(baseMaterials.get(0).getMaterialId());
            }

            //仓库
            String warehouseCode = omPurchaseReturnOrderImport.getWarehouseCode();
            if(StringUtils.isNotEmpty(warehouseCode)){
                SearchBaseWarehouse searchBaseWarehouse = new SearchBaseWarehouse();
                searchBaseWarehouse.setWarehouseCode(warehouseCode);
                searchBaseWarehouse.setCodeQueryMark(1);
                List<BaseWarehouse> baseWarehouses = baseFeignApi.findList(searchBaseWarehouse).getData();
                if (StringUtils.isEmpty(baseWarehouses)){
                    failCount++;
                    failInfo.append("仓库编码不存在").append(",");
                    fail.add(i+4);
                    continue;
                }
                omPurchaseReturnOrderImport.setWarehouseId(baseWarehouses.get(0).getWarehouseId());
            }

            succeedCount++;
            succeedInfo.append(i+4).append(",");
            purchaseReturnnOrderImports.add(omPurchaseReturnOrderImport);
        }

        SysImportAndExportLog sysImportAndExportLog = new SysImportAndExportLog();
        sysImportAndExportLog.setModuleNames("OM");
        sysImportAndExportLog.setFileName("采退订单导入信息表");
        sysImportAndExportLog.setType((byte)1);
        sysImportAndExportLog.setOperatorUserId(user.getUserId());
        sysImportAndExportLog.setResult((byte)1);
        sysImportAndExportLog.setTotalCount(omPurchaseReturnOrderImports.size());
        sysImportAndExportLog.setFailCount(failCount);
        sysImportAndExportLog.setSucceedCount(succeedCount);
        sysImportAndExportLog.setFailInfo(failInfo.toString());
        sysImportAndExportLog.setSucceedInfo(succeedInfo.toString());
        securityFeignApi.add(sysImportAndExportLog);

        if(StringUtils.isNotEmpty(purchaseReturnnOrderImports)){
            //对合格数据进行分组
            HashMap<String, List<OmPurchaseReturnOrderImport>> map = purchaseReturnnOrderImports.stream().collect(Collectors.groupingBy(OmPurchaseReturnOrderImport::getGroupNum, HashMap::new, Collectors.toList()));
            Set<String> codeList = map.keySet();
            for (String code : codeList) {
                List<OmPurchaseReturnOrderImport> omPurchaseReturnOrderImports1 = map.get(code);
                OmPurchaseReturnOrder omPurchaseReturnOrder = new OmPurchaseReturnOrder();
                //新增父级数据
                BeanUtils.copyProperties(omPurchaseReturnOrderImports1.get(0), omPurchaseReturnOrder);
                omPurchaseReturnOrder.setPurchaseReturnOrderCode(CodeUtils.getId("OUT-PRO"));
                omPurchaseReturnOrder.setCreateTime(new Date());
                omPurchaseReturnOrder.setCreateUserId(user.getUserId());
                omPurchaseReturnOrder.setModifiedUserId(user.getUserId());
                omPurchaseReturnOrder.setModifiedTime(new Date());
                omPurchaseReturnOrder.setOrgId(user.getOrganizationId());
                omPurchaseReturnOrder.setStatus((byte)1);
                success += omPurchaseReturnOrderMapper.insertUseGeneratedKeys(omPurchaseReturnOrder);

                //履历
                OmHtPurchaseReturnOrder omHtPurchaseReturnOrder = new OmHtPurchaseReturnOrder();
                BeanUtils.copyProperties(omPurchaseReturnOrder, omHtPurchaseReturnOrder);
                htList.add(omHtPurchaseReturnOrder);

                //新增明细数据
                LinkedList<OmPurchaseReturnOrderDet> detList = new LinkedList<>();
                for (OmPurchaseReturnOrderImport omPurchaseReturnOrderImport : omPurchaseReturnOrderImports1) {
                    OmPurchaseReturnOrderDet omPurchaseReturnOrderDet = new OmPurchaseReturnOrderDet();
                    BeanUtils.copyProperties(omPurchaseReturnOrderImport, omPurchaseReturnOrderDet);
                    omPurchaseReturnOrderDet.setPurchaseReturnOrderId(omPurchaseReturnOrder.getPurchaseReturnOrderId());
                    omPurchaseReturnOrderDet.setStatus((byte) 1);
                    detList.add(omPurchaseReturnOrderDet);
                }
                omPurchaseReturnOrderDetMapper.insertList(detList);
            }
            omHtPurchaseReturnOrderMapper.insertList(htList);
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
