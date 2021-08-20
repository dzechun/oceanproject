package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.entity.eam.EamEquipmentBackup;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamEquipmentBackupMapper extends MyMapper<EamEquipmentBackup> {
    List<EamEquipmentBackup> findList(Map<String,Object> map);
}