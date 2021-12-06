package com.fantechs.provider.qms.service;

import com.fantechs.common.base.general.dto.qms.QmsIncomingInspectionOrderDetDto;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrderDet;
import com.fantechs.common.base.general.entity.qms.history.QmsHtIncomingInspectionOrderDet;
import com.fantechs.common.base.support.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/06.
 */

public interface QmsIncomingInspectionOrderDetService extends IService<QmsIncomingInspectionOrderDet> {
    List<QmsIncomingInspectionOrderDetDto> findList(Map<String, Object> map);

    List<QmsHtIncomingInspectionOrderDet> findHtList(Map<String, Object> map);

    List<QmsIncomingInspectionOrderDetDto> showOrderDet(Long inspectionStandardId, BigDecimal qty);
}
