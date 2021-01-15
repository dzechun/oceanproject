package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderReportDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderReport;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderReport;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtWorkOrderReportMapper extends MyMapper<SmtWorkOrderReport> {
    List<SmtWorkOrderReportDto> findList(SearchSmtWorkOrderReport searchSmtWorkOrderReport);
}