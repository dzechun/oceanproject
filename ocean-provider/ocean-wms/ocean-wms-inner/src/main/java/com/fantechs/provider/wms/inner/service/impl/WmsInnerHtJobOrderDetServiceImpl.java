package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerHtJobOrderDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerHtJobOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.inner.mapper.WmsInnerHtJobOrderDetMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerHtJobOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/01.
 */
@Service
public class WmsInnerHtJobOrderDetServiceImpl extends BaseService<WmsInnerHtJobOrderDet> implements WmsInnerHtJobOrderDetService {

    @Resource
    private WmsInnerHtJobOrderDetMapper wmsInnerHtJobOrderDetMapper;

//    @Override
//    public List<WmsInnerHtJobOrderDetDto> findList(Map<String, Object> map) {
//        return wmsInnerHtJobOrderDetMapper.findList(map);
//    }
}
