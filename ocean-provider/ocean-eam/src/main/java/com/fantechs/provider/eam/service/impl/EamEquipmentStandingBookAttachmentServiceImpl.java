package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.general.dto.eam.EamEquipmentStandingBookAttachmentDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentStandingBookAttachment;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.api.fileserver.service.FileFeignApi;
import com.fantechs.provider.eam.mapper.EamEquipmentStandingBookAttachmentMapper;
import com.fantechs.provider.eam.service.EamEquipmentStandingBookAttachmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/20.
 */
@Service
public class EamEquipmentStandingBookAttachmentServiceImpl extends BaseService<EamEquipmentStandingBookAttachment> implements EamEquipmentStandingBookAttachmentService {

    @Resource
    private FileFeignApi fileFeignApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadFile(MultipartFile file){
        Map<String, Object> data = (Map<String, Object>)fileFeignApi.fileUpload(file).getData();
        String path = data.get("url").toString();
        return path;
    }
}
