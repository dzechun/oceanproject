package com.fantechs.provider.quartz.service;

import com.fantechs.common.base.general.dto.basic.BaseProcessSchedulingDto;
import com.fantechs.common.base.general.entity.basic.BaseProcessScheduling;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProcessScheduling;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by mr.lei on 2021/03/08.
 */

public interface SmtProcessSchedulingService extends IService<BaseProcessScheduling> {
    /**
     * 查询排程
     * @param searchBaseProcessScheduling
     * @return
     */
    List<BaseProcessSchedulingDto> findList(SearchBaseProcessScheduling searchBaseProcessScheduling);

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
