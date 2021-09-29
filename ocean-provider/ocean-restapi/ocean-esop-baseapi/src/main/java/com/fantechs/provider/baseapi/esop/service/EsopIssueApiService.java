package com.fantechs.provider.baseapi.esop.service;


public interface EsopIssueApiService {

    int getIssue(String materialCode,Long orgId);

    int getAllIssue();
}
