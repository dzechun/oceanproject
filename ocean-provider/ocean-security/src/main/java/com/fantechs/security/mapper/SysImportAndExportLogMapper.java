package com.fantechs.security.mapper;

import com.fantechs.common.base.dto.security.SysImportAndExportLogDto;
import com.fantechs.common.base.entity.security.SysImportAndExportLog;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysImportAndExportLogMapper extends MyMapper<SysImportAndExportLog> {
    List<SysImportAndExportLogDto> findList(Map<String, Object> param);
}