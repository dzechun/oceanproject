package com.fantechs.provider.om.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrderFlowDto;
import com.fantechs.common.base.general.dto.om.OmHtOtherOutOrderDto;
import com.fantechs.common.base.general.dto.om.OmOtherOutOrderDetDto;
import com.fantechs.common.base.general.dto.om.OmOtherOutOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryReqOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryReqOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanDeliveryOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanDeliveryOrderDto;
import com.fantechs.common.base.general.entity.basic.BaseOrderFlow;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrderFlow;
import com.fantechs.common.base.general.entity.om.OmHtOtherOutOrder;
import com.fantechs.common.base.general.entity.om.OmHtOtherOutOrderDet;
import com.fantechs.common.base.general.entity.om.OmOtherOutOrder;
import com.fantechs.common.base.general.entity.om.OmOtherOutOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.api.wms.out.OutFeignApi;
import com.fantechs.provider.om.mapper.OmOtherOutOrderDetMapper;
import com.fantechs.provider.om.mapper.OmOtherOutOrderMapper;
import com.fantechs.provider.om.mapper.ht.OmHtOtherOutOrderDetMapper;
import com.fantechs.provider.om.mapper.ht.OmHtOtherOutOrderMapper;
import com.fantechs.provider.om.service.OmOtherOutOrderService;
import com.fantechs.provider.om.util.OrderFlowUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by Mr.Lei on 2021/06/23.
 */
@Service
public class OmOtherOutOrderServiceImpl extends BaseService<OmOtherOutOrder> implements OmOtherOutOrderService {

    @Resource
    private OmOtherOutOrderMapper omOtherOutOrderMapper;
    @Resource
    private OmOtherOutOrderDetMapper omOtherOutOrderDetMapper;
    @Resource
    private OmHtOtherOutOrderMapper omHtOtherOutOrderMapper;
    @Resource
    private OmHtOtherOutOrderDetMapper omHtOtherOutOrderDetMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private InnerFeignApi innerFeignApi;
    @Resource
    private OutFeignApi outFeignApi;

