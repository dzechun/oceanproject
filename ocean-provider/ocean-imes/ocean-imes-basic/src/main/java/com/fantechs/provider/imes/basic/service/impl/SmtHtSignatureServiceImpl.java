package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.entity.basic.SmtSignature;
import com.fantechs.common.base.entity.basic.history.SmtHtSignature;
import com.fantechs.common.base.entity.basic.search.SearchSmtSignature;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.basic.mapper.SmtHtSignatureMapper;
import com.fantechs.provider.imes.basic.service.SmtHtSignatureService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by wcz on 2020/09/24.
 */
@Service
public class SmtHtSignatureServiceImpl  extends BaseService<SmtHtSignature> implements SmtHtSignatureService {

    @Resource
    private SmtHtSignatureMapper smtHtSignatureMapper;

    @Override
    public List<SmtSignature> findHtList(SearchSmtSignature searchSmtSignature) {
        return smtHtSignatureMapper.findHtList(searchSmtSignature);
    }
}
