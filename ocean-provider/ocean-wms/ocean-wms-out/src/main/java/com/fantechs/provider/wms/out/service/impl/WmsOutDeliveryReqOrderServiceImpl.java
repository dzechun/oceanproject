package com.fantechs.provider.wms.out.service.impl;

import cn.hutool.core.date.DateTime;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysImportAndExportLog;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrderFlowDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryReqOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryReqOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanDeliveryOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanDeliveryOrderDto;
import com.fantechs.common.base.general.dto.wms.out.imports.WmsOutDeliveryReqOrderImport;
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
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryReqOrder;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryReqOrderDet;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtDeliveryReqOrder;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtDeliveryReqOrderDet;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.wms.out.mapper.WmsOutDeliveryReqOrderDetMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutDeliveryReqOrderMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutHtDeliveryReqOrderDetMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutHtDeliveryReqOrderMapper;
import com.fantechs.provider.wms.out.service.WmsOutDeliveryReqOrderService;
import com.fantechs.provider.wms.out.service.WmsOutPlanDeliveryOrderService;
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
 *
 * Created by leifengzhi on 2021/12/21.
 */
@Service
public class WmsOutDeliveryReqOrderServiceImpl extends BaseService<WmsOutDeliveryReqOrder> implements WmsOutDeliveryReqOrderService {

    @Resource
    private WmsOutDeliveryReqOrderMapper wmsOutDeliveryReqOrderMapper;
    @Resource
    private WmsOutHtDeliveryReqOrderMapper wmsOutHtDeliveryReqOrderMapper;
    @Resource
    private WmsOutDeliveryReqOrderDetMapper wmsOutDeliveryReqOrderDetMapper;
    @Resource
    private WmsOutHtDeliveryReqOrderDetMapper wmsOutHtDeliveryReqOrderDetMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private InnerFeignApi innerFeignApi;
    @Resource
    private WmsOutPlanDeliveryOrderService wmsOutPlanDeliveryOrderService;

