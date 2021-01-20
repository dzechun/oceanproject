package com.fantechs.provider.om.service;

import com.fantechs.common.base.general.entity.om.MesSchedule;
import com.fantechs.common.base.general.dto.om.MesScheduleDTO;
import com.fantechs.common.base.support.IService;
import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2021年1月6日 18:01
 * @Description: 工单排产表接口
 * @Version: 1.0
 */
public interface MesScheduleService extends IService<MesSchedule>  {
    //===========================基础功能 start============================
    //动态条件查询对象列表
    List<MesSchedule> selectAll(Map<String,Object> map);
    //动态条件模糊查询对象列表
    List<MesSchedule> selectLikeAll(Map<String,Object> map);
    //动态条件查询对象
    MesSchedule selectByMap(Map<String,Object> map);
    //动态条件删除对象
    int deleteByMap(Map<String,Object> map);
    //通过ID查找用户名称
    String selectUserName(Object id);
    //===========================基础功能 end============================
    //以特定过滤条件查询
    List<MesScheduleDTO> selectFilterAll(Map<String,Object> map);
    //根据销售订单物料生成排产单及相关
    int saveByOrderMaterialIdList(Long proLineId,List<Long> orderMaterialIdList);

}
