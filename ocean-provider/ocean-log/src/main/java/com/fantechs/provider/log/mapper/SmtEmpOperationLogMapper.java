package com.fantechs.provider.log.mapper;

import com.fantechs.common.base.general.dto.log.SmtEmpOperationLogDto;
import com.fantechs.common.base.general.entity.log.SmtEmpOperationLog;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmtEmpOperationLogMapper extends MyMapper<SmtEmpOperationLog> {
    List<SmtEmpOperationLogDto> findList(Map<String, Object> map);
}
