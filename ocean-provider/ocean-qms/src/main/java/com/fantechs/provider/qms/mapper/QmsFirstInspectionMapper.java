package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.dto.qms.QmsFirstInspectionDto;
import com.fantechs.common.base.general.entity.qms.QmsFirstInspection;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsFirstInspectionMapper extends MyMapper<QmsFirstInspection> {
    List<QmsFirstInspectionDto> findList(Map<String, Object> map);
}
