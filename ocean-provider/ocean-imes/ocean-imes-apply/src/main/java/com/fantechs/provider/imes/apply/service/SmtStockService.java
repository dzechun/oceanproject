package com.fantechs.provider.imes.apply.service;

import com.fantechs.common.base.dto.apply.SmtStockDto;
import com.fantechs.common.base.entity.apply.SmtStock;
import com.fantechs.common.base.entity.apply.search.SearchSmtStock;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
* @author Mr.Lei
* @create 2020/11/24
*/
public interface SmtStockService extends IService<SmtStock> {
    List<SmtStockDto> findList(SearchSmtStock searchSmtStock);
}
