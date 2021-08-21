package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamEquipmentStandingBookAttachmentDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentStandingBookAttachment;
import com.fantechs.common.base.support.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/08/20.
 */

public interface EamEquipmentStandingBookAttachmentService extends IService<EamEquipmentStandingBookAttachment> {
    String uploadFile(MultipartFile file);
}
