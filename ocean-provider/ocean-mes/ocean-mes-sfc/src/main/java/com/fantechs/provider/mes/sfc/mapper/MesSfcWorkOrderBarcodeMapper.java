package com.fantechs.provider.mes.sfc.mapper;

import com.fantechs.common.base.general.dto.mes.sfc.PrintModel;
import com.fantechs.common.base.general.dto.mes.sfc.LabelRuteDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderBarcodeDto;
import com.fantechs.common.base.general.entity.basic.BaseLabel;
import com.fantechs.common.base.general.entity.basic.BaseLabelCategory;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.mes.sfc.SearchMesSfcWorkOrderBarcode;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MesSfcWorkOrderBarcodeMapper extends MyMapper<MesSfcWorkOrderBarcode> {
    List<MesSfcWorkOrderBarcodeDto> findList(SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode);

    String findMaxCode(@Param("labelCategoryId") Long labelCategoryId, @Param("workOrderId") Long workOrderId);

    LabelRuteDto findRule(@Param("code") String code, @Param("workOrderId") Long workOrderId);

    LabelRuteDto DefaultLabel(@Param("labelCategoryCode") String labelCategoryCode);

    PrintModel findPrintModel(@Param("labelCategoryId") Long labelCategoryId, @Param("id") Long id);

    String findByOneLabel(@Param("labelName") String labelName);

    Integer findCountCode(@Param("labelCategoryId") Long labelCategoryId, @Param("workOrderId") Long workOrderId);

    String findVersion(@Param("labelName") String labelName);
}