package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.general.entity.basic.history.BaseHtSignature;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSignature;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtSignatureMapper;
import com.fantechs.provider.base.service.BaseHtSignatureService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by wcz on 2020/09/24.
 */
@Service
public class BaseHtSignatureServiceImpl extends BaseService<BaseHtSignature> implements BaseHtSignatureService {

    @Resource
    private BaseHtSignatureMapper baseHtSignatureMapper;

    @Override
    public List<BaseHtSignature> findHtList(SearchBaseSignature searchBaseSignature) {
        return baseHtSignatureMapper.findHtList(searchBaseSignature);
    }
}
