package com.fantechs.provider.imes.basic.service;



import com.fantechs.common.base.entity.sysmanage.history.SmtHtFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by lfz on 2020/9/1.
 */
public interface SmtHtFactoryService {
    List<SmtHtFactory> findList(Map<String, Object> map);
}
