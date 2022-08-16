package com.fantechs.provider.mes.sfc.service.impl;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderPartBarcodeReprintDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderPartBarcodeReprint;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.sfc.mapper.MesSfcWorkOrderPartBarcodeReprintMapper;
import com.fantechs.provider.mes.sfc.service.MesSfcWorkOrderPartBarcodeReprintService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/17.
 */
@Service
public class MesSfcWorkOrderPartBarcodeReprintServiceImpl extends BaseService<MesSfcWorkOrderPartBarcodeReprint> implements MesSfcWorkOrderPartBarcodeReprintService {

    @Resource
    private MesSfcWorkOrderPartBarcodeReprintMapper mesSfcWorkOrderPartBarcodeReprintMapper;

    @Override
    public List<MesSfcWorkOrderPartBarcodeReprintDto> findList(Map<String, Object> map) {
        return mesSfcWorkOrderPartBarcodeReprintMapper.findList(map);
    }
}
