package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.general.dto.srm.SrmInHtAsnOrderDetDto;
import com.fantechs.common.base.general.entity.srm.history.SrmInHtAsnOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.srm.mapper.SrmInHtAsnOrderDetMapper;
import com.fantechs.provider.srm.service.SrmInHtAsnOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/29.
 */
@Service
public class SrmInHtAsnOrderDetServiceImpl extends BaseService<SrmInHtAsnOrderDet> implements SrmInHtAsnOrderDetService {

    @Resource
    private SrmInHtAsnOrderDetMapper srmInHtAsnOrderDetMapper;

    @Override
    public List<SrmInHtAsnOrderDetDto> findList(Map<String, Object> map) {
        return srmInHtAsnOrderDetMapper.findList(map);
    }

}
