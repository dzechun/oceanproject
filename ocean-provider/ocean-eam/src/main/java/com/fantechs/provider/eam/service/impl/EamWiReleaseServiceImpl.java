package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseWorkingAreaReWDto;
import com.fantechs.common.base.general.dto.eam.EamWiReleaseDetDto;
import com.fantechs.common.base.general.dto.eam.EamWiReleaseDto;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.BaseWorker;
import com.fantechs.common.base.general.entity.eam.EamWiRelease;
import com.fantechs.common.base.general.entity.eam.EamWiReleaseDet;
import com.fantechs.common.base.general.entity.eam.history.EamHtWiRelease;
import com.fantechs.common.base.general.entity.eam.history.EamHtWiReleaseDet;
import com.fantechs.common.base.general.entity.eam.search.SearchEamWiRelease;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamHtWiReleaseDetMapper;
import com.fantechs.provider.eam.mapper.EamHtWiReleaseMapper;
import com.fantechs.provider.eam.mapper.EamWiReleaseDetMapper;
import com.fantechs.provider.eam.mapper.EamWiReleaseMapper;
import com.fantechs.provider.eam.service.EamWiReleaseService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/08.
 */
@Service
public class EamWiReleaseServiceImpl extends BaseService<EamWiRelease> implements EamWiReleaseService {

    @Resource
    private EamWiReleaseMapper eamWiReleaseMapper;
    @Resource
    private EamHtWiReleaseMapper eamHtWiReleaseMapper ;
    @Resource
    private EamWiReleaseDetMapper eamWiReleaseDetMapper;
    @Resource
    private EamHtWiReleaseDetMapper eamHtWiReleaseDetMapper ;


    @Override
    public List<EamWiReleaseDto> findList(SearchEamWiRelease searchEamWiRelease) {
        if(StringUtils.isEmpty(searchEamWiRelease.getOrgId())){
            SysUser sysUser = currentUser();
            searchEamWiRelease.setOrgId(sysUser.getOrganizationId());
        }
        return eamWiReleaseMapper.findList(searchEamWiRelease);
    }


    @Override
    public int save(EamWiReleaseDto eamWiReleaseDto) {
        SysUser sysUser = currentUser();
        Example example1 = new Example(EamWiRelease.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("orgId", sysUser.getOrganizationId());
        criteria1.andEqualTo("wiReleaseCode",eamWiReleaseDto.getWiReleaseCode());
        EamWiRelease wiRelease = eamWiReleaseMapper.selectOneByExample(example1);
        if(StringUtils.isNotEmpty(wiRelease)) throw new BizErrorException("添加失败，已存在发布编码");

        EamWiRelease eamWiRelease = new EamWiRelease();
        BeanUtils.autoFillEqFields(eamWiReleaseDto, eamWiRelease);
        eamWiRelease.setCreateUserId(sysUser.getUserId());
        eamWiRelease.setCreateTime(new Date());
        eamWiRelease.setModifiedUserId(sysUser.getUserId());
        eamWiRelease.setModifiedTime(new Date());
        eamWiRelease.setStatus(StringUtils.isEmpty(eamWiReleaseDto.getStatus())?1: eamWiReleaseDto.getStatus());
        eamWiRelease.setOrgId(sysUser.getOrganizationId());
        int i = eamWiReleaseMapper.insertUseGeneratedKeys(eamWiRelease);
        List<EamWiReleaseDet> dets = new ArrayList<>();
        if(StringUtils.isNotEmpty(eamWiReleaseDto.getEamWiReleaseDetDtos())) {
            for (EamWiReleaseDetDto eamWiReleaseDetDto :  eamWiReleaseDto.getEamWiReleaseDetDtos()) {
                EamWiReleaseDet wiReleaseDet = new EamWiReleaseDet();
                BeanUtils.autoFillEqFields(eamWiReleaseDetDto, wiReleaseDet);
                wiReleaseDet.setCreateUserId(sysUser.getUserId());
                wiReleaseDet.setCreateTime(new Date());
                wiReleaseDet.setModifiedUserId(sysUser.getUserId());
                wiReleaseDet.setModifiedTime(new Date());
                wiReleaseDet.setStatus(StringUtils.isEmpty(eamWiReleaseDto.getStatus())?1: eamWiReleaseDto.getStatus());
                wiReleaseDet.setOrgId(sysUser.getOrganizationId());
                wiReleaseDet.setWorkInstructionId(eamWiRelease.getWiReleaseId());
                dets.add(wiReleaseDet);
            }
        }
        eamWiReleaseDetMapper.insertList(dets);

        //添加履历表
        EamHtWiRelease eamHtWiRelease = new EamHtWiRelease();
        BeanUtils.autoFillEqFields(eamWiRelease, eamHtWiRelease);
        eamHtWiReleaseMapper.insertUseGeneratedKeys(eamHtWiRelease);

        List<EamHtWiReleaseDet> htDets = new ArrayList<>();
        if(StringUtils.isNotEmpty(dets)) {
            for (EamWiReleaseDet eamWiReleaseDet :  dets) {
                EamHtWiReleaseDet htWiReleaseDet = new EamHtWiReleaseDet();
                BeanUtils.autoFillEqFields(eamWiReleaseDet, htWiReleaseDet);
                htDets.add(htWiReleaseDet);
            }
        }
        eamHtWiReleaseDetMapper.insertList(htDets);
        return i;
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
