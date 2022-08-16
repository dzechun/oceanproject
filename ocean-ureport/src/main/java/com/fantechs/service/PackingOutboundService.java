package com.fantechs.service;

import com.fantechs.entity.PackingOutboundModel;

import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/10/27
 */
public interface PackingOutboundService {
    List<PackingOutboundModel> findList(Map<String,Object> map);
}
