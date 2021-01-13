package com.fantechs.provider.pm.service;

import com.fantechs.common.base.dto.apply.SmtWorkOrderReportDto;
import com.fantechs.common.base.entity.apply.SmtWorkOrderReport;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderReport;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
* @author Mr.Lei
* @create 2020/11/21
*/
public interface SmtWorkOrderReportService extends IService<SmtWorkOrderReport> {
    List<SmtWorkOrderReportDto> findList(SearchSmtWorkOrderReport searchSmtWorkOrderReport);
}
