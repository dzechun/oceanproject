package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamJigMaterialDto;
import com.fantechs.common.base.general.entity.eam.EamJigMaterial;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamJigMaterialMapper extends MyMapper<EamJigMaterial> {
    List<EamJigMaterialDto> findList(Map<String,Object> map);

    List<EamJigMaterialDto> findJigList(Map<String,Object> map);
}