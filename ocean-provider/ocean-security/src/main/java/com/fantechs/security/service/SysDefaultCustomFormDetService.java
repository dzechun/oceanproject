package com.fantechs.security.service;

import com.fantechs.common.base.general.dto.security.SysDefaultCustomFormDetDto;
import com.fantechs.common.base.general.entity.security.SysDefaultCustomFormDet;
import com.fantechs.common.base.support.IService;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/09/07.
 */

public interface SysDefaultCustomFormDetService extends IService<SysDefaultCustomFormDet> {
    List<SysDefaultCustomFormDetDto> findList(Map<String, Object> map);
}
