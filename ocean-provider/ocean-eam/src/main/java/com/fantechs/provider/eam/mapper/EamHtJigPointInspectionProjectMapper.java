package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.entity.eam.history.EamHtJigPointInspectionProject;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamHtJigPointInspectionProjectMapper extends MyMapper<EamHtJigPointInspectionProject> {
    List<EamHtJigPointInspectionProject> findHtList(Map<String,Object> map);
}