package com.fantechs.provider.wms.inner.service;

/**
 * 拣货单
 * @Author mr.lei
 * @Date 2021/5/10
 */
public interface PDAWmsInnerCartonSplitAndCombineService {
    int checkCartonCode(String cartonCode,Integer type);
}
