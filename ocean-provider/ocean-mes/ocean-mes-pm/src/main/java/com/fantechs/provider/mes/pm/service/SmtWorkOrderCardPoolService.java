package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.dto.mes.pm.NoPutIntoCardDTO;
import com.fantechs.common.base.general.dto.mes.pm.ProcessListWorkOrderDTO;
import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderCardPoolDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderCardPool;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderCardPool;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/11/21.
 */

public interface SmtWorkOrderCardPoolService extends IService<SmtWorkOrderCardPool> {

    List<SmtWorkOrderCardPoolDto> findList(SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool);
    //通过流程单获取工单相关信息
    ProcessListWorkOrderDTO selectWorkOrderDtoByWorkOrderCardId(String workOrderCardId);
    //批量更新流程卡状态
    int batchUpdateStatus(List<SmtWorkOrderCardPool> smtWorkOrderCardPoolList);
    //获取未开工的部件流程卡
    List<NoPutIntoCardDTO> getNoPutIntoCard(Long parentId);
    //通过工单流转卡任务池ID和工序ID获取报工数量
    ProcessListWorkOrderDTO selectWorkOrderDtoByWorkOrderCardPoolIdAndProcessId(String workOrderCardPoolId,String processId);

    List<NoPutIntoCardDTO> getAppointIntoCard(SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool);
}
