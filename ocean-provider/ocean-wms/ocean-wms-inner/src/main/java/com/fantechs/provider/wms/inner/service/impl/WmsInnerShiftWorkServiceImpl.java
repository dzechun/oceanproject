package com.fantechs.provider.wms.inner.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseWorkerDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeReOrderDto;
import com.fantechs.common.base.general.dto.wms.inner.export.WmsInnerJobOrderExport;
import com.fantechs.common.base.general.dto.wms.inner.imports.WmsInnerJobOrderImport;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWorker;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrder;
import com.fantechs.common.base.general.entity.wms.inner.*;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.api.qms.OMFeignApi;
import com.fantechs.provider.api.wms.in.InFeignApi;
import com.fantechs.provider.wms.inner.mapper.*;
import com.fantechs.provider.wms.inner.service.WmsInnerInventoryService;
import com.fantechs.provider.wms.inner.service.WmsInnerMaterialBarcodeReOrderService;
import com.fantechs.provider.wms.inner.service.WmsInnerShiftWorkDetService;
import com.fantechs.provider.wms.inner.service.WmsInnerShiftWorkService;
import com.fantechs.provider.wms.inner.util.InventoryLogUtil;
import com.fantechs.provider.wms.inner.util.WmsInnerInventoryUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Mr.Lei on 2021/05/06.
 */
@Service
public class WmsInnerShiftWorkServiceImpl extends BaseService<WmsInnerJobOrder> implements WmsInnerShiftWorkService {
    @Resource
    private WmsInnerJobOrderMapper wmsInnerJobOrderMapper;
    @Resource
    private WmsInnerJobOrderDetMapper wmsInnerJobOrderDetMapper;
    @Resource
    private WmsInnerShiftWorkDetService wmsInnerShiftWorkDetService;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private WmsInnerInventoryMapper wmsInnerInventoryMapper;
    @Resource
    private WmsInnerJobOrderReMsppMapper wmsInnerJobOrderReMsppMapper;
    @Resource
    private SFCFeignApi sfcFeignApi;
    @Resource
    private InFeignApi inFeignApi;
    @Resource
    private OMFeignApi omFeignApi;
    @Resource
    private WmsInnerInventoryDetMapper wmsInnerInventoryDetMapper;
    @Resource
    private WmsInnerInventoryService wmsInnerInventoryService;
    @Resource
    private WmsInnerMaterialBarcodeMapper wmsInnerMaterialBarcodeMapper;
    @Resource
    private WmsInnerMaterialBarcodeReOrderService wmsInnerMaterialBarcodeReOrderService;
    @Resource
    private WmsInnerMaterialBarcodeReOrderMapper wmsInnerMaterialBarcodeReOrderMapper;


    @Override
    public List<WmsInnerJobOrderDto> findList(SearchWmsInnerJobOrder searchWmsInnerJobOrder) {
        if (StringUtils.isEmpty(searchWmsInnerJobOrder.getOrgId())) {
            SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
            searchWmsInnerJobOrder.setOrgId(sysUser.getOrganizationId());
        }
        return wmsInnerJobOrderMapper.findList(searchWmsInnerJobOrder);
    }

