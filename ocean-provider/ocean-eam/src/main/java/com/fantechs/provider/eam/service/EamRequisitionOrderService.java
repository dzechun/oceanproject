package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamRequisitionOrderDto;
import com.fantechs.common.base.general.entity.eam.EamRequisitionOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/29.
 */

public interface EamRequisitionOrderService extends IService<EamRequisitionOrder> {
    List<EamRequisitionOrderDto> findList(Map<String, Object> map);
}
