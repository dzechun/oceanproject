package com.fantechs.provider.ews.mapper;

import com.fantechs.common.base.general.dto.ews.EwsWarningEventExecutePushLogDto;
import com.fantechs.common.base.general.entity.ews.EwsWarningEventExecutePushLog;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EwsWarningEventExecutePushLogMapper extends MyMapper<EwsWarningEventExecutePushLog> {
    List<EwsWarningEventExecutePushLogDto> findList(Map<String,Object> map);
}