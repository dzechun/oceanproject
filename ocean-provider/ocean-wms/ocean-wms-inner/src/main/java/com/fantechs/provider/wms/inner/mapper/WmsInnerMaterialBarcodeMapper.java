package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.mes.sfc.LabelRuteDto;
import com.fantechs.common.base.general.dto.mes.sfc.PrintModel;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerMaterialBarcode;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerMaterialBarcode;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerMaterialBarcodeMapper extends MyMapper<WmsInnerMaterialBarcode> {
    List<WmsInnerMaterialBarcodeDto> findList(Map<String,Object> map);

    PrintModel findPrintModel(@Param("id")Long id,@Param("labelCode")String labelCode);

    LabelRuteDto findRule(@Param("code")String code, @Param("materialId")Long materialId , @Param("orgId")Long orgId);

    //获取已打印物料总数量
    Integer getTotalMaterialQty(SearchWmsInnerMaterialBarcode searchWmsInnerMaterialBarcode);

}
