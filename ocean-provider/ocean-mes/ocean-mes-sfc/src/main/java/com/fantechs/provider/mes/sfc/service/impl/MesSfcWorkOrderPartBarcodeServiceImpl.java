package com.fantechs.provider.mes.sfc.service.impl;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderPartBarcodeDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderPartBarcode;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.sfc.mapper.MesSfcWorkOrderPartBarcodeMapper;
import com.fantechs.provider.mes.sfc.service.MesSfcWorkOrderPartBarcodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/17.
 */
@Service
public class MesSfcWorkOrderPartBarcodeServiceImpl extends BaseService<MesSfcWorkOrderPartBarcode> implements MesSfcWorkOrderPartBarcodeService {

    @Resource
    private MesSfcWorkOrderPartBarcodeMapper mesSfcWorkOrderPartBarcodeMapper;

    @Override
    public List<MesSfcWorkOrderPartBarcodeDto> findList(Map<String, Object> map) {
        return mesSfcWorkOrderPartBarcodeMapper.findList(map);
    }
}
