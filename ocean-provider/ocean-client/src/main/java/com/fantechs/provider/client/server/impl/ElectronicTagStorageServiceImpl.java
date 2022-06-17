package com.fantechs.provider.client.server.impl;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.dto.PtlElectronicTagStorageDto;
import com.fantechs.common.base.electronic.dto.PtlEquipmentDto;
import com.fantechs.common.base.electronic.dto.PtlJobOrderDetDto;
import com.fantechs.common.base.electronic.dto.PtlJobOrderDto;
import com.fantechs.common.base.electronic.entity.PtlJobOrder;
import com.fantechs.common.base.electronic.entity.PtlJobOrderDet;
import com.fantechs.common.base.electronic.entity.search.SearchPtlElectronicTagStorage;
import com.fantechs.common.base.electronic.entity.search.SearchPtlEquipment;
import com.fantechs.common.base.electronic.entity.search.SearchPtlJobOrder;
import com.fantechs.common.base.electronic.entity.search.SearchPtlJobOrderDet;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseWorkerDto;
import com.fantechs.common.base.general.dto.tem.TemVehicleDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.BaseWarehouseArea;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWorker;
import com.fantechs.common.base.general.entity.tem.TemVehicle;
import com.fantechs.common.base.general.entity.tem.search.SearchTemVehicle;
import com.fantechs.common.base.response.MQResponseEntity;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.*;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.electronic.ElectronicTagFeignApi;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.api.tem.TemVehicleFeignApi;
import com.fantechs.provider.client.config.RabbitConfig;
import com.fantechs.provider.client.dto.*;
import com.fantechs.provider.client.server.ElectronicTagStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

@Service
@RefreshScope
public class ElectronicTagStorageServiceImpl implements ElectronicTagStorageService {
    private static final Logger log = LoggerFactory.getLogger(ElectronicTagStorageServiceImpl.class);
    @Resource
    private FanoutSender fanoutSender;
    @Resource
    private ElectronicTagFeignApi electronicTagFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private AuthFeignApi securityFeignApi;
    @Resource
    private TemVehicleFeignApi temVehicleFeignApi;
    @Resource
    private RedisUtil redisUtil;

    @Value("${wmsAPI.finishPtlJobOrderUrl}")
    private String finishPtlJobOrderUrl;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public List<PtlJobOrderDto> sendElectronicTagStorage(String ids, Long warehouseAreaId, Integer type) throws Exception {

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<String> jobOrderIds = Arrays.asList(ids.split(","));
        if (warehouseAreaId == 0) {
            SearchPtlJobOrderDet searchPtlJobOrderDet = new SearchPtlJobOrderDet();
            searchPtlJobOrderDet.setJobOrderId(Long.valueOf(jobOrderIds.get(0)));
            searchPtlJobOrderDet.setStartPage(1);
            searchPtlJobOrderDet.setPageSize(9999);
            List<PtlJobOrderDetDto> ptlJobOrderDetDtoList = electronicTagFeignApi.findPtlJobOrderDetList(searchPtlJobOrderDet).getData();
            warehouseAreaId = ptlJobOrderDetDtoList.get(0).getWarehouseAreaId();
        }
        BaseWarehouseArea baseWarehouseArea = baseFeignApi.warehouseAreaDetail(warehouseAreaId).getData();
        String lockKey = baseWarehouseArea.getWarehouseAreaCode() + "_lock";
        String lockValue = "";

        // 本次激活任务单列表
        List<PtlJobOrderDto> ptlJobOrderDtos = new LinkedList<>();
        try {
            if (redisUtil.lock(lockKey)) {
                lockValue = String.valueOf(redisUtil.get(lockKey));
                log.info("=====================获取到:" + lockKey + "--->redisKEY, " + lockValue + "--->redisVALUE");
            } else {
                throw new Exception("正在处理电子标签任务，请稍后再试！");
            }
            // 当前区域有多少任务单正在作业
            SearchPtlJobOrder searchPtlJobOrder = new SearchPtlJobOrder();
            searchPtlJobOrder.setOrderStatusList((byte) 1);
            searchPtlJobOrder.setWarehouseAreaId(warehouseAreaId);
            searchPtlJobOrder.setType(1);
            List<PtlJobOrderDto> ptlJobOrderDtoList = electronicTagFeignApi.findPtlJobOrderList(searchPtlJobOrder).getData();
            int count = jobOrderIds.size() + ptlJobOrderDtoList.size();
            // 灯颜色 -1-灯颜色未占用 0-红色 1-绿色 2-黄色
            int color = -1;
            // 只记录有激活的任务数，如果一个任务剩余的拣货任务全部被挂起，不记录
            int num = 0;
            for (PtlJobOrderDto ptlJobOrderDto : ptlJobOrderDtoList) {
                if (jobOrderIds.contains(ptlJobOrderDto.getJobOrderId().toString())) {
                    count--;
                }
                // 找到未被占用的灯颜色
                if (color == -1) {
                    color = (Integer.valueOf(ptlJobOrderDto.getOption1()) + 1) % 3;
                } else if (ptlJobOrderDto.getOption1().equals(String.valueOf(color))) {
                    color = (Integer.valueOf(ptlJobOrderDto.getOption1()) + 1) % 3;
                }
                if (StringUtils.isNotEmpty(redisUtil.get(ptlJobOrderDto.getJobOrderCode()))) {
                    num++;
                }
            }
            if (color == -1) {
                color = 0;
            }
            if (count > 3) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "该区域最多可同时进行 3 个作业任务，该次操作已超出最大作业数，请稍后再试");
            }
            for (String jobOrderId : jobOrderIds) {
                PtlJobOrder ptlJobOrder = electronicTagFeignApi.ptlJobOrderDetail(Long.valueOf(jobOrderId)).getData();
                if (ptlJobOrder.getOrderStatus() == 3) {
                    throw new BizErrorException(ErrorCodeEnum.GL99990500.getCode(), "任务单 : " + ptlJobOrder.getJobOrderCode() + "已完成");
                }
                if (ptlJobOrder.getOrderStatus() == 5) {
                    throw new BizErrorException(ErrorCodeEnum.GL99990500.getCode(), "任务单 : " + ptlJobOrder.getJobOrderCode() + "已取消");
                }
                if (ptlJobOrder.getOrderStatus() == 6) {
                    throw new BizErrorException(ErrorCodeEnum.GL99990500.getCode(), "任务单 : " + ptlJobOrder.getJobOrderCode() + "已复核完成");
                }
                if (ptlJobOrder.getIfAlreadyPrint() != 1 && type == 0) {
                    throw new BizErrorException(ErrorCodeEnum.GL99990500.getCode(), "任务单 : " + ptlJobOrder.getJobOrderCode() + "未打印标签，请先进行打印");
                }
                PtlJobOrderDto ptlJobOrderDto = new PtlJobOrderDto();
                BeanUtils.autoFillEqFields(ptlJobOrder, ptlJobOrderDto);
                if (StringUtils.isNotEmpty(ptlJobOrder.getOption1())) {
                    ptlJobOrderDto.setOption1(ptlJobOrder.getOption1());
                } else {
                    ptlJobOrderDto.setOption1(String.valueOf(color));
                    color = (color + 1) % 3;
                }
                if (StringUtils.isNotEmpty(redisUtil.get(ptlJobOrderDto.getJobOrderCode()))) {
                    ptlJobOrderDto.setSeq(Integer.parseInt(redisUtil.get(ptlJobOrderDto.getJobOrderCode()).toString()));
                } else {
                    ptlJobOrderDto.setIsNew(1);
                    ptlJobOrderDto.setSeq(num + 1);
                    num++;
                }
                ptlJobOrderDtos.add(ptlJobOrderDto);
            }
            ptlJobOrderDtos.sort(Comparator.comparing(PtlJobOrderDto::getSeq));

