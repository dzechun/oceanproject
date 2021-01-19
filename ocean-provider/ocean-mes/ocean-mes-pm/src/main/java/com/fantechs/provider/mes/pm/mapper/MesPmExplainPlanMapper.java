package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.entity.mes.pm.MesPmExplainPlan;
import com.fantechs.common.base.general.dto.mes.pm.MesPmExplainPlanDTO;
import org.apache.ibatis.annotations.Mapper;
import com.fantechs.common.base.mybatis.MyMapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface MesPmExplainPlanMapper extends MyMapper<MesPmExplainPlan> {
   //以特定过滤条件查询
   List<MesPmExplainPlanDTO> selectFilterAll(Map<String,Object> map);

}