package com.fantechs.provider.imes.storage.mapper;

import com.fantechs.common.base.dto.storage.SmtStorageInventoryDto;
import com.fantechs.common.base.entity.storage.SmtStorageInventory;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @ClassName SmtStorageInventoryMapper
 * @Date 2020/12/2 18:18
 */
@Mapper
public interface SmtStorageInventoryMapper extends MyMapper<SmtStorageInventory> {

    List<SmtStorageInventoryDto> findList(Map<String, Object> map);

    int refreshQuantity(Map<String, Object> map);

}
