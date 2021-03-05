package com.fantechs.provider.client.listener;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.storage.SmtStorageInventoryDto;
import com.fantechs.common.base.electronic.dto.SmtElectronicTagStorageDto;
import com.fantechs.common.base.electronic.dto.SmtSortingDto;
import com.fantechs.common.base.electronic.entity.SmtSorting;
import com.fantechs.common.base.electronic.entity.search.SearchSmtElectronicTagStorage;
import com.fantechs.common.base.electronic.entity.search.SearchSmtSorting;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageInventory;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.storage.SmtStorageInventory;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.MQResponseEntity;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.RestTemplateUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.electronic.ElectronicTagFeignApi;
import com.fantechs.provider.api.imes.storage.StorageInventoryFeignApi;
import com.fantechs.provider.client.config.RabbitConfig;
import com.fantechs.provider.client.dto.PtlSortingDTO;
import com.fantechs.provider.client.dto.PtlSortingDetailDTO;
import com.fantechs.provider.client.server.impl.FanoutSender;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
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

    @Autowired
    private StorageInventoryFeignApi storageInventoryFeignApi;

    @Value("${mesAPI.resApi}")
    private String resApiUrl;

    @Value("${qisAPI.confirmOutBillOrder}")
    private String confirmOutBillOrderUrl;

    // 监听标签队列
    @RabbitListener(queues = RabbitConfig.TOPIC_QUEUE1)
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public void receiveTopic1(byte[] bytes) throws Exception {
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
                SearchSmtSorting searchSmtSorting = new SearchSmtSorting();
                searchSmtSorting.setElectronicTagId(b.toString());
                searchSmtSorting.setEquipmentId(equipmentId);
                searchSmtSorting.setStatus((byte) 1);
                List<SmtSortingDto> findSortingList = electronicTagFeignApi.findSortingList(searchSmtSorting).getData();
                log.info("：" + findSortingList);
                if (StringUtils.isNotEmpty(findSortingList)) {
                    SmtSortingDto smtSortingDto = findSortingList.get(0);
                    SmtSorting smtSorting = new SmtSorting();
                    BeanUtils.copyProperties(smtSortingDto, smtSorting);
                    smtSorting.setStatus((byte) 2);
                    smtSorting.setUpdateStatus((byte) 0);
                    electronicTagFeignApi.updateSmtSorting(smtSorting);

                    SearchSmtElectronicTagStorage searchSmtElectronicTagStorage = new SearchSmtElectronicTagStorage();
                    searchSmtElectronicTagStorage.setMaterialId(smtSortingDto.getMaterialId());
                    List<SmtElectronicTagStorageDto> smtElectronicTagStorageDtoList = electronicTagFeignApi.findElectronicTagStorageList(searchSmtElectronicTagStorage).getData();

                    if (StringUtils.isEmpty(smtElectronicTagStorageDtoList)) {
                        throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "请先维护储位对应的电子标签信息");
                    }
                    if (StringUtils.isEmpty(smtElectronicTagStorageDtoList.get(0).getStorageCode())) {
                        throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到物料对应的储位信息");
                    }
                    smtElectronicTagStorageDtoList.get(0).setQuantity(smtSortingDto.getQuantity());

                    // 查询物料库存
                    SearchSmtStorageInventory searchSmtStorageInventory = new SearchSmtStorageInventory();
                    searchSmtStorageInventory.setMaterialId(Long.parseLong(smtElectronicTagStorageDtoList.get(0).getMaterialId()));
                    searchSmtStorageInventory.setStorageId(Long.parseLong(smtElectronicTagStorageDtoList.get(0).getStorageId()));
                    searchSmtStorageInventory.setStatus((byte) 1);
                    List<SmtStorageInventoryDto> smtStorageInventoryDtoList = storageInventoryFeignApi.findList(searchSmtStorageInventory).getData();
                    if (StringUtils.isEmpty(smtStorageInventoryDtoList)) {
//                   throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到物料对应的库存信息");
                        // 允许负库存
                        SmtStorageInventory smtStorageInventory = new SmtStorageInventory();
                        smtStorageInventory.setMaterialId(Long.parseLong(smtElectronicTagStorageDtoList.get(0).getMaterialId()));
                        smtStorageInventory.setStorageId(Long.parseLong(smtElectronicTagStorageDtoList.get(0).getStorageId()));
                        smtStorageInventory.setQuantity(smtSortingDto.getQuantity().negate());
                        smtStorageInventory.setStatus((byte) 1);
                        storageInventoryFeignApi.add(smtStorageInventory);
                    } else {
                        // 更新物料库存信息
                        SmtStorageInventory smtStorageInventory = new SmtStorageInventory();
                        smtStorageInventory.setStorageInventoryId(smtStorageInventoryDtoList.get(0).getStorageInventoryId());
                        smtStorageInventory.setQuantity(smtStorageInventoryDtoList.get(0).getQuantity().subtract(smtSortingDto.getQuantity()));
                        storageInventoryFeignApi.update(smtStorageInventory);
                    }

                    // 通过仓库区域ID判断当前区域内查找分拣中的物料
                    if (StringUtils.isNotEmpty(smtSortingDto.getEquipmentAreaId())) {
                        SearchSmtSorting searchSmtSorting2 = new SearchSmtSorting();
                        searchSmtSorting2.setEquipmentAreaId(smtSortingDto.getEquipmentAreaId());
                        searchSmtSorting2.setStatus((byte) 1);
                        findSortingList = electronicTagFeignApi.findSortingList(searchSmtSorting2).getData();
                    }

                    //不同的标签可能对应的队列不一样，最终一条一条发给客户端
                    MQResponseEntity mQResponseEntity = new MQResponseEntity<>();
                    mQResponseEntity.setCode(1003);
                    mQResponseEntity.setData(smtElectronicTagStorageDtoList.get(0));
                    log.info("===========开始发送消息给客户端===============");
                    //发送给PDA修改数据状态
                    fanoutSender.send(RabbitConfig.TOPIC_QUEUE_PDA,
                            JSONObject.toJSONString(mQResponseEntity));
                    //发送给客户端控制灭灯
                    fanoutSender.send(smtElectronicTagStorageDtoList.get(0).getQueueName(),
                            JSONObject.toJSONString(mQResponseEntity));
                    //当区域内没有分拣中物料时，发送给客户端控制灭灯
                    if (StringUtils.isEmpty(findSortingList) && StringUtils.isNotEmpty(smtSortingDto.getEquipmentAreaId())) {
                        mQResponseEntity.setCode(1003);
                        smtElectronicTagStorageDtoList.get(0).setEquipmentAreaId(smtSortingDto.getEquipmentAreaId());
                        fanoutSender.send(smtElectronicTagStorageDtoList.get(0).getQueueName(),
                                JSONObject.toJSONString(mQResponseEntity));
                    }
                    log.info("===========队列名称:" + smtElectronicTagStorageDtoList.get(0).getQueueName());
                    log.info("===========消息内容:" + JSONObject.toJSONString(mQResponseEntity));
                    log.info("===========发送消息给客户端完成===============");

                    //熄灭时，根据单号查询是否做完
                    SearchSmtSorting searchSmtSorting1 = new SearchSmtSorting();
                    searchSmtSorting1.setSortingCode(smtSortingDto.getSortingCode());
                    searchSmtSorting1.setStatus((byte) 1);
                    findSortingList = electronicTagFeignApi.findSortingList(searchSmtSorting1).getData();
                    //分拣单号处理完，回传给MES
                    if (StringUtils.isEmpty(findSortingList)) {
                        PtlSortingDTO ptlSortingDTO = new PtlSortingDTO();
                        searchSmtSorting1.setStatus(null);
                        searchSmtSorting1.setPageSize(99999);
                        //获取分拣单号的所有物料、储位信息回传MES
                        findSortingList = electronicTagFeignApi.findSortingList(searchSmtSorting1).getData();
                        List<PtlSortingDetailDTO> ptlSortingDetailDTOList = new LinkedList<>();
                        for (SmtSortingDto smtSortingDto1 : findSortingList) {
                            PtlSortingDetailDTO ptlSortingDetailDTO = new PtlSortingDetailDTO();
                            ptlSortingDetailDTO.setCwWarehouseCode(smtSortingDto1.getStorageCode());
                            ptlSortingDetailDTO.setMaterialCode(smtSortingDto1.getMaterialCode());
                            ptlSortingDetailDTOList.add(ptlSortingDetailDTO);
                        }
                        ptlSortingDTO.setSortingCode(smtSortingDto.getSortingCode());
                        ptlSortingDTO.setPtlSortingDetailDTOList(ptlSortingDetailDTOList);
                        ptlSortingDTO.setUser(findSortingList.get(0).getModifiedUserCode());
                        log.info("分拣单号处理完，回传给" + findSortingList.get(0).getSourceSys() + "：" + ptlSortingDTO);
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
            } else if (mqResponseEntity.getCode() == 102) {

            } else if (mqResponseEntity.getCode() == 103) {

            }
        } catch (Exception e) {
            MQResponseEntity mQResponseEntity = new MQResponseEntity<>();
            mQResponseEntity.setCode(500);
            mQResponseEntity.setMessage(e.getMessage());
            HashMap<String, Object> map = new HashMap<>();
            mQResponseEntity.setData(map);
            log.info("===========开始发送异常消息给客户端===============");
            // 出现异常，发送给PDA展示异常状态
            fanoutSender.send(RabbitConfig.TOPIC_QUEUE_PDA, JSONObject.toJSONString(mQResponseEntity));
            log.info("===========队列名称:" + RabbitConfig.TOPIC_QUEUE_PDA);
            log.info("===========消息内容:" + JSONObject.toJSONString(mQResponseEntity));
            log.info("===========发送消息给客户端完成===============");

            throw new Exception(e);
        }
    }
}
