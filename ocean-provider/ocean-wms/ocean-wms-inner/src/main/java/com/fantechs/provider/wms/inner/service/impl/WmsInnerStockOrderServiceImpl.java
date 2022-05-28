package com.fantechs.provider.wms.inner.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.*;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.general.entity.wms.inner.*;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerStockOrderDetBarcode;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.guest.eng.EngFeignApi;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.wms.inner.mapper.*;
import com.fantechs.provider.wms.inner.service.WmsInnerInventoryDetService;
import com.fantechs.provider.wms.inner.service.WmsInnerMaterialBarcodeReOrderService;
import com.fantechs.provider.wms.inner.service.WmsInnerStockOrderDetBarcodeService;
import com.fantechs.provider.wms.inner.service.WmsInnerStockOrderService;
import com.fantechs.provider.wms.inner.util.InBarcodeUtil;
import com.fantechs.provider.wms.inner.util.InventoryLogUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by Mr.Lei on 2021/05/27.
 */
@Service
public class WmsInnerStockOrderServiceImpl extends BaseService<WmsInnerStockOrder> implements WmsInnerStockOrderService {

    @Resource
    private WmsInnerStockOrderMapper wmsInventoryVerificationMapper;
    @Resource
    private WmsInnerStockOrderDetMapper wmsInventoryVerificationDetMapper;
    @Resource
    private WmsInnerInventoryMapper wmsInnerInventoryMapper;
    @Resource
    private WmsInnerInventoryDetMapper wmsInnerInventoryDetMapper;
    @Resource
    private EngFeignApi engFeignApi;
    @Resource
    private AuthFeignApi securityFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private  WmsInnerInventoryDetService wmsInnerInventoryDetService;
    @Resource
    private WmsInnerStockOrderDetBarcodeMapper wmsInnerStockOrderDetBarcodeMapper;
    @Resource
    private WmsInnerStockOrderDetBarcodeService wmsInnerStockOrderDetBarcodeService;
    @Resource
    private WmsInnerMaterialBarcodeMapper wmsInnerMaterialBarcodeMapper;
    @Resource
    private WmsInnerMaterialBarcodeReOrderService wmsInnerMaterialBarcodeReOrderService;

