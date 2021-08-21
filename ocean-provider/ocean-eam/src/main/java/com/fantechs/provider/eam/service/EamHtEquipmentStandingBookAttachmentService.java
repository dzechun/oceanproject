package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamEquipmentStandingBookAttachmentDto;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentStandingBookAttachment;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/08/20.
 */

public interface EamHtEquipmentStandingBookAttachmentService extends IService<EamHtEquipmentStandingBookAttachment> {
    List<EamEquipmentStandingBookAttachmentDto> findHtList(Map<String, Object> map);
}
