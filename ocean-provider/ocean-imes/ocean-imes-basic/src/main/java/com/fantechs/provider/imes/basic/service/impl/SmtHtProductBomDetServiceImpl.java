package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.entity.basic.SmtProductBomDet;
import com.fantechs.common.base.entity.basic.history.SmtHtProductBomDet;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductBomDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.basic.mapper.SmtHtProductBomDetMapper;
import com.fantechs.provider.imes.basic.service.SmtHtProductBomDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by wcz on 2020/10/12.
 */
@Service
public class SmtHtProductBomDetServiceImpl extends BaseService<SmtHtProductBomDet> implements SmtHtProductBomDetService {

         @Resource
         private SmtHtProductBomDetMapper smtHtProductBomDetMapper;

        @Override
        public List<SmtProductBomDet> findList(SearchSmtProductBomDet searchSmtProductBomDet) {
            return smtHtProductBomDetMapper.findList(searchSmtProductBomDet);
        }
}
