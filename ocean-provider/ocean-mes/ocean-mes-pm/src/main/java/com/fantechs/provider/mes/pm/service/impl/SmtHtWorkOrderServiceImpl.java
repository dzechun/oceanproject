package com.fantechs.provider.mes.pm.service.impl;


import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtWorkOrder;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.pm.mapper.SmtHtWorkOrderMapper;
import com.fantechs.provider.mes.pm.service.SmtHtWorkOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by wcz on 2020/10/14.
 */
@Service
public class SmtHtWorkOrderServiceImpl extends BaseService<MesPmHtWorkOrder> implements SmtHtWorkOrderService {

         @Resource
         private SmtHtWorkOrderMapper smtHtWorkOrderMapper;

        @Override
        public List<MesPmHtWorkOrder> findList(SearchMesPmWorkOrder searchMesPmWorkOrder) {
            return smtHtWorkOrderMapper.findList(searchMesPmWorkOrder);
        }
}
