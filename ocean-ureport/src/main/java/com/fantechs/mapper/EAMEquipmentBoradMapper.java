package com.fantechs.mapper;

import com.fantechs.common.base.general.entity.eam.EamEquipment;
import com.fantechs.entity.search.SearchProLineBoard;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EAMEquipmentBoradMapper {
    List<EamEquipment> findEquipmentByLine(SearchProLineBoard searchProLineBoard);
}
