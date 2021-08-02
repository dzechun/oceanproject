package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.entity.eam.history.EamHtJigReturn;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/30.
 */

public interface EamHtJigReturnService extends IService<EamHtJigReturn> {
    List<EamHtJigReturn> findHtList(Map<String, Object> map);
}
