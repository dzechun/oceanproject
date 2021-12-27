package com.fantechs.provider.wms.out.mapper;

import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryReqOrderDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryReqOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsOutDeliveryReqOrderDetMapper extends MyMapper<WmsOutDeliveryReqOrderDet> {
    List<WmsOutDeliveryReqOrderDetDto> findList(Map<String, Object> map);

    int batchUpdate(List<WmsOutDeliveryReqOrderDetDto> wmsOutDeliveryReqOrderDetDtos);
}