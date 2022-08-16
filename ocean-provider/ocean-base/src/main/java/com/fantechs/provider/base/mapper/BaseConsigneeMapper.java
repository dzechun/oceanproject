package com.fantechs.provider.base.mapper;


import com.fantechs.common.base.general.dto.basic.BaseConsigneeDto;
import com.fantechs.common.base.general.entity.basic.BaseConsignee;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseConsigneeMapper extends MyMapper<BaseConsignee> {
    List<BaseConsigneeDto> findList(Map<String, Object> map);
}