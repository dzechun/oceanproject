package com.fantechs.provider.guest.callagv.mapper;

import com.fantechs.common.base.general.dto.callagv.CallAgvWarehouseAreaBarcodeLogDto;
import com.fantechs.common.base.general.entity.callagv.CallAgvWarehouseAreaBarcodeLog;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CallAgvWarehouseAreaBarcodeLogMapper extends MyMapper<CallAgvWarehouseAreaBarcodeLog> {
    List<CallAgvWarehouseAreaBarcodeLogDto> findList(Map<String, Object> map);
}