package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtUnitPrice;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseHtUnitPriceMapper extends MyMapper<BaseHtUnitPrice> {

    List<BaseHtUnitPrice> findHtList(Map<String, Object> map);
}