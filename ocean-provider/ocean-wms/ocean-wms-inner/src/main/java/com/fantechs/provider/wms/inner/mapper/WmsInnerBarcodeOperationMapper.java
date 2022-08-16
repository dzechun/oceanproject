package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerBarcodeOperationDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerBarcodeOperation;
import com.fantechs.common.base.mybatis.MyMapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerBarcodeOperationMapper extends MyMapper<WmsInnerBarcodeOperation> {
    List<WmsInnerBarcodeOperationDto> findList(Map<String, Object> map);
}