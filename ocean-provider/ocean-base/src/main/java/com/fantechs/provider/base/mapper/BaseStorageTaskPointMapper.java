package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseStorageTaskPoint;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseStorageTaskPointMapper extends MyMapper<BaseStorageTaskPoint> {
    List<BaseStorageTaskPoint> findList(Map<String, Object> map);
}