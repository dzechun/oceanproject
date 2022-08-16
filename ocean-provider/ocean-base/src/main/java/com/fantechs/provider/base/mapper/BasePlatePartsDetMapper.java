package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BasePlatePartsDetDto;
import com.fantechs.common.base.general.entity.basic.BasePlatePartsDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BasePlatePartsDetMapper extends MyMapper<BasePlatePartsDet> {
    List<BasePlatePartsDetDto> findList(Map<String, Object> map);
    List<BasePlatePartsDetDto> findById(Long platePartsId);
}
