package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseSignature;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSignature;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface BaseSignatureMapper extends MyMapper<BaseSignature> {
    List<BaseSignature> findList(SearchBaseSignature searchBaseSignature);

}