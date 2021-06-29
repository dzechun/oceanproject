package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamRequisitionOrderDetDto;
import com.fantechs.common.base.general.entity.eam.EamRequisitionOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/29.
 */

public interface EamRequisitionOrderDetService extends IService<EamRequisitionOrderDet> {
    List<EamRequisitionOrderDetDto> findList(Map<String, Object> map);
}
