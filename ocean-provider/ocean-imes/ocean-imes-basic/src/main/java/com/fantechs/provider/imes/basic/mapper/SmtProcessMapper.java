package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.SmtProcess;
import com.fantechs.common.base.entity.basic.search.SearchSmtProcess;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtProcessMapper extends MyMapper<SmtProcess> {
    List<SmtProcess> findList(SearchSmtProcess searchSmtProcess);
}