package com.fantechs.provider.restapi.mulinsen.service;

import com.fantechs.common.base.general.dto.mulinsen.NccBdMaterialDto;
import com.fantechs.common.base.general.entity.mulinsen.NccBdMaterial;
import com.fantechs.common.base.general.entity.mulinsen.search.SearchNccBdMaterial;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

public interface NccBdMaterialService extends IService<NccBdMaterial> {
    List<NccBdMaterialDto> findList(SearchNccBdMaterial searchNccBdMaterial);
}
