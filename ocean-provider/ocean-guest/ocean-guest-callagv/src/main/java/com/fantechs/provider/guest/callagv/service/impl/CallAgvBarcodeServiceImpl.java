package com.fantechs.provider.guest.callagv.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.callagv.CallAgvBarcodeDto;
import com.fantechs.common.base.general.entity.callagv.CallAgvBarcode;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.guest.callagv.mapper.CallAgvBarcodeMapper;
import com.fantechs.provider.guest.callagv.service.CallAgvBarcodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class CallAgvBarcodeServiceImpl extends BaseService<CallAgvBarcode> implements CallAgvBarcodeService {

    @Resource
    private CallAgvBarcodeMapper callAgvBarcodeMapper;

    @Override
    public List<CallAgvBarcodeDto> findList(Map<String, Object> map) {
        return callAgvBarcodeMapper.findList(map);
    }
}
