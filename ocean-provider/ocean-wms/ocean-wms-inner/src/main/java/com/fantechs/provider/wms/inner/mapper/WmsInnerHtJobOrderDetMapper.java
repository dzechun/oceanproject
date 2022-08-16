package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerHtJobOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerHtJobOrderDetMapper extends MyMapper<WmsInnerHtJobOrderDet> {
//    List<WmsInnerJobOrderDetDto> findList(Map<String, Object> map);
}