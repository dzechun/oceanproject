package com.fantechs.provider.bcm.mapper;

import com.fantechs.common.base.general.dto.bcm.LabelRuteDto;
import com.fantechs.common.base.general.dto.bcm.MesSfcWorkOrderBarcodeDto;
import com.fantechs.common.base.general.dto.bcm.PrintModel;
import com.fantechs.common.base.general.entity.bcm.MesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.bcm.search.SearchMesSfcWorkOrderBarcode;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesSfcWorkOrderBarcodeMapper extends MyMapper<MesSfcWorkOrderBarcode> {
    List<MesSfcWorkOrderBarcodeDto> findList(SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode);

    String findMaxCode(@Param("barcodeType")Byte barcodeType,@Param("workOrderId")Long workOrderId);

    LabelRuteDto findRule(@Param("code")String code,@Param("workOrderId")Long workOrderId);

    LabelRuteDto DefaultLabel(@Param("code")String code);

    PrintModel findPrintModel(@Param("viewName")String viewName);
}