package com.fantechs.provider.om.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmPurchaseOrderDto;
import com.fantechs.common.base.general.entity.om.OmPurchaseOrder;
import com.fantechs.common.base.general.entity.om.OmPurchaseOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.mapper.OmPurchaseOrderDetMapper;
import com.fantechs.provider.om.mapper.OmPurchaseOrderMapper;
import com.fantechs.provider.om.service.OmPurchaseOrderService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/17.
 */
@Service
public class OmPurchaseOrderServiceImpl extends BaseService<OmPurchaseOrder> implements OmPurchaseOrderService {

    @Resource
    private OmPurchaseOrderMapper omPurchaseOrderMapper;
    @Resource
    private OmPurchaseOrderDetMapper omPurchaseOrderDetMapper;

    @Override
    public List<OmPurchaseOrderDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(map.get("orgId"))) {
            if (StringUtils.isEmpty(user)) {
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }
            map.put("orgId", user.getOrganizationId());
        }
        if (StringUtils.isNotEmpty(map.get("ifSupplierFind")) && Byte.valueOf(map.get("ifSupplierFind").toString()) == 1) {
            map.put("supplierId",user.getSupplierId());
        }
        return omPurchaseOrderMapper.findList(map);
    }

    @Override
    public OmPurchaseOrder saveByApi(OmPurchaseOrder omPurchaseOrder) {

        Example example = new Example(OmPurchaseOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("purchaseOrderCode", omPurchaseOrder.getPurchaseOrderCode());
        criteria.andEqualTo("orgId", omPurchaseOrder.getOrgId());
        List<OmPurchaseOrder> omPurchaseOrders = omPurchaseOrderMapper.selectByExample(example);
        omPurchaseOrderMapper.deleteByExample(example);
        example.clear();

        //删除该采购订单下的所有详情表
        if(StringUtils.isNotEmpty(omPurchaseOrders)) {
            Example detExample = new Example(OmPurchaseOrderDet.class);
            Example.Criteria detCriteria = detExample.createCriteria();
            detCriteria.andEqualTo("purchaseOrderId", omPurchaseOrders.get(0).getPurchaseOrderId());
            omPurchaseOrderDetMapper.deleteByExample(detExample);
            detExample.clear();
        }

        omPurchaseOrder.setCreateTime(new Date());
        omPurchaseOrderMapper.insertUseGeneratedKeys(omPurchaseOrder);
        return omPurchaseOrder;
    }

    @Override
    public int save(OmPurchaseOrder omPurchaseOrder) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(OmPurchaseOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("purchaseOrderCode", omPurchaseOrder.getPurchaseOrderCode());
        criteria.andEqualTo("orgId", omPurchaseOrder.getOrgId());
        if (StringUtils.isNotEmpty(omPurchaseOrder.getPurchaseOrderId())) {
            criteria.andNotEqualTo("purchaseOrderId", omPurchaseOrder.getPurchaseOrderId());
        }
        List<OmPurchaseOrder> omPurchaseOrders = omPurchaseOrderMapper.selectByExample(example);
        if (!omPurchaseOrders.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"采购单号重复");
        }
        omPurchaseOrder.setOrderStatus((byte) 1);
        omPurchaseOrder.setStatus((byte) 1);
        omPurchaseOrder.setOrgId(user.getOrganizationId());
        omPurchaseOrder.setCreateUserId(user.getUserId());
        omPurchaseOrder.setCreateTime(new Date());
        omPurchaseOrder.setIsDelete((byte) 1);
        if (StringUtils.isNotEmpty(omPurchaseOrder.getPurchaseOrderId())) {
            omPurchaseOrderMapper.updateByPrimaryKeySelective(omPurchaseOrder);

            //删除该采购订单下的所有详情表
            Example detExample = new Example(OmPurchaseOrderDet.class);
            Example.Criteria detCriteria = detExample.createCriteria();
            detCriteria.andEqualTo("purchaseOrderId", omPurchaseOrder.getPurchaseOrderId());
            omPurchaseOrderDetMapper.deleteByExample(detExample);
        } else {
            omPurchaseOrderMapper.insertUseGeneratedKeys(omPurchaseOrder);
        }
        for (OmPurchaseOrderDet omPurchaseOrderDet : omPurchaseOrder.getOmPurchaseOrderDetList()) {
            omPurchaseOrderDet.setPurchaseOrderId(omPurchaseOrder.getPurchaseOrderId());
            omPurchaseOrderDet.setStatus((byte) 1);
            omPurchaseOrderDet.setOrgId(user.getOrganizationId());
            omPurchaseOrderDet.setCreateUserId(user.getUserId());
            omPurchaseOrderDet.setCreateTime(new Date());
            omPurchaseOrderDet.setIsDelete((byte) 1);
        }
        if (!omPurchaseOrder.getOmPurchaseOrderDetList().isEmpty()) {
            omPurchaseOrderDetMapper.insertList(omPurchaseOrder.getOmPurchaseOrderDetList());
        }

        return 1;
    }

    @Override
    public String findPurchaseMaterial(String purchaseOrderCode) {
        return omPurchaseOrderMapper.findPurchaseMaterial(purchaseOrderCode);
    }

    @Override
    public int update(OmPurchaseOrder entity) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        entity.setCreateUserId(user.getUserId());
        entity.setCreateTime(new Date());
        int i = omPurchaseOrderMapper.updateByPrimaryKeySelective(entity);

        //删除该采购订单下的所有详情表
        Example detExample = new Example(OmPurchaseOrderDet.class);
        Example.Criteria detCriteria = detExample.createCriteria();
        detCriteria.andEqualTo("purchaseOrderId", entity.getPurchaseOrderId());
        omPurchaseOrderDetMapper.deleteByExample(detExample);

        for (OmPurchaseOrderDet omPurchaseOrderDet : entity.getOmPurchaseOrderDetList()) {
            omPurchaseOrderDet.setPurchaseOrderId(entity.getPurchaseOrderId());
            omPurchaseOrderDet.setStatus((byte) 1);
            omPurchaseOrderDet.setOrgId(user.getOrganizationId());
            omPurchaseOrderDet.setCreateUserId(user.getUserId());
            omPurchaseOrderDet.setCreateTime(new Date());
            omPurchaseOrderDet.setIsDelete((byte) 1);
        }
        if (StringUtils.isNotEmpty(entity.getOmPurchaseOrderDetList())) {
            omPurchaseOrderDetMapper.insertList(entity.getOmPurchaseOrderDetList());
        }

        return i;
    }
}
