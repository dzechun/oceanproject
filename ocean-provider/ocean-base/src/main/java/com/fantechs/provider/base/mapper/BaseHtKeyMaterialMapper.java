package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtKeyMaterial;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseHtKeyMaterialMapper extends MyMapper<BaseHtKeyMaterial> {

    List<BaseHtKeyMaterial> findHtList(Map<String, Object> map);
}