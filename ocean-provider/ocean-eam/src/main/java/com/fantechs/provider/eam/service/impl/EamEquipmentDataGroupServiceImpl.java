package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentDataGroupDto;
import com.fantechs.common.base.general.dto.eam.EamEquipmentDataGroupParamDto;
import com.fantechs.common.base.general.dto.eam.EamHtEquipmentDataGroupParamDto;
import com.fantechs.common.base.general.entity.eam.EamEquipment;
import com.fantechs.common.base.general.entity.eam.EamEquipmentDataGroup;
import com.fantechs.common.base.general.entity.eam.EamEquipmentDataGroupParam;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipment;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentDataGroup;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamEquipmentDataGroupMapper;
import com.fantechs.provider.eam.mapper.EamEquipmentDataGroupParamMapper;
import com.fantechs.provider.eam.mapper.EamHtEquipmentDataGroupMapper;
import com.fantechs.provider.eam.mapper.EamHtEquipmentDataGroupParamMapper;
import com.fantechs.provider.eam.service.EamEquipmentDataGroupService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/02.
 */
@Service
public class EamEquipmentDataGroupServiceImpl extends BaseService<EamEquipmentDataGroup> implements EamEquipmentDataGroupService {

    @Resource
    private EamEquipmentDataGroupMapper eamEquipmentDataGroupMapper;
    @Resource
    private EamHtEquipmentDataGroupMapper eamHtEquipmentDataGroupMapper;
    @Resource
    private EamEquipmentDataGroupParamMapper eamEquipmentDataGroupParamMapper;
    @Resource
    private EamHtEquipmentDataGroupParamMapper eamHtEquipmentDataGroupParamMapper;

    @Override
    public List<EamEquipmentDataGroupDto> findList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamEquipmentDataGroupMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamEquipmentDataGroupDto eamEquipmentDataGroupDto) {

        SysUser user = getUser();
        eamEquipmentDataGroupDto.setCreateUserId(user.getUserId());
        eamEquipmentDataGroupDto.setCreateTime(new Date());
        eamEquipmentDataGroupDto.setModifiedUserId(user.getUserId());
        eamEquipmentDataGroupDto.setModifiedTime(new Date());
        eamEquipmentDataGroupDto.setStatus(StringUtils.isEmpty(eamEquipmentDataGroupDto.getStatus())?1: eamEquipmentDataGroupDto.getStatus());
        eamEquipmentDataGroupDto.setOrgId(user.getOrganizationId());
        int i = eamEquipmentDataGroupMapper.insertUseGeneratedKeys(eamEquipmentDataGroupDto);

        //保存履历表
        EamHtEquipmentDataGroup eamHtEquipmentDataGroup = new EamHtEquipmentDataGroup();
        BeanUtils.copyProperties(eamEquipmentDataGroupDto, eamHtEquipmentDataGroup);
        eamHtEquipmentDataGroupMapper.insertSelective(eamHtEquipmentDataGroup);

        //保存param表及其履历表
        saveParam(eamEquipmentDataGroupDto,user);
        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamEquipmentDataGroupDto eamEquipmentDataGroupDto) {
        SysUser user =getUser();
        eamEquipmentDataGroupDto.setModifiedTime(new Date());
        eamEquipmentDataGroupDto.setModifiedUserId(user.getUserId());
        int i = eamEquipmentDataGroupMapper.updateByPrimaryKeySelective(eamEquipmentDataGroupDto);

        //更新履历表
        EamHtEquipmentDataGroup eamHtEquipmentDataGroup = new EamHtEquipmentDataGroup();
        BeanUtils.copyProperties(eamEquipmentDataGroupDto, eamHtEquipmentDataGroup);
        eamHtEquipmentDataGroupMapper.insertSelective(eamHtEquipmentDataGroup);

        Example examples = new Example(EamEquipmentDataGroupParam.class);
        Example.Criteria criterias = examples.createCriteria();
        criterias.andEqualTo("equipmentDataGroupParamId", eamEquipmentDataGroupDto.getEquipmentDataGroupId());
        eamEquipmentDataGroupParamMapper.deleteByPrimaryKey(examples);
        examples.clear();
        //保存param表及其履历表
        saveParam(eamEquipmentDataGroupDto,user);
        return i;
    }

    @Override
    public int batchDelete(String ids) {
        int i = 0;
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            Example examples = new Example(EamEquipmentDataGroupParam.class);
            Example.Criteria criterias = examples.createCriteria();
            criterias.andEqualTo("equipmentDataGroupParamId",id);
            eamEquipmentDataGroupParamMapper.deleteByPrimaryKey(examples);
            i = eamEquipmentDataGroupMapper.deleteByExample(examples);
        }

        return i;
    }



    public void saveParam(EamEquipmentDataGroupDto eamEquipmentDataGroupDto,SysUser user){
        List<EamHtEquipmentDataGroupParamDto>  paramHtList= null;
        if(StringUtils.isNotEmpty(eamEquipmentDataGroupDto.getEamEquipmentDataGroupParamDtos())){
            List<EamEquipmentDataGroupParamDto> list = null;
            for(EamEquipmentDataGroupParamDto dto : eamEquipmentDataGroupDto.getEamEquipmentDataGroupParamDtos()){
                dto.setEquipmentDataGroupId(eamEquipmentDataGroupDto.getEquipmentDataGroupId());
                dto.setCreateUserId(user.getUserId());
                dto.setCreateTime(new Date());
                dto.setModifiedUserId(user.getUserId());
                dto.setModifiedTime(new Date());
                dto.setStatus(StringUtils.isEmpty(eamEquipmentDataGroupDto.getStatus())?1: eamEquipmentDataGroupDto.getStatus());
                dto.setOrgId(user.getOrganizationId());
                list.add(dto);
                EamHtEquipmentDataGroupParamDto eamHtEquipmentDataGroupParamDto = new EamHtEquipmentDataGroupParamDto();
                BeanUtils.copyProperties(dto, eamHtEquipmentDataGroupParamDto);
                paramHtList.add(eamHtEquipmentDataGroupParamDto);
            }
            eamEquipmentDataGroupParamMapper.insertList(list);
            eamHtEquipmentDataGroupParamMapper.insertList(paramHtList);
        }
    }


    public SysUser getUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}
