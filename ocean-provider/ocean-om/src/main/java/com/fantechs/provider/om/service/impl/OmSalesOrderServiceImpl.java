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
import com.fantechs.common.base.general.dto.om.OmHtSalesOrderDetDto;
import com.fantechs.common.base.general.dto.om.OmHtSalesOrderDto;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDetDto;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDetDto;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterialOwner;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.general.entity.om.OmHtSalesOrderDet;
import com.fantechs.common.base.general.entity.om.OmSalesOrder;
import com.fantechs.common.base.general.entity.om.OmSalesOrderDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
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
    private SecurityFeignApi securityFeignApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(OmSalesOrderDto omSalesOrderDto) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("wanbaoSyncData");
        List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        if (specItems.isEmpty()){
            omSalesOrderDto.setSalesOrderCode(CodeUtils.getId("SEORD"));
        }else {
            JSONObject jsonObject = JSON.parseObject(specItems.get(0).getParaValue());
            if(!jsonObject.get("enable").equals(1) || StringUtils.isEmpty(omSalesOrderDto.getSalesOrderCode())){
                omSalesOrderDto.setSalesOrderCode(CodeUtils.getId("SEORD"));
            }
        }
        omSalesOrderDto.setOrgId(user.getOrganizationId());
        omSalesOrderDto.setCreateTime(new DateTime());
        omSalesOrderDto.setCreateUserId(user.getUserId());
        omSalesOrderDto.setModifiedUserId(user.getUserId());
        omSalesOrderDto.setModifiedTime(new DateTime());
        int i = omSalesOrderMapper.insertUseGeneratedKeys(omSalesOrderDto);

        //明细
        int lineNumber = 1;
        List<OmHtSalesOrderDet> htList = new LinkedList<>();
        List<OmSalesOrderDetDto> omSalesOrderDetDtoList = omSalesOrderDto.getOmSalesOrderDetDtoList();
        if(StringUtils.isNotEmpty(omSalesOrderDetDtoList)){
            for (OmSalesOrderDetDto omSalesOrderDetDto:omSalesOrderDetDtoList){
                omSalesOrderDetDto.setLineNumber(lineNumber+"");
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

        //删除原明细
        Example example = new Example(OmSalesOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("salesOrderId",omSalesOrderDto.getSalesOrderId());
        omSalesOrderDetMapper.deleteByExample(example);

        //明细
        int lineNumber = 1;
        List<OmHtSalesOrderDet> htList = new LinkedList<>();
        List<OmSalesOrderDetDto> omSalesOrderDetDtoList = omSalesOrderDto.getOmSalesOrderDetDtoList();
        if(StringUtils.isNotEmpty(omSalesOrderDetDtoList)){
            for (OmSalesOrderDetDto omSalesOrderDetDto:omSalesOrderDetDtoList){
                omSalesOrderDetDto.setLineNumber(lineNumber+"");
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

        omSalesOrderDto.setModifiedUserId(user.getUserId());
        omSalesOrderDto.setModifiedTime(new Date());
        int i = omSalesOrderMapper.updateByPrimaryKeySelective(omSalesOrderDto);

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
