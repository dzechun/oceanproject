package com.fantechs.service;

import com.fantechs.entity.MonthInOutModel;

import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/11/22
 */
public interface MonthInOutService {
    List<MonthInOutModel> findOutList(Map<String,Object> map);
}
