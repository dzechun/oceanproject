package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseInspectionItemDetDto;
import com.fantechs.common.base.general.entity.basic.BaseInspectionItemDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseInspectionItemDetMapper extends MyMapper<BaseInspectionItemDet> {
    List<BaseInspectionItemDetDto> findList(Map<String, Object> map);
}
