package com.fantechs.provider.ews.service;

import com.fantechs.common.base.general.dto.ews.EwsHtWarningUserInfoDto;
import com.fantechs.common.base.general.dto.ews.EwsWarningUserInfoDto;
import com.fantechs.common.base.general.dto.ews.imports.EwsWarningUserInfoImport;
import com.fantechs.common.base.general.entity.ews.EwsWarningUserInfo;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by mr.lei on 2021/12/27.
 */

public interface EwsWarningUserInfoService extends IService<EwsWarningUserInfo> {
    List<EwsWarningUserInfoDto> findList(Map<String, Object> map);

    Map<String,Object> importExcel(List<EwsWarningUserInfoImport> ewsWarningUserInfoImports);

    List<EwsHtWarningUserInfoDto> findHtList(Map<String,Object> map);
}
