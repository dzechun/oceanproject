package com.fantechs.provider.client.server.impl;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.dto.PtlElectronicTagStorageDto;
import com.fantechs.common.base.electronic.dto.PtlLoadingDetDto;
import com.fantechs.common.base.electronic.dto.PtlSortingDto;
import com.fantechs.common.base.electronic.entity.PtlLoading;
import com.fantechs.common.base.electronic.entity.PtlLoadingDet;
import com.fantechs.common.base.electronic.entity.PtlSorting;
import com.fantechs.common.base.electronic.entity.search.SearchPtlElectronicTagStorage;
import com.fantechs.common.base.electronic.entity.search.SearchPtlLoading;
import com.fantechs.common.base.electronic.entity.search.SearchPtlLoadingDet;
import com.fantechs.common.base.electronic.entity.search.SearchPtlSorting;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.response.MQResponseEntity;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.RestTemplateUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.electronic.ElectronicTagFeignApi;
import com.fantechs.provider.client.config.RabbitConfig;
import com.fantechs.provider.client.dto.PtlLoadingDTO;
import com.fantechs.provider.client.dto.PtlLoadingDetDTO;
import com.fantechs.provider.client.dto.PtlSortingDTO;
import com.fantechs.provider.client.dto.PtlSortingDetailDTO;
import com.fantechs.provider.client.server.ElectronicTagStorageService;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

    @Value("${mesAPI.resApi}")
    private String resApiUrl;
    @Value("${qisAPI.confirmOutBillOrder}")
    private String confirmOutBillOrderUrl;
    @Value("${qisAPI.confirmInBillOrder}")
    private String confirmInBillOrderUrl;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public List<PtlSortingDto> sendElectronicTagStorage(String sortingCode, Long warehouseAreaId) throws Exception {

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<PtlSortingDto> sortingDtoList = new LinkedList<>();
        synchronized (ElectronicTagStorageServiceImpl.class) {
            //是否有在做单据
            SearchPtlSorting searchPtlSorting = new SearchPtlSorting();
            searchPtlSorting.setStatus((byte) 1);
            searchPtlSorting.setWarehouseAreaId(warehouseAreaId);
            searchPtlSorting.setNotEqualSortingCode(sortingCode);
            List<PtlSortingDto> smtSortingList = electronicTagFeignApi.findSortingList(searchPtlSorting).getData();
            if (StringUtils.isNotEmpty(smtSortingList)) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "正在处理其他任务单，请稍后再试");
            }
