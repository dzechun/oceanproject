package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.dto.basic.SmtWarehouseAreaDto;
import com.fantechs.common.base.entity.basic.SmtWarehouseArea;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface SmtWarehouseAreaMapper extends MyMapper<SmtWarehouseArea> {
   List<SmtWarehouseAreaDto> findList(Map<String,Object> map);
}