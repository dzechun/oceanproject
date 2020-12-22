package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.dto.qms.QmsQualityInspectionDto;
import com.fantechs.common.base.general.entity.qms.QmsQualityInspection;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsQualityInspectionMapper extends MyMapper<QmsQualityInspection> {

    List<QmsQualityInspectionDto> findList(Map<String, Object> map);

    QmsQualityInspection getMax();

    int deleteDetail(String ids);
}
