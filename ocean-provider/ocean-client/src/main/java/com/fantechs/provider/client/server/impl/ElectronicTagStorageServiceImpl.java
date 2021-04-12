package com.fantechs.provider.client.server.impl;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.storage.SmtStorageInventoryDetDto;
import com.fantechs.common.base.dto.storage.SmtStorageInventoryDto;
import com.fantechs.common.base.electronic.dto.SmtElectronicTagStorageDto;
import com.fantechs.common.base.electronic.dto.SmtLoadingDetDto;
import com.fantechs.common.base.electronic.dto.SmtSortingDto;
import com.fantechs.common.base.electronic.entity.SmtLoading;
import com.fantechs.common.base.electronic.entity.SmtLoadingDet;
import com.fantechs.common.base.electronic.entity.SmtPaddingMaterial;
import com.fantechs.common.base.electronic.entity.SmtSorting;
import com.fantechs.common.base.electronic.entity.search.SearchSmtElectronicTagStorage;
import com.fantechs.common.base.electronic.entity.search.SearchSmtLoading;
import com.fantechs.common.base.electronic.entity.search.SearchSmtLoadingDet;
import com.fantechs.common.base.electronic.entity.search.SearchSmtSorting;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseStorageMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageInventory;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageInventoryDet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorageMaterial;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.storage.SmtStorageInventory;
import com.fantechs.common.base.entity.storage.SmtStorageInventoryDet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.MQResponseEntity;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.RestTemplateUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.electronic.ElectronicTagFeignApi;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;
import com.fantechs.provider.api.imes.storage.StorageInventoryFeignApi;
import com.fantechs.provider.client.config.RabbitConfig;
import com.fantechs.provider.client.dto.PtlLoadingDTO;
import com.fantechs.provider.client.dto.PtlLoadingDetDTO;
import com.fantechs.provider.client.dto.PtlSortingDTO;
import com.fantechs.provider.client.dto.PtlSortingDetailDTO;
import com.fantechs.provider.client.listener.ElectronicTagReceiver;
import com.fantechs.provider.client.server.ElectronicTagStorageService;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by lfz on 2020/12/9.
 */
@Service
public class ElectronicTagStorageServiceImpl implements ElectronicTagStorageService {
    private static final Logger log = LoggerFactory.getLogger(ElectronicTagReceiver.class);
    @Autowired
    private FanoutSender fanoutSender;
    @Autowired
    private ElectronicTagFeignApi electronicTagFeignApi;
    @Autowired
    private BasicFeignApi basicFeignApi;
    @Autowired
    private StorageInventoryFeignApi storageInventoryFeignApi;

