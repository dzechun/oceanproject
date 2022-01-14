package com.fantechs.provider.wms.out.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrderFlowDto;
import com.fantechs.common.base.general.dto.wms.out.*;
import com.fantechs.common.base.general.entity.basic.BaseOrderFlow;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrderFlow;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPlanStockListOrder;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPlanStockListOrderDet;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutPlanStockListOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.wms.out.mapper.WmsOutPlanStockListOrderDetMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutPlanStockListOrderMapper;
import com.fantechs.provider.wms.out.service.WmsOutPlanDeliveryOrderService;
import com.fantechs.provider.wms.out.service.WmsOutPlanStockListOrderService;
import com.fantechs.provider.wms.out.util.OrderFlowUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 备料计划
 * Created by leifengzhi on 2021/12/22.
 */
@Service
public class WmsOutPlanStockListOrderServiceImpl extends BaseService<WmsOutPlanStockListOrder> implements WmsOutPlanStockListOrderService {

    @Resource
    private WmsOutPlanStockListOrderMapper wmsOutPlanStockListOrderMapper;
    @Resource
    private WmsOutPlanStockListOrderDetMapper wmsOutPlanStockListOrderDetMapper;
    @Resource
    private WmsOutPlanDeliveryOrderService wmsOutPlanDeliveryOrderService;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public List<WmsOutPlanStockListOrderDto> findList(SearchWmsOutPlanStockListOrder searchWmsOutPlanStockListOrder) {
        if(StringUtils.isEmpty(searchWmsOutPlanStockListOrder.getOrgId())){
            SysUser user=currentUser();
            searchWmsOutPlanStockListOrder.setOrgId(user.getOrganizationId());
        }
        return wmsOutPlanStockListOrderMapper.findList(searchWmsOutPlanStockListOrder);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(WmsOutPlanStockListOrderDto wmsOutPlanStockListOrderDto) {
        int num=1;
        SysUser user = currentUser();
        if(StringUtils.isEmpty(wmsOutPlanStockListOrderDto.getWarehouseId())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"仓库不能为空");
        }
        String sysOrderTypeCode=wmsOutPlanStockListOrderDto.getSysOrderTypeCode();
        Byte sourceBigType=wmsOutPlanStockListOrderDto.getSourceBigType();
        String sourceSysOrderTypeCode=wmsOutPlanStockListOrderDto.getSourceSysOrderTypeCode();

        if(StringUtils.isEmpty(sysOrderTypeCode)){
            sysOrderTypeCode="OUT-PSLO";//单据类型 备料计划
            wmsOutPlanStockListOrderDto.setSysOrderTypeCode(sysOrderTypeCode);
        }
        if(StringUtils.isEmpty(sourceBigType)){
            sourceBigType=(byte)2;//来源类型 自建
            wmsOutPlanStockListOrderDto.setSourceBigType(sourceBigType);
        }
        if(StringUtils.isEmpty(sourceSysOrderTypeCode)){
            //自建的单据 单据来源类型为 生产订单
            wmsOutPlanStockListOrderDto.setSourceSysOrderTypeCode("MES-WO");
        }
        if(StringUtils.isEmpty(wmsOutPlanStockListOrderDto.getOrderStatus())){
            //待执行
            wmsOutPlanStockListOrderDto.setOrderStatus((byte)1);
        }
        if(StringUtils.isEmpty(wmsOutPlanStockListOrderDto.getPlanStockListOrderCode())){
            //计划单号
            wmsOutPlanStockListOrderDto.setPlanStockListOrderCode(CodeUtils.getId("PSLO-"));
        }
        wmsOutPlanStockListOrderDto.setCreateTime(new Date());
        wmsOutPlanStockListOrderDto.setCreateUserId(user.getUserId());
        wmsOutPlanStockListOrderDto.setOrgId(user.getOrganizationId());
        wmsOutPlanStockListOrderDto.setIsDelete((byte) 1);
        wmsOutPlanStockListOrderDto.setStatus((byte) 1);
        num=wmsOutPlanStockListOrderMapper.insertUseGeneratedKeys(wmsOutPlanStockListOrderDto);

        List<WmsOutPlanStockListOrderDetDto> listOrderDetDtos=wmsOutPlanStockListOrderDto.getWmsOutPlanStockListOrderDetDtos();
        if(listOrderDetDtos.size()>0){
            for (WmsOutPlanStockListOrderDetDto listOrderDetDto : listOrderDetDtos) {
                //自建的需要判断数量
                if(wmsOutPlanStockListOrderDto.getSourceBigType()==((byte) 2)) {
                    if(StringUtils.isNotEmpty(listOrderDetDto.getWorkOrderId())){
                        //选择工单判断
//                        MesPmWorkOrder mesPmWorkOrder = mesPmWorkOrderMapper.selectByPrimaryKey(listOrderDetDto.getWorkOrderId());
//                        if (StringUtils.isEmpty(mesPmWorkOrder)) {
//                            throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(), "找不到相应的工单信息");
//                        }
//
//                        Example exampleDet = new Example(MesPmWorkOrderBom.class);
//                        Example.Criteria criteriaDet = exampleDet.createCriteria();
//                        criteriaDet.andEqualTo("workOrderId", mesPmWorkOrder.getWorkOrderId());
//                        criteriaDet.andEqualTo("partMaterialId",listOrderDetDto.getMaterialId());
//                        List<MesPmWorkOrderBom> workOrderBoms=mesPmWorkOrderBomMapper.selectByExample(exampleDet);
//                        if(workOrderBoms.size()<=0){
//                            throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"工单BOM找不到此物料的信息 工单-->"+mesPmWorkOrder.getWorkOrderCode()+" 物料编码-->"+listOrderDetDto.getMaterialCode());
//                        }

                        //工单物料已下发总数量
//                        BigDecimal totalQty = new BigDecimal(0);
//                        Example exampleOrderDet = new Example(WmsOutPlanStockListOrderDet.class);
//                        Example.Criteria criteriaOrderDet = exampleOrderDet.createCriteria();
//                        criteriaOrderDet.andEqualTo("workOrderId", mesPmWorkOrder.getWorkOrderId());
//                        criteriaOrderDet.andEqualTo("materialId",listOrderDetDto.getMaterialId());
//                        List<WmsOutPlanStockListOrderDet> listOrderDets=wmsOutPlanStockListOrderDetMapper.selectByExample(exampleOrderDet);
//                        if(listOrderDets.size()>0){
//                            for (WmsOutPlanStockListOrderDet item : listOrderDets) {
//                                totalQty=totalQty.add(item.getOrderQty());
//                            }
//                        }
//
//                        MesPmWorkOrderBom orderBom=workOrderBoms.get(0);
//                        BigDecimal usageQty=orderBom.getUsageQty();//工单BOM物料用量
//                        BigDecimal nowQty = listOrderDetDto.getOrderQty();//本次下发数量
//
//                        if ((nowQty.add(totalQty)).compareTo(usageQty) == 1) {
//                            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "下发数量总数不能大于工单物料使用数量");
//                        }

                    }

                }

                //新增明细
                listOrderDetDto.setPlanStockListOrderId(wmsOutPlanStockListOrderDto.getPlanStockListOrderId());
                listOrderDetDto.setLineStatus((byte)1);//行状态 待出库
                listOrderDetDto.setCreateUserId(user.getUserId());
                listOrderDetDto.setCreateTime(new Date());
                listOrderDetDto.setIsDelete((byte) 1);
                listOrderDetDto.setOrgId(user.getOrganizationId());
                num = wmsOutPlanStockListOrderDetMapper.insertUseGeneratedKeys(listOrderDetDto);

            }
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(WmsOutPlanStockListOrderDto wmsOutPlanStockListOrderDto) {
        int num=1;
        SysUser user = currentUser();
        if(wmsOutPlanStockListOrderDto.getSourceBigType()!=(byte)2){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"不是自建的备料计划不允许修改");
        }

        //下发数量只能改大 不能改小
        List<WmsOutPlanStockListOrderDetDto> orderDetDtos=wmsOutPlanStockListOrderDto.getWmsOutPlanStockListOrderDetDtos();
        List<WmsOutPlanStockListOrderDet> orderDetsOld=new ArrayList<>();
        Example exampleDet = new Example(WmsOutPlanStockListOrderDet.class);
        Example.Criteria criteriaDet = exampleDet.createCriteria();
        criteriaDet.andEqualTo("planStockListOrderId", wmsOutPlanStockListOrderDto.getPlanStockListOrderId());
        orderDetsOld=wmsOutPlanStockListOrderDetMapper.selectByExample(exampleDet);
        for (WmsOutPlanStockListOrderDet listOrderDet : orderDetsOld) {
            Optional<WmsOutPlanStockListOrderDetDto> orderDetOptional = orderDetDtos.stream()
                    .filter(i -> i.getPlanStockListOrderDetId()==listOrderDet.getPlanStockListOrderDetId())
                    .findFirst();
            if (orderDetOptional.isPresent()) {
                WmsOutPlanStockListOrderDet orderDet=orderDetOptional.get();
                if(orderDet.getOrderQty().compareTo(listOrderDet.getOrderQty())==-1){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"下发数量不能改小");
                }

                orderDet.setModifiedUserId(user.getUserId());
                orderDet.setModifiedTime(new Date());
                num=wmsOutPlanStockListOrderDetMapper.updateByPrimaryKeySelective(orderDet);
            }
            else {
                //处理要删除的
                if(StringUtils.isNotEmpty(listOrderDet.getTotalIssueQty())){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"备料明细已经下发 不允许删除");
                }

                //删除备料明细
                num=wmsOutPlanStockListOrderDetMapper.deleteByPrimaryKey(listOrderDet);

            }
        }

