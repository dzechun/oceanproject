package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.general.dto.eam.EamRequisitionOrderDto;
import com.fantechs.common.base.general.entity.eam.EamRequisitionOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.eam.mapper.EamRequisitionOrderMapper;
import com.fantechs.provider.eam.service.EamRequisitionOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/29.
 */
@Service
public class EamRequisitionOrderServiceImpl extends BaseService<EamRequisitionOrder> implements EamRequisitionOrderService {

    @Resource
    private EamRequisitionOrderMapper eamRequisitionOrderMapper;

    @Override
    public List<EamRequisitionOrderDto> findList(Map<String, Object> map) {
        return eamRequisitionOrderMapper.findList(map);
    }
}
