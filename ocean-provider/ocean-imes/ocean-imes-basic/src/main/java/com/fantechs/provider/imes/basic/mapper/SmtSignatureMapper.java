package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.SmtSignature;
import com.fantechs.common.base.entity.basic.search.SearchSmtSignature;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtSignatureMapper extends MyMapper<SmtSignature> {
    List<SmtSignature> findList(SearchSmtSignature searchSmtSignature);

}