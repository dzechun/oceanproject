package com.fantechs.provider.materialapi.imes.service;


import com.fantechs.common.base.general.dto.restapi.SearchSapMaterialApi;

import java.text.ParseException;

public interface SapMaterialApiService {

    int getMaterial(SearchSapMaterialApi searchSapMaterialApi) throws ParseException;

}
