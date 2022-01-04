package com.fantechs.provider.mes.sfc.service.impl;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductCartonDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductCarton;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductCartonDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.sfc.mapper.MesSfcProductCartonDetMapper;
import com.fantechs.provider.mes.sfc.mapper.MesSfcProductCartonMapper;
import com.fantechs.provider.mes.sfc.service.MesSfcProductCartonService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/08.
 */
@Service
public class MesSfcProductCartonServiceImpl extends BaseService<MesSfcProductCarton> implements MesSfcProductCartonService {

    @Resource
    private MesSfcProductCartonMapper mesSfcProductCartonMapper;
    @Resource
    private MesSfcProductCartonDetMapper mesSfcProductCartonDetMapper;

    @Override
    public List<MesSfcProductCartonDto> findList(Map<String, Object> map) {
        return mesSfcProductCartonMapper.findList(map);
    }

    @Override
    public int save(MesSfcProductCarton record) {
        int i = mesSfcProductCartonMapper.insertUseGeneratedKeys(record);

        List<MesSfcProductCartonDet> cartonDets = record.getCartonDets();
        if(StringUtils.isNotEmpty(cartonDets)){
            for (MesSfcProductCartonDet mesSfcProductCartonDet : cartonDets){
                mesSfcProductCartonDet.setProductCartonId(record.getProductCartonId());
            }
            mesSfcProductCartonDetMapper.insertList(cartonDets);
        }
        return i;
    }

}
