package com.fantechs.provider.materialapi.imes.service;


import com.fantechs.common.base.general.dto.restapi.DTMESBOMQUERYREQ;
import com.fantechs.common.base.general.dto.restapi.DTMESBOMQUERYRES;
import com.fantechs.common.base.general.dto.restapi.SearchSapSupplierApi;


public interface SapProductBomApiService {

    int getProductBom(DTMESBOMQUERYREQ dTMESBOMQUERYREQ);
}
