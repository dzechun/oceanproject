package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInitStockBarcode;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerInitStockBarcodeMapper extends MyMapper<WmsInnerInitStockBarcode> {
    List<WmsInnerInitStockBarcode> findList(Map<String,Object> map);
}