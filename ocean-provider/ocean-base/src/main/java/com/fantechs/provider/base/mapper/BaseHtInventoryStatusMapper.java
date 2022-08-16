package com.fantechs.provider.base.mapper;


import com.fantechs.common.base.general.entity.basic.BaseInventoryStatus;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInventoryStatus;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseHtInventoryStatusMapper extends MyMapper<BaseHtInventoryStatus> {
    List<BaseHtInventoryStatus> findHtList(Map<String,Object> map);
}