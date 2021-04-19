package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseInspectionTypeDto;
import com.fantechs.common.base.general.entity.basic.BaseInspectionType;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseInspectionTypeMapper extends MyMapper<BaseInspectionType> {

    List<BaseInspectionTypeDto> findList(Map<String, Object> map);

    BaseInspectionType getMax();
}