    @Override
    public List<WmsInnerJobOrderDto> findShiftList(Map<String, Object> map) {
        return wmsInnerJobOrderMapper.findShiftList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int batchDeleteByShiftWork(String ids) {
        String[] arrId = ids.split(",");
        for (String s : arrId) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(s);
            if (wmsInnerJobOrder.getOrderStatus() ==4 || wmsInnerJobOrder.getOrderStatus() ==5) {
                throw new BizErrorException("单据已经作业，无法删除");
            }
            if (StringUtils.isEmpty(wmsInnerJobOrder)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId", s);
            List<WmsInnerJobOrderDet> jobOrderDetList = wmsInnerJobOrderDetMapper.selectByExample(example);
            for (WmsInnerJobOrderDet det : jobOrderDetList) {
                wmsInnerJobOrderDetMapper.delete(det);
                // 查询明细对应的库存
                Example exampleInventory = new Example(WmsInnerInventory.class);
                exampleInventory.createCriteria().andEqualTo("jobOrderDetId", det.getJobOrderDetId());
                WmsInnerInventory innerInventory = wmsInnerInventoryMapper.selectOneByExample(exampleInventory);
                // 查询明细库存对应的原库存
                WmsInnerInventory sourceInnerInventory = wmsInnerInventoryMapper.selectByPrimaryKey(innerInventory.getParentInventoryId());
                sourceInnerInventory.setPackingQty(sourceInnerInventory.getPackingQty().add(innerInventory.getPackingQty()));
                // 修改原库存
                wmsInnerInventoryService.update(sourceInnerInventory);
                // 删除明细库存
                wmsInnerInventoryMapper.deleteByPrimaryKey(innerInventory);

            }
        }
        return wmsInnerJobOrderMapper.deleteByIds(ids);
    }

    /**
     * 手动分配
     *
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int handDistribution(List<WmsInnerJobOrderDet> list) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        int num = 0;
        for (WmsInnerJobOrderDet wmsInPutawayOrderDet : list) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(wmsInPutawayOrderDet.getJobOrderId());
            if (StringUtils.isEmpty(wmsInnerJobOrder.getWarehouseId())) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(), "请先输入仓库再进行分配作业 上架单号-->" + wmsInnerJobOrder.getJobOrderCode());
            }
            if (!StringUtils.isEmpty(wmsInPutawayOrderDet.getDistributionQty()) && wmsInPutawayOrderDet.getDistributionQty().doubleValue() > wmsInPutawayOrderDet.getPlanQty().doubleValue()) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(), "分配数量不能大于计划数量");
            }
            Long id = null;
            BigDecimal distributionQty = BigDecimal.ZERO;
            WmsInnerJobOrderDet wms = new WmsInnerJobOrderDet();

            if (StringUtils.isEmpty(wmsInPutawayOrderDet.getInStorageId())) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "移入库位ID不能为空");
            }

            if (StringUtils.isEmpty(wmsInPutawayOrderDet.getOutStorageId())) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "移出库位ID不能为空");
            }

            if (wmsInPutawayOrderDet.getInStorageId().equals(wmsInPutawayOrderDet.getOutStorageId())) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(), "移入库位不能与移出库位相同");
            }

            //当货品分配时未全部分配完时新增一条剩余待分配数量的记录
            if (StringUtils.isNotEmpty(wmsInPutawayOrderDet.getDistributionQty()) && wmsInPutawayOrderDet.getDistributionQty().compareTo(wmsInPutawayOrderDet.getPlanQty()) == -1) {
                //分配中
                wms = new WmsInnerJobOrderDet();
                BeanUtil.copyProperties(wmsInPutawayOrderDet, wms);
                wms.setJobOrderDetId(null);
                wms.setPlanQty(wmsInPutawayOrderDet.getDistributionQty());
                wms.setDistributionQty(wmsInPutawayOrderDet.getDistributionQty());
                if (wmsInnerJobOrder.getJobOrderType() == (byte) 1)
                    wms.setLineStatus((byte) 2);
                else if (wmsInnerJobOrder.getJobOrderType() == (byte) 3)
                    wms.setLineStatus((byte) 1);
                num += wmsInnerJobOrderDetMapper.insertUseGeneratedKeys(wms);
                id = wms.getJobOrderDetId();

                wmsInPutawayOrderDet.setLineStatus((byte) 1);
                distributionQty = wmsInPutawayOrderDet.getDistributionQty();
                wmsInPutawayOrderDet.setDistributionQty(null);
                wmsInPutawayOrderDet.setInStorageId(null);
                wmsInPutawayOrderDet.setPlanQty(wmsInPutawayOrderDet.getPlanQty().subtract(wms.getDistributionQty()));
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                num += wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
            } else if (wmsInPutawayOrderDet.getDistributionQty().compareTo(wmsInPutawayOrderDet.getPlanQty()) == 0) {
                //分配完成
                if (wmsInnerJobOrder.getJobOrderType() == (byte) 1)
                    wmsInPutawayOrderDet.setLineStatus((byte) 2);
                else if (wmsInnerJobOrder.getJobOrderType() == (byte) 3)
                    wmsInPutawayOrderDet.setLineStatus((byte) 1);

                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                num += wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
                id = wmsInPutawayOrderDet.getJobOrderDetId();
                wms = wmsInPutawayOrderDet;
            }
            SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
            searchWmsInnerJobOrderDet.setJobOrderId(wmsInPutawayOrderDet.getJobOrderId());
            List<WmsInnerJobOrderDetDto> dto = wmsInnerJobOrderDetMapper.findList(searchWmsInnerJobOrderDet);

            SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
            searchWmsInnerJobOrder.setJobOrderId(wmsInPutawayOrderDet.getJobOrderId());
            WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInnerJobOrderMapper.findList(searchWmsInnerJobOrder).get(0);
            searchWmsInnerJobOrderDet.setJobOrderDetId(wmsInPutawayOrderDet.getJobOrderDetId());
            WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto = wmsInnerJobOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);

            if (wmsInnerJobOrderDto.getJobOrderType() == (byte) 1) {
                //上架分配库存
                //num += this.updateInventory(wmsInnerJobOrderDto, wms);
                num += WmsInnerInventoryUtil.distributionInventory(wmsInnerJobOrderDto, wms, wms.getDistributionQty(), sysUser, (byte) 1);
            } else if (wmsInnerJobOrderDto.getJobOrderType() == (byte) 3) {
                //旧  移位作业 手动分配逻辑
                Example example = new Example(WmsInnerInventory.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("materialId", wmsInnerJobOrderDetDto.getMaterialId())
                        .andEqualTo("warehouseId", wmsInnerJobOrderDto.getWarehouseId())
                        .andEqualTo("storageId", wmsInnerJobOrderDetDto.getOutStorageId())
                        .andEqualTo("jobOrderDetId", wmsInPutawayOrderDet.getJobOrderDetId())
                        .andEqualTo("jobStatus", (byte) 2)
                        .andEqualTo("stockLock", 0)
                        .andEqualTo("lockStatus", 0)
                        .andEqualTo("orgId", sysUser.getOrganizationId());
                WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
                if (StringUtils.isEmpty(wmsInnerInventory)) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                }
                if (StringUtils.isEmpty(wmsInnerInventory.getPackingQty()) || wmsInnerInventory.getPackingQty().compareTo(BigDecimal.ZERO) == -1) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(), "数据变动,请恢复单据");
                }
                WmsInnerInventory wmsIn = new WmsInnerInventory();
                wmsIn.setInventoryId(wmsInnerInventory.getInventoryId());
                wmsIn.setPackingQty(wmsInnerInventory.getPackingQty().subtract(distributionQty));
                num += wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsIn);

                example = new Example(WmsInnerInventory.class);
                Example.Criteria criteria1 = example.createCriteria();
                criteria1.andEqualTo("materialId", wmsInnerJobOrderDetDto.getMaterialId())
                        .andEqualTo("warehouseId", wmsInnerJobOrderDto.getWarehouseId())
                        .andEqualTo("storageId", wmsInnerJobOrderDetDto.getOutStorageId())
                        .andEqualTo("jobOrderDetId", id)
                        .andEqualTo("jobStatus", (byte) 2)
                        .andEqualTo("stockLock", 0)
                        .andEqualTo("lockStatus", 0)
                        .andEqualTo("orgId", sysUser.getOrganizationId());
                WmsInnerInventory wmsInnerInventorys = wmsInnerInventoryMapper.selectOneByExample(example);
                if (StringUtils.isEmpty(wmsInnerInventorys)) {
                    //添加库存
                    WmsInnerInventory inv = new WmsInnerInventory();
                    BeanUtil.copyProperties(wmsInnerInventory, inv);
                    inv.setStorageId(wmsInnerJobOrderDetDto.getOutStorageId());
                    inv.setWarehouseId(wmsInnerJobOrderDto.getWarehouseId());
                    inv.setRelevanceOrderCode(wmsInnerJobOrderDto.getJobOrderCode());
                    inv.setPackingQty(distributionQty);
                    inv.setJobStatus((byte) 2);
                    inv.setInventoryId(null);
                    inv.setBatchCode(wmsInnerJobOrderDetDto.getBatchCode());
                    inv.setJobOrderDetId(id);
                    inv.setOrgId(sysUser.getOrganizationId());
                    inv.setCreateUserId(sysUser.getUserId());
                    inv.setCreateTime(new Date());
                    inv.setModifiedTime(new Date());
                    inv.setModifiedUserId(sysUser.getUserId());
                    num += wmsInnerInventoryMapper.insertSelective(inv);
                } else {
                    //原库存
                    wmsInnerInventorys.setPackingQty(wmsInPutawayOrderDet.getDistributionQty());
                    num += wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
                }
            }
            if (StringUtils.isEmpty(dto)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //如果货品全部分配完成更改表头状态为待作业状态
            List<WmsInnerJobOrderDetDto> orderDetDtos = dto.stream().filter(li -> li.getOutStorageId() != null).collect(Collectors.toList());
            if (!orderDetDtos.isEmpty() && orderDetDtos.size() == dto.size()) {
                //更新表头状态
                //完工入库单需要激活状态 其他则不需要
                Byte status = 3;
                wmsInnerJobOrderMapper.updateByPrimaryKeySelective(WmsInnerJobOrder.builder()
                        .jobOrderId(wmsInPutawayOrderDet.getJobOrderId())
                        .orderStatus(status)
                        .modifiedTime(new Date())
                        .build());
            } else {
                wmsInnerJobOrderMapper.updateByPrimaryKeySelective(WmsInnerJobOrder.builder()
                        .jobOrderId(wmsInPutawayOrderDet.getJobOrderId())
                        .orderStatus((byte) 2)
                        .modifiedTime(new Date())
                        .build());
            }
        }
        return num;
    }

    /**
     * 取消分配
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int cancelDistribution(String ids) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        String[] arrId = ids.split(",");
        int num = 0;
        for (String s : arrId) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(s);
            if (StringUtils.isEmpty(wmsInnerJobOrder)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            if (wmsInnerJobOrder.getOrderStatus() == (byte) 1) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(), "单据处于未分配状态");
            }
            if (wmsInnerJobOrder.getOrderStatus() == (byte) 4 || wmsInnerJobOrder.getOrderStatus() == (byte) 5) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(), "单据作业中，无法取消");
            }
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId", s);
            List<WmsInnerJobOrderDet> list = wmsInnerJobOrderDetMapper.selectByExample(example);

            // 键值=来源单据编码 sourceOrderCode+来源明细ID sourceId+核心明细ID coreSourceId
            Map<String, List<WmsInnerJobOrderDet>> map = new HashMap<>();
            for (WmsInnerJobOrderDet wmsInnerJobOrderDet : list) {
                String keyS = wmsInnerJobOrderDet.getJobOrderDetId().toString();
                if (map.containsKey(keyS)) {
                    List<WmsInnerJobOrderDet> nm = new ArrayList<>();
                    for (WmsInnerJobOrderDet innerJobOrderDet : map.get(keyS)) {
                        innerJobOrderDet.setPlanQty(innerJobOrderDet.getPlanQty().add(wmsInnerJobOrderDet.getPlanQty()));
                        nm.add(innerJobOrderDet);
                    }
                    map.put(keyS, nm);
                } else {
                    List<WmsInnerJobOrderDet> list1 = new ArrayList<>();
                    list1.add(wmsInnerJobOrderDet);
                    map.put(keyS, list1);
                }
                if (wmsInnerJobOrder.getJobOrderType() != (byte) 3) {
                    //原逻辑 JobOrderType=2 是移位作业 现在 JobOrderType=3 是移位作业
                    Example exampleInventory = new Example(WmsInnerInventory.class);
                    exampleInventory.createCriteria().andEqualTo("jobOrderDetId", wmsInnerJobOrderDet.getJobOrderDetId())
                            .andEqualTo("jobStatus", (byte) 2)
                            .andEqualTo("relevanceOrderCode", wmsInnerJobOrder.getJobOrderCode());
                    wmsInnerInventoryMapper.deleteByExample(exampleInventory);
                } else {
                    // JobOrderType=3 是移位作业 移位作业取消分配逻辑
                    Example example1 = new Example(WmsInnerInventory.class);
                    Example.Criteria criteria = example1.createCriteria();
                    criteria.andEqualTo("materialId", wmsInnerJobOrderDet.getMaterialId())
                            .andEqualTo("warehouseId", wmsInnerJobOrder.getWarehouseId())
                            .andEqualTo("storageId", wmsInnerJobOrderDet.getOutStorageId())
                            .andEqualTo("relevanceOrderCode", wmsInnerJobOrder.getJobOrderCode())
                            .andEqualTo("jobStatus", (byte) 2)
                            .andEqualTo("stockLock", 0)
                            .andEqualTo("lockStatus", 0)
                            .andEqualTo("orgId", sysUser.getOrganizationId());
                    List<WmsInnerInventory> inventories = wmsInnerInventoryMapper.selectByExample(example1);
                    if (inventories.isEmpty()) {
                        throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                    }
                    //删除分配库存
                    wmsInnerInventoryMapper.deleteByExample(example1);
                    WmsInnerInventory wmsIn = inventories.get(0);
                    BigDecimal packingQty = inventories.stream().map(WmsInnerInventory::getPackingQty).reduce(BigDecimal::add).get();
                    wmsIn.setPackingQty(packingQty);
                    wmsInnerInventoryMapper.insert(wmsIn);
                }
            }
            //删除全部明细数据
            wmsInnerJobOrderDetMapper.deleteByExample(example);
            //还原明细
            for (List<WmsInnerJobOrderDet> value : map.values()) {
                for (WmsInnerJobOrderDet wmsInnerJobOrderDet : value) {
                    wmsInnerJobOrderDet.setDistributionQty(null);
                    wmsInnerJobOrderDet.setInStorageId(null);
                    wmsInnerJobOrderDet.setLineStatus((byte) 1);
                    wmsInnerJobOrderDet.setModifiedTime(new Date());
                    wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
                    num += wmsInnerJobOrderDetMapper.insertSelective(wmsInnerJobOrderDet);
                }
            }
            wmsInnerJobOrder.setOrderStatus((byte) 1);
            num += wmsInnerJobOrderMapper.updateByPrimaryKeySelective(wmsInnerJobOrder);
        }
        return num;
    }

    /**
     * 移位单按条码单一确认
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int singleReceivingByBarcode(WmsInnerJobOrderDet wmsInPutawayOrderDet, String ids, Byte orderType) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        int num = 0;
        WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(wmsInPutawayOrderDet.getJobOrderId());
        if (StringUtils.isEmpty(wmsInnerJobOrder)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        if (wmsInnerJobOrder.getOrderStatus() == (byte) 5) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(), "单据确认已完成");
        }

        if (StringUtils.isEmpty(ids)) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "未选择需确认的条码信息");
        }

        List<WmsInnerInventoryDetDto> newInventoryDetDtoList = new ArrayList<>();
    //    List<WmsInnerInventoryDetDto> addOnlyInventoryDetDtoList = new ArrayList<>();
        BigDecimal totalQty = new BigDecimal(0);
        BigDecimal distributionQty = StringUtils.isEmpty(wmsInPutawayOrderDet.getDistributionQty()) ? new BigDecimal(0) : wmsInPutawayOrderDet.getDistributionQty();
        String[] arrId = ids.split(",");
        Map<String, Byte> map = new HashMap();
        Set<String> set = new HashSet<>();
        for (String item : arrId) {
            //单一确认的条码为库存明细中的条码
            SearchWmsInnerInventoryDet det = new SearchWmsInnerInventoryDet();
            det.setMaterialBarcodeId(Long.parseLong(item));
            det.setOrgId(sysUser.getOrganizationId());
            List<WmsInnerInventoryDetDto> wmsInnerInventoryDetDtoList = wmsInnerInventoryDetMapper.findList(ControllerUtil.dynamicConditionByEntity(det));
            //更新库存明细，移位作业只能库内调拨，所以不用更改仓库
            if (wmsInnerInventoryDetDtoList.size() > 0) {
                WmsInnerInventoryDetDto innerInventoryDetDto = wmsInnerInventoryDetDtoList.get(0);
            //    totalQty = totalQty.add((StringUtils.isEmpty(innerInventoryDetDto.getMaterialQty()) ? new BigDecimal(0) : innerInventoryDetDto.getMaterialQty()));
                if (StringUtils.isNotEmpty(innerInventoryDetDto.getPalletCode())) {
                    map.put(innerInventoryDetDto.getPalletCode(), (byte) 4);
                    set.add(innerInventoryDetDto.getPalletCode());
                }
                if (StringUtils.isNotEmpty(innerInventoryDetDto.getCartonCode())) {
                    map.put(innerInventoryDetDto.getCartonCode(), (byte) 3);
                    set.add(innerInventoryDetDto.getCartonCode());
                }
                if (StringUtils.isNotEmpty(innerInventoryDetDto.getColorBoxCode())) {
                    map.put(innerInventoryDetDto.getColorBoxCode(), (byte) 2);
                    set.add(innerInventoryDetDto.getColorBoxCode());
                }
                if (StringUtils.isNotEmpty(innerInventoryDetDto.getBarcode())
                        && StringUtils.isEmpty(innerInventoryDetDto.getColorBoxCode(),innerInventoryDetDto.getCartonCode(),innerInventoryDetDto.getPalletCode())) {
                    map.put(innerInventoryDetDto.getBarcode(), (byte) 1);
                    set.add(innerInventoryDetDto.getBarcode());
                }
                newInventoryDetDtoList.add(innerInventoryDetDto);
            }
        }

        //校验是否整单发货
        WmsInnerInventoryUtil.isAllOutInventory(newInventoryDetDtoList);

        //更新库存明细
        Iterator<String> iterator = set.iterator();
        List<WmsInnerInventoryDetDto> updateList = new ArrayList<>();
        while (iterator.hasNext()) {
            String code = iterator.next();
            Byte type = map.get(code);
            SearchWmsInnerInventoryDet searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
            if (1 == type) {
                searchWmsInnerInventoryDet.setBarcode(code);
            }else if (2 == type) {
                searchWmsInnerInventoryDet.setColorBoxCode(code);
            } else if (3 == type) {
                searchWmsInnerInventoryDet.setCartonCode(code);
            } else if (4 == type) {
                searchWmsInnerInventoryDet.setPalletCode(code);
            } else {
                continue;
            }
            searchWmsInnerInventoryDet.setOrgId(sysUser.getOrganizationId());
            List<WmsInnerInventoryDetDto> list = wmsInnerInventoryDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInventoryDet));
            for(WmsInnerInventoryDetDto innerInventoryDetDto : list) {
                if(!updateList.contains(innerInventoryDetDto)){
                    innerInventoryDetDto.setStorageId(innerInventoryDetDto.getStorageId());
                    innerInventoryDetDto.setBarcodeStatus((byte) 1);
                    innerInventoryDetDto.setModifiedTime(new Date());
                    innerInventoryDetDto.setModifiedUserId(sysUser.getUserId());
                    updateList.add(innerInventoryDetDto);
                }
                //    wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(innerInventoryDetDto);
                if(!newInventoryDetDtoList.contains(innerInventoryDetDto))
                    newInventoryDetDtoList.add(innerInventoryDetDto);

            }

            //更新单独存在的彩盒码、箱码、栈板码
            searchWmsInnerInventoryDet.setBarcodeType(type);
            List<WmsInnerInventoryDetDto> list1 = wmsInnerInventoryDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInventoryDet));
            if(StringUtils.isNotEmpty(list1)){
                WmsInnerInventoryDetDto innerInventoryDetDto1 = list1.get(0);
                if(!updateList.contains(innerInventoryDetDto1)) {
                    innerInventoryDetDto1.setStorageId(wmsInPutawayOrderDet.getInStorageId());
                    innerInventoryDetDto1.setBarcodeStatus((byte) 1);
                    innerInventoryDetDto1.setModifiedTime(new Date());
                    innerInventoryDetDto1.setModifiedUserId(sysUser.getUserId());
                    updateList.add(innerInventoryDetDto1);
                }
            }
            iterator.remove();
        }
        if(StringUtils.isNotEmpty(updateList)){
            for (WmsInnerInventoryDetDto update : updateList){
                wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(update);
            }
        }

        //更新条码状态
        List<WmsInnerMaterialBarcodeReOrder> list = new ArrayList<>();
        for (WmsInnerInventoryDetDto newInventoryDetDto : newInventoryDetDtoList) {
            if(StringUtils.isNotEmpty(newInventoryDetDto.getBarcode())){
                //增加条码中间表信息
                WmsInnerMaterialBarcodeReOrder wmsInnerMaterialBarcodeReOrder = new WmsInnerMaterialBarcodeReOrder();
                wmsInnerMaterialBarcodeReOrder.setOrderTypeCode("INNER-SSO");
                wmsInnerMaterialBarcodeReOrder.setOrderCode(wmsInnerJobOrder.getJobOrderCode());
                wmsInnerMaterialBarcodeReOrder.setOrderId(wmsInnerJobOrder.getJobOrderId());
                wmsInnerMaterialBarcodeReOrder.setOrderDetId(wmsInPutawayOrderDet.getJobOrderDetId());
                wmsInnerMaterialBarcodeReOrder.setMaterialBarcodeId(newInventoryDetDto.getMaterialBarcodeId());
                wmsInnerMaterialBarcodeReOrder.setStatus((byte) 1);
                wmsInnerMaterialBarcodeReOrder.setOrgId(sysUser.getOrganizationId());
                wmsInnerMaterialBarcodeReOrder.setCreateUserId(sysUser.getUserId());
                wmsInnerMaterialBarcodeReOrder.setCreateTime(new Date());
                wmsInnerMaterialBarcodeReOrder.setModifiedUserId(sysUser.getUserId());
                wmsInnerMaterialBarcodeReOrder.setModifiedTime(new Date());
                list.add(wmsInnerMaterialBarcodeReOrder);

                //更新条码状态
                WmsInnerMaterialBarcode wmsInnerMaterialBarcode = new WmsInnerMaterialBarcode();
                wmsInnerMaterialBarcode.setMaterialBarcodeId(newInventoryDetDto.getMaterialBarcodeId());
                wmsInnerMaterialBarcode.setBarcodeStatus((byte) 5);//已上架
                wmsInnerMaterialBarcode.setModifiedUserId(sysUser.getUserId());
                wmsInnerMaterialBarcode.setModifiedTime(new Date());
                wmsInnerMaterialBarcodeMapper.updateByPrimaryKeySelective(wmsInnerMaterialBarcode);

                //过滤重复的数量 只计算最大单位带出的数量
                totalQty = totalQty.add(newInventoryDetDto.getMaterialQty());
            }

        }


        //判断是否大于分配数
        if (totalQty.compareTo(distributionQty) == 1) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "确认条码物料总数大于明细分配数量");
        }

        SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
        searchWmsInnerJobOrderDet.setJobOrderDetId(wmsInPutawayOrderDet.getJobOrderDetId());
        WmsInnerJobOrderDetDto oldDto = wmsInnerJobOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);
        BaseStorage inBaseStorage = baseFeignApi.detail(wmsInPutawayOrderDet.getInStorageId()).getData();

        if (totalQty.compareTo(wmsInPutawayOrderDet.getDistributionQty()) == -1) {
            WmsInnerJobOrderDet wms = new WmsInnerJobOrderDet();
            BeanUtil.copyProperties(wmsInPutawayOrderDet, wms);
            wms.setJobOrderDetId(null);
            wms.setPlanQty(totalQty);
            wms.setDistributionQty(totalQty);
            wms.setActualQty(totalQty);
            wms.setLineStatus((byte) 3);
            wms.setWorkStartTime(new Date());
            wms.setWorkEndTime(new Date());
            wms.setWorkEndTime(new Date());
            num += wmsInnerJobOrderDetMapper.insertUseGeneratedKeys(wms);

            wmsInPutawayOrderDet.setLineStatus((byte) 2);
            wmsInPutawayOrderDet.setInStorageId(null);
            wmsInPutawayOrderDet.setPlanQty(wmsInPutawayOrderDet.getPlanQty().subtract(totalQty));
            wmsInPutawayOrderDet.setDistributionQty(wmsInPutawayOrderDet.getDistributionQty().subtract(totalQty));
            wmsInPutawayOrderDet.setActualQty(null);
            wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInPutawayOrderDet.setModifiedTime(new Date());
            num += wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
        } else if (totalQty.compareTo(wmsInPutawayOrderDet.getDistributionQty()) == 0) {
            //确认完成
            wmsInPutawayOrderDet.setLineStatus((byte) 3);
            wmsInPutawayOrderDet.setActualQty(totalQty);
            wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInPutawayOrderDet.setModifiedTime(new Date());
            wmsInPutawayOrderDet.setWorkStartTime(new Date());
            wmsInPutawayOrderDet.setWorkEndTime(new Date());
            num += wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
        }

        // 更改库存状态
        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setJobOrderId(oldDto.getJobOrderId());
        WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInnerJobOrderMapper.findList(searchWmsInnerJobOrder).get(0);


        Example example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialId", oldDto.getMaterialId())
//                .andEqualTo("warehouseId", oldDto.getWarehouseId())
                .andEqualTo("storageId", oldDto.getOutStorageId())
                .andEqualTo("jobOrderDetId", oldDto.getJobOrderDetId())
                .andEqualTo("jobStatus", (byte) 2)
                .andEqualTo("stockLock", 0)
                .andEqualTo("lockStatus", 0);
        WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
        if (StringUtils.isEmpty(wmsInnerInventory))
            throw new BizErrorException("未查询到移出库存");
        example.clear();
        Example.Criteria criteria1 = example.createCriteria()
                .andEqualTo("materialId", oldDto.getMaterialId())
//                .andEqualTo("warehouseId", oldDto.getWarehouseId())
                .andEqualTo("storageId", inBaseStorage.getStorageId())
                .andEqualTo("jobStatus", (byte) 1)
                .andEqualTo("stockLock", 0)
                .andEqualTo("lockStatus", 0)
                .andGreaterThan("packingQty", 0);
        if (StringUtils.isNotEmpty(wmsInnerInventory)) {
            criteria1.andEqualTo("inventoryStatusId", wmsInnerInventory.getInventoryStatusId());
        }
        List<WmsInnerInventory> wmsInnerInventory_olds = wmsInnerInventoryMapper.selectByExample(example);
        //获取初期数量
        WmsInnerInventory innerInventory = wmsInnerInventoryMapper.selectByPrimaryKey(wmsInnerInventory.getParentInventoryId());
        BigDecimal initQty = BigDecimal.ZERO;
        if (StringUtils.isNotEmpty(innerInventory)) {
            if (StringUtils.isEmpty(innerInventory.getPackingQty())) {
                innerInventory.setPackingQty(BigDecimal.ZERO);
            }
            initQty = innerInventory.getPackingQty().add(wmsInnerInventory.getPackingQty());
        }
        oldDto.setInStorageId(inBaseStorage.getStorageId());
        if (StringUtils.isEmpty(wmsInnerInventory_olds)) {
            if (StringUtils.isEmpty(wmsInnerInventory)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            wmsInnerInventory.setJobStatus((byte) 1);
            wmsInnerInventory.setStorageId(inBaseStorage.getStorageId());
            wmsInnerInventoryService.updateByPrimaryKeySelective(wmsInnerInventory);


            //库存日志
            InventoryLogUtil.addLog(wmsInnerInventory, wmsInnerJobOrderDto, oldDto, initQty, wmsInnerInventory.getPackingQty(), (byte) 3, (byte) 2);
            InventoryLogUtil.addLog(wmsInnerInventory, wmsInnerJobOrderDto, oldDto, BigDecimal.ZERO, wmsInnerInventory.getPackingQty(), (byte) 3, (byte) 1);
        } else {
            WmsInnerInventory wmsInnerInventory_old = wmsInnerInventory_olds.get(0);
            //库存日志
            InventoryLogUtil.addLog(wmsInnerInventory_old, wmsInnerJobOrderDto, oldDto, initQty, wmsInnerInventory.getPackingQty(), (byte) 3, (byte) 2);
            InventoryLogUtil.addLog(wmsInnerInventory_old, wmsInnerJobOrderDto, oldDto, wmsInnerInventory_old.getPackingQty(), wmsInnerInventory.getPackingQty(), (byte) 3, (byte) 1);
            wmsInnerInventory_old.setPackingQty(wmsInnerInventory_old.getPackingQty() != null ? wmsInnerInventory_old.getPackingQty().add(new BigDecimal(newInventoryDetDtoList.size())) : wmsInnerInventory.getPackingQty());
            wmsInnerInventory_old.setRelevanceOrderCode(wmsInnerInventory.getRelevanceOrderCode());
            wmsInnerInventoryService.updateByPrimaryKeySelective(wmsInnerInventory_old);
            wmsInnerInventory.setPackingQty(BigDecimal.ZERO);
            wmsInnerInventoryService.updateByPrimaryKeySelective(wmsInnerInventory);
        }

        if (StringUtils.isNotEmpty(list))
            wmsInnerMaterialBarcodeReOrderMapper.insertList(list);

        WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
        wmsInnerJobOrderDet.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
        int count = wmsInnerJobOrderDetMapper.selectCount(wmsInnerJobOrderDet);
        wmsInnerJobOrderDet.setLineStatus((byte) 3);
        int oCount = wmsInnerJobOrderDetMapper.selectCount(wmsInnerJobOrderDet);

        SearchBaseWorker searchBaseWorker = new SearchBaseWorker();
        searchBaseWorker.setWarehouseId(wmsInnerJobOrderDto.getWarehouseId());
        searchBaseWorker.setUserId(sysUser.getUserId());
        List<BaseWorkerDto> workerDtos = baseFeignApi.findList(searchBaseWorker).getData();

        if (oCount == count) {
            WmsInnerJobOrder ws = wmsInnerJobOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDto.getJobOrderId());
            ws.setOrderStatus((byte) 5);

            if (!workerDtos.isEmpty()) {
                ws.setWorkerId(workerDtos.get(0).getWorkerId());
            }

            ws.setModifiedUserId(sysUser.getUserId());
            ws.setModifiedTime(new Date());
            if (StringUtils.isEmpty(ws.getWorkStartTime())) {
                ws.setWorkStartTime(new Date());
            }
            ws.setWorkEndtTime(new Date());
            num += wmsInnerJobOrderMapper.updateByPrimaryKeySelective(ws);

        } else {
            WmsInnerJobOrder ws = wmsInnerJobOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDto.getJobOrderId());
            ws.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
            if (!workerDtos.isEmpty()) {
                ws.setWorkerId(workerDtos.get(0).getWorkerId());
            }
            ws.setOrderStatus((byte) 4);
            ws.setModifiedUserId(sysUser.getUserId());
            ws.setModifiedTime(new Date());
            if (StringUtils.isEmpty(wmsInnerJobOrder.getWorkStartTime())) {
                ws.setWorkStartTime(new Date());
            }
            num += wmsInnerJobOrderMapper.updateByPrimaryKeySelective(ws);
        }

        return num;
    }

    /**
     * 回写上游单据
     *
     * @param opType 操作类型 1 上架确认 2 上架删除
     * @return
     */
    public int updateLastOrderNode(Byte opType, WmsInnerJobOrder wmsInnerJobOrder, WmsInnerJobOrderDet wmsInnerJobOrderDet) {
        int num = 1;
        //来源系统单据类型编码
        String sourceSysOrderTypeCode = wmsInnerJobOrder.getSourceSysOrderTypeCode();
        //核心单据类型编码
        String coreSourceTypeCode = wmsInnerJobOrder.getCoreSourceSysOrderTypeCode();
        //来源明细ID
        Long sourceId = wmsInnerJobOrderDet.getSourceId();
        //核心单据明细ID
        Long coreSourceId = wmsInnerJobOrderDet.getCoreSourceId();
        //上架数量
        BigDecimal actualQty = wmsInnerJobOrderDet.getPlanQty();
        if (opType == 1) {
            actualQty = wmsInnerJobOrderDet.getActualQty();
        }
        //来源单据回写
        if (StringUtils.isNotEmpty(sourceSysOrderTypeCode)) {
            switch (sourceSysOrderTypeCode) {
                case "IN-PO":
                    //采购订单
                    omFeignApi.updatePutDownQty(sourceId, actualQty);
                    break;
                case "IN-SRO":
                    //销退订单
                    omFeignApi.updateSalesReturnPutDownQty(sourceId, actualQty);
                    break;
                case "IN-OIO":
                    //其它入库订单
                    omFeignApi.updateOtherInPutDownQty(sourceId, actualQty);
                    break;
                case "IN-IPO":
                    //入库计划
                    inFeignApi.updatePutawayQty(opType, sourceId, actualQty);
                    break;
                case "IN-SWK":
                    //收货作业

                    break;
                case "IN-SPO":
                    //收货计划

                    break;
                case "QMS-MIIO":
                    //来料检验
                    QmsIncomingInspectionOrder incomingOrder = new QmsIncomingInspectionOrder();
                    incomingOrder.setIncomingInspectionOrderId(sourceId);
                    //incomingOrder
//                incomingOrder.setIfAllIssued((byte)0);//是否已全部下发(0-否 1-是)
                    //qmsFeignApi.updateIfAllIssued(incomingOrder);
                    break;
                case "INNER-TO":
                    //调拨单
                    omFeignApi.updateTransferOrderPutDownQty(sourceId, actualQty);
                    break;
                case "OUT-SO":
                    //销售订单
                    omFeignApi.updateSalesOrderPutDownQty(sourceId, actualQty);
                    break;
                case "OUT-OOO":
                    //其他出库单
                    omFeignApi.updateOtherOutOrderPutDownQty(sourceId, actualQty);
                    break;
                case "OUT-PRO":
                    //采购退货出库
                    omFeignApi.updatePurchaseReturnOrderPutDownQty(sourceId, actualQty);
                    break;
                case "OUT-PSLO":
                    //备料计划

                    break;
                default:
                    break;
            }
        }

        //核心单据回写
        if (StringUtils.isNotEmpty(coreSourceTypeCode)) {
            switch (coreSourceTypeCode) {
                case "IN-PO":
                    //采购订单
                    omFeignApi.updatePutawayQty(opType, coreSourceId, actualQty);
                    break;
                case "IN-SRO":
                    //销退订单
                    omFeignApi.updateSalesReturnPutQty(sourceId, actualQty);
                    break;
                case "IN-OIO":
                    //其它入库订单

                    break;
                case "IN-IPO":
                    //入库计划

                    break;
                case "IN-SWK":
                    //收货作业

                    break;
                case "IN-SPO":
                    //收货计划

                    break;
                case "QMS-MIIO":
                    //来料检验
                    QmsIncomingInspectionOrder incomingOrder = new QmsIncomingInspectionOrder();
                    incomingOrder.setIncomingInspectionOrderId(sourceId);

                    break;
                default:
                    break;
            }
        }
        return num;
    }

    /**
     * 校验条码
     * 上架 PDA扫描条码
     *
     * @param barCode
     * @return 包装数量
     */
    @Override
    public Map<String, Object> checkBarcode(String barCode, Long jobOrderDetId) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        //找来源单据的条码明细表 去判断条码的正确性
        WmsInnerJobOrderDet wmsInnerJobOrderDet = wmsInnerJobOrderDetMapper.selectByPrimaryKey(jobOrderDetId);
        WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDet.getJobOrderId());
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(wmsInnerJobOrderDet)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        //获取完工入库记录的工单
        Long materialId = null;
        //WmsInAsnOrderDet wms = null;
        if (wmsInnerJobOrder.getSourceSysOrderTypeCode().equals("9")) {
            materialId = wmsInnerJobOrderDetMapper.findEngMaterial(wmsInnerJobOrderDet.getSourceId());
        } else {
//             wms = inFeignApi.findDetList(SearchWmsInAsnOrderDet.builder()
//                    .asnOrderDetId(wmsInnerJobOrderDet.getSourceId())
//                    .build()).getData().get(0);
//            materialId = wms.getMaterialId();
        }

        SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
        searchBaseMaterial.setMaterialId(materialId);
        List<BaseMaterial> baseMaterialList = baseFeignApi.findList(searchBaseMaterial).getData();
        String materialCode = null;
        if (StringUtils.isNotEmpty(baseMaterialList)) {
            materialCode = baseMaterialList.get(0).getMaterialCode();
        }
        if (StringUtils.isNotEmpty(materialCode) && materialCode.equals(barCode)) {
            map.put("SN", "false");
            return map;
        }
//        else if(StringUtils.isNotEmpty(wms)){
//            BigDecimal qty = InBarcodeUtil.getInventoryDetQty(wms.getAsnOrderId(), wmsInnerJobOrderDet.getMaterialId(), barCode);
//            map.put("SN", "true");
//            map.put("qty", qty);
//        }else{
//            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"作业单与该条码不匹配");
//        }
        return map;
    }


    /**
     * PDA激活关闭栈板
     *
     * @param jobOrderId
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int activation(Long jobOrderId) {
        WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(jobOrderId);
        if (StringUtils.isEmpty(wmsInnerJobOrder)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        Example example = new Example(WmsInnerJobOrderReMspp.class);
        example.createCriteria().andEqualTo("jobOrderId", jobOrderId);
        WmsInnerJobOrderReMspp wmsInnerJobOrderReMspp = wmsInnerJobOrderReMsppMapper.selectOneByExample(example);
        if (StringUtils.isEmpty(wmsInnerJobOrderReMspp)) {
            throw new BizErrorException("未匹配到上架单关联栈板关系");
        }
        //更新栈板状态
        ResponseEntity responseEntity = sfcFeignApi.updateMoveStatus(wmsInnerJobOrderReMspp.getProductPalletId());
        if (responseEntity.getCode() != 0) {
            throw new BizErrorException("激活失败");
        }
        //更新待作业状态
        wmsInnerJobOrder.setOrderStatus((byte) 3);
        int num = wmsInnerJobOrderMapper.updateByPrimaryKeySelective(wmsInnerJobOrder);
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int save(WmsInnerJobOrder record) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        List<WmsInnerMaterialBarcodeReOrder> barcodeReOrderList = new ArrayList<>();
        String orderTypeCode = "INNER-SSO";
        Long outStorageId = null;
        Long inventoryStatusId = null;

        record.setJobOrderCode(CodeUtils.getId("INNER-SSO"));
        if (StringUtils.isEmpty(record.getSourceBigType()))
            record.setSourceBigType((byte) 2);

        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrgId(sysUser.getOrganizationId());
        record.setIsDelete((byte) 1);
        record.setOrderStatus((byte) 1);
        int num = wmsInnerJobOrderMapper.insertUseGeneratedKeys(record);
        for (WmsInnerJobOrderDet wmsInnerJobOrderDet : record.getWmsInPutawayOrderDets()) {
            if (StringUtils.isEmpty(wmsInnerJobOrderDet.getInventoryStatusId()) && record.getJobOrderType() == (byte) 1) {
                wmsInnerJobOrderDet.setInventoryStatusId(inventoryStatusId);
            }
            wmsInnerJobOrderDet.setJobOrderId(record.getJobOrderId());
            wmsInnerJobOrderDet.setLineStatus((byte) 1);
            wmsInnerJobOrderDet.setCreateTime(new Date());
            wmsInnerJobOrderDet.setCreateUserId(sysUser.getUserId());
            wmsInnerJobOrderDet.setModifiedTime(new Date());
            wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInnerJobOrderDet.setOrgId(sysUser.getOrganizationId());
            if (StringUtils.isEmpty(wmsInnerJobOrderDet.getOutStorageId()) && StringUtils.isNotEmpty(outStorageId))
                wmsInnerJobOrderDet.setOutStorageId(outStorageId);

            wmsInnerJobOrderDetMapper.insertUseGeneratedKeys(wmsInnerJobOrderDet);
            //非自建单据新增条码关系表数据
/*            if (record.getSourceBigType() != ((byte) 2) && StringUtils.isNotEmpty(record.getSourceBigType())) {
                SearchWmsInnerMaterialBarcodeReOrder sBarcodeReOrder = new SearchWmsInnerMaterialBarcodeReOrder();
                sBarcodeReOrder.setOrderTypeCode(record.getSourceSysOrderTypeCode());//单据类型
                sBarcodeReOrder.setOrderDetId(wmsInnerJobOrderDet.getSourceId());//明细ID
                List<WmsInnerMaterialBarcodeReOrderDto> reOrderList = wmsInnerMaterialBarcodeReOrderService.findList(ControllerUtil.dynamicConditionByEntity(sBarcodeReOrder));
                if (reOrderList.size() > 0) {
                    for (WmsInnerMaterialBarcodeReOrderDto item : reOrderList) {
                        WmsInnerMaterialBarcodeReOrder barcodeReOrder = new WmsInnerMaterialBarcodeReOrder();
                        BeanUtil.copyProperties(item, barcodeReOrder);
                        barcodeReOrder.setOrderTypeCode(orderTypeCode);
                        barcodeReOrder.setOrderCode(record.getJobOrderCode());
                        barcodeReOrder.setOrderId(record.getJobOrderId());
                        barcodeReOrder.setOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
                        barcodeReOrder.setScanStatus((byte) 1);
                        barcodeReOrder.setCreateUserId(sysUser.getUserId());
                        barcodeReOrder.setCreateTime(new Date());
                        barcodeReOrder.setModifiedUserId(sysUser.getUserId());
                        barcodeReOrder.setModifiedTime(new Date());
                        barcodeReOrder.setMaterialBarcodeReOrderId(null);
                        barcodeReOrderList.add(barcodeReOrder);
                    }
                }
            }*/

            if (record.getJobOrderType() == (byte) 3) {
                //移位作业库存校验、变更
                updateInnerInventory(wmsInnerJobOrderDet,record,sysUser);
            }
        }
        //批量新增到条码关系表
        if (barcodeReOrderList.size() > 0) {
            wmsInnerMaterialBarcodeReOrderService.batchAdd(barcodeReOrderList);
        }

        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(WmsInnerJobOrder record) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        record.setModifiedUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setOrgId(sysUser.getOrganizationId());
        if (record.getOrderStatus() != (byte) 1) {
            throw new BizErrorException("作业单已分配，不可变更修改");
        }
        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setJobOrderId(record.getJobOrderId());
        List<WmsInnerJobOrderDto> oldWmsInnerJobOrderDtos = wmsInnerJobOrderMapper.findList(searchWmsInnerJobOrder);

