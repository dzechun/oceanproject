package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInitStockDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInitStock;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerInitStockMapper extends MyMapper<WmsInnerInitStock> {
    List<WmsInnerInitStockDto> findList(Map<String,Object> map);
}