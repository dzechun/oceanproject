package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseWorkingAreaReWDto;
import com.fantechs.common.base.general.dto.eam.EamWiReleaseDetDto;
import com.fantechs.common.base.general.dto.eam.EamWiReleaseDto;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.BaseWorker;
import com.fantechs.common.base.general.entity.eam.EamEquipment;
import com.fantechs.common.base.general.entity.eam.EamWiBom;
import com.fantechs.common.base.general.entity.eam.EamWiRelease;
import com.fantechs.common.base.general.entity.eam.EamWiReleaseDet;
import com.fantechs.common.base.general.entity.eam.history.EamHtWiRelease;
import com.fantechs.common.base.general.entity.eam.history.EamHtWiReleaseDet;
import com.fantechs.common.base.general.entity.eam.search.SearchEamWiRelease;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.*;
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
        if(StringUtils.isEmpty(eamWiReleaseDto.getWiReleaseCode())) throw new BizErrorException("添加失败，发布编码不能为空");
        if(StringUtils.isEmpty(eamWiReleaseDto.getProLineId())) throw new BizErrorException("添加失败，产线id不能为空");
        Example example1 = new Example(EamWiRelease.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("orgId", sysUser.getOrganizationId());
        criteria1.andEqualTo("wiReleaseCode",eamWiReleaseDto.getWiReleaseCode());
        EamWiRelease wiRelease = eamWiReleaseMapper.selectOneByExample(example1);
        if(StringUtils.isNotEmpty(wiRelease)) throw new BizErrorException("添加失败，已存在发布编码");
        example1.clear();
        EamWiRelease eamWiRelease = new EamWiRelease();
        BeanUtils.autoFillEqFields(eamWiReleaseDto, eamWiRelease);
        eamWiRelease.setCreateUserId(sysUser.getUserId());
        eamWiRelease.setCreateTime(new Date());
        eamWiRelease.setModifiedUserId(sysUser.getUserId());
        eamWiRelease.setModifiedTime(new Date());
        eamWiRelease.setStatus((byte)1);
        eamWiRelease.setReleaseStatus((byte)1);
        eamWiRelease.setOrgId(sysUser.getOrganizationId());
        int i = eamWiReleaseMapper.insertUseGeneratedKeys(eamWiRelease);
        List<EamWiReleaseDet> dets = saveDet(eamWiReleaseDto,sysUser,eamWiRelease.getWiReleaseId());

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


    @Override
    public int update(EamWiReleaseDto eamWiReleaseDto) {
        SysUser sysUser = currentUser();
        if(StringUtils.isEmpty(eamWiReleaseDto.getWiReleaseId()))
            throw new BizErrorException("id不能为空");
        eamWiReleaseMapper.updateByPrimaryKey(eamWiReleaseDto);

        Example example = new Example(EamWiRelease.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("wiReleaseId", eamWiReleaseDto.getWiReleaseId());
        EamWiRelease eamWiRelease = eamWiReleaseMapper .selectOneByExample(example);
        example.clear();

        Example detExample = new Example(EamWiReleaseDet.class);
        Example.Criteria detCriteria = detExample.createCriteria();
        detCriteria.andEqualTo("wiReleaseId", eamWiReleaseDto.getWiReleaseId());
        eamWiReleaseDetMapper.deleteByExample(detExample);
        detExample.clear();


        //保存履历表
        EamHtWiRelease eamHtWiRelease = new EamHtWiRelease();
        BeanUtils.autoFillEqFields(eamWiRelease,eamHtWiRelease);
        int i = eamHtWiReleaseMapper.insertUseGeneratedKeys(eamHtWiRelease);

        saveDet(eamWiReleaseDto,sysUser,eamWiRelease.getWiReleaseId());
        return i;
    }

    @Override
    public int censor(EamWiRelease eamWiRelease) {
        SysUser sysUser = currentUser();
        if(StringUtils.isEmpty(eamWiRelease.getWiReleaseId()))
            throw new BizErrorException("发布id不能为空");
        if(StringUtils.isEmpty(eamWiRelease.getProLineId()))
            throw new BizErrorException("产线id不能为空");

        Example example = new Example(EamWiRelease.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", sysUser.getOrganizationId()).andEqualTo("proLineId", eamWiRelease.getProLineId());
        EamWiRelease oldWiRelease = eamWiReleaseMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(oldWiRelease)) {
            oldWiRelease.setStatus((byte)0);
            eamWiReleaseMapper.updateByPrimaryKey(oldWiRelease);
        }
        example.clear();

        eamWiRelease.setStatus((byte)1);
        int i = eamWiReleaseMapper.updateByPrimaryKey(eamWiRelease);
        return i;
    }


    public  List<EamWiReleaseDet> saveDet(EamWiReleaseDto eamWiReleaseDto ,SysUser sysUser,Long id){
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
                wiReleaseDet.setWiReleaseId(id);
                dets.add(wiReleaseDet);
            }
        }
        eamWiReleaseDetMapper.insertList(dets);
        return  dets;
    }


    @Override
    public int batchDelete(String ids) {
        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            Example example = new Example(EamWiReleaseDet.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("wiReleaseId", id);
            eamWiReleaseDetMapper.deleteByExample(example);
            example.clear();
        }
        return eamWiReleaseMapper.deleteByIds(ids);
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
