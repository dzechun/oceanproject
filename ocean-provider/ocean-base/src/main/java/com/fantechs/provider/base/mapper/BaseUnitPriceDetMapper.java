package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseUnitPriceDetDto;
import com.fantechs.common.base.general.entity.basic.BaseUnitPriceDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseUnitPriceDetMapper extends MyMapper<BaseUnitPriceDet> {

    List<BaseUnitPriceDetDto> findList(Map<String, Object> map);
}