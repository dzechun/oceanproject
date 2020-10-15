package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.entity.basic.history.SmtHtProcessCategory;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.basic.mapper.SmtHtProcessCategoryMapper;
import com.fantechs.provider.imes.basic.service.SmtHtProcessCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/10/15.
 */
@Service
public class SmtHtProcessCategoryServiceImpl extends BaseService<SmtHtProcessCategory> implements SmtHtProcessCategoryService {

    @Resource
    private SmtHtProcessCategoryMapper smtHtProcessCategoryMapper;

    @Override
    public List<SmtHtProcessCategory> findHtList(Map<String, Object> map) {
        return smtHtProcessCategoryMapper.findHtList(map);
    }
}
