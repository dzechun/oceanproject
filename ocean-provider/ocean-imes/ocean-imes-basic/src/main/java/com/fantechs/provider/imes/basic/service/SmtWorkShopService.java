package com.fantechs.provider.imes.basic.service;



import com.fantechs.common.base.dto.basic.SmtWorkShopDto;
import com.fantechs.common.base.entity.basic.SmtWorkShop;

import java.util.List;
import java.util.Map;

/**
 * Created by lfz on 2020/9/1.
 */
public interface SmtWorkShopService {
    List<SmtWorkShopDto> findList(Map<String, Object> map);
    int insert(SmtWorkShop workShop);
    int deleteById(String workShopId);
    int deleteByIds(List<String> workShopIds);
    int updateById(SmtWorkShop smtFactory);
    SmtWorkShop findById(String workShopId);
}
