package com.fantechs.provider.wms.in.service.impl;


import com.fantechs.common.base.general.dto.wms.in.WmsInFinishedProductDetDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInFinishedProductDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.in.mapper.WmsInFinishedProductDetMapper;
import com.fantechs.provider.wms.in.service.WmsInFinishedProductDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/07.
 */
@Service
public class WmsInFinishedProductDetServiceImpl  extends BaseService<WmsInFinishedProductDet> implements WmsInFinishedProductDetService {

         @Resource
         private WmsInFinishedProductDetMapper wmsInFinishedProductDetMapper;

    @Override
    public List<WmsInFinishedProductDetDto> findList(Map<String, Object> map) {
        return wmsInFinishedProductDetMapper.findList(map);
    }
}
