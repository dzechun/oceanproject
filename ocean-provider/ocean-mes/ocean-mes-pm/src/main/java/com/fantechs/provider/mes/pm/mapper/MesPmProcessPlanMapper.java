package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.entity.mes.pm.MesPmProcessPlan;
import com.fantechs.common.base.general.dto.mes.pm.MesPmProcessPlanDTO;
import org.apache.ibatis.annotations.Mapper;
import com.fantechs.common.base.mybatis.MyMapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface MesPmProcessPlanMapper extends MyMapper<MesPmProcessPlan> {
   //以特定过滤条件查询
   List<MesPmProcessPlanDTO> selectFilterAll(Map<String,Object> map);
   //批量新增
   int batchAdd(List<MesPmProcessPlan> mesPmProcessPlanList);
}