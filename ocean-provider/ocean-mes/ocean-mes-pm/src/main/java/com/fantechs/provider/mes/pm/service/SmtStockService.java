package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.dto.mes.pm.SmtStockDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtStock;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtStock;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
* @author Mr.Lei
* @create 2020/11/24
*/
public interface SmtStockService extends IService<SmtStock> {
    List<SmtStockDto> findList(SearchSmtStock searchSmtStock);
}
