package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamJigMaintainProjectItemDto;
import com.fantechs.common.base.general.entity.eam.EamJigMaintainProjectItem;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamJigMaintainProjectItemMapper extends MyMapper<EamJigMaintainProjectItem> {
    List<EamJigMaintainProjectItemDto> findList(Map<String,Object> map);
}