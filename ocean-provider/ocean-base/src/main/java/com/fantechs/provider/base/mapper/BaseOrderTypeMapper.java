package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseOrderTypeDto;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.entity.basic.BaseOrderType;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseOrderTypeMapper extends MyMapper<BaseOrderType> {

    List<BaseOrderTypeDto> findList(Map<String, Object> map);
}