package com.fantechs.provider.wms.inner.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseMaterialOwnerDto;
import com.fantechs.common.base.general.dto.basic.BaseWorkerDto;
import com.fantechs.common.base.general.dto.wms.inner.*;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterialOwner;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWorker;
import com.fantechs.common.base.general.entity.wms.inner.*;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrderDet;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.guest.eng.EngFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerJobOrderDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerJobOrderMapper;
import com.fantechs.provider.wms.inner.service.*;
import com.fantechs.provider.wms.inner.util.InventoryLogUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WmsInnerShiftWorkServiceImpl implements WmsInnerShiftWorkService {

    @Resource
    WmsInnerJobOrderService wmsInnerJobOrderService;

    @Resource
    WmsInnerHtJobOrderService wmsInnerHtJobOrderService;

    @Resource
    WmsInnerJobOrderMapper wmsInnerJobOrderMapper;

    @Resource
    WmsInnerJobOrderDetService wmsInnerJobOrderDetService;

    @Resource
    WmsInnerHtJobOrderDetService wmsInnerHtJobOrderDetService;

    @Resource
    WmsInnerJobOrderDetMapper wmsInnerJobOrderDetMapper;

    @Resource
    WmsInnerJobOrderDetBarcodeService wmsInnerJobOrderDetBarcodeService;

    @Resource
    WmsInnerHtJobOrderDetBarcodeService wmsInnerHtJobOrderDetBarcodeService;

    @Resource
    WmsInnerInventoryDetService wmsInnerInventoryDetService;

    @Resource
    WmsInnerInventoryService wmsInnerInventoryService;

    @Resource
    WmsInnerInventoryMapper wmsInnerInventoryMapper;

    @Resource
    BaseFeignApi baseFeignApi;

    @Resource
    private SecurityFeignApi securityFeignApi;

    @Resource
    private EngFeignApi engFeignApi;

    @Override
    public List<WmsInnerJobOrderDto> pdaFindList(Map<String, Object> map) {
        SysUser sysUser = currentUser();
        map.put("orgId", sysUser.getOrganizationId());
        map.put("jobOrderType", (byte) 3);
        return wmsInnerJobOrderService.findShiftList(map);
    }

    @Override
    public List<WmsInnerJobOrderDetDto> pdaFindDetList(SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet) {
        searchWmsInnerJobOrderDet.setNonShiftStorageStatus((byte) 4);
        return wmsInnerJobOrderDetService.findList(searchWmsInnerJobOrderDet);
    }

    @Override
    public CheckShiftWorkBarcodeRecordDto checkShiftWorkBarcode(CheckShiftWorkBarcodeDto dto) {
        if (StringUtils.isEmpty(dto.getBarcode())) {
            throw new BizErrorException(ErrorCodeEnum.PDA5001001);
        }
        if (StringUtils.isEmpty(dto.getStorageId())) {
            throw new BizErrorException(ErrorCodeEnum.PDA5001003);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("storageId", dto.getStorageId());
        map.put("barcode", dto.getBarcode());
        Long materialId = 0L;
        BigDecimal materialQty = BigDecimal.ZERO;
        List<WmsInnerInventoryDetDto> inventoryDetDtos = wmsInnerInventoryDetService.findList(map);



        if (inventoryDetDtos == null || inventoryDetDtos.size() <= 0) {
            SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
            searchBaseMaterial.setMaterialCode(dto.getBarcode());
            List<BaseMaterial> baseMaterials = baseFeignApi.findList(searchBaseMaterial).getData();
            if (baseMaterials.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.PDA5001004);
            }
            BaseMaterial material = baseMaterials.get(0);
            materialId = material.getMaterialId();
        } else {
            WmsInnerInventoryDetDto inventoryDetDto = inventoryDetDtos.get(0);
            if (dto.getWarehouseId() != null && !inventoryDetDto.getWarehouseId().equals(dto.getWarehouseId())) {
                throw new BizErrorException(ErrorCodeEnum.PDA5001005);
            }
            if (!inventoryDetDto.getStorageId().equals(dto.getStorageId())) {
                throw new BizErrorException(ErrorCodeEnum.PDA5001000);
            }
            BaseStorage baseStorage = baseFeignApi.detail(inventoryDetDto.getStorageId()).getData();
            if (baseStorage == null) {
                throw new BizErrorException(ErrorCodeEnum.PDA5001007);
            }
            if (baseStorage.getStorageType() != (byte) 1) {
                throw new BizErrorException(ErrorCodeEnum.PDA5001008);
            }
            materialId = inventoryDetDto.getMaterialId();
            materialQty = inventoryDetDto.getMaterialQty();
        }

        // 查询库存信息，同一库位跟同物料有且只有一条数据
        map.clear();
        map.put("materialId", materialId);
        map.put("storageId", dto.getStorageId());
        if(dto.getJobOrderDetId() == null){
            map.put("jobStatus", (byte) 1);
        }else {
            map.put("jobStatus", (byte) 2);
            map.put("jobOrderDetId", dto.getJobOrderDetId());
        }
        map.put("lockStatus", (byte) 0);
        map.put("stockLock", (byte) 0);
        map.put("qcLock", (byte) 0);
        List<WmsInnerInventoryDto> innerInventoryDtos = wmsInnerInventoryService.findList(map);
        if (innerInventoryDtos == null || innerInventoryDtos.size() <= 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA5001009);
        }
        WmsInnerInventoryDto innerInventoryDto = innerInventoryDtos.get(0);
        CheckShiftWorkBarcodeRecordDto recordDto = new CheckShiftWorkBarcodeRecordDto();
        recordDto.setWarehouseName(innerInventoryDto.getWarehouseName());
        recordDto.setMaterialCode(innerInventoryDto.getMaterialCode());
        recordDto.setMaterialName(innerInventoryDto.getMaterialName());
        recordDto.setPackingUnitName(innerInventoryDto.getPackingUnitName());
        recordDto.setPackageSpecificationQuantity(innerInventoryDto.getPackageSpecificationQuantity());
        recordDto.setMaterialQty(materialQty);
        recordDto.setWarehouseId(innerInventoryDto.getWarehouseId());
        recordDto.setStorageCode(innerInventoryDto.getStorageCode());
        recordDto.setStorageId(innerInventoryDto.getStorageId());
        recordDto.setMaterialId(innerInventoryDto.getMaterialId());
        recordDto.setPackingQty(innerInventoryDto.getPackingQty());
        if (dto.getJobOrderDetId() != null) {
            WmsInnerJobOrderDet jobOrderDet = wmsInnerJobOrderDetService.selectByKey(dto.getJobOrderDetId());
            recordDto.setPlanQty(jobOrderDet.getPlanQty());
        }

        return recordDto;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public String saveShiftWorkDetBarcode(SaveShiftWorkDetDto dto) {
        SysUser sysUser = currentUser();

        if (dto.getBarcodes() == null && dto.getBarcodes().size() <= 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA5001006);
        }
        SearchBaseWorker searchBaseWorker = new SearchBaseWorker();
        searchBaseWorker.setWarehouseId(dto.getWarehouseId());
        searchBaseWorker.setUserId(sysUser.getUserId());
        // 判断是否有作业单
        if (dto.getJobOrderDetId() != null) {
            // 移位作业明细单 变更移位状态
            WmsInnerJobOrderDet jobOrderDet = wmsInnerJobOrderDetService.selectByKey(dto.getJobOrderDetId());
            //jobOrderDet.setOrderStatus((byte) 4);
            jobOrderDet.setActualQty(jobOrderDet.getActualQty() != null ? jobOrderDet.getActualQty().add(dto.getMaterialQty()) : dto.getMaterialQty());
            Byte status = 2;
            if (jobOrderDet.getActualQty().compareTo(jobOrderDet.getDistributionQty()) == 0) {
                status = (byte) 3;
            }
            //jobOrderDet.setShiftStorageStatus(status);
            wmsInnerJobOrderDetService.update(jobOrderDet);

            // 作业单拣货数量+1以及变更单据状态
            WmsInnerJobOrder innerJobOrder = wmsInnerJobOrderService.selectByKey(dto.getJobOrderId());
            innerJobOrder.setOrderStatus((byte) 4);
            //innerJobOrder.setActualQty(innerJobOrder.getActualQty() != null ? innerJobOrder.getActualQty().add(dto.getMaterialQty()) : dto.getMaterialQty());
            wmsInnerJobOrderMapper.updateByPrimaryKey(innerJobOrder);
        } else {
            // 查询库存信息，同一库位跟同物料有且只有一条数据
            Map<String, Object> map = new HashMap<>();
            map.put("materialId", dto.getMaterialId());
            map.put("storageId", dto.getStorageId());
            map.put("jobStatus", (byte) 1);
            map.put("lockStatus", (byte) 0);
            map.put("stockLock", (byte) 0);
            map.put("qcLock", (byte) 0);
            List<WmsInnerInventoryDto> innerInventoryDtos = wmsInnerInventoryService.findList(map);
            if (innerInventoryDtos == null || innerInventoryDtos.size() <= 0) {
                throw new BizErrorException(ErrorCodeEnum.PDA5001009);
            }
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("inventory_status_value");
            List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            List<WmsInnerInventoryDto> dtos = innerInventoryDtos.stream().filter(item -> item.getInventoryStatusName().equals(specItems.get(0).getParaValue())).collect(Collectors.toList());
            WmsInnerInventoryDto innerInventoryDto = dtos.get(0);
            if (innerInventoryDto.getPackingQty().compareTo(dto.getMaterialQty()) < -1) {
                throw new BizErrorException(ErrorCodeEnum.PDA5001012);
            }

            WmsInnerJobOrder innerJobOrder = new WmsInnerJobOrder();
            if (dto.getJobOrderId() != null) {
                // 更新移位单
                innerJobOrder = wmsInnerJobOrderService.selectByKey(dto.getJobOrderId());
                innerJobOrder.setOrderStatus((byte) 4);

                wmsInnerJobOrderMapper.updateByPrimaryKey(innerJobOrder);
            } else {
                // 创建移位单
                SearchBaseMaterialOwner searchBaseMaterialOwner = new SearchBaseMaterialOwner();
                searchBaseMaterialOwner.setAsc((byte) 1);
                List<BaseMaterialOwnerDto> ownerDtos = baseFeignApi.findList(searchBaseMaterialOwner).getData();
                if (!ownerDtos.isEmpty()) {
                    //innerJobOrder.setMaterialOwnerId(ownerDtos.get(0).getMaterialOwnerId());
                }
                innerJobOrder.setWarehouseId(dto.getWarehouseId());
                innerJobOrder.setJobOrderCode(CodeUtils.getId("SHIFT-"));
                innerJobOrder.setJobOrderType((byte) 2);

//                innerJobOrder.setActualQty(innerJobOrder.getPlanQty());
                innerJobOrder.setOrderStatus((byte) 4);
                innerJobOrder.setStatus((byte) 1);
                innerJobOrder.setOrgId(sysUser.getOrganizationId());
                innerJobOrder.setCreateTime(new Date());
                innerJobOrder.setCreateUserId(sysUser.getUserId());
                innerJobOrder.setIsDelete((byte) 1);
                wmsInnerJobOrderMapper.insertUseGeneratedKeys(innerJobOrder);
                dto.setJobOrderId(innerJobOrder.getJobOrderId());
                WmsInnerHtJobOrder wmsInnerHtJobOrder = new WmsInnerHtJobOrder();
                BeanUtil.copyProperties(innerJobOrder, wmsInnerHtJobOrder);
                wmsInnerHtJobOrderService.save(wmsInnerHtJobOrder);
            }

            // 创建移位明细
            WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
            wmsInnerJobOrderDet.setJobOrderId(dto.getJobOrderId());

            //保存库存ID 2021/10/18
            //wmsInnerJobOrderDet.setSourceDetId(innerInventoryDto.getInventoryId());

//            wmsInnerJobOrderDet.setMaterialOwnerId(innerInventoryDto.getMaterialOwnerId());
//            wmsInnerJobOrderDet.setWarehouseId(dto.getWarehouseId());
            wmsInnerJobOrderDet.setOutStorageId(dto.getStorageId());
            wmsInnerJobOrderDet.setMaterialId(innerInventoryDto.getMaterialId());
//            wmsInnerJobOrderDet.setPackingUnitName(innerInventoryDto.getPackingUnitName());
            wmsInnerJobOrderDet.setPlanQty(dto.getMaterialQty());
            wmsInnerJobOrderDet.setDistributionQty(wmsInnerJobOrderDet.getPlanQty());
//            wmsInnerJobOrderDet.setActualQty(wmsInnerJobOrderDet.getPlanQty());
//            wmsInnerJobOrderDet.setPalletCode(innerInventoryDto.getPalletCode());
//            wmsInnerJobOrderDet.setReceivingDate(innerInventoryDto.getReceivingDate());
            wmsInnerJobOrderDet.setProductionDate(innerInventoryDto.getProductionDate());
            wmsInnerJobOrderDet.setInventoryStatusId(innerInventoryDto.getInventoryStatusId());

            wmsInnerJobOrderDet.setWorkStartTime(new Date());
            wmsInnerJobOrderDet.setWorkEndTime(new Date());
            wmsInnerJobOrderDet.setBatchCode(innerInventoryDto.getBatchCode());
            wmsInnerJobOrderDet.setStatus((byte) 1);
            wmsInnerJobOrderDet.setRemark(innerInventoryDto.getRemark());
            wmsInnerJobOrderDet.setOrgId(sysUser.getOrganizationId());
            wmsInnerJobOrderDet.setCreateTime(new Date());
            wmsInnerJobOrderDet.setCreateUserId(sysUser.getUserId());
            wmsInnerJobOrderDet.setIsDelete((byte) 1);
//            wmsInnerJobOrderDet.setOrderStatus((byte) 4);
//            wmsInnerJobOrderDet.setShiftStorageStatus((byte) 3);
            wmsInnerJobOrderDetMapper.insertUseGeneratedKeys(wmsInnerJobOrderDet);
            WmsInnerHtJobOrderDet innerHtJobOrderDet = new WmsInnerHtJobOrderDet();
            BeanUtil.copyProperties(wmsInnerJobOrderDet, innerHtJobOrderDet);
            wmsInnerHtJobOrderDetService.save(innerHtJobOrderDet);

            // 新增待出库存信息
            WmsInnerInventory newInnerInventory = new WmsInnerInventory();
            BeanUtil.copyProperties(innerInventoryDto, newInnerInventory);
            newInnerInventory.setPackingQty(dto.getMaterialQty());
            newInnerInventory.setParentInventoryId(innerInventoryDto.getInventoryId());
            newInnerInventory.setRelevanceOrderCode(innerJobOrder.getJobOrderCode());
            newInnerInventory.setJobStatus((byte) 2);
            newInnerInventory.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
            newInnerInventory.setOrgId(sysUser.getOrganizationId());
            newInnerInventory.setCreateTime(new Date());
            newInnerInventory.setCreateUserId(sysUser.getUserId());
            wmsInnerInventoryService.save(newInnerInventory);
            // 变更减少原库存
            innerInventoryDto.setPackingQty(innerInventoryDto.getPackingQty().subtract(dto.getMaterialQty()));
            wmsInnerInventoryService.update(innerInventoryDto);

        }

        if (!dto.getBarcodes().isEmpty()) {
            List<WmsInnerJobOrderDetBarcode> jobOrderDetBarcodeList = new ArrayList<>();
            List<WmsInnerHtJobOrderDetBarcode> htJobOrderDetBarcodes = new ArrayList<>();
            for (String barcode : dto.getBarcodes()) {
                // 创建条码移位单明细关系
                WmsInnerJobOrderDetBarcode wmsInnerJobOrderDetBarcode = new WmsInnerJobOrderDetBarcode();
//                wmsInnerJobOrderDetBarcode.setBarcode(barcode);
                wmsInnerJobOrderDetBarcode.setJobOrderDetId(dto.getJobOrderDetId());
                wmsInnerJobOrderDetBarcode.setStatus((byte) 1);
                wmsInnerJobOrderDetBarcode.setOrgId(sysUser.getOrganizationId());
                wmsInnerJobOrderDetBarcode.setCreateTime(new Date());
                wmsInnerJobOrderDetBarcode.setCreateUserId(sysUser.getUserId());
                wmsInnerJobOrderDetBarcode.setIsDelete((byte) 1);
                jobOrderDetBarcodeList.add(wmsInnerJobOrderDetBarcode);
                WmsInnerHtJobOrderDetBarcode innerHtJobOrderDetBarcode = new WmsInnerHtJobOrderDetBarcode();
                BeanUtil.copyProperties(wmsInnerJobOrderDetBarcode, innerHtJobOrderDetBarcode);
                htJobOrderDetBarcodes.add(innerHtJobOrderDetBarcode);
            }
            if (jobOrderDetBarcodeList.size() > 0) {
                wmsInnerJobOrderDetBarcodeService.batchSave(jobOrderDetBarcodeList);
            }
            if (htJobOrderDetBarcodes.size() > 0) {
                wmsInnerHtJobOrderDetBarcodeService.batchSave(htJobOrderDetBarcodes);
            }
        }

        return dto.getJobOrderId().toString();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int saveJobOrder(SaveShiftJobOrderDto dto) {
        SysUser sysUser = currentUser();

        WmsInnerJobOrderDet wmsInnerJobOrderDet = wmsInnerJobOrderDetMapper.selectByPrimaryKey(dto.getJobOrderDetId());
        wmsInnerJobOrderDet.setActualQty(wmsInnerJobOrderDet.getDistributionQty());
        if (StringUtils.isEmpty(wmsInnerJobOrderDet.getActualQty())) {
            throw new BizErrorException("上架数量不能小于1");
        }
        BaseStorage baseStorage = baseFeignApi.detail(dto.getStorageId()).getData();
        if (baseStorage == null) {
            throw new BizErrorException(ErrorCodeEnum.PDA5001007);
        }

        SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
        searchWmsInnerJobOrderDet.setJobOrderDetId(dto.getJobOrderDetId());
        WmsInnerJobOrderDetDto oldDto = wmsInnerJobOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);
        if (wmsInnerJobOrderDet.getActualQty().compareTo(wmsInnerJobOrderDet.getDistributionQty()) != 0) {
            throw new BizErrorException("上架数量不能大于分配数量");
        }
        // 变更移位单明细
        WmsInnerJobOrderDet wms = WmsInnerJobOrderDet.builder()
                .jobOrderDetId(dto.getJobOrderDetId())
                .inStorageId(baseStorage.getStorageId())
                .actualQty(wmsInnerJobOrderDet.getActualQty())
//                .orderStatus((byte) 5)
                .modifiedTime(new Date())
//                .shiftStorageStatus((byte) 4)
                .modifiedUserId(sysUser.getUserId())
                .workStartTime(new Date())
                .workEndTime(new Date())
                .build();
        int num = wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wms);
        if (num == 0) {
            throw new BizErrorException("上架失败");
        }

        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setJobOrderId(wmsInnerJobOrderDet.getJobOrderId());
        WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInnerJobOrderMapper.findList(searchWmsInnerJobOrder).get(0);
        List<WmsInnerJobOrderDet> wmsInnerJobOrderDetDto = wmsInnerJobOrderDto.getWmsInPutawayOrderDets();

        // 更改库存
        Example example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialId", oldDto.getMaterialId())
