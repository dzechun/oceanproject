package com.fantechs.provider.client.listener;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.dto.PtlElectronicTagStorageDto;
import com.fantechs.common.base.electronic.dto.PtlJobOrderDetDto;
import com.fantechs.common.base.electronic.dto.PtlJobOrderDto;
import com.fantechs.common.base.electronic.entity.PtlJobOrder;
import com.fantechs.common.base.electronic.entity.PtlJobOrderDet;
import com.fantechs.common.base.electronic.entity.search.SearchPtlElectronicTagStorage;
import com.fantechs.common.base.electronic.entity.search.SearchPtlJobOrder;
import com.fantechs.common.base.electronic.entity.search.SearchPtlJobOrderDet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.MQResponseEntity;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.RestTemplateUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.electronic.ElectronicTagFeignApi;
import com.fantechs.provider.client.config.RabbitConfig;
import com.fantechs.provider.client.dto.PtlJobOrderDTO;
import com.fantechs.provider.client.dto.RabbitMQDTO;
import com.fantechs.provider.client.dto.ResponseEntityDTO;
import com.fantechs.provider.client.server.ElectronicTagStorageService;
import com.fantechs.provider.client.server.impl.ElectronicTagStorageServiceImpl;
import com.fantechs.provider.client.server.impl.FanoutSender;
import com.google.gson.reflect.TypeToken;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Component
public class ElectronicTagReceiver {
    private static final Logger log = LoggerFactory.getLogger(ElectronicTagReceiver.class);
    @Autowired
    private FanoutSender fanoutSender;

    @Autowired
    private ElectronicTagFeignApi electronicTagFeignApi;

    @Autowired
    private ElectronicTagStorageService electronicTagStorageService;

    @Value("${wmsAPI.finishPtlJobOrderUrl}")
    private String finishPtlJobOrderUrl;

