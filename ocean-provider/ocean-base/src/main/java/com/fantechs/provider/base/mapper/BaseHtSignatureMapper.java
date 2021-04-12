package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtSignature;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSignature;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface BaseHtSignatureMapper extends MyMapper<BaseHtSignature> {
    List<BaseHtSignature> findHtList(SearchBaseSignature searchBaseSignature);
}