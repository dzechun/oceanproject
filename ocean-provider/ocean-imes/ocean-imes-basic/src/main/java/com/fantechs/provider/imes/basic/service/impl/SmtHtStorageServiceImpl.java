package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.entity.basic.history.SmtHtStorage;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorage;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.basic.mapper.SmtHtStorageMapper;
import com.fantechs.provider.imes.basic.service.SmtHtStorageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by wcz on 2020/09/23.
 */
@Service
public class SmtHtStorageServiceImpl extends BaseService<SmtHtStorage> implements SmtHtStorageService {

    @Resource
    private SmtHtStorageMapper smtHtStorageMapper;

    @Override
    public List<SmtHtStorage> findHtList(SearchSmtStorage searchSmtStorage) {
        return smtHtStorageMapper.findHtList(searchSmtStorage);
    }
}
