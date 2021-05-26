package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.general.entity.qms.history.QmsHtInspectionOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.qms.mapper.QmsHtInspectionOrderMapper;
import com.fantechs.provider.qms.service.QmsHtInspectionOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/25.
 */
@Service
public class QmsHtInspectionOrderServiceImpl extends BaseService<QmsHtInspectionOrder> implements QmsHtInspectionOrderService {

    @Resource
    private QmsHtInspectionOrderMapper qmsHtInspectionOrderMapper;

    @Override
    public List<QmsHtInspectionOrder> findHtList(Map<String, Object> map) {
        return qmsHtInspectionOrderMapper.findHtList(map);
    }
}
