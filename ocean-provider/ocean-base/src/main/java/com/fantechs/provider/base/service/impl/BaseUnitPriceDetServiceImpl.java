package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.dto.basic.BaseUnitPriceDetDto;
import com.fantechs.common.base.general.entity.basic.BaseUnitPriceDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.service.BaseUnitPriceDetService;
import com.fantechs.provider.base.mapper.BaseUnitPriceDetMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/03/09.
 */
@Service
public class BaseUnitPriceDetServiceImpl extends BaseService<BaseUnitPriceDet> implements BaseUnitPriceDetService {

    @Resource
    private BaseUnitPriceDetMapper baseUnitPriceDetMapper;

    @Override
    public List<BaseUnitPriceDetDto> findList(Map<String, Object> map) {
        return baseUnitPriceDetMapper.findList(map);
    }
}
