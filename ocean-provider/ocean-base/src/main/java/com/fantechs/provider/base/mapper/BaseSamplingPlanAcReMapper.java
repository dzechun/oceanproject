package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseSamplingPlanAcReDto;
import com.fantechs.common.base.general.entity.basic.BaseSamplingPlanAcRe;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseSamplingPlanAcReMapper extends MyMapper<BaseSamplingPlanAcRe> {
    List<BaseSamplingPlanAcReDto> findList(Map<String, Object> map);
}
