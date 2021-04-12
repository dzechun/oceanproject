package com.fantechs.provider.mes.sfc.mapper;

import com.fantechs.common.base.general.dto.mes.sfc.PrintModel;
import com.fantechs.common.base.general.dto.mes.sfc.LabelRuteDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderBarcodeDto;
import com.fantechs.common.base.general.entity.basic.BaseLabel;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.mes.sfc.SearchMesSfcWorkOrderBarcode;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MesSfcWorkOrderBarcodeMapper extends MyMapper<MesSfcWorkOrderBarcode> {
    List<MesSfcWorkOrderBarcodeDto> findList(SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode);

    String findMaxCode(@Param("barcodeType")Byte barcodeType,@Param("workOrderId")Long workOrderId);

    LabelRuteDto findRule(@Param("code")String code, @Param("workOrderId")Long workOrderId);

    LabelRuteDto DefaultLabel(@Param("labelCategoryCode")String labelCategoryCode);

    PrintModel findPrintModel(@Param("viewName")String viewName);

    BaseLabel findByOneLabel(@Param("labelName")String labelName);
}