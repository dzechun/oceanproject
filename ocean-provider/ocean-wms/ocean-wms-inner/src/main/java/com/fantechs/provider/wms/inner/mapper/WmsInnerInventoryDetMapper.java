package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.wms.inner.InStorageMaterialDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerInventoryDetMapper extends MyMapper<WmsInnerInventoryDet> {
    List<WmsInnerInventoryDetDto> findList(Map<String,Object> map);

    /**
     * 20220104
     * 批量移位统计库存明细按物料分组
     * @param map
     * @return
     */
    List<InStorageMaterialDto> findInventoryDetByStorage(Map<String, Object> map);

    int batchUpdateByPartField(List<WmsInnerInventoryDet> list);
}