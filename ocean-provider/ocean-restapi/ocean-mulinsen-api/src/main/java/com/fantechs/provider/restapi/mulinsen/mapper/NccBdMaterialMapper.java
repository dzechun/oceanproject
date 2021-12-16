package com.fantechs.provider.restapi.mulinsen.mapper;

import com.fantechs.common.base.general.dto.mulinsen.NccBdMaterialDto;
import com.fantechs.common.base.general.entity.mulinsen.NccBdMaterial;
import com.fantechs.common.base.general.entity.mulinsen.search.SearchNccBdMaterial;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NccBdMaterialMapper extends MyMapper<NccBdMaterial> {
    List<NccBdMaterialDto> findList(SearchNccBdMaterial searchNccBdMaterial);
}