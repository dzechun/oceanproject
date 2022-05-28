package com.fantechs.auth.service;

import com.fantechs.common.base.general.dto.security.SysConfigurationDto;
import com.fantechs.common.base.general.dto.security.SysFieldDto;
import com.fantechs.common.base.general.dto.security.SysTableDto;
import com.fantechs.common.base.general.entity.security.SysConfiguration;
import com.fantechs.common.base.support.IService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/12/09.
 */

public interface SysConfigurationService extends IService<SysConfiguration> {
    List<SysConfigurationDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<SysConfiguration> list);

    List<SysTableDto> findTablesByName(String tableName);

    List<SysFieldDto> findFieldList(String tableName);

    int push(Long id);

}
