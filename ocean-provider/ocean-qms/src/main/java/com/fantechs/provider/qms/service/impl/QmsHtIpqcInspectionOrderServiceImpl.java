package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.general.entity.qms.history.QmsHtIpqcInspectionOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.qms.mapper.QmsHtIpqcInspectionOrderMapper;
import com.fantechs.provider.qms.service.QmsHtIpqcInspectionOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/02.
 */
@Service
public class QmsHtIpqcInspectionOrderServiceImpl extends BaseService<QmsHtIpqcInspectionOrder> implements QmsHtIpqcInspectionOrderService {

    @Resource
    private QmsHtIpqcInspectionOrderMapper qmsHtIpqcInspectionOrderMapper;

    @Override
    public List<QmsHtIpqcInspectionOrder> findHtList(Map<String, Object> map) {
        return qmsHtIpqcInspectionOrderMapper.findHtList(map);
    }
}
