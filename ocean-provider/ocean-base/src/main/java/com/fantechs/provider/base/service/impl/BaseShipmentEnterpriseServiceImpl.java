package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseShipmentEnterpriseDto;
import com.fantechs.common.base.general.entity.basic.BaseShipmentEnterprise;
import com.fantechs.common.base.general.entity.basic.history.BaseHtShipmentEnterprise;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtShipmentEnterpriseMapper;
import com.fantechs.provider.base.mapper.BaseShipmentEnterpriseMapper;
import com.fantechs.provider.base.service.BaseShipmentEnterpriseService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/12/16.
 */
@Service
public class BaseShipmentEnterpriseServiceImpl extends BaseService<BaseShipmentEnterprise> implements BaseShipmentEnterpriseService {

    @Resource
    private BaseShipmentEnterpriseMapper baseShipmentEnterpriseMapper;
    @Resource
    private BaseHtShipmentEnterpriseMapper baseHtShipmentEnterpriseMapper;

    @Override
    public int save(BaseShipmentEnterprise baseShipmentEnterprise) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseShipmentEnterprise.class);
        Example.Criteria criteria1 = example.createCriteria();
        //判断编码是否重复
        criteria1.andEqualTo("organizationId", user.getOrganizationId());
        criteria1.andEqualTo("shipmentEnterpriseCode",baseShipmentEnterprise.getShipmentEnterpriseCode());
        BaseShipmentEnterprise baseShipmentEnterprise1 = baseShipmentEnterpriseMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseShipmentEnterprise1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        Example example1 = new Example(BaseShipmentEnterprise.class);
        Example.Criteria criteria2 = example1.createCriteria();
        //判断名称是否重复
        criteria2.andEqualTo("organizationId", user.getOrganizationId());
        criteria2.andEqualTo("shipmentEnterpriseName",baseShipmentEnterprise.getShipmentEnterpriseCode());
        BaseShipmentEnterprise baseShipmentEnterprise2 = baseShipmentEnterpriseMapper.selectOneByExample(example1);
        if (StringUtils.isNotEmpty(baseShipmentEnterprise2)){
            throw new BizErrorException("物流商名称已经存在");
        }

        baseShipmentEnterprise.setCreateTime(new Date());
        baseShipmentEnterprise.setCreateUserId(user.getUserId());
        baseShipmentEnterprise.setModifiedTime(new Date());
        baseShipmentEnterprise.setModifiedUserId(user.getUserId());
        baseShipmentEnterprise.setStatus(StringUtils.isEmpty(baseShipmentEnterprise.getStatus())?1:baseShipmentEnterprise.getStatus());
        baseShipmentEnterprise.setOrganizationId(user.getOrganizationId());
        int i = baseShipmentEnterpriseMapper.insertUseGeneratedKeys(baseShipmentEnterprise);

        BaseHtShipmentEnterprise baseHtShipmentEnterprise = new BaseHtShipmentEnterprise();
        BeanUtils.copyProperties(baseShipmentEnterprise,baseHtShipmentEnterprise);
        baseHtShipmentEnterpriseMapper.insert(baseHtShipmentEnterprise);

        return i;
    }

    @Override
    public int update(BaseShipmentEnterprise baseShipmentEnterprise) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseShipmentEnterprise.class);
        Example.Criteria criteria1 = example.createCriteria();
        //判断编码是否重复
        criteria1.andEqualTo("organizationId", user.getOrganizationId());
        criteria1.andEqualTo("shipmentEnterpriseCode",baseShipmentEnterprise.getShipmentEnterpriseCode())
                .andNotEqualTo("shipmentEnterpriseId",baseShipmentEnterprise.getShipmentEnterpriseId());
        BaseShipmentEnterprise baseShipmentEnterprise1 = baseShipmentEnterpriseMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseShipmentEnterprise1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        Example example1 = new Example(BaseShipmentEnterprise.class);
        Example.Criteria criteria2 = example1.createCriteria();
        //判断名称是否重复
        criteria2.andEqualTo("organizationId", user.getOrganizationId());
        criteria2.andEqualTo("shipmentEnterpriseName",baseShipmentEnterprise.getShipmentEnterpriseName())
                .andNotEqualTo("shipmentEnterpriseId",baseShipmentEnterprise.getShipmentEnterpriseId());
        BaseShipmentEnterprise baseShipmentEnterprise2 = baseShipmentEnterpriseMapper.selectOneByExample(example1);
        if (StringUtils.isNotEmpty(baseShipmentEnterprise2)){
            throw new BizErrorException("物流商名称已存在");
        }

        baseShipmentEnterprise.setModifiedUserId(user.getUserId());
        baseShipmentEnterprise.setModifiedTime(new Date());
        baseShipmentEnterprise.setOrganizationId(user.getOrganizationId());

        BaseHtShipmentEnterprise baseHtShipmentEnterprise = new BaseHtShipmentEnterprise();
        BeanUtils.copyProperties(baseShipmentEnterprise,baseHtShipmentEnterprise);
        baseHtShipmentEnterpriseMapper.insert(baseHtShipmentEnterprise);

        return baseShipmentEnterpriseMapper.updateByPrimaryKeySelective(baseShipmentEnterprise);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<BaseHtShipmentEnterprise> baseHtShipmentEnterprises = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            BaseShipmentEnterprise baseShipmentEnterprise = baseShipmentEnterpriseMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseShipmentEnterprise)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtShipmentEnterprise baseHtShipmentEnterprise = new BaseHtShipmentEnterprise();
            BeanUtils.copyProperties(baseShipmentEnterprise,baseHtShipmentEnterprise);
            baseHtShipmentEnterprises.add(baseHtShipmentEnterprise);
        }

        baseHtShipmentEnterpriseMapper.insertList(baseHtShipmentEnterprises);
        return baseShipmentEnterpriseMapper.deleteByIds(ids);
    }

    @Override
    public List<BaseShipmentEnterpriseDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return baseShipmentEnterpriseMapper.findList(map);
    }
}
