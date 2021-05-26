package com.fantechs.provider.mes.sfc.service.impl;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcPalletReportDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductPalletDetDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductPalletDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductPallet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.sfc.mapper.MesSfcProductPalletMapper;
import com.fantechs.provider.mes.sfc.service.MesSfcPalletWorkService;
import com.fantechs.provider.mes.sfc.service.MesSfcProductPalletDetService;
import com.fantechs.provider.mes.sfc.service.MesSfcProductPalletService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * Created by 北归 on 2021/05/08.
 */
@Service
public class MesSfcProductPalletServiceImpl extends BaseService<MesSfcProductPallet> implements MesSfcProductPalletService {

    @Resource
    private MesSfcProductPalletMapper mesSfcProductPalletMapper;

    @Resource
    MesSfcProductPalletDetService mesSfcProductPalletDetService;

    @Override
    public List<MesSfcProductPalletDto> findList(Map<String, Object> map) {
        return mesSfcProductPalletMapper.findList(map);
    }

    @Override
    public List<MesSfcPalletReportDto> getPalletReport() {
        List<MesSfcPalletReportDto> list = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("moveStatus", 1);
        List<MesSfcProductPalletDto> productPalletDtos = mesSfcProductPalletMapper.findList(map);
        if(productPalletDtos == null || productPalletDtos.size() <= 0){
            return list;
        }
        List<Long> productPalletIds = new ArrayList<>();
        for (MesSfcProductPalletDto dto : productPalletDtos) {
            productPalletIds.add(dto.getProductPalletId());
        }
        map.clear();
        map.put("productPalletIds", productPalletIds);
        List<MesSfcProductPalletDetDto> palletDetServiceList = mesSfcProductPalletDetService.findList(map);
        for (MesSfcProductPalletDto dto : productPalletDtos) {
            MesSfcPalletReportDto reportDto = new MesSfcPalletReportDto();
            reportDto.setPalletCode(dto.getPalletCode());
            reportDto.setWorkOrderCode(dto.getWorkOrderCode());
            reportDto.setMaterialCode(dto.getMaterialCode());
            reportDto.setNowPackageSpecQty(dto.getNowPackageSpecQty());
            reportDto.setScanCartonNum(dto.getNowPackageSpecQty());
            int count = 0;
            for (MesSfcProductPalletDetDto detDto : palletDetServiceList){
                if(detDto.getProductPalletId().equals(dto.getProductPalletId())){
                    count ++;
                }
            }
            reportDto.setScanCartonNum(new BigDecimal(count));
            list.add(reportDto);
        }
        return list;
    }
}
