package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.mes.sfc.LabelRuteDto;
import com.fantechs.common.base.general.dto.mes.sfc.PrintModel;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerMaterialBarcode;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerMaterialBarcode;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WmsInnerMaterialBarcodeMapper extends MyMapper<WmsInnerMaterialBarcode> {
    List<WmsInnerMaterialBarcodeDto> findList(SearchWmsInnerMaterialBarcode searchWmsInnerMaterialBarcode);

    int batchUpdate(List<WmsInnerMaterialBarcodeDto> list);

    PrintModel findPrintModel(@Param("id")Long id);

    LabelRuteDto findRule(@Param("code")String code, @Param("materialId")Long materialId , @Param("orgId")Long orgId);

}