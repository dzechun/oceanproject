package com.fantechs.provider.client.listener;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.dto.PtlElectronicTagStorageDto;
import com.fantechs.common.base.electronic.dto.PtlSortingDto;
import com.fantechs.common.base.electronic.entity.PtlSorting;
import com.fantechs.common.base.electronic.entity.search.SearchPtlElectronicTagStorage;
import com.fantechs.common.base.electronic.entity.search.SearchPtlSorting;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.MQResponseEntity;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.RestTemplateUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.electronic.ElectronicTagFeignApi;
import com.fantechs.provider.client.config.RabbitConfig;
import com.fantechs.provider.client.dto.PtlSortingDTO;
import com.fantechs.provider.client.dto.PtlSortingDetailDTO;
import com.fantechs.provider.client.server.impl.FanoutSender;
import com.google.gson.reflect.TypeToken;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by lfz on 2020/11/27.
 */
@Component
public class ElectronicTagReceiver {
    private static final Logger log = LoggerFactory.getLogger(ElectronicTagReceiver.class);
    @Autowired
    private FanoutSender fanoutSender;

    @Autowired
    private ElectronicTagFeignApi electronicTagFeignApi;

    @Value("${mesAPI.resApi}")
    private String resApiUrl;

    @Value("${qisAPI.confirmOutBillOrder}")
    private String confirmOutBillOrderUrl;

