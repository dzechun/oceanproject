package com.fantechs.provider.om.service.impl;


import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseMaterialOwnerDto;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDetDto;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDto;
import com.fantechs.common.base.general.dto.om.SearchOmSalesOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDetDto;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterialOwner;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.general.entity.om.OmHtSalesOrder;
import com.fantechs.common.base.general.entity.om.OmSalesOrder;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.*;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.out.OutFeignApi;
import com.fantechs.provider.om.mapper.OmSalesOrderDetMapper;
import com.fantechs.provider.om.mapper.OmSalesOrderMapper;
import com.fantechs.provider.om.service.OmSalesOrderDetService;
import com.fantechs.provider.om.service.OmSalesOrderService;
import com.fantechs.provider.om.service.ht.OmHtSalesOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

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
    private OmSalesOrderDetService omSalesOrderDetService;
    @Resource
    private OmHtSalesOrderService omHtSalesOrderService;
    @Resource
    private OutFeignApi outFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private SecurityFeignApi securityFeignApi;

    @Override
    public int saveDto(OmSalesOrderDto omSalesOrderDto) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();

        OmSalesOrder omSalesOrder = new OmSalesOrder();

        BeanUtils.autoFillEqFields(omSalesOrderDto, omSalesOrder);

        if(this.save(omSalesOrder, currentUserInfo) <= 0) {
//            throw new BizErrorException(ErrorCodeEnum.GL99990005.getCode(), "保存表头失败");
            return 0;
        }
        for(int i = 0; i < omSalesOrderDto.getOmSalesOrderDetDtoList().size(); i++) {
            OmSalesOrderDetDto omSalesOrderDetDto = omSalesOrderDto.getOmSalesOrderDetDtoList().get(i);
            omSalesOrderDetDto.setSalesOrderId(omSalesOrder.getSalesOrderId());
            if(omSalesOrderDetService.saveDto(omSalesOrderDetDto, omSalesOrder.getCustomerOrderCode(), i, currentUserInfo) <= 0) {
                return 0;
            }
        }

        return 1;
    }


    private int save(OmSalesOrder omSalesOrder, SysUser currentUserInfo) {
//        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
//        if(StringUtils.isEmpty(currentUserInfo)) {
//            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
//        }

//        if(StringUtils.isEmpty(omSalesOrder.getContractCode())) {
//            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "合同号不能为空");
//        }
//
//        if(StringUtils.isEmpty(omSalesOrder.getCustomerOrderCode())) {
//            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "客户订单号不能为空");
//        }

        omSalesOrder.setSalesOrderId(null);
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("wanbaoSyncData");
        List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        if (specItems.isEmpty()){
            omSalesOrder.setSalesOrderCode(CodeUtils.getId("SEORD"));
        }else {
            JSONObject jsonObject = JSON.parseObject(specItems.get(0).getParaValue());
            if(!jsonObject.get("enable").equals(1) || StringUtils.isEmpty(omSalesOrder.getSalesOrderCode())){
                omSalesOrder.setSalesOrderCode(CodeUtils.getId("SEORD"));
            }
        }
        omSalesOrder.setOrgId(currentUserInfo.getOrganizationId());
        omSalesOrder.setCreateTime(DateUtils.getDateTimeString(new DateTime()));
        omSalesOrder.setCreateUserId(currentUserInfo.getUserId());
        omSalesOrder.setModifiedUserId(currentUserInfo.getUserId());
        omSalesOrder.setModifiedTime(DateUtils.getDateTimeString(new DateTime()));

        int result = omSalesOrderMapper.insertUseGeneratedKeys(omSalesOrder);

        recordHistory(omSalesOrder, currentUserInfo, "新增");
//
        return result;
    }

    @Override
    public int batchDelete(String ids) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idArray = ids.split(",");
        for(String id : idArray) {
            OmSalesOrder omSalesOrder = omSalesOrderMapper.selectByPrimaryKey(Long.valueOf(id));
            if(StringUtils.isEmpty(omSalesOrder)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            recordHistory(omSalesOrder, currentUserInfo, "删除");
            //获取对应表体id
            //删除表体先
            SearchOmSalesOrderDetDto searchOmSalesOrderDetDto = new SearchOmSalesOrderDetDto();
            searchOmSalesOrderDetDto.setSalesOrderId(omSalesOrder.getSalesOrderId());
            List<OmSalesOrderDetDto> list = omSalesOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchOmSalesOrderDetDto));
            StringBuffer detIds = new StringBuffer();
            for(OmSalesOrderDetDto omSalesOrderDetDto : list) {
                detIds.append(omSalesOrderDetDto.getSalesOrderDetId().toString());
                detIds.append(',');
            }
            if(detIds.length() > 0) {
                detIds.deleteCharAt(detIds.length()-1);
                if(omSalesOrderDetService.batchDelete(detIds.toString()) <= 0) {
                    return 0;
                }
            }

        }
        if(omSalesOrderMapper.deleteByIds(ids)<=0) {
            return 0;
        }
        return 1;
    }

    @Override
    public int updateDto(OmSalesOrderDto omSalesOrderDto) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        OmSalesOrder omSalesOrder = new OmSalesOrder();

        BeanUtils.autoFillEqFields(omSalesOrderDto, omSalesOrder);

        if(this.update(omSalesOrder, currentUserInfo) <= 0) {
            return 0;
        }

        Map<String, Object> findParaMap = new HashMap<>();
        findParaMap.put("salesOrderId", omSalesOrder.getSalesOrderId());
        List<OmSalesOrderDetDto> dbOmSalesOrderDet = omSalesOrderDetService.findList(findParaMap);

        List<Long> dbDataList = new ArrayList<>();
        int maxLineNumber = 0;
        for(OmSalesOrderDetDto omSalesOrderDetDto : dbOmSalesOrderDet) {
            dbDataList.add(omSalesOrderDetDto.getSalesOrderDetId());
            int lineNumber = Integer.parseInt(omSalesOrderDetDto.getSourceLineNumber());
            maxLineNumber = Math.max(lineNumber, maxLineNumber);
        }

        List<Long> newDataList = new ArrayList<>();
        for(OmSalesOrderDetDto omSalesOrderDetDto : omSalesOrderDto.getOmSalesOrderDetDtoList()) {
            if(omSalesOrderDetDto.getSalesOrderDetId() == null) {
                omSalesOrderDetDto.setSalesOrderId(omSalesOrder.getSalesOrderId());
                if(omSalesOrderDetService.saveDto(omSalesOrderDetDto, omSalesOrder.getCustomerOrderCode(), maxLineNumber + 1, currentUserInfo) <= 0) {
                    return 0;
                }
                ++maxLineNumber;
            } else {
                newDataList.add(omSalesOrderDetDto.getSalesOrderDetId());
                if(omSalesOrderDetService.updateDto(omSalesOrderDetDto, currentUserInfo) <= 0) {
                    return 0;
                }
            }
        }

        String result = this.getDiffList(newDataList, dbDataList);
        if(!StringUtils.isEmpty(result)) {
            if(omSalesOrderDetService.batchDelete(this.getDiffList(newDataList, dbDataList)) <= 0 ) {
                return 0;
            }
        }

        return 1;
    }

    private int update(OmSalesOrder omSalesOrder, SysUser currentUserInfo) {
//        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
//        if(StringUtils.isEmpty((currentUserInfo))) {
//            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
//        }

        if(StringUtils.isEmpty(omSalesOrder.getSalesOrderCode())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "销售订单号不能为空");
        }

