package com.fantechs.provider.qms.service;

import com.fantechs.common.base.general.dto.qms.QmsIpqcInspectionOrderDetDto;
import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrderDet;
import com.fantechs.common.base.support.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/04.
 */

public interface QmsIpqcInspectionOrderDetService extends IService<QmsIpqcInspectionOrderDet> {
    List<QmsIpqcInspectionOrderDet> findList(Map<String, Object> map);

    List<QmsIpqcInspectionOrderDetDto> showOrderDet(Long inspectionStandardId, BigDecimal qty);
}
