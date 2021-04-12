package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStorageInventoryDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStorageInventory;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @ClassName SmtStorageInventoryMapper
 * @Date 2020/12/2 18:18
 */
@Mapper
public interface WmsInnerStorageInventoryMapper extends MyMapper<WmsInnerStorageInventory> {

    List<WmsInnerStorageInventoryDto> findList(Map<String, Object> map);

    int refreshQuantity(Map<String, Object> map);

}
