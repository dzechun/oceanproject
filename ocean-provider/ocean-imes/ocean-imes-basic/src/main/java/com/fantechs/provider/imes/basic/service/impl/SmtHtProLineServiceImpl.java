package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.entity.basic.history.SmtHtProLine;
import com.fantechs.common.base.entity.basic.search.SearchSmtProLine;
import com.fantechs.provider.imes.basic.mapper.SmtHtProLineMapper;
import com.fantechs.provider.imes.basic.service.SmtHtProLineService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SmtHtProLineServiceImpl  implements SmtHtProLineService {

    @Resource
    private SmtHtProLineMapper smtHtProLineMapper;

    @Override
    public List<SmtHtProLine> selectHtProLines(SearchSmtProLine searchSmtProLine) {
        return smtHtProLineMapper.selectHtProLines(searchSmtProLine);
    }
}
