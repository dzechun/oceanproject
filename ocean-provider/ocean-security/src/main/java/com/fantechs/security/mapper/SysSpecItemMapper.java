package com.fantechs.security.mapper;




import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SysSpecItemMapper extends MyMapper<SysSpecItem> {
    List<SysSpecItem> selectSpecItems(SearchSysSpecItem searchSysSpecItem);
}