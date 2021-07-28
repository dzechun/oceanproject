package com.fantechs.provider.materialapi.imes.service;


import com.fantechs.common.base.general.dto.restapi.SearchSapRouteApi;

import java.text.ParseException;

public interface SapRouteApiService {

    int getRoute(SearchSapRouteApi searchSapRouteApi) throws ParseException;

}
