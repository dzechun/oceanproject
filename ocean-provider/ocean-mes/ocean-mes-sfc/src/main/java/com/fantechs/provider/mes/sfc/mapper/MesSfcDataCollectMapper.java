package com.fantechs.provider.mes.sfc.mapper;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcDataCollectDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcDataCollect;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesSfcDataCollectMapper extends MyMapper<MesSfcDataCollect> {
    List<MesSfcDataCollectDto> findList(Map<String, Object> map);

    List<MesSfcDataCollectDto> findByGroup(@Param(value="equipmentId") Long equipmentId);
}