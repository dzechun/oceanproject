package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtLabel;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabel;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtLabelMapper;
import com.fantechs.provider.base.service.BaseHtLabelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author Mr.Lei
* @create 2020/12/17.
*/
@Service
public class BaseHtLabelServiceImpl extends BaseService<BaseHtLabel> implements BaseHtLabelService {

    @Resource
    private BaseHtLabelMapper baseHtLabelMapper;

    @Override
    public List<BaseHtLabel> findList(SearchBaseLabel searchBaseLabel) {
        return baseHtLabelMapper.findList(searchBaseLabel);
    }
}
