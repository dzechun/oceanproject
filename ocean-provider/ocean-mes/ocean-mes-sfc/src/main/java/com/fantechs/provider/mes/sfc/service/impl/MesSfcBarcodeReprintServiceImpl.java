package com.fantechs.provider.mes.sfc.service.impl;

import com.fantechs.provider.mes.sfc.service.MesSfcBarcodeReprintService;
import org.springframework.stereotype.Service;

@Service
public class MesSfcBarcodeReprintServiceImpl implements MesSfcBarcodeReprintService {
    @Override
    public String findCode(String keyword, String codeType) {
        return null;
    }

    @Override
    public int reprintBarcode(String barCode, String codeType) {
        return 0;
    }
}