    @Override
    public List<WmsOutDeliveryReqOrderDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return wmsOutDeliveryReqOrderMapper.findList(map);
    }

    @Override
    public List<WmsOutHtDeliveryReqOrder> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return wmsOutHtDeliveryReqOrderMapper.findHtList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updatePutawayQty(Long deliveryReqOrderDetId, BigDecimal putawayQty) {
        int i = 0;
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        WmsOutDeliveryReqOrderDet wmsOutDeliveryReqOrderDet = wmsOutDeliveryReqOrderDetMapper.selectByPrimaryKey(deliveryReqOrderDetId);
        if(StringUtils.isEmpty(wmsOutDeliveryReqOrderDet)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003, "找不到明细信息");
        }
        BigDecimal actualQty = StringUtils.isNotEmpty(wmsOutDeliveryReqOrderDet.getActualQty()) ? wmsOutDeliveryReqOrderDet.getActualQty().add(putawayQty) : putawayQty;
        wmsOutDeliveryReqOrderDet.setActualQty(actualQty);
        wmsOutDeliveryReqOrderDet.setDeliveryUserId(user.getUserId());
        if (actualQty.compareTo(BigDecimal.ZERO) == 1 && actualQty.compareTo(wmsOutDeliveryReqOrderDet.getTotalIssueQty()) == -1) {
            wmsOutDeliveryReqOrderDet.setLineStatus((byte) 2);
        } else if (actualQty.compareTo(wmsOutDeliveryReqOrderDet.getTotalIssueQty()) == 0) {
            wmsOutDeliveryReqOrderDet.setLineStatus((byte) 3);
        }
        i = wmsOutDeliveryReqOrderDetMapper.updateByPrimaryKeySelective(wmsOutDeliveryReqOrderDet);

        WmsOutDeliveryReqOrder wmsOutDeliveryReqOrder = wmsOutDeliveryReqOrderMapper.selectByPrimaryKey(wmsOutDeliveryReqOrderDet.getDeliveryReqOrderId());
        Example example = new Example(WmsOutDeliveryReqOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("deliveryReqOrderId", wmsOutDeliveryReqOrderDet.getDeliveryReqOrderId());
        List<WmsOutDeliveryReqOrderDet> wmsOutDeliveryReqOrderDets = wmsOutDeliveryReqOrderDetMapper.selectByExample(example);
        Byte orderStatus = 3;
        for (WmsOutDeliveryReqOrderDet deliveryReqOrderDet : wmsOutDeliveryReqOrderDets){
            if(deliveryReqOrderDet.getLineStatus()!=(byte)3){
                orderStatus = 2;
                break;
            }
        }
        wmsOutDeliveryReqOrder.setOrderStatus(orderStatus);
        i += wmsOutDeliveryReqOrderMapper.updateByPrimaryKeySelective(wmsOutDeliveryReqOrder);

        //如果出库通知单的上游单据是出库计划，也需返写
        if("OUT-PDO".equals(wmsOutDeliveryReqOrder.getSourceSysOrderTypeCode())){
            wmsOutPlanDeliveryOrderService.updatePutawayQty(wmsOutDeliveryReqOrderDet.getSourceId(),putawayQty);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int pushDown(List<WmsOutDeliveryReqOrderDetDto> wmsOutDeliveryReqOrderDetDtos) {
        int i = 0;
        Long warehouseId = wmsOutDeliveryReqOrderDetDtos.get(0).getWarehouseId();
        for (WmsOutDeliveryReqOrderDetDto wmsOutDeliveryReqOrderDetDto : wmsOutDeliveryReqOrderDetDtos){
            if(!warehouseId.equals(wmsOutDeliveryReqOrderDetDto.getWarehouseId())){
                throw new BizErrorException("所选数据的仓库需一致");
            }
            if(wmsOutDeliveryReqOrderDetDto.getIfAllIssued()!=null&&wmsOutDeliveryReqOrderDetDto.getIfAllIssued()==(byte)1){
                throw new BizErrorException("物料"+wmsOutDeliveryReqOrderDetDto.getMaterialCode()+"已下推，不能重复下推");
            }
            wmsOutDeliveryReqOrderDetDto.setTotalIssueQty(wmsOutDeliveryReqOrderDetDto.getOrderQty());
            wmsOutDeliveryReqOrderDetDto.setIfAllIssued((byte)1);
            i += wmsOutDeliveryReqOrderDetMapper.updateByPrimaryKeySelective(wmsOutDeliveryReqOrderDetDto);
        }
        //i = wmsOutDeliveryReqOrderDetMapper.batchUpdate(wmsOutDeliveryReqOrderDetDtos);

        //查当前单据类型的所有单据流
        SearchBaseOrderFlow searchBaseOrderFlow = new SearchBaseOrderFlow();
        searchBaseOrderFlow.setOrderTypeCode("OUT-DRO");
        searchBaseOrderFlow.setStatus((byte)1);
        List<BaseOrderFlowDto> baseOrderFlowDtos = baseFeignApi.findAll(searchBaseOrderFlow).getData();
        if (StringUtils.isEmpty(baseOrderFlowDtos)) {
            throw new BizErrorException("未找到当前单据配置的单据流");
        }

        //不同单据流分组
        Map<String, List<WmsOutDeliveryReqOrderDetDto>> map = new HashMap<>();
        for (WmsOutDeliveryReqOrderDetDto wmsOutDeliveryReqOrderDetDto : wmsOutDeliveryReqOrderDetDtos) {
            //查当前单据的下游单据
            BaseOrderFlow baseOrderFlow = OrderFlowUtil.getOrderFlow(baseOrderFlowDtos, wmsOutDeliveryReqOrderDetDto.getMaterialId(), null);

            String key = baseOrderFlow.getNextOrderTypeCode();
            if (map.get(key) == null) {
                List<WmsOutDeliveryReqOrderDetDto> diffOrderFlows = new LinkedList<>();
                diffOrderFlows.add(wmsOutDeliveryReqOrderDetDto);
                map.put(key, diffOrderFlows);
            } else {
                List<WmsOutDeliveryReqOrderDetDto> diffOrderFlows = map.get(key);
                diffOrderFlows.add(wmsOutDeliveryReqOrderDetDto);
                map.put(key, diffOrderFlows);
            }
        }

        Set<String> codes = map.keySet();
        for (String code : codes) {
            List<WmsOutDeliveryReqOrderDetDto> deliveryReqOrderDetDtos = map.get(code);
            if ("OUT-PDO".equals(code)) {
                //出库计划
                String coreSourceSysOrderTypeCode = deliveryReqOrderDetDtos.get(0).getCoreSourceSysOrderTypeCode();

                List<WmsOutPlanDeliveryOrderDetDto> wmsOutPlanDeliveryOrderDetDtos = new LinkedList<>();
                for (WmsOutDeliveryReqOrderDetDto wmsOutDeliveryReqOrderDetDto : deliveryReqOrderDetDtos) {
                    WmsOutPlanDeliveryOrderDetDto wmsOutPlanDeliveryOrderDetDto = new WmsOutPlanDeliveryOrderDetDto();
                    wmsOutPlanDeliveryOrderDetDto.setCoreSourceOrderCode(wmsOutDeliveryReqOrderDetDto.getCoreSourceOrderCode());
                    wmsOutPlanDeliveryOrderDetDto.setSourceOrderCode(wmsOutDeliveryReqOrderDetDto.getDeliveryReqOrderCode());
                    wmsOutPlanDeliveryOrderDetDto.setCoreSourceId(wmsOutDeliveryReqOrderDetDto.getCoreSourceId());
                    wmsOutPlanDeliveryOrderDetDto.setSourceId(wmsOutDeliveryReqOrderDetDto.getDeliveryReqOrderDetId());
                    wmsOutPlanDeliveryOrderDetDto.setMaterialId(wmsOutDeliveryReqOrderDetDto.getMaterialId());
                    wmsOutPlanDeliveryOrderDetDto.setOrderQty(wmsOutDeliveryReqOrderDetDto.getOrderQty());
                    wmsOutPlanDeliveryOrderDetDto.setLineStatus((byte) 1);
                    wmsOutPlanDeliveryOrderDetDtos.add(wmsOutPlanDeliveryOrderDetDto);
                }
                WmsOutPlanDeliveryOrderDto wmsOutPlanDeliveryOrderDto = new WmsOutPlanDeliveryOrderDto();
                wmsOutPlanDeliveryOrderDto.setSourceBigType((byte) 1);
                wmsOutPlanDeliveryOrderDto.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                wmsOutPlanDeliveryOrderDto.setSourceSysOrderTypeCode("OUT-DRO");
                wmsOutPlanDeliveryOrderDto.setSourceBigType((byte)1);
                wmsOutPlanDeliveryOrderDto.setWarehouseId(warehouseId);
                wmsOutPlanDeliveryOrderDto.setWmsOutPlanDeliveryOrderDetDtos(wmsOutPlanDeliveryOrderDetDtos);
                wmsOutPlanDeliveryOrderService.save(wmsOutPlanDeliveryOrderDto);
                i++;
            } else if ("OUT-IWK".equals(code)) {
                //拣货作业
                String coreSourceSysOrderTypeCode = deliveryReqOrderDetDtos.get(0).getCoreSourceSysOrderTypeCode();
                //查询发货库位
                SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
                searchBaseStorage.setWarehouseId(warehouseId);
                searchBaseStorage.setStorageType((byte)3);
                List<BaseStorage> baseStorages = baseFeignApi.findList(searchBaseStorage).getData();
                if(StringUtils.isEmpty(baseStorages)){
                    throw new BizErrorException("该仓库未找到发货库位");
                }
                Long inStorageId = baseStorages.get(0).getStorageId();

                int lineNumber = 1;
                List<WmsInnerJobOrderDet> wmsInnerJobOrderDets = new LinkedList<>();
                for (WmsOutDeliveryReqOrderDetDto wmsOutDeliveryReqOrderDetDto : deliveryReqOrderDetDtos) {
                    WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
                    wmsInnerJobOrderDet.setCoreSourceOrderCode(wmsOutDeliveryReqOrderDetDto.getCoreSourceOrderCode());
                    wmsInnerJobOrderDet.setSourceOrderCode(wmsOutDeliveryReqOrderDetDto.getDeliveryReqOrderCode());
                    wmsInnerJobOrderDet.setCoreSourceId(wmsOutDeliveryReqOrderDetDto.getCoreSourceId());
                    wmsInnerJobOrderDet.setSourceId(wmsOutDeliveryReqOrderDetDto.getDeliveryReqOrderDetId());
                    wmsInnerJobOrderDet.setLineNumber(lineNumber + "");
                    lineNumber++;
                    wmsInnerJobOrderDet.setMaterialId(wmsOutDeliveryReqOrderDetDto.getMaterialId());
                    wmsInnerJobOrderDet.setPlanQty(wmsOutDeliveryReqOrderDetDto.getOrderQty());
                    wmsInnerJobOrderDet.setLineStatus((byte) 1);
                    wmsInnerJobOrderDet.setInStorageId(inStorageId);
                    wmsInnerJobOrderDets.add(wmsInnerJobOrderDet);
                }
                WmsInnerJobOrder wmsInnerJobOrder = new WmsInnerJobOrder();
                wmsInnerJobOrder.setSourceBigType((byte) 1);
                wmsInnerJobOrder.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                wmsInnerJobOrder.setSourceSysOrderTypeCode("OUT-DRO");
                wmsInnerJobOrder.setWarehouseId(warehouseId);
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

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(WmsOutDeliveryReqOrderDto record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        record.setDeliveryReqOrderCode(CodeUtils.getId("OUT-DRO"));
        record.setCoreSourceSysOrderTypeCode(record.getSourceSysOrderTypeCode());
        record.setOrderStatus((byte)1);
        record.setOrgId(user.getOrganizationId());
        record.setCreateTime(new DateTime());
        record.setCreateUserId(user.getUserId());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new DateTime());
        int i = wmsOutDeliveryReqOrderMapper.insertUseGeneratedKeys(record);

        //明细
        List<WmsOutHtDeliveryReqOrderDet> htList = new LinkedList<>();
        List<WmsOutDeliveryReqOrderDetDto> wmsOutDeliveryReqOrderDetDtos = record.getWmsOutDeliveryReqOrderDetDtos();
        if(StringUtils.isNotEmpty(wmsOutDeliveryReqOrderDetDtos)){
            for (WmsOutDeliveryReqOrderDetDto wmsOutDeliveryReqOrderDetDto:wmsOutDeliveryReqOrderDetDtos){
                wmsOutDeliveryReqOrderDetDto.setDeliveryReqOrderId(record.getDeliveryReqOrderId());
                wmsOutDeliveryReqOrderDetDto.setCreateUserId(user.getUserId());
                wmsOutDeliveryReqOrderDetDto.setCreateTime(new Date());
                wmsOutDeliveryReqOrderDetDto.setModifiedUserId(user.getUserId());
                wmsOutDeliveryReqOrderDetDto.setModifiedTime(new Date());
                wmsOutDeliveryReqOrderDetDto.setOrgId(user.getOrganizationId());

                WmsOutHtDeliveryReqOrderDet wmsOutHtDeliveryReqOrderDet = new WmsOutHtDeliveryReqOrderDet();
                org.springframework.beans.BeanUtils.copyProperties(wmsOutDeliveryReqOrderDetDto, wmsOutHtDeliveryReqOrderDet);
                htList.add(wmsOutHtDeliveryReqOrderDet);
            }
            wmsOutDeliveryReqOrderDetMapper.insertList(wmsOutDeliveryReqOrderDetDtos);
            wmsOutHtDeliveryReqOrderDetMapper.insertList(htList);
        }

        //履历
        WmsOutHtDeliveryReqOrder wmsOutHtDeliveryReqOrder = new WmsOutHtDeliveryReqOrder();
        BeanUtils.copyProperties(record, wmsOutHtDeliveryReqOrder);
        wmsOutHtDeliveryReqOrderMapper.insertSelective(wmsOutHtDeliveryReqOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(WmsOutDeliveryReqOrderDto entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        int i = wmsOutDeliveryReqOrderMapper.updateByPrimaryKeySelective(entity);

        ArrayList<Long> idList = new ArrayList<>();
        List<WmsOutDeliveryReqOrderDetDto> wmsOutDeliveryReqOrderDetDtos = entity.getWmsOutDeliveryReqOrderDetDtos();
        if(StringUtils.isNotEmpty(wmsOutDeliveryReqOrderDetDtos)) {
            for (WmsOutDeliveryReqOrderDetDto wmsOutDeliveryReqOrderDetDto : wmsOutDeliveryReqOrderDetDtos) {
                if (StringUtils.isNotEmpty(wmsOutDeliveryReqOrderDetDto.getDeliveryReqOrderDetId())) {
                    wmsOutDeliveryReqOrderDetMapper.updateByPrimaryKeySelective(wmsOutDeliveryReqOrderDetDto);
                    idList.add(wmsOutDeliveryReqOrderDetDto.getDeliveryReqOrderDetId());
                }
            }
        }

        //删除原明细
        Example example = new Example(WmsOutDeliveryReqOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("deliveryReqOrderId",entity.getDeliveryReqOrderId());
        if (idList.size() > 0) {
            criteria.andNotIn("deliveryReqOrderDetId", idList);
        }
        wmsOutDeliveryReqOrderDetMapper.deleteByExample(example);

        //明细
        List<WmsOutHtDeliveryReqOrderDet> htList = new LinkedList<>();
        if(StringUtils.isNotEmpty(wmsOutDeliveryReqOrderDetDtos)){
            List<WmsOutDeliveryReqOrderDetDto> addDetList = new LinkedList<>();
            for (WmsOutDeliveryReqOrderDetDto wmsOutDeliveryReqOrderDetDto : wmsOutDeliveryReqOrderDetDtos){
                WmsOutHtDeliveryReqOrderDet wmsOutHtDeliveryReqOrderDet = new WmsOutHtDeliveryReqOrderDet();
                org.springframework.beans.BeanUtils.copyProperties(wmsOutDeliveryReqOrderDetDto, wmsOutHtDeliveryReqOrderDet);
                htList.add(wmsOutHtDeliveryReqOrderDet);

                if (idList.contains(wmsOutDeliveryReqOrderDetDto.getDeliveryReqOrderDetId())) {
                    continue;
                }
                wmsOutDeliveryReqOrderDetDto.setDeliveryReqOrderId(entity.getDeliveryReqOrderId());
                wmsOutDeliveryReqOrderDetDto.setCreateUserId(user.getUserId());
                wmsOutDeliveryReqOrderDetDto.setCreateTime(new Date());
                wmsOutDeliveryReqOrderDetDto.setModifiedUserId(user.getUserId());
                wmsOutDeliveryReqOrderDetDto.setModifiedTime(new Date());
                wmsOutDeliveryReqOrderDetDto.setOrgId(user.getOrganizationId());
                addDetList.add(wmsOutDeliveryReqOrderDetDto);
            }
            if(StringUtils.isNotEmpty(addDetList)) {
                wmsOutDeliveryReqOrderDetMapper.insertList(addDetList);
            }
            if(StringUtils.isNotEmpty(htList)) {
                wmsOutHtDeliveryReqOrderDetMapper.insertList(htList);
            }
        }

        //履历
        WmsOutHtDeliveryReqOrder wmsOutHtDeliveryReqOrder = new WmsOutHtDeliveryReqOrder();
        BeanUtils.copyProperties(entity, wmsOutHtDeliveryReqOrder);
        wmsOutHtDeliveryReqOrderMapper.insertSelective(wmsOutHtDeliveryReqOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        //表头履历
        List<WmsOutDeliveryReqOrder> wmsOutDeliveryReqOrders = wmsOutDeliveryReqOrderMapper.selectByIds(ids);
        List<WmsOutHtDeliveryReqOrder> htList = new LinkedList<>();
        if(StringUtils.isNotEmpty(wmsOutDeliveryReqOrders)) {
            for (WmsOutDeliveryReqOrder wmsOutDeliveryReqOrder : wmsOutDeliveryReqOrders) {
                WmsOutHtDeliveryReqOrder wmsOutHtDeliveryReqOrder = new WmsOutHtDeliveryReqOrder();
                org.springframework.beans.BeanUtils.copyProperties(wmsOutDeliveryReqOrder, wmsOutHtDeliveryReqOrder);
                htList.add(wmsOutHtDeliveryReqOrder);
            }
            wmsOutHtDeliveryReqOrderMapper.insertList(htList);
        }

        //表体履历
        List<String> idList = Arrays.asList(ids.split(","));
        Example example = new Example(WmsOutDeliveryReqOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("deliveryReqOrderId",idList);
        List<WmsOutDeliveryReqOrderDet> wmsOutDeliveryReqOrderDets = wmsOutDeliveryReqOrderDetMapper.selectByExample(example);
        List<WmsOutHtDeliveryReqOrderDet> htDetList = new LinkedList<>();
        if(StringUtils.isNotEmpty(wmsOutDeliveryReqOrderDets)) {
            for (WmsOutDeliveryReqOrderDet wmsOutDeliveryReqOrderDet : wmsOutDeliveryReqOrderDets) {
                WmsOutHtDeliveryReqOrderDet wmsOutHtDeliveryReqOrderDet = new WmsOutHtDeliveryReqOrderDet();
                org.springframework.beans.BeanUtils.copyProperties(wmsOutDeliveryReqOrderDet, wmsOutHtDeliveryReqOrderDet);
                htDetList.add(wmsOutHtDeliveryReqOrderDet);
            }
            wmsOutHtDeliveryReqOrderDetMapper.insertList(htDetList);
        }

        wmsOutDeliveryReqOrderDetMapper.deleteByExample(example);

        return wmsOutDeliveryReqOrderMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<WmsOutDeliveryReqOrderImport> wmsOutDeliveryReqOrderImports) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<WmsOutDeliveryReqOrder> list = new LinkedList<>();
        LinkedList<WmsOutHtDeliveryReqOrder> htList = new LinkedList<>();
        LinkedList<WmsOutDeliveryReqOrderImport> deliveryReqOrderImports = new LinkedList<>();
        //日志记录
        StringBuilder succeedInfo = new StringBuilder();
        StringBuilder failInfo = new StringBuilder();
        Integer succeedCount = 0;
        Integer failCount = 0;

        for (int i = 0; i < wmsOutDeliveryReqOrderImports.size(); i++) {
            WmsOutDeliveryReqOrderImport wmsOutDeliveryReqOrderImport = wmsOutDeliveryReqOrderImports.get(i);
            String groupNum = wmsOutDeliveryReqOrderImport.getGroupNum();

            if (StringUtils.isEmpty(
                    groupNum
            )) {
                failCount++;
                failInfo.append("必填项为空").append(",");
                fail.add(i + 4);
                continue;
            }

            //物料
            String materialCode = wmsOutDeliveryReqOrderImport.getMaterialCode();
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
                wmsOutDeliveryReqOrderImport.setMaterialId(baseMaterials.get(0).getMaterialId());
            }

            //仓库
            String warehouseCode = wmsOutDeliveryReqOrderImport.getWarehouseCode();
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
                wmsOutDeliveryReqOrderImport.setWarehouseId(baseWarehouses.get(0).getWarehouseId());
            }

            succeedCount++;
            succeedInfo.append(i+4).append(",");
            deliveryReqOrderImports.add(wmsOutDeliveryReqOrderImport);
        }

        SysImportAndExportLog sysImportAndExportLog = new SysImportAndExportLog();
        sysImportAndExportLog.setModuleNames("WMS-OUT");
        sysImportAndExportLog.setFileName("出库通知单导入信息表");
        sysImportAndExportLog.setType((byte)1);
        sysImportAndExportLog.setOperatorUserId(user.getUserId());
        sysImportAndExportLog.setResult((byte)1);
        sysImportAndExportLog.setTotalCount(wmsOutDeliveryReqOrderImports.size());
        sysImportAndExportLog.setFailCount(failCount);
        sysImportAndExportLog.setSucceedCount(succeedCount);
        sysImportAndExportLog.setFailInfo(failInfo.toString());
        sysImportAndExportLog.setSucceedInfo(succeedInfo.toString());
        securityFeignApi.add(sysImportAndExportLog);

        if(StringUtils.isNotEmpty(deliveryReqOrderImports)){
            //对合格数据进行分组
            HashMap<String, List<WmsOutDeliveryReqOrderImport>> map = deliveryReqOrderImports.stream().collect(Collectors.groupingBy(WmsOutDeliveryReqOrderImport::getGroupNum, HashMap::new, Collectors.toList()));
            Set<String> codeList = map.keySet();
            for (String code : codeList) {
                List<WmsOutDeliveryReqOrderImport> wmsOutDeliveryReqOrderImports1 = map.get(code);
                WmsOutDeliveryReqOrder wmsOutDeliveryReqOrder = new WmsOutDeliveryReqOrder();
                //新增父级数据
                BeanUtils.copyProperties(wmsOutDeliveryReqOrderImports1.get(0), wmsOutDeliveryReqOrder);
                wmsOutDeliveryReqOrder.setDeliveryReqOrderCode(CodeUtils.getId("OUT-DRO"));
                wmsOutDeliveryReqOrder.setCreateTime(new Date());
                wmsOutDeliveryReqOrder.setCreateUserId(user.getUserId());
                wmsOutDeliveryReqOrder.setModifiedUserId(user.getUserId());
                wmsOutDeliveryReqOrder.setModifiedTime(new Date());
                wmsOutDeliveryReqOrder.setOrgId(user.getOrganizationId());
                wmsOutDeliveryReqOrder.setStatus((byte)1);
                success += wmsOutDeliveryReqOrderMapper.insertUseGeneratedKeys(wmsOutDeliveryReqOrder);

                //履历
                WmsOutHtDeliveryReqOrder wmsOutHtDeliveryReqOrder = new WmsOutHtDeliveryReqOrder();
                BeanUtils.copyProperties(wmsOutDeliveryReqOrder, wmsOutHtDeliveryReqOrder);
                htList.add(wmsOutHtDeliveryReqOrder);

                //新增明细数据
                LinkedList<WmsOutDeliveryReqOrderDet> detList = new LinkedList<>();
                for (WmsOutDeliveryReqOrderImport wmsOutDeliveryReqOrderImport : wmsOutDeliveryReqOrderImports1) {
                    WmsOutDeliveryReqOrderDet wmsOutDeliveryReqOrderDet = new WmsOutDeliveryReqOrderDet();
                    BeanUtils.copyProperties(wmsOutDeliveryReqOrderImport, wmsOutDeliveryReqOrderDet);
                    wmsOutDeliveryReqOrderDet.setDeliveryReqOrderId(wmsOutDeliveryReqOrder.getDeliveryReqOrderId());
                    wmsOutDeliveryReqOrderDet.setStatus((byte) 1);
                    detList.add(wmsOutDeliveryReqOrderDet);
                }
                wmsOutDeliveryReqOrderDetMapper.insertList(detList);
            }
            wmsOutHtDeliveryReqOrderMapper.insertList(htList);
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
