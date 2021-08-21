package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentJigDto;
import com.fantechs.common.base.general.dto.eam.EamEquipmentJigListDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentJig;
import com.fantechs.common.base.general.entity.eam.EamEquipmentJigList;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaterial;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaterialList;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentJig;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentMaterial;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamEquipmentJigListMapper;
import com.fantechs.provider.eam.mapper.EamEquipmentJigMapper;
import com.fantechs.provider.eam.mapper.EamHtEquipmentJigMapper;
import com.fantechs.provider.eam.service.EamEquipmentJigService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/21.
 */
@Service
public class EamEquipmentJigServiceImpl extends BaseService<EamEquipmentJig> implements EamEquipmentJigService {

    @Resource
    private EamEquipmentJigMapper eamEquipmentJigMapper;
    @Resource
    private EamHtEquipmentJigMapper eamHtEquipmentJigMapper;
    @Resource
    private EamEquipmentJigListMapper eamEquipmentJigListMapper;

    @Override
    public List<EamEquipmentJigDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return eamEquipmentJigMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamEquipmentJigDto record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(EamEquipmentJig.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentId", record.getEquipmentId());
        EamEquipmentJig eamEquipmentJig = eamEquipmentJigMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamEquipmentJig)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        eamEquipmentJigMapper.insertUseGeneratedKeys(record);

        //新增明细
        List<EamEquipmentJigListDto> eamEquipmentJigListDtos = record.getList();
        if(StringUtils.isNotEmpty(eamEquipmentJigListDtos)){
            for (EamEquipmentJigListDto eamEquipmentJigListDto : eamEquipmentJigListDtos) {
                eamEquipmentJigListDto.setEquipmentJigId(record.getEquipmentJigId());
                eamEquipmentJigListDto.setCreateUserId(user.getUserId());
                eamEquipmentJigListDto.setCreateTime(new Date());
                eamEquipmentJigListDto.setModifiedUserId(user.getUserId());
                eamEquipmentJigListDto.setModifiedTime(new Date());
                eamEquipmentJigListDto.setStatus(StringUtils.isEmpty(eamEquipmentJigListDto.getStatus())?1:eamEquipmentJigListDto.getStatus());
                eamEquipmentJigListDto.setOrgId(user.getOrganizationId());
            }
            eamEquipmentJigListMapper.insertList(eamEquipmentJigListDtos);
        }

        //履历
        EamHtEquipmentJig eamHtEquipmentJig = new EamHtEquipmentJig();
        BeanUtils.copyProperties(record, eamHtEquipmentJig);
        int i = eamHtEquipmentJigMapper.insert(eamHtEquipmentJig);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamEquipmentJigDto entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(EamEquipmentJig.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentId", entity.getEquipmentId())
                .andNotEqualTo("equipmentJigId",entity.getEquipmentJigId());
        EamEquipmentJig eamEquipmentJig = eamEquipmentJigMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamEquipmentJig)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());
        eamEquipmentJigMapper.updateByPrimaryKeySelective(entity);

        //删除原明细
        Example example1 = new Example(EamEquipmentJigList.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("equipmentJigId", entity.getEquipmentJigId());
        eamEquipmentJigListMapper.deleteByExample(example1);

        //新增明细
        List<EamEquipmentJigListDto> eamEquipmentJigListDtos = entity.getList();
        if(StringUtils.isNotEmpty(eamEquipmentJigListDtos)){
            for (EamEquipmentJigListDto eamEquipmentJigListDto : eamEquipmentJigListDtos) {
                eamEquipmentJigListDto.setEquipmentJigId(entity.getEquipmentJigId());
                eamEquipmentJigListDto.setCreateUserId(user.getUserId());
                eamEquipmentJigListDto.setCreateTime(new Date());
                eamEquipmentJigListDto.setModifiedUserId(user.getUserId());
                eamEquipmentJigListDto.setModifiedTime(new Date());
                eamEquipmentJigListDto.setStatus(StringUtils.isEmpty(eamEquipmentJigListDto.getStatus())?1:eamEquipmentJigListDto.getStatus());
                eamEquipmentJigListDto.setOrgId(user.getOrganizationId());
            }
            eamEquipmentJigListMapper.insertList(eamEquipmentJigListDtos);
        }

        //履历
        EamHtEquipmentJig eamHtEquipmentJig = new EamHtEquipmentJig();
        BeanUtils.copyProperties(entity, eamHtEquipmentJig);
        int i = eamHtEquipmentJigMapper.insert(eamHtEquipmentJig);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<EamHtEquipmentJig> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            EamEquipmentJig eamEquipmentJig = eamEquipmentJigMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(eamEquipmentJig)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            EamHtEquipmentJig eamHtEquipmentJig = new EamHtEquipmentJig();
            BeanUtils.copyProperties(eamEquipmentJig, eamHtEquipmentJig);
            list.add(eamHtEquipmentJig);

            //删除明细
            Example example1 = new Example(EamEquipmentJigList.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("equipmentJigId", id);
            eamEquipmentJigListMapper.deleteByExample(example1);
        }

        eamHtEquipmentJigMapper.insertList(list);

        return eamEquipmentJigMapper.deleteByIds(ids);
    }
}
