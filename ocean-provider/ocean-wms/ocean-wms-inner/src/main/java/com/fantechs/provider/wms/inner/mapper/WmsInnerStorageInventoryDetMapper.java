package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStorageInventoryDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStorageInventoryDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerStorageInventoryDetMapper extends MyMapper<WmsInnerStorageInventoryDet> {

    List<WmsInnerStorageInventoryDetDto> findList(Map<String, Object> map);

    List<WmsInnerStorageInventoryDetDto> findById(Long storageInventoryId);

}
