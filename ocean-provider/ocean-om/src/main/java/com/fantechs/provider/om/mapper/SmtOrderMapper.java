package com.fantechs.provider.om.mapper;

import com.fantechs.common.base.general.dto.om.MesOrderMaterialDTO;
import com.fantechs.common.base.general.dto.mes.pm.SearchMesOrderMaterialListDTO;
import com.fantechs.common.base.general.dto.om.SmtOrderDto;
import com.fantechs.common.base.general.entity.om.MesOrderMaterial;
import com.fantechs.common.base.general.entity.om.SmtOrder;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;


public interface SmtOrderMapper extends MyMapper<SmtOrder> {

    List<SmtOrderDto> findList(Map<String,Object> parm);
    int batchAddOrderMaterial(List<MesOrderMaterial> mesOrderMaterialList);
    int deleteMaterialByOrderId(long orderId);
    //根据销售订单找到产品相关信息
    List<MesOrderMaterialDTO> findOrderMaterial(SearchMesOrderMaterialListDTO searchMesOrderMaterialListDTO);
}