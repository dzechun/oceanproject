package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtMaterialSupplier;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseHtMaterialSupplierMapper extends MyMapper<BaseHtMaterialSupplier> {
    List<BaseHtMaterialSupplier> findHtList(Map<String, Object> map);
}