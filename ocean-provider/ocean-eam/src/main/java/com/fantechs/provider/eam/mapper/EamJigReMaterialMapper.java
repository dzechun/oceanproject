package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamJigReMaterialDto;
import com.fantechs.common.base.general.entity.eam.EamJigReMaterial;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamJigReMaterialMapper extends MyMapper<EamJigReMaterial> {
    List<EamJigReMaterialDto> findList(Map<String,Object> map);

    List<EamJigReMaterialDto> findMaterial(Map<String,Object> map);
}