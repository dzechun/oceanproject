package com.fantechs.provider.daq.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.daq.DaqEquipmentDataGroupDto;
import com.fantechs.common.base.general.dto.daq.DaqEquipmentDataGroupParamDto;
import com.fantechs.common.base.general.dto.daq.DaqHtEquipmentDataGroupParamDto;
import com.fantechs.common.base.general.entity.daq.DaqEquipmentDataGroup;
import com.fantechs.common.base.general.entity.daq.DaqEquipmentDataGroupParam;
import com.fantechs.common.base.general.entity.daq.DaqHtEquipmentDataGroup;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.daq.mapper.DaqEquipmentDataGroupMapper;
import com.fantechs.provider.daq.mapper.DaqEquipmentDataGroupParamMapper;
import com.fantechs.provider.daq.mapper.DaqHtEquipmentDataGroupMapper;
import com.fantechs.provider.daq.mapper.DaqHtEquipmentDataGroupParamMapper;
import com.fantechs.provider.daq.service.DaqEquipmentDataGroupService;
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
 * Created by leifengzhi on 2021/08/02.
 */
@Service
public class DaqEquipmentDataGroupServiceImpl extends BaseService<DaqEquipmentDataGroup> implements DaqEquipmentDataGroupService {

    @Resource
    private DaqEquipmentDataGroupMapper daqEquipmentDataGroupMapper;
    @Resource
    private DaqHtEquipmentDataGroupMapper daqHtEquipmentDataGroupMapper;
    @Resource
    private DaqEquipmentDataGroupParamMapper daqEquipmentDataGroupParamMapper;
    @Resource
    private DaqHtEquipmentDataGroupParamMapper daqHtEquipmentDataGroupParamMapper;

    @Override
    public List<DaqEquipmentDataGroupDto> findList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return daqEquipmentDataGroupMapper.findList(map);
    }
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(DaqEquipmentDataGroupDto DaqEquipmentDataGroupDto) {

        SysUser user = getUser();

        Example example = new Example(DaqEquipmentDataGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentDataGroupName", DaqEquipmentDataGroupDto.getEquipmentDataGroupName());
        List<DaqEquipmentDataGroup> DaqEquipmentDataGroups = daqEquipmentDataGroupMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(DaqEquipmentDataGroups))   throw new BizErrorException("分组已存在");
        example.clear();


        DaqEquipmentDataGroupDto.setCreateUserId(user.getUserId());
        DaqEquipmentDataGroupDto.setCreateTime(new Date());
        DaqEquipmentDataGroupDto.setModifiedUserId(user.getUserId());
        DaqEquipmentDataGroupDto.setModifiedTime(new Date());
        DaqEquipmentDataGroupDto.setStatus(StringUtils.isEmpty(DaqEquipmentDataGroupDto.getStatus())?1: DaqEquipmentDataGroupDto.getStatus());
        DaqEquipmentDataGroupDto.setOrgId(user.getOrganizationId());
        int i = daqEquipmentDataGroupMapper.insertUseGeneratedKeys(DaqEquipmentDataGroupDto);

        //保存履历表
        DaqHtEquipmentDataGroup DaqHtEquipmentDataGroup = new DaqHtEquipmentDataGroup();
        BeanUtils.copyProperties(DaqEquipmentDataGroupDto, DaqHtEquipmentDataGroup);
        daqHtEquipmentDataGroupMapper.insertSelective(DaqHtEquipmentDataGroup);

        //保存param表及其履历表
        saveParam(DaqEquipmentDataGroupDto,user);
        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(DaqEquipmentDataGroupDto DaqEquipmentDataGroupDto) {
        SysUser user =getUser();
        DaqEquipmentDataGroupDto.setModifiedTime(new Date());
        DaqEquipmentDataGroupDto.setModifiedUserId(user.getUserId());
        int i = daqEquipmentDataGroupMapper.updateByPrimaryKeySelective(DaqEquipmentDataGroupDto);

        //更新履历表
        DaqHtEquipmentDataGroup DaqHtEquipmentDataGroup = new DaqHtEquipmentDataGroup();
        BeanUtils.copyProperties(DaqEquipmentDataGroupDto, DaqHtEquipmentDataGroup);
        daqHtEquipmentDataGroupMapper.insertSelective(DaqHtEquipmentDataGroup);

        Example examples = new Example(DaqEquipmentDataGroupParam.class);
        Example.Criteria criterias = examples.createCriteria();
        criterias.andEqualTo("equipmentDataGroupId", DaqEquipmentDataGroupDto.getEquipmentDataGroupId());
        daqEquipmentDataGroupParamMapper.deleteByExample(examples);
        examples.clear();
        //保存param表及其履历表
        saveParam(DaqEquipmentDataGroupDto,user);
        return i;
    }

    @Override
    public int batchDelete(String ids) {
        int i = 0;
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            Example examples = new Example(DaqEquipmentDataGroupParam.class);
            Example.Criteria criterias = examples.createCriteria();
            criterias.andEqualTo("equipmentDataGroupId",id);
            daqEquipmentDataGroupParamMapper.deleteByExample(examples);
            i = daqEquipmentDataGroupMapper.deleteByExample(examples);
        }

        return i;
    }



    public void saveParam(DaqEquipmentDataGroupDto DaqEquipmentDataGroupDto,SysUser user){
        List<DaqHtEquipmentDataGroupParamDto>  paramHtList= new ArrayList<DaqHtEquipmentDataGroupParamDto>();;
        if(StringUtils.isNotEmpty(DaqEquipmentDataGroupDto.getDaqEquipmentDataGroupParamDtos())){
            List<DaqEquipmentDataGroupParamDto> list = new ArrayList<DaqEquipmentDataGroupParamDto>();
            for(DaqEquipmentDataGroupParamDto dto : DaqEquipmentDataGroupDto.getDaqEquipmentDataGroupParamDtos()){
                dto.setEquipmentDataGroupId(DaqEquipmentDataGroupDto.getEquipmentDataGroupId());
                dto.setCreateUserId(user.getUserId());
                dto.setCreateTime(new Date());
                dto.setModifiedUserId(user.getUserId());
                dto.setModifiedTime(new Date());
                dto.setStatus(StringUtils.isEmpty(DaqEquipmentDataGroupDto.getStatus())?1: DaqEquipmentDataGroupDto.getStatus());
                dto.setOrgId(user.getOrganizationId());
                list.add(dto);
                DaqHtEquipmentDataGroupParamDto DaqHtEquipmentDataGroupParamDto = new DaqHtEquipmentDataGroupParamDto();
                BeanUtils.copyProperties(dto, DaqHtEquipmentDataGroupParamDto);
                paramHtList.add(DaqHtEquipmentDataGroupParamDto);
            }
            daqEquipmentDataGroupParamMapper.insertList(list);
            daqHtEquipmentDataGroupParamMapper.insertList(paramHtList);
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
