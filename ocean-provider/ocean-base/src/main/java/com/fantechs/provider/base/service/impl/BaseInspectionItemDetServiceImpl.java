package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.dto.basic.BaseInspectionItemDetDto;
import com.fantechs.common.base.general.entity.basic.BaseInspectionItemDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseInspectionItemDetMapper;
import com.fantechs.provider.base.service.BaseInspectionItemDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/25.
 */
@Service
public class BaseInspectionItemDetServiceImpl extends BaseService<BaseInspectionItemDet> implements BaseInspectionItemDetService {

    @Resource
    private BaseInspectionItemDetMapper baseInspectionItemDetMapper;

    @Override
    public List<BaseInspectionItemDetDto> findList(Map<String, Object> map) {
        return baseInspectionItemDetMapper.findList(map);
    }
}
