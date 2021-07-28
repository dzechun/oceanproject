package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamJigCategoryDto;
import com.fantechs.common.base.general.entity.eam.EamJigCategory;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamJigCategoryMapper extends MyMapper<EamJigCategory> {
    List<EamJigCategoryDto> findList(Map<String,Object> map);
}