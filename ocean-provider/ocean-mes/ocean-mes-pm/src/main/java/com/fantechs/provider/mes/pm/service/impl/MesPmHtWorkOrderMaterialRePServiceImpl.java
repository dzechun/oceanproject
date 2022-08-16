package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtWorkOrderMaterialReP;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.pm.mapper.MesPmHtWorkOrderMaterialRePMapper;
import com.fantechs.provider.mes.pm.service.MesPmHtWorkOrderMaterialRePService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/28.
 */
@Service
public class MesPmHtWorkOrderMaterialRePServiceImpl extends BaseService<MesPmHtWorkOrderMaterialReP> implements MesPmHtWorkOrderMaterialRePService {

    @Resource
    private MesPmHtWorkOrderMaterialRePMapper mesPmHtWorkOrderMaterialRePMapper;

    @Override
    public List<MesPmHtWorkOrderMaterialReP> findList(Map<String, Object> map) {
        return mesPmHtWorkOrderMaterialRePMapper.findList(map);
    }
}
