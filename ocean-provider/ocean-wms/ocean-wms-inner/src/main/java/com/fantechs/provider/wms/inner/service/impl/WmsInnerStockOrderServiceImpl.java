package com.fantechs.provider.wms.inner.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStockOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerStockOrderDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerStockOrderMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerStockOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<WmsInnerStockOrderDto> findList(Map<String, Object> map) {
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
        record.setOrderStatus((byte)1);
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        int num = wmsInventoryVerificationMapper.insertUseGeneratedKeys(record);
        //库位盘点/全盘
        if(record.getStockType()==(byte)1 && record.getStockType()==(byte)3){
            if(record.getStockType()==(byte)1&&record.getMaxStorageCount()> record.getStorageList().size()){
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
            for (WmsInnerStockOrderDet inventoryVerificationDet : record.getInventoryVerificationDets()) {
                inventoryVerificationDet.setStockOrderId(record.getStockOrderId());
                inventoryVerificationDet.setCreateUserId(sysUser.getUserId());
                inventoryVerificationDet.setCreateTime(new Date());
                inventoryVerificationDet.setModifiedUserId(sysUser.getUserId());
                inventoryVerificationDet.setModifiedTime(new Date());
            }
            int res = wmsInventoryVerificationDetMapper.insertList(record.getInventoryVerificationDets());
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
        //删除原有数据
        Example example = new Example(WmsInnerStockOrderDet.class);
        example.createCriteria().andEqualTo("stockOrderId",entity.getStockOrderId());
        wmsInventoryVerificationDetMapper.deleteByExample(example);
        for (WmsInnerStockOrderDet inventoryVerificationDet : entity.getInventoryVerificationDets()) {
            inventoryVerificationDet.setStockOrderId(entity.getStockOrderId());
            inventoryVerificationDet.setCreateUserId(sysUser.getUserId());
            inventoryVerificationDet.setCreateTime(new Date());
            inventoryVerificationDet.setModifiedUserId(sysUser.getUserId());
            inventoryVerificationDet.setModifiedTime(new Date());
            if(entity.getType()==(byte) 2){
                inventoryVerificationDet.setStockUserId(sysUser.getUserId());
            }
        }
        int num = wmsInventoryVerificationDetMapper.insertList(entity.getInventoryVerificationDets());
        num+=wmsInventoryVerificationMapper.updateByPrimaryKeySelective(entity);
        return num;
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
        //获取仓库名称
        String warehouseName = wmsInventoryVerificationMapper.findWarehouseName(warehouseId);
        if(StringUtils.isEmpty(warehouseName)){
            throw new BizErrorException("获取仓库失败");
        }
        //查询库位下所有库存货品
        //库位盘点
        if(type==2){
            for (Long storageId : storageList) {
                //获取库位名称
                String storageName = wmsInventoryVerificationMapper.findStorageName(storageId);
                Example example = new Example(WmsInnerInventory.class);
                //盘点锁 0 否 1 是
                example.createCriteria().andEqualTo("warehouseName",warehouseName).andEqualTo("storageName",storageName).andEqualTo("stockLock",0);
                //获取库位库存
                List<WmsInnerInventory> wmsInnerInventories = wmsInnerInventoryMapper.selectByExample(example);
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
                    list.add(wmsInventoryVerificationDet);
                }
            }
        }else if(type==3){
            //全盘
            Example example = new Example(WmsInnerInventory.class);
            //盘点锁 0 否 1 是
            example.createCriteria().andEqualTo("warehouseName",warehouseName).andEqualTo("stockLock",0);
            //获取库位库存
            List<WmsInnerInventory> wmsInnerInventories = wmsInnerInventoryMapper.selectByExample(example);
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
                list.add(wmsInventoryVerificationDet);
            }
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
    public int activation(String ids,Byte btnType){
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
            if(btnType ==(byte)1){
                //打开
                num += this.unlockOrLock((byte) 2,list,wmsInventoryVerification);
                wmsInventoryVerification.setOrderStatus((byte)2);
            }else if(btnType ==(byte)2){
                //作废
             num += this.unlockOrLock((byte) 1,list,wmsInventoryVerification);
             wmsInventoryVerification.setOrderStatus((byte)4);
            }
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
            Example example = new Example(WmsInnerStockOrderDet.class);
            example.createCriteria().andEqualTo("stockOrderId", wmsInventoryVerification.getStockOrderId());
            List<WmsInnerStockOrderDet> list = wmsInventoryVerificationDetMapper.selectByExample(example);
            List<WmsInnerStockOrderDet> wmsInventoryVerificationDets = new ArrayList<>();
            for (WmsInnerStockOrderDet wmsInventoryVerificationDet : list) {
                //盘点
                if(wmsInventoryVerification.getProjectType()==(byte)1){
                    //是否存在差异量 不存在则解锁库存盘点锁 存在则进行复盘
                    if(!StringUtils.isEmpty(wmsInventoryVerificationDet.getVarianceQty())&&wmsInventoryVerificationDet.getVarianceQty().compareTo(BigDecimal.ZERO)==1){
                        wmsInventoryVerificationDets.add(wmsInventoryVerificationDet);
                    }
                }else{
                    //复盘 //解锁更新库存
                    wmsInventoryVerificationDets.add(wmsInventoryVerificationDet);
                }
            }
            //解锁及复盘库存
            num+=this.unlockOrLock((byte) 3,wmsInventoryVerificationDets,wmsInventoryVerification);
            //更改盘点状态（已完成）
            wmsInventoryVerification.setOrderStatus((byte)5);
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
            Example example = new Example(WmsInnerStockOrderDet.class);
            example.createCriteria().andEqualTo("stockOrderId", wmsInventoryVerification.getStockOrderId());
            List<WmsInnerStockOrderDet> list = wmsInventoryVerificationDetMapper.selectByExample(example);
            List<WmsInnerStockOrderDet> wmsInventoryVerificationDets = new ArrayList<>();

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
                if(StringUtils.isEmpty(wmsInventoryVerificationDet.getVarianceQty())&&wmsInventoryVerificationDet.getVarianceQty().compareTo(BigDecimal.ZERO)==1){
                    WmsInnerStockOrderDet det = new WmsInnerStockOrderDet();
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
        return num;
    }

    /**
     * 库存上锁或解锁 1解锁 2上锁 3复盘
     * @param list
     * @return
     */
    private int unlockOrLock(byte lockType, List<WmsInnerStockOrderDet> list, WmsInnerStockOrder wmsInventoryVerification){
        SysUser sysUser = currentUser();
        int num = 0;
//        for (WmsInventoryVerificationDet wmsInventoryVerificationDet : list) {
//            //库存按库位物料上盘点锁
//            SmtStorageInventory smtStorageInventory = new SmtStorageInventory();
//            smtStorageInventory.setStorageId(wmsInventoryVerificationDet.getStorageId());
//            smtStorageInventory.setMaterialId(wmsInventoryVerificationDet.getMaterialId());
//            smtStorageInventory.setStocktakeLock(lockType==(byte) 2?(byte)2:1);
//            ResponseEntity responseEntity = storageInventoryFeignApi.lock(smtStorageInventory);
//            if(responseEntity.getCode()!=0){
//                throw new BizErrorException(responseEntity.getCode(),"激活失败："+responseEntity.getMessage());
//            }
//            //复盘
//            if(lockType==(byte) 3){
//                //更新库存 盘盈 根据盘点单生成一条库存 盘亏 按顺序扣减库存
//                SearchSmtStorageInventory searchSmtStorageInventory = new SearchSmtStorageInventory();
//                searchSmtStorageInventory.setStorageId(wmsInventoryVerificationDet.getStorageId());
//                searchSmtStorageInventory.setMaterialId(wmsInventoryVerificationDet.getMaterialId());
//                searchSmtStorageInventory.setStocktakeLock((byte)2);
//                ResponseEntity<List<SmtStorageInventoryDto>> res = storageInventoryFeignApi.findList(searchSmtStorageInventory);
//                if(res.getCode()!=0){
//                    throw new BizErrorException("获取库存失败");
//                }
//                SmtStorageInventoryDto smtStorageInventoryDtos = res.getData().get(0);
//                //盘点数大于库存数 新增一条盘单库存
//                if(!StringUtils.isEmpty(wmsInventoryVerificationDet.getInventoryQty()) && wmsInventoryVerificationDet.getInventoryQty().compareTo(smtStorageInventoryDtos.getQuantity())==1){
//                    SmtStorageInventoryDet smtStorageInventoryDet = new SmtStorageInventoryDet();
//                    smtStorageInventoryDet.setStorageInventoryId(smtStorageInventoryDtos.getStorageInventoryId());
//                    smtStorageInventoryDet.setGodownEntry(wmsInventoryVerification.getInventoryVerificationCode());
//                    smtStorageInventoryDet.setMaterialQuantity(wmsInventoryVerificationDet.getInventoryQty());
//                    smtStorageInventoryDet.setProductionCode(wmsInventoryVerificationDet.getBatchCode());
//                    smtStorageInventoryDet.setCreateUserId(sysUser.getUserId());
//                    smtStorageInventoryDet.setCreateTime(new Date());
//                    smtStorageInventoryDet.setModifiedTime(new Date());
//                    smtStorageInventoryDet.setModifiedUserId(sysUser.getUserId());
//                }else{
//                    //盘点数小于库存数 按顺序扣减库存
//                    SearchSmtStorageInventoryDet searchSmtStorageInventoryDet = new SearchSmtStorageInventoryDet();
//                    searchSmtStorageInventoryDet.setStorageInventoryId(smtStorageInventoryDtos.getStorageInventoryId());
//                    ResponseEntity<List<SmtStorageInventoryDetDto>> rs = storageInventoryFeignApi.findStorageInventoryDetList(searchSmtStorageInventoryDet);
//                    if(rs.getCode()!=0){
//                        throw new BizErrorException("获取库存失败");
//                    }
//                    List<SmtStorageInventoryDetDto> smtStorageInventoryDetDtoList = rs.getData();
//                    BigDecimal qty = wmsInventoryVerificationDet.getInventoryQty();
//                    for (SmtStorageInventoryDetDto smtStorageInventoryDetDto : smtStorageInventoryDetDtoList) {
//                        //如果数量等于0结束
//                        if(qty.compareTo(BigDecimal.ZERO)==0){
//                            break;
//                        }
//                        if(qty.compareTo(smtStorageInventoryDetDto.getMaterialQuantity())==1){
//                            //扣减库存
//                            if(smtStorageInventoryDetDto.getMaterialQuantity().compareTo(BigDecimal.ZERO)==1){
//                                SmtStorageInventoryDet smtStorageInventoryDet = new SmtStorageInventoryDet();
//                                smtStorageInventoryDet.setStorageInventoryDetId(smtStorageInventoryDetDto.getStorageInventoryDetId());
//                                if(qty.compareTo(smtStorageInventoryDetDto.getMaterialQuantity())==1){
//                                    smtStorageInventoryDet.setMaterialQuantity(BigDecimal.ZERO);
//                                }else{
//                                    smtStorageInventoryDet.setMaterialQuantity(smtStorageInventoryDetDto.getMaterialQuantity().subtract(qty));
//                                }
//                                qty.subtract(smtStorageInventoryDetDto.getMaterialQuantity());
//                                ResponseEntity reDet = storageInventoryFeignApi.updateStorageInventoryDet(smtStorageInventoryDet);
//                                if(reDet.getCode()!=0){
//                                    throw new BizErrorException("盘点库存失败");
//                                }
//                            }
//                        }
//                    }
//                    //更新主表库存数
//                    smtStorageInventory = new SmtStorageInventory();
//                    smtStorageInventory.setStorageInventoryId(smtStorageInventoryDtos.getStorageInventoryId());
//                    smtStorageInventory.setQuantity(smtStorageInventoryDtos.getQuantity().subtract(wmsInventoryVerificationDet.getInventoryQty()));
//                    ResponseEntity resmt = storageInventoryFeignApi.update(smtStorageInventory);
//                    if(resmt.getCode()!=0){
//                        throw new BizErrorException("库存盘点失败");
//                    }
//                }
//            }
//            num++;
//        }
        return num;
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
