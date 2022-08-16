package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderMaterialRePDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderMaterialReP;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.pm.mapper.MesPmWorkOrderMaterialRePMapper;
import com.fantechs.provider.mes.pm.service.MesPmWorkOrderMaterialRePService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/28.
 */
@Service
public class MesPmWorkOrderMaterialRePServiceImpl extends BaseService<MesPmWorkOrderMaterialReP> implements MesPmWorkOrderMaterialRePService {

    @Resource
    private MesPmWorkOrderMaterialRePMapper mesPmWorkOrderMaterialRePMapper;

    @Override
    public List<MesPmWorkOrderMaterialRePDto> findList(Map<String, Object> map) {
        return mesPmWorkOrderMaterialRePMapper.findList(map);
    }
}
