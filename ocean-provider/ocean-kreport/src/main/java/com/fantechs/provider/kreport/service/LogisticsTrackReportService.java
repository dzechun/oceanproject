package com.fantechs.provider.kreport.service;

import com.fantechs.common.base.general.entity.kreport.LogisticsTrackReport;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;


public interface LogisticsTrackReportService extends IService<LogisticsTrackReport> {
    List<LogisticsTrackReport> findList(Map<String, Object> map);

}
