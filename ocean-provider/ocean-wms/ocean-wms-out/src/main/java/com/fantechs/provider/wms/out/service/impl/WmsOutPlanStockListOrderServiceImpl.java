package com.fantechs.provider.wms.out.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysImportAndExportLog;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrderFlowDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanDeliveryOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanDeliveryOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanStockListOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanStockListOrderDto;
import com.fantechs.common.base.general.dto.wms.out.imports.WmsOutPlanStockListOrderImport;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseOrderFlow;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrderFlow;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehouse;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPlanStockListOrder;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPlanStockListOrderDet;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtPlanStockListOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.wms.out.mapper.WmsOutHtPlanStockListOrderMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutPlanStockListOrderDetMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutPlanStockListOrderMapper;
import com.fantechs.provider.wms.out.service.WmsOutPlanDeliveryOrderService;
import com.fantechs.provider.wms.out.service.WmsOutPlanStockListOrderService;
import com.fantechs.provider.wms.out.util.OrderFlowUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
    private WmsOutHtPlanStockListOrderMapper wmsOutHtPlanStockListOrderMapper;
    @Resource
    private WmsOutPlanStockListOrderDetMapper wmsOutPlanStockListOrderDetMapper;
    @Resource
    private WmsOutPlanDeliveryOrderService wmsOutPlanDeliveryOrderService;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private InnerFeignApi innerFeignApi;
    @Resource
    private AuthFeignApi securityFeignApi;

    @Override
    public List<WmsOutPlanStockListOrderDto> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))){
            SysUser user=currentUser();
            map.put("orgId",user.getOrganizationId());
        }
        return wmsOutPlanStockListOrderMapper.findList(map);
    }

    @Override
    public List<WmsOutHtPlanStockListOrder> findHtList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))){
            SysUser user=currentUser();
            map.put("orgId",user.getOrganizationId());
        }
        return wmsOutHtPlanStockListOrderMapper.findHtList(map);
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
        wmsOutPlanStockListOrderDto.setModifiedTime(new Date());
        wmsOutPlanStockListOrderDto.setModifiedUserId(user.getUserId());
        wmsOutPlanStockListOrderDto.setOrgId(user.getOrganizationId());
        wmsOutPlanStockListOrderDto.setIsDelete((byte) 1);
        wmsOutPlanStockListOrderDto.setStatus((byte) 1);
        num=wmsOutPlanStockListOrderMapper.insertUseGeneratedKeys(wmsOutPlanStockListOrderDto);

        //履历
        WmsOutHtPlanStockListOrder wmsOutHtPlanStockListOrder = new WmsOutHtPlanStockListOrder();
        BeanUtils.copyProperties(wmsOutPlanStockListOrderDto, wmsOutHtPlanStockListOrder);
        wmsOutHtPlanStockListOrderMapper.insertSelective(wmsOutHtPlanStockListOrder);

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

        Example example1 = new Example(WmsOutPlanStockListOrderDet.class);
        Example.Criteria criteria = example1.createCriteria();
        criteria.andEqualTo("planStockListOrderId",wmsOutPlanStockListOrderDto.getPlanStockListOrderId());
        List<WmsOutPlanStockListOrderDet> oldPlanStockListOrderDets = wmsOutPlanStockListOrderDetMapper.selectByExample(example1);

        //原来有的明细只更新
        ArrayList<Long> idList = new ArrayList<>();
        List<WmsOutPlanStockListOrderDetDto> wmsOutPlanStockListOrderDetDtos = wmsOutPlanStockListOrderDto.getWmsOutPlanStockListOrderDetDtos();
        if(StringUtils.isNotEmpty(wmsOutPlanStockListOrderDetDtos)) {
            for (WmsOutPlanStockListOrderDetDto wmsOutPlanStockListOrderDetDto : wmsOutPlanStockListOrderDetDtos) {
                if (StringUtils.isNotEmpty(wmsOutPlanStockListOrderDetDto.getPlanStockListOrderDetId())) {
                    for (WmsOutPlanStockListOrderDet oldPlanStockListOrderDet : oldPlanStockListOrderDets){
                        if(wmsOutPlanStockListOrderDetDto.getPlanStockListOrderDetId().equals(oldPlanStockListOrderDet.getPlanStockListOrderDetId())){
                            //下发数量只能改大 不能改小
                            if (wmsOutPlanStockListOrderDetDto.getOrderQty().compareTo(oldPlanStockListOrderDet.getOrderQty()) == -1) {
                                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "下发数量不能改小");
                            }
                            wmsOutPlanStockListOrderDetDto.setModifiedUserId(user.getUserId());
                            wmsOutPlanStockListOrderDetDto.setModifiedTime(new Date());
                            wmsOutPlanStockListOrderDetMapper.updateByPrimaryKey(wmsOutPlanStockListOrderDetDto);
                            idList.add(wmsOutPlanStockListOrderDetDto.getPlanStockListOrderDetId());
                        }
                    }
                }
            }
        }

        //删除原明细
        example1.clear();
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("planStockListOrderId", wmsOutPlanStockListOrderDto.getPlanStockListOrderId());
        if (idList.size() > 0) {
            criteria1.andNotIn("planStockListOrderDetId", idList);
        }
        List<WmsOutPlanStockListOrderDet> wmsOutPlanStockListOrderDets = wmsOutPlanStockListOrderDetMapper.selectByExample(example1);
        List<WmsOutPlanStockListOrderDet> collect = wmsOutPlanStockListOrderDets.stream()
                .filter(i -> StringUtils.isNotEmpty(i.getTotalIssueQty()))
                .collect(Collectors.toList());
        if(StringUtils.isNotEmpty(collect)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "备料明细已经下发 不允许删除");
        }
        wmsOutPlanStockListOrderDetMapper.deleteByExample(example1);

        //明细
        if(StringUtils.isNotEmpty(wmsOutPlanStockListOrderDetDtos)){
            for (WmsOutPlanStockListOrderDetDto wmsOutPlanStockListOrderDetDto : wmsOutPlanStockListOrderDetDtos){
                if (idList.contains(wmsOutPlanStockListOrderDetDto.getPlanStockListOrderDetId())) {
                    continue;
                }
                wmsOutPlanStockListOrderDetDto.setPlanStockListOrderId(wmsOutPlanStockListOrderDto.getPlanStockListOrderId());
                wmsOutPlanStockListOrderDetDto.setCreateUserId(user.getUserId());
                wmsOutPlanStockListOrderDetDto.setCreateTime(new Date());
                wmsOutPlanStockListOrderDetDto.setModifiedUserId(user.getUserId());
                wmsOutPlanStockListOrderDetDto.setModifiedTime(new Date());
                wmsOutPlanStockListOrderDetDto.setStatus(StringUtils.isEmpty(wmsOutPlanStockListOrderDetDto.getStatus())?1: wmsOutPlanStockListOrderDetDto.getStatus());
                wmsOutPlanStockListOrderDetDto.setOrgId(user.getOrganizationId());
                wmsOutPlanStockListOrderDetMapper.insertSelective(wmsOutPlanStockListOrderDetDto);
            }
        }

        wmsOutPlanStockListOrderDto.setModifiedUserId(user.getUserId());
        wmsOutPlanStockListOrderDto.setModifiedTime(new Date());
        num=wmsOutPlanStockListOrderMapper.updateByPrimaryKeySelective(wmsOutPlanStockListOrderDto);

        //履历
        WmsOutHtPlanStockListOrder wmsOutHtPlanStockListOrder = new WmsOutHtPlanStockListOrder();
        BeanUtils.copyProperties(wmsOutPlanStockListOrderDto, wmsOutHtPlanStockListOrder);
        wmsOutHtPlanStockListOrderMapper.insertSelective(wmsOutHtPlanStockListOrder);

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
            num += wmsOutPlanStockListOrderDetMapper.updateByPrimaryKeySelective(item);
        }
        //num = wmsOutPlanStockListOrderDetMapper.batchUpdate(wmsOutPlanStockListOrderDetDtos);

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
                String coreSourceSysOrderTypeCode=listOrderDetDtos.get(0).getCoreSourceSysOrderTypeCode();
                if(StringUtils.isEmpty(coreSourceSysOrderTypeCode))
                    coreSourceSysOrderTypeCode="OUT-PSLO";
                WmsOutPlanDeliveryOrderDto planDeliveryOrderDto=new WmsOutPlanDeliveryOrderDto();
                List<WmsOutPlanDeliveryOrderDetDto> deliveryOrderDetDtos=new ArrayList<>();

                for (WmsOutPlanStockListOrderDetDto listOrderDetDto : listOrderDetDtos) {

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
                Long inStorageId=null;
                SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
                searchBaseStorage.setWarehouseId(warehouseId);
                searchBaseStorage.setStorageType((byte)3);//库位类型（1-存货 2-收货 3-发货）
                List<BaseStorage> baseStorages = baseFeignApi.findList(searchBaseStorage).getData();
                if(baseStorages.size()>0)
                    inStorageId=baseStorages.get(0).getStorageId();

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
                    wmsInnerJobOrderDet.setInStorageId(inStorageId);
                    wmsInnerJobOrderDets.add(wmsInnerJobOrderDet);
                }
                WmsInnerJobOrder wmsInnerJobOrder = new WmsInnerJobOrder();
                wmsInnerJobOrder.setSourceBigType((byte) 1);
                wmsInnerJobOrder.setCoreSourceSysOrderTypeCode(StringUtils.isEmpty(coreSourceSysOrderTypeCode)?"OUT-PSLO":coreSourceSysOrderTypeCode);
                wmsInnerJobOrder.setSourceSysOrderTypeCode("OUT-PSLO");
                wmsInnerJobOrder.setWarehouseId(warehouseId);
                wmsInnerJobOrder.setJobOrderType((byte) 2);
                wmsInnerJobOrder.setWmsInPutawayOrderDets(wmsInnerJobOrderDets);
                ResponseEntity responseEntity = innerFeignApi.add(wmsInnerJobOrder);
                if (responseEntity.getCode() != 0) {
                    throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
                }
            }
        }


        return num;
    }

    @Override
    public int updateActualQty(Long planStockListOrderDetId, BigDecimal actualQty) {
        Example detExample = new Example(WmsOutPlanStockListOrderDet.class);
        Example example = new Example(WmsOutPlanStockListOrder.class);

        detExample.createCriteria().andEqualTo("planStockListOrderDetId",planStockListOrderDetId);
        List<WmsOutPlanStockListOrderDet> wmsOutPlanStockListOrderDets = wmsOutPlanStockListOrderDetMapper.selectByExample(detExample);
        if (StringUtils.isEmpty(wmsOutPlanStockListOrderDets)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"未找到备料计划明细数据");
        }
        WmsOutPlanStockListOrderDet wmsOutPlanStockListOrderDet = wmsOutPlanStockListOrderDets.get(0);
        BigDecimal qty = wmsOutPlanStockListOrderDet.getActualQty();
        qty = StringUtils.isNotEmpty(qty)?qty:new BigDecimal(0);
        wmsOutPlanStockListOrderDet.setActualQty(qty.add(actualQty));
        if (wmsOutPlanStockListOrderDet.getOrderQty().compareTo(wmsOutPlanStockListOrderDet.getActualQty()) == 0) {
            wmsOutPlanStockListOrderDet.setLineStatus((byte) 3);
        }else {
            wmsOutPlanStockListOrderDet.setLineStatus((byte) 2);
        }
        wmsOutPlanStockListOrderDetMapper.updateByPrimaryKeySelective(wmsOutPlanStockListOrderDet);

        detExample.clear();
        detExample.createCriteria().andEqualTo("planStockListOrderId",wmsOutPlanStockListOrderDet.getPlanStockListOrderId())
                .andNotEqualTo("lineStatus",3);
        wmsOutPlanStockListOrderDets = wmsOutPlanStockListOrderDetMapper.selectByExample(detExample);


        example.createCriteria().andEqualTo("planStockListOrderId",wmsOutPlanStockListOrderDet.getPlanStockListOrderId());
        List<WmsOutPlanStockListOrder> wmsOutPlanStockListOrders = wmsOutPlanStockListOrderMapper.selectByExample(example);

        if (StringUtils.isNotEmpty(wmsOutPlanStockListOrders)) {
            WmsOutPlanStockListOrder wmsOutPlanStockListOrder = wmsOutPlanStockListOrders.get(0);
            if (StringUtils.isEmpty(wmsOutPlanStockListOrderDets)) {
                wmsOutPlanStockListOrder.setOrderStatus((byte) 3);
            }else {
                wmsOutPlanStockListOrder.setOrderStatus((byte) 2);
            }
            return wmsOutPlanStockListOrderMapper.updateByPrimaryKeySelective(wmsOutPlanStockListOrder);
        }else {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"未找到备料计划数据");
        }
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


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<WmsOutPlanStockListOrderImport> wmsOutPlanStockListOrderImports) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<WmsOutPlanStockListOrder> list = new LinkedList<>();
        LinkedList<WmsOutHtPlanStockListOrder> htList = new LinkedList<>();
        LinkedList<WmsOutPlanStockListOrderImport> planStockListOrderImports = new LinkedList<>();
        //日志记录
        StringBuilder succeedInfo = new StringBuilder();
        StringBuilder failInfo = new StringBuilder();
        Integer succeedCount = 0;
        Integer failCount = 0;

        for (int i = 0; i < wmsOutPlanStockListOrderImports.size(); i++) {
            WmsOutPlanStockListOrderImport wmsOutPlanStockListOrderImport = wmsOutPlanStockListOrderImports.get(i);
            String groupNum = wmsOutPlanStockListOrderImport.getGroupNum();

            if (StringUtils.isEmpty(
                    groupNum
            )) {
                failCount++;
                failInfo.append("必填项为空").append(",");
                fail.add(i + 4);
                continue;
            }

            //物料
            String materialCode = wmsOutPlanStockListOrderImport.getMaterialCode();
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
                wmsOutPlanStockListOrderImport.setMaterialId(baseMaterials.get(0).getMaterialId());
            }

            //仓库
            String warehouseCode = wmsOutPlanStockListOrderImport.getWarehouseCode();
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
                wmsOutPlanStockListOrderImport.setWarehouseId(baseWarehouses.get(0).getWarehouseId());
            }

            succeedCount++;
            succeedInfo.append(i+4).append(",");
            planStockListOrderImports.add(wmsOutPlanStockListOrderImport);
        }

        SysImportAndExportLog sysImportAndExportLog = new SysImportAndExportLog();
        sysImportAndExportLog.setModuleNames("WMS-OUT");
        sysImportAndExportLog.setFileName("备料计划导入信息表");
        sysImportAndExportLog.setType((byte)1);
        sysImportAndExportLog.setOperatorUserId(user.getUserId());
        sysImportAndExportLog.setResult((byte)1);
        sysImportAndExportLog.setTotalCount(wmsOutPlanStockListOrderImports.size());
        sysImportAndExportLog.setFailCount(failCount);
        sysImportAndExportLog.setSucceedCount(succeedCount);
        sysImportAndExportLog.setFailInfo(failInfo.toString());
        sysImportAndExportLog.setSucceedInfo(succeedInfo.toString());
        securityFeignApi.add(sysImportAndExportLog);

        if(StringUtils.isNotEmpty(planStockListOrderImports)){
            //对合格数据进行分组
            HashMap<String, List<WmsOutPlanStockListOrderImport>> map = planStockListOrderImports.stream().collect(Collectors.groupingBy(WmsOutPlanStockListOrderImport::getGroupNum, HashMap::new, Collectors.toList()));
            Set<String> codeList = map.keySet();
            for (String code : codeList) {
                List<WmsOutPlanStockListOrderImport> wmsOutPlanStockListOrderImports1 = map.get(code);
                WmsOutPlanStockListOrder wmsOutPlanStockListOrder = new WmsOutPlanStockListOrder();
                //新增父级数据
                BeanUtils.copyProperties(wmsOutPlanStockListOrderImports1.get(0), wmsOutPlanStockListOrder);
                wmsOutPlanStockListOrder.setPlanStockListOrderCode(CodeUtils.getId("PSLO-"));
                wmsOutPlanStockListOrder.setSourceBigType((byte)2);
                wmsOutPlanStockListOrder.setSysOrderTypeCode("OUT-PSLO");
                wmsOutPlanStockListOrder.setSourceSysOrderTypeCode("MES-WO");
                wmsOutPlanStockListOrder.setCreateTime(new Date());
                wmsOutPlanStockListOrder.setCreateUserId(user.getUserId());
                wmsOutPlanStockListOrder.setModifiedUserId(user.getUserId());
                wmsOutPlanStockListOrder.setModifiedTime(new Date());
                wmsOutPlanStockListOrder.setOrgId(user.getOrganizationId());
                wmsOutPlanStockListOrder.setStatus((byte)1);
                wmsOutPlanStockListOrder.setOrderStatus((byte)1);
                success += wmsOutPlanStockListOrderMapper.insertUseGeneratedKeys(wmsOutPlanStockListOrder);

                //履历
                WmsOutHtPlanStockListOrder wmsOutHtPlanStockListOrder = new WmsOutHtPlanStockListOrder();
                BeanUtils.copyProperties(wmsOutPlanStockListOrder, wmsOutHtPlanStockListOrder);
                htList.add(wmsOutHtPlanStockListOrder);

                //新增明细数据
                LinkedList<WmsOutPlanStockListOrderDet> detList = new LinkedList<>();
                for (WmsOutPlanStockListOrderImport wmsOutPlanStockListOrderImport : wmsOutPlanStockListOrderImports1) {
                    WmsOutPlanStockListOrderDet wmsOutPlanStockListOrderDet = new WmsOutPlanStockListOrderDet();
                    BeanUtils.copyProperties(wmsOutPlanStockListOrderImport, wmsOutPlanStockListOrderDet);
                    wmsOutPlanStockListOrderDet.setPlanStockListOrderId(wmsOutPlanStockListOrder.getPlanStockListOrderId());
                    wmsOutPlanStockListOrderDet.setStatus((byte) 1);
                    detList.add(wmsOutPlanStockListOrderDet);
                }
                wmsOutPlanStockListOrderDetMapper.insertList(detList);
            }
            wmsOutHtPlanStockListOrderMapper.insertList(htList);
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