    // 监听标签队列
    @RabbitListener(queues = RabbitConfig.TOPIC_QUEUE1)
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public void receiveTopic1(byte[] bytes, Message message, Channel channel) throws Exception {
        String encoded = new String(bytes, "UTF-8");
        MQResponseEntity mqResponseEntity = JsonUtils.jsonToPojo(encoded, MQResponseEntity.class);
        log.info("接收到客户端消息：" + mqResponseEntity);
        //电子标签熄灭动作
        try {
            if (mqResponseEntity.getCode() == 106) {
                Map<String, Object> map = JsonUtils.jsonToMap(mqResponseEntity.getData().toString());
                String equipmentId = map.get("GwId").toString();
                String electronicTagId = map.get("TagNode").toString();
                BigInteger b = new BigInteger(electronicTagId).abs();

                //通过标签ID去找当前的分拣单信息
                SearchPtlSorting searchPtlSorting = new SearchPtlSorting();
                searchPtlSorting.setElectronicTagId(b.toString());
                searchPtlSorting.setEquipmentId(equipmentId);
                searchPtlSorting.setStatus((byte) 1);
                List<PtlSortingDto> findSortingList = electronicTagFeignApi.findSortingList(searchPtlSorting).getData();
                log.info("：" + findSortingList);
                if (StringUtils.isNotEmpty(findSortingList)) {
                    PtlSortingDto ptlSortingDto = findSortingList.get(0);
                    PtlSorting ptlSorting = new PtlSorting();
                    BeanUtils.copyProperties(ptlSortingDto, ptlSorting);
                    ptlSorting.setStatus((byte) 2);
                    ptlSorting.setUpdateStatus((byte) 0);
                    electronicTagFeignApi.updateSmtSorting(ptlSorting);

                    SearchPtlElectronicTagStorage searchPtlElectronicTagStorage = new SearchPtlElectronicTagStorage();
                    searchPtlElectronicTagStorage.setMaterialId(ptlSortingDto.getMaterialId().toString());
                    searchPtlElectronicTagStorage.setStorageId(ptlSortingDto.getStorageId().toString());
                    List<PtlElectronicTagStorageDto> ptlElectronicTagStorageDtoList = electronicTagFeignApi.findElectronicTagStorageList(searchPtlElectronicTagStorage).getData();
                    if (StringUtils.isEmpty(ptlElectronicTagStorageDtoList)) {
                        throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "请先维护储位和物料以及对应的电子标签关联信息");
                    }
                    ptlElectronicTagStorageDtoList.get(0).setQuantity(ptlSortingDto.getSortingQty());

                    // 通过仓库区域ID判断当前区域内查找分拣中的物料
                    if (StringUtils.isNotEmpty(ptlSortingDto.getEquipmentAreaId())) {
                        SearchPtlSorting searchPtlSorting2 = new SearchPtlSorting();
                        searchPtlSorting2.setEquipmentAreaId(ptlSortingDto.getEquipmentAreaId());
                        searchPtlSorting2.setStatus((byte) 1);
                        findSortingList = electronicTagFeignApi.findSortingList(searchPtlSorting2).getData();
                    }

                    //不同的标签可能对应的队列不一样，最终一条一条发给客户端
                    MQResponseEntity mQResponseEntity = new MQResponseEntity<>();
                    mQResponseEntity.setCode(1003);
                    mQResponseEntity.setData(ptlElectronicTagStorageDtoList.get(0));
                    log.info("===========开始发送消息给客户端===============");
                    //发送给PDA修改数据状态
                    fanoutSender.send(RabbitConfig.TOPIC_QUEUE_PDA,
                            JSONObject.toJSONString(mQResponseEntity));
                    //发送给客户端控制灭灯
                    fanoutSender.send(ptlElectronicTagStorageDtoList.get(0).getQueueName(),
                            JSONObject.toJSONString(mQResponseEntity));
                    //当区域内没有分拣中物料时，发送给客户端控制灭区域灯
                    if (findSortingList.size() == 1 && StringUtils.isNotEmpty(ptlSortingDto.getEquipmentAreaId())) {
                        mQResponseEntity.setCode(1004);
                        ptlElectronicTagStorageDtoList.get(0).setEquipmentAreaId(ptlSortingDto.getEquipmentAreaId());
                        fanoutSender.send(ptlElectronicTagStorageDtoList.get(0).getQueueName(),
                                JSONObject.toJSONString(mQResponseEntity));
                    }
                    log.info("===========队列名称:" + ptlElectronicTagStorageDtoList.get(0).getQueueName());
                    log.info("===========消息内容:" + JSONObject.toJSONString(mQResponseEntity));
                    log.info("===========发送消息给客户端完成===============");

                    //熄灭时，根据单号查询是否做完
                    SearchPtlSorting searchPtlSorting1 = new SearchPtlSorting();
                    searchPtlSorting1.setSortingCode(ptlSortingDto.getSortingCode());
                    searchPtlSorting1.setStatus((byte) 1);
                    findSortingList = electronicTagFeignApi.findSortingList(searchPtlSorting1).getData();
                    //分拣单号处理完，回传给MES
                    if (findSortingList.size() == 1) {
                        PtlSortingDTO ptlSortingDTO = new PtlSortingDTO();
                        searchPtlSorting1.setStatus(null);
                        searchPtlSorting1.setPageSize(99999);
                        //获取分拣单号的所有物料、储位信息回传MES
                        findSortingList = electronicTagFeignApi.findSortingList(searchPtlSorting1).getData();
                        List<PtlSortingDetailDTO> ptlSortingDetailDTOList = new LinkedList<>();
                        for (PtlSortingDto ptlSortingDto1 : findSortingList) {
                            PtlSortingDetailDTO ptlSortingDetailDTO = new PtlSortingDetailDTO();
                            ptlSortingDetailDTO.setLocationCode(ptlSortingDto1.getStorageCode());
                            ptlSortingDetailDTO.setGoodsCode(ptlSortingDto1.getMaterialCode());
                            ptlSortingDetailDTO.setQty(Double.parseDouble(ptlSortingDto1.getSortingQty().toString()));
                            ptlSortingDetailDTO.setUnit(ptlSortingDto1.getPackingUnitName());
                            ptlSortingDetailDTOList.add(ptlSortingDetailDTO);
                        }
                        ptlSortingDTO.setTaskNo(ptlSortingDto.getSortingCode());
                        ptlSortingDTO.setCustomerNo(ptlSortingDto.getRelatedOrderCode());
                        ptlSortingDTO.setWarehouseCode(ptlSortingDto.getWarehouseCode());
                        ptlSortingDTO.setWorkerCode(findSortingList.get(0).getCreateUserCode());
                        ptlSortingDTO.setDetails(ptlSortingDetailDTOList);
                        log.info("分拣单号处理完，回传给" + findSortingList.get(0).getSourceSys() + "：" + JSONObject.toJSONString(ptlSortingDTO));
                        String url = "";
                        if ("MES".equals(findSortingList.get(0).getSourceSys())) {
                            url = resApiUrl;
                        } else if ("QIS".equals(findSortingList.get(0).getSourceSys())) {
                            url = confirmOutBillOrderUrl;
                        }
                        String result = RestTemplateUtil.postJsonStrForString(ptlSortingDTO, url);
                        log.info(findSortingList.get(0).getSourceSys() + "返回信息：" + result);
                        ResponseEntity responseEntity = com.fantechs.common.base.utils.BeanUtils.convertJson(result, new TypeToken<ResponseEntity>() {
                        }.getType());
                        if (responseEntity.getCode() != 0 && responseEntity.getCode() != 200) {
                            throw new Exception("分拣单号处理完，回传给" + findSortingList.get(0).getSourceSys() + "失败：" + responseEntity.getMessage());
                        }
//                   mqResponseEntity.setCode(1003);
//            fanoutSender.send(RabbitConfig.FANOUT_QUEUE1, JSONObject.toJSONString(mqResponseEntity));
                    }
                }
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                log.warn("===========手动确认消息队列：" + RabbitConfig.TOPIC_QUEUE1 + " 消息：" + message.getMessageProperties().getDeliveryTag() + "===============> " + JSONObject.toJSONString(mqResponseEntity));
            } else if (mqResponseEntity.getCode() == 102) {

            } else if (mqResponseEntity.getCode() == 103) {

            }
        } catch (Exception e) {
            MQResponseEntity mQResponseEntity = new MQResponseEntity<>();
            mQResponseEntity.setCode(500);
            mQResponseEntity.setMessage(e.getMessage());
            HashMap<String, Object> map = new HashMap<>();
            map.put("currentTime", System.currentTimeMillis());
            mQResponseEntity.setData(map);
            log.info("===========开始发送异常消息给客户端===============");
            // 出现异常，发送给PDA展示异常状态
            fanoutSender.send(RabbitConfig.TOPIC_QUEUE_PDA, JSONObject.toJSONString(mQResponseEntity));
            log.info("===========队列名称:" + RabbitConfig.TOPIC_QUEUE_PDA);
            log.info("===========消息内容:" + JSONObject.toJSONString(mQResponseEntity));
            log.info("===========发送消息给客户端完成===============");
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
