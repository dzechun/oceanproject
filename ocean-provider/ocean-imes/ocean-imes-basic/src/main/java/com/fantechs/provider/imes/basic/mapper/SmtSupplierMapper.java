package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.SmtSupplier;
import com.fantechs.common.base.entity.basic.search.SearchSmtSupplier;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtSupplierMapper extends MyMapper<SmtSupplier> {

    List<SmtSupplier> findList(SearchSmtSupplier searchSmtSupplier);
}