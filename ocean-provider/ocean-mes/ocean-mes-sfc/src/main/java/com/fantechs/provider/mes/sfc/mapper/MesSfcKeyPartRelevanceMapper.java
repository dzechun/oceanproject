package com.fantechs.provider.mes.sfc.mapper;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcKeyPartRelevanceDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcKeyPartRelevance;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesSfcKeyPartRelevanceMapper extends MyMapper<MesSfcKeyPartRelevance> {

    List<MesSfcKeyPartRelevanceDto> findList(Map<String, Object> map);

    List<MesSfcKeyPartRelevanceDto> findListByPallet(Map<String, Object> map);

    List<MesSfcKeyPartRelevanceDto> findListForGroup(Map<String, Object> map);
}