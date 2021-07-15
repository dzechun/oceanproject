package com.fantechs.provider.ureport.service;

import com.fantechs.common.base.general.entity.tem.TemVehicle;
import com.fantechs.common.base.general.entity.ureport.MesSfcBarcodeProcessReport;
import com.fantechs.common.base.support.IService;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


public interface MesSfcBarcodeProcessReportService extends IService<MesSfcBarcodeProcessReport> {
    List<MesSfcBarcodeProcessReport> findList(Map<String, Object> map);

    MesSfcBarcodeProcessReport findRecordList(Map<String, Object> map);
}
