package com.fantechs.provider.baseapi.esop.service;


import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;

public interface EsopIssueApiService {

    int getIssue(String materialCode);

    int getAllIssue();
}
