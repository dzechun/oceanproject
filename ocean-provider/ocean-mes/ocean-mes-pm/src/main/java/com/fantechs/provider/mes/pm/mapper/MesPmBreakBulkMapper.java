package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.dto.mes.pm.MesPmBreakBulkDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmBreakBulkPrintDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmBreakBulk;
import com.fantechs.common.base.general.entity.mes.pm.MesPmBreakBulk;
import com.fantechs.common.base.mybatis.MyMapper;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesPmBreakBulkMapper extends MyMapper<MesPmBreakBulk> {
    List<MesPmBreakBulkDto> findList(SearchMesPmBreakBulk searchMesPmBreakBulk);

    Long sleProcess(@Param("workOrderCardId")String workOrderCardId);

    MesPmBreakBulkPrintDto reprint(SearchMesPmBreakBulk searchMesPmBreakBulk);

    String confirmation(@Param("workOrderCardId") String workOrderCardId, @Param("processId") Long processId);
}