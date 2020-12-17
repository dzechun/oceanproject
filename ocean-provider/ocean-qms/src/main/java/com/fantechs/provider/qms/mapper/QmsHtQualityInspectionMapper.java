package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.dto.qms.QmsQualityInspectionDto;
import com.fantechs.common.base.general.entity.qms.history.QmsHtQualityInspection;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsHtQualityInspectionMapper extends MyMapper<QmsHtQualityInspection> {
    List<QmsHtQualityInspection> findHtList(Map<String, Object> map);
}
