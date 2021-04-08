package com.fantechs.provider.om.service;


import com.fantechs.common.base.general.dto.om.*;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesOrderMaterialListDTO;
import com.fantechs.common.base.general.dto.om.imports.SmtOrderImport;
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
    //修改订单物料为排产
    int orderMaterialSchedule(Long orderMaterialId);
    //保存销售订单与产品相关
    int saveOrderMaterial(SaveOrderMaterialDTO saveOrderMaterialDTO);
    //根据销售订单找到产品相关信息
    List<MesOrderMaterialDTO> findOrderMaterial(SearchMesOrderMaterialListDTO searchMesOrderMaterialListDTO);

    List<SmtOrderReportDto> orderReport(SearchSmtOrderReportDto searchSmtOrderReportDto);

    //销售资源池
    List<FindOrderMaterialDto> findOrder();
    Map<String,Object> importExcel(List<SmtOrderImport> smtOrderImports);
}
