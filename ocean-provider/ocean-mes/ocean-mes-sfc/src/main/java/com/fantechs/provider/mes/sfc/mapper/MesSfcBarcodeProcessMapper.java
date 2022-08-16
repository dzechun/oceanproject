package com.fantechs.provider.mes.sfc.mapper;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcBarcodeProcessDto;
import com.fantechs.common.base.general.dto.mes.sfc.SyncBarcodeProcessDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesSfcBarcodeProcessMapper extends MyMapper<MesSfcBarcodeProcess> {
    List<MesSfcBarcodeProcessDto> findList(Map<String, Object> map);

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
     * 批量修改
     * @param list
     * @return
     */
    int batchUpdateCustomerBarcode(List<MesSfcBarcodeProcess> list);

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

    /**
     * 万宝同步PQMS数据=通过类别查询条码流程数据
     * @return
     */
    List<SyncBarcodeProcessDto> findByLabelCategory(Map<String, Object> map);

//    MesSfcBarcodeProcess findProductionTimeByBarcode(@Param("barcodeCode")String barcodeCode);
}