//                .andEqualTo("warehouseId", oldDto.getWarehouseId())
                .andEqualTo("storageId", oldDto.getOutStorageId())
                .andEqualTo("jobOrderDetId", oldDto.getJobOrderDetId())
                .andEqualTo("jobStatus", (byte) 2)
                .andEqualTo("stockLock", 0)
                .andEqualTo("qcLock", 0)
                .andEqualTo("lockStatus", 0);
        WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
        example.clear();
        Example.Criteria criteria1 = example.createCriteria().andEqualTo("materialId", oldDto.getMaterialId())
//                .andEqualTo("warehouseId", oldDto.getWarehouseId())
                .andEqualTo("storageId", baseStorage.getStorageId())
                .andEqualTo("jobStatus", (byte) 1)
                .andEqualTo("stockLock", 0)
                .andEqualTo("qcLock", 0)
                .andEqualTo("lockStatus", 0)
                .andGreaterThan("packingQty", 0);
        if (StringUtils.isNotEmpty(wmsInnerInventory)){
            criteria1.andEqualTo("inventoryStatusId", wmsInnerInventory.getInventoryStatusId());
        }
        WmsInnerInventory wmsInnerInventory_old = wmsInnerInventoryMapper.selectOneByExample(example);
        //获取初期数量
        WmsInnerInventory innerInventory = wmsInnerInventoryMapper.selectByPrimaryKey(wmsInnerInventory.getParentInventoryId());
        if(StringUtils.isEmpty(innerInventory.getPackingQty())){
            innerInventory.setPackingQty(BigDecimal.ZERO);
        }
        BigDecimal initQty = innerInventory.getPackingQty().add(wmsInnerInventory.getPackingQty());
        oldDto.setInStorageId(baseStorage.getStorageId());
        if (StringUtils.isEmpty(wmsInnerInventory_old)) {
            if (StringUtils.isEmpty(wmsInnerInventory)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            wmsInnerInventory.setJobStatus((byte) 1);
            wmsInnerInventory.setStorageId(baseStorage.getStorageId());
            wmsInnerInventoryService.updateByPrimaryKeySelective(wmsInnerInventory);


            //库存日志
            InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrderDto,oldDto,initQty,wmsInnerInventory.getPackingQty(),(byte)3,(byte)2);
            InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrderDto,oldDto,BigDecimal.ZERO,wmsInnerInventory.getPackingQty(),(byte)3,(byte)1);
        } else {

            //库存日志
            InventoryLogUtil.addLog(wmsInnerInventory_old,wmsInnerJobOrderDto,oldDto,initQty,wmsInnerInventory.getPackingQty(),(byte)3,(byte)2);
            InventoryLogUtil.addLog(wmsInnerInventory_old,wmsInnerJobOrderDto,oldDto,wmsInnerInventory_old.getPackingQty(),wmsInnerInventory.getPackingQty(),(byte)3,(byte)1);

            wmsInnerInventory_old.setPackingQty(wmsInnerInventory_old.getPackingQty() != null ? wmsInnerInventory_old.getPackingQty().add(wmsInnerJobOrderDet.getActualQty()) : wmsInnerInventory.getPackingQty());
            wmsInnerInventory_old.setRelevanceOrderCode(wmsInnerInventory.getRelevanceOrderCode());
            wmsInnerInventoryService.updateByPrimaryKeySelective(wmsInnerInventory_old);
            wmsInnerInventory.setPackingQty(BigDecimal.ZERO);
            wmsInnerInventoryService.updateByPrimaryKeySelective(wmsInnerInventory);
            //wmsInnerInventoryService.delete(wmsInnerInventory);
        }

        //更新库存明细
        Example example1 = new Example(WmsInnerJobOrderDetBarcode.class);
        example1.createCriteria().andEqualTo("jobOrderDetId", dto.getJobOrderDetId());
        List<WmsInnerJobOrderDetBarcode> orderDetBarcodeList = wmsInnerJobOrderDetBarcodeService.selectByExample(example1);
        if (!orderDetBarcodeList.isEmpty()) {
            for (WmsInnerJobOrderDetBarcode jobOrderDetBarcode : orderDetBarcodeList) {
                Map<String, Object> map = new HashMap<>();
                map.put("storageId", wmsInnerJobOrderDet.getOutStorageId());
//                map.put("barcode", jobOrderDetBarcode.getBarcode());
                List<WmsInnerInventoryDetDto> inventoryDetDtos = wmsInnerInventoryDetService.findList(map);
                if (inventoryDetDtos.isEmpty()) {
                    throw new BizErrorException(ErrorCodeEnum.PDA5001004);
                }
                WmsInnerInventoryDetDto inventoryDetDto = inventoryDetDtos.get(0);
                inventoryDetDto.setStorageId(dto.getStorageId());
                wmsInnerInventoryDetService.update(inventoryDetDto);
            }
        }

        // 变更移位单
        wms = new WmsInnerJobOrderDet();
        wms.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
        int count = wmsInnerJobOrderDetService.selectCount(wms);
