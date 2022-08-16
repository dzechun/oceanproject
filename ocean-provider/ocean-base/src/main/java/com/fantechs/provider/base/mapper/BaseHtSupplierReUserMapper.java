package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtSupplierReUser;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseHtSupplierReUserMapper extends MyMapper<BaseHtSupplierReUser> {
    List<BaseHtSupplierReUser> findHtList(Map<String, Object> map);
}