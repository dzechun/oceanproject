package com.fantechs.provider.smt.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquipmentDto;
import com.fantechs.common.base.general.dto.smt.SmtMaterialLifetimeDto;
import com.fantechs.common.base.general.entity.smt.SmtMaterialLifetime;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmtMaterialLifetimeMapper extends MyMapper<SmtMaterialLifetime> {
    List<SmtMaterialLifetimeDto> findList(Map<String,Object> map);
}