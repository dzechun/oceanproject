package com.fantechs.provider.esop.service;

import com.fantechs.common.base.general.dto.esop.EsopEquipmentDto;
import com.fantechs.common.base.general.dto.esop.imports.EsopEquipmentImport;
import com.fantechs.common.base.general.entity.esop.EsopEquipment;
import com.fantechs.common.base.general.entity.esop.history.EsopHtEquipment;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/25.
 */

public interface EsopEquipmentService extends IService<EsopEquipment> {
    List<EsopEquipmentDto> findList(Map<String, Object> map);
    List<EsopHtEquipment> findHtList(Map<String, Object> map);

    int batchUpdate(List<EsopEquipment> list);

    EsopEquipment detailByIp(String ip);

    List<EsopEquipmentDto> findNoGroup(Map<String, Object> map);

    Map<String, Object> importExcel(List<EsopEquipmentImport> equipmentImports);
}
