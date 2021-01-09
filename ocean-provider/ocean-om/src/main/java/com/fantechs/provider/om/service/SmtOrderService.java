package com.fantechs.provider.om.service;


import com.fantechs.common.base.general.dto.om.MesOrderMaterialDTO;
import com.fantechs.common.base.general.dto.om.SaveOrderMaterialDTO;
import com.fantechs.common.base.dto.apply.SearchMesOrderMaterialListDTO;
import com.fantechs.common.base.general.dto.om.SmtOrderDto;
import com.fantechs.common.base.general.entity.om.SmtOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/10/13.
 */

public interface SmtOrderService extends IService<SmtOrder> {

    List<SmtOrderDto> findList(Map<String,Object> parm);

    //保存销售订单与产品相关
    int saveOrderMaterial(SaveOrderMaterialDTO saveOrderMaterialDTO);
    //根据销售订单找到产品相关信息
    List<MesOrderMaterialDTO> findOrderMaterial(SearchMesOrderMaterialListDTO searchMesOrderMaterialListDTO);
}