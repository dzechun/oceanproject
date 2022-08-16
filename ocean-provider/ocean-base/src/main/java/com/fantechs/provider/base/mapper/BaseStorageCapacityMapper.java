package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.BaseStorageCapacity;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface BaseStorageCapacityMapper extends MyMapper<BaseStorageCapacity> {
    List<BaseStorageCapacity> findList(Map<String, Object> map);

    BigDecimal totalQty(Map<String ,Object> map);

    BigDecimal putJobQty(Map<String,Object> map);

    List<WmsInnerInventory> wmsList(Map<String,Object> map);
}