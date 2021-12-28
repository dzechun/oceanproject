package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStockOrderDetBarcodeDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrderDetBarcode;
import com.fantechs.common.base.mybatis.MyMapper;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerStockOrderDetBarcodeMapper extends MyMapper<WmsInnerStockOrderDetBarcode> {
    List<WmsInnerStockOrderDetBarcodeDto> findList(Map<String,Object> map);
}