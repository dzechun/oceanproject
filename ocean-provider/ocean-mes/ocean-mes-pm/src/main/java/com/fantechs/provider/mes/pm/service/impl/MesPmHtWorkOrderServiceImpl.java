package com.fantechs.provider.mes.pm.service.impl;


import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.pm.mapper.MesPmHtWorkOrderMapper;
import com.fantechs.provider.mes.pm.service.MesPmHtWorkOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by wcz on 2020/10/14.
 */
@Service
public class MesPmHtWorkOrderServiceImpl extends BaseService<MesPmHtWorkOrder> implements MesPmHtWorkOrderService {

         @Resource
         private MesPmHtWorkOrderMapper mesPmHtWorkOrderMapper;

        @Override
        public List<MesPmHtWorkOrder> findList(SearchMesPmWorkOrder searchMesPmWorkOrder) {
            return mesPmHtWorkOrderMapper.findList(searchMesPmWorkOrder);
        }
}
