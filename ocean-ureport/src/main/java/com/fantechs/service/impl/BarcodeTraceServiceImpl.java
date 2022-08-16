package com.fantechs.service.impl;

import com.fantechs.entity.BarcodeTraceModel;
import com.fantechs.mapper.BarcodeTraceMapper;
import com.fantechs.service.BarcodeTraceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/7/29
 */
@Service
public class BarcodeTraceServiceImpl implements BarcodeTraceService {

    @Resource
    private BarcodeTraceMapper barcodeTraceMapper;

    @Override
    public List<BarcodeTraceModel> findList(Map<String, Object> map) {
        return barcodeTraceMapper.findList(map);
    }
}
