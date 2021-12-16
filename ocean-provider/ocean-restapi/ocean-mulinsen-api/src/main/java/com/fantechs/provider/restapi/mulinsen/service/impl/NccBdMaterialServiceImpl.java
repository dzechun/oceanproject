package com.fantechs.provider.restapi.mulinsen.service.impl;

import com.fantechs.common.base.general.dto.mulinsen.NccBdMaterialDto;
import com.fantechs.common.base.general.entity.mulinsen.NccBdMaterial;
import com.fantechs.common.base.general.entity.mulinsen.search.SearchNccBdMaterial;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.restapi.mulinsen.mapper.NccBdMaterialMapper;
import com.fantechs.provider.restapi.mulinsen.service.NccBdMaterialService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class NccBdMaterialServiceImpl extends BaseService<NccBdMaterial> implements NccBdMaterialService {

    @Resource
    private NccBdMaterialMapper nccBdMaterialMapper;

    @Override
    public List<NccBdMaterialDto> findList(SearchNccBdMaterial searchNccBdMaterial) {
        return nccBdMaterialMapper.findList(searchNccBdMaterial);
    }

}
