package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.dto.basic.SmtMaterialCategoryDto;
import com.fantechs.common.base.entity.basic.SmtMaterialCategory;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmtMaterialCategoryMapper extends MyMapper<SmtMaterialCategory> {
    List<SmtMaterialCategoryDto> findList(Map<String, Object> map);

    List<SmtMaterialCategory> findById(Long parentId);

}
