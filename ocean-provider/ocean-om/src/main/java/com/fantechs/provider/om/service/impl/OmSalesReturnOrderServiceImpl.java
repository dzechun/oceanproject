package com.fantechs.provider.om.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrderFlowDto;
import com.fantechs.common.base.general.dto.om.OmHtSalesReturnOrderDto;
import com.fantechs.common.base.general.dto.om.OmSalesReturnOrderDto;
import com.fantechs.common.base.general.dto.qms.QmsIncomingInspectionOrderDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInInPlanOrderDetDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInInPlanOrderDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInPlanReceivingOrderDetDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInReceivingOrderDetDto;
import com.fantechs.common.base.general.entity.basic.BaseOrderFlow;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrderFlow;
import com.fantechs.common.base.general.entity.om.*;
import com.fantechs.common.base.general.entity.wms.in.WmsInPlanReceivingOrder;
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
import com.fantechs.provider.om.mapper.OmSalesReturnOrderDetMapper;
import com.fantechs.provider.om.mapper.OmSalesReturnOrderMapper;
import com.fantechs.provider.om.mapper.ht.OmHtSalesReturnOrderDetMapper;
import com.fantechs.provider.om.mapper.ht.OmHtSalesReturnOrderMapper;
import com.fantechs.provider.om.service.OmSalesReturnOrderService;
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
 * Created by Mr.Lei on 2021/06/21.
 */
@Service
public class OmSalesReturnOrderServiceImpl extends BaseService<OmSalesReturnOrder> implements OmSalesReturnOrderService {

    @Resource
    private OmSalesReturnOrderMapper omSalesReturnOrderMapper;
    @Resource
    private OmSalesReturnOrderDetMapper omSalesReturnOrderDetMapper;
    @Resource
    private OmHtSalesReturnOrderMapper omHtSalesReturnOrderMapper;
    @Resource
    private OmHtSalesReturnOrderDetMapper omHtSalesReturnOrderDetMapper;
    @Resource
    private InFeignApi inFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private InnerFeignApi innerFeignApi;
    @Resource
    private QmsFeignApi qmsFeignApi;

