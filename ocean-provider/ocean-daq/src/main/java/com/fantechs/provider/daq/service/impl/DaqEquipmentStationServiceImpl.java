package com.fantechs.provider.daq.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.daq.DaqEquipmentReEsDto;
import com.fantechs.common.base.general.dto.daq.DaqEquipmentStationDto;
import com.fantechs.common.base.general.dto.daq.DaqHtEquipmentReEsDto;
import com.fantechs.common.base.general.entity.daq.DaqEquipmentReEs;
import com.fantechs.common.base.general.entity.daq.DaqEquipmentStation;
import com.fantechs.common.base.general.entity.daq.DaqHtEquipmentStation;
import com.fantechs.common.base.general.entity.eam.EamEquipmentStation;
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
public class DaqEquipmentStationServiceImpl extends BaseService<DaqEquipmentStation> implements DaqEquipmentStationService {

    @Resource
    private DaqEquipmentStationMapper daqEquipmentStationMapper;
    @Resource
    private DaqHtEquipmentStationMapper daqHtEquipmentStationMapper;
    @Resource
    private DaqEquipmentReEsMapper daqEquipmentReEsMapper;
    @Resource
    private DaqHtEquipmentReEsMapper daqHtEquipmentReEsMapper;

    @Override
    public List<DaqEquipmentStationDto> findList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return daqEquipmentStationMapper.findList(map);
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(DaqEquipmentStationDto daqEquipmentStationDto) {
        SysUser user = getUser();
        Example example = new Example(DaqEquipmentStation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentStationCode", daqEquipmentStationDto.getEquipmentStationCode());
        List<DaqEquipmentStation> daqEquipmentStations = daqEquipmentStationMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(daqEquipmentStations))   throw new BizErrorException("监控站已存在");
        example.clear();

        daqEquipmentStationDto.setCreateUserId(user.getUserId());
        daqEquipmentStationDto.setCreateTime(new Date());
        daqEquipmentStationDto.setModifiedUserId(user.getUserId());
        daqEquipmentStationDto.setModifiedTime(new Date());
        daqEquipmentStationDto.setStatus(StringUtils.isEmpty(daqEquipmentStationDto.getStatus())?1: daqEquipmentStationDto.getStatus());
        daqEquipmentStationDto.setOrgId(user.getOrganizationId());
        int i = daqEquipmentStationMapper.insertUseGeneratedKeys(daqEquipmentStationDto);

        //保存履历表
        DaqHtEquipmentStation daqHtEquipmentStation = new DaqHtEquipmentStation();
        BeanUtils.copyProperties(daqEquipmentStationDto, daqHtEquipmentStation);
        daqHtEquipmentStationMapper.insertSelective(daqHtEquipmentStation);

        //保存关系表及其履历表
        saveReEs(daqEquipmentStationDto,user);
        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(DaqEquipmentStationDto daqEquipmentStationDto) {
        SysUser user =getUser();
        daqEquipmentStationDto.setModifiedTime(new Date());
        daqEquipmentStationDto.setModifiedUserId(user.getUserId());
        int i = daqEquipmentStationMapper.updateByPrimaryKeySelective(daqEquipmentStationDto);

        //更新履历表
        DaqHtEquipmentStation daqHtEquipmentStation = new DaqHtEquipmentStation();
        BeanUtils.copyProperties(daqEquipmentStationDto, daqHtEquipmentStation);
        daqHtEquipmentStationMapper.insertSelective(daqHtEquipmentStation);

        Example examples = new Example(DaqEquipmentReEs.class);
        Example.Criteria criterias = examples.createCriteria();
        criterias.andEqualTo("equipmentStationId", daqEquipmentStationDto.getEquipmentStationId());
        daqEquipmentReEsMapper.deleteByExample(examples);
        examples.clear();
        //保存param表及其履历表
        saveReEs(daqEquipmentStationDto,user);
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



    public void saveReEs(DaqEquipmentStationDto daqEquipmentStationDto,SysUser user){
        List<DaqHtEquipmentReEsDto>  equipmentReEsHtList= new ArrayList<DaqHtEquipmentReEsDto>();;
        if(StringUtils.isNotEmpty(daqEquipmentStationDto.getDaqEquipmentReEsDtoList())){
            List<DaqEquipmentReEsDto> list = new ArrayList<DaqEquipmentReEsDto>();
            for(DaqEquipmentReEsDto dto : daqEquipmentStationDto.getDaqEquipmentReEsDtoList()){
                dto.setEquipmentStationId(daqEquipmentStationDto.getEquipmentStationId());
                dto.setCreateUserId(user.getUserId());
                dto.setCreateTime(new Date());
                dto.setModifiedUserId(user.getUserId());
                dto.setModifiedTime(new Date());
                dto.setStatus(StringUtils.isEmpty(daqEquipmentStationDto.getStatus())?1: daqEquipmentStationDto.getStatus());
                dto.setOrgId(user.getOrganizationId());
                list.add(dto);
                DaqHtEquipmentReEsDto daqHtEquipmentReEsDto = new DaqHtEquipmentReEsDto();
                BeanUtils.copyProperties(dto, daqHtEquipmentReEsDto);
                equipmentReEsHtList.add(daqHtEquipmentReEsDto);
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
