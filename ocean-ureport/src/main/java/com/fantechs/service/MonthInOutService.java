package com.fantechs.service;

import com.fantechs.dto.MonthInDto;
import com.fantechs.dto.MonthOutDto;
import com.fantechs.entity.MonthInOutModel;

import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/11/22
 */
public interface MonthInOutService {

    List<MonthInDto> findInList(Map<String,Object> map);

    List<MonthOutDto> findOutList(Map<String,Object> map);
}
