package com.fantechs.provider.om.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmHtSalesOrder;
import com.fantechs.common.base.general.entity.om.OmHtSalesOrderDet;
import com.fantechs.common.base.general.entity.om.OmSalesOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.ReflectUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.mapper.OmSalesOrderDetMapper;
import com.fantechs.provider.om.service.OmSalesOrderDetService;
import com.fantechs.provider.om.service.ht.OmHtSalesOrderDetService;
import org.springframework.context.annotation.Bean;
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
public class OmSalesOrderDetServiceImpl extends BaseService<OmSalesOrderDet> implements OmSalesOrderDetService {

    @Resource
    private OmSalesOrderDetMapper omSalesOrderDetMapper;
    @Resource
    private OmHtSalesOrderDetService omHtSalesOrderDetService;

    @Override
    public int saveDto(OmSalesOrderDetDto omSalesOrderDetDto, String customerOrderCode, SysUser currentUserInfo) {
        OmSalesOrderDet omSalesOrderDet = new OmSalesOrderDet();

        BeanUtils.autoFillEqFields(omSalesOrderDetDto, omSalesOrderDet);

        return this.save(omSalesOrderDet, customerOrderCode, currentUserInfo);
    }

    private int save(OmSalesOrderDet omSalesOrderDet, String customerOrderCode, SysUser currentUserInfo) {
//        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
//        if(StringUtils.isEmpty(currentUserInfo)) {
//            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
//        }


        if(StringUtils.isEmpty(omSalesOrderDet.getSalesOrderId())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "销售订单ID不能为空");
        }
        if(StringUtils.isEmpty(omSalesOrderDet.getSourceLineNumber())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "行号不能为空");
        }

        //数据库有限制这个字段不能为空
        if(StringUtils.isEmpty(omSalesOrderDet.getProjectCode())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "项目号不能为空");
        }

        omSalesOrderDet.setSalesOrderDetId(null);

//        //根据销售订单ID获取客户订单号
//        String customerOrderCode = omSalesOrderMapper.getCustomerOrderCode(omSalesOrderDet.getSalesOrderId());
//        if(StringUtils.isEmpty(customerOrderCode)) {
//            throw new BizErrorException(ErrorCodeEnum.GL9999404);
//        }
        omSalesOrderDet.setCustomerOrderLineNumber(customerOrderCode + omSalesOrderDet.getSourceLineNumber());

        omSalesOrderDet.setOrgId(currentUserInfo.getOrganizationId());
        omSalesOrderDet.setCreateTime(new Date());
        omSalesOrderDet.setCreateUserId(currentUserInfo.getUserId());
        omSalesOrderDet.setModifiedUserId(currentUserInfo.getUserId());
        omSalesOrderDet.setModifiedTime(new Date());

        int result = omSalesOrderDetMapper.insertUseGeneratedKeys(omSalesOrderDet);
        recordHistory(omSalesOrderDet);

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
            OmSalesOrderDet omSalesOrderDet = omSalesOrderDetMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(omSalesOrderDet)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            recordHistory(omSalesOrderDet);
        }
        if(omSalesOrderDetMapper.deleteByIds(ids)<=0) {
            return 0;
        }
        return 1;
    }

    @Override
    public int updateDto(OmSalesOrderDetDto omSalesOrderDetDto, SysUser currentUserInfo) {
        OmSalesOrderDet omSalesOrderDet = new OmSalesOrderDet();

        BeanUtils.autoFillEqFields(omSalesOrderDetDto, omSalesOrderDet);

        if(this.update(omSalesOrderDet, currentUserInfo) <= 0) {
            return 0;
        }

        return 1;
    }

    private int update(OmSalesOrderDet omSalesOrderDet, SysUser currentUserInfo) {
//        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
//        if(StringUtils.isEmpty(currentUserInfo)) {
//            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
//        }

        omSalesOrderDet.setModifiedUserId(currentUserInfo.getUserId());
        omSalesOrderDet.setModifiedTime(new Date());


        if(omSalesOrderDetMapper.updateByPrimaryKeySelective(omSalesOrderDet)<=0) {
            return 0;
        }
        recordHistory(omSalesOrderDet);
        return 1;
    }

    @Override
    public List<OmSalesOrderDetDto> findList(Map<String, Object> map) {
        return omSalesOrderDetMapper.findList(map);
    }

    private void recordHistory(OmSalesOrderDet omSalesOrderDet){
//        OmHtSalesOrderDet omHtSalesOrderDet = new OmHtSalesOrderDet();
//        if(StringUtils.isEmpty(omSalesOrderDet)){
//            return;
//        }
//        BeanUtils.autoFillEqFields(omSalesOrderDet, omHtSalesOrderDet);
//        omHtSalesOrderDetService.save(omHtSalesOrderDet);
    }
}
