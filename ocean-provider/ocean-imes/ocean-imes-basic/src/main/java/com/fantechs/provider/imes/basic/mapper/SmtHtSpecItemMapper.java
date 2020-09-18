package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.history.SmtHtSpecItem;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface SmtHtSpecItemMapper extends MyMapper<SmtHtSpecItem> {
    //int addBatchHtItem(List<SmtHtSpecItem> list);

    List<SmtHtSpecItem> findHtSpecItemList(Map<String, Object> map);
}