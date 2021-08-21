package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquPointInspectionProjectItemDto;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquPointInspectionProjectItem;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamHtEquPointInspectionProjectItemMapper extends MyMapper<EamHtEquPointInspectionProjectItem> {
    List<EamEquPointInspectionProjectItemDto> findList(Map<String, Object> map);
}