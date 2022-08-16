package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtStaff;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/01/16.
 */

public interface BaseHtStaffService extends IService<BaseHtStaff> {
    List<BaseHtStaff> findHtList(Map<String, Object> map);
}
