package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.entity.basic.SmtProcess;
import com.fantechs.common.base.general.dto.mes.pm.MaterialAndPartsDTO;
import com.fantechs.common.base.general.dto.mes.pm.ProcessListDto;
import com.fantechs.common.base.general.dto.mes.pm.SmtProcessListProcessDto;
import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderCardPoolDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtProcessListProcess;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtProcessListProcess;
import com.fantechs.common.base.entity.basic.SmtRouteProcess;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface SmtProcessListProcessMapper extends MyMapper<SmtProcessListProcess> {
    List<SmtProcessListProcessDto> findList(SearchSmtProcessListProcess searchSmtProcessListProcess);

    List<SmtRouteProcess> select_smt_route_process(SmtRouteProcess smtRouteProcess);

    List<ProcessListDto> findProcess(Long workOrderId);
    //查找工序信息
    SmtProcess findSmtProcess(Long id);
    //查找对应流程卡集合内过站报工数最小的
    double findMinOutPut(Long workOrderCardPoolId);
    //查找工单工艺下是否存在当前工序
    int isExistProcessInWorkOrder(Long workOrderId,Long processId);
    //查找当前工序所属工段的首工序id
    Long firstProcessIdInWSection(Long processId,Long routeId);
    //查找当前工艺路线下首工序
    Long firstProcessIdInRoute(Long routeId);
    //查询当前工序所属工段是否是工艺路线下最后一个工段
    int isLastSectionInRoute(Long routeId,Long processId);
    //查找上一条数据
    SmtProcessListProcess findUp(Long processListProcessId,Long workOrderCardPoolId,Long processId);
    //查找流程卡对应的部件信息及产品信息
    MaterialAndPartsDTO findPartsInformation(Long workOrderCardPoolId);
    //根据工单查询部件的用量
    BigDecimal getQuantityByWorkOrderId(Long workOrderId);
}