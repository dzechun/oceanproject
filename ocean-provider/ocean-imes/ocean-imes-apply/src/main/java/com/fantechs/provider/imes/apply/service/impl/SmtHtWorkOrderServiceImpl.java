package com.fantechs.provider.imes.apply.service.impl;


import com.fantechs.common.base.entity.apply.history.SmtHtWorkOrder;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.apply.mapper.SmtHtWorkOrderMapper;
import com.fantechs.provider.imes.apply.service.SmtHtWorkOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by wcz on 2020/10/14.
 */
@Service
public class SmtHtWorkOrderServiceImpl extends BaseService<SmtHtWorkOrder> implements SmtHtWorkOrderService {

         @Resource
         private SmtHtWorkOrderMapper smtHtWorkOrderMapper;

        @Override
        public List<SmtHtWorkOrder> findList(SearchSmtWorkOrder searchSmtWorkOrder) {
            return smtHtWorkOrderMapper.findList(searchSmtWorkOrder);
        }
}
