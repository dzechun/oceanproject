package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.general.dto.srm.SrmInAsnOrderDetDto;
import com.fantechs.common.base.general.entity.srm.SrmInAsnOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.srm.mapper.SrmInAsnOrderDetMapper;
import com.fantechs.provider.srm.service.SrmInAsnOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/25.
 */
@Service
public class SrmInAsnOrderDetServiceImpl extends BaseService<SrmInAsnOrderDet> implements SrmInAsnOrderDetService {

    @Resource
    private SrmInAsnOrderDetMapper srmInAsnOrderDetMapper;

    @Override
    public List<SrmInAsnOrderDetDto> findList(Map<String, Object> map) {
        return srmInAsnOrderDetMapper.findList(map);
    }

}
