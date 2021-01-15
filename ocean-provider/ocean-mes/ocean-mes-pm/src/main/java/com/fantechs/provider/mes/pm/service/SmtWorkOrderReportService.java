package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderReportDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderReport;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderReport;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
* @author Mr.Lei
* @create 2020/11/21
*/
public interface SmtWorkOrderReportService extends IService<SmtWorkOrderReport> {
    List<SmtWorkOrderReportDto> findList(SearchSmtWorkOrderReport searchSmtWorkOrderReport);
}
