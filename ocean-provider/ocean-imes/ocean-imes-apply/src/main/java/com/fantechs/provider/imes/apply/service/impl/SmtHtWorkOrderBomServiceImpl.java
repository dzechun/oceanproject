package com.fantechs.provider.imes.apply.service.impl;


import com.fantechs.common.base.entity.apply.history.SmtHtWorkOrderBom;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderBom;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.apply.mapper.SmtHtWorkOrderBomMapper;
import com.fantechs.provider.imes.apply.service.SmtHtWorkOrderBomService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by wcz on 2020/10/14.
 */
@Service
public class SmtHtWorkOrderBomServiceImpl extends BaseService<SmtHtWorkOrderBom> implements SmtHtWorkOrderBomService {

         @Resource
         private SmtHtWorkOrderBomMapper smtHtWorkOrderBomMapper;

        @Override
        public List<SmtHtWorkOrderBom> findList(SearchSmtWorkOrderBom searchSmtWorkOrderBom) {
            return smtHtWorkOrderBomMapper.findList(searchSmtWorkOrderBom);
        }
}
