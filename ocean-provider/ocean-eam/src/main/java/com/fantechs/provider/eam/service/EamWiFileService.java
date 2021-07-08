package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.entity.eam.EamWiFile;
import com.fantechs.common.base.support.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * Created by leifengzhi on 2021/07/06.
 */

public interface EamWiFileService extends IService<EamWiFile> {
    String uploadFile(MultipartFile file);
}
