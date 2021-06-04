package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.qms.mapper.QmsIpqcInspectionOrderDetMapper;
import com.fantechs.provider.qms.service.QmsIpqcInspectionOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/04.
 */
@Service
public class QmsIpqcInspectionOrderDetServiceImpl extends BaseService<QmsIpqcInspectionOrderDet> implements QmsIpqcInspectionOrderDetService {

    @Resource
    private QmsIpqcInspectionOrderDetMapper qmsIpqcInspectionOrderDetMapper;

    @Override
    public List<QmsIpqcInspectionOrderDet> findList(Map<String, Object> map) {
        return qmsIpqcInspectionOrderDetMapper.findList(map);
    }
}
