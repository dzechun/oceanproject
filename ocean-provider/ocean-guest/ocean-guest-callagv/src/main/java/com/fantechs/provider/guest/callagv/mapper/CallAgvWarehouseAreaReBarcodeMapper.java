package com.fantechs.provider.guest.callagv.mapper;

import com.fantechs.common.base.general.dto.callagv.CallAgvWarehouseAreaReBarcodeDto;
import com.fantechs.common.base.general.entity.callagv.CallAgvWarehouseAreaReBarcode;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CallAgvWarehouseAreaReBarcodeMapper extends MyMapper<CallAgvWarehouseAreaReBarcode> {
    List<CallAgvWarehouseAreaReBarcodeDto> findList(Map<String, Object> map);
}