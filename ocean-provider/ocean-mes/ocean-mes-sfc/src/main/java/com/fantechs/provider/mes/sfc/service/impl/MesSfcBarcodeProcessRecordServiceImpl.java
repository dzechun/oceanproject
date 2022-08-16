package com.fantechs.provider.mes.sfc.service.impl;


import com.fantechs.common.base.general.dto.mes.sfc.MesSfcBarcodeProcessRecordDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcessRecord;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.sfc.mapper.MesSfcBarcodeProcessRecordMapper;
import com.fantechs.provider.mes.sfc.service.MesSfcBarcodeProcessRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/09.
 */
@Service
public class MesSfcBarcodeProcessRecordServiceImpl extends BaseService<MesSfcBarcodeProcessRecord> implements MesSfcBarcodeProcessRecordService {

    @Resource
    private MesSfcBarcodeProcessRecordMapper mesSfcBarcodeProcessRecordMapper;

    @Override
    public List<MesSfcBarcodeProcessRecordDto> findList(Map<String, Object> map) {
        return mesSfcBarcodeProcessRecordMapper.findList(map);
    }
}
