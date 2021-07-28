package com.fantechs.provider.materialapi.imes.service;


import com.fantechs.common.base.general.dto.restapi.SearchSapReportWorkApi;

import java.text.ParseException;

public interface SapReportWorkApiService {

    int sendReportWork(SearchSapReportWorkApi searchSapReportWorkApi) throws ParseException;

}