    @Override
    public List<WmsInnerStockOrderDto> findList(Map<String, Object> map) {
        SysUser sysUser = currentUser();
        map.put("orgId",sysUser.getOrganizationId());
        return wmsInventoryVerificationMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(WmsInnerStockOrder record) {
        SysUser sysUser = currentUser();
        if(StringUtils.isEmpty(record.getWarehouseId())){
            throw new BizErrorException("仓库不能为空");
        }
        record.setPlanStockOrderCode(CodeUtils.getId("INPD-"));
        record.setProjectType((byte)1);
        record.setOrderStatus((byte)1);
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrganizationId(sysUser.getOrganizationId());
        int num = wmsInventoryVerificationMapper.insertUseGeneratedKeys(record);
        //库位盘点/全盘
        if(record.getStockType()==(byte)2 || record.getStockType()==(byte)3){
            if(record.getStockType()==(byte)2 && (StringUtils.isNotEmpty(record.getMaxStorageCount()) && record.getMaxStorageCount()< record.getStorageList().size())){
                throw new BizErrorException("所选库位数不能大于最大库位数");
            }
            //盘点类型：按库位盘点
            //查询库位下的所以库存货品
            if(record.getStockType()==(byte)2 && StringUtils.isEmpty(record.getStorageList())){
                throw new BizErrorException("库位不能为空");
            }
            List<WmsInnerStockOrderDet> wmsInventoryVerificationDets = this.findInvGoods(record.getStockType(),record.getStockOrderId(), record.getStorageList(),record.getWarehouseId(),sysUser);
            int res = wmsInventoryVerificationDetMapper.insertList(wmsInventoryVerificationDets);
            if(res<0){
                throw new BizErrorException("新增盘点单失败");
            }
        }else if(record.getStockType()==(byte)1){
            //按物料
            List<WmsInnerStockOrderDet> list = this.MaterialfindInvGoofs(record.getStockOrderId(),record.getMaterialList(),record.getWarehouseId(),sysUser);
            int res = wmsInventoryVerificationDetMapper.insertList(list);
            if(res<0){
                throw new BizErrorException("新增盘点单失败");
            }
        }
        return num;
    }

    /**
     * 修改或盘点登记
     * @param
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(WmsInnerStockOrder entity) {
        SysUser sysUser = currentUser();
        //type类型：(1-修改 2-盘点登记)
        if(entity.getType()==(byte)2 && (entity.getOrderStatus()==1 || entity.getOrderStatus()>3)){
            throw new BizErrorException(entity.getOrderStatus()==1?"单据未激活，无法登记":"无法登记");
        }
        if(entity.getType()==(byte) 2){
            entity.setOrderStatus((byte)3);
        }
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(sysUser.getUserId());
//        //删除原有数据
//        Example example = new Example(WmsInnerStockOrderDet.class);
//        example.createCriteria().andEqualTo("stockOrderId",entity.getStockOrderId()).andNotEqualTo("option1","1");
//        wmsInventoryVerificationDetMapper.deleteByExample(example);
        //盘点登记 type=2
        if(entity.getType()==(byte)2){
            for (WmsInnerStockOrderDet inventoryVerificationDet : entity.getWmsInnerStockOrderDets()) {
                if(StringUtils.isEmpty(inventoryVerificationDet.getStorageId())){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003,"初盘货品库位不能为空");
                }
                inventoryVerificationDet.setModifiedUserId(sysUser.getUserId());
                inventoryVerificationDet.setModifiedTime(new Date());
                inventoryVerificationDet.setOrgId(sysUser.getOrganizationId());

                inventoryVerificationDet.setStockUserId(sysUser.getUserId());
                inventoryVerificationDet.setIfRegister((byte)1);

                if(StringUtils.isEmpty(inventoryVerificationDet.getStockQty())){
                    inventoryVerificationDet.setStockQty(BigDecimal.ZERO);
                }
                inventoryVerificationDet.setVarianceQty(inventoryVerificationDet.getStockQty().subtract(inventoryVerificationDet.getOriginalQty()));
                if(StringUtils.isEmpty(inventoryVerificationDet.getStockOrderDetId())){
                    inventoryVerificationDet.setStockOrderId(entity.getStockOrderId());
                    inventoryVerificationDet.setCreateUserId(sysUser.getUserId());
                    inventoryVerificationDet.setCreateTime(new Date());
                    inventoryVerificationDet.setOption1("new");//option1 标志是盘点增补的明细行
                    wmsInventoryVerificationDetMapper.insertSelective(inventoryVerificationDet);
                }else {
                    wmsInventoryVerificationDetMapper.updateByPrimaryKeySelective(inventoryVerificationDet);
                }
            }
            //int num = wmsInventoryVerificationDetMapper.insertList(entity.getWmsInnerStockOrderDets());
        }else if(entity.getType()==(byte) 1){
            if(entity.getOrderStatus()!=1){
                throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"盘点单不是打开状态 不能修改");
            }
            //删除原来明细
            Example example = new Example(WmsInnerStockOrderDet.class);
            example.createCriteria().andEqualTo("stockOrderId",entity.getStockOrderId());
            wmsInventoryVerificationDetMapper.deleteByExample(example);
            //库位盘点/全盘
            if(entity.getStockType()==(byte)1 || entity.getStockType()==(byte)3){
                if(entity.getStockType()==(byte)1 && (StringUtils.isNotEmpty(entity.getMaxStorageCount()) && entity.getMaxStorageCount()<entity.getStorageList().size())){
                    throw new BizErrorException("所选库位数不能大于最大库位数");
                }
                //盘点类型：库位盘
                //查询库位下的所以库存货品
                if(entity.getStockType()==(byte)2 && StringUtils.isEmpty(entity)){
                    throw new BizErrorException("库位不能未空");
                }
                List<WmsInnerStockOrderDet> wmsInventoryVerificationDets = this.findInvGoods(entity.getStockType(),entity.getStockOrderId(), entity.getStorageList(),entity.getWarehouseId(),sysUser);
                int res = wmsInventoryVerificationDetMapper.insertList(wmsInventoryVerificationDets);
                if(res<0){
                    throw new BizErrorException("新增盘点单失败");
                }
            }else if(entity.getStockType()==(byte)1){
                List<WmsInnerStockOrderDet> list = this.MaterialfindInvGoofs(entity.getStockOrderId(),entity.getMaterialList(),entity.getWarehouseId(),sysUser);
                int res = wmsInventoryVerificationDetMapper.insertList(list);
                if(res<0){
                    throw new BizErrorException("新增盘点单失败");
                }
            }
        }
        int num=wmsInventoryVerificationMapper.updateByPrimaryKeySelective(entity);
        return num;
    }

    /**
     * 按货品查询库位
     * @param id
     * @param materialList
     * @return
     */
    private List<WmsInnerStockOrderDet> MaterialfindInvGoofs(Long id,List<Long> materialList,Long warehouseId,SysUser sysUser){
        List<WmsInnerStockOrderDet> list = new ArrayList<>();
        for (Long materialId : materialList) {
            //获取库位名称
            Example example = new Example(WmsInnerInventory.class);
            //盘点锁 0 否 1 是
            example.createCriteria().andEqualTo("warehouseId",warehouseId)
                    .andEqualTo("materialId",materialId)
                    .andEqualTo("stockLock",0)
                    .andGreaterThan("packingQty",0)
                    .andEqualTo("jobStatus",1)
                    .andEqualTo("orgId",sysUser.getOrganizationId())
                    .andEqualTo("lockStatus", 0);
            //获取货品库存
            List<WmsInnerInventory> wmsInnerInventories = wmsInnerInventoryMapper.selectByExample(example);
            wmsInnerInventories = this.filtration(wmsInnerInventories);
            if(wmsInnerInventories.size()>0) {
                for (WmsInnerInventory wmsInnerInventory : wmsInnerInventories) {
                    WmsInnerStockOrderDet wmsInventoryVerificationDet = new WmsInnerStockOrderDet();
                    wmsInventoryVerificationDet.setStockOrderId(id);
                    wmsInventoryVerificationDet.setMaterialId(wmsInnerInventory.getMaterialId());
                    wmsInventoryVerificationDet.setStorageId(wmsInnerInventory.getStorageId());
                    wmsInventoryVerificationDet.setIfRegister((byte) 0);
                    wmsInventoryVerificationDet.setOriginalQty(wmsInnerInventory.getPackingQty());
                    wmsInventoryVerificationDet.setCreateTime(new Date());
                    wmsInventoryVerificationDet.setCreateUserId(sysUser.getUserId());
                    wmsInventoryVerificationDet.setModifiedTime(new Date());
                    wmsInventoryVerificationDet.setModifiedUserId(sysUser.getUserId());
                    wmsInventoryVerificationDet.setOrgId(sysUser.getOrganizationId());
                    wmsInventoryVerificationDet.setBatchCode(wmsInnerInventory.getBatchCode());
                    wmsInventoryVerificationDet.setInventoryStatusId(wmsInnerInventory.getInventoryStatusId());
                    wmsInventoryVerificationDet.setSupplierId(wmsInnerInventory.getSupplierId());
                    wmsInventoryVerificationDet.setProductionTime(wmsInnerInventory.getProductionDate());
                    list.add(wmsInventoryVerificationDet);
                }
            }else {
                //允许无库存盘点
                WmsInnerStockOrderDet newStockOrderDet = new WmsInnerStockOrderDet();
                newStockOrderDet.setStockOrderId(id);
                newStockOrderDet.setMaterialId(materialId);
                newStockOrderDet.setStorageId(null);
                newStockOrderDet.setIfRegister((byte) 0);
                newStockOrderDet.setOriginalQty(new BigDecimal(0));
                newStockOrderDet.setCreateTime(new Date());
                newStockOrderDet.setCreateUserId(sysUser.getUserId());
                newStockOrderDet.setModifiedTime(new Date());
                newStockOrderDet.setModifiedUserId(sysUser.getUserId());
                newStockOrderDet.setOrgId(sysUser.getOrganizationId());
                newStockOrderDet.setBatchCode(null);
                newStockOrderDet.setInventoryStatusId(null);
                newStockOrderDet.setSupplierId(null);
                list.add(newStockOrderDet);
            }

        }

        return list;
    }

    private List<WmsInnerInventory> filtration(List<WmsInnerInventory> wmsInnerInventories){
        //只返回存货库位的库存信息
        List<WmsInnerInventory> list = new ArrayList<>();
        Map<Long,Byte> map = new HashMap<>();
        for (WmsInnerInventory wmsInnerInventory : wmsInnerInventories) {
            if(map.containsKey(wmsInnerInventory.getStorageId())){
                if(map.get(wmsInnerInventory.getStorageId())==1){
                    list.add(wmsInnerInventory);
                }
            }else{
                //查询库位类型
                SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
                searchBaseStorage.setStorageId(wmsInnerInventory.getStorageId());
                List<BaseStorage> storages = baseFeignApi.findList(searchBaseStorage).getData();
                if(StringUtils.isEmpty(storages)){
                    throw new BizErrorException("获取库位信息失败");
                }
                if(storages.get(0).getStorageType()==1){
                    list.add(wmsInnerInventory);
                    map.put(wmsInnerInventory.getStorageId(),(byte)1);
                }
            }
        }
        return list;
    }

    /**
     * 按库位获取库存货品
     * @param id
     * @param storageList
     * @return
     */
    private List<WmsInnerStockOrderDet> findInvGoods(Byte type, Long id, List<Long> storageList,Long warehouseId,SysUser sysUser){
        List<WmsInnerStockOrderDet> list = new ArrayList<>();
        //查询库位下所有库存货品
        //库位盘点
        if(type==2){
            for (Long storageId : storageList) {
                //获取库位名称
                Example example = new Example(WmsInnerInventory.class);
                //盘点锁 0 否 1 是
                //jobStatus 作业状态(1正常 2待出)
                //lockStatus 锁定状态(0-否 1-是)
                //stockLock 盘点锁(0-否 1-是)
                example.createCriteria().andEqualTo("warehouseId",warehouseId)
                        .andEqualTo("storageId",storageId)
                        .andEqualTo("stockLock",0)
                        .andGreaterThan("packingQty",0)
                        .andEqualTo("jobStatus",1)
                        .andEqualTo("orgId",sysUser.getOrganizationId())
                        .andEqualTo("lockStatus", 0);
                //获取库位库存
                List<WmsInnerInventory> wmsInnerInventories = wmsInnerInventoryMapper.selectByExample(example);
                wmsInnerInventories = this.filtration(wmsInnerInventories);
                for (WmsInnerInventory wmsInnerInventory : wmsInnerInventories) {
                    WmsInnerStockOrderDet wmsInventoryVerificationDet = new WmsInnerStockOrderDet();
                    wmsInventoryVerificationDet.setStockOrderId(id);
                    wmsInventoryVerificationDet.setMaterialId(wmsInnerInventory.getMaterialId());
                    wmsInventoryVerificationDet.setStorageId(storageId);
                    wmsInventoryVerificationDet.setIfRegister((byte) 0);
                    wmsInventoryVerificationDet.setOriginalQty(wmsInnerInventory.getPackingQty());
                    wmsInventoryVerificationDet.setCreateTime(new Date());
                    wmsInventoryVerificationDet.setCreateUserId(sysUser.getUserId());
                    wmsInventoryVerificationDet.setModifiedTime(new Date());
                    wmsInventoryVerificationDet.setModifiedUserId(sysUser.getUserId());
                    wmsInventoryVerificationDet.setOrgId(sysUser.getOrganizationId());

                    wmsInventoryVerificationDet.setBatchCode(wmsInnerInventory.getBatchCode());
                    wmsInventoryVerificationDet.setInventoryStatusId(wmsInnerInventory.getInventoryStatusId());
                    wmsInventoryVerificationDet.setSupplierId(wmsInnerInventory.getSupplierId());
                    wmsInventoryVerificationDet.setProductionTime(wmsInnerInventory.getProductionDate());
                    list.add(wmsInventoryVerificationDet);
                }
            }
        }else if(type==3){
            //全盘
            Example example = new Example(WmsInnerInventory.class);
            //盘点锁 0 否 1 是
            example.createCriteria().andEqualTo("warehouseId",warehouseId).andEqualTo("stockLock",0)
                    .andGreaterThan("packingQty",0).andEqualTo("jobStatus",1)
                    .andEqualTo("lockStatus", 0).andGreaterThan("packingQty",0);
            //获取库位库存
            List<WmsInnerInventory> wmsInnerInventories = wmsInnerInventoryMapper.selectByExample(example);
            wmsInnerInventories = this.filtration(wmsInnerInventories);
            for (WmsInnerInventory wmsInnerInventory : wmsInnerInventories) {
                WmsInnerStockOrderDet wmsInventoryVerificationDet = new WmsInnerStockOrderDet();
                wmsInventoryVerificationDet.setStockOrderId(id);
                wmsInventoryVerificationDet.setMaterialId(wmsInnerInventory.getMaterialId());
                //获取库位id
                wmsInventoryVerificationDet.setStorageId(wmsInnerInventory.getStorageId());
                wmsInventoryVerificationDet.setIfRegister((byte) 0);
                wmsInventoryVerificationDet.setOriginalQty(wmsInnerInventory.getPackingQty());
                wmsInventoryVerificationDet.setCreateTime(new Date());
                wmsInventoryVerificationDet.setCreateUserId(sysUser.getUserId());
                wmsInventoryVerificationDet.setModifiedTime(new Date());
                wmsInventoryVerificationDet.setModifiedUserId(sysUser.getUserId());
                wmsInventoryVerificationDet.setBatchCode(wmsInnerInventory.getBatchCode());
                wmsInventoryVerificationDet.setInventoryStatusId(wmsInnerInventory.getInventoryStatusId());
                wmsInventoryVerificationDet.setSupplierId(wmsInnerInventory.getSupplierId());
                wmsInventoryVerificationDet.setProductionTime(wmsInnerInventory.getProductionDate());
                list.add(wmsInventoryVerificationDet);
            }
        }
        if(list.size()<1){
            throw new BizErrorException("该条件暂无可盘点库存");
        }
        return list;
    }

    /**
     * 盘点激活 作废
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor=RuntimeException.class)
    public int activation(String ids,Integer btnType){
        SysUser sysUser=currentUser();
        String[] arrId = ids.split(",");
        int num = 0;
        for (String id : arrId) {
            WmsInnerStockOrder wmsInventoryVerification = wmsInventoryVerificationMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(wmsInventoryVerification)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            Example example = new Example(WmsInnerStockOrderDet.class);
            example.createCriteria().andEqualTo("stockOrderId",wmsInventoryVerification.getStockOrderId());
            List<WmsInnerStockOrderDet> list = wmsInventoryVerificationDetMapper.selectByExample(example);
            List<Long> materialList=new ArrayList<>();
            List<Long> storageList=new ArrayList<>();
            for (WmsInnerStockOrderDet stockOrderDet : list) {
                if(!materialList.contains(stockOrderDet.getMaterialId()))
                    materialList.add(stockOrderDet.getMaterialId());
                if(!storageList.contains(stockOrderDet.getStorageId()))
                    storageList.add(stockOrderDet.getStorageId());
            }

            //库位盘点将盘点单的所有库位下库存更改上锁状态及基础信息库位上锁 货品盘点将
            //btnType 按钮类型 1-激活 2作废
            if(btnType ==1){
                if(wmsInventoryVerification.getOrderStatus()>1){
                    throw new BizErrorException("盘点单已激活 请勿重复操作");
                }

                //激活重新生成盘点明细 开始
                //删除原来明细
                Example exampleDet = new Example(WmsInnerStockOrderDet.class);
                exampleDet.createCriteria().andEqualTo("stockOrderId",wmsInventoryVerification.getStockOrderId());
                wmsInventoryVerificationDetMapper.deleteByExample(exampleDet);

                List<WmsInnerStockOrderDet> listNew=new ArrayList<>();
                if(wmsInventoryVerification.getStockType()==(byte)1) {
                    listNew = this.MaterialfindInvGoofs(wmsInventoryVerification.getStockOrderId(), materialList, wmsInventoryVerification.getWarehouseId(), sysUser);
                    int res = wmsInventoryVerificationDetMapper.insertList(listNew);
                    if (res < 0) {
                        throw new BizErrorException("新增盘点单失败");
                    }
                }
                else{
                    listNew = this.findInvGoods(wmsInventoryVerification.getStockType(),
                            wmsInventoryVerification.getStockOrderId(),
                            storageList,wmsInventoryVerification.getWarehouseId(),sysUser);
                    int res = wmsInventoryVerificationDetMapper.insertList(listNew);
                    if(res<0){
                        throw new BizErrorException("新增盘点单失败");
                    }
                }
                //激活重新生成盘点明细 结束

                //打开
                num += this.unlockOrLock((byte) 2,listNew,wmsInventoryVerification);
                wmsInventoryVerification.setOrderStatus((byte)2);
                //对应条码新增到盘点条码明细 锁定
                num+=addStockOrderDetBarcode((byte)1,(byte)1,listNew,sysUser);

            }else if(btnType ==2){
                if(wmsInventoryVerification.getOrderStatus()>=5){
                    throw new BizErrorException("单据已完成,无法作废");
                }
                //作废
                num += this.unlockOrLock((byte) 1,list,wmsInventoryVerification);
                //对应条码解锁
                num+=addStockOrderDetBarcode((byte)2,(byte)1,list,sysUser);

                wmsInventoryVerification.setOrderStatus((byte)6);
            }
            num+=wmsInventoryVerificationMapper.updateByPrimaryKeySelective(wmsInventoryVerification);
        }
        return num;
    }

    /**
     * Web端盘点确认
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int ascertained(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        int num = 0;
        for (String id : arrId) {
            WmsInnerStockOrder wmsInventoryVerification = wmsInventoryVerificationMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(wmsInventoryVerification)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            if(wmsInventoryVerification.getOrderStatus()!=3 && wmsInventoryVerification.getProjectType()==(byte)1){
                throw new BizErrorException("盘点单未登记,无法确认");
            }
            Example example = new Example(WmsInnerStockOrderDet.class);
            example.createCriteria().andEqualTo("stockOrderId", wmsInventoryVerification.getStockOrderId());
            List<WmsInnerStockOrderDet> list = wmsInventoryVerificationDetMapper.selectByExample(example);
            List<WmsInnerStockOrderDet> wmsInventoryVerificationDets = new ArrayList<>();

            //有差异生成复盘单
            if(wmsInventoryVerification.getProjectType()==(byte)1){
                //判断是否有差异记录
                if(list.stream().filter(li->StringUtils.isNotEmpty(li.getVarianceQty())&&(li.getVarianceQty().compareTo(BigDecimal.ZERO)==1 || li.getVarianceQty().compareTo(BigDecimal.ZERO)==-1)).collect(Collectors.toList()).size()>0){
                    //新增复盘盘点单
                    WmsInnerStockOrder ws = new WmsInnerStockOrder();
                    BeanUtil.copyProperties(wmsInventoryVerification,ws);
                    ws.setStockOrderId(null);
                    ws.setProjectType((byte)2);
                    ws.setRelatedOrderCode(wmsInventoryVerification.getPlanStockOrderCode());
                    ws.setPlanStockOrderCode(CodeUtils.getId("INPD-"));
                    ws.setCreateUserId(sysUser.getUserId());
                    ws.setCreateTime(new Date());
                    ws.setModifiedTime(new Date());
                    ws.setModifiedUserId(sysUser.getUserId());
                    ws.setOrderStatus((byte)2);
                    num += wmsInventoryVerificationMapper.insertUseGeneratedKeys(ws);

                    for (WmsInnerStockOrderDet wmsInventoryVerificationDet : list) {
                        if(!StringUtils.isEmpty(wmsInventoryVerificationDet.getVarianceQty())&&(wmsInventoryVerificationDet.getVarianceQty().compareTo(BigDecimal.ZERO)==1 || wmsInventoryVerificationDet.getVarianceQty().compareTo(BigDecimal.ZERO)==-1)){
                            WmsInnerStockOrderDet det = new WmsInnerStockOrderDet();
                            BeanUtils.copyProperties(wmsInventoryVerificationDet,det);
                            det.setStockOrderId(ws.getStockOrderId());
                            det.setSourceDetId(wmsInventoryVerificationDet.getStockOrderDetId());
                            det.setStorageId(wmsInventoryVerificationDet.getStorageId());
                            det.setMaterialId(wmsInventoryVerificationDet.getMaterialId());
                            det.setOriginalQty(wmsInventoryVerificationDet.getOriginalQty());
                            det.setStockQty(null);
                            det.setVarianceQty(null);
                            det.setLastTimeVarianceQty(wmsInventoryVerificationDet.getVarianceQty());
                            det.setIfRegister((byte)0);
                            det.setCreateUserId(sysUser.getUserId());
                            det.setCreateTime(new Date());
                            det.setModifiedTime(new Date());
                            det.setModifiedUserId(sysUser.getUserId());
                            wmsInventoryVerificationDets.add(det);
                        }
                    }
                    num+=wmsInventoryVerificationDetMapper.insertList(wmsInventoryVerificationDets);

                    //对应条码新增到盘点条码明细 锁定
                    num+=addStockOrderDetBarcode((byte)1,(byte)2,wmsInventoryVerificationDets,sysUser);
                }
            }
            wmsInventoryVerificationDets.clear();
            for (WmsInnerStockOrderDet wmsInventoryVerificationDet : list) {
                //盘点
                if(wmsInventoryVerification.getProjectType()==(byte)1){
                    //是否存在差异量 不存在则解锁库存盘点锁 存在则进行复盘
                    if(!StringUtils.isEmpty(wmsInventoryVerificationDet.getVarianceQty())&&wmsInventoryVerificationDet.getVarianceQty().compareTo(BigDecimal.ZERO)==0){
                        wmsInventoryVerificationDets.add(wmsInventoryVerificationDet);
                    }
                }
            }

            if(wmsInventoryVerificationDets.size()>0){
                //无差异数量 解锁及复盘库存
                num+=this.unlockOrLock((byte)1,wmsInventoryVerificationDets,wmsInventoryVerification);

                //无差异数量 解锁库存明细条码
                num+=addStockOrderDetBarcode((byte)2,(byte)1,wmsInventoryVerificationDets,sysUser);
            }

            //单据状态(1-打开 2-待作业 3-作业中 4-待处理 5-完成 6-作废)
            //&& wmsInventoryVerification.getStockMode()==(byte)2
            if(wmsInventoryVerification.getProjectType()==2){
                // 复盘单确认后 单据状态为 4-待处理
                wmsInventoryVerification.setOrderStatus((byte)4);
            }else if(wmsInventoryVerification.getProjectType()==(byte)1){
                //更改盘点状态（已完成）
                wmsInventoryVerification.setOrderStatus((byte)5);
            }

            wmsInventoryVerification.setModifiedTime(new Date());
            wmsInventoryVerification.setModifiedUserId(sysUser.getUserId());
            num +=wmsInventoryVerificationMapper.updateByPrimaryKeySelective(wmsInventoryVerification);

        }
        return num;
    }

    /**
     * 自动适配
     * @param
     * @return
     */
    @Override
    public int autoAdapter(Long stockOrderId) {
        int num=1;
        SysUser sysUser=currentUser();
        WmsInnerStockOrder wmsInnerStockOrder=wmsInventoryVerificationMapper.selectByPrimaryKey(stockOrderId);
        if(StringUtils.isEmpty(wmsInnerStockOrder)){
            throw  new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"传入的盘点单ID无效");
        }
        if(wmsInnerStockOrder.getOrderStatus()>=4){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"盘点单状态错误");
        }
        Example example = new Example(WmsInnerStockOrderDet.class);
        example.createCriteria().andEqualTo("stockOrderId", stockOrderId);
        List<WmsInnerStockOrderDet> detlist = wmsInventoryVerificationDetMapper.selectByExample(example);
        List<WmsInnerStockOrderDet> notQtyList = detlist.stream().filter(u -> ((StringUtils.isEmpty(u.getStockQty())?new BigDecimal(0):u.getStockQty()).compareTo(new BigDecimal(0))==0)).collect(Collectors.toList());
        if(notQtyList.size()>0){
            for (WmsInnerStockOrderDet item : notQtyList) {
                SearchWmsInnerStockOrderDetBarcode searchOrderDetBarcode=new SearchWmsInnerStockOrderDetBarcode();
                searchOrderDetBarcode.setQueryAll("true");
                searchOrderDetBarcode.setStockOrderDetId(item.getStockOrderDetId());
                List<WmsInnerStockOrderDetBarcodeDto> detBarcodeDtos=wmsInnerStockOrderDetBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchOrderDetBarcode));
                if(detBarcodeDtos.size()>0){
                    //更新盘点条码状态为已提交
                    for (WmsInnerStockOrderDetBarcodeDto detBarcodeDto : detBarcodeDtos) {
                        WmsInnerStockOrderDetBarcode orderDetBarcode=new WmsInnerStockOrderDetBarcode();
                        orderDetBarcode.setStockOrderDetBarcodeId(detBarcodeDto.getStockOrderDetBarcodeId());
                        orderDetBarcode.setScanStatus((byte)3);
                        orderDetBarcode.setModifiedUserId(sysUser.getUserId());
                        orderDetBarcode.setModifiedTime(new Date());
                        num+=wmsInnerStockOrderDetBarcodeMapper.updateByPrimaryKeySelective(orderDetBarcode);
                    }
                    //更新明细盘点数量 差异数量
                    List<WmsInnerStockOrderDetBarcodeDto> barcodeDetList = detBarcodeDtos.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode())?"":u.getBarcode())!="")).collect(Collectors.toList());
                    BigDecimal totalMaterialQty=barcodeDetList.stream().map(WmsInnerStockOrderDetBarcodeDto::getMaterialQty).reduce(BigDecimal.ZERO,BigDecimal::add);
                    item.setStockQty(totalMaterialQty);
                    item.setModifiedUserId(sysUser.getUserId());
                    item.setModifiedTime(new Date());
                    if(StringUtils.isEmpty(item.getStockQty())){
                        item.setStockQty(BigDecimal.ZERO);
                    }
                    if(StringUtils.isEmpty(item.getOriginalQty())){
                        item.setOriginalQty(new BigDecimal(0));
                    }
                    item.setVarianceQty(item.getStockQty().subtract(item.getOriginalQty()));

                    num+=wmsInventoryVerificationDetMapper.updateByPrimaryKeySelective(item);
                }
            }
        }

        return num;
    }

    /**
     * 差异处理
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int difference(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        int num = 0;
        for (String id : arrId) {
            WmsInnerStockOrder wmsInventoryVerification = wmsInventoryVerificationMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(wmsInventoryVerification)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //单据状态(1-打开 2-待作业 3-作业中 4-待处理 5-完成 6-作废)
            if(wmsInventoryVerification.getOrderStatus()==(byte)5){
                throw new BizErrorException("盘点已完成");
            }
            if(wmsInventoryVerification.getProjectType()==1){
                throw new BizErrorException("只能对复盘单进行差异处理");
            }
            if(wmsInventoryVerification.getOrderStatus()!=4){
                throw new BizErrorException("复盘单未确认完成,无法差异处理");
            }
            Example example = new Example(WmsInnerStockOrderDet.class);
            example.createCriteria().andEqualTo("stockOrderId", wmsInventoryVerification.getStockOrderId());
            List<WmsInnerStockOrderDet> list = wmsInventoryVerificationDetMapper.selectByExample(example);

            //差异处理盘盈盘亏计算
            num+=this.unlockOrLock((byte) 3,list,wmsInventoryVerification);

            wmsInventoryVerification.setOrderStatus((byte)5);
            num+=wmsInventoryVerificationMapper.updateByPrimaryKeySelective(wmsInventoryVerification);

        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int pdaConfirm(String ids) {
        int num=1;
        //盘点确认
        //明细是否已全部登记
        Example example = new Example(WmsInnerStockOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("stockOrderId", ids);
        criteria.andNotEqualTo("ifRegister",1);
        List<WmsInnerStockOrderDet> stockOrderDetList=wmsInventoryVerificationDetMapper.selectByExample(example);
        if(stockOrderDetList.size()>0){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"盘点单存在未登记的明细 不能确认");
        }
        WmsInnerStockOrder wmsInnerStockOrder=wmsInventoryVerificationMapper.selectByPrimaryKey(ids);
        if(StringUtils.isEmpty(wmsInnerStockOrder)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"找不到相应的盘点单信息");
        }
        if(wmsInnerStockOrder.getProjectType()==(byte)2)
            wmsInnerStockOrder.setOrderStatus((byte)4);

//        WmsInnerStockOrder wmsInnerStockOrder = new WmsInnerStockOrder();
//        wmsInnerStockOrder.setOrderStatus((byte)3);
//        wmsInnerStockOrder.setStockOrderId(Long.parseLong(ids));
//        int num = wmsInventoryVerificationMapper.updateByPrimaryKeySelective(wmsInnerStockOrder);
//        if(num<1){
//            throw new BizErrorException("盘点确认失败");
//        }
        num+= this.ascertained(ids);
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int pdaCommit(Long stockOrderDetId,List<CommitInnerStockBarcodeDto> barcodeList){
        int num = 0;
        SysUser sysUser = currentUser();
        WmsInnerStockOrderDet stockOrderDet=wmsInventoryVerificationDetMapper.selectByPrimaryKey(stockOrderDetId);
        if(StringUtils.isEmpty(stockOrderDet)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"找不到相应的盘点明细信息");
        }

        List<WmsInnerStockOrderDetBarcode> detBarcodeAllIDList=new ArrayList<>();
        Example example = new Example(WmsInnerStockOrderDetBarcode.class);
        example.createCriteria().andEqualTo("stockOrderDetId",stockOrderDetId)
                .andEqualTo("stockResult",(byte)1);
        detBarcodeAllIDList=wmsInnerStockOrderDetBarcodeMapper.selectByExample(example);

        if(barcodeList.size()==1 && (barcodeList.get(0).getBarcode().equals("noBarcode") || StringUtils.isEmpty(barcodeList.get(0).getBarcode()))){
            //throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"未提交任何条码");
            if(StringUtils.isEmpty(stockOrderDet.getOriginalQty())){
                stockOrderDet.setOriginalQty(new BigDecimal(0));
            }
            stockOrderDet.setStockQty(BigDecimal.ZERO);
            stockOrderDet.setVarianceQty(stockOrderDet.getStockQty().subtract(stockOrderDet.getOriginalQty()));
            stockOrderDet.setIfRegister((byte)1);
            stockOrderDet.setStockUserId(sysUser.getUserId());
            stockOrderDet.setWorkerId(sysUser.getUserId());
            stockOrderDet.setModifiedTime(new Date());
            stockOrderDet.setModifiedUserId(sysUser.getUserId());
            num += wmsInventoryVerificationDetMapper.updateByPrimaryKeySelective(stockOrderDet);

            Long stockOrderId=stockOrderDet.getStockOrderId();
            WmsInnerStockOrder wmsInnerStockOrder=wmsInventoryVerificationMapper.selectByPrimaryKey(stockOrderId);
            wmsInnerStockOrder.setOrderStatus((byte)3);
            wmsInnerStockOrder.setModifiedUserId(sysUser.getUserId());
            wmsInnerStockOrder.setModifiedTime(new Date());
            num+=wmsInventoryVerificationMapper.updateByPrimaryKeySelective(wmsInnerStockOrder);

            for (WmsInnerStockOrderDetBarcode item : detBarcodeAllIDList) {
                WmsInnerStockOrderDetBarcode upStockOrderDetBarcode=new WmsInnerStockOrderDetBarcode();
                upStockOrderDetBarcode.setStockOrderDetBarcodeId(item.getStockOrderDetBarcodeId());
                upStockOrderDetBarcode.setStockResult((byte)4);
                upStockOrderDetBarcode.setModifiedUserId(sysUser.getUserId());
                upStockOrderDetBarcode.setModifiedTime(new Date());
                num+=wmsInnerStockOrderDetBarcodeMapper.updateByPrimaryKeySelective(upStockOrderDetBarcode);
            }

            return num;
        }
        List<WmsInnerStockOrderDetBarcodeDto> detBarcodeDtos=new ArrayList<>();
        List<WmsInnerStockOrderDetBarcode> detBarcodes=new ArrayList<>();

        WmsInnerStockOrder wmsInnerStockOrder=wmsInventoryVerificationMapper.selectByPrimaryKey(stockOrderDet.getStockOrderId());
        if(StringUtils.isEmpty(wmsInnerStockOrder)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"找不到相应的盘点单信息");
        }

        BigDecimal totalQty=new BigDecimal(0);
        //条码关系集合
        List<WmsInnerMaterialBarcodeReOrder> materialBarcodeReOrderList=new ArrayList<>();
        //盘点条码集合
        List<WmsInnerStockOrderDetBarcode> stockOrderDetBarcodeList=new ArrayList<>();

        //提交条码相应盘点明细条码ID集合
        List<Long> detBarcodeIDList=new ArrayList<>();

        //
        List<WmsInnerInventoryDet> wmsInnerInventoryDets=new ArrayList<>();

        //更新盘点条码状态为已提交
        SearchWmsInnerStockOrderDetBarcode searchOrderDetBarcode=new SearchWmsInnerStockOrderDetBarcode();
        searchOrderDetBarcode.setQueryAll("true");
        for (CommitInnerStockBarcodeDto item : barcodeList) {
            if(StringUtils.isEmpty(item.getBarcodeType())){
                item.setBarcodeType((byte)0);
            }
            byte barcodeType=item.getBarcodeType();
            String barcode=item.getBarcode();
            if(barcodeType==(byte)1){
                //SN
                searchOrderDetBarcode.setStockOrderDetId(stockOrderDetId);
                searchOrderDetBarcode.setBarcode(barcode);
                searchOrderDetBarcode.setColorBoxCode(null);
                searchOrderDetBarcode.setCartonCode(null);
                searchOrderDetBarcode.setPalletCode(null);
                detBarcodeDtos=wmsInnerStockOrderDetBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchOrderDetBarcode));
                if(detBarcodeDtos.size()>0){
                    /*WmsInnerStockOrderDetBarcode orderDetBarcode=new WmsInnerStockOrderDetBarcode();
                    orderDetBarcode.setStockOrderDetBarcodeId(detBarcodeDtos.get(0).getStockOrderDetBarcodeId());
                    orderDetBarcode.setScanStatus((byte)3);
                    orderDetBarcode.setStockResult((byte)2);
                    orderDetBarcode.setModifiedUserId(sysUser.getUserId());
                    orderDetBarcode.setModifiedTime(new Date());
                    num+=wmsInnerStockOrderDetBarcodeMapper.updateByPrimaryKeySelective(orderDetBarcode);*/

                    totalQty=totalQty.add(detBarcodeDtos.get(0).getMaterialQty());

                    if(!detBarcodeIDList.contains(detBarcodeDtos.get(0).getStockOrderDetBarcodeId())) {
                        detBarcodeIDList.add(detBarcodeDtos.get(0).getStockOrderDetBarcodeId());
                    }

                    if(StringUtils.isNotEmpty(detBarcodeDtos.get(0).getColorBoxCode())
                            || StringUtils.isNotEmpty(detBarcodeDtos.get(0).getCartonCode())
                            || StringUtils.isNotEmpty(detBarcodeDtos.get(0).getPalletCode())){
                        Example exampleBarcode = new Example(WmsInnerMaterialBarcode.class);
                        Example.Criteria criteriaBarcode = exampleBarcode.createCriteria();
                        if(StringUtils.isNotEmpty(detBarcodeDtos.get(0).getColorBoxCode())){
                            criteriaBarcode.orEqualTo("colorBoxCode",detBarcodeDtos.get(0).getColorBoxCode());
                        }
                        if(StringUtils.isNotEmpty(detBarcodeDtos.get(0).getCartonCode())){
                            criteriaBarcode.orEqualTo("cartonCode",detBarcodeDtos.get(0).getCartonCode());
                        }
                        if(StringUtils.isNotEmpty(detBarcodeDtos.get(0).getPalletCode())){
                            criteriaBarcode.orEqualTo("palletCode",detBarcodeDtos.get(0).getPalletCode());
                        }
                        criteriaBarcode.andIsNull("barcode");
                        List<WmsInnerMaterialBarcode> listBarcode=wmsInnerMaterialBarcodeMapper.selectByExample(exampleBarcode);
                        if(StringUtils.isNotEmpty(listBarcode) && listBarcode.size()>0){
                            for (WmsInnerMaterialBarcode wmsInnerMaterialBarcode : listBarcode) {
                                Example exampleDetBarcode = new Example(WmsInnerStockOrderDetBarcode.class);
                                Example.Criteria criteriaDetBarcode = exampleDetBarcode.createCriteria();
                                criteriaDetBarcode.andEqualTo("stockOrderDetId",stockOrderDetId);
                                criteriaDetBarcode.andEqualTo("materialBarcodeId",wmsInnerMaterialBarcode.getMaterialBarcodeId());
                                WmsInnerStockOrderDetBarcode detBarcode=wmsInnerStockOrderDetBarcodeMapper.selectOneByExample(exampleDetBarcode);
                                if(StringUtils.isNotEmpty(detBarcode)){
                                    if(!detBarcodeIDList.contains(detBarcode.getStockOrderDetBarcodeId())) {
                                        detBarcodeIDList.add(detBarcode.getStockOrderDetBarcodeId());
                                    }
                                }
                            }
                        }
                    }
                }
                else {
                    WmsInnerStockOrderDetBarcodeDto result= webScanBarcode(stockOrderDetId,item.getBarcode());
                    if(StringUtils.isNotEmpty(result.getOption1()) && "out".equals(result.getOption1())){
                        totalQty=totalQty.add(result.getMaterialQty());
                        //盘点条码明细
                        WmsInnerStockOrderDetBarcode detBarcode=new WmsInnerStockOrderDetBarcode();
                        detBarcode.setStockOrderDetBarcodeId(null);
                        detBarcode.setStockOrderDetId(stockOrderDetId);
                        detBarcode.setMaterialBarcodeId(result.getMaterialBarcodeId());
                        detBarcode.setScanStatus((byte)3);
                        detBarcode.setStockResult((byte)3);//盘盈
                        detBarcode.setCreateUserId(sysUser.getUserId());
                        detBarcode.setCreateTime(new Date());
                        stockOrderDetBarcodeList.add(detBarcode);
                    }
                }
            }
            else if(barcodeType==(byte)2){
                //彩盒
                searchOrderDetBarcode.setStockOrderDetId(stockOrderDetId);
                searchOrderDetBarcode.setColorBoxCode(barcode);
                searchOrderDetBarcode.setBarcode(null);
                searchOrderDetBarcode.setCartonCode(null);
                searchOrderDetBarcode.setPalletCode(null);
                detBarcodeDtos=wmsInnerStockOrderDetBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchOrderDetBarcode));
                if(detBarcodeDtos.size()>0){
                    for (WmsInnerStockOrderDetBarcodeDto detBarcodeDto : detBarcodeDtos) {
                        /*WmsInnerStockOrderDetBarcode orderDetBarcode=new WmsInnerStockOrderDetBarcode();
                        orderDetBarcode.setStockOrderDetBarcodeId(detBarcodeDto.getStockOrderDetBarcodeId());
                        orderDetBarcode.setScanStatus((byte)3);
                        orderDetBarcode.setStockResult((byte)2);
                        orderDetBarcode.setModifiedUserId(sysUser.getUserId());
                        orderDetBarcode.setModifiedTime(new Date());
                        num+=wmsInnerStockOrderDetBarcodeMapper.updateByPrimaryKeySelective(orderDetBarcode);*/

                        if(!detBarcodeIDList.contains(detBarcodeDto.getStockOrderDetBarcodeId())) {
                            detBarcodeIDList.add(detBarcodeDto.getStockOrderDetBarcodeId());
                        }

                        if(StringUtils.isNotEmpty(detBarcodeDto.getCartonCode())
                                || StringUtils.isNotEmpty(detBarcodeDto.getPalletCode())){
                            Example exampleBarcode = new Example(WmsInnerMaterialBarcode.class);
                            Example.Criteria criteriaBarcode = exampleBarcode.createCriteria();
                            if(StringUtils.isNotEmpty(detBarcodeDto.getCartonCode())){
                                criteriaBarcode.orEqualTo("cartonCode",detBarcodeDto.getCartonCode());
                            }
                            if(StringUtils.isNotEmpty(detBarcodeDto.getPalletCode())){
                                criteriaBarcode.orEqualTo("palletCode",detBarcodeDto.getPalletCode());
                            }
                            criteriaBarcode.andIsNull("barcode");
                            List<WmsInnerMaterialBarcode> listBarcode=wmsInnerMaterialBarcodeMapper.selectByExample(exampleBarcode);
                            if(StringUtils.isNotEmpty(listBarcode) && listBarcode.size()>0){
                                for (WmsInnerMaterialBarcode wmsInnerMaterialBarcode : listBarcode) {
                                    Example exampleDetBarcode = new Example(WmsInnerStockOrderDetBarcode.class);
                                    Example.Criteria criteriaDetBarcode = exampleDetBarcode.createCriteria();
                                    criteriaDetBarcode.andEqualTo("stockOrderDetId",stockOrderDetId);
                                    criteriaDetBarcode.andEqualTo("materialBarcodeId",wmsInnerMaterialBarcode.getMaterialBarcodeId());
                                    WmsInnerStockOrderDetBarcode detBarcode=wmsInnerStockOrderDetBarcodeMapper.selectOneByExample(exampleDetBarcode);
                                    if(StringUtils.isNotEmpty(detBarcode)){
                                        if(detBarcodeIDList.contains(detBarcode.getStockOrderDetBarcodeId())) {
                                            detBarcodeIDList.add(detBarcode.getStockOrderDetBarcodeId());
                                        }
                                    }
                                }
                            }
                        }

                    }

                    totalQty=totalQty.add(detBarcodeDtos.get(0).getMaterialQty());
                }
                else {
                    WmsInnerStockOrderDetBarcodeDto result= webScanBarcode(stockOrderDetId,item.getBarcode());
                    if(StringUtils.isNotEmpty(result.getOption1()) && "out".equals(result.getOption1())){
                        totalQty=totalQty.add(result.getMaterialQty());
                        //盘点条码明细
                        WmsInnerStockOrderDetBarcode detBarcode=new WmsInnerStockOrderDetBarcode();
                        detBarcode.setStockOrderDetBarcodeId(null);
                        detBarcode.setStockOrderDetId(stockOrderDetId);
                        detBarcode.setMaterialBarcodeId(result.getMaterialBarcodeId());
                        detBarcode.setScanStatus((byte)3);
                        detBarcode.setStockResult((byte)3);//盘盈
                        detBarcode.setCreateUserId(sysUser.getUserId());
                        detBarcode.setCreateTime(new Date());
                        stockOrderDetBarcodeList.add(detBarcode);
                    }
                }

            }
            else if(barcodeType==(byte)3){
                //箱码
                searchOrderDetBarcode.setStockOrderDetId(stockOrderDetId);
                searchOrderDetBarcode.setCartonCode(barcode);
                searchOrderDetBarcode.setBarcode(null);
                searchOrderDetBarcode.setColorBoxCode(null);
                searchOrderDetBarcode.setPalletCode(null);
                detBarcodeDtos=wmsInnerStockOrderDetBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchOrderDetBarcode));
                if(detBarcodeDtos.size()>0){
                    for (WmsInnerStockOrderDetBarcodeDto detBarcodeDto : detBarcodeDtos) {
                        /*WmsInnerStockOrderDetBarcode orderDetBarcode=new WmsInnerStockOrderDetBarcode();
                        orderDetBarcode.setStockOrderDetBarcodeId(detBarcodeDto.getStockOrderDetBarcodeId());
                        orderDetBarcode.setScanStatus((byte)3);
                        orderDetBarcode.setStockResult((byte)2);
                        orderDetBarcode.setModifiedUserId(sysUser.getUserId());
                        orderDetBarcode.setModifiedTime(new Date());
                        num+=wmsInnerStockOrderDetBarcodeMapper.updateByPrimaryKeySelective(orderDetBarcode);*/

                        if(!detBarcodeIDList.contains(detBarcodeDto.getStockOrderDetBarcodeId())) {
                            detBarcodeIDList.add(detBarcodeDto.getStockOrderDetBarcodeId());
                        }

                        if(StringUtils.isNotEmpty(detBarcodeDto.getColorBoxCode())
                                || StringUtils.isNotEmpty(detBarcodeDto.getPalletCode())){
                            Example exampleBarcode = new Example(WmsInnerMaterialBarcode.class);
                            Example.Criteria criteriaBarcode = exampleBarcode.createCriteria();
                            if(StringUtils.isNotEmpty(detBarcodeDto.getColorBoxCode())){
                                criteriaBarcode.orEqualTo("colorBoxCode",detBarcodeDto.getColorBoxCode());
                            }
                            if(StringUtils.isNotEmpty(detBarcodeDto.getPalletCode())){
                                criteriaBarcode.orEqualTo("palletCode",detBarcodeDto.getPalletCode());
                            }
                            criteriaBarcode.andIsNull("barcode");
                            List<WmsInnerMaterialBarcode> listBarcode=wmsInnerMaterialBarcodeMapper.selectByExample(exampleBarcode);
                            if(StringUtils.isNotEmpty(listBarcode) && listBarcode.size()>0){
                                for (WmsInnerMaterialBarcode wmsInnerMaterialBarcode : listBarcode) {
                                    Example exampleDetBarcode = new Example(WmsInnerStockOrderDetBarcode.class);
                                    Example.Criteria criteriaDetBarcode = exampleDetBarcode.createCriteria();
                                    criteriaDetBarcode.andEqualTo("stockOrderDetId",stockOrderDetId);
                                    criteriaDetBarcode.andEqualTo("materialBarcodeId",wmsInnerMaterialBarcode.getMaterialBarcodeId());
                                    WmsInnerStockOrderDetBarcode detBarcode=wmsInnerStockOrderDetBarcodeMapper.selectOneByExample(exampleDetBarcode);
                                    if(StringUtils.isNotEmpty(detBarcode)){
                                        if(!detBarcodeIDList.contains(detBarcode.getStockOrderDetBarcodeId())) {
                                            detBarcodeIDList.add(detBarcode.getStockOrderDetBarcodeId());
                                        }
                                    }
                                }
                            }
                        }
                    }

                    List<WmsInnerStockOrderDetBarcodeDto> detBarcodeList = detBarcodeDtos.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode())?"":u.getBarcode())!="")).collect(Collectors.toList());
                    BigDecimal totalMaterialQty=detBarcodeList.stream().map(WmsInnerStockOrderDetBarcodeDto::getMaterialQty).reduce(BigDecimal.ZERO,BigDecimal::add);
                    totalQty=totalQty.add(totalMaterialQty);
                }
                else {
                    WmsInnerStockOrderDetBarcodeDto result= webScanBarcode(stockOrderDetId,item.getBarcode());
                    if(StringUtils.isNotEmpty(result.getOption1()) && "out".equals(result.getOption1())){
                        totalQty=totalQty.add(result.getMaterialQty());
                        //盘点条码明细
                        WmsInnerStockOrderDetBarcode detBarcode=new WmsInnerStockOrderDetBarcode();
                        detBarcode.setStockOrderDetBarcodeId(null);
                        detBarcode.setStockOrderDetId(stockOrderDetId);
                        detBarcode.setMaterialBarcodeId(result.getMaterialBarcodeId());
                        detBarcode.setScanStatus((byte)3);
                        detBarcode.setStockResult((byte)3);//盘盈
                        detBarcode.setCreateUserId(sysUser.getUserId());
                        detBarcode.setCreateTime(new Date());
                        stockOrderDetBarcodeList.add(detBarcode);
                    }
                }
            }
            else if(barcodeType==(byte)4){
                //栈板码
                searchOrderDetBarcode.setStockOrderDetId(stockOrderDetId);
                searchOrderDetBarcode.setPalletCode(barcode);
                searchOrderDetBarcode.setBarcode(null);
                searchOrderDetBarcode.setColorBoxCode(null);
                searchOrderDetBarcode.setCartonCode(null);
                detBarcodeDtos=wmsInnerStockOrderDetBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchOrderDetBarcode));
                if(detBarcodeDtos.size()>0){
                    for (WmsInnerStockOrderDetBarcodeDto detBarcodeDto : detBarcodeDtos) {
                        /*WmsInnerStockOrderDetBarcode orderDetBarcode=new WmsInnerStockOrderDetBarcode();
                        orderDetBarcode.setStockOrderDetBarcodeId(detBarcodeDto.getStockOrderDetBarcodeId());
                        orderDetBarcode.setScanStatus((byte)3);
                        orderDetBarcode.setStockResult((byte)2);
                        orderDetBarcode.setModifiedUserId(sysUser.getUserId());
                        orderDetBarcode.setModifiedTime(new Date());
                        num+=wmsInnerStockOrderDetBarcodeMapper.updateByPrimaryKeySelective(orderDetBarcode);*/

                        if(!detBarcodeIDList.contains(detBarcodeDto.getStockOrderDetBarcodeId())) {
                            detBarcodeIDList.add(detBarcodeDto.getStockOrderDetBarcodeId());
                        }

                        if(StringUtils.isNotEmpty(detBarcodeDto.getColorBoxCode())
                                || StringUtils.isNotEmpty(detBarcodeDto.getCartonCode())){
                            Example exampleBarcode = new Example(WmsInnerMaterialBarcode.class);
                            Example.Criteria criteriaBarcode = exampleBarcode.createCriteria();
                            if(StringUtils.isNotEmpty(detBarcodeDto.getColorBoxCode())){
                                criteriaBarcode.orEqualTo("colorBoxCode",detBarcodeDto.getColorBoxCode());
                            }
                            if(StringUtils.isNotEmpty(detBarcodeDto.getCartonCode())){
                                criteriaBarcode.orEqualTo("cartonCode",detBarcodeDto.getCartonCode());
                            }
                            criteriaBarcode.andIsNull("barcode");
                            List<WmsInnerMaterialBarcode> listBarcode=wmsInnerMaterialBarcodeMapper.selectByExample(exampleBarcode);
                            if(StringUtils.isNotEmpty(listBarcode) && listBarcode.size()>0){
                                for (WmsInnerMaterialBarcode wmsInnerMaterialBarcode : listBarcode) {
                                    Example exampleDetBarcode = new Example(WmsInnerStockOrderDetBarcode.class);
                                    Example.Criteria criteriaDetBarcode = exampleDetBarcode.createCriteria();
                                    criteriaDetBarcode.andEqualTo("stockOrderDetId",stockOrderDetId);
                                    criteriaDetBarcode.andEqualTo("materialBarcodeId",wmsInnerMaterialBarcode.getMaterialBarcodeId());
                                    WmsInnerStockOrderDetBarcode detBarcode=wmsInnerStockOrderDetBarcodeMapper.selectOneByExample(exampleDetBarcode);
                                    if(StringUtils.isNotEmpty(detBarcode)){
                                        if(!detBarcodeIDList.contains(detBarcode.getStockOrderDetBarcodeId())) {
                                            detBarcodeIDList.add(detBarcode.getStockOrderDetBarcodeId());
                                        }
                                    }
                                }
                            }
                        }
                    }

                    List<WmsInnerStockOrderDetBarcodeDto> detBarcodeList = detBarcodeDtos.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode())?"":u.getBarcode())!="")).collect(Collectors.toList());
                    BigDecimal totalMaterialQty=detBarcodeList.stream().map(WmsInnerStockOrderDetBarcodeDto::getMaterialQty).reduce(BigDecimal.ZERO,BigDecimal::add);
                    totalQty=totalQty.add(totalMaterialQty);
                }
                else {
                    WmsInnerStockOrderDetBarcodeDto result= webScanBarcode(stockOrderDetId,item.getBarcode());
                    if(StringUtils.isNotEmpty(result.getOption1()) && "out".equals(result.getOption1())){
                        totalQty=totalQty.add(result.getMaterialQty());
                        //盘点条码明细
                        WmsInnerStockOrderDetBarcode detBarcode=new WmsInnerStockOrderDetBarcode();
                        detBarcode.setStockOrderDetBarcodeId(null);
                        detBarcode.setStockOrderDetId(stockOrderDetId);
                        detBarcode.setMaterialBarcodeId(result.getMaterialBarcodeId());
                        detBarcode.setScanStatus((byte)3);
                        detBarcode.setStockResult((byte)3);//盘盈
                        detBarcode.setCreateUserId(sysUser.getUserId());
                        detBarcode.setCreateTime(new Date());
                        stockOrderDetBarcodeList.add(detBarcode);
                    }
                }
            }
            else {
                //非系统条码
                Long materialBarcodeId=null;
                Example exampleBarcode = new Example(WmsInnerMaterialBarcode.class);
                Example.Criteria criteriaBarcode = exampleBarcode.createCriteria();
                criteriaBarcode.andEqualTo("barcode", item.getBarcode());
                WmsInnerMaterialBarcode barcodeExist=wmsInnerMaterialBarcodeMapper.selectOneByExample(exampleBarcode);
                if(StringUtils.isNotEmpty(barcodeExist)){
                    materialBarcodeId=barcodeExist.getMaterialBarcodeId();
                }
                if(StringUtils.isEmpty(materialBarcodeId)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    WmsInnerMaterialBarcode wmsInnerMaterialBarcode = new WmsInnerMaterialBarcode();
                    wmsInnerMaterialBarcode.setMaterialId(stockOrderDet.getMaterialId());
                    wmsInnerMaterialBarcode.setBarcode(item.getBarcode());
                    if (StringUtils.isEmpty(item.getBatchCode())) {
                        wmsInnerMaterialBarcode.setBatchCode(stockOrderDet.getBatchCode());
                    } else {
                        wmsInnerMaterialBarcode.setBatchCode(item.getBatchCode());
                    }
                    if (StringUtils.isNotEmpty(stockOrderDet.getSupplierId())) {
                        wmsInnerMaterialBarcode.setSupplierId(stockOrderDet.getSupplierId());
                    }

                    wmsInnerMaterialBarcode.setMaterialQty(item.getMaterialQty());
                    wmsInnerMaterialBarcode.setIfSysBarcode((byte) 0);
                    Date productionTime = null;
                    if (StringUtils.isNotEmpty(item.getProductionTime())) {
                        try {
                            productionTime = sdf.parse(item.getProductionTime());
                        } catch (Exception ex) {
                            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "参数生产日期转换为时间类型异常");
                        }
                    }
                    wmsInnerMaterialBarcode.setProductionTime(productionTime);

                    //产生类型(1-供应商条码 2-自己打印 3-生产条码)
                    wmsInnerMaterialBarcode.setCreateType((byte) 1);
                    //条码状态(1-已生成 2-已打印 3-已收货 4-已质检 5-已上架 6-已出库)
                    wmsInnerMaterialBarcode.setBarcodeStatus((byte) 1);
                    wmsInnerMaterialBarcode.setBarcodeType((byte) 1);
                    wmsInnerMaterialBarcode.setOrgId(sysUser.getOrganizationId());
                    wmsInnerMaterialBarcode.setCreateTime(new Date());
                    wmsInnerMaterialBarcode.setCreateUserId(sysUser.getUserId());
                    num += wmsInnerMaterialBarcodeMapper.insertUseGeneratedKeys(wmsInnerMaterialBarcode);

                    materialBarcodeId=wmsInnerMaterialBarcode.getMaterialBarcodeId();
                }

                //条码关系
                WmsInnerMaterialBarcodeReOrder wmsInnerMaterialBarcodeReOrder=new WmsInnerMaterialBarcodeReOrder();
                wmsInnerMaterialBarcodeReOrder.setOrderTypeCode("INNER-TSO");//类型 盘点
                wmsInnerMaterialBarcodeReOrder.setOrderCode(wmsInnerStockOrder.getPlanStockOrderCode());
                wmsInnerMaterialBarcodeReOrder.setOrderId(wmsInnerStockOrder.getStockOrderId());
                wmsInnerMaterialBarcodeReOrder.setOrderDetId(stockOrderDetId);
                //来料条码ID
                wmsInnerMaterialBarcodeReOrder.setMaterialBarcodeId(materialBarcodeId);
                wmsInnerMaterialBarcodeReOrder.setScanStatus((byte)3);
                wmsInnerMaterialBarcodeReOrder.setOrgId(sysUser.getOrganizationId());
                wmsInnerMaterialBarcodeReOrder.setCreateTime(new Date());
                wmsInnerMaterialBarcodeReOrder.setCreateUserId(sysUser.getUserId());
                materialBarcodeReOrderList.add(wmsInnerMaterialBarcodeReOrder);

                //盘点条码
                WmsInnerStockOrderDetBarcode wmsInnerStockOrderDetBarcode=new WmsInnerStockOrderDetBarcode();
                wmsInnerStockOrderDetBarcode.setStockOrderDetId(stockOrderDetId);
                wmsInnerStockOrderDetBarcode.setMaterialBarcodeId(materialBarcodeId);
                wmsInnerStockOrderDetBarcode.setScanStatus((byte)3);
                wmsInnerStockOrderDetBarcode.setStockResult((byte)3);
                wmsInnerStockOrderDetBarcode.setCreateUserId(sysUser.getUserId());
                wmsInnerStockOrderDetBarcode.setCreateTime(new Date());
                wmsInnerStockOrderDetBarcode.setOrgId(sysUser.getOrganizationId());
                stockOrderDetBarcodeList.add(wmsInnerStockOrderDetBarcode);

                //库存条码明细
                /*WmsInnerInventoryDet inventoryDet = new WmsInnerInventoryDet();
                inventoryDet.setStorageId(stockOrderDet.getStorageId());
                inventoryDet.setMaterialBarcodeId(wmsInnerMaterialBarcode.getMaterialBarcodeId());
                inventoryDet.setReceivingDate(new Date());//入库日期
                inventoryDet.setAsnCode(wmsInnerStockOrder.getPlanStockOrderCode());//盘点单号
                inventoryDet.setIfStockLock((byte) 0);
                inventoryDet.setInventoryStatusId(stockOrderDet.getInventoryStatusId());
                inventoryDet.setBarcodeStatus((byte) 1);//在库
                inventoryDet.setCreateUserId(sysUser.getUserId());
                inventoryDet.setCreateTime(new Date());
                inventoryDet.setOrgId(sysUser.getOrganizationId());
                wmsInnerInventoryDets.add(inventoryDet);*/

                totalQty=totalQty.add(item.getMaterialQty());
            }
        }

        //更新盘点明细为登记状态 盘点数量 差异数量
        if(StringUtils.isEmpty(stockOrderDet.getOriginalQty())){
            stockOrderDet.setOriginalQty(new BigDecimal(0));
        }
        stockOrderDet.setStockQty(totalQty);
        stockOrderDet.setVarianceQty(stockOrderDet.getStockQty().subtract(stockOrderDet.getOriginalQty()));
        stockOrderDet.setIfRegister((byte)1);
        stockOrderDet.setStockUserId(sysUser.getUserId());
        stockOrderDet.setWorkerId(sysUser.getUserId());
        stockOrderDet.setModifiedTime(new Date());
        stockOrderDet.setModifiedUserId(sysUser.getUserId());
        num += wmsInventoryVerificationDetMapper.updateByPrimaryKeySelective(stockOrderDet);

        //非系统条码增加到关系表
        if(materialBarcodeReOrderList.size()>0){
            num+=wmsInnerMaterialBarcodeReOrderService.batchAdd(materialBarcodeReOrderList);
        }

        //非系统条码增加到盘点条码明细
        if(stockOrderDetBarcodeList.size()>0){
            num+=wmsInnerStockOrderDetBarcodeMapper.insertList(stockOrderDetBarcodeList);
        }

        //非系统条码增加到库存条码明细
        /*if(wmsInnerInventoryDets.size()>0){
            num+=wmsInnerInventoryDetMapper.insertList(wmsInnerInventoryDets);
        }*/

        //条码状态盘亏
        List<Long> tempList=new ArrayList<>();
        if(detBarcodeAllIDList.size()>0){
            for (WmsInnerStockOrderDetBarcode detBarcode : detBarcodeAllIDList) {
                tempList.add(detBarcode.getStockOrderDetBarcodeId());
            }
            if(StringUtils.isNotEmpty(detBarcodeIDList) && detBarcodeIDList.size()>0)
                tempList.removeAll(detBarcodeIDList);
        }
        //盘亏
        if(tempList.size()>0){
            for (Long aLong : tempList) {
                WmsInnerStockOrderDetBarcode upStockOrderDetBarcode=new WmsInnerStockOrderDetBarcode();
                upStockOrderDetBarcode.setStockOrderDetBarcodeId(aLong);
                upStockOrderDetBarcode.setStockResult((byte)4);
                upStockOrderDetBarcode.setModifiedUserId(sysUser.getUserId());
                upStockOrderDetBarcode.setModifiedTime(new Date());
                wmsInnerStockOrderDetBarcodeMapper.updateByPrimaryKeySelective(upStockOrderDetBarcode);
            }
        }
        //已盘点
        if(detBarcodeIDList.size()>0){
            for (Long aLong : detBarcodeIDList) {
                WmsInnerStockOrderDetBarcode upStockOrderDetBarcode=new WmsInnerStockOrderDetBarcode();
                upStockOrderDetBarcode.setStockOrderDetBarcodeId(aLong);
                upStockOrderDetBarcode.setScanStatus((byte)3);
                upStockOrderDetBarcode.setStockResult((byte)2);
                upStockOrderDetBarcode.setModifiedUserId(sysUser.getUserId());
                upStockOrderDetBarcode.setModifiedTime(new Date());
                wmsInnerStockOrderDetBarcodeMapper.updateByPrimaryKeySelective(upStockOrderDetBarcode);
            }
        }

        Long stockOrderId=stockOrderDet.getStockOrderId();
        WmsInnerStockOrder upWmsInnerStockOrder=wmsInventoryVerificationMapper.selectByPrimaryKey(stockOrderId);
        upWmsInnerStockOrder.setOrderStatus((byte)3);
        upWmsInnerStockOrder.setModifiedUserId(sysUser.getUserId());
        upWmsInnerStockOrder.setModifiedTime(new Date());
        num+=wmsInventoryVerificationMapper.updateByPrimaryKeySelective(upWmsInnerStockOrder);

        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int webCommit(Byte commitType,Long stockOrderDetId,List<CommitInnerStockBarcodeDto> barcodeList){
        WmsInnerStockOrderDet wmsInnerStockOrderDet=wmsInventoryVerificationDetMapper.selectByPrimaryKey(stockOrderDetId);
        if(StringUtils.isEmpty(wmsInnerStockOrderDet)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"未找到相应明细ID的盘点明细信息 ID-->"+stockOrderDetId.toString());
        }

        WmsInnerStockOrder wmsInnerStockOrder=wmsInventoryVerificationMapper.selectByPrimaryKey(wmsInnerStockOrderDet.getStockOrderId());
        if(StringUtils.isEmpty(wmsInnerStockOrder)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"未找到相应的盘点单信息");
        }
        int num = 0;
        SysUser sysUser = currentUser();
        //提交条码相应盘点明细条码ID集合
        List<Long> detBarcodeIDList=new ArrayList<>();

        List<WmsInnerStockOrderDetBarcode> detBarcodeAllIDList=new ArrayList<>();
        Example example = new Example(WmsInnerStockOrderDetBarcode.class);
        example.createCriteria().andEqualTo("stockOrderDetId",stockOrderDetId)
                .andEqualTo("stockResult",(byte)1);
        detBarcodeAllIDList=wmsInnerStockOrderDetBarcodeMapper.selectByExample(example);

        if(barcodeList.size()<=0){
            //throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"未提交任何条码");
            if(StringUtils.isEmpty(wmsInnerStockOrderDet.getOriginalQty())){
                wmsInnerStockOrderDet.setOriginalQty(new BigDecimal(0));
            }
            wmsInnerStockOrderDet.setStockQty(BigDecimal.ZERO);
            wmsInnerStockOrderDet.setVarianceQty(wmsInnerStockOrderDet.getStockQty().subtract(wmsInnerStockOrderDet.getOriginalQty()));
            wmsInnerStockOrderDet.setIfRegister((byte)1);
            wmsInnerStockOrderDet.setStockUserId(sysUser.getUserId());
            wmsInnerStockOrderDet.setWorkerId(sysUser.getUserId());
            wmsInnerStockOrderDet.setModifiedTime(new Date());
            wmsInnerStockOrderDet.setModifiedUserId(sysUser.getUserId());
            num += wmsInventoryVerificationDetMapper.updateByPrimaryKeySelective(wmsInnerStockOrderDet);

            wmsInnerStockOrder.setOrderStatus((byte)3);
            wmsInnerStockOrder.setModifiedUserId(sysUser.getUserId());
            wmsInnerStockOrder.setModifiedTime(new Date());
            num+=wmsInventoryVerificationMapper.updateByPrimaryKeySelective(wmsInnerStockOrder);

            for (WmsInnerStockOrderDetBarcode item : detBarcodeAllIDList) {
                WmsInnerStockOrderDetBarcode upStockOrderDetBarcode=new WmsInnerStockOrderDetBarcode();
                upStockOrderDetBarcode.setStockOrderDetBarcodeId(item.getStockOrderDetBarcodeId());
                upStockOrderDetBarcode.setStockResult((byte)4);
                upStockOrderDetBarcode.setModifiedUserId(sysUser.getUserId());
                upStockOrderDetBarcode.setModifiedTime(new Date());
                num+=wmsInnerStockOrderDetBarcodeMapper.updateByPrimaryKeySelective(upStockOrderDetBarcode);
            }

            return num;
        }


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //条码关系集合
        List<WmsInnerMaterialBarcodeReOrder> materialBarcodeReOrderList=new ArrayList<>();
        //条码库存集合
        List<WmsInnerInventoryDet> wmsInnerInventoryDets =new ArrayList<>();
        //盘点条码集合
        List<WmsInnerStockOrderDetBarcode> orderDetBarcodeList =new ArrayList<>();

        //物料ID
        Long materialId=wmsInnerStockOrderDet.getMaterialId();
        //供应商ID
        Long supplierId=wmsInnerStockOrderDet.getSupplierId();
        //库位ID
        Long storageId=wmsInnerStockOrderDet.getStorageId();
        //总数量
        BigDecimal totalQty=new BigDecimal(0);

        List<WmsInnerStockOrderDetBarcodeDto> detBarcodeDtos=new ArrayList<>();
        SearchWmsInnerStockOrderDetBarcode searchOrderDetBarcode=new SearchWmsInnerStockOrderDetBarcode();
        searchOrderDetBarcode.setQueryAll("true");
        //更新盘点条码状态为已提交
        for (CommitInnerStockBarcodeDto item : barcodeList) {
            WmsInnerStockOrderDetBarcodeDto result= webScanBarcode(stockOrderDetId,item.getBarcode());
            if(StringUtils.isEmpty(result)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"提交条码无效");
            }
            byte barcodeType=result.getBarcodeType();
            if(StringUtils.isEmpty(barcodeType))
                barcodeType=(byte)0;
            String barcode=item.getBarcode();
            if(barcodeType==(byte)1 || commitType==(byte)1){
                //SN
                searchOrderDetBarcode.setStockOrderDetId(stockOrderDetId);
                searchOrderDetBarcode.setBarcode(barcode);
                searchOrderDetBarcode.setColorBoxCode(null);
                searchOrderDetBarcode.setCartonCode(null);
                searchOrderDetBarcode.setPalletCode(null);
                detBarcodeDtos=wmsInnerStockOrderDetBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchOrderDetBarcode));
                if(detBarcodeDtos.size()>0){
                    /*WmsInnerStockOrderDetBarcode orderDetBarcode=new WmsInnerStockOrderDetBarcode();
                    orderDetBarcode.setStockOrderDetBarcodeId(detBarcodeDtos.get(0).getStockOrderDetBarcodeId());
                    orderDetBarcode.setScanStatus((byte)3);
                    orderDetBarcode.setStockResult((byte)2);
                    orderDetBarcode.setModifiedUserId(sysUser.getUserId());
                    orderDetBarcode.setModifiedTime(new Date());
                    num+=wmsInnerStockOrderDetBarcodeMapper.updateByPrimaryKeySelective(orderDetBarcode);*/

                    totalQty=totalQty.add(detBarcodeDtos.get(0).getMaterialQty());

                    if(!detBarcodeIDList.contains(detBarcodeDtos.get(0).getStockOrderDetBarcodeId())) {
                        detBarcodeIDList.add(detBarcodeDtos.get(0).getStockOrderDetBarcodeId());
                    }

                    if(StringUtils.isNotEmpty(detBarcodeDtos.get(0).getColorBoxCode())
                        || StringUtils.isNotEmpty(detBarcodeDtos.get(0).getCartonCode())
                        || StringUtils.isNotEmpty(detBarcodeDtos.get(0).getPalletCode())){
                        Example exampleBarcode = new Example(WmsInnerMaterialBarcode.class);
                        Example.Criteria criteriaBarcode = exampleBarcode.createCriteria();
                        if(StringUtils.isNotEmpty(detBarcodeDtos.get(0).getColorBoxCode())){
                            criteriaBarcode.orEqualTo("colorBoxCode",detBarcodeDtos.get(0).getColorBoxCode());
                        }
                        if(StringUtils.isNotEmpty(detBarcodeDtos.get(0).getCartonCode())){
                            criteriaBarcode.orEqualTo("cartonCode",detBarcodeDtos.get(0).getCartonCode());
                        }
                        if(StringUtils.isNotEmpty(detBarcodeDtos.get(0).getPalletCode())){
                            criteriaBarcode.orEqualTo("palletCode",detBarcodeDtos.get(0).getPalletCode());
                        }
                        criteriaBarcode.andIsNull("barcode");
                        List<WmsInnerMaterialBarcode> listBarcode=wmsInnerMaterialBarcodeMapper.selectByExample(exampleBarcode);
                        if(StringUtils.isNotEmpty(listBarcode) && listBarcode.size()>0){
                            for (WmsInnerMaterialBarcode wmsInnerMaterialBarcode : listBarcode) {
                                Example exampleDetBarcode = new Example(WmsInnerStockOrderDetBarcode.class);
                                Example.Criteria criteriaDetBarcode = exampleDetBarcode.createCriteria();
                                criteriaDetBarcode.andEqualTo("stockOrderDetId",stockOrderDetId);
                                criteriaDetBarcode.andEqualTo("materialBarcodeId",wmsInnerMaterialBarcode.getMaterialBarcodeId());
                                WmsInnerStockOrderDetBarcode detBarcode=wmsInnerStockOrderDetBarcodeMapper.selectOneByExample(exampleDetBarcode);
                                if(StringUtils.isNotEmpty(detBarcode)){
                                    if(!detBarcodeIDList.contains(detBarcode.getStockOrderDetBarcodeId())) {
                                        detBarcodeIDList.add(detBarcode.getStockOrderDetBarcodeId());
                                    }
                                }
                            }
                        }
                    }
                }
                else{
                    //扫描的是已出库或已取消条码
                    if(StringUtils.isNotEmpty(result.getOption1()) && "out".equals(result.getOption1())){
                        totalQty=totalQty.add(result.getMaterialQty());
                        //盘点条码明细
                        WmsInnerStockOrderDetBarcode detBarcode=new WmsInnerStockOrderDetBarcode();
                        detBarcode.setStockOrderDetBarcodeId(null);
                        detBarcode.setStockOrderDetId(stockOrderDetId);
                        detBarcode.setMaterialBarcodeId(result.getMaterialBarcodeId());
                        detBarcode.setScanStatus((byte)3);
                        detBarcode.setStockResult((byte)3);//盘盈
                        detBarcode.setCreateUserId(sysUser.getUserId());
                        detBarcode.setCreateTime(new Date());
                        orderDetBarcodeList.add(detBarcode);
                    }
                }

            }
            else if(barcodeType==(byte)2){
                //彩盒
                searchOrderDetBarcode.setStockOrderDetId(stockOrderDetId);
                searchOrderDetBarcode.setColorBoxCode(barcode);
                searchOrderDetBarcode.setBarcode(null);
                searchOrderDetBarcode.setCartonCode(null);
                searchOrderDetBarcode.setPalletCode(null);
                detBarcodeDtos=wmsInnerStockOrderDetBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchOrderDetBarcode));
                if(detBarcodeDtos.size()>0){
                    for (WmsInnerStockOrderDetBarcodeDto detBarcodeDto : detBarcodeDtos) {
                        /*WmsInnerStockOrderDetBarcode orderDetBarcode=new WmsInnerStockOrderDetBarcode();
                        orderDetBarcode.setStockOrderDetBarcodeId(detBarcodeDto.getStockOrderDetBarcodeId());
                        orderDetBarcode.setScanStatus((byte)3);
                        orderDetBarcode.setStockResult((byte)2);
                        orderDetBarcode.setModifiedUserId(sysUser.getUserId());
                        orderDetBarcode.setModifiedTime(new Date());
                        num+=wmsInnerStockOrderDetBarcodeMapper.updateByPrimaryKeySelective(orderDetBarcode);*/

                        if(!detBarcodeIDList.contains(detBarcodeDto.getStockOrderDetBarcodeId())) {
                            detBarcodeIDList.add(detBarcodeDto.getStockOrderDetBarcodeId());
                        }

                        if(StringUtils.isNotEmpty(detBarcodeDto.getCartonCode())
                                || StringUtils.isNotEmpty(detBarcodeDto.getPalletCode())){
                            Example exampleBarcode = new Example(WmsInnerMaterialBarcode.class);
                            Example.Criteria criteriaBarcode = exampleBarcode.createCriteria();
                            if(StringUtils.isNotEmpty(detBarcodeDto.getCartonCode())){
                                criteriaBarcode.orEqualTo("cartonCode",detBarcodeDto.getCartonCode());
                            }
                            if(StringUtils.isNotEmpty(detBarcodeDto.getPalletCode())){
                                criteriaBarcode.orEqualTo("palletCode",detBarcodeDto.getPalletCode());
                            }
                            criteriaBarcode.andIsNull("barcode");
                            List<WmsInnerMaterialBarcode> listBarcode=wmsInnerMaterialBarcodeMapper.selectByExample(exampleBarcode);
                            if(StringUtils.isNotEmpty(listBarcode) && listBarcode.size()>0){
                                for (WmsInnerMaterialBarcode wmsInnerMaterialBarcode : listBarcode) {
                                    Example exampleDetBarcode = new Example(WmsInnerStockOrderDetBarcode.class);
                                    Example.Criteria criteriaDetBarcode = exampleDetBarcode.createCriteria();
                                    criteriaDetBarcode.andEqualTo("stockOrderDetId",stockOrderDetId);
                                    criteriaDetBarcode.andEqualTo("materialBarcodeId",wmsInnerMaterialBarcode.getMaterialBarcodeId());
                                    WmsInnerStockOrderDetBarcode detBarcode=wmsInnerStockOrderDetBarcodeMapper.selectOneByExample(exampleDetBarcode);
                                    if(StringUtils.isNotEmpty(detBarcode)){
                                        if(detBarcodeIDList.contains(detBarcode.getStockOrderDetBarcodeId())) {
                                            detBarcodeIDList.add(detBarcode.getStockOrderDetBarcodeId());
                                        }
                                    }
                                }
                            }
                        }
                    }

                    totalQty=totalQty.add(detBarcodeDtos.get(0).getMaterialQty());
                }
                else{
                    //扫描的是已出库或已取消条码
                    if(StringUtils.isNotEmpty(result.getOption1()) && "out".equals(result.getOption1())){
                        totalQty=totalQty.add(result.getMaterialQty());
                        //盘点条码明细
                        WmsInnerStockOrderDetBarcode detBarcode=new WmsInnerStockOrderDetBarcode();
                        detBarcode.setStockOrderDetBarcodeId(null);
                        detBarcode.setStockOrderDetId(stockOrderDetId);
                        detBarcode.setMaterialBarcodeId(result.getMaterialBarcodeId());
                        detBarcode.setScanStatus((byte)3);
                        detBarcode.setStockResult((byte)3);//盘盈
                        detBarcode.setCreateUserId(sysUser.getUserId());
                        detBarcode.setCreateTime(new Date());
                        orderDetBarcodeList.add(detBarcode);
                    }
                }

            }
            else if(barcodeType==(byte)3){
                //箱码
                searchOrderDetBarcode.setStockOrderDetId(stockOrderDetId);
                searchOrderDetBarcode.setCartonCode(barcode);
                searchOrderDetBarcode.setBarcode(null);
                searchOrderDetBarcode.setColorBoxCode(null);
                searchOrderDetBarcode.setPalletCode(null);
                detBarcodeDtos=wmsInnerStockOrderDetBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchOrderDetBarcode));
                if(detBarcodeDtos.size()>0){
                    for (WmsInnerStockOrderDetBarcodeDto detBarcodeDto : detBarcodeDtos) {
                        /*WmsInnerStockOrderDetBarcode orderDetBarcode=new WmsInnerStockOrderDetBarcode();
                        orderDetBarcode.setStockOrderDetBarcodeId(detBarcodeDto.getStockOrderDetBarcodeId());
                        orderDetBarcode.setScanStatus((byte)3);
                        orderDetBarcode.setStockResult((byte)2);
                        orderDetBarcode.setModifiedUserId(sysUser.getUserId());
                        orderDetBarcode.setModifiedTime(new Date());
                        num+=wmsInnerStockOrderDetBarcodeMapper.updateByPrimaryKeySelective(orderDetBarcode);*/

                        if(!detBarcodeIDList.contains(detBarcodeDto.getStockOrderDetBarcodeId())) {
                            detBarcodeIDList.add(detBarcodeDto.getStockOrderDetBarcodeId());
                        }

                        if(StringUtils.isNotEmpty(detBarcodeDto.getColorBoxCode())
                                || StringUtils.isNotEmpty(detBarcodeDto.getPalletCode())){
                            Example exampleBarcode = new Example(WmsInnerMaterialBarcode.class);
                            Example.Criteria criteriaBarcode = exampleBarcode.createCriteria();
                            if(StringUtils.isNotEmpty(detBarcodeDto.getColorBoxCode())){
                                criteriaBarcode.orEqualTo("colorBoxCode",detBarcodeDto.getColorBoxCode());
                            }
                            if(StringUtils.isNotEmpty(detBarcodeDto.getPalletCode())){
                                criteriaBarcode.orEqualTo("palletCode",detBarcodeDto.getPalletCode());
                            }
                            criteriaBarcode.andIsNull("barcode");
                            List<WmsInnerMaterialBarcode> listBarcode=wmsInnerMaterialBarcodeMapper.selectByExample(exampleBarcode);
                            if(StringUtils.isNotEmpty(listBarcode) && listBarcode.size()>0){
                                for (WmsInnerMaterialBarcode wmsInnerMaterialBarcode : listBarcode) {
                                    Example exampleDetBarcode = new Example(WmsInnerStockOrderDetBarcode.class);
                                    Example.Criteria criteriaDetBarcode = exampleDetBarcode.createCriteria();
                                    criteriaDetBarcode.andEqualTo("stockOrderDetId",stockOrderDetId);
                                    criteriaDetBarcode.andEqualTo("materialBarcodeId",wmsInnerMaterialBarcode.getMaterialBarcodeId());
                                    WmsInnerStockOrderDetBarcode detBarcode=wmsInnerStockOrderDetBarcodeMapper.selectOneByExample(exampleDetBarcode);
                                    if(StringUtils.isNotEmpty(detBarcode)){
                                        if(!detBarcodeIDList.contains(detBarcode.getStockOrderDetBarcodeId())) {
                                            detBarcodeIDList.add(detBarcode.getStockOrderDetBarcodeId());
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //箱号最少包装单位数量
                    List<WmsInnerStockOrderDetBarcodeDto> barcodeDetList = detBarcodeDtos.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode())?"":u.getBarcode())!="")).collect(Collectors.toList());
                    BigDecimal totalMaterialQty=barcodeDetList.stream().map(WmsInnerStockOrderDetBarcodeDto::getMaterialQty).reduce(BigDecimal.ZERO,BigDecimal::add);
                    totalQty=totalQty.add(totalMaterialQty);

                }
                else{
                    //扫描的是已出库或已取消条码
                    if(StringUtils.isNotEmpty(result.getOption1()) && "out".equals(result.getOption1())){
                        totalQty=totalQty.add(result.getMaterialQty());
                        //盘点条码明细
                        WmsInnerStockOrderDetBarcode detBarcode=new WmsInnerStockOrderDetBarcode();
                        detBarcode.setStockOrderDetBarcodeId(null);
                        detBarcode.setStockOrderDetId(stockOrderDetId);
                        detBarcode.setMaterialBarcodeId(result.getMaterialBarcodeId());
                        detBarcode.setScanStatus((byte)3);
                        detBarcode.setStockResult((byte)3);//盘盈
                        detBarcode.setCreateUserId(sysUser.getUserId());
                        detBarcode.setCreateTime(new Date());
                        orderDetBarcodeList.add(detBarcode);
                    }
                }
            }
            else if(barcodeType==(byte)4) {
                //栈板码
                searchOrderDetBarcode.setStockOrderDetId(stockOrderDetId);
                searchOrderDetBarcode.setPalletCode(barcode);
                searchOrderDetBarcode.setBarcode(null);
                searchOrderDetBarcode.setColorBoxCode(null);
                searchOrderDetBarcode.setCartonCode(null);
                detBarcodeDtos=wmsInnerStockOrderDetBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchOrderDetBarcode));
                if(detBarcodeDtos.size()>0){
                    for (WmsInnerStockOrderDetBarcodeDto detBarcodeDto : detBarcodeDtos) {
                        /*WmsInnerStockOrderDetBarcode orderDetBarcode=new WmsInnerStockOrderDetBarcode();
                        orderDetBarcode.setStockOrderDetBarcodeId(detBarcodeDto.getStockOrderDetBarcodeId());
                        orderDetBarcode.setScanStatus((byte)3);
                        orderDetBarcode.setStockResult((byte)2);
                        orderDetBarcode.setModifiedUserId(sysUser.getUserId());
                        orderDetBarcode.setModifiedTime(new Date());
                        num+=wmsInnerStockOrderDetBarcodeMapper.updateByPrimaryKeySelective(orderDetBarcode);*/

                        if(!detBarcodeIDList.contains(detBarcodeDto.getStockOrderDetBarcodeId())) {
                            detBarcodeIDList.add(detBarcodeDto.getStockOrderDetBarcodeId());
                        }

                        if(StringUtils.isNotEmpty(detBarcodeDto.getColorBoxCode())
                                || StringUtils.isNotEmpty(detBarcodeDto.getCartonCode())){
                            Example exampleBarcode = new Example(WmsInnerMaterialBarcode.class);
                            Example.Criteria criteriaBarcode = exampleBarcode.createCriteria();
                            if(StringUtils.isNotEmpty(detBarcodeDto.getColorBoxCode())){
                                criteriaBarcode.orEqualTo("colorBoxCode",detBarcodeDto.getColorBoxCode());
                            }
                            if(StringUtils.isNotEmpty(detBarcodeDto.getCartonCode())){
                                criteriaBarcode.orEqualTo("cartonCode",detBarcodeDto.getCartonCode());
                            }
                            criteriaBarcode.andIsNull("barcode");
                            List<WmsInnerMaterialBarcode> listBarcode=wmsInnerMaterialBarcodeMapper.selectByExample(exampleBarcode);
                            if(StringUtils.isNotEmpty(listBarcode) && listBarcode.size()>0){
                                for (WmsInnerMaterialBarcode wmsInnerMaterialBarcode : listBarcode) {
                                    Example exampleDetBarcode = new Example(WmsInnerStockOrderDetBarcode.class);
                                    Example.Criteria criteriaDetBarcode = exampleDetBarcode.createCriteria();
                                    criteriaDetBarcode.andEqualTo("stockOrderDetId",stockOrderDetId);
                                    criteriaDetBarcode.andEqualTo("materialBarcodeId",wmsInnerMaterialBarcode.getMaterialBarcodeId());
                                    WmsInnerStockOrderDetBarcode detBarcode=wmsInnerStockOrderDetBarcodeMapper.selectOneByExample(exampleDetBarcode);
                                    if(StringUtils.isNotEmpty(detBarcode)){
                                        if(!detBarcodeIDList.contains(detBarcode.getStockOrderDetBarcodeId())) {
                                            detBarcodeIDList.add(detBarcode.getStockOrderDetBarcodeId());
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //栈板最少包装单位数量
                    List<WmsInnerStockOrderDetBarcodeDto> barcodeDetList = detBarcodeDtos.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode())?"":u.getBarcode())!="")).collect(Collectors.toList());
                    BigDecimal totalMaterialQty=barcodeDetList.stream().map(WmsInnerStockOrderDetBarcodeDto::getMaterialQty).reduce(BigDecimal.ZERO,BigDecimal::add);
                    totalQty=totalQty.add(totalMaterialQty);
                }
                else{
                    //扫描的是已出库或已取消条码
                    if(StringUtils.isNotEmpty(result.getOption1()) && "out".equals(result.getOption1())){
                        totalQty=totalQty.add(result.getMaterialQty());
                        //盘点条码明细
                        WmsInnerStockOrderDetBarcode detBarcode=new WmsInnerStockOrderDetBarcode();
                        detBarcode.setStockOrderDetBarcodeId(null);
                        detBarcode.setStockOrderDetId(stockOrderDetId);
                        detBarcode.setMaterialBarcodeId(result.getMaterialBarcodeId());
                        detBarcode.setScanStatus((byte)3);
                        detBarcode.setStockResult((byte)3);//盘盈
                        detBarcode.setCreateUserId(sysUser.getUserId());
                        detBarcode.setCreateTime(new Date());
                        orderDetBarcodeList.add(detBarcode);
                    }
                }
            }
            else {
                //非系统条码新增到条码表
                Long materialBarcodeId=null;
                Example exampleBarcode = new Example(WmsInnerMaterialBarcode.class);
                Example.Criteria criteriaBarcode = exampleBarcode.createCriteria();
                criteriaBarcode.andEqualTo("barcode", item.getBarcode());
                WmsInnerMaterialBarcode barcodeExist=wmsInnerMaterialBarcodeMapper.selectOneByExample(exampleBarcode);
                if(StringUtils.isNotEmpty(barcodeExist)){
                    materialBarcodeId=barcodeExist.getMaterialBarcodeId();
                }
                if(StringUtils.isEmpty(materialBarcodeId)) {
                    WmsInnerMaterialBarcode wmsInnerMaterialBarcode = new WmsInnerMaterialBarcode();
                    wmsInnerMaterialBarcode.setMaterialId(materialId);
                    wmsInnerMaterialBarcode.setBarcode(item.getBarcode());
                    wmsInnerMaterialBarcode.setBatchCode(item.getBatchCode());
                    wmsInnerMaterialBarcode.setMaterialQty(item.getMaterialQty());
                    wmsInnerMaterialBarcode.setIfSysBarcode((byte) 0);
                    Date productionTime = null;
                    try {
                        if (StringUtils.isNotEmpty(item.getProductionTime())) {
                            productionTime = sdf.parse(item.getProductionTime());
                        }

                    } catch (Exception ex) {
                        throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "参数生产日期转换为时间类型异常");
                    }

                    wmsInnerMaterialBarcode.setProductionTime(productionTime);

                    //产生类型(1-供应商条码 2-自己打印 3-生产条码)
                    wmsInnerMaterialBarcode.setCreateType((byte) 1);
                    //条码状态(1-已生成 2-已打印 3-已收货 4-已质检 5-已上架 6-已出库)
                    wmsInnerMaterialBarcode.setBarcodeStatus((byte) 5);
                    wmsInnerMaterialBarcode.setOrgId(sysUser.getOrganizationId());
                    wmsInnerMaterialBarcode.setCreateTime(new Date());
                    wmsInnerMaterialBarcode.setCreateUserId(sysUser.getUserId());
                    wmsInnerMaterialBarcode.setBarcodeType((byte) 1);
                    //新增到来料条码表
                    num += wmsInnerMaterialBarcodeMapper.insertUseGeneratedKeys(wmsInnerMaterialBarcode);
                    //设置来料条码ID
                    materialBarcodeId = wmsInnerMaterialBarcode.getMaterialBarcodeId();
                }

                //条码关系
                WmsInnerMaterialBarcodeReOrder wmsInnerMaterialBarcodeReOrder=new WmsInnerMaterialBarcodeReOrder();
                wmsInnerMaterialBarcodeReOrder.setOrderTypeCode("INNER-TSO");//类型 盘点
                wmsInnerMaterialBarcodeReOrder.setOrderCode(wmsInnerStockOrder.getPlanStockOrderCode());
                wmsInnerMaterialBarcodeReOrder.setOrderId(wmsInnerStockOrder.getStockOrderId());
                //来料条码ID
                wmsInnerMaterialBarcodeReOrder.setMaterialBarcodeId(materialBarcodeId);
                wmsInnerMaterialBarcodeReOrder.setScanStatus((byte)3);
                wmsInnerMaterialBarcodeReOrder.setOrgId(sysUser.getOrganizationId());
                wmsInnerMaterialBarcodeReOrder.setCreateTime(new Date());
                wmsInnerMaterialBarcodeReOrder.setCreateUserId(sysUser.getUserId());
                materialBarcodeReOrderList.add(wmsInnerMaterialBarcodeReOrder);

                //库存明细条码
                /*WmsInnerInventoryDet inventoryDet=new WmsInnerInventoryDet();
                inventoryDet.setStorageId(storageId);
                inventoryDet.setMaterialBarcodeId(materialBarcodeId);
                inventoryDet.setReceivingDate(new Date());//入库日期
                inventoryDet.setAsnCode(wmsInnerStockOrder.getPlanStockOrderCode());//盘点单号
                inventoryDet.setIfStockLock((byte)0);
                inventoryDet.setInventoryStatusId(wmsInnerStockOrderDet.getInventoryStatusId());
                inventoryDet.setBarcodeStatus((byte)3);//在库
                inventoryDet.setCreateUserId(sysUser.getUserId());
                inventoryDet.setCreateTime(new Date());
                wmsInnerInventoryDets.add(inventoryDet);*/

                //盘点条码明细
                WmsInnerStockOrderDetBarcode detBarcode=new WmsInnerStockOrderDetBarcode();
                detBarcode.setStockOrderDetBarcodeId(null);
                detBarcode.setStockOrderDetId(stockOrderDetId);
                detBarcode.setMaterialBarcodeId(materialBarcodeId);
                detBarcode.setScanStatus((byte)3);
                detBarcode.setStockResult((byte)3);//盘盈
                detBarcode.setCreateUserId(sysUser.getUserId());
                detBarcode.setCreateTime(new Date());
                orderDetBarcodeList.add(detBarcode);

                totalQty=totalQty.add(item.getMaterialQty());
            }
        }
        //更新明细为登记状态 盘点数量
        if(StringUtils.isEmpty(wmsInnerStockOrderDet.getStockQty()))
            wmsInnerStockOrderDet.setStockQty(BigDecimal.ZERO);
        if(StringUtils.isEmpty(wmsInnerStockOrderDet.getOriginalQty()))
            wmsInnerStockOrderDet.setOriginalQty(BigDecimal.ZERO);

        //wmsInnerStockOrderDet.setStockQty(wmsInnerStockOrderDet.getStockQty().add(totalQty));
        wmsInnerStockOrderDet.setStockQty(totalQty);
        wmsInnerStockOrderDet.setVarianceQty(wmsInnerStockOrderDet.getStockQty().subtract(wmsInnerStockOrderDet.getOriginalQty()));
        wmsInnerStockOrderDet.setIfRegister((byte)1);
        wmsInnerStockOrderDet.setStockUserId(sysUser.getUserId());
        wmsInnerStockOrderDet.setWorkerId(sysUser.getUserId());
        wmsInnerStockOrderDet.setModifiedTime(new Date());
        wmsInnerStockOrderDet.setModifiedUserId(sysUser.getUserId());
        num += wmsInventoryVerificationDetMapper.updateByPrimaryKeySelective(wmsInnerStockOrderDet);

        //更新表头状态为作业中
        wmsInnerStockOrder.setOrderStatus((byte)3);
        wmsInnerStockOrder.setModifiedUserId(sysUser.getUserId());
        wmsInnerStockOrder.setModifiedTime(new Date());
        num+=wmsInventoryVerificationMapper.updateByPrimaryKeySelective(wmsInnerStockOrder);

        //非系统条码增加到关系表
        if(materialBarcodeReOrderList.size()>0){
            num+=wmsInnerMaterialBarcodeReOrderService.batchAdd(materialBarcodeReOrderList);
        }
        //增加条码库存明细
        /*if(wmsInnerInventoryDets.size()>0){
            num+=WmsInnerInventoryUtil.updateInventoryDet(wmsInnerInventoryDets);
        }*/
        //增加盘点条码明细
        if(orderDetBarcodeList.size()>0){
            num+=wmsInnerStockOrderDetBarcodeMapper.insertList(orderDetBarcodeList);
        }

        //条码状态盘亏
        List<Long> tempList=new ArrayList<>();
        if(detBarcodeAllIDList.size()>0){
            for (WmsInnerStockOrderDetBarcode detBarcode : detBarcodeAllIDList) {
                tempList.add(detBarcode.getStockOrderDetBarcodeId());
            }
            if(StringUtils.isNotEmpty(detBarcodeIDList) && detBarcodeIDList.size()>0)
                tempList.removeAll(detBarcodeIDList);
        }
        //盘亏
        if(tempList.size()>0){
            for (Long aLong : tempList) {
                WmsInnerStockOrderDetBarcode upStockOrderDetBarcode=new WmsInnerStockOrderDetBarcode();
                upStockOrderDetBarcode.setStockOrderDetBarcodeId(aLong);
                upStockOrderDetBarcode.setStockResult((byte)4);
                upStockOrderDetBarcode.setModifiedUserId(sysUser.getUserId());
                upStockOrderDetBarcode.setModifiedTime(new Date());
                num+=wmsInnerStockOrderDetBarcodeMapper.updateByPrimaryKeySelective(upStockOrderDetBarcode);
            }
        }
        //已盘点
        if(detBarcodeIDList.size()>0){
            for (Long aLong : detBarcodeIDList) {
                WmsInnerStockOrderDetBarcode upStockOrderDetBarcode=new WmsInnerStockOrderDetBarcode();
                upStockOrderDetBarcode.setStockOrderDetBarcodeId(aLong);
                upStockOrderDetBarcode.setScanStatus((byte)3);
                upStockOrderDetBarcode.setStockResult((byte)2);
                upStockOrderDetBarcode.setModifiedUserId(sysUser.getUserId());
                upStockOrderDetBarcode.setModifiedTime(new Date());
                num+=wmsInnerStockOrderDetBarcodeMapper.updateByPrimaryKeySelective(upStockOrderDetBarcode);
            }
        }

        return num;
    }

    /**
     * PDA盘点增补
     * @param
     * @return
     */
    @Override
    public int addInnerStockDet(List<AddInnerStockDetDto> addDetList) {
        int num=0;
        if(addDetList.size()<=0){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"未提交任何增补数据");
        }
        BigDecimal totalQty=addDetList.stream().map(AddInnerStockDetDto::getMaterialQty).reduce(BigDecimal.ZERO,BigDecimal::add);
        if(totalQty.compareTo(new BigDecimal(0))==0){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"条码总数为零");
        }

        SysUser sysUser=currentUser();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<WmsInnerStockOrderDet> detList=new ArrayList<>();

        //盘点主表ID
        Long stockOrderId=addDetList.get(0).getStockOrderId();
        WmsInnerStockOrder wmsInnerStockOrder=wmsInventoryVerificationMapper.selectByPrimaryKey(stockOrderId);
        if(StringUtils.isEmpty(wmsInnerStockOrder)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"找不到相应的盘点单数据");
        }
        for (AddInnerStockDetDto item : addDetList) {
            Long materialId=item.getMaterialId();
            String batchCode=item.getBatchCode();
            Long storageId=item.getStorageId();
            Date productionTime=null;
            try {
                productionTime=sdf.parse(item.getProductionTime());
            }catch (Exception ex){
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"参数生产日期转换为时间类型异常");
            }

            Example example = new Example(WmsInnerStockOrderDet.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("materialId",materialId);
            criteria.andEqualTo("batchCode",batchCode);
            criteria.andEqualTo("storageId",storageId);
            criteria.andEqualTo("productionTime",productionTime);
            List<WmsInnerStockOrderDet> stockOrderDets=wmsInventoryVerificationDetMapper.selectByExample(example);
            if(stockOrderDets.size()>0){
                WmsInnerStockOrderDet upStockOrderDet=new WmsInnerStockOrderDet();
                upStockOrderDet.setStockOrderDetId(stockOrderDets.get(0).getStockOrderDetId());
                upStockOrderDet.setStockQty(stockOrderDets.get(0).getStockQty().add(totalQty));
                upStockOrderDet.setVarianceQty(upStockOrderDet.getStockQty().subtract(stockOrderDets.get(0).getOriginalQty()));
                upStockOrderDet.setIfRegister((byte)1);
                num+=wmsInventoryVerificationDetMapper.updateByPrimaryKeySelective(upStockOrderDet);
            }
            else{
                WmsInnerStockOrderDet newStockOrderDet=new WmsInnerStockOrderDet();
                newStockOrderDet.setSourceDetId(null);
                newStockOrderDet.setStockOrderId(stockOrderId);
                newStockOrderDet.setMaterialId(materialId);
                newStockOrderDet.setStorageId(storageId);
                newStockOrderDet.setBatchCode(batchCode);
                newStockOrderDet.setProductionTime(productionTime);
                newStockOrderDet.setOriginalQty(new BigDecimal(0));
                newStockOrderDet.setStockQty(totalQty);
                newStockOrderDet.setVarianceQty(totalQty);
                newStockOrderDet.setIfRegister((byte)1);
                newStockOrderDet.setCreateUserId(sysUser.getUserId());
                newStockOrderDet.setStockUserId(sysUser.getUserId());
                newStockOrderDet.setCreateTime(new Date());
                newStockOrderDet.setOption1("new");
                detList.add(newStockOrderDet);
            }
        }
        if(detList.size()>0){
            num+=wmsInventoryVerificationDetMapper.insertList(detList);
        }

        return num;
    }

    /**
     * PDA扫码返回数量
     * @param barcode
     * @return
     */
    @Override
    public BarcodeResultDto scanBarcode(Long stockOrderDetId,String barcode) {
        BarcodeResultDto barcodeResultDto=new BarcodeResultDto();
        if(StringUtils.isEmpty(barcode)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"条码不能为空");
        }

        //条码判断
        if(StringUtils.isNotEmpty(stockOrderDetId)) {
            WmsInnerStockOrderDet stockOrderDet = wmsInventoryVerificationDetMapper.selectByPrimaryKey(stockOrderDetId);
            if (StringUtils.isEmpty(stockOrderDet)) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "未找到相应的盘点明细信息");
            }

            WmsInnerStockOrder stockOrder = wmsInventoryVerificationMapper.selectByPrimaryKey(stockOrderDet.getStockOrderId());
            if (StringUtils.isEmpty(stockOrder)) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "未找到相应的盘点单信息");
            }

            List<WmsInnerStockOrderDetBarcodeDto> detBarcodeDtos = new ArrayList<>();
            SearchWmsInnerStockOrderDetBarcode searchOrderDetBarcode = new SearchWmsInnerStockOrderDetBarcode();
            searchOrderDetBarcode.setQueryAll("true");
            searchOrderDetBarcode.setStockOrderDetId(stockOrderDetId);
            searchOrderDetBarcode.setBarcode(barcode);
            detBarcodeDtos = wmsInnerStockOrderDetBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchOrderDetBarcode));
            if (detBarcodeDtos.size() > 0) {
                if (detBarcodeDtos.get(0).getScanStatus() == (byte) 3) {
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "条码已扫描 请勿重复扫码-->" + barcode);
                }
                barcodeResultDto.setBarcodeType((byte) 1);
                barcodeResultDto.setMaterialQty(detBarcodeDtos.get(0).getMaterialQty());
                barcodeResultDto.setBarcode(barcode);
                barcodeResultDto.setMaterialId(detBarcodeDtos.get(0).getMaterialId());
                barcodeResultDto.setMaterialBarcodeId(detBarcodeDtos.get(0).getMaterialBarcodeId());
            } else {
                searchOrderDetBarcode.setBarcode(null);
                searchOrderDetBarcode.setColorBoxCode(barcode);
                detBarcodeDtos = wmsInnerStockOrderDetBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchOrderDetBarcode));
                if (detBarcodeDtos.size() > 0) {
                    List<WmsInnerStockOrderDetBarcodeDto> barcodeListOne = detBarcodeDtos.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode()) ? "" : u.getBarcode()) == "")).collect(Collectors.toList());
                    if (StringUtils.isEmpty(barcodeListOne) || barcodeListOne.size() <= 0) {
                        throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "条码明细中不存在彩盒码-->" + barcode);
                    }
                    if (barcodeListOne.get(0).getScanStatus() == (byte) 3) {
                        throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "条码已扫描 请勿重复扫码-->" + barcode);
                    }
                    barcodeResultDto.setBarcodeType((byte) 2);
                    barcodeResultDto.setMaterialQty(detBarcodeDtos.get(0).getMaterialQty());
                    barcodeResultDto.setBarcode(barcode);
                    barcodeResultDto.setMaterialId(detBarcodeDtos.get(0).getMaterialId());
                    barcodeResultDto.setMaterialBarcodeId(detBarcodeDtos.get(0).getMaterialBarcodeId());
                } else {
                    searchOrderDetBarcode.setBarcode(null);
                    searchOrderDetBarcode.setColorBoxCode(null);
                    searchOrderDetBarcode.setCartonCode(barcode);
                    detBarcodeDtos = wmsInnerStockOrderDetBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchOrderDetBarcode));
                    if (detBarcodeDtos.size() > 0) {
                        List<WmsInnerStockOrderDetBarcodeDto> barcodeListOne = detBarcodeDtos.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode()) ? "" : u.getBarcode()) == "")).collect(Collectors.toList());
                        if (StringUtils.isEmpty(barcodeListOne) || barcodeListOne.size() <= 0) {
                            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "条码明细中不存在箱码-->" + barcode);
                        }
                        if (barcodeListOne.get(0).getScanStatus() == (byte) 3) {
                            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "条码已扫描 请勿重复扫码-->" + barcode);
                        }
                        List<WmsInnerStockOrderDetBarcodeDto> barcodeList = detBarcodeDtos.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode()) ? "" : u.getBarcode()) != "")).collect(Collectors.toList());
                        BigDecimal totalQty = barcodeList.stream().map(WmsInnerStockOrderDetBarcodeDto::getMaterialQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                        barcodeResultDto.setBarcodeType((byte) 3);
                        barcodeResultDto.setMaterialQty(totalQty);
                        barcodeResultDto.setBarcode(barcode);
                        barcodeResultDto.setMaterialId(detBarcodeDtos.get(0).getMaterialId());
                        barcodeResultDto.setMaterialBarcodeId(detBarcodeDtos.get(0).getMaterialBarcodeId());
                    } else {
                        searchOrderDetBarcode.setBarcode(null);
                        searchOrderDetBarcode.setColorBoxCode(null);
                        searchOrderDetBarcode.setCartonCode(null);
                        searchOrderDetBarcode.setPalletCode(barcode);
                        detBarcodeDtos = wmsInnerStockOrderDetBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchOrderDetBarcode));
                        if (detBarcodeDtos.size() > 0) {
                            List<WmsInnerStockOrderDetBarcodeDto> barcodeListOne = detBarcodeDtos.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode()) ? "" : u.getBarcode()) == "")).collect(Collectors.toList());
                            if (StringUtils.isEmpty(barcodeListOne) || barcodeListOne.size() <= 0) {
                                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "条码明细中不存在栈板码-->" + barcode);
                            }
                            if (barcodeListOne.get(0).getScanStatus() == (byte) 3) {
                                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "条码已扫描 请勿重复扫码-->" + barcode);
                            }
                            List<WmsInnerStockOrderDetBarcodeDto> barcodeList = detBarcodeDtos.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode()) ? "" : u.getBarcode()) != "")).collect(Collectors.toList());
                            BigDecimal totalQty = barcodeList.stream().map(WmsInnerStockOrderDetBarcodeDto::getMaterialQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                            barcodeResultDto.setBarcodeType((byte) 4);
                            barcodeResultDto.setMaterialQty(totalQty);
                            barcodeResultDto.setBarcode(barcode);
                            barcodeResultDto.setMaterialId(detBarcodeDtos.get(0).getMaterialId());
                            barcodeResultDto.setMaterialBarcodeId(detBarcodeDtos.get(0).getMaterialBarcodeId());
                        }
                    }
                }
            }

            if (StringUtils.isEmpty(barcodeResultDto.getBarcodeType())) {
                searchOrderDetBarcode.setQueryAll("true");
                searchOrderDetBarcode.setStockOrderId(stockOrderDet.getStockOrderId());
                searchOrderDetBarcode.setBarcode(barcode);
                searchOrderDetBarcode.setColorBoxCode(null);
                searchOrderDetBarcode.setCartonCode(null);
                searchOrderDetBarcode.setStockOrderDetId(null);
                detBarcodeDtos = wmsInnerStockOrderDetBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchOrderDetBarcode));
                if(StringUtils.isNotEmpty(detBarcodeDtos) && detBarcodeDtos.size()>0){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "条码已扫描 请勿重复扫码-->" + barcode);
                }

                //扫描已取消或已出库条码判断
                List<WmsInnerInventoryDetDto> inventoryDetDtoList=new ArrayList<>();
                SearchWmsInnerInventoryDet searchinventoryDet=new SearchWmsInnerInventoryDet();
                searchinventoryDet.setBarcode(barcode);
                searchinventoryDet.setNotEqualMark(0);
                searchinventoryDet.setMaterialId(stockOrderDet.getMaterialId());
                searchinventoryDet.setOrgId(stockOrderDet.getOrgId());

                inventoryDetDtoList=wmsInnerInventoryDetService.findList(ControllerUtil.dynamicConditionByEntity(searchinventoryDet));
                if(inventoryDetDtoList.size()>0){
                    if(inventoryDetDtoList.get(0).getBarcodeStatus()==(byte)1){
                        throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"不能扫描在库条码-->"+barcode);
                    }
                    barcodeResultDto.setBarcodeType((byte) 1);
                    barcodeResultDto.setMaterialQty(inventoryDetDtoList.get(0).getMaterialQty());
                    barcodeResultDto.setBarcode(barcode);
                    barcodeResultDto.setMaterialId(inventoryDetDtoList.get(0).getMaterialId());
                    barcodeResultDto.setMaterialBarcodeId(inventoryDetDtoList.get(0).getMaterialBarcodeId());
                }
                else {
                    searchinventoryDet.setBarcode(null);
                    searchinventoryDet.setColorBoxCode(barcode);
                    inventoryDetDtoList=wmsInnerInventoryDetService.findList(ControllerUtil.dynamicConditionByEntity(searchinventoryDet));
                    if(inventoryDetDtoList.size()>0){
                        List<WmsInnerInventoryDetDto> barcodeListOne = inventoryDetDtoList.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode()) ? "" : u.getBarcode()) == "")).collect(Collectors.toList());
                        if (StringUtils.isEmpty(barcodeListOne) || barcodeListOne.size() <= 0) {
                            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "条码明细中不存在彩盒码-->" + barcode);
                        }
                        if (barcodeListOne.get(0).getBarcodeStatus() == (byte) 1) {
                            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "不能扫描在库条码-->" + barcode);
                        }
                        barcodeResultDto.setBarcodeType((byte) 2);
                        barcodeResultDto.setMaterialQty(inventoryDetDtoList.get(0).getMaterialQty());
                        barcodeResultDto.setBarcode(barcode);
                        barcodeResultDto.setMaterialId(inventoryDetDtoList.get(0).getMaterialId());
                        barcodeResultDto.setMaterialBarcodeId(inventoryDetDtoList.get(0).getMaterialBarcodeId());
                    }
                    else{
                        searchinventoryDet.setBarcode(null);
                        searchinventoryDet.setColorBoxCode(null);
                        searchinventoryDet.setCartonCode(barcode);
                        inventoryDetDtoList=wmsInnerInventoryDetService.findList(ControllerUtil.dynamicConditionByEntity(searchinventoryDet));
                        if(inventoryDetDtoList.size()>0){
                            List<WmsInnerInventoryDetDto> barcodeListOne = inventoryDetDtoList.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode()) ? "" : u.getBarcode()) == "")).collect(Collectors.toList());
                            if (StringUtils.isEmpty(barcodeListOne) || barcodeListOne.size() <= 0) {
                                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "条码明细中不存在箱码-->" + barcode);
                            }
                            if (barcodeListOne.get(0).getBarcodeStatus() == (byte) 1) {
                                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "不能扫描在库条码-->" + barcode);
                            }
                            List<WmsInnerInventoryDetDto> barcodeList = inventoryDetDtoList.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode()) ? "" : u.getBarcode()) != "")).collect(Collectors.toList());
                            BigDecimal totalQty = barcodeList.stream().map(WmsInnerInventoryDetDto::getMaterialQty).reduce(BigDecimal.ZERO, BigDecimal::add);

                            barcodeResultDto.setBarcodeType((byte) 3);
                            barcodeResultDto.setMaterialQty(totalQty);
                            barcodeResultDto.setBarcode(barcode);
                            barcodeResultDto.setMaterialId(barcodeListOne.get(0).getMaterialId());
                            barcodeResultDto.setMaterialBarcodeId(barcodeListOne.get(0).getMaterialBarcodeId());
                        }
                        else {
                            searchinventoryDet.setBarcode(null);
                            searchinventoryDet.setColorBoxCode(null);
                            searchinventoryDet.setCartonCode(null);
                            searchinventoryDet.setPalletCode(barcode);
                            inventoryDetDtoList=wmsInnerInventoryDetService.findList(ControllerUtil.dynamicConditionByEntity(searchinventoryDet));
                            if(inventoryDetDtoList.size()>0){
                                List<WmsInnerInventoryDetDto> barcodeListOne = inventoryDetDtoList.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode()) ? "" : u.getBarcode()) == "")).collect(Collectors.toList());
                                if (StringUtils.isEmpty(barcodeListOne) || barcodeListOne.size() <= 0) {
                                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "条码明细中不存在箱码-->" + barcode);
                                }
                                if (barcodeListOne.get(0).getBarcodeStatus() == (byte) 1) {
                                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "不能扫描在库条码-->" + barcode);
                                }
                                List<WmsInnerInventoryDetDto> barcodeList = inventoryDetDtoList.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode()) ? "" : u.getBarcode()) != "")).collect(Collectors.toList());
                                BigDecimal totalQty = barcodeList.stream().map(WmsInnerInventoryDetDto::getMaterialQty).reduce(BigDecimal.ZERO, BigDecimal::add);

                                barcodeResultDto.setBarcodeType((byte) 4);
                                barcodeResultDto.setMaterialQty(totalQty);
                                barcodeResultDto.setBarcode(barcode);
                                barcodeResultDto.setMaterialId(barcodeListOne.get(0).getMaterialId());
                                barcodeResultDto.setMaterialBarcodeId(barcodeListOne.get(0).getMaterialBarcodeId());
                            }
                        }
                    }
                }
                //是否扫的是其他的系统条码
                if(StringUtils.isEmpty(barcodeResultDto.getBarcodeType())) {
                    BarcodeResultDto resultDto1 = InBarcodeUtil.scanJugeBarcode(barcode);
                    if (resultDto1.getBarcodeType() != (byte) 5) {
                        if(StringUtils.isNotEmpty(resultDto1.getMaterialBarcodeId())) {
                            SearchWmsInnerInventoryDet searchinventoryDet1 = new SearchWmsInnerInventoryDet();
                            searchinventoryDet1.setMaterialBarcodeId(resultDto1.getMaterialBarcodeId());
                            searchinventoryDet1.setOrgId(stockOrderDet.getOrgId());
                            inventoryDetDtoList = wmsInnerInventoryDetService.findList(ControllerUtil.dynamicConditionByEntity(searchinventoryDet1));
                            if(StringUtils.isNotEmpty(inventoryDetDtoList) && inventoryDetDtoList.size()>0){
                                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "扫描的是系统其他条码 请重新扫码-->" + barcode);
                            }
                        }

                    }

                    barcodeResultDto.setBarcodeType((byte) 5);
                }
            }
        }

        return barcodeResultDto;
    }

    @Override
    public BarcodeResultDto scanBarcodeNewDet(Long materialId, String barcode) {
        //增补明细 条码判断
        BarcodeResultDto barcodeResultDto=new BarcodeResultDto();
        SysUser sysUser=currentUser();
        if(StringUtils.isEmpty(barcode)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"条码不能为空");
        }
        List<WmsInnerInventoryDetDto> inventoryDetDtoList=new ArrayList<>();

        SearchWmsInnerInventoryDet searchinventoryDet=new SearchWmsInnerInventoryDet();
        searchinventoryDet.setBarcode(barcode);
        searchinventoryDet.setNotEqualMark(0);
        searchinventoryDet.setMaterialId(materialId);
        searchinventoryDet.setOrgId(sysUser.getOrganizationId());

        inventoryDetDtoList=wmsInnerInventoryDetService.findList(ControllerUtil.dynamicConditionByEntity(searchinventoryDet));
        if(inventoryDetDtoList.size()>0){
            if(inventoryDetDtoList.get(0).getBarcodeStatus()==(byte)1){
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"盘点增补明细不能扫描在库条码-->"+barcode);
            }

            barcodeResultDto.setBarcodeType((byte) 1);
            barcodeResultDto.setMaterialQty(inventoryDetDtoList.get(0).getMaterialQty());
            barcodeResultDto.setBarcode(barcode);
            barcodeResultDto.setMaterialId(materialId);
            barcodeResultDto.setMaterialBarcodeId(inventoryDetDtoList.get(0).getMaterialBarcodeId());
        }
        else {
            searchinventoryDet.setBarcode(null);
            searchinventoryDet.setColorBoxCode(barcode);
            inventoryDetDtoList=wmsInnerInventoryDetService.findList(ControllerUtil.dynamicConditionByEntity(searchinventoryDet));
            if(inventoryDetDtoList.size()>0){
                List<WmsInnerInventoryDetDto> barcodeListOne = inventoryDetDtoList.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode()) ? "" : u.getBarcode()) == "")).collect(Collectors.toList());
                if (StringUtils.isEmpty(barcodeListOne) || barcodeListOne.size() <= 0) {
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "条码明细中不存在彩盒码-->" + barcode);
                }
                if (barcodeListOne.get(0).getBarcodeStatus() == (byte) 1) {
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "盘点增补明细不能扫描在库条码-->" + barcode);
                }

                barcodeResultDto.setBarcodeType((byte) 2);
                barcodeResultDto.setMaterialQty(inventoryDetDtoList.get(0).getMaterialQty());
                barcodeResultDto.setBarcode(barcode);
                barcodeResultDto.setMaterialId(materialId);
                barcodeResultDto.setMaterialBarcodeId(barcodeListOne.get(0).getMaterialBarcodeId());
            }
            else{
                searchinventoryDet.setBarcode(null);
                searchinventoryDet.setColorBoxCode(null);
                searchinventoryDet.setCartonCode(barcode);
                inventoryDetDtoList=wmsInnerInventoryDetService.findList(ControllerUtil.dynamicConditionByEntity(searchinventoryDet));
                if(inventoryDetDtoList.size()>0){
                    List<WmsInnerInventoryDetDto> barcodeListOne = inventoryDetDtoList.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode()) ? "" : u.getBarcode()) == "")).collect(Collectors.toList());
                    if (StringUtils.isEmpty(barcodeListOne) || barcodeListOne.size() <= 0) {
                        throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "条码明细中不存在箱码-->" + barcode);
                    }
                    if (barcodeListOne.get(0).getBarcodeStatus() == (byte) 1) {
                        throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "盘点增补明细不能扫描在库条码-->" + barcode);
                    }
                    List<WmsInnerInventoryDetDto> barcodeList = inventoryDetDtoList.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode()) ? "" : u.getBarcode()) != "")).collect(Collectors.toList());
                    BigDecimal totalQty = barcodeList.stream().map(WmsInnerInventoryDetDto::getMaterialQty).reduce(BigDecimal.ZERO, BigDecimal::add);

                    barcodeResultDto.setBarcodeType((byte) 3);
                    barcodeResultDto.setMaterialQty(totalQty);
                    barcodeResultDto.setBarcode(barcode);
                    barcodeResultDto.setMaterialId(materialId);
                    barcodeResultDto.setMaterialBarcodeId(barcodeListOne.get(0).getMaterialBarcodeId());
                }
                else {
                    searchinventoryDet.setBarcode(null);
                    searchinventoryDet.setColorBoxCode(null);
                    searchinventoryDet.setCartonCode(null);
                    searchinventoryDet.setPalletCode(barcode);
                    inventoryDetDtoList=wmsInnerInventoryDetService.findList(ControllerUtil.dynamicConditionByEntity(searchinventoryDet));
                    if(inventoryDetDtoList.size()>0){
                        List<WmsInnerInventoryDetDto> barcodeListOne = inventoryDetDtoList.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode()) ? "" : u.getBarcode()) == "")).collect(Collectors.toList());
                        if (StringUtils.isEmpty(barcodeListOne) || barcodeListOne.size() <= 0) {
                            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "条码明细中不存在箱码-->" + barcode);
                        }
                        if (barcodeListOne.get(0).getBarcodeStatus() == (byte) 1) {
                            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "盘点增补明细不能扫描在库条码-->" + barcode);
                        }
                        List<WmsInnerInventoryDetDto> barcodeList = inventoryDetDtoList.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode()) ? "" : u.getBarcode()) != "")).collect(Collectors.toList());
                        BigDecimal totalQty = barcodeList.stream().map(WmsInnerInventoryDetDto::getMaterialQty).reduce(BigDecimal.ZERO, BigDecimal::add);

                        barcodeResultDto.setBarcodeType((byte) 4);
                        barcodeResultDto.setMaterialQty(totalQty);
                        barcodeResultDto.setBarcode(barcode);
                        barcodeResultDto.setMaterialId(materialId);
                        barcodeResultDto.setMaterialBarcodeId(barcodeListOne.get(0).getMaterialBarcodeId());
                    }
                }
            }
        }
        if (StringUtils.isEmpty(barcodeResultDto.getBarcodeType())) {
            //是否扫的是其他的系统条码
            BarcodeResultDto resultDto1 = InBarcodeUtil.scanJugeBarcode(barcode);
            if (resultDto1.getBarcodeType() != (byte) 5) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "扫描的是系统其他条码 请重新扫码-->" + barcode);
            }

            barcodeResultDto.setBarcodeType((byte) 5);
        }

        return barcodeResultDto;
    }

    /**
     * web扫码返回条码信息
     * @param barcode
     * @return
     */
    @Override
    public WmsInnerStockOrderDetBarcodeDto webScanBarcode(Long stockOrderDetId, String barcode) {
        WmsInnerStockOrderDetBarcodeDto resultDto=new WmsInnerStockOrderDetBarcodeDto();
        if(StringUtils.isEmpty(barcode)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"条码不能为空");
        }
        WmsInnerStockOrderDet wmsInnerStockOrderDet=wmsInventoryVerificationDetMapper.selectByPrimaryKey(stockOrderDetId);
        if(StringUtils.isEmpty(wmsInnerStockOrderDet)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"未找到对应的盘点明细信息");
        }

        //条码判断
        List<WmsInnerStockOrderDetBarcodeDto> detBarcodeDtos = new ArrayList<>();
        SearchWmsInnerStockOrderDetBarcode searchOrderDetBarcode = new SearchWmsInnerStockOrderDetBarcode();
        searchOrderDetBarcode.setQueryAll("true");
        searchOrderDetBarcode.setStockOrderDetId(stockOrderDetId);
        searchOrderDetBarcode.setBarcode(barcode);
        detBarcodeDtos = wmsInnerStockOrderDetBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchOrderDetBarcode));
        if (detBarcodeDtos.size() > 0) {
            if (detBarcodeDtos.get(0).getScanStatus() == (byte) 3) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "条码已扫描 请勿重复扫码-->" + barcode);
            }
            BeanUtil.copyProperties(detBarcodeDtos.get(0), resultDto);
            resultDto.setBarcodeType((byte) 1);
        } else {
            searchOrderDetBarcode.setBarcode(null);
            searchOrderDetBarcode.setColorBoxCode(barcode);
            detBarcodeDtos = wmsInnerStockOrderDetBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchOrderDetBarcode));
            if (detBarcodeDtos.size() > 0) {
                List<WmsInnerStockOrderDetBarcodeDto> barcodeListOne = detBarcodeDtos.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode()) ? "" : u.getBarcode()) == "")).collect(Collectors.toList());
                if (StringUtils.isEmpty(barcodeListOne) || barcodeListOne.size() <= 0) {
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "条码明细中不存在彩盒码-->" + barcode);
                }
                if (barcodeListOne.get(0).getScanStatus() == (byte) 3) {
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "条码已扫描 请勿重复扫码-->" + barcode);
                }
                BeanUtil.copyProperties(barcodeListOne.get(0), resultDto);
                resultDto.setBarcodeType((byte) 2);
            } else {
                searchOrderDetBarcode.setBarcode(null);
                searchOrderDetBarcode.setColorBoxCode(null);
                searchOrderDetBarcode.setCartonCode(barcode);
                detBarcodeDtos = wmsInnerStockOrderDetBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchOrderDetBarcode));
                if (detBarcodeDtos.size() > 0) {
                    List<WmsInnerStockOrderDetBarcodeDto> barcodeListOne = detBarcodeDtos.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode()) ? "" : u.getBarcode()) == "")).collect(Collectors.toList());
                    if (StringUtils.isEmpty(barcodeListOne) || barcodeListOne.size() <= 0) {
                        throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "条码明细中不存在箱码-->" + barcode);
                    }
                    if (barcodeListOne.get(0).getScanStatus() == (byte) 3) {
                        throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "条码已扫描 请勿重复扫码-->" + barcode);
                    }
                    List<WmsInnerStockOrderDetBarcodeDto> barcodeList = detBarcodeDtos.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode()) ? "" : u.getBarcode()) != "")).collect(Collectors.toList());
                    BigDecimal totalQty = barcodeList.stream().map(WmsInnerStockOrderDetBarcodeDto::getMaterialQty).reduce(BigDecimal.ZERO, BigDecimal::add);

                    BeanUtil.copyProperties(barcodeListOne.get(0), resultDto);
                    resultDto.setBarcodeType((byte) 3);
                    resultDto.setMaterialQty(totalQty);
                } else {
                    searchOrderDetBarcode.setBarcode(null);
                    searchOrderDetBarcode.setColorBoxCode(null);
                    searchOrderDetBarcode.setCartonCode(null);
                    searchOrderDetBarcode.setPalletCode(barcode);
                    detBarcodeDtos = wmsInnerStockOrderDetBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchOrderDetBarcode));
                    if (detBarcodeDtos.size() > 0) {
                        List<WmsInnerStockOrderDetBarcodeDto> barcodeListOne = detBarcodeDtos.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode()) ? "" : u.getBarcode()) == "")).collect(Collectors.toList());
                        if (StringUtils.isEmpty(barcodeListOne) || barcodeListOne.size() <= 0) {
                            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "条码明细中不存在栈板码-->" + barcode);
                        }
                        if (barcodeListOne.get(0).getScanStatus() == (byte) 3) {
                            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "条码已扫描 请勿重复扫码-->" + barcode);
                        }
                        List<WmsInnerStockOrderDetBarcodeDto> barcodeList = detBarcodeDtos.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode()) ? "" : u.getBarcode()) != "")).collect(Collectors.toList());
                        BigDecimal totalQty = barcodeList.stream().map(WmsInnerStockOrderDetBarcodeDto::getMaterialQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                        BeanUtil.copyProperties(barcodeListOne.get(0), resultDto);
                        resultDto.setBarcodeType((byte) 4);
                        resultDto.setMaterialQty(totalQty);
                    }
                }
            }
        }

        if (StringUtils.isEmpty(resultDto.getBarcodeType())) {
            searchOrderDetBarcode.setQueryAll("true");
            searchOrderDetBarcode.setStockOrderId(wmsInnerStockOrderDet.getStockOrderId());
            searchOrderDetBarcode.setBarcode(barcode);
            searchOrderDetBarcode.setColorBoxCode(null);
            searchOrderDetBarcode.setCartonCode(null);
            searchOrderDetBarcode.setStockOrderDetId(null);
            detBarcodeDtos = wmsInnerStockOrderDetBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchOrderDetBarcode));
            if(StringUtils.isNotEmpty(detBarcodeDtos) && detBarcodeDtos.size()>0){
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "条码已扫描 请勿重复扫码-->" + barcode);
            }

            //扫描已取消或已出库条码判断
            List<WmsInnerInventoryDetDto> inventoryDetDtoList=new ArrayList<>();
            SearchWmsInnerInventoryDet searchinventoryDet=new SearchWmsInnerInventoryDet();
            searchinventoryDet.setBarcode(barcode);
            searchinventoryDet.setNotEqualMark(0);
            searchinventoryDet.setMaterialId(wmsInnerStockOrderDet.getMaterialId());
            searchinventoryDet.setOrgId(wmsInnerStockOrderDet.getOrgId());

            inventoryDetDtoList=wmsInnerInventoryDetService.findList(ControllerUtil.dynamicConditionByEntity(searchinventoryDet));
            if(inventoryDetDtoList.size()>0){
                if(inventoryDetDtoList.get(0).getBarcodeStatus()==(byte)1){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"不能扫描在库条码-->"+barcode);
                }
                BeanUtil.copyProperties(inventoryDetDtoList.get(0), resultDto);
                resultDto.setBarcodeType((byte) 1);
                resultDto.setOption1("out");
            }
            else {
                searchinventoryDet.setBarcode(null);
                searchinventoryDet.setColorBoxCode(barcode);
                inventoryDetDtoList=wmsInnerInventoryDetService.findList(ControllerUtil.dynamicConditionByEntity(searchinventoryDet));
                if(inventoryDetDtoList.size()>0){
                    List<WmsInnerInventoryDetDto> barcodeListOne = inventoryDetDtoList.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode()) ? "" : u.getBarcode()) == "")).collect(Collectors.toList());
                    if (StringUtils.isEmpty(barcodeListOne) || barcodeListOne.size() <= 0) {
                        throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "条码明细中不存在彩盒码-->" + barcode);
                    }
                    if (barcodeListOne.get(0).getBarcodeStatus() == (byte) 1) {
                        throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "不能扫描在库条码-->" + barcode);
                    }
                    BeanUtil.copyProperties(barcodeListOne.get(0), resultDto);
                    resultDto.setBarcodeType((byte) 2);
                    resultDto.setOption1("out");
                }
                else{
                    searchinventoryDet.setBarcode(null);
                    searchinventoryDet.setColorBoxCode(null);
                    searchinventoryDet.setCartonCode(barcode);
                    inventoryDetDtoList=wmsInnerInventoryDetService.findList(ControllerUtil.dynamicConditionByEntity(searchinventoryDet));
                    if(inventoryDetDtoList.size()>0){
                        List<WmsInnerInventoryDetDto> barcodeListOne = inventoryDetDtoList.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode()) ? "" : u.getBarcode()) == "")).collect(Collectors.toList());
                        if (StringUtils.isEmpty(barcodeListOne) || barcodeListOne.size() <= 0) {
                            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "条码明细中不存在箱码-->" + barcode);
                        }
                        if (barcodeListOne.get(0).getBarcodeStatus() == (byte) 1) {
                            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "不能扫描在库条码-->" + barcode);
                        }
                        List<WmsInnerInventoryDetDto> barcodeList = inventoryDetDtoList.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode()) ? "" : u.getBarcode()) != "")).collect(Collectors.toList());
                        BigDecimal totalQty = barcodeList.stream().map(WmsInnerInventoryDetDto::getMaterialQty).reduce(BigDecimal.ZERO, BigDecimal::add);

                        BeanUtil.copyProperties(barcodeListOne.get(0), resultDto);
                        resultDto.setBarcodeType((byte) 3);
                        resultDto.setMaterialQty(totalQty);
                        resultDto.setOption1("out");
                    }
                    else {
                        searchinventoryDet.setBarcode(null);
                        searchinventoryDet.setColorBoxCode(null);
                        searchinventoryDet.setCartonCode(null);
                        searchinventoryDet.setPalletCode(barcode);
                        inventoryDetDtoList=wmsInnerInventoryDetService.findList(ControllerUtil.dynamicConditionByEntity(searchinventoryDet));
                        if(inventoryDetDtoList.size()>0){
                            List<WmsInnerInventoryDetDto> barcodeListOne = inventoryDetDtoList.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode()) ? "" : u.getBarcode()) == "")).collect(Collectors.toList());
                            if (StringUtils.isEmpty(barcodeListOne) || barcodeListOne.size() <= 0) {
                                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "条码明细中不存在箱码-->" + barcode);
                            }
                            if (barcodeListOne.get(0).getBarcodeStatus() == (byte) 1) {
                                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "不能扫描在库条码-->" + barcode);
                            }
                            List<WmsInnerInventoryDetDto> barcodeList = inventoryDetDtoList.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode()) ? "" : u.getBarcode()) != "")).collect(Collectors.toList());
                            BigDecimal totalQty = barcodeList.stream().map(WmsInnerInventoryDetDto::getMaterialQty).reduce(BigDecimal.ZERO, BigDecimal::add);

                            BeanUtil.copyProperties(barcodeListOne.get(0), resultDto);
                            resultDto.setBarcodeType((byte) 4);
                            resultDto.setMaterialQty(totalQty);
                            resultDto.setOption1("out");
                        }
                    }
                }
            }
            //是否扫的是其他的系统条码
            if(StringUtils.isEmpty(resultDto.getBarcodeType())) {

                BarcodeResultDto resultDto1 = InBarcodeUtil.scanJugeBarcode(barcode);
                if (resultDto1.getBarcodeType() != (byte) 5) {
                    if(StringUtils.isNotEmpty(resultDto1.getMaterialBarcodeId())) {
                        SearchWmsInnerInventoryDet searchinventoryDet1 = new SearchWmsInnerInventoryDet();
                        searchinventoryDet1.setMaterialBarcodeId(resultDto1.getMaterialBarcodeId());
                        searchinventoryDet1.setOrgId(wmsInnerStockOrderDet.getOrgId());
                        inventoryDetDtoList = wmsInnerInventoryDetService.findList(ControllerUtil.dynamicConditionByEntity(searchinventoryDet1));
                        if(StringUtils.isNotEmpty(inventoryDetDtoList) && inventoryDetDtoList.size()>0){
                            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "扫描的是系统其他条码 请重新扫码-->" + barcode);
                        }
                    }

                }

                resultDto.setBarcodeType((byte) 5);
            }
        }

        return resultDto;
    }

    /**
     * 盘点激活或作废操作
     * 库存上锁或解锁 1解锁 2上锁 3复盘 4-PDA复盘
     * @param list
     * @return
     */
    private int unlockOrLock(byte lockType, List<WmsInnerStockOrderDet> list, WmsInnerStockOrder wmsInventoryVerification){
        SysUser sysUser = currentUser();
        int num = 0;
        for (WmsInnerStockOrderDet wmsInnerStockOrderDet : list) {
            //解锁库存
            if(lockType!=3){
                Example example = new Example(WmsInnerInventory.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("warehouseId",wmsInventoryVerification.getWarehouseId())
                        .andEqualTo("storageId",wmsInnerStockOrderDet.getStorageId())
                        .andEqualTo("materialId",wmsInnerStockOrderDet.getMaterialId())
                        //.andEqualTo("batchCode",wmsInnerStockOrderDet.getBatchCode())
                        .andEqualTo("stockLock",lockType==(byte) 2?0:1)
                        .andEqualTo("orgId",sysUser.getOrganizationId())
                        .andEqualTo("jobStatus",1)
                        .andEqualTo("packingQty",wmsInnerStockOrderDet.getOriginalQty())
                        .andEqualTo("lockStatus", 0);
                if(StringUtils.isNotEmpty(wmsInnerStockOrderDet.getBatchCode())){
                    criteria.andEqualTo("batchCode",wmsInnerStockOrderDet.getBatchCode());
                }else {
                    criteria.andIsNull("batchCode");
                }
                if(StringUtils.isNotEmpty(wmsInnerStockOrderDet.getSupplierId())){
                    criteria.andEqualTo("supplierId",wmsInnerStockOrderDet.getSupplierId());
                }else {
                    criteria.andIsNull("supplierId");
                }

                if(lockType==(byte) 1){
                    criteria.andEqualTo("relevanceOrderCode",wmsInventoryVerification.getPlanStockOrderCode());
                }
                List<WmsInnerInventory> wmsInnerInventories = wmsInnerInventoryMapper.selectByExample(example);
                for (WmsInnerInventory wmsInnerInventory : wmsInnerInventories) {
                    if(lockType==1){
                        //合并库存
                        example.clear();
                        criteria = example.createCriteria();
                        criteria.andEqualTo("warehouseId",wmsInventoryVerification.getWarehouseId())
                                .andEqualTo("storageId",wmsInnerStockOrderDet.getStorageId())
                                .andEqualTo("materialId",wmsInnerStockOrderDet.getMaterialId())
                                .andEqualTo("stockLock",0)
                                .andEqualTo("orgId",sysUser.getOrganizationId())
                                .andEqualTo("jobStatus",1)
                                .andEqualTo("lockStatus",0);
                        if(StringUtils.isNotEmpty(wmsInnerStockOrderDet.getBatchCode())){
                            criteria.andEqualTo("batchCode",wmsInnerStockOrderDet.getBatchCode());
                        }else {
                            criteria.andIsNull("batchCode");
                        }
                        if(StringUtils.isNotEmpty(wmsInnerStockOrderDet.getSupplierId())){
                            criteria.andEqualTo("supplierId",wmsInnerStockOrderDet.getSupplierId());
                        }else {
                            criteria.andIsNull("supplierId");
                        }
                        criteria.andGreaterThan("packingQty",0);

                        WmsInnerInventory wmsInnerInventory1 = wmsInnerInventoryMapper.selectOneByExample(example);
                        if(StringUtils.isEmpty(wmsInnerInventory1)){
                            wmsInnerInventory.setStockLock((byte)0);
                            wmsInnerInventory.setRelevanceOrderCode(wmsInventoryVerification.getPlanStockOrderCode());
                            num+=wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventory);
                        }else {
                            wmsInnerInventory1.setPackingQty(wmsInnerInventory1.getPackingQty().add(wmsInnerInventory.getPackingQty()));
                            wmsInnerInventory1.setRelevanceOrderCode(wmsInventoryVerification.getPlanStockOrderCode());
                            wmsInnerInventoryMapper.deleteByPrimaryKey(wmsInnerInventory);

                            //更新合并库存
                            wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventory1);
                        }
                    }else {
                        //激活 锁定库存
                        wmsInnerInventory.setStockLock(lockType==(byte) 2?(byte)1:0);
                        wmsInnerInventory.setRelevanceOrderCode(wmsInventoryVerification.getPlanStockOrderCode());
                        num+=wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventory);
                    }
                }
            }

            //库存明细
