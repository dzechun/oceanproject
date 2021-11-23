package com.fantechs.provider.kreport.mapper;

import com.fantechs.common.base.general.entity.kreport.LogisticsTrackReport;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LogisticsTrackReportMapper extends MyMapper<LogisticsTrackReport>{

    List<LogisticsTrackReport> findDeliverList(Map<String, Object> map);

    List<LogisticsTrackReport> findTakeList(Map<String, Object> map);
}
