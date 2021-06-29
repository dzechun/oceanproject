package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.general.entity.eam.history.EamHtReturnOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.eam.mapper.EamHtReturnOrderMapper;
import com.fantechs.provider.eam.service.EamHtReturnOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/29.
 */
@Service
public class EamHtReturnOrderServiceImpl extends BaseService<EamHtReturnOrder> implements EamHtReturnOrderService {

    @Resource
    private EamHtReturnOrderMapper eamHtReturnOrderMapper;

    @Override
    public List<EamHtReturnOrder> findHtList(Map<String, Object> map) {
        return eamHtReturnOrderMapper.findHtList(map);
    }
}
