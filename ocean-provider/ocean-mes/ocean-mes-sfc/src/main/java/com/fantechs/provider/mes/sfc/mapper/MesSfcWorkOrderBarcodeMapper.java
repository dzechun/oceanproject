package com.fantechs.provider.mes.sfc.mapper;

import com.fantechs.common.base.general.dto.mes.sfc.PrintModel;
import com.fantechs.common.base.general.dto.mes.sfc.LabelRuteDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderBarcodeDto;
import com.fantechs.common.base.general.dto.wms.in.PalletAutoAsnDto;
import com.fantechs.common.base.general.entity.basic.BaseLabel;
import com.fantechs.common.base.general.entity.basic.BaseLabelCategory;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.mes.sfc.SearchMesSfcWorkOrderBarcode;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface MesSfcWorkOrderBarcodeMapper extends MyMapper<MesSfcWorkOrderBarcode> {
    List<MesSfcWorkOrderBarcodeDto> findList(SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode);

    List<MesSfcWorkOrderBarcodeDto> findListOrderBarcode(SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode);

    String findMaxCode(@Param("barcodeType")Byte barcodeType,@Param("workOrderId")Long workOrderId);

    LabelRuteDto findRule(@Param("code")String code, @Param("workOrderId")Long workOrderId);

    LabelRuteDto findOmRule(@Param("salesOrderDetId")Long salesOrderDetId);

    LabelRuteDto DefaultLabel(@Param("labelCategoryCode")String labelCategoryCode);

    PrintModel findPrintModel(Map<String,Object>map);

    String findByOneLabel(@Param("labelName")String labelName);

    Integer findCountCode(@Param("barcodeType")Long barcodeType,@Param("workOrderId")Long workOrderId);

    BigDecimal saleOrderTotalQty(Map<String,Object> map);

    String findVersion(@Param("labelName")String labelName);

    Long finByTypeId(@Param("labelCategoryName")String labelCategoryName);

    /**
     * 批量修改生产订单条码状态
     * @param workOrderBarcodes
     * @return
     */
    int batchUpdate(List<MesSfcWorkOrderBarcode> workOrderBarcodes);

    List<MesSfcWorkOrderBarcodeDto> findByWorkOrderGroup(Map<String, Object> map);

    List<PalletAutoAsnDto> findListGroupByWorkOrder(Map<String, Object> map);

    List<MesSfcWorkOrderBarcodeDto> findListByPalletDet(Map<String, Object> map);

    String findSysUser(@Param("userCode")String userCode);

    String findLabelView(@Param("labelCode")String labelCode);
}