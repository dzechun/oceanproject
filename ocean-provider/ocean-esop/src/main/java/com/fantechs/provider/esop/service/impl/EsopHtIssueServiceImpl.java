package com.fantechs.provider.esop.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.esop.history.EsopHtIssue;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.esop.mapper.EsopHtIssueMapper;
import com.fantechs.provider.esop.service.EsopHtIssueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/07.
 */
@Service
public class EsopHtIssueServiceImpl extends BaseService<EsopHtIssue> implements EsopHtIssueService {

    @Resource
    private EsopHtIssueMapper esopHtIssueMapper;

    @Override
    public List<EsopHtIssue> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return esopHtIssueMapper.findHtList(map);
    }
}
