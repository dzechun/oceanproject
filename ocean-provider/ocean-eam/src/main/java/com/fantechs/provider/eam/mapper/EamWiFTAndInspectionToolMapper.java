package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.entity.eam.EamWiFTAndInspectionTool;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamWiFTAndInspectionToolMapper extends MyMapper<EamWiFTAndInspectionTool> {
    List<EamWiFTAndInspectionTool> findList(Map<String,Object> map);
}