    @Override
    public List<OmSalesReturnOrderDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return omSalesReturnOrderMapper.findList(map);
    }

    @Override
    public List<OmHtSalesReturnOrderDto> findHtList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return omHtSalesReturnOrderMapper.findList(map);
    }

    /**
     * 数量累加更新状态
     * @param omSalesReturnOrder
     * @return
     */
    private int updateStatus(OmSalesReturnOrder omSalesReturnOrder){
        int num = 0;
        for (OmSalesReturnOrderDet omSalesReturnOrderDet : omSalesReturnOrder.getOmSalesReturnOrderDets()) {
            OmSalesReturnOrderDet omSalesReturnOrderDet1 = omSalesReturnOrderDetMapper.selectByPrimaryKey(omSalesReturnOrderDet.getSalesReturnOrderDetId());
            if(StringUtils.isEmpty(omSalesReturnOrderDet1.getIssueQty()) || StringUtils.isEmpty(omSalesReturnOrderDet.getIssueQty())){
                omSalesReturnOrderDet1.setIssueQty(BigDecimal.ZERO);
                omSalesReturnOrderDet.setIssueQty(BigDecimal.ZERO);
            }
            omSalesReturnOrderDet.setIssueQty(omSalesReturnOrderDet.getQty().add(omSalesReturnOrderDet1.getIssueQty()));
            num+=omSalesReturnOrderDetMapper.updateByPrimaryKeySelective(omSalesReturnOrderDet);
        }
        //统计总订单数量
        Example example = new Example(OmSalesReturnOrderDet.class);
        example.createCriteria().andEqualTo("salesReturnOrderId",omSalesReturnOrder.getSalesReturnOrderId());
        List<OmSalesReturnOrderDet> list = omSalesReturnOrderDetMapper.selectByExample(example);
        BigDecimal totalQty = list.stream()
                .map(OmSalesReturnOrderDet::getOrderQty)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal totalIssuseQty  = list.stream()
                .map(OmSalesReturnOrderDet::getIssueQty)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        if(totalQty.compareTo(totalIssuseQty)==0){
            omSalesReturnOrder.setOrderStatus((byte)3);
        }else{
            omSalesReturnOrder.setOrderStatus((byte)2);
        }
        num+=omSalesReturnOrderMapper.updateByPrimaryKeySelective(omSalesReturnOrder);
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(OmSalesReturnOrder record) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        record.setSalesReturnOrderCode(CodeUtils.getId("IN-SRO"));
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrderStatus((byte)1);
        record.setOrgId(sysUser.getOrganizationId());
        int num = omSalesReturnOrderMapper.insertUseGeneratedKeys(record);
        for (OmSalesReturnOrderDet omSalesReturnOrderDet : record.getOmSalesReturnOrderDets()) {
            omSalesReturnOrderDet.setTotalIssueQty(BigDecimal.ZERO);
            omSalesReturnOrderDet.setSalesReturnOrderId(record.getSalesReturnOrderId());
            omSalesReturnOrderDet.setCreateTime(new Date());
            omSalesReturnOrderDet.setCreateUserId(sysUser.getUserId());
            omSalesReturnOrderDet.setModifiedTime(new Date());
            omSalesReturnOrderDet.setModifiedUserId(sysUser.getUserId());
            omSalesReturnOrderDet.setOrgId(sysUser.getOrganizationId());
        }
        if(StringUtils.isNotEmpty(record.getOmSalesReturnOrderDets()) && record.getOmSalesReturnOrderDets().size()>0){
            num+=omSalesReturnOrderDetMapper.insertList(record.getOmSalesReturnOrderDets());
        }
        num+=this.addHt(record, record.getOmSalesReturnOrderDets());
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(OmSalesReturnOrder entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        int i =0;
        if(entity.getOrderStatus()>1){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"单据已被操作，无法修改");
        }
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());
        omSalesReturnOrderMapper.updateByPrimaryKeySelective(entity);

        //保存履历表
        OmHtSalesReturnOrder omHtSalesReturnOrder = new OmHtSalesReturnOrder();
        BeanUtils.copyProperties(entity, omHtSalesReturnOrder);
        omHtSalesReturnOrderMapper.insertSelective(omHtSalesReturnOrder);
        
        
        //保存详情表
        //更新原有明细
        ArrayList<Long> idList = new ArrayList<>();
        List<OmSalesReturnOrderDet> list = entity.getOmSalesReturnOrderDets();
        if(StringUtils.isNotEmpty(list)) {
            for (OmSalesReturnOrderDet det : list) {
                if(StringUtils.isEmpty(det.getOrderQty()) || det.getOrderQty().compareTo(BigDecimal.ZERO) == -1)
                    throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"计划数量需大于0");

                if (StringUtils.isNotEmpty(det.getSalesReturnOrderId())) {
                    omSalesReturnOrderDetMapper.updateByPrimaryKey(det);
                    idList.add(det.getSalesReturnOrderId());
                }
            }
        }

        //删除更新之外的明细
        Example example1 = new Example(OmSalesReturnOrderDet.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("purchaseOrderId", entity.getSalesReturnOrderId());
        if (idList.size() > 0) {
            criteria1.andNotIn("purchaseOrderDetId", idList);
        }
        omSalesReturnOrderDetMapper.deleteByExample(example1);

        //新增剩余的明细
        if(StringUtils.isNotEmpty(list)){
            List<OmSalesReturnOrderDet> addlist = new ArrayList<>();
            for (OmSalesReturnOrderDet det  : list){
                if (idList.contains(det.getSalesReturnOrderDetId())) {
                    continue;
                }
                det.setSalesReturnOrderId(entity.getSalesReturnOrderId());
                det.setCreateUserId(user.getUserId());
                det.setCreateTime(new Date());
                det.setModifiedUserId(user.getUserId());
                det.setModifiedTime(new Date());
                det.setStatus(StringUtils.isEmpty(det.getStatus())?1: det.getStatus());
                det.setOrgId(user.getOrganizationId());
                addlist.add(det);
            }
            if (StringUtils.isNotEmpty(addlist))
                omSalesReturnOrderDetMapper.insertList(addlist);
        }
        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        String[] arrId = ids.split(",");
        for (String id : arrId) {
            OmSalesReturnOrder omSalesReturnOrder = omSalesReturnOrderMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(omSalesReturnOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012000,id);
            }
            Example example = new Example(OmSalesReturnOrderDet.class);
            example.createCriteria().andEqualTo("salesReturnOrderId",omSalesReturnOrder.getSalesReturnOrderId());
            omSalesReturnOrderDetMapper.deleteByExample(example);
        }
        return omSalesReturnOrderMapper.deleteByIds(ids);
    }

    /**
     * 添加历史记录
     * @param omSalesReturnOrder
     * @param omSalesReturnOrderDets
     * @return
     */
    private int addHt(OmSalesReturnOrder omSalesReturnOrder, List<OmSalesReturnOrderDet> omSalesReturnOrderDets){
        int num = 0;
        if(StringUtils.isNotEmpty(omSalesReturnOrder)){
            OmHtSalesReturnOrder omHtSalesReturnOrder = new OmHtSalesReturnOrder();
            BeanUtil.copyProperties(omSalesReturnOrder,omHtSalesReturnOrder);
            num+=omHtSalesReturnOrderMapper.insertSelective(omHtSalesReturnOrder);
        }
        if(StringUtils.isNotEmpty(omSalesReturnOrderDets)){
            for (OmSalesReturnOrderDet omSalesReturnOrderDet : omSalesReturnOrderDets) {
                OmHtSalesReturnOrderDet omHtSalesReturnOrderDet = new OmHtSalesReturnOrderDet();
                BeanUtil.copyProperties(omSalesReturnOrderDet,omHtSalesReturnOrderDet);
                num+=omHtSalesReturnOrderDetMapper.insertSelective(omHtSalesReturnOrderDet);
            }
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int writeQty(OmSalesReturnOrderDet omSalesReturnOrderDet){
        OmSalesReturnOrder omSalesReturnOrder = omSalesReturnOrderMapper.selectByPrimaryKey(omSalesReturnOrderDet.getSalesReturnOrderId());
        if(StringUtils.isEmpty(omSalesReturnOrder)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"查询订单失败");
        }
        OmSalesReturnOrderDet omSalesReturnOrderDet1 = omSalesReturnOrderDetMapper.selectByPrimaryKey(omSalesReturnOrderDet.getSalesReturnOrderDetId());
        if(StringUtils.isEmpty(omSalesReturnOrderDet1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"未获取到订单信息");
        }
        if(StringUtils.isEmpty(omSalesReturnOrderDet1.getReceivingQty())){
            omSalesReturnOrderDet1.setReceivingQty(BigDecimal.ZERO);
        }
        omSalesReturnOrderDet.setReceivingQty(omSalesReturnOrderDet1.getReceivingQty().add(omSalesReturnOrderDet.getReceivingQty()));
        if(omSalesReturnOrderDet.getReceivingQty().compareTo(omSalesReturnOrderDet1.getIssueQty())==0){
            omSalesReturnOrder.setOrderStatus((byte)4);
        }
        int num = omSalesReturnOrderDetMapper.updateByPrimaryKeySelective(omSalesReturnOrderDet);
        num+=omSalesReturnOrderMapper.updateByPrimaryKeySelective(omSalesReturnOrder);
        return num;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int pushDown(List<OmSalesReturnOrderDet> omSalesReturnOrderDets) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        String coreSourceSysOrderTypeCode = null;
        List<OmSalesReturnOrderDet> list = new ArrayList<>();
        List<OmSalesReturnOrder> orderList = new ArrayList<>();
        int i = 0;
        HashSet<Long> set = new HashSet();
        for (OmSalesReturnOrderDet order : omSalesReturnOrderDets) {
            if (order.getIfAllIssued() != null && order.getIfAllIssued() == (byte) 1) {
                throw new BizErrorException("订单已下推，无法再次下推");
            }
            if(StringUtils.isEmpty(order.getTotalIssueQty()))
                order.setTotalIssueQty(BigDecimal.ZERO);
            if (order.getOrderQty().compareTo(order.getTotalIssueQty().add(order.getQty())) == -1) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "累计下发数量大于包装总数");
            }
            set.add(order.getWarehouseId());
        }

        if (set.size() > 1)
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "请选择相同仓库的进行下发操作");


        //查当前单据的下游单据
        SearchBaseOrderFlow searchBaseOrderFlow = new SearchBaseOrderFlow();
        searchBaseOrderFlow.setOrderTypeCode("IN-SRO");
        searchBaseOrderFlow.setStatus((byte)1);
        List<BaseOrderFlowDto> baseOrderFlowDtos = baseFeignApi.findAll(searchBaseOrderFlow).getData();
        if (StringUtils.isEmpty(baseOrderFlowDtos)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "未找到当前单据配置的下游单据");
        }

        //根据仓库分组，不同仓库生成多张单
        Map<String, List<OmSalesReturnOrderDet>> detMap = new HashMap<>();
        //不同单据流分组
        for (OmSalesReturnOrderDet omSalesReturnOrderDet : omSalesReturnOrderDets) {
            //当前单据的下游单据
            BaseOrderFlow baseOrderFlow = OrderFlowUtil.getOrderFlow(baseOrderFlowDtos, omSalesReturnOrderDet.getMaterialId(), null);
            String key = baseOrderFlow.getNextOrderTypeCode();
            if (detMap.get(key) == null) {
                List<OmSalesReturnOrderDet> diffOrderFlows = new LinkedList<>();
                diffOrderFlows.add(omSalesReturnOrderDet);
                detMap.put(key, diffOrderFlows);
            } else {
                List<OmSalesReturnOrderDet> diffOrderFlows = detMap.get(key);
                diffOrderFlows.add(omSalesReturnOrderDet);
                detMap.put(key, diffOrderFlows);
            }
        }

        Set<String> codes = detMap.keySet();
        for (String code : codes) {
            String[] split = code.split("_");
            String nextOrderTypeCode = split[0];//下游单据类型

            if ("IN-SPO".equals(nextOrderTypeCode)) {
                //生成收货计划
                List<WmsInPlanReceivingOrderDetDto> detList = new LinkedList<>();

                for (OmSalesReturnOrderDet omSalesReturnOrderDet : omSalesReturnOrderDets) {
                    int lineNumber = 1;

                    Map map = new HashMap();
                    map.put("salesReturnOrderId", omSalesReturnOrderDet.getSalesReturnOrderId());
                    List<OmSalesReturnOrderDto> omSalesReturnOrderDto = omSalesReturnOrderMapper.findList(map);
                    OmSalesReturnOrderDto order = omSalesReturnOrderDto.get(0);
                    coreSourceSysOrderTypeCode = order.getSysOrderTypeCode();

                    WmsInPlanReceivingOrderDetDto wmsInPlanReceivingOrderDetDto = new WmsInPlanReceivingOrderDetDto();
                    wmsInPlanReceivingOrderDetDto.setCoreSourceOrderCode(order.getSalesReturnOrderCode());
                    wmsInPlanReceivingOrderDetDto.setSourceOrderCode(order.getSalesReturnOrderCode());
                    wmsInPlanReceivingOrderDetDto.setLineNumber(lineNumber + "");
                    wmsInPlanReceivingOrderDetDto.setSourceId(omSalesReturnOrderDet.getSalesReturnOrderDetId());
                    wmsInPlanReceivingOrderDetDto.setMaterialId(omSalesReturnOrderDet.getMaterialId());
                    wmsInPlanReceivingOrderDetDto.setPlanQty(omSalesReturnOrderDet.getOrderQty());
                    wmsInPlanReceivingOrderDetDto.setLineStatus((byte) 1);
                    wmsInPlanReceivingOrderDetDto.setActualQty(omSalesReturnOrderDet.getReceivingQty());
                    wmsInPlanReceivingOrderDetDto.setOperatorUserId(user.getUserId());
                    detList.add(wmsInPlanReceivingOrderDetDto);
                    omSalesReturnOrderDet.setTotalIssueQty(omSalesReturnOrderDet.getTotalIssueQty().add(omSalesReturnOrderDet.getQty()));
                    if (omSalesReturnOrderDet.getTotalIssueQty().compareTo(omSalesReturnOrderDet.getOrderQty()) == 0) {
                        omSalesReturnOrderDet.setIfAllIssued((byte) 1);
                        order.setOrderStatus((byte)3);
                    } else {
                        omSalesReturnOrderDet.setIfAllIssued((byte) 0);
                        order.setOrderStatus((byte)2);
                    }
                    list.add(omSalesReturnOrderDet);
                    orderList.add(order);
                }
                WmsInPlanReceivingOrder wmsInPlanReceivingOrder = new WmsInPlanReceivingOrder();
                wmsInPlanReceivingOrder.setSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                wmsInPlanReceivingOrder.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                wmsInPlanReceivingOrder.setOrderStatus((byte) 1);
                wmsInPlanReceivingOrder.setCreateUserId(user.getUserId());
                wmsInPlanReceivingOrder.setCreateTime(new Date());
                wmsInPlanReceivingOrder.setModifiedUserId(user.getUserId());
                wmsInPlanReceivingOrder.setModifiedTime(new Date());
                wmsInPlanReceivingOrder.setStatus((byte) 1);
                wmsInPlanReceivingOrder.setOrgId(user.getOrganizationId());
                wmsInPlanReceivingOrder.setWarehouseId(omSalesReturnOrderDets.get(0).getWarehouseId());
                wmsInPlanReceivingOrder.setInPlanReceivingOrderDets(detList);

                ResponseEntity responseEntity = inFeignApi.add(wmsInPlanReceivingOrder);
                if (responseEntity.getCode() != 0) {
                    throw new BizErrorException("下推生成收货计划单失败");
                } else {
                    i++;
                }
            } else if ("IN-SWK".equals(nextOrderTypeCode)) {
                //生成收货作业

                List<WmsInReceivingOrderDetDto> detList = new LinkedList<>();

                for (OmSalesReturnOrderDet omSalesReturnOrderDet : omSalesReturnOrderDets) {
                    int lineNumber = 1;
                    Map map = new HashMap();
                    map.put("salesReturnOrderId", omSalesReturnOrderDet.getSalesReturnOrderId());
                    List<OmSalesReturnOrderDto> omSalesReturnOrderDto = omSalesReturnOrderMapper.findList(map);
                    OmSalesReturnOrderDto order = omSalesReturnOrderDto.get(0);
                    coreSourceSysOrderTypeCode = order.getSysOrderTypeCode();

                    WmsInReceivingOrderDetDto wmsInReceivingOrderDetDto = new WmsInReceivingOrderDetDto();
                    wmsInReceivingOrderDetDto.setCoreSourceOrderCode(order.getSalesReturnOrderCode());
                    wmsInReceivingOrderDetDto.setSourceOrderCode(order.getSalesReturnOrderCode());
                    wmsInReceivingOrderDetDto.setLineNumber(lineNumber + "");
                    wmsInReceivingOrderDetDto.setSourceId(omSalesReturnOrderDet.getSalesReturnOrderDetId());
                    wmsInReceivingOrderDetDto.setMaterialId(omSalesReturnOrderDet.getMaterialId());
                    wmsInReceivingOrderDetDto.setPlanQty(omSalesReturnOrderDet.getOrderQty());
                    wmsInReceivingOrderDetDto.setLineStatus((byte) 1);
                    wmsInReceivingOrderDetDto.setActualQty(omSalesReturnOrderDet.getReceivingQty());
                    wmsInReceivingOrderDetDto.setOperatorUserId(user.getUserId());
                    detList.add(wmsInReceivingOrderDetDto);
                    omSalesReturnOrderDet.setTotalIssueQty(omSalesReturnOrderDet.getTotalIssueQty().add(omSalesReturnOrderDet.getQty()));
                    if (omSalesReturnOrderDet.getTotalIssueQty().compareTo(omSalesReturnOrderDet.getOrderQty()) == 0) {
                        omSalesReturnOrderDet.setIfAllIssued((byte) 1);
                        order.setOrderStatus((byte)3);
                    } else {
                        omSalesReturnOrderDet.setIfAllIssued((byte) 0);
                        order.setOrderStatus((byte)2);
                    }
                    list.add(omSalesReturnOrderDet);
                    orderList.add(order);
                }
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
                wmsInReceivingOrder.setWarehouseId(omSalesReturnOrderDets.get(0).getWarehouseId());
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
                for (OmSalesReturnOrderDet omSalesReturnOrderDet : omSalesReturnOrderDets) {
                    int lineNumber = 1;

                    Map map = new HashMap();
                    map.put("salesReturnOrderId", omSalesReturnOrderDet.getSalesReturnOrderId());
                    List<OmSalesReturnOrderDto> omSalesReturnOrderDto = omSalesReturnOrderMapper.findList(map);
                    OmSalesReturnOrderDto order = omSalesReturnOrderDto.get(0);
                    coreSourceSysOrderTypeCode = order.getSysOrderTypeCode();

                    QmsIncomingInspectionOrderDto qmsIncomingInspectionOrderDto = new QmsIncomingInspectionOrderDto();
                    qmsIncomingInspectionOrderDto.setCoreSourceOrderCode(order.getSalesReturnOrderCode());
                    qmsIncomingInspectionOrderDto.setSourceOrderCode(order.getSalesReturnOrderCode());
                    qmsIncomingInspectionOrderDto.setSourceId(omSalesReturnOrderDet.getSalesReturnOrderDetId());
                    qmsIncomingInspectionOrderDto.setMaterialId(omSalesReturnOrderDet.getMaterialId());
                    qmsIncomingInspectionOrderDto.setWarehouseId(omSalesReturnOrderDet.getWarehouseId());
                    qmsIncomingInspectionOrderDto.setOrderQty(omSalesReturnOrderDet.getOrderQty());
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
                    omSalesReturnOrderDet.setTotalIssueQty(omSalesReturnOrderDet.getTotalIssueQty().add(omSalesReturnOrderDet.getQty()));
                    if (omSalesReturnOrderDet.getTotalIssueQty().compareTo(omSalesReturnOrderDet.getOrderQty()) == 0) {
                        omSalesReturnOrderDet.setIfAllIssued((byte) 1);
                        order.setOrderStatus((byte)3);
                    } else {
                        omSalesReturnOrderDet.setIfAllIssued((byte) 0);
                        order.setOrderStatus((byte)2);
                    }
                    list.add(omSalesReturnOrderDet);
                    orderList.add(order);
                }
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
                    throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "需先配置作业循序先后");
                if ("0".equals(StringUtils.isEmpty(specItems.get(0).getParaValue())))
                    throw new BizErrorException(ErrorCodeEnum.OPT20012002.getCode(), "先作业后单据无法进行下推操作");

                List<WmsInInPlanOrderDetDto> detList = new LinkedList<>();

                for (OmSalesReturnOrderDet omSalesReturnOrderDet : omSalesReturnOrderDets) {
                    int lineNumber = 1;

                    Map map = new HashMap();
                    map.put("salesReturnOrderId", omSalesReturnOrderDet.getSalesReturnOrderId());
                    List<OmSalesReturnOrderDto> omSalesReturnOrderDto = omSalesReturnOrderMapper.findList(map);
                    OmSalesReturnOrderDto order = omSalesReturnOrderDto.get(0);
                    coreSourceSysOrderTypeCode = order.getSysOrderTypeCode();

                    WmsInInPlanOrderDetDto wmsInInPlanOrderDet = new WmsInInPlanOrderDetDto();
                    wmsInInPlanOrderDet.setCoreSourceOrderCode(order.getSalesReturnOrderCode());
                    wmsInInPlanOrderDet.setSourceOrderCode(order.getSalesReturnOrderCode());
                    wmsInInPlanOrderDet.setLineNumber(lineNumber + "");
                    wmsInInPlanOrderDet.setSourceId(omSalesReturnOrderDet.getSalesReturnOrderDetId());
                    wmsInInPlanOrderDet.setMaterialId(omSalesReturnOrderDet.getMaterialId());
                    wmsInInPlanOrderDet.setPlanQty(omSalesReturnOrderDet.getOrderQty());
                    wmsInInPlanOrderDet.setLineStatus((byte) 1);
                    detList.add(wmsInInPlanOrderDet);
                    omSalesReturnOrderDet.setTotalIssueQty(omSalesReturnOrderDet.getTotalIssueQty().add(omSalesReturnOrderDet.getQty()));
                    if (omSalesReturnOrderDet.getTotalIssueQty().compareTo(omSalesReturnOrderDet.getOrderQty()) == 0) {
                        omSalesReturnOrderDet.setIfAllIssued((byte) 1);
                        order.setOrderStatus((byte)3);
                    } else {
                        omSalesReturnOrderDet.setIfAllIssued((byte) 0);
                        order.setOrderStatus((byte)2);
                    }
                    list.add(omSalesReturnOrderDet);
                    orderList.add(order);
                }

                WmsInInPlanOrderDto wmsInInPlanOrder = new WmsInInPlanOrderDto();
                wmsInInPlanOrder.setMakeOrderUserId(user.getUserId());
                wmsInInPlanOrder.setSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                wmsInInPlanOrder.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                wmsInInPlanOrder.setOrderStatus((byte) 1);
                wmsInInPlanOrder.setWarehouseId(omSalesReturnOrderDets.get(0).getWarehouseId());
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
                for (OmSalesReturnOrderDet omSalesReturnOrderDet : omSalesReturnOrderDets) {
                    int lineNumber = 1;

                    Map map = new HashMap();
                    map.put("salesReturnOrderId", omSalesReturnOrderDet.getSalesReturnOrderId());
                    List<OmSalesReturnOrderDto> omSalesReturnOrderDto = omSalesReturnOrderMapper.findList(map);
                    OmSalesReturnOrderDto order = omSalesReturnOrderDto.get(0);
                    coreSourceSysOrderTypeCode = order.getSysOrderTypeCode();

                    WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
                    wmsInnerJobOrderDet.setCoreSourceOrderCode(order.getSalesReturnOrderCode());
                    wmsInnerJobOrderDet.setSourceOrderCode(order.getSalesReturnOrderCode());
                    wmsInnerJobOrderDet.setSourceId(omSalesReturnOrderDet.getSalesReturnOrderDetId());
                    wmsInnerJobOrderDet.setLineNumber(lineNumber + "");
                    wmsInnerJobOrderDet.setMaterialId(omSalesReturnOrderDet.getMaterialId());
                    wmsInnerJobOrderDet.setPlanQty(omSalesReturnOrderDet.getQty());
                    wmsInnerJobOrderDet.setLineStatus((byte) 1);
                    detList.add(wmsInnerJobOrderDet);
                    omSalesReturnOrderDet.setTotalIssueQty(omSalesReturnOrderDet.getTotalIssueQty().add(omSalesReturnOrderDet.getQty()));
                    if (omSalesReturnOrderDet.getTotalIssueQty().compareTo(omSalesReturnOrderDet.getOrderQty()) == 0) {
                        omSalesReturnOrderDet.setIfAllIssued((byte) 1);
                        order.setOrderStatus((byte)3);
                    } else {
                        omSalesReturnOrderDet.setIfAllIssued((byte) 0);
                        order.setOrderStatus((byte)2);
                    }
                    list.add(omSalesReturnOrderDet);
                    orderList.add(order);
                }

                WmsInnerJobOrder wmsInnerJobOrder = new WmsInnerJobOrder();
                wmsInnerJobOrder.setSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                wmsInnerJobOrder.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                wmsInnerJobOrder.setSourceBigType((byte)1);
                wmsInnerJobOrder.setJobOrderType((byte) 1);
                wmsInnerJobOrder.setWarehouseId(omSalesReturnOrderDets.get(0).getWarehouseId());
                wmsInnerJobOrder.setOrderStatus((byte) 1);
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
        }
        //返写下推数据
        if(StringUtils.isNotEmpty(list)) {
            for (OmSalesReturnOrderDet omSalesReturnOrderDet : list) {
                omSalesReturnOrderDet.setModifiedTime(new Date());
                omSalesReturnOrderDet.setModifiedUserId(user.getUserId());
                omSalesReturnOrderDetMapper.updateByPrimaryKeySelective(omSalesReturnOrderDet);
            }
        }
        if (StringUtils.isNotEmpty(orderList)) {
            for (OmSalesReturnOrder omSalesReturnOrder : orderList) {
                omSalesReturnOrder.setModifiedTime(new Date());
                omSalesReturnOrder.setModifiedUserId(user.getUserId());
                omSalesReturnOrderMapper.updateByPrimaryKeySelective(omSalesReturnOrder);
            }
        }
        return i;
    }

}
