package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BasePackageSpecificationDto;
import com.fantechs.common.base.general.dto.basic.imports.BasePackageSpecificationImport;
import com.fantechs.common.base.general.entity.basic.BasePackageSpecification;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/11/04.
 */

public interface BasePackageSpecificationService extends IService<BasePackageSpecification> {

    List<BasePackageSpecificationDto> findList(Map<String, Object> map);

    List<BasePackageSpecificationDto> findByMaterialProcess(Map<String, Object> map);
    List<BasePackageSpecificationDto> findByMaterialProcessNotDet(Map<String, Object> map);

    Map<String, Object> importExcel(List<BasePackageSpecificationImport> basePackageSpecificationImports);
}
