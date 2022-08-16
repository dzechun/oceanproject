package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseSupplier;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplier;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface BaseSupplierMapper extends MyMapper<BaseSupplier> {

    List<BaseSupplier> findList(Map<String, Object> map);

    List<BaseSupplier> findInspectionSupplierList(Map<String, Object> map);
}