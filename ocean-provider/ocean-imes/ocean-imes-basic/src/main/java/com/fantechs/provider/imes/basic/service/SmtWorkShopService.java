package com.fantechs.provider.imes.basic.service;



import com.fantechs.common.base.dto.basic.SmtFactoryDto;
import com.fantechs.common.base.dto.basic.SmtWorkShopDto;
import com.fantechs.common.base.entity.basic.SmtWorkShop;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 * Created by lfz on 2020/9/1.
 */
public interface SmtWorkShopService extends IService<SmtWorkShop> {
    List<SmtWorkShopDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<SmtWorkShopDto> smtWorkShopDtos);
}
