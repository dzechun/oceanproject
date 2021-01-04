package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.general.entity.srm.history.SrmHtDeliveryNote;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.srm.mapper.SrmHtDeliveryNoteMapper;
import com.fantechs.provider.srm.service.SrmHtDeliveryNoteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/29.
 */
@Service
public class SrmHtDeliveryNoteServiceImpl extends BaseService<SrmHtDeliveryNote> implements SrmHtDeliveryNoteService {

    @Resource
    private SrmHtDeliveryNoteMapper srmHtDeliveryNoteMapper;

    @Override
    public List<SrmHtDeliveryNote> findHtList(Map<String, Object> map) {
        return srmHtDeliveryNoteMapper.findList(map);
    }
}
