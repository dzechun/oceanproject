package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.BaseFile;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/09/13.
 */

public interface BaseFileService extends IService<BaseFile> {
    List<BaseFile> findList(Map<String, Object> map);
    int batchAddFile(List<BaseFile> list);
}
