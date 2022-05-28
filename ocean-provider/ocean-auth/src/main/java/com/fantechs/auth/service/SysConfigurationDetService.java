package com.fantechs.auth.service;

import com.fantechs.common.base.general.dto.security.SysConfigurationDetDto;
import com.fantechs.common.base.general.entity.security.SysConfigurationDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/12/09.
 */

public interface SysConfigurationDetService extends IService<SysConfigurationDet> {
    List<SysConfigurationDetDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<SysConfigurationDet> list);

    int batchUpdate(List<SysConfigurationDet> sysConfigurationDets);
}
