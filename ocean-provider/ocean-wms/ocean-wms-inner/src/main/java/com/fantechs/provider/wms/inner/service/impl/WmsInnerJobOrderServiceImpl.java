package com.fantechs.provider.wms.inner.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseWorkerDto;
import com.fantechs.common.base.general.dto.basic.JobRuleDto;
import com.fantechs.common.base.general.dto.basic.StorageRuleDto;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderTakeCancel;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcBarcodeProcessRecordDto;
import com.fantechs.common.base.general.dto.wms.inner.*;
import com.fantechs.common.base.general.dto.wms.inner.imports.WmsInnerJobOrderImport;
import com.fantechs.common.base.general.dto.wms.inner.imports.WmsInnerStockOrderImport;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.*;
import com.fantechs.common.base.general.entity.eam.EamEquipment;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaterial;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaterialList;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrder;
import com.fantechs.common.base.general.entity.wms.inner.*;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerMaterialBarcode;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerMaterialBarcodeReOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.DateUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.guest.eng.EngFeignApi;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.api.qms.QmsFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.in.InFeignApi;
import com.fantechs.provider.wms.inner.mapper.*;
import com.fantechs.provider.wms.inner.service.*;
import com.fantechs.provider.wms.inner.util.InBarcodeUtil;
import com.fantechs.provider.wms.inner.util.InventoryLogUtil;
import com.fantechs.provider.wms.inner.util.WmsInnerInventoryUtil;
import com.sun.javafx.fxml.BeanAdapter;
import io.micrometer.core.instrument.search.Search;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Mr.Lei on 2021/05/06.
 */
@Service
public class WmsInnerJobOrderServiceImpl extends BaseService<WmsInnerJobOrder> implements WmsInnerJobOrderService {
    @Resource
    private WmsInnerJobOrderMapper wmsInPutawayOrderMapper;
    @Resource
    private WmsInnerJobOrderDetMapper wmsInPutawayOrderDetMapper;
    @Resource
    private WmsInnerJobOrderDetService wmsInnerJobOrderDetService;
    @Resource
    private InFeignApi inFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private WmsInnerInventoryMapper wmsInnerInventoryMapper;
    @Resource
    private WmsInnerJobOrderReMsppMapper wmsInnerJobOrderReMsppMapper;
    @Resource
    private SFCFeignApi sfcFeignApi;
    @Resource
    private WmsInnerInventoryDetMapper wmsInnerInventoryDetMapper;
    @Resource
    WmsInnerInventoryService wmsInnerInventoryService;
    @Resource
    WmsInnerJobOrderDetBarcodeService wmsInnerJobOrderDetBarcodeService;
    @Resource
    private PickingOrderService pickingOrderService;
    @Resource
    private EngFeignApi engFeignApi;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private QmsFeignApi qmsFeignApi;
    @Resource
    private WmsDataExportInnerJobOrderService wmsDataExportInnerJobOrderService;
    @Resource
    private WmsInnerMaterialBarcodeMapper wmsInnerMaterialBarcodeMapper;
    @Resource
    WmsInnerMaterialBarcodeReOrderService wmsInnerMaterialBarcodeReOrderService;
    @Resource
    WmsInnerMaterialBarcodeReOrderMapper wmsInnerMaterialBarcodeReOrderMapper;
    @Resource
    WmsInnerMaterialBarcodeService wmsInnerMaterialBarcodeService;

    @Override
    public List<WmsInnerJobOrderDto> findList(SearchWmsInnerJobOrder searchWmsInnerJobOrder) {
        if(StringUtils.isEmpty(searchWmsInnerJobOrder.getOrgId())) {
            SysUser sysUser = currentUser();
            searchWmsInnerJobOrder.setOrgId(sysUser.getOrganizationId());
        }
        return wmsInPutawayOrderMapper.findList(searchWmsInnerJobOrder);
    }

