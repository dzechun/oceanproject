package com.fantechs.provider.imes.basic.mapper;



import com.fantechs.common.base.entity.basic.history.SmtHtMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterial;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtHtMaterialMapper extends MyMapper<SmtHtMaterial> {
    List<SmtHtMaterial> findHtList(SearchSmtMaterial searchSmtMaterial);
}