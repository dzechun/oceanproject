package com.fantechs.provider.esop.service.impl;

import com.fantechs.common.base.general.entity.esop.EsopWiFile;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.api.fileserver.service.FileFeignApi;
import com.fantechs.provider.esop.service.EsopWiFileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/06.
 */
@Service
public class EsopWiFileServiceImpl extends BaseService<EsopWiFile> implements EsopWiFileService {

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
