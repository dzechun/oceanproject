package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.history.BaseHtLabel;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabel;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtLabelMapper;
import com.fantechs.provider.base.service.BaseHtLabelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* @author Mr.Lei
* @create 2020/12/17.
*/
@Service
public class BaseHtLabelServiceImpl extends BaseService<BaseHtLabel> implements BaseHtLabelService {

    @Resource
    private BaseHtLabelMapper baseHtLabelMapper;

    @Override
    public List<BaseHtLabel> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseHtLabelMapper.findList(map);
    }
}
