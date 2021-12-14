package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.entity.wms.inner.history.WmsInnerHtMaterialBarcodeReOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerHtMaterialBarcodeReOrderMapper extends MyMapper<WmsInnerHtMaterialBarcodeReOrder> {
    List<WmsInnerHtMaterialBarcodeReOrder> findList(Map<String, Object> map);
}
