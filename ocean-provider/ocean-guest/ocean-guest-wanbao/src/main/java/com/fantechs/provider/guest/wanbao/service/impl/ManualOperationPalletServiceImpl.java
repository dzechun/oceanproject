package com.fantechs.provider.guest.wanbao.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.PalletWorkByManualOperationDto;
import com.fantechs.common.base.general.dto.mes.sfc.ScanByManualOperationDto;
import com.fantechs.common.base.general.dto.wanbao.WanbaoStackingDto;
import com.fantechs.common.base.general.entity.wanbao.WanbaoStacking;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.guest.wanbao.service.ManualOperationPalletService;
import com.fantechs.provider.guest.wanbao.service.WanbaoStackingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ManualOperationPalletServiceImpl implements ManualOperationPalletService {

    @Resource
    WanbaoStackingService stackingService;
    @Resource
    SFCFeignApi sfcFeignApi;

    @Override
    public int workByManualOperation(PalletWorkByManualOperationDto dto) throws Exception {
        Integer i = sfcFeignApi.workByManualOperation(dto).getData();
        return i;
    }

    @Override
    public ScanByManualOperationDto scanByManualOperation(String barcode, Long proLineId) {
        ScanByManualOperationDto dto = sfcFeignApi.scanByManualOperation(barcode, proLineId).getData();
        return dto;
    }

    @Override
    public WanbaoStackingDto scanStackingCode(String stackingCode, Long proLineId) {
        Map<String, Object> map = new HashMap<>();
        map.put("stackingCode", stackingCode);
        List<WanbaoStackingDto> stackingDtos = stackingService.findList(map);
        if (stackingDtos.isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该堆垛编码在平台中不存在或已被删除");
        }
        return stackingDtos.get(0);
    }
}
