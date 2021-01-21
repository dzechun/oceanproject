package com.fantechs.provider.exhibition.service.impl;

import com.fantechs.common.base.entity.basic.SmtRouteProcess;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderBarcodePoolDto;
import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtOrder;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrder;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderBarcodePool;
import com.fantechs.common.base.general.dto.om.SmtOrderDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtProcessListProcess;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderCardCollocation;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.qms.OMFeignApi;
import com.fantechs.provider.exhibition.service.ExhibitionClientService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class ExhibitionClientServiceImpl implements ExhibitionClientService {
    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private OMFeignApi omFeignApi;
    @Resource
    private BasicFeignApi basicFeignApi;

    @Override
    public int makingOrders() {
        //查找所有创建状态的订单
        SearchSmtOrder searchSmtOrder = new SearchSmtOrder();
        searchSmtOrder.setStatus((byte) 0);
        List<SmtOrderDto> orderList = omFeignApi.findOrderList(searchSmtOrder).getData();
        //订单转工单生成备料单、产品BOM、备料明细
        for (SmtOrderDto smtOrderDto : orderList) {
            SmtWorkOrder smtWorkOrder = new SmtWorkOrder();
            smtWorkOrder.setWorkOrderCode(CodeUtils.getId("WORK"));
            smtWorkOrder.setOrderId(smtOrderDto.getOrderId());
            smtWorkOrder.setMaterialId(smtOrderDto.getMaterialId());
            smtWorkOrder.setWorkOrderQuantity(new BigDecimal(smtOrderDto.getOrderQuantity()));
            smtWorkOrder.setBarcodeRuleSetId(17l);
            smtWorkOrder.setProLineId(1l);
            smtWorkOrder.setRouteId(37l);
            smtWorkOrder.setPlannedStartTime(new Date());
            smtWorkOrder.setPlannedEndTime(new Date());
            ResponseEntity responseEntity =  pmFeignApi.addWorkOrder(smtWorkOrder);

            //查询工单信息
            SearchSmtWorkOrder searchSmtWorkOrder = new SearchSmtWorkOrder();
            searchSmtWorkOrder.setWorkOrderCode(smtWorkOrder.getWorkOrderCode());
            List<SmtWorkOrderDto> smtWorkOrderDtoList = pmFeignApi.findWorkOrderList(searchSmtWorkOrder).getData();

            if(StringUtils.isEmpty(smtWorkOrderDtoList)){
                throw new BizErrorException("该工单不存在");
            }
            //根据工单信息生成工单流转卡、产品流转卡
            SmtWorkOrderDto smtWorkOrderDto = smtWorkOrderDtoList.get(0);
            SmtWorkOrderCardCollocation smtWorkOrderCardCollocation = new SmtWorkOrderCardCollocation();
            smtWorkOrderCardCollocation.setWorkOrderId(smtWorkOrderDto.getWorkOrderId());
            smtWorkOrderCardCollocation.setProduceQuantity(smtWorkOrderDto.getWorkOrderQuantity().intValue());
            pmFeignApi.generateWorkOrderCardCollocation(smtWorkOrderCardCollocation);

            //根据条码流转卡生产过站明细
            SearchSmtWorkOrderBarcodePool searchSmtWorkOrderBarcodePool = new SearchSmtWorkOrderBarcodePool();
            searchSmtWorkOrderBarcodePool.setWorkOrderId(smtWorkOrderDto.getWorkOrderId());
            searchSmtWorkOrderBarcodePool.setTaskStatus((byte)0);
            searchSmtWorkOrderBarcodePool.setPageSize(99999);
            List<SmtWorkOrderBarcodePoolDto>  smtWorkOrderBarcodePoolDtoList=pmFeignApi.findWorkOrderBarcodePoolList(searchSmtWorkOrderBarcodePool).getData();
            for(SmtWorkOrderBarcodePoolDto smtWorkOrderBarcodePoolDto:smtWorkOrderBarcodePoolDtoList){
                pmFeignApi.startJob(smtWorkOrderBarcodePoolDto);
            }
        }

        return 1;
    }
}
