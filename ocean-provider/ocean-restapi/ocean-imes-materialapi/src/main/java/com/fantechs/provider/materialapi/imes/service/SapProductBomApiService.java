package com.fantechs.provider.materialapi.imes.service;


import com.fantechs.common.base.general.dto.restapi.SearchSapProductBomApi;

import java.text.ParseException;


public interface SapProductBomApiService {

    int getProductBom(SearchSapProductBomApi searchSapProductBomApi) throws ParseException;

}