//            example = new Example(WmsInnerInventoryDet.class);
//            example.createCriteria().andEqualTo("storageId",wmsInnerStockOrderDet.getStorageId())
//                    .andEqualTo("materialId",wmsInnerStockOrderDet.getMaterialId())
//                    .andEqualTo("jobStatus",(byte)2)
//                    .andEqualTo("ifStockLock",lockType==(byte) 2?0:1)
//                    .andEqualTo("jobStatus",1).andEqualTo("packingQty",wmsInnerStockOrderDet.getOriginalQty());
//            List<WmsInnerInventoryDet> wmsInnerInventoryDets = wmsInnerInventoryDetMapper.selectByExample(example);
//            for (WmsInnerInventoryDet wmsInnerInventoryDet : wmsInnerInventoryDets) {
//                wmsInnerInventoryDet.setIfStockLock(lockType==(byte) 2?(byte)1:0);
//                num+=wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(wmsInnerInventoryDet);
//            }
            //复盘
            if(lockType==(byte) 3){
                num+=this.analyse((byte) 1,null,wmsInnerStockOrderDet,wmsInventoryVerification);
            }
        }
        return num;
    }

    /**
     * 盘点激活或作废操作
     * @param oprationType 盘点条码明细新增或更新 1 新增 2 更新
     * @param activeOrAgain 激活或复盘 1 激活 2 复盘
     * @return
     */
    private int addStockOrderDetBarcode(byte oprationType, byte activeOrAgain, List<WmsInnerStockOrderDet> list, SysUser sysUser){
        int num = 0;
        List<WmsInnerStockOrderDetBarcode> stockOrderDetBarcodeList=new ArrayList<>();
        List<WmsInnerInventoryDet> wmsInnerInventoryDets=new ArrayList<>();
        WmsInnerStockOrder wmsInnerStockOrder=wmsInventoryVerificationMapper.selectByPrimaryKey(list.get(0).getStockOrderId());
        for (WmsInnerStockOrderDet wmsInnerStockOrderDet : list) {
            if(oprationType==(byte)1){
                //新增盘点条码明细
                SearchWmsInnerInventoryDet searchinventoryDet=new SearchWmsInnerInventoryDet();
                searchinventoryDet.setStorageId(wmsInnerStockOrderDet.getStorageId());
                searchinventoryDet.setNotEqualMark(0);
                searchinventoryDet.setMaterialId(wmsInnerStockOrderDet.getMaterialId());
                searchinventoryDet.setBarcodeStatus("1");
                if(StringUtils.isNotEmpty(activeOrAgain) && activeOrAgain==(byte)1)
                    searchinventoryDet.setIfStockLock((byte)0);//盘点锁(0-否 1-是)
                else if(StringUtils.isNotEmpty(activeOrAgain) && activeOrAgain==(byte)2)
                    searchinventoryDet.setIfStockLock((byte)1);//盘点锁(0-否 1-是)
                searchinventoryDet.setOrgId(wmsInnerStockOrderDet.getOrgId());
                searchinventoryDet.setBatchCode(wmsInnerStockOrderDet.getBatchCode());
                searchinventoryDet.setSupplierId(wmsInnerStockOrderDet.getSupplierId());

                List<WmsInnerInventoryDetDto> inventoryDetDtoList=wmsInnerInventoryDetService.findList(ControllerUtil.dynamicConditionByEntity(searchinventoryDet));
                for (WmsInnerInventoryDetDto item : inventoryDetDtoList) {
                    WmsInnerStockOrderDetBarcode stockOrderDetBarcode=new WmsInnerStockOrderDetBarcode();
                    stockOrderDetBarcode.setStockOrderDetBarcodeId(null);
                    stockOrderDetBarcode.setStockOrderDetId(wmsInnerStockOrderDet.getStockOrderDetId());
                    stockOrderDetBarcode.setMaterialBarcodeId(item.getMaterialBarcodeId());
                    stockOrderDetBarcode.setSourceDetId(item.getInventoryDetId());
                    stockOrderDetBarcode.setScanStatus((byte)1);
                    stockOrderDetBarcode.setStockResult((byte)1);
                    stockOrderDetBarcode.setStatus((byte)1);
                    stockOrderDetBarcode.setCreateUserId(sysUser.getUserId());
                    stockOrderDetBarcode.setCreateTime(new Date());
                    stockOrderDetBarcode.setOrgId(sysUser.getOrganizationId());
                    stockOrderDetBarcodeList.add(stockOrderDetBarcode);

                    //更新库存条码明细盘点锁 锁定
                    WmsInnerInventoryDet inventoryDet=new WmsInnerInventoryDet();
                    inventoryDet.setInventoryDetId(item.getInventoryDetId());
                    inventoryDet.setIfStockLock((byte)1);
                    inventoryDet.setModifiedUserId(sysUser.getUserId());
                    inventoryDet.setModifiedTime(new Date());
                    num+=wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(inventoryDet);
                }
                //盘赢条码新增到复盘单条码明细
                /*if(StringUtils.isNotEmpty(wmsInnerStockOrderDet.getSourceDetId())) {
                    Example example = new Example(WmsInnerStockOrderDetBarcode.class);
                    Example.Criteria criteria = example.createCriteria();
                    criteria.andEqualTo("stockOrderDetId", wmsInnerStockOrderDet.getSourceDetId());
                    criteria.andEqualTo("stockResult", (byte) 3);
                    List<WmsInnerStockOrderDetBarcode> stockOrderDetBarcodes = wmsInnerStockOrderDetBarcodeMapper.selectByExample(example);
                    if (StringUtils.isNotEmpty(stockOrderDetBarcodes) && stockOrderDetBarcodes.size() > 0) {
                        for (WmsInnerStockOrderDetBarcode item : stockOrderDetBarcodes) {
                            WmsInnerStockOrderDetBarcode stockOrderDetBarcode = new WmsInnerStockOrderDetBarcode();
                            stockOrderDetBarcode.setStockOrderDetBarcodeId(null);
                            stockOrderDetBarcode.setStockOrderDetId(wmsInnerStockOrderDet.getStockOrderDetId());
                            stockOrderDetBarcode.setMaterialBarcodeId(item.getMaterialBarcodeId());
                            stockOrderDetBarcode.setScanStatus((byte) 1);
                            stockOrderDetBarcode.setStockResult((byte) 1);
                            stockOrderDetBarcode.setStatus((byte) 1);
                            stockOrderDetBarcode.setCreateUserId(sysUser.getUserId());
                            stockOrderDetBarcode.setCreateTime(new Date());
                            stockOrderDetBarcode.setOrgId(sysUser.getOrganizationId());
                            stockOrderDetBarcodeList.add(stockOrderDetBarcode);
                        }

                    }
                }*/
            }
            else {
                Example example = new Example(WmsInnerStockOrderDetBarcode.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("stockOrderDetId",wmsInnerStockOrderDet.getStockOrderDetId());
                List<WmsInnerStockOrderDetBarcode> stockOrderDetBarcodes=wmsInnerStockOrderDetBarcodeMapper.selectByExample(example);
                for (WmsInnerStockOrderDetBarcode stockOrderDetBarcode : stockOrderDetBarcodes) {
                    //更新库存条码明细盘点锁 解锁
                    WmsInnerInventoryDet inventoryDet=new WmsInnerInventoryDet();
                    inventoryDet.setInventoryDetId(stockOrderDetBarcode.getSourceDetId());
                    inventoryDet.setIfStockLock((byte)0);
                    inventoryDet.setModifiedUserId(sysUser.getUserId());
                    inventoryDet.setModifiedTime(new Date());
                    wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(inventoryDet);

                    //处盘时 盘盈数=盘亏数 处理盘盈 新增到库存条码明细 盘亏更新为已出库
                    if(stockOrderDetBarcode.getStockResult()==(byte)3){
                        WmsInnerInventoryDet inventoryDetNew = new WmsInnerInventoryDet();
                        inventoryDetNew.setStorageId(wmsInnerStockOrderDet.getStorageId());
                        inventoryDetNew.setMaterialBarcodeId(stockOrderDetBarcode.getMaterialBarcodeId());
                        inventoryDetNew.setReceivingDate(new Date());//入库日期
                        inventoryDetNew.setAsnCode(wmsInnerStockOrder.getPlanStockOrderCode());//盘点单号
                        inventoryDetNew.setIfStockLock((byte) 0);
                        inventoryDetNew.setInventoryStatusId(wmsInnerStockOrderDet.getInventoryStatusId());
                        inventoryDetNew.setBarcodeStatus((byte) 1);//在库
                        inventoryDetNew.setCreateUserId(sysUser.getUserId());
                        inventoryDetNew.setCreateTime(new Date());
                        inventoryDetNew.setModifiedUserId(sysUser.getUserId());
                        inventoryDetNew.setModifiedTime(new Date());
                        inventoryDetNew.setOrgId(sysUser.getOrganizationId());
                        wmsInnerInventoryDets.add(inventoryDetNew);
                    }
                    else if(stockOrderDetBarcode.getStockResult()==(byte)4){
                        WmsInnerInventoryDet upInnerInventoryDet=new WmsInnerInventoryDet();
                        upInnerInventoryDet.setInventoryDetId(stockOrderDetBarcode.getSourceDetId());
                        upInnerInventoryDet.setBarcodeStatus((byte)3);
                        upInnerInventoryDet.setModifiedTime(new Date());
                        upInnerInventoryDet.setModifiedUserId(sysUser.getUserId());
                        wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(upInnerInventoryDet);
                    }

                }

            }
        }
        if(stockOrderDetBarcodeList.size()>0) {
            num += wmsInnerStockOrderDetBarcodeMapper.insertList(stockOrderDetBarcodeList);
        }
        if(wmsInnerInventoryDets.size()>0){
            num+=wmsInnerInventoryDetMapper.insertList(wmsInnerInventoryDets);
        }
        return num;
    }

    /**
     * 复盘盘盈盘亏
     * @param type 1不扫条码 3-PDA扫条码盘点
     * @param wmsInnerStockOrderDet
     * @return
     */
    private int analyse(byte type,String barcode,WmsInnerStockOrderDet wmsInnerStockOrderDet,WmsInnerStockOrder wmsInnerStockOrder){
            //更新库存 盘盈 根据盘点单生成一条库存 盘亏 按顺序扣减库存
            //解锁库存
        int num = 0;
        SysUser sysUser=currentUser();
        WmsInnerInventoryLog wmsInnerInventoryLog = new WmsInnerInventoryLogDto();
            Example example = new Example(WmsInnerInventory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("warehouseId",wmsInnerStockOrder.getWarehouseId())
                    .andEqualTo("storageId",wmsInnerStockOrderDet.getStorageId())
                    .andEqualTo("materialId",wmsInnerStockOrderDet.getMaterialId())
                    //.andEqualTo("batchCode",wmsInnerStockOrderDet.getBatchCode())
                    .andEqualTo("relevanceOrderCode",wmsInnerStockOrder.getRelatedOrderCode())
                    .andEqualTo("stockLock",1)
                    .andEqualTo("orgId",wmsInnerStockOrder.getOrganizationId())
                    .andGreaterThan("packingQty",0)
                    .andEqualTo("jobStatus",1)
                    .andEqualTo("packingQty",wmsInnerStockOrderDet.getOriginalQty());
            if(StringUtils.isNotEmpty(wmsInnerStockOrderDet.getBatchCode())){
                criteria.andEqualTo("batchCode",wmsInnerStockOrderDet.getBatchCode());
            }else {
                criteria.andIsNull("batchCode");
            }
            if(StringUtils.isNotEmpty(wmsInnerStockOrderDet.getSupplierId())){
                criteria.andEqualTo("supplierId",wmsInnerStockOrderDet.getSupplierId());
            }else {
                criteria.andIsNull("supplierId");
            }
            WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
            if(StringUtils.isEmpty(wmsInnerInventory)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"库存变动 请重新盘点");
            }
            example.clear();
            criteria = example.createCriteria();
            criteria.andEqualTo("warehouseId",wmsInnerStockOrder.getWarehouseId())
                    .andEqualTo("storageId",wmsInnerStockOrderDet.getStorageId())
            .andEqualTo("materialId",wmsInnerStockOrderDet.getMaterialId())
            .andEqualTo("stockLock",0)
            .andEqualTo("orgId",wmsInnerStockOrder.getOrganizationId())
            .andEqualTo("jobStatus",1)
            .andEqualTo("lockStatus",0);
            if(StringUtils.isNotEmpty(wmsInnerStockOrderDet.getBatchCode())){
                criteria.andEqualTo("batchCode",wmsInnerStockOrderDet.getBatchCode());
            }else {
                criteria.andIsNull("batchCode");
            }
        WmsInnerInventory wmsInnerInventory1 = wmsInnerInventoryMapper.selectOneByExample(example);
            //解锁查询是否存在库存 存在则解锁 不存在则新增
        if(StringUtils.isEmpty(wmsInnerInventory1)){
            wmsInnerInventory.setStockLock((byte)0);
            if(!StringUtils.isEmpty(wmsInnerInventory.getPackingQty()) && wmsInnerStockOrderDet.getStockQty().compareTo(wmsInnerInventory.getPackingQty())==1){
                wmsInnerInventoryLog = initLog(wmsInnerInventory);
                wmsInnerInventoryLog.setAddOrSubtract((byte)1);
                wmsInnerInventoryLog.setInitialQty(wmsInnerInventory.getPackingQty());

                wmsInnerInventory.setPackingQty(wmsInnerInventory.getPackingQty().add(wmsInnerStockOrderDet.getVarianceQty()));
                num+= wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventory);

                wmsInnerInventoryLog.setChangeQty(wmsInnerStockOrderDet.getVarianceQty());
            }else{
                if(wmsInnerInventory.getPackingQty().compareTo(wmsInnerStockOrderDet.getVarianceQty())==-1){
                    throw new BizErrorException("库存波动，请重新盘点");
                }
                wmsInnerInventoryLog = initLog(wmsInnerInventory);
                wmsInnerInventoryLog.setAddOrSubtract((byte)2);
                wmsInnerInventoryLog.setInitialQty(wmsInnerInventory.getPackingQty());
                if(wmsInnerStockOrderDet.getVarianceQty().signum()==-1){
                    int qty = ~(wmsInnerStockOrderDet.getVarianceQty().intValue() - 1);
                    wmsInnerStockOrderDet.setVarianceQty(BigDecimal.valueOf(qty));
                }
                wmsInnerInventoryLog.setChangeQty(wmsInnerStockOrderDet.getVarianceQty());
                //盘点数小于库存
                wmsInnerInventory.setPackingQty(wmsInnerInventory.getPackingQty().subtract(wmsInnerStockOrderDet.getVarianceQty()));
                num+= wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventory);
            }
        }else {
            //库存日志
            wmsInnerInventoryLog = initLog(wmsInnerInventory);
            //存在想同货品库存
            //盘盈
            wmsInnerInventory1.setRelevanceOrderCode(wmsInnerStockOrder.getPlanStockOrderCode());
            if(wmsInnerStockOrderDet.getStockQty().compareTo(wmsInnerInventory.getPackingQty())==1){
                //初始数量=库存数量+锁定库存数量
                wmsInnerInventoryLog.setInitialQty(wmsInnerInventory1.getPackingQty().add(wmsInnerStockOrderDet.getOriginalQty()));

                //现有数量加 = 原有数量+(锁定库存原始数量+差异数量)
                wmsInnerInventory1.setPackingQty(wmsInnerInventory1.getPackingQty().add(wmsInnerStockOrderDet.getOriginalQty().add(wmsInnerStockOrderDet.getVarianceQty())));

                wmsInnerInventoryLog.setAddOrSubtract((byte)1);
                //变化数量=差异数量
                wmsInnerInventoryLog.setChangeQty(wmsInnerStockOrderDet.getVarianceQty());
            }else {
                //盘亏/正常
                //现有数量 = 原有数量+（锁定库存数量-差异数量）
                wmsInnerInventory1.setPackingQty(wmsInnerInventory1.getPackingQty().add(wmsInnerStockOrderDet.getOriginalQty().add(wmsInnerStockOrderDet.getVarianceQty())));

                if(wmsInnerStockOrderDet.getVarianceQty().compareTo(BigDecimal.ZERO)==-1){
                    wmsInnerInventoryLog = initLog(wmsInnerInventory);
                    wmsInnerInventoryLog.setAddOrSubtract((byte)2);

                    //初始数量=库存数量+锁定库存数量
                    wmsInnerInventoryLog.setInitialQty(wmsInnerInventory1.getPackingQty().add(wmsInnerStockOrderDet.getOriginalQty()));

                    if(wmsInnerStockOrderDet.getVarianceQty().signum()==-1){
                        int qty = ~(wmsInnerStockOrderDet.getVarianceQty().intValue() - 1);
                        wmsInnerStockOrderDet.setVarianceQty(BigDecimal.valueOf(qty));
                    }
                    wmsInnerInventoryLog.setChangeQty(wmsInnerStockOrderDet.getVarianceQty());
                }
            }
            wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventory1);
            wmsInnerInventoryMapper.deleteByPrimaryKey(wmsInnerInventory.getInventoryId());
        }

        //条码解锁
        List<WmsInnerInventoryDet> wmsInnerInventoryDets=new ArrayList<>();
        Example exampleDet = new Example(WmsInnerStockOrderDetBarcode.class);
        Example.Criteria criteriaDet = exampleDet.createCriteria();
        criteriaDet.andEqualTo("stockOrderDetId",wmsInnerStockOrderDet.getStockOrderDetId());
        List<WmsInnerStockOrderDetBarcode> detBarcodeList=wmsInnerStockOrderDetBarcodeMapper.selectByExample(exampleDet);
        if(StringUtils.isNotEmpty(detBarcodeList) && detBarcodeList.size()>0){
            for (WmsInnerStockOrderDetBarcode detBarcode : detBarcodeList) {
                WmsInnerInventoryDet inventoryDet=new WmsInnerInventoryDet();
                inventoryDet.setInventoryDetId(detBarcode.getSourceDetId());
                inventoryDet.setIfStockLock((byte)0);
                inventoryDet.setModifiedTime(new Date());
                if(detBarcode.getStockResult()==(byte)4){
                    inventoryDet.setBarcodeStatus((byte)3);
                }
                wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(inventoryDet);

                if(detBarcode.getStockResult()==(byte)3){
                    //库存明细条码
                    Example exampleInDet = new Example(WmsInnerInventoryDet.class);
                    Example.Criteria criteriaInDet = exampleInDet.createCriteria();
                    criteriaInDet.andEqualTo("materialBarcodeId",detBarcode.getMaterialBarcodeId());
                    List<WmsInnerInventoryDet> detList=wmsInnerInventoryDetMapper.selectByExample(exampleInDet);
                    if(StringUtils.isEmpty(detList) || detList.size()<=0) {
                        WmsInnerInventoryDet inventoryDetNew = new WmsInnerInventoryDet();
                        inventoryDetNew.setStorageId(wmsInnerStockOrderDet.getStorageId());
                        inventoryDetNew.setMaterialBarcodeId(detBarcode.getMaterialBarcodeId());
                        inventoryDetNew.setReceivingDate(new Date());//入库日期
                        inventoryDetNew.setAsnCode(wmsInnerStockOrder.getPlanStockOrderCode());//盘点单号
                        inventoryDetNew.setIfStockLock((byte) 0);
                        inventoryDetNew.setInventoryStatusId(wmsInnerStockOrderDet.getInventoryStatusId());
                        inventoryDetNew.setBarcodeStatus((byte) 1);//在库
                        inventoryDetNew.setCreateUserId(sysUser.getUserId());
                        inventoryDetNew.setCreateTime(new Date());
                        inventoryDetNew.setModifiedUserId(sysUser.getUserId());
                        inventoryDetNew.setModifiedTime(new Date());
                        inventoryDetNew.setOrgId(sysUser.getOrganizationId());
                        wmsInnerInventoryDets.add(inventoryDetNew);
                    }
                    else{
                        WmsInnerInventoryDet inventoryDetUp = new WmsInnerInventoryDet();
                        inventoryDetUp.setInventoryDetId(detList.get(0).getInventoryDetId());
                        inventoryDetUp.setModifiedTime(new Date());
                        inventoryDetUp.setModifiedUserId(sysUser.getUserId());
                        inventoryDetUp.setBarcodeStatus((byte)1);
                        inventoryDetUp.setStorageId(wmsInnerStockOrderDet.getStorageId());
                        wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(inventoryDetUp);
                    }
                }
            }
        }

        if(wmsInnerInventoryDets.size()>0){
            wmsInnerInventoryDetMapper.insertList(wmsInnerInventoryDets);
        }

        //盘点数大于库存数 原有数量新增
