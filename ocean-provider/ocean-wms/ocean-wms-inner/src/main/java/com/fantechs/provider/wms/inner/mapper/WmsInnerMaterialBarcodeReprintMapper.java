package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeReprintDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerMaterialBarcodeReprint;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerMaterialBarcodeReprintMapper extends MyMapper<WmsInnerMaterialBarcodeReprint> {
    List<WmsInnerMaterialBarcodeReprintDto> findList(Map<String, Object> map);
}
