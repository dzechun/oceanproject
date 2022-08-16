package com.fantechs.service;

import com.fantechs.entity.ProductionLine;
import com.fantechs.entity.search.SearchProductDailyPlan;

public interface ProductionLineService {
    ProductionLine findList(SearchProductDailyPlan searchProductDailyPlan);
}
