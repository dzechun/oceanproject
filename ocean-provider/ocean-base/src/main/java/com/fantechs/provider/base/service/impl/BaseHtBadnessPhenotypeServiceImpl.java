package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessPhenotype;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtBadnessPhenotypeMapper;
import com.fantechs.provider.base.service.BaseHtBadnessPhenotypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/07.
 */
@Service
public class BaseHtBadnessPhenotypeServiceImpl extends BaseService<BaseHtBadnessPhenotype> implements BaseHtBadnessPhenotypeService {

    @Resource
    private BaseHtBadnessPhenotypeMapper baseHtBadnessPhenotypeMapper;

    @Override
    public List<BaseHtBadnessPhenotype> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseHtBadnessPhenotypeMapper.findList(map);
    }
}
