package com.fantechs.provider.mes.sfc.service;

public interface MesSfcBarcodeReprintService {

    /**
     *
     * @param keyword
     * @param codeType
     * @return
     */
    String findCode(String keyword, String codeType);

    int reprintBarcode(String barCode, String codeType);
}
