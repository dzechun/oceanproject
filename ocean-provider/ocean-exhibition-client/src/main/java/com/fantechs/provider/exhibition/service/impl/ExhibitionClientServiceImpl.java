package com.fantechs.provider.exhibition.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.agv.dto.MaterialAndPositionCodeEnum;
import com.fantechs.common.base.agv.dto.PositionCodePath;
import com.fantechs.common.base.entity.basic.SmtRouteProcess;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.SmtStockDetDto;
import com.fantechs.common.base.general.dto.mes.pm.SmtStockDto;
import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderBarcodePoolDto;
import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.pm.search.*;
import com.fantechs.common.base.general.dto.om.SmtOrderDto;
import com.fantechs.common.base.general.entity.mes.pm.*;
import com.fantechs.common.base.response.MQResponseEntity;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.agv.AgvFeignApi;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.exhibition.config.RabbitConfig;
import com.fantechs.provider.api.qms.OMFeignApi;
import com.fantechs.provider.exhibition.service.ExhibitionClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ExhibitionClientServiceImpl implements ExhibitionClientService {

    private static final Logger log = LoggerFactory.getLogger(ExhibitionClientServiceImpl.class);

    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private OMFeignApi omFeignApi;
    @Resource
    private BasicFeignApi basicFeignApi;
    @Autowired
    private AgvFeignApi agvFeignApi;
    @Autowired
    private FanoutSender fanoutSender;

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

    @Override
    public String agvStockTask(Long stockId) {

        String taskCode = "";

        // 查询未配送的物料
        SearchSmtStockDet searchSmtStockDet = new SearchSmtStockDet();
        searchSmtStockDet.setStockId(stockId);
        searchSmtStockDet.setStatus((byte) 0);
        List<SmtStockDetDto> smtStockDetDtoList = pmFeignApi.findSmtStockDetList(searchSmtStockDet).getData();
        if (StringUtils.isNotEmpty(smtStockDetDtoList)) {

            if (smtStockDetDtoList.size() == 7) {
                // 发送图片、镭雕信息到客户端
                SearchSmtStock searchSmtStock = new SearchSmtStock();
                searchSmtStock.setStockId(stockId);
                List<SmtStockDto> smtStockDtoList = pmFeignApi.findSmtStockList(searchSmtStock).getData();
                MQResponseEntity mQResponseEntity = new MQResponseEntity<>();
                mQResponseEntity.setCode(1009);
                mQResponseEntity.setData(smtStockDtoList.get(0));
                fanoutSender.send(RabbitConfig.TOPIC_IMAGE_QUEUE, JSONObject.toJSONString(mQResponseEntity));
                log.info("发送图片、镭雕信息到客户端：" + JSONObject.toJSONString(mQResponseEntity));
            }

            String startPositionCode = "";
            String endPositionCode = "";
            for(MaterialAndPositionCodeEnum.MaterialAndPositionCode materialAndPositionCode : MaterialAndPositionCodeEnum.MaterialAndPositionCode.values()){
                if(smtStockDetDtoList.get(0).getMaterialCode().equals(materialAndPositionCode.getMaterialCode())) {
                    startPositionCode = materialAndPositionCode.getStartPositionCode();
                    endPositionCode = materialAndPositionCode.getEndPositionCode();
                }
            }

            Map<String, Object> map = new HashMap<>();
            map.put("taskTyp", "c02");
            List<PositionCodePath> positionCodePathList = new LinkedList<>();
            // 起始地标条码
            PositionCodePath positionCodePath = new PositionCodePath();
            positionCodePath.setPositionCode(startPositionCode);
            positionCodePath.setType("00");
            positionCodePathList.add(positionCodePath);
            // 目标位置条码
            PositionCodePath positionCodePath2 = new PositionCodePath();
            positionCodePath2.setPositionCode(endPositionCode);
            positionCodePath2.setType("00");
            positionCodePathList.add(positionCodePath2);
            map.put("positionCodePath", positionCodePathList);
            taskCode = agvFeignApi.genAgvSchedulingTask(map).getData();

            SmtStockDet smtStockDet = smtStockDetDtoList.get(0);
            smtStockDet.setStatus((byte) 1);
            smtStockDet.setRemark(taskCode);
            pmFeignApi.updateSmtStockDet(smtStockDet);
        }

        return taskCode;
    }

    @Override
    public String agvStockTaskTest(String materialCode) {
        String startPositionCode = "";
        String endPositionCode = "";
        for (MaterialAndPositionCodeEnum.MaterialAndPositionCode materialAndPositionCode : MaterialAndPositionCodeEnum.MaterialAndPositionCode.values()) {
            if (materialCode.equals(materialAndPositionCode.getMaterialCode())) {
                startPositionCode = materialAndPositionCode.getStartPositionCode();
                endPositionCode = materialAndPositionCode.getEndPositionCode();
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("taskTyp", "c02");
        List<PositionCodePath> positionCodePathList = new LinkedList<>();
        // 起始地标条码
        PositionCodePath positionCodePath = new PositionCodePath();
        positionCodePath.setPositionCode(startPositionCode);
        positionCodePath.setType("00");
        positionCodePathList.add(positionCodePath);
        // 目标位置条码
        PositionCodePath positionCodePath2 = new PositionCodePath();
        positionCodePath2.setPositionCode(endPositionCode);
        positionCodePath2.setType("00");
        positionCodePathList.add(positionCodePath2);
        map.put("positionCodePath", positionCodePathList);
        String taskCode = agvFeignApi.genAgvSchedulingTask(map).getData();

        return taskCode;
    }

    @Override
    public String agvContinueTask() {
        // agv继续执行任务
        Map<String, Object> map = new HashMap<>();
        map.put("agvCode", "2249");
        String result = agvFeignApi.continueTask(map).getData();

        return result;
    }

}
