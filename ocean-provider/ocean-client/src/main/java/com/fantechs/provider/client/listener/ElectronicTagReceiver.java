package com.fantechs.provider.client.listener;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
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
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.RestTemplateUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.electronic.ElectronicTagFeignApi;
import com.fantechs.provider.client.config.RabbitConfig;
import com.fantechs.provider.client.dto.PtlJobOrderDTO;
import com.fantechs.provider.client.dto.RabbitMQDTO;
import com.fantechs.provider.client.dto.ResponseEntityDTO;
import com.fantechs.provider.client.server.ElectronicTagStorageService;
import com.fantechs.provider.client.server.impl.FanoutSender;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class ElectronicTagReceiver {
    private static final Logger log = LoggerFactory.getLogger(ElectronicTagReceiver.class);
    @Autowired
    private FanoutSender fanoutSender;

    @Autowired
    private ElectronicTagFeignApi electronicTagFeignApi;

    @Autowired
    private ElectronicTagStorageService electronicTagStorageService;

    @Resource
    private RedisUtil redisUtil;

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
        String lockKey = "lock";
        String lockValue = "";
        RabbitMQDTO rabbitMQDTO1 = new RabbitMQDTO();
        Boolean finishBoolean = false;
        //电子标签熄灭动作
        try {
            if (mqResponseEntity.getCode() == 106) {
                Map<String, Object> map = JsonUtils.jsonToMap(mqResponseEntity.getData().toString());
                String equipmentTagId = map.get("GwId").toString();
                String electronicTagId = map.get("TagNode").toString();
                BigInteger b = new BigInteger(electronicTagId).abs();

                SearchPtlElectronicTagStorage searchPtlElectronicTagStorage = new SearchPtlElectronicTagStorage();
                searchPtlElectronicTagStorage.setEquipmentTagId(equipmentTagId);
                searchPtlElectronicTagStorage.setElectronicTagId(b.toString());
                List<PtlElectronicTagStorageDto> ptlElectronicTagStorageDtoList = electronicTagFeignApi.findElectronicTagStorageList(searchPtlElectronicTagStorage).getData();
                if (!ptlElectronicTagStorageDtoList.isEmpty()) {
                    lockKey = ptlElectronicTagStorageDtoList.get(0).getWarehouseAreaCode() + "_lock";
                    if (redisUtil.lock(lockKey)) {
                        lockValue = String.valueOf(redisUtil.get(lockKey));
                        log.info("=====================获取到:" + lockKey + "--->redisKEY, " + lockValue + "--->redisVALUE");
                    } else {
                        throw new BizErrorException("正在处理电子标签任务，请稍后再试！");
                    }

                    String endElectronicTagId = b.toString();
                    RabbitMQDTO endRabbitMQDTO = null;

                    List<RabbitMQDTO> listC = new LinkedList<>();
                    List<RabbitMQDTO> listE = new LinkedList<>();
                    Map<String, Long> updateMap = new HashMap<>();
                    List<String> delRedis = new LinkedList<>();
                    RabbitMQDTO rabbitMQDTOArea = new RabbitMQDTO();
                    List<RabbitMQDTO> rabbitMQDTOS = new LinkedList<>();
                    rabbitMQDTO1.setEquipmentTagId(equipmentTagId);
                    rabbitMQDTO1.setElectronicTagId(b.toString());
                    rabbitMQDTO1.setQueueName(ptlElectronicTagStorageDtoList.get(0).getQueueName());
                    rabbitMQDTOS.add(rabbitMQDTO1);
                    // 通过标签ID去找对应的任务单明细信息
                    List<PtlJobOrderDetDto> ptlJobOrderDetDtoList = new LinkedList<>();
                    String key = ptlElectronicTagStorageDtoList.get(0).getWarehouseAreaCode() + "_" + b.toString();
                    if (StringUtils.isNotEmpty(redisUtil.get(key))) {
                        Long jobOrderDetId = Long.valueOf(redisUtil.get(key).toString());
                        SearchPtlJobOrderDet searchPtlJobOrderDet = new SearchPtlJobOrderDet();
                        searchPtlJobOrderDet.setJobOrderDetId(jobOrderDetId);
                        searchPtlJobOrderDet.setJobStatus((byte) 2);
                        searchPtlJobOrderDet.setJobOrderDet(0);
                        ptlJobOrderDetDtoList = electronicTagFeignApi.findPtlJobOrderDetList(searchPtlJobOrderDet).getData();
                        PtlJobOrder ptlJobOrderCancel = electronicTagFeignApi.ptlJobOrderDetail(ptlJobOrderDetDtoList.get(0).getJobOrderId()).getData();
                        if (ptlJobOrderCancel.getOrderStatus() == 2) {

                            PtlJobOrderDet ptlJobOrderDet = new PtlJobOrderDet();
                            ptlJobOrderDet.setJobOrderDetId(ptlJobOrderDetDtoList.get(0).getJobOrderDetId());
                            ptlJobOrderDet.setJobStatus((byte) 3);
                            ptlJobOrderDet.setModifiedTime(new Date());
                            ptlJobOrderDet.setLightOutTime(new Date());
                            electronicTagFeignApi.updatePtlJobOrderDet(ptlJobOrderDet);
                            // 该储位对应的另一个电子标签灭灯
                            for (PtlJobOrderDetDto ptlJobOrderDetDto : ptlJobOrderDetDtoList) {
                                if (!b.toString().equals(ptlJobOrderDetDto.getElectronicTagId())) {
                                    RabbitMQDTO rabbitMQDTO = new RabbitMQDTO();
                                    rabbitMQDTO.setEquipmentTagId(equipmentTagId);
                                    rabbitMQDTO.setElectronicTagId(ptlJobOrderDetDto.getElectronicTagId());
                                    rabbitMQDTO.setQueueName(ptlJobOrderDetDto.getQueueName());
                                    rabbitMQDTOS.add(rabbitMQDTO);

                                    if (ptlJobOrderDetDto.getElectronicTagLangType() == 1) {
                                        endElectronicTagId = ptlJobOrderDetDto.getElectronicTagId();
                                    }
                                }

                                // 判断当前储位是否还有其他拣货任务需要亮灯
                                key = ptlElectronicTagStorageDtoList.get(0).getWarehouseAreaCode() + "_" + ptlJobOrderDetDto.getElectronicTagId();
                                Long nextJobOrderDetId = electronicTagStorageService.getNextJobOrderDet(ptlJobOrderDetDto, 0);
                                if (nextJobOrderDetId != 0) {
                                    updateMap.put(key, nextJobOrderDetId);
                                    electronicTagStorageService.getNextMaterial(nextJobOrderDetId, listC, listE);
                                } else {
                                    delRedis.add(key);
                                }
                            }
                            // 通过区域设备ID判断当前区域（通道）内是否还有作业中的物料
                            if (StringUtils.isNotEmpty(ptlElectronicTagStorageDtoList.get(0).getEquipmentAreaId())) {
                                SearchPtlJobOrderDet searchPtlJobOrderDet2 = new SearchPtlJobOrderDet();
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

                                finishBoolean = true;

                                PtlJobOrder ptlJobOrder = new PtlJobOrder();
                                ptlJobOrder.setJobOrderId(ptlJobOrderDetDtoList.get(0).getJobOrderId());
                                ptlJobOrder.setOrderStatus((byte) 3);
                                ptlJobOrder.setFinishTime(new Date());
                                ptlJobOrder.setModifiedTime(new Date());
                                electronicTagFeignApi.updatePtlJobOrder(ptlJobOrder);

                                // 任务完成，给最后一个灭灯的中文标签发送END
                                endRabbitMQDTO = new RabbitMQDTO();
                                endRabbitMQDTO.setEquipmentTagId(equipmentTagId);
                                endRabbitMQDTO.setElectronicTagId(b.toString());
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
                    }
//                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//                log.info("===========手动确认消息队列：" + RabbitConfig.TOPIC_QUEUE1 + " 消息：" + message.getMessageProperties().getDeliveryTag() + "===============> " + JSONObject.toJSONString(mqResponseEntity));

                    if (StringUtils.isNotEmpty(rabbitMQDTOS)) {
                        electronicTagStorageService.fanoutSender(1003, null, rabbitMQDTOS);
                        log.info("===========发送消息给客户端控制该储位对应的电子标签灭灯完成===============");
                    }

                    if (StringUtils.isNotEmpty(rabbitMQDTOArea.getElectronicTagId())) {
                        electronicTagStorageService.fanoutSender(1015, rabbitMQDTOArea, null);
                        log.info("===========发送给客户端控制通道灯灭灯===============");
                    }
                    if (finishBoolean) {
                        electronicTagStorageService.updateJobOrderSeq(ptlJobOrderDetDtoList.get(0).getWarehouseAreaId(), ptlJobOrderDetDtoList.get(0).getJobOrderCode());
                    }
                    for (String key2 : updateMap.keySet()) {
                        redisUtil.set(key2, updateMap.get(key2));
                        log.info("修改电子标签：" + key2 + "当前对应拣货任务明细ID为：" + updateMap.get(key2));
                    }
                    for (String redisKey : delRedis) {
                        log.info("删除当前电子标签redisKey：" + redisKey + ", 值为：" + redisUtil.get(redisKey));
                        redisUtil.del(redisKey);
                    }
                    if (!listC.isEmpty()) {
                        redisUtil.set(lockKey + "_" + electronicTagId, equipmentTagId, 100, TimeUnit.MILLISECONDS);
                        log.info("##########延时发送指令控制标签重新亮灯开始##########");
                        while (StringUtils.isNotEmpty(redisUtil.get(lockKey + "_" + electronicTagId))) {
                        }
                        log.info("---------延时发送指令控制标签重新亮灯结束-----------");
                        electronicTagStorageService.fanoutSender(1007, null, listC);
                        log.info("===========发送消息给客户端控制另一个中文标签亮灯完成===============");
                    }
                    if (!listE.isEmpty()) {
                        electronicTagStorageService.fanoutSender(1007, null, listE);
                        log.info("===========发送消息给客户端控制另一个英文标签亮灯完成===============");
                    }

//                if (StringUtils.isNotEmpty(endRabbitMQDTO)) {
//                    electronicTagStorageService.fanoutSender(1003, endRabbitMQDTO, null);
//                    log.info("===========任务完成，发送消息给最后一个灭灯的中文标签发送END完成===============");
//                }
                }
            }
        } catch (BizErrorException e) {
            //第二个参数设为true为自动应答，false为手动ack
//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            //重新放入队列
//            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            //抛弃此条消息
//            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
//            log.info("===========删除消息队列：" + RabbitConfig.TOPIC_QUEUE1 + " 消息：" + message.getMessageProperties().getDeliveryTag() + "===============> " + JSONObject.toJSONString(mqResponseEntity));

            electronicTagStorageService.fanoutSender(1001, rabbitMQDTO1, null);
            log.info("===========发送消息给客户端控制该储位对应的电子标签再次亮灯完成===============");

            throw new BizErrorException(e);
        } finally {

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.info("===========手动确认消息队列：" + RabbitConfig.TOPIC_QUEUE1 + " 消息：" + message.getMessageProperties().getDeliveryTag() + "===============> " + JSONObject.toJSONString(mqResponseEntity));

            redisUtil.unlock(lockKey, lockValue);
            log.info("=====================释放了:" + lockKey + "--->redisKEY");
        }
    }
}
