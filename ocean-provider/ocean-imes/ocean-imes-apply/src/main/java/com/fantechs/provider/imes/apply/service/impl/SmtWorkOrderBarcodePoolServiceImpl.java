package com.fantechs.provider.imes.apply.service.impl;

import com.fantechs.common.base.entity.apply.SmtWorkOrderBarcodePool;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderBarcodePool;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.apply.mapper.SmtWorkOrderBarcodePoolMapper;
import com.fantechs.provider.imes.apply.service.SmtWorkOrderBarcodePoolService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by wcz on 2020/11/23.
 */
@Service
public class SmtWorkOrderBarcodePoolServiceImpl extends BaseService<SmtWorkOrderBarcodePool> implements SmtWorkOrderBarcodePoolService {

    @Resource
    private SmtWorkOrderBarcodePoolMapper smtWorkOrderBarcodePoolMapper;

    @Override
    public List<SmtWorkOrderBarcodePool> findList(SearchSmtWorkOrderBarcodePool searchSmtWorkOrderBarcodePool) {
        return smtWorkOrderBarcodePoolMapper.findList(searchSmtWorkOrderBarcodePool);
    }
}