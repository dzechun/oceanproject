package com.fantechs.provider.guest.callagv.service.impl;

import com.fantechs.common.base.general.dto.callagv.CallAgvWarehouseAreaReBarcodeDto;
import com.fantechs.common.base.general.entity.callagv.CallAgvWarehouseAreaReBarcode;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.guest.callagv.mapper.CallAgvWarehouseAreaReBarcodeMapper;
import com.fantechs.provider.guest.callagv.service.CallAgvWarehouseAreaReBarcodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class CallAgvWarehouseAreaReBarcodeServiceImpl extends BaseService<CallAgvWarehouseAreaReBarcode> implements CallAgvWarehouseAreaReBarcodeService {

    @Resource
    private CallAgvWarehouseAreaReBarcodeMapper callAgvWarehouseAreaReBarcodeMapper;

    @Override
    public List<CallAgvWarehouseAreaReBarcodeDto> findList(Map<String, Object> map) {
        return callAgvWarehouseAreaReBarcodeMapper.findList(map);
    }
}
