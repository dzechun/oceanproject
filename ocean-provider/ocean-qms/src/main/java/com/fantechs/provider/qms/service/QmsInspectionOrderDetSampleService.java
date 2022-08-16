package com.fantechs.provider.qms.service;

import com.fantechs.common.base.general.entity.qms.QmsInspectionOrderDetSample;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/25.
 */

public interface QmsInspectionOrderDetSampleService extends IService<QmsInspectionOrderDetSample> {
    List<QmsInspectionOrderDetSample> findList(Map<String, Object> map);
    int batchAdd(List<QmsInspectionOrderDetSample> qmsInspectionOrderDetSampleList);
    Boolean checkBarcode(String barcode,Long qmsInspectionOrderDetId);
    List<QmsInspectionOrderDetSample> findBarcodes(Long inspectionOrderId);
    QmsInspectionOrderDetSample checkAndSaveBarcode(QmsInspectionOrderDetSample qmsInspectionOrderDetSample);
}
