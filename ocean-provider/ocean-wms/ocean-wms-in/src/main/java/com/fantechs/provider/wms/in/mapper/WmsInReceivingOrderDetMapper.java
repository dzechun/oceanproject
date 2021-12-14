package com.fantechs.provider.wms.in.mapper;

import com.fantechs.common.base.general.dto.wms.in.WmsInReceivingOrderDetDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInReceivingOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInReceivingOrderDetMapper extends MyMapper<WmsInReceivingOrderDet> {
    List<WmsInReceivingOrderDetDto> findList(Map<String,Object> map);
}