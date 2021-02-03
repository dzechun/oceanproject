package com.fantechs.provider.exhibition.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.agv.dto.MaterialAndPositionCodeEnum;
import com.fantechs.common.base.agv.dto.PositionCodePath;
import com.fantechs.common.base.agv.dto.RcsResponseDTO;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.*;
import com.fantechs.common.base.general.dto.mes.pm.search.*;
import com.fantechs.common.base.general.dto.om.SmtOrderDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtStock;
import com.fantechs.common.base.general.entity.mes.pm.SmtStockDet;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderCardCollocation;
import com.fantechs.common.base.general.entity.om.SmtOrder;
import com.fantechs.common.base.response.MQResponseEntity;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.agv.AgvFeignApi;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.qms.OMFeignApi;
import com.fantechs.provider.exhibition.config.RabbitConfig;
import com.fantechs.provider.exhibition.service.ExhibitionClientService;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @Resource
    private AgvFeignApi agvFeignApi;
    @Resource
    private FanoutSender fanoutSender;

    @Override
    public int makingOrders(Long orderId) throws Exception{
        //查找所有创建状态的订单
//        SearchSmtOrder searchSmtOrder = new SearchSmtOrder();
//        searchSmtOrder.setStatus((byte) 0);
//        List<SmtOrderDto> orderList = omFeignApi.findOrderList(searchSmtOrder).getData();
        SmtOrder smtOrder = omFeignApi.detailSmtOrder(orderId).getData();
        //订单转工单生成备料单、产品BOM、备料明细
//        for (SmtOrderDto smtOrderDto : orderList) {
            SmtWorkOrder smtWorkOrder = new SmtWorkOrder();
            smtWorkOrder.setWorkOrderCode(CodeUtils.getId("WORK"));
            smtWorkOrder.setOrderId(smtOrder.getOrderId());
            smtWorkOrder.setMaterialId(smtOrder.getMaterialId());
            smtWorkOrder.setWorkOrderQuantity(new BigDecimal(smtOrder.getOrderQuantity()));
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

            SearchSmtStock searchSmtStock = new SearchSmtStock();
            searchSmtStock.setWorkOrderCode(smtWorkOrderDto.getWorkOrderCode());
            List<SmtStockDto> smtStockDtos=  pmFeignApi.findSmtStockList(searchSmtStock).getData();
            if(StringUtils.isEmpty(smtStockDtos)){
                throw new BizErrorException("该备料单不存在");
            }
            //通知AGV去上料
            String taskCode = agvStockTask(smtStockDtos.get(0).getStockId(), 1);
            log.info("通知AGV去上料 : " + ", stockId : " + smtStockDtos.get(0).getStockId() + ", taskCode : " + taskCode);

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
            if(StringUtils.isEmpty(smtWorkOrderBarcodePoolDtoList)){
                throw new BizErrorException("未找到对应产品条码流转卡");
            }

            for(SmtWorkOrderBarcodePoolDto smtWorkOrderBarcodePoolDto:smtWorkOrderBarcodePoolDtoList){
                //通过产品流转卡生成过站明细
                pmFeignApi.startJob(smtWorkOrderBarcodePoolDto);
                //获取过站工序明细发送客户端
                SearchSmtProcessListProcess searchSmtProcessListProcess = new SearchSmtProcessListProcess();
                searchSmtProcessListProcess.setWorkOrderCardPoolId(smtWorkOrderBarcodePoolDto.getWorkOrderCardPoolId());
                List<SmtProcessListProcessDto>  smtProcessListProcessDtoList=pmFeignApi.findSmtProcessListProcessList( searchSmtProcessListProcess).getData();
                MQResponseEntity mQResponseEntity = new MQResponseEntity<>();
                mQResponseEntity.setCode(1100);
                mQResponseEntity.setData(smtProcessListProcessDtoList);
                fanoutSender.send(RabbitConfig.TOPIC_PROCESS_LIST_QUEUE, JSONObject.toJSONString(mQResponseEntity));
                log.info("发送过站信息给客户端：" + JSONObject.toJSONString(mQResponseEntity));
            }
//        }

        return 1;
    }

    @Override
    public String agvStockTask(Long stockId, Integer type) throws Exception{

        String taskCode = "";

        // 查询未配送的物料/托盘
        SearchSmtStockDet searchSmtStockDet = new SearchSmtStockDet();
        searchSmtStockDet.setStockId(stockId);
        if (type == 1) {
            searchSmtStockDet.setStatus((byte) 0);
        } else {
            searchSmtStockDet.setStatus((byte) 2);
        }
        List<SmtStockDetDto> smtStockDetDtoList = pmFeignApi.findSmtStockDetList(searchSmtStockDet).getData();
        if (StringUtils.isNotEmpty(smtStockDetDtoList)) {

            if (smtStockDetDtoList.size() == 7 || type == 1) {
                // 发送图片、镭雕信息到客户端
                SearchSmtStock searchSmtStock = new SearchSmtStock();
                searchSmtStock.setStockId(stockId);
                List<SmtStockDto> smtStockDtoList = pmFeignApi.findSmtStockList(searchSmtStock).getData();
                MQResponseEntity mQResponseEntity = new MQResponseEntity<>();
                mQResponseEntity.setCode(1009);
                mQResponseEntity.setData(smtStockDtoList.get(0));
                fanoutSender.send(RabbitConfig.TOPIC_IMAGE_QUEUE, JSONObject.toJSONString(mQResponseEntity));
                log.info("发送图片、镭雕信息到客户端：" + JSONObject.toJSONString(mQResponseEntity));

                SmtStock smtStock = new SmtStock();
                BeanUtils.autoFillEqFields(smtStockDtoList.get(0), smtStock);
                smtStock.setStatus((byte) 1);
                pmFeignApi.updateSmtStock(smtStock);
            }

            String startPositionCode = "";
            String endPositionCode = "";
            for(MaterialAndPositionCodeEnum.MaterialAndPositionCode materialAndPositionCode : MaterialAndPositionCodeEnum.MaterialAndPositionCode.values()){
                if(smtStockDetDtoList.get(0).getMaterialCode().equals(materialAndPositionCode.getMaterialCode())) {
                    if (type == 1) {
                        startPositionCode = materialAndPositionCode.getStartPositionCode();
                        endPositionCode = materialAndPositionCode.getEndPositionCode();
                    } else {
                        startPositionCode = materialAndPositionCode.getEndPositionCode();
                        endPositionCode = materialAndPositionCode.getStartPositionCode();
                    }
                    break;
                }
            }

            Map<String, Object> map = new HashMap<>();
            if (type == 1) {
                map.put("taskTyp", "ZT04");
            } else {
                map.put("taskTyp", "ZT03");
            }
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
            String result = agvFeignApi.genAgvSchedulingTask(map).getData();
            RcsResponseDTO rcsResponseDTO = BeanUtils.convertJson(result, new TypeToken<RcsResponseDTO>(){}.getType());
            taskCode = rcsResponseDTO.getData();
            log.info("启动AGV配送任务：请求参数：" + JSONObject.toJSONString(map) + "， 返回结果：" + JSONObject.toJSONString(rcsResponseDTO));

            SmtStockDet smtStockDet = smtStockDetDtoList.get(0);
            if (type == 1) {
                smtStockDet.setStatus((byte) 1);
            } else {
                smtStockDet.setStatus((byte) 3);
            }
            smtStockDet.setRemark(taskCode);
            pmFeignApi.updateSmtStockDet(smtStockDet);
        }

        return taskCode;
    }

    @Override
    public String agvStockTaskTest(String materialCode) throws Exception{
        String startPositionCode = "";
        String endPositionCode = "";
        for (MaterialAndPositionCodeEnum.MaterialAndPositionCode materialAndPositionCode : MaterialAndPositionCodeEnum.MaterialAndPositionCode.values()) {
            if (materialCode.equals(materialAndPositionCode.getMaterialCode())) {
                startPositionCode = materialAndPositionCode.getStartPositionCode();
                endPositionCode = materialAndPositionCode.getEndPositionCode();
            }
            break;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("taskTyp", "ZT04");
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
        String result = agvFeignApi.genAgvSchedulingTask(map).getData();
        RcsResponseDTO rcsResponseDTO = BeanUtils.convertJson(result, new TypeToken<RcsResponseDTO>(){}.getType());
        String taskCode = rcsResponseDTO.getData();
        log.info("启动AGV配送任务：请求参数：" + JSONObject.toJSONString(map) + "， 返回结果：" + JSONObject.toJSONString(rcsResponseDTO));

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
