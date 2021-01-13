package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.dto.apply.SmtStockDto;
import com.fantechs.common.base.entity.apply.SmtStock;
import com.fantechs.common.base.entity.apply.search.SearchSmtStock;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.pm.mapper.SmtStockDetMapper;
import com.fantechs.provider.mes.pm.mapper.SmtStockMapper;
import com.fantechs.provider.mes.pm.service.SmtStockService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author Mr.Lei
* @create 2020/11/24
*/
@Service
public class SmtStockServiceImpl  extends BaseService<SmtStock> implements SmtStockService {

    @Resource
    private SmtStockMapper smtStockMapper;
    @Resource
    private SmtStockDetMapper smtStockDetMapper;

    @Override
    public List<SmtStockDto> findList(SearchSmtStock searchSmtStock) {
        return smtStockMapper.findList(searchSmtStock);
    }
}
