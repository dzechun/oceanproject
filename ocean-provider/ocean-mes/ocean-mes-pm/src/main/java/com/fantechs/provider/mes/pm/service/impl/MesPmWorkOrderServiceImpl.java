package com.fantechs.provider.mes.pm.service.impl;


import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrderFlowDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderBomDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderMaterialRePDto;
import com.fantechs.common.base.general.dto.qms.QmsIncomingInspectionOrderDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInInPlanOrderDetDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInInPlanOrderDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInPlanReceivingOrderDetDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInReceivingOrderDetDto;
import com.fantechs.common.base.general.entity.basic.BaseOrderFlow;
import com.fantechs.common.base.general.entity.basic.BaseProductMaterialReP;
import com.fantechs.common.base.general.entity.basic.BaseProductProcessReM;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrderFlow;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductProcessReM;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderBom;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderProcessReWo;
import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
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
import com.fantechs.provider.mes.pm.mapper.MesPmHtWorkOrderMapper;
import com.fantechs.provider.mes.pm.mapper.MesPmWorkOrderBomMapper;
import com.fantechs.provider.mes.pm.mapper.MesPmWorkOrderMapper;
import com.fantechs.provider.mes.pm.service.MesPmWorkOrderProcessReWoService;
import com.fantechs.provider.mes.pm.service.MesPmWorkOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import com.fantechs.provider.mes.pm.util.OrderFlowUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;


@Service
@Slf4j
public class MesPmWorkOrderServiceImpl extends BaseService<MesPmWorkOrder> implements MesPmWorkOrderService {

