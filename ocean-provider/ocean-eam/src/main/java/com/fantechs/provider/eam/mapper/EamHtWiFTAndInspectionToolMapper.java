package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.entity.eam.history.EamHtWiFTAndInspectionTool;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamHtWiFTAndInspectionToolMapper extends MyMapper<EamHtWiFTAndInspectionTool> {
    List<EamHtWiFTAndInspectionTool> findHtList(Map<String,Object> map);
}