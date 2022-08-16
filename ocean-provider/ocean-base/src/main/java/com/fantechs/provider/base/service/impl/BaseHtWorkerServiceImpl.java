package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.history.BaseHtWorker;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtWorkerMapper;
import com.fantechs.provider.base.service.BaseHtWorkerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/25.
 */
@Service
public class BaseHtWorkerServiceImpl extends BaseService<BaseHtWorker> implements BaseHtWorkerService {

    @Resource
    private BaseHtWorkerMapper baseHtWorkerMapper;

    @Override
    public int save(BaseHtWorker baseHtWorker) {
        return baseHtWorkerMapper.insertUseGeneratedKeys(baseHtWorker);
    }


    @Override
    public List<BaseHtWorker> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseHtWorkerMapper.findList(map);
    }
}
