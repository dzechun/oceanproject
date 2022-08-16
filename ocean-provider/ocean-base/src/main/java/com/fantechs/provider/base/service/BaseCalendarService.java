package com.fantechs.provider.base.service;


import com.fantechs.common.base.general.dto.basic.BaseCalendarDto;
import com.fantechs.common.base.general.entity.basic.BaseCalendar;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/21.
 */

public interface BaseCalendarService extends IService<BaseCalendar> {

    List<BaseCalendarDto> findList(Map<String, Object> map);

    //日历顺延
    void calendarPostpone();

/*    //日历查询
    BaseCalendarDto findAllWorkShiftTime(Long proLineId, String date);*/
}
