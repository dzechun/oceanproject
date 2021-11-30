package com.fantechs.provider.srm.service;

import com.fantechs.common.base.general.entity.srm.history.SrmInHtAsnOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/29.
 */

public interface SrmInHtAsnOrderDetService extends IService<SrmInHtAsnOrderDet> {
    List<SrmInHtAsnOrderDet> findList(Map<String, Object> map);

}
