package com.fantechs.provider.wms.inner.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseWorkerDto;
import com.fantechs.common.base.general.dto.wms.inner.*;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
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
import com.fantechs.provider.wms.inner.mapper.*;
import com.fantechs.provider.wms.inner.service.*;
import com.fantechs.provider.wms.inner.util.InventoryLogUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

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
    private WmsInnerInventoryDetMapper wmsInnerInventoryDetMapper;

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

    @Resource
    private WmsInnerMaterialBarcodeMapper wmsInnerMaterialBarcodeMapper;
    @Resource
    private WmsInnerMaterialBarcodeReOrderMapper wmsInnerMaterialBarcodeReOrderMapper;


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
        if (dto.getJobOrderDetId() == null) {
            map.put("jobStatus", (byte) 1);
        } else {
            map.put("jobStatus", (byte) 2);
            map.put("jobOrderDetId", dto.getJobOrderDetId());
        }
        map.put("lockStatus", (byte) 0);
        map.put("stockLock", (byte) 0);
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

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (dto.getPdaWmsInnerDirectTransferOrderDetDtos() == null && dto.getPdaWmsInnerDirectTransferOrderDetDtos().size() <= 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA5001006);
        }
        WmsInnerJobOrderDet jobOrderDet;
        WmsInnerJobOrder innerJobOrder;
        // 判断是否有作业单
        if (dto.getJobOrderDetId() != null) {
            // 移位作业明细单 变更移位状态
            jobOrderDet = wmsInnerJobOrderDetService.selectByKey(dto.getJobOrderDetId());
            //   jobOrderDet.setLineStatus((byte)2);
            jobOrderDet.setActualQty(jobOrderDet.getActualQty() != null ? jobOrderDet.getActualQty().add(dto.getMaterialQty()) : dto.getMaterialQty());
            if (jobOrderDet.getActualQty().compareTo(jobOrderDet.getDistributionQty()) == 1) {
                throw new BizErrorException(ErrorCodeEnum.PDA5001006.getCode(), "移位数量不能大于计划数量");
            }
            Byte status = 1;
            if (jobOrderDet.getActualQty().compareTo(jobOrderDet.getDistributionQty()) == 0) {
                status = (byte) 2;
            }
            jobOrderDet.setLineStatus(status);
            wmsInnerJobOrderDetService.update(jobOrderDet);

            // 作业单拣货数量+1以及变更单据状态
            innerJobOrder = wmsInnerJobOrderService.selectByKey(dto.getJobOrderId());
            innerJobOrder.setOrderStatus((byte) 4);
            //      innerJobOrder.set(innerJobOrder.getActualQty() != null ? innerJobOrder.getActualQty().add(dto.getMaterialQty()) : dto.getMaterialQty());
            wmsInnerJobOrderMapper.updateByPrimaryKey(innerJobOrder);
        } else {
            // 查询库存信息，同一库位跟同物料有且只有一条数据
            Map<String, Object> map = new HashMap<>();
            map.put("materialId", dto.getMaterialId());
            map.put("storageId", dto.getOutStorageId());
            map.put("jobStatus", (byte) 1);
            map.put("lockStatus", (byte) 0);
            map.put("stockLock", (byte) 0);
            List<WmsInnerInventoryDto> innerInventoryDtos = wmsInnerInventoryService.findList(map);
            if (innerInventoryDtos == null || innerInventoryDtos.size() <= 0) {
                throw new BizErrorException(ErrorCodeEnum.PDA5001009);
            }
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("inventory_status_value");
            List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            List<WmsInnerInventoryDto> dtos = new ArrayList<>();
            if (specItems.size() > 0 && !innerInventoryDtos.isEmpty() && innerInventoryDtos.size() > 0) {
                for (WmsInnerInventoryDto inventoryDto : innerInventoryDtos) {
                    if (StringUtils.isNotEmpty(inventoryDto.getInventoryStatusName()) && inventoryDto.getInventoryStatusName().equals(specItems.get(0).getParaValue())) {
                        dtos.add(inventoryDto);
                    }
                }
                if (dtos.size() <= 0) {
                    throw new BizErrorException(ErrorCodeEnum.PDA5001012.getCode(), "暂无库存或存库状态为待捡，不可操作");
                }
            } else {
                dtos = innerInventoryDtos;
            }
            WmsInnerInventoryDto innerInventoryDto = dtos.get(0);
            if (innerInventoryDto.getPackingQty().compareTo(dto.getMaterialQty()) < -1) {
                throw new BizErrorException(ErrorCodeEnum.PDA5001012);
            }

            innerJobOrder = new WmsInnerJobOrder();
            SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
            searchBaseStorage.setStorageId(dto.getOutStorageId());
            List<BaseStorage> baseStorage = baseFeignApi.findList(searchBaseStorage).getData();
            if (StringUtils.isEmpty(baseStorage))
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "未查询到移出库位");
            if (dto.getJobOrderId() != null) {
                // 更新移位单
                innerJobOrder = wmsInnerJobOrderService.selectByKey(dto.getJobOrderId());
                innerJobOrder.setOrderStatus((byte) 4);
                wmsInnerJobOrderMapper.updateByPrimaryKey(innerJobOrder);
            } else {
                // 创建移位单
                innerJobOrder.setWarehouseId(baseStorage.get(0).getWarehouseId());
                innerJobOrder.setJobOrderCode(CodeUtils.getId("INNER-SSO"));
                innerJobOrder.setJobOrderType((byte) 3);
                innerJobOrder.setOrderStatus((byte) 4);
                innerJobOrder.setStatus((byte) 1);
                innerJobOrder.setOrgId(user.getOrganizationId());
                innerJobOrder.setCreateTime(new Date());
                innerJobOrder.setCreateUserId(user.getUserId());
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
            wmsInnerJobOrderDet.setOutStorageId(baseStorage.get(0).getStorageId());
            wmsInnerJobOrderDet.setInStorageId(dto.getInStorageId());
            wmsInnerJobOrderDet.setMaterialId(innerInventoryDto.getMaterialId());
            wmsInnerJobOrderDet.setPlanQty(dto.getMaterialQty());
            wmsInnerJobOrderDet.setDistributionQty(dto.getMaterialQty());
            wmsInnerJobOrderDet.setActualQty(dto.getMaterialQty()); //已移位数量
            wmsInnerJobOrderDet.setProductionDate(innerInventoryDto.getProductionDate());
            wmsInnerJobOrderDet.setInventoryStatusId(innerInventoryDto.getInventoryStatusId());
            wmsInnerJobOrderDet.setWorkStartTime(new Date());
            wmsInnerJobOrderDet.setWorkEndTime(new Date());
            wmsInnerJobOrderDet.setBatchCode(innerInventoryDto.getBatchCode());
            wmsInnerJobOrderDet.setStatus((byte) 1);
            wmsInnerJobOrderDet.setRemark(innerInventoryDto.getRemark());
            wmsInnerJobOrderDet.setOrgId(user.getOrganizationId());
            wmsInnerJobOrderDet.setCreateTime(new Date());
            wmsInnerJobOrderDet.setCreateUserId(user.getUserId());
            wmsInnerJobOrderDet.setIsDelete((byte) 1);
            wmsInnerJobOrderDet.setLineStatus((byte) 2);
            wmsInnerJobOrderDetMapper.insertUseGeneratedKeys(wmsInnerJobOrderDet);
            WmsInnerHtJobOrderDet innerHtJobOrderDet = new WmsInnerHtJobOrderDet();
            BeanUtil.copyProperties(wmsInnerJobOrderDet, innerHtJobOrderDet);
            wmsInnerHtJobOrderDetService.save(innerHtJobOrderDet);
            jobOrderDet = wmsInnerJobOrderDet;
            // 新增待出库存信息


            WmsInnerInventory newInnerInventory = new WmsInnerInventory();
            BeanUtil.copyProperties(innerInventoryDto, newInnerInventory);
            newInnerInventory.setPackingQty(dto.getMaterialQty());
            newInnerInventory.setParentInventoryId(innerInventoryDto.getInventoryId());
            newInnerInventory.setRelevanceOrderCode(innerJobOrder.getJobOrderCode());
            newInnerInventory.setJobStatus((byte) 2);
            newInnerInventory.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
            newInnerInventory.setOrgId(user.getOrganizationId());
            newInnerInventory.setCreateTime(new Date());
            newInnerInventory.setCreateUserId(user.getUserId());
            wmsInnerInventoryService.save(newInnerInventory);

            // 变更减少原库存
            innerInventoryDto.setPackingQty(innerInventoryDto.getPackingQty().subtract(dto.getMaterialQty()));
            wmsInnerInventoryService.update(innerInventoryDto);

        }

        //保存条码
        if (!dto.getPdaWmsInnerDirectTransferOrderDetDtos().isEmpty()) {
            List<WmsInnerJobOrderDetBarcode> jobOrderDetBarcodeList = new ArrayList<>();
            List<WmsInnerMaterialBarcodeReOrder> wmsInnerMaterialBarcodeReOrders = new ArrayList<>();

            for (PDAWmsInnerDirectTransferOrderDetDto barcode : dto.getPdaWmsInnerDirectTransferOrderDetDtos()) {

                //查询条码是否存在
                Example example2 = new Example(WmsInnerMaterialBarcode.class);
                Example.Criteria criteria2 = example2.createCriteria();
                criteria2.andEqualTo("materialId", dto.getMaterialId());
                criteria2.andEqualTo("materialBarcodeId", barcode.getMaterialBarcodeId());
                List<WmsInnerMaterialBarcode> wmsInnerMaterialBarcodes = wmsInnerMaterialBarcodeMapper.selectByExample(example2);
                if (StringUtils.isEmpty(wmsInnerMaterialBarcodes)) {
                    throw new BizErrorException(ErrorCodeEnum.PDA5001004);
                }

                //查询条码是否在库,更新库存明细状态
                Example example3 = new Example(WmsInnerInventoryDet.class);
                Example.Criteria criteria3 = example3.createCriteria()
                        .andEqualTo("storageId", dto.getOutStorageId())
                        .andEqualTo("materialBarcodeId", barcode.getMaterialBarcodeId())
                        .andEqualTo("barcodeStatus", 1);
                List<WmsInnerInventoryDet> wmsInnerInventoryDets = wmsInnerInventoryDetMapper.selectByExample(example3);
                if (StringUtils.isEmpty(wmsInnerInventoryDets)) {
                    throw new BizErrorException(ErrorCodeEnum.PDA5001009);
                }
                WmsInnerInventoryDet det = wmsInnerInventoryDets.get(0);
                det.setBarcodeStatus((byte) 2);
                wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(det);

                // 创建条码移位单明细关系
                WmsInnerMaterialBarcodeReOrder wmsInnerMaterialBarcodeReOrder = new WmsInnerMaterialBarcodeReOrder();
                wmsInnerMaterialBarcodeReOrder.setOrderTypeCode("INNER-SSO");
                wmsInnerMaterialBarcodeReOrder.setOrderCode(innerJobOrder.getJobOrderCode());
                wmsInnerMaterialBarcodeReOrder.setOrderId(innerJobOrder.getJobOrderId());
                wmsInnerMaterialBarcodeReOrder.setOrderDetId(jobOrderDet.getJobOrderDetId());
                wmsInnerMaterialBarcodeReOrder.setMaterialBarcodeId(wmsInnerMaterialBarcodes.get(0).getMaterialBarcodeId());
                wmsInnerMaterialBarcodeReOrder.setStatus((byte) 1);
                wmsInnerMaterialBarcodeReOrder.setOrgId(user.getOrganizationId());
                wmsInnerMaterialBarcodeReOrder.setCreateUserId(user.getUserId());
                wmsInnerMaterialBarcodeReOrder.setCreateTime(new Date());
                wmsInnerMaterialBarcodeReOrder.setModifiedUserId(user.getUserId());
                wmsInnerMaterialBarcodeReOrder.setModifiedTime(new Date());
                wmsInnerMaterialBarcodeReOrders.add(wmsInnerMaterialBarcodeReOrder);
            }
            if (jobOrderDetBarcodeList.size() > 0) {
                wmsInnerJobOrderDetBarcodeService.batchSave(jobOrderDetBarcodeList);
            }
            if (wmsInnerMaterialBarcodeReOrders.size() > 0) {
                wmsInnerMaterialBarcodeReOrderMapper.insertList(wmsInnerMaterialBarcodeReOrders);
            }
        }

        return dto.getJobOrderId().toString();
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int saveJobOrder(SaveShiftJobOrderDto dto) {
        SysUser sysUser = currentUser();

        WmsInnerJobOrderDet wmsInnerJobOrderDet = wmsInnerJobOrderDetMapper.selectByPrimaryKey(dto.getJobOrderDetId());
        if (StringUtils.isEmpty(wmsInnerJobOrderDet))
            throw new BizErrorException("未查询到对应的移位单明细");
        if (StringUtils.isEmpty(wmsInnerJobOrderDet.getDistributionQty()) || wmsInnerJobOrderDet.getDistributionQty().compareTo(BigDecimal.ZERO) < 0) {
            throw new BizErrorException("上架数量不能小于1");
        }
        wmsInnerJobOrderDet.setActualQty(wmsInnerJobOrderDet.getDistributionQty());
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
                .modifiedTime(new Date())
                .lineStatus((byte) 3)
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
//                .andEqualTo("jobOrderDetId", oldDto.getJobOrderDetId())
                .andEqualTo("jobStatus", (byte) 2)
                .andEqualTo("stockLock", 0)
                .andEqualTo("lockStatus", 0);
        WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
        if (StringUtils.isEmpty(wmsInnerInventory))
            throw new BizErrorException("未查询到移出库位");
        example.clear();
        Example.Criteria criteria1 = example.createCriteria().andEqualTo("materialId", oldDto.getMaterialId())
//                .andEqualTo("warehouseId", oldDto.getWarehouseId())
                .andEqualTo("storageId", baseStorage.getStorageId())
                .andEqualTo("jobStatus", (byte) 1)
                .andEqualTo("stockLock", 0)
                .andEqualTo("lockStatus", 0)
                .andGreaterThan("packingQty", 0);
        if (StringUtils.isNotEmpty(wmsInnerInventory)) {
            criteria1.andEqualTo("inventoryStatusId", wmsInnerInventory.getInventoryStatusId());
        }
        WmsInnerInventory wmsInnerInventory_old = wmsInnerInventoryMapper.selectOneByExample(example);
        //获取初期数量
        WmsInnerInventory innerInventory = wmsInnerInventoryMapper.selectByPrimaryKey(wmsInnerInventory.getParentInventoryId());
        BigDecimal initQty = BigDecimal.ZERO;
        if (StringUtils.isNotEmpty(innerInventory)) {
            if (StringUtils.isEmpty(innerInventory.getPackingQty())) {
                innerInventory.setPackingQty(BigDecimal.ZERO);
            }
            initQty = innerInventory.getPackingQty().add(wmsInnerInventory.getPackingQty());
        }
        oldDto.setInStorageId(baseStorage.getStorageId());
        if (StringUtils.isEmpty(wmsInnerInventory_old)) {
            if (StringUtils.isEmpty(wmsInnerInventory)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            wmsInnerInventory.setJobStatus((byte) 1);
            wmsInnerInventory.setStorageId(baseStorage.getStorageId());
            wmsInnerInventoryService.updateByPrimaryKeySelective(wmsInnerInventory);


            //库存日志
            InventoryLogUtil.addLog(wmsInnerInventory, wmsInnerJobOrderDto, oldDto, initQty, wmsInnerInventory.getPackingQty(), (byte) 3, (byte) 2);
            InventoryLogUtil.addLog(wmsInnerInventory, wmsInnerJobOrderDto, oldDto, BigDecimal.ZERO, wmsInnerInventory.getPackingQty(), (byte) 3, (byte) 1);
        } else {

            //库存日志
            InventoryLogUtil.addLog(wmsInnerInventory_old, wmsInnerJobOrderDto, oldDto, initQty, wmsInnerInventory.getPackingQty(), (byte) 3, (byte) 2);
            InventoryLogUtil.addLog(wmsInnerInventory_old, wmsInnerJobOrderDto, oldDto, wmsInnerInventory_old.getPackingQty(), wmsInnerInventory.getPackingQty(), (byte) 3, (byte) 1);

            wmsInnerInventory_old.setPackingQty(wmsInnerInventory_old.getPackingQty() != null ? wmsInnerInventory_old.getPackingQty().add(wmsInnerJobOrderDet.getActualQty()) : wmsInnerInventory.getPackingQty());
            wmsInnerInventory_old.setRelevanceOrderCode(wmsInnerInventory.getRelevanceOrderCode());
            wmsInnerInventoryService.updateByPrimaryKeySelective(wmsInnerInventory_old);
            wmsInnerInventory.setPackingQty(BigDecimal.ZERO);
            wmsInnerInventoryService.updateByPrimaryKeySelective(wmsInnerInventory);
            //wmsInnerInventoryService.delete(wmsInnerInventory);
        }

        //更新库存明细、状态
        Example example1 = new Example(WmsInnerMaterialBarcodeReOrderDto.class);
        example1.createCriteria().andEqualTo("jobOrderDetId", dto.getJobOrderDetId());
        List<WmsInnerMaterialBarcodeReOrder> wmsInnerMaterialBarcodeReOrders = wmsInnerMaterialBarcodeReOrderMapper.selectByExample(example1);
        if (StringUtils.isNotEmpty(wmsInnerMaterialBarcodeReOrders)) {
            for (WmsInnerMaterialBarcodeReOrder wmsInnerMaterialBarcodeReOrder : wmsInnerMaterialBarcodeReOrders) {
                Map<String, Object> map = new HashMap<>();
                map.put("storageId", wmsInnerJobOrderDet.getOutStorageId());
                map.put("materialBarcodeId", wmsInnerMaterialBarcodeReOrder.getMaterialBarcodeId());
                List<WmsInnerInventoryDetDto> inventoryDetDtos = wmsInnerInventoryDetService.findList(map);
                if (inventoryDetDtos.isEmpty()) {
                    throw new BizErrorException(ErrorCodeEnum.PDA5001004);
                }
                WmsInnerInventoryDetDto inventoryDetDto = inventoryDetDtos.get(0);
                inventoryDetDto.setStorageId(dto.getStorageId());
                inventoryDetDto.setBarcodeType((byte) 1);
                wmsInnerInventoryDetService.update(inventoryDetDto);
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("storageId", wmsInnerJobOrderDet.getOutStorageId());
        List<WmsInnerInventoryDetDto> inventoryDetDtos = wmsInnerInventoryDetService.findList(map);
        if (inventoryDetDtos.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.PDA5001004);
        }

        WmsInnerInventoryDetDto inventoryDetDto = inventoryDetDtos.get(0);
        inventoryDetDto.setStorageId(dto.getStorageId());
        wmsInnerInventoryDetService.update(inventoryDetDto);

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
            if (StringUtils.isEmpty(wmsInnerJobOrderDto.getWorkStartTime())) {
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


    /**
     * 获取当前登录用户
     *
     * @return
     */
    private SysUser currentUser() {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        return user;
    }
}
