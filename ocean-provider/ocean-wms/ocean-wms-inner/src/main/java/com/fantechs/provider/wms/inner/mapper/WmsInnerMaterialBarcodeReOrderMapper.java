package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeReOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerMaterialBarcodeReOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerMaterialBarcodeReOrderMapper extends MyMapper<WmsInnerMaterialBarcodeReOrder> {
    List<WmsInnerMaterialBarcodeReOrderDto> findList(Map<String, Object> map);
}
