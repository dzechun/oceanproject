package com.fantechs.auth.service;

import com.fantechs.common.base.general.dto.security.SysDefaultCustomFormDto;
import com.fantechs.common.base.general.entity.security.SysDefaultCustomForm;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/27.
 */

public interface SysDefaultCustomFormService extends IService<SysDefaultCustomForm> {
    List<SysDefaultCustomFormDto> findList(Map<String, Object> map);

    int syncDefaultData(Long orgId);
}
