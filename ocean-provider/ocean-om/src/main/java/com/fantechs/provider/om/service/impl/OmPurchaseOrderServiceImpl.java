package com.fantechs.provider.om.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmPurchaseOrderDto;
import com.fantechs.common.base.general.dto.qms.QmsIncomingInspectionOrderDto;
import com.fantechs.common.base.general.dto.wms.in.*;
import com.fantechs.common.base.general.entity.basic.BaseOrderFlow;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrderFlow;
import com.fantechs.common.base.general.entity.om.OmPurchaseOrder;
import com.fantechs.common.base.general.entity.om.OmPurchaseOrderDet;
import com.fantechs.common.base.general.entity.wms.in.WmsInReceivingOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.qms.QmsFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.in.InFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.om.mapper.OmPurchaseOrderDetMapper;
import com.fantechs.provider.om.mapper.OmPurchaseOrderMapper;
import com.fantechs.provider.om.service.OmPurchaseOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/06/17.
 */
@Service
public class OmPurchaseOrderServiceImpl extends BaseService<OmPurchaseOrder> implements OmPurchaseOrderService {

    @Resource
    private OmPurchaseOrderMapper omPurchaseOrderMapper;
    @Resource
    private OmPurchaseOrderDetMapper omPurchaseOrderDetMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private InFeignApi inFeignApi;
    @Resource
    private InnerFeignApi innerFeignApi;
    @Resource
    private QmsFeignApi qmsFeignApi;


