package com.fantechs.provider.imes.basic.service;



import com.fantechs.common.base.entity.basic.history.SmtHtWorkShop;

import java.util.List;
import java.util.Map;

/**
 * Created by lfz on 2020/9/1.
 */
public interface SmtHtWorkShopService {
    List<SmtHtWorkShop> findList(Map<String, Object> map);
}
