package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface MesPmHtWorkOrderMapper extends MyMapper<MesPmHtWorkOrder> {
    List<MesPmHtWorkOrder> findList(SearchMesPmWorkOrder searchMesPmWorkOrder);
}
