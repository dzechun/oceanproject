package com.fantechs.provider.om.mapper;

import com.fantechs.common.base.general.dto.om.MesOrderMaterialDTO;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesOrderMaterialListDTO;
import com.fantechs.common.base.general.dto.om.SearchSmtOrderReportDto;
import com.fantechs.common.base.general.dto.om.SmtOrderDto;
import com.fantechs.common.base.general.dto.om.SmtOrderReportDto;
import com.fantechs.common.base.general.entity.om.MesOrderMaterial;
import com.fantechs.common.base.general.entity.om.SmtOrder;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;


public interface SmtOrderMapper extends MyMapper<SmtOrder> {

    List<SmtOrderDto> findList(Map<String,Object> parm);
    int batchAddOrderMaterial(List<MesOrderMaterial> mesOrderMaterialList);
    int deleteMaterialByOrderId(long orderId);
    int orderMaterialSchedule(Long orderMaterialId);
    //根据销售订单找到产品相关信息
    List<MesOrderMaterialDTO> findOrderMaterial(SearchMesOrderMaterialListDTO searchMesOrderMaterialListDTO);

    //销售情况报表
    List<SmtOrderReportDto> orderReport(SearchSmtOrderReportDto searchSmtOrderReportDto);
}