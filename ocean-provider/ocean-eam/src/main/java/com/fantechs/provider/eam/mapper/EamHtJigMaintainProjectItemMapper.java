package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.entity.eam.history.EamHtJigMaintainProject;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigMaintainProjectItem;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamHtJigMaintainProjectItemMapper extends MyMapper<EamHtJigMaintainProjectItem> {
    List<EamHtJigMaintainProjectItem> findHtList(Map<String,Object> map);
}