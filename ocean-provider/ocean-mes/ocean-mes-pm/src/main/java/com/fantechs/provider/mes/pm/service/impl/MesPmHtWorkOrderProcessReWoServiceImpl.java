package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtWorkOrderProcessReWo;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.pm.mapper.MesPmHtWorkOrderProcessReWoMapper;
import com.fantechs.provider.mes.pm.service.MesPmHtWorkOrderProcessReWoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/28.
 */
@Service
public class MesPmHtWorkOrderProcessReWoServiceImpl extends BaseService<MesPmHtWorkOrderProcessReWo> implements MesPmHtWorkOrderProcessReWoService {

    @Resource
    private MesPmHtWorkOrderProcessReWoMapper mesPmHtWorkOrderProcessReWoMapper;

    @Override
    public List<MesPmHtWorkOrderProcessReWo> findList(Map<String, Object> map) {
        return mesPmHtWorkOrderProcessReWoMapper.findList(map);
    }
}
