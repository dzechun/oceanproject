package com.fantechs.provider.wms.out.mapper;

import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface WmsOutDeliveryOrderMapper extends MyMapper<WmsOutDeliveryOrder> {
    List<WmsOutDeliveryOrderDto> findList(Map<String, Object> map);

    /**
     * 批量修改审核状态为1
     * @param ids
     * @return
     */
    int batchUpdateStatus(List<Long> ids);

    BigDecimal totalInventoryQty(Map<String ,Object> map);
}