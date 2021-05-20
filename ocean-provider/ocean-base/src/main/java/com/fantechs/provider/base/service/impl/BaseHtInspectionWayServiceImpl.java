package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionWay;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtInspectionWayMapper;
import com.fantechs.provider.base.service.BaseHtInspectionWayService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/19.
 */
@Service
public class BaseHtInspectionWayServiceImpl extends BaseService<BaseHtInspectionWay> implements BaseHtInspectionWayService {

    @Resource
    private BaseHtInspectionWayMapper baseHtInspectionWayMapper;

    @Override
    public List<BaseHtInspectionWay> findHtList(Map<String, Object> map) {
        return baseHtInspectionWayMapper.findHtList(map);
    }
}
