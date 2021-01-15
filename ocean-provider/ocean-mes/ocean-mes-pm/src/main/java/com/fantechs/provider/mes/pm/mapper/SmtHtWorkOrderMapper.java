package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.entity.mes.pm.history.SmtHtWorkOrder;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrder;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtHtWorkOrderMapper extends MyMapper<SmtHtWorkOrder> {
    List<SmtHtWorkOrder> findList(SearchSmtWorkOrder searchSmtWorkOrder);
}