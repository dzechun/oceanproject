package com.fantechs.provider.mes.sfc.service.impl;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcHtReworkOrderBarcodeDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcReworkOrderBarcodeDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcReworkOrderBarcode;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.sfc.mapper.MesSfcHtReworkOrderBarcodeMapper;
import com.fantechs.provider.mes.sfc.mapper.MesSfcReworkOrderBarcodeMapper;
import com.fantechs.provider.mes.sfc.service.MesSfcReworkOrderBarcodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/15.
 */
@Service
public class MesSfcReworkOrderBarcodeServiceImpl extends BaseService<MesSfcReworkOrderBarcode> implements MesSfcReworkOrderBarcodeService {

    @Resource
    private MesSfcReworkOrderBarcodeMapper mesSfcReworkOrderBarcodeMapper;

    @Resource
    private MesSfcHtReworkOrderBarcodeMapper mesSfcHtReworkOrderBarcodeMapper;

    @Override
    public List<MesSfcReworkOrderBarcodeDto> findList(Map<String, Object> map) {
        return mesSfcReworkOrderBarcodeMapper.findList(map);
    }

    @Override
    public List<MesSfcHtReworkOrderBarcodeDto> findHtList(Map<String, Object> map) {
        return mesSfcHtReworkOrderBarcodeMapper.findList(map);
    }
}
