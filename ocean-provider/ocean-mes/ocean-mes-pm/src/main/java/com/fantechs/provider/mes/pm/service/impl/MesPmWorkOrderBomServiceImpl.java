package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderBom;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.pm.mapper.MesPmWorkOrderBomMapper;
import com.fantechs.provider.mes.pm.service.MesPmWorkOrderBomService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 * Created by leifengzhi on 2021/05/31.
 */
@Service
public class MesPmWorkOrderBomServiceImpl extends BaseService<MesPmWorkOrderBom> implements MesPmWorkOrderBomService {

    @Resource
    private MesPmWorkOrderBomMapper mesPmWorkOrderBomMapper;

   /* @Override
    public List<MesPmWorkOrderBomDto> findList(Map<String, Object> map) {
        return mesPmWorkOrderBomMapper.findList(map);
    }*/

}
