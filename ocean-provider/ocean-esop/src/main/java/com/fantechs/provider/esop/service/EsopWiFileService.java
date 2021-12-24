package com.fantechs.provider.esop.service;

import com.fantechs.common.base.general.entity.esop.EsopWiFile;
import com.fantechs.common.base.support.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/06.
 */

public interface EsopWiFileService extends IService<EsopWiFile> {
    String uploadFile(MultipartFile file);

    int batchAdd(List<EsopWiFile> esopWiFiles);

    int batchUpdate(List<EsopWiFile> esopWiFiles);

    List<EsopWiFile> findList(Map<String, Object> map);

    int batchUploadFile(MultipartFile file);
}
