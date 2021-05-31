package com.fantechs.provider.electronic.mapper;

import com.fantechs.common.base.electronic.dto.PtlEquipmentDto;
import com.fantechs.common.base.electronic.entity.PtlEquipment;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PtlEquipmentMapper extends MyMapper<PtlEquipment> {

    List<PtlEquipmentDto> findList(Map<String, Object> map);
}