package com.fantechs.provider.mes.sfc.service;

import java.util.List;

public interface MesSfcBarcodeReprintService {

    /**
     * 查找条码
     * @param keyword 条码编号，模糊匹配
     * @param barocdeType 1:包箱，2:栈板
     * @return
     */
    List<String> findCode(String keyword, String barocdeType);

    /**
     * 重打条码
     * @param barCode 条码编码
     * @param barocdeType 1:包箱，2:栈板
     * @return
     */
    int reprintBarcode(String barCode, byte barocdeType, String printName) throws Exception;
}
