package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.dto.qms.QmsPdaInspectionDto;
import com.fantechs.common.base.general.entity.qms.QmsPdaInspection;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsPdaInspectionMapper extends MyMapper<QmsPdaInspection> {

    List<QmsPdaInspectionDto> findList(Map<String, Object> map);
}
