package com.fantechs.provider.guest.wanbao.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.PalletWorkByManualOperationDto;
import com.fantechs.common.base.general.dto.mes.sfc.ScanByManualOperationDto;
import com.fantechs.common.base.general.dto.wanbao.WanbaoStackingDetDto;
import com.fantechs.common.base.general.dto.wanbao.WanbaoStackingDto;
import com.fantechs.common.base.general.entity.wanbao.WanbaoStacking;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.guest.wanbao.service.ManualOperationPalletService;
import com.fantechs.provider.guest.wanbao.service.WanbaoStackingDetService;
import com.fantechs.provider.guest.wanbao.service.WanbaoStackingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ManualOperationPalletServiceImpl implements ManualOperationPalletService {

    @Resource
    WanbaoStackingService stackingService;
    @Resource
    WanbaoStackingDetService stackingDetService;
    @Resource
    SFCFeignApi sfcFeignApi;

    @Override
    public int workByManualOperation(PalletWorkByManualOperationDto dto) {
        if (dto.getWanbaoBarcodeDtos().isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "条码集合为空，请扫码后提交");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("stackingCodeNotLike", dto.getStackingCode());
        List<WanbaoStackingDto> stackingDtos = stackingService.findList(map);
        if (stackingDtos.isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该堆垛编码在平台中不存在或已被删除");
        }
        WanbaoStackingDto stackingDto = stackingDtos.get(0);
        if (!stackingDtos.get(0).getProLineId().equals(dto.getProLineId())){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该堆垛编码跟配置产线不匹配");
        }
        // 容量校验
        map.clear();
        map.put("stackingId", stackingDto.getStackingId());
        List<WanbaoStackingDetDto> stackingDetDtos = stackingDetService.findList(map);
        if (!stackingDetDtos.isEmpty() && new BigDecimal(stackingDetDtos.size()).compareTo(stackingDto.getMaxCapacity()) > -1){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该堆垛编码容量已满，不可继续操作");
        }
        int count = dto.getWanbaoBarcodeDtos().size() + stackingDetDtos.size();
        if (new BigDecimal(count).compareTo(stackingDto.getMaxCapacity()) == 1){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该堆垛编码容量存放不下当前提交数量");
        }
        ResponseEntity<Integer> responseEntity = sfcFeignApi.workByManualOperation(dto);
        if (responseEntity.getCode() != 0){
            throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
        }
        // 修改状态
        WanbaoStacking stacking = new WanbaoStacking();
        BeanUtil.copyProperties(stackingDto, stacking);
        stacking.setUsageStatus((byte) 2);
        return stackingService.update(stacking);
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
        if (StringUtils.isNotEmpty(stackingCode)){
            map.put("stackingCode", stackingCode);
        }
        map.put("proLineId", proLineId);
        List<WanbaoStackingDto> stackingDtos = stackingService.findList(map);
        if (stackingDtos.isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该堆垛编码在平台中不存在或已被删除");
        }
        return stackingDtos;
    }
}
