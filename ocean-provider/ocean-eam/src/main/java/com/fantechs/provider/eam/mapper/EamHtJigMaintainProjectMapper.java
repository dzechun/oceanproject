package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.entity.eam.history.EamHtJig;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigMaintainProject;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamHtJigMaintainProjectMapper extends MyMapper<EamHtJigMaintainProject> {
    List<EamHtJigMaintainProject> findHtList(Map<String,Object> map);
}