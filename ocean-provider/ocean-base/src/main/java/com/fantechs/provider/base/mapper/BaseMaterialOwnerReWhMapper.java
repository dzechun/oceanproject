package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseMaterialOwnerReWhDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterialOwnerReWh;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterialOwnerReWh;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseMaterialOwnerReWhMapper extends MyMapper<BaseMaterialOwnerReWh> {
    List<BaseMaterialOwnerReWhDto> findList(Map<String, Object> map);
}