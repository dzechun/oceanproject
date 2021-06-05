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
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehouse;
import com.fantechs.common.base.response.MQResponseEntity;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.RestTemplateUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.electronic.ElectronicTagFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.client.config.RabbitConfig;
import com.fantechs.provider.client.dto.PtlJobOrderDTO;
import com.fantechs.provider.client.dto.PtlJobOrderDetDTO;
import com.fantechs.provider.client.dto.RabbitMQDTO;
import com.fantechs.provider.client.server.ElectronicTagStorageService;
import com.google.gson.reflect.TypeToken;
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

/**
 * Created by lfz on 2020/12/9.
 */
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

    @Value("${mesAPI.resApi}")
    private String resApiUrl;
    @Value("${qisAPI.confirmOutBillOrder}")
    private String confirmOutBillOrderUrl;
    @Value("${qisAPI.confirmInBillOrder}")
    private String confirmInBillOrderUrl;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public PtlJobOrder sendElectronicTagStorage(Long jobOrderId, Long warehouseAreaId) throws Exception {

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        PtlJobOrder ptlJobOrder = electronicTagFeignApi.ptlJobOrderDetail(jobOrderId).getData();
        if (ptlJobOrder.getOrderStatus() != 1) {
            String message = "";
            if (ptlJobOrder.getOrderStatus() == 2) {
                message = "该任务单已激活，请不要重复操作";
            }
            if (ptlJobOrder.getOrderStatus() == 3) {
                message = "该任务单已完成";
            }
            if (ptlJobOrder.getOrderStatus() == 4) {
                message = "该任务单处于异常状态，不可操作";
            }
            throw new BizErrorException(ErrorCodeEnum.GL99990500.getCode(), message);
        }

        SearchPtlJobOrderDet searchPtlJobOrderDet = new SearchPtlJobOrderDet();
        searchPtlJobOrderDet.setJobOrderId(jobOrderId);
        List<PtlJobOrderDetDto> ptlJobOrderDetDtoList = electronicTagFeignApi.findPtlJobOrderDetList(searchPtlJobOrderDet).getData();
        if (warehouseAreaId == 0) {
            warehouseAreaId = ptlJobOrderDetDtoList.get(0).getWarehouseAreaId();
        }
        synchronized (ElectronicTagStorageServiceImpl.class) {
            //是否有在做单据
            SearchPtlJobOrder searchPtlJobOrder = new SearchPtlJobOrder();
            searchPtlJobOrder.setOrderStatus((byte) 2);
            searchPtlJobOrder.setWarehouseAreaId(warehouseAreaId);
            List<PtlJobOrderDto> ptlJobOrderDtoList = electronicTagFeignApi.findPtlJobOrderList(searchPtlJobOrder).getData();
            if (StringUtils.isNotEmpty(ptlJobOrderDtoList)) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "正在处理其他任务单，请稍后再试");
            }
        }

        List<RabbitMQDTO> list = new LinkedList<>();
        List<PtlJobOrderDet> ptlJobOrderDetList = new LinkedList<>();
        List<String> equipmentAreaTagIds = new LinkedList<>();
        for (PtlJobOrderDetDto ptlJobOrderDetDto : ptlJobOrderDetDtoList) {
            SearchPtlElectronicTagStorage searchPtlElectronicTagStorage = new SearchPtlElectronicTagStorage();
            searchPtlElectronicTagStorage.setMaterialId(ptlJobOrderDetDto.getMaterialId().toString());
            searchPtlElectronicTagStorage.setStorageId(ptlJobOrderDetDto.getStorageId().toString());
            List<PtlElectronicTagStorageDto> ptlElectronicTagStorageDtoList = electronicTagFeignApi.findElectronicTagStorageList(searchPtlElectronicTagStorage).getData();
            if (StringUtils.isEmpty(ptlElectronicTagStorageDtoList)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "请先维护储位和物料以及对应的电子标签关联信息");
            }
            for (PtlElectronicTagStorageDto ptlElectronicTagStorageDto : ptlElectronicTagStorageDtoList) {

                // 通道灯亮灯
                if (!equipmentAreaTagIds.contains(ptlElectronicTagStorageDto.getEquipmentAreaTagId())) {
                    RabbitMQDTO rabbitMQDTO = new RabbitMQDTO();
                    rabbitMQDTO.setEquipmentTagId(ptlElectronicTagStorageDto.getEquipmentTagId());
                    rabbitMQDTO.setElectronicTagId(ptlElectronicTagStorageDto.getEquipmentAreaTagId());
                    rabbitMQDTO.setQueueName(ptlElectronicTagStorageDto.getQueueName());
                    fanoutSender(1001, rabbitMQDTO);
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
                        materialDesc += ptlJobOrderDetDto.getMaterialName() + ptlJobOrderDetDto.getWholeQty() + ptlJobOrderDetDto.getWholeUnitName();
                        materialDesc = intercepting(materialDesc, 22);
                    }
                    if (StringUtils.isNotEmpty(ptlJobOrderDetDto.getScatteredQty()) && ptlJobOrderDetDto.getScatteredQty().compareTo(BigDecimal.ZERO) != 0) {
                        materialDesc2 += ptlJobOrderDetDto.getMaterialName() + ptlJobOrderDetDto.getScatteredQty() + ptlJobOrderDetDto.getScatteredUnitName();
                        materialDesc2 = intercepting(materialDesc2, 22);
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
                list.add(rabbitMQDTO);
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

        for (RabbitMQDTO rabbitMQDTO : list) {
            if (rabbitMQDTO.getElectronicTagLangType() == 1) {
                fanoutSender(1007, rabbitMQDTO);
                log.info("===========发送消息给客户端控制中文标签亮灯完成===============");
            } else {
                fanoutSender(1008, rabbitMQDTO);
                log.info("===========发送消息给客户端控制英文标签亮灯完成===============");
            }
        }

        return ptlJobOrder;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int createPtlJobOrder(List<PtlJobOrderDTO> ptlJobOrderDTOList) throws Exception {

        List<PtlJobOrderDet> list = new LinkedList<>();
        List<RabbitMQDTO> rabbitMQDTOList = new LinkedList<>();
        Long clientId = Long.valueOf(0);
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
            SearchSysUser searchSysUser = new SearchSysUser();
            searchSysUser.setUserCode(ptlJobOrderDTO.getWorkerCode());
            List<SysUser> sysUsers = securityFeignApi.selectUsers(searchSysUser).getData();
            if (StringUtils.isEmpty(baseWarehouses)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到对应作业人员信息");
            }
            PtlJobOrder ptlJobOrder = new PtlJobOrder();
            ptlJobOrder.setJobOrderCode(ptlJobOrderDTO.getTaskNo());
            ptlJobOrder.setJobOrderType((byte) 2);
            ptlJobOrder.setRelatedOrderCode(ptlJobOrderDTO.getCustomerNo());
            ptlJobOrder.setDespatchOrderCode(ptlJobOrderDTO.getDeliveryNo());
            ptlJobOrder.setWarehouseId(baseWarehouses.get(0).getWarehouseId());
            ptlJobOrder.setWarehouseName(baseWarehouses.get(0).getWarehouseName());
            ptlJobOrder.setWorkerUserId(sysUsers.get(0).getUserId());
            ptlJobOrder.setWorkerUserName(sysUsers.get(0).getUserName());
            ptlJobOrder.setOrderStatus((byte) 1);
            ptlJobOrder.setIfAlreadyPrint((byte) 0);
            ptlJobOrder.setStatus((byte) 1);
            ptlJobOrder.setOrgId(sysUsers.get(0).getOrganizationId());
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
                searchPtlElectronicTagStorage.setMaterialId(baseMaterials.get(0).getMaterialId().toString());
                searchPtlElectronicTagStorage.setStorageId(baseStorages.get(0).getStorageId().toString());
                List<PtlElectronicTagStorageDto> ptlElectronicTagStorageDtoList = electronicTagFeignApi.findElectronicTagStorageList(searchPtlElectronicTagStorage).getData();
                if (StringUtils.isEmpty(ptlElectronicTagStorageDtoList)) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "请先维护储位和物料以及对应的电子标签关联信息");
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
                if (!clientId.equals(ptlElectronicTagStorageDtoList.get(0).getClientId())) {
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
                    if (clientId == 0) {
                        clientId = ptlElectronicTagStorageDtoList.get(0).getClientId();
                    }
                } else {
                    clientId = ptlElectronicTagStorageDtoList.get(0).getClientId();
                }

                PtlJobOrderDet ptlJobOrderDet = new PtlJobOrderDet();
                ptlJobOrderDet.setJobOrderId(ptlJobOrder.getJobOrderId());
                ptlJobOrderDet.setStorageId(baseStorages.get(0).getStorageId());
                ptlJobOrderDet.setStorageCode(baseStorages.get(0).getStorageCode());
                ptlJobOrderDet.setMaterialId(baseMaterials.get(0).getMaterialId());
                ptlJobOrderDet.setMaterialCode(baseMaterials.get(0).getMaterialCode());
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
                ptlJobOrderDet.setOrgId(sysUsers.get(0).getOrganizationId());
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
            fanoutSender(1005, rabbitMQDTO);
            log.info("===========发送消息给客户端控制蜂鸣器开启完成===============");
        }

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public PtlJobOrderDto writeBackPtlJobOrder(Long jobOrderId, String status) throws Exception {

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        // 判断是否还有未关闭的电子标签
        SearchPtlJobOrderDet searchPtlJobOrderDet = new SearchPtlJobOrderDet();
        searchPtlJobOrderDet.setJobOrderId(jobOrderId);
        searchPtlJobOrderDet.setJobStatus((byte) 2);
        searchPtlJobOrderDet.setJobStatus((byte) 0);
        List<PtlJobOrderDetDto> ptlJobOrderDetDtoList = electronicTagFeignApi.findPtlJobOrderDetList(searchPtlJobOrderDet).getData();
        List<RabbitMQDTO> list = new LinkedList<>();
        for (PtlJobOrderDetDto ptlJobOrderDetDto : ptlJobOrderDetDtoList) {
            RabbitMQDTO rabbitMQDTO = new RabbitMQDTO();
            rabbitMQDTO.setEquipmentTagId(ptlJobOrderDetDto.getEquipmentTagId());
            rabbitMQDTO.setElectronicTagId(ptlJobOrderDetDto.getElectronicTagId());
            rabbitMQDTO.setQueueName(ptlJobOrderDetDto.getQueueName());
            list.add(rabbitMQDTO);
        }

        SearchPtlJobOrder searchPtlJobOrder = new SearchPtlJobOrder();
        searchPtlJobOrder.setJobOrderId(jobOrderId);
        List<PtlJobOrderDto> ptlJobOrderDtoList = electronicTagFeignApi.findPtlJobOrderList(searchPtlJobOrder).getData();
        PtlJobOrderDto ptlJobOrderDto = ptlJobOrderDtoList.get(0);
        PtlJobOrder ptlJobOrder = new PtlJobOrder();
        ptlJobOrder.setJobOrderId(ptlJobOrderDto.getJobOrderId());
        if ("F".equals(status)) {
            ptlJobOrder.setOrderStatus((byte) 3);
        } else if ("E".equals(status)){
            ptlJobOrder.setOrderStatus((byte) 4);
        } else {
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }
        ptlJobOrder.setModifiedUserId(currentUser.getUserId());
        ptlJobOrder.setModifiedTime(new Date());
        electronicTagFeignApi.updatePtlJobOrder(ptlJobOrder);

        PtlJobOrderDet ptlJobOrderDet = new PtlJobOrderDet();
        ptlJobOrderDet.setJobOrderId(jobOrderId);
        if ("F".equals(status)) {
            ptlJobOrderDet.setJobStatus((byte) 3);
        } else if ("E".equals(status)){
            ptlJobOrderDet.setJobStatus((byte) 4);
        } else {
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }
        ptlJobOrderDet.setModifiedUserId(currentUser.getUserId());
        ptlJobOrderDet.setModifiedTime(new Date());
        electronicTagFeignApi.updateByJobOrderId(ptlJobOrderDet);

        if (ptlJobOrderDto.getOrderStatus() != 2) {
            String message = "";
            if (ptlJobOrderDto.getOrderStatus() == 1) {
                message = "请先激活任务单，再进行操作";
            }
            if (ptlJobOrderDto.getOrderStatus() == 3) {
                message = "该任务单已完成";
            }
            if (ptlJobOrderDto.getOrderStatus() == 4) {
                message = "该任务单处于异常状态，不可操作";
            }
            throw new BizErrorException(ErrorCodeEnum.GL99990500.getCode(), message);
        }
        PtlJobOrderDTO ptlJobOrderDTO = new PtlJobOrderDTO();
        ptlJobOrderDTO.setTaskNo(ptlJobOrderDto.getJobOrderCode());
        ptlJobOrderDTO.setCustomerNo(ptlJobOrderDto.getRelatedOrderCode());
        ptlJobOrderDTO.setWarehouseCode(ptlJobOrderDto.getWarehouseCode());
        ptlJobOrderDTO.setWorkerCode(ptlJobOrderDto.getWorkerUserCode());
        ptlJobOrderDTO.setStatus(status);
        // 回写PTL作业任务
        log.info("电子作业标签完成/异常，回传WMS" + JSONObject.toJSONString(ptlJobOrderDTO));
        String url = "";
        String result = RestTemplateUtil.postJsonStrForString(ptlJobOrderDTO, url);
        log.info("电子作业标签完成回写返回信息：" + result);
        ResponseEntity responseEntity = com.fantechs.common.base.utils.BeanUtils.convertJson(result, new TypeToken<ResponseEntity>() {
        }.getType());
        if (responseEntity.getCode() != 0 && responseEntity.getCode() != 200) {
            throw new Exception("电子作业标签完成，回传WMS失败：" + responseEntity.getMessage());
        }

        for (RabbitMQDTO rabbitMQDTO : list) {
            fanoutSender(1002, rabbitMQDTO);
            log.info("===========发送消息给客户端控制剩余电子标签灭灯完成===============");
        }

        return ptlJobOrderDto;
    }

    @Override
    public void fanoutSender(Integer code, RabbitMQDTO rabbitMQDTO) throws Exception{

        //不同的标签可能对应的队列不一样，最终一条一条发给客户端
        MQResponseEntity mQResponseEntity = new MQResponseEntity<>();
        mQResponseEntity.setCode(code);
        mQResponseEntity.setData(rabbitMQDTO);
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

    @Override
    public String sendElectronicTagStorageLightTest(String materialCode, Integer code) throws Exception {

        SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
        searchBaseMaterial.setMaterialCode(materialCode);
        List<BaseMaterial> baseMaterials = baseFeignApi.findSmtMaterialList(searchBaseMaterial).getData();
        if (StringUtils.isEmpty(baseMaterials)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到对应物料信息");
        }
        SearchPtlElectronicTagStorage searchPtlElectronicTagStorage = new SearchPtlElectronicTagStorage();
        searchPtlElectronicTagStorage.setMaterialId(baseMaterials.get(0).getMaterialId().toString());
        List<PtlElectronicTagStorageDto> ptlElectronicTagStorageDtoList = electronicTagFeignApi.findElectronicTagStorageList(searchPtlElectronicTagStorage).getData();

        if (StringUtils.isEmpty(ptlElectronicTagStorageDtoList)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "请先维护储位对应的电子标签信息");
        }
        if (StringUtils.isEmpty(ptlElectronicTagStorageDtoList.get(0).getStorageCode())) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到物料对应的储位信息");
        }

        //不同的标签可能对应的队列不一样，最终一条一条发给客户端
        for (PtlElectronicTagStorageDto ptlElectronicTagStorageDto : ptlElectronicTagStorageDtoList) {
            RabbitMQDTO rabbitMQDTO = new RabbitMQDTO();
            rabbitMQDTO.setEquipmentTagId(ptlElectronicTagStorageDto.getEquipmentTagId());
            rabbitMQDTO.setElectronicTagId(ptlElectronicTagStorageDto.getElectronicTagId());
            rabbitMQDTO.setOption1("0");
            rabbitMQDTO.setOption2("1");
            rabbitMQDTO.setOption3("0");
            rabbitMQDTO.setQueueName(ptlElectronicTagStorageDto.getQueueName());
            fanoutSender(code, rabbitMQDTO);
            log.info("===========发送消息给客户端完成===============");
        }

        return "0";
    }
}
