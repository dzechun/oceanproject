package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.entity.basic.history.SmtHtPackageSpecification;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/11/04.
 */

public interface SmtHtPackageSpecificationService extends IService<SmtHtPackageSpecification> {

    List<SmtHtPackageSpecification> findHtList(Map<String, Object> map);
}
