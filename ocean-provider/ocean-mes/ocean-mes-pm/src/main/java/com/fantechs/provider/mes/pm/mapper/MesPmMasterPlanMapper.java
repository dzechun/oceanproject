package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.entity.mes.pm.MesPmMasterPlan;
import com.fantechs.common.base.general.dto.mes.pm.MesPmMasterPlanDTO;
import org.apache.ibatis.annotations.Mapper;
import com.fantechs.common.base.mybatis.MyMapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface MesPmMasterPlanMapper extends MyMapper<MesPmMasterPlan> {
   //以特定过滤条件查询
   List<MesPmMasterPlanDTO> selectFilterAll(Map<String,Object> map);

}