package com.fantechs.provider.imes.basic.service;



import com.fantechs.common.base.entity.basic.SmtMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterial;

import java.util.List;

public interface SmtMaterialService {

    List<SmtMaterial> findList(SearchSmtMaterial searchSmtMaterial);

    int insert(SmtMaterial smtMaterial);

    int updateById(SmtMaterial smtMaterial);

    int deleteByIds(List<Long> materialIds);

    SmtMaterial findById(Long materialId);
}
