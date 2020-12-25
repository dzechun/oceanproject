package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.general.entity.qms.history.QmsHtInspectionItem;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.qms.mapper.QmsHtInspectionItemMapper;
import com.fantechs.provider.qms.service.QmsHtInspectionItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/25.
 */
@Service
public class QmsHtInspectionItemServiceImpl extends BaseService<QmsHtInspectionItem> implements QmsHtInspectionItemService {

    @Resource
    private QmsHtInspectionItemMapper qmsHtInspectionItemMapper;


    @Override
    public List<QmsHtInspectionItem> findHtList(Map<String, Object> map) {
        return qmsHtInspectionItemMapper.findHtList(map);
    }
}
