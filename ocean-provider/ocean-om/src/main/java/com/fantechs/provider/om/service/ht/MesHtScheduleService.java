package com.fantechs.provider.om.service.ht;

import com.fantechs.common.base.general.entity.mes.pm.history.MesHtSchedule;
import com.fantechs.common.base.general.dto.mes.pm.history.MesHtScheduleDTO;
import com.fantechs.common.base.support.IService;
import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2021年1月9日 14:18
 * @Description: 排产单履历表接口
 * @Version: 1.0
 */
public interface MesHtScheduleService extends IService<MesHtSchedule>  {
    //===========================基础功能 start============================
     //动态条件查询对象列表
    List<MesHtSchedule> selectAll(Map<String,Object> map);
    //动态条件删除对象
    int deleteByMap(Map<String,Object> map);
    //===========================基础功能 end============================
    //以特定过滤条件查询
    List<MesHtScheduleDTO> selectFilterAll(Map<String,Object> map);

}