/*        //删除原有明细
        String ids = null;
        for(WmsInnerJobOrderDet det : oldWmsInnerJobOrderDtos.get(0).getWmsInPutawayOrderDets()){
            if(StringUtils.isEmpty(ids))
                ids = det.getJobOrderDetId().toString();
            else
                ids = ids + "," + det.getJobOrderDetId().toString();
        }
        wmsInnerShiftWorkDetService.batchDeleteByShiftWork(ids);*/


        //新增剩余的明细
        if(StringUtils.isNotEmpty(record.getWmsInPutawayOrderDets())){
            List<WmsInnerJobOrderDet> addlist = new ArrayList<>();
            for (WmsInnerJobOrderDet det  : record.getWmsInPutawayOrderDets()){
                det.setJobOrderId(record.getJobOrderId());
                det.setCreateUserId(sysUser.getUserId());
                det.setCreateTime(new Date());
                det.setModifiedUserId(sysUser.getUserId());
                det.setModifiedTime(new Date());
                det.setStatus(StringUtils.isEmpty(det.getStatus())?1: det.getStatus());
                addlist.add(det);
                //移位作业库存校验、变更
                updateInnerInventory(det,record,sysUser);
            }
            if (StringUtils.isNotEmpty(addlist))
                wmsInnerJobOrderDetMapper.insertList(addlist);
        }

        int i = wmsInnerJobOrderMapper.updateByPrimaryKey(record);
        return i;
    }

    public void updateInnerInventory(WmsInnerJobOrderDet wmsInnerJobOrderDet,WmsInnerJobOrder wmsInnerJobOrder,SysUser sysUser){
        //移位作业校验
        Example example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("warehouseId", wmsInnerJobOrder.getWarehouseId());
        criteria.andEqualTo("materialId", wmsInnerJobOrderDet.getMaterialId());
        criteria.andEqualTo("storageId", wmsInnerJobOrderDet.getOutStorageId());
        criteria.andEqualTo("lockStatus", (byte) 0);
        criteria.andEqualTo("stockLock", (byte) 0);
        criteria.andEqualTo("jobStatus", (byte) 1);
        criteria.andEqualTo("inventoryId", wmsInnerJobOrderDet.getInventoryId());
        List<WmsInnerInventory> wmsInnerInventorys = wmsInnerInventoryMapper.selectByExample(example);
        if (StringUtils.isEmpty(wmsInnerInventorys)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "未查询到出库位的物料条码，物料为：" + wmsInnerJobOrderDet.getMaterialId());
        }
        if (wmsInnerInventorys.size() > 1) {
            throw new BizErrorException(ErrorCodeEnum.STO30012002.getCode(), "查询到的库存不唯一,相同的库位未合并");
        }

        // 生成库存，扣减原库存 移位作业
        WmsInnerInventory innerInventory = wmsInnerInventorys.get(0);
        if (StringUtils.isEmpty(wmsInnerJobOrderDet.getPlanQty()))
            wmsInnerJobOrderDet.setPlanQty(BigDecimal.ZERO);
        if (StringUtils.isEmpty(innerInventory.getPackingQty()))
            innerInventory.setPackingQty(BigDecimal.ZERO);
        if (innerInventory.getPackingQty().compareTo(wmsInnerJobOrderDet.getPlanQty()) < 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA5001012);
        }

        //待出库存不合并
        WmsInnerInventory newInnerInventory = new WmsInnerInventory();
        BeanUtil.copyProperties(innerInventory, newInnerInventory,new String[]{"inventoryId"});
        newInnerInventory.setPackingQty(wmsInnerJobOrderDet.getPlanQty());
        newInnerInventory.setJobStatus((byte) 2);
        newInnerInventory.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
        newInnerInventory.setOrgId(sysUser.getOrganizationId());
        newInnerInventory.setCreateTime(new Date());
        newInnerInventory.setCreateUserId(sysUser.getUserId());
        newInnerInventory.setParentInventoryId(innerInventory.getInventoryId());
        newInnerInventory.setRelevanceOrderCode(wmsInnerJobOrder.getJobOrderCode());
        newInnerInventory.setModifiedUserId(sysUser.getUserId());
        newInnerInventory.setModifiedTime(new Date());
        wmsInnerInventoryService.save(newInnerInventory);

        // 变更减少原库存
        innerInventory.setPackingQty(innerInventory.getPackingQty().subtract(wmsInnerJobOrderDet.getPlanQty()));
        wmsInnerInventoryService.update(innerInventory);
        
    }
    
    
    
    
    
    /**
     * 删除
     *
     * @param
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        String[] arrId = ids.split(",");
        for (String s : arrId) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(s);
            if (StringUtils.isEmpty(wmsInnerJobOrder)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            if (wmsInnerJobOrder.getOrderStatus() >= (byte) 4) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(), "单据已经作业，无法删除");
            }

            //删除已分配库存
            Example exampleInventory = new Example(WmsInnerInventory.class);
            Example.Criteria criteriaInventory = exampleInventory.createCriteria();
            criteriaInventory.andEqualTo("relevanceOrderCode", wmsInnerJobOrder.getJobOrderCode());
            criteriaInventory.andEqualTo("jobStatus", (byte) 2);
            criteriaInventory.andEqualTo("orgId", sysUser.getOrganizationId());
            wmsInnerInventoryMapper.deleteByExample(exampleInventory);

            //删除条码关系表
            Example exampleReOrder = new Example(WmsInnerMaterialBarcodeReOrder.class);
            Example.Criteria criteriaReOrder = exampleReOrder.createCriteria();
            criteriaReOrder.andEqualTo("orderTypeCode", "IN-IWK");
            criteriaReOrder.andEqualTo("orderCode", wmsInnerJobOrder.getJobOrderCode());
            criteriaReOrder.andEqualTo("orgId", sysUser.getOrganizationId());
            wmsInnerMaterialBarcodeReOrderMapper.deleteByExample(exampleReOrder);

            //回写上游单据已下推数量
            Example exampleDet = new Example(WmsInnerJobOrderDet.class);
            exampleDet.createCriteria().andEqualTo("jobOrderId", wmsInnerJobOrder.getJobOrderId());
            List<WmsInnerJobOrderDet> jobOrderDets = wmsInnerJobOrderDetMapper.selectByExample(exampleDet);
            if (wmsInnerJobOrder.getJobOrderType() == (byte) 1 && wmsInnerJobOrder.getSourceBigType() != (byte) 2) {
                for (WmsInnerJobOrderDet jobOrderDetIPO : jobOrderDets) {
                    updateLastOrderNode((byte) 2, wmsInnerJobOrder, jobOrderDetIPO);
                }
            }
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId", s);
            wmsInnerJobOrderDetMapper.deleteByExample(example);
        }
        return wmsInnerJobOrderMapper.deleteByIds(ids);
    }

    /**
     * 关闭单据
     *
     * @param
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int closeWmsInnerJobOrder(String ids) {
        int num = 0;
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        String[] arrId = ids.split(",");
        for (String s : arrId) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(s);
            if (StringUtils.isEmpty(wmsInnerJobOrder)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            if (wmsInnerJobOrder.getOrderStatus() == (byte) 5) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(), "单据已经作业完成，不需关闭单据");
            }

            //删除未上架的已分配库存
            Example exampleDet = new Example(WmsInnerJobOrderDet.class);
            Example.Criteria criteriaDet = exampleDet.createCriteria();
            criteriaDet.andEqualTo("jobOrderId", wmsInnerJobOrder.getJobOrderId());
            criteriaDet.andNotEqualTo("lineStatus", (byte) 3);
            criteriaDet.andEqualTo("orgId", sysUser.getOrganizationId());
            List<WmsInnerJobOrderDet> listDet = wmsInnerJobOrderDetMapper.selectByExample(exampleDet);
            if (listDet.size() > 0) {
                for (WmsInnerJobOrderDet item : listDet) {
                    //删除分配库存
                    Example exampleInventory = new Example(WmsInnerInventory.class);
                    Example.Criteria criteriaInventory = exampleInventory.createCriteria();
                    criteriaInventory.andEqualTo("relevanceOrderCode", wmsInnerJobOrder.getJobOrderCode());
                    criteriaInventory.andEqualTo("jobStatus", (byte) 2);//
                    criteriaInventory.andEqualTo("jobOrderDetId", item.getJobOrderDetId());
                    criteriaInventory.andEqualTo("storageId", item.getOutStorageId());
                    criteriaInventory.andEqualTo("orgId", sysUser.getOrganizationId());
                    num = wmsInnerInventoryMapper.deleteByExample(exampleInventory);

                    //更新明细上架数量=分配数量 状态
                    item.setDistributionQty(item.getPlanQty());
                    //item.setActualQty(item.getPlanQty());
                    item.setModifiedUserId(sysUser.getUserId());
                    item.setModifiedTime(new Date());
                    item.setLineStatus((byte) 3);
                    if (StringUtils.isEmpty(item.getRemark()))
                        item.setRemark("关闭");
                    else
                        item.setRemark(item.getRemark() + " 关闭");

                    num = wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(item);
                }
            }

            //更新单据状态
            wmsInnerJobOrder.setOrderStatus((byte) 5);
            wmsInnerJobOrder.setModifiedUserId(sysUser.getUserId());
            wmsInnerJobOrder.setModifiedTime(new Date());
            if (StringUtils.isEmpty(wmsInnerJobOrder.getRemark()))
                wmsInnerJobOrder.setRemark("关闭");
            else
                wmsInnerJobOrder.setRemark(wmsInnerJobOrder.getRemark() + " 关闭");

            num = wmsInnerJobOrderMapper.updateByPrimaryKeySelective(wmsInnerJobOrder);

        }
        return num;
    }

    /**
     * 分配库存
     *
     * @param wmsInnerJobOrderDto
     * @param wmsInnerJobOrderDet
     * @return
     */
    private int updateInventory(WmsInnerJobOrderDto wmsInnerJobOrderDto, WmsInnerJobOrderDet wmsInnerJobOrderDet) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();

        //增加分配库存 库位是收货库位
        Example example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("relevanceOrderCode", wmsInnerJobOrderDto.getJobOrderCode())
                .andEqualTo("materialId", wmsInnerJobOrderDet.getMaterialId())
                .andEqualTo("warehouseId", wmsInnerJobOrderDto.getWarehouseId())
                .andEqualTo("storageId", wmsInnerJobOrderDet.getOutStorageId())
                .andEqualTo("inventoryStatusId", wmsInnerJobOrderDet.getInventoryStatusId());
        if (!StringUtils.isEmpty(wmsInnerJobOrderDet.getBatchCode())) {
            criteria1.andEqualTo("batchCode", wmsInnerJobOrderDet.getBatchCode());
        }
        criteria1.andEqualTo("jobOrderDetId", wmsInnerJobOrderDet.getJobOrderDetId());
        criteria1.andEqualTo("jobStatus", (byte) 2);
        criteria1.andEqualTo("orgId", sysUser.getOrganizationId());
        WmsInnerInventory wmsInnerInventorys = wmsInnerInventoryMapper.selectOneByExample(example);
        if (StringUtils.isEmpty(wmsInnerInventorys)) {
            //添加库存
            WmsInnerInventory inv = new WmsInnerInventory();
            inv.setStorageId(wmsInnerJobOrderDet.getOutStorageId());
            inv.setWarehouseId(wmsInnerJobOrderDto.getWarehouseId());
            inv.setRelevanceOrderCode(wmsInnerJobOrderDto.getJobOrderCode());
            inv.setPackingQty(wmsInnerJobOrderDet.getDistributionQty());
            inv.setJobStatus((byte) 2);
            inv.setInventoryId(null);
            inv.setBatchCode(wmsInnerJobOrderDet.getBatchCode());
            inv.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
            inv.setOrgId(sysUser.getOrganizationId());
            inv.setCreateUserId(sysUser.getUserId());
            inv.setCreateTime(new Date());
            inv.setModifiedTime(new Date());
            inv.setModifiedUserId(sysUser.getUserId());
            return wmsInnerInventoryMapper.insertSelective(inv);
        } else {
            //原库存
            wmsInnerInventorys.setPackingQty(wmsInnerInventorys.getPackingQty().add(wmsInnerJobOrderDet.getDistributionQty()));
            return wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
        }
    }

    /**
     * 从Excel导入数据
     *
     * @return
     */
    @Override
    public Map<String, Object> importExcel(List<WmsInnerJobOrderImport> wmsInnerJobOrderImportsTemp) throws ParseException {
        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        List<WmsInnerJobOrderImport> wmsInnerJobOrderImports = new ArrayList<>();
        for (WmsInnerJobOrderImport wmsInnerJobOrderImport : wmsInnerJobOrderImportsTemp) {
            if (StringUtils.isNotEmpty(wmsInnerJobOrderImport.getOutStorageName())) {
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
            String outStorageName = wmsInnerJobOrderImport.getOutStorageName();
            String planQty = wmsInnerJobOrderImport.getPlanQty().toString();

            //判断必传字段
            if (StringUtils.isEmpty(
                    warehouseName, materialCode, outStorageName, planQty
            )) {
                fail.add(i + 4);
                iterator.remove();
                i++;
                continue;
            }

            //判断物料信息是否存在
            SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
            searchBaseMaterial.setMaterialCode(materialCode);
            ResponseEntity<List<BaseMaterial>> baseMaterialList = baseFeignApi.findList(searchBaseMaterial);
            if (StringUtils.isNotEmpty(baseMaterialList.getData())) {
                BaseMaterial baseMaterial = baseMaterialList.getData().get(0);
                if (StringUtils.isEmpty(baseMaterial)) {
                    fail.add(i + 4);
                    iterator.remove();
                    i++;
                    continue;
                }
                wmsInnerJobOrderImport.setMaterialId(baseMaterial.getMaterialId());
                i++;
            }

            //仓库是否存在
            SearchBaseWarehouse searchBaseWarehouse = new SearchBaseWarehouse();
            searchBaseWarehouse.setWarehouseName(warehouseName);
            ResponseEntity<List<BaseWarehouse>> listResponseEntity = baseFeignApi.findList(searchBaseWarehouse);
            if (StringUtils.isNotEmpty(listResponseEntity.getData())) {
                BaseWarehouse baseWarehouse = listResponseEntity.getData().get(0);
                if (StringUtils.isEmpty(baseWarehouse)) {
                    fail.add(i + 4);
                    iterator.remove();
                    i++;
                    continue;
                }
                wmsInnerJobOrderImport.setWarehouseId(baseWarehouse.getWarehouseId());
                i++;
            }

            //移出库位是否存在
            SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
            searchBaseStorage.setStorageName(outStorageName);
            ResponseEntity<List<BaseStorage>> baseStorageRe = baseFeignApi.findList(searchBaseStorage);
            if (StringUtils.isNotEmpty(baseStorageRe.getData())) {
                BaseStorage baseStorage = baseStorageRe.getData().get(0);
                if (StringUtils.isEmpty(baseStorage)) {
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
            Long jobOrderId = null;
            if (StringUtils.isEmpty(jobOrderId)) {
                WmsInnerJobOrder wmsInnerJobOrder = new WmsInnerJobOrder();
                wmsInnerJobOrder.setJobOrderType((byte) 1);
                wmsInnerJobOrder.setWarehouseId(code);
                wmsInnerJobOrder.setStatus((byte) 1);
                wmsInnerJobOrder.setOrgId(currentUser.getOrganizationId());
                wmsInnerJobOrder.setCreateUserId(currentUser.getUserId());
                wmsInnerJobOrder.setCreateTime(new Date());
                wmsInnerJobOrderMapper.insertUseGeneratedKeys(wmsInnerJobOrder);
                jobOrderId = wmsInnerJobOrder.getJobOrderId();
            }
            //新增明细 WmsInnerJobOrderDet
            if (StringUtils.isNotEmpty(jobOrderId)) {
                List<WmsInnerJobOrderDet> wmsInnerJobOrderDetList = new ArrayList<>();
                for (WmsInnerJobOrderImport item : wmsInnerJobOrderImport1) {

                    WmsInnerJobOrderDet wmsInnerJobOrderDetNew = new WmsInnerJobOrderDet();
                    wmsInnerJobOrderDetNew.setJobOrderId(jobOrderId);
                    wmsInnerJobOrderDetNew.setMaterialId(item.getMaterialId());
                    wmsInnerJobOrderDetNew.setPlanQty(new BigDecimal(item.getPlanQty()));
                    wmsInnerJobOrderDetNew.setOrgId(currentUser.getOrganizationId());
                    wmsInnerJobOrderDetNew.setCreateUserId(currentUser.getUserId());
                    wmsInnerJobOrderDetNew.setCreateTime(new Date());
                    wmsInnerJobOrderDetList.add(wmsInnerJobOrderDetNew);
                }

                if (wmsInnerJobOrderDetList.size() > 0) {
                    success += wmsInnerJobOrderDetMapper.insertList(wmsInnerJobOrderDetList);
                }
            }
        }
        resultMap.put("操作成功总数", success);
        resultMap.put("操作失败行", fail);
        return resultMap;
    }

    /**
     * 导出
     *
     * @return
     */
    @Override
    public List<WmsInnerJobOrderExport> findExportList(Map<String, Object> map) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", currentUser.getOrganizationId());
        return wmsInnerJobOrderMapper.findExportList(map);
    }

    @Override
    public WmsInnerJobOrderDto detail(Long id, String sourceSysOrderTypeCode) {
        WmsInnerJobOrderDto dto = new WmsInnerJobOrderDto();
        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setJobOrderId(id);
        List<WmsInnerJobOrderDto> list = wmsInnerJobOrderMapper.findList(searchWmsInnerJobOrder);
        if (StringUtils.isNotEmpty(list)) {
            dto = list.get(0);
            List<Long> ids = new ArrayList<>();
            for (WmsInnerJobOrderDet det : dto.getWmsInPutawayOrderDets()) {
                ids.add(det.getJobOrderDetId());
            }
            Map map = new HashMap();
            map.put("orderDetIdList", ids);
            map.put("orderTypeCode", sourceSysOrderTypeCode);
            List<WmsInnerMaterialBarcodeReOrderDto> wmsInnerMaterialBarcodeReOrderDtos = wmsInnerMaterialBarcodeReOrderMapper.findList(map);
            dto.setWmsInnerMaterialBarcodeReOrderDtos(wmsInnerMaterialBarcodeReOrderDtos);
        }
        return dto;
    }

}
