package com.fantechs.security.service;

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
    List<SysCustomForm> findList(Map<String, Object> map);
}
