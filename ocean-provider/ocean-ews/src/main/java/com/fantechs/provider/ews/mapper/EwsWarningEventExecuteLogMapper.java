package com.fantechs.provider.ews.mapper;

import com.fantechs.common.base.general.dto.ews.EwsWarningEventExecuteLogDto;
import com.fantechs.common.base.general.dto.ews.LogUreportDto;
import com.fantechs.common.base.general.dto.ews.UntreatedLogDto;
import com.fantechs.common.base.general.entity.ews.EwsWarningEventExecuteLog;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EwsWarningEventExecuteLogMapper extends MyMapper<EwsWarningEventExecuteLog> {
    List<EwsWarningEventExecuteLogDto> findList(Map<String,Object> map);

    List<UntreatedLogDto> findUntreatedLog();

    List<LogUreportDto> findLogUreport(Map<String,Object> map);
}