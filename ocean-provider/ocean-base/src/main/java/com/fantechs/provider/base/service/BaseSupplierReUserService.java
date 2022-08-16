package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.BaseSupplierReUser;
import com.fantechs.common.base.general.entity.basic.history.BaseHtSupplierReUser;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/27.
 */

public interface BaseSupplierReUserService extends IService<BaseSupplierReUser> {
    List<BaseSupplierReUser> findList(Map<String, Object> map);

    List<BaseHtSupplierReUser> findHtList(Map<String, Object> map);

    int addUser(Long supplierId, List<Long> userIds);

    int saveByApi (BaseSupplierReUser baseSupplierReUser);
}


