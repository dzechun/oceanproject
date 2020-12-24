package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.dto.qms.QmsInspectionTypeDto;
import com.fantechs.common.base.general.entity.qms.QmsInspectionType;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsInspectionTypeMapper extends MyMapper<QmsInspectionType> {

    List<QmsInspectionTypeDto> findList(Map<String, Object> map);

    QmsInspectionType getMax();
}
