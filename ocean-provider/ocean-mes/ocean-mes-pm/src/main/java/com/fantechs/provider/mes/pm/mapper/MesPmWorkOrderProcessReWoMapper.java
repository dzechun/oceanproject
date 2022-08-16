package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderProcessReWoDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderProcessReWo;
import com.fantechs.common.base.mybatis.MyMapper;
import com.fantechs.provider.mes.pm.vo.MesPmWorkOrderProcessReWoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesPmWorkOrderProcessReWoMapper extends MyMapper<MesPmWorkOrderProcessReWo> {
    List<MesPmWorkOrderProcessReWoDto> findList(Map<String, Object> map);
}