//        Byte addOrSubtract = null;
//        if(StringUtils.isEmpty(wmsInnerInventory)){
//            wmsInnerInventory = new WmsInnerInventory();
//            wmsInnerInventory.setWarehouseId(wmsInnerStockOrder.getWarehouseId());
//            wmsInnerInventory.setStorageId(wmsInnerStockOrderDet.getStorageId());
//            wmsInnerInventory.setPackingUnitName(wmsInnerStockOrderDet.getPackingUnitName());
//            wmsInnerInventory.setPackingQty(wmsInnerStockOrderDet.getStockQty());
//            wmsInnerInventory.setRelevanceOrderCode(wmsInnerStockOrder.getPlanStockOrderCode());
//            wmsInnerInventory.setStockLock((byte)0);
//            wmsInnerInventory.setInventoryStatusId(wmsInnerStockOrderDet.getInventoryStatusId());
//            wmsInnerInventory.setMaterialId(wmsInnerStockOrderDet.getMaterialId());
//            wmsInnerInventory.setJobStatus((byte)1);
//            wmsInnerInventory.setLockStatus((byte)0);
//            wmsInnerInventory.setCreateTime(new Date());
//            wmsInnerInventory.setCreateUserId(wmsInnerStockOrderDet.getModifiedUserId());
//            wmsInnerInventory.setModifiedTime(new Date());
//            wmsInnerInventory.setModifiedUserId(wmsInnerStockOrderDet.getModifiedUserId());
//            wmsInnerInventory.setOrgId(wmsInnerStockOrderDet.getOrganizationId());
//            wmsInnerInventory.setContractCode(wmsInnerStockOrderDet.getContractCode());
//            wmsInnerInventory.setPurchaseReqOrderCode(wmsInnerStockOrderDet.getPurchaseReqOrderCode());
//            wmsInnerInventory.setOption4(wmsInnerStockOrderDet.getLocationNum());
//            wmsInnerInventory.setOption2(wmsInnerStockOrderDet.getDominantTermCode());
//            wmsInnerInventory.setOption1(wmsInnerStockOrderDet.getDeviceCode());
//            wmsInnerInventory.setOption3(wmsInnerStockOrderDet.getMaterialPurpose());
//            wmsInnerInventory.setSupplierId(wmsInnerStockOrderDet.getSupplierId());
//            wmsInnerInventory.setPalletCode(wmsInnerStockOrderDet.getPalletCode());
//
//            num+=wmsInnerInventoryMapper.insertSelective(wmsInnerInventory);
//            wmsInnerInventoryLog = initLog(wmsInnerInventory);
//            wmsInnerInventoryLog.setInitialQty(BigDecimal.ZERO);
//            wmsInnerInventoryLog.setAddOrSubtract((byte)1);
//            wmsInnerInventoryLog.setChangeQty(wmsInnerStockOrderDet.getVarianceQty());
//        }else if(!StringUtils.isEmpty(wmsInnerInventory.getPackingQty()) && wmsInnerStockOrderDet.getStockQty().compareTo(wmsInnerInventory.getPackingQty())==1){
//            wmsInnerInventoryLog = initLog(wmsInnerInventory);
//            wmsInnerInventoryLog.setAddOrSubtract((byte)1);
//            wmsInnerInventoryLog.setInitialQty(wmsInnerInventory.getPackingQty());
//
//                wmsInnerInventory.setPackingQty(wmsInnerInventory.getPackingQty().add(wmsInnerStockOrderDet.getVarianceQty()));
//                num+= wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventory);
//
//                wmsInnerInventoryLog.setChangeQty(wmsInnerStockOrderDet.getVarianceQty());
//        }else{
//            if(wmsInnerInventory.getPackingQty().compareTo(wmsInnerStockOrderDet.getVarianceQty())==-1){
//                throw new BizErrorException("库存波动，请重新盘点");
//            }
//            wmsInnerInventoryLog = initLog(wmsInnerInventory);
//            wmsInnerInventoryLog.setAddOrSubtract((byte)2);
//            wmsInnerInventoryLog.setInitialQty(wmsInnerInventory.getPackingQty());
//            if(wmsInnerStockOrderDet.getVarianceQty().signum()==-1){
//                int qty = ~(wmsInnerStockOrderDet.getVarianceQty().intValue() - 1);
//                wmsInnerStockOrderDet.setVarianceQty(BigDecimal.valueOf(qty));
//            }
//            wmsInnerInventoryLog.setChangeQty(wmsInnerStockOrderDet.getVarianceQty());
//            //盘点数小于库存
//            wmsInnerInventory.setPackingQty(wmsInnerInventory.getPackingQty().subtract(wmsInnerStockOrderDet.getVarianceQty()));
//            num+= wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventory);
//        }

            //PDA扫码盘点
            if(type==3){
                if(StringUtils.isEmpty(barcode)){
                    throw new BizErrorException("条码错误");
                }
                Example example1 = new Example(WmsInnerInventoryDet.class);
                example.createCriteria().andEqualTo("storageId",wmsInnerStockOrderDet.getStorageId()).andEqualTo("materialId",wmsInnerStockOrderDet.getMaterialId())
                        .andEqualTo("productionBatchCode",wmsInnerStockOrderDet.getBatchCode()).andEqualTo("barcode",barcode).andGreaterThan("packingQty",0);
                WmsInnerInventoryDet wmsInnerInventoryDet = wmsInnerInventoryDetMapper.selectOneByExample(example1);
                if(StringUtils.isEmpty(wmsInnerInventoryDet)){
                    throw new BizErrorException("库存查询失败");
                }
              /*  if(StringUtils.isEmpty(wmsInnerInventoryDet.getMaterialQty())||wmsInnerInventoryDet.getMaterialQty().compareTo(wmsInnerStockOrderDet.getStockQty())==-1){
                    //盘盈
                    wmsInnerInventoryDet.setMaterialQty(wmsInnerInventoryDet.getMaterialQty().add(wmsInnerStockOrderDet.getVarianceQty()));
                    num+=wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(wmsInnerInventoryDet);
                }else {
                    //盘亏
                    wmsInnerInventoryDet.setMaterialQty(wmsInnerInventoryDet.getMaterialQty().subtract(wmsInnerStockOrderDet.getVarianceQty()));
                    num+=wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(wmsInnerInventoryDet);
                }*/
            }
        //添加库存日志
        if(wmsInnerInventoryLog.getChangeQty().compareTo(BigDecimal.ZERO)==1){
            wmsInnerInventoryLog.setRelatedOrderCode(wmsInnerStockOrder.getPlanStockOrderCode());
            InventoryLogUtil.addLog(wmsInnerInventoryLog);
        }
            return num;
    }

    private WmsInnerInventoryLog initLog(WmsInnerInventory wmsInnerInventory){
        WmsInnerInventoryLog wmsInnerInventoryLog = new WmsInnerInventoryLogDto();
        BeanUtil.copyProperties(wmsInnerInventory,wmsInnerInventoryLog);
        //收货
        wmsInnerInventoryLog.setJobOrderType((byte)7);
        wmsInnerInventoryLog.setProductionDate(wmsInnerInventory.getProductionDate());
        wmsInnerInventoryLog.setExpiredDate(wmsInnerInventory.getExpiredDate());
        wmsInnerInventoryLog.setBatchCode(wmsInnerInventory.getBatchCode());
        wmsInnerInventoryLog.setPalletCode(wmsInnerInventory.getPalletCode());
        wmsInnerInventoryLog.setInventoryStatusId(wmsInnerInventory.getInventoryStatusId());
        wmsInnerInventoryLog.setChangeQty(wmsInnerInventory.getPackingQty());
        wmsInnerInventoryLog.setMaterialOwnerId(wmsInnerInventory.getMaterialOwnerId());
        return wmsInnerInventoryLog;
    }
    /**
     * 获取当前登录用户
     * @return
     */
    private SysUser currentUser(){
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(sysUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return sysUser;
    }
}
