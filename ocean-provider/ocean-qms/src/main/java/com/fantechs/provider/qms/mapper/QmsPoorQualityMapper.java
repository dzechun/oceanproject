package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.dto.qms.QmsPoorQualityDto;
import com.fantechs.common.base.general.entity.qms.QmsPoorQuality;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QmsPoorQualityMapper extends MyMapper<QmsPoorQuality> {
    List<QmsPoorQualityDto> findById(Long qualityId);
}