package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.general.entity.qms.history.QmsHtFirstInspection;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.qms.mapper.QmsHtFirstInspectionMapper;
import com.fantechs.provider.qms.service.QmsHtFirstInspectionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/09.
 */
@Service
public class QmsHtFirstInspectionServiceImpl  extends BaseService<QmsHtFirstInspection> implements QmsHtFirstInspectionService {

    @Resource
    private QmsHtFirstInspectionMapper qmsHtFirstInspectionMapper;

    @Override
    public List<QmsHtFirstInspection> findHtList(Map<String, Object> map) {
        return qmsHtFirstInspectionMapper.findHtList(map);
    }
}
