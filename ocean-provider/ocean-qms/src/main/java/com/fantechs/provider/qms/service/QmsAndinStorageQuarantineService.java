package com.fantechs.provider.qms.service;

import com.fantechs.common.base.dto.storage.MesPackageManagerDTO;
import com.fantechs.common.base.general.dto.qms.QmsAndinStorageQuarantineDto;
import com.fantechs.common.base.general.entity.qms.QmsAndinStorageQuarantine;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/07.
 */

public interface QmsAndinStorageQuarantineService extends IService<QmsAndinStorageQuarantine> {

    List<QmsAndinStorageQuarantineDto> findList(Map<String, Object> map);

    MesPackageManagerDTO analysisCode(Map<String, Object> map);
}
