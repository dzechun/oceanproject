package com.fantechs.auth.service;


import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

public interface SysSpecItemService extends IService<SysSpecItem> {
    //通过条件查询配置项信息
    List<SysSpecItem> findList(SearchSysSpecItem searchSysSpecItem);

}
