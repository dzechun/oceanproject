package com.fantechs.provider.om.service.impl;

import com.fantechs.common.base.general.dto.om.OmTransferOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmTransferOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.om.mapper.OmTransferOrderDetMapper;
import com.fantechs.provider.om.service.OmTransferOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/15.
 */
@Service
public class OmTransferOrderDetServiceImpl extends BaseService<OmTransferOrderDet> implements OmTransferOrderDetService {

    @Resource
    private OmTransferOrderDetMapper omTransferOrderDetMapper;

    @Override
    public List<OmTransferOrderDetDto> findList(Map<String, Object> map) {
        return omTransferOrderDetMapper.findList(map);
    }
}
