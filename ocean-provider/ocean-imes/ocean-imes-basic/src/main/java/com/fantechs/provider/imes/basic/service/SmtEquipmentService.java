package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.dto.basic.SmtEquipmentDto;
import com.fantechs.common.base.entity.basic.SmtEquipment;
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
