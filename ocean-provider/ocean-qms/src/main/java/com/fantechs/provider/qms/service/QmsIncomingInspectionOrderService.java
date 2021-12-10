package com.fantechs.provider.qms.service;


import com.fantechs.common.base.general.dto.qms.QmsIncomingInspectionOrderDto;
import com.fantechs.common.base.general.dto.qms.imports.QmsIncomingInspectionOrderImport;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrder;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrderDet;
import com.fantechs.common.base.general.entity.qms.history.QmsHtIncomingInspectionOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/06.
 */

public interface QmsIncomingInspectionOrderService extends IService<QmsIncomingInspectionOrder> {
    List<QmsIncomingInspectionOrderDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<QmsIncomingInspectionOrderImport> qmsIncomingInspectionOrderImports);

    List<QmsHtIncomingInspectionOrder> findHtList(Map<String, Object> map);

    int save(QmsIncomingInspectionOrderDto record);

    int update(QmsIncomingInspectionOrderDto entity);

    int MRBReview(Long incomingInspectionOrderId, Byte mrbResult);

    QmsIncomingInspectionOrder selectByKey(Long incomingInspectionOrderId);

    int pushDown(String ids);

    void checkInspectionResult(List<QmsIncomingInspectionOrderDet> list);

    int updateIfAllIssued(QmsIncomingInspectionOrder entity);
}
