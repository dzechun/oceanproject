package com.fantechs.provider.baseapi.esop.service;


import com.fantechs.common.base.general.dto.restapi.DTMESBOMQUERYREQ;
import com.fantechs.common.base.general.dto.restapi.DTMESBOMQUERYRES;
import com.fantechs.common.base.general.dto.restapi.SearchSapProductBomApi;
import com.fantechs.common.base.general.dto.restapi.SearchSapSupplierApi;


public interface SapProductBomApiService {

    int getProductBom(SearchSapProductBomApi searchSapProductBomApi);
}
