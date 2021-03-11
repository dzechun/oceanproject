package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryScrapDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryScrap;
import com.fantechs.common.base.general.entity.wms.inner.history.WmsInnerHtInventoryScrap;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerInventoryScrapMapper extends MyMapper<WmsInnerInventoryScrap> {
    List<WmsInnerInventoryScrapDto> findList(Map<String, Object> map);
}