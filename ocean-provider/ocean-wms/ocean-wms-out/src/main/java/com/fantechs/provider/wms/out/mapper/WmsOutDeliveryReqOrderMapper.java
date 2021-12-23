package com.fantechs.provider.wms.out.mapper;

import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryReqOrderDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryReqOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsOutDeliveryReqOrderMapper extends MyMapper<WmsOutDeliveryReqOrder> {
    List<WmsOutDeliveryReqOrderDto> findList(Map<String, Object> map);
}