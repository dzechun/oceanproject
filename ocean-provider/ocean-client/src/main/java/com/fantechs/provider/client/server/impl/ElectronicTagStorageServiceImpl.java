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
import com.fantechs.common.base.entity.security.search.SearchSysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.tem.TemVehicleDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehouse;
import com.fantechs.common.base.general.entity.tem.TemVehicle;
import com.fantechs.common.base.general.entity.tem.search.SearchTemVehicle;
import com.fantechs.common.base.response.MQResponseEntity;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.RestTemplateUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.electronic.ElectronicTagFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.tem.TemVehicleFeignApi;
import com.fantechs.provider.client.config.RabbitConfig;
import com.fantechs.provider.client.dto.*;
import com.fantechs.provider.client.server.ElectronicTagStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class ElectronicTagStorageServiceImpl implements ElectronicTagStorageService {
    private static final Logger log = LoggerFactory.getLogger(ElectronicTagStorageServiceImpl.class);
    @Resource
    private FanoutSender fanoutSender;
    @Resource
    private ElectronicTagFeignApi electronicTagFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private TemVehicleFeignApi temVehicleFeignApi;

    @Value("${wmsAPI.finishPtlJobOrderUrl}")
    private String finishPtlJobOrderUrl;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public PtlJobOrder sendElectronicTagStorage(Long jobOrderId, Long warehouseAreaId) throws Exception {

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        PtlJobOrder ptlJobOrder = electronicTagFeignApi.ptlJobOrderDetail(jobOrderId).getData();
        if (ptlJobOrder.getOrderStatus() == 3) {
            throw new BizErrorException(ErrorCodeEnum.GL99990500.getCode(), "该任务单已完成");
        }
        if (ptlJobOrder.getOrderStatus() == 5) {
            throw new BizErrorException(ErrorCodeEnum.GL99990500.getCode(), "该任务单已取消");
        }

        SearchPtlJobOrderDet searchPtlJobOrderDet = new SearchPtlJobOrderDet();
        searchPtlJobOrderDet.setJobOrderId(jobOrderId);
        if (ptlJobOrder.getOrderStatus() == 2) {
            searchPtlJobOrderDet.setJobStatus((byte) 2);
        } else if (ptlJobOrder.getOrderStatus() == 4) {
            searchPtlJobOrderDet.setIfHangUp((byte) 1);
        }
        List<PtlJobOrderDetDto> ptlJobOrderDetDtoList = electronicTagFeignApi.findPtlJobOrderDetList(searchPtlJobOrderDet).getData();
        if (warehouseAreaId == 0) {
            warehouseAreaId = ptlJobOrderDetDtoList.get(0).getWarehouseAreaId();
        }
        synchronized (ElectronicTagStorageServiceImpl.class) {
            //是否有在做单据
            SearchPtlJobOrder searchPtlJobOrder = new SearchPtlJobOrder();
            searchPtlJobOrder.setOrderStatusList((byte) 1);
            searchPtlJobOrder.setWarehouseAreaId(warehouseAreaId);
            List<PtlJobOrderDto> ptlJobOrderDtoList = electronicTagFeignApi.findPtlJobOrderList(searchPtlJobOrder).getData();
            if (StringUtils.isNotEmpty(ptlJobOrderDtoList)) {
                if (ptlJobOrderDtoList.size() != 1 || !ptlJobOrderDtoList.get(0).getJobOrderId().equals(jobOrderId)) {
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "正在处理其他任务单，请稍后再试");
                }
            }
        }

        List<RabbitMQDTO> listC = new LinkedList<>();
        List<RabbitMQDTO> listE = new LinkedList<>();
        List<PtlJobOrderDet> ptlJobOrderDetList = new LinkedList<>();
        List<String> electronicTagIds = new LinkedList<>();
        List<String> equipmentAreaTagIds = new LinkedList<>();
        for (PtlJobOrderDetDto ptlJobOrderDetDto : ptlJobOrderDetDtoList) {
            SearchPtlElectronicTagStorage searchPtlElectronicTagStorage = new SearchPtlElectronicTagStorage();
            searchPtlElectronicTagStorage.setStorageId(ptlJobOrderDetDto.getStorageId().toString());
            List<PtlElectronicTagStorageDto> ptlElectronicTagStorageDtoList = electronicTagFeignApi.findElectronicTagStorageList(searchPtlElectronicTagStorage).getData();
            if (StringUtils.isEmpty(ptlElectronicTagStorageDtoList)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "请先维护储位：" + ptlJobOrderDetDto.getStorageCode() + "和物料：" + ptlJobOrderDetDto.getMaterialCode() + "以及对应的电子标签关联信息");
            }
            if (!electronicTagIds.contains(ptlElectronicTagStorageDtoList.get(0).getElectronicTagId())) {
                for (PtlElectronicTagStorageDto ptlElectronicTagStorageDto : ptlElectronicTagStorageDtoList) {

                    // 通道灯亮灯
                    if (!equipmentAreaTagIds.contains(ptlElectronicTagStorageDto.getEquipmentAreaTagId())) {
                        RabbitMQDTO rabbitMQDTO = new RabbitMQDTO();
                        rabbitMQDTO.setEquipmentTagId(ptlElectronicTagStorageDto.getEquipmentTagId());
                        rabbitMQDTO.setElectronicTagId(ptlElectronicTagStorageDto.getEquipmentAreaTagId());
                        rabbitMQDTO.setQueueName(ptlElectronicTagStorageDto.getQueueName());
                        fanoutSender(1001, rabbitMQDTO, null);
                        log.info("===========发送消息给客户端控制通道灯亮灯完成===============");
                        equipmentAreaTagIds.add(ptlElectronicTagStorageDto.getEquipmentAreaTagId());
                    }

                    String materialDesc = "";
                    String materialDesc2 = "";
                    RabbitMQDTO rabbitMQDTO = new RabbitMQDTO();
                    rabbitMQDTO.setEquipmentTagId(ptlElectronicTagStorageDto.getEquipmentTagId());
                    rabbitMQDTO.setElectronicTagId(ptlElectronicTagStorageDto.getElectronicTagId());
                    if (ptlElectronicTagStorageDto.getElectronicTagLangType() == 1) {
                        if (StringUtils.isNotEmpty(ptlJobOrderDetDto.getWholeQty()) && ptlJobOrderDetDto.getWholeQty().compareTo(BigDecimal.ZERO) != 0) {
                            materialDesc += ptlJobOrderDetDto.getMaterialName() + " " + ptlJobOrderDetDto.getWholeQty();
                            materialDesc = intercepting(materialDesc, 24 - ptlJobOrderDetDto.getWholeUnitName().length() * 2) + ptlJobOrderDetDto.getWholeUnitName();
                        }
                        if (StringUtils.isNotEmpty(ptlJobOrderDetDto.getScatteredQty()) && ptlJobOrderDetDto.getScatteredQty().compareTo(BigDecimal.ZERO) != 0) {
                            materialDesc2 += ptlJobOrderDetDto.getMaterialName() + " " + ptlJobOrderDetDto.getScatteredQty();
                            materialDesc2 = intercepting(materialDesc2, 24 - ptlJobOrderDetDto.getScatteredUnitName().length() * 2)  + ptlJobOrderDetDto.getScatteredUnitName();
                        }
                        rabbitMQDTO.setMaterialDesc(materialDesc + materialDesc2);
                    } else {
                        rabbitMQDTO.setMaterialDesc(intercepting(ptlJobOrderDetDto.getMaterialCode(), 8));
                    }
                    rabbitMQDTO.setOption1("0");
                    rabbitMQDTO.setOption2("1");
                    rabbitMQDTO.setOption3("0");
                    rabbitMQDTO.setElectronicTagLangType(ptlElectronicTagStorageDto.getElectronicTagLangType());
                    rabbitMQDTO.setQueueName(ptlElectronicTagStorageDto.getQueueName());
                    if (ptlElectronicTagStorageDto.getElectronicTagLangType() == 1) {
                        listC.add(rabbitMQDTO);
                    } else {
                        listE.add(rabbitMQDTO);
                    }
                }
                electronicTagIds.add(ptlElectronicTagStorageDtoList.get(0).getElectronicTagId());
            }
            if (electronicTagIds.isEmpty()) {
                electronicTagIds.add(ptlElectronicTagStorageDtoList.get(0).getElectronicTagId());
            }

            PtlJobOrderDet ptlJobOrderDet = new PtlJobOrderDet();
            ptlJobOrderDet.setJobOrderDetId(ptlJobOrderDetDto.getJobOrderDetId());
            ptlJobOrderDet.setJobStatus((byte) 2);
            ptlJobOrderDet.setModifiedTime(new Date());
            ptlJobOrderDet.setModifiedUserId(currentUser.getUserId());
            ptlJobOrderDetList.add(ptlJobOrderDet);
        }
        ptlJobOrder.setJobOrderId(jobOrderId);
        ptlJobOrder.setOrderStatus((byte) 2);
        ptlJobOrder.setModifiedTime(new Date());
        ptlJobOrder.setModifiedUserId(currentUser.getUserId());
        electronicTagFeignApi.updatePtlJobOrder(ptlJobOrder);
        electronicTagFeignApi.batchUpdatePtlJobOrderDet(ptlJobOrderDetList);

        if (!listC.isEmpty()) {
            fanoutSender(1007, null, listC);
            log.info("===========发送消息给客户端控制中文标签亮灯完成===============");
        }
        if (!listE.isEmpty()) {
            fanoutSender(1008, null, listE);
            log.info("===========发送消息给客户端控制英文标签亮灯完成===============");
        }

        return ptlJobOrder;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public ResponseEntityDTO createPtlJobOrder(List<PtlJobOrderDTO> ptlJobOrderDTOList) throws Exception {

        List<PtlJobOrderDet> list = new LinkedList<>();
        List<RabbitMQDTO> rabbitMQDTOList = new LinkedList<>();
        List<Long> clientIdList = new LinkedList<>();
        for (PtlJobOrderDTO ptlJobOrderDTO : ptlJobOrderDTOList) {
            SearchPtlJobOrder searchPtlJobOrder = new SearchPtlJobOrder();
            searchPtlJobOrder.setJobOrderCode(ptlJobOrderDTO.getTaskNo());
            List<PtlJobOrderDto> ptlJobOrderDtoList = electronicTagFeignApi.findPtlJobOrderList(searchPtlJobOrder).getData();
            if (StringUtils.isNotEmpty(ptlJobOrderDtoList)) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "重复分拣单");
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
            ptlJobOrder.setCreateTime(new Date());
            ptlJobOrder = electronicTagFeignApi.addPtlJobOrder(ptlJobOrder).getData();
            if (StringUtils.isEmpty(ptlJobOrder)) {
                throw new BizErrorException(ErrorCodeEnum.GL99990005);
            }
            String warehouseAreaCode = "";
            for (PtlJobOrderDetDTO ptlJobOrderDetDTO : ptlJobOrderDTO.getDetails()) {
                SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
                searchBaseMaterial.setMaterialCode(ptlJobOrderDetDTO.getGoodsCode());
                searchBaseMaterial.setCodeQueryMark(1);
                List<BaseMaterial> baseMaterials = baseFeignApi.findSmtMaterialList(searchBaseMaterial).getData();
                if (StringUtils.isEmpty(baseMaterials)) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到对应物料信息");
                }
                SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
                searchBaseStorage.setStorageCode(ptlJobOrderDetDTO.getLocationCode());
                searchBaseStorage.setCodeQueryMark((byte) 1);
                List<BaseStorage> baseStorages = baseFeignApi.findList(searchBaseStorage).getData();
                if (StringUtils.isEmpty(baseStorages)) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到对应库位信息");
                }
                SearchPtlElectronicTagStorage searchPtlElectronicTagStorage = new SearchPtlElectronicTagStorage();
                searchPtlElectronicTagStorage.setStorageId(baseStorages.get(0).getStorageId().toString());
                List<PtlElectronicTagStorageDto> ptlElectronicTagStorageDtoList = electronicTagFeignApi.findElectronicTagStorageList(searchPtlElectronicTagStorage).getData();
                if (StringUtils.isEmpty(ptlElectronicTagStorageDtoList)) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "请先维护储位：" + ptlJobOrderDetDTO.getLocationCode() + "和物料：" + ptlJobOrderDetDTO.getGoodsCode() + "以及对应的电子标签关联信息");
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
                    ptlJobOrderDet.setWholeOrScattered((byte) 1);
                    ptlJobOrderDet.setWholeQty(BigDecimal.valueOf(ptlJobOrderDetDTO.getW_qty()));
                    ptlJobOrderDet.setWholeUnitName(ptlJobOrderDetDTO.getW_unit());
                } else {
                    ptlJobOrderDet.setWholeOrScattered((byte) 0);
                }
                ptlJobOrderDet.setScatteredQty(BigDecimal.valueOf(ptlJobOrderDetDTO.getL_qty()));
                ptlJobOrderDet.setScatteredUnitName(ptlJobOrderDetDTO.getL_unit());
                ptlJobOrderDet.setSpec(ptlJobOrderDetDTO.getSpecification());
                ptlJobOrderDet.setJobStatus((byte) 1);
                ptlJobOrderDet.setStatus((byte) 1);
                ptlJobOrderDet.setCreateTime(new Date());
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

        if (!rabbitMQDTOList.isEmpty()) {
            fanoutSender(1005, null, rabbitMQDTOList);
            log.info("===========发送消息给客户端控制蜂鸣器开启完成===============");
        }

        ResponseEntityDTO responseEntityDTO = new ResponseEntityDTO();
        responseEntityDTO.setCode(0);
        responseEntityDTO.setMessage("保存成功");
        responseEntityDTO.setSuccess("s");

        return responseEntityDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public PtlJobOrderDto writeBackPtlJobOrder(Long jobOrderId) throws Exception {

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        SearchPtlJobOrder searchPtlJobOrder = new SearchPtlJobOrder();
        searchPtlJobOrder.setJobOrderId(jobOrderId);
        List<PtlJobOrderDto> ptlJobOrderDtoList = electronicTagFeignApi.findPtlJobOrderList(searchPtlJobOrder).getData();
        PtlJobOrderDto ptlJobOrderDto = ptlJobOrderDtoList.get(0);
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
        List<PtlJobOrderDetDto> ptlJobOrderDetDtoList = electronicTagFeignApi.findPtlJobOrderDetList(searchPtlJobOrderDet).getData();
        List<RabbitMQDTO> list = new LinkedList<>();
        List<String> equipmentAreaTagIds = new LinkedList<>();
        for (PtlJobOrderDetDto ptlJobOrderDetDto : ptlJobOrderDetDtoList) {

            // 通道灯灭灯
            if (!equipmentAreaTagIds.contains(ptlJobOrderDetDto.getEquipmentAreaTagId())) {
                RabbitMQDTO rabbitMQDTO = new RabbitMQDTO();
                rabbitMQDTO.setEquipmentTagId(ptlJobOrderDetDto.getEquipmentTagId());
                rabbitMQDTO.setElectronicTagId(ptlJobOrderDetDto.getEquipmentAreaTagId());
                rabbitMQDTO.setQueueName(ptlJobOrderDetDto.getQueueName());
                fanoutSender(1002, rabbitMQDTO, null);
                log.info("===========发送消息给客户端控制任务单通道灯灭灯完成===============");
                equipmentAreaTagIds.add(ptlJobOrderDetDto.getEquipmentAreaTagId());
            }

            RabbitMQDTO rabbitMQDTO = new RabbitMQDTO();
            rabbitMQDTO.setEquipmentTagId(ptlJobOrderDetDto.getEquipmentTagId());
            rabbitMQDTO.setElectronicTagId(ptlJobOrderDetDto.getElectronicTagId());
            rabbitMQDTO.setQueueName(ptlJobOrderDetDto.getQueueName());
            list.add(rabbitMQDTO);
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

        PtlJobOrderDTO ptlJobOrderDTO = new PtlJobOrderDTO();
        ptlJobOrderDTO.setCustomerNo(ptlJobOrderDto.getRelatedOrderCode());
        ptlJobOrderDTO.setWarehouseCode(ptlJobOrderDto.getWarehouseCode());
        ptlJobOrderDTO.setWorkerCode(ptlJobOrderDto.getWorkerUserCode());
        ptlJobOrderDTO.setStatus("F");
        // 回写PTL作业任务
        log.info("电子作业标签完成/异常，回传WMS" + JSONObject.toJSONString(ptlJobOrderDTO));
//        String result = RestTemplateUtil.postJsonStrForString(ptlJobOrderDTO, finishPtlJobOrderUrl);
        log.info("电子作业标签回写返回信息：" + "result");
//        ResponseEntityDTO responseEntityDTO = JsonUtils.jsonToPojo(result, ResponseEntityDTO.class);
//        if (!"s".equals(responseEntityDTO.getSuccess())) {
//            throw new Exception("电子作业标签完成，回传WMS失败：" + responseEntityDTO.getMessage());
//        }

        if (!list.isEmpty()) {
            fanoutSender(1003, null, list);
            log.info("===========发送消息给客户端控制剩余电子标签灭灯完成===============");
        }

        return ptlJobOrderDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public List<PtlJobOrderDetPrintDTO> printPtlJobOrderLabel(Long jobOrderId, Long workUserId) throws Exception {

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        PtlJobOrder ptlJobOrder = electronicTagFeignApi.ptlJobOrderDetail(jobOrderId).getData();
        if (ptlJobOrder.getIfAlreadyPrint() == 1) {
            throw new Exception("该任务单已打印过标签，请不要重复操作");
        }
        SysUser sysUser = securityFeignApi.selectUserById(workUserId).getData();
        String vehicleCode = "";
        if (StringUtils.isNotEmpty(ptlJobOrder.getVehicleId())) {
            TemVehicle temVehicle = temVehicleFeignApi.detail(ptlJobOrder.getVehicleId()).getData();
            vehicleCode = temVehicle.getVehicleCode();
        }
        if (StringUtils.isEmpty(vehicleCode)) {
            SearchTemVehicle searchTemVehicle = new SearchTemVehicle();
            searchTemVehicle.setVehicleStatus((byte) 1);
            List<TemVehicleDto> temVehicleDtoList = temVehicleFeignApi.findList(searchTemVehicle).getData();
            if (StringUtils.isEmpty(temVehicleDtoList)) {
                throw new Exception("目前没有空闲的集货位，请稍后再操作");
            }
            vehicleCode = temVehicleDtoList.get(0).getVehicleCode();

            TemVehicle temVehicle = temVehicleDtoList.get(0);
            temVehicle.setVehicleStatus((byte) 3);
            temVehicle.setModifiedTime(new Date());
            temVehicle.setModifiedUserId(currentUser.getUserId());
            temVehicleFeignApi.update(temVehicle);

            PtlJobOrder ptlJobOrder1 = new PtlJobOrder();
            ptlJobOrder1.setRelatedOrderCode(ptlJobOrder.getRelatedOrderCode());
            ptlJobOrder1.setWorkerUserId(workUserId);
            ptlJobOrder1.setWorkerUserName(sysUser.getUserName());
            ptlJobOrder1.setVehicleId(temVehicleDtoList.get(0).getVehicleId());
            ptlJobOrder1.setModifiedTime(new Date());
            ptlJobOrder1.setModifiedUserId(currentUser.getUserId());
            electronicTagFeignApi.updateByRelatedOrderCode(ptlJobOrder1);
        }
        SearchPtlJobOrderDet searchPtlJobOrderDet = new SearchPtlJobOrderDet();
        searchPtlJobOrderDet.setJobOrderId(jobOrderId);
        List<PtlJobOrderDetDto> ptlJobOrderDetDtoList = electronicTagFeignApi.findPtlJobOrderDetList(searchPtlJobOrderDet).getData();
        List<PtlJobOrderDetPrintDTO> ptlJobOrderDetPrintDTOList = new LinkedList<>();
        for (PtlJobOrderDetDto ptlJobOrderDetDto : ptlJobOrderDetDtoList) {
            if (ptlJobOrderDetDto.getWholeOrScattered() == 1) {
                int i = 1;
                int cartonNum = ptlJobOrderDetDto.getWholeQty().intValue();
                while (cartonNum > 0) {
                    PtlJobOrderDetPrintDTO ptlJobOrderDetPrintDTO = new PtlJobOrderDetPrintDTO();
                    ptlJobOrderDetPrintDTO.setJobOrderCode(ptlJobOrder.getJobOrderCode());
                    ptlJobOrderDetPrintDTO.setDespatchOrderCode(ptlJobOrder.getDespatchOrderCode());
                    ptlJobOrderDetPrintDTO.setRelatedOrderCode(ptlJobOrder.getRelatedOrderCode());
                    ptlJobOrderDetPrintDTO.setMaterialName(ptlJobOrderDetDto.getMaterialName());
                    ptlJobOrderDetPrintDTO.setMaterialCode(ptlJobOrderDetDto.getMaterialCode());
                    ptlJobOrderDetPrintDTO.setSpec(ptlJobOrderDetDto.getSpec());
                    ptlJobOrderDetPrintDTO.setWarehouseAreaCode(ptlJobOrderDetDto.getWarehouseAreaCode());
                    ptlJobOrderDetPrintDTO.setStorageCode(ptlJobOrderDetDto.getStorageCode());
                    ptlJobOrderDetPrintDTO.setWorkerUserName(sysUser.getUserName());
                    ptlJobOrderDetPrintDTO.setWholeOrScattered(ptlJobOrderDetDto.getWholeOrScattered());
                    ptlJobOrderDetPrintDTO.setCartonCode(i + "-" + ptlJobOrderDetDto.getWholeQty().intValue());
                    ptlJobOrderDetPrintDTO.setVehicleCode("集货：" + vehicleCode);
                    ptlJobOrderDetPrintDTOList.add(ptlJobOrderDetPrintDTO);

                    cartonNum--;
                    i++;
                }
            } else {
                PtlJobOrderDetPrintDTO ptlJobOrderDetPrintDTO = new PtlJobOrderDetPrintDTO();
                ptlJobOrderDetPrintDTO.setJobOrderCode(ptlJobOrder.getJobOrderCode());
                ptlJobOrderDetPrintDTO.setDespatchOrderCode(ptlJobOrder.getDespatchOrderCode());
                ptlJobOrderDetPrintDTO.setRelatedOrderCode(ptlJobOrder.getRelatedOrderCode());
                ptlJobOrderDetPrintDTO.setMaterialName(ptlJobOrderDetDto.getMaterialName());
                ptlJobOrderDetPrintDTO.setMaterialCode(ptlJobOrderDetDto.getMaterialCode());
                ptlJobOrderDetPrintDTO.setSpec(ptlJobOrderDetDto.getSpec());
                ptlJobOrderDetPrintDTO.setWarehouseAreaCode(ptlJobOrderDetDto.getWarehouseAreaCode());
                ptlJobOrderDetPrintDTO.setStorageCode(ptlJobOrderDetDto.getStorageCode());
                ptlJobOrderDetPrintDTO.setWorkerUserName(sysUser.getUserName());
                ptlJobOrderDetPrintDTO.setWholeOrScattered(ptlJobOrderDetDto.getWholeOrScattered());
                ptlJobOrderDetPrintDTO.setVehicleCode("集货：" + vehicleCode);
                ptlJobOrderDetPrintDTOList.add(ptlJobOrderDetPrintDTO);
            }
        }

        ptlJobOrder.setIfAlreadyPrint((byte) 1);
        ptlJobOrder.setModifiedTime(new Date());
        ptlJobOrder.setModifiedUserId(currentUser.getUserId());
        electronicTagFeignApi.updatePtlJobOrder(ptlJobOrder);

        MQResponseEntity mQResponseEntity = new MQResponseEntity<>();
        mQResponseEntity.setCode(1101);
        mQResponseEntity.setData(ptlJobOrderDetPrintDTOList);
        log.info("===========开始发送消息给打印客户端===============");
        fanoutSender.send(RabbitConfig.TOPIC_QUEUE_PRINT, JSONObject.toJSONString(mQResponseEntity));
        log.info("===========队列名称:" + RabbitConfig.TOPIC_QUEUE_PRINT);
        log.info("===========消息内容:" + JSONObject.toJSONString(mQResponseEntity));
        log.info("===========发送消息给打印客户端打印整、零标签完成===============");

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

        List<RabbitMQDTO> list = new LinkedList<>();
        String[] idArray = ids.split(",");
        for (String jobOrderDetId : idArray) {
            PtlJobOrderDet ptlJobOrderDet = new PtlJobOrderDet();
            ptlJobOrderDet.setJobOrderDetId(Long.valueOf(jobOrderDetId));
            ptlJobOrderDet.setJobStatus((byte) 4);
            ptlJobOrderDet.setModifiedTime(new Date());
            ptlJobOrderDet.setModifiedUserId(currentUser.getUserId());
            electronicTagFeignApi.updatePtlJobOrderDet(ptlJobOrderDet);

            SearchPtlJobOrderDet searchPtlJobOrderDet = new SearchPtlJobOrderDet();
            searchPtlJobOrderDet.setJobOrderDetId(Long.valueOf(jobOrderDetId));
            searchPtlJobOrderDet.setJobOrderDet(0);
            List<PtlJobOrderDetDto> ptlJobOrderDetDtoList = electronicTagFeignApi.findPtlJobOrderDetList(searchPtlJobOrderDet).getData();

            PtlJobOrder ptlJobOrder = new PtlJobOrder();
            ptlJobOrder.setJobOrderId(ptlJobOrderDetDtoList.get(0).getJobOrderId());
            ptlJobOrder.setOrderStatus((byte) 4);
            ptlJobOrder.setModifiedTime(new Date());
            ptlJobOrder.setModifiedUserId(currentUser.getUserId());
            electronicTagFeignApi.updatePtlJobOrder(ptlJobOrder);

            for (PtlJobOrderDetDto ptlJobOrderDetDto : ptlJobOrderDetDtoList) {
                RabbitMQDTO rabbitMQDTO = new RabbitMQDTO();
                rabbitMQDTO.setEquipmentTagId(ptlJobOrderDetDto.getEquipmentTagId());
                rabbitMQDTO.setElectronicTagId(ptlJobOrderDetDto.getElectronicTagId());
                rabbitMQDTO.setQueueName(ptlJobOrderDetDto.getQueueName());
                list.add(rabbitMQDTO);
            }
        }
        fanoutSender(1003, null, list);
        log.info("===========发送消息给客户端控制挂起电子标签灭灯完成===============");

        return 1;
    }

    @Override
    @Transactional
    @LcnTransaction
    public ResponseEntityDTO cancelPtrlJobOrder(PtlJobOrderDTO ptlJobOrderDTO) throws Exception {

        if ("C".equals(ptlJobOrderDTO.getStatus())) {
            SearchPtlJobOrder searchPtlJobOrder = new SearchPtlJobOrder();
            searchPtlJobOrder.setRelatedOrderCode(ptlJobOrderDTO.getCustomerNo());
            searchPtlJobOrder.setNotOrderStatus((byte) 3);
            List<PtlJobOrderDto> ptlJobOrderDtoList = electronicTagFeignApi.findPtlJobOrderList(searchPtlJobOrder).getData();
            List<RabbitMQDTO> list = new LinkedList<>();
            for (PtlJobOrderDto ptlJobOrderDto : ptlJobOrderDtoList) {

                PtlJobOrder ptlJobOrder = new PtlJobOrder();
                ptlJobOrder.setJobOrderId(ptlJobOrderDto.getJobOrderId());
                ptlJobOrder.setOrderStatus((byte) 5);
                ptlJobOrder.setModifiedTime(new Date());
                electronicTagFeignApi.updatePtlJobOrder(ptlJobOrder);

                if (ptlJobOrder.getOrderStatus() == 1 && ptlJobOrder.getOrderStatus() == 3) {
                    continue;
                }

                SearchPtlJobOrderDet searchPtlJobOrderDet = new SearchPtlJobOrderDet();
                searchPtlJobOrderDet.setJobOrderId(ptlJobOrderDto.getJobOrderId());
                searchPtlJobOrderDet.setIfHangUp((byte) 1);
                searchPtlJobOrderDet.setJobOrderDet(0);
                List<PtlJobOrderDetDto> ptlJobOrderDetDtoList = electronicTagFeignApi.findPtlJobOrderDetList(searchPtlJobOrderDet).getData();
                for (PtlJobOrderDetDto ptlJobOrderDetDto : ptlJobOrderDetDtoList) {
                    if (ptlJobOrderDetDto.getElectronicTagLangType() == 1) {
                        RabbitMQDTO rabbitMQDTO = new RabbitMQDTO();
                        rabbitMQDTO.setEquipmentTagId(ptlJobOrderDetDto.getEquipmentTagId());
                        rabbitMQDTO.setElectronicTagId(ptlJobOrderDetDto.getElectronicTagId());
                        rabbitMQDTO.setMaterialDesc("拣货单：" + ptlJobOrderDTO.getCustomerNo() + " 已取消, 请退拣");
                        rabbitMQDTO.setOption1("0");
                        rabbitMQDTO.setOption2("1");
                        rabbitMQDTO.setOption3("0");
                        list.add(rabbitMQDTO);
                    }
                }
            }

            if (StringUtils.isNotEmpty(list)) {
                fanoutSender(1007, null, list);
                log.info("===========发送消息给客户端控制拣货单号取消提示完成===============");
            }
        } else {
            PtlJobOrder ptlJobOrder = new PtlJobOrder();
            ptlJobOrder.setRelatedOrderCode(ptlJobOrderDTO.getCustomerNo());
            ptlJobOrder.setOrderStatus((byte) 6);
            ptlJobOrder.setModifiedTime(new Date());
            electronicTagFeignApi.updateByRelatedOrderCode(ptlJobOrder);

            SearchPtlJobOrder searchPtlJobOrder = new SearchPtlJobOrder();
            searchPtlJobOrder.setRelatedOrderCode(ptlJobOrderDTO.getCustomerNo());
            List<PtlJobOrderDto> ptlJobOrderDtoList = electronicTagFeignApi.findPtlJobOrderList(searchPtlJobOrder).getData();
            if (StringUtils.isNotEmpty(ptlJobOrderDtoList)) {
                TemVehicle temVehicle = new TemVehicle();
                temVehicle.setVehicleId(ptlJobOrderDtoList.get(0).getVehicleId());
                temVehicle.setVehicleStatus((byte) 1);
                temVehicle.setModifiedTime(new Date());
                temVehicleFeignApi.update(temVehicle);
            }
        }

        ResponseEntityDTO responseEntityDTO = new ResponseEntityDTO();
        responseEntityDTO.setCode(0);
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

        ptlJobOrder.setPickBackStatus((byte) 1);
        ptlJobOrder.setModifiedTime(new Date());
        ptlJobOrder.setModifiedUserId(currentUser.getUserId());
        electronicTagFeignApi.updatePtlJobOrder(ptlJobOrder);

        if (StringUtils.isNotEmpty(ptlJobOrder.getVehicleId())) {
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
        List<PtlJobOrderDetDto> ptlJobOrderDetDtoList = electronicTagFeignApi.findPtlJobOrderDetList(searchPtlJobOrderDet).getData();
        List<RabbitMQDTO> list = new LinkedList<>();
        List<String> equipmentAreaTagIds = new LinkedList<>();
        List<PtlJobOrderDet> ptlJobOrderDetList = new LinkedList<>();
        for (PtlJobOrderDetDto ptlJobOrderDetDto : ptlJobOrderDetDtoList) {
            // 通道灯灭灯
            if (!equipmentAreaTagIds.contains(ptlJobOrderDetDto.getEquipmentAreaTagId())) {
                RabbitMQDTO rabbitMQDTO = new RabbitMQDTO();
                rabbitMQDTO.setEquipmentTagId(ptlJobOrderDetDto.getEquipmentTagId());
                rabbitMQDTO.setElectronicTagId(ptlJobOrderDetDto.getEquipmentAreaTagId());
                rabbitMQDTO.setQueueName(ptlJobOrderDetDto.getQueueName());
                fanoutSender(1002, rabbitMQDTO, null);
                log.info("===========发送消息给客户端控制任务单通道灯灭灯完成===============");
                equipmentAreaTagIds.add(ptlJobOrderDetDto.getEquipmentAreaTagId());
            }

            RabbitMQDTO rabbitMQDTO = new RabbitMQDTO();
            rabbitMQDTO.setEquipmentTagId(ptlJobOrderDetDto.getEquipmentTagId());
            rabbitMQDTO.setElectronicTagId(ptlJobOrderDetDto.getElectronicTagId());
            list.add(rabbitMQDTO);

            PtlJobOrderDet ptlJobOrderDet = new PtlJobOrderDet();
            ptlJobOrderDet.setJobOrderDetId(ptlJobOrderDetDto.getJobOrderDetId());
            ptlJobOrderDet.setJobStatus((byte) 3);
            ptlJobOrderDet.setModifiedTime(new Date());
            ptlJobOrderDet.setModifiedUserId(currentUser.getUserId());
            ptlJobOrderDetList.add(ptlJobOrderDet);
        }

        electronicTagFeignApi.batchUpdatePtlJobOrderDet(ptlJobOrderDetList);

        if (StringUtils.isNotEmpty(list)) {
            fanoutSender(1003, null, list);
            log.info("===========发送消息给客户端控制任务单电子标签灭灯提示完成===============");
        }

        return 1;
    }

    @Override
    public void fanoutSender(Integer code, RabbitMQDTO rabbitMQDTO, List<RabbitMQDTO> rabbitMQDTOList) throws Exception {

        MQResponseEntity mQResponseEntity = new MQResponseEntity<>();
        mQResponseEntity.setCode(code);
        if (StringUtils.isEmpty(rabbitMQDTOList)) {
            mQResponseEntity.setData(rabbitMQDTO);
        } else {
            mQResponseEntity.setData(rabbitMQDTOList);
        }
        log.info("===========开始发送消息给客户端===============");
        //发送给客户端控制亮/灭灯
        fanoutSender.send(RabbitConfig.TOPIC_QUEUE_PTL, JSONObject.toJSONString(mQResponseEntity));
        log.info("===========队列名称:" + RabbitConfig.TOPIC_QUEUE_PTL);
        log.info("===========消息内容:" + JSONObject.toJSONString(mQResponseEntity));
    }

    @Override
    public String intercepting(String s, int number) throws UnsupportedEncodingException {

        while (s.getBytes("GBK").length < number) {
            s += " ";
        }
        while (s.getBytes("GBK").length > number) {
            s = s.substring(1);
        }

        return s;
    }
}
