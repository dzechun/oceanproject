package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseWarehouseAreaDto;
import com.fantechs.common.base.general.entity.basic.BaseWarehouseArea;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface BaseWarehouseAreaMapper extends MyMapper<BaseWarehouseArea> {
   List<BaseWarehouseAreaDto> findList(Map<String,Object> map);
}