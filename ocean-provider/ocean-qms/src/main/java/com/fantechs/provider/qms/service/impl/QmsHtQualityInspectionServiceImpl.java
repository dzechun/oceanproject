package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.general.dto.qms.QmsQualityInspectionDto;
import com.fantechs.common.base.general.entity.qms.history.QmsHtQualityInspection;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.qms.mapper.QmsHtQualityInspectionMapper;
import com.fantechs.provider.qms.service.QmsHtQualityInspectionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/16.
 */
@Service
public class QmsHtQualityInspectionServiceImpl extends BaseService<QmsHtQualityInspection> implements QmsHtQualityInspectionService {

    @Resource
    private QmsHtQualityInspectionMapper qmsHtQualityInspectionMapper;

    @Override
    public List<QmsHtQualityInspection> findHtList(Map<String, Object> map) {
        return qmsHtQualityInspectionMapper.findHtList(map);
    }
}
