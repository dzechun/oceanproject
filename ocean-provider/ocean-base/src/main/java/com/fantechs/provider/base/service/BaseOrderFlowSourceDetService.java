package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.BaseOrderFlowSourceDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2022/02/15.
 */

public interface BaseOrderFlowSourceDetService extends IService<BaseOrderFlowSourceDet> {
    List<BaseOrderFlowSourceDet> findList(Map<String, Object> map);
}
