package com.fantechs.provider.wms.inner.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerJobOrderDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerJobOrderMapper;
import com.fantechs.provider.wms.inner.service.PickingOrderService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author mr.lei
 * @Date 2021/5/10
 */
@Service
public class PickingOrderServiceImpl implements PickingOrderService {

    @Resource
    private WmsInnerJobOrderMapper wmsInnerJobOrderMapper;
    @Resource
    private WmsInnerJobOrderDetMapper wmsInnerJobOrderDetMapper;
    @Resource
    private WmsInnerInventoryMapper wmsInnerInventoryMapper;

    @Override
    public int scanAffirmQty(Long jobOrderId,String barCode) {
        SysUser sysUser = currentUser();

        //检验条码

        WmsInnerJobOrderDet wmsInnerJobOrderDet = wmsInnerJobOrderDetMapper.selectByPrimaryKey(jobOrderId);
        wmsInnerJobOrderDet.setActualQty(wmsInnerJobOrderDet.getActualQty().add(new BigDecimal("1")));
            wmsInnerJobOrderDet.setModifiedTime(new Date());
            wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
            int num =allocationInv(wmsInnerJobOrderDet);
        return num;
    }

    @Override
    public int autoDistribution(String ids) {
        SysUser sysUser = currentUser();
        String[] arrIds = ids.split(",");
        int num = 0;
        for (String arrId : arrIds) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(arrId);
            if(StringUtils.isEmpty(wmsInnerJobOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId",arrIds);
            List<WmsInnerJobOrderDet> list = wmsInnerJobOrderDetMapper.selectByExample(example);
            for (WmsInnerJobOrderDet wmsInnerJobOrderDet : list) {
                wmsInnerJobOrderDet.setDistributionQty(wmsInnerJobOrderDet.getPlanQty());
                wmsInnerJobOrderDet.setModifiedTime(new Date());
                wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
                num +=moveInventory(wmsInnerJobOrderDet);
            }
        }
        return num;
    }

    @Override
    public int handDistribution(List<WmsInnerJobOrderDet> list) {
        SysUser sysUser = currentUser();
        int num = 0;
        for (WmsInnerJobOrderDet wmsInnerJobOrderDet : list) {
            wmsInnerJobOrderDet.setModifiedTime(new Date());
            wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
            num +=moveInventory(wmsInnerJobOrderDet);
        }
        return num;
    }

    @Override
    public int cancelDistribution(String ids) {
        return 0;
    }

    @Override
    public int allReceiving(String ids) {
        SysUser sysUser = currentUser();
        String[] arrIds = ids.split(",");
        int num = 0;
        for (String arrId : arrIds) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(arrId);
            if(StringUtils.isEmpty(wmsInnerJobOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId",arrIds);
            List<WmsInnerJobOrderDet> list = wmsInnerJobOrderDetMapper.selectByExample(example);
            for (WmsInnerJobOrderDet wmsInnerJobOrderDet : list) {
                if(wmsInnerJobOrderDet.getDistributionQty()!=null && wmsInnerJobOrderDet.getPlanQty().compareTo(wmsInnerJobOrderDet.getDistributionQty())==0){
                    wmsInnerJobOrderDet.setActualQty(wmsInnerJobOrderDet.getPlanQty());
                    wmsInnerJobOrderDet.setModifiedTime(new Date());
                    wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
                    num +=allocationInv(wmsInnerJobOrderDet);
                }else{
                    throw new BizErrorException("数量未分配完");
                }
            }
        }
        return num;
    }

    @Override
    public int singleReceiving(List<WmsInnerJobOrderDet> wmsInPutawayOrderDets) {
        SysUser sysUser = currentUser();
        int num = 0;
        for (WmsInnerJobOrderDet wmsInnerJobOrderDet : wmsInPutawayOrderDets) {
            wmsInnerJobOrderDet.setModifiedTime(new Date());
            wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
            num +=allocationInv(wmsInnerJobOrderDet);
        }
        return num;
    }

    private int allocationInv(WmsInnerJobOrderDet wmsInnerJobOrderDet){
        SysUser sysUser = currentUser();
        int num = 0;
        Example example = new Example(WmsInnerInventory.class);
        example.createCriteria().andEqualTo("warehouseId",wmsInnerJobOrderDet.getWarehouseId()).andEqualTo("storageId",wmsInnerJobOrderDet.getOutStorageId())
                .andEqualTo("materialId",wmsInnerJobOrderDet.getMaterialId()).andEqualTo("batchCode",wmsInnerJobOrderDet.getBatchCode());
        List<WmsInnerInventory> list = wmsInnerInventoryMapper.selectByExample(example);
        if(list.size()<1){
            throw new BizErrorException("未查询到库存");
        }
        //WmsInnerInventory oldInventory = list.get(0);

        WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDet.getJobOrderId());
        if(StringUtils.isEmpty(wmsInnerJobOrder)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        example.clear();
        example.createCriteria().andEqualTo("relevanceOrderCode",wmsInnerJobOrder.getJobOrderCode()).andEqualTo("storageId",wmsInnerJobOrderDet.getOutStorageId())
                .andEqualTo("materialId",wmsInnerJobOrderDet.getMaterialId()).andEqualTo("batchCode",wmsInnerJobOrderDet.getBatchCode());
        List<WmsInnerInventory> newList = wmsInnerInventoryMapper.selectByExample(example);
        if(list.get(0).getPackingQty().compareTo(wmsInnerJobOrderDet.getActualQty())==-1){
            throw new BizErrorException("库存不足");
        }
        if(newList.size()<1){
            //新增代出库存
            WmsInnerInventory newInventory = new WmsInnerInventory();
            BeanUtil.copyProperties(list.get(0),newInventory);
            newInventory.setJobStatus((byte)3);
            newInventory.setInventoryId(null);
            newInventory.setCreateTime(new Date());
            newInventory.setCreateUserId(sysUser.getUserId());
            newInventory.setModifiedTime(new Date());
            newInventory.setPackingQty(wmsInnerJobOrderDet.getActualQty());
            newInventory.setModifiedUserId(sysUser.getUserId());
            num += wmsInnerInventoryMapper.insertSelective(newInventory);

            //扣除原库存
           WmsInnerInventory oldInventory = list.get(0);
           oldInventory.setPackingQty(oldInventory.getPackingQty().subtract(wmsInnerJobOrderDet.getActualQty()));
           wmsInnerInventoryMapper.updateByPrimaryKeySelective(oldInventory);
        }else{
            WmsInnerInventory newInventory = new WmsInnerInventory();
            newInventory.setInventoryId(newList.get(0).getInventoryId());
            newInventory.setPackingQty(wmsInnerJobOrderDet.getActualQty());
            newInventory.setModifiedUserId(sysUser.getUserId());
            newInventory.setModifiedTime(new Date());
            num+=wmsInnerInventoryMapper.updateByPrimaryKeySelective(newInventory);

            WmsInnerInventory oldInventory = list.get(0);
            oldInventory.setPackingQty(oldInventory.getPackingQty().subtract(wmsInnerJobOrderDet.getActualQty()));
            wmsInnerInventoryMapper.updateByPrimaryKeySelective(oldInventory);
        }
        return num;
    }

    private int moveInventory(WmsInnerJobOrderDet wmsInnerJobOrderDet){
        SysUser sysUser = currentUser();
        int num = 0;
        String newStorageName = wmsInnerJobOrderMapper.findStorageName(wmsInnerJobOrderDet.getOutStorageId());
        WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDet.getJobOrderId());
        if(StringUtils.isEmpty(wmsInnerJobOrder)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        WmsInnerJobOrderDet oldDet = wmsInnerJobOrderDetMapper.selectByPrimaryKey(wmsInnerJobOrderDet.getJobOrderDetId());
        String oldStorageName = wmsInnerJobOrderMapper.findStorageName(oldDet.getOutStorageId());
        Example example = new Example(WmsInnerInventory.class);
        //分配库位
        example.createCriteria().andEqualTo("relevanceOrderCode",wmsInnerJobOrder.getJobOrderCode()).andEqualTo("storageName",oldStorageName)
                .andEqualTo("materialId",wmsInnerJobOrderDet.getMaterialId()).andEqualTo("batchCode",wmsInnerJobOrderDet.getBatchCode());
        List<WmsInnerInventory> oldlist = wmsInnerInventoryMapper.selectByExample(example);

        //确认库位
        example.clear();
        example.createCriteria().andEqualTo("relevanceOrderCode",wmsInnerJobOrder.getJobOrderCode()).andEqualTo("storageName",newStorageName)
                .andEqualTo("materialId",wmsInnerJobOrderDet.getMaterialId()).andEqualTo("batchCode",wmsInnerJobOrderDet.getBatchCode());
        List<WmsInnerInventory> newList = wmsInnerInventoryMapper.selectByExample(example);
        if(oldlist.get(0).getPackingQty().compareTo(wmsInnerJobOrderDet.getDistributionQty())==-1){
            throw new BizErrorException("库存不足");
        }
        if(newList.size()<1){
            //新增代出库存
            WmsInnerInventory newInventory = new WmsInnerInventory();
            BeanUtil.copyProperties(oldlist.get(0),newInventory);
            newInventory.setJobStatus((byte)3);
            newInventory.setInventoryId(null);
            newInventory.setStorageName(newStorageName);
            newInventory.setCreateTime(new Date());
            newInventory.setCreateUserId(sysUser.getUserId());
            newInventory.setModifiedTime(new Date());
            newInventory.setPackingQty(wmsInnerJobOrderDet.getDistributionQty());
            newInventory.setModifiedUserId(sysUser.getUserId());
            num += wmsInnerInventoryMapper.insertSelective(newInventory);

            //扣除原库存
            WmsInnerInventory oldInventory = oldlist.get(0);
            oldInventory.setPackingQty(oldInventory.getPackingQty().subtract(wmsInnerJobOrderDet.getDistributionQty()));
            wmsInnerInventoryMapper.updateByPrimaryKeySelective(oldInventory);
        }else{
            WmsInnerInventory newInventory = new WmsInnerInventory();
            newInventory.setInventoryId(newList.get(0).getInventoryId());
            newInventory.setPackingQty(wmsInnerJobOrderDet.getDistributionQty());
            newInventory.setModifiedUserId(sysUser.getUserId());
            newInventory.setModifiedTime(new Date());
            num+=wmsInnerInventoryMapper.updateByPrimaryKeySelective(newInventory);

            WmsInnerInventory oldInventory = oldlist.get(0);
            oldInventory.setPackingQty(oldInventory.getPackingQty().subtract(wmsInnerJobOrderDet.getDistributionQty()));
            wmsInnerInventoryMapper.updateByPrimaryKeySelective(oldInventory);
        }
        return num;
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
}
