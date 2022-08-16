package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseInAndOutRuleType;
import com.fantechs.common.base.general.entity.basic.history.BaseHtDept;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInAndOutRuleType;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtInAndOutRuleTypeMapper;
import com.fantechs.provider.base.mapper.BaseInAndOutRuleTypeMapper;
import com.fantechs.provider.base.service.BaseInAndOutRuleTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/14.
 */
@Service
public class BaseInAndOutRuleTypeServiceImpl extends BaseService<BaseInAndOutRuleType> implements BaseInAndOutRuleTypeService {

    @Resource
    private BaseInAndOutRuleTypeMapper baseInAndOutRuleTypeMapper;
    @Resource
    private BaseHtInAndOutRuleTypeMapper baseHtInAndOutRuleTypeMapper;

    @Override
    public List<BaseInAndOutRuleType> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseInAndOutRuleTypeMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseInAndOutRuleType baseInAndOutRuleType) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        baseInAndOutRuleType.setModifiedUserId(currentUser.getUserId());
        baseInAndOutRuleType.setModifiedTime(new Date());
        baseInAndOutRuleType.setOrgId(currentUser.getOrganizationId());
        int i = baseInAndOutRuleTypeMapper.updateByPrimaryKeySelective(baseInAndOutRuleType);

        //新增履历
        BaseHtInAndOutRuleType baseHtInAndOutRuleType =new BaseHtInAndOutRuleType();
        BeanUtils.copyProperties(baseInAndOutRuleType, baseHtInAndOutRuleType);
        baseHtInAndOutRuleTypeMapper.insertSelective(baseHtInAndOutRuleType);

        return i;
    }

}
