package com.fantechs.security.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.security.SysCustomFormDto;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrganization;
import com.fantechs.common.base.general.entity.security.SysCustomForm;
import com.fantechs.common.base.general.entity.security.SysCustomFormDet;
import com.fantechs.common.base.general.entity.security.SysDefaultCustomForm;
import com.fantechs.common.base.general.entity.security.SysDefaultCustomFormDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.security.mapper.SysCustomFormDetMapper;
import com.fantechs.security.mapper.SysCustomFormMapper;
import com.fantechs.security.mapper.SysDefaultCustomFormMapper;
import com.fantechs.security.service.SysCustomFormService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

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
    private SysCustomFormDetMapper sysCustomFormDetMapper;

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

        ifCodeRepeat(sysCustomForm,user);

        sysCustomForm.setOrgId(user.getOrganizationId());
        return sysCustomFormMapper.updateByPrimaryKeySelective(sysCustomForm);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SysCustomForm sysCustomForm) {
        // 获取登录用户
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        ifCodeRepeat(sysCustomForm,user);

        sysCustomForm.setOrgId(user.getOrganizationId());
        return sysCustomFormMapper.insertSelective(sysCustomForm);
    }


    public void ifCodeRepeat(SysCustomForm sysCustomForm,SysUser user){
        Example example = new Example(SysCustomForm.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("customFormCode",sysCustomForm.getCustomFormCode())
                .andEqualTo("orgId",user.getOrganizationId());
        if(StringUtils.isNotEmpty(sysCustomForm.getCustomFormId())){
            criteria.andNotEqualTo("customFormId",sysCustomForm.getCustomFormId());
        }
        SysCustomForm customForm = sysCustomFormMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(customForm)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        // 获取登录用户
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            SysCustomForm sysCustomForm = sysCustomFormMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(sysCustomForm)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            Example example = new Example(SysCustomFormDet.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("customFormId",id);
            sysCustomFormDetMapper.deleteByExample(example);
        }

        return sysCustomFormMapper.deleteByIds(ids);
    }
}
