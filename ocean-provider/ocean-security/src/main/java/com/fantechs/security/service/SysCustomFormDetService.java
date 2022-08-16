package com.fantechs.security.service;

import com.fantechs.common.base.general.dto.security.SysCustomFormDetDto;
import com.fantechs.common.base.general.entity.security.SysCustomFormDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/08.
 */

public interface SysCustomFormDetService extends IService<SysCustomFormDet> {
    List<SysCustomFormDetDto> findList(Map<String, Object> map);

    int saveInAllOrg(SysCustomFormDet sysCustomFormDet);

    int updateInAllOrg(SysCustomFormDet sysCustomFormDet);

    int batchDeleteInAllOrg(String ids);
}
