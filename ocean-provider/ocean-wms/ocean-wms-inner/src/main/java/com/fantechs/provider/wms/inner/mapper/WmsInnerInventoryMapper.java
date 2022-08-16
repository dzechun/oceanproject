package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerInventoryMapper extends MyMapper<WmsInnerInventory> {
    List<WmsInnerInventoryDto> findList(Map<String, Object> map);

    int batchUpdate(List<WmsInnerInventory> list);
}
