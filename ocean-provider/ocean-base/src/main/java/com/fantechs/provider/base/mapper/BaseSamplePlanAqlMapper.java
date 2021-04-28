package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseSamplePlanAqlDto;
import com.fantechs.common.base.general.entity.basic.BaseSamplePlanAql;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseSamplePlanAqlMapper extends MyMapper<BaseSamplePlanAql> {
    List<BaseSamplePlanAqlDto> findList(Map<String, Object> map);
}
