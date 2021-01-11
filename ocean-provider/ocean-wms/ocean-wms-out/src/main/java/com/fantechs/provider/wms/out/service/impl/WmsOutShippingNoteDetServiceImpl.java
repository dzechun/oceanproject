package com.fantechs.provider.wms.out.service.impl;

import com.fantechs.common.base.general.dto.wms.out.WmsOutShippingNoteDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutShippingNoteDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.out.mapper.WmsOutShippingNoteDetMapper;
import com.fantechs.provider.wms.out.service.WmsOutShippingNoteDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/09.
 */
@Service
public class WmsOutShippingNoteDetServiceImpl  extends BaseService<WmsOutShippingNoteDet> implements WmsOutShippingNoteDetService {

    @Resource
    private WmsOutShippingNoteDetMapper wmsOutShippingNoteDetMapper;

    @Override
    public List<WmsOutShippingNoteDetDto> findList(Map<String, Object> dynamicConditionByEntity) {
        return null;
    }
}
