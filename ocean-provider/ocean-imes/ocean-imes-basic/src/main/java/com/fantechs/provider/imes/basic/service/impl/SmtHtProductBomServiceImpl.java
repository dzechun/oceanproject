package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.entity.basic.history.SmtHtProductBom;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductBom;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.basic.mapper.SmtHtProductBomMapper;
import com.fantechs.provider.imes.basic.service.SmtHtProductBomService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by wcz on 2020/10/12.
 */
@Service
public class SmtHtProductBomServiceImpl extends BaseService<SmtHtProductBom> implements SmtHtProductBomService {

         @Resource
         private SmtHtProductBomMapper smtHtProductBomMapper;


        @Override
        public List<SmtHtProductBom> findList(SearchSmtProductBom searchSmtProductBom) {
            return smtHtProductBomMapper.findList(searchSmtProductBom);
        }
}
