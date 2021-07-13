package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamWiReleaseDetDto;
import com.fantechs.common.base.general.entity.eam.EamWiReleaseDet;
import com.fantechs.common.base.general.entity.eam.search.SearchEamWiReleaseDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamWiReleaseDetMapper;
import com.fantechs.provider.eam.service.EamWiReleaseDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/07/08.
 */
@Service
public class EamWiReleaseDetServiceImpl extends BaseService<EamWiReleaseDet> implements EamWiReleaseDetService {

    @Resource
    private EamWiReleaseDetMapper eamWiReleaseDetMapper;

    @Override
    public List<EamWiReleaseDetDto> findList(SearchEamWiReleaseDet searchEamWiReleaseDet) {
        if(StringUtils.isEmpty(searchEamWiReleaseDet.getOrgId())){
            SysUser sysUser = currentUser();
            searchEamWiReleaseDet.setOrgId(sysUser.getOrganizationId());
        }
        return eamWiReleaseDetMapper.findList(searchEamWiReleaseDet);
    }

    /**
     * 获取当前登录用户
     * @return
     */
    private SysUser currentUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}
