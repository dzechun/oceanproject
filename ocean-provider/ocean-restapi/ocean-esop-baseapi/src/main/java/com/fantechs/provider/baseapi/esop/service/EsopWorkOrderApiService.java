package com.fantechs.provider.baseapi.esop.service;


import com.fantechs.common.base.general.dto.restapi.SearchSapMaterialApi;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;

import java.text.ParseException;

public interface EsopWorkOrderApiService {

    int getWorkOrder(String proLineId);

}
