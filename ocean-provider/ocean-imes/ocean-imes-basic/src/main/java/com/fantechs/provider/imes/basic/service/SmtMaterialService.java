package com.fantechs.provider.imes.basic.service;



import com.fantechs.common.base.entity.basic.SmtMaterial;
import com.fantechs.common.base.entity.basic.SmtStorage;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterial;
import com.fantechs.common.base.support.IService;

import java.util.List;

public interface SmtMaterialService  extends IService<SmtMaterial>{

    List<SmtMaterial> findList(SearchSmtMaterial searchSmtMaterial);

    //根据编码进行批量更新
    int batchUpdateByCode(List<SmtMaterial> smtMaterials);
}
