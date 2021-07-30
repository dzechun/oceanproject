package com.fantechs.service.impl;

import com.fantechs.entity.WmsInnerInventoryModel;
import com.fantechs.mapper.WmsInnerInventoryMapper;
import com.fantechs.service.WmsInnerInventoryUreportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/7/29
 */
@Service
public class WmsInnerInventoryUreportServiceImpl implements WmsInnerInventoryUreportService {

    @Resource
    private WmsInnerInventoryMapper wmsInnerInventoryMapper;

    @Override
    public List<WmsInnerInventoryModel> findList(Map<String, Object> map) {
        return wmsInnerInventoryMapper.findList(map);
    }
}
