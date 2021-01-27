package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.general.entity.qms.history.QmsHtBadItem;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.qms.mapper.QmsHtBadItemMapper;
import com.fantechs.provider.qms.service.QmsHtBadItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/27.
 */
@Service
public class QmsHtBadItemServiceImpl extends BaseService<QmsHtBadItem> implements QmsHtBadItemService {

    @Resource
    private QmsHtBadItemMapper qmsHtBadItemMapper;

    @Override
    public List<QmsHtBadItem> findHtList(Map<String, Object> map) {
        return qmsHtBadItemMapper.findHtList(map);
    }
}
