package com.fantechs.security.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.security.SysCustomFormDto;
import com.fantechs.common.base.general.entity.security.SysCustomForm;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.security.mapper.SysCustomFormMapper;
import com.fantechs.security.service.SysCustomFormService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/08.
 */
@Service
public class SysCustomFormServiceImpl  extends BaseService<SysCustomForm> implements SysCustomFormService {

         @Resource
         private SysCustomFormMapper sysCustomFormMapper;

    @Override
    public List<SysCustomFormDto> findList(Map<String, Object> map) {
        // 获取登录用户
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return sysCustomFormMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SysCustomForm sysCustomForm) {
        // 获取登录用户
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        sysCustomForm.setOrgId(user.getOrganizationId());
        return sysCustomFormMapper.updateByPrimaryKey(sysCustomForm);
    }
}
