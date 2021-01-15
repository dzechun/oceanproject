package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderBarcodePoolDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderBarcodePool;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderBarcodePool;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.pm.mapper.SmtWorkOrderBarcodePoolMapper;
import com.fantechs.provider.mes.pm.service.SmtWorkOrderBarcodePoolService;
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
    public List<SmtWorkOrderBarcodePoolDto> findList(SearchSmtWorkOrderBarcodePool searchSmtWorkOrderBarcodePool) {
        return smtWorkOrderBarcodePoolMapper.findList(searchSmtWorkOrderBarcodePool);
    }
}
