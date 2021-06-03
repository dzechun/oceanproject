package com.fantechs.provider.electronic.service;

import com.fantechs.common.base.electronic.dto.PtlEquipmentDto;
import com.fantechs.common.base.electronic.entity.PtlEquipment;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/11/23.
 */

public interface PtlEquipmentService extends IService<PtlEquipment> {

    List<PtlEquipmentDto> findList(Map<String, Object> map);
}
