package com.fantechs.mapper;


import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductYield;
import com.fantechs.entity.*;
import com.fantechs.entity.search.SearchProLineBoard;

import java.util.List;
import java.util.Map;

public interface ProLineBoardMapper {

    ProLineBoardModel findPlanList(SearchProLineBoard searchProLineBoard);

    Long findEquipMentList(SearchProLineBoard searchProLineBoard);

    Long findBarCodeRecordList(SearchProLineBoard searchProLineBoard);

    BaseProductYield findYieldList(SearchBaseProductYield searchBaseProductYield);

    ProductLineLeft findLastBarCideRecirdList(SearchProLineBoard searchProLineBoard);

    ProductDailyPlanModel findNextPlan(SearchProLineBoard searchProLineBoard);

    List<ProductLineRight> findProductLineRight(Map<String, Object> workOrderId);
}
