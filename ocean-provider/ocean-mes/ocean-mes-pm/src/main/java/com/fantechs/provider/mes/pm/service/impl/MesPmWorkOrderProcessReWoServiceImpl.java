package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderProcessReWoDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderProcessReWo;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.pm.mapper.MesPmWorkOrderProcessReWoMapper;
import com.fantechs.provider.mes.pm.service.MesPmWorkOrderProcessReWoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/28.
 */
@Service
public class MesPmWorkOrderProcessReWoServiceImpl extends BaseService<MesPmWorkOrderProcessReWo> implements MesPmWorkOrderProcessReWoService {

    @Resource
    private MesPmWorkOrderProcessReWoMapper mesPmWorkOrderProcessReWoMapper;

    @Override
    public List<MesPmWorkOrderProcessReWoDto> findList(Map<String, Object> map) {
        return mesPmWorkOrderProcessReWoMapper.findList(map);
    }
}
