package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.entity.basic.history.SmtHtWorkShop;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.basic.mapper.SmtHtWorkShopMapper;
import com.fantechs.provider.imes.basic.service.SmtHtWorkShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by lfz on 2020/9/2.
 */
@Service
public class SmtHtWorkShopServiceImpl extends BaseService<SmtHtWorkShop> implements SmtHtWorkShopService {
    @Autowired
    private SmtHtWorkShopMapper smtHtWorkShopMapper;
    @Override
    public List<SmtHtWorkShop> findList(Map<String, Object> map) {
        return smtHtWorkShopMapper.findList(map);
    }
}
