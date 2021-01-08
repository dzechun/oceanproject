package com.fantechs.provider.imes.apply.mapper;

import com.fantechs.common.base.entity.apply.MesSchedule;
import com.fantechs.common.base.dto.apply.MesScheduleDTO;
import com.fantechs.common.base.entity.apply.MesScheduleDetail;
import org.apache.ibatis.annotations.Mapper;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesScheduleMapper extends MyMapper<MesSchedule> {
    //通过ID查找用户名称
   String selectUserName(Object id);
   //以特定过滤条件查询
   List<MesScheduleDTO> selectFilterAll(Map<String,Object> map);
   //批量插入排产单详情
    int batchScheduleDetail(List<MesScheduleDetail> mesScheduleDetailList);

}