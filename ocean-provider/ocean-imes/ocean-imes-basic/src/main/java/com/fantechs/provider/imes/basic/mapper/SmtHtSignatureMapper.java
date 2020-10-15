package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.history.SmtHtSignature;
import com.fantechs.common.base.entity.basic.search.SearchSmtSignature;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtHtSignatureMapper extends MyMapper<SmtHtSignature> {
    List<SmtHtSignature> findHtList(SearchSmtSignature searchSmtSignature);
}