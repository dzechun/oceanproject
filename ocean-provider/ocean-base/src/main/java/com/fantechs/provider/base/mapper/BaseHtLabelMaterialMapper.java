package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtLabelMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabelMaterial;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BaseHtLabelMaterialMapper extends MyMapper<BaseHtLabelMaterial> {
    List<BaseHtLabelMaterial> findList(SearchBaseLabelMaterial searchBaseLabelMaterial);
}