//        wms.setOrderStatus((byte) 5);
        int oCount = wmsInnerJobOrderDetService.selectCount(wms);

        SearchBaseWorker searchBaseWorker = new SearchBaseWorker();
        searchBaseWorker.setWarehouseId(wmsInnerJobOrderDto.getWarehouseId());
        searchBaseWorker.setUserId(sysUser.getUserId());
        List<BaseWorkerDto> workerDtos = baseFeignApi.findList(searchBaseWorker).getData();
        if (workerDtos.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.PDA5001014);
        }

        BigDecimal resQty = wmsInnerJobOrderDetDto.stream()
                .map(WmsInnerJobOrderDet::getDistributionQty)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        WmsInnerJobOrder ws = new WmsInnerJobOrder();
        ws.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
//        ws.setActualQty(resQty);
        ws.setModifiedUserId(sysUser.getUserId());
        ws.setModifiedTime(new Date());
        ws.setWorkEndtTime(new Date());
        ws.setWorkerId(workerDtos.get(0).getWorkerId());
        if (oCount == count) {
            ws.setOrderStatus((byte) 5);
            if(StringUtils.isEmpty(wmsInnerJobOrderDto.getWorkStartTime())){
                ws.setWorkStartTime(new Date());
            }
            ws.setWorkEndtTime(new Date());

        } else {
            ws.setOrderStatus((byte) 4);
            if (StringUtils.isEmpty(wmsInnerJobOrderDto.getWorkStartTime())) {
                ws.setWorkStartTime(new Date());
            }
        }
        num += wmsInnerJobOrderMapper.updateByPrimaryKeySelective(ws);

        if(oCount == count){
            //回传接口（五环）
            //获取程序配置项
            SearchSysSpecItem searchSysSpecItemFiveRing = new SearchSysSpecItem();
            searchSysSpecItemFiveRing.setSpecCode("FiveRing");
            List<SysSpecItem> itemListFiveRing = securityFeignApi.findSpecItemList(searchSysSpecItemFiveRing).getData();
            if(itemListFiveRing.size()<1){
                throw new BizErrorException("配置项 FiveRing 获取失败");
            }
            SysSpecItem sysSpecItem = itemListFiveRing.get(0);
            if("1".equals(sysSpecItem.getParaValue())) {
                //返写移位接口（五环）
                if(wmsInnerJobOrderDto.getJobOrderType()==2){
                    ws.setOption1(baseStorage.getOption1());//baseStorage
                    engFeignApi.reportInnerJobOrder(ws);
                }
            }
            //回传结束
        }

        return num;
    }


    /**
     * 获取当前登录用户
     *
     * @return
     */
    private SysUser currentUser() {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}
