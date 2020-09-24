package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.history.SmtHtStorageMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageMaterial;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtHtStorageMaterialMapper extends MyMapper<SmtHtStorageMaterial> {
    List<SmtHtStorageMaterial> findHtList(SearchSmtStorageMaterial searchSmtStorageMaterial);
}