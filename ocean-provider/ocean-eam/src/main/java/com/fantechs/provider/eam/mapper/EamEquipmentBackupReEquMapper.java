package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquipmentBackupReEquDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentBackupReEqu;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamEquipmentBackupReEquMapper extends MyMapper<EamEquipmentBackupReEqu> {
    List<EamEquipmentBackupReEquDto> findList(Map<String,Object> map);
}