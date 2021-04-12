package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BasePackingUnitDto;
import com.fantechs.common.base.general.entity.basic.BasePackingUnit;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BasePackingUnitMapper extends MyMapper<BasePackingUnit> {

    List<BasePackingUnitDto> findList(Map<String,Object> map);
}