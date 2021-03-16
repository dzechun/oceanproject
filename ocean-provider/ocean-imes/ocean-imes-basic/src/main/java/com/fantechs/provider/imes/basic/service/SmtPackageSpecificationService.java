package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.dto.basic.SmtPackageSpecificationDto;
import com.fantechs.common.base.dto.basic.imports.SmtPackageSpecificationImport;
import com.fantechs.common.base.entity.basic.SmtPackageSpecification;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/11/04.
 */

public interface SmtPackageSpecificationService extends IService<SmtPackageSpecification> {

    List<SmtPackageSpecificationDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<SmtPackageSpecificationImport> smtPackageSpecificationImports);
}
