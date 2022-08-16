package com.fantechs.provider.om.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmHtOtherOutOrderDto;
import com.fantechs.common.base.general.dto.om.OmOtherOutOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDetDto;
import com.fantechs.common.base.general.entity.om.*;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.wms.out.OutFeignApi;
import com.fantechs.provider.om.mapper.OmOtherOutOrderDetMapper;
import com.fantechs.provider.om.mapper.OmOtherOutOrderMapper;
import com.fantechs.provider.om.mapper.OmSalesReturnOrderDetMapper;
import com.fantechs.provider.om.mapper.OmTransferOrderMapper;
import com.fantechs.provider.om.mapper.ht.OmHtOtherOutOrderDetMapper;
import com.fantechs.provider.om.mapper.ht.OmHtOtherOutOrderMapper;
import com.fantechs.provider.om.service.OmOtherOutOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

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
    @Resource
    private OmTransferOrderMapper omTransferOrderMapper;
    @Resource
    private OmSalesReturnOrderDetMapper omSalesReturnOrderDetMapper;
    @Resource
    private OmHtOtherOutOrderMapper omHtOtherOutOrderMapper;
    @Resource
    private OmHtOtherOutOrderDetMapper omHtOtherOutOrderDetMapper;

    @Override
    public List<OmOtherOutOrderDto> findList(Map<String, Object> map) {
        SysUser sysUser = currentUser();
        map.put("orgId",sysUser.getOrganizationId());
        return omOtherOutOrderMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int packageAutoOutOrder(OmOtherOutOrder omOtherOutOrder) {
        if(omOtherOutOrder.getOrderStatus()>2){
            throw new BizErrorException("单据已下发完成");
        }
        SysUser sysUser = currentUser();
        if(omOtherOutOrder.getOrderStatus()>=3){
            throw new BizErrorException("单据已完成");
        }

        if(omOtherOutOrder.getOmOtherOutOrderDets().size()<1){
            throw new BizErrorException("请输入下发数量");
        }
        List<OmOtherOutOrderDto> list = omOtherOutOrderMapper.findList(ControllerUtil.dynamicCondition("otherOutOrderId",omOtherOutOrder.getOtherOutOrderId()));
        int num = 0;
        List<WmsOutDeliveryOrderDetDto> wmsOutDeliveryOrderDetDtos = new ArrayList<>();
        int i=1;
        for (OmOtherOutOrderDet omOtherOutOrderDet : omOtherOutOrder.getOmOtherOutOrderDets()) {
            //获取发货库位
            Map<String,Object> map = new HashMap<>();
            map.put("orgId",sysUser.getOrganizationId());
            map.put("warehouseId",omOtherOutOrderDet.getWarehouseId());
            map.put("storageType",3);
            Long storageId = omTransferOrderMapper.findStorage(map);
            if(StringUtils.isEmpty(storageId)){
                throw new BizErrorException("未获取到该仓库的发货库位");
            }

            //获取物料单位名称
            String unitName =omSalesReturnOrderDetMapper.findUnitName(omOtherOutOrderDet.getMaterialId());

            WmsOutDeliveryOrderDetDto wmsOutDeliveryOrderDetDto = new WmsOutDeliveryOrderDetDto();
            wmsOutDeliveryOrderDetDto.setWarehouseId(omOtherOutOrderDet.getWarehouseId());
            wmsOutDeliveryOrderDetDto.setSourceOrderId(omOtherOutOrderDet.getOtherOutOrderId());
            wmsOutDeliveryOrderDetDto.setOrderDetId(omOtherOutOrderDet.getOtherOutOrderDetId());
            wmsOutDeliveryOrderDetDto.setMaterialId(omOtherOutOrderDet.getMaterialId());
            wmsOutDeliveryOrderDetDto.setStorageId(storageId);
            wmsOutDeliveryOrderDetDto.setLineNumber(i);
            wmsOutDeliveryOrderDetDto.setPackingUnitName(unitName);
            wmsOutDeliveryOrderDetDto.setPackingQty(omOtherOutOrderDet.getQty());
            wmsOutDeliveryOrderDetDto.setBatchCode(omOtherOutOrderDet.getBatchCode());
            wmsOutDeliveryOrderDetDtos.add(wmsOutDeliveryOrderDetDto);
            i++;
        }
        WmsOutDeliveryOrder wmsOutDeliveryOrder = new WmsOutDeliveryOrder();
        wmsOutDeliveryOrder.setDeliveryOrderCode(CodeUtils.getId("QTCK-"));
        wmsOutDeliveryOrder.setMaterialOwnerId(omOtherOutOrder.getMaterialOwnerId());
        wmsOutDeliveryOrder.setSourceOrderId(omOtherOutOrder.getOtherOutOrderId());
        wmsOutDeliveryOrder.setRelatedOrderCode1(omOtherOutOrder.getOtherOutOrderCode());
        wmsOutDeliveryOrder.setOrderTypeId((long)7);
        wmsOutDeliveryOrder.setOrderStatus((byte)1);
        wmsOutDeliveryOrder.setOrderDate(new Date());
        wmsOutDeliveryOrder.setConsignee(list.get(0).getConsigneeName());
        wmsOutDeliveryOrder.setLinkManName(omOtherOutOrder.getLinkManName());
        wmsOutDeliveryOrder.setLinkManPhone(omOtherOutOrder.getLinkManPhone());
        wmsOutDeliveryOrder.setEmailAddress(omOtherOutOrder.getEMailAddress());
        wmsOutDeliveryOrder.setFaxNumber(omOtherOutOrder.getFaxNumber());
        wmsOutDeliveryOrder.setDetailedAddress(omOtherOutOrder.getAddress());
        wmsOutDeliveryOrder.setOrgId(sysUser.getOrganizationId());
        wmsOutDeliveryOrder.setPlanDespatchDate(omOtherOutOrder.getPlanArriveDate());
        wmsOutDeliveryOrder.setDemandArriveDate(omOtherOutOrder.getReqArriveDate());
        wmsOutDeliveryOrder.setWmsOutDeliveryOrderDetList(wmsOutDeliveryOrderDetDtos);
        ResponseEntity responseEntity = outFeignApi.add(wmsOutDeliveryOrder);
        if(responseEntity.getCode()!=0){
            throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
        }
        num+=this.updateStatus(omOtherOutOrder);

        this.addHt(omOtherOutOrder,omOtherOutOrder.getOmOtherOutOrderDets());
        return num;
    }

    private int updateStatus(OmOtherOutOrder omOtherOutOrder){
        int num = 0;
        for (OmOtherOutOrderDet omOtherOutOrderDet : omOtherOutOrder.getOmOtherOutOrderDets()) {
            OmOtherOutOrderDet omOtherOutOrderDet1 = omOtherOutOrderDetMapper.selectByPrimaryKey(omOtherOutOrderDet.getOtherOutOrderDetId());
            if(StringUtils.isEmpty(omOtherOutOrderDet1.getIssueQty())){
                omOtherOutOrderDet1.setIssueQty(BigDecimal.ZERO);
            }
            omOtherOutOrderDet.setIssueQty(omOtherOutOrderDet.getQty().add(omOtherOutOrderDet1.getIssueQty()));
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

        this.addHt(omOtherOutOrder,omOtherOutOrder.getOmOtherOutOrderDets());
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int writeQty(OmOtherOutOrderDet omOtherOutOrderDet) {
        Map<String,Object> map = new HashMap<>();
        int num = 0;
        map.put("otherOutOrderId",omOtherOutOrderDet.getOtherOutOrderId());
        OmOtherOutOrder omOtherOutOrder = omOtherOutOrderMapper.findList(map).get(0);
        OmOtherOutOrderDet omOtherOutOrderDet1 = omOtherOutOrderDetMapper.selectByPrimaryKey(omOtherOutOrderDet.getOtherOutOrderDetId());
        if(StringUtils.isEmpty(omOtherOutOrderDet1.getDispatchQty())){
            omOtherOutOrderDet1.setDispatchQty(BigDecimal.ZERO);
        }
        BigDecimal total = omOtherOutOrderDet1.getDispatchQty().add(omOtherOutOrderDet.getDispatchQty());
        omOtherOutOrderDet1.setDispatchQty(total);
        num+=omOtherOutOrderDetMapper.updateByPrimaryKeySelective(omOtherOutOrderDet1);
        if(StringUtils.isEmpty(omOtherOutOrder.getTotalDispatchQty())){
            omOtherOutOrder.setTotalDispatchQty(BigDecimal.ZERO);
        }
        if(StringUtils.isEmpty(omOtherOutOrder.getActualDespatchDate())){
            omOtherOutOrder.setActualDespatchDate(new Date());
        }
        if(total.compareTo(omOtherOutOrder.getTotalQty())==0){
            omOtherOutOrder.setOrderStatus((byte)4);
        }
        omOtherOutOrder.setActualDespatchDate(new Date());
        num+=omOtherOutOrderMapper.updateByPrimaryKeySelective(omOtherOutOrder);
        return num;
    }

    @Override
    public List<OmHtOtherOutOrderDto> findHtList(Map<String, Object> map) {
        SysUser sysUser = currentUser();
        map.put("orgId",sysUser.getOrganizationId());
        return omHtOtherOutOrderMapper.findHtList(map);
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

        this.addHt(record,record.getOmOtherOutOrderDets());
        return num;
    }

    @Override
    public int update(OmOtherOutOrder entity) {
        SysUser sysUser = currentUser();
        if(entity.getOrderStatus()>1){
            throw new BizErrorException("单据下发中无法更改");
        }
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(sysUser.getUserId());
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
        this.addHt(entity,entity.getOmOtherOutOrderDets());
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
            this.addHt(omOtherOutOrder,null);
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


    /**
     * 添加历史记录
     * @return
     */
    private int addHt(OmOtherOutOrder omOtherOutOrder,List<OmOtherOutOrderDet> omOtherOutOrderDets){
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
