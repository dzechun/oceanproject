package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseMaterialSupplierDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterialSupplier;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterialSupplier;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseMaterialSupplierMapper extends MyMapper<BaseMaterialSupplier> {
    List<BaseMaterialSupplierDto> findList(Map<String, Object> map);
}