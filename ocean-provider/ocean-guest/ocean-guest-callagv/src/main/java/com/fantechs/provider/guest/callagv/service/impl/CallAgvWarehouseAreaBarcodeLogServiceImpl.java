package com.fantechs.provider.guest.callagv.service.impl;

import com.fantechs.common.base.general.dto.callagv.CallAgvWarehouseAreaBarcodeLogDto;
import com.fantechs.common.base.general.entity.callagv.CallAgvWarehouseAreaBarcodeLog;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.guest.callagv.mapper.CallAgvWarehouseAreaBarcodeLogMapper;
import com.fantechs.provider.guest.callagv.service.CallAgvWarehouseAreaBarcodeLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class CallAgvWarehouseAreaBarcodeLogServiceImpl extends BaseService<CallAgvWarehouseAreaBarcodeLog> implements CallAgvWarehouseAreaBarcodeLogService {

    @Resource
    private CallAgvWarehouseAreaBarcodeLogMapper callAgvWarehouseAreaBarcodeLogMapper;

    @Override
    public List<CallAgvWarehouseAreaBarcodeLogDto> findList(Map<String, Object> map) {
        return callAgvWarehouseAreaBarcodeLogMapper.findList(map);
    }
}
