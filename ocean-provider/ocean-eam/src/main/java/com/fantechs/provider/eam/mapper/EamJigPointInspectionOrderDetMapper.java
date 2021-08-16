package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamJigPointInspectionOrderDetDto;
import com.fantechs.common.base.general.dto.eam.EamJigPointInspectionProjectItemDto;
import com.fantechs.common.base.general.entity.eam.EamJigPointInspectionOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamJigPointInspectionOrderDetMapper extends MyMapper<EamJigPointInspectionOrderDet> {
    List<EamJigPointInspectionOrderDetDto> findList(Map<String,Object> map);
}