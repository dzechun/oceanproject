package com.fantechs.provider.om.mapper;

import com.fantechs.common.base.general.entity.om.MesSchedule;
import com.fantechs.common.base.general.dto.om.MesScheduleDTO;
import com.fantechs.common.base.general.entity.om.SmtOrder;
import com.fantechs.common.base.general.entity.om.MesScheduleDetail;
import org.apache.ibatis.annotations.Mapper;
import com.fantechs.common.base.mybatis.MyMapper;

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
    //查询订单信息
    SmtOrder selectSmtOrderById(Long orderId);
}