package com.fantechs.provider.exhibition.service.impl;

import com.fantechs.common.base.dto.apply.SmtOrderDto;
import com.fantechs.common.base.dto.apply.SmtWorkOrderDto;
import com.fantechs.common.base.entity.apply.SmtWorkOrder;
import com.fantechs.common.base.entity.apply.SmtWorkOrderCardCollocation;
import com.fantechs.common.base.entity.apply.search.SearchSmtOrder;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.imes.apply.ApplyFeignApi;
import com.fantechs.provider.exhibition.service.ExhibitionClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ExhibitionClientServiceImpl implements ExhibitionClientService {
    @Autowired
    private ApplyFeignApi applyFeignApi;

    @Override
    public int makingOrders() {
        //查找所有创建状态的订单
        SearchSmtOrder searchSmtOrder = new SearchSmtOrder();
        searchSmtOrder.setStatus((byte) 0);
        List<SmtOrderDto> orderList = applyFeignApi.findOrderList(searchSmtOrder).getData();
        //订单转工单生成备料单、产品BOM、备料明细
        for (SmtOrderDto smtOrderDto : orderList) {
            SmtWorkOrder smtWorkOrder = new SmtWorkOrder();
            smtWorkOrder.setWorkOrderCode(CodeUtils.getId("Work-Order-"));
            smtWorkOrder.setMaterialId(smtOrderDto.getMaterialId());
            smtWorkOrder.setWorkOrderQuantity(smtOrderDto.getOrderQuantity());
            smtWorkOrder.setBarcodeRuleSetId(1l);
            smtWorkOrder.setProLineId(1l);
            smtWorkOrder.setRouteId(1l);
            smtWorkOrder.setPlannedStartTime(new Date());
            smtWorkOrder.setPlannedEndTime(new Date());
            ResponseEntity responseEntity =  applyFeignApi.addWorkOrder(smtWorkOrder);

            //查询工单信息
            SearchSmtWorkOrder searchSmtWorkOrder = new SearchSmtWorkOrder();
            searchSmtWorkOrder.setWorkOrderCode(smtWorkOrder.getWorkOrderCode());
            List<SmtWorkOrderDto> smtWorkOrderDtoList = applyFeignApi.findWorkOrderList(searchSmtWorkOrder).getData();
            if (StringUtils.isEmpty(smtWorkOrderDtoList)) {

            }
            //根据工单信息生成工单流转卡、产品流转卡
            SmtWorkOrderDto smtWorkOrderDto = smtWorkOrderDtoList.get(0);
            SmtWorkOrderCardCollocation smtWorkOrderCardCollocation = new SmtWorkOrderCardCollocation();
            smtWorkOrderCardCollocation.setWorkOrderId(smtWorkOrderDto.getWorkOrderId());
            smtWorkOrderCardCollocation.setProduceQuantity(smtWorkOrderDto.getWorkOrderQuantity());
            applyFeignApi.generateWorkOrderCardCollocation(smtWorkOrderCardCollocation);
        }

        return 1;
    }
}
