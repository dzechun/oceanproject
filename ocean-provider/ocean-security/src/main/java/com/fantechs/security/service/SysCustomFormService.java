package com.fantechs.security.service;

import com.fantechs.common.base.general.dto.security.SysCustomFormDto;
import com.fantechs.common.base.general.entity.security.SysCustomForm;
import com.fantechs.common.base.general.entity.security.SysCustomFormDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/08.
 */

public interface SysCustomFormService extends IService<SysCustomForm> {
    List<SysCustomFormDto> findList(Map<String, Object> map);
    int saveInAllOrg(SysCustomForm sysCustomForm);
    int updateInAllOrg(SysCustomForm sysCustomForm);
    int batchDeleteInAllOrg(String ids);
}
