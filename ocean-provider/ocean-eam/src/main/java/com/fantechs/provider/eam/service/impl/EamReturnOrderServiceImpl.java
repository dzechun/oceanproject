package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.general.dto.eam.EamReturnOrderDto;
import com.fantechs.common.base.general.entity.eam.EamReturnOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.eam.mapper.EamReturnOrderMapper;
import com.fantechs.provider.eam.service.EamReturnOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 *
 * Created by leifengzhi on 2021/06/29.
 */
@Service
public class EamReturnOrderServiceImpl extends BaseService<EamReturnOrder> implements EamReturnOrderService {

    @Resource
    private EamReturnOrderMapper eamReturnOrderMapper;

    @Override
    public List<EamReturnOrderDto> findList(Map<String, Object> map) {
        return eamReturnOrderMapper.findList(map);
    }
}
