package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseUnitPriceDto;
import com.fantechs.common.base.general.entity.basic.BaseUnitPrice;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseUnitPriceMapper extends MyMapper<BaseUnitPrice> {

    List<BaseUnitPriceDto> findList(Map<String, Object> map);
}