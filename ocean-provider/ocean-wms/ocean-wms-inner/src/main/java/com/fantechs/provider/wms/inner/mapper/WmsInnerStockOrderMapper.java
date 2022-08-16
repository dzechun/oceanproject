package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStockOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerStockOrderMapper extends MyMapper<WmsInnerStockOrder> {
    List<WmsInnerStockOrderDto> findList(Map<String,Object> map);

    String findStorageName(@Param("storageId")Long storageId);

    String findWarehouseName(@Param("warehouseId")Long warehouseId);

    Long findMaterialId(@Param("materialCode")String materialCode);
}