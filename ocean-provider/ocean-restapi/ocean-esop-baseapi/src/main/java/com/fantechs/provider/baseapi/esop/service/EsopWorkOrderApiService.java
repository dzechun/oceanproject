package com.fantechs.provider.baseapi.esop.service;


import com.fantechs.common.base.general.entity.basic.search.SearchBaseProLine;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;

public interface EsopWorkOrderApiService {

    MesPmWorkOrder getWorkOrder(String proCode);

    int getAllWorkOrder(SearchBaseProLine searchBaseProLine);
}
