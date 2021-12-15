package com.fantechs.provider.wms.inner.util;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryLog;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerInventoryLogService;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author
 * @Date 2021/12/11
 * 库存操作类
 */
@Component
public class WmsInnerInventoryUtil {

    @Resource
    private WmsInnerInventoryMapper wmsInnerInventoryMapper;

    private static WmsInnerInventoryUtil wmsInnerInventoryUtil;

    @PostConstruct
    public void init(){
        wmsInnerInventoryUtil = this;
        wmsInnerInventoryUtil.wmsInnerInventoryMapper=wmsInnerInventoryMapper;
    }

    /**
     * 上架或拣货分配库存
     * @param wmsInnerJobOrder 拣货/上架
     * @param wmsInnerJobOrderDet 拣货/上架
     * @param sysUser 用户
     * @param addOrSubtract 加减类型(1-加 2-减)
     */
    public static int distributionInventory(WmsInnerJobOrder wmsInnerJobOrder, WmsInnerJobOrderDet wmsInnerJobOrderDet,BigDecimal finalQty,SysUser sysUser,byte addOrSubtract) {
        int num=0;
        // 上架 增加分配库存 库位是收货库位
        if(wmsInnerJobOrder.getJobOrderType()==((byte) 1)){
            Example example = new Example(WmsInnerInventory.class);
            Example.Criteria criteria1 = example.createCriteria();
            criteria1.andEqualTo("relevanceOrderCode", wmsInnerJobOrder.getJobOrderCode())
                    .andEqualTo("materialId", wmsInnerJobOrderDet.getMaterialId())
                    .andEqualTo("warehouseId", wmsInnerJobOrder.getWarehouseId())
                    .andEqualTo("storageId", wmsInnerJobOrderDet.getOutStorageId())
                    .andEqualTo("inventoryStatusId", wmsInnerJobOrderDet.getInventoryStatusId());
            if (!StringUtils.isEmpty(wmsInnerJobOrderDet.getBatchCode())) {
                criteria1.andEqualTo("batchCode", wmsInnerJobOrderDet.getBatchCode());
            }
            criteria1.andEqualTo("jobOrderDetId", wmsInnerJobOrderDet.getJobOrderDetId());
            criteria1.andEqualTo("jobStatus", (byte) 2);
            criteria1.andEqualTo("orgId", sysUser.getOrganizationId());
            WmsInnerInventory wmsInnerInventorys = wmsInnerInventoryUtil.wmsInnerInventoryMapper.selectOneByExample(example);
            if(addOrSubtract==((byte)2) && StringUtils.isEmpty(wmsInnerInventorys)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"库存数据不存在");
            }
            if (addOrSubtract==((byte)1) && StringUtils.isEmpty(wmsInnerInventorys)) {
                //添加库存
                WmsInnerInventory inv = new WmsInnerInventory();
                inv.setStorageId(wmsInnerJobOrderDet.getOutStorageId());
                inv.setWarehouseId(wmsInnerJobOrder.getWarehouseId());
                inv.setRelevanceOrderCode(wmsInnerJobOrder.getJobOrderCode());
                inv.setPackingQty(finalQty);
                inv.setJobStatus((byte) 2);
                inv.setInventoryId(null);
                inv.setBatchCode(wmsInnerJobOrderDet.getBatchCode());
                inv.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
                inv.setOrgId(sysUser.getOrganizationId());
                inv.setCreateUserId(sysUser.getUserId());
                inv.setCreateTime(new Date());
                inv.setModifiedTime(new Date());
                inv.setModifiedUserId(sysUser.getUserId());
                //记录库存日志
                BigDecimal qty=new BigDecimal(0);
                BigDecimal chaQty=finalQty;
                InventoryLogUtil.addLog(wmsInnerInventorys,wmsInnerJobOrder,wmsInnerJobOrderDet,qty,chaQty,(byte)2,(byte)1);
                return wmsInnerInventoryUtil.wmsInnerInventoryMapper.insertSelective(inv);
            } else {
                //原库存
                BigDecimal qty=wmsInnerInventorys.getPackingQty();
                BigDecimal chaQty=wmsInnerJobOrderDet.getDistributionQty();
                if(addOrSubtract==((byte)1)){

                    wmsInnerInventorys.setPackingQty(wmsInnerInventorys.getPackingQty().add(finalQty));
                    //记录库存日志
                    InventoryLogUtil.addLog(wmsInnerInventorys,wmsInnerJobOrder,wmsInnerJobOrderDet,qty,chaQty,(byte)2,(byte)1);
                    return wmsInnerInventoryUtil.wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
                }
                else if(addOrSubtract==((byte)2)) {
                    wmsInnerInventorys.setPackingQty(wmsInnerInventorys.getPackingQty().subtract(finalQty));
                    //记录库存日志
                    InventoryLogUtil.addLog(wmsInnerInventorys,wmsInnerJobOrder,wmsInnerJobOrderDet,qty,chaQty,(byte)2,(byte)2);
                    return wmsInnerInventoryUtil.wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
                }
            }
        }

