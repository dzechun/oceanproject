package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.dto.mes.pm.MesPmExplainProcessPlanDTO;
import com.fantechs.common.base.general.entity.mes.pm.MesPmExplainProcessPlan;
import com.fantechs.common.base.support.IService;
import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2021年1月18日 10:46
 * @Description: 执行工序计划表接口
 * @Version: 1.0
 */
public interface MesPmExplainProcessPlanService extends IService<MesPmExplainProcessPlan>  {
    //===========================基础功能 start============================
     //动态条件查询对象列表
    List<MesPmExplainProcessPlan> selectAll(Map<String,Object> map);
    //动态条件删除对象
    int deleteByMap(Map<String,Object> map);
    //===========================基础功能 end============================
    //以特定过滤条件查询
    List<MesPmExplainProcessPlanDTO> selectFilterAll(Map<String,Object> map);
    //批量新增
    int batchAdd(List<MesPmExplainProcessPlan> mesPmExplainProcessPlanList);
    //批量更新
    int batchUpdate(List<MesPmExplainProcessPlan> mesPmExplainProcessPlanList);
}
