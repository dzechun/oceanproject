package com.fantechs.provider.electronic.service;

import com.fantechs.common.base.electronic.dto.SmtEquipmentDto;
import com.fantechs.common.base.electronic.entity.SmtEquipment;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/11/23.
 */

public interface SmtEquipmentService extends IService<SmtEquipment> {

    List<SmtEquipmentDto> findList(Map<String, Object> map);
}
