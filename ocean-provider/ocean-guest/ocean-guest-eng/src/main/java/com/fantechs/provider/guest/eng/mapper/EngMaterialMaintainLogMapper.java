package com.fantechs.provider.guest.eng.mapper;

import com.fantechs.common.base.general.dto.eng.EngMaterialMaintainLogDto;
import com.fantechs.common.base.general.entity.eng.EngMaterialMaintainLog;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EngMaterialMaintainLogMapper extends MyMapper<EngMaterialMaintainLog> {
    List<EngMaterialMaintainLogDto> findList(Map<String, Object> map);
}
