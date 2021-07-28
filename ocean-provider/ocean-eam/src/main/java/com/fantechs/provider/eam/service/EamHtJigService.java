package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.entity.eam.history.EamHtJig;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/28.
 */

public interface EamHtJigService extends IService<EamHtJig> {
    List<EamHtJig> findHtList(Map<String, Object> map);
}
