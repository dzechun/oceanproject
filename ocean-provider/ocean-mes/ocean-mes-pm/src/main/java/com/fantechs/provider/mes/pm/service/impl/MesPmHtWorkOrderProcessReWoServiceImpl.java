package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtWorkOrderProcessReWo;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.mapper.MesPmHtWorkOrderProcessReWoMapper;
import com.fantechs.provider.mes.pm.service.MesPmHtWorkOrderProcessReWoService;
import com.fantechs.provider.mes.pm.vo.MesPmHtWorkOrderProcessReWoVo;
import com.fantechs.provider.mes.pm.vo.MesPmWorkOrderProcessReWoVo;
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
    public List<MesPmHtWorkOrderProcessReWoVo> findList(Map<String, Object> map) {
        List<MesPmHtWorkOrderProcessReWoVo> list = mesPmHtWorkOrderProcessReWoMapper.findMaterialList(map);
        for (MesPmHtWorkOrderProcessReWoVo mesPmHtWorkOrderProcessReWoVo : list) {
            if (StringUtils.isNotEmpty(mesPmHtWorkOrderProcessReWoVo)){
                map.put("workOrderId",mesPmHtWorkOrderProcessReWoVo.getWorkOrderId());
                mesPmHtWorkOrderProcessReWoVo.setList(mesPmHtWorkOrderProcessReWoMapper.findList(map));
            }
        }
        return list;
    }
}
