package com.fantechs.provider.om.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmOtherOutOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDetDto;
import com.fantechs.common.base.general.entity.om.*;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.wms.out.OutFeignApi;
import com.fantechs.provider.om.mapper.OmOtherOutOrderDetMapper;
import com.fantechs.provider.om.mapper.OmOtherOutOrderMapper;
import com.fantechs.provider.om.service.OmOtherOutOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private OutFeignApi outFeignApi;

    @Override
    public List<OmOtherOutOrderDto> findList(Map<String, Object> map) {
        SysUser sysUser = currentUser();
        map.put("orgId",sysUser.getOrganizationId());
        return omOtherOutOrderMapper.findList(map);
    }

    @Override
    public int packageAutoOutOrder(OmOtherOutOrder omOtherOutOrder) {
        SysUser sysUser = currentUser();
        if(omOtherOutOrder.getOrderStatus()>=3){
            throw new BizErrorException("单据已完成");
        }
        int num = 0;
        List<WmsOutDeliveryOrderDetDto> wmsOutDeliveryOrderDetDtos = new ArrayList<>();
        int i=1;
        for (OmOtherOutOrderDet omOtherOutOrderDet : omOtherOutOrder.getOmOtherOutOrderDets()) {
            WmsOutDeliveryOrderDetDto wmsOutDeliveryOrderDetDto = new WmsOutDeliveryOrderDetDto();
            wmsOutDeliveryOrderDetDto.setWarehouseId(omOtherOutOrderDet.getWarehouseId());
            wmsOutDeliveryOrderDetDto.setSourceOrderId(omOtherOutOrderDet.getOtherOutOrderId());
            wmsOutDeliveryOrderDetDto.setOrderDetId(omOtherOutOrderDet.getOtherOutOrderDetId());
            wmsOutDeliveryOrderDetDto.setLineNumber(i);
            wmsOutDeliveryOrderDetDto.setPackingUnitName(omOtherOutOrderDet.getUnitName());
            wmsOutDeliveryOrderDetDto.setPackingQty(omOtherOutOrderDet.getOrderQty());
            wmsOutDeliveryOrderDetDto.setBatchCode(omOtherOutOrderDet.getBatchCode());
            wmsOutDeliveryOrderDetDtos.add(wmsOutDeliveryOrderDetDto);
            i++;
        }
        WmsOutDeliveryOrder wmsOutDeliveryOrder = new WmsOutDeliveryOrder();
        wmsOutDeliveryOrder.setDeliveryOrderCode(CodeUtils.getId("DBCK-"));
        wmsOutDeliveryOrder.setMaterialOwnerId(omOtherOutOrder.getMaterialOwnerId());
        wmsOutDeliveryOrder.setSourceOrderId(omOtherOutOrder.getOtherOutOrderId());
        wmsOutDeliveryOrder.setRelatedOrderCode1(omOtherOutOrder.getOtherOutOrderCode());
        wmsOutDeliveryOrder.setOrderTypeId((long)2);
        wmsOutDeliveryOrder.setOrderStatus((byte)1);
        wmsOutDeliveryOrder.setOrderDate(new Date());
        wmsOutDeliveryOrder.setOrgId(sysUser.getOrganizationId());
        wmsOutDeliveryOrder.setWmsOutDeliveryOrderDetList(wmsOutDeliveryOrderDetDtos);
        ResponseEntity responseEntity = outFeignApi.add(wmsOutDeliveryOrder);
        if(responseEntity.getCode()!=0){
            throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
        }
        num+=this.updateStatus(omOtherOutOrder);
        return num;
    }

    private int updateStatus(OmOtherOutOrder omOtherOutOrder){
        int num = 0;
        for (OmOtherOutOrderDet omOtherOutOrderDet : omOtherOutOrder.getOmOtherOutOrderDets()) {
            OmOtherOutOrderDet omOtherOutOrderDet1 = omOtherOutOrderDetMapper.selectByPrimaryKey(omOtherOutOrderDet.getOtherOutOrderDetId());
            omOtherOutOrderDet.setIssueQty(omOtherOutOrderDet1.getQty().add(omOtherOutOrderDet.getIssueQty()));
            num+=omOtherOutOrderDetMapper.updateByPrimaryKeySelective(omOtherOutOrderDet);
        }
        BigDecimal total = omOtherOutOrder.getOmOtherOutOrderDets().stream()
                .map(OmOtherOutOrderDet::getIssueQty)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        omOtherOutOrder.setTotalIssueQty(total);
        if(omOtherOutOrder.getTotalQty().compareTo(total)==0){
            omOtherOutOrder.setOrderStatus((byte)3);
        }else{
            omOtherOutOrder.setOrderStatus((byte)2);
        }
        num+=omOtherOutOrderMapper.updateByPrimaryKeySelective(omOtherOutOrder);
        return num;
    }

    @Override
    public int writeQty(OmOtherOutOrder omOtherOutOrder) {
        return 0;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(OmOtherOutOrder record) {
        SysUser sysUser = currentUser();
        record.setOtherOutOrderCode(CodeUtils.getId("OTCK-"));
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrgId(sysUser.getOrganizationId());
        record.setOrderStatus((byte)1);
        record = this.calculateWeight(record);
        int num = omOtherOutOrderMapper.insertUseGeneratedKeys(record);
        for (OmOtherOutOrderDet omOtherOutOrderDet : record.getOmOtherOutOrderDets()) {
            omOtherOutOrderDet.setOtherOutOrderId(record.getOtherOutOrderId());
            omOtherOutOrderDet.setCreateTime(new Date());
            omOtherOutOrderDet.setCreateUserId(sysUser.getUserId());
            omOtherOutOrderDet.setModifiedTime(new Date());
            omOtherOutOrderDet.setModifiedUserId(sysUser.getUserId());
            omOtherOutOrderDet.setOrgId(sysUser.getOrganizationId());
            num+=omOtherOutOrderDetMapper.insertSelective(omOtherOutOrderDet);
        }
        return num;
    }

    /**
     * 计算重量
     * @param omOtherOutOrder
     * @return
     */
    private OmOtherOutOrder calculateWeight(OmOtherOutOrder omOtherOutOrder){
        BigDecimal totalVolume = BigDecimal.ZERO;
        BigDecimal totalNetWeight = BigDecimal.ZERO;
        BigDecimal totalGrossWeight = BigDecimal.ZERO;
        for (OmOtherOutOrderDet omOtherOutOrderDet : omOtherOutOrder.getOmOtherOutOrderDets()) {
            if(StringUtils.isEmpty(omOtherOutOrderDet.getMaterialId())){
                throw new BizErrorException("物料错误");
            }
            OmOtherOutOrder om = omOtherOutOrderMapper.findMaterial(omOtherOutOrderDet.getMaterialId());
            totalVolume.add(om.getTotalVolume());
            totalNetWeight.add(om.getTotalNetWeight());
            totalGrossWeight.add(om.getTotalGrossWeight());
        }
        omOtherOutOrder.setTotalVolume(totalVolume);
        omOtherOutOrder.setTotalNetWeight(totalNetWeight);
        omOtherOutOrder.setTotalGrossWeight(totalGrossWeight);
        return omOtherOutOrder;
    }

    @Override
    public int update(OmOtherOutOrder entity) {
        SysUser sysUser = currentUser();
        if(entity.getOrderStatus()>1){
            throw new BizErrorException("单据下发中无法更改");
        }
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(sysUser.getUserId());
        entity = this.calculateWeight(entity);
        int num = omOtherOutOrderMapper.updateByPrimaryKeySelective(entity);
        //删除原有明细
        Example example = new Example(OmOtherOutOrderDet.class);
        example.createCriteria().andEqualTo("otherOutOrderId",entity.getOtherOutOrderId());
        omOtherOutOrderDetMapper.deleteByExample(example);
        for (OmOtherOutOrderDet omOtherOutOrderDet : entity.getOmOtherOutOrderDets()) {
            omOtherOutOrderDet.setOtherOutOrderId(entity.getOtherOutOrderId());
            omOtherOutOrderDet.setCreateTime(new Date());
            omOtherOutOrderDet.setCreateUserId(sysUser.getUserId());
            omOtherOutOrderDet.setModifiedTime(new Date());
            omOtherOutOrderDet.setModifiedUserId(sysUser.getUserId());
            omOtherOutOrderDet.setOrgId(sysUser.getOrganizationId());
            num+=omOtherOutOrderDetMapper.insertSelective(omOtherOutOrderDet);
        }
        return num;
    }

    @Override
    public int batchDelete(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        for (String id : arrId) {
            OmOtherOutOrder omOtherOutOrder = omOtherOutOrderMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(omOtherOutOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012000,id);
            }
            if(omOtherOutOrder.getOrderStatus()>1){
                throw new BizErrorException("单据已下发，无法删除");
            }
            Example example = new Example(OmOtherOutOrderDet.class);
            example.createCriteria().andEqualTo("otherOutOrderId",omOtherOutOrder.getOtherOutOrderId());
            omOtherOutOrderDetMapper.deleteByExample(example);
        }
        return omOtherOutOrderMapper.deleteByIds(ids);
    }

    private SysUser currentUser(){
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(sysUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return sysUser;
    }
}
