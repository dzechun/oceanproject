package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerHtJobOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerHtJobOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerHtJobOrderMapper extends MyMapper<WmsInnerHtJobOrder> {

    List<WmsInnerHtJobOrderDto> findList(Map<String, Object> map);
}