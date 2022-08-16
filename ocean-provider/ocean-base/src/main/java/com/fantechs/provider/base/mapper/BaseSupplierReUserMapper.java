package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseSupplierReUser;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseSupplierReUserMapper extends MyMapper<BaseSupplierReUser> {
    List<BaseSupplierReUser> findList(Map<String, Object> map);
}