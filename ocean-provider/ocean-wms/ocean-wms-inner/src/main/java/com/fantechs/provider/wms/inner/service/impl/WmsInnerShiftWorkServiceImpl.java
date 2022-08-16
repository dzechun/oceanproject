package com.fantechs.provider.wms.inner.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseMaterialOwnerDto;
import com.fantechs.common.base.general.dto.basic.BaseWorkerDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcBarcodeProcessDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcKeyPartRelevanceDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcess;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcKeyPartRelevance;
import com.fantechs.common.base.general.dto.wms.inner.*;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.BaseStorageCapacity;
import com.fantechs.common.base.general.entity.basic.search.*;
import com.fantechs.common.base.general.entity.wms.inner.*;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerJobOrderDetBarcodeMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerJobOrderDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerJobOrderMapper;
import com.fantechs.provider.wms.inner.service.*;
import com.fantechs.provider.wms.inner.util.InventoryLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
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
    WmsInnerJobOrderDetBarcodeMapper wmsInnerJobOrderDetBarcodeMapper;

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
    private SFCFeignApi sfcFeignApi;

    @Override
    public List<WmsInnerJobOrderDto> pdaFindList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", sysUser.getOrganizationId());
        map.put("jobOrderType", (byte) 2);
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
        //获取厂内码
        String factoryBarcode = getFactoryBarcode(dto.getBarcode());

        Map<String, Object> map = new HashMap<>();
        map.put("storageId", dto.getStorageId());
        map.put("barcode", factoryBarcode);

        List<WmsInnerInventoryDetDto> inventoryDetDtos = wmsInnerInventoryDetService.findList(map);
        Long materialId = 0L;
        BigDecimal materialQty = BigDecimal.ZERO;
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
        //移位类型(1-正常移位单 2-质检移位单 3-三星移位单)
        Byte shiftType=null;
        List<WmsInnerJobOrderDet> detList=new ArrayList<>();
        if(StringUtils.isNotEmpty(dto.getJobOrderDetId())) {
            WmsInnerJobOrderDet wmsInnerJobOrderDet = wmsInnerJobOrderDetMapper.selectByPrimaryKey(dto.getJobOrderDetId());
            WmsInnerJobOrder wmsInnerJobOrder=wmsInnerJobOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDet.getJobOrderId());
            shiftType=wmsInnerJobOrder.getShiftType();

            Example example = new Example(WmsInnerJobOrderDet.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("jobOrderId",wmsInnerJobOrder.getJobOrderId());
            detList=wmsInnerJobOrderDetMapper.selectByExample(example);
        }
        if(detList.size()>0){
            for (WmsInnerJobOrderDet wmsInnerJobOrderDet : detList) {
                Long jobOrderDetId=wmsInnerJobOrderDet.getJobOrderDetId();
                Example example = new Example(WmsInnerJobOrderDetBarcode.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("jobOrderDetId",jobOrderDetId);
                criteria.andEqualTo("barcode",factoryBarcode);
                WmsInnerJobOrderDetBarcode detBarcode=wmsInnerJobOrderDetBarcodeMapper.selectOneByExample(example);
                if(StringUtils.isNotEmpty(detBarcode)){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"条码已扫描-->"+dto.getBarcode());
                }
            }
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
        if(StringUtils.isNotEmpty(shiftType) && shiftType==(byte)1){
            map.put("qcLock", (byte) 0);
        }
        //map.put("qcLock", (byte) 0);
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
        recordDto.setWarehouseId(dto.getWarehouseId());
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
    @LcnTransaction
    public String saveShiftWorkDetBarcode(SaveShiftWorkDetDto dto) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
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
            jobOrderDet.setOrderStatus((byte) 4);
            jobOrderDet.setActualQty(jobOrderDet.getActualQty() != null ? jobOrderDet.getActualQty().add(dto.getMaterialQty()) : dto.getMaterialQty());
            if (jobOrderDet.getActualQty().compareTo(jobOrderDet.getDistributionQty()) == 1){
                throw new BizErrorException(ErrorCodeEnum.PDA5001006.getCode(), "移位数量不能大于计划数量");
            }
            Byte status = 2;
            if (jobOrderDet.getActualQty().compareTo(jobOrderDet.getDistributionQty()) == 0) {
                status = (byte) 3;
            }
            jobOrderDet.setShiftStorageStatus(status);
            wmsInnerJobOrderDetService.update(jobOrderDet);

            // 作业单拣货数量+1以及变更单据状态
            WmsInnerJobOrder innerJobOrder = wmsInnerJobOrderService.selectByKey(dto.getJobOrderId());
            innerJobOrder.setOrderStatus((byte) 4);
            innerJobOrder.setActualQty(innerJobOrder.getActualQty() != null ? innerJobOrder.getActualQty().add(dto.getMaterialQty()) : dto.getMaterialQty());
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
//            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
//            searchSysSpecItem.setSpecCode("inventory_status_value");
//            List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
//            List<WmsInnerInventoryDto> dtos = new ArrayList<>();
//            if (specItems.size() > 0 && !innerInventoryDtos.isEmpty() && innerInventoryDtos.size() > 0){
//                for (WmsInnerInventoryDto inventoryDto : innerInventoryDtos){
//                    if (inventoryDto.getInventoryStatusName().equals(specItems.get(0).getParaValue())){
//                        dtos.add(inventoryDto);
//                    }
//                }
//                if (dtos.size() <= 0){
//                    throw new BizErrorException(ErrorCodeEnum.PDA5001012.getCode(), "暂无库存或存库状态为待捡，不可操作");
//                }
//            }else {
//                dtos = innerInventoryDtos;
//            }
//            WmsInnerInventoryDto innerInventoryDto = dtos.get(0);
            WmsInnerInventoryDto innerInventoryDto = innerInventoryDtos.get(0);
            if (innerInventoryDto.getPackingQty().compareTo(dto.getMaterialQty()) < -1) {
                throw new BizErrorException(ErrorCodeEnum.PDA5001012);
            }

            WmsInnerJobOrder innerJobOrder = new WmsInnerJobOrder();
            if (dto.getJobOrderId() != null) {
                // 更新移位单
                innerJobOrder = wmsInnerJobOrderService.selectByKey(dto.getJobOrderId());
                innerJobOrder.setOrderStatus((byte) 4);
                innerJobOrder.setActualQty(innerJobOrder.getActualQty() != null ? innerJobOrder.getActualQty().add(dto.getMaterialQty()) : dto.getMaterialQty());
                innerJobOrder.setPlanQty(innerJobOrder.getActualQty());
                wmsInnerJobOrderMapper.updateByPrimaryKey(innerJobOrder);
            } else {
                // 创建移位单
                SearchBaseMaterialOwner searchBaseMaterialOwner = new SearchBaseMaterialOwner();
                searchBaseMaterialOwner.setAsc((byte) 1);
                List<BaseMaterialOwnerDto> ownerDtos = baseFeignApi.findList(searchBaseMaterialOwner).getData();
                if (!ownerDtos.isEmpty()) {
                    innerJobOrder.setMaterialOwnerId(ownerDtos.get(0).getMaterialOwnerId());
                }
                innerJobOrder.setWarehouseId(dto.getWarehouseId());
                innerJobOrder.setJobOrderCode(CodeUtils.getId("SHIFT-"));
                innerJobOrder.setJobOrderType((byte) 2);
                innerJobOrder.setPlanQty(dto.getMaterialQty());
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
            wmsInnerJobOrderDet.setSourceDetId(innerInventoryDto.getInventoryId());

            wmsInnerJobOrderDet.setMaterialOwnerId(innerInventoryDto.getMaterialOwnerId());
            wmsInnerJobOrderDet.setWarehouseId(dto.getWarehouseId());
            wmsInnerJobOrderDet.setOutStorageId(dto.getStorageId());
            wmsInnerJobOrderDet.setMaterialId(innerInventoryDto.getMaterialId());
            wmsInnerJobOrderDet.setPackingUnitName(innerInventoryDto.getPackingUnitName());
            wmsInnerJobOrderDet.setPlanQty(dto.getMaterialQty());
            wmsInnerJobOrderDet.setDistributionQty(wmsInnerJobOrderDet.getPlanQty());
//            wmsInnerJobOrderDet.setActualQty(wmsInnerJobOrderDet.getPlanQty());
            wmsInnerJobOrderDet.setPalletCode(innerInventoryDto.getPalletCode());
            wmsInnerJobOrderDet.setReceivingDate(innerInventoryDto.getReceivingDate());
            wmsInnerJobOrderDet.setProductionDate(innerInventoryDto.getProductionDate());
            wmsInnerJobOrderDet.setInventoryStatusId(innerInventoryDto.getInventoryStatusId());
            wmsInnerJobOrderDet.setExpiredDate(innerInventoryDto.getExpiredDate());
            wmsInnerJobOrderDet.setWorkStartTime(new Date());
            wmsInnerJobOrderDet.setWorkEndTime(new Date());
            wmsInnerJobOrderDet.setBatchCode(innerInventoryDto.getBatchCode());
            wmsInnerJobOrderDet.setStatus((byte) 1);
            wmsInnerJobOrderDet.setRemark(innerInventoryDto.getRemark());
            wmsInnerJobOrderDet.setOrgId(sysUser.getOrganizationId());
            wmsInnerJobOrderDet.setCreateTime(new Date());
            wmsInnerJobOrderDet.setCreateUserId(sysUser.getUserId());
            wmsInnerJobOrderDet.setIsDelete((byte) 1);
            wmsInnerJobOrderDet.setOrderStatus((byte) 4);
            wmsInnerJobOrderDet.setShiftStorageStatus((byte) 3);
            wmsInnerJobOrderDetMapper.insertUseGeneratedKeys(wmsInnerJobOrderDet);
            dto.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
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
                String factoryBarcode = this.getFactoryBarcode(barcode);
                // 创建条码移位单明细关系
                WmsInnerJobOrderDetBarcode wmsInnerJobOrderDetBarcode = new WmsInnerJobOrderDetBarcode();
                wmsInnerJobOrderDetBarcode.setBarcode(factoryBarcode);
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
    @LcnTransaction
    public int saveJobOrder(SaveShiftJobOrderDto dto) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();

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
                .orderStatus((byte) 5)
                .modifiedTime(new Date())
                .shiftStorageStatus((byte) 4)
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

        Byte shiftType=wmsInnerJobOrderDto.getShiftType();

        // 更改库存
        Example example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialId", oldDto.getMaterialId())
                .andEqualTo("warehouseId", oldDto.getWarehouseId())
                .andEqualTo("storageId", oldDto.getOutStorageId())
                .andEqualTo("jobOrderDetId", oldDto.getJobOrderDetId())
                .andEqualTo("jobStatus", (byte) 2)
                .andEqualTo("stockLock", 0)
                //.andEqualTo("qcLock", 0)
                .andEqualTo("lockStatus", 0);

        //正常移位单
        if(StringUtils.isNotEmpty(shiftType) && shiftType==(byte)1){
            criteria.andEqualTo("qcLock",0);
        }
        WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
        example.clear();
        Example.Criteria criteria1 = example.createCriteria().andEqualTo("materialId", oldDto.getMaterialId())
                .andEqualTo("warehouseId", oldDto.getWarehouseId())
                .andEqualTo("storageId", baseStorage.getStorageId())
                .andEqualTo("jobStatus", (byte) 1)
                .andEqualTo("stockLock", 0)
                //.andEqualTo("qcLock", 0)
                .andEqualTo("lockStatus", 0)
                .andGreaterThan("packingQty", 0);
        if (StringUtils.isNotEmpty(wmsInnerInventory) && StringUtils.isNotEmpty(shiftType) && shiftType==(byte)1){
            criteria1.andEqualTo("inventoryStatusId", wmsInnerInventory.getInventoryStatusId());
        }
        if(StringUtils.isNotEmpty(shiftType) && shiftType==(byte)1){
            criteria.andEqualTo("qcLock",0);
        }
        else if(StringUtils.isNotEmpty(shiftType) && shiftType!=(byte)1){
            criteria1.andEqualTo("inventoryStatusId", wmsInnerJobOrderDet.getInventoryStatusId());
            criteria1.andEqualTo("inspectionOrderCode", wmsInnerJobOrderDto.getRelatedOrderCode());
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
                map.put("barcode", jobOrderDetBarcode.getBarcode());
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
        wms.setOrderStatus((byte) 5);
        int oCount = wmsInnerJobOrderDetService.selectCount(wms);

        BigDecimal resQty = wmsInnerJobOrderDetDto.stream()
                .map(WmsInnerJobOrderDet::getDistributionQty)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        WmsInnerJobOrder ws = new WmsInnerJobOrder();
        ws.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
        ws.setActualQty(resQty);
        ws.setModifiedUserId(sysUser.getUserId());
        ws.setModifiedTime(new Date());
        ws.setWorkEndtTime(new Date());

        /**
         * 20211216 bgkun
         * 万宝项目不限制操作人员作业，通过程序配置项过滤
         * 0-不校验1-校验
         */
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("checkWorkPerson");
        List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        if(specItems.isEmpty() || "1".equals(specItems.get(0).getParaValue())){
            SearchBaseWorker searchBaseWorker = new SearchBaseWorker();
            searchBaseWorker.setWarehouseId(wmsInnerJobOrderDto.getWarehouseId());
            searchBaseWorker.setUserId(sysUser.getUserId());
            List<BaseWorkerDto> workerDtos = baseFeignApi.findList(searchBaseWorker).getData();
            if (workerDtos.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.PDA5001014);
            }
            ws.setWorkerId(workerDtos.get(0).getWorkerId());
        }

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
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public Long saveJobOrderReturnId(SaveShiftJobOrderDto dto) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        Long inventoryId=null;
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
                .orderStatus((byte) 5)
                .modifiedTime(new Date())
                .shiftStorageStatus((byte) 4)
                .modifiedUserId(sysUser.getUserId())
                .workStartTime(new Date())
                .workEndTime(new Date())
                .build();
        int num = wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wms);
        if (num == 0) {
            throw new BizErrorException("上架失败");
        }

        log.info("============= 移位单上架确认参数===================" + JSON.toJSONString(dto));

        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setJobOrderId(wmsInnerJobOrderDet.getJobOrderId());
        WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInnerJobOrderMapper.findList(searchWmsInnerJobOrder).get(0);
        List<WmsInnerJobOrderDet> wmsInnerJobOrderDetDto = wmsInnerJobOrderDto.getWmsInPutawayOrderDets();

        Byte shiftType=wmsInnerJobOrderDto.getShiftType();

        // 更改库存
        Example example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialId", oldDto.getMaterialId())
                .andEqualTo("warehouseId", oldDto.getWarehouseId())
                .andEqualTo("storageId", oldDto.getOutStorageId())
                .andEqualTo("jobOrderDetId", oldDto.getJobOrderDetId())
                .andEqualTo("jobStatus", (byte) 2)
                .andEqualTo("stockLock", 0)
                //.andEqualTo("qcLock", 0)
                .andEqualTo("lockStatus", 0);

        //正常移位单
        if(StringUtils.isNotEmpty(shiftType) && shiftType==(byte)1){
            criteria.andEqualTo("qcLock",0);
        }
        WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
        log.info("============= 移位单明细待出库存===================" + JSON.toJSONString(wmsInnerInventory));
        example.clear();
        Example.Criteria criteria1 = example.createCriteria().andEqualTo("materialId", oldDto.getMaterialId())
                .andEqualTo("warehouseId", oldDto.getWarehouseId())
                .andEqualTo("storageId", baseStorage.getStorageId())
                .andEqualTo("jobStatus", (byte) 1)
                .andEqualTo("stockLock", 0)
                //.andEqualTo("qcLock", 0)
                .andEqualTo("lockStatus", 0);
//                .andGreaterThan("packingQty", 0);
        if (StringUtils.isNotEmpty(wmsInnerInventory) && StringUtils.isNotEmpty(shiftType) && shiftType==(byte)1){
            criteria1.andEqualTo("inventoryStatusId", wmsInnerInventory.getInventoryStatusId());
        }
        if(StringUtils.isNotEmpty(shiftType) && shiftType==(byte)1){
            criteria.andEqualTo("qcLock",0);
        }
        else if(StringUtils.isNotEmpty(shiftType) && shiftType!=(byte)1){
            criteria1.andEqualTo("inventoryStatusId", wmsInnerJobOrderDet.getInventoryStatusId());
            criteria1.andEqualTo("inspectionOrderCode", wmsInnerJobOrderDto.getRelatedOrderCode());
        }
        WmsInnerInventory wmsInnerInventory_old = wmsInnerInventoryMapper.selectOneByExample(example);
        log.info("============= 移位单明细移入库位库存===================" + JSON.toJSONString(wmsInnerInventory_old));
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

            inventoryId=wmsInnerInventory.getInventoryId();
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

            inventoryId=wmsInnerInventory_old.getInventoryId();
        }

        //更新库存明细
        Example example1 = new Example(WmsInnerJobOrderDetBarcode.class);
        example1.createCriteria().andEqualTo("jobOrderDetId", dto.getJobOrderDetId());
        List<WmsInnerJobOrderDetBarcode> orderDetBarcodeList = wmsInnerJobOrderDetBarcodeService.selectByExample(example1);
        if (!orderDetBarcodeList.isEmpty()) {
            for (WmsInnerJobOrderDetBarcode jobOrderDetBarcode : orderDetBarcodeList) {
                Map<String, Object> map = new HashMap<>();
                map.put("storageId", wmsInnerJobOrderDet.getOutStorageId());
                map.put("barcode", jobOrderDetBarcode.getBarcode());
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
        wms.setOrderStatus((byte) 5);
        int oCount = wmsInnerJobOrderDetService.selectCount(wms);

        BigDecimal resQty = wmsInnerJobOrderDetDto.stream()
                .map(WmsInnerJobOrderDet::getDistributionQty)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        WmsInnerJobOrder ws = new WmsInnerJobOrder();
        ws.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
        ws.setActualQty(resQty);
        ws.setModifiedUserId(sysUser.getUserId());
        ws.setModifiedTime(new Date());
        ws.setWorkEndtTime(new Date());

        /**
         * 20211216 bgkun
         * 万宝项目不限制操作人员作业，通过程序配置项过滤
         * 0-不校验1-校验
         */
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("checkWorkPerson");
        List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        if(specItems.isEmpty() || "1".equals(specItems.get(0).getParaValue())){
            SearchBaseWorker searchBaseWorker = new SearchBaseWorker();
            searchBaseWorker.setWarehouseId(wmsInnerJobOrderDto.getWarehouseId());
            searchBaseWorker.setUserId(sysUser.getUserId());
            List<BaseWorkerDto> workerDtos = baseFeignApi.findList(searchBaseWorker).getData();
            if (workerDtos.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.PDA5001014);
            }
            ws.setWorkerId(workerDtos.get(0).getWorkerId());
        }

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

        return inventoryId;
    }

    @Override
    public StorageDto scanStorage(ScanStorageDto dto) {
        if (StringUtils.isEmpty(dto.getStorageCode())){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "移出移入库位为空，不允许操作");
        }

        SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
        searchBaseStorage.setStorageCode(dto.getStorageCode());
        List<BaseStorage> baseStorages = baseFeignApi.findList(searchBaseStorage).getData();
        if (baseStorages.isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), dto.getStorageCode() + "此库位编码不存在，不允许操作");
        }
        StorageDto storageDto = new StorageDto();
        storageDto.setStorageId(baseStorages.get(0).getStorageId());
        storageDto.setStorageCode(baseStorages.get(0).getStorageCode());
        storageDto.setType(dto.getType());

        if ("1".equals(dto.getType())){
            // 移出库位，返回物料相关信息
            Map<String, Object> map = new HashMap<>();
            map.put("storageCode", dto.getStorageCode());
            // 扫条码，返回条码相关物料。没有指定条码，返回库位下所有物料
            if (StringUtils.isNotEmpty(dto.getBarcode())){
                Example example = new Example(WmsInnerInventoryDet.class);
                example.createCriteria().orEqualTo("barcode", dto.getBarcode())
                        .orEqualTo("salesBarcode", dto.getBarcode())
                        .orEqualTo("customerBarcode", dto.getBarcode());
                List<WmsInnerInventoryDet> inventoryDets = wmsInnerInventoryDetService.selectByExample(example);
                if (inventoryDets.isEmpty()){
                    throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), dto.getBarcode() + "此条码在库存中不存在");
                }
                map.put("materialId", inventoryDets.get(0).getMaterialId());
            }
            List<InStorageMaterialDto> inStorageMaterialDtos = wmsInnerInventoryDetService.findInventoryDetByStorage(map);
            if (inStorageMaterialDtos.isEmpty()){
                throw new BizErrorException(ErrorCodeEnum.PDA5001012.getCode(), dto.getStorageCode() + "的库位暂无库存或存库非正常状态");
            }
            List<InStorageMaterialDto> list = new ArrayList<>();
            for (InStorageMaterialDto inStorageMaterialDto : inStorageMaterialDtos){
                Map<String, Object> inventoryMap = new HashMap<>();
                inventoryMap.put("materialId", inStorageMaterialDto.getMaterialId());
                inventoryMap.put("storageId", storageDto.getStorageId());
                inventoryMap.put("warehouseId", baseStorages.get(0).getWarehouseId());
                inventoryMap.put("jobStatus", 1);
                inventoryMap.put("stockLock", 0);
                inventoryMap.put("qcLock", 0);
                inventoryMap.put("lockStatus", 0);
                List<WmsInnerInventoryDto> innerInventoryDtoList = wmsInnerInventoryService.findList(inventoryMap);
                if (!innerInventoryDtoList.isEmpty() && innerInventoryDtoList.get(0).getInventoryStatusName().equals("合格")){
                    list.add(inStorageMaterialDto);
                }
            }
            if (list.size() < 1){
                throw new BizErrorException(ErrorCodeEnum.PDA5001012.getCode(), dto.getStorageCode() + "的库位暂无库存或存库非正常状态");
            }
            storageDto.setList(list);
        }
        return storageDto;
    }

    @Override
    @Transactional
    public int batchShiftWork(BatchSiftWorkDto dto) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(dto.getOutStorageId(), dto.getInStorageId())){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "移出移入库位为空，不允许操作");
        }

        SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
        searchBaseStorage.setStorageId(dto.getInStorageId());
        List<BaseStorage> baseStorages = baseFeignApi.findList(searchBaseStorage).getData();
        if (baseStorages.isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "移入库位不存在，不允许操作");
        }
        // 移入库位
        BaseStorage baseStorage = baseStorages.get(0);

        searchBaseStorage.setStorageId(dto.getOutStorageId());
        List<BaseStorage> outBaseStorage = baseFeignApi.findList(searchBaseStorage).getData();
        if (outBaseStorage.isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "移出库位不存在，不允许操作");
        }
        if (!outBaseStorage.get(0).getWarehouseId().equals(baseStorage.getWarehouseId())){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "移入库位跟移出库位必须同仓库");
        }

        // 计算库容容量能否存放
        BigDecimal count = BigDecimal.ZERO;
        for (InStorageMaterialDto storageMaterialDto : dto.getList()){
            // 暂用数量
            BigDecimal calculate = calculate(baseStorage, storageMaterialDto.getMaterialId(), storageMaterialDto.getMaterialCode());
            if (storageMaterialDto.getQty().compareTo(calculate) == 1){
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "移入库位" + baseStorage.getStorageCode() + "的库容容量不足以存放此次移位");
            }
            // 当前物料数量占用库位中可用数量的百分比
            BigDecimal divide = storageMaterialDto.getQty().divide(calculate,2, BigDecimal.ROUND_DOWN);
            count.add(divide);
        }
        // 累积百分比大于100，总库存不可存放
        if (count.compareTo(new BigDecimal("100")) == 1){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "移入库位" + baseStorage.getStorageCode() + "的库容容量不足以存放此次移位");
        }

        // 生成移位单
        WmsInnerJobOrder innerJobOrder = new WmsInnerJobOrder();
        SearchBaseMaterialOwner searchBaseMaterialOwner = new SearchBaseMaterialOwner();
        searchBaseMaterialOwner.setAsc((byte) 1);
        List<BaseMaterialOwnerDto> ownerDtos = baseFeignApi.findList(searchBaseMaterialOwner).getData();
        if (!ownerDtos.isEmpty()) {
            innerJobOrder.setMaterialOwnerId(ownerDtos.get(0).getMaterialOwnerId());
        }
        innerJobOrder.setWarehouseId(baseStorage.getWarehouseId());
        innerJobOrder.setJobOrderCode(CodeUtils.getId("SHIFT-"));
        innerJobOrder.setJobOrderType((byte) 2);
        innerJobOrder.setPlanQty(dto.getList().stream().map(InStorageMaterialDto::getQty).reduce(BigDecimal.ZERO, BigDecimal::add));
        innerJobOrder.setOrderStatus((byte) 5);
        innerJobOrder.setStatus((byte) 1);
        innerJobOrder.setOrgId(user.getOrganizationId());
        innerJobOrder.setCreateTime(new Date());
        innerJobOrder.setCreateUserId(user.getUserId());
        innerJobOrder.setIsDelete((byte) 1);
        wmsInnerJobOrderMapper.insertUseGeneratedKeys(innerJobOrder);
        WmsInnerHtJobOrder wmsInnerHtJobOrder = new WmsInnerHtJobOrder();
        BeanUtil.copyProperties(innerJobOrder, wmsInnerHtJobOrder);
        wmsInnerHtJobOrderService.save(wmsInnerHtJobOrder);

        // 生成移位单明细
        // 查找移出库位下库存明细
        Map<String, Object> map = new HashMap<>();
        map.put("storageId", dto.getOutStorageId());
        map.put("materialIds", dto.getList().stream().map(InStorageMaterialDto::getMaterialId).collect(Collectors.toList()));
        List<WmsInnerInventoryDetDto> inventoryDetDtos = wmsInnerInventoryDetService.findList(map);
        if (inventoryDetDtos.isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "移出库位下没有库存，不允许操作");
        }
        List<WmsInnerJobOrderDetBarcode> jobOrderDetBarcodeList = new ArrayList<>();
        List<WmsInnerHtJobOrderDetBarcode> htJobOrderDetBarcodes = new ArrayList<>();
        for (InStorageMaterialDto storageMaterialDto : dto.getList()){
            // 查询库存信息，同一库位跟同物料有且只有一条数据
            map.clear();
            map.put("materialId", storageMaterialDto.getMaterialId());
            map.put("storageId", dto.getOutStorageId());
            map.put("jobStatus", (byte) 1);
            map.put("lockStatus", (byte) 0);
            map.put("stockLock", (byte) 0);
            map.put("qcLock", (byte) 0);
            List<WmsInnerInventoryDto> innerInventoryDtos = wmsInnerInventoryService.findList(map);
            if (innerInventoryDtos == null || innerInventoryDtos.size() <= 0) {
                throw new BizErrorException(ErrorCodeEnum.PDA5001009);
            }
//            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
//            searchSysSpecItem.setSpecCode("inventory_status_value");
//            List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
//            List<WmsInnerInventoryDto> dtos = new ArrayList<>();
//            if (specItems.size() > 0 && !innerInventoryDtos.isEmpty() && innerInventoryDtos.size() > 0){
//                for (WmsInnerInventoryDto inventoryDto : innerInventoryDtos){
//                    if (inventoryDto.getInventoryStatusName().equals(specItems.get(0).getParaValue())){
//                        dtos.add(inventoryDto);
//                    }
//                }
//                if (dtos.size() <= 0){
//                    throw new BizErrorException(ErrorCodeEnum.PDA5001012.getCode(), "暂无库存或存库状态为待捡，不可操作");
//                }
//            }else {
//                dtos = innerInventoryDtos;
//            }
//            WmsInnerInventoryDto innerInventoryDto = dtos.get(0);
            WmsInnerInventoryDto innerInventoryDto = innerInventoryDtos.get(0);

            WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
            wmsInnerJobOrderDet.setJobOrderId(innerJobOrder.getJobOrderId());
            wmsInnerJobOrderDet.setSourceDetId(innerInventoryDto.getInventoryId());
            wmsInnerJobOrderDet.setMaterialOwnerId(innerJobOrder.getMaterialOwnerId());
            wmsInnerJobOrderDet.setWarehouseId(baseStorage.getWarehouseId());
            wmsInnerJobOrderDet.setOutStorageId(dto.getOutStorageId());
            wmsInnerJobOrderDet.setInStorageId(dto.getInStorageId());
            wmsInnerJobOrderDet.setInventoryStatusId(innerInventoryDto.getInventoryStatusId());
            wmsInnerJobOrderDet.setMaterialId(storageMaterialDto.getMaterialId());
            wmsInnerJobOrderDet.setPackingUnitName(innerInventoryDto.getPackingUnitName());
            wmsInnerJobOrderDet.setPlanQty(storageMaterialDto.getQty());
            wmsInnerJobOrderDet.setDistributionQty(storageMaterialDto.getQty());
            wmsInnerJobOrderDet.setActualQty(storageMaterialDto.getQty());
            wmsInnerJobOrderDet.setPalletCode(innerInventoryDto.getPalletCode());
            wmsInnerJobOrderDet.setReceivingDate(innerInventoryDto.getReceivingDate());
            wmsInnerJobOrderDet.setProductionDate(innerInventoryDto.getProductionDate());
            wmsInnerJobOrderDet.setInventoryStatusId(innerInventoryDto.getInventoryStatusId());
            wmsInnerJobOrderDet.setExpiredDate(innerInventoryDto.getExpiredDate());
            wmsInnerJobOrderDet.setWorkStartTime(new Date());
            wmsInnerJobOrderDet.setWorkEndTime(new Date());
            wmsInnerJobOrderDet.setBatchCode(innerInventoryDto.getBatchCode());
            wmsInnerJobOrderDet.setStatus((byte) 1);
            wmsInnerJobOrderDet.setRemark(innerInventoryDto.getRemark());
            wmsInnerJobOrderDet.setOrgId(user.getOrganizationId());
            wmsInnerJobOrderDet.setCreateTime(new Date());
            wmsInnerJobOrderDet.setCreateUserId(user.getUserId());
            wmsInnerJobOrderDet.setIsDelete((byte) 1);
            wmsInnerJobOrderDet.setOrderStatus((byte) 5);
            wmsInnerJobOrderDet.setShiftStorageStatus((byte) 5);
            wmsInnerJobOrderDetMapper.insertUseGeneratedKeys(wmsInnerJobOrderDet);
            WmsInnerHtJobOrderDet innerHtJobOrderDet = new WmsInnerHtJobOrderDet();
            BeanUtil.copyProperties(wmsInnerJobOrderDet, innerHtJobOrderDet);
            wmsInnerHtJobOrderDetService.save(innerHtJobOrderDet);

            // 移位单条码关系
            for (WmsInnerInventoryDetDto inventoryDetDto : inventoryDetDtos) {
                // 创建条码移位单明细关系
                if (wmsInnerJobOrderDet.getMaterialId().equals(inventoryDetDto.getMaterialId())){
                    WmsInnerJobOrderDetBarcode wmsInnerJobOrderDetBarcode = new WmsInnerJobOrderDetBarcode();
                    wmsInnerJobOrderDetBarcode.setBarcode(inventoryDetDto.getBarcode());
                    wmsInnerJobOrderDetBarcode.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
                    wmsInnerJobOrderDetBarcode.setStatus((byte) 1);
                    wmsInnerJobOrderDetBarcode.setOrgId(user.getOrganizationId());
                    wmsInnerJobOrderDetBarcode.setCreateTime(new Date());
                    wmsInnerJobOrderDetBarcode.setCreateUserId(user.getUserId());
                    wmsInnerJobOrderDetBarcode.setIsDelete((byte) 1);
                    jobOrderDetBarcodeList.add(wmsInnerJobOrderDetBarcode);
                    WmsInnerHtJobOrderDetBarcode innerHtJobOrderDetBarcode = new WmsInnerHtJobOrderDetBarcode();
                    BeanUtil.copyProperties(wmsInnerJobOrderDetBarcode, innerHtJobOrderDetBarcode);
                    htJobOrderDetBarcodes.add(innerHtJobOrderDetBarcode);

                    // 更新库存明细
                    inventoryDetDto.setStorageId(dto.getInStorageId());
                    wmsInnerInventoryDetService.update(inventoryDetDto);
                }
            }

            // 库存变更
            // 原库存
            Example example = new Example(WmsInnerInventory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("materialId", storageMaterialDto.getMaterialId())
                    .andEqualTo("warehouseId", baseStorage.getWarehouseId())
                    .andEqualTo("storageId", dto.getOutStorageId())
                    .andEqualTo("jobStatus", (byte) 1)
                    .andEqualTo("stockLock", 0)
                    .andEqualTo("qcLock", 0)
                    .andEqualTo("lockStatus", 0);
            WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
            if (wmsInnerInventory == null){
                throw new BizErrorException(ErrorCodeEnum.PDA5001012.getCode(), storageMaterialDto.getMaterialCode() + "的物料暂无库存或存库状态为待入，不可操作");
            }
            BigDecimal initQty = wmsInnerInventory.getPackingQty();
            wmsInnerInventory.setPackingQty(wmsInnerInventory.getPackingQty().subtract(storageMaterialDto.getQty()));
            wmsInnerInventoryService.updateByPrimaryKeySelective(wmsInnerInventory);

            // 移出库存日志
            SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
            searchWmsInnerJobOrderDet.setJobOrderDetId(wmsInnerInventory.getJobOrderDetId());
            WmsInnerJobOrderDetDto oldDto = wmsInnerJobOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);
            SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
            searchWmsInnerJobOrder.setJobOrderId(oldDto.getJobOrderId());
            WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInnerJobOrderMapper.findList(searchWmsInnerJobOrder).get(0);
            InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrderDto,oldDto,initQty,storageMaterialDto.getQty(),(byte)3,(byte)2);

            example.clear();
            Example.Criteria criteria1 = example.createCriteria().andEqualTo("materialId", storageMaterialDto.getMaterialId())
                    .andEqualTo("warehouseId", oldDto.getWarehouseId())
                    .andEqualTo("storageId", dto.getInStorageId())
                    .andEqualTo("jobStatus", (byte) 1)
                    .andEqualTo("stockLock", 0)
                    .andEqualTo("qcLock", 0)
                    .andEqualTo("lockStatus", 0);
            if (StringUtils.isNotEmpty(wmsInnerInventory)){
                criteria1.andEqualTo("inventoryStatusId", wmsInnerInventory.getInventoryStatusId());
            }
            WmsInnerInventory wmsInnerInventory_new = wmsInnerInventoryMapper.selectOneByExample(example);
            if (wmsInnerInventory_new != null){
                // 累加库存
                initQty = wmsInnerInventory_new.getPackingQty();
                wmsInnerInventory_new.setPackingQty(wmsInnerInventory_new.getPackingQty().add(storageMaterialDto.getQty()));
                wmsInnerInventoryService.updateByPrimaryKeySelective(wmsInnerInventory_new);
            }else {
                // 新增库存
                initQty = BigDecimal.ZERO;
                wmsInnerInventory_new = new WmsInnerInventory();
                BeanUtil.copyProperties(wmsInnerInventory, wmsInnerInventory_new);
                wmsInnerInventory_new.setPackingQty(storageMaterialDto.getQty());
                wmsInnerInventory_new.setStorageId(dto.getInStorageId());
                wmsInnerInventoryService.save(wmsInnerInventory_new);
            }
            // 移入库存日志
            InventoryLogUtil.addLog(wmsInnerInventory,innerJobOrder,wmsInnerJobOrderDet,initQty,storageMaterialDto.getQty(),(byte)3,(byte)1);
        }
        if (jobOrderDetBarcodeList.size() > 0) {
            wmsInnerJobOrderDetBarcodeService.batchSave(jobOrderDetBarcodeList);
        }
        if (htJobOrderDetBarcodes.size() > 0) {
            wmsInnerHtJobOrderDetBarcodeService.batchSave(htJobOrderDetBarcodes);
        }

        return 1;
    }


    /**
     * 库容比例计算
     * @return
     */
    private BigDecimal calculate(BaseStorage baseStorage, Long materialId, String materalCode){

        //获取物料编码并截取前八位数
        if(materalCode.length()<8){
            throw new BizErrorException("物料编码错误，长度小于规定8位，无法匹配库容");
        }
        String strMaterialCode = materalCode.substring(0,8);
        SearchBaseStorageCapacity searchBaseStorageCapacity = new SearchBaseStorageCapacity();
        searchBaseStorageCapacity.setPageSize(99999);
        searchBaseStorageCapacity.setMaterialCodePrefix(strMaterialCode);
        List<BaseStorageCapacity> baseStorageCapacities = baseFeignApi.findList(searchBaseStorageCapacity).getData();

        //查询库位下的所以货品及数量
        List<WmsInnerInventory> wmsInnerInventories = baseFeignApi.wmsList(ControllerUtil.dynamicCondition("storageId", baseStorage.getStorageId())).getData();
        //
        //已知货品A在A仓库可以放10个货品B在A仓库可以放20个 现A仓库有货品A 5个 货品B 4个 求货品A跟货品B还可以在仓库A各存放多少个
        //B货品可存放量 = 5/10*20=10 20-10=10    A货品可存放量 = 4/20*10=2
        //可存放数量
        BigDecimal totalQty = BigDecimal.ZERO;
        if(baseStorageCapacities.size()>0) {
            if (StringUtils.isNotEmpty(baseStorage.getMaterialStoreType())) {

                BigDecimal TypeCapacity = BigDecimal.ZERO;
                byte type = 0;
                switch (baseStorage.getMaterialStoreType()){
                    case 1:
                        if (StringUtils.isEmpty(baseStorageCapacities.get(0).getTypeACapacity())) {
                            throw new BizErrorException("未维护A类容量");
                        }
                        TypeCapacity = baseStorageCapacities.get(0).getTypeACapacity();
                        type = 1;
                        break;
                    case 2:
                        if (StringUtils.isEmpty(baseStorageCapacities.get(0).getTypeBCapacity())) {
                            throw new BizErrorException("未维护B类容量");
                        }
                        TypeCapacity = baseStorageCapacities.get(0).getTypeBCapacity();
                        type = 2;
                        break;
                    case 3:
                        if (StringUtils.isEmpty(baseStorageCapacities.get(0).getTypeCCapacity())) {
                            throw new BizErrorException("未维护C类容量");
                        }
                        TypeCapacity = baseStorageCapacities.get(0).getTypeCCapacity();
                        type = 3;
                        break;
                    case 4:
                        if (StringUtils.isEmpty(baseStorageCapacities.get(0).getTypeDCapacity())) {
                            throw new BizErrorException("未维护D类容量");
                        }
                        TypeCapacity = baseStorageCapacities.get(0).getTypeDCapacity();
                        type = 4;
                        break;
                }
                log.info("======TypeCapacity：" + TypeCapacity);
                if(wmsInnerInventories.size()>0){
                    BigDecimal qty = TypeCapacity;
                    for (WmsInnerInventory wmsInnerInventory : wmsInnerInventories) {
                        log.info("====== wmsInnerInventory.getMaterialId()：" + wmsInnerInventory.getMaterialId() + "====== materialId：" + materialId);
                        if(!Objects.equals(wmsInnerInventory.getMaterialId(), materialId)){

                            //获取物料编码并截取前八位数
                            BaseMaterial baseMaterial = baseFeignApi.materialDetail(wmsInnerInventory.getMaterialId()).getData();
                            if(baseMaterial.getMaterialCode().length()<8){
                                throw new BizErrorException("物料编码错误，长度小于规定8位，无法匹配库容");
                            }
                            String substring = baseMaterial.getMaterialCode().substring(0,8);

                            //转换数量
                            //查询该货品库容
                            SearchBaseStorageCapacity baseStorageCapacity = new SearchBaseStorageCapacity();
                            baseStorageCapacity.setPageSize(99999);
                            baseStorageCapacity.setMaterialCodePrefix(substring);
                            List<BaseStorageCapacity> shiftStorageCapacity = baseFeignApi.findList(baseStorageCapacity).getData();
                            if(shiftStorageCapacity.isEmpty()){
                                break;
                            }
                            BigDecimal shiftCapacity = BigDecimal.ZERO;
                            if(StringUtils.isNotEmpty(shiftStorageCapacity)) {
                                if (type == 1) {
                                    shiftCapacity = shiftStorageCapacity.get(0).getTypeACapacity();
                                } else if (type == 2) {
                                    shiftCapacity = shiftStorageCapacity.get(0).getTypeBCapacity();
                                } else if (type == 3) {
                                    shiftCapacity = shiftStorageCapacity.get(0).getTypeCCapacity();
                                } else if (type == 4) {
                                    shiftCapacity = shiftStorageCapacity.get(0).getTypeDCapacity();
                                }
                            }
                            log.info("=======qty:" + qty + "====== shiftCapacity：" + shiftCapacity + "=====TypeCapacity：" + TypeCapacity);
                            //库存数/转换货品库容*货品库容
                            BigDecimal a = wmsInnerInventory.getPackingQty().divide(shiftCapacity,2).multiply(TypeCapacity);
                            log.info("====== a：" + a);
                            //四舍五入取整数
                            a = a.setScale(0, RoundingMode.HALF_UP);
                            log.info("====== a：" + a);
                            qty = qty.subtract(a);
                            log.info("====== qty：" + qty);
                        }else {
                            qty = qty.subtract(wmsInnerInventory.getPackingQty());
                        }
                    }
                    log.info("====== qty.compareTo(BigDecimal.ZERO)：" + qty.compareTo(BigDecimal.ZERO));
                    if(qty.compareTo(BigDecimal.ZERO)==1){
                        totalQty = qty;
                    }
                    log.info("====== totalQty：" + totalQty);
                }else {
                    totalQty = TypeCapacity;
                }
            }
        }

        return totalQty;
    }

    // 条码清洗，获取厂内码
    public String getFactoryBarcode(String barcode){
        String factoryBarcode = null;
        if (barcode.length() != 23){
            // 判断是否三星客户条码
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("wanbaoCheckBarcode");
            List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            if (!specItems.isEmpty()){
                SearchMesSfcBarcodeProcess searchMesSfcBarcodeProcess = new SearchMesSfcBarcodeProcess();
                searchMesSfcBarcodeProcess.setIsCustomerBarcode(barcode);
                List<MesSfcBarcodeProcessDto> mesSfcBarcodeProcessDtos = sfcFeignApi.findList(searchMesSfcBarcodeProcess).getData();
                if (!mesSfcBarcodeProcessDtos.isEmpty()){
                    factoryBarcode = mesSfcBarcodeProcessDtos.get(0).getBarcode();
                }
            }
        }

        if (factoryBarcode == null) {
            SearchMesSfcKeyPartRelevance searchMesSfcKeyPartRelevance = new SearchMesSfcKeyPartRelevance();
            searchMesSfcKeyPartRelevance.setPartBarcode(barcode);
            List<MesSfcKeyPartRelevanceDto> mesSfcKeyPartRelevanceDtos = sfcFeignApi.findList(searchMesSfcKeyPartRelevance).getData();
            if (!mesSfcKeyPartRelevanceDtos.isEmpty()) {
                factoryBarcode = mesSfcKeyPartRelevanceDtos.get(0).getBarcodeCode();
            }else{
                factoryBarcode = barcode;
            }
        }
        return factoryBarcode;
    }
}
