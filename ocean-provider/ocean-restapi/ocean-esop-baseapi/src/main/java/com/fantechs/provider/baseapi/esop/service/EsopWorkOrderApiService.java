package com.fantechs.provider.baseapi.esop.service;


import com.fantechs.common.base.general.entity.basic.search.SearchBaseProLine;

public interface EsopWorkOrderApiService {

    int getWorkOrder(String proCode);

    int getAllWorkOrder(SearchBaseProLine searchBaseProLine);
}
