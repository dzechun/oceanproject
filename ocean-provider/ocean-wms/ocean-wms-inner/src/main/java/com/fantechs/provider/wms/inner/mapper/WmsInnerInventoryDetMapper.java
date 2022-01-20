package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerInventoryDetMapper extends MyMapper<WmsInnerInventoryDet> {
    List<WmsInnerInventoryDetDto> findList(Map<String,Object> map);

    int updateStroage(@Param("inventoryDetList") List<WmsInnerInventoryDet> inventoryDetList);

    Integer materialQty(Map<String,Object> map);
}
