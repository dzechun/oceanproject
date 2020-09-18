package com.fantechs.provider.imes.basic.service;


import com.fantechs.common.base.entity.basic.history.SmtHtSpecItem;

import java.util.List;
import java.util.Map;

public interface SmtHtSpecItemService {
    List<SmtHtSpecItem> findHtSpecItemList(Map<String, Object> map);
}
