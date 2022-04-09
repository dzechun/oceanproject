package com.fantechs.provider.wms.inner.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryLogDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStockOrderDto;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.general.entity.wms.inner.*;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerStockOrderDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerStockOrderMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerStockOrderService;
import com.fantechs.provider.wms.inner.util.InventoryLogUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
    private SecurityFeignApi securityFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;

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
        record.setStockOrderCode(CodeUtils.getId("INPD-"));
        record.setProjectType((byte)1);
        record.setOrderStatus((byte)1);
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrganizationId(sysUser.getOrganizationId());
        int num = wmsInventoryVerificationMapper.insertUseGeneratedKeys(record);
        //库位盘点/全盘
        if(record.getStockType()==(byte)1 || record.getStockType()==(byte)3){
            if(record.getStockType()==(byte)1 && (StringUtils.isNotEmpty(record.getMaxStorageCount()) && record.getMaxStorageCount() < record.getStorageList().size())){
                throw new BizErrorException("所选库位数不能大于最大库位数");
            }
            //盘点类型：库位盘点
            //查询库位下的所以库存货品
            if(record.getStockType()==(byte)1&&StringUtils.isEmpty(record)){
                throw new BizErrorException("库位不能未空");
            }
            List<WmsInnerStockOrderDet> wmsInventoryVerificationDets = this.findInvGoods(record.getStockType(),record.getStockOrderId(), record.getStorageList(),record.getWarehouseId());
            int res = wmsInventoryVerificationDetMapper.insertList(wmsInventoryVerificationDets);
            if(res<0){
                throw new BizErrorException("新增盘点单失败");
            }
        }else if(record.getStockType()==(byte)2){
            //货品
//            for (WmsInnerStockOrderDet inventoryVerificationDet : record.getInventoryVerificationDets()) {
//                inventoryVerificationDet.setStockOrderId(record.getStockOrderId());
//                inventoryVerificationDet.setCreateUserId(sysUser.getUserId());
//                inventoryVerificationDet.setCreateTime(new Date());
//                inventoryVerificationDet.setModifiedUserId(sysUser.getUserId());
//                inventoryVerificationDet.setModifiedTime(new Date());
//                inventoryVerificationDet.setOrganizationId(sysUser.getOrganizationId());
//            }
//            int res = wmsInventoryVerificationDetMapper.insertList(record.getInventoryVerificationDets());
            List<WmsInnerStockOrderDet> list = this.MaterialfindInvGoofs(record.getStockOrderId(),record.getMaterialList(),record.getWarehouseId());
            int res = wmsInventoryVerificationDetMapper.insertList(list);
            if(res<0){
                throw new BizErrorException("新增盘点单失败");
            }
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(WmsInnerStockOrder entity) {
        SysUser sysUser = currentUser();
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
            for (WmsInnerStockOrderDet inventoryVerificationDet : entity.getInventoryVerificationDets()) {
                if(StringUtils.isEmpty(inventoryVerificationDet.getStorageId())){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003,"初盘货品库位不能为空");
                }
                inventoryVerificationDet.setModifiedUserId(sysUser.getUserId());
                inventoryVerificationDet.setModifiedTime(new Date());
                inventoryVerificationDet.setOrganizationId(sysUser.getOrganizationId());
                if(entity.getType()==(byte) 2){
                    inventoryVerificationDet.setStockUserId(sysUser.getUserId());
                    inventoryVerificationDet.setIfRegister((byte)1);
                }
                if(StringUtils.isEmpty(inventoryVerificationDet.getStockQty())){
                    inventoryVerificationDet.setStockQty(BigDecimal.ZERO);
                }
                inventoryVerificationDet.setVarianceQty(inventoryVerificationDet.getStockQty().subtract(inventoryVerificationDet.getOriginalQty()));
                if(StringUtils.isEmpty(inventoryVerificationDet.getStockOrderDetId())){
                    inventoryVerificationDet.setStockOrderId(entity.getStockOrderId());
                    inventoryVerificationDet.setCreateUserId(sysUser.getUserId());
                    inventoryVerificationDet.setCreateTime(new Date());
                    wmsInventoryVerificationDetMapper.insertSelective(inventoryVerificationDet);
                }else {
                    wmsInventoryVerificationDetMapper.updateByPrimaryKeySelective(inventoryVerificationDet);
                }
            }
            //int num = wmsInventoryVerificationDetMapper.insertList(entity.getInventoryVerificationDets());
        }else if(entity.getType()==(byte) 1){
            //库位盘点/全盘
            if(entity.getStockType()==(byte)1 || entity.getStockType()==(byte)3){
                if(entity.getStockType()==(byte)1&& (StringUtils.isNotEmpty(entity.getMaxStorageCount()) && entity.getMaxStorageCount()<entity.getStorageList().size())){
                    throw new BizErrorException("所选库位数不能大于最大库位数");
                }
                //盘点类型：库位盘
                //查询库位下的所以库存货品
                if(entity.getStockType()==(byte)1&&StringUtils.isEmpty(entity)){
                    throw new BizErrorException("库位不能未空");
                }
                List<WmsInnerStockOrderDet> wmsInventoryVerificationDets = this.findInvGoods(entity.getStockType(),entity.getStockOrderId(), entity.getStorageList(),entity.getWarehouseId());
                int res = wmsInventoryVerificationDetMapper.insertList(wmsInventoryVerificationDets);
                if(res<0){
                    throw new BizErrorException("新增盘点单失败");
                }
            }else if(entity.getStockType()==(byte)2){
                //货品
//                for (WmsInnerStockOrderDet inventoryVerificationDet : entity.getInventoryVerificationDets()) {
//                    inventoryVerificationDet.setStockOrderId(entity.getStockOrderId());
//                    inventoryVerificationDet.setCreateUserId(sysUser.getUserId());
//                    inventoryVerificationDet.setCreateTime(new Date());
//                    inventoryVerificationDet.setModifiedUserId(sysUser.getUserId());
//                    inventoryVerificationDet.setModifiedTime(new Date());
//                    inventoryVerificationDet.setOrganizationId(sysUser.getOrganizationId());
//                }
                List<WmsInnerStockOrderDet> list = this.MaterialfindInvGoofs(entity.getStockOrderId(),entity.getMaterialList(),entity.getWarehouseId());
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
    private List<WmsInnerStockOrderDet> MaterialfindInvGoofs(Long id,List<Long> materialList,Long warehouseId){
        SysUser sysUser = currentUser();
        List<WmsInnerStockOrderDet> list = new ArrayList<>();
        for (Long materialId : materialList) {
            //获取库位名称
            Example example = new Example(WmsInnerInventory.class);
            //盘点锁 0 否 1 是
            example.createCriteria().andEqualTo("warehouseId",warehouseId).andEqualTo("materialId",materialId).andEqualTo("stockLock",0)
                    .andGreaterThan("packingQty",0).andEqualTo("jobStatus",1).andEqualTo("orgId",sysUser.getOrganizationId())
                    .andEqualTo("qcLock", 0)
                    .andEqualTo("lockStatus", 0);
            //获取货品库存
            List<WmsInnerInventory> wmsInnerInventories = wmsInnerInventoryMapper.selectByExample(example);
            wmsInnerInventories = this.filtration(wmsInnerInventories);
//            if(wmsInnerInventories.size()<1){
//                WmsInnerStockOrderDet wmsInventoryVerificationDet = new WmsInnerStockOrderDet();
//                wmsInventoryVerificationDet.setStockOrderId(id);
//                wmsInventoryVerificationDet.setMaterialId(materialId);
//                wmsInventoryVerificationDet.setIfRegister((byte)2);
//                wmsInventoryVerificationDet.setOriginalQty(BigDecimal.ZERO);
//                wmsInventoryVerificationDet.setCreateTime(new Date());
//                wmsInventoryVerificationDet.setCreateUserId(sysUser.getUserId());
//                wmsInventoryVerificationDet.setModifiedTime(new Date());
//                wmsInventoryVerificationDet.setModifiedUserId(sysUser.getUserId());
//                wmsInventoryVerificationDet.setOrganizationId(sysUser.getOrganizationId());
//                list.add(wmsInventoryVerificationDet);
//            }
            for (WmsInnerInventory wmsInnerInventory : wmsInnerInventories) {
                WmsInnerStockOrderDet wmsInventoryVerificationDet = new WmsInnerStockOrderDet();
                wmsInventoryVerificationDet.setStockOrderId(id);
                wmsInventoryVerificationDet.setMaterialId(wmsInnerInventory.getMaterialId());
                wmsInventoryVerificationDet.setStorageId(wmsInnerInventory.getStorageId());
                wmsInventoryVerificationDet.setIfRegister((byte)2);
                wmsInventoryVerificationDet.setOriginalQty(wmsInnerInventory.getPackingQty());
                wmsInventoryVerificationDet.setCreateTime(new Date());
                wmsInventoryVerificationDet.setCreateUserId(sysUser.getUserId());
                wmsInventoryVerificationDet.setModifiedTime(new Date());
                wmsInventoryVerificationDet.setModifiedUserId(sysUser.getUserId());
                wmsInventoryVerificationDet.setOrganizationId(sysUser.getOrganizationId());
                wmsInventoryVerificationDet.setPalletCode(wmsInnerInventory.getPalletCode());
                wmsInventoryVerificationDet.setBatchCode(wmsInnerInventory.getBatchCode());
                wmsInventoryVerificationDet.setPackingUnitName(wmsInnerInventory.getPackingUnitName());
                wmsInventoryVerificationDet.setInventoryStatusId(wmsInnerInventory.getInventoryStatusId());
                wmsInventoryVerificationDet.setContractCode(wmsInnerInventory.getContractCode());
                wmsInventoryVerificationDet.setPurchaseReqOrderCode(wmsInnerInventory.getPurchaseReqOrderCode());
                wmsInventoryVerificationDet.setLocationNum(wmsInnerInventory.getOption4());
                wmsInventoryVerificationDet.setDominantTermCode(wmsInnerInventory.getOption2());
                wmsInventoryVerificationDet.setDeviceCode(wmsInnerInventory.getOption1());
                wmsInventoryVerificationDet.setMaterialPurpose(wmsInnerInventory.getOption3());
                wmsInventoryVerificationDet.setSupplierId(wmsInnerInventory.getSupplierId());
                list.add(wmsInventoryVerificationDet);
            }
        }
        if(list.size()<1){
            throw new BizErrorException("所选产品暂无库存");
        }
        return list;
    }

    private List<WmsInnerInventory> filtration(List<WmsInnerInventory> wmsInnerInventories){
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
    private List<WmsInnerStockOrderDet> findInvGoods(Byte type, Long id, List<Long> storageList,Long warehouseId){
        SysUser sysUser = currentUser();
        List<WmsInnerStockOrderDet> list = new ArrayList<>();
        //查询库位下所有库存货品
        //库位盘点
        if(type==1){
            for (Long storageId : storageList) {
                //获取库位名称
                Example example = new Example(WmsInnerInventory.class);
                //盘点锁 0 否 1 是
                example.createCriteria().andEqualTo("warehouseId",warehouseId)
                        .andEqualTo("storageId",storageId)
                        .andEqualTo("stockLock",0)
                        .andGreaterThan("packingQty",0)
                        .andEqualTo("jobStatus",1)
                        .andEqualTo("orgId",sysUser.getOrganizationId())
                        .andEqualTo("qcLock", 0)
                        .andEqualTo("lockStatus", 0);
                //获取库位库存
                List<WmsInnerInventory> wmsInnerInventories = wmsInnerInventoryMapper.selectByExample(example);
                wmsInnerInventories = this.filtration(wmsInnerInventories);
                for (WmsInnerInventory wmsInnerInventory : wmsInnerInventories) {
                    WmsInnerStockOrderDet wmsInventoryVerificationDet = new WmsInnerStockOrderDet();
                    wmsInventoryVerificationDet.setStockOrderId(id);
                    wmsInventoryVerificationDet.setMaterialId(wmsInnerInventory.getMaterialId());
                    wmsInventoryVerificationDet.setStorageId(storageId);
                    wmsInventoryVerificationDet.setIfRegister((byte)2);
                    wmsInventoryVerificationDet.setOriginalQty(wmsInnerInventory.getPackingQty());
                    wmsInventoryVerificationDet.setCreateTime(new Date());
                    wmsInventoryVerificationDet.setCreateUserId(sysUser.getUserId());
                    wmsInventoryVerificationDet.setModifiedTime(new Date());
                    wmsInventoryVerificationDet.setModifiedUserId(sysUser.getUserId());
                    wmsInventoryVerificationDet.setOrganizationId(sysUser.getOrganizationId());
                    wmsInventoryVerificationDet.setPalletCode(wmsInnerInventory.getPalletCode());
                    wmsInventoryVerificationDet.setBatchCode(wmsInnerInventory.getBatchCode());
                    wmsInventoryVerificationDet.setPackingUnitName(wmsInnerInventory.getPackingUnitName());
                    wmsInventoryVerificationDet.setInventoryStatusId(wmsInnerInventory.getInventoryStatusId());
                    wmsInventoryVerificationDet.setContractCode(wmsInnerInventory.getContractCode());
                    wmsInventoryVerificationDet.setPurchaseReqOrderCode(wmsInnerInventory.getPurchaseReqOrderCode());
                    wmsInventoryVerificationDet.setLocationNum(wmsInnerInventory.getOption4());
                    wmsInventoryVerificationDet.setDominantTermCode(wmsInnerInventory.getOption2());
                    wmsInventoryVerificationDet.setDeviceCode(wmsInnerInventory.getOption1());
                    wmsInventoryVerificationDet.setMaterialPurpose(wmsInnerInventory.getOption3());
                    wmsInventoryVerificationDet.setSupplierId(wmsInnerInventory.getSupplierId());
                    list.add(wmsInventoryVerificationDet);
                }
            }
        }else if(type==3){
            //全盘
            Example example = new Example(WmsInnerInventory.class);
            //盘点锁 0 否 1 是
            example.createCriteria().andEqualTo("warehouseId",warehouseId).andEqualTo("stockLock",0)
                    .andGreaterThan("packingQty",0).andEqualTo("jobStatus",1)
                    .andEqualTo("qcLock", 0)
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
                wmsInventoryVerificationDet.setIfRegister((byte)2);
                wmsInventoryVerificationDet.setOriginalQty(wmsInnerInventory.getPackingQty());
                wmsInventoryVerificationDet.setCreateTime(new Date());
                wmsInventoryVerificationDet.setCreateUserId(sysUser.getUserId());
                wmsInventoryVerificationDet.setModifiedTime(new Date());
                wmsInventoryVerificationDet.setModifiedUserId(sysUser.getUserId());
                wmsInventoryVerificationDet.setPalletCode(wmsInnerInventory.getPalletCode());
                wmsInventoryVerificationDet.setBatchCode(wmsInnerInventory.getBatchCode());
                wmsInventoryVerificationDet.setInventoryStatusId(wmsInnerInventory.getInventoryStatusId());
                wmsInventoryVerificationDet.setContractCode(wmsInnerInventory.getContractCode());
                wmsInventoryVerificationDet.setPurchaseReqOrderCode(wmsInnerInventory.getPurchaseReqOrderCode());
                wmsInventoryVerificationDet.setLocationNum(wmsInnerInventory.getOption4());
                wmsInventoryVerificationDet.setDominantTermCode(wmsInnerInventory.getOption2());
                wmsInventoryVerificationDet.setDeviceCode(wmsInnerInventory.getOption1());
                wmsInventoryVerificationDet.setMaterialPurpose(wmsInnerInventory.getOption3());
                wmsInventoryVerificationDet.setSupplierId(wmsInnerInventory.getSupplierId());
                list.add(wmsInventoryVerificationDet);
            }
        }
        if(list.size()<1){
            throw new BizErrorException("该条件暂无可盘点库存");
        }
        return list;
    }

    /**
     * 盘点激活
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor=RuntimeException.class)
    public int activation(String ids,Integer btnType){
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
            //库位盘点将盘点单的所有库位下库存更改上锁状态及基础信息库位上锁 货品盘点将
            if(btnType ==1){
                if(wmsInventoryVerification.getOrderStatus()>1){
                    throw new BizErrorException("重复操作");
                }
                //打开
                num += this.unlockOrLock((byte) 2,list,wmsInventoryVerification);
                wmsInventoryVerification.setOrderStatus((byte)2);
            }else if(btnType ==2){
                if(wmsInventoryVerification.getOrderStatus()>=5){
                    throw new BizErrorException("单据已完成,无法作废");
                }
                //作废
             num += this.unlockOrLock((byte) 1,list,wmsInventoryVerification);
             wmsInventoryVerification.setOrderStatus((byte)4);
            }
            num+=wmsInventoryVerificationMapper.updateByPrimaryKeySelective(wmsInventoryVerification);
        }
        return num;
    }

    /**
     * 盘点确认
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
            if(wmsInventoryVerification.getOrderStatus()!=3){
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
                    ws.setRelatedOrderCode(wmsInventoryVerification.getStockOrderCode());
                    ws.setStockOrderCode(CodeUtils.getId("INPD-"));
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
                            det.setLastTimeVarianceQty(wmsInventoryVerificationDet.getVarianceQty());
                            det.setOriginalQty(wmsInventoryVerificationDet.getOriginalQty());
                            det.setIfRegister((byte)2);
                            det.setCreateUserId(sysUser.getUserId());
                            det.setCreateTime(new Date());
                            det.setModifiedTime(new Date());
                            det.setModifiedUserId(sysUser.getUserId());
                            wmsInventoryVerificationDets.add(det);
                        }
                    }
                    num+=wmsInventoryVerificationDetMapper.insertList(wmsInventoryVerificationDets);
                }
            }
            wmsInventoryVerificationDets.clear();
            for (WmsInnerStockOrderDet wmsInventoryVerificationDet : list) {
                //盘点
                if(wmsInventoryVerification.getProjectType()==(byte)1){
                    //是否存在差异量 不存在则解锁库存盘点锁 存在则进行复盘
                    if(!StringUtils.isEmpty(wmsInventoryVerificationDet.getVarianceQty()) || wmsInventoryVerificationDet.getVarianceQty().compareTo(BigDecimal.ZERO)==0){
                        wmsInventoryVerificationDets.add(wmsInventoryVerificationDet);
                    }
                }
            }
            //解锁及复盘库存
            num+=this.unlockOrLock((byte)1,wmsInventoryVerificationDets,wmsInventoryVerification);
            if(wmsInventoryVerification.getProjectType()==2){
                wmsInventoryVerification.setOrderStatus((byte)5);
            }else {
                //更改盘点状态（已完成）
                wmsInventoryVerification.setOrderStatus((byte)6);
            }
            wmsInventoryVerification.setModifiedTime(new Date());
            wmsInventoryVerification.setModifiedUserId(sysUser.getUserId());
            num +=wmsInventoryVerificationMapper.updateByPrimaryKeySelective(wmsInventoryVerification);

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
            if(wmsInventoryVerification.getOrderStatus()==(byte)6){
                throw new BizErrorException("盘点已完成");
            }
            if(wmsInventoryVerification.getProjectType()==1){
                throw new BizErrorException("盘点无差异");
            }
            if(wmsInventoryVerification.getOrderStatus()!=5){
                throw new BizErrorException("盘点单未完成,无法差异处理");
            }
            Example example = new Example(WmsInnerStockOrderDet.class);
            example.createCriteria().andEqualTo("stockOrderId", wmsInventoryVerification.getStockOrderId());
            List<WmsInnerStockOrderDet> list = wmsInventoryVerificationDetMapper.selectByExample(example);
            List<WmsInnerStockOrderDet> wmsInventoryVerificationDets = new ArrayList<>();

            //差异处理盘盈盘亏计算
            num+=this.unlockOrLock((byte) 3,list,wmsInventoryVerification);

            wmsInventoryVerification.setOrderStatus((byte)6);
            num+=wmsInventoryVerificationMapper.updateByPrimaryKeySelective(wmsInventoryVerification);

        }
        return num;
    }

    @Override
    public int PdaAscertained(String ids) {
        //盘点确认
        WmsInnerStockOrder wmsInnerStockOrder = new WmsInnerStockOrder();
        wmsInnerStockOrder.setOrderStatus((byte)3);
        wmsInnerStockOrder.setStockOrderId(Long.parseLong(ids));
        int num = wmsInventoryVerificationMapper.updateByPrimaryKeySelective(wmsInnerStockOrder);
        if(num<1){
            throw new BizErrorException("盘点确认失败");
        }
        num+= this.ascertained(ids);
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int PdaCommit(WmsInnerStockOrderDet wmsInnerStockOrderDet){
        SysUser sysUser = currentUser();
        int num = 0;
            if(StringUtils.isEmpty(wmsInnerStockOrderDet.getStockOrderId())){
                wmsInnerStockOrderDet.setStockUserId(sysUser.getUserId());
                wmsInnerStockOrderDet.setCreateTime(new Date());
                wmsInnerStockOrderDet.setCreateUserId(sysUser.getUserId());
                wmsInnerStockOrderDet.setModifiedTime(new Date());
                wmsInnerStockOrderDet.setModifiedUserId(sysUser.getUserId());
                num+=wmsInventoryVerificationDetMapper.insertSelective(wmsInnerStockOrderDet);
            }else {
                wmsInnerStockOrderDet.setStockUserId(sysUser.getUserId());
                wmsInnerStockOrderDet.setModifiedTime(new Date());
                wmsInnerStockOrderDet.setModifiedUserId(sysUser.getUserId());
                num += wmsInventoryVerificationDetMapper.updateByPrimaryKeySelective(wmsInnerStockOrderDet);
            }
            return num;
    }

    /**
     * PDA扫码返回数量
     * @param barcode
     * @return
     */
    @Override
    public Map<String,Object> scanBarcode(String barcode) {
        SysUser sysUser = currentUser();
        Map<String,Object> map = new HashMap<>();
        //查询库存明细
        if(StringUtils.isEmpty(barcode)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }
        Example example = new Example(WmsInnerInventoryDet.class);

        example.createCriteria().andEqualTo("barcode",barcode);
        example.or().andEqualTo("salesBarcode", barcode);
        example.or().andEqualTo("customerBarcode", barcode);
        List<WmsInnerInventoryDet> list = wmsInnerInventoryDetMapper.selectByExample(example);
        if(list.size()<1){
            //获取产品物料信息
            Long storageId = wmsInventoryVerificationMapper.findMaterialId(barcode);
            if(StringUtils.isEmpty(storageId)){
                throw new BizErrorException("条码错误");
            }
            map.put("SN","false");
            map.put("qty",0);
            return map;
        }
        BigDecimal qty = list.stream()
                .map(WmsInnerInventoryDet::getMaterialQty)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        map.put("SN","true");
        map.put("qty",qty);
        return map;
    }

    /**
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
                criteria.andEqualTo("warehouseId",wmsInventoryVerification.getWarehouseId()).andEqualTo("storageId",wmsInnerStockOrderDet.getStorageId())
                        .andEqualTo("materialId",wmsInnerStockOrderDet.getMaterialId())
                        .andEqualTo("stockLock",lockType==(byte) 2?0:1)
                        .andEqualTo("orgId",sysUser.getOrganizationId())
                        .andEqualTo("jobStatus",1).andEqualTo("packingQty",wmsInnerStockOrderDet.getOriginalQty()).andEqualTo("qcLock", 0)
                        .andEqualTo("lockStatus", 0);
                if(StringUtils.isNotEmpty(wmsInnerStockOrderDet.getBatchCode())){
                    criteria.andEqualTo("batchCode",wmsInnerStockOrderDet.getBatchCode());
                }else {
                    criteria.andIsNull("batchCode");
                }
                if(lockType==(byte) 1){
                    criteria.andEqualTo("relevanceOrderCode",wmsInventoryVerification.getStockOrderCode());
                }
                List<WmsInnerInventory> wmsInnerInventories = wmsInnerInventoryMapper.selectByExample(example);

                //查询库存明细
                example = new Example(WmsInnerInventoryDet.class);
                example.createCriteria().andEqualTo("storageId",wmsInnerStockOrderDet.getStorageId())
                        .andEqualTo("materialId",wmsInnerStockOrderDet.getMaterialId())
                        .andEqualTo("ifStockLock",lockType==(byte) 2?0:1)
                        .andEqualTo("orgId",sysUser.getOrganizationId())
                        .andEqualTo("jobStatus",3).andEqualTo("qcLock", 0).andEqualTo("batchCode",wmsInnerStockOrderDet.getBatchCode());
                List<WmsInnerInventoryDet> wmsInnerInventoryDets = wmsInnerInventoryDetMapper.selectByExample(example);

                for (WmsInnerInventory wmsInnerInventory : wmsInnerInventories) {
                    if(lockType==1){
                        //合并库存
                        example.clear();
                        criteria = example.createCriteria();
                        criteria.andEqualTo("warehouseId",wmsInventoryVerification.getWarehouseId()).andEqualTo("storageId",wmsInnerStockOrderDet.getStorageId())
                                .andEqualTo("materialId",wmsInnerStockOrderDet.getMaterialId())
                                .andEqualTo("stockLock",0)
                                .andEqualTo("orgId",sysUser.getOrganizationId())
                                .andEqualTo("jobStatus",1)
                                .andEqualTo("qcLock",0)
                                .andEqualTo("lockStatus",0);
                        if(StringUtils.isNotEmpty(wmsInnerStockOrderDet.getBatchCode())){
                            criteria.andEqualTo("batchCode",wmsInnerStockOrderDet.getBatchCode());
                        }else {
                            criteria.andIsNull("batchCode");
                        }
                        WmsInnerInventory wmsInnerInventory1 = wmsInnerInventoryMapper.selectOneByExample(example);
                        if(StringUtils.isEmpty(wmsInnerInventory1)){
                            wmsInnerInventory.setStockLock((byte)0);
                            wmsInnerInventory.setRelevanceOrderCode(wmsInventoryVerification.getStockOrderCode());
                            num+=wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventory);
                        }else {
                            wmsInnerInventory1.setPackingQty(wmsInnerInventory1.getPackingQty().add(wmsInnerInventory.getPackingQty()));
                            wmsInnerInventory1.setRelevanceOrderCode(wmsInventoryVerification.getStockOrderCode());
                            wmsInnerInventoryMapper.deleteByPrimaryKey(wmsInnerInventory);
                        }
                    }else {
                        wmsInnerInventory.setStockLock(lockType==(byte) 2?(byte)1:0);
                        wmsInnerInventory.setRelevanceOrderCode(wmsInventoryVerification.getStockOrderCode());
                        num+=wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventory);
                    }
                }

                for (WmsInnerInventoryDet wmsInnerInventoryDet : wmsInnerInventoryDets) {
                    if(lockType==1){
                        //解锁
                        wmsInnerInventoryDet.setIfStockLock((byte)0);
                    }else {
                        wmsInnerInventoryDet.setIfStockLock((byte)1);
                    }
                    wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(wmsInnerInventoryDet);
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
     * 复盘盘盈盘亏
     * @param type 1不扫条码 3-PDA扫条码盘点
     * @param wmsInnerStockOrderDet
     * @return
     */
    private int analyse(byte type,String barcode,WmsInnerStockOrderDet wmsInnerStockOrderDet,WmsInnerStockOrder wmsInnerStockOrder){
            //更新库存 盘盈 根据盘点单生成一条库存 盘亏 按顺序扣减库存
            //解锁库存
        int num = 0;
        WmsInnerInventoryLog wmsInnerInventoryLog = new WmsInnerInventoryLogDto();
            Example example = new Example(WmsInnerInventory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("warehouseId",wmsInnerStockOrder.getWarehouseId()).andEqualTo("storageId",wmsInnerStockOrderDet.getStorageId())
                    .andEqualTo("materialId",wmsInnerStockOrderDet.getMaterialId())
                    .andEqualTo("relevanceOrderCode",wmsInnerStockOrder.getRelatedOrderCode())
                    .andEqualTo("stockLock",1)
                    .andEqualTo("orgId",wmsInnerStockOrder.getOrganizationId())
                    .andGreaterThan("packingQty",0).andEqualTo("jobStatus",1).andEqualTo("packingQty",wmsInnerStockOrderDet.getOriginalQty());
            if(StringUtils.isNotEmpty(wmsInnerStockOrderDet.getBatchCode())){
                criteria.andEqualTo("batchCode",wmsInnerStockOrderDet.getBatchCode());
            }else {
                criteria.andIsNull("batchCode");
            }
            WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
            example.clear();
            criteria = example.createCriteria();
                    criteria.andEqualTo("warehouseId",wmsInnerStockOrder.getWarehouseId()).andEqualTo("storageId",wmsInnerStockOrderDet.getStorageId())
                    .andEqualTo("materialId",wmsInnerStockOrderDet.getMaterialId())
                    .andEqualTo("stockLock",0)
                    .andEqualTo("orgId",wmsInnerStockOrder.getOrganizationId())
                    .andEqualTo("jobStatus",1)
                    .andEqualTo("qcLock",0)
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
            wmsInnerInventory1.setRelevanceOrderCode(wmsInnerStockOrder.getStockOrderCode());
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
                //盘点数大于库存数 原有数量新增
//        Byte addOrSubtract = null;
//        if(StringUtils.isEmpty(wmsInnerInventory)){
//            wmsInnerInventory = new WmsInnerInventory();
//            wmsInnerInventory.setWarehouseId(wmsInnerStockOrder.getWarehouseId());
//            wmsInnerInventory.setStorageId(wmsInnerStockOrderDet.getStorageId());
//            wmsInnerInventory.setPackingUnitName(wmsInnerStockOrderDet.getPackingUnitName());
//            wmsInnerInventory.setPackingQty(wmsInnerStockOrderDet.getStockQty());
//            wmsInnerInventory.setRelevanceOrderCode(wmsInnerStockOrder.getStockOrderCode());
//            wmsInnerInventory.setStockLock((byte)0);
//            wmsInnerInventory.setInventoryStatusId(wmsInnerStockOrderDet.getInventoryStatusId());
//            wmsInnerInventory.setMaterialId(wmsInnerStockOrderDet.getMaterialId());
//            wmsInnerInventory.setJobStatus((byte)1);
//            wmsInnerInventory.setLockStatus((byte)0);
//            wmsInnerInventory.setQcLock((byte)0);
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
                wmsInnerInventoryDet.setIfStockLock((byte)0);
                if(StringUtils.isEmpty(wmsInnerInventoryDet.getMaterialQty())||wmsInnerInventoryDet.getMaterialQty().compareTo(wmsInnerStockOrderDet.getStockQty())==-1){
                    //盘盈
                    wmsInnerInventoryDet.setMaterialQty(wmsInnerInventoryDet.getMaterialQty().add(wmsInnerStockOrderDet.getVarianceQty()));
                    num+=wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(wmsInnerInventoryDet);
                }else {
                    //盘亏
                    wmsInnerInventoryDet.setMaterialQty(wmsInnerInventoryDet.getMaterialQty().subtract(wmsInnerStockOrderDet.getVarianceQty()));
                    num+=wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(wmsInnerInventoryDet);
                }

            }
        //添加库存日志
        if(wmsInnerInventoryLog.getChangeQty().compareTo(BigDecimal.ZERO)==1){
            wmsInnerInventoryLog.setRelatedOrderCode(wmsInnerStockOrder.getStockOrderCode());
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
