package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.general.dto.srm.SrmDeliveryNoteDetDto;
import com.fantechs.common.base.general.entity.srm.SrmDeliveryNoteDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.srm.mapper.SrmDeliveryNoteDetMapper;
import com.fantechs.provider.srm.service.SrmDeliveryNoteDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/29.
 */
@Service
public class SrmDeliveryNoteDetServiceImpl extends BaseService<SrmDeliveryNoteDet> implements SrmDeliveryNoteDetService {

    @Resource
    private SrmDeliveryNoteDetMapper srmDeliveryNoteDetMapper;

    @Override
    public List<SrmDeliveryNoteDetDto> findList(Map<String, Object> map) {
        return srmDeliveryNoteDetMapper.findList(map);
    }
}
