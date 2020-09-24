package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.entity.basic.history.SmtHtStorageMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorage;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageMaterial;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.basic.mapper.SmtHtStorageMaterialMapper;
import com.fantechs.provider.imes.basic.service.SmtHtStorageMaterialService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by wcz on 2020/09/24.
 */
@Service
public class SmtHtStorageMaterialServiceImpl  extends BaseService<SmtHtStorageMaterial> implements SmtHtStorageMaterialService {

    @Resource
    private SmtHtStorageMaterialMapper smtHtStorageMaterialMapper;

    @Override
    public List<SmtHtStorageMaterial> findHtList(SearchSmtStorageMaterial searchSmtStorageMaterial) {
        return smtHtStorageMaterialMapper.findHtList(searchSmtStorageMaterial);
    }
}
