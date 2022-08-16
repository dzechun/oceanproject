package com.fantechs.provider.mes.sfc.mapper;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductCartonDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcProductCarton;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductCarton;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesSfcProductCartonMapper extends MyMapper<MesSfcProductCarton> {

    List<MesSfcProductCartonDto> findList(Map<String, Object> map);
}