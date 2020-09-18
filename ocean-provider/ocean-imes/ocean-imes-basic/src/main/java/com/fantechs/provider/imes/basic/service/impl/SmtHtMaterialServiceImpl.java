package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.entity.basic.history.SmtHtMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterial;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.basic.mapper.SmtHtMaterialMapper;
import com.fantechs.provider.imes.basic.service.SmtHtMaterialService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SmtHtMaterialServiceImpl extends BaseService<SmtHtMaterial> implements SmtHtMaterialService {

    @Resource
    private SmtHtMaterialMapper smtHtMaterialMapper;

    @Override
    public List<SmtHtMaterial> findHtList(SearchSmtMaterial searchSmtMaterial) {
        return smtHtMaterialMapper.findHtList(searchSmtMaterial);
    }
}
