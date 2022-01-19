package com.fantechs.provider.wms.inner.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONArray;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDetDto;
import com.fantechs.common.base.general.dto.om.OmTransferOrderDetDto;
import com.fantechs.common.base.general.dto.om.SearchOmSalesOrderDetDto;
import com.fantechs.common.base.general.dto.wms.inner.*;
import com.fantechs.common.base.general.dto.wms.inner.imports.WmsInnerJobOrderImport;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehouse;
import com.fantechs.common.base.general.entity.om.search.SearchOmTransferOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.*;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrderDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDespatchOrder;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDespatchOrderReJo;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.guest.eng.EngFeignApi;
import com.fantechs.provider.api.qms.OMFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.out.OutFeignApi;
import com.fantechs.provider.wms.inner.mapper.*;
import com.fantechs.provider.wms.inner.service.PickingOrderService;
import com.fantechs.provider.wms.inner.service.WmsInnerInventoryService;
import com.fantechs.provider.wms.inner.service.WmsInnerJobOrderService;
import com.fantechs.provider.wms.inner.util.InBarcodeUtil;
import com.fantechs.provider.wms.inner.util.InventoryLogUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author mr.lei
 * @Date 2021/5/10
 */
@Service
public class PickingOrderServiceImpl implements PickingOrderService {

    @Resource
    private WmsInnerJobOrderMapper wmsInnerJobOrderMapper;
    @Resource
    private WmsInnerJobOrderService wmsInnerJobOrderService;
    @Resource
    private WmsInnerJobOrderDetMapper wmsInnerJobOrderDetMapper;
    @Resource
    private WmsInnerInventoryMapper wmsInnerInventoryMapper;
    @Resource
    private WmsInnerInventoryService wmsInnerInventoryService;
    @Resource
    private OutFeignApi outFeignApi;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private OMFeignApi omFeignApi;
    @Resource
    private WmsInnerInventoryDetMapper wmsInnerInventoryDetMapper;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private EngFeignApi engFeignApi;
    @Resource
    private WmsInnerMaterialBarcodeMapper wmsInnerMaterialBarcodeMapper;
    @Resource
    private WmsInnerMaterialBarcodeReOrderMapper wmsInnerMaterialBarcodeReOrderMapper;

    private String REDIS_KEY = "PICKINGID:";

    @Override
    public WmsInnerInventoryDetDto scan(Long storageId, Long materialId, String barcode) {
        SysUser sysUser = currentUser();
        Map<String,Object> barcodeMap = new HashMap<>();
        barcodeMap.put("barcode",barcode);
        barcodeMap.put("barcodeType",1);
        barcodeMap.put("orgId",sysUser.getOrganizationId());
        List<WmsInnerMaterialBarcodeDto> barcodeList = wmsInnerMaterialBarcodeMapper.findList(barcodeMap);
        Long barcodeId = 0L;
        Byte type = 1;

        //判断是扫描是否是sn码
        if (StringUtils.isEmpty(barcodeList)) {
            barcodeMap.put("barcode",null);
            barcodeMap.put("colorBoxCode",barcode);
            barcodeMap.put("barcodeType",2);
            barcodeList = wmsInnerMaterialBarcodeMapper.findList(barcodeMap);
            //判断是扫描是否是彩盒码
            if (StringUtils.isEmpty(barcodeList)) {
                barcodeMap.put("colorBoxCode",null);
                barcodeMap.put("cartonCode",barcode);
                barcodeMap.put("barcodeType",3);
                barcodeList = wmsInnerMaterialBarcodeMapper.findList(barcodeMap);
                //判断是扫描是否是箱码
                if (StringUtils.isEmpty(barcodeList)) {
                    barcodeMap.put("cartonCode",null);
                    barcodeMap.put("palletCode",barcode);
                    barcodeMap.put("barcodeType",4);
                    barcodeList = wmsInnerMaterialBarcodeMapper.findList(barcodeMap);
                    //判断是扫描是否是栈板码
                    if (StringUtils.isEmpty(barcodeList)) {
                        throw new BizErrorException("当前条码不存在");
                    }else {
                        barcodeId = barcodeList.get(0).getMaterialBarcodeId();
                        type = 4;
                        barcodeMap.put("barcodeType",1);
                        barcodeList = wmsInnerMaterialBarcodeMapper.findList(barcodeMap);
                    }
                }else {
                    barcodeId = barcodeList.get(0).getMaterialBarcodeId();
                    type = 3;
                    barcodeMap.put("barcodeType",1);
                    barcodeList = wmsInnerMaterialBarcodeMapper.findList(barcodeMap);
                }
            }else {
                barcodeId = barcodeList.get(0).getMaterialBarcodeId();
                type = 2;
                barcodeMap.put("barcodeType",1);
                barcodeList = wmsInnerMaterialBarcodeMapper.findList(barcodeMap);
            }
        }else {
            barcodeId = barcodeList.get(0).getMaterialBarcodeId();
        }
        List barcodeIds = new ArrayList();
        barcodeIds.add(barcodeId);
        Map<String,Object> map = new HashMap<>();
        map.put("orgId",sysUser.getOrganizationId());
        map.put("storageId",storageId);
        map.put("materialId",materialId);
        map.put("materialBarcodeIdList",barcodeIds);
        map.put("barcodeStatus",1);
        List<WmsInnerInventoryDetDto> list = wmsInnerInventoryDetMapper.findList(map);
        if (StringUtils.isEmpty(list)) {
            throw new BizErrorException("库存未找到当前条码数据，或该条码已拣货");
        }
        BigDecimal materialTotalQty = new BigDecimal(0);
        if (StringUtils.isNotEmpty(barcodeList)) {
            for (WmsInnerMaterialBarcodeDto wmsInnerMaterialBarcodeDto : barcodeList) {
                materialTotalQty = materialTotalQty.add(wmsInnerMaterialBarcodeDto.getMaterialQty());
            }
        }
        list.get(0).setMaterialTotalQty(materialTotalQty);
        list.get(0).setBarcodeType(type);
        return list.get(0);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public List<WmsInnerJobOrderDetDto> pdaSave(List<WmsInnerPdaInventoryDetDto> list) {
        SysUser sysUser = currentUser();
        List<WmsInnerJobOrderDetDto> jobOrderDetList = new ArrayList<>();
        if (StringUtils.isNotEmpty(list)) {
            Map<Long, List<WmsInnerPdaInventoryDetDto>> collect = list.stream().collect(Collectors.groupingBy(WmsInnerPdaInventoryDetDto::getWarehouseId));
            if (collect.size() > 1) {
                throw new BizErrorException("当前扫描条码存在不在同一个仓库的条码");
            }
            Map<Long, Map<Long, List<WmsInnerPdaInventoryDetDto>>> detMap = list.stream().collect(Collectors.groupingBy(WmsInnerPdaInventoryDetDto::getStorageId, Collectors.groupingBy(WmsInnerPdaInventoryDetDto::getMaterialId)));

            WmsInnerJobOrder wmsInnerJobOrder = new WmsInnerJobOrder();

            if (StringUtils.isNotEmpty(list.get(0).getJobOrderId())) {
                Example example = new Example(WmsInnerJobOrder.class);
                example.createCriteria().andEqualTo("jobOrderId",list.get(0).getJobOrderId());
                wmsInnerJobOrder = wmsInnerJobOrderMapper.selectOneByExample(example);
                wmsInnerJobOrder.setWorkEndtTime(new Date());
            }else {
                wmsInnerJobOrder.setJobOrderCode(CodeUtils.getId("PICK-"));
                wmsInnerJobOrder.setCreateTime(new Date());
                wmsInnerJobOrder.setCreateUserId(sysUser.getUserId());
                wmsInnerJobOrder.setModifiedTime(new Date());
                wmsInnerJobOrder.setModifiedUserId(sysUser.getUserId());
                wmsInnerJobOrder.setOrgId(sysUser.getOrganizationId());
                wmsInnerJobOrder.setIsDelete((byte) 1);
                wmsInnerJobOrder.setOrderStatus((byte) 5);
                wmsInnerJobOrder.setWarehouseId(list.get(0).getWarehouseId());
                wmsInnerJobOrder.setWorkerId(sysUser.getUserId());
                wmsInnerJobOrder.setJobOrderType((byte) 2);
                wmsInnerJobOrder.setWorkStartTime(new Date());
                wmsInnerJobOrder.setWorkEndtTime(new Date());

                wmsInnerJobOrderMapper.insertUseGeneratedKeys(wmsInnerJobOrder);
            }


            List<WmsInnerInventoryDet> wmsInnerInventoryDetList = new ArrayList<>();

            SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
            searchBaseStorage.setStorageType((byte) 3);
            searchBaseStorage.setWarehouseId(list.get(0).getWarehouseId());
            List<BaseStorage> storageList = baseFeignApi.findList(searchBaseStorage).getData();
            if (StringUtils.isEmpty(storageList)) {
                throw new BizErrorException("当前扫描条码所在仓库未找到发货库位");
            }

            Map<String, Object> map = new HashMap<>();
            List<WmsInnerMaterialBarcodeReOrder> barcodeReOrderList = new ArrayList<>();
            for (Long storageId : detMap.keySet()) {
                for (Long materialId : detMap.get(storageId).keySet()) {
                    WmsInnerJobOrderDetDto wmsInnerJobOrderDet = new WmsInnerJobOrderDetDto();
                    wmsInnerJobOrderDet.setJobOrderId(wmsInnerJobOrder.getJobOrderId());
                    wmsInnerJobOrderDet.setOutStorageId(storageId);
                    wmsInnerJobOrderDet.setMaterialId(materialId);
                    wmsInnerJobOrderDet.setInStorageId(storageList.get(0).getStorageId());
                    wmsInnerJobOrderDet.setWorkStartTime(new Date());
                    wmsInnerJobOrderDet.setWorkEndTime(new Date());
                    wmsInnerJobOrderDet.setLineStatus((byte) 3);
                    wmsInnerJobOrderDet.setCreateTime(new Date());
                    wmsInnerJobOrderDet.setCreateUserId(sysUser.getUserId());
                    wmsInnerJobOrderDet.setModifiedTime(new Date());
                    wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
                    wmsInnerJobOrderDet.setOrgId(sysUser.getOrganizationId());
                    wmsInnerJobOrderDet.setIsDelete((byte) 1);
                    wmsInnerJobOrderDetMapper.insertUseGeneratedKeys(wmsInnerJobOrderDet);
                    BigDecimal planQty = new BigDecimal(0);


                    List<WmsInnerPdaInventoryDetDto> wmsInnerPdaInventoryDetDtos = detMap.get(storageId).get(materialId);
                    for (WmsInnerPdaInventoryDetDto wmsInnerPdaInventoryDetDto : wmsInnerPdaInventoryDetDtos) {
                        WmsInnerInventoryDetDto scan = this.scan(null, null, wmsInnerPdaInventoryDetDto.getBarcode());
                        if (scan.getBarcodeType() == 1) {
                            WmsInnerInventoryDet wmsInnerInventoryDet = new WmsInnerInventoryDet();
                            wmsInnerInventoryDet.setInventoryDetId(scan.getInventoryDetId());
                            wmsInnerInventoryDet.setStorageId(storageList.get(0).getStorageId());
                            wmsInnerInventoryDet.setBarcodeStatus((byte) 2);
                            planQty = planQty.add(scan.getMaterialQty());
                            wmsInnerInventoryDetList.add(wmsInnerInventoryDet);


                            WmsInnerMaterialBarcodeReOrder wmsInnerMaterialBarcodeReOrder = new WmsInnerMaterialBarcodeReOrder();
                            wmsInnerMaterialBarcodeReOrder.setMaterialBarcodeId(scan.getMaterialBarcodeId());
                            wmsInnerMaterialBarcodeReOrder.setOrderId(wmsInnerJobOrder.getJobOrderId());
                            wmsInnerMaterialBarcodeReOrder.setOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
                            wmsInnerMaterialBarcodeReOrder.setOrderTypeCode("OUT-IWK");
                            wmsInnerMaterialBarcodeReOrder.setOrderCode(wmsInnerJobOrder.getJobOrderCode());
                            wmsInnerMaterialBarcodeReOrder.setScanStatus((byte) 3);
                            barcodeReOrderList.add(wmsInnerMaterialBarcodeReOrder);

                        }else {
                            List<WmsInnerMaterialBarcodeDto> sn = getSn(wmsInnerPdaInventoryDetDto.getBarcodeType(), wmsInnerPdaInventoryDetDto.getBarcode());
                            List<Long> materialBarcodeIdList = new ArrayList<>();
                            for (WmsInnerMaterialBarcodeDto wmsInnerMaterialBarcodeDto : sn) {
                                materialBarcodeIdList.add(wmsInnerMaterialBarcodeDto.getMaterialBarcodeId());
                            }
                            map.put("materialBarcodeIdList",materialBarcodeIdList);
                            List<WmsInnerInventoryDetDto> innerInventoryDetList = wmsInnerInventoryDetMapper.findList(map);
                            map.put("materialBarcodeIdList",null);
                            for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : innerInventoryDetList) {
                                WmsInnerInventoryDet wmsInnerInventoryDet = new WmsInnerInventoryDet();
                                wmsInnerInventoryDet.setInventoryDetId(wmsInnerInventoryDetDto.getInventoryDetId());
                                wmsInnerInventoryDet.setStorageId(storageList.get(0).getStorageId());
                                wmsInnerInventoryDet.setBarcodeStatus((byte) 2);
                                planQty = planQty.add(wmsInnerInventoryDetDto.getMaterialQty());
                                wmsInnerInventoryDetList.add(wmsInnerInventoryDet);

                                WmsInnerMaterialBarcodeReOrder wmsInnerMaterialBarcodeReOrder = new WmsInnerMaterialBarcodeReOrder();
                                wmsInnerMaterialBarcodeReOrder.setMaterialBarcodeId(wmsInnerInventoryDetDto.getMaterialBarcodeId());
                                wmsInnerMaterialBarcodeReOrder.setOrderId(wmsInnerJobOrder.getJobOrderId());
                                wmsInnerMaterialBarcodeReOrder.setOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
                                wmsInnerMaterialBarcodeReOrder.setOrderTypeCode("OUT-IWK");
                                wmsInnerMaterialBarcodeReOrder.setOrderCode(wmsInnerJobOrder.getJobOrderCode());
                                wmsInnerMaterialBarcodeReOrder.setScanStatus((byte) 3);
                                barcodeReOrderList.add(wmsInnerMaterialBarcodeReOrder);
                            }
                        }

                    }

                    //扣减存货库位库存
                    map.put("storageId",storageId);
                    map.put("materialId",materialId);
                    map.put("isStorage",1);
                    List<WmsInnerInventoryDto> inventoryList = wmsInnerInventoryMapper.findList(map);
                    if (StringUtils.isEmpty(inventoryList) || inventoryList.get(0).getPackingQty().compareTo(planQty) == -1) {
                        throw new BizErrorException("库存不足");
                    }
                    //存货库位
                    WmsInnerInventoryDto wmsInnerInventoryDto = inventoryList.get(0);
                    wmsInnerInventoryDto.setPackingQty(wmsInnerInventoryDto.getPackingQty().subtract(planQty));
                    //添加发货库位库存
                    map.put("storageId",storageList.get(0).getStorageId());
                    inventoryList = wmsInnerInventoryMapper.findList(map);
                    //没有就新增发货库位，有就累加
                    if (StringUtils.isNotEmpty(inventoryList)) {
                        WmsInnerInventoryDto deliverInventory = inventoryList.get(0);
                        deliverInventory.setPackingQty(deliverInventory.getPackingQty().add(planQty));
                        wmsInnerInventoryService.update(deliverInventory);
                    }else {
                        WmsInnerInventory wmsInnerInventory = new WmsInnerInventory();
                        BeanUtil.copyProperties(wmsInnerInventoryDto,wmsInnerInventory);
                        wmsInnerInventory.setRelevanceOrderCode(wmsInnerJobOrder.getJobOrderCode());
                        wmsInnerInventory.setPackingQty(planQty);
                        wmsInnerInventory.setJobStatus((byte) 1);
                        wmsInnerInventory.setJobOrderDetId(null);
                        wmsInnerInventory.setStorageId(storageList.get(0).getStorageId());
                        wmsInnerInventoryService.save(wmsInnerInventory);
                    }
                    wmsInnerJobOrderDet.setMaterialCode(wmsInnerInventoryDto.getMaterialCode());
                    wmsInnerJobOrderDet.setMaterialDesc(wmsInnerInventoryDto.getMaterialDesc());
                    wmsInnerJobOrderDet.setOutStorageCode(wmsInnerInventoryDto.getStorageCode());
                    wmsInnerJobOrderDet.setPlanQty(planQty);
                    wmsInnerJobOrderDet.setDistributionQty(planQty);
                    wmsInnerJobOrderDet.setActualQty(planQty);
                    wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInnerJobOrderDet);

                    jobOrderDetList.add(wmsInnerJobOrderDet);
                }
            }
            if (StringUtils.isNotEmpty(wmsInnerInventoryDetList)) {
                wmsInnerInventoryDetMapper.updateStroage(wmsInnerInventoryDetList);
            }
            if (StringUtils.isNotEmpty(barcodeReOrderList)) {
                wmsInnerMaterialBarcodeReOrderMapper.insertList(barcodeReOrderList);
            }
        }

