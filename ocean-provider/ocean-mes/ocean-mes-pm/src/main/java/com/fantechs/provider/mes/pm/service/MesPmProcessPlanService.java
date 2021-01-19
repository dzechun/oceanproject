package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.entity.mes.pm.MesPmProcessPlan;
import com.fantechs.common.base.general.dto.mes.pm.MesPmProcessPlanDTO;
import com.fantechs.common.base.support.IService;
import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2021年1月15日 16:26
 * @Description: 工序计划表接口
 * @Version: 1.0
 */
public interface MesPmProcessPlanService extends IService<MesPmProcessPlan>  {
    //===========================基础功能 start============================
     //动态条件查询对象列表
    List<MesPmProcessPlan> selectAll(Map<String,Object> map);
    //动态条件删除对象
    int deleteByMap(Map<String,Object> map);
    //===========================基础功能 end============================
    //以特定过滤条件查询
    List<MesPmProcessPlanDTO> selectFilterAll(Map<String,Object> map);
    //批量新增
    int batchAdd(List<MesPmProcessPlan> mesPmProcessPlanList);
}