//        if(StringUtils.isEmpty(omSalesOrder.getContractCode())) {
//            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "合同号不能为空");
//        }

//        if(StringUtils.isEmpty(omSalesOrder.getCustomerOrderCode())) {
//            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "客户订单号不能为空");
//        }

        omSalesOrder.setModifiedUserId(currentUserInfo.getUserId());
        omSalesOrder.setModifiedTime(DateUtils.getDateTimeString(new DateTime()));


        if(omSalesOrderMapper.updateByPrimaryKeySelective(omSalesOrder)<=0) {
            return 0;
        }
        recordHistory(omSalesOrder, currentUserInfo, "更新");
        return 1;
    }

    @Override
    public List<OmSalesOrderDto> findList(Map<String, Object> map) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();

        map.put("orgId",currentUserInfo.getOrganizationId());
        List<OmSalesOrderDto> omSalesOrderDtoList = omSalesOrderMapper.findList(map);
        for(OmSalesOrderDto omSalesOrderDto : omSalesOrderDtoList) {
            Map<String, Object> omSalesOrderDetMap = new HashMap<>();
            omSalesOrderDetMap.put("salesOrderId", omSalesOrderDto.getSalesOrderId());
            omSalesOrderDto.setOmSalesOrderDetDtoList(omSalesOrderDetService.findList(omSalesOrderDetMap));
        }
        return omSalesOrderDtoList;
    }

    @Override
    public List<OmSalesOrderDto> findAll() {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        Map<String, Object> map = new HashMap<>();
        map.put("orgId",currentUserInfo.getOrganizationId());
        List<OmSalesOrderDto> omSalesOrderDtoList = omSalesOrderMapper.findList(map);
        return omSalesOrderDtoList;
    }


    private void recordHistory(OmSalesOrder omSalesOrder, SysUser currentUserInfo, String operation) {
        OmHtSalesOrder omHtSalesOrder = new OmHtSalesOrder();
        if(StringUtils.isEmpty(omSalesOrder)) {
            return;
        }
        BeanUtils.autoFillEqFields(omSalesOrder, omHtSalesOrder);
        omHtSalesOrder.setOption1(operation);
//        omHtSalesOrder.setOrgId(currentUserInfo.getOrganizationId());
//        omHtSalesOrder.setCreateTime(new Date());
//        omHtSalesOrder.setCreateUserId(currentUserInfo.getUserId());
        omHtSalesOrder.setModifiedTime(DateUtils.getDateTimeString(new DateTime()));
        omHtSalesOrder.setModifiedUserId(currentUserInfo.getUserId());

        omHtSalesOrderService.save(omHtSalesOrder);
    }

    private String getDiffList(List<Long> newDataList, List<Long> dbDataList) {
        StringBuffer idsString = new StringBuffer();
        if(dbDataList != null && !dbDataList.isEmpty() && newDataList != null && !newDataList.isEmpty()) {
            Map<Long, Long> newDataMap = new HashMap<>();
            for(Long newDataId : newDataList) {
                newDataMap.put(newDataId, newDataId);
            }


            for(Long dbId : dbDataList) {
                if(!newDataMap.containsKey(dbId)) {
                    idsString.append(dbId);
                    idsString.append(',');
                }
            }
            if(idsString.length() > 0) {
                idsString.deleteCharAt(idsString.length()-1);
            }
        }

        return idsString.toString();
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
                throw new BizErrorException("下发仓库失败");
            }
            return 1;
        }

        return 0;
    }

    @Override
    public int batchUpdate(List<OmSalesOrder> orders) {
        return omSalesOrderMapper.batchUpdate(orders);
    }

}
