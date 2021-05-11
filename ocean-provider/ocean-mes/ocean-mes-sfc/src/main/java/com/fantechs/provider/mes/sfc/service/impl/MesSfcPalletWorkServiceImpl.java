package com.fantechs.provider.mes.sfc.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.*;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcProductPallet;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.mes.sfc.SearchMesSfcWorkOrderBarcode;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.provider.mes.sfc.service.MesSfcKeyPartRelevanceService;
import com.fantechs.provider.mes.sfc.service.MesSfcPalletWorkService;
import com.fantechs.provider.mes.sfc.service.MesSfcProductPalletService;
import com.fantechs.provider.mes.sfc.service.MesSfcWorkOrderBarcodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class MesSfcPalletWorkServiceImpl implements MesSfcPalletWorkService {

    @Resource
    MesSfcWorkOrderBarcodeService mesSfcWorkOrderBarcodeService;

    @Resource
    MesSfcKeyPartRelevanceService mesSfcKeyPartRelevanceService;

    @Resource
    MesSfcProductPalletService mesSfcProductPalletService;

    @Override
    public int palletWorkScanBarcode(RequestPalletWorkScanDto requestPalletWorkScanDto) throws Exception {

        SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode = new SearchMesSfcWorkOrderBarcode();
        searchMesSfcWorkOrderBarcode.setBarcode(requestPalletWorkScanDto.getBarcode());
        List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtoList = mesSfcWorkOrderBarcodeService.findList(searchMesSfcWorkOrderBarcode);
        if (mesSfcWorkOrderBarcodeDtoList.isEmpty() || mesSfcWorkOrderBarcodeDtoList.size() > 1) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012000);
        }
        String productCode = "";
        if (mesSfcWorkOrderBarcodeDtoList.get(0).getBarcodeType() == 2) {
            productCode = requestPalletWorkScanDto.getBarcode();
        } else if (mesSfcWorkOrderBarcodeDtoList.get(0).getBarcodeType() == 3) {
            Map<String, Object> map = new HashMap<>();
            map.put("partBarcode", requestPalletWorkScanDto.getBarcode());
            List<MesSfcKeyPartRelevanceDto> mesSfcKeyPartRelevanceDtoList = mesSfcKeyPartRelevanceService.findList(map);
            if (mesSfcKeyPartRelevanceDtoList.isEmpty() || mesSfcKeyPartRelevanceDtoList.size() > 1) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该条码未绑定产品条码");
            }
            productCode = mesSfcKeyPartRelevanceDtoList.get(0).getBarcodeCode();
        }
        Map<String, Object> productPalletMap = new HashMap<>();
        productPalletMap.put("workOrderId", mesSfcWorkOrderBarcodeDtoList.get(0).getWorkOrderId());
        productPalletMap.put("closeStatus", 0);
        List<MesSfcProductPalletDto> mesSfcProductPalletDtoList = mesSfcProductPalletService.findList(productPalletMap);
        if (mesSfcProductPalletDtoList.size() >= requestPalletWorkScanDto.getMaxPalletNum()) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该条码不属于正在操作的" + mesSfcProductPalletDtoList.size() + "个栈板，最大栈板操作数量" + requestPalletWorkScanDto.getMaxPalletNum());
        }
        for (MesSfcProductPalletDto mesSfcProductPalletDto : mesSfcProductPalletDtoList) {
            if (requestPalletWorkScanDto.getPalletType() == 0 && mesSfcWorkOrderBarcodeDtoList.get(0).getWorkOrderId().equals(mesSfcProductPalletDto.getWorkOrderId())) {

            }
        }

        return 1;
    }

    @Override
    public List<PalletWorkScanDto> palletWorkScan() {

        List<PalletWorkScanDto> palletWorkScanDtoList = new LinkedList<>();

        return palletWorkScanDtoList;
    }

    @Override
    public List<String> findPalletCarton(String palletCode) {

        List<String> cartonCodeList = new LinkedList<>();

        return cartonCodeList;
    }

    @Override
    public int submitNoFullPallet(List<String> palletCodeList) throws Exception {

        return palletCodeList.size();
    }

    @Override
    public Boolean updatePalletType(Long stationId) {
        List<MesSfcProductPalletDto> list = mesSfcProductPalletService.findList(ControllerUtil.dynamicConditionByEntity(SearchMesSfcProductPallet.builder()
                .stationId(stationId)
                .build()));
        if(list.size()>0){
            return false;
        }
        return true;
    }
}
