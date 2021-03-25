package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.dto.mes.pm.MesProcessReportWorkDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesProcessReportWork;
import com.fantechs.common.base.general.entity.mes.pm.MesProcessReportWork;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesProcessReportWorkMapper extends MyMapper<MesProcessReportWork> {

    List<MesProcessReportWorkDto> findList(SearchMesProcessReportWork searchMesProcessReportWork);
}