package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.general.dto.srm.SrmInHtAsnOrderDetBarcodeDto;
import com.fantechs.common.base.general.entity.srm.history.SrmInHtAsnOrderDetBarcode;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.srm.mapper.SrmInHtAsnOrderDetBarcodeMapper;
import com.fantechs.provider.srm.service.SrmInHtAsnOrderDetBarcodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/29.
 */
@Service
public class SrmInHtAsnOrderDetBarcodeServiceImpl extends BaseService<SrmInHtAsnOrderDetBarcode> implements SrmInHtAsnOrderDetBarcodeService {

    @Resource
    private SrmInHtAsnOrderDetBarcodeMapper srmInHtAsnOrderDetBarcodeMapper;

    @Override
    public List<SrmInHtAsnOrderDetBarcodeDto> findList(Map<String, Object> map) {
        return srmInHtAsnOrderDetBarcodeMapper.findList(map);
    }

}
