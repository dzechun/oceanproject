package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.entity.basic.history.SmtHtProcess;
import com.fantechs.common.base.entity.basic.search.SearchSmtProcess;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.basic.mapper.SmtHtProcessMapper;
import com.fantechs.provider.imes.basic.service.SmtHtProcessService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by wcz on 2020/09/25.
 */
@Service
public class SmtHtProcessServiceImpl extends BaseService<SmtHtProcess> implements SmtHtProcessService {

    @Resource
    private SmtHtProcessMapper smtHtProcessMapper;

    @Override
    public List<SmtHtProcess> findHtList(SearchSmtProcess searchSmtProcess) {
        return smtHtProcessMapper.findHtList(searchSmtProcess);
    }
}
