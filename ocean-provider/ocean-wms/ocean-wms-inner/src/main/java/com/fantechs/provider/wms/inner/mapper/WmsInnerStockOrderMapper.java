package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.wms.inner.WmsInventoryVerificationDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInventoryVerification;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInventoryVerification;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerStockOrderMapper extends MyMapper<WmsInventoryVerification> {
    List<WmsInventoryVerificationDto> findList(Map<String,Object> map);
}