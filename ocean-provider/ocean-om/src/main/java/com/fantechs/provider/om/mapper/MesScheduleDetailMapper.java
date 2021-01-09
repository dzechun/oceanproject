package com.fantechs.provider.om.mapper;


import com.fantechs.common.base.general.entity.om.MesScheduleDetail;
import com.fantechs.common.base.general.dto.om.MesScheduleDetailDTO;
import org.apache.ibatis.annotations.Mapper;
import com.fantechs.common.base.mybatis.MyMapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface MesScheduleDetailMapper extends MyMapper<MesScheduleDetail> {
   //以特定过滤条件查询
   List<MesScheduleDetailDTO> selectFilterAll(Map<String,Object> map);

}