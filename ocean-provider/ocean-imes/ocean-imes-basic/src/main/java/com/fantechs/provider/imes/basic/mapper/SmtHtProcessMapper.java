package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.history.SmtHtProcess;
import com.fantechs.common.base.entity.basic.search.SearchSmtProcess;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtHtProcessMapper extends MyMapper<SmtHtProcess> {
    List<SmtHtProcess> findHtList(SearchSmtProcess searchSmtProcess);
}