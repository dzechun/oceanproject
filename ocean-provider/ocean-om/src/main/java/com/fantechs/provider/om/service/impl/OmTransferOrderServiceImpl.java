package com.fantechs.provider.om.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmTransferOrderDto;
import com.fantechs.common.base.general.entity.om.OmTransferOrder;
import com.fantechs.common.base.general.entity.om.OmTransferOrderDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrder;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrderDet;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.wms.out.OutFeignApi;
import com.fantechs.provider.om.mapper.OmTransferOrderDetMapper;
import com.fantechs.provider.om.mapper.OmTransferOrderMapper;
import com.fantechs.provider.om.service.OmTransferOrderService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by Mr.Lei on 2021/06/15.
 */
@Service
public class OmTransferOrderServiceImpl extends BaseService<OmTransferOrder> implements OmTransferOrderService {

    @Resource
    private OmTransferOrderMapper omTransferOrderMapper;
    @Resource
    private OmTransferOrderDetMapper omTransferOrderDetMapper;
    @Resource
    private OutFeignApi outFeignApi;

    @Override
    public List<OmTransferOrderDto> findList(Map<String, Object> map) {
        SysUser sysUser = currentUser();
        map.put("orgId",sysUser.getOrganizationId());
        return omTransferOrderMapper.findList(map);
    }

    /**
     * 下发生成调拨出库单
     * @param ids
     * @return
     */
    @Override
    public int packageAutoOutOrder(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        int num = 0;
        for (String id : arrId) {
            OmTransferOrder omTransferOrder = omTransferOrderMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(omTransferOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            Example example = new Example(OmTransferOrderDet.class);
            example.createCriteria().andEqualTo("transferOrderId",omTransferOrder.getTransferOrderId());
            List<OmTransferOrderDet> list = omTransferOrderDetMapper.selectByExample(example);
            if(list.size()<1){
                throw new BizErrorException("下发失败");
            }
            //出库单表头
            WmsOutDeliveryOrder wmsOutDeliveryOrder = new WmsOutDeliveryOrder();
            wmsOutDeliveryOrder.setDeliveryOrderCode(CodeUtils.getId("DBCK-"));
            wmsOutDeliveryOrder.setMaterialOwnerId(omTransferOrder.getMaterialOwnerId());
            wmsOutDeliveryOrder.setSourceOrderId(omTransferOrder.getTransferOrderId());
            wmsOutDeliveryOrder.setRelatedOrderCode1(omTransferOrder.getTransferOrderCode());
            wmsOutDeliveryOrder.setOrderTypeId((long)2);
            wmsOutDeliveryOrder.setOrderStatus((byte)1);
            wmsOutDeliveryOrder.setOrderDate(new Date());
            wmsOutDeliveryOrder.setOrgId(sysUser.getOrganizationId());
            List<WmsOutDeliveryOrderDet> wmsOutDeliveryOrderDets = new ArrayList<>();
            int i = 1;
            for (OmTransferOrderDet omTransferOrderDet : list) {
                WmsOutDeliveryOrderDet wmsOutDeliveryOrderDet = new WmsOutDeliveryOrderDet();
                wmsOutDeliveryOrderDet.setWarehouseId(omTransferOrder.getOutWarehouseId());
                wmsOutDeliveryOrderDet.setSourceOrderId(omTransferOrder.getTransferOrderId());
                wmsOutDeliveryOrderDet.setOrderDetId(omTransferOrderDet.getTransferOrderDetId());
                wmsOutDeliveryOrderDet.setLineNumber(i);
                wmsOutDeliveryOrderDet.setPackingUnitName(omTransferOrderDet.getUnitName());
                wmsOutDeliveryOrderDet.setPackingQty(omTransferOrderDet.getOrderQty());
                wmsOutDeliveryOrderDet.setBatchCode(omTransferOrderDet.getBatchCode());
                wmsOutDeliveryOrderDets.add(wmsOutDeliveryOrderDet);
                i++;
            }
            ResponseEntity responseEntity = outFeignApi.add(wmsOutDeliveryOrder);
            if(responseEntity.getCode()!=0){
                throw new BizErrorException("生成出库单失败");
            }
            omTransferOrder.setOrderStatus((byte)2);
            omTransferOrderMapper.updateByPrimaryKeySelective(omTransferOrder);
            num++;
        }
        return num;
    }

    @Override
    public int save(OmTransferOrder record) {
        SysUser sysUser = currentUser();
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrgId(sysUser.getOrganizationId());
        int num = omTransferOrderMapper.insertUseGeneratedKeys(record);
        for (OmTransferOrderDet omTransferOrderDet : record.getOmTransferOrderDets()) {
            omTransferOrderDet.setTransferOrderId(record.getTransferOrderId());
            omTransferOrderDet.setCreateTime(new Date());
            omTransferOrderDet.setCreateUserId(sysUser.getUserId());
            omTransferOrderDet.setOrgId(sysUser.getOrganizationId());
            omTransferOrderDet.setModifiedTime(new Date());
            omTransferOrderDet.setModifiedUserId(sysUser.getUserId());
            omTransferOrderDet.setOrgId(sysUser.getOrganizationId());
        }
        num+=omTransferOrderDetMapper.insertList(record.getOmTransferOrderDets());
        return num;
    }

    @Override
    public int update(OmTransferOrder entity) {
        SysUser sysUser = currentUser();
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(sysUser.getUserId());
        //删除原有明细
        Example example = new Example(OmTransferOrderDet.class);
        example.createCriteria().andEqualTo("transferOrderId",entity.getTransferOrderId());
        omTransferOrderDetMapper.deleteByExample(example);
        for (OmTransferOrderDet omTransferOrderDet : entity.getOmTransferOrderDets()) {
            omTransferOrderDet.setTransferOrderDetId(null);
            omTransferOrderDet.setTransferOrderId(entity.getTransferOrderId());
            omTransferOrderDet.setCreateTime(new Date());
            omTransferOrderDet.setCreateUserId(sysUser.getUserId());
            omTransferOrderDet.setOrgId(sysUser.getOrganizationId());
            omTransferOrderDet.setModifiedTime(new Date());
            omTransferOrderDet.setModifiedUserId(sysUser.getUserId());
            omTransferOrderDet.setOrgId(sysUser.getOrganizationId());
        }
        int num = omTransferOrderDetMapper.insertList(entity.getOmTransferOrderDets());
        num+=omTransferOrderMapper.updateByPrimaryKeySelective(entity);
        return num;
    }

    @Override
    public int batchDelete(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        for (String id : arrId) {
            OmTransferOrder omTransferOrder = omTransferOrderMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(omTransferOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //删除明细
            Example example = new Example(OmTransferOrderDet.class);
            example.createCriteria().andEqualTo("transferOrderId",omTransferOrder.getTransferOrderId());
            omTransferOrderDetMapper.deleteByExample(example);
        }
        return omTransferOrderMapper.deleteByIds(ids);
    }

    private SysUser currentUser(){
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(sysUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return sysUser;
    }
}
