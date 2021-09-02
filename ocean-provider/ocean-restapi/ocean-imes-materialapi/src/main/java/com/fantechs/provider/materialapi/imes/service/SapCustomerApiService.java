package com.fantechs.provider.materialapi.imes.service;


import com.fantechs.common.base.general.dto.restapi.SearchSapSupplierApi;

import java.text.ParseException;


public interface SapCustomerApiService {

    int getCustomer(SearchSapSupplierApi searchSapSupplierApi) throws ParseException;

    int getCustomers();
}
