package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderMaterialRePDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderMaterialReP;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesPmWorkOrderMaterialRePMapper extends MyMapper<MesPmWorkOrderMaterialReP> {
    List<MesPmWorkOrderMaterialRePDto> findList(Map<String, Object> map);
}
