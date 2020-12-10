package com.fantechs.provider.imes.basic.mapper;



import com.fantechs.common.base.entity.basic.SmtMaterial;
import com.fantechs.common.base.entity.basic.SmtStorage;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterial;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtMaterialMapper extends MyMapper<SmtMaterial> {

    List<SmtMaterial> findList(SearchSmtMaterial searchSmtMaterial);

    //根据编码进行批量更新
    int batchUpdateByCode(List<SmtMaterial> smtMaterials);
}