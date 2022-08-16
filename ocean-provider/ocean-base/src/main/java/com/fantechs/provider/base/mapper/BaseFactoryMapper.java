package com.fantechs.provider.base.mapper;



import com.fantechs.common.base.general.dto.basic.BaseFactoryDto;
import com.fantechs.common.base.general.entity.basic.BaseFactory;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface BaseFactoryMapper extends MyMapper<BaseFactory> {
    List<BaseFactoryDto> findList(Map<String, Object> map);
}