    @Resource
    private MesPmWorkOrderMapper mesPmWorkOrderMapper;
    @Resource
    private MesPmHtWorkOrderMapper smtHtWorkOrderMapper;
    @Resource
    private MesPmWorkOrderBomMapper mesPmWorkOrderBomMapper;
    @Resource
    private MesPmWorkOrderProcessReWoService mesPmWorkOrderProcessReWoService;
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
    @Transactional(rollbackFor = Exception.class)
    public int save(MesPmWorkOrderDto mesPmWorkOrderDto) {
        SysUser currentUser = currentUser();


        if(StringUtils.isEmpty(mesPmWorkOrderDto.getWorkOrderCode())){
            mesPmWorkOrderDto.setWorkOrderCode(CodeUtils.getId("WORK"));
        }else{
            Example example = new Example(MesPmWorkOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("workOrderCode", mesPmWorkOrderDto.getWorkOrderCode());

            List<MesPmWorkOrder> mesPmWorkOrders = mesPmWorkOrderMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(mesPmWorkOrders)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }
        }
        mesPmWorkOrderDto.setTotalIssueQty(BigDecimal.ZERO);
        mesPmWorkOrderDto.setWorkOrderStatus((byte) 1);
        mesPmWorkOrderDto.setCreateUserId(currentUser.getUserId());
        mesPmWorkOrderDto.setCreateTime(new Date());
        mesPmWorkOrderDto.setModifiedUserId(currentUser.getUserId());
        mesPmWorkOrderDto.setModifiedTime(new Date());
        mesPmWorkOrderDto.setOrgId(currentUser.getOrganizationId());
        mesPmWorkOrderDto.setIsDelete((byte)1);
        int i = mesPmWorkOrderMapper.insertUseGeneratedKeys(mesPmWorkOrderDto);

        //工单履历表
        recordHistory(mesPmWorkOrderDto);
        //保存bom表
        savebom(mesPmWorkOrderDto,currentUser);
        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int updateInventoryQty(MesPmWorkOrder mesPmWorkOrder){
       return mesPmWorkOrderMapper.updateByPrimaryKeySelective(mesPmWorkOrder);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int update(MesPmWorkOrderDto mesPmWorkOrderDto) {
        int i = 0;
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        MesPmWorkOrder order = mesPmWorkOrderMapper.selectByPrimaryKey(mesPmWorkOrderDto.getWorkOrderId());

        //工单状态(0、待生产 1、生产中 2、暂停生产 3、生产完成)
        Integer workOrderStatus = order.getWorkOrderStatus().intValue();
        if (workOrderStatus != 4) {
            Example example = new Example(MesPmWorkOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("workOrderCode", mesPmWorkOrderDto.getWorkOrderCode());

            MesPmWorkOrder workOrder = mesPmWorkOrderMapper.selectOneByExample(example);

            if (StringUtils.isNotEmpty(workOrder) && !workOrder.getWorkOrderId().equals(mesPmWorkOrderDto.getWorkOrderId())) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }

            mesPmWorkOrderDto.setModifiedUserId(currentUser.getUserId());
            mesPmWorkOrderDto.setModifiedTime(new Date());
            i = mesPmWorkOrderMapper.updateByPrimaryKeySelective(mesPmWorkOrderDto);

            //新增工单历史信息
            MesPmHtWorkOrder mesPmHtWorkOrder = new MesPmHtWorkOrder();
            BeanUtils.copyProperties(mesPmWorkOrderDto, mesPmHtWorkOrder);
            mesPmHtWorkOrder.setModifiedUserId(currentUser.getUserId());
            mesPmHtWorkOrder.setModifiedTime(new Date());
            smtHtWorkOrderMapper.insertSelective(mesPmHtWorkOrder);
        } else {
            throw new BizErrorException("生产完成的工单不允许修改");
        }

        Example detExample = new Example(MesPmWorkOrderBom.class);
        Example.Criteria detCriteria = detExample.createCriteria();
        detCriteria.andEqualTo("workOrderId", mesPmWorkOrderDto.getWorkOrderId());
        mesPmWorkOrderBomMapper.deleteByExample(detExample);
        detExample.clear();
        //保存bom表
        savebom(mesPmWorkOrderDto,currentUser);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int updatePmWorkOrder(MesPmWorkOrder mesPmWorkOrder) {
        int i = 0;
        //mesPmWorkOrder.setModifiedUserId(currentUser.getUserId());
        mesPmWorkOrder.setModifiedTime(new Date());
        i = mesPmWorkOrderMapper.updateByPrimaryKeySelective(mesPmWorkOrder);

        //新增工单历史信息
        MesPmHtWorkOrder mesPmHtWorkOrder = new MesPmHtWorkOrder();
        BeanUtils.copyProperties(mesPmWorkOrder, mesPmHtWorkOrder);
        //mesPmHtWorkOrder.setModifiedUserId(currentUser.getUserId());
        mesPmHtWorkOrder.setModifiedTime(new Date());
        smtHtWorkOrderMapper.insertSelective(mesPmHtWorkOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        List<MesPmHtWorkOrder> list = new ArrayList<>();

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] workOrderIds = ids.split(",");
        for (String workOrderId : workOrderIds) {
            MesPmWorkOrder mesPmWorkOrder = mesPmWorkOrderMapper.selectByPrimaryKey(workOrderId);
            if(StringUtils.isNotEmpty(mesPmWorkOrder.getScheduledQty()) && mesPmWorkOrder.getScheduledQty().doubleValue()>0){
                throw new BizErrorException("工单已排产，不允许删除:"+ mesPmWorkOrder.getWorkOrderCode());
            }
            if (StringUtils.isEmpty(mesPmWorkOrder)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //新增工单历史信息
            MesPmHtWorkOrder mesPmHtWorkOrder = new MesPmHtWorkOrder();
            BeanUtils.copyProperties(mesPmWorkOrder, mesPmHtWorkOrder);
            mesPmHtWorkOrder.setModifiedUserId(currentUser.getUserId());
            mesPmHtWorkOrder.setModifiedTime(new Date());
            list.add(mesPmHtWorkOrder);
        }
        if(StringUtils.isNotEmpty(list)) smtHtWorkOrderMapper.insertList(list);
        return mesPmWorkOrderMapper.deleteByIds(ids);
    }


    @Override
    public List<MesPmWorkOrderDto> findList(SearchMesPmWorkOrder searchMesPmWorkOrder) {
        if(StringUtils.isEmpty(searchMesPmWorkOrder.getOrgId())) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            searchMesPmWorkOrder.setOrgId(user.getOrganizationId());
        }
        return mesPmWorkOrderMapper.findList(searchMesPmWorkOrder);
    }

    @Override
    public List<MesPmWorkOrderDto> pdaFindList(SearchMesPmWorkOrder searchMesPmWorkOrder) {
        return mesPmWorkOrderMapper.pdaFindList(searchMesPmWorkOrder);
    }

    /**
     * 获取当前登录用户
     * @return
     */
    private SysUser currentUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }

    /**
     * 记录操作历史
     * @param mesPmWorkOrder
     */
    private void recordHistory(MesPmWorkOrder mesPmWorkOrder){
        MesPmHtWorkOrder mesPmHtWorkOrder = new MesPmHtWorkOrder();
        BeanUtils.copyProperties(mesPmWorkOrder, mesPmHtWorkOrder);
        smtHtWorkOrderMapper.insertSelective(mesPmHtWorkOrder);
    }

    @Override
    public MesPmWorkOrder saveByApi(MesPmWorkOrder mesPmWorkOrder){
        Example example = new Example(MesPmWorkOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workOrderCode", mesPmWorkOrder.getWorkOrderCode());
        criteria.andEqualTo("orgId", mesPmWorkOrder.getOrgId());
        List<MesPmWorkOrder> mesPmWorkOrders = mesPmWorkOrderMapper.selectByExample(example);
        //mesPmWorkOrderMapper.deleteByExample(example);
        if(StringUtils.isNotEmpty(mesPmWorkOrders)) {
            mesPmWorkOrder.setWorkOrderId(mesPmWorkOrders.get(0).getWorkOrderId());
            mesPmWorkOrderMapper.updateByPrimaryKeySelective(mesPmWorkOrder);
        }else{
            mesPmWorkOrder.setModifiedTime(new Date());
            mesPmWorkOrder.setCreateTime(new Date());
            mesPmWorkOrderMapper.insertUseGeneratedKeys(mesPmWorkOrder);
        }
        example.clear();

        //删除该工单下的所有bom表单
        if(StringUtils.isNotEmpty(mesPmWorkOrders)) {
            Example bomExample = new Example(MesPmWorkOrderBom.class);
            Example.Criteria bomCriteria = bomExample.createCriteria();
            bomCriteria.andEqualTo("workOrderId", mesPmWorkOrders.get(0).getWorkOrderId());
            mesPmWorkOrderBomMapper.deleteByExample(bomExample);
            bomExample.clear();
        }

        return mesPmWorkOrder;
    }

    @Override
    public List<MesPmWorkOrder> getWorkOrderList(List<String> workOrderIds) {
        return mesPmWorkOrderMapper.getWorkOrderList(workOrderIds);
    }

    @Override
    public int batchUpdate(List<MesPmWorkOrder> mesPmWorkOrders) {
        return mesPmWorkOrderMapper.batchUpdate(mesPmWorkOrders);
    }

    @Override
    public int batchSave(List<MesPmWorkOrder> pmWorkOrders){
        // 产品关键物料清单
        SearchBaseProductProcessReM searchBaseProductProcessReM = new SearchBaseProductProcessReM();
        searchBaseProductProcessReM.setPageSize(999999);
        List<BaseProductProcessReM> baseProductProcessReMS = baseFeignApi.findList(searchBaseProductProcessReM).getData();
        for (MesPmWorkOrder order : pmWorkOrders){
            mesPmWorkOrderMapper.insertUseGeneratedKeys(order);

            // 工单关键物料清单
            MesPmWorkOrderProcessReWo reWo = new MesPmWorkOrderProcessReWo();
            boolean flag = false;
            for (BaseProductProcessReM baseProductProcessReM : baseProductProcessReMS){
                if (order.getMaterialId().equals(baseProductProcessReM.getMaterialId())){
                    BeanUtils.copyProperties(baseProductProcessReM, reWo);

                    List<MesPmWorkOrderMaterialRePDto> list = new ArrayList<>();
                    for (BaseProductMaterialReP baseProductMaterialReP : baseProductProcessReM.getList()){
                        MesPmWorkOrderMaterialRePDto mesPmWorkOrderMaterialRePDto = new MesPmWorkOrderMaterialRePDto();
                        BeanUtils.copyProperties(baseProductMaterialReP, mesPmWorkOrderMaterialRePDto);
//                        log.info("====================mesPmWorkOrderMaterialRePDto" + JSON.toJSONString(mesPmWorkOrderMaterialRePDto));
                        list.add(mesPmWorkOrderMaterialRePDto);
                    }
                    reWo.setList(list);

                    flag = true;
                    break;
                }
            }
            if(flag){
                reWo.setWorkOrderId(order.getWorkOrderId());
                reWo.setCreateTime(new Date());
                reWo.setModifiedTime(new Date());
                mesPmWorkOrderProcessReWoService.save(reWo);
            }
        }
        return 1;
    }

    public  void savebom(MesPmWorkOrderDto mesPmWorkOrderDto , SysUser user){
        List<MesPmWorkOrderBom> boms = new ArrayList<>();
        if(StringUtils.isNotEmpty(mesPmWorkOrderDto.getMesPmWorkOrderBomDtos())) {
            for (MesPmWorkOrderBomDto mesPmWorkOrderBomDto :  mesPmWorkOrderDto.getMesPmWorkOrderBomDtos()) {
                mesPmWorkOrderBomDto.setCreateUserId(user.getUserId());
                mesPmWorkOrderBomDto.setCreateTime(new Date());
                mesPmWorkOrderBomDto.setModifiedUserId(user.getUserId());
                mesPmWorkOrderBomDto.setModifiedTime(new Date());
                mesPmWorkOrderBomDto.setOrgId(user.getOrganizationId());
                mesPmWorkOrderBomDto.setWorkOrderId(mesPmWorkOrderDto.getWorkOrderId());
                boms.add(mesPmWorkOrderBomDto);
            }
        }
        if(StringUtils.isNotEmpty(boms))  mesPmWorkOrderBomMapper.insertList(boms);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int pushDown(List<MesPmWorkOrder> mesPmWorkOrders) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        List<MesPmWorkOrder> list = new ArrayList<>();
        String coreSourceSysOrderTypeCode = mesPmWorkOrders.get(0).getCoreSourceSysOrderTypeCode();

        int i = 0;
        HashSet<Long> set = new HashSet();
        for (MesPmWorkOrder order : mesPmWorkOrders) {
            if (order.getIfAllIssued() != null && order.getIfAllIssued() == (byte) 1) {
                throw new BizErrorException("订单已下推，无法再次下推");
            }
            if(StringUtils.isEmpty(order.getTotalIssueQty()))
                order.setTotalIssueQty(BigDecimal.ZERO);
            if(order.getOutputQty().compareTo(order.getTotalIssueQty().add(order.getQty())) == -1 )
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "累计下发数量大于包装总数");
            set.add(order.getWarehouseId());
        }
        if (set.size() > 1)
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "请选择相同仓库的进行下发操作");

        //查当前单据的下游单据
        SearchBaseOrderFlow searchBaseOrderFlow = new SearchBaseOrderFlow();
        searchBaseOrderFlow.setOrderTypeCode("MES-WO");
        List<BaseOrderFlowDto> baseOrderFlowDtos = baseFeignApi.findList(searchBaseOrderFlow).getData();
        if (StringUtils.isEmpty(baseOrderFlowDtos)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "未找到当前单据配置的下游单据");
        }

        //根据仓库分组，不同仓库生成多张单
        Map<String, List<MesPmWorkOrder>> detMap = new HashMap<>();
        //不同单据流分组
        for (MesPmWorkOrder mesPmWorkOrder : mesPmWorkOrders) {
            //当前单据的下游单据
            BaseOrderFlow baseOrderFlow = OrderFlowUtil.getOrderFlow(baseOrderFlowDtos, mesPmWorkOrder.getMaterialId(), null);
            String key = baseOrderFlow.getNextOrderTypeCode();
            if (detMap.get(key) == null) {
                List<MesPmWorkOrder> diffOrderFlows = new LinkedList<>();
                diffOrderFlows.add(mesPmWorkOrder);
                detMap.put(key, diffOrderFlows);
            } else {
                List<MesPmWorkOrder> diffOrderFlows = detMap.get(key);
                diffOrderFlows.add(mesPmWorkOrder);
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

                for (MesPmWorkOrder mesPmWorkOrder : mesPmWorkOrders) {
                    int lineNumber = 1;

                    WmsInPlanReceivingOrderDetDto wmsInPlanReceivingOrderDetDto = new WmsInPlanReceivingOrderDetDto();
                    wmsInPlanReceivingOrderDetDto.setCoreSourceOrderCode(mesPmWorkOrder.getWorkOrderCode());
                    wmsInPlanReceivingOrderDetDto.setSourceOrderCode(mesPmWorkOrder.getWorkOrderCode());
                    wmsInPlanReceivingOrderDetDto.setLineNumber(lineNumber + "");
                    wmsInPlanReceivingOrderDetDto.setSourceId(mesPmWorkOrder.getWorkOrderId());
                    wmsInPlanReceivingOrderDetDto.setMaterialId(mesPmWorkOrder.getMaterialId());
                    wmsInPlanReceivingOrderDetDto.setPlanQty(mesPmWorkOrder.getWorkOrderQty());  //完工数量
                    wmsInPlanReceivingOrderDetDto.setLineStatus((byte) 1);
                    wmsInPlanReceivingOrderDetDto.setActualQty(mesPmWorkOrder.getOutputQty());
                    wmsInPlanReceivingOrderDetDto.setOperatorUserId(user.getUserId());
                    detList.add(wmsInPlanReceivingOrderDetDto);
                    mesPmWorkOrder.setTotalIssueQty(mesPmWorkOrder.getTotalIssueQty().add(mesPmWorkOrder.getQty()));
                    if (mesPmWorkOrder.getTotalIssueQty().compareTo(mesPmWorkOrder.getOutputQty()) == 0) {
                        mesPmWorkOrder.setIfAllIssued((byte) 1);
                    } else {
                        mesPmWorkOrder.setIfAllIssued((byte) 0);
                    }
                    list.add(mesPmWorkOrder);
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
                wmsInPlanReceivingOrder.setWarehouseId(mesPmWorkOrders.get(0).getWarehouseId());
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

                for (MesPmWorkOrder mesPmWorkOrder : mesPmWorkOrders) {
                    int lineNumber = 1;

                    WmsInReceivingOrderDetDto wmsInReceivingOrderDetDto = new WmsInReceivingOrderDetDto();
                    wmsInReceivingOrderDetDto.setCoreSourceOrderCode(mesPmWorkOrder.getWorkOrderCode());
                    wmsInReceivingOrderDetDto.setSourceOrderCode(mesPmWorkOrder.getWorkOrderCode());
                    wmsInReceivingOrderDetDto.setLineNumber(lineNumber + "");
                    wmsInReceivingOrderDetDto.setSourceId(mesPmWorkOrder.getWorkOrderId());
                    wmsInReceivingOrderDetDto.setMaterialId(mesPmWorkOrder.getMaterialId());
                    wmsInReceivingOrderDetDto.setPlanQty(mesPmWorkOrder.getWorkOrderQty());
                    wmsInReceivingOrderDetDto.setLineStatus((byte) 1);
                    wmsInReceivingOrderDetDto.setActualQty(mesPmWorkOrder.getOutputQty());
                    wmsInReceivingOrderDetDto.setOperatorUserId(user.getUserId());
                    detList.add(wmsInReceivingOrderDetDto);
                    mesPmWorkOrder.setTotalIssueQty(mesPmWorkOrder.getTotalIssueQty().add(mesPmWorkOrder.getQty()));
                    if (mesPmWorkOrder.getTotalIssueQty().compareTo(mesPmWorkOrder.getOutputQty()) == 0) {
                        mesPmWorkOrder.setIfAllIssued((byte) 1);
                    } else {
                        mesPmWorkOrder.setIfAllIssued((byte) 0);
                    }
                    list.add(mesPmWorkOrder);
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
                wmsInReceivingOrder.setWarehouseId(mesPmWorkOrders.get(0).getWarehouseId());
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
                for (MesPmWorkOrder mesPmWorkOrder : mesPmWorkOrders) {
                    int lineNumber = 1;

                    QmsIncomingInspectionOrderDto qmsIncomingInspectionOrderDto = new QmsIncomingInspectionOrderDto();
                    qmsIncomingInspectionOrderDto.setCoreSourceOrderCode(mesPmWorkOrder.getWorkOrderCode());
                    qmsIncomingInspectionOrderDto.setSourceOrderCode(mesPmWorkOrder.getWorkOrderCode());
                    qmsIncomingInspectionOrderDto.setSourceId(mesPmWorkOrder.getWorkOrderId());
                    qmsIncomingInspectionOrderDto.setMaterialId(mesPmWorkOrder.getMaterialId());
                    qmsIncomingInspectionOrderDto.setWarehouseId(mesPmWorkOrder.getWarehouseId());
                    qmsIncomingInspectionOrderDto.setOrderQty(mesPmWorkOrder.getOutputQty());
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
                    mesPmWorkOrder.setTotalIssueQty(mesPmWorkOrder.getTotalIssueQty().add(mesPmWorkOrder.getQty()));
                    if (mesPmWorkOrder.getTotalIssueQty().compareTo(mesPmWorkOrder.getOutputQty()) == 0)
                        mesPmWorkOrder.setIfAllIssued((byte) 1);
                    else
                        mesPmWorkOrder.setIfAllIssued((byte) 0);
                    list.add(mesPmWorkOrder);
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

                for (MesPmWorkOrder mesPmWorkOrder : mesPmWorkOrders) {
                    int lineNumber = 1;

                    WmsInInPlanOrderDetDto wmsInInPlanOrderDet = new WmsInInPlanOrderDetDto();
                    wmsInInPlanOrderDet.setCoreSourceOrderCode(mesPmWorkOrder.getWorkOrderCode());
                    wmsInInPlanOrderDet.setSourceOrderCode(mesPmWorkOrder.getWorkOrderCode());
                    wmsInInPlanOrderDet.setLineNumber(lineNumber + "");
                    wmsInInPlanOrderDet.setSourceId(mesPmWorkOrder.getWorkOrderId());
                    wmsInInPlanOrderDet.setMaterialId(mesPmWorkOrder.getMaterialId());
                    wmsInInPlanOrderDet.setPlanQty(mesPmWorkOrder.getOutputQty());
                    wmsInInPlanOrderDet.setLineStatus((byte) 1);
                    detList.add(wmsInInPlanOrderDet);
                    mesPmWorkOrder.setTotalIssueQty(mesPmWorkOrder.getTotalIssueQty().add(mesPmWorkOrder.getQty()));
                    if (mesPmWorkOrder.getTotalIssueQty().compareTo(mesPmWorkOrder.getOutputQty()) == 0)
                        mesPmWorkOrder.setIfAllIssued((byte) 1);
                    else
                        mesPmWorkOrder.setIfAllIssued((byte) 0);
                    list.add(mesPmWorkOrder);
                }

                WmsInInPlanOrderDto wmsInInPlanOrder = new WmsInInPlanOrderDto();
                wmsInInPlanOrder.setMakeOrderUserId(user.getUserId());
                wmsInInPlanOrder.setSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                wmsInInPlanOrder.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                wmsInInPlanOrder.setOrderStatus((byte) 1);
                wmsInInPlanOrder.setCreateUserId(user.getUserId());
                wmsInInPlanOrder.setCreateTime(new Date());
                wmsInInPlanOrder.setWarehouseId(mesPmWorkOrders.get(0).getWarehouseId());
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
                for (MesPmWorkOrder mesPmWorkOrder : mesPmWorkOrders) {
                    int lineNumber = 1;

                    WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
                    wmsInnerJobOrderDet.setCoreSourceOrderCode(mesPmWorkOrder.getWorkOrderCode());
                    wmsInnerJobOrderDet.setSourceOrderCode(mesPmWorkOrder.getWorkOrderCode());
                    wmsInnerJobOrderDet.setSourceId(mesPmWorkOrder.getWorkOrderId());
                    wmsInnerJobOrderDet.setLineNumber(lineNumber + "");
                    wmsInnerJobOrderDet.setMaterialId(mesPmWorkOrder.getMaterialId());
                    wmsInnerJobOrderDet.setPlanQty(mesPmWorkOrder.getQty());
                    wmsInnerJobOrderDet.setLineStatus((byte) 1);
                    detList.add(wmsInnerJobOrderDet);
                    mesPmWorkOrder.setTotalIssueQty(mesPmWorkOrder.getTotalIssueQty().add(mesPmWorkOrder.getQty()));
                    if (mesPmWorkOrder.getTotalIssueQty().compareTo(mesPmWorkOrder.getOutputQty()) == 0)
                        mesPmWorkOrder.setIfAllIssued((byte) 1);
                    else
                        mesPmWorkOrder.setIfAllIssued((byte) 0);
                    list.add(mesPmWorkOrder);
                }

                WmsInnerJobOrder wmsInnerJobOrder = new WmsInnerJobOrder();
                wmsInnerJobOrder.setSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                wmsInnerJobOrder.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                wmsInnerJobOrder.setSourceBigType((byte)1);
                wmsInnerJobOrder.setJobOrderType((byte) 1);
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
            for (MesPmWorkOrder mesPmWorkOrder : list) {
                mesPmWorkOrder.setModifiedTime(new Date());
                mesPmWorkOrder.setModifiedUserId(user.getUserId());
                mesPmWorkOrderMapper.updateByPrimaryKeySelective(mesPmWorkOrder);
            }
        }

        return i;
    }

}
