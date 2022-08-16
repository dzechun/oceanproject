package com.fantechs.provider.om.mapper;

import com.fantechs.common.base.general.dto.om.OmPurchaseOrderDto;
import com.fantechs.common.base.general.entity.om.OmPurchaseOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OmPurchaseOrderMapper extends MyMapper<OmPurchaseOrder> {
    List<OmPurchaseOrderDto> findList(Map<String, Object> map);

    String findPurchaseMaterial(@Param("purchaseOrderCode")String purchaseOrderCode);
}