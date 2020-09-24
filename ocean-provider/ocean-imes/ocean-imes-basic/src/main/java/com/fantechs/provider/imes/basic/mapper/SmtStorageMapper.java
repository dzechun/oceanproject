package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.SmtStorage;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorage;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;


public interface SmtStorageMapper extends MyMapper<SmtStorage> {
    List<SmtStorage> findList(SearchSmtStorage searchSmtStorage);
}