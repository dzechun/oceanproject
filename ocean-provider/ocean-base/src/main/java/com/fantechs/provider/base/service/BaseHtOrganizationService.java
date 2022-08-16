package com.fantechs.provider.base.service;


import com.fantechs.common.base.general.entity.basic.history.BaseHtOrganization;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/29.
 */

public interface BaseHtOrganizationService extends IService<BaseHtOrganization> {


    List<BaseHtOrganization> findHtList(Map<String, Object> map);
}
