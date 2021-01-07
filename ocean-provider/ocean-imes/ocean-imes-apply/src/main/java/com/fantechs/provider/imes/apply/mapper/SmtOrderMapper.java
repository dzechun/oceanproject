package com.fantechs.provider.imes.apply.mapper;

import com.fantechs.common.base.dto.apply.MesOrderMaterialDTO;
import com.fantechs.common.base.dto.apply.SmtOrderDto;
import com.fantechs.common.base.entity.apply.MesOrderMaterial;
import com.fantechs.common.base.entity.apply.SmtOrder;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;


public interface SmtOrderMapper extends MyMapper<SmtOrder> {

    List<SmtOrderDto> findList(Map<String,Object> parm);
    int batchAddOrderMaterial(List<MesOrderMaterial> mesOrderMaterialList);
    int deleteMaterialByOrderId(long orderId);
    //根据销售订单找到产品相关信息
    List<MesOrderMaterialDTO> findOrderMaterial(Long orderId);
}