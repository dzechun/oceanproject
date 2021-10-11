package com.fantechs.mapper;


import com.fantechs.entity.ProLineBoardModel;
import com.fantechs.entity.search.SearchProLineBoard;

public interface ProLineBoardMapper {

    ProLineBoardModel findPlanList(SearchProLineBoard searchProLineBoard);

    Long findEquipMentList(SearchProLineBoard searchProLineBoard);

    Long findBarCodeRecordList(SearchProLineBoard searchProLineBoard);
}
