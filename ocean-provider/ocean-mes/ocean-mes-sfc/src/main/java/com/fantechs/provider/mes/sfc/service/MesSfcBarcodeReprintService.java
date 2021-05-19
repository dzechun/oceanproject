package com.fantechs.provider.mes.sfc.service;

public interface MesSfcBarcodeReprintService {

    /**
     * 查找条码
     * @param keyword 条码编号，模糊匹配
     * @param codeType 1:包箱，2:栈板
     * @return
     */
    String findCode(String keyword, String codeType);

    /**
     * 重打条码
     * @param barCode 条码编码
     * @param codeType 1:包箱，2:栈板
     * @return
     */
    int reprintBarcode(String barCode, String codeType);
}
