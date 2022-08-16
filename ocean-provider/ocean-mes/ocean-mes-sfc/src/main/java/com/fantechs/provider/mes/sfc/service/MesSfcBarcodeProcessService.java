package com.fantechs.provider.mes.sfc.service;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcBarcodeProcessDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/04/09.
 */

public interface MesSfcBarcodeProcessService extends IService<MesSfcBarcodeProcess> {
    List<MesSfcBarcodeProcessDto> findList(Map<String, Object> map);

    List<MesSfcBarcodeProcess> findBarcode(SearchMesSfcBarcodeProcess searchMesSfcBarcodeProcess);

    /**
     * 按PO分组找PO
     * @param map
     * @return
     */
    List<MesSfcBarcodeProcess> findByPOGroup(Map<String, Object> map);

    /**
     * 按PO分组找栈板PO
     * @param map
     * @return
     */
    List<MesSfcBarcodeProcess> findByPalletPOGroup(Map<String, Object> map);

    /**
     * 查找下一道工序为栈板作业的记录
     * @param map
     * @return
     */
    List<MesSfcBarcodeProcess> findNextProcessIsPallet(Map<String, Object> map);

    /**
     * 批量修改
     * @param mesSfcBarcodeProcessList
     * @return
     */
    int batchUpdate(List<MesSfcBarcodeProcess> mesSfcBarcodeProcessList);

    /**
     * 人工栈板作业-按PO号分组，统计条码的PO号个数
     * @return
     */
    String countBarcodeListForPOGroup(Map<String, Object> map);

    /**
     * 人工栈板作业-按销售编码分组，统计条码的销售编码个数
     * @return
     */
    String countBarcodeListForSalesOrder(Map<String, Object> map);

    /**
     * 人工栈板作业-按物料分组，统计条码的物料个数
     * @param map
     * @return
     */
    String countBarcodeListForMaterial(Map<String, Object> map);
}
