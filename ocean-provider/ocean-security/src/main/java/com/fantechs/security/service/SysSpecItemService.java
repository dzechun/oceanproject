package com.fantechs.security.service;


import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

public interface SysSpecItemService extends IService<SysSpecItem> {
    //通过条件查询配置项信息
    List<SysSpecItem> findList(SearchSysSpecItem searchSysSpecItem);

    //通过配置类别，查询该类别下所有模块名称
    List<SysSpecItem> findModule();

    int addModule(String moduleName);

    int updateModule(Map<String, Object> map);

    int deleteModule(String ids);
}
