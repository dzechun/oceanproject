package com.fantechs.provider.qms.service;

import com.fantechs.common.base.general.entity.qms.QmsInspectionOrderDetSample;
import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrderDetSample;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/04.
 */

public interface QmsIpqcInspectionOrderDetSampleService extends IService<QmsIpqcInspectionOrderDetSample> {
    List<QmsIpqcInspectionOrderDetSample> findList(Map<String, Object> map);
    int batchAdd(List<QmsIpqcInspectionOrderDetSample> qmsIpqcInspectionOrderDetSampleList);
    Boolean checkBarcode(String barcode, Long qmsIpqcInspectionOrderDetId);
    int barcodeBatchAdd(List<QmsIpqcInspectionOrderDetSample> qmsIpqcInspectionOrderDetSampleList);
}
