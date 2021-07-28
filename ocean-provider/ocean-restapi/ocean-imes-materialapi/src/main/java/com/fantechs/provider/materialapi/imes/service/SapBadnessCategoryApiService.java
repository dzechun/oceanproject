package com.fantechs.provider.materialapi.imes.service;


import com.fantechs.common.base.general.dto.restapi.SearchSapBadnessCategoryApi;

import java.text.ParseException;

public interface SapBadnessCategoryApiService {

    int getbadnessCategory(SearchSapBadnessCategoryApi searchSapBadnessCategoryApi) throws ParseException;

}
