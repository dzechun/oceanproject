package com.fantechs.mapper;

import com.fantechs.common.base.mybatis.MyMapper;
import com.fantechs.dto.StorageMonthEndInventoryDto;
import com.fantechs.entity.StorageMonthEndInventory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface StorageMonthEndInventoryMapper extends MyMapper<StorageMonthEndInventory> {

    List<StorageMonthEndInventoryDto> findMonthEndList(Map<String, Object> map);

    List<StorageMonthEndInventoryDto> findList(Map<String, Object> map);
}
