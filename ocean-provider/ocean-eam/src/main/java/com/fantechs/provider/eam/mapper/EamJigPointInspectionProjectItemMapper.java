package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamJigPointInspectionProjectItemDto;
import com.fantechs.common.base.general.entity.eam.EamJigPointInspectionProjectItem;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamJigPointInspectionProjectItemMapper extends MyMapper<EamJigPointInspectionProjectItem> {
    List<EamJigPointInspectionProjectItemDto> findList(Map<String,Object> map);
}