        return num;
    }

    /**
     * 更新库存
     *
     * @return
     */
    public static int updateInventory(WmsInnerJobOrder wmsInnerJobOrder, WmsInnerJobOrderDetDto newDto,SysUser sysUser) {
        int num=0;
        if(wmsInnerJobOrder.getJobOrderType()==((byte) 1)) {
            //增加移入库位库存 作业状态 jobStatus (1正常 2待出)为正常的库存
            Example example = new Example(WmsInnerInventory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("materialId", newDto.getMaterialId());
            criteria.andEqualTo("warehouseId", wmsInnerJobOrder.getWarehouseId());
            criteria.andEqualTo("storageId", newDto.getInStorageId());
            if (!StringUtils.isEmpty(newDto.getBatchCode())) {
                criteria.andEqualTo("batchCode", newDto.getBatchCode());
            }
            criteria.andEqualTo("jobStatus", (byte) 1);
            criteria.andEqualTo("inventoryStatusId", newDto.getInventoryStatusId());
            criteria.andEqualTo("stockLock", 0).andEqualTo("qcLock", 0).andEqualTo("lockStatus", 0);
            criteria.andEqualTo("orgId", sysUser.getOrganizationId());
            WmsInnerInventory wmsInnerInventorys = wmsInnerInventoryUtil.wmsInnerInventoryMapper.selectOneByExample(example);
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
                inv.setStockLock((byte) 0);
                inv.setQcLock((byte) 0);
                inv.setLockStatus((byte) 0);
                inv.setOrgId(sysUser.getOrganizationId());
                inv.setPackingQty(newDto.getActualQty());
                inv.setCreateUserId(sysUser.getUserId());
                inv.setCreateTime(new Date());
                inv.setModifiedTime(new Date());
                inv.setModifiedUserId(sysUser.getUserId());
                inv.setRelevanceOrderCode(wmsInnerJobOrder.getJobOrderCode());
                //inv.setJobOrderDetId(newDto.getJobOrderDetId());

                num += wmsInnerInventoryUtil.wmsInnerInventoryMapper.insertSelective(inv);
            } else {
                qty = wmsInnerInventorys.getPackingQty();
                //原库存
                wmsInnerInventorys.setPackingQty(wmsInnerInventorys.getPackingQty().add(newDto.getActualQty()));
                wmsInnerInventorys.setRelevanceOrderCode(wmsInnerJobOrder.getJobOrderCode());
                wmsInnerInventorys.setModifiedTime(new Date());
                wmsInnerInventorys.setModifiedUserId(sysUser.getUserId());
                num += wmsInnerInventoryUtil.wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
            }
            //记录库存日志
            InventoryLogUtil.addLog(wmsInnerInventorys, wmsInnerJobOrder, newDto, qty, newDto.getActualQty(), (byte) 2, (byte) 1);
        }

        return num;
    }

}
