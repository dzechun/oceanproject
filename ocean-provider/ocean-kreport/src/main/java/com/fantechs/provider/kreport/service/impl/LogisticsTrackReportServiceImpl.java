package com.fantechs.provider.kreport.service.impl;

import com.fantechs.common.base.general.entity.kreport.LogisticsTrackReport;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.kreport.mapper.LogisticsTrackReportMapper;
import com.fantechs.provider.kreport.service.LogisticsTrackReportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class LogisticsTrackReportServiceImpl extends BaseService<LogisticsTrackReport> implements LogisticsTrackReportService {

    @Resource
    private LogisticsTrackReportMapper logisticsTrackReportMapper;

    @Override
    public List<LogisticsTrackReport> findList(Map<String, Object> map) {
        if (StringUtils.isNotEmpty(map.get("type")) && Byte.valueOf(map.get("type").toString()) == 1) {
            return logisticsTrackReportMapper.findTakeList(map);
        }else {
            return logisticsTrackReportMapper.findDeliverList(map);
        }
    }
}
