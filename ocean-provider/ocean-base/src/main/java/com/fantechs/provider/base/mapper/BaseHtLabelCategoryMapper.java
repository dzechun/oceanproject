package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtLabelCategory;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabelCategory;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BaseHtLabelCategoryMapper extends MyMapper<BaseHtLabelCategory> {
    List<BaseHtLabelCategory> findList(SearchBaseLabelCategory searchBaseLabelCategory);
}