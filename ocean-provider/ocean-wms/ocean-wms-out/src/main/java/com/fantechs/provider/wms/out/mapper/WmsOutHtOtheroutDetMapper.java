package com.fantechs.provider.wms.out.mapper;

import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtOtheroutDet;
import com.fantechs.common.base.mybatis.MyMapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsOutHtOtheroutDetMapper extends MyMapper<WmsOutHtOtheroutDet> {
    List<WmsOutHtOtheroutDet> findHTList(Map<String, Object> map);
}