        return jobOrderDetList;
    }

    private List<WmsInnerMaterialBarcodeDto> getSn(Byte barcodeType, String barcode) {
        Map<String,Object> barcodeMap = new HashMap<>();
        if (barcodeType == 1) {
            barcodeMap.put("barcodeType",1);
            barcodeMap.put("barcode",barcode);
            return wmsInnerMaterialBarcodeMapper.findList(barcodeMap);
        }else if (barcodeType == 2) {
            barcodeMap.put("barcodeType",2);
            barcodeMap.put("colorBoxCode",barcode);
            return wmsInnerMaterialBarcodeMapper.findList(barcodeMap);
        }else if (barcodeType == 3) {
            barcodeMap.put("barcodeType",3);
            barcodeMap.put("cartonCode",barcode);
            return wmsInnerMaterialBarcodeMapper.findList(barcodeMap);
        }else if (barcodeType == 4) {
            barcodeMap.put("barcodeType",4);
            barcodeMap.put("palletCode",barcode);
            return wmsInnerMaterialBarcodeMapper.findList(barcodeMap);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int pdaSubmit(WmsInnerPdaJobOrderDet wmsInnerPdaJobOrderDet) {
        SysUser sysUser = currentUser();

        Example jobDetExample = new Example(WmsInnerJobOrderDet.class);
        jobDetExample.createCriteria().andEqualTo("jobOrderDetId",wmsInnerPdaJobOrderDet.getJobOrderDetId());
        WmsInnerJobOrderDet wmsInnerJobOrderDet = wmsInnerJobOrderDetMapper.selectOneByExample(jobDetExample);
        wmsInnerJobOrderDet.setLineStatus((byte) 3);
        jobDetExample.clear();

        wmsInnerPdaJobOrderDet.setInStorageId(wmsInnerJobOrderDet.getInStorageId());
        wmsInnerPdaJobOrderDet.setOutStorageId(wmsInnerJobOrderDet.getOutStorageId());


        Example jobExample = new Example(WmsInnerJobOrder.class);
        jobExample.createCriteria().andEqualTo("jobOrderId",wmsInnerJobOrderDet.getJobOrderId());
        WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectOneByExample(jobExample);

        //判断拣货数量是否等于分配数量
        if (wmsInnerJobOrderDet.getDistributionQty().compareTo(wmsInnerPdaJobOrderDet.getActualQty()) == 0) {

            log(wmsInnerPdaJobOrderDet,wmsInnerJobOrder,wmsInnerJobOrderDet,1);

            wmsInnerJobOrderDet.setLineStatus((byte) 3);
            wmsInnerJobOrderDet.setActualQty(wmsInnerPdaJobOrderDet.getActualQty());
            wmsInnerJobOrderDet.setWorkStartTime(new Date());
            wmsInnerJobOrderDet.setWorkEndTime(new Date());
            wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInnerJobOrderDet);

        }else if (wmsInnerJobOrderDet.getDistributionQty().compareTo(wmsInnerPdaJobOrderDet.getActualQty()) == 1) {
            //拣货数量小于分配数量
            WmsInnerJobOrderDet wms = new WmsInnerJobOrderDet();
            BeanUtil.copyProperties(wmsInnerJobOrderDet,wms);
            wms.setJobOrderDetId(null);
            wms.setPlanQty(wmsInnerPdaJobOrderDet.getActualQty());
            wms.setDistributionQty(wmsInnerPdaJobOrderDet.getActualQty());
            wms.setActualQty(wmsInnerPdaJobOrderDet.getActualQty());
            wms.setLineStatus((byte)3);
            wms.setWorkStartTime(new Date());
            wms.setWorkEndTime(new Date());
            wmsInnerJobOrderDetMapper.insertUseGeneratedKeys(wms);


            wmsInnerJobOrderDet.setDistributionQty(wmsInnerJobOrderDet.getDistributionQty().subtract(wmsInnerPdaJobOrderDet.getActualQty()));
            wmsInnerJobOrderDet.setPlanQty(wmsInnerJobOrderDet.getPlanQty().subtract(wmsInnerPdaJobOrderDet.getActualQty()));
            wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInnerJobOrderDet.setModifiedTime(new Date());
            wmsInnerJobOrderDet.setLineStatus((byte) 2);
            wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInnerJobOrderDet);

            log(wmsInnerPdaJobOrderDet,wmsInnerJobOrder,wmsInnerJobOrderDet,2);
        }else if (wmsInnerJobOrderDet.getDistributionQty().compareTo(wmsInnerPdaJobOrderDet.getActualQty()) == -1)  {
            throw new BizErrorException("拣货数量大于实际分配数量");
        }

        if (StringUtils.isNotEmpty(wmsInnerPdaJobOrderDet.getInventoryDetList())) {

            List<WmsInnerMaterialBarcodeReOrder> barcodeReOrderList = new ArrayList<>();
            List<WmsInnerInventoryDet> inventoryDetList = new ArrayList<>();
//            Example example = new Example(WmsInnerMaterialBarcode.class);
            Example inventoryDetExample = new Example(WmsInnerInventoryDet.class);
            Map<String,Object> map = new HashMap<>();
            for (WmsInnerPdaInventoryDetDto wmsInnerPdaInventoryDetDto : wmsInnerPdaJobOrderDet.getInventoryDetList()) {

                if (StringUtils.isNotEmpty(wmsInnerPdaInventoryDetDto.getInventoryDetId())) {
                    map.put("barcode",wmsInnerPdaInventoryDetDto.getBarcode());
                    map.put("orgId",sysUser.getOrganizationId());
                    List<WmsInnerInventoryDetDto> list = wmsInnerInventoryDetMapper.findList(map);
                    if (StringUtils.isEmpty(list)) {
                        throw new BizErrorException("条码不存在库存内");
                    }else if (!list.get(0).getStorageId().equals(wmsInnerJobOrderDet.getOutStorageId())
                            || !list.get(0).getMaterialId().equals(wmsInnerJobOrderDet.getMaterialId())
                            ) {
                        throw new BizErrorException("该条码不是分配库位上的物料条码，或者该条码物料不是物料");
                    }
                    BeanUtil.copyProperties(list.get(0),wmsInnerPdaInventoryDetDto);
                }

                WmsInnerInventoryDetDto scan = this.scan(null, null, wmsInnerPdaInventoryDetDto.getBarcode());

                List<WmsInnerMaterialBarcodeDto> sn = this.getSn(scan.getBarcodeType(), scan.getBarcode());

                for (WmsInnerMaterialBarcodeDto wmsInnerMaterialBarcodeDto : sn) {

                    inventoryDetExample.createCriteria().andEqualTo("materialBarcodeId",wmsInnerMaterialBarcodeDto.getMaterialBarcodeId());
                    List<WmsInnerInventoryDet> wmsInnerInventoryDetList = wmsInnerInventoryDetMapper.selectByExample(inventoryDetExample);
                    inventoryDetExample.clear();
                    if (StringUtils.isEmpty(wmsInnerInventoryDetList)) {
                        throw new BizErrorException("条码不存在库存内,或者该条码下的子条码不在库存内");
                    }

                    if (StringUtils.isNotEmpty(wmsInnerMaterialBarcodeDto)) {
                        WmsInnerMaterialBarcodeReOrder wmsInnerMaterialBarcodeReOrder = new WmsInnerMaterialBarcodeReOrder();
                        wmsInnerMaterialBarcodeReOrder.setMaterialBarcodeId(wmsInnerMaterialBarcodeDto.getMaterialBarcodeId());
                        wmsInnerMaterialBarcodeReOrder.setOrderId(wmsInnerJobOrder.getJobOrderId());
                        wmsInnerMaterialBarcodeReOrder.setOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
                        wmsInnerMaterialBarcodeReOrder.setOrderTypeCode("OUT-IWK");
                        wmsInnerMaterialBarcodeReOrder.setOrderCode(wmsInnerJobOrder.getJobOrderCode());
                        wmsInnerMaterialBarcodeReOrder.setScanStatus((byte) 3);
                        barcodeReOrderList.add(wmsInnerMaterialBarcodeReOrder);
                    }
                    WmsInnerInventoryDet wmsInnerInventoryDet = new WmsInnerInventoryDet();
                    wmsInnerInventoryDet.setInventoryDetId(wmsInnerInventoryDetList.get(0).getInventoryDetId());
                    wmsInnerInventoryDet.setStorageId(wmsInnerJobOrderDet.getInStorageId());
                    wmsInnerInventoryDet.setBarcodeStatus((byte) 2);
                    inventoryDetList.add(wmsInnerInventoryDet);
                }

            }
            wmsInnerInventoryDetMapper.updateStroage(inventoryDetList);
            if (StringUtils.isNotEmpty(barcodeReOrderList)) {
                wmsInnerMaterialBarcodeReOrderMapper.insertList(barcodeReOrderList);
            }
        }

        jobDetExample.clear();
        jobDetExample.createCriteria().andEqualTo("jobOrderId",wmsInnerJobOrder.getJobOrderId())
                .andNotEqualTo("lineStatus",3);
        List<WmsInnerJobOrderDet> wmsInnerJobOrderDetList = wmsInnerJobOrderDetMapper.selectByExample(jobDetExample);

        if (StringUtils.isEmpty(wmsInnerJobOrderDetList)) {
            wmsInnerJobOrder.setOrderStatus((byte) 5);
            wmsInnerJobOrder.setWorkStartTime(StringUtils.isNotEmpty(wmsInnerJobOrder.getWorkStartTime())?wmsInnerJobOrder.getWorkStartTime():new Date());
            wmsInnerJobOrder.setWorkEndtTime(new Date());
        }else {
            wmsInnerJobOrder.setOrderStatus((byte) 4);
            wmsInnerJobOrder.setWorkStartTime(new Date());
        }

        //反写上游单据数量
        if ("OUT-SO".equals(wmsInnerJobOrder.getSourceSysOrderTypeCode())) {
            SearchOmSalesOrderDetDto searchOmSalesOrderDetDto = new SearchOmSalesOrderDetDto();
            searchOmSalesOrderDetDto.setSalesOrderDetId(wmsInnerJobOrderDet.getCoreSourceId());
            List<OmSalesOrderDetDto> list = omFeignApi.findList(searchOmSalesOrderDetDto).getData();
            if (StringUtils.isNotEmpty(list)) {
                OmSalesOrderDetDto omSalesOrderDetDto = list.get(0);
                BigDecimal actualQty = StringUtils.isNotEmpty(omSalesOrderDetDto.getActualQty())?omSalesOrderDetDto.getActualQty():new BigDecimal(0);
                omSalesOrderDetDto.setActualQty(actualQty.add(wmsInnerPdaJobOrderDet.getActualQty()));
                omFeignApi.update(omSalesOrderDetDto);
            }
        }else if ("INNER-TO".equals(wmsInnerJobOrder.getSourceSysOrderTypeCode())) {
            SearchOmTransferOrderDet searchOmTransferOrderDet = new SearchOmTransferOrderDet();
            searchOmTransferOrderDet.setTransferOrderDetId(wmsInnerJobOrderDet.getCoreSourceId());
            List<OmTransferOrderDetDto> list = omFeignApi.findList(searchOmTransferOrderDet).getData();
            if (StringUtils.isNotEmpty(list)) {
                OmTransferOrderDetDto omTransferOrderDetDto = list.get(0);
                BigDecimal actualQty = StringUtils.isNotEmpty(omTransferOrderDetDto.getActualInQty())?omTransferOrderDetDto.getActualInQty():new BigDecimal(0);
                omTransferOrderDetDto.setActualInQty(actualQty.add(wmsInnerPdaJobOrderDet.getActualQty()));
                omFeignApi.update(omTransferOrderDetDto);
            }

            if (wmsInnerJobOrder.getOrderStatus() == 5) {
                SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
                searchBaseStorage.setWarehouseId(wmsInnerJobOrder.getWarehouseId());
                searchBaseStorage.setStorageType((byte)2);
                List<BaseStorage> storageList = baseFeignApi.findList(searchBaseStorage).getData();
                if(storageList.size()<1){
                    throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"未维护仓库收货库位");
                }

                List<WmsInnerJobOrderDet> detList = new LinkedList<>();
                int lineNumber = 1;
                for (WmsInnerJobOrderDet innerJobOrderDet : wmsInnerJobOrderDetList) {
                    WmsInnerJobOrderDet newWmsInnerJobOrderDet = new WmsInnerJobOrderDet();
                    newWmsInnerJobOrderDet.setCoreSourceOrderCode(innerJobOrderDet.getCoreSourceOrderCode());
                    newWmsInnerJobOrderDet.setSourceOrderCode(wmsInnerJobOrder.getJobOrderCode());
                    newWmsInnerJobOrderDet.setSourceId(innerJobOrderDet.getJobOrderDetId());
                    newWmsInnerJobOrderDet.setLineNumber(lineNumber+"");
                    newWmsInnerJobOrderDet.setMaterialId(innerJobOrderDet.getMaterialId());
                    newWmsInnerJobOrderDet.setPlanQty(innerJobOrderDet.getPlanQty());
                    newWmsInnerJobOrderDet.setLineStatus((byte)1);
                    newWmsInnerJobOrderDet.setOutStorageId(storageList.get(0).getStorageId());
                    detList.add(newWmsInnerJobOrderDet);
                    lineNumber++;
                }
                WmsInnerJobOrder innerJobOrder = new WmsInnerJobOrder();
                innerJobOrder.setSourceSysOrderTypeCode(wmsInnerJobOrder.getSourceSysOrderTypeCode());
                innerJobOrder.setCoreSourceSysOrderTypeCode(wmsInnerJobOrder.getCoreSourceSysOrderTypeCode());
                innerJobOrder.setJobOrderType((byte)1);
                innerJobOrder.setOrderStatus((byte)1);
                innerJobOrder.setCreateUserId(sysUser.getUserId());
                innerJobOrder.setCreateTime(new Date());
                innerJobOrder.setModifiedUserId(sysUser.getUserId());
                innerJobOrder.setModifiedTime(new Date());
                innerJobOrder.setStatus((byte)1);
                innerJobOrder.setOrgId(sysUser.getOrganizationId());
                innerJobOrder.setWmsInPutawayOrderDets(detList);
                innerJobOrder.setSourceBigType((byte)1);
                wmsInnerJobOrderService.save(innerJobOrder);
            }
        }else if("OUT-PDO".equals(wmsInnerJobOrder.getSourceSysOrderTypeCode())){
            ResponseEntity responseEntity = outFeignApi.updatePlanDeliveryOrderPutawayQty(wmsInnerJobOrderDet.getSourceId(), wmsInnerPdaJobOrderDet.getActualQty());
            if(responseEntity.getCode()!=0){
                throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
            }
        }else if("OUT-DRO".equals(wmsInnerJobOrder.getSourceSysOrderTypeCode())){
            ResponseEntity responseEntity = outFeignApi.updateDeliveryReqOrderPutawayQty(wmsInnerJobOrderDet.getSourceId(), wmsInnerPdaJobOrderDet.getActualQty());
            if(responseEntity.getCode()!=0){
                throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
            }
        }
        wmsInnerJobOrder.setWorkerId(sysUser.getUserId());
        return wmsInnerJobOrderMapper.updateByPrimaryKeySelective(wmsInnerJobOrder);
    }

    private void log (WmsInnerPdaJobOrderDet wmsInnerPdaJobOrderDet,WmsInnerJobOrder wmsInnerJobOrder,WmsInnerJobOrderDet wmsInnerJobOrderDet,Integer status) {
        Example inventoryExample = new Example(WmsInnerInventory.class);
        //查询发货库位库存
        inventoryExample.createCriteria().andEqualTo("storageId",wmsInnerPdaJobOrderDet.getInStorageId())
                .andEqualTo("materialId",wmsInnerPdaJobOrderDet.getMaterialId());
        List<WmsInnerInventory> wmsInnerInventories = wmsInnerInventoryMapper.selectByExample(inventoryExample);
        inventoryExample.clear();

        WmsInnerInventory wmsInnerInventory = new WmsInnerInventory();
        //判断是否存在发货库位库存，没有新增，有修改库存数量
        if (StringUtils.isNotEmpty(wmsInnerInventories)) {
            wmsInnerInventory = wmsInnerInventories.get(0);
            wmsInnerInventory.setPackingQty(wmsInnerInventory.getPackingQty().add(wmsInnerPdaJobOrderDet.getActualQty()));
            //添加增加库存日志
            InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrder,wmsInnerJobOrderDet,new BigDecimal(0),wmsInnerPdaJobOrderDet.getActualQty(),(byte)4,(byte)1);

            inventoryExample.createCriteria().andEqualTo("jobOrderDetId",wmsInnerJobOrderDet.getJobOrderDetId());
            List<WmsInnerInventory> jobDetInventories = wmsInnerInventoryMapper.selectByExample(inventoryExample);
            if (StringUtils.isNotEmpty(jobDetInventories)) {
                //添加扣减库存日志
                InventoryLogUtil.addLog(jobDetInventories.get(0),wmsInnerJobOrder,wmsInnerJobOrderDet,new BigDecimal(0),wmsInnerPdaJobOrderDet.getActualQty(),(byte)4,(byte)2);
            }

            //拣货数量等于分配数量(1,相等 2,少于)
            if (status == 1) {
                wmsInnerInventoryMapper.deleteByExample(inventoryExample);
                inventoryExample.clear();
            }else {
                WmsInnerInventory jobDetInventory = jobDetInventories.get(0);
                jobDetInventory.setPackingQty(jobDetInventory.getPackingQty().subtract(wmsInnerPdaJobOrderDet.getActualQty()));
                wmsInnerInventoryMapper.updateByPrimaryKeySelective(jobDetInventory);
            }
        }else {
            inventoryExample.createCriteria().andEqualTo("jobOrderDetId",wmsInnerJobOrderDet.getJobOrderDetId());
            wmsInnerInventories = wmsInnerInventoryMapper.selectByExample(inventoryExample);

            WmsInnerInventory jobDetInventory = wmsInnerInventories.get(0);
            BeanUtil.copyProperties(jobDetInventory,wmsInnerInventory);

            //拣货数量等于分配数量(1,相等 2,少于)
            if (status == 1) {
                wmsInnerInventoryMapper.deleteByExample(inventoryExample);
                inventoryExample.clear();
            }else {
                jobDetInventory.setPackingQty(jobDetInventory.getPackingQty().subtract(wmsInnerPdaJobOrderDet.getActualQty()));
                wmsInnerInventoryMapper.updateByPrimaryKeySelective(jobDetInventory);
            }

            //添加扣减库存日志
            InventoryLogUtil.addLog(jobDetInventory,wmsInnerJobOrder,wmsInnerJobOrderDet,new BigDecimal(0),wmsInnerPdaJobOrderDet.getActualQty(),(byte)4,(byte)2);

            wmsInnerInventory.setRelevanceOrderCode(wmsInnerJobOrder.getJobOrderCode());
            wmsInnerInventory.setPackingQty(wmsInnerPdaJobOrderDet.getActualQty());
            wmsInnerInventory.setJobStatus((byte) 1);
            wmsInnerInventory.setJobOrderDetId(null);
            wmsInnerInventory.setStorageId(wmsInnerPdaJobOrderDet.getInStorageId());
            wmsInnerInventoryMapper.insertUseGeneratedKeys(wmsInnerInventory);

            //拣货数量等于分配数量(1,相等 2,少于)
//            if (status == 1) {
//                //添加扣减库存日志
//                InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrder,wmsInnerJobOrderDet,new BigDecimal(0),wmsInnerPdaJobOrderDet.getActualQty(),(byte)4,(byte)2);
//                if (StringUtils.isNotEmpty(wmsInnerInventories)) {
//                    wmsInnerInventory = wmsInnerInventories.get(0);
//                    wmsInnerInventory.setStorageId(wmsInnerPdaJobOrderDet.getInStorageId());
//                    wmsInnerInventory.setJobStatus((byte) 1);
//                    wmsInnerInventory.setJobOrderDetId(null);
//                }else {
//                    throw new BizErrorException("分配库存数据错误");
//                }
//            }else {
//                WmsInnerInventory jobDetInventory = wmsInnerInventories.get(0);
//                jobDetInventory.setPackingQty(jobDetInventory.getPackingQty().subtract(wmsInnerPdaJobOrderDet.getActualQty()));
//                wmsInnerInventoryMapper.updateByPrimaryKeySelective(jobDetInventory);
//            }


            //添加增加库存日志
            InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrder,wmsInnerJobOrderDet,new BigDecimal(0),wmsInnerPdaJobOrderDet.getActualQty(),(byte)4,(byte)1);
        }
        wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventory);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public WmsInnerJobOrderDet scanAffirmQty(String barCode,String storageCode,BigDecimal qty,Long jobOrderDetId) {
        SysUser sysUser = currentUser();

        if(StringUtils.isEmpty(qty)){
            throw new BizErrorException("拣货数量不能小于1");
        }
        //通过储位编码查询储位id
        ResponseEntity<List<BaseStorage>> list = baseFeignApi.findList(SearchBaseStorage.builder()
                .storageCode(storageCode)
                .codeQueryMark((byte)1)
                .build());
        if(StringUtils.isEmpty(list.getData())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"库位查询失败");
        }
        WmsInnerJobOrderDet wmsInnerJobOrderDet = wmsInnerJobOrderDetMapper.selectByPrimaryKey(jobOrderDetId);
        if(StringUtils.isEmpty(wmsInnerJobOrderDet.getActualQty())){
            wmsInnerJobOrderDet.setActualQty(BigDecimal.ZERO);
        }
        if(wmsInnerJobOrderDet.getActualQty().add(qty).compareTo(wmsInnerJobOrderDet.getDistributionQty())==1){
            throw new BizErrorException("拣货数量不能大于分配数量");
        }
        //BaseStorage baseStorage = list.getData().get(0);

        SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
        searchWmsInnerJobOrderDet.setJobOrderDetId(jobOrderDetId);
        WmsInnerJobOrderDetDto oldDto = wmsInnerJobOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);
        int num = 0;
        if(wmsInnerJobOrderDet.getActualQty().add(qty).compareTo(wmsInnerJobOrderDet.getDistributionQty())==-1){
            WmsInnerJobOrderDet wms = new WmsInnerJobOrderDet();
            BeanUtil.copyProperties(wmsInnerJobOrderDet,wms);
            wms.setJobOrderDetId(null);
            wms.setPlanQty(qty);
            wms.setDistributionQty(qty);
            wms.setActualQty(qty);
            //wms.setOrderStatus((byte)5);
            wms.setLineStatus((byte)3);
            num+=wmsInnerJobOrderDetMapper.insertUseGeneratedKeys(wms);
            searchWmsInnerJobOrderDet.setJobOrderDetId(wms.getJobOrderDetId());

            //wmsInnerJobOrderDet.setOrderStatus((byte)4);
            wmsInnerJobOrderDet.setLineStatus((byte)2);

            wmsInnerJobOrderDet.setInStorageId(null);
            wmsInnerJobOrderDet.setPlanQty(wmsInnerJobOrderDet.getPlanQty().subtract(wms.getPlanQty()));
            wmsInnerJobOrderDet.setDistributionQty(wmsInnerJobOrderDet.getDistributionQty().subtract(wms.getDistributionQty()));
            wmsInnerJobOrderDet.setActualQty(null);
            wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInnerJobOrderDet.setModifiedTime(new Date());
            num+=wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInnerJobOrderDet);
        }else if(wmsInnerJobOrderDet.getActualQty().add(qty).compareTo(wmsInnerJobOrderDet.getDistributionQty())==0){
            //确认完成
            wmsInnerJobOrderDet.setActualQty(qty);
            //wmsInnerJobOrderDet.setOrderStatus((byte)5);
            wmsInnerJobOrderDet.setLineStatus((byte)3);
            wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInnerJobOrderDet.setModifiedTime(new Date());
            num += wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInnerJobOrderDet);
            searchWmsInnerJobOrderDet.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
        }


        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setJobOrderId(wmsInnerJobOrderDet.getJobOrderId());
        WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInnerJobOrderMapper.findList(searchWmsInnerJobOrder).get(0);
        List<WmsInnerJobOrderDetDto> wmsInnerJobOrderDetDto = wmsInnerJobOrderDetMapper.findList(searchWmsInnerJobOrderDet);
        //更改库存
        num = this.Inventory(oldDto,wmsInnerJobOrderDetDto.get(0));
        //更改库存明细
        if(StringUtils.isNotEmpty(barCode)){
            String[] code = barCode.split(",");
            for (String s : code) {
                num +=this.addInventoryDet(s,wmsInnerJobOrderDto.getJobOrderCode(),wmsInnerJobOrderDetDto.get(0));
            }
        }
        WmsInnerJobOrderDet wms= new WmsInnerJobOrderDet();
        wms.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
        int count = wmsInnerJobOrderDetMapper.selectCount(wms);
        //wms.setOrderStatus((byte)5);
        wms.setLineStatus((byte)3);
        int oCount = wmsInnerJobOrderDetMapper.selectCount(wms);


        if(oCount==count){
            WmsInnerJobOrder ws = wmsInnerJobOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDto.getJobOrderId());
            ws.setOrderStatus((byte)5);
            ws.setModifiedUserId(sysUser.getUserId());
            ws.setModifiedTime(new Date());
            ws.setWorkEndtTime(new Date());
            num +=wmsInnerJobOrderMapper.updateByPrimaryKeySelective(ws);
        }else{
            WmsInnerJobOrder ws = wmsInnerJobOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDto.getJobOrderId());
            ws.setOrderStatus((byte)4);
            ws.setModifiedUserId(sysUser.getUserId());
            ws.setModifiedTime(new Date());
            if(StringUtils.isEmpty(wmsInnerJobOrderDto.getWorkStartTime())){
                ws.setWorkStartTime(new Date());
            }
            num +=wmsInnerJobOrderMapper.updateByPrimaryKeySelective(ws);
        }

        //反写出库单拣货数量
        this.writeDeliveryOrderQty(wmsInnerJobOrderDetDto.get(0));


        return wmsInnerJobOrderDet;
    }

    /**
     * PDA扫码拣货确认修改库存明细
     * @return
     */
    private int addInventoryDet(String barcode,String jobOrderCode,WmsInnerJobOrderDet wmsInnerJobOrderDet){
        //获取完工入库单单号
        Example example = new Example(WmsInnerInventoryDet.class);
        example.createCriteria().andEqualTo("barcode",barcode).andEqualTo("storageId",wmsInnerJobOrderDet.getOutStorageId())
                .andEqualTo("materialId",wmsInnerJobOrderDet.getMaterialId())
                .andEqualTo("barcodeStatus",3)
                .andEqualTo("orgId",wmsInnerJobOrderDet.getOrgId());
        WmsInnerInventoryDet wmsInnerInventoryDet = wmsInnerInventoryDetMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(wmsInnerInventoryDet)){
            throw new BizErrorException("库存匹配失败");
        }
        wmsInnerInventoryDet.setStorageId(wmsInnerJobOrderDet.getInStorageId());
        wmsInnerInventoryDet.setDeliveryOrderCode(jobOrderCode);
        wmsInnerInventoryDet.setDeliverDate(new Date());
        wmsInnerInventoryDet.setBarcodeStatus((byte)4);
        return wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(wmsInnerInventoryDet);
    }

    /**
     * 校验条码
     * @param barCode
     * @return 包装数量
     */
    @Override
    public Map<String ,Object> checkBarcode(String barCode,Long jobOrderDetId) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        WmsInnerJobOrderDet wmsInnerJobOrderDet = wmsInnerJobOrderDetMapper.selectByPrimaryKey(jobOrderDetId);
        if(StringUtils.isEmpty(wmsInnerJobOrderDet)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        Map<String ,Object> map  = new HashMap<>();

        SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
        searchBaseMaterial.setMaterialId(wmsInnerJobOrderDet.getMaterialId());
        List<BaseMaterial> baseMaterialList = baseFeignApi.findList(searchBaseMaterial).getData();
        String materialCode = null;
        if(StringUtils.isNotEmpty(baseMaterialList)){
            materialCode = baseMaterialList.get(0).getMaterialCode();
        }
        if(StringUtils.isNotEmpty(materialCode) && materialCode.equals(barCode)){
            map.put("SN","false");
            return map;
        }else{
            //获取出库单对应的工单
//            WmsOutDeliveryOrderDet wmsOutDeliveryOrderDet = outFeignApi.detail(wmsInnerJobOrderDet.getSourceDetId()).getData();
            BigDecimal qty = InBarcodeUtil.pickCheckBarCode(wmsInnerJobOrderDet.getInventoryStatusId(),wmsInnerJobOrderDet.getMaterialId(),barCode);
            map.put("SN","true");
            map.put("qty",qty);
        }
        return map;
    }

    /**
     * 自动分配
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int autoDistribution(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        int num = 1;
        Map<String, Object> map = new HashMap<>();
        for (String id : arrId) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(id);
            if(wmsInnerJobOrder.getOrderStatus()==(byte)3){
                throw new BizErrorException("单据已分配完成");
            }
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId",wmsInnerJobOrder.getJobOrderId())
                    .andEqualTo("lineStatus",1);
            List<WmsInnerJobOrderDet> list = wmsInnerJobOrderDetMapper.selectByExample(example);
            for (WmsInnerJobOrderDet wms : list) {
                if(StringUtils.isEmpty(wms)){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                }
                //推荐库位
//                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
//                List<WmsInnerInventory> wmsInnerInventories = OutInventoryRule.jobMainRule(wmsInnerJobOrder.getWarehouseId(),wms.getMaterialId(),StringUtils.isNotEmpty(wms.getBatchCode())?wms.getBatchCode():null,StringUtils.isNotEmpty(wms.getProductionDate())?sf.format(wms.getProductionDate()):null,StringUtils.isNotEmpty(wms.getInventoryStatusId())?wms.getInventoryStatusId():null);
//                if(StringUtils.isEmpty(wmsInnerInventories) || wmsInnerInventories.size()<1){
//                    throw new BizErrorException("未匹配到库位");
//                }
                Example inventorExample = new Example(WmsInnerInventory.class);

                map.put("isStorage",1);
                map.put("warehouseId",wmsInnerJobOrder.getWarehouseId());
                map.put("materialId",wms.getMaterialId());
                List<WmsInnerInventoryDto> wmsInnerInventories = wmsInnerInventoryMapper.findList(map);

                if(StringUtils.isEmpty(wmsInnerInventories)) {
                    continue;
                }

                BigDecimal playQty = wms.getPlanQty();
                Long jobDetId = null;
                for (WmsInnerInventory wmsInnerInventory : wmsInnerInventories) {

                    if(playQty.compareTo(BigDecimal.ZERO)==1){
                        BigDecimal qty = new BigDecimal(0);

                        if(wmsInnerInventory.getPackingQty().compareTo(playQty)>-1){
                            num+=wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(WmsInnerJobOrderDet.builder()
                                    .jobOrderDetId(wms.getJobOrderDetId())
                                    .outStorageId(wmsInnerInventory.getStorageId())
                                    .distributionQty(playQty)
                                    .modifiedUserId(sysUser.getUserId())
                                    .modifiedTime(new Date())
                                    //.orderStatus((byte)3)
                                    .lineStatus((byte)2)
                                    .build());
                            qty = playQty;
                            playQty = playQty.subtract(playQty);
                            jobDetId = wms.getJobOrderDetId();
                            //分配库存
//                            num += this.DistributionInventory(wmsInnerJobOrder, wms,2,wmsInnerInventory);
                        }else{
//                            if(wmsInnerInventory.getPackingQty().compareTo(playQty)>-1){
//                                WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
//                                BeanUtil.copyProperties(wms,wmsInnerJobOrderDet);
//                                wmsInnerJobOrderDet.setPlanQty(playQty);
//                                wmsInnerJobOrderDet.setDistributionQty(playQty);
//                                wmsInnerJobOrderDet.setLineStatus((byte) 2);
//                                wmsInnerJobOrderDet.setOutStorageId(wmsInnerInventory.getStorageId());
//                                num+=wmsInnerJobOrderDetMapper.insertUseGeneratedKeys(wmsInnerJobOrderDet);
//                                playQty = BigDecimal.ZERO;
//                                qty = playQty;
//                                jobDetId = wmsInnerJobOrderDet.getJobOrderDetId();
//                                //分配库存
////                                num += this.DistributionInventory(wmsInnerJobOrder, wmsInnerJobOrderDet,2,wmsInnerInventory);
//                            }else {
                            WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
                            BeanUtil.copyProperties(wms,wmsInnerJobOrderDet);
                            wmsInnerJobOrderDet.setJobOrderDetId(null);
                            wmsInnerJobOrderDet.setPlanQty(wmsInnerInventory.getPackingQty());
                            wmsInnerJobOrderDet.setDistributionQty(wmsInnerInventory.getPackingQty());
                            wmsInnerJobOrderDet.setOutStorageId(wmsInnerInventory.getStorageId());
                            wmsInnerJobOrderDet.setLineStatus((byte) 2);
                            num+=wmsInnerJobOrderDetMapper.insertUseGeneratedKeys(wmsInnerJobOrderDet);
                            playQty = playQty.subtract(wmsInnerInventory.getPackingQty());
                            qty = wmsInnerInventory.getPackingQty();
                            jobDetId = wmsInnerJobOrderDet.getJobOrderDetId();
                                //分配库存
//                                num += this.DistributionInventory(wmsInnerJobOrder, wmsInnerJobOrderDet,2,wmsInnerInventory);
//                            }

                            wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(WmsInnerJobOrderDet.builder()
                                    .jobOrderDetId(wms.getJobOrderDetId())
                                    .planQty(playQty)
                                    .modifiedUserId(sysUser.getUserId())
                                    .modifiedTime(new Date())
                                    .build());

                        }

                        WmsInnerInventory newWmsInnerInventory = new WmsInnerInventory();
                        BeanUtil.copyProperties(wmsInnerInventory,newWmsInnerInventory);
                        //更新旧库存
                        wmsInnerInventory.setPackingQty(wmsInnerInventory.getPackingQty().subtract(qty));
                        wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventory);
                        InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrder,wms,new BigDecimal(0),wmsInnerInventory.getPackingQty().subtract(qty),(byte)4,(byte)2);

                        //添加新库存
                        newWmsInnerInventory.setRelevanceOrderCode(wmsInnerJobOrder.getJobOrderCode());
                        newWmsInnerInventory.setPackingQty(qty);
                        newWmsInnerInventory.setJobStatus((byte) 2);
                        newWmsInnerInventory.setJobOrderDetId(jobDetId);
                        wmsInnerInventoryMapper.insertUseGeneratedKeys(newWmsInnerInventory);
                        InventoryLogUtil.addLog(newWmsInnerInventory,wmsInnerJobOrder,wms,new BigDecimal(0),wmsInnerInventory.getPackingQty().subtract(qty),(byte)4,(byte)1);

                    }

                }
                //库位容量减1
                //baseFeignApi.minusSurplusCanPutSalver(wms.getInStorageId(),1);
//                WmsInnerJobOrderDet wmsInnerJobOrderDet = wmsInnerJobOrderDetMapper.selectByPrimaryKey(wms.getJobOrderDetId());
            }

            SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
            searchWmsInnerJobOrderDet.setJobOrderId(wmsInnerJobOrder.getJobOrderId());
            List<WmsInnerJobOrderDetDto> dto = wmsInnerJobOrderDetMapper.findList(searchWmsInnerJobOrderDet);

            if(StringUtils.isEmpty(dto)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            if(dto.stream().filter(li->li.getLineStatus()==(byte)2).collect(Collectors.toList()).size()==dto.size()){
                //更新表头状态
                wmsInnerJobOrderMapper.updateByPrimaryKeySelective(WmsInnerJobOrder.builder()
                        .jobOrderId(wmsInnerJobOrder.getJobOrderId())
                        .orderStatus((byte)3)
                        .build());
            }else if(dto.stream().filter(li->li.getLineStatus()==(byte)1).collect(Collectors.toList()).size()!=0) {
                wmsInnerJobOrderMapper.updateByPrimaryKeySelective(WmsInnerJobOrder.builder()
                        .jobOrderId(wmsInnerJobOrder.getJobOrderId())
                        .orderStatus((byte)2)
                        .build());
            }
        }
        return num;
    }

    /**
     * 手动分配
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int handDistribution(List<WmsInnerJobOrderDetDto> list) {
        SysUser sysUser = currentUser();
        int num=0;
        for (WmsInnerJobOrderDetDto wmsInPutawayOrderDet : list) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(wmsInPutawayOrderDet.getJobOrderId());
//            if(!StringUtils.isEmpty(wmsInPutawayOrderDet.getDistributionQty()) && wmsInPutawayOrderDet.getDistributionQty().doubleValue()>wmsInPutawayOrderDet.getPlanQty().doubleValue()){
//                throw new BizErrorException("分配数量不能大于计划数量");
//            }
//            WmsInnerJobOrderDet ss = wmsInnerJobOrderDetMapper.selectByPrimaryKey(wmsInPutawayOrderDet.getJobOrderDetId());
//            if(ss.getLineStatus()>1){
//                throw new BizErrorException("产品已分配完成");
//            }
//            WmsInnerJobOrderDet ws = new WmsInnerJobOrderDet();

            List<WmsInnerInventoryDto> wmsInnerInventory = wmsInPutawayOrderDet.getWmsInnerInventory();
            if (StringUtils.isNotEmpty(wmsInnerInventory)) {
                BigDecimal planQty = wmsInPutawayOrderDet.getPlanQty();
                for (WmsInnerInventoryDto wmsInnerInventoryDto : wmsInnerInventory) {
                    //库存分配数量是否大于拣货作业明细计划数量
                    if (wmsInnerInventoryDto.getDistributionQty().compareTo(planQty) >= 0) {
                        wmsInPutawayOrderDet.setDistributionQty(planQty);
                        wmsInPutawayOrderDet.setLineStatus((byte)2);
                        wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                        wmsInPutawayOrderDet.setModifiedTime(new Date());
                        wmsInPutawayOrderDet.setOutStorageId(wmsInnerInventoryDto.getStorageId());
                        num += wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);

                    }else {
                        //分配中
                        WmsInnerJobOrderDet wms = new WmsInnerJobOrderDet();
                        BeanUtil.copyProperties(wmsInPutawayOrderDet,wms);
                        wms.setJobOrderDetId(null);
                        wms.setPlanQty(wmsInnerInventoryDto.getDistributionQty());
                        wms.setDistributionQty(wmsInnerInventoryDto.getDistributionQty());
                        wms.setLineStatus((byte)2);
                        wms.setOutStorageId(wmsInnerInventoryDto.getStorageId());
                        num+=wmsInnerJobOrderDetMapper.insertUseGeneratedKeys(wms);

                        wmsInPutawayOrderDet.setLineStatus((byte)1);
                        wmsInPutawayOrderDet.setDistributionQty(null);
                        wmsInPutawayOrderDet.setOutStorageId(null);
                        wmsInPutawayOrderDet.setPlanQty(new BigDecimal(wmsInPutawayOrderDet.getPlanQty().doubleValue()-wmsInnerInventoryDto.getDistributionQty().doubleValue()));
                        wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                        wmsInPutawayOrderDet.setModifiedTime(new Date());
                        num += wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
                        planQty = planQty.subtract(wmsInnerInventoryDto.getDistributionQty());

                        wmsInPutawayOrderDet.setJobOrderDetId(wms.getJobOrderDetId());
                    }

                    WmsInnerInventory newWmsInnerInventory = new WmsInnerInventory();
                    BeanUtil.copyProperties(wmsInnerInventoryDto,newWmsInnerInventory);
                    //更新旧库存
                    wmsInnerInventoryDto.setPackingQty(wmsInnerInventoryDto.getPackingQty().subtract(wmsInnerInventoryDto.getDistributionQty()));
                    wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventoryDto);
                    InventoryLogUtil.addLog(wmsInnerInventoryDto,wmsInnerJobOrder,wmsInPutawayOrderDet,new BigDecimal(0),wmsInnerInventoryDto.getPackingQty().subtract(wmsInnerInventoryDto.getDistributionQty()),(byte)4,(byte)2);

                    //添加新库存
                    newWmsInnerInventory.setRelevanceOrderCode(wmsInnerJobOrder.getJobOrderCode());
                    newWmsInnerInventory.setPackingQty(wmsInnerInventoryDto.getDistributionQty());
                    newWmsInnerInventory.setJobStatus((byte) 2);
                    newWmsInnerInventory.setJobOrderDetId(wmsInPutawayOrderDet.getJobOrderDetId());
                    wmsInnerInventoryMapper.insertUseGeneratedKeys(newWmsInnerInventory);
                    InventoryLogUtil.addLog(newWmsInnerInventory,wmsInnerJobOrder,wmsInPutawayOrderDet,new BigDecimal(0),wmsInnerInventoryDto.getPackingQty().subtract(wmsInnerInventoryDto.getDistributionQty()),(byte)4,(byte)1);

                }

                SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
                searchWmsInnerJobOrderDet.setJobOrderId(wmsInPutawayOrderDet.getJobOrderId());
                List<WmsInnerJobOrderDetDto> dto = wmsInnerJobOrderDetMapper.findList(searchWmsInnerJobOrderDet);

                if(StringUtils.isEmpty(dto)){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                }
                if(dto.stream().filter(li->li.getLineStatus()==(byte)2).collect(Collectors.toList()).size()==dto.size()){
                    //更新表头状态
                    wmsInnerJobOrderMapper.updateByPrimaryKeySelective(WmsInnerJobOrder.builder()
                            .jobOrderId(wmsInPutawayOrderDet.getJobOrderId())
                            .orderStatus((byte)3)
                            .build());
                }else if(dto.stream().filter(li->li.getLineStatus()==(byte)1).collect(Collectors.toList()).size()!=0) {
                    wmsInnerJobOrderMapper.updateByPrimaryKeySelective(WmsInnerJobOrder.builder()
                            .jobOrderId(wmsInPutawayOrderDet.getJobOrderId())
                            .orderStatus((byte)2)
                            .build());
                }
            }
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int closeDocuments(String id) {
        Example detExample = new Example(WmsInnerJobOrderDet.class);
        detExample.createCriteria().andEqualTo("jobOrderId",id)
                .andEqualTo("lineStatus",2);
        List<WmsInnerJobOrderDet> list = wmsInnerJobOrderDetMapper.selectByExample(detExample);
        List<Long> detIdList = new ArrayList<>();
        Map<String, BigDecimal> map = new HashMap<>();
        //将当前关闭单不同库位不同物料分配的数据提取
        for (WmsInnerJobOrderDet wmsInnerJobOrderDet : list) {
            BigDecimal qty = map.get(wmsInnerJobOrderDet.getOutStorageId() + "," + wmsInnerJobOrderDet.getMaterialId());
            qty = StringUtils.isNotEmpty(qty)?qty:new BigDecimal(0);
            qty = qty.add(wmsInnerJobOrderDet.getDistributionQty());
            map.put(wmsInnerJobOrderDet.getOutStorageId()+","+wmsInnerJobOrderDet.getMaterialId(),qty);
            detIdList.add(wmsInnerJobOrderDet.getJobOrderDetId());
        }
        //删除分配新建库存
        Example inventoryExample = new Example(WmsInnerInventory.class);
        if (StringUtils.isNotEmpty(detIdList)) {
            inventoryExample.createCriteria().andIn("jobOrderDetId",detIdList);
            wmsInnerInventoryMapper.deleteByExample(inventoryExample);
            inventoryExample.clear();
        }

        List<WmsInnerInventory> inventoryList = new ArrayList<>();
        //更新库存数量
        for (String s : map.keySet()) {
            String[] split = s.split(",");
            inventoryExample.createCriteria().andEqualTo("storageId",split[0])
                    .andEqualTo("materialId",split[1]).andEqualTo("jobStatus",1);
            List<WmsInnerInventory> wmsInnerInventories = wmsInnerInventoryMapper.selectByExample(inventoryExample);
            if (StringUtils.isNotEmpty(wmsInnerInventories)) {
                WmsInnerInventory wmsInnerInventory = wmsInnerInventories.get(0);
                wmsInnerInventory.setPackingQty(wmsInnerInventory.getPackingQty().add(map.get(s)));
                inventoryList.add(wmsInnerInventory);
            }
            inventoryExample.clear();
        }
        if (StringUtils.isNotEmpty(inventoryList)) {
            wmsInnerInventoryMapper.batchUpdate(inventoryList);
        }

        Example example = new Example(WmsInnerJobOrder.class);
        example.createCriteria().andEqualTo("jobOrderId",id);
        WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectOneByExample(example);
        wmsInnerJobOrder.setOrderStatus((byte) 5);
        wmsInnerJobOrder.setRemark("非正常作业，手动关闭");
        return wmsInnerJobOrderMapper.updateByPrimaryKeySelective(wmsInnerJobOrder);
    }

    /**
     * 取消分配
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int cancelDistribution(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        int num = 0;
        for (String s : arrId) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(s);
            if(StringUtils.isEmpty(wmsInnerJobOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
//            if(wmsInnerJobOrder.getOrderTypeId()==8){
//                throw new BizErrorException("领料拣货单无法取消分配");
//            }
            if(wmsInnerJobOrder.getOrderStatus()==(byte)4 || wmsInnerJobOrder.getOrderStatus()==(byte)5){
                throw new BizErrorException("单据作业中，无法取消");
            }
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId",s);
            List<WmsInnerJobOrderDet> list = wmsInnerJobOrderDetMapper.selectByExample(example);
            Map<Long,List<WmsInnerJobOrderDet>> map = new HashMap<>();
            for (WmsInnerJobOrderDet wmsInnerJobOrderDet : list) {
                if(wmsInnerJobOrderDet.getLineStatus()==(byte)3){
                    throw new BizErrorException("单据作业中 无法取消");
                }
                if(map.containsKey(wmsInnerJobOrderDet.getMaterialId())){
                    List<WmsInnerJobOrderDet> nm = new ArrayList<>();
                    for (WmsInnerJobOrderDet innerJobOrderDet : map.get(wmsInnerJobOrderDet.getMaterialId())) {
                        innerJobOrderDet.setPlanQty(innerJobOrderDet.getPlanQty().add(wmsInnerJobOrderDet.getPlanQty()));
                        nm.add(innerJobOrderDet);
                    }
                    map.put(wmsInnerJobOrderDet.getMaterialId(),nm);
                }else{
                    List<WmsInnerJobOrderDet> list1 = new ArrayList<>();
                    list1.add(wmsInnerJobOrderDet);
                    map.put(wmsInnerJobOrderDet.getMaterialId(),list1);
                }

                //恢复库存数量
                if(wmsInnerJobOrderDet.getLineStatus()==(byte)2){
                    Example inventoryExample = new Example(WmsInnerInventory.class);
                    inventoryExample.createCriteria().andEqualTo("storageId",wmsInnerJobOrderDet.getOutStorageId())
                            .andEqualTo("materialId",wmsInnerJobOrderDet.getMaterialId()).andEqualTo("jobStatus",1);
                    List<WmsInnerInventory> wmsInnerInventories = wmsInnerInventoryMapper.selectByExample(inventoryExample);
                    inventoryExample.clear();
                    if(StringUtils.isNotEmpty(wmsInnerInventories)){
                        WmsInnerInventory wmsInnerInventory = wmsInnerInventories.get(0);
                        wmsInnerInventory.setPackingQty(wmsInnerInventory.getPackingQty().add(wmsInnerJobOrderDet.getDistributionQty()));
                        wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventory);
                    }else{
                        throw new BizErrorException("恢复占用库存失败");
                    }
//                    int res = this.reconver(wmsInnerJobOrderDet.getJobOrderDetId());
                }
                //删除分配库存
                Example example1 = new Example(WmsInnerInventory.class);
                example1.createCriteria().andEqualTo("jobOrderDetId",wmsInnerJobOrderDet.getJobOrderDetId()).andEqualTo("jobStatus",(byte)2);
                wmsInnerInventoryMapper.deleteByExample(example1);
            }
            wmsInnerJobOrderDetMapper.deleteByExample(example);
            for (List<WmsInnerJobOrderDet> value : map.values()) {
                for (WmsInnerJobOrderDet wmsInnerJobOrderDet : value) {
                    wmsInnerJobOrderDet.setDistributionQty(null);
                    wmsInnerJobOrderDet.setOutStorageId(null);
                    //wmsInnerJobOrderDet.setOrderStatus((byte)1);
                    wmsInnerJobOrderDet.setLineStatus((byte)1);
                    wmsInnerJobOrderDet.setModifiedTime(new Date());
                    wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
                    //wmsInnerJobOrderDet.setOrderStatus((byte)1);
                    wmsInnerJobOrderDet.setLineStatus((byte)1);
                    num +=wmsInnerJobOrderDetMapper.insertSelective(wmsInnerJobOrderDet);
                }
            }
            wmsInnerJobOrder.setOrderStatus((byte)1);
            num +=wmsInnerJobOrderMapper.updateByPrimaryKeySelective(wmsInnerJobOrder);
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int allReceiving(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        int num = 0;
        for (String id : arrId) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(id);
            if (wmsInnerJobOrder.getOrderStatus()<(byte) 3) {
                throw new BizErrorException("未分配完成,无法拣货");
            }
            double total = 0.00;
            if (wmsInnerJobOrder.getOrderStatus() == (byte) 5) {
                throw new BizErrorException("单据确认已完成");
            }
            Long jobOrderDetId = null;
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId", wmsInnerJobOrder.getJobOrderId());
            List<WmsInnerJobOrderDet> wmsInnerJobOrderDets = wmsInnerJobOrderDetMapper.selectByExample(example);


            //领料拣货单整单确认
//            if(wmsInnerJobOrder.getOrderTypeId()==8){
//                wmsInnerJobOrder.setModifiedTime(new Date());
//                wmsInnerJobOrder.setModifiedUserId(sysUser.getUserId());
//                wmsInnerJobOrder.setWmsInPutawayOrderDets(wmsInnerJobOrderDets);
//                return this.PickingConfirmation(wmsInnerJobOrder,1);
//
//            }


            for (WmsInnerJobOrderDet wmsInnerJobOrderDet : wmsInnerJobOrderDets) {
                if(wmsInnerJobOrderDet.getLineStatus()==(byte)1) {
                    if (StringUtils.isEmpty(id)) {
                        throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                    }
                    SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
                    searchWmsInnerJobOrderDet.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
                    WmsInnerJobOrderDetDto oldDto = wmsInnerJobOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);

                    total += wmsInnerJobOrderDet.getDistributionQty().doubleValue();
                    num += wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(WmsInnerJobOrderDet.builder()
                            .jobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId())
                            //.orderStatus((byte) 5)
                            .lineStatus((byte) 3)
                            .actualQty(wmsInnerJobOrderDet.getDistributionQty())
                            .modifiedUserId(sysUser.getUserId())
                            .modifiedTime(new Date())
                            .build());

                    //更改库存为正常状态
                    searchWmsInnerJobOrderDet.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
                    WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto = wmsInnerJobOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);

                    //拣货作业更改库存
                    num+= this.Inventory(oldDto,wmsInnerJobOrderDetDto);

                    wmsInnerJobOrderDet.setActualQty(wmsInnerJobOrderDet.getDistributionQty());
                    //反写出库单拣货数量
                    num += this.writeDeliveryOrderQty(wmsInnerJobOrderDet);

                    //清除redis
                    this.removeRedis(wmsInnerJobOrderDet.getJobOrderDetId());
                }
            }
            BigDecimal resultQty = wmsInnerJobOrderDets.stream()
                    .map(WmsInnerJobOrderDet::getDistributionQty)
                    .reduce(BigDecimal.ZERO,BigDecimal::add);
                //更改表头为作业完成状态
                wmsInnerJobOrderMapper.updateByPrimaryKeySelective(WmsInnerJobOrder.builder()
                        .orderStatus((byte) 5)
                        .jobOrderId(wmsInnerJobOrder.getJobOrderId())
                        .workStartTime(new Date())
                        .workEndtTime(new Date())
                        .build());

            }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int singleReceiving(List<WmsInnerJobOrderDet> wmsInPutawayOrderDets) {
        SysUser sysUser = currentUser();
        int num = 0;
        WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(wmsInPutawayOrderDets.get(0).getJobOrderId());


        //领料拣货单整单确认
//        if(wmsInnerJobOrder.getOrderTypeId()==8){
//            wmsInnerJobOrder.setModifiedTime(new Date());
//            wmsInnerJobOrder.setModifiedUserId(sysUser.getUserId());
//            wmsInnerJobOrder.setWmsInPutawayOrderDets(wmsInPutawayOrderDets);
//            return this.PickingConfirmation(wmsInnerJobOrder,0);
//        }


        for (WmsInnerJobOrderDet wmsInPutawayOrderDet : wmsInPutawayOrderDets) {
            if(wmsInnerJobOrder.getOrderStatus()==(byte)5){
                throw new BizErrorException("单据确认已完成");
            }

            SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
            searchWmsInnerJobOrderDet.setJobOrderDetId(wmsInPutawayOrderDet.getJobOrderDetId());
            WmsInnerJobOrderDetDto oldDto = wmsInnerJobOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);

            BigDecimal aqty = wmsInPutawayOrderDet.getActualQty();
            Long jobOrderDetId = null;
            if(wmsInPutawayOrderDet.getActualQty().compareTo(wmsInPutawayOrderDet.getDistributionQty())==-1){
                WmsInnerJobOrderDet wms = new WmsInnerJobOrderDet();
                BeanUtil.copyProperties(wmsInPutawayOrderDet,wms);
                wms.setJobOrderDetId(null);
                wms.setPlanQty(wmsInPutawayOrderDet.getActualQty());
                wms.setDistributionQty(wmsInPutawayOrderDet.getActualQty());
                //wms.setOrderStatus((byte)5);
                wms.setLineStatus((byte)3);
                num+=wmsInnerJobOrderDetMapper.insertUseGeneratedKeys(wms);
                jobOrderDetId = wms.getJobOrderDetId();

                //wmsInPutawayOrderDet.setOrderStatus((byte)3);
                wmsInPutawayOrderDet.setLineStatus((byte)2);
                wmsInPutawayOrderDet.setInStorageId(null);
                wmsInPutawayOrderDet.setPlanQty(wmsInPutawayOrderDet.getPlanQty().subtract(wms.getPlanQty()));
                wmsInPutawayOrderDet.setDistributionQty(wmsInPutawayOrderDet.getDistributionQty().subtract(wms.getDistributionQty()));
                wmsInPutawayOrderDet.setActualQty(null);
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                num+=wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
            }else if(wmsInPutawayOrderDet.getDistributionQty().compareTo(wmsInPutawayOrderDet.getPlanQty())==0){
                //确认完成
                //wmsInPutawayOrderDet.setOrderStatus((byte)5);
                wmsInPutawayOrderDet.setLineStatus((byte)3);
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                num += wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
                jobOrderDetId = wmsInPutawayOrderDet.getJobOrderDetId();
            }
            //更改库存
            SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
            searchWmsInnerJobOrder.setJobOrderId(wmsInPutawayOrderDet.getJobOrderId());
            WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInnerJobOrderMapper.findList(searchWmsInnerJobOrder).get(0);
            WmsInnerJobOrderDet Det = wmsInnerJobOrderDetMapper.selectByPrimaryKey(jobOrderDetId);

            //拣货作业更改库存
            searchWmsInnerJobOrderDet.setJobOrderDetId(jobOrderDetId);
            List<WmsInnerJobOrderDetDto> wmsInner = wmsInnerJobOrderDetMapper.findList(searchWmsInnerJobOrderDet);
            num+= this.Inventory(oldDto,wmsInner.get(0));

            //反写出库单拣货数量
            num+=this.writeDeliveryOrderQty(Det);


            WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
            wmsInnerJobOrderDet.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
            int count = wmsInnerJobOrderDetMapper.selectCount(wmsInnerJobOrderDet);
            //wmsInnerJobOrderDet.setOrderStatus((byte)5);
            wmsInnerJobOrderDet.setLineStatus((byte)3);
            int oCount = wmsInnerJobOrderDetMapper.selectCount(wmsInnerJobOrderDet);


            if(oCount==count){
                WmsInnerJobOrder ws = wmsInnerJobOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDto.getJobOrderId());
                ws.setOrderStatus((byte)5);
                ws.setModifiedUserId(sysUser.getUserId());
                ws.setModifiedTime(new Date());
                if(StringUtils.isEmpty(ws.getWorkStartTime())){
                    ws.setWorkStartTime(new Date());
                }
                ws.setWorkEndtTime(new Date());
                num +=wmsInnerJobOrderMapper.updateByPrimaryKeySelective(ws);
                //清除redis
                this.removeRedis(wmsInPutawayOrderDet.getJobOrderDetId());
            }else{
                WmsInnerJobOrder ws = wmsInnerJobOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDto.getJobOrderId());
                ws.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());

                ws.setOrderStatus((byte)4);
                ws.setModifiedUserId(sysUser.getUserId());
                ws.setModifiedTime(new Date());
                if(StringUtils.isEmpty(wmsInnerJobOrder.getWorkStartTime())){
                    ws.setWorkStartTime(new Date());
                }
                num +=wmsInnerJobOrderMapper.updateByPrimaryKeySelective(ws);
            }
        }
        return num;
    }

    @Override
    public List<WmsInnerJobOrderDto> findList(SearchWmsInnerJobOrder searchWmsInnerJobOrder) {
        SysUser sysUser = currentUser();
        searchWmsInnerJobOrder.setOrgId(sysUser.getOrganizationId());
        return wmsInnerJobOrderMapper.findList(searchWmsInnerJobOrder);
    }
    /**
     * 拣货分配库存
     * @param wmsInnerJobOrder
     * @param wmsInnerJobOrderDet
     */
    private int  DistributionInventory(WmsInnerJobOrder wmsInnerJobOrder,WmsInnerJobOrderDet wmsInnerJobOrderDet,int type,WmsInnerInventory wmsInnerInventory){
//        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
//        searchWmsInnerJobOrder.setJobOrderId(wmsInnerJobOrder.getJobOrderId());
//        WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInnerJobOrderMapper.findList(searchWmsInnerJobOrder).get(0);
        //获取分配库存库存
        SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
        searchWmsInnerJobOrderDet.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
        List<WmsInnerJobOrderDetDto> list = wmsInnerJobOrderDetMapper.findList(searchWmsInnerJobOrderDet);
        if(list.size()<1){
            throw new BizErrorException(ErrorCodeEnum.GL9999404);
        }
        WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto = list.get(0);
        wmsInnerJobOrderDetDto.setDistributionQty(wmsInnerJobOrderDet.getDistributionQty());
        wmsInnerJobOrderDetDto.setInventoryStatusId(wmsInnerJobOrderDet.getInventoryStatusId());
//        wmsInnerJobOrderDetDto.setWarehouseId(wmsInnerJobOrderDto.getWarehouseId());
//        wmsInnerJobOrderDetDto.setWarehouseName(wmsInnerJobOrderDto.getWarehouseName());
        int num = 0;
        num+=subtract(wmsInnerJobOrder,wmsInnerJobOrderDetDto,type,wmsInnerInventory);
        if(num>0){
            plus(wmsInnerJobOrder,wmsInnerJobOrderDetDto);
        }else {
            throw new BizErrorException("库存分配失败");
        }
        return num;
    }

    /**
     * 减库存
     * @param wmsInnerJobOrderDetDto
     * @return
     */
    private int subtract(WmsInnerJobOrder wmsInnerJobOrder,WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto,int type,WmsInnerInventory wmsInnerInventorys){
        List<WmsInnerInventory> wmsInnerInventory = new ArrayList<>();
        if(type==1 || type==3){
            Example example = new Example(WmsInnerInventory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("materialId",wmsInnerJobOrderDetDto.getMaterialId());
            if(!StringUtils.isNotEmpty(wmsInnerJobOrderDetDto.getBatchCode())){
                criteria.andEqualTo("batchCode",wmsInnerJobOrderDetDto.getBatchCode());
            }
            criteria.andEqualTo("jobStatus",(byte)1);
            criteria.andEqualTo("storageId",wmsInnerJobOrderDetDto.getOutStorageId()).andEqualTo("warehouseId",wmsInnerJobOrder.getWarehouseId()).andGreaterThan("packingQty",0).andEqualTo("orgId",wmsInnerJobOrderDetDto.getOrgId());
            criteria.andEqualTo("stockLock",0).andEqualTo("lockStatus",0);
            if(StringUtils.isNotEmpty(wmsInnerJobOrderDetDto.getInventoryStatusId())){
                criteria.andEqualTo("inventoryStatusId",wmsInnerJobOrderDetDto.getInventoryStatusId());
            }
            wmsInnerInventory = wmsInnerInventoryMapper.selectByExample(example);
        }else if(type==2){
            wmsInnerInventory.add(wmsInnerInventorys);
        }
        if(StringUtils.isEmpty(wmsInnerInventory)){
            throw new BizErrorException("未匹配到库存");
        }
        BigDecimal acuQty = wmsInnerJobOrderDetDto.getDistributionQty();
        //库存数量
        BigDecimal countQty = wmsInnerInventory.stream()
                .map(li->{
                    if(StringUtils.isEmpty(li.getPackingQty())){
                        return BigDecimal.ZERO;
                    }else {
                        return li.getPackingQty();
                    }
                })
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        if(acuQty.compareTo(countQty)==1){
            throw new BizErrorException("库存不足");
        }
        int num = 0;
        Map<Long,BigDecimal> bigDecimalMap = new HashMap<>();
        if(redisUtil.hasKey(this.REDIS_KEY+wmsInnerJobOrderDetDto.getJobOrderDetId().toString())){
            bigDecimalMap = (Map<Long, BigDecimal>) redisUtil.get(this.REDIS_KEY+wmsInnerJobOrderDetDto.getJobOrderDetId().toString());
        }
        for (WmsInnerInventory innerInventory : wmsInnerInventory) {
            if(acuQty.compareTo(BigDecimal.ZERO)==1) {
                if (innerInventory.getPackingQty().compareTo(acuQty) == -1 && innerInventory.getPackingQty().compareTo(BigDecimal.ZERO) == 1) {
                    if (bigDecimalMap.containsKey(innerInventory.getInventoryId())) {
                        bigDecimalMap.put(innerInventory.getInventoryId(), bigDecimalMap.get(innerInventory.getInventoryId()).add(innerInventory.getPackingQty()));
                    } else {
                        bigDecimalMap.put(innerInventory.getInventoryId(), innerInventory.getPackingQty());
                    }
                    acuQty = acuQty.subtract(innerInventory.getPackingQty());
                    if (type == 3) {
                        //添加库存日志
                        InventoryLogUtil.addLog(innerInventory, wmsInnerJobOrder, wmsInnerJobOrderDetDto, innerInventory.getPackingQty(), innerInventory.getPackingQty(), (byte) 4, (byte) 2);
                    }
                    innerInventory.setPackingQty(innerInventory.getPackingQty().subtract(innerInventory.getPackingQty()));
                } else if (innerInventory.getPackingQty().compareTo(acuQty) > -1) {
                    if (type == 3) {
                        //添加库存日志
                        InventoryLogUtil.addLog(innerInventory, wmsInnerJobOrder, wmsInnerJobOrderDetDto, innerInventory.getPackingQty(), acuQty, (byte) 4, (byte) 2);
                    }
                    innerInventory.setPackingQty(innerInventory.getPackingQty().subtract(acuQty));
                    if (bigDecimalMap.containsKey(innerInventory.getInventoryId())) {
                        bigDecimalMap.put(innerInventory.getInventoryId(), bigDecimalMap.get(innerInventory.getInventoryId()).add(acuQty));
                    } else {
                        bigDecimalMap.put(innerInventory.getInventoryId(), acuQty);
                    }
                    acuQty = acuQty.subtract(acuQty);
                }
                num += wmsInnerInventoryMapper.updateByPrimaryKeySelective(innerInventory);
            }
//            //记录材料日志
//            //获取程序配置项
//            SearchSysSpecItem searchSysSpecItemFiveRing = new SearchSysSpecItem();
//            searchSysSpecItemFiveRing.setSpecCode("sendMaterialLogMessage");
//            List<SysSpecItem> itemListFiveRing = securityFeignApi.findSpecItemList(searchSysSpecItemFiveRing).getData();
//            if(itemListFiveRing.size()<1){
//                throw new BizErrorException("配置项 sendMaterialLogMessage 获取失败");
//            }
//            SysSpecItem sysSpecItem = itemListFiveRing.get(0);
//            if("1".equals(sysSpecItem.getParaValue())) {
//                List<EngPackingOrderSummaryDetDto> list = new ArrayList<>();
//                EngPackingOrderSummaryDetDto engPackingOrderSummaryDetDto = new EngPackingOrderSummaryDetDto();
//                engPackingOrderSummaryDetDto.setContractCode(innerInventory.getContractCode());
//                engPackingOrderSummaryDetDto.setPurchaseReqOrderCode(innerInventory.getPurchaseReqOrderCode());
//                engPackingOrderSummaryDetDto.setLocationNum(innerInventory.getOption4());
//                engPackingOrderSummaryDetDto.setDeviceCode(innerInventory.getOption1());
//                engPackingOrderSummaryDetDto.setDominantTermCode(innerInventory.getOption2());
//                engPackingOrderSummaryDetDto.setMaterialCode(wmsInnerJobOrderDetDto.getMaterialCode());
//                engPackingOrderSummaryDetDto.setQty(wmsInnerJobOrderDetDto.getDistributionQty().subtract(acuQty));
//                list.add(engPackingOrderSummaryDetDto);
//                EngPackingOrder engPackingOrder = new EngPackingOrder();
//                engPackingOrder.setSummaryDetList(list);
//                engFeignApi.saveRecord(engPackingOrder,(byte)6,"出库");
//            }
        }
        redisUtil.set(this.REDIS_KEY+wmsInnerJobOrderDetDto.getJobOrderDetId().toString(),bigDecimalMap);
        return num;
    }

    /**
     * 获取原库存数量
     * @return
     */
    private BigDecimal getInvQty(Long jobOrderDetId,BigDecimal chageQty){
        Map<Long,BigDecimal> bigDecimalMap = new HashMap<>();
        if(redisUtil.hasKey(this.REDIS_KEY+jobOrderDetId.toString())){
            bigDecimalMap = (Map<Long, BigDecimal>) redisUtil.get(this.REDIS_KEY+jobOrderDetId.toString());
        }
        for (Map.Entry<Long, BigDecimal> m : bigDecimalMap.entrySet()){
            WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectByPrimaryKey(m.getKey());
            if(StringUtils.isEmpty(wmsInnerInventory)){
                throw new BizErrorException("恢复库存失败");
            }
            chageQty = chageQty.add(wmsInnerInventory.getPackingQty());
        }
        return chageQty;
    }

    /**
     * 添加数量
     * @param wmsInnerJobOrderDetDto
     * @return
     */
    private int plus(WmsInnerJobOrder wmsInnerJobOrder,WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto){
        SysUser sysUser = currentUser();

        Map<Long,BigDecimal> map = (Map<Long, BigDecimal>) redisUtil.get(this.REDIS_KEY+wmsInnerJobOrderDetDto.getJobOrderDetId().toString());
        WmsInnerInventory wmsInnerInventory = null;
        for (Map.Entry<Long, BigDecimal> m : map.entrySet()) {
            wmsInnerInventory = wmsInnerInventoryMapper.selectByPrimaryKey(m.getKey());
            if(wmsInnerJobOrder!=null){
                break;
            }
        }

        Example example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria = example.createCriteria();
        int num = 0;
        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setJobOrderId(wmsInnerJobOrder.getJobOrderId());
        WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInnerJobOrderMapper.findList(searchWmsInnerJobOrder).get(0);
        criteria.andEqualTo("relevanceOrderCode",wmsInnerJobOrder.getJobOrderCode()).andEqualTo("materialId",wmsInnerJobOrderDetDto.getMaterialId());
//        if(StringUtils.isNotEmpty(wmsInnerJobOrderDetDto.getDistributionQty())){
//            criteria.andEqualTo("batchCode",wmsInnerJobOrderDetDto.getBatchCode());
//        }

        //应该判断批次号不为空 2021-09-14 huangshuijun
        if(StringUtils.isNotEmpty(wmsInnerJobOrderDetDto.getBatchCode())){
            criteria.andEqualTo("batchCode",wmsInnerJobOrderDetDto.getBatchCode());
        }
        criteria.andEqualTo("jobStatus",(byte)2);
        criteria.andEqualTo("jobOrderDetId",wmsInnerJobOrderDetDto.getJobOrderDetId());
        criteria.andEqualTo("storageId",wmsInnerJobOrderDetDto.getOutStorageId()).andEqualTo("warehouseId",wmsInnerJobOrder.getWarehouseId());
        criteria.andEqualTo("orgId",sysUser.getOrganizationId());
        WmsInnerInventory wmsInnerInventorys = wmsInnerInventoryMapper.selectOneByExample(example);
        BigDecimal qty = BigDecimal.ZERO;
        if(StringUtils.isEmpty(wmsInnerInventorys)){
            //新增一条分配库存
            WmsInnerInventory wms = new WmsInnerInventory();
            BeanUtil.copyProperties(wmsInnerInventory,wms);
            wms.setInventoryId(null);
            //wms.setMaterialOwnerId(wmsInnerJobOrderDto.getMaterialOwnerId());
            wms.setWarehouseId(wmsInnerJobOrderDto.getWarehouseId());
            wms.setStorageId(wmsInnerJobOrderDetDto.getOutStorageId());
            wms.setRelevanceOrderCode(wmsInnerJobOrder.getJobOrderCode());
            wms.setPackingQty(wmsInnerJobOrderDetDto.getDistributionQty());
            //wms.setPackingUnitName(wmsInnerJobOrderDetDto.getPackingUnitName());
            wms.setMaterialId(wmsInnerJobOrderDetDto.getMaterialId());
            wms.setJobStatus((byte)2);
            wms.setJobOrderDetId(wmsInnerJobOrderDetDto.getJobOrderDetId());
            wms.setCreateTime(new Date());
            wms.setCreateUserId(sysUser.getUserId());
            wms.setModifiedUserId(sysUser.getUserId());
            wms.setModifiedTime(new Date());
            wms.setOrgId(sysUser.getOrganizationId());
            num = wmsInnerInventoryMapper.insertSelective(wms);
            if(num<1){
                throw new BizErrorException("库存分配失败");
            }
        }else{
            qty = wmsInnerInventorys.getPackingQty();
            wmsInnerInventorys.setPackingQty(wmsInnerInventorys.getPackingQty().add(wmsInnerJobOrderDetDto.getDistributionQty()));
            wmsInnerInventorys.setModifiedTime(new Date());
            wmsInnerInventorys.setModifiedUserId(sysUser.getUserId());
            num = wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
            if(num<1){
                throw new BizErrorException("库存分配失败");
            }
        }
        //添加库存日志
        //InventoryLogUtil.addLog(wmsInnerInventorys,wmsInnerJobOrder,wmsInnerJobOrderDetDto,qty,wmsInnerJobOrderDetDto.getDistributionQty(),(byte)4,(byte)1);
        return num;
    }

    /**
     * 库存
     * @return
     */
    private int Inventory(WmsInnerJobOrderDetDto oldDto,WmsInnerJobOrderDetDto newDto){
        SysUser sysUser = currentUser();
        WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(oldDto.getJobOrderId());
        //旧
        Example example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("relevanceOrderCode",wmsInnerJobOrder.getJobOrderCode()).andEqualTo("materialId",oldDto.getMaterialId()).andEqualTo("warehouseId",wmsInnerJobOrder.getWarehouseId()).andEqualTo("storageId",oldDto.getOutStorageId());
        if(!StringUtils.isEmpty(oldDto.getBatchCode())){
            criteria.andEqualTo("batchCode",oldDto.getBatchCode());
        }
        criteria.andEqualTo("jobOrderDetId",oldDto.getJobOrderDetId());
        criteria.andEqualTo("jobStatus",(byte)2);
        criteria.andEqualTo("orgId",sysUser.getOrganizationId());
        WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(wmsInnerInventory)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        //库存日志
        InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrder,newDto,wmsInnerInventory.getPackingQty(),newDto.getActualQty(),(byte)4,(byte)2);

        WmsInnerInventory wmsIn = new WmsInnerInventory();
        wmsIn.setInventoryId(wmsInnerInventory.getInventoryId());
        wmsIn.setPackingQty(wmsInnerInventory.getPackingQty().subtract(newDto.getActualQty()));
        int num = wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsIn);


        example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("relevanceOrderCode",wmsInnerJobOrder.getJobOrderCode()).andEqualTo("materialId",newDto.getMaterialId()).andEqualTo("warehouseId",wmsInnerJobOrder.getWarehouseId()).andEqualTo("storageId",newDto.getInStorageId());
        if(!StringUtils.isEmpty(newDto.getBatchCode())){
            criteria1.andEqualTo("batchCode",newDto.getBatchCode());
        }
        criteria1.andEqualTo("jobOrderDetId",newDto.getJobOrderDetId());
        criteria1.andEqualTo("jobStatus",(byte)2);
        criteria1.andEqualTo("orgId",sysUser.getOrganizationId());
        WmsInnerInventory wmsInnerInventorys = wmsInnerInventoryMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(wmsInnerInventorys)){
            //添加库存
            WmsInnerInventory inv = new WmsInnerInventory();
            BeanUtil.copyProperties(wmsInnerInventory,inv);
            inv.setStorageId(newDto.getInStorageId());
            inv.setWarehouseId(wmsInnerJobOrder.getWarehouseId());
            inv.setRelevanceOrderCode(wmsInnerJobOrder.getJobOrderCode());
            inv.setPackingQty(newDto.getActualQty());
            inv.setJobStatus((byte)2);
            inv.setJobOrderDetId(newDto.getJobOrderDetId());
            inv.setInventoryId(null);
            inv.setCreateTime(new Date());
            inv.setCreateUserId(sysUser.getUserId());
            inv.setModifiedUserId(sysUser.getUserId());
            inv.setModifiedTime(new Date());
            inv.setOrgId(sysUser.getOrganizationId());

            //库存日志
            InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrder,newDto,BigDecimal.ZERO,inv.getPackingQty(),(byte)4,(byte)1);
            return wmsInnerInventoryMapper.insertSelective(inv);
        }else{
            //原库存

            //库存日志
            InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrder,oldDto,wmsInnerInventorys.getPackingQty(),newDto.getActualQty(),(byte)4,(byte)1);
            wmsInnerInventorys.setPackingQty(wmsInnerInventorys.getPackingQty().add(newDto.getActualQty()));
            return wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
        }
    }

    /**
     * 恢复库存
     * @return
     */
    private int  reconver(Long jobOrderDetId){
        //查询redis
        int num = 0;

        if(redisUtil.hasKey(this.REDIS_KEY+jobOrderDetId.toString())){
            Map<Long,BigDecimal> map = (Map<Long, BigDecimal>) redisUtil.get(this.REDIS_KEY+jobOrderDetId.toString());
            for (Map.Entry<Long, BigDecimal> m : map.entrySet()){
                WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectByPrimaryKey(m.getKey());
                if(StringUtils.isEmpty(wmsInnerInventory)){
                    throw new BizErrorException("恢复库存失败");
                }
                wmsInnerInventory.setPackingQty(wmsInnerInventory.getPackingQty().add(new BigDecimal(String.valueOf(m.getValue()))));
                num+=wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventory);
            }
            redisUtil.expire(this.REDIS_KEY+jobOrderDetId,3);
        }else{
            throw new BizErrorException("恢复占用库存失败");
        }
        return num;
    }

    /**
     * 清除redis
     */
    private void removeRedis(Long jobOrderDetId){
        if(redisUtil.hasKey(this.REDIS_KEY+jobOrderDetId)){
            Map<Long,BigDecimal> map = (Map<Long, BigDecimal>) redisUtil.get(this.REDIS_KEY+jobOrderDetId.toString());
            //设置3秒后失效
            redisUtil.expire(this.REDIS_KEY+jobOrderDetId,3);
        }
    }

    /**
     * 反写销售出库单拣货数量
     * @param wmsInnerJobOrderDet
     * @return
     */
    private int writeDeliveryOrderQty(WmsInnerJobOrderDet wmsInnerJobOrderDet){
        WmsOutDeliveryOrderDet wmsOutDeliveryOrderDet = new WmsOutDeliveryOrderDet();
        wmsOutDeliveryOrderDet.setDeliveryOrderDetId(wmsInnerJobOrderDet.getSourceId());
        wmsOutDeliveryOrderDet.setPickingQty(wmsInnerJobOrderDet.getActualQty());
        ResponseEntity responseEntity = outFeignApi.update(wmsOutDeliveryOrderDet);
        if(responseEntity.getCode()!=0){
            throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
        }
        return 1;
    }

    /**
     * 改状态
     * @param wmsInnerJobOrderDet
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int retrographyStatus(WmsInnerJobOrderDet wmsInnerJobOrderDet){
        wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInnerJobOrderDet);
        Example example = new Example(WmsInnerJobOrderDet.class);
        example.createCriteria().andEqualTo("jobOrderId",wmsInnerJobOrderDet.getJobOrderId());
        List<WmsInnerJobOrderDet> list = wmsInnerJobOrderDetMapper.selectByExample(example);
        byte by = 4;
//        if(list.size()==list.stream().filter(li->li.getOrderStatus()==6).collect(Collectors.toList()).size()){
//            by=6;
//        }
//        if(list.stream().filter(li->li.getOrderStatus()==5).collect(Collectors.toList()).size()==list.size()){
//            by=5;
//        }
        return wmsInnerJobOrderMapper.updateByPrimaryKeySelective(WmsInnerJobOrder.builder()
                .jobOrderId(wmsInnerJobOrderDet.getJobOrderId())
                .orderStatus(by)
                .build());
    }


    /**
     * 调拨拣货
     */


    /**
     * 快速发运
     * @param outDeliveryOrderId
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int autoOutOrder(Long outDeliveryOrderId,Byte orderTypeId) {
        SysUser sysUser = currentUser();
        //查询调拨出库对应的待发运拣货作业
        Example example = new Example(WmsInnerJobOrder.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("sourceOrderId",outDeliveryOrderId).andEqualTo("jobOrderType",4);
        //领料拣货单
        if(orderTypeId==8){
            List<Byte> bytes = new ArrayList<>();
            bytes.add((byte)4);
            bytes.add((byte)5);
            criteria.andIn("orderStatus",bytes);
        }else {
            criteria.andEqualTo("orderStatus",5);
        }
        List<WmsInnerJobOrder> list = wmsInnerJobOrderMapper.selectByExample(example);
        if(list.size()<1){
            throw new BizErrorException("出库单已完成或未拣货");
        }

        if(list.get(0).getJobOrderType()==8){
            //获取程序配置项
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("stockRequisition");
            List<SysSpecItem> itemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            if(itemList.size()<1){
                throw new BizErrorException("领料拣货配置项获取失败");
            }
            Map<String ,String> map = JSONArray.parseObject(itemList.get(0).getParaValue(), Map.class);
            if(map.get("lack").equals("false") && map.get("beyond").equals("false")){
                if(list.size()>list.stream().filter(li->li.getOrderStatus()==(byte)5).collect(Collectors.toList()).size()){
                    throw new BizErrorException("拣货未完成,发运失败");
                }
            }else {
                //领料拣货单只有作业中状态
                if(list.size()>list.stream().filter(li->li.getOrderStatus()==(orderTypeId==8?(byte)4:(byte)5)).collect(Collectors.toList()).size()){
                    throw new BizErrorException("拣货未完成,发运失败");
                }
            }
        }else {
            //领料拣货单只有作业中状态
            if(list.size()>list.stream().filter(li->li.getOrderStatus()==(byte)5).collect(Collectors.toList()).size()){
                throw new BizErrorException("拣货未完成,发运失败");
            }
        }
        //出库装车单
        WmsOutDespatchOrder wmsOutDespatchOrder = new WmsOutDespatchOrder();
        List<WmsOutDespatchOrderReJo> wmsOutDespatchOrderReJos = new ArrayList<>();
        List<WmsInnerJobOrder> wmsInnerJobOrders = new ArrayList<>();
        for (WmsInnerJobOrder wmsInnerJobOrder : list) {
            //领料拣货单是否少拣
            if(wmsInnerJobOrder.getJobOrderType()==8){
                List<WmsInnerJobOrderDet> wmsInnerJobOrderDets = new ArrayList<>();
                example = new Example(WmsInnerJobOrderDet.class);
                example.createCriteria().andEqualTo("jobOrderId",wmsInnerJobOrder.getJobOrderId());
                List<WmsInnerJobOrderDet> dets = wmsInnerJobOrderDetMapper.selectByExample(example);

                //查询是否有可发运数量
                if(dets.stream().filter(li->li.getLineStatus()==1 || li.getLineStatus()==3).collect(Collectors.toList()).size()<1){
                    throw new BizErrorException("暂无可发运数量");
                }

                if(dets.stream().filter(li->li.getActualQty().compareTo(li.getDistributionQty())==-1).collect(Collectors.toList()).size()>0){
                    //获取配置项
                    //获取程序配置项
                    SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
                    searchSysSpecItem.setSpecCode("stockRequisition");
                    List<SysSpecItem> itemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
                    Map<String ,String> map = JSONArray.parseObject(itemList.get(0).getParaValue(), Map.class);
                    if(map.get("lack").equals("false")){
                        throw new BizErrorException("领料拣货单不允许少拣");
                    }else {
                        for (WmsInnerJobOrderDet det : dets) {
                            if(StringUtils.isNotEmpty(det.getActualQty()) && det.getActualQty().compareTo(det.getDistributionQty())==-1){
                                wmsInnerJobOrderDets.add(det);
                            }
                        }
                    }
                }
                if(wmsInnerJobOrderDets.size()>0){
                    wmsInnerJobOrder.setWmsInPutawayOrderDets(wmsInnerJobOrderDets);
                    wmsInnerJobOrders.add(wmsInnerJobOrder);
                }
            }
            wmsOutDespatchOrder.setWarehouseId(wmsInnerJobOrder.getWarehouseId());
            WmsOutDespatchOrderReJo wms = new WmsOutDespatchOrderReJo();
            wms.setJobOrderId(wmsInnerJobOrder.getJobOrderId());
            wmsOutDespatchOrderReJos.add(wms);
        }

        if(wmsInnerJobOrders.size()>0){
            this.pickDisQty(wmsInnerJobOrders);
        }

        wmsOutDespatchOrder.setActualDespatchTime(new Date());
        wmsOutDespatchOrder.setPlanDespatchTime(new Date());
        wmsOutDespatchOrder.setWmsOutDespatchOrderReJo(wmsOutDespatchOrderReJos);
        ResponseEntity<String> responseEntity = outFeignApi.add(wmsOutDespatchOrder);
        if(responseEntity.getCode()!=0){
            throw new BizErrorException(responseEntity.getMessage());
        }

        //发运
        ResponseEntity rs = outFeignApi.forwarding(responseEntity.getData());
        if(rs.getCode()!=0){
            throw new BizErrorException(rs.getMessage());
        }

        //领料出库回传接口（五环） 发运调用 回传数量=发运数量 开始
        //获取程序配置项
        if(orderTypeId==(byte)8) {
            SearchSysSpecItem searchSysSpecItemFiveRing = new SearchSysSpecItem();
            searchSysSpecItemFiveRing.setSpecCode("FiveRing");
            List<SysSpecItem> itemListFiveRing = securityFeignApi.findSpecItemList(searchSysSpecItemFiveRing).getData();
            if (itemListFiveRing.size() < 1) {
                throw new BizErrorException("配置项 FiveRing 获取失败");
            }
            SysSpecItem sysSpecItem = itemListFiveRing.get(0);
            if ("1".equals(sysSpecItem.getParaValue())) {
                WmsInnerJobOrder wmsInnerJobOrderIssue=new WmsInnerJobOrder();
                wmsInnerJobOrderIssue.setJobOrderId(outDeliveryOrderId);
                engFeignApi.reportIssueDetails(wmsInnerJobOrderIssue);
            }
        }
        //领料出库回传接口（五环） 发运之前调用 回传数量=发运数量 结束

        return 1;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int sealOrder(List<Long> outDeliveryOrderIds,Byte type) {
        int num=0;
        if(StringUtils.isEmpty(type)){
            for (Long outDeliveryOrderId : outDeliveryOrderIds) {
                //查询当前出库单下所有拣货单
                Example example = new Example(WmsInnerJobOrder.class);
                example.createCriteria().andEqualTo("sourceOrderId",outDeliveryOrderId).andEqualTo("orderTypeId",8).andNotEqualTo("orderStatus",6);
                List<WmsInnerJobOrder> wmsInnerJobOrders = wmsInnerJobOrderMapper.selectByExample(example);
                num = this.sealOrderDet(wmsInnerJobOrders);

                if(wmsInnerJobOrders.size()>0 && wmsInnerJobOrders.get(0).getJobOrderType().equals(8L)) {
                    SearchSysSpecItem searchSysSpecItemFiveRing = new SearchSysSpecItem();
                    searchSysSpecItemFiveRing.setSpecCode("FiveRing");
                    List<SysSpecItem> itemListFiveRing = securityFeignApi.findSpecItemList(searchSysSpecItemFiveRing).getData();
                    if (itemListFiveRing.size() < 1) {
                        throw new BizErrorException("配置项 FiveRing 获取失败");
                    }
                    SysSpecItem sysSpecItem = itemListFiveRing.get(0);
                    if ("1".equals(sysSpecItem.getParaValue())) {
                        outFeignApi.overIssue(outDeliveryOrderId);
                    }
                }
            }
        }else if(type==1){
            //查询超过21天的作业单
            SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
//            searchWmsInnerJobOrder.setSealOrder((byte)1);
            List<WmsInnerJobOrderDto> list = wmsInnerJobOrderMapper.findList(searchWmsInnerJobOrder);
            if(list.size()>0){
                List<WmsInnerJobOrder> wmsInnerJobOrders = new ArrayList<>(list);
                num = this.sealOrderDet(wmsInnerJobOrders);
            }
        }
        return  num;
    }

    /**
     * 领料封单
     * @param wmsInnerJobOrders
     * @return
     */
    private int sealOrderDet(List<WmsInnerJobOrder> wmsInnerJobOrders){
        int num = 0;
        for (WmsInnerJobOrder wmsInnerJobOrder : wmsInnerJobOrders) {
            Example example1 = new Example(WmsInnerJobOrderDet.class);
            example1.createCriteria().andEqualTo("jobOrderId", wmsInnerJobOrder.getJobOrderId()).andNotEqualTo("orderStatus", 6);
            List<WmsInnerJobOrderDet> detList = wmsInnerJobOrderDetMapper.selectByExample(example1);
            for (WmsInnerJobOrderDet wmsInnerJobOrderDet : detList) {
                Map<Byte, BigDecimal> map = new HashMap<>();
                if (StringUtils.isEmpty(wmsInnerJobOrderDet.getActualQty())) {
                    wmsInnerJobOrderDet.setActualQty(BigDecimal.ZERO);
                }
                if (wmsInnerJobOrderDet.getLineStatus() >= 3) {
                    BigDecimal sealQty = wmsInnerJobOrderDet.getDistributionQty().subtract(wmsInnerJobOrderDet.getActualQty());
                    map.put((byte) 1, sealQty);
                    if (wmsInnerJobOrderDet.getActualQty().compareTo(BigDecimal.ZERO) == 1) {
                        map.put((byte) 2, wmsInnerJobOrderDet.getActualQty());
                    }
                } else {
                    if (StringUtils.isEmpty(wmsInnerJobOrderDet.getDistributionQty())) {
                        wmsInnerJobOrderDet.setDistributionQty(BigDecimal.ZERO);
                    }
                    BigDecimal sealQty = wmsInnerJobOrderDet.getPlanQty().subtract(wmsInnerJobOrderDet.getDistributionQty());
                    map.put((byte) 1, sealQty);
                    if (wmsInnerJobOrderDet.getActualQty().compareTo(BigDecimal.ZERO) == 1) {
                        map.put((byte) 2, wmsInnerJobOrderDet.getActualQty());
                    }
                }

                for (Map.Entry<Byte, BigDecimal> byteBigDecimalEntry : map.entrySet()) {
                    BigDecimal sealQty = byteBigDecimalEntry.getValue();
                    //封单数量是否大于0
                    if (sealQty.compareTo(BigDecimal.ZERO) == 1) {
                        //
                        Example example2 = new Example(WmsInnerInventory.class);
                        Example.Criteria criteria = example2.createCriteria();
                        criteria.andEqualTo("relevanceOrderCode", wmsInnerJobOrder.getJobOrderCode()).andEqualTo("materialId", wmsInnerJobOrderDet.getMaterialId()).andEqualTo("warehouseId", wmsInnerJobOrder.getWarehouseId());

                        if(byteBigDecimalEntry.getKey()==1){
                            criteria.andEqualTo("storageId", wmsInnerJobOrderDet.getOutStorageId());
                        }else {
                            criteria.andEqualTo("storageId", wmsInnerJobOrderDet.getInStorageId());
                        }
                        if (StringUtils.isNotEmpty(wmsInnerJobOrderDet.getBatchCode())) {
                            criteria.andEqualTo("batchCode", wmsInnerJobOrderDet.getBatchCode());
                        } else {
                            criteria.andIsNull("batchCode");
                        }
                        criteria.andEqualTo("jobOrderDetId", wmsInnerJobOrderDet.getJobOrderDetId());
                        criteria.andEqualTo("jobStatus", (byte) 2);
                        criteria.andEqualTo("orgId", wmsInnerJobOrder.getOrgId());
                        WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example2);
                        if (StringUtils.isEmpty(wmsInnerInventory)) {
                            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                        }
                        //库存日志
                        //InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrder,wmsInnerJobOrderDet,wmsInnerInventory.getPackingQty(),sealQty,(byte)4,(byte)2);
                        num += wmsInnerInventoryMapper.deleteByPrimaryKey(wmsInnerInventory.getInventoryId());

                        //恢复库存
                        example2.clear();
                        Example.Criteria criterias = example2.createCriteria();
                        criterias.andEqualTo("materialId", wmsInnerJobOrderDet.getMaterialId()).andEqualTo("warehouseId", wmsInnerJobOrder.getWarehouseId()).andEqualTo("storageId", wmsInnerJobOrderDet.getOutStorageId());
                        if (StringUtils.isNotEmpty(wmsInnerJobOrderDet.getBatchCode())) {
                            criterias.andEqualTo("batchCode", wmsInnerJobOrderDet.getBatchCode());
                        } else {
                            criterias.andIsNull("batchCode");
                        }
                        criterias.andEqualTo("jobStatus", (byte) 1);
                        criterias.andEqualTo("orgId", wmsInnerJobOrder.getOrgId());
                        criteria.andGreaterThan("packingQty",0);
                        criteria.andEqualTo("inventoryStatusId",wmsInnerJobOrderDet.getInventoryStatusId());
                        WmsInnerInventory innerInventory = wmsInnerInventoryMapper.selectOneByExample(example2);
                        if (StringUtils.isEmpty(innerInventory)) {
                            BeanUtil.copyProperties(wmsInnerInventory, innerInventory);
                            innerInventory.setInventoryId(null);
                            innerInventory.setJobOrderDetId(null);
                            innerInventory.setJobStatus((byte) 1);
                            num += wmsInnerInventoryMapper.insertSelective(innerInventory);
                        } else {
                            if (StringUtils.isEmpty(innerInventory.getPackingQty())) {
                                innerInventory.setPackingQty(BigDecimal.ZERO);
                            }
                            //库存日志
                            Long outStorageId = wmsInnerJobOrderDet.getOutStorageId();
                            wmsInnerJobOrderDet.setOutStorageId(wmsInnerJobOrderDet.getInStorageId());
                            InventoryLogUtil.addLog(wmsInnerInventory, wmsInnerJobOrder, wmsInnerJobOrderDet, wmsInnerInventory.getPackingQty(), sealQty, (byte) 4, (byte) 1);
                            wmsInnerJobOrderDet.setOutStorageId(outStorageId);
                            innerInventory.setPackingQty(innerInventory.getPackingQty().add(sealQty));
                            num += wmsInnerInventoryMapper.updateByPrimaryKeySelective(innerInventory);
                        }
                    }

                    wmsInnerJobOrderDet.setActualQty(BigDecimal.ZERO);
                    wmsInnerJobOrderDet.setDistributionQty(BigDecimal.ZERO);
                    //wmsInnerJobOrderDet.setOrderStatus((byte) 6);
                    if (StringUtils.isEmpty(wmsInnerJobOrderDet.getWorkStartTime())) {
                        wmsInnerJobOrderDet.setWorkStartTime(new Date());
                    }
                    wmsInnerJobOrderDet.setWorkEndTime(new Date());
                    wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInnerJobOrderDet);

                    //清除redis
                    this.removeRedis(wmsInnerJobOrderDet.getJobOrderDetId());
                }
                wmsInnerJobOrder.setOrderStatus((byte) 6);
                if (StringUtils.isEmpty(wmsInnerJobOrder.getWorkStartTime())) {
                    wmsInnerJobOrder.setWorkStartTime(new Date());
                }
                wmsInnerJobOrder.setWorkEndtTime(new Date());
                wmsInnerJobOrderMapper.updateByPrimaryKeySelective(wmsInnerJobOrder);

                //更新出库单
//                ResponseEntity responseEntity = outFeignApi.sealOrder(wmsInnerJobOrder.getSourceOrderId());
//                if (responseEntity.getCode() != 0) {
//                    throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
//                }
            }
        }
        return num;
    }

    private int pickDisQty(List<WmsInnerJobOrder> wmsInnerJobOrders){
        int num = 0;
        for (WmsInnerJobOrder wmsInnerJobOrder : wmsInnerJobOrders) {
            for (WmsInnerJobOrderDet det : wmsInnerJobOrder.getWmsInPutawayOrderDets()) {
                //领料拣货单
                //拣货数量小于分配数量
                if(det.getActualQty().compareTo(det.getDistributionQty())==-1){
                    //已分配未拣货数量 = 分配数量-拣货数量
                    BigDecimal qty = det.getDistributionQty().subtract(det.getActualQty());
                    WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
                    BeanUtil.copyProperties(det,wmsInnerJobOrderDet);
                    wmsInnerJobOrderDet.setJobOrderDetId(null);
                    wmsInnerJobOrderDet.setWorkStartTime(null);
                    wmsInnerJobOrderDet.setWorkEndTime(null);
                    wmsInnerJobOrderDet.setLineStatus((byte)2);
                    wmsInnerJobOrderDet.setPlanQty(qty);
                    wmsInnerJobOrderDet.setDistributionQty(qty);
                    wmsInnerJobOrderDet.setActualQty(BigDecimal.ZERO);
                    wmsInnerJobOrderDetMapper.insertUseGeneratedKeys(wmsInnerJobOrderDet);
                    if(redisUtil.hasKey("PICKINGID:"+det.getJobOrderDetId())){
                        Example example = new Example(WmsInnerInventory.class);
                        Example.Criteria criteria = example.createCriteria();
                        criteria.andEqualTo("relevanceOrderCode",wmsInnerJobOrder.getJobOrderCode()).andEqualTo("materialId",det.getMaterialId()).andEqualTo("warehouseId",wmsInnerJobOrder.getWarehouseId()).andEqualTo("storageId",det.getOutStorageId());
                        if(!StringUtils.isEmpty(wmsInnerJobOrderDet.getBatchCode())){
                            criteria.andEqualTo("batchCode",det.getBatchCode());
                        }
                        criteria.andEqualTo("jobOrderDetId",det.getJobOrderDetId());
                        criteria.andEqualTo("jobStatus",(byte)2);
                        criteria.andEqualTo("orgId",wmsInnerJobOrder.getOrgId());
                        WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
                        if(StringUtils.isEmpty(wmsInnerInventory)){
                            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                        }
                        wmsInnerInventory.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
                        wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventory);

                        //已拣货数量 = 分配-拣货
                        BigDecimal pickQty = det.getDistributionQty().subtract(det.getActualQty());
                        Map<Long,BigDecimal> map = (Map<Long, BigDecimal>) redisUtil.get("PICKINGID:"+det.getJobOrderDetId().toString());
                        Map<Long,BigDecimal> bigDecimalMap = new HashMap<>();
                        for (Map.Entry<Long, BigDecimal> m : map.entrySet()){
                            BigDecimal mQty = new BigDecimal(String.valueOf(m.getValue()));
                            if(pickQty.compareTo(BigDecimal.ZERO)==1){
                                if(mQty.compareTo(pickQty)==1){
                                    if(mQty.subtract(pickQty).compareTo(BigDecimal.ZERO)==1){
                                        bigDecimalMap.put(m.getKey(),mQty);
                                    }
                                    pickQty.subtract(mQty);
                                }
                            }else {
                                bigDecimalMap.put(m.getKey(),mQty);
                            }
                        }
                        //设置3秒后失效
                        redisUtil.expire("PICKINGID:"+det.getJobOrderDetId(),3);

                        redisUtil.set("PICKINGID:"+wmsInnerJobOrderDet.getJobOrderDetId(),bigDecimalMap);
                    }
                    det.setPlanQty(det.getPlanQty().subtract(qty));
                    det.setDistributionQty(det.getDistributionQty().subtract(qty));
                    det.setWorkEndTime(new Date());
                    wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(det);
                }
            }
        }
        return num;
    }


    /**
     * 领料单拣货确认
     * //领料拣货单存在超拣货及少减情况根据配置项判断是否可以超减或者少减 一但拣货则状态都为拣货中 直到发运完成
     * @param wmsInnerJobOrder
     * @param allOrder 整单或者单一 1-整单 0-单一
     * @return
     */
    private int PickingConfirmation(WmsInnerJobOrder wmsInnerJobOrder,Integer allOrder){
        int num = 0;
        wmsInnerJobOrder.setOrderStatus((byte)4);
        BigDecimal totalQty = BigDecimal.ZERO;
        //获取程序配置项
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("stockRequisition");
        List<SysSpecItem> itemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        if(itemList.size()<1){
            throw new BizErrorException("领料拣货配置项获取失败");
        }
        Map<String ,String> map = JSONArray.parseObject(itemList.get(0).getParaValue(), Map.class);
            for (WmsInnerJobOrderDet wmsInPutawayOrderDet : wmsInnerJobOrder.getWmsInPutawayOrderDets()) {
                if (allOrder == 1){
                    //整单确认只针对没有拣货的但及拣货数量未达到计划数量的单据
                    if (StringUtils.isEmpty(wmsInPutawayOrderDet.getActualQty()) || wmsInPutawayOrderDet.getActualQty().compareTo(wmsInPutawayOrderDet.getPlanQty()) == -1) {
                        BigDecimal actualQty = BigDecimal.ZERO;
                        if (StringUtils.isEmpty(wmsInPutawayOrderDet.getActualQty())) {
                            actualQty = wmsInPutawayOrderDet.getDistributionQty();
                            wmsInPutawayOrderDet.setActualQty(actualQty);
                        } else {
                            //差值=分配数量-已确认数量
                            actualQty = wmsInPutawayOrderDet.getDistributionQty().subtract(wmsInPutawayOrderDet.getActualQty());
                            wmsInPutawayOrderDet.setActualQty(wmsInPutawayOrderDet.getActualQty().add(actualQty));
                        }
                        //判断是否允许超收
//                        if(map.get("beyond").equals("false")){
//                            wmsInPutawayOrderDet.setOrderStatus((byte)5);
//                        }else {
//                            wmsInPutawayOrderDet.setOrderStatus((byte)4);
//                        }
                        this.addDistribute(false,wmsInnerJobOrder,wmsInPutawayOrderDet);
                        wmsInPutawayOrderDet.setWorkEndTime(new Date());
                        totalQty = totalQty.add(actualQty);
                    }
                }else if(allOrder==0){
                    //单一确认判断是否超减
                    WmsInnerJobOrderDet det = wmsInnerJobOrderDetMapper.selectByPrimaryKey(wmsInPutawayOrderDet.getJobOrderDetId());
                    if(StringUtils.isNotEmpty(det.getActualQty()) && det.getActualQty().compareTo(det.getDistributionQty())>-1){
                        throw new BizErrorException("领料单不允许继续超拣");
                    }
                    WmsInnerInventory wmsInnerInventory = null;
                    Boolean isBeyond = false;
                    //已拣货数量为空
                    if(StringUtils.isEmpty(det.getActualQty())){
                        // 拣货数量小于等于计划数量
                        if(wmsInPutawayOrderDet.getActualQty().compareTo(det.getPlanQty())<1){
                            isBeyond = false;
                            if(wmsInPutawayOrderDet.getActualQty().compareTo(det.getPlanQty())==0){
                                wmsInPutawayOrderDet.setWorkEndTime(new Date());
                            }
                        }
                        //拣货数量大于计划数量
                        else if (wmsInPutawayOrderDet.getActualQty().compareTo(det.getPlanQty())==1){
                            //是否允许超拣
                            if(map.get("beyond").equals("false")){
                                //不允许超拣
                                throw new BizErrorException("领料拣货单超出计划数量");
                            }else {
                                isBeyond=true;
                            }
                        }
                    }else {
                        //已拣货数量加拣货数量小于等于计划数量
                        if(det.getActualQty().add(wmsInPutawayOrderDet.getActualQty()).compareTo(det.getPlanQty())<1){
                            isBeyond = false;
                            if(det.getActualQty().add(wmsInPutawayOrderDet.getActualQty()).compareTo(det.getPlanQty())==0){
                                wmsInPutawayOrderDet.setWorkEndTime(new Date());
                            }
                        }
                        //已拣货数量加拣货数量大于计划数量
                        else if(det.getActualQty().add(wmsInPutawayOrderDet.getActualQty()).compareTo(det.getPlanQty())==1){
                            //是否允许超拣
                            if(map.get("beyond").equals("false")){
                                //不允许超拣
                                throw new BizErrorException("领料拣货单超出计划数量");
                            }else {
                                isBeyond=true;
                            }
                        }
                    }
                    if(map.get("lack").equals("false") && map.get("beyond").equals("false")){
                        if(StringUtils.isEmpty(det.getActualQty())){
                            det.setActualQty(BigDecimal.ZERO);
                        }
                        if(det.getActualQty().add(wmsInPutawayOrderDet.getActualQty()).compareTo(det.getPlanQty())==0) {
                            wmsInPutawayOrderDet.setLineStatus((byte) 3);
                            wmsInPutawayOrderDet.setWorkEndTime(new Date());
                        }else {
                            wmsInPutawayOrderDet.setLineStatus((byte)2);
                        }
                    }else {
                        wmsInPutawayOrderDet.setLineStatus((byte)2);
                    }
                    totalQty = totalQty.add(wmsInPutawayOrderDet.getActualQty());
                    this.addDistribute(isBeyond,wmsInnerJobOrder,wmsInPutawayOrderDet);
                    if(StringUtils.isNotEmpty(det.getActualQty())){
                        wmsInPutawayOrderDet.setActualQty(wmsInPutawayOrderDet.getActualQty().add(det.getActualQty()));
                    }
                }
                if(totalQty.compareTo(BigDecimal.ZERO)==1){
                    //作业中
                    if(StringUtils.isEmpty(wmsInPutawayOrderDet.getWorkStartTime())){
                        wmsInPutawayOrderDet.setWorkStartTime(new Date());
                    }
                    //wmsInPutawayOrderDet.setOrderStatus((byte)4);
                    num+=wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
                    //反写出库单拣货数量
                    num+=this.writeDeliveryOrderQty(wmsInPutawayOrderDet);
                    if(wmsInPutawayOrderDet.getLineStatus()==3){
                        //清除redis
                        this.removeRedis(wmsInPutawayOrderDet.getJobOrderDetId());
                    }
                }
            }
            if(StringUtils.isEmpty(wmsInnerJobOrder.getWorkStartTime())){
                wmsInnerJobOrder.setWorkStartTime(new Date());
            }
        //更新为拣货中状态
//        if(StringUtils.isEmpty(wmsInnerJobOrder.getActualQty())){
//            wmsInnerJobOrder.setActualQty(totalQty);
//        }else {
//           wmsInnerJobOrder.setActualQty(wmsInnerJobOrder.getActualQty().add(totalQty));
//        }
//        if(wmsInnerJobOrder.getActualQty().compareTo(wmsInnerJobOrder.getPlanQty())>-1){
//            wmsInnerJobOrder.setWorkEndtTime(new Date());
//            if(map.get("beyond").equals("false")){
//                wmsInnerJobOrder.setOrderStatus((byte)5);
//            }else {
//                wmsInnerJobOrder.setOrderStatus((byte)4);
//            }
//        }
        num+=wmsInnerJobOrderMapper.updateByPrimaryKeySelective(wmsInnerJobOrder);

        return num;
    }

    private void addDistribute(Boolean isBeyond,WmsInnerJobOrder wmsInnerJobOrder,WmsInnerJobOrderDet wmsInPutawayOrderDet){
        WmsInnerInventory wmsInnerInventory = null;
//        if(isBeyond){
//
//        }else {
            //扣件分配库存
            Example example = new Example(WmsInnerInventory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("relevanceOrderCode",wmsInnerJobOrder.getJobOrderCode()).andEqualTo("materialId",wmsInPutawayOrderDet.getMaterialId()).andEqualTo("warehouseId",wmsInnerJobOrder.getWarehouseId()).andEqualTo("storageId",wmsInPutawayOrderDet.getOutStorageId());
            if(!StringUtils.isEmpty(wmsInPutawayOrderDet.getBatchCode())){
                criteria.andEqualTo("batchCode",wmsInPutawayOrderDet.getBatchCode());
            }
            criteria.andEqualTo("jobOrderDetId",wmsInPutawayOrderDet.getJobOrderDetId());
            criteria.andEqualTo("jobStatus",(byte)2);
            criteria.andEqualTo("orgId",wmsInnerJobOrder.getOrgId());
            wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
            if(StringUtils.isEmpty(wmsInnerInventory)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            WmsInnerInventory wmsIn = new WmsInnerInventory();
            wmsIn.setInventoryId(wmsInnerInventory.getInventoryId());
            wmsIn.setPackingQty(wmsInnerInventory.getPackingQty());
            if(isBeyond){
                BigDecimal qty = wmsInPutawayOrderDet.getActualQty();
                if(StringUtils.isNotEmpty(wmsIn.getPackingQty()) && wmsInPutawayOrderDet.getActualQty().compareTo(wmsIn.getPackingQty())>-1){
                    InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrder,wmsInPutawayOrderDet,wmsInnerInventory.getPackingQty(),wmsIn.getPackingQty(),(byte)4,(byte)2);

                    qty = wmsInPutawayOrderDet.getActualQty().subtract(wmsIn.getPackingQty());
                    wmsIn.setPackingQty(wmsIn.getPackingQty().subtract(wmsIn.getPackingQty()));
                    wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsIn);
                }
                if(StringUtils.isNotEmpty(qty) && qty.compareTo(BigDecimal.ZERO)==1){
                    //查询库存是否足够
                    WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto = new WmsInnerJobOrderDetDto();
                    BeanUtil.copyProperties(wmsInPutawayOrderDet,wmsInnerJobOrderDetDto);
                    wmsInnerJobOrderDetDto.setActualQty(qty);
                    this.subtract(wmsInnerJobOrder,wmsInnerJobOrderDetDto,3,null);
                }
            }else {
                InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrder,wmsInPutawayOrderDet,getInvQty(wmsInPutawayOrderDet.getJobOrderDetId(),wmsInPutawayOrderDet.getActualQty()),wmsInPutawayOrderDet.getActualQty(),(byte)4,(byte)2);
                wmsIn.setPackingQty(wmsInnerInventory.getPackingQty().subtract(wmsInPutawayOrderDet.getActualQty()));
                wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsIn);
            }
        //}
        //加库存
        example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("relevanceOrderCode",wmsInnerJobOrder.getJobOrderCode()).andEqualTo("materialId",wmsInPutawayOrderDet.getMaterialId()).andEqualTo("warehouseId",wmsInnerJobOrder.getWarehouseId()).andEqualTo("storageId",wmsInPutawayOrderDet.getInStorageId());
        if(!StringUtils.isEmpty(wmsInPutawayOrderDet.getBatchCode())){
            criteria1.andEqualTo("batchCode",wmsInPutawayOrderDet.getBatchCode());
        }
        criteria1.andEqualTo("jobOrderDetId",wmsInPutawayOrderDet.getJobOrderDetId());
        criteria1.andEqualTo("jobStatus",(byte)2);
        criteria1.andEqualTo("orgId",wmsInnerJobOrder.getOrgId());
        WmsInnerInventory wmsInnerInventorys = wmsInnerInventoryMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(wmsInnerInventorys)){
            //添加库存
            WmsInnerInventory inv = new WmsInnerInventory();
            BeanUtil.copyProperties(wmsInnerInventory,inv);
            inv.setStorageId(wmsInPutawayOrderDet.getInStorageId());
            inv.setWarehouseId(wmsInnerJobOrder.getWarehouseId());
            inv.setRelevanceOrderCode(wmsInnerJobOrder.getJobOrderCode());
            inv.setPackingQty(wmsInPutawayOrderDet.getActualQty());
            inv.setJobStatus((byte)2);
            inv.setJobOrderDetId(wmsInPutawayOrderDet.getJobOrderDetId());
            inv.setInventoryId(null);
            inv.setCreateTime(new Date());
            inv.setCreateUserId(wmsInnerJobOrder.getModifiedUserId());
            inv.setModifiedUserId(wmsInnerJobOrder.getModifiedUserId());
            inv.setModifiedTime(new Date());
            inv.setOrgId(wmsInnerJobOrder.getOrgId());
            wmsInnerInventoryMapper.insertSelective(inv);
            InventoryLogUtil.addLog(wmsInnerInventorys,wmsInnerJobOrder,wmsInPutawayOrderDet,BigDecimal.ZERO,inv.getPackingQty(),(byte)4,(byte)1);
        }else{
            //原库存
            InventoryLogUtil.addLog(wmsInnerInventorys,wmsInnerJobOrder,wmsInPutawayOrderDet,wmsInnerInventorys.getPackingQty(),wmsInPutawayOrderDet.getActualQty(),(byte)4,(byte)1);
            wmsInnerInventorys.setPackingQty(wmsInnerInventorys.getPackingQty().add(wmsInPutawayOrderDet.getActualQty()));
            wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
        }
    }

    /**
     * 获取当前登录用户
     * @return
     */
    private SysUser currentUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }

    /**
     * 从Excel导入数据
     * @return
     */
    @Override
    public Map<String, Object> importExcel(List<WmsInnerJobOrderImport> wmsInnerJobOrderImportsTemp) throws ParseException {
        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        List<WmsInnerJobOrderImport> wmsInnerJobOrderImports=new ArrayList<>();
        for (WmsInnerJobOrderImport wmsInnerJobOrderImport : wmsInnerJobOrderImportsTemp) {
            if(StringUtils.isNotEmpty(wmsInnerJobOrderImport.getOutStorageName())){
                wmsInnerJobOrderImports.add(wmsInnerJobOrderImport);
            }
        }
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        //排除不合法的数据
        Iterator<WmsInnerJobOrderImport> iterator = wmsInnerJobOrderImports.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            WmsInnerJobOrderImport wmsInnerJobOrderImport = iterator.next();
            String warehouseName = wmsInnerJobOrderImport.getWarehouseName();
            String materialCode = wmsInnerJobOrderImport.getMaterialCode();
            String outStorageName=wmsInnerJobOrderImport.getOutStorageName();
            String planQty = wmsInnerJobOrderImport.getPlanQty().toString();

            //判断必传字段
            if (StringUtils.isEmpty(
                    warehouseName,materialCode,outStorageName,planQty
            )) {
                fail.add(i + 4);
                iterator.remove();
                i++;
                continue;
            }

            //判断物料信息是否存在
            SearchBaseMaterial searchBaseMaterial=new SearchBaseMaterial();
            searchBaseMaterial.setMaterialCode(materialCode);
            ResponseEntity<List<BaseMaterial>> baseMaterialList=baseFeignApi.findList(searchBaseMaterial);
            if(StringUtils.isNotEmpty(baseMaterialList.getData())){
                BaseMaterial baseMaterial=baseMaterialList.getData().get(0);
                if (StringUtils.isEmpty(baseMaterial)){
                    fail.add(i + 4);
                    iterator.remove();
                    i++;
                    continue;
                }
                wmsInnerJobOrderImport.setMaterialId(baseMaterial.getMaterialId());
                i++;
            }

            //仓库是否存在
            SearchBaseWarehouse searchBaseWarehouse=new SearchBaseWarehouse();
            searchBaseWarehouse.setWarehouseName(warehouseName);
            ResponseEntity<List<BaseWarehouse>> listResponseEntity=baseFeignApi.findList(searchBaseWarehouse);
            if(StringUtils.isNotEmpty(listResponseEntity.getData())){
                BaseWarehouse baseWarehouse=listResponseEntity.getData().get(0);
                if (StringUtils.isEmpty(baseWarehouse)){
                    fail.add(i + 4);
                    iterator.remove();
                    i++;
                    continue;
                }
                wmsInnerJobOrderImport.setWarehouseId(baseWarehouse.getWarehouseId());
                i++;
            }

            //移出库位是否存在
            SearchBaseStorage searchBaseStorage=new SearchBaseStorage();
            searchBaseStorage.setStorageName(outStorageName);
            ResponseEntity<List<BaseStorage>> baseStorageRe=baseFeignApi.findList(searchBaseStorage);
            if(StringUtils.isNotEmpty(baseStorageRe.getData())){
                BaseStorage baseStorage=baseStorageRe.getData().get(0);
                if (StringUtils.isEmpty(baseStorage)){
                    fail.add(i + 4);
                    iterator.remove();
                    i++;
                    continue;
                }
                wmsInnerJobOrderImport.setOutStorageId(baseStorage.getStorageId());
                i++;
            }
        }

        //对合格数据进行分组
        Map<Long, List<WmsInnerJobOrderImport>> map = wmsInnerJobOrderImports.stream().collect(Collectors.groupingBy(WmsInnerJobOrderImport::getWarehouseId, HashMap::new, Collectors.toList()));
        Set<Long> codeList = map.keySet();
        for (Long code : codeList) {
            List<WmsInnerJobOrderImport> wmsInnerJobOrderImport1 = map.get(code);

            //新增表头 WmsInnerJobOrder
            Long jobOrderId=null;
            if(StringUtils.isEmpty(jobOrderId)){
                WmsInnerJobOrder wmsInnerJobOrder=new WmsInnerJobOrder();
                wmsInnerJobOrder.setJobOrderType((byte)2);
                wmsInnerJobOrder.setWarehouseId(code);
                wmsInnerJobOrder.setStatus((byte)1);
                wmsInnerJobOrder.setOrgId(currentUser.getOrganizationId());
                wmsInnerJobOrder.setCreateUserId(currentUser.getUserId());
                wmsInnerJobOrder.setCreateTime(new Date());
                wmsInnerJobOrderMapper.insertUseGeneratedKeys(wmsInnerJobOrder);
                jobOrderId=wmsInnerJobOrder.getJobOrderId();
            }
            //新增明细 WmsInnerJobOrderDet
            if(StringUtils.isNotEmpty(jobOrderId)) {
                List<WmsInnerJobOrderDet> wmsInnerJobOrderDetList=new ArrayList<>();
                for (WmsInnerJobOrderImport item : wmsInnerJobOrderImport1) {

                    WmsInnerJobOrderDet wmsInnerJobOrderDetNew=new WmsInnerJobOrderDet();
                    wmsInnerJobOrderDetNew.setJobOrderId(jobOrderId);
                    wmsInnerJobOrderDetNew.setOutStorageId(item.getOutStorageId());
                    wmsInnerJobOrderDetNew.setMaterialId(item.getMaterialId());
                    wmsInnerJobOrderDetNew.setPlanQty(new BigDecimal(item.getPlanQty()));
                    wmsInnerJobOrderDetNew.setOrgId(currentUser.getOrganizationId());
                    wmsInnerJobOrderDetNew.setCreateUserId(currentUser.getUserId());
                    wmsInnerJobOrderDetNew.setCreateTime(new Date());
                    wmsInnerJobOrderDetList.add(wmsInnerJobOrderDetNew);
                }

                if(wmsInnerJobOrderDetList.size()>0){
                    success += wmsInnerJobOrderDetMapper.insertList(wmsInnerJobOrderDetList);
                }
            }
        }
        resultMap.put("操作成功总数", success);
        resultMap.put("操作失败行", fail);
        return resultMap;
    }
}
