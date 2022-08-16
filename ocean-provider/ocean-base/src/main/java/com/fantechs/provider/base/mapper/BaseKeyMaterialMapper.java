package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseKeyMaterialDto;
import com.fantechs.common.base.general.entity.basic.BaseKeyMaterial;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseKeyMaterialMapper extends MyMapper<BaseKeyMaterial> {

    List<BaseKeyMaterialDto> findList(Map<String,Object> map);
}