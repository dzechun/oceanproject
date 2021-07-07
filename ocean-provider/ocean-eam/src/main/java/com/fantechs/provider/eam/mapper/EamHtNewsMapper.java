package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.entity.eam.history.EamHtNews;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamHtNewsMapper extends MyMapper<EamHtNews> {
    List<EamHtNews> findHtList(Map<String,Object> map);
}