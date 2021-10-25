package com.fantechs.mapper;


import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductYield;
import com.fantechs.entity.BaseProductYield;
import com.fantechs.entity.ProLineBoardModel;
import com.fantechs.entity.search.SearchProLineBoard;

public interface ProLineBoardMapper {

    ProLineBoardModel findPlanList(SearchProLineBoard searchProLineBoard);

    Long findEquipMentList(SearchProLineBoard searchProLineBoard);

    Long findBarCodeRecordList(SearchProLineBoard searchProLineBoard);

    BaseProductYield findYieldList(SearchBaseProductYield searchBaseProductYield);
}
