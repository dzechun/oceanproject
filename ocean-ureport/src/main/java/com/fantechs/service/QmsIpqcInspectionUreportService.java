package com.fantechs.service;


import com.fantechs.common.base.general.entity.qms.search.SearchQmsIpqcInspectionOrder;
import com.fantechs.entity.QmsIpqcDtaticElectricityModel;
import com.fantechs.entity.QmsIpqcFirstArticleModel;
import com.fantechs.entity.QmsIpqcProcessInspectionModel;
import com.fantechs.entity.QmsIpqcSamplingModel;

import java.util.List;

public interface QmsIpqcInspectionUreportService {

    List<QmsIpqcDtaticElectricityModel> findDtaticElectricityList(SearchQmsIpqcInspectionOrder searchQmsIpqcInspectionOrder);

    List<QmsIpqcSamplingModel> findSamplingList(SearchQmsIpqcInspectionOrder searchQmsIpqcInspectionOrder);

    List<QmsIpqcFirstArticleModel> findFirstArticleList(SearchQmsIpqcInspectionOrder searchQmsIpqcInspectionOrder);

    List<QmsIpqcProcessInspectionModel> findProcessInspectionList(SearchQmsIpqcInspectionOrder searchQmsIpqcInspectionOrder);

}
