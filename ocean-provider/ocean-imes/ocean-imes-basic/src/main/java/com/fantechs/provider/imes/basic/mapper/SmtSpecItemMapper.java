package com.fantechs.provider.imes.basic.mapper;



import com.fantechs.common.base.entity.basic.SmtSpecItem;
import com.fantechs.common.base.entity.basic.search.SearchSmtSpecItem;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtSpecItemMapper extends MyMapper<SmtSpecItem> {
    List<SmtSpecItem> selectSpecItems(SearchSmtSpecItem searchSmtSpecItem);
}