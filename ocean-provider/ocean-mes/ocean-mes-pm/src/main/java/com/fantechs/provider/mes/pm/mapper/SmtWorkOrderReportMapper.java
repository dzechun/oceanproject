package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.dto.apply.SmtWorkOrderReportDto;
import com.fantechs.common.base.entity.apply.SmtWorkOrderReport;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderReport;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtWorkOrderReportMapper extends MyMapper<SmtWorkOrderReport> {
    List<SmtWorkOrderReportDto> findList(SearchSmtWorkOrderReport searchSmtWorkOrderReport);
}