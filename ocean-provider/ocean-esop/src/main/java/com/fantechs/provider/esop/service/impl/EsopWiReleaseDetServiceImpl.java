package com.fantechs.provider.esop.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.esop.EsopWiReleaseDetDto;
import com.fantechs.common.base.general.entity.esop.EsopWiReleaseDet;
import com.fantechs.common.base.general.entity.esop.search.SearchEsopWiReleaseDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.esop.mapper.EsopWiReleaseDetMapper;
import com.fantechs.provider.esop.service.EsopWiReleaseDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/07/08.
 */
@Service
public class EsopWiReleaseDetServiceImpl extends BaseService<EsopWiReleaseDet> implements EsopWiReleaseDetService {

    @Resource
    private EsopWiReleaseDetMapper esopWiReleaseDetMapper;

    @Override
    public List<EsopWiReleaseDetDto> findList(SearchEsopWiReleaseDet searchEsopWiReleaseDet) {
        if(StringUtils.isEmpty(searchEsopWiReleaseDet.getOrgId())){
            SysUser sysUser = currentUser();
            searchEsopWiReleaseDet.setOrgId(sysUser.getOrganizationId());
        }
        return esopWiReleaseDetMapper.findList(searchEsopWiReleaseDet);
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
