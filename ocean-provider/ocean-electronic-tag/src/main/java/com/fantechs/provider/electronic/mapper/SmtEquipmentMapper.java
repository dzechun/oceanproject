package com.fantechs.provider.electronic.mapper;

import com.fantechs.common.base.electronic.dto.SmtEquipmentDto;
import com.fantechs.common.base.electronic.entity.SmtEquipment;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmtEquipmentMapper extends MyMapper<SmtEquipment> {

    List<SmtEquipmentDto> findList(Map<String, Object> map);
}