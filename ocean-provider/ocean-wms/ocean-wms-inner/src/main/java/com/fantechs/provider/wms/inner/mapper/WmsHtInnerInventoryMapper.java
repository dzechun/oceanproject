package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.entity.wms.inner.history.WmsHtInnerInventory;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsHtInnerInventoryMapper extends MyMapper<WmsHtInnerInventory> {
    List<WmsHtInnerInventory> findList(Map<String, Object> map);
}
