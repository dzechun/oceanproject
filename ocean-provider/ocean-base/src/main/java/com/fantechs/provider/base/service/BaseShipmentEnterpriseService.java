package com.fantechs.provider.base.service;


import com.fantechs.common.base.general.dto.basic.BaseShipmentEnterpriseDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseShipmentEnterpriseImport;
import com.fantechs.common.base.general.entity.basic.BaseShipmentEnterprise;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/16.
 */

public interface BaseShipmentEnterpriseService extends IService<BaseShipmentEnterprise> {

    List<BaseShipmentEnterpriseDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<BaseShipmentEnterpriseImport> baseShipmentEnterpriseImports);
}
