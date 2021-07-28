package com.fantechs.provider.materialapi.imes.service;


import com.fantechs.common.base.general.dto.restapi.DTMESBOMQUERYREQ;
import com.fantechs.common.base.general.dto.restapi.DTMESBOMQUERYRES;
import com.fantechs.common.base.general.dto.restapi.SearchSapProductBomApi;
import com.fantechs.common.base.general.dto.restapi.SearchSapSupplierApi;

import java.text.ParseException;


public interface SapProductBomApiService {

    int getProductBom(SearchSapProductBomApi searchSapProductBomApi) throws ParseException;
}
