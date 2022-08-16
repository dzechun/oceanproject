package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseMaterialCategoryDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterialCategory;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseMaterialCategoryMapper extends MyMapper<BaseMaterialCategory> {
    List<BaseMaterialCategoryDto> findList(Map<String, Object> map);

    List<BaseMaterialCategory> findById(Long parentId);

}
