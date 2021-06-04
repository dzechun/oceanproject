package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrderDetSample;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.qms.mapper.QmsIpqcInspectionOrderDetSampleMapper;
import com.fantechs.provider.qms.service.QmsIpqcInspectionOrderDetSampleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/04.
 */
@Service
public class QmsIpqcInspectionOrderDetSampleServiceImpl extends BaseService<QmsIpqcInspectionOrderDetSample> implements QmsIpqcInspectionOrderDetSampleService {

    @Resource
    private QmsIpqcInspectionOrderDetSampleMapper qmsIpqcInspectionOrderDetSampleMapper;

    @Override
    public List<QmsIpqcInspectionOrderDetSample> findList(Map<String, Object> map) {
        return qmsIpqcInspectionOrderDetSampleMapper.findList(map);
    }
}
