package com.fantechs.provider.om.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDto;
import com.fantechs.common.base.general.entity.om.OmSalesOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.mapper.OmSalesOrderMapper;
import com.fantechs.provider.om.service.OmSalesOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/19.
 */
@Service
public class OmSalesOrderServiceImpl extends BaseService<OmSalesOrder> implements OmSalesOrderService {

    @Resource
    private OmSalesOrderMapper omSalesOrderMapper;

    @Override
    public int save(OmSalesOrder omSalesOrder) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        if(StringUtils.isEmpty(omSalesOrder.getSalesOrderCode())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "销售订单号不能为空");
        }

        if(StringUtils.isEmpty(omSalesOrder.getContractCode())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "合同号不能为空");
        }

        if(StringUtils.isEmpty(omSalesOrder.getCustomerOrderCode())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "客户订单号不能为空");
        }

        omSalesOrder.setSalesOrderId(null);
        omSalesOrder.setSalesOrderCode(CodeUtils.getId("SEORD"));
        omSalesOrder.setOrgId(currentUserInfo.getOrganizationId());
        omSalesOrder.setCreateTime(new Date());
        omSalesOrder.setCreateUserId(currentUserInfo.getUserId());
        omSalesOrder.setModifiedUserId(currentUserInfo.getUserId());
        omSalesOrder.setModifiedTime(new Date());

        int result = omSalesOrderMapper.insertUseGeneratedKeys(omSalesOrder);

//        recordHistory(omSalesOrder, "新增");
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
        }
        if(omSalesOrderMapper.deleteByIds(ids)<=0) {
            return 0;
        }
        return 1;
    }

    @Override
    public int update(OmSalesOrder omSalesOrder) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty((currentUserInfo))) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        if(StringUtils.isEmpty(omSalesOrder.getSalesOrderCode())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "销售订单号不能为空");
        }

        if(StringUtils.isEmpty(omSalesOrder.getContractCode())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "合同号不能为空");
        }

        if(StringUtils.isEmpty(omSalesOrder.getCustomerOrderCode())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "客户订单号不能为空");
        }

        omSalesOrder.setModifiedUserId(currentUserInfo.getUserId());
        omSalesOrder.setModifiedTime(new Date());


        if(omSalesOrderMapper.updateByPrimaryKeySelective(omSalesOrder)<=0) {
            return 0;
        }
        return 1;
    }

    @Override
    public List<OmSalesOrderDto> findList(Map<String, Object> map) {
        return omSalesOrderMapper.findList(map);
    }


}
