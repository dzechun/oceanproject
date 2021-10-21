package com.fantechs.provider.mes.sfc.mapper;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcBarcodeProcessDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

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
}