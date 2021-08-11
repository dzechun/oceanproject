package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamJigBackupDto;
import com.fantechs.common.base.general.entity.eam.EamJigBackup;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamJigBackupMapper extends MyMapper<EamJigBackup> {
    List<EamJigBackupDto> findList(Map<String,Object> map);
}