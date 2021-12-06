package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.dto.qms.QmsIncomingInspectionOrderDetSampleDto;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrderDetSample;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsIncomingInspectionOrderDetSampleMapper extends MyMapper<QmsIncomingInspectionOrderDetSample> {
    List<QmsIncomingInspectionOrderDetSampleDto> findList(Map<String, Object> map);
}