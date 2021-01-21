package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.dto.mes.pm.*;
import com.fantechs.common.base.general.entity.mes.pm.MesPmMasterPlan;
import com.fantechs.common.base.support.IService;
import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2021年1月15日 15:52
 * @Description: 总计划表（月计划表）接口
 * @Version: 1.0
 */
public interface MesPmMasterPlanService extends IService<MesPmMasterPlan>  {
    //===========================基础功能 start============================
     //动态条件查询对象列表
    List<MesPmMasterPlan> selectAll(Map<String,Object> map);
    //动态条件删除对象
    int deleteByMap(Map<String,Object> map);
    //===========================基础功能 end============================
    //以特定过滤条件查询
    List<MesPmMasterPlanDTO> selectFilterAll(Map<String,Object> map);
    //新增及更新主计划及工序计划
    int save(SaveMesPmMasterPlanDTO saveMesPmMasterPlanDTO);
    //转执行计划（周/日计划）
    int turnExplainPlan(TurnExplainPlanDTO turnExplainPlanDTO);
    //输出工单相关信息（总计划打印A4）
    MasterPlanPrintWorkOrderDTO masterPlanPrintWorkOrder(Long masterPlanId);
    //转流程卡
    int turnWorkOrderCardPool(TurnWorkOrderCardPoolDTO turnWorkOrderCardPoolDTO);
}
