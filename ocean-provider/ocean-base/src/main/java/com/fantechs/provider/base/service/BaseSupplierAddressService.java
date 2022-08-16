package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.BaseSupplierAddress;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/05.
 */

public interface BaseSupplierAddressService extends IService<BaseSupplierAddress> {

    List<BaseSupplierAddress> findList(Map<String, Object> map);
}
