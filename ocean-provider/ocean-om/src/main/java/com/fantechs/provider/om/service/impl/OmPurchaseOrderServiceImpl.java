package com.fantechs.provider.om.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrderFlowDto;
import com.fantechs.common.base.general.dto.om.OmPurchaseOrderDto;
import com.fantechs.common.base.general.dto.qms.QmsIncomingInspectionOrderDto;
import com.fantechs.common.base.general.dto.wms.in.*;
import com.fantechs.common.base.general.entity.basic.BaseOrderFlow;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrderFlow;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.general.entity.om.*;
import com.fantechs.common.base.general.entity.wms.in.WmsInReceivingOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.qms.QmsFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.in.InFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.om.mapper.OmPurchaseOrderDetMapper;
import com.fantechs.provider.om.mapper.OmPurchaseOrderMapper;
import com.fantechs.provider.om.mapper.OmPurchaseReturnOrderDetMapper;
import com.fantechs.provider.om.mapper.ht.OmHtPurchaseOrderDetMapper;
import com.fantechs.provider.om.mapper.ht.OmHtPurchaseOrderMapper;
import com.fantechs.provider.om.service.OmPurchaseOrderService;
import com.fantechs.provider.om.util.OrderFlowUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
    private OmHtPurchaseOrderMapper omHtPurchaseOrderMapper;
    @Resource
    private OmPurchaseOrderDetMapper omPurchaseOrderDetMapper;
    @Resource
    private OmHtPurchaseOrderDetMapper omHtPurchaseOrderDetMapper;
    @Resource
    private OmPurchaseReturnOrderDetMapper omPurchaseReturnOrderDetMapper;
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
        int i=0;
        List<OmPurchaseOrderDet> detList = new ArrayList<>();
        List<OmHtPurchaseOrderDet> htdetList = new ArrayList<>();
        omPurchaseOrder.setPurchaseOrderCode(CodeUtils.getId("IN-PO"));
        omPurchaseOrder.setOrderStatus((byte) 1);
        omPurchaseOrder.setStatus((byte) 1);
        omPurchaseOrder.setOrgId(user.getOrganizationId());
        omPurchaseOrder.setCreateUserId(user.getUserId());
        omPurchaseOrder.setCreateTime(new Date());
        omPurchaseOrder.setModifiedUserId(user.getUserId());
        omPurchaseOrder.setModifiedTime(new Date());
        omPurchaseOrder.setIsDelete((byte) 1);
        omPurchaseOrderMapper.insertUseGeneratedKeys(omPurchaseOrder);

        //保存履历表
        OmHtPurchaseOrder omHtPurchaseOrder = new OmHtPurchaseOrder();
        BeanUtils.copyProperties(omPurchaseOrder, omHtPurchaseOrder);
        omHtPurchaseOrderMapper.insertSelective(omHtPurchaseOrder);

        for (OmPurchaseOrderDet omPurchaseOrderDet : omPurchaseOrder.getOmPurchaseOrderDetList()) {
            omPurchaseOrderDet.setPurchaseOrderId(omPurchaseOrder.getPurchaseOrderId());
            omPurchaseOrderDet.setTotalIssueQty(BigDecimal.ZERO);
            omPurchaseOrderDet.setStatus((byte) 1);
            omPurchaseOrderDet.setOrgId(user.getOrganizationId());
            omPurchaseOrderDet.setCreateUserId(user.getUserId());
            omPurchaseOrderDet.setCreateTime(new Date());
            omPurchaseOrderDet.setModifiedUserId(user.getUserId());
            omPurchaseOrderDet.setModifiedTime(new Date());
            omPurchaseOrderDet.setIsDelete((byte) 1);
            detList.add(omPurchaseOrderDet);
            //保存履历表
            OmHtPurchaseOrderDet omHtPurchaseOrderDet = new OmHtPurchaseOrderDet();
            BeanUtils.copyProperties(omPurchaseOrderDet, omHtPurchaseOrderDet);
            htdetList.add(omHtPurchaseOrderDet);
        }
        if (StringUtils.isNotEmpty(detList)) {
            i = omPurchaseOrderDetMapper.insertList(detList);
        }
        if (StringUtils.isNotEmpty(htdetList)) {
            omHtPurchaseOrderDetMapper.insertList(htdetList);
        }
        return i;
    }

    @Override
    public String findPurchaseMaterial(String purchaseOrderCode) {
        return omPurchaseOrderMapper.findPurchaseMaterial(purchaseOrderCode);
    }

    @Override
    public int update(OmPurchaseOrder entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        int i = omPurchaseOrderMapper.updateByPrimaryKeySelective(entity);

        //保存履历表
        OmHtPurchaseOrder omHtPurchaseOrder = new OmHtPurchaseOrder();
        BeanUtils.copyProperties(entity, omHtPurchaseOrder);
        omHtPurchaseOrderMapper.insertSelective(omHtPurchaseOrder);

        //保存详情表
        //更新原有明细
        ArrayList<Long> idList = new ArrayList<>();
        List<OmPurchaseOrderDet> list = entity.getOmPurchaseOrderDetList();
        if(StringUtils.isNotEmpty(list)) {
            for (OmPurchaseOrderDet det : list) {
                if(StringUtils.isEmpty(det.getOrderQty()) || det.getOrderQty().compareTo(BigDecimal.ZERO) == -1)
                    throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"计划数量需大于0");

                if (StringUtils.isNotEmpty(det.getPurchaseOrderId())) {
                    omPurchaseOrderDetMapper.updateByPrimaryKey(det);
                    idList.add(det.getPurchaseOrderId());
                }
            }
        }

        //删除更新之外的明细
        Example example1 = new Example(OmPurchaseOrderDet.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("purchaseOrderId", entity.getPurchaseOrderId());
        if (idList.size() > 0) {
            criteria1.andNotIn("purchaseOrderDetId", idList);
        }
        omPurchaseOrderDetMapper.deleteByExample(example1);

        //新增剩余的明细
        if(StringUtils.isNotEmpty(list)){
            List<OmPurchaseOrderDet> addlist = new ArrayList<>();
            for (OmPurchaseOrderDet det  : list){
                if (idList.contains(det.getPurchaseOrderDetId())) {
                    continue;
                }
                det.setPurchaseOrderId(entity.getPurchaseOrderId());
                det.setCreateUserId(user.getUserId());
                det.setCreateTime(new Date());
                det.setModifiedUserId(user.getUserId());
                det.setModifiedTime(new Date());
                det.setStatus(StringUtils.isEmpty(det.getStatus())?1: det.getStatus());
                det.setOrgId(user.getOrganizationId());
                addlist.add(det);
            }
            if (StringUtils.isNotEmpty(addlist))
                omPurchaseOrderDetMapper.insertList(addlist);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int pushDown(List<OmPurchaseOrderDet> omPurchaseOrderDets) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        String coreSourceSysOrderTypeCode = null;
        List<OmPurchaseOrderDet> list = new ArrayList<>();
        List<OmPurchaseOrder> orderList = new ArrayList<>();

        Example example = new Example(OmPurchaseReturnOrderDet.class);
        int i = 0;
        for (OmPurchaseOrderDet orderDet : omPurchaseOrderDets) {
            if (orderDet.getIfAllIssued() != null && orderDet.getIfAllIssued() == (byte) 1) {
                throw new BizErrorException("订单已下推，无法再次下推");
            }

            //查采购订单明细的退货数量
            BigDecimal purchaseReturnQty = BigDecimal.ZERO;
            example.clear();
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("purchaseOrderDetId",orderDet.getPurchaseOrderDetId());
            List<OmPurchaseReturnOrderDet> omPurchaseReturnOrderDets = omPurchaseReturnOrderDetMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(omPurchaseReturnOrderDets)){
                for (OmPurchaseReturnOrderDet omPurchaseReturnOrderDet : omPurchaseReturnOrderDets){
                    purchaseReturnQty = purchaseReturnQty.add(omPurchaseReturnOrderDet.getOrderQty());
                }
            }
            orderDet.setPurchaseReturnQty(purchaseReturnQty);
            orderDet.setTotalIssueQty(StringUtils.isEmpty(orderDet.getTotalIssueQty()) ? BigDecimal.ZERO : orderDet.getTotalIssueQty());
            if (orderDet.getOrderQty().compareTo(orderDet.getTotalIssueQty().add(orderDet.getQty()).add(orderDet.getPurchaseReturnQty())) == -1) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "累计下发数量与采购退货数量之和大于包装总数");
            }
        }
        
        //查当前单据的下游单据
        SearchBaseOrderFlow searchBaseOrderFlow = new SearchBaseOrderFlow();
        searchBaseOrderFlow.setOrderTypeCode("IN-PO");
        searchBaseOrderFlow.setStatus((byte)1);
        List<BaseOrderFlowDto> baseOrderFlowDtos = baseFeignApi.findAll(searchBaseOrderFlow).getData();
        if (StringUtils.isEmpty(baseOrderFlowDtos)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "未找到当前单据配置的下游单据");
        }

        for(OmPurchaseOrderDet omPurchaseOrderDet : omPurchaseOrderDets){
            if(omPurchaseOrderDet.getOrderQty().compareTo(omPurchaseOrderDet.getTotalIssueQty().add(omPurchaseOrderDet.getQty())) == -1 )
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "累计下发数量大于采购数量");

        }


        //根据仓库分组，不同仓库生成多张单
        Map<String, List<OmPurchaseOrderDet>> detMap = new HashMap<>();
        //不同单据流分组
        for (OmPurchaseOrderDet omPurchaseOrderDet : omPurchaseOrderDets) {
            //当前单据的下游单据
            BaseOrderFlow baseOrderFlow = OrderFlowUtil.getOrderFlow(baseOrderFlowDtos, omPurchaseOrderDet.getMaterialId(), omPurchaseOrderDet.getSupplierId());
            String key = baseOrderFlow.getNextOrderTypeCode();
            if (detMap.get(key) == null) {
                List<OmPurchaseOrderDet> diffOrderFlows = new LinkedList<>();
                diffOrderFlows.add(omPurchaseOrderDet);
                detMap.put(key, diffOrderFlows);
            } else {
                List<OmPurchaseOrderDet> diffOrderFlows = detMap.get(key);
                diffOrderFlows.add(omPurchaseOrderDet);
                detMap.put(key, diffOrderFlows);
            }
        }

        HashSet<Long> set = new HashSet();
        Set<String> codes = detMap.keySet();
        for (String code : codes) {
            String[] split = code.split("_");
            String nextOrderTypeCode = split[0];//下游单据类型

            if ("IN-SPO".equals(nextOrderTypeCode)) {
                //生成收货计划
                List<WmsInPlanReceivingOrderDetDto> detList = new LinkedList<>();

                for (OmPurchaseOrderDet omPurchaseOrderDet : detMap.get(nextOrderTypeCode)) {
                    int lineNumber = 1;

                    Map map = new HashMap();
                    map.put("purchaseOrderId", omPurchaseOrderDet.getPurchaseOrderId());
                    List<OmPurchaseOrderDto> omPurchaseOrderDto = omPurchaseOrderMapper.findList(map);
                    OmPurchaseOrderDto order = omPurchaseOrderDto.get(0);
                    coreSourceSysOrderTypeCode = order.getSysOrderTypeCode();

                    WmsInPlanReceivingOrderDetDto wmsInPlanReceivingOrderDetDto = new WmsInPlanReceivingOrderDetDto();
                    wmsInPlanReceivingOrderDetDto.setCoreSourceOrderCode(order.getPurchaseOrderCode());
                    wmsInPlanReceivingOrderDetDto.setSourceOrderCode(order.getPurchaseOrderCode());
                    wmsInPlanReceivingOrderDetDto.setLineNumber(lineNumber + "");
                    wmsInPlanReceivingOrderDetDto.setSourceId(omPurchaseOrderDet.getPurchaseOrderDetId());
                    wmsInPlanReceivingOrderDetDto.setMaterialId(omPurchaseOrderDet.getMaterialId());
                    wmsInPlanReceivingOrderDetDto.setPlanQty(omPurchaseOrderDet.getQty());
                    wmsInPlanReceivingOrderDetDto.setLineStatus((byte) 1);
                    wmsInPlanReceivingOrderDetDto.setActualQty(omPurchaseOrderDet.getActualQty());
                    wmsInPlanReceivingOrderDetDto.setOperatorUserId(user.getUserId());
                    detList.add(wmsInPlanReceivingOrderDetDto);
                    omPurchaseOrderDet.setTotalIssueQty(omPurchaseOrderDet.getTotalIssueQty().add(omPurchaseOrderDet.getQty()));
                    if (omPurchaseOrderDet.getTotalIssueQty().compareTo(omPurchaseOrderDet.getOrderQty()) == 0) {
                        omPurchaseOrderDet.setIfAllIssued((byte) 1);
                        omPurchaseOrderDet.setMaterialStatus((byte) 3);
                    } else {
                        omPurchaseOrderDet.setIfAllIssued((byte) 0);
                        omPurchaseOrderDet.setMaterialStatus((byte) 2);
                    }
                    list.add(omPurchaseOrderDet);
                    set.add(order.getWarehouseId());
                }

                if (set.size() > 1)
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "请选择相同仓库的进行下发操作");

                WmsInPlanReceivingOrderDto wmsInPlanReceivingOrderDto = new WmsInPlanReceivingOrderDto();
                wmsInPlanReceivingOrderDto.setSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                wmsInPlanReceivingOrderDto.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                wmsInPlanReceivingOrderDto.setOrderStatus((byte) 1);
                wmsInPlanReceivingOrderDto.setCreateUserId(user.getUserId());
                wmsInPlanReceivingOrderDto.setCreateTime(new Date());
                wmsInPlanReceivingOrderDto.setModifiedUserId(user.getUserId());
                wmsInPlanReceivingOrderDto.setModifiedTime(new Date());
                wmsInPlanReceivingOrderDto.setStatus((byte) 1);
                wmsInPlanReceivingOrderDto.setOrgId(user.getOrganizationId());
                Iterator<Long> iterator = set.iterator();
                while (iterator.hasNext()) {
                    wmsInPlanReceivingOrderDto.setWarehouseId(iterator.next());
                }
                wmsInPlanReceivingOrderDto.setInPlanReceivingOrderDets(detList);

                ResponseEntity responseEntity = inFeignApi.add(wmsInPlanReceivingOrderDto);
                if (responseEntity.getCode() != 0) {
                    throw new BizErrorException("下推生成收货计划单失败");
                } else {
                    i++;
                }
            } else if ("IN-SWK".equals(nextOrderTypeCode)) {
                //生成收货作业

                List<WmsInReceivingOrderDetDto> detList = new LinkedList<>();

                for (OmPurchaseOrderDet omPurchaseOrderDet : detMap.get(nextOrderTypeCode)) {
                    int lineNumber = 1;
                    Map map = new HashMap();
                    map.put("purchaseOrderId", omPurchaseOrderDet.getPurchaseOrderId());
                    List<OmPurchaseOrderDto> omPurchaseOrderDto = omPurchaseOrderMapper.findList(map);
                    OmPurchaseOrderDto order = omPurchaseOrderDto.get(0);
                    coreSourceSysOrderTypeCode = order.getSysOrderTypeCode();

                    WmsInReceivingOrderDetDto wmsInReceivingOrderDetDto = new WmsInReceivingOrderDetDto();
                    wmsInReceivingOrderDetDto.setCoreSourceOrderCode(order.getPurchaseOrderCode());
                    wmsInReceivingOrderDetDto.setSourceOrderCode(order.getPurchaseOrderCode());
                    wmsInReceivingOrderDetDto.setLineNumber(lineNumber + "");
                    wmsInReceivingOrderDetDto.setSourceId(omPurchaseOrderDet.getPurchaseOrderDetId());
                    wmsInReceivingOrderDetDto.setMaterialId(omPurchaseOrderDet.getMaterialId());
                    wmsInReceivingOrderDetDto.setPlanQty(omPurchaseOrderDet.getQty());
                    wmsInReceivingOrderDetDto.setLineStatus((byte) 1);
                    wmsInReceivingOrderDetDto.setActualQty(omPurchaseOrderDet.getActualQty());
                    wmsInReceivingOrderDetDto.setOperatorUserId(user.getUserId());
                    detList.add(wmsInReceivingOrderDetDto);
                    omPurchaseOrderDet.setTotalIssueQty(omPurchaseOrderDet.getTotalIssueQty().add(omPurchaseOrderDet.getQty()));
                    if (omPurchaseOrderDet.getTotalIssueQty().compareTo(omPurchaseOrderDet.getOrderQty()) == 0) {
                        omPurchaseOrderDet.setIfAllIssued((byte) 1);
                        omPurchaseOrderDet.setMaterialStatus((byte) 3);
                    } else {
                        omPurchaseOrderDet.setIfAllIssued((byte) 0);
                        omPurchaseOrderDet.setMaterialStatus((byte) 2);
                    }
                    list.add(omPurchaseOrderDet);
                    set.add(order.getWarehouseId());
                }

                if (set.size() > 1)
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "请选择相同仓库的进行下发操作");

                WmsInReceivingOrder wmsInReceivingOrder = new WmsInReceivingOrder();
                wmsInReceivingOrder.setSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                wmsInReceivingOrder.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                wmsInReceivingOrder.setOrderStatus((byte) 1);
                wmsInReceivingOrder.setCreateUserId(user.getUserId());
                wmsInReceivingOrder.setCreateTime(new Date());
                wmsInReceivingOrder.setModifiedUserId(user.getUserId());
                wmsInReceivingOrder.setModifiedTime(new Date());
                wmsInReceivingOrder.setStatus((byte) 1);
                wmsInReceivingOrder.setOrgId(user.getOrganizationId());
                Iterator<Long> iterator = set.iterator();
                while (iterator.hasNext()) {
                    wmsInReceivingOrder.setWarehouseId(iterator.next());
                }
                wmsInReceivingOrder.setWmsInReceivingOrderDets(detList);

                ResponseEntity responseEntity = inFeignApi.add(wmsInReceivingOrder);
                if (responseEntity.getCode() != 0) {
                    throw new BizErrorException("下推生成收货作业单失败");
                } else {
                    i++;
                }
            } else if ("QMS-MIIO".equals(nextOrderTypeCode)) {
                //生成来料检验单

                List<QmsIncomingInspectionOrderDto> detList = new LinkedList<>();
                for (OmPurchaseOrderDet omPurchaseOrderDet : detMap.get(nextOrderTypeCode)) {
                    int lineNumber = 1;
                    Map map = new HashMap();
                    map.put("purchaseOrderId", omPurchaseOrderDet.getPurchaseOrderId());
                    List<OmPurchaseOrderDto> omPurchaseOrderDto = omPurchaseOrderMapper.findList(map);
                    OmPurchaseOrderDto order = omPurchaseOrderDto.get(0);
                    coreSourceSysOrderTypeCode = order.getSysOrderTypeCode();

                    QmsIncomingInspectionOrderDto qmsIncomingInspectionOrderDto = new QmsIncomingInspectionOrderDto();
                    qmsIncomingInspectionOrderDto.setCoreSourceOrderCode(order.getPurchaseOrderCode());
                    qmsIncomingInspectionOrderDto.setSourceOrderCode(order.getPurchaseOrderCode());
                    qmsIncomingInspectionOrderDto.setSourceId(omPurchaseOrderDet.getPurchaseOrderDetId());
                    qmsIncomingInspectionOrderDto.setMaterialId(omPurchaseOrderDet.getMaterialId());
                    qmsIncomingInspectionOrderDto.setWarehouseId(order.getWarehouseId());
                    qmsIncomingInspectionOrderDto.setOrderQty(omPurchaseOrderDet.getQty());
                    qmsIncomingInspectionOrderDto.setInspectionStatus((byte) 1);
                    qmsIncomingInspectionOrderDto.setSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                    qmsIncomingInspectionOrderDto.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                    qmsIncomingInspectionOrderDto.setCreateUserId(user.getUserId());
                    qmsIncomingInspectionOrderDto.setCreateTime(new Date());
                    qmsIncomingInspectionOrderDto.setModifiedUserId(user.getUserId());
                    qmsIncomingInspectionOrderDto.setModifiedTime(new Date());
                    qmsIncomingInspectionOrderDto.setStatus((byte) 1);
                    qmsIncomingInspectionOrderDto.setOrgId(user.getOrganizationId());
                    detList.add(qmsIncomingInspectionOrderDto);
                    omPurchaseOrderDet.setTotalIssueQty(omPurchaseOrderDet.getTotalIssueQty().add(omPurchaseOrderDet.getQty()));
                    if (omPurchaseOrderDet.getTotalIssueQty().compareTo(omPurchaseOrderDet.getOrderQty()) == 0) {
                        omPurchaseOrderDet.setIfAllIssued((byte) 1);
                        omPurchaseOrderDet.setMaterialStatus((byte) 3);
                    } else {
                        omPurchaseOrderDet.setIfAllIssued((byte) 0);
                        omPurchaseOrderDet.setMaterialStatus((byte) 2);
                    }
                    list.add(omPurchaseOrderDet);
                    set.add(order.getWarehouseId());
                }
                if (set.size() > 1)
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "请选择相同仓库的进行下发操作");

                ResponseEntity responseEntity = qmsFeignApi.batchAdd(detList);
                if (responseEntity.getCode() != 0) {
                    throw new BizErrorException("下推生成来料检验单失败");
                } else {
                    i++;
                }

            } else if ("IN-IPO".equals(nextOrderTypeCode)) {
                //生成入库计划单
                SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
                searchSysSpecItem.setSpecCode("InPlanOrderIsWork");
                List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
                if (StringUtils.isEmpty(specItems))
                    throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "需先配置作业顺序先后");
                if ("0".equals(StringUtils.isEmpty(specItems.get(0).getParaValue())))
                    throw new BizErrorException(ErrorCodeEnum.OPT20012002.getCode(), "先作业后单据无法进行下推操作");

                List<WmsInInPlanOrderDetDto> detList = new LinkedList<>();

                for (OmPurchaseOrderDet omPurchaseOrderDet : detMap.get(nextOrderTypeCode)) {
                    int lineNumber = 1;

                    Map map = new HashMap();
                    map.put("purchaseOrderId", omPurchaseOrderDet.getPurchaseOrderId());
                    List<OmPurchaseOrderDto> omPurchaseOrderDto = omPurchaseOrderMapper.findList(map);
                    OmPurchaseOrderDto order = omPurchaseOrderDto.get(0);
                    coreSourceSysOrderTypeCode = order.getSysOrderTypeCode();

                    WmsInInPlanOrderDetDto wmsInInPlanOrderDet = new WmsInInPlanOrderDetDto();
                    wmsInInPlanOrderDet.setCoreSourceOrderCode(order.getPurchaseOrderCode());
                    wmsInInPlanOrderDet.setSourceOrderCode(order.getPurchaseOrderCode());
                    wmsInInPlanOrderDet.setLineNumber(lineNumber + "");
                    wmsInInPlanOrderDet.setSourceId(omPurchaseOrderDet.getPurchaseOrderDetId());
                    wmsInInPlanOrderDet.setMaterialId(omPurchaseOrderDet.getMaterialId());
                    wmsInInPlanOrderDet.setPlanQty(omPurchaseOrderDet.getQty());
                    wmsInInPlanOrderDet.setLineStatus((byte) 1);
                    detList.add(wmsInInPlanOrderDet);
                    omPurchaseOrderDet.setTotalIssueQty(omPurchaseOrderDet.getTotalIssueQty().add(omPurchaseOrderDet.getQty()));
                    if (omPurchaseOrderDet.getTotalIssueQty().compareTo(omPurchaseOrderDet.getOrderQty()) == 0) {
                        omPurchaseOrderDet.setIfAllIssued((byte) 1);
                        omPurchaseOrderDet.setMaterialStatus((byte) 3);
                    } else {
                        omPurchaseOrderDet.setIfAllIssued((byte) 0);
                        omPurchaseOrderDet.setMaterialStatus((byte) 2);
                    }
                    list.add(omPurchaseOrderDet);
                    set.add(order.getWarehouseId());
                }
                if (set.size() > 1)
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "请选择相同仓库的进行下发操作");

                WmsInInPlanOrderDto wmsInInPlanOrder = new WmsInInPlanOrderDto();
                wmsInInPlanOrder.setMakeOrderUserId(user.getUserId());
                wmsInInPlanOrder.setSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                wmsInInPlanOrder.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                wmsInInPlanOrder.setOrderStatus((byte) 1);
                Iterator<Long> iterator = set.iterator();
                while (iterator.hasNext()) {
                    wmsInInPlanOrder.setWarehouseId(iterator.next());
                }
                //查询默认收货库位
                SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
                searchBaseStorage.setWarehouseId(wmsInInPlanOrder.getWarehouseId());
                searchBaseStorage.setStorageType((byte)2);
                List<BaseStorage> data = baseFeignApi.findList(searchBaseStorage).getData();
                if(StringUtils.isNotEmpty(data))
                    wmsInInPlanOrder.setStorageId(data.get(0).getStorageId());

                wmsInInPlanOrder.setCreateUserId(user.getUserId());
                wmsInInPlanOrder.setCreateTime(new Date());
                wmsInInPlanOrder.setModifiedUserId(user.getUserId());
                wmsInInPlanOrder.setModifiedTime(new Date());
                wmsInInPlanOrder.setStatus((byte) 1);
                wmsInInPlanOrder.setOrgId(user.getOrganizationId());
                wmsInInPlanOrder.setWmsInInPlanOrderDetDtos(detList);


                ResponseEntity responseEntity = inFeignApi.add(wmsInInPlanOrder);
                if (responseEntity.getCode() != 0) {
                    throw new BizErrorException("下推生成入库计划单失败");
                } else {
                    i++;
                }
            } else if ("IN-IWK".equals(nextOrderTypeCode)) {
                //生成上架作业单

                List<WmsInnerJobOrderDet> detList = new LinkedList<>();
                for (OmPurchaseOrderDet omPurchaseOrderDet : detMap.get(nextOrderTypeCode)) {
                    int lineNumber = 1;

                    Map map = new HashMap();
                    map.put("purchaseOrderId", omPurchaseOrderDet.getPurchaseOrderId());
                    List<OmPurchaseOrderDto> omPurchaseOrderDto = omPurchaseOrderMapper.findList(map);
                    OmPurchaseOrderDto order = omPurchaseOrderDto.get(0);
                    coreSourceSysOrderTypeCode = order.getSysOrderTypeCode();

                    //查询默认收货库位
                    SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
                    searchBaseStorage.setWarehouseId(order.getWarehouseId());
                    searchBaseStorage.setStorageType((byte)2);
                    List<BaseStorage> data = baseFeignApi.findList(searchBaseStorage).getData();

                    WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
                    wmsInnerJobOrderDet.setCoreSourceOrderCode(order.getPurchaseOrderCode());
                    wmsInnerJobOrderDet.setSourceOrderCode(order.getPurchaseOrderCode());
                    wmsInnerJobOrderDet.setSourceId(omPurchaseOrderDet.getPurchaseOrderDetId());
                    wmsInnerJobOrderDet.setLineNumber(lineNumber + "");
                    wmsInnerJobOrderDet.setMaterialId(omPurchaseOrderDet.getMaterialId());
                    wmsInnerJobOrderDet.setPlanQty(omPurchaseOrderDet.getQty());
                    wmsInnerJobOrderDet.setLineStatus((byte) 1);
                    if(StringUtils.isNotEmpty(data))
                        wmsInnerJobOrderDet.setOutStorageId(data.get(0).getStorageId());

                    detList.add(wmsInnerJobOrderDet);
                    omPurchaseOrderDet.setTotalIssueQty(omPurchaseOrderDet.getTotalIssueQty().add(omPurchaseOrderDet.getQty()));
                    if (omPurchaseOrderDet.getTotalIssueQty().compareTo(omPurchaseOrderDet.getOrderQty()) == 0) {
                        omPurchaseOrderDet.setIfAllIssued((byte) 1);
                        omPurchaseOrderDet.setMaterialStatus((byte) 3);
                    } else {
                        omPurchaseOrderDet.setIfAllIssued((byte) 0);
                        omPurchaseOrderDet.setMaterialStatus((byte) 2);
                    }
                    list.add(omPurchaseOrderDet);
                    set.add(order.getWarehouseId());
                }
                if (set.size() > 1)
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "请选择相同仓库的进行下发操作");

                WmsInnerJobOrder wmsInnerJobOrder = new WmsInnerJobOrder();
                wmsInnerJobOrder.setSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                wmsInnerJobOrder.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                wmsInnerJobOrder.setSourceBigType((byte)1);
                wmsInnerJobOrder.setJobOrderType((byte) 1);
                wmsInnerJobOrder.setOrderStatus((byte) 1);
                Iterator<Long> iterator = set.iterator();
                while (iterator.hasNext()) {
                    wmsInnerJobOrder.setWarehouseId(iterator.next());
                }
                wmsInnerJobOrder.setCreateUserId(user.getUserId());
                wmsInnerJobOrder.setCreateTime(new Date());
                wmsInnerJobOrder.setModifiedUserId(user.getUserId());
                wmsInnerJobOrder.setModifiedTime(new Date());
                wmsInnerJobOrder.setStatus((byte) 1);
                wmsInnerJobOrder.setOrgId(user.getOrganizationId());
                wmsInnerJobOrder.setWmsInPutawayOrderDets(detList);

                ResponseEntity responseEntity = innerFeignApi.add(wmsInnerJobOrder);
                if (responseEntity.getCode() != 0) {
                    throw new BizErrorException("下推生成上架作业单失败");
                } else {
                    i++;
                }
            } else {
                throw new BizErrorException("单据流配置错误");
            }

            //返写下推数据
            if (StringUtils.isNotEmpty(list)) {
                for (OmPurchaseOrderDet omPurchaseOrderDet : list) {
                    omPurchaseOrderDet.setModifiedTime(new Date());
                    omPurchaseOrderDet.setModifiedUserId(user.getUserId());
                    omPurchaseOrderDetMapper.updateByPrimaryKeySelective(omPurchaseOrderDet);
                }
            }
        }
        return i;
    }

}
