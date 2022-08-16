package com.fantechs.provider.ews.service;

import com.fantechs.common.base.general.dto.ews.EwsProcessSchedulingDto;
import com.fantechs.common.base.general.entity.ews.EwsProcessScheduling;
import com.fantechs.common.base.general.entity.ews.search.SearchEwsProcessScheduling;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by mr.lei on 2021/03/08.
 */

public interface EwsProcessSchedulingService extends IService<EwsProcessScheduling> {
    /**
     * 查询排程
     * @param searchEwsProcessScheduling
     * @return
     */
    List<EwsProcessSchedulingDto> findList(SearchEwsProcessScheduling searchEwsProcessScheduling);

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


    /**
     * 立即执行
     * @param Id
     * @return
     */
    int immediately(Long Id);
}
