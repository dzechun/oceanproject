package com.fantechs.provider.imes.apply.mapper;

import com.fantechs.common.base.entity.apply.SmtWorkOrder;
import com.fantechs.common.base.entity.apply.history.SmtHtWorkOrder;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrder;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtHtWorkOrderMapper extends MyMapper<SmtHtWorkOrder> {
    List<SmtWorkOrder> findList(SearchSmtWorkOrder searchSmtWorkOrder);
}