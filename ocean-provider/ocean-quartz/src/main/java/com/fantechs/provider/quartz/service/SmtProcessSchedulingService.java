package com.fantechs.provider.quartz.service;

import com.fantechs.common.base.general.dto.bcm.SmtProcessSchedulingDto;
import com.fantechs.common.base.general.entity.bcm.SmtProcessScheduling;
import com.fantechs.common.base.general.entity.bcm.search.SearchSmtProcessScheduling;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by mr.lei on 2021/03/08.
 */

public interface SmtProcessSchedulingService extends IService<SmtProcessScheduling> {
    /**
     * 查询排程
     * @param searchSmtProcessScheduling
     * @return
     */
    List<SmtProcessSchedulingDto> findList(SearchSmtProcessScheduling searchSmtProcessScheduling);

    /**
     * 开始任务
     * @param Id
     * @return
     */
    int start(Long Id);

    /**
     * 暂停任务
     * @param Id
     * @return
     */
    int stop(Long Id);

    /**
     * 查看任务详情
     * @param Id
     * @return
     */
    List<Map<String, Object>> detail(Long Id);
}