    @Override
    public List<OmPurchaseOrderDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(map.get("orgId"))) {
            if (StringUtils.isEmpty(user)) {
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }
            map.put("orgId", user.getOrganizationId());
        }
        if (StringUtils.isNotEmpty(map.get("ifSupplierFind")) && Byte.valueOf(map.get("ifSupplierFind").toString()) == 1) {
            map.put("supplierId",user.getSupplierId());
        }
        return omPurchaseOrderMapper.findList(map);
    }

    @Override
    public OmPurchaseOrder saveByApi(OmPurchaseOrder omPurchaseOrder) {

        Example example = new Example(OmPurchaseOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("purchaseOrderCode", omPurchaseOrder.getPurchaseOrderCode());
        criteria.andEqualTo("orgId", omPurchaseOrder.getOrgId());
        List<OmPurchaseOrder> omPurchaseOrders = omPurchaseOrderMapper.selectByExample(example);
        omPurchaseOrderMapper.deleteByExample(example);
        example.clear();

        //删除该采购订单下的所有详情表
        if(StringUtils.isNotEmpty(omPurchaseOrders)) {
            Example detExample = new Example(OmPurchaseOrderDet.class);
            Example.Criteria detCriteria = detExample.createCriteria();
            detCriteria.andEqualTo("purchaseOrderId", omPurchaseOrders.get(0).getPurchaseOrderId());
            omPurchaseOrderDetMapper.deleteByExample(detExample);
            detExample.clear();
        }

        omPurchaseOrder.setCreateTime(new Date());
        omPurchaseOrderMapper.insertUseGeneratedKeys(omPurchaseOrder);
        return omPurchaseOrder;
    }

    @Override
    public int save(OmPurchaseOrder omPurchaseOrder) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(OmPurchaseOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("purchaseOrderCode", omPurchaseOrder.getPurchaseOrderCode());
        criteria.andEqualTo("orgId", omPurchaseOrder.getOrgId());
        if (StringUtils.isNotEmpty(omPurchaseOrder.getPurchaseOrderId())) {
            criteria.andNotEqualTo("purchaseOrderId", omPurchaseOrder.getPurchaseOrderId());
        }
        List<OmPurchaseOrder> omPurchaseOrders = omPurchaseOrderMapper.selectByExample(example);
        if (!omPurchaseOrders.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"采购单号重复");
        }
        omPurchaseOrder.setOrderStatus((byte) 1);
        omPurchaseOrder.setStatus((byte) 1);
        omPurchaseOrder.setOrgId(user.getOrganizationId());
        omPurchaseOrder.setCreateUserId(user.getUserId());
        omPurchaseOrder.setCreateTime(new Date());
        omPurchaseOrder.setIsDelete((byte) 1);
        if (StringUtils.isNotEmpty(omPurchaseOrder.getPurchaseOrderId())) {
            omPurchaseOrderMapper.updateByPrimaryKeySelective(omPurchaseOrder);

            //删除该采购订单下的所有详情表
            Example detExample = new Example(OmPurchaseOrderDet.class);
            Example.Criteria detCriteria = detExample.createCriteria();
            detCriteria.andEqualTo("purchaseOrderId", omPurchaseOrder.getPurchaseOrderId());
            omPurchaseOrderDetMapper.deleteByExample(detExample);
        } else {
            omPurchaseOrderMapper.insertUseGeneratedKeys(omPurchaseOrder);
        }
        for (OmPurchaseOrderDet omPurchaseOrderDet : omPurchaseOrder.getOmPurchaseOrderDetList()) {
            omPurchaseOrderDet.setPurchaseOrderId(omPurchaseOrder.getPurchaseOrderId());
            omPurchaseOrderDet.setStatus((byte) 1);
            omPurchaseOrderDet.setOrgId(user.getOrganizationId());
            omPurchaseOrderDet.setCreateUserId(user.getUserId());
            omPurchaseOrderDet.setCreateTime(new Date());
            omPurchaseOrderDet.setIsDelete((byte) 1);
        }
        if (!omPurchaseOrder.getOmPurchaseOrderDetList().isEmpty()) {
            omPurchaseOrderDetMapper.insertList(omPurchaseOrder.getOmPurchaseOrderDetList());
        }

        return 1;
    }

    @Override
    public String findPurchaseMaterial(String purchaseOrderCode) {
        return omPurchaseOrderMapper.findPurchaseMaterial(purchaseOrderCode);
    }

    @Override
    public int update(OmPurchaseOrder entity) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        entity.setCreateUserId(user.getUserId());
        entity.setCreateTime(new Date());
        int i = omPurchaseOrderMapper.updateByPrimaryKeySelective(entity);

        //删除该采购订单下的所有详情表
        Example detExample = new Example(OmPurchaseOrderDet.class);
        Example.Criteria detCriteria = detExample.createCriteria();
        detCriteria.andEqualTo("purchaseOrderId", entity.getPurchaseOrderId());
        omPurchaseOrderDetMapper.deleteByExample(detExample);

        for (OmPurchaseOrderDet omPurchaseOrderDet : entity.getOmPurchaseOrderDetList()) {
            omPurchaseOrderDet.setPurchaseOrderId(entity.getPurchaseOrderId());
            omPurchaseOrderDet.setStatus((byte) 1);
            omPurchaseOrderDet.setOrgId(user.getOrganizationId());
            omPurchaseOrderDet.setCreateUserId(user.getUserId());
            omPurchaseOrderDet.setCreateTime(new Date());
            omPurchaseOrderDet.setIsDelete((byte) 1);
        }
        if (StringUtils.isNotEmpty(entity.getOmPurchaseOrderDetList())) {
            omPurchaseOrderDetMapper.insertList(entity.getOmPurchaseOrderDetList());
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int pushDown(List<OmPurchaseOrderDet> omPurchaseOrderDets) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        String coreSourceSysOrderTypeCode = null;
        int i = 0;
        List<OmPurchaseOrderDet> list = new ArrayList<>();
        //查当前单据的下游单据
        SearchBaseOrderFlow searchBaseOrderFlow = new SearchBaseOrderFlow();
        searchBaseOrderFlow.setBusinessType((byte)1);
        searchBaseOrderFlow.setOrderNode((byte)5);
        BaseOrderFlow baseOrderFlow = baseFeignApi.findOrderFlow(searchBaseOrderFlow).getData();
        if(StringUtils.isEmpty(baseOrderFlow)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "未找到当前单据配置的下游单据");
        }

        for(OmPurchaseOrderDet omPurchaseOrderDet : omPurchaseOrderDets){
            if(omPurchaseOrderDet.getOrderQty().compareTo(omPurchaseOrderDet.getTotalIssueQty().add(omPurchaseOrderDet.getQty())) == -1 )
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "累计下发数量大于采购数量");

        }
        HashSet<Long> set = new HashSet();

        if("IN-SPO".equals(baseOrderFlow.getNextOrderTypeCode())){
            //生成收货计划
            List<WmsInPlanReceivingOrderDetDto> detList = new LinkedList<>();

            for(OmPurchaseOrderDet omPurchaseOrderDet : omPurchaseOrderDets){
                int lineNumber = 1;

                Map map = new HashMap();
                map.put("purchaseOrderId",omPurchaseOrderDet.getPurchaseOrderId());
                List<OmPurchaseOrderDto> omPurchaseOrderDto = omPurchaseOrderMapper.findList(map);
                coreSourceSysOrderTypeCode = "IN-PO";

                WmsInPlanReceivingOrderDetDto wmsInPlanReceivingOrderDetDto = new WmsInPlanReceivingOrderDetDto();
                wmsInPlanReceivingOrderDetDto.setCoreSourceOrderCode(omPurchaseOrderDto.get(0).getPurchaseOrderCode());
                wmsInPlanReceivingOrderDetDto.setSourceOrderCode(omPurchaseOrderDto.get(0).getPurchaseOrderCode());
                wmsInPlanReceivingOrderDetDto.setLineNumber(lineNumber+"");
                wmsInPlanReceivingOrderDetDto.setSourceId(omPurchaseOrderDet.getPurchaseOrderDetId());
                wmsInPlanReceivingOrderDetDto.setMaterialId(omPurchaseOrderDet.getMaterialId());
                wmsInPlanReceivingOrderDetDto.setPlanQty(omPurchaseOrderDet.getOrderQty());
                wmsInPlanReceivingOrderDetDto.setLineStatus((byte)1);
                wmsInPlanReceivingOrderDetDto.setActualQty(omPurchaseOrderDet.getActualQty());
                wmsInPlanReceivingOrderDetDto.setOperatorUserId(user.getUserId());
                detList.add(wmsInPlanReceivingOrderDetDto);
                omPurchaseOrderDet.setTotalIssueQty(omPurchaseOrderDet.getTotalIssueQty().add(omPurchaseOrderDet.getQty()));
                if(omPurchaseOrderDet.getTotalIssueQty().compareTo(omPurchaseOrderDet.getOrderQty())== 0) {
                    omPurchaseOrderDet.setIfAllIssued((byte) 1);
                    omPurchaseOrderDet.setMaterialStatus((byte)3);
                }else {
                    omPurchaseOrderDet.setIfAllIssued((byte) 0);
                    omPurchaseOrderDet.setMaterialStatus((byte)2);
                }
                list.add(omPurchaseOrderDet);
                set.add(omPurchaseOrderDto.get(0).getWarehouseId());
            }

            if (set.size()>1)
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "请选择相同仓库的进行下发操作");

            WmsInPlanReceivingOrderDto wmsInPlanReceivingOrderDto = new WmsInPlanReceivingOrderDto();
            wmsInPlanReceivingOrderDto.setSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
            wmsInPlanReceivingOrderDto.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
            wmsInPlanReceivingOrderDto.setOrderStatus((byte)1);
            wmsInPlanReceivingOrderDto.setCreateUserId(user.getUserId());
            wmsInPlanReceivingOrderDto.setCreateTime(new Date());
            wmsInPlanReceivingOrderDto.setModifiedUserId(user.getUserId());
            wmsInPlanReceivingOrderDto.setModifiedTime(new Date());
            wmsInPlanReceivingOrderDto.setStatus((byte)1);
            wmsInPlanReceivingOrderDto.setOrgId(user.getOrganizationId());
            Iterator<Long> iterator=set.iterator();
            while(iterator.hasNext()){
                wmsInPlanReceivingOrderDto.setWarehouseId(iterator.next());
            }
            wmsInPlanReceivingOrderDto.setInPlanReceivingOrderDets(detList);

            ResponseEntity responseEntity = inFeignApi.add(wmsInPlanReceivingOrderDto);
            if(responseEntity.getCode() != 0){
                throw new BizErrorException("下推生成收货计划单失败");
            }else {
                i++;
            }
        }else if ("IN-SWK".equals(baseOrderFlow.getNextOrderTypeCode())){
            //生成收货作业

            List<WmsInReceivingOrderDetDto> detList = new LinkedList<>();

            for(OmPurchaseOrderDet omPurchaseOrderDet : omPurchaseOrderDets){
                int lineNumber = 1;
                Map map = new HashMap();
                map.put("purchaseOrderId",omPurchaseOrderDet.getPurchaseOrderId());
                List<OmPurchaseOrderDto> omPurchaseOrderDto = omPurchaseOrderMapper.findList(map);
                coreSourceSysOrderTypeCode = "IN-PO";

                WmsInReceivingOrderDetDto wmsInReceivingOrderDetDto = new WmsInReceivingOrderDetDto();
                wmsInReceivingOrderDetDto.setCoreSourceOrderCode(omPurchaseOrderDto.get(0).getPurchaseOrderCode());
                wmsInReceivingOrderDetDto.setSourceOrderCode(omPurchaseOrderDto.get(0).getPurchaseOrderCode());
                wmsInReceivingOrderDetDto.setLineNumber(lineNumber+"");
                wmsInReceivingOrderDetDto.setSourceId(omPurchaseOrderDet.getPurchaseOrderDetId());
                wmsInReceivingOrderDetDto.setMaterialId(omPurchaseOrderDet.getMaterialId());
                wmsInReceivingOrderDetDto.setPlanQty(omPurchaseOrderDet.getOrderQty());
                wmsInReceivingOrderDetDto.setLineStatus((byte)1);
                wmsInReceivingOrderDetDto.setActualQty(omPurchaseOrderDet.getActualQty());
                wmsInReceivingOrderDetDto.setOperatorUserId(user.getUserId());
                detList.add(wmsInReceivingOrderDetDto);
                omPurchaseOrderDet.setTotalIssueQty(omPurchaseOrderDet.getTotalIssueQty().add(omPurchaseOrderDet.getQty()));
                if(omPurchaseOrderDet.getTotalIssueQty().compareTo(omPurchaseOrderDet.getOrderQty())== 0) {
                    omPurchaseOrderDet.setIfAllIssued((byte) 1);
                    omPurchaseOrderDet.setMaterialStatus((byte)3);
                }else {
                    omPurchaseOrderDet.setIfAllIssued((byte) 0);
                    omPurchaseOrderDet.setMaterialStatus((byte)2);
                }
                list.add(omPurchaseOrderDet);
                set.add(omPurchaseOrderDto.get(0).getWarehouseId());
            }

            if (set.size()>1)
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "请选择相同仓库的进行下发操作");

            WmsInReceivingOrder wmsInReceivingOrder = new WmsInReceivingOrder();
            wmsInReceivingOrder.setSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
            wmsInReceivingOrder.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
            wmsInReceivingOrder.setOrderStatus((byte)1);
            wmsInReceivingOrder.setCreateUserId(user.getUserId());
            wmsInReceivingOrder.setCreateTime(new Date());
            wmsInReceivingOrder.setModifiedUserId(user.getUserId());
            wmsInReceivingOrder.setModifiedTime(new Date());
            wmsInReceivingOrder.setStatus((byte)1);
            wmsInReceivingOrder.setOrgId(user.getOrganizationId());
            Iterator<Long> iterator=set.iterator();
            while(iterator.hasNext()){
                wmsInReceivingOrder.setWarehouseId(iterator.next());
            }
            wmsInReceivingOrder.setWmsInReceivingOrderDets(detList);

            ResponseEntity responseEntity = inFeignApi.add(wmsInReceivingOrder);
            if(responseEntity.getCode() != 0){
                throw new BizErrorException("下推生成收货作业单失败");
            }else {
                i++;
            }
        }else if ("QMS-MIIO".equals(baseOrderFlow.getNextOrderTypeCode())){
            //生成来料检验单

            List<QmsIncomingInspectionOrderDto> detList = new LinkedList<>();
            for(OmPurchaseOrderDet omPurchaseOrderDet : omPurchaseOrderDets){
                int lineNumber = 1;

                Map map = new HashMap();
                map.put("purchaseOrderId",omPurchaseOrderDet.getPurchaseOrderId());
                List<OmPurchaseOrderDto> omPurchaseOrderDto = omPurchaseOrderMapper.findList(map);
               // coreSourceSysOrderTypeCode =  omPurchaseOrderDto.get(0).getcode();
                coreSourceSysOrderTypeCode = "IN-PO";

                QmsIncomingInspectionOrderDto qmsIncomingInspectionOrderDto = new QmsIncomingInspectionOrderDto();
                qmsIncomingInspectionOrderDto.setCoreSourceOrderCode(omPurchaseOrderDto.get(0).getPurchaseOrderCode());
                qmsIncomingInspectionOrderDto.setSourceOrderCode(omPurchaseOrderDto.get(0).getPurchaseOrderCode());
                qmsIncomingInspectionOrderDto.setSourceId(omPurchaseOrderDet.getPurchaseOrderDetId());
                qmsIncomingInspectionOrderDto.setMaterialId(omPurchaseOrderDet.getMaterialId());
                qmsIncomingInspectionOrderDto.setWarehouseId(omPurchaseOrderDto.get(0).getWarehouseId());
                qmsIncomingInspectionOrderDto.setOrderQty(omPurchaseOrderDet.getOrderQty());
                qmsIncomingInspectionOrderDto.setInspectionStatus((byte)1);
                qmsIncomingInspectionOrderDto.setSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                qmsIncomingInspectionOrderDto.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                qmsIncomingInspectionOrderDto.setCreateUserId(user.getUserId());
                qmsIncomingInspectionOrderDto.setCreateTime(new Date());
                qmsIncomingInspectionOrderDto.setModifiedUserId(user.getUserId());
                qmsIncomingInspectionOrderDto.setModifiedTime(new Date());
                qmsIncomingInspectionOrderDto.setStatus((byte)1);
                qmsIncomingInspectionOrderDto.setOrgId(user.getOrganizationId());
                detList.add(qmsIncomingInspectionOrderDto);
                omPurchaseOrderDet.setTotalIssueQty(omPurchaseOrderDet.getTotalIssueQty().add(omPurchaseOrderDet.getQty()));
                if(omPurchaseOrderDet.getTotalIssueQty().compareTo(omPurchaseOrderDet.getOrderQty())== 0) {
                    omPurchaseOrderDet.setIfAllIssued((byte) 1);
                    omPurchaseOrderDet.setMaterialStatus((byte)3);
                }else {
                    omPurchaseOrderDet.setIfAllIssued((byte) 0);
                    omPurchaseOrderDet.setMaterialStatus((byte)2);
                }
                list.add(omPurchaseOrderDet);
                set.add(omPurchaseOrderDto.get(0).getWarehouseId());
            }
            if (set.size()>1)
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "请选择相同仓库的进行下发操作");

            ResponseEntity responseEntity = qmsFeignApi.batchAdd(detList);
            if(responseEntity.getCode() != 0){
                throw new BizErrorException("下推生成来料检验单失败");
            }else {
                i++;
            }

        }else if("IN-IPO".equals(baseOrderFlow.getNextOrderTypeCode())){
            //生成入库计划单
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("InPlanOrderIsWork");
            List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            if(StringUtils.isEmpty(specItems))
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"需先配置作业顺序先后");
            if("0".equals(StringUtils.isEmpty(specItems.get(0).getParaValue())))
                throw new BizErrorException(ErrorCodeEnum.OPT20012002.getCode(),"先作业后单据无法进行下推操作");

            List<WmsInInPlanOrderDetDto> detList = new LinkedList<>();

            for(OmPurchaseOrderDet omPurchaseOrderDet : omPurchaseOrderDets){
                int lineNumber = 1;

                Map map = new HashMap();
                map.put("purchaseOrderId",omPurchaseOrderDet.getPurchaseOrderId());
                List<OmPurchaseOrderDto> omPurchaseOrderDto = omPurchaseOrderMapper.findList(map);
                // coreSourceSysOrderTypeCode =  omPurchaseOrderDto.get(0).getcode();
                coreSourceSysOrderTypeCode = "IN-PO";

                WmsInInPlanOrderDetDto wmsInInPlanOrderDet = new WmsInInPlanOrderDetDto();
                wmsInInPlanOrderDet.setCoreSourceOrderCode(omPurchaseOrderDto.get(0).getPurchaseOrderCode());
                wmsInInPlanOrderDet.setSourceOrderCode(omPurchaseOrderDto.get(0).getPurchaseOrderCode());
                wmsInInPlanOrderDet.setLineNumber(lineNumber+"");
                wmsInInPlanOrderDet.setSourceId(omPurchaseOrderDet.getPurchaseOrderDetId());
                wmsInInPlanOrderDet.setMaterialId(omPurchaseOrderDet.getMaterialId());
                wmsInInPlanOrderDet.setPlanQty(omPurchaseOrderDet.getOrderQty());
                wmsInInPlanOrderDet.setLineStatus((byte)1);
                detList.add(wmsInInPlanOrderDet);
                omPurchaseOrderDet.setTotalIssueQty(omPurchaseOrderDet.getTotalIssueQty().add(omPurchaseOrderDet.getQty()));
                if(omPurchaseOrderDet.getTotalIssueQty().compareTo(omPurchaseOrderDet.getOrderQty())== 0) {
                    omPurchaseOrderDet.setIfAllIssued((byte) 1);
                    omPurchaseOrderDet.setMaterialStatus((byte)3);
                }else {
                    omPurchaseOrderDet.setIfAllIssued((byte) 0);
                    omPurchaseOrderDet.setMaterialStatus((byte)2);
                }
                list.add(omPurchaseOrderDet);
                set.add(omPurchaseOrderDto.get(0).getWarehouseId());
            }
            if (set.size()>1)
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "请选择相同仓库的进行下发操作");

            WmsInInPlanOrderDto wmsInInPlanOrder = new WmsInInPlanOrderDto();
            wmsInInPlanOrder.setMakeOrderUserId(user.getUserId());
            wmsInInPlanOrder.setSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
            wmsInInPlanOrder.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
            wmsInInPlanOrder.setOrderStatus((byte)1);
            Iterator<Long> iterator=set.iterator();
            while(iterator.hasNext()){
                wmsInInPlanOrder.setWarehouseId(iterator.next());
            }
            wmsInInPlanOrder.setCreateUserId(user.getUserId());
            wmsInInPlanOrder.setCreateTime(new Date());
            wmsInInPlanOrder.setModifiedUserId(user.getUserId());
            wmsInInPlanOrder.setModifiedTime(new Date());
            wmsInInPlanOrder.setStatus((byte)1);
            wmsInInPlanOrder.setOrgId(user.getOrganizationId());
            wmsInInPlanOrder.setWmsInInPlanOrderDetDtos(detList);

            ResponseEntity responseEntity = inFeignApi.add(wmsInInPlanOrder);
            if(responseEntity.getCode() != 0){
                throw new BizErrorException("下推生成入库计划单失败");
            }else {
                i++;
            }
        }else if("IN-IWK".equals(baseOrderFlow.getNextOrderTypeCode())){
            //生成上架作业单

            List<WmsInnerJobOrderDet> detList = new LinkedList<>();
            for(OmPurchaseOrderDet omPurchaseOrderDet : omPurchaseOrderDets){
                int lineNumber = 1;

                Map map = new HashMap();
                map.put("purchaseOrderId",omPurchaseOrderDet.getPurchaseOrderId());
                List<OmPurchaseOrderDto> omPurchaseOrderDto = omPurchaseOrderMapper.findList(map);
                // coreSourceSysOrderTypeCode =  omPurchaseOrderDto.get(0).getcode();
                coreSourceSysOrderTypeCode = "IN-PO";

                WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
                wmsInnerJobOrderDet.setCoreSourceOrderCode(omPurchaseOrderDto.get(0).getPurchaseOrderCode());
                wmsInnerJobOrderDet.setSourceOrderCode(omPurchaseOrderDto.get(0).getPurchaseOrderCode());
                wmsInnerJobOrderDet.setSourceId(omPurchaseOrderDet.getPurchaseOrderDetId());
                wmsInnerJobOrderDet.setLineNumber(lineNumber+"");
                wmsInnerJobOrderDet.setMaterialId(omPurchaseOrderDet.getMaterialId());
                wmsInnerJobOrderDet.setPlanQty(omPurchaseOrderDet.getQty());
                wmsInnerJobOrderDet.setLineStatus((byte)1);
                detList.add(wmsInnerJobOrderDet);
                omPurchaseOrderDet.setTotalIssueQty(omPurchaseOrderDet.getTotalIssueQty().add(omPurchaseOrderDet.getQty()));
                if(omPurchaseOrderDet.getTotalIssueQty().compareTo(omPurchaseOrderDet.getOrderQty())== 0) {
                    omPurchaseOrderDet.setIfAllIssued((byte) 1);
                    omPurchaseOrderDet.setMaterialStatus((byte)3);
                }else {
                    omPurchaseOrderDet.setIfAllIssued((byte) 0);
                    omPurchaseOrderDet.setMaterialStatus((byte)2);
                }
                set.add(omPurchaseOrderDto.get(0).getWarehouseId());
            }
            if (set.size()>1)
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "请选择相同仓库的进行下发操作");

            WmsInnerJobOrder wmsInnerJobOrder = new WmsInnerJobOrder();
            wmsInnerJobOrder.setSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
            wmsInnerJobOrder.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
            wmsInnerJobOrder.setJobOrderType((byte)1);
            wmsInnerJobOrder.setOrderStatus((byte)1);
            Iterator<Long> iterator=set.iterator();
            while(iterator.hasNext()){
                wmsInnerJobOrder.setWarehouseId(iterator.next());
            }
            wmsInnerJobOrder.setCreateUserId(user.getUserId());
            wmsInnerJobOrder.setCreateTime(new Date());
            wmsInnerJobOrder.setModifiedUserId(user.getUserId());
            wmsInnerJobOrder.setModifiedTime(new Date());
            wmsInnerJobOrder.setStatus((byte)1);
            wmsInnerJobOrder.setOrgId(user.getOrganizationId());
            wmsInnerJobOrder.setWmsInPutawayOrderDets(detList);

            ResponseEntity responseEntity = innerFeignApi.add(wmsInnerJobOrder);
            if(responseEntity.getCode() != 0){
                throw new BizErrorException("下推生成上架作业单失败");
            }else {
                i++;
            }
        }else {
            throw new BizErrorException("单据流配置错误");
        }

        //返写下推数据
        if(StringUtils.isNotEmpty(list)) {
            for (OmPurchaseOrderDet omPurchaseOrderDet : list) {
                omPurchaseOrderDetMapper.updateByPrimaryKeySelective(omPurchaseOrderDet);
            }
        }

        return i;
    }

}
