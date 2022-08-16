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
import com.fantechs.security.mapper.SysDefaultCustomFormDetMapper;
import com.fantechs.security.mapper.SysDefaultCustomFormMapper;
import com.fantechs.security.service.SysCustomFormService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
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
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private SysDefaultCustomFormMapper sysDefaultCustomFormMapper;
    @Resource
    private SysDefaultCustomFormDetMapper sysDefaultCustomFormDetMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveInAllOrg(SysCustomForm sysCustomForm) {
        // 获取登录用户
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        ifCodeRepeat(sysCustomForm,user);
        sysCustomForm.setOrgId(user.getOrganizationId());

        //默认表新增
        SysDefaultCustomForm defaultCustomForm = new SysDefaultCustomForm();
        BeanUtil.copyProperties(sysCustomForm, defaultCustomForm);
        defaultCustomForm.setOrgId(null);
        sysDefaultCustomFormMapper.insertSelective(defaultCustomForm);

        //全组织新增
        List<SysCustomForm> formList = new LinkedList<>();
        formList.add(sysCustomForm);
        List<BaseOrganizationDto> organizationDtos = baseFeignApi.findOrganizationList(new SearchBaseOrganization()).getData();
        if(!organizationDtos.isEmpty()){
            for (BaseOrganizationDto org : organizationDtos){
                if(!org.getOrganizationId().equals(sysCustomForm.getOrgId())){
                    SysCustomForm form = new SysCustomForm();
                    BeanUtil.copyProperties(sysCustomForm, form);
                    form.setOrgId(org.getOrganizationId());
                    formList.add(form);
                }
            }
        }

        return sysCustomFormMapper.insertList(formList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateInAllOrg(SysCustomForm sysCustomForm) {
        // 获取登录用户
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        ifCodeRepeat(sysCustomForm,user);
        sysCustomForm.setOrgId(user.getOrganizationId());

        int i = 0;
        //获取原数据
        SysCustomForm oldCustomForm = sysCustomFormMapper.selectByPrimaryKey(sysCustomForm.getCustomFormId());

        SysCustomForm subForm = new SysCustomForm();
        if(StringUtils.isNotEmpty(sysCustomForm.getSubId())) {
            subForm = sysCustomFormMapper.selectByPrimaryKey(sysCustomForm.getSubId());
        }

        //默认表修改
        Example example = new Example(SysDefaultCustomForm.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("customFormCode",oldCustomForm.getCustomFormCode());
        SysDefaultCustomForm defaultCustomForm = sysDefaultCustomFormMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(defaultCustomForm)) {
            defaultCustomForm.setCustomFormCode(sysCustomForm.getCustomFormCode());
            defaultCustomForm.setCustomFormName(sysCustomForm.getCustomFormName());
            defaultCustomForm.setFromRout(sysCustomForm.getFromRout());
            defaultCustomForm.setStatus(sysCustomForm.getStatus());
            defaultCustomForm.setOrgId(null);
            if (StringUtils.isNotEmpty(sysCustomForm.getSubId())) {
                example.clear();
                Example.Criteria criteria1 = example.createCriteria();
                criteria1.andEqualTo("customFormCode", subForm.getCustomFormCode());
                SysDefaultCustomForm defaultSubForm = sysDefaultCustomFormMapper.selectOneByExample(example);
                defaultCustomForm.setSubId(StringUtils.isNotEmpty(defaultSubForm)?defaultSubForm.getCustomFormId():null);
            }
            sysDefaultCustomFormMapper.updateByPrimaryKeySelective(defaultCustomForm);
        }

        //全组织修改
        Example example2 = new Example(SysCustomForm.class);
        Example.Criteria criteria2 = example2.createCriteria();
        criteria2.andEqualTo("customFormCode",oldCustomForm.getCustomFormCode());
        List<SysCustomForm> formList = sysCustomFormMapper.selectByExample(example2);
        for (SysCustomForm customForm : formList){
            customForm.setCustomFormCode(sysCustomForm.getCustomFormCode());
            customForm.setCustomFormName(sysCustomForm.getCustomFormName());
            customForm.setFromRout(sysCustomForm.getFromRout());
            customForm.setStatus(sysCustomForm.getStatus());
            if(StringUtils.isNotEmpty(sysCustomForm.getSubId())) {
                example2.clear();
                Example.Criteria criteria3 = example2.createCriteria();
                criteria3.andEqualTo("customFormCode",subForm.getCustomFormCode())
                        .andEqualTo("orgId",customForm.getOrgId());
                SysCustomForm subFormInOrg = sysCustomFormMapper.selectOneByExample(example2);
                customForm.setSubId(StringUtils.isNotEmpty(subFormInOrg)?subFormInOrg.getCustomFormId():null);
            }
            i += sysCustomFormMapper.updateByPrimaryKeySelective(customForm);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteInAllOrg(String ids) {
        int i = 0;

        List<SysCustomForm> formList = sysCustomFormMapper.selectByIds(ids);
        for (SysCustomForm sysCustomForm : formList){
            //删除默认表单
            Example example1 = new Example(SysDefaultCustomForm.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("customFormCode",sysCustomForm.getCustomFormCode());
            SysDefaultCustomForm defaultCustomForm = sysDefaultCustomFormMapper.selectOneByExample(example1);
            if(StringUtils.isNotEmpty(defaultCustomForm)) {
                Example example2 = new Example(SysDefaultCustomFormDet.class);
                Example.Criteria criteria2 = example2.createCriteria();
                criteria2.andEqualTo("customFormId", defaultCustomForm.getCustomFormId());
                sysDefaultCustomFormDetMapper.deleteByExample(example2);
                sysDefaultCustomFormMapper.deleteByExample(example1);
            }

            //全组织删除
            Example example3 = new Example(SysCustomForm.class);
            Example.Criteria criteria3 = example3.createCriteria();
            criteria3.andEqualTo("customFormCode",sysCustomForm.getCustomFormCode());
            List<SysCustomForm> formList1 = sysCustomFormMapper.selectByExample(example3);
            for(SysCustomForm sysCustomForm1 : formList1){
                //删除明细
                Example example4 = new Example(SysCustomFormDet.class);
                Example.Criteria criteria4 = example4.createCriteria();
                criteria4.andEqualTo("customFormId",sysCustomForm1.getCustomFormId());
                sysCustomFormDetMapper.deleteByExample(example4);
            }
            i = sysCustomFormMapper.deleteByExample(example3);
        }

        return i;
    }

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
