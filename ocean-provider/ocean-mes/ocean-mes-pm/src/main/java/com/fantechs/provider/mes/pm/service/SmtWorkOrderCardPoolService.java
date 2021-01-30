package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.dto.mes.pm.ProcessListWorkOrderDTO;
import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderCardPoolDto;
import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderDto;
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
}
