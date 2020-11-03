package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.entity.basic.history.SmtHtPackingUnit;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.basic.mapper.SmtHtPackingUnitMapper;
import com.fantechs.provider.imes.basic.service.SmtHtPackingUnitService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/11/03.
 */
@Service
public class SmtHtPackingUnitServiceImpl extends BaseService<SmtHtPackingUnit> implements SmtHtPackingUnitService {

    @Resource
    private SmtHtPackingUnitMapper smtHtPackingUnitMapper;

    @Override
    public List<SmtHtPackingUnit> findHtList(Map<String, Object> map) {
        return smtHtPackingUnitMapper.findHtList(map);
    }
}
