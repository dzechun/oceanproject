package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.entity.basic.SmtSupplierAddress;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/05.
 */

public interface SmtSupplierAddressService extends IService<SmtSupplierAddress> {

    List<SmtSupplierAddress> findList(Map<String, Object> map);
}
