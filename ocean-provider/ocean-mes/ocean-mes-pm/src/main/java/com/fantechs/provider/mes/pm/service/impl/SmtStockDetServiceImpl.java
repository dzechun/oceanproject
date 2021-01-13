package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.dto.apply.SmtStockDetDto;
import com.fantechs.common.base.entity.apply.SmtStockDet;
import com.fantechs.common.base.entity.apply.search.SearchSmtStockDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.pm.mapper.SmtStockDetMapper;
import com.fantechs.provider.mes.pm.service.SmtStockDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author Mr.Lei
* @create 2020/11/24
*/
@Service
public class SmtStockDetServiceImpl  extends BaseService<SmtStockDet> implements SmtStockDetService {

         @Resource
         private SmtStockDetMapper smtStockDetMapper;

    @Override
    public List<SmtStockDetDto> findList(SearchSmtStockDet searchSmtStockDet) {
        return smtStockDetMapper.findList(searchSmtStockDet);
    }
}