    @Override
    public List<WmsInnerJobOrderDto> findShiftList(Map<String, Object> map) {
        return wmsInPutawayOrderMapper.findShiftList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDeleteByShiftWork(String ids) {
        String[] arrId = ids.split(",");
        for (String s : arrId) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(s);
            if (wmsInnerJobOrder.getOrderStatus() >= (byte) 4) {
                throw new BizErrorException("单据已经作业，无法删除");
            }
            if (StringUtils.isEmpty(wmsInnerJobOrder)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId", s);
            List<WmsInnerJobOrderDet> jobOrderDetList = wmsInPutawayOrderDetMapper.selectByExample(example);
            for (WmsInnerJobOrderDet det : jobOrderDetList) {
                wmsInPutawayOrderDetMapper.delete(det);
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
                wmsInnerInventoryService.delete(innerInventory);
            }
        }
        return wmsInPutawayOrderMapper.deleteByIds(ids);
    }

    /**
     * 自动分配
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int autoDistribution(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        int num = 0;
        for (String id : arrId) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(wmsInnerJobOrder.getWarehouseId())){
                throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"请先输入仓库再进行分配作业 上架单号-->"+wmsInnerJobOrder.getJobOrderCode());
            }
            if (wmsInnerJobOrder.getOrderStatus() > (byte) 2) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"上架作业单已分配完成-->"+wmsInnerJobOrder.getJobOrderCode());
            }

            wmsInnerJobOrder.setOrderStatus((byte)3);

            // 获取表头详情
            SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
            searchWmsInnerJobOrder.setJobOrderId(wmsInnerJobOrder.getJobOrderId());
            WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInPutawayOrderMapper.findList(searchWmsInnerJobOrder).get(0);

            //如果上游单据已预设好移入库位 则不需要再分配库位
            Example exampleExist = new Example(WmsInnerJobOrderDet.class);
            exampleExist.createCriteria().andEqualTo("jobOrderId", wmsInnerJobOrder.getJobOrderId()).andIsNull("inStorageId");
            List<WmsInnerJobOrderDet> list = wmsInPutawayOrderDetMapper.selectByExample(exampleExist);
            if(list.size()<=0){
                SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
                searchWmsInnerJobOrderDet.setJobOrderId(wmsInnerJobOrder.getJobOrderId());
                List<WmsInnerJobOrderDetDto> listInnerDetDto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet);
                for (WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto : listInnerDetDto) {
                    //分配库存
                    //num += this.updateInventory(wmsInnerJobOrderDto, wmsInnerJobOrderDetDto);
                    num+= WmsInnerInventoryUtil.distributionInventory(wmsInnerJobOrderDto, wmsInnerJobOrderDetDto,wmsInnerJobOrderDetDto.getPlanQty(),sysUser,(byte) 1);
                }

                return num;
            }

            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId", wmsInnerJobOrder.getJobOrderId()).andEqualTo("lineStatus",1);
            list = wmsInPutawayOrderDetMapper.selectByExample(example);
            for (WmsInnerJobOrderDet wms : list) {
                if (StringUtils.isEmpty(wms)) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                }
                //推荐库位
//                Long storageId = wmsInPutawayOrderMapper.findStorage(wms.getMaterialId(), wmsInnerJobOrder.getWarehouseId(), sysUser.getOrganizationId());
                JobRuleDto jobRuleDto = new JobRuleDto();
                jobRuleDto.setPackageQty(wms.getPlanQty());
                jobRuleDto.setWarehouseId(wmsInnerJobOrder.getWarehouseId());
                jobRuleDto.setMaterialId(wms.getMaterialId());
                jobRuleDto.setBatchCode(StringUtils.isEmpty(wms.getBatchCode())?null:wms.getBatchCode());
                jobRuleDto.setProDate(StringUtils.isEmpty(wms.getProductionDate())?null:DateUtils.getDateString(wms.getProductionDate(),"yyyy-MM-dd"));
                ResponseEntity<List<StorageRuleDto>> responseEntity = baseFeignApi.JobRule(jobRuleDto);
                if(responseEntity.getCode()!=0){
                    throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
                }
                List<StorageRuleDto> listStorage = responseEntity.getData();
                if(listStorage.size()<1){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"暂无分配库位");
                }

                BigDecimal totalQty = wms.getPlanQty();
                for (StorageRuleDto storageRuleDto : listStorage) {
                    //计划数量>库位的可上架数量 库位容量不够
                    if(totalQty.compareTo(storageRuleDto.getPutawayQty())==1){
                        wmsInnerJobOrder.setOrderStatus((byte)3);
                        wmsInnerJobOrder.setWorkStartTime(new Date());
                        wmsInnerJobOrder.setWorkEndtTime(new Date());

                        //可上架数量小于计划数量新增一条新明细 分配数量=可上架数量
                        WmsInnerJobOrderDet wmsInnerJobOrderDetNew = new WmsInnerJobOrderDet();
                        BeanUtil.copyProperties(wms,wmsInnerJobOrderDetNew);
                        wmsInnerJobOrderDetNew.setJobOrderDetId(null);
                        wmsInnerJobOrderDetNew.setPlanQty(storageRuleDto.getPutawayQty());
                        wmsInnerJobOrderDetNew.setDistributionQty(storageRuleDto.getPutawayQty());
                        wmsInnerJobOrderDetNew.setInStorageId(storageRuleDto.getStorageId());
                        wmsInnerJobOrderDetNew.setLineStatus((byte)2);
                        wmsInnerJobOrderDetNew.setCreateTime(new Date());
                        num += wmsInPutawayOrderDetMapper.insertUseGeneratedKeys(wmsInnerJobOrderDetNew);

                        //获取明细详情
                        SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
                        searchWmsInnerJobOrderDet.setJobOrderDetId(wmsInnerJobOrderDetNew.getJobOrderDetId());
                        WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);
                        //分配库存
                        //num += this.updateInventory(wmsInnerJobOrderDto, wmsInnerJobOrderDetDto);
                        num+= WmsInnerInventoryUtil.distributionInventory(wmsInnerJobOrderDto, wmsInnerJobOrderDetDto,wmsInnerJobOrderDetDto.getDistributionQty(),sysUser,(byte) 1);

                        wms = wmsInnerJobOrderDetNew;

                        //计划数量=计划数量-已分配数量
                        totalQty = totalQty.subtract(storageRuleDto.getPutawayQty());

                    }
                    else {
                        //计划数量<=库位的可上架数量 可全部分配计划数
                        num += wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(WmsInnerJobOrderDet.builder()
                                .jobOrderDetId(wms.getJobOrderDetId())
                                .inStorageId(storageRuleDto.getStorageId())
                                .distributionQty(totalQty)
                                .modifiedUserId(sysUser.getUserId())
                                .modifiedTime(new Date())
                                .lineStatus((byte) 2) //行状态(1-待分配、2-待作业、3-完成)
                                .workStartTime(new Date())
                                .workEndTime(new Date())
                                .build());
                        //获取明细详情
                        SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
                        searchWmsInnerJobOrderDet.setJobOrderDetId(wms.getJobOrderDetId());
                        WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);
                        //分配库存
                        //num += this.updateInventory(wmsInnerJobOrderDto, wmsInnerJobOrderDetDto);
                        num+= WmsInnerInventoryUtil.distributionInventory(wmsInnerJobOrderDto, wmsInnerJobOrderDetDto,wmsInnerJobOrderDetDto.getDistributionQty(),sysUser,(byte) 1);
                        totalQty = BigDecimal.ZERO;
                    }

                    if(totalQty.compareTo(BigDecimal.ZERO)!=1){
                        break;
                    }

                }
                if(totalQty.compareTo(BigDecimal.ZERO)==1){
                    //如果当前明细的计划数量>0 说明库位容量已全部分配 更新当前明细的剩余计划数
                    num += wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(WmsInnerJobOrderDet.builder()
                            .jobOrderDetId(wms.getJobOrderDetId())
                            .inStorageId(null)
                            .planQty(totalQty)
                            .distributionQty(BigDecimal.ZERO)
                            .modifiedUserId(sysUser.getUserId())
                            .modifiedTime(new Date())
                            .lineStatus((byte) 1) //行状态(1-待分配、2-待作业、3-完成)
                            .workStartTime(null)
                            .workEndTime(null)
                            .build());

                    wmsInnerJobOrder.setOrderStatus((byte)2);

                }
            }

            wmsInnerJobOrder.setModifiedTime(new Date());
            wmsInnerJobOrder.setModifiedUserId(sysUser.getUserId());
            wmsInPutawayOrderMapper.updateByPrimaryKeySelective(wmsInnerJobOrder);
        }
        return num;
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
        SysUser sysUser = currentUser();
        int num = 0;
        for (WmsInnerJobOrderDet wmsInPutawayOrderDet : list) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(wmsInPutawayOrderDet.getJobOrderId());
            if(StringUtils.isEmpty(wmsInnerJobOrder.getWarehouseId())){
                throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"请先输入仓库再进行分配作业 上架单号-->"+wmsInnerJobOrder.getJobOrderCode());
            }
            if (!StringUtils.isEmpty(wmsInPutawayOrderDet.getDistributionQty()) && wmsInPutawayOrderDet.getDistributionQty().doubleValue() > wmsInPutawayOrderDet.getPlanQty().doubleValue()) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"分配数量不能大于计划数量");
            }
            Long id = null;
            BigDecimal distributionQty = BigDecimal.ZERO;
            WmsInnerJobOrderDet wms = new WmsInnerJobOrderDet();

            if(StringUtils.isEmpty(wmsInPutawayOrderDet.getInStorageId())){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"移入库位ID不能为空");
            }

            if(StringUtils.isEmpty(wmsInPutawayOrderDet.getOutStorageId())){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"移出库位ID不能为空");
            }

            if(wmsInPutawayOrderDet.getInStorageId().equals(wmsInPutawayOrderDet.getOutStorageId())){
                throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"移入库位不能与移出库位相同");
            }

            //当货品分配时未全部分配完时新增一条剩余待分配数量的记录
            if (StringUtils.isNotEmpty(wmsInPutawayOrderDet.getDistributionQty()) && wmsInPutawayOrderDet.getDistributionQty().compareTo(wmsInPutawayOrderDet.getPlanQty()) == -1) {
                //分配中
                wms = new WmsInnerJobOrderDet();
                BeanUtil.copyProperties(wmsInPutawayOrderDet, wms);
                wms.setJobOrderDetId(null);
                wms.setPlanQty(wmsInPutawayOrderDet.getDistributionQty());
                wms.setDistributionQty(wmsInPutawayOrderDet.getDistributionQty());
                wms.setLineStatus((byte) 2);
                num += wmsInPutawayOrderDetMapper.insertUseGeneratedKeys(wms);
                id = wms.getJobOrderDetId();

                wmsInPutawayOrderDet.setLineStatus((byte) 1);
                distributionQty = wmsInPutawayOrderDet.getDistributionQty();
                wmsInPutawayOrderDet.setDistributionQty(null);
                wmsInPutawayOrderDet.setInStorageId(null);
                wmsInPutawayOrderDet.setPlanQty(wmsInPutawayOrderDet.getPlanQty().subtract(wms.getDistributionQty()));
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                num += wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
            } else if (wmsInPutawayOrderDet.getDistributionQty().compareTo(wmsInPutawayOrderDet.getPlanQty()) == 0) {
                //分配完成
                wmsInPutawayOrderDet.setLineStatus((byte) 2);
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                num += wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
                id = wmsInPutawayOrderDet.getJobOrderDetId();
                wms = wmsInPutawayOrderDet;
            }
            SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
            searchWmsInnerJobOrderDet.setJobOrderId(wmsInPutawayOrderDet.getJobOrderId());
            List<WmsInnerJobOrderDetDto> dto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet);

            SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
            searchWmsInnerJobOrder.setJobOrderId(wmsInPutawayOrderDet.getJobOrderId());
            WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInPutawayOrderMapper.findList(searchWmsInnerJobOrder).get(0);
            searchWmsInnerJobOrderDet.setJobOrderDetId(wmsInPutawayOrderDet.getJobOrderDetId());
            WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);

            if (wmsInnerJobOrderDto.getJobOrderType() == (byte) 1) {
                //上架分配库存
                //num += this.updateInventory(wmsInnerJobOrderDto, wms);
                num+= WmsInnerInventoryUtil.distributionInventory(wmsInnerJobOrderDto, wms,wms.getDistributionQty(),sysUser,(byte) 1);
            } else if(wmsInnerJobOrderDto.getJobOrderType() == (byte) 3){
                //旧  移位作业 手动分配逻辑
                Example example = new Example(WmsInnerInventory.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("materialId", wmsInnerJobOrderDetDto.getMaterialId())
                        .andEqualTo("warehouseId", wmsInnerJobOrderDto.getWarehouseId())
                        .andEqualTo("storageId", wmsInnerJobOrderDetDto.getOutStorageId())
                        .andEqualTo("jobOrderDetId", wmsInPutawayOrderDet.getJobOrderDetId())
                        .andEqualTo("relevanceOrderCode", wmsInnerJobOrder.getJobOrderCode())
                        .andEqualTo("jobStatus", (byte) 2)
                        .andEqualTo("stockLock", 0)
                        .andEqualTo("qcLock", 0)
                        .andEqualTo("lockStatus", 0)
                        .andEqualTo("orgId",sysUser.getOrganizationId());
                WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
                if (StringUtils.isEmpty(wmsInnerInventory)) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                }
                if (StringUtils.isEmpty(wmsInnerInventory.getPackingQty()) || wmsInnerInventory.getPackingQty().compareTo(BigDecimal.ZERO) == -1) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"数据变动,请恢复单据");
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
                        .andEqualTo("relevanceOrderCode", wmsInnerJobOrder.getJobOrderCode())
                        .andEqualTo("jobOrderDetId", id)
                        .andEqualTo("jobStatus", (byte) 2)
                        .andEqualTo("stockLock", 0)
                        .andEqualTo("qcLock", 0)
                        .andEqualTo("lockStatus", 0)
                        .andEqualTo("orgId",sysUser.getOrganizationId());
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
            List<WmsInnerJobOrderDetDto> orderDetDtos = dto.stream().filter(li -> li.getLineStatus() != null && li.getLineStatus() == (byte) 2).collect(Collectors.toList());
            if (!orderDetDtos.isEmpty() && orderDetDtos.size() == dto.size()) {
                //更新表头状态
                //完工入库单需要激活状态 其他则不需要
                Byte status = 3;
                wmsInPutawayOrderMapper.updateByPrimaryKeySelective(WmsInnerJobOrder.builder()
                        .jobOrderId(wmsInPutawayOrderDet.getJobOrderId())
                        .orderStatus(status)
                        .modifiedTime(new Date())
                        .build());
            } else {
                wmsInPutawayOrderMapper.updateByPrimaryKeySelective(WmsInnerJobOrder.builder()
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
    public int cancelDistribution(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        int num = 0;
        for (String s : arrId) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(s);
            if (StringUtils.isEmpty(wmsInnerJobOrder)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            if (wmsInnerJobOrder.getOrderStatus() == (byte) 1) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"单据处于未分配状态");
            }
            if (wmsInnerJobOrder.getOrderStatus() == (byte) 4 || wmsInnerJobOrder.getOrderStatus() == (byte) 5) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"单据作业中，无法取消");
            }
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId", s);
            List<WmsInnerJobOrderDet> list = wmsInPutawayOrderDetMapper.selectByExample(example);

            // 键值=来源单据编码 sourceOrderCode+来源明细ID sourceId+核心明细ID coreSourceId
            Map<String, List<WmsInnerJobOrderDet>> map = new HashMap<>();
            for (WmsInnerJobOrderDet wmsInnerJobOrderDet : list) {
                String sourceOrderCode=wmsInnerJobOrderDet.getSourceOrderCode();
                String sourceIdS=StringUtils.isEmpty(wmsInnerJobOrderDet.getSourceId())?"":wmsInnerJobOrderDet.getSourceId().toString();
                String coreSourceIdS=StringUtils.isEmpty(wmsInnerJobOrderDet.getCoreSourceId())?"":wmsInnerJobOrderDet.getCoreSourceId().toString();
                String keyS=sourceOrderCode+sourceIdS+coreSourceIdS;

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
                    exampleInventory.createCriteria().andEqualTo("jobOrderDetId", wmsInnerJobOrderDet.getJobOrderDetId()).andEqualTo("jobStatus", (byte) 2).andEqualTo("relevanceOrderCode", wmsInnerJobOrder.getJobOrderCode());
                    wmsInnerInventoryMapper.deleteByExample(exampleInventory);
                }else {
                    // JobOrderType=3 是移位作业 移位作业取消分配逻辑
                    Example example1 = new Example(WmsInnerInventory.class);
                    Example.Criteria criteria = example1.createCriteria();
                    criteria.andEqualTo("materialId", wmsInnerJobOrderDet.getMaterialId())
                            .andEqualTo("warehouseId", wmsInnerJobOrder.getWarehouseId())
                            .andEqualTo("storageId", wmsInnerJobOrderDet.getOutStorageId())
                            .andEqualTo("relevanceOrderCode", wmsInnerJobOrder.getJobOrderCode())
                            .andEqualTo("jobStatus", (byte) 2)
                            .andEqualTo("stockLock", 0)
                            .andEqualTo("qcLock", 0)
                            .andEqualTo("lockStatus", 0)
                            .andEqualTo("orgId",sysUser.getOrganizationId());
                    List<WmsInnerInventory> inventories = wmsInnerInventoryMapper.selectByExample(example1);
                    if(inventories.isEmpty()){
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
            wmsInPutawayOrderDetMapper.deleteByExample(example);
            //还原明细
            for (List<WmsInnerJobOrderDet> value : map.values()) {
                for (WmsInnerJobOrderDet wmsInnerJobOrderDet : value) {
                    wmsInnerJobOrderDet.setDistributionQty(null);
                    wmsInnerJobOrderDet.setInStorageId(null);
                    wmsInnerJobOrderDet.setLineStatus((byte) 1);
                    wmsInnerJobOrderDet.setModifiedTime(new Date());
                    wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
                    wmsInnerJobOrderDet.setLineStatus((byte) 1);
                    num += wmsInPutawayOrderDetMapper.insertSelective(wmsInnerJobOrderDet);
                }
            }
            wmsInnerJobOrder.setOrderStatus((byte) 1);
            num += wmsInPutawayOrderMapper.updateByPrimaryKeySelective(wmsInnerJobOrder);
        }
        return num;
    }

    /**
     * 指定工作人员
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int distributionWorker(Long jobOrderId,Long workerId) {
        int num=0;
        SysUser sysUser = currentUser();
        WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(jobOrderId);
        if (StringUtils.isEmpty(wmsInnerJobOrder)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        if (wmsInnerJobOrder.getOrderStatus() == (byte) 5) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"上架单已完成作业，无法分配作业人员");
        }

        wmsInnerJobOrder.setWorkerId(workerId);
        wmsInnerJobOrder.setModifiedTime(new Date());
        wmsInnerJobOrder.setModifiedUserId(sysUser.getUserId());
        num=wmsInPutawayOrderMapper.updateByPrimaryKeySelective(wmsInnerJobOrder);

        return num;
    }

    /**
     * 整单确认
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int allReceiving(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        int num = 0;
        for (String id : arrId) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(wmsInnerJobOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            if (wmsInnerJobOrder.getOrderStatus() < (byte) 3) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"上架单未分配完成,无法全部上架");
            }
            if (wmsInnerJobOrder.getOrderStatus() == (byte) 5) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"单据确认已完成");
            }
            Example example = new Example(WmsInnerJobOrderDet.class);
            Example.Criteria criteriaH = example.createCriteria();
            criteriaH.andEqualTo("jobOrderId", wmsInnerJobOrder.getJobOrderId())
            .andNotEqualTo("lineStatus",(byte)3);

            List<WmsInnerJobOrderDet> wmsInnerJobOrderDets = wmsInPutawayOrderDetMapper.selectByExample(example);

            for (WmsInnerJobOrderDet wmsInnerJobOrderDet : wmsInnerJobOrderDets) {
                //判断条码数量是否和明细数量相等
                SearchWmsInnerMaterialBarcodeReOrder sBarcodeReOrder=new SearchWmsInnerMaterialBarcodeReOrder();
                sBarcodeReOrder.setOrderTypeCode("IN-IWK");
                sBarcodeReOrder.setOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
                List<WmsInnerMaterialBarcodeReOrderDto> reOrderList=wmsInnerMaterialBarcodeReOrderService.findList(ControllerUtil.dynamicConditionByEntity(sBarcodeReOrder));
                if(reOrderList.size()<=0){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"上架单未找到相应的条码数据-->"+wmsInnerJobOrder.getJobOrderCode());
                }

                BigDecimal totalQty=reOrderList.stream().map(WmsInnerMaterialBarcodeReOrderDto::getQty).reduce(BigDecimal.ZERO,BigDecimal::add);
                if(totalQty.compareTo(wmsInnerJobOrderDet.getPlanQty())!=0){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012009.getCode(),"条码数量和明细计划数量不相等");
                }

                for (WmsInnerMaterialBarcodeReOrderDto reOrderDto : reOrderList) {
                    //更新条码关系表为已提交
                    reOrderDto.setScanStatus((byte)3);
                    reOrderDto.setModifiedTime(new Date());
                    reOrderDto.setModifiedUserId(sysUser.getUserId());
                    num+=wmsInnerMaterialBarcodeReOrderMapper.updateByPrimaryKeySelective(reOrderDto);

                    //更新来料条码为已上架
                    WmsInnerMaterialBarcode wmsInnerMaterialBarcode = new WmsInnerMaterialBarcode();
                    wmsInnerMaterialBarcode.setMaterialBarcodeId(reOrderDto.getMaterialBarcodeId());
                    wmsInnerMaterialBarcode.setBarcodeStatus((byte) 5);
                    num += wmsInnerMaterialBarcodeMapper.updateByPrimaryKeySelective(wmsInnerMaterialBarcode);
                }

                SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
                searchWmsInnerJobOrderDet.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
                WmsInnerJobOrderDetDto oldDto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);

                num += wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(WmsInnerJobOrderDet.builder()
                        .jobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId())
                        .lineStatus((byte) 3)
                        .actualQty(wmsInnerJobOrderDet.getDistributionQty())
                        .workStartTime(new Date())
                        .workEndTime(new Date())
                        .modifiedUserId(sysUser.getUserId())
                        .modifiedTime(new Date())
                        .build());

                //更改库存为正常状态
                WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);

                // 新 作业类型(1-上架 2-拣货 3-移位）
                if (wmsInnerJobOrder.getJobOrderType() == (byte) 1) {
                    //上架作业逻辑
                    //num = this.Inventory(wmsInnerJobOrder,oldDto, wmsInnerJobOrderDetDto);

                    //减少分配库存
                    num= WmsInnerInventoryUtil.distributionInventory(wmsInnerJobOrder, oldDto,oldDto.getDistributionQty(),sysUser,(byte) 2);
                    if(num==0){
                        throw new BizErrorException(ErrorCodeEnum.OPT20012006.getCode(),"更新分配库存失败");
                    }
                    //增加上架库存
                    num=WmsInnerInventoryUtil.updateInventory(wmsInnerJobOrder,wmsInnerJobOrderDetDto,sysUser);

                }
                if (wmsInnerJobOrder.getJobOrderType() == (byte) 3) {
                    // 更改库存 移位作业逻辑
                    Example example1 = new Example(WmsInnerInventory.class);
                    Example.Criteria criteria = example1.createCriteria();
                    criteria.andEqualTo("materialId", oldDto.getMaterialId())
                            .andEqualTo("warehouseId", wmsInnerJobOrder.getWarehouseId())
                            .andEqualTo("storageId", oldDto.getOutStorageId())
                            .andEqualTo("jobOrderDetId", oldDto.getJobOrderDetId())
                            .andEqualTo("jobStatus", (byte) 2)
                            .andEqualTo("stockLock", 0)
                            .andEqualTo("qcLock", 0)
                            .andEqualTo("lockStatus", 0)
                            .andEqualTo("orgId",sysUser.getOrganizationId());
                    WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example1);
                    example1.clear();
                    Example.Criteria criteria1 = example1.createCriteria().andEqualTo("materialId", oldDto.getMaterialId())
                            .andEqualTo("warehouseId", wmsInnerJobOrder.getWarehouseId())
                            .andEqualTo("storageId", oldDto.getInStorageId())
                            .andEqualTo("jobStatus", (byte) 1)
                            .andEqualTo("stockLock", 0)
                            .andEqualTo("qcLock", 0)
                            .andEqualTo("lockStatus", 0)
                            .andGreaterThan("packingQty", 0)
                            .andEqualTo("orgId",sysUser.getOrganizationId());
                    if (StringUtils.isNotEmpty(wmsInnerInventory)){
                        criteria1.andEqualTo("inventoryStatusId", wmsInnerInventory.getInventoryStatusId());
                    }

                    //获取初期数量
                    WmsInnerInventory innerInventory = wmsInnerInventoryMapper.selectByPrimaryKey(wmsInnerInventory.getParentInventoryId());
                    if(StringUtils.isEmpty(innerInventory.getPackingQty())){
                        innerInventory.setPackingQty(BigDecimal.ZERO);
                    }

                    WmsInnerInventory wmsInnerInventory_old = wmsInnerInventoryMapper.selectOneByExample(example1);
                    BigDecimal packageQty = BigDecimal.ZERO;
                    if (StringUtils.isEmpty(wmsInnerInventory_old)) {
                        if (StringUtils.isEmpty(wmsInnerInventory)) {
                            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                        }
                        wmsInnerInventory.setJobStatus((byte) 1);
                        wmsInnerInventory.setStorageId(oldDto.getInStorageId());
                        wmsInnerInventoryService.updateByPrimaryKeySelective(wmsInnerInventory);
                    } else {
                        packageQty = wmsInnerInventory_old.getPackingQty();
                        wmsInnerInventory_old.setPackingQty(wmsInnerInventory_old.getPackingQty() != null ? wmsInnerInventory_old.getPackingQty().add(wmsInnerInventory.getPackingQty()) : wmsInnerInventory.getPackingQty());
                        wmsInnerInventory_old.setRelevanceOrderCode(wmsInnerInventory.getRelevanceOrderCode());
                        wmsInnerInventoryService.updateByPrimaryKeySelective(wmsInnerInventory_old);
                        wmsInnerInventoryService.delete(wmsInnerInventory);
                    }
                    InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrder,oldDto,innerInventory.getPackingQty().add(wmsInnerInventory.getPackingQty()),wmsInnerInventory.getPackingQty(),(byte)3,(byte)2);
                    InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrder,oldDto,packageQty,wmsInnerInventory.getPackingQty(),(byte)3,(byte)1);
                    //更新库存明细
                    Example example2 = new Example(WmsInnerJobOrderDetBarcode.class);
                    example2.createCriteria().andEqualTo("jobOrderDetId", oldDto.getJobOrderDetId());
                    List<WmsInnerJobOrderDetBarcode> orderDetBarcodeList = wmsInnerJobOrderDetBarcodeService.selectByExample(example2);
                    if (!orderDetBarcodeList.isEmpty()) {
                        for (WmsInnerJobOrderDetBarcode jobOrderDetBarcode : orderDetBarcodeList) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("storageId", oldDto.getOutStorageId());
                            map.put("materialBarcodeId", jobOrderDetBarcode.getMaterialBarcodeId());
                            List<WmsInnerInventoryDetDto> inventoryDetDtos = wmsInnerInventoryDetMapper.findList(map);
                            if (inventoryDetDtos.isEmpty()) {
                                throw new BizErrorException(ErrorCodeEnum.PDA5001004);
                            }
                            WmsInnerInventoryDetDto inventoryDetDto = inventoryDetDtos.get(0);
                            inventoryDetDto.setStorageId(oldDto.getInStorageId());
                            wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(inventoryDetDto);
                        }
                    }
                }

            }

            WmsInnerJobOrder innerJobOrder = WmsInnerJobOrder.builder()
                    .orderStatus((byte) 5)
                    .jobOrderId(wmsInnerJobOrder.getJobOrderId())
                    .workStartTime(new Date())
                    .workEndtTime(new Date())
                    .build();
//            SearchBaseWorker searchBaseWorker = new SearchBaseWorker();
//            searchBaseWorker.setWarehouseId(wmsInnerJobOrder.getWarehouseId());
//            searchBaseWorker.setUserId(sysUser.getUserId());
//            List<BaseWorkerDto> workerDtos = baseFeignApi.findList(searchBaseWorker).getData();
//            if (!workerDtos.isEmpty()) {
//                innerJobOrder.setWorkerId(workerDtos.get(0).getWorkerId());
//            }

            //更改表头为作业完成状态
            wmsInPutawayOrderMapper.updateByPrimaryKeySelective(innerJobOrder);

        }
        return num;
    }

    /**
     * 单一确认
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int singleReceiving(List<WmsInnerJobOrderDet> wmsInPutawayOrderDets) {
        SysUser sysUser = currentUser();
        int num = 0;
        for (WmsInnerJobOrderDet wmsInPutawayOrderDet : wmsInPutawayOrderDets) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(wmsInPutawayOrderDet.getJobOrderId());
            if(StringUtils.isEmpty(wmsInnerJobOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            if (wmsInnerJobOrder.getOrderStatus() == (byte) 5) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"单据确认已完成");
            }
            BigDecimal aqty = wmsInPutawayOrderDet.getActualQty();

            SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
            searchWmsInnerJobOrderDet.setJobOrderDetId(wmsInPutawayOrderDet.getJobOrderDetId());
            WmsInnerJobOrderDetDto oldDto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);

            BigDecimal actualQty = BigDecimal.ZERO;
            Long jobOrderDetId = null;
            if (wmsInPutawayOrderDet.getActualQty().compareTo(wmsInPutawayOrderDet.getDistributionQty()) == -1) {
                WmsInnerJobOrderDet wms = new WmsInnerJobOrderDet();
                BeanUtil.copyProperties(wmsInPutawayOrderDet, wms);
                wms.setJobOrderDetId(null);
                wms.setPlanQty(wmsInPutawayOrderDet.getActualQty());
                wms.setDistributionQty(wmsInPutawayOrderDet.getActualQty());
                wms.setActualQty(wmsInPutawayOrderDet.getActualQty());
                wms.setLineStatus((byte) 3);
                wms.setWorkStartTime(new Date());
                wms.setWorkEndTime(new Date());
                wms.setWorkEndTime(new Date());
                wms.setOption3("finish");
                num += wmsInPutawayOrderDetMapper.insertUseGeneratedKeys(wms);
                jobOrderDetId = wms.getJobOrderDetId();

                wmsInPutawayOrderDet.setLineStatus((byte) 2);
                wmsInPutawayOrderDet.setInStorageId(null);
                wmsInPutawayOrderDet.setPlanQty(wmsInPutawayOrderDet.getPlanQty().subtract(wms.getActualQty()));
                wmsInPutawayOrderDet.setDistributionQty(wmsInPutawayOrderDet.getDistributionQty().subtract(wms.getActualQty()));
                actualQty = wmsInPutawayOrderDet.getActualQty();
                wmsInPutawayOrderDet.setActualQty(null);
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                num += wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
            } else if (wmsInPutawayOrderDet.getDistributionQty().compareTo(wmsInPutawayOrderDet.getPlanQty()) == 0) {
                //确认完成
                wmsInPutawayOrderDet.setLineStatus((byte) 3);
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                wmsInPutawayOrderDet.setWorkStartTime(new Date());
                wmsInPutawayOrderDet.setWorkEndTime(new Date());
                wmsInPutawayOrderDet.setOption3("finish");
                num += wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
                jobOrderDetId = wmsInPutawayOrderDet.getJobOrderDetId();
            }

            //更改库存
            SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
            searchWmsInnerJobOrder.setJobOrderId(wmsInPutawayOrderDet.getJobOrderId());
            WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInPutawayOrderMapper.findList(searchWmsInnerJobOrder).get(0);
            searchWmsInnerJobOrderDet.setJobOrderDetId(jobOrderDetId);
            List<WmsInnerJobOrderDetDto> wmsInner = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet);
            WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto = wmsInner.get(0);
            if(wmsInnerJobOrder.getJobOrderType() == (byte) 1){
                //更改库存 上架作业逻辑
                //num = this.Inventory(wmsInnerJobOrder,oldDto, wmsInnerJobOrderDetDto);

                //减少分配库存
                num= WmsInnerInventoryUtil.distributionInventory(wmsInnerJobOrder, oldDto,wmsInPutawayOrderDet.getActualQty(),sysUser,(byte) 2);
                if(num==0){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012006.getCode(),"更新分配库存失败");
                }
                //增加上架库存
                num=WmsInnerInventoryUtil.updateInventory(wmsInnerJobOrder,wmsInnerJobOrderDetDto,sysUser);
            }
            else if (wmsInnerJobOrder.getJobOrderType() == (byte) 3) {
                // 更改库存 作业类型(1-加工拣货 2-移位 3-上架 4-拣货 5-补货)
                // 移位作业逻辑
                Example example = new Example(WmsInnerInventory.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("materialId", oldDto.getMaterialId())
                        .andEqualTo("warehouseId", wmsInnerJobOrder.getWarehouseId())
                        .andEqualTo("storageId", oldDto.getOutStorageId())
                        .andEqualTo("jobOrderDetId", oldDto.getJobOrderDetId())
                        .andEqualTo("relevanceOrderCode", wmsInnerJobOrder.getJobOrderCode())
                        .andEqualTo("jobStatus", (byte) 2)
                        .andEqualTo("stockLock", 0)
                        .andEqualTo("qcLock", 0)
                        .andEqualTo("lockStatus", 0)
                        .andEqualTo("orgId",sysUser.getOrganizationId());
                WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
                example.clear();
                Example.Criteria criteria1 = example.createCriteria().andEqualTo("materialId", oldDto.getMaterialId())
                        .andEqualTo("warehouseId", wmsInnerJobOrder.getWarehouseId())
                        .andEqualTo("storageId", oldDto.getInStorageId())
//                        .andEqualTo("relevanceOrderCode", wmsInnerJobOrder.getJobOrderCode())
                        .andEqualTo("jobStatus", (byte) 1)
                        .andEqualTo("stockLock", 0)
                        .andEqualTo("qcLock", 0)
                        .andEqualTo("lockStatus", 0)
                        .andGreaterThan("packingQty", 0)
                        .andEqualTo("orgId",sysUser.getOrganizationId());
                if (StringUtils.isNotEmpty(wmsInnerInventory)){
                    criteria1.andEqualTo("inventoryStatusId", wmsInnerInventory.getInventoryStatusId());
                }
                WmsInnerInventory wmsInnerInventory_old = wmsInnerInventoryMapper.selectOneByExample(example);

                //获取初期数量
                WmsInnerInventory innerInventory = wmsInnerInventoryMapper.selectByPrimaryKey(wmsInnerInventory.getParentInventoryId());
                if(StringUtils.isEmpty(innerInventory.getPackingQty())){
                    innerInventory.setPackingQty(BigDecimal.ZERO);
                }
                if (StringUtils.isEmpty(wmsInnerInventory_old)) {
                    //添加库存
                    BigDecimal packingQty = wmsInnerInventory.getPackingQty();
                    WmsInnerInventory inv = new WmsInnerInventory();
                    BeanUtil.copyProperties(wmsInnerInventory, inv);
                    inv.setStorageId(oldDto.getInStorageId());
                    inv.setWarehouseId(wmsInnerJobOrder.getWarehouseId());
                    inv.setPackingQty(aqty);
                    inv.setJobStatus((byte) 1);
                    inv.setBatchCode(oldDto.getBatchCode());
                    inv.setJobOrderDetId(oldDto.getJobOrderDetId());
                    inv.setInventoryId(null);
                    inv.setCreateUserId(sysUser.getUserId());
                    inv.setCreateTime(new Date());
                    inv.setModifiedTime(new Date());
                    inv.setModifiedUserId(sysUser.getUserId());
                    inv.setOrgId(sysUser.getOrganizationId());
                    inv.setRelevanceOrderCode(wmsInnerJobOrder.getJobOrderCode());
                    inv.setJobOrderDetId(null);
                    wmsInnerInventoryMapper.insertSelective(inv);
                    wmsInnerInventory.setPackingQty(wmsInnerInventory.getPackingQty().subtract(aqty));
                    wmsInnerInventoryService.updateByPrimaryKeySelective(wmsInnerInventory);

                    BigDecimal initQty = innerInventory.getPackingQty().add(packingQty);
                    InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrder,oldDto,initQty,aqty,(byte)3,(byte)2);
                    InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrder,oldDto,BigDecimal.ZERO,aqty,(byte)3,(byte)1);
                } else {
                    BigDecimal packingQty = innerInventory.getPackingQty().add(wmsInnerInventory.getPackingQty());
                    BigDecimal initQty = wmsInnerInventory_old.getPackingQty();
                    wmsInnerInventory_old.setPackingQty(wmsInnerInventory_old.getPackingQty() != null ? wmsInnerInventory_old.getPackingQty().add(wmsInnerInventory.getPackingQty()) : wmsInnerInventory.getPackingQty());
                    wmsInnerInventory_old.setRelevanceOrderCode(wmsInnerInventory.getRelevanceOrderCode());
                    wmsInnerInventoryService.updateByPrimaryKeySelective(wmsInnerInventory_old);
                    wmsInnerInventory.setPackingQty(BigDecimal.ZERO);
                    wmsInnerInventoryService.updateByPrimaryKeySelective(wmsInnerInventory);

                    InventoryLogUtil.addLog(wmsInnerInventory_old,wmsInnerJobOrder,oldDto,packingQty,aqty,(byte)3,(byte)2);
                    InventoryLogUtil.addLog(wmsInnerInventory_old,wmsInnerJobOrder,oldDto,initQty,aqty,(byte)3,(byte)1);
                }
                //更新库存明细
                Example example1 = new Example(WmsInnerJobOrderDetBarcode.class);
                example1.createCriteria().andEqualTo("jobOrderDetId", oldDto.getJobOrderDetId());
                List<WmsInnerJobOrderDetBarcode> orderDetBarcodeList = wmsInnerJobOrderDetBarcodeService.selectByExample(example1);
                if (!orderDetBarcodeList.isEmpty()) {
                    for (WmsInnerJobOrderDetBarcode jobOrderDetBarcode : orderDetBarcodeList) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("storageId", oldDto.getOutStorageId());
                        map.put("materialBarcodeId", jobOrderDetBarcode.getMaterialBarcodeId());
                        List<WmsInnerInventoryDetDto> inventoryDetDtos = wmsInnerInventoryDetMapper.findList(map);
                        if (inventoryDetDtos.isEmpty()) {
                            throw new BizErrorException(ErrorCodeEnum.PDA5001004);
                        }
                        WmsInnerInventoryDetDto inventoryDetDto = inventoryDetDtos.get(0);
                        inventoryDetDto.setStorageId(oldDto.getInStorageId());
                        wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(inventoryDetDto);
                    }
                }
            }

            WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
            wmsInnerJobOrderDet.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
            int count = wmsInPutawayOrderDetMapper.selectCount(wmsInnerJobOrderDet);
            wmsInnerJobOrderDet.setLineStatus((byte) 3);
            int oCount = wmsInPutawayOrderDetMapper.selectCount(wmsInnerJobOrderDet);

            SearchBaseWorker searchBaseWorker = new SearchBaseWorker();
            searchBaseWorker.setWarehouseId(wmsInnerJobOrderDto.getWarehouseId());
            searchBaseWorker.setUserId(sysUser.getUserId());
            List<BaseWorkerDto> workerDtos = baseFeignApi.findList(searchBaseWorker).getData();

            if (oCount == count) {
                WmsInnerJobOrder ws = wmsInPutawayOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDto.getJobOrderId());
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
                num += wmsInPutawayOrderMapper.updateByPrimaryKeySelective(ws);

            } else {
                WmsInnerJobOrder ws = wmsInPutawayOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDto.getJobOrderId());
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
                num += wmsInPutawayOrderMapper.updateByPrimaryKeySelective(ws);
            }

        }
        //返写领料出库单

        return num;
    }

    /**
     * 按条码单一确认
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int singleReceivingByBarcode(WmsInnerJobOrderDet wmsInPutawayOrderDet,String ids) {
        SysUser sysUser = currentUser();
        int num = 0;

        WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(wmsInPutawayOrderDet.getJobOrderId());
        if(StringUtils.isEmpty(wmsInnerJobOrder)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        if (wmsInnerJobOrder.getOrderStatus() == (byte) 5) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"单据确认已完成");
        }

        if(StringUtils.isEmpty(ids)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"未选择需确认的条码信息");
        }

        List<WmsInnerMaterialBarcodeReOrderDto> reOrderDtoList=new ArrayList<>();
        BigDecimal totalQty=new BigDecimal(0);
        BigDecimal distributionQty=StringUtils.isEmpty(wmsInPutawayOrderDet.getDistributionQty())?new BigDecimal(0):wmsInPutawayOrderDet.getDistributionQty();
        String[] arrId = ids.split(",");
        for (String item : arrId) {
            SearchWmsInnerMaterialBarcodeReOrder sBarcodeReOrder=new SearchWmsInnerMaterialBarcodeReOrder();
            sBarcodeReOrder.setOrderTypeCode("IN-IWK");
            sBarcodeReOrder.setMaterialBarcodeId(Long.parseLong(item));
            List<WmsInnerMaterialBarcodeReOrderDto> barcodeReOrderDtoList= wmsInnerMaterialBarcodeReOrderService.findList(ControllerUtil.dynamicConditionByEntity(sBarcodeReOrder));
            if(barcodeReOrderDtoList.size()>0) {
                totalQty = totalQty.add((StringUtils.isEmpty(barcodeReOrderDtoList.get(0).getQty()) ? new BigDecimal(0) : barcodeReOrderDtoList.get(0).getQty()));
                reOrderDtoList.add(barcodeReOrderDtoList.get(0));
            }
        }

        //判断是否大于分配数
        if(totalQty.compareTo(distributionQty)==1){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"确认条码物料总数大于明细分配数量");
        }

        SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
        searchWmsInnerJobOrderDet.setJobOrderDetId(wmsInPutawayOrderDet.getJobOrderDetId());
        WmsInnerJobOrderDetDto oldDto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);

        Long jobOrderDetId = null;
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
            num += wmsInPutawayOrderDetMapper.insertUseGeneratedKeys(wms);
            jobOrderDetId = wms.getJobOrderDetId();

            wmsInPutawayOrderDet.setLineStatus((byte) 2);
            wmsInPutawayOrderDet.setInStorageId(null);
            wmsInPutawayOrderDet.setPlanQty(wmsInPutawayOrderDet.getPlanQty().subtract(totalQty));
            wmsInPutawayOrderDet.setDistributionQty(wmsInPutawayOrderDet.getDistributionQty().subtract(totalQty));
            wmsInPutawayOrderDet.setActualQty(null);
            wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInPutawayOrderDet.setModifiedTime(new Date());
            num += wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
        } else if (totalQty.compareTo(wmsInPutawayOrderDet.getDistributionQty()) == 0) {
            //确认完成
            wmsInPutawayOrderDet.setLineStatus((byte) 3);
            wmsInPutawayOrderDet.setActualQty(totalQty);
            wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInPutawayOrderDet.setModifiedTime(new Date());
            wmsInPutawayOrderDet.setWorkStartTime(new Date());
            wmsInPutawayOrderDet.setWorkEndTime(new Date());
            num += wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
            jobOrderDetId = wmsInPutawayOrderDet.getJobOrderDetId();
        }

        //更改库存
        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setJobOrderId(wmsInPutawayOrderDet.getJobOrderId());
        WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInPutawayOrderMapper.findList(searchWmsInnerJobOrder).get(0);
        searchWmsInnerJobOrderDet.setJobOrderDetId(jobOrderDetId);
        List<WmsInnerJobOrderDetDto> wmsInner = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet);
        WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto = wmsInner.get(0);
        if(wmsInnerJobOrder.getJobOrderType() == (byte) 1){
            //更改库存 上架作业逻辑
            //num = this.Inventory(wmsInnerJobOrder,oldDto, wmsInnerJobOrderDetDto);

            //减少分配库存
            num= WmsInnerInventoryUtil.distributionInventory(wmsInnerJobOrder, oldDto,totalQty,sysUser,(byte) 2);
            if(num==0){
                throw new BizErrorException(ErrorCodeEnum.OPT20012006.getCode(),"更新分配库存失败");
            }
            //增加上架库存
            num=WmsInnerInventoryUtil.updateInventory(wmsInnerJobOrder,wmsInnerJobOrderDetDto,sysUser);
        }

        //更新条码状态
        for (WmsInnerMaterialBarcodeReOrderDto reOrderDto : reOrderDtoList) {
            reOrderDto.setScanStatus((byte)3);
            reOrderDto.setModifiedUserId(sysUser.getUserId());
            reOrderDto.setModifiedTime(new Date());
            wmsInnerMaterialBarcodeReOrderMapper.updateByPrimaryKeySelective(reOrderDto);

            WmsInnerMaterialBarcode wmsInnerMaterialBarcode=new WmsInnerMaterialBarcode();
            wmsInnerMaterialBarcode.setMaterialBarcodeId(reOrderDto.getMaterialBarcodeId());
            wmsInnerMaterialBarcode.setBarcodeStatus((byte)5);//已上架
            wmsInnerMaterialBarcode.setModifiedUserId(sysUser.getUserId());
            wmsInnerMaterialBarcode.setModifiedTime(new Date());
            wmsInnerMaterialBarcodeMapper.updateByPrimaryKeySelective(wmsInnerMaterialBarcode);
        }

        WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
        wmsInnerJobOrderDet.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
        int count = wmsInPutawayOrderDetMapper.selectCount(wmsInnerJobOrderDet);
        wmsInnerJobOrderDet.setLineStatus((byte) 3);
        int oCount = wmsInPutawayOrderDetMapper.selectCount(wmsInnerJobOrderDet);

        SearchBaseWorker searchBaseWorker = new SearchBaseWorker();
        searchBaseWorker.setWarehouseId(wmsInnerJobOrderDto.getWarehouseId());
        searchBaseWorker.setUserId(sysUser.getUserId());
        List<BaseWorkerDto> workerDtos = baseFeignApi.findList(searchBaseWorker).getData();

        if (oCount == count) {
            WmsInnerJobOrder ws = wmsInPutawayOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDto.getJobOrderId());
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
            num += wmsInPutawayOrderMapper.updateByPrimaryKeySelective(ws);

        } else {
            WmsInnerJobOrder ws = wmsInPutawayOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDto.getJobOrderId());
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
            num += wmsInPutawayOrderMapper.updateByPrimaryKeySelective(ws);
        }


        //返写领料出库单

        return num;
    }

    /**
     * 回写上游单据
     * @return
     */
    public int updateLastOrderNode(WmsInnerJobOrder wmsInnerJobOrder,WmsInnerJobOrderDet wmsInnerJobOrderDet){
        int num=0;

        //来源系统单据类型编码
        String sourceSysOrderTypeCode=wmsInnerJobOrder.getSourceSysOrderTypeCode();
        //来源明细ID
        Long sourceId=wmsInnerJobOrderDet.getSourceId();
        //上架数量
        BigDecimal actualQty=wmsInnerJobOrderDet.getActualQty();
        switch (sourceSysOrderTypeCode) {
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
                QmsIncomingInspectionOrder incomingOrder=new QmsIncomingInspectionOrder();
                incomingOrder.setIncomingInspectionOrderId(sourceId);
                //incomingOrder
//                incomingOrder.setIfAllIssued((byte)0);//是否已全部下发(0-否 1-是)
                //qmsFeignApi.updateIfAllIssued(incomingOrder);
                break;
            default:
                break;
        }

        return num;
    }

    /**
     * 校验条码
     * 上架 PDA扫描条码
     * @param barCode
     * @return 包装数量
     */
    @Override
    public Map<String, Object> checkBarcode(String barCode, Long jobOrderDetId) {
        SysUser sysUser = currentUser();
        //找来源单据的条码明细表 去判断条码的正确性
        WmsInnerJobOrderDet wmsInnerJobOrderDet = wmsInPutawayOrderDetMapper.selectByPrimaryKey(jobOrderDetId);
        WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDet.getJobOrderId());
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(wmsInnerJobOrderDet)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        //获取完工入库记录的工单
        Long materialId = null;
        //WmsInAsnOrderDet wms = null;
        if(wmsInnerJobOrder.getSourceSysOrderTypeCode().equals("9")){
            materialId = wmsInPutawayOrderDetMapper.findEngMaterial(wmsInnerJobOrderDet.getSourceId());
        }else {
//             wms = inFeignApi.findDetList(SearchWmsInAsnOrderDet.builder()
//                    .asnOrderDetId(wmsInnerJobOrderDet.getSourceId())
//                    .build()).getData().get(0);
//            materialId = wms.getMaterialId();
        }

        SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
        searchBaseMaterial.setMaterialId(materialId);
        List<BaseMaterial> baseMaterialList = baseFeignApi.findList(searchBaseMaterial).getData();
        String materialCode = null;
        if(StringUtils.isNotEmpty(baseMaterialList)){
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
     * PDA扫码上架新增库存明细
     *
     * @return
     */
    private int addInventoryDet(Long asnOrderId, String jobOrderCode, WmsInnerJobOrderDet wmsInnerJobOrderDet, String barcode) {
        //获取完工入库单单号
//        List<WmsInAsnOrderDto> wmsInAsnOrderDtoList = inFeignApi.findList(SearchWmsInAsnOrder.builder()
//                .asnOrderId(asnOrderId)
//                .build()).getData();
//        if(StringUtils.isEmpty(wmsInAsnOrderDtoList)){
//            throw new BizErrorException(ErrorCodeEnum.GL9999404);
//        }
//        String asnOrderCode = wmsInAsnOrderDtoList.get(0).getSourceOrderCode();

        Example example = new Example(WmsInnerInventoryDet.class);
        //example.createCriteria().andEqualTo("asnCode", asnOrderCode).andEqualTo("storageId", wmsInnerJobOrderDet.getOutStorageId()).andEqualTo("materialId", wmsInnerJobOrderDet.getMaterialId()).andEqualTo("barcode", barcode).andEqualTo("barcodeStatus",2).andEqualTo("orgId",wmsInnerJobOrderDet.getOrgId());
        WmsInnerInventoryDet wmsInnerInventoryDet = wmsInnerInventoryDetMapper.selectOneByExample(example);
        if (StringUtils.isEmpty(wmsInnerInventoryDet)) {
            throw new BizErrorException("未查询到收货条码");
        }
        //wmsInnerInventoryDet.setAsnCode(jobOrderCode);
        wmsInnerInventoryDet.setStorageId(wmsInnerJobOrderDet.getInStorageId());
        wmsInnerInventoryDet.setReceivingDate(new Date());
        wmsInnerInventoryDet.setBarcodeStatus((byte)3);
        return wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(wmsInnerInventoryDet);
    }

    /**
     * PDA上架作业扫描库位上架
     * @param storageCode
     * @param jobOrderDetId
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public WmsInnerJobOrderDet scanStorageBackQty(String storageCode, Long jobOrderDetId, BigDecimal qty, String barcode) {
        SysUser sysUser = currentUser();
        if (StringUtils.isEmpty(qty)) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"上架数量不能小于1");
        }
        if(StringUtils.isEmpty(storageCode)){
            throw new BizErrorException(ErrorCodeEnum.PDA5001003);
        }
        //通过储位编码查询储位id
        ResponseEntity<List<BaseStorage>> listStorage = baseFeignApi.findList(SearchBaseStorage.builder()
                .storageCode(storageCode)
                .codeQueryMark((byte) 1)
                .build());
        if (StringUtils.isEmpty(listStorage.getData())) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "库位编码无效-->"+storageCode);
        }

        WmsInnerJobOrderDet wmsInnerJobOrderDet = wmsInPutawayOrderDetMapper.selectByPrimaryKey(jobOrderDetId);
        if(StringUtils.isEmpty(wmsInnerJobOrderDet)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"找不到相应的上架单明细 明细ID-->"+jobOrderDetId.toString());
        }
        if (StringUtils.isEmpty(wmsInnerJobOrderDet.getActualQty())) {
            wmsInnerJobOrderDet.setActualQty(BigDecimal.ZERO);
        }
        if (wmsInnerJobOrderDet.getActualQty().add(qty).compareTo(wmsInnerJobOrderDet.getDistributionQty()) == 1) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"上架数量不能大于分配数量");
        }
        //获取上架库位信息
        BaseStorage baseStorage = listStorage.getData().get(0);

        SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
        searchWmsInnerJobOrderDet.setJobOrderDetId(jobOrderDetId);
        WmsInnerJobOrderDetDto oldDto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);
        int num = 0;
        if (wmsInnerJobOrderDet.getActualQty().add(qty).compareTo(wmsInnerJobOrderDet.getDistributionQty()) == -1) {
            //部分上架
            WmsInnerJobOrderDet wmss = new WmsInnerJobOrderDet();
            BeanUtil.copyProperties(wmsInnerJobOrderDet, wmss);
            wmss.setJobOrderDetId(null);
            wmss.setInStorageId(baseStorage.getStorageId());
            wmss.setActualQty(qty);
            wmss.setPlanQty(qty);
            wmss.setDistributionQty(qty);
            wmss.setLineStatus((byte) 3);
            wmss.setWorkStartTime(new Date());
            wmss.setWorkEndTime(new Date());
            wmss.setOrgId(sysUser.getOrganizationId());
            num += wmsInPutawayOrderDetMapper.insertUseGeneratedKeys(wmss);
            jobOrderDetId = wmss.getJobOrderDetId();

            wmsInnerJobOrderDet.setLineStatus((byte) 2);
            wmsInnerJobOrderDet.setInStorageId(null);
            wmsInnerJobOrderDet.setPlanQty(wmsInnerJobOrderDet.getPlanQty().subtract(wmss.getPlanQty()));
            wmsInnerJobOrderDet.setDistributionQty(wmsInnerJobOrderDet.getDistributionQty().subtract(wmss.getDistributionQty()));
            wmsInnerJobOrderDet.setActualQty(null);
            wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInnerJobOrderDet.setModifiedTime(new Date());
            wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wmsInnerJobOrderDet);
            wmsInnerJobOrderDet.setInStorageId(wmss.getInStorageId());
        } else if (wmsInnerJobOrderDet.getDistributionQty().compareTo(wmsInnerJobOrderDet.getPlanQty()) == 0) {
            //确认完成
            wmsInnerJobOrderDet.setActualQty(qty);
            wmsInnerJobOrderDet.setInStorageId(baseStorage.getStorageId());
            wmsInnerJobOrderDet.setLineStatus((byte) 3);
            wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInnerJobOrderDet.setModifiedTime(new Date());
            wmsInnerJobOrderDet.setWorkStartTime(new Date());
            wmsInnerJobOrderDet.setWorkEndTime(new Date());
            num += wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wmsInnerJobOrderDet);
        }
        if (num == 0) {
            throw new BizErrorException("上架失败");
        }

        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setJobOrderId(wmsInnerJobOrderDet.getJobOrderId());
        WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDet.getJobOrderId());
        searchWmsInnerJobOrderDet.setJobOrderDetId(jobOrderDetId);
        List<WmsInnerJobOrderDetDto> wmsInnerJobOrderDetDtoList = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet);
        WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto=wmsInnerJobOrderDetDtoList.get(0);
        //更改库存
        //num = this.Inventory(wmsInnerJobOrderDto,oldDto, wmsInnerJobOrderDetDto.get(0));

        //减少分配库存
        num= WmsInnerInventoryUtil.distributionInventory(wmsInnerJobOrder, oldDto,qty,sysUser,(byte) 2);
        if(num==0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012006.getCode(),"更新分配库存失败");
        }
        //增加上架库存
        num=WmsInnerInventoryUtil.updateInventory(wmsInnerJobOrder,wmsInnerJobOrderDetDto,sysUser);

        //是否条码上架
        if(wmsInnerJobOrder.getSourceSysOrderTypeCode().equals("9")==false) {
            if (StringUtils.isEmpty(barcode)) {
                barcode = InBarcodeUtil.getWorkBarCodeList(wmsInnerJobOrder.getJobOrderId());
            }
            String[] code = barcode.split(",");
            for (String s : code) {
                //更新库存明细 wmsInnerJobOrderDto.getSourceOrderId()
                num += this.addInventoryDet(wmsInnerJobOrder.getJobOrderId(), wmsInnerJobOrder.getJobOrderCode(), wmsInnerJobOrderDet, s);
            }
        }

        WmsInnerJobOrderDet wms = new WmsInnerJobOrderDet();
        wms.setJobOrderId(wmsInnerJobOrder.getJobOrderId());
        int count = wmsInPutawayOrderDetMapper.selectCount(wms);
        wms.setLineStatus((byte) 3);
        int oCount = wmsInPutawayOrderDetMapper.selectCount(wms);

        SearchBaseWorker searchBaseWorker = new SearchBaseWorker();
        searchBaseWorker.setWarehouseId(wmsInnerJobOrder.getWarehouseId());
        searchBaseWorker.setUserId(sysUser.getUserId());
        List<BaseWorkerDto> workerDtos = baseFeignApi.findList(searchBaseWorker).getData();

        if (oCount == count) {
            WmsInnerJobOrder ws = new WmsInnerJobOrder();
            ws.setJobOrderId(wmsInnerJobOrder.getJobOrderId());
            ws.setOrderStatus((byte) 5);
            ws.setModifiedUserId(sysUser.getUserId());
            ws.setModifiedTime(new Date());
            ws.setWorkEndtTime(new Date());
            if (!workerDtos.isEmpty() && StringUtils.isEmpty(ws.getWorkerId())) {
                ws.setWorkerId(workerDtos.get(0).getWorkerId());
            }
            if (StringUtils.isEmpty(wmsInnerJobOrder.getWorkStartTime())) {
                ws.setWorkStartTime(new Date());
            }
            ws.setWorkEndtTime(new Date());
            num += wmsInPutawayOrderMapper.updateByPrimaryKeySelective(ws);

        } else {
            WmsInnerJobOrder ws = new WmsInnerJobOrder();
            ws.setJobOrderId(wmsInnerJobOrder.getJobOrderId());
            ws.setOrderStatus((byte) 4);
            ws.setModifiedUserId(sysUser.getUserId());
            ws.setModifiedTime(new Date());
            if (!workerDtos.isEmpty() && StringUtils.isEmpty(ws.getWorkerId())) {
                ws.setWorkerId(workerDtos.get(0).getWorkerId());
            }
            if (StringUtils.isEmpty(wmsInnerJobOrder.getWorkStartTime())) {
                ws.setWorkStartTime(new Date());
            }
            num += wmsInPutawayOrderMapper.updateByPrimaryKeySelective(ws);
        }

        return wmsInnerJobOrderDet;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public WmsInnerJobOrder packageAutoAdd(WmsInnerJobOrder wmsInnerJobOrder) {
        SysUser sysUser = currentUser();
        wmsInnerJobOrder.setJobOrderCode(CodeUtils.getId("WORK-"));
        wmsInnerJobOrder.setCreateTime(new Date());
        wmsInnerJobOrder.setCreateUserId(sysUser.getUserId());
        wmsInnerJobOrder.setModifiedTime(new Date());
        wmsInnerJobOrder.setModifiedUserId(sysUser.getUserId());
        int num = wmsInPutawayOrderMapper.insertUseGeneratedKeys(wmsInnerJobOrder);
        for (WmsInnerJobOrderDet wmsInPutawayOrderDet : wmsInnerJobOrder.getWmsInPutawayOrderDets()) {
            wmsInPutawayOrderDetMapper.insertUseGeneratedKeys(wmsInPutawayOrderDet.builder()
                    .jobOrderId(wmsInnerJobOrder.getJobOrderId())
                    .createTime(new Date())
                    .createUserId(sysUser.getUserId())
                    .modifiedTime(new Date())
                    .modifiedUserId(sysUser.getUserId())
                    .orgId(sysUser.getOrganizationId())
                    .build());

//            WmsInAsnOrderDto wmsInAsnOrderDto = inFeignApi.findList(SearchWmsInAsnOrder.builder()
//                    .asnOrderId(wmsInnerJobOrder.getJobOrderId())
//                    .build()).getData().get(0);
//            Example example = new Example(WmsInnerInventory.class);
//            example.createCriteria().andEqualTo("relevanceOrderCode", wmsInAsnOrderDto.getAsnCode()).andEqualTo("materialId", wmsInPutawayOrderDet.getMaterialId()).andEqualTo("batchCode", wmsInPutawayOrderDet.getBatchCode()).andEqualTo("orgId",sysUser.getOrganizationId());
//            WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
//            if (StringUtils.isEmpty(wmsInnerInventory)) {
//                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
//            }
//            WmsInnerInventory wmsInnerInventorys = new WmsInnerInventory();
//            wmsInnerInventorys.setInventoryId(wmsInnerInventory.getInventoryId());
//            wmsInnerInventorys.setJobStatus((byte) 2);
//            wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
        }
        return wmsInnerJobOrder;
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
        WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(jobOrderId);
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
        int num = wmsInPutawayOrderMapper.updateByPrimaryKeySelective(wmsInnerJobOrder);
        return num;
    }

    /**
     * 收货作业批量新增上架单
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int addList(List<WmsInnerJobOrder> list){
        SysUser sysUser = currentUser();
        for (WmsInnerJobOrder wmsInnerJobOrder : list) {
            wmsInnerJobOrder.setJobOrderCode(CodeUtils.getId("PUT-"));
            wmsInnerJobOrder.setCreateTime(new Date());
            wmsInnerJobOrder.setCreateUserId(sysUser.getUserId());
            wmsInnerJobOrder.setModifiedTime(new Date());
            wmsInnerJobOrder.setModifiedUserId(sysUser.getUserId());
            wmsInnerJobOrder.setOrgId(sysUser.getOrganizationId());
            wmsInnerJobOrder.setIsDelete((byte) 1);
            wmsInPutawayOrderMapper.insertUseGeneratedKeys(wmsInnerJobOrder);
            for (WmsInnerJobOrderDet wmsInPutawayOrderDet : wmsInnerJobOrder.getWmsInPutawayOrderDets()) {
                wmsInPutawayOrderDet.setJobOrderId(wmsInnerJobOrder.getJobOrderId());
                wmsInPutawayOrderDet.setCreateTime(new Date());
                wmsInPutawayOrderDet.setCreateUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setOrgId(sysUser.getOrganizationId());
                wmsInPutawayOrderDetMapper.insertUseGeneratedKeys(wmsInPutawayOrderDet);

                //扣拣收货库存
                Example example = new Example(WmsInnerInventory.class);
                example.createCriteria().andEqualTo("relevanceOrderCode", wmsInPutawayOrderDet.getOption1())
                        .andEqualTo("materialId", wmsInPutawayOrderDet.getMaterialId())
                        .andEqualTo("warehouseId", wmsInnerJobOrder.getWarehouseId())
                        .andEqualTo("storageId", wmsInPutawayOrderDet.getOutStorageId())
                        .andEqualTo("inventoryStatusId", wmsInPutawayOrderDet.getInventoryStatusId())
                        .andEqualTo("orgId",wmsInPutawayOrderDet.getOrgId());
                WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
                if(StringUtils.isEmpty(wmsInnerInventory)){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                }
                if (StringUtils.isEmpty(wmsInnerInventory.getPackingQty()) || wmsInnerInventory.getPackingQty().compareTo(BigDecimal.ZERO) == -1) {
                    throw new BizErrorException("数据变动,请恢复单据");
                }
                WmsInnerInventory wmsIn = new WmsInnerInventory();
                wmsIn.setInventoryId(wmsInnerInventory.getInventoryId());
                wmsIn.setPackingQty(wmsInnerInventory.getPackingQty().subtract(wmsInPutawayOrderDet.getDistributionQty()));
                wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsIn);

                //查询是否存在库存
                example = new Example(WmsInnerInventory.class);
                Example.Criteria criteria1 = example.createCriteria();
                criteria1.andEqualTo("relevanceOrderCode", wmsInnerJobOrder.getJobOrderCode())
                        .andEqualTo("materialId", wmsInPutawayOrderDet.getMaterialId())
                        .andEqualTo("warehouseId", wmsInnerJobOrder.getWarehouseId())
                        .andEqualTo("storageId", wmsInPutawayOrderDet.getOutStorageId())
                        .andEqualTo("inventoryStatusId", wmsInPutawayOrderDet.getInventoryStatusId())
                        .andEqualTo("jobOrderDetId", wmsInPutawayOrderDet.getJobOrderDetId())
                        .andEqualTo("jobStatus", (byte) 2)
                        .andEqualTo("orgId",sysUser.getOrganizationId());
                WmsInnerInventory wmsInnerInventorys = wmsInnerInventoryMapper.selectOneByExample(example);
                if (StringUtils.isEmpty(wmsInnerInventorys)) {
                    //添加库存
                    WmsInnerInventory inv = new WmsInnerInventory();
                    BeanUtil.copyProperties(wmsInnerInventory, inv);
                    inv.setStorageId(wmsInPutawayOrderDet.getOutStorageId());
                    inv.setWarehouseId(wmsInnerJobOrder.getWarehouseId());
                    inv.setRelevanceOrderCode(wmsInnerJobOrder.getJobOrderCode());
                    inv.setPackingQty(wmsInPutawayOrderDet.getDistributionQty());
                    inv.setJobStatus((byte) 2);
                    inv.setInventoryId(null);
                    inv.setJobOrderDetId(wmsInPutawayOrderDet.getJobOrderDetId());
                    inv.setOrgId(sysUser.getOrganizationId());
                    inv.setCreateUserId(sysUser.getUserId());
                    inv.setCreateTime(new Date());
                    inv.setModifiedTime(new Date());
                    inv.setModifiedUserId(sysUser.getUserId());
                    wmsInnerInventoryMapper.insertSelective(inv);
                } else {
                    //原库存
                    wmsInnerInventorys.setPackingQty(wmsInnerInventorys.getPackingQty().add(wmsInPutawayOrderDet.getDistributionQty()));
                    wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
                }

            }
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int cancelJobOrder(List<EngPackingOrderTakeCancel> engPackingOrderTakeCancels){
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        int num=0;
        for (EngPackingOrderTakeCancel engPackingOrderTakeCancel : engPackingOrderTakeCancels) {
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("sourceDetId",engPackingOrderTakeCancel.getPackingOrderSummaryDetId()).andEqualTo("orderStatus",3);
            example.orderBy("createTime").desc();
            List<WmsInnerJobOrderDet>list = wmsInPutawayOrderDetMapper.selectByExample(example);
            if(list.size()<1){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"获取上架单失败");
            }
            BigDecimal totalQty = engPackingOrderTakeCancel.getQty();
            for (WmsInnerJobOrderDet wmsInnerJobOrderDet : list) {
                WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDet.getJobOrderId());
                if(totalQty.compareTo(BigDecimal.ZERO)==1){
                    //计算可扣减数量=分配数量-上架数量
                    if(StringUtils.isEmpty(wmsInnerJobOrderDet.getActualQty())){
                        wmsInnerJobOrderDet.setActualQty(BigDecimal.ZERO);
                    }
                    BigDecimal calQty = wmsInnerJobOrderDet.getDistributionQty().subtract(wmsInnerJobOrderDet.getActualQty());
                    //可扣减分配数量大于扣减数量
                    if(calQty.compareTo(totalQty)==1){
                        wmsInnerJobOrderDet.setPlanQty(wmsInnerJobOrderDet.getPlanQty().subtract(totalQty));
                        wmsInnerJobOrderDet.setDistributionQty(wmsInnerJobOrderDet.getDistributionQty().subtract(totalQty));
                        //扣减库存
                        example = new Example(WmsInnerInventory.class);
                        Example.Criteria criteria1 = example.createCriteria();
                        criteria1.andEqualTo("relevanceOrderCode", wmsInnerJobOrder.getJobOrderCode())
                                .andEqualTo("materialId", wmsInnerJobOrderDet.getMaterialId())
                                .andEqualTo("warehouseId", wmsInnerJobOrder.getWarehouseId())
                                .andEqualTo("storageId", wmsInnerJobOrderDet.getOutStorageId())
                                .andEqualTo("inventoryStatusId", wmsInnerJobOrderDet.getInventoryStatusId())
                                .andEqualTo("jobOrderDetId", wmsInnerJobOrderDet.getJobOrderDetId())
                                .andEqualTo("jobStatus", (byte) 2)
                                .andEqualTo("orgId",sysUser.getOrganizationId());
                        WmsInnerInventory wmsInnerInventorys = wmsInnerInventoryMapper.selectOneByExample(example);
                        if(StringUtils.isEmpty(wmsInnerInventorys)){
                            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"未匹配到库存");
                        }
                        BigDecimal qty = wmsInnerInventorys.getPackingQty();
                        wmsInnerInventorys.setPackingQty(wmsInnerJobOrderDet.getDistributionQty());
                        num+=wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
                        num+=wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wmsInnerJobOrderDet);
                        num+=wmsInPutawayOrderMapper.updateByPrimaryKeySelective(wmsInnerJobOrder);

                        //添加库存日志
                        InventoryLogUtil.addLog(wmsInnerInventorys,wmsInnerJobOrder,wmsInnerJobOrderDet,qty,totalQty,(byte)1,(byte)2);
                        totalQty = BigDecimal.ZERO;

                    }else if(calQty.compareTo(totalQty)<1){
                        //删除库存
                        example = new Example(WmsInnerInventory.class);
                        Example.Criteria criteria1 = example.createCriteria();
                        criteria1.andEqualTo("relevanceOrderCode", wmsInnerJobOrder.getJobOrderCode())
                                .andEqualTo("materialId", wmsInnerJobOrderDet.getMaterialId())
                                .andEqualTo("warehouseId", wmsInnerJobOrder.getWarehouseId())
                                .andEqualTo("storageId", wmsInnerJobOrderDet.getOutStorageId())
                                .andEqualTo("inventoryStatusId", wmsInnerJobOrderDet.getInventoryStatusId())
                                .andEqualTo("jobOrderDetId", wmsInnerJobOrderDet.getJobOrderDetId())
                                .andEqualTo("jobStatus", (byte) 2)
                                .andEqualTo("orgId",sysUser.getOrganizationId());
                        WmsInnerInventory wmsInnerInventorys = wmsInnerInventoryMapper.selectOneByExample(example);
                        if(StringUtils.isEmpty(wmsInnerInventorys)){
                            throw new BizErrorException("未匹配到库存");
                        }
                        wmsInnerInventorys.setPackingQty(wmsInnerInventorys.getPackingQty().subtract(calQty));
                        num+=wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);

                        wmsInnerJobOrderDet.setDistributionQty(wmsInnerJobOrderDet.getDistributionQty().subtract(calQty));
                        if(StringUtils.isNotEmpty(wmsInnerJobOrderDet.getDistributionQty()) && wmsInnerJobOrderDet.getDistributionQty().compareTo(BigDecimal.ZERO)==1){
                            wmsInnerJobOrderDet.setPlanQty(wmsInnerJobOrderDet.getDistributionQty());
                            num+=wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wmsInnerJobOrderDet);
                        }else {
                            //扣除数量小于等于分配数量
                            num+=wmsInPutawayOrderDetMapper.deleteByPrimaryKey(wmsInnerJobOrderDet.getJobOrderDetId());
                        }

                        if(totalQty.compareTo(calQty)==0){
                            //删除当前单据
                            num+=wmsInPutawayOrderMapper.deleteByPrimaryKey(wmsInnerJobOrder.getJobOrderId());
                        }else {
                            example = new Example(WmsInnerJobOrderDet.class);
                            example.createCriteria().andEqualTo("jobOrderId",wmsInnerJobOrder.getJobOrderId());
                            List<WmsInnerJobOrderDet> dets = wmsInPutawayOrderDetMapper.selectByExample(example);
                            if(dets.stream().filter(dt->dt.getLineStatus()==3).collect(Collectors.toList()).size()==dets.size()){
                                wmsInnerJobOrder.setOrderStatus((byte)5);

                            }
                            num+=wmsInPutawayOrderMapper.updateByPrimaryKeySelective(wmsInnerJobOrder);
                        }
                        totalQty = totalQty.subtract(calQty);

                        //添加库存日志
                        InventoryLogUtil.addLog(wmsInnerInventorys,wmsInnerJobOrder,wmsInnerJobOrderDet,calQty,calQty,(byte)1,(byte)2);
                    }
                }else {
                    break;
                }
            }
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int save(WmsInnerJobOrder record) {
        SysUser sysUser = currentUser();
        List<WmsInnerMaterialBarcodeReOrder> barcodeReOrderList=new ArrayList<>();
        String orderTypeCode="IN-IWK";
        if (record.getJobOrderType() == (byte) 1) {
            //上架单
            record.setJobOrderCode(CodeUtils.getId("PUT-"));
        } else if (record.getJobOrderType() == (byte) 2) {
            //拣货单
            record.setJobOrderCode(CodeUtils.getId("PICK-"));
        } else if (record.getJobOrderType() == (byte) 3) {
            //移位单
            record.setJobOrderCode(CodeUtils.getId("SHIFT-"));
        }
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrgId(sysUser.getOrganizationId());
        record.setIsDelete((byte) 1);
        record.setOrderStatus((byte) 1);
        int num = wmsInPutawayOrderMapper.insertUseGeneratedKeys(record);
        for (WmsInnerJobOrderDet wmsInPutawayOrderDet : record.getWmsInPutawayOrderDets()) {
            wmsInPutawayOrderDet.setJobOrderId(record.getJobOrderId());
            wmsInPutawayOrderDet.setLineStatus((byte)1);
            wmsInPutawayOrderDet.setCreateTime(new Date());
            wmsInPutawayOrderDet.setCreateUserId(sysUser.getUserId());
            wmsInPutawayOrderDet.setModifiedTime(new Date());
            wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInPutawayOrderDet.setOrgId(sysUser.getOrganizationId());

            wmsInPutawayOrderDetMapper.insertUseGeneratedKeys(wmsInPutawayOrderDet);
            //非自建单据新增条码关系表数据
            if(record.getSourceSysOrderTypeCode().equals("SELF-CRT")==false) {
                SearchWmsInnerMaterialBarcodeReOrder sBarcodeReOrder = new SearchWmsInnerMaterialBarcodeReOrder();
                sBarcodeReOrder.setOrderTypeCode(record.getSourceSysOrderTypeCode());//单据类型
                sBarcodeReOrder.setOrderDetId(wmsInPutawayOrderDet.getSourceId());//明细ID
                List<WmsInnerMaterialBarcodeReOrderDto> reOrderList = wmsInnerMaterialBarcodeReOrderService.findList(ControllerUtil.dynamicConditionByEntity(sBarcodeReOrder));
                if (reOrderList.size() > 0) {
                    for (WmsInnerMaterialBarcodeReOrderDto item : reOrderList) {
                        WmsInnerMaterialBarcodeReOrder barcodeReOrder = new WmsInnerMaterialBarcodeReOrder();
                        BeanUtil.copyProperties(item, barcodeReOrder);
                        barcodeReOrder.setOrderTypeCode(orderTypeCode);
                        barcodeReOrder.setOrderCode(record.getJobOrderCode());
                        barcodeReOrder.setOrderId(record.getJobOrderId());
                        barcodeReOrder.setOrderDetId(wmsInPutawayOrderDet.getJobOrderDetId());
                        barcodeReOrder.setScanStatus((byte) 1);
                        barcodeReOrder.setCreateUserId(sysUser.getUserId());
                        barcodeReOrder.setCreateTime(new Date());
                        barcodeReOrder.setModifiedUserId(sysUser.getUserId());
                        barcodeReOrder.setModifiedTime(new Date());
                        barcodeReOrder.setMaterialBarcodeReOrderId(null);
                        barcodeReOrderList.add(barcodeReOrder);
                    }
                }
            }

            if (record.getJobOrderType() == (byte) 3) {
                // 生成库存，扣减原库存 移位作业
                WmsInnerInventory innerInventory = wmsInnerInventoryService.selectByKey(wmsInPutawayOrderDet.getSourceId());
                if (innerInventory.getPackingQty().compareTo(wmsInPutawayOrderDet.getPlanQty()) < 0) {
                    throw new BizErrorException(ErrorCodeEnum.PDA5001012);
                }
                WmsInnerInventory newInnerInventory = new WmsInnerInventory();
                BeanUtil.copyProperties(innerInventory, newInnerInventory);
                newInnerInventory.setPackingQty(wmsInPutawayOrderDet.getPlanQty());
                newInnerInventory.setJobStatus((byte) 2);
                newInnerInventory.setJobOrderDetId(wmsInPutawayOrderDet.getJobOrderDetId());
                newInnerInventory.setOrgId(sysUser.getOrganizationId());
                newInnerInventory.setCreateTime(new Date());
                newInnerInventory.setCreateUserId(sysUser.getUserId());
                newInnerInventory.setParentInventoryId(innerInventory.getInventoryId());
                newInnerInventory.setRelevanceOrderCode(record.getJobOrderCode());
                wmsInnerInventoryService.save(newInnerInventory);
                // 变更减少原库存
                innerInventory.setPackingQty(innerInventory.getPackingQty().subtract(wmsInPutawayOrderDet.getPlanQty()));
                wmsInnerInventoryService.update(innerInventory);
            }
        }
        //批量新增到条码关系表
        if(barcodeReOrderList.size()>0){
            wmsInnerMaterialBarcodeReOrderService.batchAdd(barcodeReOrderList);
        }

        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(WmsInnerJobOrder record) {
        SysUser sysUser = currentUser();
        if (record.getOrderStatus() != (byte) 1) {
            throw new BizErrorException("移位单已分配，不可变更修改");
        }

        if (record.getJobOrderType() == (byte) 2) {
            // 查询明细
            SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
            searchWmsInnerJobOrderDet.setJobOrderId(record.getJobOrderId());
            List<WmsInnerJobOrderDetDto> orderDetMapperList = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet);
            if (!orderDetMapperList.isEmpty()) {
                // 查询明细对应的库存
                Example example = new Example(WmsInnerInventory.class);
                example.createCriteria().andIn("jobOrderDetId", orderDetMapperList.stream().map(WmsInnerJobOrderDet::getJobOrderDetId).collect(Collectors.toList()));
                List<WmsInnerInventory> innerInventories = wmsInnerInventoryMapper.selectByExample(example);
                // 查询明细库存对应的原库存
                example.clear();
                example.createCriteria().andIn("inventoryId", innerInventories.stream().map(WmsInnerInventory::getParentInventoryId).collect(Collectors.toList()));
                List<WmsInnerInventory> sourceInnerInventories = wmsInnerInventoryMapper.selectByExample(example);
                for (WmsInnerInventory source : sourceInnerInventories) {
                    for (WmsInnerInventory innerInventory : innerInventories) {
                        if (innerInventory.getParentInventoryId().equals(source.getInventoryId())) {
                            source.setPackingQty(source.getPackingQty().add(innerInventory.getPackingQty()));
                        }
                    }
                }

                if (!sourceInnerInventories.isEmpty()) {
                    // 批量修改原库存
                    wmsInnerInventoryService.batchUpdate(sourceInnerInventories);
                }
                if (!innerInventories.isEmpty()) {
                    // 删除明细库存
                    wmsInnerInventoryService.batchDelete(innerInventories);
                }
                if (!innerInventories.isEmpty()) {
                    // 删除明细
                    List<WmsInnerJobOrderDet> detList = new ArrayList<>();
                    for (WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto : orderDetMapperList) {
                        WmsInnerJobOrderDet det = wmsInnerJobOrderDetDto;
                        detList.add(det);
                    }
                    wmsInnerJobOrderDetService.batchDelete(detList);
                }
            }

            for (WmsInnerJobOrderDet wmsInPutawayOrderDet : record.getWmsInPutawayOrderDets()) {
                wmsInPutawayOrderDet.setJobOrderId(record.getJobOrderId());
                wmsInPutawayOrderDet.setCreateTime(new Date());
                wmsInPutawayOrderDet.setCreateUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setOrgId(sysUser.getOrganizationId());
                wmsInPutawayOrderDetMapper.insertUseGeneratedKeys(wmsInPutawayOrderDet);

                // 生成库存，扣减原库存
                WmsInnerInventory innerInventory = wmsInnerInventoryService.selectByKey(wmsInPutawayOrderDet.getSourceId());
                if (innerInventory.getPackingQty().compareTo(wmsInPutawayOrderDet.getPlanQty()) < 0) {
                    throw new BizErrorException(ErrorCodeEnum.PDA5001012);
                }
                WmsInnerInventory newInnerInventory = new WmsInnerInventory();
                BeanUtil.copyProperties(innerInventory, newInnerInventory);
                newInnerInventory.setJobStatus((byte) 2);
                newInnerInventory.setJobOrderDetId(wmsInPutawayOrderDet.getJobOrderDetId());
                newInnerInventory.setOrgId(sysUser.getOrganizationId());
                newInnerInventory.setRelevanceOrderCode(record.getJobOrderCode());
                newInnerInventory.setCreateTime(new Date());
                newInnerInventory.setCreateUserId(sysUser.getUserId());
                newInnerInventory.setParentInventoryId(innerInventory.getInventoryId());
                newInnerInventory.setPackingQty(wmsInPutawayOrderDet.getPlanQty());
                wmsInnerInventoryService.save(newInnerInventory);
                // 变更减少原库存
                innerInventory.setPackingQty(innerInventory.getPackingQty().subtract(wmsInPutawayOrderDet.getPlanQty()));
                wmsInnerInventoryService.update(innerInventory);
            }
        }
        int i = wmsInPutawayOrderMapper.updateByPrimaryKey(record);
        return i;
    }

    /**
     * 删除
     * @param
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        for (String s : arrId) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(s);
            if (StringUtils.isEmpty(wmsInnerJobOrder)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            if (wmsInnerJobOrder.getOrderStatus() >= (byte) 4) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"单据已经作业，无法删除");
            }

            //删除已分配库存
            Example exampleInventory = new Example(WmsInnerInventory.class);
            Example.Criteria criteriaInventory = exampleInventory.createCriteria();
            criteriaInventory.andEqualTo("relevanceOrderCode", wmsInnerJobOrder.getJobOrderCode());
            criteriaInventory.andEqualTo("jobStatus", (byte) 2);
            criteriaInventory.andEqualTo("orgId",sysUser.getOrganizationId());
            wmsInnerInventoryMapper.deleteByExample(exampleInventory);

            //回写上游单据已下推数量
            Example exampleDet = new Example(WmsInnerJobOrderDet.class);
            exampleDet.createCriteria().andEqualTo("jobOrderId",wmsInnerJobOrder.getJobOrderId());
            List<WmsInnerJobOrderDet> jobOrderDets = wmsInPutawayOrderDetMapper.selectByExample(exampleDet);
            String sourceSysOrderTypeCode=wmsInnerJobOrder.getSourceSysOrderTypeCode();
            switch (sourceSysOrderTypeCode) {
                case "IN-IPO":
                    //入库计划
                    for (WmsInnerJobOrderDet jobOrderDetIPO : jobOrderDets) {

                    }
                    break;
                case "IN-SWK":
                    //收货作业
                    for (WmsInnerJobOrderDet jobOrderDetSWK : jobOrderDets) {

                    }
                    break;
                case "IN-SPO":
                    //收货计划
                    for (WmsInnerJobOrderDet jobOrderDetSPO : jobOrderDets) {

                    }
                    break;
                case "QMS-MIIO":
                    //来料检验
                    for (WmsInnerJobOrderDet jobOrderDetMIIO : jobOrderDets) {
                        Long sourceId=jobOrderDetMIIO.getSourceId();
                        QmsIncomingInspectionOrder incomingOrder=new QmsIncomingInspectionOrder();
                        incomingOrder.setIncomingInspectionOrderId(sourceId);
//                        incomingOrder.setIfAllIssued((byte)0);//是否已全部下发(0-否 1-是)
                        //qmsFeignApi.updateIfAllIssued(incomingOrder);
                    }
                    break;
                default:
                    break;
            }


            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId", s);
            wmsInPutawayOrderDetMapper.deleteByExample(example);
        }
        return wmsInPutawayOrderMapper.deleteByIds(ids);
    }

    /**
     * 关闭单据
     * @param
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int closeWmsInnerJobOrder(String ids) {
        int num=0;
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        for (String s : arrId) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(s);
            if (StringUtils.isEmpty(wmsInnerJobOrder)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            if (wmsInnerJobOrder.getOrderStatus() == (byte) 5) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"单据已经作业完成，不需关闭单据");
            }

            //删除未上架的已分配库存
            Example exampleDet = new Example(WmsInnerJobOrderDet.class);
            Example.Criteria criteriaDet = exampleDet.createCriteria();
            criteriaDet.andEqualTo("jobOrderId", wmsInnerJobOrder.getJobOrderId());
            criteriaDet.andNotEqualTo("lineStatus", (byte) 3);
            criteriaDet.andEqualTo("orgId",sysUser.getOrganizationId());
            List<WmsInnerJobOrderDet> listDet=wmsInPutawayOrderDetMapper.selectByExample(exampleDet);
            if(listDet.size()>0){
                for (WmsInnerJobOrderDet item : listDet) {
                    //删除分配库存
                    Example exampleInventory = new Example(WmsInnerInventory.class);
                    Example.Criteria criteriaInventory = exampleInventory.createCriteria();
                    criteriaInventory.andEqualTo("relevanceOrderCode", wmsInnerJobOrder.getJobOrderCode());
                    criteriaInventory.andEqualTo("jobStatus", (byte) 2);//
                    criteriaInventory.andEqualTo("jobOrderDetId", item.getJobOrderDetId());
                    criteriaInventory.andEqualTo("storageId",item.getOutStorageId());
                    criteriaInventory.andEqualTo("orgId",sysUser.getOrganizationId());
                    num=wmsInnerInventoryMapper.deleteByExample(exampleInventory);

                    //更新明细上架数量=分配数量 状态
                    item.setDistributionQty(item.getPlanQty());
                    item.setActualQty(item.getPlanQty());
                    item.setModifiedUserId(sysUser.getUserId());
                    item.setModifiedTime(new Date());
                    item.setLineStatus((byte)3);
                    if(StringUtils.isEmpty(item.getRemark()))
                        item.setRemark("关闭");
                    else
                        item.setRemark(item.getRemark()+" 关闭");

                    num=wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(item);
                }
            }

            //更新单据状态
            wmsInnerJobOrder.setOrderStatus((byte)5);
            wmsInnerJobOrder.setModifiedUserId(sysUser.getUserId());
            wmsInnerJobOrder.setModifiedTime(new Date());
            if(StringUtils.isEmpty(wmsInnerJobOrder.getRemark()))
                wmsInnerJobOrder.setRemark("关闭");
            else
                wmsInnerJobOrder.setRemark(wmsInnerJobOrder.getRemark()+" 关闭");

            num=wmsInPutawayOrderMapper.updateByPrimaryKeySelective(wmsInnerJobOrder);

        }
        return num;
    }

    /**
     * 先单后作业 检验条码
     * @param
     * @return
     */
    @Override
    public Map<String, Object> checkBarcodeHaveOrder(String ifSysBarcode, Long orderId, Long orderDetId, String barCode) {
        Map<String, Object> map = new HashMap<>();
        if(StringUtils.isEmpty(barCode)){
            throw new BizErrorException(ErrorCodeEnum.PDA40012033);
        }
        if(StringUtils.isEmpty(ifSysBarcode)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"是否系统条码不能为空");
        }
        if(StringUtils.isEmpty(orderId)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"作业单主表ID不能为空");
        }
        if(StringUtils.isEmpty(orderDetId)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"作业单明细ID不能为空");
        }
        if(StringUtils.isEmpty(barCode)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"条码不能为空");
        }
        SearchWmsInnerMaterialBarcode swmsInnerMaterialBarcode=new SearchWmsInnerMaterialBarcode();
        swmsInnerMaterialBarcode.setBarcode(barCode);
        List<WmsInnerMaterialBarcodeDto> materialBarcodeList=wmsInnerMaterialBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(swmsInnerMaterialBarcode));

        if(ifSysBarcode.equals("1")){
            map=checkBarcodeType(barCode);
            if(StringUtils.isEmpty(map.get("barcodeType"))){
                throw new BizErrorException(ErrorCodeEnum.PDA5001006.getCode(),"扫描的条码无效-->"+barCode);
            }

            //来料条码ID
            Long materialBarcodeId=Long.parseLong(map.get("materialBarcodeId").toString());
            //系统条码判断 1 判断作业单是否由上游单据下推得来
            WmsInnerJobOrder wmsInnerJobOrder=wmsInPutawayOrderMapper.selectByPrimaryKey(orderId);
            if(StringUtils.isEmpty(wmsInnerJobOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"作业单主表ID找不到对应的作业单");
            }
            WmsInnerJobOrderDet wmsInnerJobOrderDet=wmsInPutawayOrderDetMapper.selectByPrimaryKey(orderDetId);
            if(StringUtils.isEmpty(wmsInnerJobOrderDet)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"作业单明细ID找不到对应的作业单明细");
            }
            //单据类型
            String sourceSysOrderTypeCode=wmsInnerJobOrder.getSourceSysOrderTypeCode();

            if(sourceSysOrderTypeCode.equals("SELF-CRT")){
                // SELF-CRT 单据类型 自建
                SearchWmsInnerMaterialBarcodeReOrder sBarcodeReOrder=new SearchWmsInnerMaterialBarcodeReOrder();
                sBarcodeReOrder.setMaterialBarcodeId(materialBarcodeId);
                sBarcodeReOrder.setOrderTypeCode("SELF-CRT");
                sBarcodeReOrder.setOrderDetId(orderDetId);
                List<WmsInnerMaterialBarcodeReOrderDto> reOrderList=wmsInnerMaterialBarcodeReOrderService.findList(ControllerUtil.dynamicConditionByEntity(sBarcodeReOrder));
                if(reOrderList.size()<=0){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"自建的上架单未找到此条码数据-->"+barCode);
                }

                WmsInnerMaterialBarcodeReOrderDto reOrderDto=reOrderList.get(0);
                if(reOrderDto.getScanStatus()>(byte)1){
                    throw new BizErrorException(ErrorCodeEnum.PDA40012001.getCode(),"条码已扫描-->"+barCode);
                }

            }
            else if(StringUtils.isNotEmpty(sourceSysOrderTypeCode) && !sourceSysOrderTypeCode.equals("SELF-CRT")) {
                // 由上游单据下推
                SearchWmsInnerMaterialBarcodeReOrder sBarcodeReOrder=new SearchWmsInnerMaterialBarcodeReOrder();
                sBarcodeReOrder.setMaterialBarcodeId(materialBarcodeId);
                sBarcodeReOrder.setOrderTypeCode("IN-IWK");//上架作业单类型
                sBarcodeReOrder.setOrderId(orderId);
                List<WmsInnerMaterialBarcodeReOrderDto> reOrderList=wmsInnerMaterialBarcodeReOrderService.findList(ControllerUtil.dynamicConditionByEntity(sBarcodeReOrder));
                if(reOrderList.size()<=0){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"上架单未找到此条码数据-->"+barCode);
                }

                // 物料是否相等
                if(wmsInnerJobOrderDet.getMaterialId().longValue()!=swmsInnerMaterialBarcode.getMaterialId().longValue()){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"条码相应物料与当前上架物料不相等");
                }

                WmsInnerMaterialBarcodeReOrderDto reOrderDto=reOrderList.get(0);
                if(reOrderDto.getScanStatus()>(byte)1){
                    throw new BizErrorException(ErrorCodeEnum.PDA40012001.getCode(),"条码已扫描-->"+barCode);
                }
            }

        }
        else if(ifSysBarcode.equals("0")){
            //非系统条码判断
            if(materialBarcodeList.size()>0){
                SearchWmsInnerMaterialBarcodeReOrder sBarcodeReOrder=new SearchWmsInnerMaterialBarcodeReOrder();
                sBarcodeReOrder.setMaterialBarcodeId(materialBarcodeList.get(0).getMaterialBarcodeId());
                sBarcodeReOrder.setOrderTypeCode("IN-IWK");//上架作业单类型
                List<WmsInnerMaterialBarcodeReOrderDto> reOrderList=wmsInnerMaterialBarcodeReOrderService.findList(ControllerUtil.dynamicConditionByEntity(sBarcodeReOrder));
                if(reOrderList.size()>0){
                    WmsInnerMaterialBarcodeReOrderDto reOrderDto=reOrderList.get(0);
                    if(reOrderDto.getScanStatus()>(byte)1){
                        throw new BizErrorException(ErrorCodeEnum.PDA40012001.getCode(),"条码已扫描-->"+barCode);
                    }
                }
            }
            map.put("materialBarcodeId","");
            map.put("barcodeType","");
            map.put("qty",0);
        }
        return map;
    }

    /**
     * 先作业后单 检验条码
     * @param
     * @return
     */
    @Override
    public Map<String, Object> checkBarcodeNotOrder(String ifSysBarcode, String barCode) {
        Map<String, Object> map = new HashMap<>();
        SearchWmsInnerMaterialBarcode swmsInnerMaterialBarcode=new SearchWmsInnerMaterialBarcode();
        swmsInnerMaterialBarcode.setBarcode(barCode);
        List<WmsInnerMaterialBarcodeDto> materialBarcodeList=wmsInnerMaterialBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(swmsInnerMaterialBarcode));
        if(ifSysBarcode.equals("1")){
            //系统条码判断
            map=checkBarcodeType(barCode);
            if(StringUtils.isEmpty(map.get("barcodeType"))){
                throw new BizErrorException(ErrorCodeEnum.PDA5001006.getCode(),"扫描的条码无效-->"+barCode);
            }

            //来料条码ID
            Long materialBarcodeId=Long.parseLong(map.get("materialBarcodeId").toString());

            SearchWmsInnerMaterialBarcodeReOrder sBarcodeReOrder=new SearchWmsInnerMaterialBarcodeReOrder();
            sBarcodeReOrder.setMaterialBarcodeId(materialBarcodeId);
            List<WmsInnerMaterialBarcodeReOrderDto> reOrderList=wmsInnerMaterialBarcodeReOrderService.findList(ControllerUtil.dynamicConditionByEntity(sBarcodeReOrder));
            if(reOrderList.size()<=0){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"条码关系未找到此条码数据-->"+barCode);
            }

            Optional<WmsInnerMaterialBarcodeReOrderDto> barcodeReOrderDtoLast = reOrderList.stream()
                    .sorted(Comparator.comparing(WmsInnerMaterialBarcodeReOrderDto::getCreateTime).reversed())
                    .findFirst();
            if (barcodeReOrderDtoLast.isPresent()) {
                WmsInnerMaterialBarcodeReOrderDto reOrderDtoLast = barcodeReOrderDtoLast.get();
                if(reOrderDtoLast.getScanStatus()>(byte)1){
                    throw new BizErrorException(ErrorCodeEnum.PDA40012001.getCode(),"条码已扫描-->"+barCode);
                }
            }

        }
        else{
            //非系统条码判断
            if(materialBarcodeList.size()>0){
                SearchWmsInnerMaterialBarcodeReOrder sBarcodeReOrder=new SearchWmsInnerMaterialBarcodeReOrder();
                sBarcodeReOrder.setMaterialBarcodeId(materialBarcodeList.get(0).getMaterialBarcodeId());
                sBarcodeReOrder.setOrderTypeCode("IN-IWK");//上架作业单类型
                List<WmsInnerMaterialBarcodeReOrderDto> reOrderList=wmsInnerMaterialBarcodeReOrderService.findList(ControllerUtil.dynamicConditionByEntity(sBarcodeReOrder));
                if(reOrderList.size()>0){
                    WmsInnerMaterialBarcodeReOrderDto reOrderDto=reOrderList.get(0);
                    if(reOrderDto.getScanStatus()>(byte)1){
                        throw new BizErrorException(ErrorCodeEnum.PDA40012001.getCode(),"条码已扫描-->"+barCode);
                    }
                }
            }
            map.put("materialBarcodeId","");
            map.put("barcodeType","");
            map.put("qty",0);
        }
        return map;
    }

    /**
     * Web端单一确认作业 扫描条码
     * @param
     * @return
     */
    @Override
    public WmsInnerMaterialBarcodeDto checkBarcodeOrderWeb(String ifSysBarcode, Long orderId, Long orderDetId, String barCode) {
        WmsInnerMaterialBarcodeDto wmsInnerMaterialBarcodeDto=new WmsInnerMaterialBarcodeDto();
        Map<String,Object> map=new HashMap<>();
        if(StringUtils.isEmpty(barCode)){
            throw new BizErrorException(ErrorCodeEnum.PDA40012033);
        }
        if(StringUtils.isEmpty(ifSysBarcode)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"是否系统条码不能为空");
        }
        if(StringUtils.isEmpty(orderId)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"作业单主表ID不能为空");
        }
        if(StringUtils.isEmpty(orderDetId)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"作业单明细ID不能为空");
        }
        if(StringUtils.isEmpty(barCode)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"条码不能为空");
        }

        SearchWmsInnerMaterialBarcode swmsInnerMaterialBarcode=new SearchWmsInnerMaterialBarcode();
        swmsInnerMaterialBarcode.setBarcode(barCode);
        List<WmsInnerMaterialBarcodeDto> materialBarcodeList=wmsInnerMaterialBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(swmsInnerMaterialBarcode));

        if(ifSysBarcode.equals("1")){
            //系统条码判断
            map=checkBarcodeType(barCode);
            if(StringUtils.isEmpty(map.get("barcodeType"))){
                throw new BizErrorException(ErrorCodeEnum.PDA5001006.getCode(),"扫描的条码无效-->"+barCode);
            }
            //来料条码ID
            Long materialBarcodeId=Long.parseLong(map.get("materialBarcodeId").toString());

            //系统条码判断 1 判断作业单是否由上游单据下推得来
            WmsInnerJobOrder wmsInnerJobOrder=wmsInPutawayOrderMapper.selectByPrimaryKey(orderId);
            if(StringUtils.isEmpty(wmsInnerJobOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"作业单主表ID找不到对应的作业单");
            }
            WmsInnerJobOrderDet wmsInnerJobOrderDet=wmsInPutawayOrderDetMapper.selectByPrimaryKey(orderDetId);
            if(StringUtils.isEmpty(wmsInnerJobOrderDet)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"作业单明细ID找不到对应的作业单明细");
            }
            //单据类型
            String sourceSysOrderTypeCode=wmsInnerJobOrder.getSourceSysOrderTypeCode();

            if(sourceSysOrderTypeCode.equals("SELF-CRT")){
                // SELF-CRT 单据类型 自建
                SearchWmsInnerMaterialBarcodeReOrder sBarcodeReOrder=new SearchWmsInnerMaterialBarcodeReOrder();
                sBarcodeReOrder.setMaterialBarcodeId(materialBarcodeId);
                sBarcodeReOrder.setOrderTypeCode("SELF-CRT");
                sBarcodeReOrder.setOrderDetId(orderDetId);
                List<WmsInnerMaterialBarcodeReOrderDto> reOrderList=wmsInnerMaterialBarcodeReOrderService.findList(ControllerUtil.dynamicConditionByEntity(sBarcodeReOrder));
                if(reOrderList.size()<=0){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"自建的上架单未找到此条码数据-->"+barCode);
                }

                WmsInnerMaterialBarcodeReOrderDto reOrderDto=reOrderList.get(0);
                if(reOrderDto.getScanStatus()>(byte)1){
                    throw new BizErrorException(ErrorCodeEnum.PDA40012001.getCode(),"条码已扫描-->"+barCode);
                }

            }
            else if(StringUtils.isNotEmpty(sourceSysOrderTypeCode) && !sourceSysOrderTypeCode.equals("SELF-CRT")) {
                // 由上游单据下推
                SearchWmsInnerMaterialBarcodeReOrder sBarcodeReOrder=new SearchWmsInnerMaterialBarcodeReOrder();
                sBarcodeReOrder.setMaterialBarcodeId(materialBarcodeId);
                sBarcodeReOrder.setOrderTypeCode("IN-IWK");//上架作业单类型
                sBarcodeReOrder.setOrderId(orderId);
                List<WmsInnerMaterialBarcodeReOrderDto> reOrderList=wmsInnerMaterialBarcodeReOrderService.findList(ControllerUtil.dynamicConditionByEntity(sBarcodeReOrder));
                if(reOrderList.size()<=0){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"上架单未找到此条码数据-->"+barCode);
                }

                // 物料是否相等
                if(wmsInnerJobOrderDet.getMaterialId().longValue()!=swmsInnerMaterialBarcode.getMaterialId().longValue()){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"条码相应物料与当前上架物料不相等");
                }

                WmsInnerMaterialBarcodeReOrderDto reOrderDto=reOrderList.get(0);
                if(reOrderDto.getScanStatus()>(byte)1){
                    throw new BizErrorException(ErrorCodeEnum.PDA40012001.getCode(),"条码已扫描-->"+barCode);
                }
            }
            swmsInnerMaterialBarcode.setBarcode(null);
            swmsInnerMaterialBarcode.setMaterialBarcodeId(materialBarcodeId);
            materialBarcodeList=wmsInnerMaterialBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(swmsInnerMaterialBarcode));
            wmsInnerMaterialBarcodeDto=materialBarcodeList.get(0);

            return wmsInnerMaterialBarcodeDto;
        }
        else if(ifSysBarcode.equals("0")){
            //非系统条码判断
            if(materialBarcodeList.size()>0){
                SearchWmsInnerMaterialBarcodeReOrder sBarcodeReOrder=new SearchWmsInnerMaterialBarcodeReOrder();
                sBarcodeReOrder.setMaterialBarcodeId(materialBarcodeList.get(0).getMaterialBarcodeId());
                sBarcodeReOrder.setOrderTypeCode("IN-IWK");//上架作业单类型
                List<WmsInnerMaterialBarcodeReOrderDto> reOrderList=wmsInnerMaterialBarcodeReOrderService.findList(ControllerUtil.dynamicConditionByEntity(sBarcodeReOrder));
                if(reOrderList.size()>0){
                    WmsInnerMaterialBarcodeReOrderDto reOrderDto=reOrderList.get(0);
                    if(reOrderDto.getScanStatus()>(byte)1){
                        throw new BizErrorException(ErrorCodeEnum.PDA40012001.getCode(),"条码已扫描-->"+barCode);
                    }
                }
            }

            return wmsInnerMaterialBarcodeDto;

        }
        return wmsInnerMaterialBarcodeDto;
    }


    /**
     * PDA先单后作业 提交
     * @param
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public WmsInnerJobOrderDet saveHaveInnerJobOrder(List<SaveHaveInnerJobOrderDto> list) {
        SysUser sysUser = currentUser();

        Map<String,List<SaveHaveInnerJobOrderDto>> collet=list.parallelStream().collect(Collectors.groupingBy(SaveHaveInnerJobOrderDto::getIfSysBarcode));
        if(collet.size()>1){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"系统条码和非系统条码不能一起提交");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //是否系统条码(0-否 1-是)
        String ifSysBarcode=list.get(0).getIfSysBarcode();
        //明细ID
        Long jobOrderDetId=list.get(0).getJobOrderDetId();
        //存货库位
        Long inStorageId=list.get(0).getStorageId();
        //本次上架数量
        BigDecimal qty=new BigDecimal(0);
        for (SaveHaveInnerJobOrderDto saveHaveInnerJobOrderDto : list) {
            qty=qty.add(StringUtils.isEmpty(saveHaveInnerJobOrderDto.getMaterialQty())?new BigDecimal(0):saveHaveInnerJobOrderDto.getMaterialQty());
        }

        ResponseEntity<BaseStorage> rbaseStorage=baseFeignApi.detail(inStorageId);
        if(StringUtils.isEmpty(rbaseStorage.getData())){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"扫描的库位无效");
        }
        BaseStorage baseStorage=rbaseStorage.getData();
        if (StringUtils.isEmpty(qty)) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"上架数量不能小于1");
        }

        WmsInnerJobOrderDet wmsInnerJobOrderDet = wmsInPutawayOrderDetMapper.selectByPrimaryKey(jobOrderDetId);
        if(StringUtils.isEmpty(wmsInnerJobOrderDet)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"找不到相应的上架单明细 明细ID-->"+jobOrderDetId.toString());
        }
        if (StringUtils.isEmpty(wmsInnerJobOrderDet.getActualQty())) {
            wmsInnerJobOrderDet.setActualQty(BigDecimal.ZERO);
        }
        if (wmsInnerJobOrderDet.getActualQty().add(qty).compareTo(wmsInnerJobOrderDet.getDistributionQty()) == 1) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"上架数量不能大于分配数量");
        }

        SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
        searchWmsInnerJobOrderDet.setJobOrderDetId(jobOrderDetId);
        WmsInnerJobOrderDetDto oldDto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);
        int num = 0;
        if (wmsInnerJobOrderDet.getActualQty().add(qty).compareTo(wmsInnerJobOrderDet.getDistributionQty()) == -1) {
            //部分上架
            WmsInnerJobOrderDet wmss = new WmsInnerJobOrderDet();
            BeanUtil.copyProperties(wmsInnerJobOrderDet, wmss);
            wmss.setJobOrderDetId(null);
            wmss.setInStorageId(baseStorage.getStorageId());
            wmss.setActualQty(qty);
            wmss.setPlanQty(qty);
            wmss.setDistributionQty(qty);
            wmss.setLineStatus((byte) 3);
            wmss.setWorkStartTime(new Date());
            wmss.setWorkEndTime(new Date());
            wmss.setOrgId(sysUser.getOrganizationId());
            num += wmsInPutawayOrderDetMapper.insertUseGeneratedKeys(wmss);
            jobOrderDetId = wmss.getJobOrderDetId();

            wmsInnerJobOrderDet.setLineStatus((byte) 2);
            wmsInnerJobOrderDet.setInStorageId(null);
            wmsInnerJobOrderDet.setPlanQty(wmsInnerJobOrderDet.getPlanQty().subtract(wmss.getPlanQty()));
            wmsInnerJobOrderDet.setDistributionQty(wmsInnerJobOrderDet.getDistributionQty().subtract(wmss.getDistributionQty()));
            wmsInnerJobOrderDet.setActualQty(null);
            wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInnerJobOrderDet.setModifiedTime(new Date());
            wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wmsInnerJobOrderDet);
            wmsInnerJobOrderDet.setInStorageId(wmss.getInStorageId());
        } else if (wmsInnerJobOrderDet.getDistributionQty().compareTo(wmsInnerJobOrderDet.getPlanQty()) == 0) {
            //确认完成
            wmsInnerJobOrderDet.setActualQty(qty);
            wmsInnerJobOrderDet.setInStorageId(baseStorage.getStorageId());
            wmsInnerJobOrderDet.setLineStatus((byte) 3);
            wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInnerJobOrderDet.setModifiedTime(new Date());
            wmsInnerJobOrderDet.setWorkStartTime(new Date());
            wmsInnerJobOrderDet.setWorkEndTime(new Date());
            num += wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wmsInnerJobOrderDet);
        }
        if (num == 0) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012006.getCode(),"上架失败");
        }

        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setJobOrderId(wmsInnerJobOrderDet.getJobOrderId());
        WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDet.getJobOrderId());
        searchWmsInnerJobOrderDet.setJobOrderDetId(jobOrderDetId);
        List<WmsInnerJobOrderDetDto> wmsInnerJobOrderDetDtoList = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet);
        WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto=wmsInnerJobOrderDetDtoList.get(0);

        //减少分配库存
        num= WmsInnerInventoryUtil.distributionInventory(wmsInnerJobOrder, oldDto,qty,sysUser,(byte) 2);
        if(num==0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012006.getCode(),"更新分配库存失败");
        }
        //增加上架库存
        num=WmsInnerInventoryUtil.updateInventory(wmsInnerJobOrder,wmsInnerJobOrderDetDto,sysUser);
        //条码关系集合
        List<WmsInnerMaterialBarcodeReOrder> materialBarcodeReOrderList=new ArrayList<>();
        //条码库存集合
        List<WmsInnerInventoryDet> wmsInnerInventoryDets =new ArrayList<>();

        for (SaveHaveInnerJobOrderDto item : list) {
            if (ifSysBarcode.equals("0")) {
                //非系统条码
                WmsInnerMaterialBarcode wmsInnerMaterialBarcode = new WmsInnerMaterialBarcode();
                wmsInnerMaterialBarcode.setMaterialId(item.getMaterialId());
                wmsInnerMaterialBarcode.setBarcode(item.getBarcode());
                wmsInnerMaterialBarcode.setBatchCode(item.getBatchCode());
                wmsInnerMaterialBarcode.setMaterialQty(item.getMaterialQty());
                wmsInnerMaterialBarcode.setIfSysBarcode((byte) 0);
                Date productionTime=null;
                try {
                    productionTime=sdf.parse(item.getProductionTime());
                }catch (Exception ex){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"参数生产日期转换为时间类型异常");
                }

                wmsInnerMaterialBarcode.setProductionTime(productionTime);

                //产生类型(1-供应商条码 2-自己打印 3-生产条码)
                wmsInnerMaterialBarcode.setCreateType((byte) 1);
                //条码状态(1-已生成 2-已打印 3-已收货 4-已质检 5-已上架 6-已出库)
                wmsInnerMaterialBarcode.setBarcodeStatus((byte) 5);
                wmsInnerMaterialBarcode.setOrgId(sysUser.getOrganizationId());
                wmsInnerMaterialBarcode.setCreateTime(new Date());
                wmsInnerMaterialBarcode.setCreateUserId(sysUser.getUserId());

                num += wmsInnerMaterialBarcodeMapper.insertUseGeneratedKeys(wmsInnerMaterialBarcode);
                //设置来料条码ID
                item.setMaterialBarcodeId(wmsInnerMaterialBarcode.getMaterialBarcodeId());
                //条码关系
                WmsInnerMaterialBarcodeReOrder wmsInnerMaterialBarcodeReOrder=new WmsInnerMaterialBarcodeReOrder();
                wmsInnerMaterialBarcodeReOrder.setOrderTypeCode("IN-IWK");//类型 上架
                wmsInnerMaterialBarcodeReOrder.setOrderCode(wmsInnerJobOrder.getJobOrderCode());
                wmsInnerMaterialBarcodeReOrder.setOrderId(wmsInnerJobOrder.getJobOrderId());
                //来料条码ID
                wmsInnerMaterialBarcodeReOrder.setMaterialBarcodeId(wmsInnerMaterialBarcode.getMaterialBarcodeId());

                wmsInnerMaterialBarcodeReOrder.setScanStatus((byte)3);
                wmsInnerMaterialBarcodeReOrder.setOrgId(sysUser.getOrganizationId());
                wmsInnerMaterialBarcodeReOrder.setCreateTime(new Date());
                wmsInnerMaterialBarcodeReOrder.setCreateUserId(sysUser.getUserId());
                materialBarcodeReOrderList.add(wmsInnerMaterialBarcodeReOrder);

                WmsInnerInventoryDet inventoryDet=new WmsInnerInventoryDet();
                inventoryDet.setStorageId(inStorageId);
                inventoryDet.setMaterialId(item.getMaterialId());
                inventoryDet.setBarcode(item.getBarcode());
                inventoryDet.setMaterialQty(item.getMaterialQty());
                inventoryDet.setProductionDate(productionTime);
                inventoryDet.setProductionBatchCode(item.getBatchCode());
                inventoryDet.setJobStatus((byte)2);//在库
                inventoryDet.setIfStockLock((byte)0);
                inventoryDet.setInventoryStatusId(wmsInnerJobOrderDet.getInventoryStatusId());
                inventoryDet.setBarcodeStatus((byte)3);//在库
                inventoryDet.setCreateUserId(sysUser.getUserId());
                inventoryDet.setCreateTime(new Date());
                wmsInnerInventoryDets.add(inventoryDet);

            } else {
                //系统条码更新条码状态
                //条码类别处理
                SearchWmsInnerMaterialBarcode sWmsBarcode=new SearchWmsInnerMaterialBarcode();
                if(item.getBarcodeType()==((byte)2)){
                    sWmsBarcode.setColorBoxCode(item.getBatchCode());
                }
                else if(item.getBarcodeType()==((byte)3)){
                    sWmsBarcode.setCartonCode(item.getBatchCode());
                }
                else if(item.getBarcodeType()==((byte)4)){
                    sWmsBarcode.setPalletCode(item.getBatchCode());
                }
                List<WmsInnerMaterialBarcodeDto> materialDtoList=wmsInnerMaterialBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(sWmsBarcode));
                for (WmsInnerMaterialBarcodeDto materialBarcodeDto : materialDtoList) {
                    WmsInnerMaterialBarcode upBarcode=new WmsInnerMaterialBarcode();
                    upBarcode.setMaterialBarcodeId(materialBarcodeDto.getMaterialBarcodeId());
                    upBarcode.setBarcodeStatus((byte)5);
                    upBarcode.setModifiedUserId(sysUser.getUserId());
                    upBarcode.setModifiedTime(new Date());
                    wmsInnerMaterialBarcodeMapper.updateByPrimaryKeySelective(upBarcode);

                    //更新条码关系表条码状态
                    SearchWmsInnerMaterialBarcodeReOrder sReOrder=new SearchWmsInnerMaterialBarcodeReOrder();
                    sReOrder.setOrderTypeCode("IN-IWK");
                    sReOrder.setOrderId(wmsInnerJobOrder.getJobOrderId());
                    sReOrder.setMaterialBarcodeId(item.getMaterialBarcodeId());
                    List<WmsInnerMaterialBarcodeReOrderDto> reOrderDtoList=wmsInnerMaterialBarcodeReOrderService.findList(ControllerUtil.dynamicConditionByEntity(sReOrder));
                    if(reOrderDtoList.size()<=0){
                        throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"条码关系表找不到此条码数据 条码ID-->"+item.getMaterialBarcodeId().toString());
                    }
                    WmsInnerMaterialBarcodeReOrder wmsInnerMaterialBarcodeReOrder=new WmsInnerMaterialBarcodeReOrder();
                    wmsInnerMaterialBarcodeReOrder.setMaterialBarcodeReOrderId(reOrderDtoList.get(0).getMaterialBarcodeReOrderId());
                    wmsInnerMaterialBarcodeReOrder.setScanStatus((byte)3);
                    wmsInnerMaterialBarcodeReOrder.setModifiedUserId(sysUser.getUserId());
                    wmsInnerMaterialBarcodeReOrder.setModifiedTime(new Date());
                    wmsInnerMaterialBarcodeReOrderMapper.updateByPrimaryKeySelective(wmsInnerMaterialBarcodeReOrder);

                    WmsInnerInventoryDet inventoryDet=new WmsInnerInventoryDet();
                    inventoryDet.setStorageId(inStorageId);
                    inventoryDet.setMaterialId(materialBarcodeDto.getMaterialId());
                    inventoryDet.setBarcode(materialBarcodeDto.getBarcode());
                    inventoryDet.setMaterialQty(materialBarcodeDto.getMaterialQty());
                    inventoryDet.setProductionDate(materialBarcodeDto.getProductionTime());
                    inventoryDet.setProductionBatchCode(materialBarcodeDto.getBatchCode());
                    inventoryDet.setSupplierId(materialBarcodeDto.getSupplierId());
                    inventoryDet.setJobStatus((byte)2);//在库
                    inventoryDet.setIfStockLock((byte)0);
                    inventoryDet.setCartonCode(materialBarcodeDto.getCartonCode());
                    inventoryDet.setInventoryStatusId(wmsInnerJobOrderDet.getInventoryStatusId());
                    inventoryDet.setBarcodeStatus((byte)3);//在库
                    inventoryDet.setCreateUserId(sysUser.getUserId());
                    inventoryDet.setCreateTime(new Date());
                    wmsInnerInventoryDets.add(inventoryDet);
                }
            }
        }
        //非系统条码增加到关系表
        if(materialBarcodeReOrderList.size()>0){
            wmsInnerMaterialBarcodeReOrderService.batchAdd(materialBarcodeReOrderList);
        }
        //增加条码库存明细
        if(wmsInnerInventoryDets.size()>0){
            WmsInnerInventoryUtil.updateInventoryDet(wmsInnerInventoryDets);
        }

        WmsInnerJobOrderDet wms = new WmsInnerJobOrderDet();
        wms.setJobOrderId(wmsInnerJobOrder.getJobOrderId());
        int count = wmsInPutawayOrderDetMapper.selectCount(wms);
        wms.setLineStatus((byte) 3);
        int oCount = wmsInPutawayOrderDetMapper.selectCount(wms);

        SearchBaseWorker searchBaseWorker = new SearchBaseWorker();
        searchBaseWorker.setWarehouseId(wmsInnerJobOrder.getWarehouseId());
        searchBaseWorker.setUserId(sysUser.getUserId());
        List<BaseWorkerDto> workerDtos = baseFeignApi.findList(searchBaseWorker).getData();

        if (oCount == count) {
            WmsInnerJobOrder ws = new WmsInnerJobOrder();
            ws.setJobOrderId(wmsInnerJobOrder.getJobOrderId());
            ws.setOrderStatus((byte) 5);
            ws.setModifiedUserId(sysUser.getUserId());
            ws.setModifiedTime(new Date());
            ws.setWorkEndtTime(new Date());
            if (!workerDtos.isEmpty() && StringUtils.isEmpty(ws.getWorkerId())) {
                ws.setWorkerId(workerDtos.get(0).getWorkerId());
            }
            if (StringUtils.isEmpty(wmsInnerJobOrder.getWorkStartTime())) {
                ws.setWorkStartTime(new Date());
            }
            ws.setWorkEndtTime(new Date());
            num += wmsInPutawayOrderMapper.updateByPrimaryKeySelective(ws);

        } else {
            WmsInnerJobOrder ws = new WmsInnerJobOrder();
            ws.setJobOrderId(wmsInnerJobOrder.getJobOrderId());
            ws.setOrderStatus((byte) 4);
            ws.setModifiedUserId(sysUser.getUserId());
            ws.setModifiedTime(new Date());
            if (!workerDtos.isEmpty() && StringUtils.isEmpty(ws.getWorkerId())) {
                ws.setWorkerId(workerDtos.get(0).getWorkerId());
            }
            if (StringUtils.isEmpty(wmsInnerJobOrder.getWorkStartTime())) {
                ws.setWorkStartTime(new Date());
            }
            num += wmsInPutawayOrderMapper.updateByPrimaryKeySelective(ws);
        }

        return wmsInnerJobOrderDet;

    }

    /**
     * PDA先作业后单 提交产生上架单
     * @param
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public WmsInnerJobOrder saveInnerJobOrder(List<SaveInnerJobOrderDto> list) {
        SysUser sysUser = currentUser();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int num=0;
        //判断是否已创建上架单
        Long jobOrderIdExist=list.get(0).getJobOrderId();
        Long warehouseIdExist=null;
        WmsInnerJobOrder jobOrder=new WmsInnerJobOrder();
        if(StringUtils.isNotEmpty(jobOrderIdExist)){
            jobOrder=wmsInPutawayOrderMapper.selectByPrimaryKey(jobOrderIdExist);
            if(StringUtils.isNotEmpty(jobOrder))
                warehouseIdExist=jobOrder.getWarehouseId();
        }
        WmsInnerJobOrder record=new WmsInnerJobOrder();
        //是否系统条码(0-否 1-是)
        String ifSysBarcode=list.get(0).getIfSysBarcode();

        //存货库位
        Long inStorageId=list.get(0).getStorageId();
        ResponseEntity<BaseStorage> rbaseStorage=baseFeignApi.detail(inStorageId);
        if(StringUtils.isEmpty(rbaseStorage.getData())){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"扫描的库位无效");
        }
        BaseStorage baseStorage=rbaseStorage.getData();
        if(baseStorage.getStorageType()!=(byte)1){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"扫描的库位不是存货库位");
        }

        //仓库
        Long warehouseId=baseStorage.getWarehouseId();
        if(StringUtils.isEmpty(warehouseId)){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"扫描的库位未设置对应的仓库");
        }

        //如果已提交生成的上架单仓库和此次提交的库位仓库不相等 报错
        if(StringUtils.isNotEmpty(warehouseIdExist) && warehouseIdExist.longValue()!=warehouseId.longValue()){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"此次提交库位对应仓库与已提交的库位仓库不相等");
        }

        //找仓库合格状态
        Long inventoryStatusId=null;
        SearchBaseInventoryStatus searchBaseInventoryStatus=new SearchBaseInventoryStatus();
        searchBaseInventoryStatus.setWarehouseId(warehouseId);
        searchBaseInventoryStatus.setInventoryStatusName("合格");
        ResponseEntity<List<BaseInventoryStatus>> rStatus=baseFeignApi.findList(searchBaseInventoryStatus);
        if(StringUtils.isNotEmpty(rStatus.getData()))
            inventoryStatusId=rStatus.getData().get(0).getInventoryStatusId();

        //找收货库位
        SearchBaseStorage searchBaseStorage=new SearchBaseStorage();
        searchBaseStorage.setStorageType((byte)2);// 储位类型 1 存货 2 收货
        ResponseEntity<List<BaseStorage>> rBaseStorageList=baseFeignApi.findList(searchBaseStorage);
        if(StringUtils.isEmpty(rBaseStorageList.getData())){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"未找到收货库位");
        }
        //收货库位
        Long outStorageId=rBaseStorageList.getData().get(0).getStorageId();

        //创建上架单
        if(StringUtils.isEmpty(jobOrderIdExist)) {
            record.setJobOrderType((byte) 1);
            if (record.getJobOrderType() == (byte) 1) {
                //上架单
                record.setJobOrderCode(CodeUtils.getId("PUT-"));
            } else if (record.getJobOrderType() == (byte) 2) {
                //拣货单
                record.setJobOrderCode(CodeUtils.getId("PICK-"));
            } else if (record.getJobOrderType() == (byte) 3) {
                //移位单
                record.setJobOrderCode(CodeUtils.getId("SHIFT-"));
            }

            record.setWarehouseId(warehouseId);
            record.setWorkerId(sysUser.getUserId());
            //开始作业时间
            record.setWorkStartTime(new Date());
            record.setCreateTime(new Date());
            record.setCreateUserId(sysUser.getUserId());
            record.setModifiedTime(new Date());
            record.setModifiedUserId(sysUser.getUserId());
            record.setOrgId(sysUser.getOrganizationId());
            record.setIsDelete((byte) 1);
            record.setOrderStatus((byte) 4);//作业中
            num = wmsInPutawayOrderMapper.insertUseGeneratedKeys(record);
        }
        else {
            BeanUtil.copyProperties(jobOrder,record);
        }

        //物料+批次号分组 数量求和
        Map<String, List<WmsInnerJobOrderDet>> map = new HashMap<>();
        List<WmsInnerMaterialBarcodeReOrder> materialBarcodeReOrderList=new ArrayList<>();
        //条码库存集合
        List<WmsInnerInventoryDet> wmsInnerInventoryDets =new ArrayList<>();

        for (SaveInnerJobOrderDto saveInnerJobOrderDto : list) {
            String keyS=saveInnerJobOrderDto.getMaterialId().toString()+(StringUtils.isEmpty(saveInnerJobOrderDto.getBatchCode())?"":saveInnerJobOrderDto.getBatchCode());
            if (map.containsKey(keyS)) {
                List<WmsInnerJobOrderDet> nm = new ArrayList<>();
                for (WmsInnerJobOrderDet innerJobOrderDet : map.get(keyS)) {
                    innerJobOrderDet.setPlanQty(innerJobOrderDet.getPlanQty().add(saveInnerJobOrderDto.getMaterialQty()));
                    nm.add(innerJobOrderDet);
                }
                map.put(keyS, nm);
            } else {
                List<WmsInnerJobOrderDet> list1 = new ArrayList<>();
                WmsInnerJobOrderDet wmsInnerJobOrderDet=new WmsInnerJobOrderDet();
                wmsInnerJobOrderDet.setJobOrderId(record.getJobOrderId());
                wmsInnerJobOrderDet.setLineStatus((byte)3);
                wmsInnerJobOrderDet.setMaterialId(saveInnerJobOrderDto.getMaterialId());
                wmsInnerJobOrderDet.setPlanQty(saveInnerJobOrderDto.getMaterialQty());
                wmsInnerJobOrderDet.setOutStorageId(outStorageId);
                wmsInnerJobOrderDet.setInStorageId(inStorageId);
                wmsInnerJobOrderDet.setInventoryStatusId(inventoryStatusId);
                wmsInnerJobOrderDet.setBatchCode(saveInnerJobOrderDto.getBatchCode());
                wmsInnerJobOrderDet.setWorkStartTime(new Date());
                wmsInnerJobOrderDet.setWorkEndTime(new Date());
                wmsInnerJobOrderDet.setCreateTime(new Date());
                wmsInnerJobOrderDet.setCreateUserId(sysUser.getUserId());
                list1.add(wmsInnerJobOrderDet);
                map.put(keyS, list1);
            }
            //新增非系统条码到条码表
            if(ifSysBarcode.equals("0")){
                WmsInnerMaterialBarcode wmsInnerMaterialBarcode=new WmsInnerMaterialBarcode();
                wmsInnerMaterialBarcode.setMaterialId(saveInnerJobOrderDto.getMaterialId());
                wmsInnerMaterialBarcode.setBarcode(saveInnerJobOrderDto.getBarcode());
                wmsInnerMaterialBarcode.setBatchCode(saveInnerJobOrderDto.getBatchCode());
                wmsInnerMaterialBarcode.setMaterialQty(saveInnerJobOrderDto.getMaterialQty());
                wmsInnerMaterialBarcode.setIfSysBarcode((byte)0);
                Date productionTime=null;
                try {
                    productionTime=sdf.parse(saveInnerJobOrderDto.getProductionTime());
                }catch (Exception ex){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"参数生产日期转换为时间类型异常");
                }
                wmsInnerMaterialBarcode.setProductionTime(productionTime);

                //产生类型(1-供应商条码 2-自己打印 3-生产条码)
                wmsInnerMaterialBarcode.setCreateType((byte)1);
                //条码状态(1-已生成 2-已打印 3-已收货 4-已质检 5-已上架 6-已出库)
                wmsInnerMaterialBarcode.setBarcodeStatus((byte)5);
                wmsInnerMaterialBarcode.setOrgId(sysUser.getOrganizationId());
                wmsInnerMaterialBarcode.setCreateTime(new Date());
                wmsInnerMaterialBarcode.setCreateUserId(sysUser.getUserId());

                num+=wmsInnerMaterialBarcodeMapper.insertUseGeneratedKeys(wmsInnerMaterialBarcode);
                //设置来料条码ID
                saveInnerJobOrderDto.setMaterialBarcodeId(wmsInnerMaterialBarcode.getMaterialBarcodeId());

                WmsInnerMaterialBarcodeReOrder wmsInnerMaterialBarcodeReOrder = new WmsInnerMaterialBarcodeReOrder();
                wmsInnerMaterialBarcodeReOrder.setOrderTypeCode("IN-IWK");//类型 上架
                wmsInnerMaterialBarcodeReOrder.setOrderCode(record.getJobOrderCode());
                wmsInnerMaterialBarcodeReOrder.setOrderId(record.getJobOrderId());
                //来料条码ID
                wmsInnerMaterialBarcodeReOrder.setMaterialBarcodeId(saveInnerJobOrderDto.getMaterialBarcodeId());
                wmsInnerMaterialBarcodeReOrder.setScanStatus((byte) 3);
                wmsInnerMaterialBarcodeReOrder.setOrgId(sysUser.getOrganizationId());
                wmsInnerMaterialBarcodeReOrder.setCreateTime(new Date());
                wmsInnerMaterialBarcodeReOrder.setCreateUserId(sysUser.getUserId());

                //条码关系集合
                materialBarcodeReOrderList.add(wmsInnerMaterialBarcodeReOrder);

                WmsInnerInventoryDet inventoryDet=new WmsInnerInventoryDet();
                inventoryDet.setStorageId(inStorageId);
                inventoryDet.setMaterialId(saveInnerJobOrderDto.getMaterialId());
                inventoryDet.setBarcode(saveInnerJobOrderDto.getBarcode());
                inventoryDet.setMaterialQty(saveInnerJobOrderDto.getMaterialQty());
                inventoryDet.setProductionDate(productionTime);
                inventoryDet.setProductionBatchCode(saveInnerJobOrderDto.getBatchCode());
                inventoryDet.setJobStatus((byte)2);//在库
                inventoryDet.setIfStockLock((byte)0);
                inventoryDet.setInventoryStatusId(null);
                inventoryDet.setBarcodeStatus((byte)3);//在库
                inventoryDet.setCreateUserId(sysUser.getUserId());
                inventoryDet.setCreateTime(new Date());
                ////条码库存集合
                wmsInnerInventoryDets.add(inventoryDet);
            }
            else {
                //系统条码更新条码状态
                //条码类别处理
                SearchWmsInnerMaterialBarcode sWmsBarcode=new SearchWmsInnerMaterialBarcode();
                if(saveInnerJobOrderDto.getBarcodeType()==((byte)2)){
                    sWmsBarcode.setColorBoxCode(saveInnerJobOrderDto.getBatchCode());
                }
                else if(saveInnerJobOrderDto.getBarcodeType()==((byte)3)){
                    sWmsBarcode.setCartonCode(saveInnerJobOrderDto.getBatchCode());
                }
                else if(saveInnerJobOrderDto.getBarcodeType()==((byte)4)){
                    sWmsBarcode.setPalletCode(saveInnerJobOrderDto.getBatchCode());
                }
                List<WmsInnerMaterialBarcodeDto> materialDtoList=wmsInnerMaterialBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(sWmsBarcode));
                for (WmsInnerMaterialBarcodeDto materialBarcodeDto : materialDtoList) {
                    WmsInnerMaterialBarcode upBarcode=new WmsInnerMaterialBarcode();
                    upBarcode.setMaterialBarcodeId(materialBarcodeDto.getMaterialBarcodeId());
                    upBarcode.setBarcodeStatus((byte)5);
                    upBarcode.setModifiedUserId(sysUser.getUserId());
                    upBarcode.setModifiedTime(new Date());
                    wmsInnerMaterialBarcodeMapper.updateByPrimaryKeySelective(upBarcode);

                    //更新条码关系表条码状态
                    WmsInnerMaterialBarcodeReOrder newReOrder=new WmsInnerMaterialBarcodeReOrder();
                    newReOrder.setOrderTypeCode("IN-IWK");//类型 上架
                    newReOrder.setOrderCode(record.getJobOrderCode());
                    newReOrder.setOrderId(record.getJobOrderId());
                    //来料条码ID
                    newReOrder.setMaterialBarcodeId(materialBarcodeDto.getMaterialBarcodeId());
                    newReOrder.setScanStatus((byte) 3);
                    newReOrder.setOrgId(sysUser.getOrganizationId());
                    newReOrder.setCreateTime(new Date());
                    newReOrder.setCreateUserId(sysUser.getUserId());

                    //条码关系集合
                    materialBarcodeReOrderList.add(newReOrder);

                    WmsInnerInventoryDet inventoryDet=new WmsInnerInventoryDet();
                    inventoryDet.setStorageId(inStorageId);
                    inventoryDet.setMaterialId(materialBarcodeDto.getMaterialId());
                    inventoryDet.setBarcode(materialBarcodeDto.getBarcode());
                    inventoryDet.setMaterialQty(materialBarcodeDto.getMaterialQty());
                    inventoryDet.setProductionDate(materialBarcodeDto.getProductionTime());
                    inventoryDet.setProductionBatchCode(materialBarcodeDto.getBatchCode());
                    inventoryDet.setSupplierId(materialBarcodeDto.getSupplierId());
                    inventoryDet.setJobStatus((byte)2);//在库
                    inventoryDet.setIfStockLock((byte)0);
                    inventoryDet.setCartonCode(materialBarcodeDto.getCartonCode());
                    inventoryDet.setInventoryStatusId(null);
                    inventoryDet.setBarcodeStatus((byte)3);//在库
                    inventoryDet.setCreateUserId(sysUser.getUserId());
                    inventoryDet.setCreateTime(new Date());
                    //条码库存集合
                    wmsInnerInventoryDets.add(inventoryDet);
                }
            }
        }

        //新增明细
        for (List<WmsInnerJobOrderDet> value : map.values()) {
            for (WmsInnerJobOrderDet wmsInnerJobOrderDet : value) {
                wmsInnerJobOrderDet.setDistributionQty(wmsInnerJobOrderDet.getPlanQty());
                wmsInnerJobOrderDet.setActualQty(wmsInnerJobOrderDet.getPlanQty());
                wmsInnerJobOrderDet.setOrgId(sysUser.getOrganizationId());
                num += wmsInPutawayOrderDetMapper.insertSelective(wmsInnerJobOrderDet);

                WmsInnerJobOrderDetDto newDto=new WmsInnerJobOrderDetDto();
                BeanUtil.copyProperties(wmsInnerJobOrderDet,newDto);
                //增加库存
                WmsInnerInventoryUtil.updateInventory(record,newDto,sysUser);
            }
        }
        //新增条码关系
        if(materialBarcodeReOrderList.size()>0){
            wmsInnerMaterialBarcodeReOrderService.batchAdd(materialBarcodeReOrderList);
        }

        //新增条码明细
        if(wmsInnerInventoryDets.size()>0){
            WmsInnerInventoryUtil.updateInventoryDet(wmsInnerInventoryDets);
        }

        SearchWmsInnerJobOrder searchWmsInnerJobOrder=new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setJobOrderId(record.getJobOrderId());
        WmsInnerJobOrder wmsInnerJobOrder=this.findList(searchWmsInnerJobOrder).get(0);
        return wmsInnerJobOrder;
    }

    /**
     * PDA先作业后单 更新上架单完成状态
     * @param jobOrderId
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int updateInnerJobOrderFinish(Long jobOrderId) {
        int num=0;
        SysUser sysUser=currentUser();
        WmsInnerJobOrder wmsInnerJobOrder=wmsInPutawayOrderMapper.selectByPrimaryKey(jobOrderId);
        if(StringUtils.isEmpty(wmsInnerJobOrder)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"找不到相应的上架单信息");
        }
        if(wmsInnerJobOrder.getOrderStatus()==((byte)5)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"上架单已完成");
        }
        wmsInnerJobOrder.setOrderStatus((byte)5);
        //作业结束时间
        wmsInnerJobOrder.setWorkEndtTime(new Date());
        wmsInnerJobOrder.setModifiedUserId(sysUser.getUserId());
        wmsInnerJobOrder.setModifiedTime(new Date());
        num=wmsInPutawayOrderMapper.updateByPrimaryKeySelective(wmsInnerJobOrder);
        if(num<=0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012006.getCode(),"更新上架单完成失败");
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
        SysUser sysUser = currentUser();

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
        criteria1.andEqualTo("orgId",sysUser.getOrganizationId());
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
     * 库存
     *
     * @return
     */
    private int Inventory(WmsInnerJobOrder wmsInnerJobOrder,WmsInnerJobOrderDetDto oldDto, WmsInnerJobOrderDetDto newDto) {
        SysUser sysUser = currentUser();
        //WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(oldDto.getJobOrderId());
        // 减少分配库存 库位为移出库位 作业状态 jobStatus (1正常 2待出)为待出的库存
        Example example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("relevanceOrderCode", wmsInnerJobOrder.getJobOrderCode()).andEqualTo("materialId", oldDto.getMaterialId()).andEqualTo("warehouseId", wmsInnerJobOrder.getWarehouseId()).andEqualTo("storageId", oldDto.getOutStorageId());
        if (!StringUtils.isEmpty(oldDto.getBatchCode())) {
            criteria.andEqualTo("batchCode", oldDto.getBatchCode());
        }
        criteria.andEqualTo("jobOrderDetId", oldDto.getJobOrderDetId());
        criteria.andEqualTo("jobStatus", (byte) 2);
        criteria.andEqualTo("inventoryStatusId", oldDto.getInventoryStatusId());
        criteria.andEqualTo("orgId",sysUser.getOrganizationId());
        WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
        if (StringUtils.isEmpty(wmsInnerInventory)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        WmsInnerInventory wmsIn = new WmsInnerInventory();
        wmsIn.setInventoryId(wmsInnerInventory.getInventoryId());
        wmsIn.setPackingQty(wmsInnerInventory.getPackingQty().subtract(newDto.getActualQty()));
        int num = wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsIn);

        //添加扣减库存日志
        oldDto.setInStorageId(oldDto.getOutStorageId());
        InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrder,oldDto,wmsInnerInventory.getPackingQty(),newDto.getActualQty(),(byte)2,(byte)2);

        //增加移入库位库存 作业状态 jobStatus (1正常 2待出)为正常的库存
        example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("materialId", newDto.getMaterialId()).andEqualTo("warehouseId", wmsInnerJobOrder.getWarehouseId()).andEqualTo("storageId", newDto.getInStorageId());
        if (!StringUtils.isEmpty(newDto.getBatchCode())) {
            criteria1.andEqualTo("batchCode", newDto.getBatchCode());
        }
        criteria1.andEqualTo("jobStatus", (byte) 1);
        criteria1.andEqualTo("inventoryStatusId", newDto.getInventoryStatusId());
        criteria1.andEqualTo("stockLock", 0).andEqualTo("qcLock", 0).andEqualTo("lockStatus", 0);
        criteria1.andEqualTo("orgId",sysUser.getOrganizationId());
        WmsInnerInventory wmsInnerInventorys = wmsInnerInventoryMapper.selectOneByExample(example);
        BigDecimal qty = BigDecimal.ZERO;
        if (StringUtils.isEmpty(wmsInnerInventorys)) {
            //添加库存
            WmsInnerInventory inv = new WmsInnerInventory();
            //BeanUtil.copyProperties(wmsInnerInventory, inv);
            inv.setInventoryId(null);
            inv.setMaterialId(newDto.getMaterialId());
            inv.setWarehouseId(wmsInnerJobOrder.getWarehouseId());
            inv.setStorageId(newDto.getInStorageId());
            inv.setBatchCode(newDto.getBatchCode());
            inv.setJobStatus((byte) 1);
            inv.setInventoryStatusId(newDto.getInventoryStatusId());
            inv.setStockLock((byte)0);
            inv.setQcLock((byte)0);
            inv.setLockStatus((byte)0);
            inv.setOrgId(sysUser.getOrganizationId());
            inv.setPackingQty(newDto.getActualQty());
            inv.setCreateUserId(sysUser.getUserId());
            inv.setCreateTime(new Date());
            inv.setModifiedTime(new Date());
            inv.setModifiedUserId(sysUser.getUserId());
            inv.setRelevanceOrderCode(wmsInnerJobOrder.getJobOrderCode());
            //inv.setJobOrderDetId(newDto.getJobOrderDetId());

            num+=wmsInnerInventoryMapper.insertSelective(inv);
        } else {
            qty = wmsInnerInventorys.getPackingQty();
            if(StringUtils.isEmpty(wmsInnerInventorys.getPackingQty()) || wmsInnerInventorys.getPackingQty().compareTo(BigDecimal.ZERO)==0){
                wmsInnerInventorys.setReceivingDate(wmsInnerInventory.getReceivingDate());
            }
            //原库存
            wmsInnerInventorys.setPackingQty(wmsInnerInventorys.getPackingQty().add(newDto.getActualQty()));
            wmsInnerInventorys.setWorkOrderCode(wmsInnerInventory.getWorkOrderCode());
            wmsInnerInventorys.setRelevanceOrderCode(wmsInnerJobOrder.getJobOrderCode());
            wmsInnerInventorys.setModifiedTime(new Date());
            wmsInnerInventorys.setModifiedUserId(sysUser.getUserId());
            num+= wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
        }
        //记录库存日志
        InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrder,newDto,qty,newDto.getActualQty(),(byte)2,(byte)1);
        return num;
    }

    /**
     * 库容入库规则计算容量
     * @param materialId
     * @param storageId
     * @param qty
     * @return
     */
    @Override
    public Boolean storageCapacity(Long materialId,Long storageId,BigDecimal qty){
        SysUser sysUser = currentUser();
        Boolean isSuccess = true;
        //获取配置项
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("capacity");
        List<SysSpecItem> itemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        if(itemList.size()>0){

            //获取库位
            SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
            searchBaseStorage.setStorageId(storageId);
            List<BaseStorage> baseStorages = baseFeignApi.findList(searchBaseStorage).getData();
            if(baseStorages.size()<1){
                throw new BizErrorException("获取库位信息失败");
            }
            if(StringUtils.isEmpty(baseStorages.get(0).getMaterialStoreType())){
                throw new BizErrorException("库位未维护生产存储类型");
            }

            //库容入库规则判断是否足够存放
            if(itemList.get(0).getParaValue().equals("base_storage_capacity")){
                //获取库容
                SearchBaseStorageCapacity searchBaseStorageCapacity = new SearchBaseStorageCapacity();
                searchBaseStorageCapacity.setMaterialId(materialId);
                List<BaseStorageCapacity> baseStorageCapacities = baseFeignApi.findList(searchBaseStorageCapacity).getData();
                if(baseStorageCapacities.size()<1){
                    throw new BizErrorException("物料未维护库容信息");
                }
                Example example = new Example(WmsInnerInventory.class);
                example.createCriteria().andEqualTo("storageId",storageId).andEqualTo("materialId",materialId).andEqualTo("orgId",sysUser.getOrganizationId());
                List<WmsInnerInventory> inventories = wmsInnerInventoryMapper.selectByExample(example);
                BigDecimal totalQty = inventories.stream()
                        .map(WmsInnerInventory::getPackingQty)
                        .reduce(BigDecimal.ZERO,BigDecimal::add);
                switch (baseStorages.get(0).getMaterialStoreType()) {
                    case 1:
                        if (StringUtils.isEmpty(baseStorageCapacities.get(0).getTypeACapacity())) {
                            throw new BizErrorException("未维护A类容量");
                        }
                        totalQty = baseStorageCapacities.get(0).getTypeACapacity().subtract(totalQty);
                        if(totalQty.compareTo(qty)==-1){
                            isSuccess = false;
                        }
                        break;
                    case 2:
                        if (StringUtils.isEmpty(baseStorageCapacities.get(0).getTypeBCapacity())) {
                            throw new BizErrorException("未维护B类容量");
                        }
                        totalQty = baseStorageCapacities.get(0).getTypeBCapacity().subtract(totalQty);
                        if(totalQty.compareTo(qty)==-1){
                            isSuccess = false;
                        }
                        break;
                    case 3:
                        if (StringUtils.isEmpty(baseStorageCapacities.get(0).getTypeCCapacity())) {
                            throw new BizErrorException("未维护C类容量");
                        }
                        totalQty = baseStorageCapacities.get(0).getTypeCCapacity().subtract(totalQty);
                        if(totalQty.compareTo(qty)==-1){
                            isSuccess = false;
                        }
                        break;
                    case 4:
                        if (StringUtils.isEmpty(baseStorageCapacities.get(0).getTypeDCapacity())) {
                            throw new BizErrorException("未维护D类容量");
                        }
                        totalQty = baseStorageCapacities.get(0).getTypeDCapacity().subtract(totalQty);
                        if(totalQty.compareTo(qty)==-1){
                            isSuccess = false;
                        }
                        break;
                }
            }
        }
        return isSuccess;
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
                wmsInnerJobOrder.setJobOrderType((byte)1);
                wmsInnerJobOrder.setWarehouseId(code);
                wmsInnerJobOrder.setStatus((byte)1);
                wmsInnerJobOrder.setOrgId(currentUser.getOrganizationId());
                wmsInnerJobOrder.setCreateUserId(currentUser.getUserId());
                wmsInnerJobOrder.setCreateTime(new Date());
                wmsInPutawayOrderMapper.insertUseGeneratedKeys(wmsInnerJobOrder);
                jobOrderId=wmsInnerJobOrder.getJobOrderId();
            }
            //新增明细 WmsInnerJobOrderDet
            if(StringUtils.isNotEmpty(jobOrderId)) {
                List<WmsInnerJobOrderDet> wmsInnerJobOrderDetList=new ArrayList<>();
                for (WmsInnerJobOrderImport item : wmsInnerJobOrderImport1) {

                    WmsInnerJobOrderDet wmsInnerJobOrderDetNew=new WmsInnerJobOrderDet();
                    wmsInnerJobOrderDetNew.setJobOrderId(jobOrderId);
                    wmsInnerJobOrderDetNew.setMaterialId(item.getMaterialId());
                    wmsInnerJobOrderDetNew.setPlanQty(new BigDecimal(item.getPlanQty()));
                    wmsInnerJobOrderDetNew.setOrgId(currentUser.getOrganizationId());
                    wmsInnerJobOrderDetNew.setCreateUserId(currentUser.getUserId());
                    wmsInnerJobOrderDetNew.setCreateTime(new Date());
                    wmsInnerJobOrderDetList.add(wmsInnerJobOrderDetNew);
                }

                if(wmsInnerJobOrderDetList.size()>0){
                    success += wmsInPutawayOrderDetMapper.insertList(wmsInnerJobOrderDetList);
                }
            }
        }
        resultMap.put("操作成功总数", success);
        resultMap.put("操作失败行", fail);
        return resultMap;
    }
    /**
     * 判断条码类型
     * @return 返回条码类型及数量
     */
    public Map<String, Object> checkBarcodeType(String barCode){
        Map<String, Object> returnMap=new HashMap<>();
        SearchWmsInnerMaterialBarcode swmsInnerMaterialBarcode=new SearchWmsInnerMaterialBarcode();
        swmsInnerMaterialBarcode.setBarcode(barCode);
        List<WmsInnerMaterialBarcodeDto> materialBarcodeList=wmsInnerMaterialBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(swmsInnerMaterialBarcode));
        if(materialBarcodeList.size()>0){
            //SN
            returnMap.put("barcodeType","1");
            returnMap.put("materialBarcodeId",materialBarcodeList.get(0).getMaterialBarcodeId());
            returnMap.put("qty",materialBarcodeList.get(0).getMaterialQty());
            return returnMap;
        }
        else {
            //彩盒
            swmsInnerMaterialBarcode.setBarcode(null);
            swmsInnerMaterialBarcode.setColorBoxCode(barCode);
            materialBarcodeList=wmsInnerMaterialBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(swmsInnerMaterialBarcode));
            if(materialBarcodeList.size()>0){
                Optional<WmsInnerMaterialBarcodeDto> barcodeOptional = materialBarcodeList.stream()
                        .filter(i -> i.getBarcodeType()==((byte)2))
                        .findFirst();
                if (barcodeOptional.isPresent()) {
                    returnMap.put("materialBarcodeId",barcodeOptional.get().getMaterialBarcodeId());
                }
                else {
                    throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"来料条码表找不到此箱号信息");
                }
                returnMap.put("barcodeType","2");
                returnMap.put("qty",materialBarcodeList.size()-1);
                return returnMap;
            }
            else {
                //箱号
                swmsInnerMaterialBarcode.setBarcode(null);
                swmsInnerMaterialBarcode.setColorBoxCode(null);
                swmsInnerMaterialBarcode.setCartonCode(barCode);
                materialBarcodeList = wmsInnerMaterialBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(swmsInnerMaterialBarcode));
                if(materialBarcodeList.size()>0){
                    Optional<WmsInnerMaterialBarcodeDto> barcodeOptional = materialBarcodeList.stream()
                            .filter(i -> i.getBarcodeType()==((byte)3))
                            .findFirst();
                    if (barcodeOptional.isPresent()) {
                        returnMap.put("materialBarcodeId",barcodeOptional.get().getMaterialBarcodeId());
                    }
                    else {
                        throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"来料条码表找不到此箱号信息");
                    }
                    returnMap.put("barcodeType","3");
                    returnMap.put("qty",materialBarcodeList.size()-1);
                    return returnMap;
                }
                else {
                    //栈板
                    swmsInnerMaterialBarcode.setBarcode(null);
                    swmsInnerMaterialBarcode.setColorBoxCode(null);
                    swmsInnerMaterialBarcode.setCartonCode(null);
                    swmsInnerMaterialBarcode.setPalletCode(barCode);
                    materialBarcodeList = wmsInnerMaterialBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(swmsInnerMaterialBarcode));
                    if(materialBarcodeList.size()>0){
                        Optional<WmsInnerMaterialBarcodeDto> barcodeOptional = materialBarcodeList.stream()
                                .filter(i -> i.getBarcodeType()==((byte)4))
                                .findFirst();
                        if (barcodeOptional.isPresent()) {
                            returnMap.put("materialBarcodeId",barcodeOptional.get().getMaterialBarcodeId());
                        }
                        else {
                            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"来料条码表找不到此箱号信息");
                        }
                        returnMap.put("barcodeType","4");
                        returnMap.put("qty",materialBarcodeList.size()-1);
                        return returnMap;
                    }
                    else {
                        returnMap.put("materialBarcodeId","");
                        returnMap.put("barcodeType","");
                        returnMap.put("qty",0);
                    }
                }
            }
        }

        return returnMap;
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
