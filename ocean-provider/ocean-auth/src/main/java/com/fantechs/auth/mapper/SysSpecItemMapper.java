package com.fantechs.auth.mapper;

import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface SysSpecItemMapper extends MyMapper<SysSpecItem> {
    List<SysSpecItem> findList(SearchSysSpecItem searchSysSpecItem);

    List<SysSpecItem> findByMenuIdList(SearchSysSpecItem searchSysSpecItem);
}
