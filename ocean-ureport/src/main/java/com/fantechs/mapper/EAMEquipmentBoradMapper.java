package com.fantechs.mapper;

import com.fantechs.entity.EAMEquipmentBorad;
import com.fantechs.entity.search.SearchProLineBoard;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EAMEquipmentBoradMapper {

    List<EAMEquipmentBorad> findEquipment(SearchProLineBoard searchProLineBoard);

    int findEquipmentInfo(SearchProLineBoard searchProLineBoard);
}
