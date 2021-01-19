package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.dto.mes.pm.MesPmExplainProcessPlanDTO;
import com.fantechs.common.base.general.entity.mes.pm.MesPmExplainProcessPlan;
import org.apache.ibatis.annotations.Mapper;
import com.fantechs.common.base.mybatis.MyMapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface MesPmExplainProcessPlanMapper extends MyMapper<MesPmExplainProcessPlan> {
   //以特定过滤条件查询
   List<MesPmExplainProcessPlanDTO> selectFilterAll(Map<String,Object> map);
   //批量新增
   int batchAdd(List<MesPmExplainProcessPlan> mesPmExplainProcessPlanList);
}