package com.fantechs.provider.smt.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquipmentDto;
import com.fantechs.common.base.general.entity.smt.history.SmtHtMaterialLifetime;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmtHtMaterialLifetimeMapper extends MyMapper<SmtHtMaterialLifetime> {
    List<SmtHtMaterialLifetime> findHtList(Map<String,Object> map);
}