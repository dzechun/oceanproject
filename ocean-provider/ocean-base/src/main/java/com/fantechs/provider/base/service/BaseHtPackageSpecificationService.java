package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtPackageSpecification;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/11/04.
 */

public interface BaseHtPackageSpecificationService extends IService<BaseHtPackageSpecification> {

    List<BaseHtPackageSpecification> findHtList(Map<String, Object> map);
}
