package com.fantechs.provider.wms.out.service.impl;

import com.fantechs.common.base.general.dto.wms.out.WmsOutDespatchOrderReJoReDetDto;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutDespatchOrderReJoReDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDespatchOrderReJoReDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.out.mapper.WmsOutDespatchOrderReJoReDetMapper;
import com.fantechs.provider.wms.out.service.WmsOutDespatchOrderReJoReDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by Mr.Lei on 2021/05/10.
 */
@Service
public class WmsOutDespatchOrderReJoReDetServiceImpl extends BaseService<WmsOutDespatchOrderReJoReDet> implements WmsOutDespatchOrderReJoReDetService {

    @Resource
    private WmsOutDespatchOrderReJoReDetMapper wmsOutDespatchOrderReJoReDetMapper;

    @Override
    public List<WmsOutDespatchOrderReJoReDetDto> findList(SearchWmsOutDespatchOrderReJoReDet searchWmsOutDespatchOrderReJoReDet) {
        return wmsOutDespatchOrderReJoReDetMapper.findList(searchWmsOutDespatchOrderReJoReDet);
    }
}
