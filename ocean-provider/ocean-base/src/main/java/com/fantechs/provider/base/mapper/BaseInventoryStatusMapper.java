package com.fantechs.provider.base.mapper;


import com.fantechs.common.base.general.dto.basic.BaseKeyMaterialDto;
import com.fantechs.common.base.general.entity.basic.BaseInventoryStatus;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseInventoryStatusMapper extends MyMapper<BaseInventoryStatus> {
    List<BaseInventoryStatus> findList(Map<String,Object> map);
}