    @Value("${mesAPI.resApi}")
    private String resApiUrl;
    @Value("${qisAPI.confirmOutBillOrder}")
    private String confirmOutBillOrderUrl;
    @Value("${qisAPI.confirmInBillOrder}")
    private String confirmInBillOrderUrl;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public List<SmtSortingDto> sendElectronicTagStorage(String sortingCode) throws Exception {

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<SmtSortingDto> sortingDtoList = new LinkedList<>();
        synchronized (ElectronicTagStorageServiceImpl.class) {
            //是否有在做单据
            SearchSmtSorting searchSmtSorting = new SearchSmtSorting();
            searchSmtSorting.setStatus((byte) 1);
            searchSmtSorting.setNotEqualSortingCode(sortingCode);
            List<SmtSortingDto> smtSortingList = electronicTagFeignApi.findSortingList(searchSmtSorting).getData();
            if (StringUtils.isNotEmpty(smtSortingList)) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "正在处理其他分拣单，请稍后再试");
            }
            SearchSmtLoading searchSmtLoading = new SearchSmtLoading();
            searchSmtLoading.setStatus((byte) 1);
            List<SmtLoading> smtLoadingList = electronicTagFeignApi.findLoadingList(searchSmtLoading).getData();
            if (StringUtils.isNotEmpty(smtLoadingList)) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "正在处理其他上料单，请稍后再试");
            }

            searchSmtSorting = new SearchSmtSorting();
            searchSmtSorting.setSortingCode(sortingCode);
            searchSmtSorting.setNotEqualstatus((byte) 2);
            sortingDtoList = electronicTagFeignApi.findSortingList(searchSmtSorting).getData();
            if (StringUtils.isEmpty(sortingDtoList)) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "该分拣单没有可拣料的物料");
            }
        }

        List<SmtElectronicTagStorageDto> list = new LinkedList<>();
        List<SmtSorting> smtSortingList = new LinkedList<>();
        for (SmtSorting sorting : sortingDtoList) {
            SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
            searchBaseMaterial.setMaterialCode(sorting.getMaterialCode());
            List<BaseMaterial> baseMaterials = basicFeignApi.findSmtMaterialList(searchBaseMaterial).getData();
            if (StringUtils.isEmpty(baseMaterials)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到对应物料信息");
            }
            SearchSmtElectronicTagStorage searchSmtElectronicTagStorage = new SearchSmtElectronicTagStorage();
            searchSmtElectronicTagStorage.setMaterialId(baseMaterials.get(0).getMaterialId().toString());
            List<SmtElectronicTagStorageDto> smtElectronicTagStorageDtoList = electronicTagFeignApi.findElectronicTagStorageList(searchSmtElectronicTagStorage).getData();

            if (StringUtils.isEmpty(smtElectronicTagStorageDtoList)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "请先维护储位对应的电子标签信息");
            }
            if (StringUtils.isEmpty(smtElectronicTagStorageDtoList.get(0).getStorageCode())) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到物料对应的储位信息");
            }
            smtElectronicTagStorageDtoList.get(0).setQuantity(sorting.getQuantity());
            smtElectronicTagStorageDtoList.get(0).setOrderType((byte) 1);
            list.add(smtElectronicTagStorageDtoList.get(0));

            // 查询物料库存，库存不足不能进行拣料
            /*SearchSmtStorageInventory searchSmtStorageInventory = new SearchSmtStorageInventory();
            searchSmtStorageInventory.setMaterialId(smtElectronicTagStorageDtoList.get(0).getMaterialId());
            searchSmtStorageInventory.setStorageId(smtElectronicTagStorageDtoList.get(0).getStorageId());
            searchSmtStorageInventory.setStatus((byte) 1);
            List<SmtStorageInventoryDto> smtStorageInventoryDtoList = storageInventoryFeignApi.findStorageInventoryList(searchSmtStorageInventory).getData();
            if (StringUtils.isEmpty(smtStorageInventoryDtoList)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到物料：" + sorting.getMaterialCode() + " 对应的库存信息");
            }
            if (smtStorageInventoryDtoList.get(0).getQuantity().compareTo(sorting.getQuantity()) == -1) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "物料：" + sorting.getMaterialCode() + " 库存不足不能进行拣料！");
            }*/

            SmtSorting smtSorting = new SmtSorting();
            smtSorting.setSortingId(sorting.getSortingId());
            smtSorting.setStatus((byte) 1);
            smtSorting.setModifiedUserId(currentUser.getUserId());
            smtSorting.setModifiedTime(new Date());
            smtSortingList.add(smtSorting);
        }

        for (SmtElectronicTagStorageDto smtElectronicTagStorageDto : list) {

            //不同的标签可能对应的队列不一样，最终一条一条发给客户端
            fanoutSender(1001, smtElectronicTagStorageDto);
            if (StringUtils.isNotEmpty(smtElectronicTagStorageDto.getEquipmentAreaId())) {
                fanoutSender(1002, smtElectronicTagStorageDto);
            }
        }

        if (StringUtils.isNotEmpty(smtSortingList)) {
            electronicTagFeignApi.batchUpdateSorting(smtSortingList).getCount();
        }

        return sortingDtoList;
    }

    @Override
    public SmtElectronicTagStorageDto sendPlaceMaterials(String materialCode) {
        if (StringUtils.isEmpty(materialCode)) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }
        SearchBaseStorageMaterial searchBaseStorageMaterial = new SearchBaseStorageMaterial();
        searchBaseStorageMaterial.setMaterialCode(materialCode);
        List<BaseStorageMaterial> storageMaterialList = basicFeignApi.findStorageMaterialList(searchBaseStorageMaterial).getData();
        if (StringUtils.isEmpty(storageMaterialList)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到对应物料信息");
        }

        SearchSmtElectronicTagStorage searchSmtElectronicTagStorage = new SearchSmtElectronicTagStorage();
        searchSmtElectronicTagStorage.setMaterialId(storageMaterialList.get(0).getMaterialId().toString());
        List<SmtElectronicTagStorageDto> smtElectronicTagStorageDtoList = electronicTagFeignApi.findElectronicTagStorageList(searchSmtElectronicTagStorage).getData();
        if (StringUtils.isEmpty(smtElectronicTagStorageDtoList)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "请先维护物料对应储位的信息");
        }
        SmtPaddingMaterial smtPaddingMaterial = new SmtPaddingMaterial();
        smtPaddingMaterial.setMaterialCode(materialCode);
        smtPaddingMaterial.setQuantity(BigDecimal.ZERO);
        smtPaddingMaterial.setStatus((byte) 2);
        smtPaddingMaterial.setPaddingMaterialCode(CodeUtils.getScheduleNo("PM-"));
        electronicTagFeignApi.addPaddingMaterial(smtPaddingMaterial);
