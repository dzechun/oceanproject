package com.fantechs.provider.om.mapper;

import com.fantechs.common.base.general.entity.mes.pm.history.MesHtSchedule;
import com.fantechs.common.base.general.dto.mes.pm.history.MesHtScheduleDTO;
import org.apache.ibatis.annotations.Mapper;
import com.fantechs.common.base.mybatis.MyMapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface MesHtScheduleMapper extends MyMapper<MesHtSchedule> {
   //以特定过滤条件查询
   List<MesHtScheduleDTO> selectFilterAll(Map<String,Object> map);

}