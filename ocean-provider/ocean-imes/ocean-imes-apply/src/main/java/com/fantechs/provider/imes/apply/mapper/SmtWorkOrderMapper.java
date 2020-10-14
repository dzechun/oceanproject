package com.fantechs.provider.imes.apply.mapper;

import com.fantechs.common.base.entity.apply.SmtWorkOrder;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrder;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtWorkOrderMapper extends MyMapper<SmtWorkOrder> {
    List<SmtWorkOrder> findList(SearchSmtWorkOrder searchSmtWorkOrder);
}