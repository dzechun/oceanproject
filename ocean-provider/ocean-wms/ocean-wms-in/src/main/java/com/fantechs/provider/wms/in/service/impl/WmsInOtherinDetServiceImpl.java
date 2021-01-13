package com.fantechs.provider.wms.in.service.impl;

import com.fantechs.common.base.general.dto.wms.in.WmsInOtherinDetDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInOtherinDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.in.mapper.WmsInOtherinDetMapper;
import com.fantechs.provider.wms.in.service.WmsInOtherinDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/12.
 */
@Service
public class WmsInOtherinDetServiceImpl  extends BaseService<WmsInOtherinDet> implements WmsInOtherinDetService {

         @Resource
         private WmsInOtherinDetMapper wmsInOtherinDetMapper;

    @Override
    public List<WmsInOtherinDetDto> findList(Map<String, Object> dynamicConditionByEntity) {
        return null;
    }
}
