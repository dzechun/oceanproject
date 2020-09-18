package com.fantechs.provider.imes.basic.service;



import com.fantechs.common.base.entity.basic.history.SmtHtMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterial;

import java.util.List;

public interface SmtHtMaterialService {

    List<SmtHtMaterial> findHtList(SearchSmtMaterial searchSmtMaterial);
}
