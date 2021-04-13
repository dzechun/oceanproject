package com.fantechs.provider.mes.sfc.service.impl;


import com.fantechs.common.base.general.dto.mes.sfc.MesSfcBarcodeProcessDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.sfc.mapper.MesSfcBarcodeProcessMapper;
import com.fantechs.provider.mes.sfc.service.MesSfcBarcodeProcessService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/09.
 */
@Service
public class MesSfcBarcodeProcessServiceImpl extends BaseService<MesSfcBarcodeProcess> implements MesSfcBarcodeProcessService {

    @Resource
    private MesSfcBarcodeProcessMapper mesSfcBarcodeProcessMapper;

    @Override
    public List<MesSfcBarcodeProcessDto> findList(Map<String, Object> map) {
        return mesSfcBarcodeProcessMapper.findList(map);
    }
}
