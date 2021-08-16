package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamJigPointInspectionOrderDto;
import com.fantechs.common.base.general.entity.eam.EamJigPointInspectionOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamJigPointInspectionOrderMapper extends MyMapper<EamJigPointInspectionOrder> {
    List<EamJigPointInspectionOrderDto> findList(Map<String,Object> map);
}