//        MQResponseEntity mQResponseEntity =  new MQResponseEntity<>();
//        mQResponseEntity.setCode(1001);
//        smtElectronicTagStorageDtoList.get(0).setQuantity(BigDecimal.ZERO);
//        mQResponseEntity.setData(smtElectronicTagStorageDtoList.get(0));
//        fanoutSender.send(smtElectronicTagStorageDtoList.get(0).getQueueName(),
//                JSONObject.toJSONString(mQResponseEntity));
        return smtElectronicTagStorageDtoList.get(0);
    }

    @Override
    public int batchSortingDelete(List<String> sortingCodes) throws Exception {
        electronicTagFeignApi.batchDeleteSorting(sortingCodes);
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int createSorting(List<SmtSorting> sortingList) throws Exception {

        List<SmtSorting> list = new LinkedList<>();
        for (SmtSorting sorting : sortingList) {
            SearchSmtSorting searchSmtSorting = new SearchSmtSorting();
            searchSmtSorting.setSortingCode(sorting.getSortingCode());
            List<SmtSortingDto> smtSortingList = electronicTagFeignApi.findSortingList(searchSmtSorting).getData();
            if (StringUtils.isNotEmpty(smtSortingList)) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "重复分拣单");
            }
            SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
            searchBaseMaterial.setMaterialCode(sorting.getMaterialCode());
            List<BaseMaterial> baseMaterials = basicFeignApi.findSmtMaterialList(searchBaseMaterial).getData();
            if (StringUtils.isEmpty(baseMaterials)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到对应物料信息");
            }
            SearchSmtElectronicTagStorage searchSmtElectronicTagStorage = new SearchSmtElectronicTagStorage();
            searchSmtElectronicTagStorage.setMaterialId(baseMaterials.get(0).getMaterialId().toString());
            List<SmtElectronicTagStorageDto> smtElectronicTagStorageDtoList = electronicTagFeignApi.findElectronicTagStorageList(searchSmtElectronicTagStorage).getData();

            if (StringUtils.isEmpty(smtElectronicTagStorageDtoList)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "请先维护储位对应的电子标签信息");
            }
            if (StringUtils.isEmpty(smtElectronicTagStorageDtoList.get(0).getStorageCode())) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到物料对应的储位信息");
            }

            sorting.setStatus((byte) 0);
            sorting.setCreateTime(new Date());
            sorting.setModifiedTime(new Date());
            sorting.setIsDetele((byte) 1);
            list.add(sorting);

        }
        if (StringUtils.isNotEmpty(list)) {
            electronicTagFeignApi.batchInsertSmtSorting(list).getCount();
        }

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int createLoading(List<SmtLoading> smtLoadingList) throws Exception {

        for (SmtLoading smtLoading : smtLoadingList) {

            SearchSmtLoading searchSmtLoading = new SearchSmtLoading();
            searchSmtLoading.setLoadingCode(smtLoading.getLoadingCode());
            List<SmtLoading> smtLoadings = electronicTagFeignApi.findLoadingList(searchSmtLoading).getData();
            if (StringUtils.isNotEmpty(smtLoadings) && smtLoading.getOrderType() != 1) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "重复入库单");
            }
            if (StringUtils.isNotEmpty(smtLoadings) && smtLoadings.get(0).getStatus() == 1) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "该上料单正在进行上料操作，请稍后再进行同步！");
            }
            if (StringUtils.isNotEmpty(smtLoadings) && smtLoadings.get(0).getStatus() != 0 && smtLoading.getOrderType() != 1) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "该上料单已进行上料操作");
            }
            if (smtLoading.getOrderType() == 1 && StringUtils.isNotEmpty(smtLoadings)) {
                // 查询本次传入采购订单物料明细比较上次是否有删减
                SearchSmtLoadingDet searchSmtLoadingDet = new SearchSmtLoadingDet();
                searchSmtLoadingDet.setLoadingCode(smtLoading.getLoadingCode());
                List<SmtLoadingDetDto> smtLoadingDetDtoList = electronicTagFeignApi.findLoadingDetList(searchSmtLoadingDet).getData();
                for (SmtLoadingDetDto smtLoadingDetDto : smtLoadingDetDtoList) {
                    Boolean deleteBoolean = true;
                    for (SmtLoadingDetDto smtLoadingDetDto2 : smtLoading.getSmtLoadingDetDtoList()) {
                        if (smtLoadingDetDto.getMaterialCode().equals(smtLoadingDetDto2.getMaterialCode())) {
                            deleteBoolean = false;
                            break;
                        }
                    }
                    if (deleteBoolean) {
                        SmtLoadingDet smtLoadingDet = new SmtLoadingDet();
                        smtLoadingDet.setLoadingDetId(smtLoadingDetDto.getLoadingDetId());
                        smtLoadingDet.setIsDetele((byte) 0);
                        electronicTagFeignApi.updateLoadingDet(smtLoadingDet);
                    }
                }
            }
            if (StringUtils.isEmpty(smtLoadings)) {
                smtLoading = electronicTagFeignApi.addSmtLoading(smtLoading).getData();
            } else {
                smtLoading.setLoadingId(smtLoadings.get(0).getLoadingId());
            }

            for (SmtLoadingDetDto smtLoadingDetDto : smtLoading.getSmtLoadingDetDtoList()) {

                SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
                searchBaseMaterial.setMaterialCode(smtLoadingDetDto.getMaterialCode());
                List<BaseMaterial> baseMaterials = basicFeignApi.findSmtMaterialList(searchBaseMaterial).getData();
                if (StringUtils.isEmpty(baseMaterials)) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到对应物料信息");
                }
                SearchSmtElectronicTagStorage searchSmtElectronicTagStorage = new SearchSmtElectronicTagStorage();
                searchSmtElectronicTagStorage.setMaterialId(baseMaterials.get(0).getMaterialId().toString());
                List<SmtElectronicTagStorageDto> smtElectronicTagStorageDtoList = electronicTagFeignApi.findElectronicTagStorageList(searchSmtElectronicTagStorage).getData();

                if (StringUtils.isEmpty(smtElectronicTagStorageDtoList)) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "请先维护储位对应的电子标签信息");
                }
                if (StringUtils.isEmpty(smtElectronicTagStorageDtoList.get(0).getStorageCode())) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到物料对应的储位信息");
                }

                List<SmtLoadingDetDto> smtLoadingDetDtoList = new LinkedList<>();
                if (smtLoading.getOrderType() == 1) {
                    SearchSmtLoadingDet searchSmtLoadingDet = new SearchSmtLoadingDet();
                    searchSmtLoadingDet.setLoadingId(smtLoading.getLoadingId());
                    searchSmtLoadingDet.setMaterialCode(smtLoadingDetDto.getMaterialCode());
                    smtLoadingDetDtoList = electronicTagFeignApi.findLoadingDetList(searchSmtLoadingDet).getData();
                }

                SmtLoadingDet smtLoadingDet = new SmtLoadingDet();
                smtLoadingDet.setLoadingId(smtLoading.getLoadingId());
                smtLoadingDet.setMaterialId(baseMaterials.get(0).getMaterialId());
                smtLoadingDet.setPlanQty(smtLoadingDetDto.getPlanQty());
                if (StringUtils.isEmpty(smtLoadingDetDtoList)) {
                    electronicTagFeignApi.addSmtLoadingDet(smtLoadingDet);
                } else {
                    smtLoadingDet.setLoadingDetId(smtLoadingDetDtoList.get(0).getLoadingDetId());
                    if (smtLoadingDetDtoList.get(0).getStatus() == 3 && smtLoadingDetDto.getPlanQty().compareTo(smtLoadingDetDtoList.get(0).getActualQty()) == 1) {
                        smtLoadingDet.setStatus((byte) 2);
                    }
                    electronicTagFeignApi.updateLoadingDet(smtLoadingDet);
                }
            }
        }

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public List<SmtLoadingDetDto> sendLoadingElectronicTagStorage(String loadingCode) throws Exception {

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        // 判断是否有其他上料单正在进行中
        List<SmtLoading> smtLoadingList = new LinkedList<>();
        List<SmtLoadingDetDto> smtLoadingDetDtoList = new LinkedList<>();
        synchronized (ElectronicTagStorageServiceImpl.class) {
            SearchSmtLoading searchSmtLoading = new SearchSmtLoading();
            searchSmtLoading.setStatus((byte) 1);
            smtLoadingList = electronicTagFeignApi.findLoadingList(searchSmtLoading).getData();
            if (StringUtils.isNotEmpty(smtLoadingList) && (smtLoadingList.size() > 1 || !loadingCode.equals(smtLoadingList.get(0).getLoadingCode()))) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "正在处理其他上料单，请稍后再试");
            }
            if (StringUtils.isNotEmpty(smtLoadingList) && smtLoadingList.get(0).getStatus() == 3) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "该上料单已经完成！");
            }
            SearchSmtSorting searchSmtSorting = new SearchSmtSorting();
            searchSmtSorting.setStatus((byte) 1);
            List<SmtSortingDto> smtSortingList = electronicTagFeignApi.findSortingList(searchSmtSorting).getData();
            if (StringUtils.isNotEmpty(smtSortingList)) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "正在处理其他分拣单，请稍后再试");
            }

            SearchSmtLoadingDet searchSmtLoadingDet = new SearchSmtLoadingDet();
            searchSmtLoadingDet.setLoadingCode(loadingCode);
            searchSmtLoadingDet.setNotEqualstatus((byte) 3);
            smtLoadingDetDtoList = electronicTagFeignApi.findLoadingDetList(searchSmtLoadingDet).getData();
            if (StringUtils.isEmpty(smtLoadingDetDtoList)) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "该上料单没有可上料的物料");
            }
        }

        SmtLoading smtLoading = new SmtLoading();
        smtLoading.setLoadingId(smtLoadingDetDtoList.get(0).getLoadingId());
        smtLoading.setStatus((byte) 1);
        smtLoading.setModifiedUserId(currentUser.getUserId());
        electronicTagFeignApi.updateLoading(smtLoading);

        for (SmtLoadingDetDto smtLoadingDetDto : smtLoadingDetDtoList) {
            SearchSmtElectronicTagStorage searchSmtElectronicTagStorage = new SearchSmtElectronicTagStorage();
            searchSmtElectronicTagStorage.setMaterialId(smtLoadingDetDto.getMaterialId().toString());
            List<SmtElectronicTagStorageDto> smtElectronicTagStorageDtoList = electronicTagFeignApi.findElectronicTagStorageList(searchSmtElectronicTagStorage).getData();

            if (StringUtils.isEmpty(smtElectronicTagStorageDtoList)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "请先维护储位对应的电子标签信息");
            }
            if (StringUtils.isEmpty(smtElectronicTagStorageDtoList.get(0).getStorageCode())) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到物料对应的储位信息");
            }
            smtElectronicTagStorageDtoList.get(0).setQuantity(smtLoadingDetDto.getPlanQty().subtract(smtLoadingDetDto.getActualQty()));
            smtElectronicTagStorageDtoList.get(0).setOrderType((byte) 2);

            SmtLoadingDet smtLoadingDet = new SmtLoadingDet();
            smtLoadingDet.setLoadingDetId(smtLoadingDetDto.getLoadingDetId());
            smtLoadingDet.setStatus((byte) 1);
            smtLoadingDet.setModifiedUserId(currentUser.getUserId());
            electronicTagFeignApi.updateLoadingDet(smtLoadingDet);

            // 不同的标签可能对应的队列不一样，最终一条一条发给客户端，控制biao亮灯
            fanoutSender(1001, smtElectronicTagStorageDtoList.get(0));
            // 控制区域灯亮灯
            if (StringUtils.isNotEmpty(smtElectronicTagStorageDtoList.get(0).getEquipmentAreaId())) {
                fanoutSender(1002, smtElectronicTagStorageDtoList.get(0));
            }
        }

        return smtLoadingDetDtoList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int submitLoadingDet(List<SmtLoadingDetDto> smtLoadingDetDtoList) throws Exception {

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        SearchSmtLoading searchSmtLoading = new SearchSmtLoading();
        searchSmtLoading.setLoadingCode(smtLoadingDetDtoList.get(0).getLoadingCode());
        List<SmtLoading> smtLoadingList = electronicTagFeignApi.findLoadingList(searchSmtLoading).getData();
        if (StringUtils.isEmpty(smtLoadingList)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到该上料单！");
        } else if (smtLoadingList.get(0).getStatus() == 0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "该上料单物料明细电子标签尚未亮灯，请先进行亮灯！");
        } else if (smtLoadingList.get(0).getStatus() == 3){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "该上料单已完成！");
        }

        // 回传MES/QIS上料单
        PtlLoadingDTO ptlLoadingDTO = new PtlLoadingDTO();
        ptlLoadingDTO.setLoadingCode(smtLoadingDetDtoList.get(0).getLoadingCode());
        if (StringUtils.isNotEmpty(currentUser)) {
            ptlLoadingDTO.setUser(currentUser.getUserCode());
        }

        List<PtlLoadingDetDTO> ptlLoadingDetDTOList = new LinkedList<>();
        SmtLoading smtLoading = new SmtLoading();
        smtLoading.setLoadingId(smtLoadingList.get(0).getLoadingId());
        Byte status = 3;
        for (SmtLoadingDetDto smtLoadingDetDto : smtLoadingDetDtoList) {

            SmtLoadingDet smtLoadingDet = electronicTagFeignApi.detailSmtLoadingDet(smtLoadingDetDto.getLoadingDetId()).getData();
            BigDecimal qty = smtLoadingDet.getPlanQty().subtract(smtLoadingDet.getActualQty());
            System.out.println("qty：" + qty + "====================================");
            System.out.println("smtLoadingDetDto.getActualQty()：" + smtLoadingDetDto.getActualQty() + "==============================");
            System.out.println(qty.compareTo(smtLoadingDetDto.getActualQty()) + "===========================================");
            if (qty.compareTo(smtLoadingDetDto.getActualQty()) < 0) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "实际上料数量不能超过计划上料数量！");
            }
            if (qty.compareTo(smtLoadingDetDto.getActualQty()) != 0) {
                if (smtLoadingList.get(0).getOrderType() != 1) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "当前单据来源不是采购订单，不能分批入库，实际上料数量不能小于计划上料数量！");
                }
                status = 2;
                smtLoadingDet.setStatus((byte) 2);
            } else {
                smtLoadingDet.setStatus((byte) 3);
            }
            smtLoadingDet.setActualQty(smtLoadingDet.getActualQty().add(smtLoadingDetDto.getActualQty()));
            electronicTagFeignApi.updateLoadingDet(smtLoadingDet);

            // 查询物料库存
            SearchSmtStorageInventory searchSmtStorageInventory = new SearchSmtStorageInventory();
            searchSmtStorageInventory.setMaterialId(smtLoadingDetDto.getMaterialId());
            searchSmtStorageInventory.setStorageId(smtLoadingDetDto.getStorageId());
            searchSmtStorageInventory.setStatus((byte) 1);
            List<SmtStorageInventoryDto> smtStorageInventoryDtoList = storageInventoryFeignApi.findList(searchSmtStorageInventory).getData();

            List<SmtStorageInventoryDetDto> smtStorageInventoryDetDtoList = new LinkedList<>();
            // 更新物料库存信息
            SmtStorageInventory smtStorageInventory = new SmtStorageInventory();
            smtStorageInventory.setMaterialId(smtLoadingDetDto.getMaterialId());
            smtStorageInventory.setStorageId(smtLoadingDetDto.getStorageId());
            if (StringUtils.isEmpty(smtStorageInventoryDtoList)) {
                smtStorageInventory.setQuantity(smtLoadingDetDto.getActualQty());
                smtStorageInventory = storageInventoryFeignApi.add(smtStorageInventory).getData();
            } else {
                smtStorageInventory.setStorageInventoryId(smtStorageInventoryDtoList.get(0).getStorageInventoryId());
                smtStorageInventory.setQuantity(smtStorageInventoryDtoList.get(0).getQuantity().add(smtLoadingDetDto.getActualQty()));
                storageInventoryFeignApi.update(smtStorageInventory);

                // 查询物料入库明细
                SearchSmtStorageInventoryDet searchSmtStorageInventoryDet = new SearchSmtStorageInventoryDet();
                searchSmtStorageInventoryDet.setStorageInventoryId(smtStorageInventoryDtoList.get(0).getStorageInventoryId());
                searchSmtStorageInventoryDet.setGodownEntry(smtLoadingDetDto.getLoadingCode());
                searchSmtStorageInventoryDet.setStatus((byte) 1);
                smtStorageInventoryDetDtoList = storageInventoryFeignApi.findStorageInventoryDetList(searchSmtStorageInventoryDet).getData();
            }

            // 更新物料入库明细
            SmtStorageInventoryDet smtStorageInventoryDet = new SmtStorageInventoryDet();
            smtStorageInventoryDet.setStorageInventoryId(smtStorageInventory.getStorageInventoryId());
            smtStorageInventoryDet.setMaterialBarcodeCode(smtLoadingDetDto.getMaterialCode());
            smtStorageInventoryDet.setGodownEntry(smtLoadingDetDto.getLoadingCode());
            if (StringUtils.isEmpty(smtStorageInventoryDetDtoList)) {
                smtStorageInventoryDet.setMaterialQuantity(smtLoadingDetDto.getActualQty());
                storageInventoryFeignApi.add(smtStorageInventoryDet);
            } else {
                smtStorageInventoryDet.setStorageInventoryDetId(smtStorageInventoryDetDtoList.get(0).getStorageInventoryDetId());
                smtStorageInventoryDet.setMaterialQuantity(smtStorageInventoryDetDtoList.get(0).getMaterialQuantity().add(smtLoadingDetDto.getActualQty()));
                storageInventoryFeignApi.updateStorageInventoryDet(smtStorageInventoryDet);
            }

            SearchSmtElectronicTagStorage searchSmtElectronicTagStorage = new SearchSmtElectronicTagStorage();
            searchSmtElectronicTagStorage.setMaterialId(smtLoadingDetDto.getMaterialId().toString());
            List<SmtElectronicTagStorageDto> smtElectronicTagStorageDtoList = electronicTagFeignApi.findElectronicTagStorageList(searchSmtElectronicTagStorage).getData();

            if (StringUtils.isEmpty(smtElectronicTagStorageDtoList)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "请先维护储位对应的电子标签信息");
            }
            if (StringUtils.isEmpty(smtElectronicTagStorageDtoList.get(0).getStorageCode())) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到物料对应的储位信息");
            }
            smtElectronicTagStorageDtoList.get(0).setQuantity(smtLoadingDetDto.getPlanQty().subtract(smtLoadingDetDto.getActualQty()));
            smtElectronicTagStorageDtoList.get(0).setOrderType((byte) 2);

            // 发给客户端，控制灭灯
            fanoutSender(1003, smtElectronicTagStorageDtoList.get(0));
            // 控制区域灯亮灯
            if (StringUtils.isNotEmpty(smtElectronicTagStorageDtoList.get(0).getEquipmentAreaId())) {
                fanoutSender(1004, smtElectronicTagStorageDtoList.get(0));
            }

            if (BigDecimal.ZERO.compareTo(smtLoadingDetDto.getActualQty()) != 0) {
                PtlLoadingDetDTO ptlLoadingDetDTO = new PtlLoadingDetDTO();
                ptlLoadingDetDTO.setMaterialCode(smtLoadingDetDto.getMaterialCode());
                ptlLoadingDetDTO.setActualQty(smtLoadingDetDto.getActualQty());
                ptlLoadingDetDTO.setStorageCode(smtElectronicTagStorageDtoList.get(0).getStorageCode());
                ptlLoadingDetDTOList.add(ptlLoadingDetDTO);
            }

            ptlLoadingDTO.setWarehouseCode(smtElectronicTagStorageDtoList.get(0).getWarehouseCode());
        }

        smtLoading.setStatus(status);
        electronicTagFeignApi.updateLoading(smtLoading);
        smtLoading = electronicTagFeignApi.detailSmtLoading(smtLoading.getLoadingId()).getData();

        ptlLoadingDTO.setPtlLoadingDetDTOList(ptlLoadingDetDTOList);

        log.info("上料单号处理完，回传给MES：" + JSONObject.toJSONString(ptlLoadingDTO));
        String url = "";
        if ("MES".equals(smtLoading.getSourceSys())) {
            url = "";
        } else if ("QIS".equals(smtLoading.getSourceSys())) {
            url = confirmInBillOrderUrl;
        }
        String result = RestTemplateUtil.postJsonStrForString(ptlLoadingDTO, url);
        log.info("MES返回信息：" + result);
        ResponseEntity responseEntity = com.fantechs.common.base.utils.BeanUtils.convertJson(result, new TypeToken<ResponseEntity>(){}.getType());
        if (responseEntity.getCode() != 0 && responseEntity.getCode() != 200) {
            throw new Exception("分拣单号处理完，回传给MES失败：" + responseEntity.getMessage());
        }

        return 1;

    }

    @Override
    public int revokeLoading(String loadingCode) throws Exception {

        SearchSmtLoadingDet searchSmtLoadingDet = new SearchSmtLoadingDet();
        searchSmtLoadingDet.setLoadingCode(loadingCode);
        searchSmtLoadingDet.setStatus((byte) 1);
        List<SmtLoadingDetDto> smtLoadingDetDtoList = electronicTagFeignApi.findLoadingDetList(searchSmtLoadingDet).getData();
        if (StringUtils.isEmpty(smtLoadingDetDtoList)) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "当前上料单不存在未上料的物料，无需撤销！");
        }

        SmtLoading smtLoading = new SmtLoading();
        smtLoading.setLoadingId(smtLoadingDetDtoList.get(0).getLoadingId());
        smtLoading.setStatus((byte) 2);
        electronicTagFeignApi.updateLoading(smtLoading);

        for (SmtLoadingDetDto smtLoadingDetDto : smtLoadingDetDtoList) {
            SearchSmtElectronicTagStorage searchSmtElectronicTagStorage = new SearchSmtElectronicTagStorage();
            searchSmtElectronicTagStorage.setMaterialId(smtLoadingDetDto.getMaterialId().toString());
            List<SmtElectronicTagStorageDto> smtElectronicTagStorageDtoList = electronicTagFeignApi.findElectronicTagStorageList(searchSmtElectronicTagStorage).getData();

            if (StringUtils.isEmpty(smtElectronicTagStorageDtoList)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "请先维护储位对应的电子标签信息");
            }
            if (StringUtils.isEmpty(smtElectronicTagStorageDtoList.get(0).getStorageCode())) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到物料对应的储位信息");
            }
            smtElectronicTagStorageDtoList.get(0).setQuantity(smtLoadingDetDto.getPlanQty().subtract(smtLoadingDetDto.getActualQty()));
            smtElectronicTagStorageDtoList.get(0).setOrderType((byte) 2);

            SmtLoadingDet smtLoadingDet = new SmtLoadingDet();
            smtLoadingDet.setLoadingDetId(smtLoadingDetDto.getLoadingDetId());
            smtLoadingDet.setStatus((byte) 2);
            electronicTagFeignApi.updateLoadingDet(smtLoadingDet);

            // 不同的标签可能对应的队列不一样，最终一条一条发给客户端，控制灭灯
            fanoutSender(1003, smtElectronicTagStorageDtoList.get(0));
            // 控制区域灯亮灯
            if (StringUtils.isNotEmpty(smtElectronicTagStorageDtoList.get(0).getEquipmentAreaId())) {
                fanoutSender(1004, smtElectronicTagStorageDtoList.get(0));
            }
        }

        return 1;
    }

    public int comfirmLoadingDet(SmtLoadingDetDto smtLoadingDetDto) throws Exception {

        SearchSmtLoading searchSmtLoading = new SearchSmtLoading();
        searchSmtLoading.setLoadingCode(smtLoadingDetDto.getLoadingCode());
        List<SmtLoading> smtLoadingList = electronicTagFeignApi.findLoadingList(searchSmtLoading).getData();
        if (smtLoadingList.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "没有找到对应的上料单！");
        }
        if (smtLoadingList.get(0).getOrderType() == 2 && smtLoadingDetDto.getPlanQty().compareTo(smtLoadingDetDto.getActualQty()) != 0) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "当前单据来源不是采购订单，不能分批入库，实际上料数量不能小于计划上料数量！");
        }

        SearchSmtLoadingDet searchSmtLoadingDet = new SearchSmtLoadingDet();
        searchSmtLoadingDet.setLoadingCode(smtLoadingDetDto.getLoadingCode());
        searchSmtLoadingDet.setMaterialId(smtLoadingDetDto.getMaterialId());
        searchSmtLoadingDet.setStatus((byte) 1);
        List<SmtLoadingDetDto> smtLoadingDetDtoList = electronicTagFeignApi.findLoadingDetList(searchSmtLoadingDet).getData();
        if (StringUtils.isEmpty(smtLoadingDetDtoList)) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "当前物料已上料灭灯，无需再次确认！");
        }

        SearchSmtElectronicTagStorage searchSmtElectronicTagStorage = new SearchSmtElectronicTagStorage();
        searchSmtElectronicTagStorage.setMaterialId(smtLoadingDetDto.getMaterialId().toString());
        List<SmtElectronicTagStorageDto> smtElectronicTagStorageDtoList = electronicTagFeignApi.findElectronicTagStorageList(searchSmtElectronicTagStorage).getData();

        if (StringUtils.isEmpty(smtElectronicTagStorageDtoList)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "请先维护储位对应的电子标签信息");
        }
        if (StringUtils.isEmpty(smtElectronicTagStorageDtoList.get(0).getStorageCode())) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到物料对应的储位信息");
        }
        BigDecimal qty = smtLoadingDetDtoList.get(0).getPlanQty().subtract(smtLoadingDetDtoList.get(0).getActualQty());
        smtElectronicTagStorageDtoList.get(0).setQuantity(qty.subtract(smtLoadingDetDto.getActualQty()));
        smtElectronicTagStorageDtoList.get(0).setActualQty(smtLoadingDetDto.getActualQty());
        smtElectronicTagStorageDtoList.get(0).setOrderType((byte) 2);

        // 发给客户端，控制灭灯
        fanoutSender(1003, smtElectronicTagStorageDtoList.get(0));

        return 1;
    }

    public void fanoutSender(Integer code, SmtElectronicTagStorageDto smtElectronicTagStorageDto) throws Exception{

        //不同的标签可能对应的队列不一样，最终一条一条发给客户端
        MQResponseEntity mQResponseEntity = new MQResponseEntity<>();
        mQResponseEntity.setCode(code);
        mQResponseEntity.setData(smtElectronicTagStorageDto);
        log.info("===========开始发送消息给客户端===============");
        //发送给PDA修改数据状态
        fanoutSender.send(RabbitConfig.TOPIC_QUEUE_PDA,
                JSONObject.toJSONString(mQResponseEntity));
        //发送给客户端控制亮/灭灯
        fanoutSender.send(smtElectronicTagStorageDto.getQueueName(),
                JSONObject.toJSONString(mQResponseEntity));
        log.info("===========队列名称:" + smtElectronicTagStorageDto.getQueueName());
        log.info("===========消息内容:" + JSONObject.toJSONString(mQResponseEntity));
        log.info("===========发送消息给客户端完成===============");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public List<SmtSortingDto> sendElectronicTagStorageTest(String sortingCode) throws Exception {

        List<SmtSortingDto> sortingDtoList = new LinkedList<>();
        SearchSmtSorting searchSmtSorting = new SearchSmtSorting();
        searchSmtSorting.setSortingCode(sortingCode);
        searchSmtSorting.setStatus((byte) 1);
        sortingDtoList = electronicTagFeignApi.findSortingList(searchSmtSorting).getData();
        SmtSorting smtSorting = new SmtSorting();
        BeanUtils.copyProperties(sortingDtoList.get(0), smtSorting);
        smtSorting.setStatus((byte) 2);
        smtSorting.setUpdateStatus((byte) 0);
        electronicTagFeignApi.updateSmtSorting(smtSorting);

        for (SmtSorting sorting : sortingDtoList) {
            SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
            searchBaseMaterial.setMaterialCode(sorting.getMaterialCode());
            List<BaseMaterial> baseMaterials = basicFeignApi.findSmtMaterialList(searchBaseMaterial).getData();
            if (StringUtils.isEmpty(baseMaterials)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到对应物料信息");
            }
            SearchSmtElectronicTagStorage searchSmtElectronicTagStorage = new SearchSmtElectronicTagStorage();
            searchSmtElectronicTagStorage.setMaterialId(baseMaterials.get(0).getMaterialId().toString());
            List<SmtElectronicTagStorageDto> smtElectronicTagStorageDtoList = electronicTagFeignApi.findElectronicTagStorageList(searchSmtElectronicTagStorage).getData();

            if (StringUtils.isEmpty(smtElectronicTagStorageDtoList)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "请先维护储位对应的电子标签信息");
            }
            if (StringUtils.isEmpty(smtElectronicTagStorageDtoList.get(0).getStorageCode())) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到物料对应的储位信息");
            }
            smtElectronicTagStorageDtoList.get(0).setQuantity(sorting.getQuantity());
            smtElectronicTagStorageDtoList.get(0).setOrderType((byte) 1);

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
                smtStorageInventory.setQuantity(sorting.getQuantity().negate());
                smtStorageInventory.setStatus((byte) 1);
                storageInventoryFeignApi.add(smtStorageInventory);
            }

            // 更新物料库存信息
            SmtStorageInventory smtStorageInventory = new SmtStorageInventory();
            smtStorageInventory.setStorageInventoryId(smtStorageInventoryDtoList.get(0).getStorageInventoryId());
            smtStorageInventory.setQuantity(smtStorageInventoryDtoList.get(0).getQuantity().subtract(sorting.getQuantity()));
            storageInventoryFeignApi.update(smtStorageInventory);

            //不同的标签可能对应的队列不一样，最终一条一条发给客户端
            fanoutSender(1003, smtElectronicTagStorageDtoList.get(0));

            //熄灭时，根据单号查询是否做完
            SearchSmtSorting searchSmtSorting1 = new SearchSmtSorting();
            searchSmtSorting1.setSortingCode(sorting.getSortingCode());
            searchSmtSorting1.setStatus((byte) 1);
            List<SmtSortingDto> findSortingList = electronicTagFeignApi.findSortingList(searchSmtSorting1).getData();
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
                ptlSortingDTO.setSortingCode(sorting.getSortingCode());
                ptlSortingDTO.setPtlSortingDetailDTOList(ptlSortingDetailDTOList);
                ptlSortingDTO.setUser(findSortingList.get(0).getModifiedUserCode());
                log.info("分拣单号处理完，回传给MES：" + ptlSortingDTO);
                String url = "";
                if ("MES".equals(findSortingList.get(0).getSourceSys())) {
                    url = resApiUrl;
                } else if ("QIS".equals(findSortingList.get(0).getSourceSys())) {
                    url = confirmOutBillOrderUrl;
                }
                String result = RestTemplateUtil.postJsonStrForString(ptlSortingDTO, url);
                log.info("MES返回信息：" + result);
            }
        }

        return sortingDtoList;
    }

    @Override
    public String sendElectronicTagStorageLightTest(String materialCode, Integer code) throws Exception {

        SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
        searchBaseMaterial.setMaterialCode(materialCode);
        List<BaseMaterial> baseMaterials = basicFeignApi.findSmtMaterialList(searchBaseMaterial).getData();
        if (StringUtils.isEmpty(baseMaterials)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到对应物料信息");
        }
        SearchSmtElectronicTagStorage searchSmtElectronicTagStorage = new SearchSmtElectronicTagStorage();
        searchSmtElectronicTagStorage.setMaterialId(baseMaterials.get(0).getMaterialId().toString());
        List<SmtElectronicTagStorageDto> smtElectronicTagStorageDtoList = electronicTagFeignApi.findElectronicTagStorageList(searchSmtElectronicTagStorage).getData();

        if (StringUtils.isEmpty(smtElectronicTagStorageDtoList)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "请先维护储位对应的电子标签信息");
        }
        if (StringUtils.isEmpty(smtElectronicTagStorageDtoList.get(0).getStorageCode())) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到物料对应的储位信息");
        }
        smtElectronicTagStorageDtoList.get(0).setQuantity(BigDecimal.valueOf(100));
        smtElectronicTagStorageDtoList.get(0).setOrderType((byte) 1);

        //不同的标签可能对应的队列不一样，最终一条一条发给客户端
        fanoutSender(code, smtElectronicTagStorageDtoList.get(0));

        return "0";
    }
}
