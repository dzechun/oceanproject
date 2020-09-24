package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.history.SmtHtWarehouseArea;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface SmtHtWarehouseAreaMapper extends MyMapper<SmtHtWarehouseArea> {
    List<SmtHtWarehouseArea> findHtList (Map<String,Object> map);
}