package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.dto.apply.SmtStockDetDto;
import com.fantechs.common.base.entity.apply.SmtStockDet;
import com.fantechs.common.base.entity.apply.search.SearchSmtStockDet;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
* @author Mr.Lei
* @create 2020/11/24
*/
public interface SmtStockDetService extends IService<SmtStockDet> {

    List<SmtStockDetDto> findList(SearchSmtStockDet searchSmtStockDet);

}
