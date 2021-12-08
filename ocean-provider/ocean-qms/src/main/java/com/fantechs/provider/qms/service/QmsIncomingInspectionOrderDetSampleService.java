package com.fantechs.provider.qms.service;

import com.fantechs.common.base.general.dto.qms.PdaIncomingCheckBarcodeDto;
import com.fantechs.common.base.general.dto.qms.PdaIncomingSampleSubmitDto;
import com.fantechs.common.base.general.dto.qms.QmsIncomingInspectionOrderDetSampleDto;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrderDetSample;
import com.fantechs.common.base.general.entity.qms.history.QmsHtIncomingInspectionOrderDetSample;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/06.
 */

public interface QmsIncomingInspectionOrderDetSampleService extends IService<QmsIncomingInspectionOrderDetSample> {
    List<QmsIncomingInspectionOrderDetSampleDto> findList(Map<String, Object> map);

    List<QmsHtIncomingInspectionOrderDetSample> findHtList(Map<String, Object> map);

    String checkBarcode(PdaIncomingCheckBarcodeDto pdaIncomingCheckBarcodeDto);

    int sampleSubmit(List<PdaIncomingSampleSubmitDto> list);

    int batchAdd(List<QmsIncomingInspectionOrderDetSample> qmsIncomingInspectionOrderDetSampleList);
}
