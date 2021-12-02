package com.fantechs.provider.kreport.service.impl;

import com.fantechs.common.base.general.entity.kreport.LogisticsTrackReport;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.kreport.mapper.LogisticsTrackReportMapper;
import com.fantechs.provider.kreport.service.LogisticsTrackReportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LogisticsTrackReportServiceImpl extends BaseService<LogisticsTrackReport> implements LogisticsTrackReportService {

    @Resource
    private LogisticsTrackReportMapper logisticsTrackReportMapper;

    @Override
    public List<LogisticsTrackReport> findList(Map<String, Object> map) {
        List<LogisticsTrackReport> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(map.get("type")) && Byte.valueOf(map.get("type").toString()) == 1) {
            list = logisticsTrackReportMapper.findTakeList(map);
        }else {
            list = logisticsTrackReportMapper.findDeliverList(map);
        }
        for (LogisticsTrackReport logisticsTrackReport : list) {
            if (StringUtils.isNotEmpty(logisticsTrackReport.getRouteTrace())) {
                String routeTrace = logisticsTrackReport.getRouteTrace();
                String[] split = routeTrace.split("\r\n");
                if (split[0].contains("签收人类型：")) {
                    logisticsTrackReport.setRecipients(split[0].substring(split[0].indexOf("签收人类型：")+6));
                    logisticsTrackReport.setRecipientsDate(split[0].substring(0,split[0].indexOf("签收人类型：")-4));
                }
                if (logisticsTrackReport.getCarrierName().equals("顺丰") && split.length >= 2) {
                    logisticsTrackReport.setPullPerson(split[split.length-2].substring(split[split.length-2].indexOf("【")+1,split[split.length-2].indexOf("】")));
                    logisticsTrackReport.setPullDate(split[split.length-2].substring(0,19));
                }else if (logisticsTrackReport.getCarrierName().equals("德邦")) {
                    logisticsTrackReport.setPullPerson(split[split.length-1].substring(split[split.length-1].indexOf("【")+1,split[split.length-1].indexOf("】")));
                    logisticsTrackReport.setPullDate(split[split.length-1].substring(0,19));
                }
            }
            if ("O".equals(logisticsTrackReport.getType())) {
                logisticsTrackReport.setTypeName("发货");
            }else {
                logisticsTrackReport.setTypeName("收货");
            }
        }
        return list;
    }
}
