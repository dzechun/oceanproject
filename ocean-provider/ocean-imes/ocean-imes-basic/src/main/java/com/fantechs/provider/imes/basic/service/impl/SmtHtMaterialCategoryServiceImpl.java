package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.entity.basic.history.SmtHtMaterialCategory;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.basic.mapper.SmtHtMaterialCategoryMapper;
import com.fantechs.provider.imes.basic.service.SmtHtMaterialCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/31.
 */
@Service
public class SmtHtMaterialCategoryServiceImpl extends BaseService<SmtHtMaterialCategory> implements SmtHtMaterialCategoryService {

    @Resource
    private SmtHtMaterialCategoryMapper smtHtMaterialCategoryMapper;

    @Override
    public List<SmtHtMaterialCategory> findHtList(Map<String, Object> map) {
        return smtHtMaterialCategoryMapper.findHtList(map);
    }
}
