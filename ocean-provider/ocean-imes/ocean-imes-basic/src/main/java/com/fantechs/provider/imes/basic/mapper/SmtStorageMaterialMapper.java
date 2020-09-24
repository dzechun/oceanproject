package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.SmtStorageMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageMaterial;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtStorageMaterialMapper extends MyMapper<SmtStorageMaterial> {
    List<SmtStorageMaterial> findList(SearchSmtStorageMaterial searchSmtStorageMaterial);
}