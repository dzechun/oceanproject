package com.fantechs.security.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.security.SysCustomFormDto;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrganization;
import com.fantechs.common.base.general.entity.security.SysCustomForm;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.security.mapper.SysCustomFormMapper;
import com.fantechs.security.service.SysCustomFormService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2021/01/08.
 */
@Service
public class SysCustomFormServiceImpl extends BaseService<SysCustomForm> implements SysCustomFormService {

    @Resource
    private SysCustomFormMapper sysCustomFormMapper;

    @Resource
    private BaseFeignApi baseFeignApi;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SysCustomForm sysCustomForm) {
        List<SysCustomForm> dets = new ArrayList<>();
        dets.add(sysCustomForm);
        List<BaseOrganizationDto> organizationDtos = baseFeignApi.findOrganizationList(new SearchBaseOrganization()).getData();
        if(!organizationDtos.isEmpty()){
            for (BaseOrganizationDto org : organizationDtos){
                if(!org.getOrganizationId().equals(sysCustomForm.getOrgId())){
                    SysCustomForm form = new SysCustomForm();
                    BeanUtil.copyProperties(sysCustomForm, form);
                    form.setOrgId(org.getOrganizationId());
                    dets.add(form);
                }
            }
        }
        return sysCustomFormMapper.insertList(dets);
    }
}