            List<RabbitMQDTO> listC = new LinkedList<>();
            List<RabbitMQDTO> listE = new LinkedList<>();
            List<PtlJobOrderDet> ptlJobOrderDetList = new LinkedList<>();
            List<String> electronicTagIds = new LinkedList<>();
            List<String> equipmentAreaIds = new LinkedList<>();
            Map<String, Long> map = new HashMap<>();
            String warehouseAreaCode = "";
            for (PtlJobOrderDto ptlJobOrderDto : ptlJobOrderDtos) {
                SearchPtlJobOrderDet searchPtlJobOrderDet = new SearchPtlJobOrderDet();
                searchPtlJobOrderDet.setJobOrderId(ptlJobOrderDto.getJobOrderId());
                if (ptlJobOrderDto.getOrderStatus() == 2) {
                    searchPtlJobOrderDet.setJobStatus((byte) 2);
                } else if (ptlJobOrderDto.getOrderStatus() == 4) {
                    searchPtlJobOrderDet.setIfHangUp((byte) 1);
                }
                searchPtlJobOrderDet.setStartPage(1);
                searchPtlJobOrderDet.setPageSize(9999);
                List<PtlJobOrderDetDto> ptlJobOrderDetDtoList = electronicTagFeignApi.findPtlJobOrderDetList(searchPtlJobOrderDet).getData();
                for (PtlJobOrderDetDto ptlJobOrderDetDto : ptlJobOrderDetDtoList) {
                    warehouseAreaCode = ptlJobOrderDetDto.getWarehouseAreaCode();
                    SearchPtlElectronicTagStorage searchPtlElectronicTagStorage = new SearchPtlElectronicTagStorage();
                    searchPtlElectronicTagStorage.setStorageId(ptlJobOrderDetDto.getStorageId().toString());
                    List<PtlElectronicTagStorageDto> ptlElectronicTagStorageDtoList = electronicTagFeignApi.findElectronicTagStorageList(searchPtlElectronicTagStorage).getData();
                    if (StringUtils.isEmpty(ptlElectronicTagStorageDtoList)) {
                        throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "请先维护储位：" + ptlJobOrderDetDto.getStorageCode() + "和物料：" + ptlJobOrderDetDto.getMaterialCode() + "以及对应的电子标签关联信息");
                    }
                    for (PtlElectronicTagStorageDto ptlElectronicTagStorageDto : ptlElectronicTagStorageDtoList) {
                        if (StringUtils.isEmpty(redisUtil.get(warehouseAreaCode + "_" + ptlElectronicTagStorageDto.getElectronicTagId())) ||
                                ptlJobOrderDetDto.getJobOrderDetId().equals(Long.valueOf(redisUtil.get(warehouseAreaCode + "_" + ptlElectronicTagStorageDto.getElectronicTagId()).toString()))) {
                            if (!electronicTagIds.contains(ptlElectronicTagStorageDto.getElectronicTagId())) {
                                // 通道灯亮灯
                                if (!equipmentAreaIds.contains(ptlElectronicTagStorageDto.getEquipmentAreaId())) {
                                    RabbitMQDTO rabbitMQDTO = new RabbitMQDTO();
                                    rabbitMQDTO.setEquipmentTagId(ptlElectronicTagStorageDto.getEquipmentTagId());
                                    rabbitMQDTO.setElectronicTagId(ptlElectronicTagStorageDto.getEquipmentAreaTagId());
                                    rabbitMQDTO.setPosition(ptlElectronicTagStorageDto.getPosition());
                                    rabbitMQDTO.setQueueName(ptlElectronicTagStorageDto.getQueueName());
                                    fanoutSender(1014, rabbitMQDTO, null);
                                    log.info("===========发送消息给客户端控制通道灯亮灯完成===============");
                                    equipmentAreaIds.add(ptlElectronicTagStorageDto.getEquipmentAreaId());
                                }

                                String materialDesc = "";
                                String materialDesc2 = "";
                                RabbitMQDTO rabbitMQDTO = new RabbitMQDTO();
                                rabbitMQDTO.setEquipmentTagId(ptlElectronicTagStorageDto.getEquipmentTagId());
                                rabbitMQDTO.setElectronicTagId(ptlElectronicTagStorageDto.getElectronicTagId());
                                if (ptlElectronicTagStorageDto.getElectronicTagLangType() == 1) {
                                    if (StringUtils.isNotEmpty(ptlJobOrderDetDto.getWholeQty()) && ptlJobOrderDetDto.getWholeQty().compareTo(BigDecimal.ZERO) != 0) {
                                        materialDesc += intercepting(ptlJobOrderDetDto.getMaterialName() + " ", 24 - ptlJobOrderDetDto.getWholeQty().toString().length() - ptlJobOrderDetDto.getWholeUnitName().length() * 2);
                                        materialDesc += ptlJobOrderDetDto.getWholeQty() + ptlJobOrderDetDto.getWholeUnitName();
                                    }
                                    if (StringUtils.isNotEmpty(ptlJobOrderDetDto.getScatteredQty()) && ptlJobOrderDetDto.getScatteredQty().compareTo(BigDecimal.ZERO) != 0) {
                                        materialDesc2 += intercepting(ptlJobOrderDetDto.getMaterialName() + " ", 24 - ptlJobOrderDetDto.getScatteredQty().toString().length() - ptlJobOrderDetDto.getScatteredUnitName().length() * 2);
                                        materialDesc2 += ptlJobOrderDetDto.getScatteredQty() + ptlJobOrderDetDto.getScatteredUnitName();
                                    }
                                    rabbitMQDTO.setMaterialDesc(materialDesc + materialDesc2);
                                } else {
                                    String materialCode = "";
                                    while (ptlJobOrderDetDto.getMaterialCode().length() + materialCode.length() < 8) {
                                        materialCode += " ";
                                    }
                                    materialCode += ptlJobOrderDetDto.getMaterialCode();
                                    while (materialCode.length() > 8) {
                                        materialCode = materialCode.substring(1);
                                    }
                                    rabbitMQDTO.setMaterialDesc(materialCode);
                                }
                                rabbitMQDTO.setOption1(ptlJobOrderDto.getOption1());
                                rabbitMQDTO.setOption2("1");
                                rabbitMQDTO.setOption3("0");
                                rabbitMQDTO.setElectronicTagLangType(ptlElectronicTagStorageDto.getElectronicTagLangType());
                                rabbitMQDTO.setQueueName(ptlElectronicTagStorageDto.getQueueName());
                                if (ptlElectronicTagStorageDto.getElectronicTagLangType() == 1) {
                                    listC.add(rabbitMQDTO);
                                } else {
                                    listE.add(rabbitMQDTO);
                                }
                                electronicTagIds.add(ptlElectronicTagStorageDto.getElectronicTagId());
                                map.put(warehouseAreaCode + "_" + ptlElectronicTagStorageDto.getElectronicTagId(), ptlJobOrderDetDto.getJobOrderDetId());
                            }
                        }
                    }

                    PtlJobOrderDet ptlJobOrderDet = new PtlJobOrderDet();
                    ptlJobOrderDet.setJobOrderDetId(ptlJobOrderDetDto.getJobOrderDetId());
                    ptlJobOrderDet.setJobStatus((byte) 2);
                    ptlJobOrderDet.setModifiedTime(new Date());
                    ptlJobOrderDet.setModifiedUserId(currentUser.getUserId());
                    ptlJobOrderDetList.add(ptlJobOrderDet);
                }
                PtlJobOrder ptlJobOrderUpdate = new PtlJobOrder();
                ptlJobOrderUpdate.setJobOrderId(ptlJobOrderDto.getJobOrderId());
                ptlJobOrderUpdate.setOrderStatus((byte) 2);
                ptlJobOrderUpdate.setActivationTime(new Date());
                ptlJobOrderUpdate.setModifiedTime(new Date());
                ptlJobOrderUpdate.setModifiedUserId(currentUser.getUserId());
                ptlJobOrderUpdate.setOption1(ptlJobOrderDto.getOption1());
                electronicTagFeignApi.updatePtlJobOrder(ptlJobOrderUpdate);
                electronicTagFeignApi.batchUpdatePtlJobOrderDet(ptlJobOrderDetList);
            }

            if (!listC.isEmpty()) {
                fanoutSender(1007, null, listC);
                log.info("===========发送消息给客户端控制中文标签亮灯完成===============");
            }
            if (!listE.isEmpty()) {
                fanoutSender(1007, null, listE);
                log.info("===========发送消息给客户端控制英文标签亮灯完成===============");
            }

            for (PtlJobOrderDto ptlJobOrderDto : ptlJobOrderDtos) {
                if (StringUtils.isEmpty(redisUtil.get(ptlJobOrderDto.getJobOrderCode()))) {
                    redisUtil.set(ptlJobOrderDto.getJobOrderCode(), ptlJobOrderDto.getSeq());
                    log.info("设置任务单：" + ptlJobOrderDto.getJobOrderCode() + " 作业顺序为：" + ptlJobOrderDto.getSeq());
                }
            }
            for (String key : map.keySet()) {
                if (StringUtils.isEmpty(redisUtil.get(key))) {
                    redisUtil.set(key, map.get(key));
                    log.info("设置电子标签：" + key + "当前对应拣货任务明细ID为：" + map.get(key));
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            redisUtil.unlock(lockKey, lockValue);
            log.info("=====================释放了:" + lockKey + "--->redisKEY");
        }

        return ptlJobOrderDtos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public ResponseEntityDTO createPtlJobOrder(List<PtlJobOrderDTO> ptlJobOrderDTOList) throws Exception {

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        List<PtlJobOrderDet> list = new LinkedList<>();
        List<RabbitMQDTO> rabbitMQDTOList = new LinkedList<>();
        List<Long> clientIdList = new LinkedList<>();
        List<String> cancelRelatedOrderList = new LinkedList<>();
        for (PtlJobOrderDTO ptlJobOrderDTO : ptlJobOrderDTOList) {
            if (cancelRelatedOrderList.contains(ptlJobOrderDTO.getCustomerNo())) {
                continue;
            }
            SearchPtlJobOrder searchPtlJobOrder = new SearchPtlJobOrder();
            searchPtlJobOrder.setJobOrderCode(ptlJobOrderDTO.getTaskNo());
            searchPtlJobOrder.setType(1);
            List<PtlJobOrderDto> ptlJobOrderDtoList = electronicTagFeignApi.findPtlJobOrderList(searchPtlJobOrder).getData();
            if (StringUtils.isNotEmpty(ptlJobOrderDtoList)) {
                List<String> cancelJobOrderList = new LinkedList<>();
                for (PtlJobOrderDto ptlJobOrderDto : ptlJobOrderDtoList) {
                    if (ptlJobOrderDto.getOrderStatus() == 5 && ptlJobOrderDto.getPickBackStatus() == 0) {
                        cancelJobOrderList.add(ptlJobOrderDto.getJobOrderCode());
                        if (!cancelRelatedOrderList.contains(ptlJobOrderDTO.getCustomerNo())) {
                            cancelRelatedOrderList.add(ptlJobOrderDTO.getCustomerNo());
                        }
                    }
                }
                continue;
//                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "重复分拣单");
            }
            SearchBaseWarehouse searchBaseWarehouse = new SearchBaseWarehouse();
            searchBaseWarehouse.setWarehouseCode(ptlJobOrderDTO.getWarehouseCode());
            searchBaseWarehouse.setCodeQueryMark(1);
            List<BaseWarehouse> baseWarehouses = baseFeignApi.findList(searchBaseWarehouse).getData();
            if (StringUtils.isEmpty(baseWarehouses)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到对应仓库信息");
            }
            PtlJobOrder ptlJobOrder = new PtlJobOrder();
            ptlJobOrder.setJobOrderCode(ptlJobOrderDTO.getTaskNo());
            ptlJobOrder.setJobOrderType((byte) 2);
            ptlJobOrder.setRelatedOrderCode(ptlJobOrderDTO.getCustomerNo());
            ptlJobOrder.setDespatchOrderCode(ptlJobOrderDTO.getDeliveryNo());
            ptlJobOrder.setWarehouseId(baseWarehouses.get(0).getWarehouseId());
            ptlJobOrder.setWarehouseName(baseWarehouses.get(0).getWarehouseName());
            ptlJobOrder.setOrderStatus((byte) 1);
            ptlJobOrder.setPickBackStatus((byte) 0);
            ptlJobOrder.setIfAlreadyPrint((byte) 0);
            ptlJobOrder.setStatus((byte) 1);
            ptlJobOrder.setRemark(ptlJobOrderDTO.getLineno());
            if (ptlJobOrderDTO.getLineno().length() == 1) {
                SearchTemVehicle searchTemVehicle = new SearchTemVehicle();
                searchTemVehicle.setVehicleCode("JH-FAST");
                List<TemVehicleDto> temVehicleDtoList = temVehicleFeignApi.findList(searchTemVehicle).getData();
                if (StringUtils.isNotEmpty(temVehicleDtoList)) {
                    ptlJobOrder.setVehicleId(temVehicleDtoList.get(0).getVehicleId());
                    redisUtil.set("JH-FAST", ptlJobOrderDTO.getTaskNo());
                }
            }
            ptlJobOrder.setOrgId(currentUser.getOrganizationId());
            ptlJobOrder.setCreateTime(new Date());
            ptlJobOrder.setCreateUserId(currentUser.getUserId());

            ptlJobOrder = electronicTagFeignApi.addPtlJobOrder(ptlJobOrder).getData();
            if (StringUtils.isEmpty(ptlJobOrder)) {
                throw new BizErrorException(ErrorCodeEnum.GL99990005);
            }
            String warehouseAreaCode = "";
            for (PtlJobOrderDetDTO ptlJobOrderDetDTO : ptlJobOrderDTO.getDetails()) {
                SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
                searchBaseMaterial.setMaterialCode(ptlJobOrderDetDTO.getGoodsCode());
                searchBaseMaterial.setCodeQueryMark(1);
                List<BaseMaterial> baseMaterials = baseFeignApi.findList(searchBaseMaterial).getData();
                if (StringUtils.isEmpty(baseMaterials)) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到对应物料信息：" + ptlJobOrderDetDTO.getGoodsCode());
                }
                SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
                searchBaseStorage.setStorageCode(ptlJobOrderDetDTO.getLocationCode());
                searchBaseStorage.setCodeQueryMark((byte) 1);
                List<BaseStorage> baseStorages = baseFeignApi.findList(searchBaseStorage).getData();
                if (StringUtils.isEmpty(baseStorages)) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到对应库位信息：" + ptlJobOrderDetDTO.getLocationCode());
                }
                SearchPtlElectronicTagStorage searchPtlElectronicTagStorage = new SearchPtlElectronicTagStorage();
                searchPtlElectronicTagStorage.setStorageId(baseStorages.get(0).getStorageId().toString());
                List<PtlElectronicTagStorageDto> ptlElectronicTagStorageDtoList = electronicTagFeignApi.findElectronicTagStorageList(searchPtlElectronicTagStorage).getData();
                if (StringUtils.isEmpty(ptlElectronicTagStorageDtoList)) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "请先维护储位：" + ptlJobOrderDetDTO.getLocationCode() + "对应的电子标签关联信息");
                }
                if (!ptlJobOrderDTO.getWarehouseCode().equals(ptlElectronicTagStorageDtoList.get(0).getWarehouseCode())) {
                    throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "任务号：" + ptlJobOrderDTO.getTaskNo() + "对应明细储位："
                            + ptlJobOrderDetDTO.getLocationCode() + "没有找到与仓库：" + ptlJobOrderDTO.getWarehouseCode() + "的关联关系");
                }
                // 是否有跨区域物料
                if (StringUtils.isNotEmpty(warehouseAreaCode) && !warehouseAreaCode.equals(ptlElectronicTagStorageDtoList.get(0).getWarehouseAreaCode())) {
                    throw new BizErrorException(ErrorCodeEnum.GL99990500.getCode(), "同一个任务单物料明细不能跨区域，物料编码：" + ptlJobOrderDetDTO.getGoodsCode());
                } else {
                    warehouseAreaCode = ptlElectronicTagStorageDtoList.get(0).getWarehouseAreaCode();
                }

                // 通过客户端Id获取蜂鸣器标签id
                if (!clientIdList.contains(ptlElectronicTagStorageDtoList.get(0).getClientId())) {
                    SearchPtlEquipment searchPtlEquipment = new SearchPtlEquipment();
                    searchPtlEquipment.setClientId(ptlElectronicTagStorageDtoList.get(0).getClientId());
                    searchPtlEquipment.setEquipmentType((byte) 2);
                    List<PtlEquipmentDto> ptlEquipmentDtoList = electronicTagFeignApi.findEquipmentList(searchPtlEquipment).getData();
                    if (StringUtils.isNotEmpty(ptlEquipmentDtoList)) {
                        RabbitMQDTO rabbitMQDTO = new RabbitMQDTO();
                        rabbitMQDTO.setEquipmentTagId(ptlElectronicTagStorageDtoList.get(0).getEquipmentTagId());
                        rabbitMQDTO.setElectronicTagId(ptlEquipmentDtoList.get(0).getEquipmentTagId());
                        rabbitMQDTO.setOption1("1");
                        rabbitMQDTO.setOption2("2");
                        rabbitMQDTO.setOption3("1");
                        rabbitMQDTO.setQueueName(ptlElectronicTagStorageDtoList.get(0).getQueueName());
                        rabbitMQDTOList.add(rabbitMQDTO);
                    }
                    clientIdList.add(ptlElectronicTagStorageDtoList.get(0).getClientId());
                }

                PtlJobOrderDet ptlJobOrderDet = new PtlJobOrderDet();
                ptlJobOrderDet.setJobOrderId(ptlJobOrder.getJobOrderId());
                ptlJobOrderDet.setStorageId(baseStorages.get(0).getStorageId());
                ptlJobOrderDet.setStorageCode(ptlJobOrderDetDTO.getLocationCode());
                ptlJobOrderDet.setMaterialId(baseMaterials.get(0).getMaterialId());
                ptlJobOrderDet.setMaterialCode(ptlJobOrderDetDTO.getGoodsCode());
                ptlJobOrderDet.setMaterialName(baseMaterials.get(0).getMaterialName());
                if (StringUtils.isNotEmpty(ptlJobOrderDetDTO.getW_qty())) {
                    if (ptlJobOrderDetDTO.getW_qty() - 0.0 > 0.000001) {
                        ptlJobOrderDet.setWholeOrScattered((byte) 1);
                        ptlJobOrderDet.setWholeQty(BigDecimal.valueOf(ptlJobOrderDetDTO.getW_qty()));
                        ptlJobOrderDet.setWholeUnitName(ptlJobOrderDetDTO.getW_unit());
                    } else {
                        ptlJobOrderDet.setWholeOrScattered((byte) 0);
                    }
                } else {
                    ptlJobOrderDet.setWholeOrScattered((byte) 0);
                }
                if (StringUtils.isNotEmpty(ptlJobOrderDetDTO.getL_qty())) {
                    if (ptlJobOrderDetDTO.getL_qty() - 0.0 > 0.000001) {
                        ptlJobOrderDet.setScatteredQty(BigDecimal.valueOf(ptlJobOrderDetDTO.getL_qty()));
                        ptlJobOrderDet.setScatteredUnitName(ptlJobOrderDetDTO.getL_unit());
                    }
                }
                ptlJobOrderDet.setSpec(ptlJobOrderDetDTO.getSpecification());
                ptlJobOrderDet.setJobStatus((byte) 1);
                ptlJobOrderDet.setStatus((byte) 1);
                ptlJobOrderDet.setOrgId(currentUser.getOrganizationId());
                ptlJobOrderDet.setCreateTime(new Date());
                ptlJobOrderDet.setCreateUserId(currentUser.getUserId());
                ptlJobOrderDet.setIsDelete((byte) 1);
                list.add(ptlJobOrderDet);
            }
        }
        if (StringUtils.isNotEmpty(list)) {
            ResponseEntity responseEntity = electronicTagFeignApi.batchSavePtlJobOrderDet(list);
            if (responseEntity.getCode() != 0) {
                throw new BizErrorException(ErrorCodeEnum.GL99990005);
            }
        }

        for (RabbitMQDTO rabbitMQDTO : rabbitMQDTOList) {
            fanoutSender(1005, rabbitMQDTO, null);
            log.info("===========发送消息给客户端控制蜂鸣器开启完成===============");
            fanoutSender(1004, rabbitMQDTO, null);
            log.info("===========发送消息给客户端控制蜂鸣器亮灯颜色和频率完成===============");
        }

        ResponseEntityDTO responseEntityDTO = new ResponseEntityDTO();
        responseEntityDTO.setCode(ptlJobOrderDTOList.get(0).getCustomerNo());
        if (cancelRelatedOrderList.isEmpty()) {
            responseEntityDTO.setMessage("保存成功");
        } else {
            responseEntityDTO.setMessage("拣货单：" + cancelRelatedOrderList.toString() + "已取消，但存在未退拣任务，请先进行退拣后再下发该单据。其他单据已正常接收完成！");
        }
        responseEntityDTO.setSuccess("s");

        return responseEntityDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public PtlJobOrderDto writeBackPtlJobOrder(Long jobOrderId, Integer type) throws Exception {

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        SearchPtlJobOrderDet searchPtlJobOrderDet1 = new SearchPtlJobOrderDet();
        searchPtlJobOrderDet1.setJobOrderId(jobOrderId);
        searchPtlJobOrderDet1.setStartPage(1);
        searchPtlJobOrderDet1.setPageSize(9999);
        List<PtlJobOrderDetDto> ptlJobOrderDetDtoList1 = electronicTagFeignApi.findPtlJobOrderDetList(searchPtlJobOrderDet1).getData();
        String lockKey = ptlJobOrderDetDtoList1.get(0).getWarehouseAreaCode() + "_lock";
        String lockValue = "";

        PtlJobOrderDto ptlJobOrderDto = null;
        try {
            if (redisUtil.lock(lockKey)) {
                lockValue = String.valueOf(redisUtil.get(lockKey));
                log.info("=====================获取到:" + lockKey + "--->redisKEY, " + lockValue + "--->redisVALUE");
            } else {
                throw new Exception("正在处理电子标签任务，请稍后再试！");
            }
            SearchPtlJobOrder searchPtlJobOrder = new SearchPtlJobOrder();
            searchPtlJobOrder.setJobOrderId(jobOrderId);
            searchPtlJobOrder.setType(1);
            List<PtlJobOrderDto> ptlJobOrderDtoList = electronicTagFeignApi.findPtlJobOrderList(searchPtlJobOrder).getData();
            ptlJobOrderDto = ptlJobOrderDtoList.get(0);
            if (ptlJobOrderDto.getOrderStatus() != 2) {
                String message = "";
                if (ptlJobOrderDto.getOrderStatus() == 1) {
                    message = "该任务单未激活，请先激活";
                }
                if (ptlJobOrderDto.getOrderStatus() == 3) {
                    message = "该任务单已完成";
                }
                if (ptlJobOrderDto.getOrderStatus() == 4) {
                    message = "该任务单处于异常状态，不可操作";
                }
                if (ptlJobOrderDto.getOrderStatus() == 5) {
                    message = "该任务单已取消";
                }
                if (ptlJobOrderDto.getOrderStatus() == 6) {
                    message = "该任务单已复核";
                }
                throw new BizErrorException(ErrorCodeEnum.GL99990500.getCode(), message);
            }

            // 判断是否还有未关闭的电子标签
            SearchPtlJobOrderDet searchPtlJobOrderDet = new SearchPtlJobOrderDet();
            searchPtlJobOrderDet.setJobOrderId(jobOrderId);
            if (ptlJobOrderDto.getOrderStatus() == 4) {
                searchPtlJobOrderDet.setIfHangUp((byte) 1);
            } else {
                searchPtlJobOrderDet.setJobStatus((byte) 2);
            }
            searchPtlJobOrderDet.setJobOrderDet(0);
            searchPtlJobOrderDet.setStartPage(1);
            searchPtlJobOrderDet.setPageSize(9999);
            List<PtlJobOrderDetDto> ptlJobOrderDetDtoList = electronicTagFeignApi.findPtlJobOrderDetList(searchPtlJobOrderDet).getData();
            List<RabbitMQDTO> list = new LinkedList<>();
            List<RabbitMQDTO> listC = new LinkedList<>();
            List<RabbitMQDTO> listE = new LinkedList<>();
            Map<String, Long> map = new HashMap<>();
            List<String> delRedis = new LinkedList<>();
            List<String> equipmentAreaIds = new LinkedList<>();
            for (PtlJobOrderDetDto ptlJobOrderDetDto : ptlJobOrderDetDtoList) {

                String key = ptlJobOrderDetDto.getWarehouseAreaCode() + "_" + ptlJobOrderDetDto.getElectronicTagId();
                if (StringUtils.isNotEmpty(redisUtil.get(key)) && ptlJobOrderDetDto.getJobOrderDetId().equals(Long.valueOf(redisUtil.get(key).toString()))) {
                    // 通道灯灭灯
                    if (!equipmentAreaIds.contains(ptlJobOrderDetDto.getEquipmentAreaId())) {
                        // 获取当前标签对应的所有拣货任务明细
                        SearchPtlJobOrderDet searchPtlJobOrderDetNow = new SearchPtlJobOrderDet();
                        searchPtlJobOrderDetNow.setEquipmentAreaId(ptlJobOrderDetDto.getEquipmentAreaId());
                        searchPtlJobOrderDetNow.setIfHangUp((byte) 1);
                        searchPtlJobOrderDetNow.setJobOrderDet(0);
                        searchPtlJobOrderDetNow.setStartPage(1);
                        searchPtlJobOrderDetNow.setPageSize(9999);
                        List<PtlJobOrderDetDto> ptlJobOrderDetDtos = electronicTagFeignApi.findPtlJobOrderDetList(searchPtlJobOrderDetNow).getData();
                        Boolean areaBoolean = true;
                        for (PtlJobOrderDetDto ptlJobOrderDetDtoNow : ptlJobOrderDetDtos) {
                            if (!ptlJobOrderDetDtoNow.getJobOrderId().equals(ptlJobOrderDetDto.getJobOrderId())) {
                                areaBoolean = false;
                                equipmentAreaIds.add(ptlJobOrderDetDto.getEquipmentAreaId());
                                break;
                            }
                        }
                        if (areaBoolean) {
                            RabbitMQDTO rabbitMQDTO = new RabbitMQDTO();
                            rabbitMQDTO.setEquipmentTagId(ptlJobOrderDetDto.getEquipmentTagId());
                            rabbitMQDTO.setElectronicTagId(ptlJobOrderDetDto.getEquipmentAreaTagId());
                            rabbitMQDTO.setPosition(ptlJobOrderDetDto.getPosition());
                            rabbitMQDTO.setQueueName(ptlJobOrderDetDto.getQueueName());
                            fanoutSender(1015, rabbitMQDTO, null);
                            log.info("===========发送消息给客户端控制任务单通道灯灭灯完成===============");
                            equipmentAreaIds.add(ptlJobOrderDetDto.getEquipmentAreaId());
                        }
                    }

                    RabbitMQDTO rabbitMQDTO = new RabbitMQDTO();
                    rabbitMQDTO.setEquipmentTagId(ptlJobOrderDetDto.getEquipmentTagId());
                    rabbitMQDTO.setElectronicTagId(ptlJobOrderDetDto.getElectronicTagId());
                    rabbitMQDTO.setQueueName(ptlJobOrderDetDto.getQueueName());
                    list.add(rabbitMQDTO);

                    // 判断当前储位是否还有其他拣货任务需要亮灯
                    Long nextJobOrderDetId = getNextJobOrderDet(ptlJobOrderDetDto, 1);
                    if (nextJobOrderDetId != 0) {
                        map.put(key, nextJobOrderDetId);
                        getNextMaterial(nextJobOrderDetId, listC, listE);
                    } else {
                        delRedis.add(key);
                    }
                }
            }
            PtlJobOrder ptlJobOrder = new PtlJobOrder();
            ptlJobOrder.setJobOrderId(ptlJobOrderDto.getJobOrderId());
            ptlJobOrder.setOrderStatus((byte) 3);
            ptlJobOrder.setModifiedUserId(currentUser.getUserId());
            ptlJobOrder.setModifiedTime(new Date());
            electronicTagFeignApi.updatePtlJobOrder(ptlJobOrder);

            PtlJobOrderDet ptlJobOrderDet = new PtlJobOrderDet();
            ptlJobOrderDet.setJobOrderId(jobOrderId);
            ptlJobOrderDet.setJobStatus((byte) 3);
            ptlJobOrderDet.setModifiedUserId(currentUser.getUserId());
            ptlJobOrderDet.setModifiedTime(new Date());
            electronicTagFeignApi.updateByJobOrderId(ptlJobOrderDet);

            if (type == 1) {
                SearchPtlJobOrder searchPtlJobOrderWB = new SearchPtlJobOrder();
                searchPtlJobOrderWB.setRelatedOrderCode(ptlJobOrderDto.getRelatedOrderCode());
                searchPtlJobOrderWB.setNotOrderStatus((byte) 3);
                searchPtlJobOrderWB.setType(1);
                searchPtlJobOrderWB.setAllwarehouseAreaId(1);
                List<PtlJobOrderDto> ptlJobOrderWBDtoList = electronicTagFeignApi.findPtlJobOrderList(searchPtlJobOrderWB).getData();
                if (ptlJobOrderWBDtoList.size() == 1) {
                    PtlJobOrderDTO ptlJobOrderDTO = new PtlJobOrderDTO();
                    ptlJobOrderDTO.setCustomerNo(ptlJobOrderDto.getRelatedOrderCode());
                    ptlJobOrderDTO.setWarehouseCode(ptlJobOrderDto.getWarehouseCode());
                    ptlJobOrderDTO.setWorkerCode(ptlJobOrderDto.getWorkerUserCode());
                    ptlJobOrderDTO.setStatus("F");
                    // 回写PTL作业任务
                    log.info("电子作业标签完成/异常，回传WMS" + JSONObject.toJSONString(ptlJobOrderDTO));
                    String result = RestTemplateUtil.postJsonStrForString(ptlJobOrderDTO, finishPtlJobOrderUrl);
                    log.info("电子作业标签回写返回信息：" + result);
                    ResponseEntityDTO responseEntityDTO = JsonUtils.jsonToPojo(result, ResponseEntityDTO.class);
                    if (!"s".equals(responseEntityDTO.getSuccess())) {
                        throw new Exception("电子作业标签完成，回传WMS失败：" + responseEntityDTO.getMessage());
                    }
                }
            }

            if (!list.isEmpty()) {
                fanoutSender(1003, null, list);
                log.info("===========发送消息给客户端控制剩余电子标签灭灯完成===============");
            }
            if (!listC.isEmpty()) {
                fanoutSender(1007, null, listC);
                log.info("===========发送消息给客户端控制另一个中文标签亮灯完成===============");
            }
            if (!listE.isEmpty()) {
                fanoutSender(1007, null, listE);
                log.info("===========发送消息给客户端控制另一个英文标签亮灯完成===============");
            }
            updateJobOrderSeq(ptlJobOrderDetDtoList.get(0).getWarehouseAreaId(), ptlJobOrderDto.getJobOrderCode());
            for (String key : map.keySet()) {
                redisUtil.set(key, map.get(key));
                log.info("修改电子标签：" + key + "当前对应拣货任务明细ID为：" + map.get(key));
            }
            for (String redisKey : delRedis) {
                log.info("删除当前电子标签redisKey：" + redisKey + ", 值为：" + redisUtil.get(redisKey));
                redisUtil.del(redisKey);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            redisUtil.unlock(lockKey, lockValue);
            log.info("=====================释放了:" + lockKey + "--->redisKEY");
        }

        return ptlJobOrderDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public List<PtlJobOrderDetPrintDTO> printPtlJobOrderLabel(String ids, Long workUserId, Integer type) throws Exception {

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        SysUser sysUser = securityFeignApi.selectUserById(workUserId).getData();
        SearchBaseWorker searchBaseWorker = new SearchBaseWorker();
        searchBaseWorker.setUserId(currentUser.getUserId());
        List<BaseWorkerDto> baseWorkerDtos = baseFeignApi.findList(searchBaseWorker).getData();
        if (StringUtils.isNotEmpty(baseWorkerDtos)) {
            if (baseWorkerDtos.get(0).getBaseWorkingAreaReWDtoList().size() == 1) {
                redisUtil.set(baseWorkerDtos.get(0).getBaseWorkingAreaReWDtoList().get(0).getWorkingAreaCode(), sysUser);
            }
        }

        String[] jobOrderIds = ids.split(",");
        List<PtlJobOrderDetPrintDTO> ptlJobOrderDetPrintDTOList = new LinkedList<>();

        SearchPtlJobOrderDet searchPtlJobOrderDet1 = new SearchPtlJobOrderDet();
        searchPtlJobOrderDet1.setJobOrderId(Long.valueOf(jobOrderIds[0]));
        List<PtlJobOrderDetDto> ptlJobOrderDetDtoList1 = electronicTagFeignApi.findPtlJobOrderDetList(searchPtlJobOrderDet1).getData();
        String lockKey = ptlJobOrderDetDtoList1.get(0).getWarehouseAreaCode() + "_lock";
        String lockValue = "";
        String endVehicleCode = "";
        String relatedOrderCode = "";
        try {
            if (redisUtil.lock(lockKey)) {
                lockValue = String.valueOf(redisUtil.get(lockKey));
                log.info("=====================获取到:" + lockKey + "--->redisKEY, " + lockValue + "--->redisVALUE");
            } else {
                throw new Exception("正在处理电子标签任务，请稍后再试！");
            }
            String queueName = RabbitConfig.TOPIC_QUEUE_PRINT;
            for (String jobOrderId : jobOrderIds) {
                PtlJobOrder ptlJobOrder = electronicTagFeignApi.ptlJobOrderDetail(Long.valueOf(jobOrderId)).getData();
                String color = "";
                if (StringUtils.isNotEmpty(ptlJobOrder.getOption1())) {
                    switch (ptlJobOrder.getOption1()) {
                        case "0":
                            color = "红色";
                            break;
                        case "1":
                            color = "绿色";
                            break;
                        case "2":
                            color = "黄色";
                            break;
                    }
                }
//        if (ptlJobOrder.getIfAlreadyPrint() == 1) {
//            throw new Exception("该任务单已打印过标签，请不要重复操作");
//        }
                String vehicleCode = "";
                if (StringUtils.isNotEmpty(ptlJobOrder.getVehicleId())) {
                    TemVehicle temVehicle = temVehicleFeignApi.detail(ptlJobOrder.getVehicleId()).getData();
                    vehicleCode = temVehicle.getVehicleCode();
                } else if (StringUtils.isNotEmpty(redisUtil.get(ptlJobOrder.getRelatedOrderCode()))) {
                    vehicleCode = redisUtil.get(ptlJobOrder.getRelatedOrderCode()).toString();
                }
                if (StringUtils.isEmpty(vehicleCode)) {
                    SearchTemVehicle searchTemVehicle = new SearchTemVehicle();
                    searchTemVehicle.setVehicleStatus((byte) 1);
                    searchTemVehicle.setStartPage(1);
                    searchTemVehicle.setPageSize(9999);
                    List<TemVehicleDto> temVehicleDtoList = temVehicleFeignApi.findList(searchTemVehicle).getData();
                    for (TemVehicleDto temVehicleDto : temVehicleDtoList) {
                        if (StringUtils.isNotEmpty(redisUtil.get(temVehicleDto.getVehicleCode() + "_count"))) {
                            temVehicleDto.setCount(Long.valueOf(redisUtil.get(temVehicleDto.getVehicleCode() + "_count").toString()));
                        } else {
                            temVehicleDto.setCount(Long.valueOf(0));
                        }
                    }
                    temVehicleDtoList.sort(Comparator.comparing(TemVehicleDto::getCount));
                    for (TemVehicleDto temVehicleDto : temVehicleDtoList) {
                        if (!"JH-FAST".equals(temVehicleDto.getVehicleCode()) && StringUtils.isEmpty(redisUtil.get(temVehicleDto.getVehicleCode()))) {
                            vehicleCode = temVehicleDto.getVehicleCode();
                            redisUtil.set(temVehicleDto.getVehicleCode(), 1, 5);
                            log.info("开始打印，redis锁定集货位：" + vehicleCode);
                            endVehicleCode = vehicleCode;
                            redisUtil.set(ptlJobOrder.getRelatedOrderCode(), temVehicleDto.getVehicleCode(), 5);
                            log.info("开始打印，redis锁定拣货单：" + ptlJobOrder.getRelatedOrderCode());
                            relatedOrderCode = ptlJobOrder.getRelatedOrderCode();

                            TemVehicle temVehicle = new TemVehicle();
                            temVehicle.setVehicleId(temVehicleDto.getVehicleId());
                            temVehicle.setVehicleStatus((byte) 3);
                            temVehicle.setModifiedTime(new Date());
                            temVehicle.setModifiedUserId(currentUser.getUserId());
                            temVehicleFeignApi.update(temVehicle);

                            PtlJobOrder ptlJobOrderUpdate = new PtlJobOrder();
                            ptlJobOrderUpdate.setRelatedOrderCode(ptlJobOrder.getRelatedOrderCode());
                            ptlJobOrderUpdate.setVehicleId(temVehicleDto.getVehicleId());
                            ptlJobOrderUpdate.setModifiedTime(new Date());
                            ptlJobOrderUpdate.setModifiedUserId(currentUser.getUserId());
                            electronicTagFeignApi.updateByRelatedOrderCode(ptlJobOrderUpdate);
                            break;
                        }
                    }
                    if (StringUtils.isEmpty(temVehicleDtoList, vehicleCode)) {
                        throw new Exception("空闲集货位不足，请稍后再操作");
                    }
                }
                SearchPtlJobOrderDet searchPtlJobOrderDet = new SearchPtlJobOrderDet();
                searchPtlJobOrderDet.setJobOrderId(Long.valueOf(jobOrderId));
                searchPtlJobOrderDet.setStartPage(1);
                searchPtlJobOrderDet.setPageSize(9999);
                List<PtlJobOrderDetDto> ptlJobOrderDetDtoList = electronicTagFeignApi.findPtlJobOrderDetList(searchPtlJobOrderDet).getData();
                Boolean scatteredBoolean = true;
                for (PtlJobOrderDetDto ptlJobOrderDetDto : ptlJobOrderDetDtoList) {
                    queueName = ptlJobOrderDetDto.getQueueName() + ".print";
                    if (ptlJobOrderDetDto.getWholeOrScattered() == 1) {

//                int i = 1;
//                int cartonNum = ptlJobOrderDetDto.getWholeQty().intValue();
//                while (cartonNum > 0) {
                        PtlJobOrderDetPrintDTO ptlJobOrderDetPrintDTO = new PtlJobOrderDetPrintDTO();
                        ptlJobOrderDetPrintDTO.setJobOrderId(ptlJobOrderDetDto.getJobOrderId());
                        ptlJobOrderDetPrintDTO.setJobOrderCode(ptlJobOrder.getRelatedOrderCode());
                        ptlJobOrderDetPrintDTO.setDespatchOrderCode(color);
                        ptlJobOrderDetPrintDTO.setRelatedOrderCode(ptlJobOrder.getRelatedOrderCode());
                        if (ptlJobOrder.getIfUrgent() == 1) {
                            ptlJobOrderDetPrintDTO.setIfUrgent("急");
                        } else {
                            ptlJobOrderDetPrintDTO.setIfUrgent("");
                        }
                        ptlJobOrderDetPrintDTO.setMaterialName(ptlJobOrderDetDto.getMaterialName());
                        ptlJobOrderDetPrintDTO.setMaterialCode(ptlJobOrderDetDto.getMaterialCode());
                        ptlJobOrderDetPrintDTO.setSpec(ptlJobOrderDetDto.getSpec());
                        ptlJobOrderDetPrintDTO.setWarehouseAreaCode(ptlJobOrderDetDto.getWarehouseAreaCode());
                        ptlJobOrderDetPrintDTO.setStorageCode(ptlJobOrderDetDto.getStorageCode());
                        ptlJobOrderDetPrintDTO.setWorkerUserName(sysUser.getNickName());
                        ptlJobOrderDetPrintDTO.setWholeOrScattered(ptlJobOrderDetDto.getWholeOrScattered());
//                    ptlJobOrderDetPrintDTO.setCartonCode(i + "-" + ptlJobOrderDetDto.getWholeQty().intValue());
                        ptlJobOrderDetPrintDTO.setCartonCode(" ");
                        ptlJobOrderDetPrintDTO.setVehicleCode(vehicleCode + "(" + ptlJobOrder.getRemark() + ")");
                        ptlJobOrderDetPrintDTO.setQueueName(queueName);
                        ptlJobOrderDetPrintDTOList.add(ptlJobOrderDetPrintDTO);

//                    cartonNum--;
//                    i++;
//                }
                    }
                    if (scatteredBoolean && StringUtils.isNotEmpty(ptlJobOrderDetDto.getScatteredQty())) {
                        if (ptlJobOrderDetDto.getScatteredQty().compareTo(BigDecimal.ZERO) == 1) {
                            PtlJobOrderDetPrintDTO ptlJobOrderDetPrintDTO = new PtlJobOrderDetPrintDTO();
                            ptlJobOrderDetPrintDTO.setJobOrderId(ptlJobOrderDetDto.getJobOrderId());
                            ptlJobOrderDetPrintDTO.setJobOrderCode(ptlJobOrder.getRelatedOrderCode());
                            ptlJobOrderDetPrintDTO.setDespatchOrderCode(color);
                            ptlJobOrderDetPrintDTO.setRelatedOrderCode(ptlJobOrder.getRelatedOrderCode());
                            if (ptlJobOrder.getIfUrgent() == 1) {
                                ptlJobOrderDetPrintDTO.setIfUrgent("急");
                            } else {
                                ptlJobOrderDetPrintDTO.setIfUrgent("");
                            }
                            ptlJobOrderDetPrintDTO.setMaterialName(ptlJobOrderDetDto.getMaterialName());
                            ptlJobOrderDetPrintDTO.setMaterialCode(ptlJobOrderDetDto.getMaterialCode());
                            ptlJobOrderDetPrintDTO.setSpec(ptlJobOrderDetDto.getSpec());
                            ptlJobOrderDetPrintDTO.setWarehouseAreaCode(ptlJobOrderDetDto.getWarehouseAreaCode());
                            ptlJobOrderDetPrintDTO.setStorageCode(ptlJobOrderDetDto.getStorageCode());
                            ptlJobOrderDetPrintDTO.setWorkerUserName(sysUser.getNickName());
                            ptlJobOrderDetPrintDTO.setWholeOrScattered((byte) 0);
                            ptlJobOrderDetPrintDTO.setVehicleCode(vehicleCode + "(" + ptlJobOrder.getRemark() + ")");
                            ptlJobOrderDetPrintDTO.setQueueName(queueName);
                            ptlJobOrderDetPrintDTOList.add(ptlJobOrderDetPrintDTO);

                            scatteredBoolean = false;
                        }
                    }
                }

                PtlJobOrder ptlJobOrderUpdate = new PtlJobOrder();
                ptlJobOrderUpdate.setJobOrderId(ptlJobOrder.getJobOrderId());
                ptlJobOrderUpdate.setIfAlreadyPrint((byte) 1);
                ptlJobOrderUpdate.setWorkerUserId(workUserId);
                ptlJobOrderUpdate.setWorkerUserName(sysUser.getNickName());
                ptlJobOrderUpdate.setModifiedTime(new Date());
                ptlJobOrderUpdate.setModifiedUserId(currentUser.getUserId());
                electronicTagFeignApi.updatePtlJobOrder(ptlJobOrderUpdate);
            }

            if (type == 0) {
                MQResponseEntity mQResponseEntity = new MQResponseEntity<>();
                mQResponseEntity.setCode(1101);
                mQResponseEntity.setData(ptlJobOrderDetPrintDTOList);
                log.info("===========开始发送消息给打印客户端===============");
                fanoutSender.send(queueName, JSONObject.toJSONString(mQResponseEntity));
                log.info("===========队列名称:" + queueName);
                log.info("===========消息内容:" + JSONObject.toJSONString(mQResponseEntity));
                log.info("===========发送消息给打印客户端打印整、零标签完成===============");
            }
        } catch (Exception e) {
            log.info(e.toString());
            redisUtil.del(endVehicleCode);
            log.info("打印失败，redis释放集货位：" + endVehicleCode);
            redisUtil.del(relatedOrderCode);
            log.info("打印失败，redis释放拣货单：" + relatedOrderCode);
            throw new Exception(e.getMessage());
        } finally {
            redisUtil.unlock(lockKey, lockValue);
            log.info("=====================释放了:" + lockKey + "--->redisKEY");
        }

        return ptlJobOrderDetPrintDTOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int hangUpPtlJobOrderDet(String ids) throws Exception {

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<String> jobOrderDetIds = Arrays.asList(ids.split(","));

        SearchPtlJobOrderDet searchPtlJobOrderDet1 = new SearchPtlJobOrderDet();
        searchPtlJobOrderDet1.setJobOrderDetId(Long.valueOf(jobOrderDetIds.get(0)));
        searchPtlJobOrderDet1.setStartPage(1);
        searchPtlJobOrderDet1.setPageSize(9999);
        List<PtlJobOrderDetDto> ptlJobOrderDetDtoList1 = electronicTagFeignApi.findPtlJobOrderDetList(searchPtlJobOrderDet1).getData();
        String lockKey = ptlJobOrderDetDtoList1.get(0).getWarehouseAreaCode() + "_lock";
        String lockValue = "";

        try {
            if (redisUtil.lock(lockKey)) {
                lockValue = String.valueOf(redisUtil.get(lockKey));
                log.info("=====================获取到:" + lockKey + "--->redisKEY, " + lockValue + "--->redisVALUE");
            } else {
                throw new Exception("正在处理电子标签任务，请稍后再试！");
            }
            List<RabbitMQDTO> list = new LinkedList<>();
            List<RabbitMQDTO> listC = new LinkedList<>();
            List<RabbitMQDTO> listE = new LinkedList<>();
            Map<String, Long> map = new HashMap<>();
            List<String> delRedis = new LinkedList<>();
            Long jobOrderId = 0l;
            String jobOrderCode = "";
            for (String jobOrderDetId : jobOrderDetIds) {

                SearchPtlJobOrderDet searchPtlJobOrderDet = new SearchPtlJobOrderDet();
                searchPtlJobOrderDet.setJobOrderDetId(Long.valueOf(jobOrderDetId));
                searchPtlJobOrderDet.setJobOrderDet(0);
                List<PtlJobOrderDetDto> ptlJobOrderDetDtoList = electronicTagFeignApi.findPtlJobOrderDetList(searchPtlJobOrderDet).getData();
                if (ptlJobOrderDetDtoList.get(0).getJobStatus() != 2) {
                    throw new Exception("未激活作业的拣货物料不能挂起！");
                }
                jobOrderId = ptlJobOrderDetDtoList.get(0).getJobOrderId();
                jobOrderCode = ptlJobOrderDetDtoList.get(0).getJobOrderCode();

                // 判断当前标签亮灯是否属于该拣货任务明细
                for (PtlJobOrderDetDto ptlJobOrderDetDto : ptlJobOrderDetDtoList) {
                    String key = ptlJobOrderDetDtoList.get(0).getWarehouseAreaCode() + "_" + ptlJobOrderDetDto.getElectronicTagId();
                    if (StringUtils.isNotEmpty(redisUtil.get(key)) && jobOrderDetId.equals(redisUtil.get(key).toString())) {

                        RabbitMQDTO rabbitMQDTO = new RabbitMQDTO();
                        rabbitMQDTO.setEquipmentTagId(ptlJobOrderDetDto.getEquipmentTagId());
                        rabbitMQDTO.setElectronicTagId(ptlJobOrderDetDto.getElectronicTagId());
                        rabbitMQDTO.setQueueName(ptlJobOrderDetDto.getQueueName());
                        list.add(rabbitMQDTO);

                        // 判断当前储位是否还有其他拣货任务需要亮灯
                        Long nextJobOrderDetId = getNextJobOrderDet(ptlJobOrderDetDto, 0);
                        if (nextJobOrderDetId != 0) {
                            map.put(key, nextJobOrderDetId);
                            getNextMaterial(nextJobOrderDetId, listC, listE);
                        } else {
                            delRedis.add(key);
                        }
                    }
                }
                PtlJobOrderDet ptlJobOrderDet = new PtlJobOrderDet();
                ptlJobOrderDet.setJobOrderDetId(Long.valueOf(jobOrderDetId));
                ptlJobOrderDet.setJobStatus((byte) 4);
                ptlJobOrderDet.setModifiedTime(new Date());
                ptlJobOrderDet.setModifiedUserId(currentUser.getUserId());
                electronicTagFeignApi.updatePtlJobOrderDet(ptlJobOrderDet);

                PtlJobOrder ptlJobOrder = new PtlJobOrder();
                ptlJobOrder.setJobOrderId(ptlJobOrderDetDtoList.get(0).getJobOrderId());
                ptlJobOrder.setOrderStatus((byte) 4);
                ptlJobOrder.setModifiedTime(new Date());
                ptlJobOrder.setModifiedUserId(currentUser.getUserId());
                electronicTagFeignApi.updatePtlJobOrder(ptlJobOrder);
            }
            SearchPtlJobOrderDet searchPtlJobOrderDet = new SearchPtlJobOrderDet();
            searchPtlJobOrderDet.setJobOrderId(jobOrderId);
            searchPtlJobOrderDet.setJobStatus((byte) 2);
            List<PtlJobOrderDetDto> ptlJobOrderDetDtoList = electronicTagFeignApi.findPtlJobOrderDetList(searchPtlJobOrderDet).getData();
            if (ptlJobOrderDetDtoList.size() == jobOrderDetIds.size()) {
                // 该任务单剩余激活任务全部被挂起，判定该单暂不参与亮灯激活顺序中
                updateJobOrderSeq(ptlJobOrderDetDtoList.get(0).getWarehouseAreaId(), jobOrderCode);
                log.info("拣货任务单已完全挂起：" + jobOrderCode);
            }

            if (!list.isEmpty()) {
                fanoutSender(1003, null, list);
                log.info("===========发送消息给客户端控制挂起电子标签灭灯完成===============");
            }

            if (!listC.isEmpty()) {
                fanoutSender(1007, null, listC);
                log.info("===========发送消息给客户端控制另一个中文标签亮灯完成===============");
            }
            if (!listE.isEmpty()) {
                fanoutSender(1007, null, listE);
                log.info("===========发送消息给客户端控制另一个英文标签亮灯完成===============");
            }
            for (String key : map.keySet()) {
                redisUtil.set(key, map.get(key));
                log.info("修改电子标签：" + key + "当前对应拣货任务明细ID为：" + map.get(key));
            }
            for (String redisKey : delRedis) {
                log.info("删除当前电子标签redisKey：" + redisKey + ", 值为：" + redisUtil.get(redisKey));
                redisUtil.del(redisKey);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            redisUtil.unlock(lockKey, lockValue);
            log.info("=====================释放了:" + lockKey + "--->redisKEY");
        }

        return 1;
    }

    @Override
    @Transactional
    @LcnTransaction
    public ResponseEntityDTO cancelPtrlJobOrder(PtlJobOrderDTO ptlJobOrderDTO) throws Exception {

        if ("C".equals(ptlJobOrderDTO.getStatus())) {
            SearchPtlJobOrder searchPtlJobOrder = new SearchPtlJobOrder();
            searchPtlJobOrder.setRelatedOrderCode(ptlJobOrderDTO.getCustomerNo());
            searchPtlJobOrder.setType(1);
            List<PtlJobOrderDto> ptlJobOrderDtoList = electronicTagFeignApi.findPtlJobOrderList(searchPtlJobOrder).getData();
            String lockKey = ptlJobOrderDtoList.get(0).getWarehouseAreaCode() + "_lock";
            String lockValue = "";
            try {
                if (redisUtil.lock(lockKey)) {
                    lockValue = String.valueOf(redisUtil.get(lockKey));
                    log.info("=====================获取到:" + lockKey + "--->redisKEY, " + lockValue + "--->redisVALUE");
                } else {
                    throw new Exception("正在处理电子标签任务，请稍后再试！");
                }
                Boolean temBoolean = true;
                long vehicleId = 0l;
                List<RabbitMQDTO> list = new LinkedList<>();
                List<PtlJobOrderDet> ptlJobOrderDetList = new LinkedList<>();
                for (PtlJobOrderDto ptlJobOrderDto : ptlJobOrderDtoList) {

                    if (ptlJobOrderDto.getOrderStatus() == 5 || ptlJobOrderDto.getOrderStatus() == 6) {
                        continue;
                    }

                    if (StringUtils.isNotEmpty(ptlJobOrderDto.getVehicleId())) {
                        vehicleId = ptlJobOrderDto.getVehicleId();
                    }

                    PtlJobOrder ptlJobOrder = new PtlJobOrder();
                    ptlJobOrder.setJobOrderId(ptlJobOrderDto.getJobOrderId());
                    ptlJobOrder.setOrderStatus((byte) 5);
                    if (ptlJobOrderDto.getOrderStatus() == 1 || ptlJobOrderDto.getOrderStatus() == 3) {
                        ptlJobOrder.setPickBackStatus((byte) 1);
                        ptlJobOrder.setStatus((byte) 0);
                    }
                    ptlJobOrder.setModifiedTime(new Date());
                    electronicTagFeignApi.updatePtlJobOrder(ptlJobOrder);

                    if (ptlJobOrderDto.getOrderStatus() == 1 || ptlJobOrderDto.getOrderStatus() == 3) {
                        PtlJobOrderDet ptlJobOrderDet = new PtlJobOrderDet();
                        ptlJobOrderDet.setJobOrderId(ptlJobOrderDto.getJobOrderId());
                        ptlJobOrderDet.setJobStatus((byte) 3);
                        ptlJobOrderDet.setStatus((byte) 0);
                        ptlJobOrderDet.setModifiedTime(new Date());
                        ptlJobOrderDetList.add(ptlJobOrderDet);
                        continue;
                    }

                    SearchPtlJobOrderDet searchPtlJobOrderDet = new SearchPtlJobOrderDet();
                    searchPtlJobOrderDet.setJobOrderId(ptlJobOrderDto.getJobOrderId());
                    searchPtlJobOrderDet.setIfHangUp((byte) 1);
                    searchPtlJobOrderDet.setJobOrderDet(0);
                    searchPtlJobOrderDet.setStartPage(1);
                    searchPtlJobOrderDet.setPageSize(9999);
                    List<PtlJobOrderDetDto> ptlJobOrderDetDtoList = electronicTagFeignApi.findPtlJobOrderDetList(searchPtlJobOrderDet).getData();
                    Boolean canelBoolean = true;
                    for (PtlJobOrderDetDto ptlJobOrderDetDto : ptlJobOrderDetDtoList) {
                        if (ptlJobOrderDetDto.getElectronicTagLangType() == 1) {
                            String key = ptlJobOrderDetDtoList.get(0).getWarehouseAreaCode() + "_" + ptlJobOrderDetDto.getElectronicTagId();
                            if (StringUtils.isEmpty(redisUtil.get(key)) || ptlJobOrderDetDto.getJobOrderDetId().equals(Long.valueOf(redisUtil.get(key).toString()))) {
                                RabbitMQDTO rabbitMQDTO = new RabbitMQDTO();
                                rabbitMQDTO.setEquipmentTagId(ptlJobOrderDetDto.getEquipmentTagId());
                                rabbitMQDTO.setElectronicTagId(ptlJobOrderDetDto.getElectronicTagId());
                                rabbitMQDTO.setMaterialDesc("拣货单：" + ptlJobOrderDTO.getCustomerNo() + " 已取消, 请退拣");
                                rabbitMQDTO.setOption1(ptlJobOrderDto.getOption1());
                                rabbitMQDTO.setOption2("1");
                                rabbitMQDTO.setOption3("0");
                                rabbitMQDTO.setQueueName(ptlJobOrderDetDto.getQueueName());
                                list.add(rabbitMQDTO);

                                canelBoolean = false;
                                temBoolean = false;
                            } else {
                                PtlJobOrderDet ptlJobOrderDet = new PtlJobOrderDet();
                                ptlJobOrderDet.setJobOrderDetId(ptlJobOrderDetDto.getJobOrderDetId());
                                ptlJobOrderDet.setJobStatus((byte) 3);
                                ptlJobOrderDet.setStatus((byte) 0);
                                ptlJobOrderDet.setModifiedTime(new Date());
                                ptlJobOrderDetList.add(ptlJobOrderDet);
                            }
                        }
                    }
                    if (canelBoolean) {
                        PtlJobOrder ptlJobOrderUpdate = new PtlJobOrder();
                        ptlJobOrderUpdate.setJobOrderId(ptlJobOrderDto.getJobOrderId());
                        ptlJobOrderUpdate.setPickBackStatus((byte) 1);
                        ptlJobOrderUpdate.setStatus((byte) 0);
                        electronicTagFeignApi.updatePtlJobOrder(ptlJobOrderUpdate);

                        // 当前取消任务单没有退拣物料时，任务单结束完成
                        updateJobOrderSeq(ptlJobOrderDetDtoList.get(0).getWarehouseAreaId(), ptlJobOrderDto.getJobOrderCode());
                    }
                }

                if (vehicleId != 0 && temBoolean) {
                    TemVehicle temVehicle = new TemVehicle();
                    temVehicle.setVehicleId(vehicleId);
                    temVehicle.setVehicleStatus((byte) 1);
                    temVehicle.setModifiedTime(new Date());
                    temVehicleFeignApi.update(temVehicle);
                }

                if (StringUtils.isNotEmpty(ptlJobOrderDetList)) {
                    electronicTagFeignApi.batchUpdatePtlJobOrderDet(ptlJobOrderDetList);
                }

                if (StringUtils.isNotEmpty(list)) {
                    fanoutSender(1007, null, list);
                    log.info("===========发送消息给客户端控制拣货单号取消提示完成===============");
                }
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            } finally {
                redisUtil.unlock(lockKey, lockValue);
                log.info("=====================释放了:" + lockKey + "--->redisKEY");
            }
        } else {
            PtlJobOrder ptlJobOrder = new PtlJobOrder();
            ptlJobOrder.setRelatedOrderCode(ptlJobOrderDTO.getCustomerNo());
            ptlJobOrder.setOrderStatus((byte) 6);
            ptlJobOrder.setModifiedTime(new Date());
            electronicTagFeignApi.updateByRelatedOrderCode(ptlJobOrder);

            SearchPtlJobOrder searchPtlJobOrder = new SearchPtlJobOrder();
            searchPtlJobOrder.setRelatedOrderCode(ptlJobOrderDTO.getCustomerNo());
            searchPtlJobOrder.setType(1);
            List<PtlJobOrderDto> ptlJobOrderDtoList = electronicTagFeignApi.findPtlJobOrderList(searchPtlJobOrder).getData();
            List<String> jobOrderCodeList = new LinkedList<>();
            Boolean finishBoolean = true;
            for (PtlJobOrderDto ptlJobOrderDto : ptlJobOrderDtoList) {
                if (ptlJobOrderDto.getOrderStatus() != 3 && ptlJobOrderDto.getOrderStatus() != 6) {
                    jobOrderCodeList.add(ptlJobOrderDto.getJobOrderCode());
                }
                if (ptlJobOrderDto.getOrderStatus() == 6) {
                    finishBoolean = false;
                }
            }
            if (StringUtils.isNotEmpty(jobOrderCodeList)) {
                throw new Exception("拣货单对应的拣货任务：" + jobOrderCodeList.toString() + "未完成，暂不能进行复核！");
            }
            if (finishBoolean) {
                TemVehicle temVehicle = new TemVehicle();
                temVehicle.setVehicleId(ptlJobOrderDtoList.get(0).getVehicleId());
                temVehicle.setVehicleStatus((byte) 1);
                temVehicle.setModifiedTime(new Date());
                temVehicleFeignApi.update(temVehicle);
            }
        }

        ResponseEntityDTO responseEntityDTO = new ResponseEntityDTO();
        responseEntityDTO.setCode(ptlJobOrderDTO.getCustomerNo());
        responseEntityDTO.setMessage("保存成功");
        responseEntityDTO.setSuccess("s");

        return responseEntityDTO;
    }

    @Override
    @Transactional
    @LcnTransaction
    public int ptlJobOrderLightOff(Long jobOrderId) throws Exception {

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        PtlJobOrder ptlJobOrder = electronicTagFeignApi.ptlJobOrderDetail(jobOrderId).getData();
        if (ptlJobOrder.getOrderStatus() != 5) {
            throw new BizErrorException(ErrorCodeEnum.GL99990500.getCode(), "该任务单未取消，不能进行该操作");
        }
        if (ptlJobOrder.getPickBackStatus() == 1) {
            throw new BizErrorException(ErrorCodeEnum.GL99990500.getCode(), "该任务单已退拣");
        }

        ptlJobOrder.setPickBackStatus((byte) 1);
        ptlJobOrder.setStatus((byte) 0);
        ptlJobOrder.setModifiedTime(new Date());
        ptlJobOrder.setModifiedUserId(currentUser.getUserId());
        electronicTagFeignApi.updatePtlJobOrder(ptlJobOrder);

        SearchPtlJobOrder searchPtlJobOrder = new SearchPtlJobOrder();
        searchPtlJobOrder.setRelatedOrderCode(ptlJobOrder.getRelatedOrderCode());
        searchPtlJobOrder.setType(1);
        List<PtlJobOrderDto> ptlJobOrderDtoList = electronicTagFeignApi.findPtlJobOrderList(searchPtlJobOrder).getData();

        if (StringUtils.isNotEmpty(ptlJobOrder.getVehicleId()) && ptlJobOrderDtoList.size() == 1) {
            TemVehicle temVehicle = new TemVehicle();
            temVehicle.setVehicleId(ptlJobOrder.getVehicleId());
            temVehicle.setVehicleStatus((byte) 1);
            temVehicle.setModifiedUserId(currentUser.getUserId());
            temVehicle.setModifiedTime(new Date());
            temVehicleFeignApi.update(temVehicle);
        }

        SearchPtlJobOrderDet searchPtlJobOrderDet = new SearchPtlJobOrderDet();
        searchPtlJobOrderDet.setJobOrderId(jobOrderId);
        searchPtlJobOrderDet.setIfHangUp((byte) 1);
        searchPtlJobOrderDet.setJobOrderDet(0);
        searchPtlJobOrderDet.setStartPage(1);
        searchPtlJobOrderDet.setPageSize(9999);
        List<PtlJobOrderDetDto> ptlJobOrderDetDtoList = electronicTagFeignApi.findPtlJobOrderDetList(searchPtlJobOrderDet).getData();
        List<RabbitMQDTO> list = new LinkedList<>();
        List<RabbitMQDTO> listC = new LinkedList<>();
        List<RabbitMQDTO> listE = new LinkedList<>();
        Map<String, Long> map = new HashMap<>();
        List<String> delRedis = new LinkedList<>();
        List<String> equipmentAreaIds = new LinkedList<>();
        List<PtlJobOrderDet> ptlJobOrderDetList = new LinkedList<>();
        for (PtlJobOrderDetDto ptlJobOrderDetDto : ptlJobOrderDetDtoList) {
            if (!equipmentAreaIds.contains(ptlJobOrderDetDto.getEquipmentAreaId())) {
                // 获取当前标签对应区域的所有拣货任务明细
                SearchPtlJobOrderDet searchPtlJobOrderDetNow = new SearchPtlJobOrderDet();
                searchPtlJobOrderDetNow.setEquipmentAreaId(ptlJobOrderDetDto.getEquipmentAreaId());
                searchPtlJobOrderDetNow.setIfHangUp((byte) 1);
                searchPtlJobOrderDetNow.setJobOrderDet(0);
                searchPtlJobOrderDetNow.setStartPage(1);
                searchPtlJobOrderDetNow.setPageSize(9999);
                List<PtlJobOrderDetDto> ptlJobOrderDetDtos = electronicTagFeignApi.findPtlJobOrderDetList(searchPtlJobOrderDetNow).getData();
                Boolean areaBoolean = true;
                for (PtlJobOrderDetDto ptlJobOrderDetDtoNow : ptlJobOrderDetDtos) {
                    if (!ptlJobOrderDetDtoNow.getJobOrderId().equals(ptlJobOrderDetDto.getJobOrderId())) {
                        areaBoolean = false;
                        equipmentAreaIds.add(ptlJobOrderDetDto.getEquipmentAreaId());
                        break;
                    }
                }
                // 通道灯灭灯
                if (areaBoolean) {
                    RabbitMQDTO rabbitMQDTO = new RabbitMQDTO();
                    rabbitMQDTO.setEquipmentTagId(ptlJobOrderDetDto.getEquipmentTagId());
                    rabbitMQDTO.setElectronicTagId(ptlJobOrderDetDto.getEquipmentAreaTagId());
                    rabbitMQDTO.setPosition(ptlJobOrderDetDto.getPosition());
                    rabbitMQDTO.setQueueName(ptlJobOrderDetDto.getQueueName());
                    fanoutSender(1015, rabbitMQDTO, null);
                    log.info("===========发送消息给客户端控制任务单通道灯灭灯完成===============");
                    equipmentAreaIds.add(ptlJobOrderDetDto.getEquipmentAreaId());
                }
            }

            RabbitMQDTO rabbitMQDTO = new RabbitMQDTO();
            rabbitMQDTO.setEquipmentTagId(ptlJobOrderDetDto.getEquipmentTagId());
            rabbitMQDTO.setElectronicTagId(ptlJobOrderDetDto.getElectronicTagId());
            rabbitMQDTO.setQueueName(ptlJobOrderDetDto.getQueueName());
            list.add(rabbitMQDTO);

            // 判断当前储位是否还有其他拣货任务需要亮灯
            String key = ptlJobOrderDetDto.getWarehouseAreaCode() + "_" + ptlJobOrderDetDto.getElectronicTagId();
            Long nextJobOrderDetId = getNextJobOrderDet(ptlJobOrderDetDto, 1);
            if (nextJobOrderDetId != 0) {
                map.put(key, nextJobOrderDetId);
                getNextMaterial(nextJobOrderDetId, listC, listE);
            } else {
                delRedis.add(key);
            }

            PtlJobOrderDet ptlJobOrderDet = new PtlJobOrderDet();
            ptlJobOrderDet.setJobOrderDetId(ptlJobOrderDetDto.getJobOrderDetId());
            ptlJobOrderDet.setJobStatus((byte) 3);
            ptlJobOrderDet.setStatus((byte) 0);
            ptlJobOrderDet.setModifiedTime(new Date());
            ptlJobOrderDet.setModifiedUserId(currentUser.getUserId());
            ptlJobOrderDetList.add(ptlJobOrderDet);
        }

        electronicTagFeignApi.batchUpdatePtlJobOrderDet(ptlJobOrderDetList);

        if (StringUtils.isNotEmpty(list)) {
            fanoutSender(1003, null, list);
            log.info("===========发送消息给客户端控制任务单电子标签灭灯提示完成===============");
        }
        if (!listC.isEmpty()) {
            fanoutSender(1007, null, listC);
            log.info("===========发送消息给客户端控制另一个中文标签亮灯完成===============");
        }
        if (!listE.isEmpty()) {
            fanoutSender(1007, null, listE);
            log.info("===========发送消息给客户端控制另一个英文标签亮灯完成===============");
        }
        updateJobOrderSeq(ptlJobOrderDetDtoList.get(0).getWarehouseAreaId(), ptlJobOrder.getJobOrderCode());
        for (String key : map.keySet()) {
            redisUtil.set(key, map.get(key));
            log.info("修改电子标签：" + key + "当前对应拣货任务明细ID为：" + map.get(key));
        }
        for (String redisKey : delRedis) {
            log.info("删除当前电子标签redisKey：" + redisKey + ", 值为：" + redisUtil.get(redisKey));
            redisUtil.del(redisKey);
        }

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int activateAndPrint(String ids, Long workUserId) throws Exception {

        try {
            List<PtlJobOrderDetPrintDTO> ptlJobOrderDetPrintDTOList = printPtlJobOrderLabel(ids, workUserId, 1);
            List<PtlJobOrderDto> ptlJobOrderDtoList = sendElectronicTagStorage(ids, Long.valueOf(0), 1);
            for (PtlJobOrderDetPrintDTO ptlJobOrderDetPrintDTO : ptlJobOrderDetPrintDTOList) {
                for (PtlJobOrderDto ptlJobOrderDto : ptlJobOrderDtoList) {
                    if (ptlJobOrderDetPrintDTO.getJobOrderId().equals(ptlJobOrderDto.getJobOrderId())) {
                        String color = "";
                        switch (ptlJobOrderDto.getOption1()) {
                            case "0":
                                color = "红色";
                                break;
                            case "1":
                                color = "绿色";
                                break;
                            case "2":
                                color = "黄色";
                                break;
                        }
                        ptlJobOrderDetPrintDTO.setDespatchOrderCode(color);
                    }
                }
            }
            MQResponseEntity mQResponseEntity = new MQResponseEntity<>();
            mQResponseEntity.setCode(1101);
            mQResponseEntity.setData(ptlJobOrderDetPrintDTOList);
            log.info("===========开始发送消息给打印客户端===============");
            fanoutSender.send(ptlJobOrderDetPrintDTOList.get(0).getQueueName(), JSONObject.toJSONString(mQResponseEntity));
            log.info("===========队列名称:" + ptlJobOrderDetPrintDTOList.get(0).getQueueName());
            log.info("===========消息内容:" + JSONObject.toJSONString(mQResponseEntity));
            log.info("===========发送消息给打印客户端打印整、零标签完成===============");

            List<String> vehicleCodeList = new LinkedList<>();
            for (PtlJobOrderDetPrintDTO ptlJobOrderDetPrintDTO : ptlJobOrderDetPrintDTOList) {
                String vehicleCode = ptlJobOrderDetPrintDTO.getVehicleCode().split("\\(")[0];
                String relatedOrderCode = ptlJobOrderDetPrintDTO.getRelatedOrderCode();
                if (!vehicleCodeList.contains(vehicleCode)) {
                    if (StringUtils.isNotEmpty(redisUtil.get(vehicleCode))) {
                        redisUtil.del(vehicleCode);
                        log.info("打印完成，redis释放集货位：" + vehicleCode);
                    }
                    if (StringUtils.isNotEmpty(redisUtil.get(relatedOrderCode))) {
                        redisUtil.set(relatedOrderCode, redisUtil.get(relatedOrderCode), 1);
                        log.info("打印完成，redis延时1秒释放拣货单：" + relatedOrderCode);
                    }
                    redisUtil.incr(vehicleCode + "_count", 1);
                    log.info("打印完成，集货位：" + vehicleCode + "使用次数加1：" + redisUtil.get(vehicleCode + "_count"));

                    vehicleCodeList.add(vehicleCode);
                }
            }
        } catch (Exception e) {
            throw new BizErrorException(ErrorCodeEnum.GL99990500.getCode(), e.getMessage());
        }

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public SysUser getPrinter() throws Exception {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        SearchBaseWorker searchBaseWorker = new SearchBaseWorker();
        searchBaseWorker.setUserId(currentUser.getUserId());
        List<BaseWorkerDto> baseWorkerDtos = baseFeignApi.findList(searchBaseWorker).getData();
        SysUser printUser = null;
        if (StringUtils.isNotEmpty(baseWorkerDtos)) {
            if (baseWorkerDtos.get(0).getBaseWorkingAreaReWDtoList().size() == 1) {
                Object o = redisUtil.get(baseWorkerDtos.get(0).getBaseWorkingAreaReWDtoList().get(0).getWorkingAreaCode());
                printUser = JSONObject.parseObject(JSONObject.toJSONString(o), SysUser.class);
            }
        }

        return printUser;
    }

    @Override
    public void fanoutSender(Integer code, RabbitMQDTO rabbitMQDTO, List<RabbitMQDTO> rabbitMQDTOList) throws Exception {

        String queueName = RabbitConfig.TOPIC_QUEUE_PTL;
        MQResponseEntity mQResponseEntity = new MQResponseEntity<>();
        mQResponseEntity.setCode(code);
        if (StringUtils.isEmpty(rabbitMQDTOList)) {
            mQResponseEntity.setData(rabbitMQDTO);
            queueName = rabbitMQDTO.getQueueName();
        } else {
            mQResponseEntity.setData(rabbitMQDTOList);
            queueName = rabbitMQDTOList.get(0).getQueueName();
        }
        log.info("===========开始发送消息给客户端===============");
        //发送给客户端控制亮/灭灯
        fanoutSender.send(queueName, JSONObject.toJSONString(mQResponseEntity));
        log.info("===========队列名称:" + queueName);
        log.info("===========消息内容:" + JSONObject.toJSONString(mQResponseEntity));
    }

    @Override
    public String intercepting(String s, int number) throws UnsupportedEncodingException {

//        while (s.getBytes("GBK").length < number) {
//            s += " ";
//        }
        while (s.getBytes("GBK").length > number) {
            s = s.substring(1);
        }
        String[] ss = s.split("");
        int count = 0;
        int length = 0;
        for (int i = 0; i < ss.length; i++) {
            if (ss[i].getBytes("GBK").length == 2 && length % 2 == 1) {
                count++;
                length++;
            }
            length += ss[i].getBytes("GBK").length;
        }
        while (s.getBytes("GBK").length + count > number) {
            s = s.substring(1);
        }
        if (s.getBytes("GBK").length + count < number) {
            s += " ";
        }

        return s;
    }

    /**
     * 获取电子标签下一个需要亮灯的拣货任务
     *
     * @param ptlJobOrderDetDto
     * @param type 0-优先获取当前拣货任务单 1-只判断是否有下一个拣货任务单
     * @return
     */
    @Override
    public Long getNextJobOrderDet(PtlJobOrderDetDto ptlJobOrderDetDto, Integer type) {
        // 获取当前标签对应的所有拣货任务明细
        SearchPtlJobOrderDet searchPtlJobOrderDet = new SearchPtlJobOrderDet();
        searchPtlJobOrderDet.setElectronicTagId(ptlJobOrderDetDto.getElectronicTagId());
        searchPtlJobOrderDet.setEquipmentTagId(ptlJobOrderDetDto.getEquipmentTagId());
        searchPtlJobOrderDet.setJobStatus((byte) 2);
        List<PtlJobOrderDetDto> ptlJobOrderDetDtoList = electronicTagFeignApi.findPtlJobOrderDetList(searchPtlJobOrderDet).getData();
        // 判断下个拣货任务明细
        Long jobOrderDetId = 0l;
        Long nextJobOrderDetId = 0l;
        Boolean nowBoolean = true;
        Boolean newBoolean = true;
        int seq = Integer.parseInt(redisUtil.get(ptlJobOrderDetDto.getJobOrderCode()).toString());
        for (PtlJobOrderDetDto ptlJobOrderDetDto2 : ptlJobOrderDetDtoList) {
            if (nowBoolean && ptlJobOrderDetDto.getJobOrderId().equals(ptlJobOrderDetDto2.getJobOrderId()) && !ptlJobOrderDetDto.getJobOrderDetId().equals(ptlJobOrderDetDto2.getJobOrderDetId())) {
                // 当前拣货任务
                jobOrderDetId = ptlJobOrderDetDto2.getJobOrderDetId();
                nowBoolean = false;
            }
            if (Integer.parseInt(redisUtil.get(ptlJobOrderDetDto2.getJobOrderCode()).toString()) == (seq + 1) && newBoolean) {
                // 下一个拣货任务
                nextJobOrderDetId = ptlJobOrderDetDto2.getJobOrderDetId();
                newBoolean = false;
            }
        }
        if (jobOrderDetId != 0 && type == 0) {
            return jobOrderDetId;
        }

        return nextJobOrderDetId;
    }

    /**
     * 更新拣货任务对应的亮灯顺序
     *
     * @param warehouseAreaId
     * @param deleteJobOrderCode
     */
    @Override
    public void updateJobOrderSeq(Long warehouseAreaId, String deleteJobOrderCode) {
        // 当前区域有多少任务单正在作业
        SearchPtlJobOrder searchPtlJobOrder = new SearchPtlJobOrder();
        searchPtlJobOrder.setOrderStatusList((byte) 1);
        searchPtlJobOrder.setWarehouseAreaId(warehouseAreaId);
        searchPtlJobOrder.setType(1);
        List<PtlJobOrderDto> ptlJobOrderDtoList = electronicTagFeignApi.findPtlJobOrderList(searchPtlJobOrder).getData();
        int seq = Integer.parseInt(redisUtil.get(deleteJobOrderCode).toString());
        for (PtlJobOrderDto ptlJobOrderDto : ptlJobOrderDtoList) {
            if (Integer.parseInt(redisUtil.get(ptlJobOrderDto.getJobOrderCode()).toString()) > seq) {
                redisUtil.set(ptlJobOrderDto.getJobOrderCode(), Integer.parseInt(redisUtil.get(ptlJobOrderDto.getJobOrderCode()).toString()) - 1);
                log.info("修改任务单：" + ptlJobOrderDto.getJobOrderCode() + " 作业顺序为：" + Integer.parseInt(redisUtil.get(ptlJobOrderDto.getJobOrderCode()).toString()));
            }
        }
        redisUtil.del(deleteJobOrderCode);
        log.info("删除拣货任务单：" + deleteJobOrderCode + "亮灯排序：" + seq);
    }

    /**
     * 获取下一个拣货物料
     *
     * @param nextJobOrderDetId
     * @param listC
     * @param listE
     * @throws Exception
     */
    @Override
    public void getNextMaterial(Long nextJobOrderDetId, List<RabbitMQDTO> listC, List<RabbitMQDTO> listE) throws Exception {
        // 判断当前储位是否还有其他拣货任务需要亮灯
        if (nextJobOrderDetId != 0) {
            SearchPtlJobOrderDet searchPtlJobOrderDetNext = new SearchPtlJobOrderDet();
            searchPtlJobOrderDetNext.setJobOrderDetId(nextJobOrderDetId);
            searchPtlJobOrderDetNext.setJobOrderDet(0);
            List<PtlJobOrderDetDto> ptlJobOrderDetDtos = electronicTagFeignApi.findPtlJobOrderDetList(searchPtlJobOrderDetNext).getData();
            PtlJobOrder ptlJobOrderNext = electronicTagFeignApi.ptlJobOrderDetail(ptlJobOrderDetDtos.get(0).getJobOrderId()).getData();
            for (PtlJobOrderDetDto ptlJobOrderDetDto : ptlJobOrderDetDtos) {
                // 该储位对应的另一个物料亮灯
                String materialDesc = "";
                String materialDesc2 = "";
                RabbitMQDTO rabbitMQDTO = new RabbitMQDTO();
                rabbitMQDTO.setEquipmentTagId(ptlJobOrderDetDto.getEquipmentTagId());
                rabbitMQDTO.setElectronicTagId(ptlJobOrderDetDto.getElectronicTagId());
                if (ptlJobOrderDetDto.getElectronicTagLangType() == 1) {
                    if (StringUtils.isNotEmpty(ptlJobOrderDetDto.getWholeQty()) && ptlJobOrderDetDto.getWholeQty().compareTo(BigDecimal.ZERO) != 0) {
                        materialDesc += intercepting(ptlJobOrderDetDto.getMaterialName() + " ", 24 - ptlJobOrderDetDto.getWholeQty().toString().length() - ptlJobOrderDetDto.getWholeUnitName().length() * 2);
                        materialDesc += ptlJobOrderDetDto.getWholeQty() + ptlJobOrderDetDto.getWholeUnitName();
                    }
                    if (StringUtils.isNotEmpty(ptlJobOrderDetDto.getScatteredQty()) && ptlJobOrderDetDto.getScatteredQty().compareTo(BigDecimal.ZERO) != 0) {
                        materialDesc2 += intercepting(ptlJobOrderDetDto.getMaterialName() + " ", 24 - ptlJobOrderDetDto.getScatteredQty().toString().length() - ptlJobOrderDetDto.getScatteredUnitName().length() * 2);
                        materialDesc2 += ptlJobOrderDetDto.getScatteredQty() + ptlJobOrderDetDto.getScatteredUnitName();
                    }
                    rabbitMQDTO.setMaterialDesc(materialDesc + materialDesc2);
                } else {
                    String materialCode = "";
                    while (ptlJobOrderDetDto.getMaterialCode().length() + materialCode.length() < 8) {
                        materialCode += " ";
                    }
                    materialCode += ptlJobOrderDetDto.getMaterialCode();
                    while (materialCode.length() > 8) {
                        materialCode = materialCode.substring(1);
                    }
                    rabbitMQDTO.setMaterialDesc(materialCode);
                }
                rabbitMQDTO.setOption1(ptlJobOrderNext.getOption1());
                rabbitMQDTO.setOption2("1");
                rabbitMQDTO.setOption3("0");
                rabbitMQDTO.setElectronicTagLangType(ptlJobOrderDetDto.getElectronicTagLangType());
                rabbitMQDTO.setQueueName(ptlJobOrderDetDto.getQueueName());
                if (ptlJobOrderDetDto.getElectronicTagLangType() == 1) {
                    listC.add(rabbitMQDTO);
                } else {
                    listE.add(rabbitMQDTO);
                }
            }
        }
    }
}
