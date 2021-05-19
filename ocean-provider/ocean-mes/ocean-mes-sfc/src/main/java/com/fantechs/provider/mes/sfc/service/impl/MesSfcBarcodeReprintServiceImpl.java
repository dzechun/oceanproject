package com.fantechs.provider.mes.sfc.service.impl;

import com.fantechs.provider.mes.sfc.service.MesSfcBarcodeReprintService;
import org.springframework.stereotype.Service;

@Service
public class MesSfcBarcodeReprintServiceImpl implements MesSfcBarcodeReprintService {

    @Override
    public String findCode(String keyword, String codeType) {
        String barCode = null;
        if(codeType.equals("1")){

        }else if(codeType.equals("2")){

        }
        return barCode;
    }

    @Override
    public int reprintBarcode(String barCode, String codeType) {
        // 生成补打记录
        // 调用打印机打印
        return 0;
    }
}
