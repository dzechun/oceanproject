package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.entity.eam.history.EamHtRequisitionOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/29.
 */

public interface EamHtRequisitionOrderDetService extends IService<EamHtRequisitionOrderDet> {
    List<EamHtRequisitionOrderDet> findHtList(Map<String, Object> map);
}
