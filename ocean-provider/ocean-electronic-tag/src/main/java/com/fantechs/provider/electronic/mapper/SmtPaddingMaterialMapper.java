package com.fantechs.provider.electronic.mapper;


import com.fantechs.common.base.electronic.dto.SmtPaddingMaterialDto;
import com.fantechs.common.base.electronic.entity.SmtPaddingMaterial;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmtPaddingMaterialMapper extends MyMapper<SmtPaddingMaterial> {

    List<SmtPaddingMaterialDto> findList(Map<String, Object> map);
}