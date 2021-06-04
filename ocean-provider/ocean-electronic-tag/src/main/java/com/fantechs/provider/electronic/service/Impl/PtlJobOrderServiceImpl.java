package com.fantechs.provider.electronic.service.Impl;

import com.fantechs.common.base.electronic.dto.PtlJobOrderDto;
import com.fantechs.common.base.electronic.entity.PtlJobOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.electronic.mapper.PtlJobOrderMapper;
import com.fantechs.provider.electronic.service.PtlJobOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/01.
 */
@Service
public class PtlJobOrderServiceImpl extends BaseService<PtlJobOrder> implements PtlJobOrderService {

    @Resource
    private PtlJobOrderMapper ptlJobOrderMapper;

    @Override
    public List<PtlJobOrderDto> findList(Map<String, Object> map) {
        return ptlJobOrderMapper.findList(map);
    }
}
