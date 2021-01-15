package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.dto.basic.BasePlatePartsDetDto;
import com.fantechs.common.base.general.entity.basic.BasePlatePartsDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BasePlatePartsDetMapper;
import com.fantechs.provider.base.service.BasePlatePartsDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/15.
 */
@Service
public class BasePlatePartsDetServiceImpl  extends BaseService<BasePlatePartsDet> implements BasePlatePartsDetService {

    @Resource
    private BasePlatePartsDetMapper basePlatePartsDetMapper;

    @Override
    public List<BasePlatePartsDetDto> findList(Map<String, Object> map) {
        return basePlatePartsDetMapper.findList(map);
    }
}
