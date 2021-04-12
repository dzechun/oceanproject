package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.general.entity.basic.history.BaseHtProLine;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProLine;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtProLineMapper;
import com.fantechs.provider.base.service.BaseHtProLineService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BaseHtProLineServiceImpl extends BaseService<BaseHtProLine> implements BaseHtProLineService {

    @Resource
    private BaseHtProLineMapper baseHtProLineMapper;

    @Override
    public List<BaseHtProLine> selectHtProLines(SearchBaseProLine searchBaseProLine) {
        return baseHtProLineMapper.selectHtProLines(searchBaseProLine);
    }
}
