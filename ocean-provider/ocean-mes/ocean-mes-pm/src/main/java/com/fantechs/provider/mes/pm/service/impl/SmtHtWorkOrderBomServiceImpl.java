package com.fantechs.provider.mes.pm.service.impl;


import com.fantechs.common.base.general.entity.mes.pm.history.SmtHtWorkOrderBom;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderBom;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.pm.mapper.SmtHtWorkOrderBomMapper;
import com.fantechs.provider.mes.pm.service.SmtHtWorkOrderBomService;
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
