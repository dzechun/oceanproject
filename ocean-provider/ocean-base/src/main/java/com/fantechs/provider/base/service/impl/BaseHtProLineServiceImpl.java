package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProLine;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProLine;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtProLineMapper;
import com.fantechs.provider.base.service.BaseHtProLineService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class BaseHtProLineServiceImpl extends BaseService<BaseHtProLine> implements BaseHtProLineService {

    @Resource
    private BaseHtProLineMapper baseHtProLineMapper;

    @Override
    public List<BaseHtProLine> selectHtProLines(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        map.put("orgId", user.getOrganizationId());
        return baseHtProLineMapper.selectHtProLines(map);
    }
}
