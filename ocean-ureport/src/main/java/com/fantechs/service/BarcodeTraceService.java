package com.fantechs.service;

import com.fantechs.entity.BarcodeTraceModel;

import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/7/29
 */
public interface BarcodeTraceService {
    List<BarcodeTraceModel> findList(Map<String, Object> map);
}
