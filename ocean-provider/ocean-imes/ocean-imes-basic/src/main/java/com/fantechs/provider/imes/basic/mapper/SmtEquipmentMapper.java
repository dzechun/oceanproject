package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.dto.basic.SmtEquipmentDto;
import com.fantechs.common.base.entity.basic.SmtEquipment;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmtEquipmentMapper extends MyMapper<SmtEquipment> {

    List<SmtEquipmentDto> findList(Map<String, Object> map);
}