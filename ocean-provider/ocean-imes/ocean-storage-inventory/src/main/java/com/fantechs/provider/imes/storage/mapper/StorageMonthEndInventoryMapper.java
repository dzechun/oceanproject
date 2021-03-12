package com.fantechs.provider.imes.storage.mapper;

import com.fantechs.common.base.dto.storage.StorageMonthEndInventoryDto;
import com.fantechs.common.base.entity.storage.StorageMonthEndInventory;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface StorageMonthEndInventoryMapper extends MyMapper<StorageMonthEndInventory> {

    List<StorageMonthEndInventoryDto> findMonthEndList(Map<String, Object> map);

    List<StorageMonthEndInventoryDto> findList(Map<String, Object> map);
}
