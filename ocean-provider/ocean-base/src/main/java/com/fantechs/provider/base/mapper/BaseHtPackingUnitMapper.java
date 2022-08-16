package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtPackingUnit;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseHtPackingUnitMapper extends MyMapper<BaseHtPackingUnit> {
    List<BaseHtPackingUnit> findHtList(Map<String, Object> map);
}