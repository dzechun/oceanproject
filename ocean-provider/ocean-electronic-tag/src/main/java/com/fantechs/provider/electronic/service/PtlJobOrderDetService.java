package com.fantechs.provider.electronic.service;

import com.fantechs.common.base.electronic.dto.PtlJobOrderDetDto;
import com.fantechs.common.base.electronic.entity.PtlJobOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/06/01.
 */

public interface PtlJobOrderDetService extends IService<PtlJobOrderDet> {
    List<PtlJobOrderDetDto> findList(Map<String, Object> map);

    int batchUpdate(List<PtlJobOrderDet> ptlJobOrderDetList);

    int updateByJobOrderId(PtlJobOrderDet ptlJobOrderDet);
}
