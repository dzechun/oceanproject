package com.fantechs.provider.tem.service.impl;

import com.fantechs.common.base.general.dto.tem.TemVehicleDto;
import com.fantechs.common.base.general.entity.tem.TemVehicle;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.tem.mapper.TemVehicleMapper;
import com.fantechs.provider.tem.service.TemVehicleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/08.
 */
@Service
public class TemVehicleServiceImpl extends BaseService<TemVehicle> implements TemVehicleService {

    @Resource
    private TemVehicleMapper temVehicleMapper;

    @Override
    public List<TemVehicleDto> findList(Map<String, Object> map) {
        return temVehicleMapper.findList(map);
    }
}
