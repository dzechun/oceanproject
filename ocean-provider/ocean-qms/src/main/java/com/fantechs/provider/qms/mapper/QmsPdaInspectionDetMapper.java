package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.dto.qms.QmsPdaInspectionDetDto;
import com.fantechs.common.base.general.entity.qms.QmsPdaInspectionDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsPdaInspectionDetMapper extends MyMapper<QmsPdaInspectionDet> {
    List<QmsPdaInspectionDetDto> findList(Map<String, Object> map);
}