    // 监听标签队列
    @RabbitListener(queues = RabbitConfig.TOPIC_QUEUE1)
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public void receiveTopic1(byte[] bytes, Message message, Channel channel) throws Exception {
        String encoded = new String(bytes, "UTF-8");
        MQResponseEntity mqResponseEntity = JsonUtils.jsonToPojo(encoded, MQResponseEntity.class);
        log.info("接收到客户端消息：" + mqResponseEntity);
        RabbitMQDTO rabbitMQDTO1 = new RabbitMQDTO();
        //电子标签熄灭动作
        try {
            if (mqResponseEntity.getCode() == 106) {
                Map<String, Object> map = JsonUtils.jsonToMap(mqResponseEntity.getData().toString());
                String equipmentTagId = map.get("GwId").toString();
                String electronicTagId = map.get("TagNode").toString();
                BigInteger b = new BigInteger(electronicTagId).abs();

                String endElectronicTagId = b.toString();
                RabbitMQDTO endRabbitMQDTO = null;

                List<RabbitMQDTO> list = new LinkedList<>();
                List<RabbitMQDTO> rabbitMQDTOS = new LinkedList<>();
                rabbitMQDTO1.setEquipmentTagId(equipmentTagId);
                rabbitMQDTO1.setElectronicTagId(b.toString());
                rabbitMQDTOS.add(rabbitMQDTO1);
                //通过标签ID去找对应的任务单明细信息
                SearchPtlJobOrderDet searchPtlJobOrderDet = new SearchPtlJobOrderDet();
                searchPtlJobOrderDet.setElectronicTagId(b.toString());
                searchPtlJobOrderDet.setEquipmentTagId(equipmentTagId);
                searchPtlJobOrderDet.setJobStatus((byte) 2);
                List<PtlJobOrderDetDto> ptlJobOrderDetDtoList = electronicTagFeignApi.findPtlJobOrderDetList(searchPtlJobOrderDet).getData();
                RabbitMQDTO rabbitMQDTOArea = new RabbitMQDTO();
                if (StringUtils.isNotEmpty(ptlJobOrderDetDtoList)) {

                    PtlJobOrderDet ptlJobOrderDet = new PtlJobOrderDet();
                    ptlJobOrderDet.setJobOrderDetId(ptlJobOrderDetDtoList.get(0).getJobOrderDetId());
                    ptlJobOrderDet.setJobStatus((byte) 3);
                    ptlJobOrderDet.setModifiedTime(new Date());
                    electronicTagFeignApi.updatePtlJobOrderDet(ptlJobOrderDet);

                    SearchPtlElectronicTagStorage searchPtlElectronicTagStorage = new SearchPtlElectronicTagStorage();
                    searchPtlElectronicTagStorage.setStorageId(ptlJobOrderDetDtoList.get(0).getStorageId().toString());
                    List<PtlElectronicTagStorageDto> ptlElectronicTagStorageDtoList = electronicTagFeignApi.findElectronicTagStorageList(searchPtlElectronicTagStorage).getData();
                    if (StringUtils.isEmpty(ptlElectronicTagStorageDtoList)) {
                        throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "请先维护储位：" + ptlJobOrderDetDtoList.get(0).getStorageCode() +  "和物料：" + ptlJobOrderDetDtoList.get(0).getMaterialCode() + "以及对应的电子标签关联信息");
                    }
                    rabbitMQDTO1.setQueueName(ptlElectronicTagStorageDtoList.get(0).getQueueName());
                    // 该储位对应的另一个电子标签灭灯
                    for (PtlElectronicTagStorageDto ptlElectronicTagStorageDto : ptlElectronicTagStorageDtoList) {
                        if (!b.toString().equals(ptlElectronicTagStorageDto.getElectronicTagId())) {
                            RabbitMQDTO rabbitMQDTO = new RabbitMQDTO();
                            rabbitMQDTO.setEquipmentTagId(ptlElectronicTagStorageDto.getEquipmentTagId());
                            rabbitMQDTO.setElectronicTagId(ptlElectronicTagStorageDto.getElectronicTagId());
                            rabbitMQDTO.setQueueName(ptlElectronicTagStorageDto.getQueueName());
                            rabbitMQDTOS.add(rabbitMQDTO);

                            if (ptlElectronicTagStorageDto.getElectronicTagLangType() == 1) {
                                endElectronicTagId = ptlElectronicTagStorageDto.getElectronicTagId();
                            }
                        }
                    }

                    // 判断当前储位是否还有其他物料需要亮灯
                    SearchPtlJobOrderDet searchPtlJobOrderDet1 = new SearchPtlJobOrderDet();
                    searchPtlJobOrderDet1.setJobOrderId(ptlJobOrderDetDtoList.get(0).getJobOrderId());
                    searchPtlJobOrderDet1.setStorageId(ptlJobOrderDetDtoList.get(0).getStorageId());
                    searchPtlJobOrderDet1.setJobStatus((byte) 2);
                    searchPtlJobOrderDet1.setJobOrderDet(0);
                    searchPtlJobOrderDet1.setStartPage(1);
                    searchPtlJobOrderDet1.setPageSize(9999);
                    List<PtlJobOrderDetDto> ptlJobOrderDetDtoList1 = electronicTagFeignApi.findPtlJobOrderDetList(searchPtlJobOrderDet1).getData();
                    if (ptlJobOrderDetDtoList1.size() > 1) {
                        String materialDesc = "";
                        String materialDesc2 = "";
                        for (PtlJobOrderDetDto ptlJobOrderDetDto : ptlJobOrderDetDtoList1) {
                            if (!ptlJobOrderDetDtoList.get(0).getJobOrderDetId().equals(ptlJobOrderDetDto.getJobOrderDetId())) {
                                // 该储位对应的另一个物料亮灯
                                RabbitMQDTO rabbitMQDTO = new RabbitMQDTO();
                                rabbitMQDTO.setEquipmentTagId(ptlJobOrderDetDto.getEquipmentTagId());
                                rabbitMQDTO.setElectronicTagId(ptlJobOrderDetDto.getElectronicTagId());
                                rabbitMQDTO.setOption1("0");
                                rabbitMQDTO.setOption2("1");
                                rabbitMQDTO.setOption3("0");
                                rabbitMQDTO.setQueueName(ptlElectronicTagStorageDtoList.get(0).getQueueName());
                                if (ptlJobOrderDetDto.getElectronicTagLangType() == 1) {
                                    if (StringUtils.isNotEmpty(ptlJobOrderDetDto.getWholeQty()) && ptlJobOrderDetDto.getWholeQty().compareTo(BigDecimal.ZERO) != 0) {
                                        materialDesc += electronicTagStorageService.intercepting(ptlJobOrderDetDto.getMaterialName() + " ", 24 - ptlJobOrderDetDto.getWholeQty().toString().length() - ptlJobOrderDetDto.getWholeUnitName().length() * 2);
                                        materialDesc += ptlJobOrderDetDto.getWholeQty() + ptlJobOrderDetDto.getWholeUnitName();
                                    }
                                    if (StringUtils.isNotEmpty(ptlJobOrderDetDto.getScatteredQty()) && ptlJobOrderDetDto.getScatteredQty().compareTo(BigDecimal.ZERO) != 0) {
                                        materialDesc2 += electronicTagStorageService.intercepting(ptlJobOrderDetDto.getMaterialName() + " ", 24 - ptlJobOrderDetDto.getScatteredQty().toString().length() - ptlJobOrderDetDto.getScatteredUnitName().length() * 2);
                                        materialDesc2 += ptlJobOrderDetDto.getScatteredQty() + ptlJobOrderDetDto.getScatteredUnitName();
                                    }
                                    rabbitMQDTO.setMaterialDesc(materialDesc + materialDesc2);
                                } else {
                                    rabbitMQDTO.setMaterialDesc(electronicTagStorageService.intercepting(ptlJobOrderDetDto.getMaterialCode(), 8));
                                }
                                rabbitMQDTO.setElectronicTagLangType(ptlJobOrderDetDto.getElectronicTagLangType());
                                list.add(rabbitMQDTO);
                                materialDesc = "";
                                materialDesc2 = "";
                                for (PtlJobOrderDetDto ptlJobOrderDetDto1 : ptlJobOrderDetDtoList1) {
                                    if (ptlJobOrderDetDto.getJobOrderDetId().equals(ptlJobOrderDetDto1.getJobOrderDetId()) && !ptlJobOrderDetDto.getElectronicTagLangType().equals(ptlJobOrderDetDto1.getElectronicTagLangType())) {
                                        RabbitMQDTO rabbitMQDTO2 = new RabbitMQDTO();
                                        rabbitMQDTO2.setEquipmentTagId(ptlJobOrderDetDto1.getEquipmentTagId());
                                        rabbitMQDTO2.setElectronicTagId(ptlJobOrderDetDto1.getElectronicTagId());
                                        rabbitMQDTO2.setOption1("0");
                                        rabbitMQDTO2.setOption2("1");
                                        rabbitMQDTO2.setOption3("0");
                                        rabbitMQDTO2.setQueueName(ptlElectronicTagStorageDtoList.get(0).getQueueName());
                                        if (ptlJobOrderDetDto1.getElectronicTagLangType() == 1) {
                                            if (StringUtils.isNotEmpty(ptlJobOrderDetDto1.getWholeQty()) && ptlJobOrderDetDto1.getWholeQty().compareTo(BigDecimal.ZERO) != 0) {
                                                materialDesc += electronicTagStorageService.intercepting(ptlJobOrderDetDto1.getMaterialName() + " ", 24 - ptlJobOrderDetDto1.getWholeQty().toString().length() - ptlJobOrderDetDto1.getWholeUnitName().length() * 2);
                                                materialDesc += ptlJobOrderDetDto1.getWholeQty() + ptlJobOrderDetDto1.getWholeUnitName();
                                            }
                                            if (StringUtils.isNotEmpty(ptlJobOrderDetDto1.getScatteredQty()) && ptlJobOrderDetDto1.getScatteredQty().compareTo(BigDecimal.ZERO) != 0) {
                                                materialDesc2 += electronicTagStorageService.intercepting(ptlJobOrderDetDto1.getMaterialName() + " ", 24 - ptlJobOrderDetDto1.getScatteredQty().toString().length() - ptlJobOrderDetDto1.getScatteredUnitName().length() * 2);
                                                materialDesc2 += ptlJobOrderDetDto1.getScatteredQty() + ptlJobOrderDetDto1.getScatteredUnitName();
                                            }
                                            rabbitMQDTO2.setMaterialDesc(materialDesc + materialDesc2);
                                        } else {
                                            rabbitMQDTO2.setMaterialDesc(electronicTagStorageService.intercepting(ptlJobOrderDetDto1.getMaterialCode(), 8));
                                        }
                                        rabbitMQDTO2.setElectronicTagLangType(ptlJobOrderDetDto1.getElectronicTagLangType());
                                        list.add(rabbitMQDTO2);
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                    }
                    // 通过区域设备ID判断当前区域（通道）内是否还有作业中的物料
                    if (StringUtils.isNotEmpty(ptlElectronicTagStorageDtoList.get(0).getEquipmentAreaId())) {
                        SearchPtlJobOrderDet searchPtlJobOrderDet2 = new SearchPtlJobOrderDet();
                        searchPtlJobOrderDet2.setJobOrderId(ptlJobOrderDetDtoList.get(0).getJobOrderId());
                        searchPtlJobOrderDet2.setEquipmentAreaId(ptlElectronicTagStorageDtoList.get(0).getEquipmentAreaId());
                        searchPtlJobOrderDet2.setIfHangUp((byte) 1);
                        searchPtlJobOrderDet2.setStartPage(1);
                        searchPtlJobOrderDet2.setPageSize(9999);
                        List<PtlJobOrderDetDto> ptlJobOrderDetDtos = electronicTagFeignApi.findPtlJobOrderDetList(searchPtlJobOrderDet2).getData();
                        if (ptlJobOrderDetDtos.size() == 1) {
                            //发送给客户端控制通道灯灭灯
                            rabbitMQDTOArea.setEquipmentTagId(ptlElectronicTagStorageDtoList.get(0).getEquipmentTagId());
                            rabbitMQDTOArea.setElectronicTagId(ptlElectronicTagStorageDtoList.get(0).getEquipmentAreaTagId());
                            rabbitMQDTOArea.setPosition(ptlElectronicTagStorageDtoList.get(0).getPosition());
                            rabbitMQDTOArea.setQueueName(ptlElectronicTagStorageDtoList.get(0).getQueueName());
                        }
                    }

                    //熄灭时，根据单号查询是否做完
                    SearchPtlJobOrderDet searchPtlJobOrderDet2 = new SearchPtlJobOrderDet();
                    searchPtlJobOrderDet2.setJobOrderId(ptlJobOrderDetDtoList.get(0).getJobOrderId());
                    searchPtlJobOrderDet2.setIfHangUp((byte) 1);
                    searchPtlJobOrderDet2.setStartPage(1);
                    searchPtlJobOrderDet2.setPageSize(9999);
                    List<PtlJobOrderDetDto> ptlJobOrderDetDtos = electronicTagFeignApi.findPtlJobOrderDetList(searchPtlJobOrderDet2).getData();
                    //电子作业标签完成，回传给WMS
                    if (ptlJobOrderDetDtos.size() == 1) {

                        PtlJobOrder ptlJobOrder = new PtlJobOrder();
                        ptlJobOrder.setJobOrderId(ptlJobOrderDetDtoList.get(0).getJobOrderId());
                        ptlJobOrder.setOrderStatus((byte) 3);
                        ptlJobOrder.setModifiedTime(new Date());
                        electronicTagFeignApi.updatePtlJobOrder(ptlJobOrder);

                        // 任务完成，给最后一个灭灯的中文标签发送END
                        endRabbitMQDTO = new RabbitMQDTO();
                        endRabbitMQDTO.setEquipmentTagId(equipmentTagId);
                        endRabbitMQDTO.setElectronicTagId(electronicTagId);
                        endRabbitMQDTO.setMaterialDesc("          END");
                        endRabbitMQDTO.setOption1("1");
                        endRabbitMQDTO.setOption2("1");
                        endRabbitMQDTO.setOption3("0");

                        SearchPtlJobOrder searchPtlJobOrder = new SearchPtlJobOrder();
                        searchPtlJobOrder.setRelatedOrderCode(ptlJobOrderDetDtoList.get(0).getRelatedOrderCode());
                        searchPtlJobOrder.setNotOrderStatus((byte) 3);
                        searchPtlJobOrder.setType(1);
                        List<PtlJobOrderDto> ptlJobOrderDtoList = electronicTagFeignApi.findPtlJobOrderList(searchPtlJobOrder).getData();
                        if (ptlJobOrderDtoList.size() == 1) {
                            PtlJobOrderDto ptlJobOrderDto = ptlJobOrderDtoList.get(0);
                            PtlJobOrderDTO ptlJobOrderDTO = new PtlJobOrderDTO();
                            ptlJobOrderDTO.setCustomerNo(ptlJobOrderDto.getRelatedOrderCode());
                            ptlJobOrderDTO.setWarehouseCode(ptlJobOrderDto.getWarehouseCode());
                            ptlJobOrderDTO.setWorkerCode(ptlJobOrderDto.getWorkerUserCode());
                            ptlJobOrderDTO.setStatus("F");
                            // 回写PTL作业任务
                            log.info("电子作业标签完成，回传WMS" + JSONObject.toJSONString(ptlJobOrderDTO));
                            String result = RestTemplateUtil.postJsonStrForString(ptlJobOrderDTO, finishPtlJobOrderUrl);
                            log.info("电子作业标签回写返回信息：" + result);
                            ResponseEntityDTO responseEntityDTO = JsonUtils.jsonToPojo(result, ResponseEntityDTO.class);
                            if (!"s".equals(responseEntityDTO.getSuccess())) {
                                throw new Exception("电子作业标签完成，回传WMS失败：" + responseEntityDTO.getMessage());
                            }
                        }
                    }
                }
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                log.warn("===========手动确认消息队列：" + RabbitConfig.TOPIC_QUEUE1 + " 消息：" + message.getMessageProperties().getDeliveryTag() + "===============> " + JSONObject.toJSONString(mqResponseEntity));

                if (StringUtils.isNotEmpty(rabbitMQDTOS)) {
                    electronicTagStorageService.fanoutSender(1003, null, rabbitMQDTOS);
                    log.info("===========发送消息给客户端控制该储位对应的电子标签灭灯完成===============");
                }

                if (StringUtils.isNotEmpty(rabbitMQDTOArea.getElectronicTagId())) {
                    electronicTagStorageService.fanoutSender(1015, rabbitMQDTOArea, null);
                    log.info("===========发送给客户端控制通道灯灭灯===============");
                }

                for (RabbitMQDTO rabbitMQDTO : list) {
                    if (rabbitMQDTO.getElectronicTagLangType() == 1) {
                        electronicTagStorageService.fanoutSender(1007, rabbitMQDTO, null);
                        log.info("===========发送消息给客户端控制该储位对应的另一个物料中文标签亮灯完成===============");
                    } else {
                        electronicTagStorageService.fanoutSender(1008, rabbitMQDTO, null);
                        log.info("===========发送消息给客户端控制该储位对应的另一个物料英文标签亮灯完成===============");
                    }
                }

//                if (StringUtils.isNotEmpty(endRabbitMQDTO)) {
//                    electronicTagStorageService.fanoutSender(1003, endRabbitMQDTO, null);
//                    log.info("===========任务完成，发送消息给最后一个灭灯的中文标签发送END完成===============");
//                }
            }
        } catch (Exception e) {
            electronicTagStorageService.fanoutSender(1001, rabbitMQDTO1, null);
            log.info("===========发送消息给客户端控制该储位对应的电子标签再次亮灯完成===============");
            //第二个参数设为true为自动应答，false为手动ack
//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            //重新放入队列
//            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            //抛弃此条消息
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            log.warn("===========删除消息队列：" + RabbitConfig.TOPIC_QUEUE1 + " 消息：" + message.getMessageProperties().getDeliveryTag() + "===============> " + JSONObject.toJSONString(mqResponseEntity));

            throw new Exception(e);
        }
    }
}
