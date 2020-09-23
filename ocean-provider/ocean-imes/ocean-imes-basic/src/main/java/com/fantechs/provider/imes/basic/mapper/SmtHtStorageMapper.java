package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.SmtHtStorage;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorage;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtHtStorageMapper extends MyMapper<SmtHtStorage> {
    List<SmtHtStorage> findHtList(SearchSmtStorage searchSmtStorage);
}