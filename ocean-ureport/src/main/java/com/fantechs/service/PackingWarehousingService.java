package com.fantechs.service;

import com.fantechs.entity.PackingWarehousingModel;

import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/10/27
 */
public interface PackingWarehousingService {
    List<PackingWarehousingModel> findList(Map<String,Object> map);
}
