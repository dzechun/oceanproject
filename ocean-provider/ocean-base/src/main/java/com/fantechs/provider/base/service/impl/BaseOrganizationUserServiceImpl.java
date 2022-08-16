package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationUserDto;
import com.fantechs.common.base.general.entity.basic.BaseOrganizationUser;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseOrganizationUserMapper;
import com.fantechs.provider.base.service.BaseOrganizationUserService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/12/29.
 */
@Service
public class BaseOrganizationUserServiceImpl extends BaseService<BaseOrganizationUser> implements BaseOrganizationUserService {

    @Resource
    private BaseOrganizationUserMapper baseOrganizationUserMapper;

    @Override
    public int save(BaseOrganizationUser baseOrganizationUser) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseOrganizationUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId",baseOrganizationUser.getOrganizationId())
                .andEqualTo("userId",baseOrganizationUser.getUserId());
        BaseOrganizationUser baseOrganizationUser1 = baseOrganizationUserMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseOrganizationUser1)){
            throw new BizErrorException("该绑定关系已存在");
        }

        return baseOrganizationUserMapper.insertUseGeneratedKeys(baseOrganizationUser);
    }

    @Override
    public int update(BaseOrganizationUser baseOrganizationUser) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseOrganizationUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId",baseOrganizationUser.getOrganizationId())
                .andEqualTo("userId",baseOrganizationUser.getUserId())
                .andNotEqualTo("organizationUserId",baseOrganizationUser.getOrganizationUserId());
        BaseOrganizationUser baseOrganizationUser1 = baseOrganizationUserMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseOrganizationUser1)){
            throw new BizErrorException("该绑定关系已存在");
        }

        return baseOrganizationUserMapper.updateByPrimaryKeySelective(baseOrganizationUser);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            BaseOrganizationUser baseOrganizationUser = baseOrganizationUserMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseOrganizationUser)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

        }

        return baseOrganizationUserMapper.deleteByIds(ids);
    }

    @Override
    public List<BaseOrganizationUserDto> findList(Map<String, Object> map) {
        return baseOrganizationUserMapper.findList(map);
    }
}
