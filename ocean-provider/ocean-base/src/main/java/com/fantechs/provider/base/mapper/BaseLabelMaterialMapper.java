package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseLabelMaterialDto;
import com.fantechs.common.base.general.entity.basic.BaseLabelMaterial;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseLabelMaterialMapper extends MyMapper<BaseLabelMaterial> {
    List<BaseLabelMaterialDto> findList(Map<String, Object> map);

    List<BaseLabelMaterial> findEqualLabel(Map<String,Object> map);
}