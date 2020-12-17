package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.entity.qms.QmsQualityInspectionDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsQualityInspectionDetMapper extends MyMapper<QmsQualityInspectionDet> {
    List<QmsQualityInspectionDet> findList(Map<String, Object> map);
}