        num=wmsOutPlanStockListOrderMapper.updateByPrimaryKeySelective(wmsOutPlanStockListOrderDto);

        return num;
    }

    /**
     * 下推出库计划
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int pushDown(List<WmsOutPlanStockListOrderDetDto> wmsOutPlanStockListOrderDetDtos) {
        int num=1;
        SysUser user=currentUser();
        Map<Long, List<WmsOutPlanStockListOrderDetDto>> collect = wmsOutPlanStockListOrderDetDtos.stream().collect(Collectors.groupingBy(WmsOutPlanStockListOrderDetDto::getWarehouseId));
        if(collect.size()>1){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"下推数据存在不同仓库 请选择同一仓库后再下推");
        }

        for (WmsOutPlanStockListOrderDetDto item : wmsOutPlanStockListOrderDetDtos){
            if(item.getIfAllIssued()!=null && item.getIfAllIssued()==(byte)1){
                throw new BizErrorException("物料"+item.getMaterialCode()+"已下推，不能重复下推");
            }
            item.setTotalIssueQty(item.getOrderQty());
            item.setIfAllIssued((byte)1);
        }
        num = wmsOutPlanStockListOrderDetMapper.batchUpdate(wmsOutPlanStockListOrderDetDtos);

        //查当前单据类型的所有单据流
        SearchBaseOrderFlow searchBaseOrderFlow = new SearchBaseOrderFlow();
        searchBaseOrderFlow.setOrderTypeCode("OUT-PSLO");//备料计划
        searchBaseOrderFlow.setStatus((byte)1);
        List<BaseOrderFlowDto> baseOrderFlowDtos = baseFeignApi.findAll(searchBaseOrderFlow).getData();
        if (StringUtils.isEmpty(baseOrderFlowDtos)) {
            throw new BizErrorException("未找到当前单据配置的单据流");
        }

        //不同单据流分组
        Map<String, List<WmsOutPlanStockListOrderDetDto>> map = new HashMap<>();
        for (WmsOutPlanStockListOrderDetDto wmsOutPlanStockListOrderDetDto : wmsOutPlanStockListOrderDetDtos) {
            //查当前单据的下游单据
            BaseOrderFlow baseOrderFlow = OrderFlowUtil.getOrderFlow(baseOrderFlowDtos, wmsOutPlanStockListOrderDetDto.getMaterialId(), null);

            String key = baseOrderFlow.getNextOrderTypeCode();
            if (map.get(key) == null) {
                List<WmsOutPlanStockListOrderDetDto> diffOrderFlows = new LinkedList<>();
                diffOrderFlows.add(wmsOutPlanStockListOrderDetDto);
                map.put(key, diffOrderFlows);
            } else {
                List<WmsOutPlanStockListOrderDetDto> diffOrderFlows = map.get(key);
                diffOrderFlows.add(wmsOutPlanStockListOrderDetDto);
                map.put(key, diffOrderFlows);
            }
        }
        //仓库ID
        Long warehouseId=wmsOutPlanStockListOrderDetDtos.get(0).getWarehouseId();

        Set<String> codes = map.keySet();
        for (String code : codes) {
            List<WmsOutPlanStockListOrderDetDto> listOrderDetDtos = map.get(code);

            if ("OUT-PDO".equals(code)) {
                //出库计划
                //核心单据类型编码
                String coreSourceSysOrderTypeCode=wmsOutPlanStockListOrderDetDtos.get(0).getCoreSourceSysOrderTypeCode();
                if(StringUtils.isEmpty(coreSourceSysOrderTypeCode))
                    coreSourceSysOrderTypeCode="OUT-PSLO";
                WmsOutPlanDeliveryOrderDto planDeliveryOrderDto=new WmsOutPlanDeliveryOrderDto();
                List<WmsOutPlanDeliveryOrderDetDto> deliveryOrderDetDtos=new ArrayList<>();

                for (WmsOutPlanStockListOrderDetDto listOrderDetDto : wmsOutPlanStockListOrderDetDtos) {
                    if (listOrderDetDto.getIfAllIssued() != null && listOrderDetDto.getIfAllIssued() == (byte) 1) {
                        throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"备料计划物料已下推完成，无法再次下推");
                    }

                    WmsOutPlanDeliveryOrderDetDto deliveryorderDetDto=new WmsOutPlanDeliveryOrderDetDto();
                    //核心单据编号
                    String coreSourceOrderCode=listOrderDetDto.getCoreSourceOrderCode();
                    if(StringUtils.isEmpty(coreSourceOrderCode))
                        coreSourceOrderCode=listOrderDetDto.getPlanStockListOrderCode();

                    deliveryorderDetDto.setCoreSourceOrderCode(coreSourceOrderCode);
                    //来源单据编号
                    deliveryorderDetDto.setSourceOrderCode(listOrderDetDto.getPlanStockListOrderCode());

                    //核心单据ID
                    Long coreSourceId=listOrderDetDto.getCoreSourceId();
                    if(StringUtils.isEmpty(coreSourceId))
                        coreSourceId=listOrderDetDto.getPlanStockListOrderDetId();
                    deliveryorderDetDto.setCoreSourceId(coreSourceId);
                    //来源单据ID
                    deliveryorderDetDto.setSourceId(listOrderDetDto.getPlanStockListOrderDetId());
                    deliveryorderDetDto.setMaterialId(listOrderDetDto.getMaterialId());
                    deliveryorderDetDto.setOrderQty(listOrderDetDto.getOrderQty());
                    deliveryorderDetDto.setLineStatus((byte)1);
                    deliveryorderDetDto.setCreateUserId(user.getUserId());
                    deliveryorderDetDto.setCreateTime(new Date());
                    deliveryOrderDetDtos.add(deliveryorderDetDto);

                    //更新明细
                    WmsOutPlanStockListOrderDet stockListOrderDet=new WmsOutPlanStockListOrderDet();
                    stockListOrderDet.setPlanStockListOrderDetId(listOrderDetDto.getPlanStockListOrderDetId());
                    stockListOrderDet.setTotalIssueQty(listOrderDetDto.getOrderQty());
                    stockListOrderDet.setIfAllIssued((byte)1);
                    stockListOrderDet.setModifiedUserId(user.getUserId());
                    stockListOrderDet.setModifiedTime(new Date());
                    num=wmsOutPlanStockListOrderDetMapper.updateByPrimaryKeySelective(stockListOrderDet);
                }


                planDeliveryOrderDto.setWarehouseId(warehouseId);
                planDeliveryOrderDto.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);//核心单据编码
                planDeliveryOrderDto.setSourceSysOrderTypeCode("OUT-PSLO");//来源单据编码 备料计划
                planDeliveryOrderDto.setSourceBigType((byte)1);//来源大类 下推
                planDeliveryOrderDto.setOrderStatus((byte)1);//待执行
                planDeliveryOrderDto.setCreateUserId(user.getUserId());
                planDeliveryOrderDto.setCreateTime(new Date());


                planDeliveryOrderDto.setWmsOutPlanDeliveryOrderDetDtos(deliveryOrderDetDtos);
                num=wmsOutPlanDeliveryOrderService.save(planDeliveryOrderDto);
                if(num<=0){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012006);
                }
            }
            else if ("OUT-IWK".equals(code)) {
                //拣货作业
                String coreSourceSysOrderTypeCode = listOrderDetDtos.get(0).getCoreSourceSysOrderTypeCode();
                int lineNumber = 1;
                List<WmsInnerJobOrderDet> wmsInnerJobOrderDets = new LinkedList<>();
                for (WmsOutPlanStockListOrderDetDto wmsOutPlanStockListOrderDetDto : listOrderDetDtos) {
                    WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
                    wmsInnerJobOrderDet.setCoreSourceOrderCode(wmsOutPlanStockListOrderDetDto.getCoreSourceOrderCode());
                    wmsInnerJobOrderDet.setSourceOrderCode(wmsOutPlanStockListOrderDetDto.getPlanStockListOrderCode());
                    wmsInnerJobOrderDet.setCoreSourceId(wmsOutPlanStockListOrderDetDto.getCoreSourceId());
                    wmsInnerJobOrderDet.setSourceId(wmsOutPlanStockListOrderDetDto.getPlanStockListOrderDetId());
                    wmsInnerJobOrderDet.setLineNumber(lineNumber + "");
                    lineNumber++;
                    wmsInnerJobOrderDet.setMaterialId(wmsOutPlanStockListOrderDetDto.getMaterialId());
                    wmsInnerJobOrderDet.setPlanQty(wmsOutPlanStockListOrderDetDto.getOrderQty());
                    wmsInnerJobOrderDet.setLineStatus((byte) 1);
                    //wmsInnerJobOrderDet.setInStorageId(inStorageId);
                    wmsInnerJobOrderDets.add(wmsInnerJobOrderDet);
                }
                WmsInnerJobOrder wmsInnerJobOrder = new WmsInnerJobOrder();
                wmsInnerJobOrder.setSourceBigType((byte) 1);
                wmsInnerJobOrder.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                wmsInnerJobOrder.setSourceSysOrderTypeCode("OUT-PSLO");
                wmsInnerJobOrder.setWarehouseId(warehouseId);
                wmsInnerJobOrder.setJobOrderType((byte) 2);
                wmsInnerJobOrder.setWmsInPutawayOrderDets(wmsInnerJobOrderDets);
//                ResponseEntity responseEntity = innerFeignApi.add(wmsInnerJobOrder);
//                if (responseEntity.getCode() != 0) {
//                    throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
//                } else {
//                    i++;
//                }
            }
        }


        return num;
    }

    /**
     * 获取当前登录用户
     * @return
     */
    private SysUser currentUser() {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}
