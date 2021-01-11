package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.general.entity.qms.history.QmsHtPdaInspection;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.qms.mapper.QmsHtPdaInspectionMapper;
import com.fantechs.provider.qms.service.QmsHtPdaInspectionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/09.
 */
@Service
public class QmsHtPdaInspectionServiceImpl  extends BaseService<QmsHtPdaInspection> implements QmsHtPdaInspectionService {

    @Resource
    private QmsHtPdaInspectionMapper qmsHtPdaInspectionMapper;

    @Override
    public List<QmsHtPdaInspection> findHtList(Map<String, Object> map) {
        return qmsHtPdaInspectionMapper.findHtList(map);
    }
}
