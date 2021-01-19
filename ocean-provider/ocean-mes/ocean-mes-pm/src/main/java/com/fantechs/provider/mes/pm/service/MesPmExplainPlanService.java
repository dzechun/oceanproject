package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.dto.mes.pm.SaveMesPmExplainPlanDTO;
import com.fantechs.common.base.general.entity.mes.pm.MesPmExplainPlan;
import com.fantechs.common.base.general.dto.mes.pm.MesPmExplainPlanDTO;
import com.fantechs.common.base.support.IService;
import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2021年1月15日 15:58
 * @Description: 执行计划（周/日计划表）接口
 * @Version: 1.0
 */
public interface MesPmExplainPlanService extends IService<MesPmExplainPlan>  {
    //===========================基础功能 start============================
     //动态条件查询对象列表
    List<MesPmExplainPlan> selectAll(Map<String,Object> map);
    //动态条件删除对象
    int deleteByMap(Map<String,Object> map);
    //===========================基础功能 end============================
    //以特定过滤条件查询
    List<MesPmExplainPlanDTO> selectFilterAll(Map<String,Object> map);
    //新增及更新主计划及工序计划
    int save(SaveMesPmExplainPlanDTO saveMesPmExplainPlanDTO);
}
