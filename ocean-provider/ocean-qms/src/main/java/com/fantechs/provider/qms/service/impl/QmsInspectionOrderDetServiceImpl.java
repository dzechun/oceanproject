package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.general.entity.qms.QmsInspectionOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.qms.mapper.QmsInspectionOrderDetMapper;
import com.fantechs.provider.qms.service.QmsInspectionOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/25.
 */
@Service
public class QmsInspectionOrderDetServiceImpl extends BaseService<QmsInspectionOrderDet> implements QmsInspectionOrderDetService {

    @Resource
    private QmsInspectionOrderDetMapper qmsInspectionOrderDetMapper;

    @Override
    public List<QmsInspectionOrderDet> findList(Map<String, Object> map) {
        return qmsInspectionOrderDetMapper.findList(map);
    }
}
