package com.fantechs.provider.om.service.impl;


import cn.hutool.core.date.DateTime;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseMaterialOwnerDto;
import com.fantechs.common.base.general.dto.om.OmHtSalesOrderDetDto;
import com.fantechs.common.base.general.dto.om.OmHtSalesOrderDto;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDetDto;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDetDto;
import com.fantechs.common.base.general.entity.basic.BaseOrderFlow;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterialOwner;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrderFlow;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.general.entity.om.OmHtSalesOrderDet;
import com.fantechs.common.base.general.entity.om.OmSalesOrder;
import com.fantechs.common.base.general.entity.om.OmSalesOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.api.wms.out.OutFeignApi;
import com.fantechs.provider.om.mapper.OmSalesOrderDetMapper;
import com.fantechs.provider.om.mapper.OmSalesOrderMapper;
import com.fantechs.provider.om.mapper.ht.OmHtSalesOrderDetMapper;
import com.fantechs.provider.om.mapper.ht.OmHtSalesOrderMapper;
import com.fantechs.provider.om.service.OmSalesOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2021/04/19.
 */
@Service
@Transactional
@Slf4j
public class OmSalesOrderServiceImpl extends BaseService<OmSalesOrder> implements OmSalesOrderService {

    @Resource
    private OmSalesOrderMapper omSalesOrderMapper;
    @Resource
    private OmSalesOrderDetMapper omSalesOrderDetMapper;
    @Resource
    private OmHtSalesOrderMapper omHtSalesOrderMapper;
    @Resource
    private OmHtSalesOrderDetMapper omHtSalesOrderDetMapper;
    @Resource
    private OutFeignApi outFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private InnerFeignApi innerFeignApi;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int pushDown(List<OmSalesOrderDetDto> omSalesOrderDetDtoList) {
        int i = 0;
        for (OmSalesOrderDetDto omSalesOrderDetDto : omSalesOrderDetDtoList){
            BigDecimal add = omSalesOrderDetDto.getTotalIssueQty().add(omSalesOrderDetDto.getIssueQty());
            if(add.compareTo(omSalesOrderDetDto.getOrderQty()) == 1){
                throw new BizErrorException("下发数量不能大于订单数量");
            }else if(add.compareTo(omSalesOrderDetDto.getOrderQty()) == 0){
                omSalesOrderDetDto.setIfAllIssued((byte)1);
            }
            omSalesOrderDetDto.setTotalIssueQty(add);
        }
        i = omSalesOrderDetMapper.batchUpdate(omSalesOrderDetDtoList);

        //查当前单据的下游单据
        SearchBaseOrderFlow searchBaseOrderFlow = new SearchBaseOrderFlow();
        searchBaseOrderFlow.setOrderTypeCode("OUT-SO");
        BaseOrderFlow baseOrderFlow = baseFeignApi.findOrderFlow(searchBaseOrderFlow).getData();
        if(StringUtils.isEmpty(baseOrderFlow)){
            throw new BizErrorException("未找到当前单据配置的下游单据");
        }

        //按仓库分组，不同仓库生成多张单
        HashMap<Long, List<OmSalesOrderDetDto>> collect = omSalesOrderDetDtoList.stream().collect(Collectors.groupingBy(OmSalesOrderDetDto::getWarehouseId, HashMap::new, Collectors.toList()));
        Set<Long> set = collect.keySet();
        for (Long id : set) {
            List<OmSalesOrderDetDto> omSalesOrderDetDtos = collect.get(id);
            if ("".equals(baseOrderFlow.getNextOrderTypeCode())) {
                //出库通知单

            } else if ("".equals(baseOrderFlow.getNextOrderTypeCode())) {
                //出库计划

            } else if ("".equals(baseOrderFlow.getNextOrderTypeCode())) {
                //拣货作业
                int lineNumber = 1;
                List<WmsInnerJobOrderDet> wmsInnerJobOrderDets = new LinkedList<>();
                for (OmSalesOrderDetDto omSalesOrderDetDto : omSalesOrderDetDtos) {
                    WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
                    wmsInnerJobOrderDet.setCoreSourceOrderCode(omSalesOrderDetDto.getCoreSourceOrderCode());
                    wmsInnerJobOrderDet.setSourceOrderCode(omSalesOrderDetDto.getSourceOrderCode());
                    wmsInnerJobOrderDet.setCoreSourceId(omSalesOrderDetDto.getCoreSourceId());
                    wmsInnerJobOrderDet.setSourceId(omSalesOrderDetDto.getSalesOrderDetId());
                    wmsInnerJobOrderDet.setLineNumber(lineNumber + "");
                    lineNumber++;
                    wmsInnerJobOrderDet.setMaterialId(omSalesOrderDetDto.getMaterialId());
                    wmsInnerJobOrderDet.setPlanQty(omSalesOrderDetDto.getOrderQty());
                    wmsInnerJobOrderDet.setLineStatus((byte) 1);
                    wmsInnerJobOrderDets.add(wmsInnerJobOrderDet);
                }
                WmsInnerJobOrder wmsInnerJobOrder = new WmsInnerJobOrder();
                wmsInnerJobOrder.setCoreSourceSysOrderTypeCode("OUT-OOO");
                wmsInnerJobOrder.setSourceSysOrderTypeCode("OUT-OOO");
                wmsInnerJobOrder.setWarehouseId(omSalesOrderDetDtos.get(0).getWarehouseId());
                wmsInnerJobOrder.setJobOrderType((byte) 2);
                wmsInnerJobOrder.setWmsInPutawayOrderDets(wmsInnerJobOrderDets);
                ResponseEntity responseEntity = innerFeignApi.add(wmsInnerJobOrder);
                if (responseEntity.getCode() != 0) {
                    throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
                } else {
                    i++;
                }
            } else {
                throw new BizErrorException("单据流配置错误");
            }
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(OmSalesOrderDto omSalesOrderDto) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        omSalesOrderDto.setSalesOrderCode(CodeUtils.getId("OUT-SO"));
        omSalesOrderDto.setOrgId(user.getOrganizationId());
        omSalesOrderDto.setCreateTime(new DateTime());
        omSalesOrderDto.setCreateUserId(user.getUserId());
        omSalesOrderDto.setModifiedUserId(user.getUserId());
        omSalesOrderDto.setModifiedTime(new DateTime());
        int i = omSalesOrderMapper.insertUseGeneratedKeys(omSalesOrderDto);

        //明细
        List<OmHtSalesOrderDet> htList = new LinkedList<>();
        List<OmSalesOrderDetDto> omSalesOrderDetDtoList = omSalesOrderDto.getOmSalesOrderDetDtoList();
        if(StringUtils.isNotEmpty(omSalesOrderDetDtoList)){
            for (OmSalesOrderDetDto omSalesOrderDetDto:omSalesOrderDetDtoList){
                omSalesOrderDetDto.setSalesOrderId(omSalesOrderDto.getSalesOrderId());
                omSalesOrderDetDto.setCreateUserId(user.getUserId());
                omSalesOrderDetDto.setCreateTime(new Date());
                omSalesOrderDetDto.setModifiedUserId(user.getUserId());
                omSalesOrderDetDto.setModifiedTime(new Date());
                omSalesOrderDetDto.setOrgId(user.getOrganizationId());

                OmHtSalesOrderDetDto omHtSalesOrderDetDto = new OmHtSalesOrderDetDto();
                org.springframework.beans.BeanUtils.copyProperties(omSalesOrderDetDto, omHtSalesOrderDetDto);
                htList.add(omHtSalesOrderDetDto);
            }
            omSalesOrderDetMapper.insertList(omSalesOrderDetDtoList);
            omHtSalesOrderDetMapper.insertList(htList);
        }

        //履历
        OmHtSalesOrderDto omHtSalesOrderDto = new OmHtSalesOrderDto();
        org.springframework.beans.BeanUtils.copyProperties(omSalesOrderDto, omHtSalesOrderDto);
        omHtSalesOrderMapper.insertSelective(omHtSalesOrderDto);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(OmSalesOrderDto omSalesOrderDto) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        omSalesOrderDto.setModifiedUserId(user.getUserId());
        omSalesOrderDto.setModifiedTime(new Date());
        int i = omSalesOrderMapper.updateByPrimaryKeySelective(omSalesOrderDto);

        //原来有的明细只更新
        ArrayList<Long> idList = new ArrayList<>();
        List<OmSalesOrderDetDto> omSalesOrderDetDtoList = omSalesOrderDto.getOmSalesOrderDetDtoList();
        if(StringUtils.isNotEmpty(omSalesOrderDetDtoList)) {
            for (OmSalesOrderDetDto omSalesOrderDetDto : omSalesOrderDetDtoList) {
                if (StringUtils.isNotEmpty(omSalesOrderDetDto.getSalesOrderDetId())) {
                    omSalesOrderDetMapper.updateByPrimaryKeySelective(omSalesOrderDetDto);
                    idList.add(omSalesOrderDetDto.getSalesOrderDetId());
                }
            }
        }

        //删除原明细
        Example example = new Example(OmSalesOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("salesOrderId",omSalesOrderDto.getSalesOrderId());
        if (idList.size() > 0) {
            criteria.andNotIn("salesOrderDetId", idList);
        }
        omSalesOrderDetMapper.deleteByExample(example);

        //明细
        List<OmHtSalesOrderDet> htList = new LinkedList<>();
        if(StringUtils.isNotEmpty(omSalesOrderDetDtoList)){
            List<OmSalesOrderDetDto> addDetList = new LinkedList<>();
            for (OmSalesOrderDetDto omSalesOrderDetDto:omSalesOrderDetDtoList){
                OmHtSalesOrderDetDto omHtSalesOrderDetDto = new OmHtSalesOrderDetDto();
                org.springframework.beans.BeanUtils.copyProperties(omSalesOrderDetDto, omHtSalesOrderDetDto);
                htList.add(omHtSalesOrderDetDto);

                if (idList.contains(omSalesOrderDetDto.getSalesOrderDetId())) {
                    continue;
                }
                omSalesOrderDetDto.setSalesOrderId(omSalesOrderDto.getSalesOrderId());
                omSalesOrderDetDto.setCreateUserId(user.getUserId());
                omSalesOrderDetDto.setCreateTime(new Date());
                omSalesOrderDetDto.setModifiedUserId(user.getUserId());
                omSalesOrderDetDto.setModifiedTime(new Date());
                omSalesOrderDetDto.setOrgId(user.getOrganizationId());
                addDetList.add(omSalesOrderDetDto);
            }
            omSalesOrderDetMapper.insertList(addDetList);
            omHtSalesOrderDetMapper.insertList(htList);
        }

        //履历
        OmHtSalesOrderDto omHtSalesOrderDto = new OmHtSalesOrderDto();
        org.springframework.beans.BeanUtils.copyProperties(omSalesOrderDto, omHtSalesOrderDto);
        omHtSalesOrderMapper.insertSelective(omHtSalesOrderDto);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idArray = ids.split(",");
        for(String id : idArray) {
            OmSalesOrder omSalesOrder = omSalesOrderMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(omSalesOrder)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //删除表体
            Example example  = new Example(OmSalesOrderDet.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("salesOrderId",id);
            omSalesOrderDetMapper.deleteByExample(example);
        }

        return omSalesOrderMapper.deleteByIds(ids);
    }

    @Override
    public List<OmSalesOrderDto> findList(Map<String, Object> map) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",currentUserInfo.getOrganizationId());
        return omSalesOrderMapper.findList(map);
    }

    @Override
    public List<OmSalesOrderDto> findAll() {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        Map<String, Object> map = new HashMap<>();
        map.put("orgId",currentUserInfo.getOrganizationId());
        return omSalesOrderMapper.findList(map);
    }

    /**
     * 下发生成销售出库单
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int issueWarehouse(Long id) {
        Map<String, Object> map = new HashMap<>();
        map.put("salesOrderId", id);
        List<OmSalesOrderDto> list = this.findList(map);
        if (StringUtils.isEmpty(list)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }

        OmSalesOrderDto omSalesOrderDto = list.get(0);
        WmsOutDeliveryOrder wmsOutDeliveryOrder = new WmsOutDeliveryOrder();
        wmsOutDeliveryOrder.setSourceOrderId(omSalesOrderDto.getSalesOrderId());
        wmsOutDeliveryOrder.setRelatedOrderCode1(omSalesOrderDto.getSalesOrderCode());
        List<BaseMaterialOwnerDto> baseMaterialOwnerDtos = baseFeignApi.findList(new SearchBaseMaterialOwner()).getData();
        if (StringUtils.isEmpty(baseMaterialOwnerDtos)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "不存在货主信息");
        }
        wmsOutDeliveryOrder.setMaterialOwnerId(baseMaterialOwnerDtos.get(0).getMaterialOwnerId());
        wmsOutDeliveryOrder.setOrderDate(new Date());
        wmsOutDeliveryOrder.setOrderTypeId((long)1);
        wmsOutDeliveryOrder.setDetailedAddress(omSalesOrderDto.getOmSalesOrderDetDtoList().size() > 0 ? omSalesOrderDto.getOmSalesOrderDetDtoList().get(0).getDeliveryAddress() : null);


        //明细
        List<OmSalesOrderDetDto> omSalesOrderDetDtoList = omSalesOrderDto.getOmSalesOrderDetDtoList();
        if (StringUtils.isNotEmpty(omSalesOrderDetDtoList)) {
            List<WmsOutDeliveryOrderDetDto> wmsOutDeliveryOrderDetDtos = new ArrayList<>();
            for (OmSalesOrderDetDto omSalesOrderDetDto : omSalesOrderDetDtoList) {
                if(StringUtils.isEmpty(omSalesOrderDetDto.getWarehouseId())){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "销售订单明细仓库不能为空");
                }

                if (omSalesOrderDetDto.getArrangeDispatchQty().compareTo(new BigDecimal("0")) == 1) {
                    WmsOutDeliveryOrderDetDto wmsOutDeliveryOrderDetDto = new WmsOutDeliveryOrderDetDto();
                    wmsOutDeliveryOrderDetDto.setSourceOrderId(omSalesOrderDto.getSalesOrderId());
                    wmsOutDeliveryOrderDetDto.setOrderDetId(omSalesOrderDetDto.getSalesOrderDetId());
                    wmsOutDeliveryOrderDetDto.setWarehouseId(omSalesOrderDetDto.getWarehouseId());
                    //查询指定仓库下库位类型为发货的库位
                    SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
                    searchBaseStorage.setStorageType((byte) 3);
                    searchBaseStorage.setWarehouseId(omSalesOrderDetDto.getWarehouseId());
                    List<BaseStorage> baseStorages = baseFeignApi.findList(searchBaseStorage).getData();
                    wmsOutDeliveryOrderDetDto.setStorageId(StringUtils.isEmpty(baseStorages) ? null : baseStorages.get(0).getStorageId());
                    wmsOutDeliveryOrderDetDto.setMaterialId(omSalesOrderDetDto.getMaterialId());
                    wmsOutDeliveryOrderDetDto.setPackingUnitName(omSalesOrderDetDto.getUnitName());
                    wmsOutDeliveryOrderDetDto.setPackingQty(omSalesOrderDetDto.getArrangeDispatchQty());
                    wmsOutDeliveryOrderDetDtos.add(wmsOutDeliveryOrderDetDto);

                    //修改累计通知发货数量及安排发运数量
                    omSalesOrderDetDto.setTotalInformDeliverQty(omSalesOrderDetDto.getTotalInformDeliverQty() == null ?
                            omSalesOrderDetDto.getArrangeDispatchQty() : omSalesOrderDetDto.getTotalInformDeliverQty().add(omSalesOrderDetDto.getArrangeDispatchQty()));
                    omSalesOrderDetDto.setArrangeDispatchQty(new BigDecimal("0"));
                    omSalesOrderDetMapper.updateByPrimaryKeySelective(omSalesOrderDetDto);
                }
            }
            wmsOutDeliveryOrder.setWmsOutDeliveryOrderDetList(wmsOutDeliveryOrderDetDtos);

            //下发仓库
            ResponseEntity responseEntity = outFeignApi.add(wmsOutDeliveryOrder);
            if (responseEntity.getCode() != 0) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"下发仓库失败");
            }
            return 1;
        }

        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<OmSalesOrder> orders) {
        return omSalesOrderMapper.batchUpdate(orders);
    }

}
