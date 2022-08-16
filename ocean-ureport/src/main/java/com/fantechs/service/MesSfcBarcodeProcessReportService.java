package com.fantechs.service;

import com.fantechs.common.base.general.entity.ureport.MesSfcBarcodeProcessReport;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;


public interface MesSfcBarcodeProcessReportService extends IService<MesSfcBarcodeProcessReport> {
    List<MesSfcBarcodeProcessReport> findList(Map<String, Object> map);

    MesSfcBarcodeProcessReport findRecordList(Map<String, Object> map);
}
