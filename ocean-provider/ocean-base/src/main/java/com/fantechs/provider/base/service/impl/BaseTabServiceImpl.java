package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseTabDto;
import com.fantechs.common.base.general.entity.basic.BaseTab;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseTabMapper;
import com.fantechs.provider.base.service.BaseTabService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2021/01/08.
 */
@Service
public class BaseTabServiceImpl extends BaseService<BaseTab> implements BaseTabService {

    @Resource
    private BaseTabMapper baseTabMapper;

    @Override
    public List<BaseTabDto> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            if (!StringUtils.isEmpty(user)) {
                map.put("orgId", user.getOrganizationId());
            }
        }
        return baseTabMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseTab baseTab) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        return baseTabMapper.insertUseGeneratedKeys(baseTab);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseTab baseTab) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        return baseTabMapper.updateByPrimaryKeySelective(baseTab);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(List<BaseTab> baseTabs) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        int i=0;
        if (StringUtils.isNotEmpty(baseTabs)){
            for (BaseTab baseTab : baseTabs) {
                BaseTab baseTab1 = baseTabMapper.selectByPrimaryKey(baseTab.getTabId());
                if (StringUtils.isEmpty(baseTab1)){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                }
                i += baseTabMapper.deleteByPrimaryKey(baseTab.getTabId());
            }
        }
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertList(List<BaseTab> baseTabs) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        return baseTabMapper.insertList(baseTabs);
    }
}