//            SearchPtlLoading searchPtlLoading = new SearchPtlLoading();
//            searchPtlLoading.setStatus((byte) 1);
//            List<PtlLoading> ptlLoadingList = electronicTagFeignApi.findLoadingList(searchPtlLoading).getData();
//            if (StringUtils.isNotEmpty(ptlLoadingList)) {
//                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "正在处理其他上料单，请稍后再试");
//            }

            searchPtlSorting = new SearchPtlSorting();
            searchPtlSorting.setSortingCode(sortingCode);
            searchPtlSorting.setNotEqualstatus((byte) 0);
            sortingDtoList = electronicTagFeignApi.findSortingList(searchPtlSorting).getData();
            if (StringUtils.isEmpty(sortingDtoList)) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "该任务单没有待激活的物料");
            }
        }

        List<PtlElectronicTagStorageDto> list = new LinkedList<>();
        List<PtlSorting> ptlSortingList = new LinkedList<>();
        for (PtlSorting sorting : sortingDtoList) {
            SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
            searchBaseMaterial.setMaterialCode(sorting.getMaterialCode());
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
            ptlElectronicTagStorageDtoList.get(0).setQuantity(sorting.getSortingQty());
            ptlElectronicTagStorageDtoList.get(0).setOrderType((byte) 1);
            list.add(ptlElectronicTagStorageDtoList.get(0));

            PtlSorting ptlSorting = new PtlSorting();
            ptlSorting.setSortingId(sorting.getSortingId());
            ptlSorting.setStatus((byte) 1);
            ptlSorting.setModifiedUserId(currentUser.getUserId());
            ptlSorting.setModifiedTime(new Date());
            ptlSortingList.add(ptlSorting);
        }

        for (PtlElectronicTagStorageDto ptlElectronicTagStorageDto : list) {

            //不同的标签可能对应的队列不一样，最终一条一条发给客户端
            fanoutSender(1001, ptlElectronicTagStorageDto);
            if (StringUtils.isNotEmpty(ptlElectronicTagStorageDto.getEquipmentAreaId())) {
                fanoutSender(1002, ptlElectronicTagStorageDto);
            }
        }

        if (StringUtils.isNotEmpty(ptlSortingList)) {
            electronicTagFeignApi.batchUpdateSorting(ptlSortingList).getCount();
        }

        return sortingDtoList;
    }

    @Override
    public int batchSortingDelete(List<String> sortingCodes) throws Exception {
        electronicTagFeignApi.batchDeleteSorting(sortingCodes);
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int createSorting(List<PtlSortingDTO> ptlSortingDTOList) throws Exception {

        List<PtlSorting> list = new LinkedList<>();
        for (PtlSortingDTO sortingDTO : ptlSortingDTOList) {
            SearchPtlSorting searchPtlSorting = new SearchPtlSorting();
            searchPtlSorting.setSortingCode(sortingDTO.getTaskNo());
            List<PtlSortingDto> smtSortingList = electronicTagFeignApi.findSortingList(searchPtlSorting).getData();
            if (StringUtils.isNotEmpty(smtSortingList)) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "重复分拣单");
            }
            for (PtlSortingDetailDTO ptlSortingDetailDTO : sortingDTO.getDetails()) {
                SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
                searchBaseMaterial.setMaterialCode(ptlSortingDetailDTO.getGoodsCode());
                List<BaseMaterial> baseMaterials = baseFeignApi.findSmtMaterialList(searchBaseMaterial).getData();
                if (StringUtils.isEmpty(baseMaterials)) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到对应物料信息");
                }
                SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
                searchBaseStorage.setStorageCode(ptlSortingDetailDTO.getLocationCode());
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
                if (!sortingDTO.getWarehouseCode().equals(ptlElectronicTagStorageDtoList.get(0).getWarehouseCode())) {
                    throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "任务号：" + sortingDTO.getTaskNo() + "对应明细储位："
                            + ptlSortingDetailDTO.getLocationCode() + "没有找到与仓库：" + sortingDTO.getWarehouseCode() + "的关联关系");
                }

                PtlSorting ptlSorting = new PtlSorting();
                ptlSorting.setSortingCode(sortingDTO.getTaskNo());
                ptlSorting.setRelatedOrderCode(sortingDTO.getCustomerNo());
                ptlSorting.setMaterialId(baseMaterials.get(0).getMaterialId());
                ptlSorting.setMaterialCode(baseMaterials.get(0).getMaterialCode());
                ptlSorting.setMaterialName(baseMaterials.get(0).getMaterialName());
                ptlSorting.setWarehouseId(Long.valueOf(ptlElectronicTagStorageDtoList.get(0).getWarehouseId()));
                ptlSorting.setWarehouseName(ptlElectronicTagStorageDtoList.get(0).getWarehouseName());
                ptlSorting.setStorageId(Long.valueOf(ptlElectronicTagStorageDtoList.get(0).getStorageId()));
                ptlSorting.setStorageCode(ptlSortingDetailDTO.getLocationCode());
                ptlSorting.setSortingQty(BigDecimal.valueOf(ptlSortingDetailDTO.getQty()));
                ptlSorting.setPackingUnitName(ptlSortingDetailDTO.getUnit());
                ptlSorting.setWholeOrScattered(Byte.valueOf(sortingDTO.getIsWhole()));
                ptlSorting.setIfAlreadyPrint((byte) 0);
                ptlSorting.setStatus((byte) 0);
                ptlSorting.setCreateTime(new Date());
                ptlSorting.setModifiedTime(new Date());
                ptlSorting.setIsDetele((byte) 1);
                list.add(ptlSorting);
            }
        }
        if (StringUtils.isNotEmpty(list)) {
            electronicTagFeignApi.batchInsertSmtSorting(list).getCount();
        }

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int createLoading(List<PtlLoading> ptlLoadingList) throws Exception {

        for (PtlLoading ptlLoading : ptlLoadingList) {

            SearchPtlLoading searchPtlLoading = new SearchPtlLoading();
            searchPtlLoading.setLoadingCode(ptlLoading.getLoadingCode());
            List<PtlLoading> ptlLoadings = electronicTagFeignApi.findLoadingList(searchPtlLoading).getData();
            if (StringUtils.isNotEmpty(ptlLoadings) && ptlLoading.getOrderType() != 1) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "重复入库单");
            }
            if (StringUtils.isNotEmpty(ptlLoadings) && ptlLoadings.get(0).getStatus() == 1) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "该上料单正在进行上料操作，请稍后再进行同步！");
            }
            if (StringUtils.isNotEmpty(ptlLoadings) && ptlLoadings.get(0).getStatus() != 0 && ptlLoading.getOrderType() != 1) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "该上料单已进行上料操作");
            }
            if (ptlLoading.getOrderType() == 1 && StringUtils.isNotEmpty(ptlLoadings)) {
                // 查询本次传入采购订单物料明细比较上次是否有删减
                SearchPtlLoadingDet searchPtlLoadingDet = new SearchPtlLoadingDet();
                searchPtlLoadingDet.setLoadingCode(ptlLoading.getLoadingCode());
                List<PtlLoadingDetDto> ptlLoadingDetDtoList = electronicTagFeignApi.findLoadingDetList(searchPtlLoadingDet).getData();
                for (PtlLoadingDetDto ptlLoadingDetDto : ptlLoadingDetDtoList) {
                    Boolean deleteBoolean = true;
                    for (PtlLoadingDetDto ptlLoadingDetDto2 : ptlLoading.getPtlLoadingDetDtoList()) {
                        if (ptlLoadingDetDto.getMaterialCode().equals(ptlLoadingDetDto2.getMaterialCode())) {
                            deleteBoolean = false;
                            break;
                        }
                    }
                    if (deleteBoolean) {
                        PtlLoadingDet ptlLoadingDet = new PtlLoadingDet();
                        ptlLoadingDet.setLoadingDetId(ptlLoadingDetDto.getLoadingDetId());
                        ptlLoadingDet.setIsDetele((byte) 0);
                        electronicTagFeignApi.updateLoadingDet(ptlLoadingDet);
                    }
                }
            }
            if (StringUtils.isEmpty(ptlLoadings)) {
                ptlLoading = electronicTagFeignApi.addSmtLoading(ptlLoading).getData();
            } else {
                ptlLoading.setLoadingId(ptlLoadings.get(0).getLoadingId());
            }

            for (PtlLoadingDetDto ptlLoadingDetDto : ptlLoading.getPtlLoadingDetDtoList()) {

                SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
                searchBaseMaterial.setMaterialCode(ptlLoadingDetDto.getMaterialCode());
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

                List<PtlLoadingDetDto> ptlLoadingDetDtoList = new LinkedList<>();
                if (ptlLoading.getOrderType() == 1) {
                    SearchPtlLoadingDet searchPtlLoadingDet = new SearchPtlLoadingDet();
                    searchPtlLoadingDet.setLoadingId(ptlLoading.getLoadingId());
                    searchPtlLoadingDet.setMaterialCode(ptlLoadingDetDto.getMaterialCode());
                    ptlLoadingDetDtoList = electronicTagFeignApi.findLoadingDetList(searchPtlLoadingDet).getData();
                }

                PtlLoadingDet ptlLoadingDet = new PtlLoadingDet();
                ptlLoadingDet.setLoadingId(ptlLoading.getLoadingId());
                ptlLoadingDet.setMaterialId(baseMaterials.get(0).getMaterialId());
                ptlLoadingDet.setPlanQty(ptlLoadingDetDto.getPlanQty());
                if (StringUtils.isEmpty(ptlLoadingDetDtoList)) {
                    electronicTagFeignApi.addSmtLoadingDet(ptlLoadingDet);
                } else {
                    ptlLoadingDet.setLoadingDetId(ptlLoadingDetDtoList.get(0).getLoadingDetId());
                    if (ptlLoadingDetDtoList.get(0).getStatus() == 3 && ptlLoadingDetDto.getPlanQty().compareTo(ptlLoadingDetDtoList.get(0).getActualQty()) == 1) {
                        ptlLoadingDet.setStatus((byte) 2);
                    }
                    electronicTagFeignApi.updateLoadingDet(ptlLoadingDet);
                }
            }
        }

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public List<PtlLoadingDetDto> sendLoadingElectronicTagStorage(String loadingCode) throws Exception {

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        // 判断是否有其他上料单正在进行中
        List<PtlLoading> ptlLoadingList = new LinkedList<>();
        List<PtlLoadingDetDto> ptlLoadingDetDtoList = new LinkedList<>();
        synchronized (ElectronicTagStorageServiceImpl.class) {
            SearchPtlLoading searchPtlLoading = new SearchPtlLoading();
            searchPtlLoading.setStatus((byte) 1);
            ptlLoadingList = electronicTagFeignApi.findLoadingList(searchPtlLoading).getData();
            if (StringUtils.isNotEmpty(ptlLoadingList) && (ptlLoadingList.size() > 1 || !loadingCode.equals(ptlLoadingList.get(0).getLoadingCode()))) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "正在处理其他上料单，请稍后再试");
            }
            if (StringUtils.isNotEmpty(ptlLoadingList) && ptlLoadingList.get(0).getStatus() == 3) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "该上料单已经完成！");
            }
            SearchPtlSorting searchPtlSorting = new SearchPtlSorting();
            searchPtlSorting.setStatus((byte) 1);
            List<PtlSortingDto> smtSortingList = electronicTagFeignApi.findSortingList(searchPtlSorting).getData();
            if (StringUtils.isNotEmpty(smtSortingList)) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "正在处理其他分拣单，请稍后再试");
            }

            SearchPtlLoadingDet searchPtlLoadingDet = new SearchPtlLoadingDet();
            searchPtlLoadingDet.setLoadingCode(loadingCode);
            searchPtlLoadingDet.setNotEqualstatus((byte) 3);
            ptlLoadingDetDtoList = electronicTagFeignApi.findLoadingDetList(searchPtlLoadingDet).getData();
            if (StringUtils.isEmpty(ptlLoadingDetDtoList)) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "该上料单没有可上料的物料");
            }
        }

        PtlLoading ptlLoading = new PtlLoading();
        ptlLoading.setLoadingId(ptlLoadingDetDtoList.get(0).getLoadingId());
        ptlLoading.setStatus((byte) 1);
        ptlLoading.setModifiedUserId(currentUser.getUserId());
        electronicTagFeignApi.updateLoading(ptlLoading);

        for (PtlLoadingDetDto ptlLoadingDetDto : ptlLoadingDetDtoList) {
            SearchPtlElectronicTagStorage searchPtlElectronicTagStorage = new SearchPtlElectronicTagStorage();
            searchPtlElectronicTagStorage.setMaterialId(ptlLoadingDetDto.getMaterialId().toString());
            List<PtlElectronicTagStorageDto> ptlElectronicTagStorageDtoList = electronicTagFeignApi.findElectronicTagStorageList(searchPtlElectronicTagStorage).getData();

            if (StringUtils.isEmpty(ptlElectronicTagStorageDtoList)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "请先维护储位对应的电子标签信息");
            }
            if (StringUtils.isEmpty(ptlElectronicTagStorageDtoList.get(0).getStorageCode())) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到物料对应的储位信息");
            }
            ptlElectronicTagStorageDtoList.get(0).setQuantity(ptlLoadingDetDto.getPlanQty().subtract(ptlLoadingDetDto.getActualQty()));
            ptlElectronicTagStorageDtoList.get(0).setOrderType((byte) 2);

            PtlLoadingDet ptlLoadingDet = new PtlLoadingDet();
            ptlLoadingDet.setLoadingDetId(ptlLoadingDetDto.getLoadingDetId());
            ptlLoadingDet.setStatus((byte) 1);
            ptlLoadingDet.setModifiedUserId(currentUser.getUserId());
            electronicTagFeignApi.updateLoadingDet(ptlLoadingDet);

            // 不同的标签可能对应的队列不一样，最终一条一条发给客户端，控制biao亮灯
            fanoutSender(1001, ptlElectronicTagStorageDtoList.get(0));
            // 控制区域灯亮灯
            if (StringUtils.isNotEmpty(ptlElectronicTagStorageDtoList.get(0).getEquipmentAreaId())) {
                fanoutSender(1002, ptlElectronicTagStorageDtoList.get(0));
            }
        }

        return ptlLoadingDetDtoList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int submitLoadingDet(List<PtlLoadingDetDto> ptlLoadingDetDtoList) throws Exception {

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        SearchPtlLoading searchPtlLoading = new SearchPtlLoading();
        searchPtlLoading.setLoadingCode(ptlLoadingDetDtoList.get(0).getLoadingCode());
        List<PtlLoading> ptlLoadingList = electronicTagFeignApi.findLoadingList(searchPtlLoading).getData();
        if (StringUtils.isEmpty(ptlLoadingList)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到该上料单！");
        } else if (ptlLoadingList.get(0).getStatus() == 0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "该上料单物料明细电子标签尚未亮灯，请先进行亮灯！");
        } else if (ptlLoadingList.get(0).getStatus() == 3){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "该上料单已完成！");
        }

        // 回传MES/QIS上料单
        PtlLoadingDTO ptlLoadingDTO = new PtlLoadingDTO();
        ptlLoadingDTO.setLoadingCode(ptlLoadingDetDtoList.get(0).getLoadingCode());
        if (StringUtils.isNotEmpty(currentUser)) {
            ptlLoadingDTO.setUser(currentUser.getUserCode());
        }

        List<PtlLoadingDetDTO> ptlLoadingDetDTOList = new LinkedList<>();
        PtlLoading ptlLoading = new PtlLoading();
        ptlLoading.setLoadingId(ptlLoadingList.get(0).getLoadingId());
        Byte status = 3;
        for (PtlLoadingDetDto ptlLoadingDetDto : ptlLoadingDetDtoList) {

            PtlLoadingDet ptlLoadingDet = electronicTagFeignApi.detailSmtLoadingDet(ptlLoadingDetDto.getLoadingDetId()).getData();
            BigDecimal qty = ptlLoadingDet.getPlanQty().subtract(ptlLoadingDet.getActualQty());
            System.out.println("qty：" + qty + "====================================");
            System.out.println("smtLoadingDetDto.getActualQty()：" + ptlLoadingDetDto.getActualQty() + "==============================");
            System.out.println(qty.compareTo(ptlLoadingDetDto.getActualQty()) + "===========================================");
            if (qty.compareTo(ptlLoadingDetDto.getActualQty()) < 0) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "实际上料数量不能超过计划上料数量！");
            }
            if (qty.compareTo(ptlLoadingDetDto.getActualQty()) != 0) {
                if (ptlLoadingList.get(0).getOrderType() != 1) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "当前单据来源不是采购订单，不能分批入库，实际上料数量不能小于计划上料数量！");
                }
                status = 2;
                ptlLoadingDet.setStatus((byte) 2);
            } else {
                ptlLoadingDet.setStatus((byte) 3);
            }
            ptlLoadingDet.setActualQty(ptlLoadingDet.getActualQty().add(ptlLoadingDetDto.getActualQty()));
            electronicTagFeignApi.updateLoadingDet(ptlLoadingDet);

            SearchPtlElectronicTagStorage searchPtlElectronicTagStorage = new SearchPtlElectronicTagStorage();
            searchPtlElectronicTagStorage.setMaterialId(ptlLoadingDetDto.getMaterialId().toString());
            List<PtlElectronicTagStorageDto> ptlElectronicTagStorageDtoList = electronicTagFeignApi.findElectronicTagStorageList(searchPtlElectronicTagStorage).getData();

            if (StringUtils.isEmpty(ptlElectronicTagStorageDtoList)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "请先维护储位对应的电子标签信息");
            }
            if (StringUtils.isEmpty(ptlElectronicTagStorageDtoList.get(0).getStorageCode())) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到物料对应的储位信息");
            }
            ptlElectronicTagStorageDtoList.get(0).setQuantity(ptlLoadingDetDto.getPlanQty().subtract(ptlLoadingDetDto.getActualQty()));
            ptlElectronicTagStorageDtoList.get(0).setOrderType((byte) 2);

            // 发给客户端，控制灭灯
            fanoutSender(1003, ptlElectronicTagStorageDtoList.get(0));
            // 控制区域灯亮灯
            if (StringUtils.isNotEmpty(ptlElectronicTagStorageDtoList.get(0).getEquipmentAreaId())) {
                fanoutSender(1004, ptlElectronicTagStorageDtoList.get(0));
            }

            if (BigDecimal.ZERO.compareTo(ptlLoadingDetDto.getActualQty()) != 0) {
                PtlLoadingDetDTO ptlLoadingDetDTO = new PtlLoadingDetDTO();
                ptlLoadingDetDTO.setMaterialCode(ptlLoadingDetDto.getMaterialCode());
                ptlLoadingDetDTO.setActualQty(ptlLoadingDetDto.getActualQty());
                ptlLoadingDetDTO.setStorageCode(ptlElectronicTagStorageDtoList.get(0).getStorageCode());
                ptlLoadingDetDTOList.add(ptlLoadingDetDTO);
            }

            ptlLoadingDTO.setWarehouseCode(ptlElectronicTagStorageDtoList.get(0).getWarehouseCode());
        }

        ptlLoading.setStatus(status);
        electronicTagFeignApi.updateLoading(ptlLoading);
        ptlLoading = electronicTagFeignApi.detailSmtLoading(ptlLoading.getLoadingId()).getData();

        ptlLoadingDTO.setPtlLoadingDetDTOList(ptlLoadingDetDTOList);

        log.info("上料单号处理完，回传给MES：" + JSONObject.toJSONString(ptlLoadingDTO));
        String url = "";
        if ("MES".equals(ptlLoading.getSourceSys())) {
            url = "";
        } else if ("QIS".equals(ptlLoading.getSourceSys())) {
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

        SearchPtlLoadingDet searchPtlLoadingDet = new SearchPtlLoadingDet();
        searchPtlLoadingDet.setLoadingCode(loadingCode);
        searchPtlLoadingDet.setStatus((byte) 1);
        List<PtlLoadingDetDto> ptlLoadingDetDtoList = electronicTagFeignApi.findLoadingDetList(searchPtlLoadingDet).getData();
        if (StringUtils.isEmpty(ptlLoadingDetDtoList)) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "当前上料单不存在未上料的物料，无需撤销！");
        }

        PtlLoading ptlLoading = new PtlLoading();
        ptlLoading.setLoadingId(ptlLoadingDetDtoList.get(0).getLoadingId());
        ptlLoading.setStatus((byte) 2);
        electronicTagFeignApi.updateLoading(ptlLoading);

        for (PtlLoadingDetDto ptlLoadingDetDto : ptlLoadingDetDtoList) {
            SearchPtlElectronicTagStorage searchPtlElectronicTagStorage = new SearchPtlElectronicTagStorage();
            searchPtlElectronicTagStorage.setMaterialId(ptlLoadingDetDto.getMaterialId().toString());
            List<PtlElectronicTagStorageDto> ptlElectronicTagStorageDtoList = electronicTagFeignApi.findElectronicTagStorageList(searchPtlElectronicTagStorage).getData();

            if (StringUtils.isEmpty(ptlElectronicTagStorageDtoList)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "请先维护储位对应的电子标签信息");
            }
            if (StringUtils.isEmpty(ptlElectronicTagStorageDtoList.get(0).getStorageCode())) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到物料对应的储位信息");
            }
            ptlElectronicTagStorageDtoList.get(0).setQuantity(ptlLoadingDetDto.getPlanQty().subtract(ptlLoadingDetDto.getActualQty()));
            ptlElectronicTagStorageDtoList.get(0).setOrderType((byte) 2);

            PtlLoadingDet ptlLoadingDet = new PtlLoadingDet();
            ptlLoadingDet.setLoadingDetId(ptlLoadingDetDto.getLoadingDetId());
            ptlLoadingDet.setStatus((byte) 2);
            electronicTagFeignApi.updateLoadingDet(ptlLoadingDet);

            // 不同的标签可能对应的队列不一样，最终一条一条发给客户端，控制灭灯
            fanoutSender(1003, ptlElectronicTagStorageDtoList.get(0));
            // 控制区域灯亮灯
            if (StringUtils.isNotEmpty(ptlElectronicTagStorageDtoList.get(0).getEquipmentAreaId())) {
                fanoutSender(1004, ptlElectronicTagStorageDtoList.get(0));
            }
        }

        return 1;
    }

    @Override
    public int comfirmLoadingDet(PtlLoadingDetDto ptlLoadingDetDto) throws Exception {

        SearchPtlLoading searchPtlLoading = new SearchPtlLoading();
        searchPtlLoading.setLoadingCode(ptlLoadingDetDto.getLoadingCode());
        List<PtlLoading> ptlLoadingList = electronicTagFeignApi.findLoadingList(searchPtlLoading).getData();
        if (ptlLoadingList.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "没有找到对应的上料单！");
        }
        if (ptlLoadingList.get(0).getOrderType() == 2 && ptlLoadingDetDto.getPlanQty().compareTo(ptlLoadingDetDto.getActualQty()) != 0) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "当前单据来源不是采购订单，不能分批入库，实际上料数量不能小于计划上料数量！");
        }

        SearchPtlLoadingDet searchPtlLoadingDet = new SearchPtlLoadingDet();
        searchPtlLoadingDet.setLoadingCode(ptlLoadingDetDto.getLoadingCode());
        searchPtlLoadingDet.setMaterialId(ptlLoadingDetDto.getMaterialId());
        searchPtlLoadingDet.setStatus((byte) 1);
        List<PtlLoadingDetDto> ptlLoadingDetDtoList = electronicTagFeignApi.findLoadingDetList(searchPtlLoadingDet).getData();
        if (StringUtils.isEmpty(ptlLoadingDetDtoList)) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "当前物料已上料灭灯，无需再次确认！");
        }

        SearchPtlElectronicTagStorage searchPtlElectronicTagStorage = new SearchPtlElectronicTagStorage();
        searchPtlElectronicTagStorage.setMaterialId(ptlLoadingDetDto.getMaterialId().toString());
        List<PtlElectronicTagStorageDto> ptlElectronicTagStorageDtoList = electronicTagFeignApi.findElectronicTagStorageList(searchPtlElectronicTagStorage).getData();

        if (StringUtils.isEmpty(ptlElectronicTagStorageDtoList)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "请先维护储位对应的电子标签信息");
        }
        if (StringUtils.isEmpty(ptlElectronicTagStorageDtoList.get(0).getStorageCode())) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到物料对应的储位信息");
        }
        BigDecimal qty = ptlLoadingDetDtoList.get(0).getPlanQty().subtract(ptlLoadingDetDtoList.get(0).getActualQty());
        ptlElectronicTagStorageDtoList.get(0).setQuantity(qty.subtract(ptlLoadingDetDto.getActualQty()));
        ptlElectronicTagStorageDtoList.get(0).setActualQty(ptlLoadingDetDto.getActualQty());
        ptlElectronicTagStorageDtoList.get(0).setOrderType((byte) 2);

        // 发给客户端，控制灭灯
        fanoutSender(1003, ptlElectronicTagStorageDtoList.get(0));

        return 1;
    }

    public void fanoutSender(Integer code, PtlElectronicTagStorageDto ptlElectronicTagStorageDto) throws Exception{

        //不同的标签可能对应的队列不一样，最终一条一条发给客户端
        MQResponseEntity mQResponseEntity = new MQResponseEntity<>();
        mQResponseEntity.setCode(code);
        mQResponseEntity.setData(ptlElectronicTagStorageDto);
        log.info("===========开始发送消息给客户端===============");
        //发送给PDA修改数据状态
        fanoutSender.send(RabbitConfig.TOPIC_QUEUE_PDA,
                JSONObject.toJSONString(mQResponseEntity));
        //发送给客户端控制亮/灭灯
        fanoutSender.send(ptlElectronicTagStorageDto.getQueueName(),
                JSONObject.toJSONString(mQResponseEntity));
        log.info("===========队列名称:" + ptlElectronicTagStorageDto.getQueueName());
        log.info("===========消息内容:" + JSONObject.toJSONString(mQResponseEntity));
        log.info("===========发送消息给客户端完成===============");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public List<PtlSortingDto> sendElectronicTagStorageTest(String sortingCode) throws Exception {

        List<PtlSortingDto> sortingDtoList = new LinkedList<>();
        SearchPtlSorting searchPtlSorting = new SearchPtlSorting();
        searchPtlSorting.setSortingCode(sortingCode);
        searchPtlSorting.setStatus((byte) 1);
        sortingDtoList = electronicTagFeignApi.findSortingList(searchPtlSorting).getData();
        PtlSorting ptlSorting = new PtlSorting();
        BeanUtils.copyProperties(sortingDtoList.get(0), ptlSorting);
        ptlSorting.setStatus((byte) 2);
        ptlSorting.setUpdateStatus((byte) 0);
        electronicTagFeignApi.updateSmtSorting(ptlSorting);

        for (PtlSorting sorting : sortingDtoList) {
            SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
            searchBaseMaterial.setMaterialCode(sorting.getMaterialCode());
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
            ptlElectronicTagStorageDtoList.get(0).setQuantity(sorting.getSortingQty());
            ptlElectronicTagStorageDtoList.get(0).setOrderType((byte) 1);

            //不同的标签可能对应的队列不一样，最终一条一条发给客户端
            fanoutSender(1003, ptlElectronicTagStorageDtoList.get(0));

            //熄灭时，根据单号查询是否做完
            SearchPtlSorting searchPtlSorting1 = new SearchPtlSorting();
            searchPtlSorting1.setSortingCode(sorting.getSortingCode());
            searchPtlSorting1.setStatus((byte) 1);
            List<PtlSortingDto> findSortingList = electronicTagFeignApi.findSortingList(searchPtlSorting1).getData();
            //分拣单号处理完，回传给MES
            if (StringUtils.isEmpty(findSortingList)) {
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
                    ptlSortingDetailDTOList.add(ptlSortingDetailDTO);
                }
                ptlSortingDTO.setTaskNo(sorting.getSortingCode());
                ptlSortingDTO.setDetails(ptlSortingDetailDTOList);
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
        ptlElectronicTagStorageDtoList.get(0).setQuantity(BigDecimal.valueOf(100));
        ptlElectronicTagStorageDtoList.get(0).setOrderType((byte) 1);

        //不同的标签可能对应的队列不一样，最终一条一条发给客户端
        fanoutSender(code, ptlElectronicTagStorageDtoList.get(0));

        return "0";
    }
}
