package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerJobOrderMapper extends MyMapper<WmsInnerJobOrder> {
    List<WmsInnerJobOrderDto> findList(SearchWmsInnerJobOrder searchWmsInnerJobOrder);

    Long findStorage(@Param("materialId")Long materialId,@Param("warehouseId")Long warehouseId);

    Long SelectStorage();

    String findStorageName(@Param("storageId")Long storageId);

    String findWarehouseName(@Param("warehouseId")Long warehouseId);

    String findAsnCode(@Param("asnOrderId")Long asnOrderId);

    Long findOmWarehouseId(@Param("sourceId")Long sourceId);

    /**
     * 移位单查询
     * @param map
     * @return
     */
    List<WmsInnerJobOrderDto> findShiftList(Map<String, Object> map);
}