package com.fantechs.provider.guest.wanbao.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.PalletWorkByManualOperationDto;
import com.fantechs.common.base.general.dto.mes.sfc.ScanByManualOperationDto;
import com.fantechs.common.base.general.dto.wanbao.WanbaoStackingDto;
import com.fantechs.common.base.general.entity.wanbao.WanbaoStacking;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.guest.wanbao.service.ManualOperationPalletService;
import com.fantechs.provider.guest.wanbao.service.WanbaoStackingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ManualOperationPalletServiceImpl implements ManualOperationPalletService {

    @Resource
    WanbaoStackingService stackingService;
    @Resource
    SFCFeignApi sfcFeignApi;

    @Override
    public int workByManualOperation(PalletWorkByManualOperationDto dto) {
        ResponseEntity<Integer> responseEntity = sfcFeignApi.workByManualOperation(dto);
        if (responseEntity.getCode() != 0){
            throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
        }
        Integer i = responseEntity.getData();
        return i;
    }

    @Override
    public ScanByManualOperationDto scanByManualOperation(String barcode, Long proLineId) {
        ResponseEntity<ScanByManualOperationDto> responseEntity = sfcFeignApi.scanByManualOperation(barcode, proLineId);
        if (responseEntity.getCode() != 0){
            throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
        }
        ScanByManualOperationDto dto = responseEntity.getData();
        return dto;
    }

    @Override
    public List<WanbaoStackingDto> scanStackingCode(String stackingCode, Long proLineId) {
        Map<String, Object> map = new HashMap<>();
        map.put("stackingCode", stackingCode);
        List<WanbaoStackingDto> stackingDtos = stackingService.findList(map);
        if (stackingDtos.isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该堆垛编码在平台中不存在或已被删除");
        }
        List<WanbaoStackingDto> list = new ArrayList<>();
        for (WanbaoStackingDto dto : stackingDtos){
            if (dto.getProLineId().equals(proLineId)){
                list.add(dto);
            }
        }
        return list;
    }
}
