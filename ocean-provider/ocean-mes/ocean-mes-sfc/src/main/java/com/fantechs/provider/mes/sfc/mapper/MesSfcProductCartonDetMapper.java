package com.fantechs.provider.mes.sfc.mapper;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductCartonDetDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcProductCartonDet;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductCartonDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesSfcProductCartonDetMapper extends MyMapper<MesSfcProductCartonDet> {

    List<MesSfcProductCartonDetDto> findList(Map<String, Object> map);
}