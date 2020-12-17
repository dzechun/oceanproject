package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.general.entity.qms.QmsQualityInspectionDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.qms.mapper.QmsQualityInspectionDetMapper;
import com.fantechs.provider.qms.service.QmsQualityInspectionDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/16.
 */
@Service
public class QmsQualityInspectionDetServiceImpl extends BaseService<QmsQualityInspectionDet> implements QmsQualityInspectionDetService {

    @Resource
    private QmsQualityInspectionDetMapper qmsQualityInspectionDetMapper;

    @Override
    public List<QmsQualityInspectionDet> findList(Map<String, Object> map) {
        return qmsQualityInspectionDetMapper.findList(map);
    }
}
