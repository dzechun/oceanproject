package com.fantechs.provider.daq.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.*;
import com.fantechs.common.base.general.entity.eam.EamEquipmentReEs;
import com.fantechs.common.base.general.entity.eam.EamEquipmentStation;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentStation;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.daq.mapper.DaqEquipmentReEsMapper;
import com.fantechs.provider.daq.mapper.DaqEquipmentStationMapper;
import com.fantechs.provider.daq.mapper.DaqHtEquipmentReEsMapper;
import com.fantechs.provider.daq.mapper.DaqHtEquipmentStationMapper;
import com.fantechs.provider.daq.service.DaqEquipmentStationService;
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
 * Created by leifengzhi on 2021/08/09.
 */
@Service
public class DaqEquipmentStationServiceImpl extends BaseService<EamEquipmentStation> implements DaqEquipmentStationService {

    @Resource
    private DaqEquipmentStationMapper daqEquipmentStationMapper;
    @Resource
    private DaqHtEquipmentStationMapper daqHtEquipmentStationMapper;
    @Resource
    private DaqEquipmentReEsMapper daqEquipmentReEsMapper;
    @Resource
    private DaqHtEquipmentReEsMapper daqHtEquipmentReEsMapper;

    @Override
    public List<EamEquipmentStationDto> findList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return daqEquipmentStationMapper.findList(map);
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamEquipmentStationDto eamEquipmentStationDto) {
        SysUser user = getUser();
        Example example = new Example(EamEquipmentStation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentStationCode", eamEquipmentStationDto.getEquipmentStationCode());
        List<EamEquipmentStation> eamEquipmentStations = daqEquipmentStationMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(eamEquipmentStations))   throw new BizErrorException("监控站已存在");
        example.clear();

        eamEquipmentStationDto.setCreateUserId(user.getUserId());
        eamEquipmentStationDto.setCreateTime(new Date());
        eamEquipmentStationDto.setModifiedUserId(user.getUserId());
        eamEquipmentStationDto.setModifiedTime(new Date());
        eamEquipmentStationDto.setStatus(StringUtils.isEmpty(eamEquipmentStationDto.getStatus())?1: eamEquipmentStationDto.getStatus());
        eamEquipmentStationDto.setOrgId(user.getOrganizationId());
        int i = daqEquipmentStationMapper.insertUseGeneratedKeys(eamEquipmentStationDto);

        //保存履历表
        EamHtEquipmentStation eamHtEquipmentStation = new EamHtEquipmentStation();
        BeanUtils.copyProperties(eamEquipmentStationDto, eamHtEquipmentStation);
        daqHtEquipmentStationMapper.insertSelective(eamHtEquipmentStation);

        //保存关系表及其履历表
        saveReEs(eamEquipmentStationDto,user);
        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamEquipmentStationDto eamEquipmentStationDto) {
        SysUser user =getUser();
        eamEquipmentStationDto.setModifiedTime(new Date());
        eamEquipmentStationDto.setModifiedUserId(user.getUserId());
        int i = daqEquipmentStationMapper.updateByPrimaryKeySelective(eamEquipmentStationDto);

        //更新履历表
        EamHtEquipmentStation eamHtEquipmentStation = new EamHtEquipmentStation();
        BeanUtils.copyProperties(eamEquipmentStationDto, eamHtEquipmentStation);
        daqHtEquipmentStationMapper.insertSelective(eamHtEquipmentStation);

        Example examples = new Example(EamEquipmentReEs.class);
        Example.Criteria criterias = examples.createCriteria();
        criterias.andEqualTo("equipmentStationId", eamEquipmentStationDto.getEquipmentStationId());
        daqEquipmentReEsMapper.deleteByExample(examples);
        examples.clear();
        //保存param表及其履历表
        saveReEs(eamEquipmentStationDto,user);
        return i;
    }

    @Override
    public int batchDelete(String ids) {
        int i = 0;
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            Example examples = new Example(EamEquipmentStation.class);
            Example.Criteria criterias = examples.createCriteria();
            criterias.andEqualTo("equipmentDataGroupId",id);
            daqEquipmentReEsMapper.deleteByExample(examples);
            i = daqEquipmentStationMapper.deleteByExample(examples);
            examples.clear();
        }

        return i;
    }



    public void saveReEs(EamEquipmentStationDto eamEquipmentStationDto,SysUser user){
        List<EamHtEquipmentReEsDto>  equipmentReEsHtList= new ArrayList<EamHtEquipmentReEsDto>();;
        if(StringUtils.isNotEmpty(eamEquipmentStationDto.getEamEquipmentReEsDtoList())){
            List<EamEquipmentReEsDto> list = new ArrayList<EamEquipmentReEsDto>();
            for(EamEquipmentReEsDto dto : eamEquipmentStationDto.getEamEquipmentReEsDtoList()){
                dto.setEquipmentStationId(eamEquipmentStationDto.getEquipmentStationId());
                dto.setCreateUserId(user.getUserId());
                dto.setCreateTime(new Date());
                dto.setModifiedUserId(user.getUserId());
                dto.setModifiedTime(new Date());
                dto.setStatus(StringUtils.isEmpty(eamEquipmentStationDto.getStatus())?1: eamEquipmentStationDto.getStatus());
                dto.setOrgId(user.getOrganizationId());
                list.add(dto);
                EamHtEquipmentReEsDto eamHtEquipmentReEsDto = new EamHtEquipmentReEsDto();
                BeanUtils.copyProperties(dto, eamHtEquipmentReEsDto);
                equipmentReEsHtList.add(eamHtEquipmentReEsDto);
            }
            daqEquipmentReEsMapper.insertList(list);
            daqHtEquipmentReEsMapper.insertList(equipmentReEsHtList);
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
