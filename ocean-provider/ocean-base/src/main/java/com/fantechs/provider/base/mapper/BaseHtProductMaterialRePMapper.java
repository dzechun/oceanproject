package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseProductProcessReM;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductMaterialReP;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductMaterialReP;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseHtProductMaterialRePMapper extends MyMapper<BaseHtProductMaterialReP> {
    List<BaseHtProductMaterialReP> findHtList(Map<String,Object> map);
}