package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryLogDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryLog;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerInventoryLogMapper extends MyMapper<WmsInnerInventoryLog> {
    List<WmsInnerInventoryLogDto> findList(Map<String,Object>map);

    BigDecimal findInv(Map<String ,Object> map);

    String findInvName(@Param("inventoryStatusId")Long inventoryStatusId);
}