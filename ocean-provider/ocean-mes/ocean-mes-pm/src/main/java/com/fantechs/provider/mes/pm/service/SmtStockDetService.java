package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.dto.mes.pm.SmtStockDetDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtStockDet;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtStockDet;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
* @author Mr.Lei
* @create 2020/11/24
*/
public interface SmtStockDetService extends IService<SmtStockDet> {

    List<SmtStockDetDto> findList(SearchSmtStockDet searchSmtStockDet);

}
