package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.general.entity.qms.history.QmsHtInspectionType;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.qms.mapper.QmsHtInspectionTypeMapper;
import com.fantechs.provider.qms.service.QmsHtInspectionTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/23.
 */
@Service
public class QmsHtInspectionTypeServiceImpl extends BaseService<QmsHtInspectionType> implements QmsHtInspectionTypeService {

    @Resource
    private QmsHtInspectionTypeMapper qmsHtInspectionTypeMapper;


    @Override
    public List<QmsHtInspectionType> findHtList(Map<String, Object> map) {
        return qmsHtInspectionTypeMapper.findHtList(map);
    }
}
