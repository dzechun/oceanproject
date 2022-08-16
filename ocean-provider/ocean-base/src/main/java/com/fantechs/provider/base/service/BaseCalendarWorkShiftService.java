package com.fantechs.provider.base.service;


import com.fantechs.common.base.general.dto.basic.BaseCalendarWorkShiftDto;
import com.fantechs.common.base.general.entity.basic.BaseCalendarWorkShift;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/21.
 */

public interface BaseCalendarWorkShiftService extends IService<BaseCalendarWorkShift> {

    List<BaseCalendarWorkShiftDto> findList(Map<String, Object> map);

    int batchSave(List<BaseCalendarWorkShift> baseCalendarWorkShifts);

    //根据日历和日期清空班次
    int deleteByCalendarIdAndDay(Integer CalendarId,Byte day);


}