    @Override
    public List<OmOtherOutOrderDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return omOtherOutOrderMapper.findList(map);
    }

    @Override
    public List<OmHtOtherOutOrderDto> findHtList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return omHtOtherOutOrderMapper.findHtList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int pushDown(List<OmOtherOutOrderDetDto> omOtherOutOrderDets) {
        int i;
        for (OmOtherOutOrderDetDto omOtherOutOrderDetDto : omOtherOutOrderDets){
            BigDecimal totalIssueQty = omOtherOutOrderDetDto.getTotalIssueQty() == null ? BigDecimal.ZERO : omOtherOutOrderDetDto.getTotalIssueQty();
            BigDecimal add = totalIssueQty.add(omOtherOutOrderDetDto.getIssueQty());
            if(add.compareTo(omOtherOutOrderDetDto.getOrderQty()) == 1){
                throw new BizErrorException("下发数量不能大于订单数量");
            }else if(add.compareTo(omOtherOutOrderDetDto.getOrderQty()) == 0){
                omOtherOutOrderDetDto.setIfAllIssued((byte)1);
            }
            omOtherOutOrderDetDto.setTotalIssueQty(add);
        }
        i = omOtherOutOrderDetMapper.batchUpdate(omOtherOutOrderDets);

        //查当前单据类型的所有单据流
        SearchBaseOrderFlow searchBaseOrderFlow = new SearchBaseOrderFlow();
        searchBaseOrderFlow.setOrderTypeCode("OUT-OOO");
        searchBaseOrderFlow.setStatus((byte)1);
        List<BaseOrderFlowDto> baseOrderFlowDtos = baseFeignApi.findAll(searchBaseOrderFlow).getData();
        if (StringUtils.isEmpty(baseOrderFlowDtos)) {
            throw new BizErrorException("未找到当前单据配置的单据流");
        }

        //按仓库分组，不同仓库生成多张单
        Map<String,List<OmOtherOutOrderDetDto>> map = new HashMap<>();
        HashMap<Long, List<OmOtherOutOrderDetDto>> collect = omOtherOutOrderDets.stream().collect(Collectors.groupingBy(OmOtherOutOrderDetDto::getWarehouseId, HashMap::new, Collectors.toList()));
        Set<Long> set = collect.keySet();
        for (Long id : set) {
            List<OmOtherOutOrderDetDto> omOtherOutOrderDetDtos = collect.get(id);

            //不同单据流分组
            for (OmOtherOutOrderDetDto omOtherOutOrderDetDto : omOtherOutOrderDetDtos){
                //查当前单据的下游单据
                BaseOrderFlow baseOrderFlow = OrderFlowUtil.getOrderFlow(baseOrderFlowDtos, omOtherOutOrderDetDto.getMaterialId(), null);

                String key = id+"_"+baseOrderFlow.getNextOrderTypeCode();
                if(map.get(key)==null){
                    List<OmOtherOutOrderDetDto> diffOrderFlows = new LinkedList<>();
                    diffOrderFlows.add(omOtherOutOrderDetDto);
                    map.put(key,diffOrderFlows);
                }else {
                    List<OmOtherOutOrderDetDto> diffOrderFlows = map.get(key);
                    diffOrderFlows.add(omOtherOutOrderDetDto);
                    map.put(key,diffOrderFlows);
                }
            }
        }

        Set<String> codes = map.keySet();
        for (String code : codes) {
            String[] split = code.split("_");
            String nextOrderTypeCode = split[1];//下游单据类型
            List<OmOtherOutOrderDetDto> omOtherOutOrderDetDtos = map.get(code);
            if ("OUT-DRO".equals(nextOrderTypeCode)) {
                //出库通知单
                List<WmsOutDeliveryReqOrderDetDto> wmsOutDeliveryReqOrderDetDtos = new LinkedList<>();
                for (OmOtherOutOrderDetDto omOtherOutOrderDetDto : omOtherOutOrderDetDtos) {
                    WmsOutDeliveryReqOrderDetDto wmsOutDeliveryReqOrderDetDto = new WmsOutDeliveryReqOrderDetDto();
                    wmsOutDeliveryReqOrderDetDto.setCoreSourceOrderCode(omOtherOutOrderDetDto.getCoreSourceOrderCode());
                    wmsOutDeliveryReqOrderDetDto.setSourceOrderCode(omOtherOutOrderDetDto.getOtherOutOrderCode());
                    wmsOutDeliveryReqOrderDetDto.setCoreSourceId(omOtherOutOrderDetDto.getCoreSourceId());
                    wmsOutDeliveryReqOrderDetDto.setSourceId(omOtherOutOrderDetDto.getOtherOutOrderDetId());
                    wmsOutDeliveryReqOrderDetDto.setMaterialId(omOtherOutOrderDetDto.getMaterialId());
                    wmsOutDeliveryReqOrderDetDto.setOrderQty(omOtherOutOrderDetDto.getIssueQty());
                    wmsOutDeliveryReqOrderDetDto.setLineStatus((byte) 1);
                    wmsOutDeliveryReqOrderDetDtos.add(wmsOutDeliveryReqOrderDetDto);
                }
                WmsOutDeliveryReqOrderDto wmsOutDeliveryReqOrderDto = new WmsOutDeliveryReqOrderDto();
                wmsOutDeliveryReqOrderDto.setSourceBigType((byte)1);
                wmsOutDeliveryReqOrderDto.setCoreSourceSysOrderTypeCode("OUT-OOO");
                wmsOutDeliveryReqOrderDto.setSourceSysOrderTypeCode("OUT-OOO");
                wmsOutDeliveryReqOrderDto.setSourceBigType((byte)1);
                wmsOutDeliveryReqOrderDto.setWarehouseId(omOtherOutOrderDetDtos.get(0).getWarehouseId());
                wmsOutDeliveryReqOrderDto.setWmsOutDeliveryReqOrderDetDtos(wmsOutDeliveryReqOrderDetDtos);
                ResponseEntity responseEntity = outFeignApi.add(wmsOutDeliveryReqOrderDto);
                if (responseEntity.getCode() != 0) {
                    throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
                } else {
                    i++;
                }
            } else if ("OUT-PDO".equals(nextOrderTypeCode)) {
                //出库计划
                List<WmsOutPlanDeliveryOrderDetDto> wmsOutPlanDeliveryOrderDetDtos = new LinkedList<>();
                for (OmOtherOutOrderDetDto omOtherOutOrderDetDto : omOtherOutOrderDetDtos) {
                    WmsOutPlanDeliveryOrderDetDto wmsOutPlanDeliveryOrderDetDto = new WmsOutPlanDeliveryOrderDetDto();
                    wmsOutPlanDeliveryOrderDetDto.setCoreSourceOrderCode(omOtherOutOrderDetDto.getCoreSourceOrderCode());
                    wmsOutPlanDeliveryOrderDetDto.setSourceOrderCode(omOtherOutOrderDetDto.getOtherOutOrderCode());
                    wmsOutPlanDeliveryOrderDetDto.setCoreSourceId(omOtherOutOrderDetDto.getCoreSourceId());
                    wmsOutPlanDeliveryOrderDetDto.setSourceId(omOtherOutOrderDetDto.getOtherOutOrderDetId());
                    wmsOutPlanDeliveryOrderDetDto.setMaterialId(omOtherOutOrderDetDto.getMaterialId());
                    wmsOutPlanDeliveryOrderDetDto.setOrderQty(omOtherOutOrderDetDto.getIssueQty());
                    wmsOutPlanDeliveryOrderDetDto.setLineStatus((byte) 1);
                    wmsOutPlanDeliveryOrderDetDtos.add(wmsOutPlanDeliveryOrderDetDto);
                }
                WmsOutPlanDeliveryOrderDto wmsOutPlanDeliveryOrderDto = new WmsOutPlanDeliveryOrderDto();
                wmsOutPlanDeliveryOrderDto.setSourceBigType((byte)1);
                wmsOutPlanDeliveryOrderDto.setCoreSourceSysOrderTypeCode("OUT-OOO");
                wmsOutPlanDeliveryOrderDto.setSourceSysOrderTypeCode("OUT-OOO");
                wmsOutPlanDeliveryOrderDto.setSourceBigType((byte)1);
                wmsOutPlanDeliveryOrderDto.setWarehouseId(omOtherOutOrderDetDtos.get(0).getWarehouseId());
                wmsOutPlanDeliveryOrderDto.setWmsOutPlanDeliveryOrderDetDtos(wmsOutPlanDeliveryOrderDetDtos);
                ResponseEntity responseEntity = outFeignApi.add(wmsOutPlanDeliveryOrderDto);
                if (responseEntity.getCode() != 0) {
                    throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
                } else {
                    i++;
                }
            } else if ("OUT-IWK".equals(nextOrderTypeCode)) {
                //拣货作业

                //查询存货库位
                /*SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
                searchBaseStorage.setWarehouseId(omOtherOutOrderDetDtos.get(0).getWarehouseId());
                searchBaseStorage.setStorageType((byte)1);
                List<BaseStorage> baseStorages = baseFeignApi.findList(searchBaseStorage).getData();
                if(StringUtils.isEmpty(baseStorages)){
                    throw new BizErrorException("该仓库未找到存货库位");
                }
                Long outStorageId = baseStorages.get(0).getStorageId();*/

                int lineNumber = 1;
                List<WmsInnerJobOrderDet> wmsInnerJobOrderDets = new LinkedList<>();
                for (OmOtherOutOrderDetDto omOtherOutOrderDetDto : omOtherOutOrderDetDtos) {
                    WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
                    wmsInnerJobOrderDet.setCoreSourceOrderCode(omOtherOutOrderDetDto.getCoreSourceOrderCode());
                    wmsInnerJobOrderDet.setSourceOrderCode(omOtherOutOrderDetDto.getOtherOutOrderCode());
                    wmsInnerJobOrderDet.setCoreSourceId(omOtherOutOrderDetDto.getCoreSourceId());
                    wmsInnerJobOrderDet.setSourceId(omOtherOutOrderDetDto.getOtherOutOrderDetId());
                    wmsInnerJobOrderDet.setLineNumber(lineNumber + "");
                    lineNumber++;
                    wmsInnerJobOrderDet.setMaterialId(omOtherOutOrderDetDto.getMaterialId());
                    wmsInnerJobOrderDet.setBatchCode(omOtherOutOrderDetDto.getBatchCode());
                    wmsInnerJobOrderDet.setPlanQty(omOtherOutOrderDetDto.getIssueQty());
                    wmsInnerJobOrderDet.setLineStatus((byte) 1);
                    //wmsInnerJobOrderDet.setOutStorageId(outStorageId);
                    wmsInnerJobOrderDets.add(wmsInnerJobOrderDet);
                }
                WmsInnerJobOrder wmsInnerJobOrder = new WmsInnerJobOrder();
                wmsInnerJobOrder.setSourceBigType((byte)1);
                wmsInnerJobOrder.setCoreSourceSysOrderTypeCode("OUT-OOO");
                wmsInnerJobOrder.setSourceSysOrderTypeCode("OUT-OOO");
                wmsInnerJobOrder.setWarehouseId(omOtherOutOrderDetDtos.get(0).getWarehouseId());
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
        OmOtherOutOrder omOtherOutOrder = omOtherOutOrderMapper.selectByPrimaryKey(omOtherOutOrderDets.get(0).getOtherOutOrderId());
        Example example = new Example(OmOtherOutOrderDet.class);
        example.createCriteria().andEqualTo("otherOutOrderId",omOtherOutOrder.getOtherOutOrderId());
        List<OmOtherOutOrderDet> otherOutOrderDets = omOtherOutOrderDetMapper.selectByExample(example);
        for (OmOtherOutOrderDet omOtherOutOrderDet : otherOutOrderDets){
            if(omOtherOutOrderDet.getIfAllIssued()!=(byte)1){
                orderStatus = (byte)2;
                break;
            }
        }
        omOtherOutOrder.setOrderStatus(orderStatus);
        omOtherOutOrderMapper.updateByPrimaryKeySelective(omOtherOutOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(OmOtherOutOrder record) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();

        record.setOtherOutOrderCode(CodeUtils.getId("OUT-OOO"));
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrgId(sysUser.getOrganizationId());
        record.setOrderStatus((byte)1);
        int num = omOtherOutOrderMapper.insertUseGeneratedKeys(record);
        List<OmOtherOutOrderDetDto> omOtherOutOrderDets = record.getOmOtherOutOrderDets();
        if(StringUtils.isNotEmpty(omOtherOutOrderDets)){
            for (OmOtherOutOrderDet omOtherOutOrderDet : omOtherOutOrderDets) {
                omOtherOutOrderDet.setOtherOutOrderId(record.getOtherOutOrderId());
                omOtherOutOrderDet.setCreateTime(new Date());
                omOtherOutOrderDet.setCreateUserId(sysUser.getUserId());
                omOtherOutOrderDet.setModifiedTime(new Date());
                omOtherOutOrderDet.setModifiedUserId(sysUser.getUserId());
                omOtherOutOrderDet.setOrgId(sysUser.getOrganizationId());
            }
            num+=omOtherOutOrderDetMapper.insertList(omOtherOutOrderDets);
        }

        this.addHt(record,omOtherOutOrderDets);
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(OmOtherOutOrder entity) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(sysUser.getUserId());
        int num = omOtherOutOrderMapper.updateByPrimaryKeySelective(entity);

        //原来有的明细只更新
        ArrayList<Long> idList = new ArrayList<>();
        List<OmOtherOutOrderDetDto> omOtherOutOrderDets = entity.getOmOtherOutOrderDets();
        if(StringUtils.isNotEmpty(omOtherOutOrderDets)) {
            for (OmOtherOutOrderDetDto omOtherOutOrderDetDto : omOtherOutOrderDets) {
                if (StringUtils.isNotEmpty(omOtherOutOrderDetDto.getOtherOutOrderDetId())) {
                    omOtherOutOrderDetMapper.updateByPrimaryKeySelective(omOtherOutOrderDetDto);
                    idList.add(omOtherOutOrderDetDto.getOtherOutOrderDetId());
                }
            }
        }

        //删除原有明细
        Example example = new Example(OmOtherOutOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("otherOutOrderId",entity.getOtherOutOrderId());
        if (idList.size() > 0) {
            criteria.andNotIn("otherOutOrderDetId", idList);
        }
        omOtherOutOrderDetMapper.deleteByExample(example);

        if(StringUtils.isNotEmpty(omOtherOutOrderDets)) {
            List<OmOtherOutOrderDet> addDetList = new LinkedList<>();
            for (OmOtherOutOrderDet omOtherOutOrderDet : omOtherOutOrderDets) {
                if (idList.contains(omOtherOutOrderDet.getOtherOutOrderDetId())) {
                    continue;
                }
                omOtherOutOrderDet.setOtherOutOrderId(entity.getOtherOutOrderId());
                omOtherOutOrderDet.setCreateTime(new Date());
                omOtherOutOrderDet.setCreateUserId(sysUser.getUserId());
                omOtherOutOrderDet.setModifiedTime(new Date());
                omOtherOutOrderDet.setModifiedUserId(sysUser.getUserId());
                omOtherOutOrderDet.setOrgId(sysUser.getOrganizationId());
                addDetList.add(omOtherOutOrderDet);
            }
            if(StringUtils.isNotEmpty(addDetList)) {
                num += omOtherOutOrderDetMapper.insertList(addDetList);
            }
        }

        this.addHt(entity,omOtherOutOrderDets);
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        String[] arrId = ids.split(",");
        for (String id : arrId) {
            OmOtherOutOrder omOtherOutOrder = omOtherOutOrderMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(omOtherOutOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012000,id);
            }

            Example example = new Example(OmOtherOutOrderDet.class);
            example.createCriteria().andEqualTo("otherOutOrderId",omOtherOutOrder.getOtherOutOrderId());
            omOtherOutOrderDetMapper.deleteByExample(example);
            this.addHt(omOtherOutOrder,null);
        }
        return omOtherOutOrderMapper.deleteByIds(ids);
    }


    /**
     * 添加历史记录
     * @return
     */
    private int addHt(OmOtherOutOrder omOtherOutOrder,List<OmOtherOutOrderDetDto> omOtherOutOrderDets){
        int num = 0;
        if(StringUtils.isNotEmpty(omOtherOutOrder)){
            OmHtOtherOutOrder omHtOtherOutOrder = new OmHtOtherOutOrder();
            BeanUtil.copyProperties(omOtherOutOrder,omHtOtherOutOrder);
            num+=omHtOtherOutOrderMapper.insertSelective(omHtOtherOutOrder);
        }
        if(StringUtils.isNotEmpty(omOtherOutOrderDets)){
            for (OmOtherOutOrderDet omOtherOutOrderDet : omOtherOutOrderDets) {
                OmHtOtherOutOrderDet omHtOtherOutOrderDet = new OmHtOtherOutOrderDet();
                BeanUtil.copyProperties(omOtherOutOrderDet,omHtOtherOutOrderDet);
                num+=omHtOtherOutOrderDetMapper.insertSelective(omHtOtherOutOrderDet);
            }
        }
        return num;
    }
}
