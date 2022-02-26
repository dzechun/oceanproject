package com.fantechs.provider.wms.inner.util;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDetDto;
import com.fantechs.common.base.general.entity.wms.inner.*;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerMaterialBarcodeMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerInventoryDetService;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    @Resource
    private WmsInnerInventoryDetService wmsInnerInventoryDetService;

    private static WmsInnerInventoryUtil wmsInnerInventoryUtil;
    @Resource
    private WmsInnerMaterialBarcodeMapper wmsInnerMaterialBarcodeMapper;


    @PostConstruct
    public void init(){
        wmsInnerInventoryUtil = this;
        wmsInnerInventoryUtil.wmsInnerInventoryMapper=wmsInnerInventoryMapper;
        wmsInnerInventoryUtil.wmsInnerInventoryDetService=wmsInnerInventoryDetService;
        wmsInnerInventoryUtil.wmsInnerMaterialBarcodeMapper=wmsInnerMaterialBarcodeMapper;
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
    //    if(wmsInnerJobOrder.getJobOrderType()==((byte) 1)){
            Example example = new Example(WmsInnerInventory.class);
            Example.Criteria criteria1 = example.createCriteria();
            criteria1.andEqualTo("materialId", wmsInnerJobOrderDet.getMaterialId())
                    .andEqualTo("warehouseId", wmsInnerJobOrder.getWarehouseId())
                    .andEqualTo("storageId", wmsInnerJobOrderDet.getOutStorageId())
                    .andEqualTo("inventoryStatusId", wmsInnerJobOrderDet.getInventoryStatusId());
            if (!StringUtils.isEmpty(wmsInnerJobOrderDet.getBatchCode())) {
                criteria1.andEqualTo("batchCode", wmsInnerJobOrderDet.getBatchCode());
            }
        //    criteria1.andEqualTo("jobOrderDetId", wmsInnerJobOrderDet.getJobOrderDetId());
            criteria1.andEqualTo("jobStatus", (byte) 2);
            criteria1.andEqualTo("orgId", sysUser.getOrganizationId());
            WmsInnerInventory wmsInnerInventorys = wmsInnerInventoryUtil.wmsInnerInventoryMapper.selectOneByExample(example);
            if(addOrSubtract==((byte)2) && StringUtils.isEmpty(wmsInnerInventorys)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"库存数据不存在");
            }
            if (addOrSubtract==((byte)1) && StringUtils.isEmpty(wmsInnerInventorys)) {
                //添加库存
                WmsInnerInventory inv = new WmsInnerInventory();
                inv.setRelevanceOrderCode(wmsInnerJobOrder.getJobOrderCode());
                inv.setMaterialId(wmsInnerJobOrderDet.getMaterialId());
                inv.setWarehouseId(wmsInnerJobOrder.getWarehouseId());
                inv.setStorageId(wmsInnerJobOrderDet.getOutStorageId());
                inv.setInventoryStatusId(wmsInnerJobOrderDet.getInventoryStatusId());
                inv.setBatchCode(wmsInnerJobOrderDet.getBatchCode());
                inv.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
                inv.setPackingQty(finalQty);
                inv.setJobStatus((byte) 2);
                inv.setOrgId(sysUser.getOrganizationId());
                inv.setCreateUserId(sysUser.getUserId());
                inv.setCreateTime(new Date());
                inv.setModifiedTime(new Date());
                inv.setModifiedUserId(sysUser.getUserId());
                inv.setInventoryId(null);

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
   //     }

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

            if (StringUtils.isEmpty(newDto.getBatchCode())) {
                criteria.andIsNull("batchCode");
            }
            else {
                criteria.andEqualTo("batchCode",newDto.getBatchCode());
            }

            if(StringUtils.isEmpty(newDto.getProductionDate())){
                criteria.andIsNull("productionDate");
            }
            else {
                criteria.andEqualTo("productionDate", newDto.getProductionDate());
            }

            if(StringUtils.isEmpty(newDto.getSupplierId())){
                criteria.andIsNull("supplierId");
            }
            else {
                criteria.andEqualTo("supplierId", newDto.getSupplierId());
            }

            criteria.andEqualTo("jobStatus", (byte) 1);
            criteria.andEqualTo("inventoryStatusId", newDto.getInventoryStatusId());
            criteria.andEqualTo("stockLock", 0);
            criteria.andEqualTo("lockStatus", 0);
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
                inv.setProductionDate(newDto.getProductionDate());
                inv.setSupplierId(newDto.getSupplierId());
                inv.setJobStatus((byte) 1);
                inv.setInventoryStatusId(newDto.getInventoryStatusId());
                inv.setStockLock((byte) 0);
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

    /**
     * 加库存明细
     * @param wmsInnerInventoryDets 拣货/上架
     */
    public static int updateInventoryDet(List<WmsInnerInventoryDet> wmsInnerInventoryDets) {
        int num=0;
        // 上架 增加条码库存
        num=wmsInnerInventoryUtil.wmsInnerInventoryDetService.add(wmsInnerInventoryDets);
        if(num<=0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012006.getCode(),"新增条码库存明细出错");
        }
        return num;
    }

    /**
     * 校验条码是否整单发货
     *
     * @return
     */
    public static void isAllOutInventory(List<WmsInnerInventoryDetDto> list) {
        if (StringUtils.isEmpty(list)) {
            return ;
        }
        WmsInnerInventoryDetDto wmsInnerInventoryDetDto = list.get(0);
        Example example = new Example(WmsInnerMaterialBarcode.class);
        Map<String,Integer> barcodeMap = new HashMap<>();
        for (WmsInnerInventoryDetDto innerPdaInventoryDetDto : list) {
            List<WmsInnerMaterialBarcode> wmsInnerMaterialBarcodes = null;
            //按照条码类型组合查询对应的所有sn码查询条件
            if (wmsInnerInventoryDetDto.getBarcodeType() == 1) {
                example.createCriteria().andEqualTo("barcode",innerPdaInventoryDetDto.getBarcode())
                        .andEqualTo("barcodeType",1);
            }else if (wmsInnerInventoryDetDto.getBarcodeType() == 2) {
                example.createCriteria().andEqualTo("colorBoxCode",innerPdaInventoryDetDto.getColorBoxCode())
                        .andEqualTo("barcodeType",1);
            }else if (wmsInnerInventoryDetDto.getBarcodeType() == 3) {
                example.createCriteria().andEqualTo("cartonCode",innerPdaInventoryDetDto.getCartonCode())
                        .andEqualTo("barcodeType",1);
            }else if (wmsInnerInventoryDetDto.getBarcodeType() == 4) {
                continue;
            }
            //查询当前条码类型下的所有sn码，查询对应条码最大的条码类型，加入map
            wmsInnerMaterialBarcodes = wmsInnerInventoryUtil.wmsInnerMaterialBarcodeMapper.selectByExample(example);
            example.clear();
            if (StringUtils.isNotEmpty(wmsInnerMaterialBarcodes)) {
                WmsInnerMaterialBarcode wmsInnerMaterialBarcode = wmsInnerMaterialBarcodes.get(0);
                String barcode = "";
                Integer barcodeType = 1;
                //判断对应单位的条码是否有值，获取最大单位的条码
                if (StringUtils.isNotEmpty(wmsInnerMaterialBarcode.getPalletCode())) {
                    barcode = wmsInnerMaterialBarcode.getPalletCode();
                    barcodeType = 4;
                }else if (StringUtils.isNotEmpty(wmsInnerMaterialBarcode.getCartonCode())) {
                    barcode = wmsInnerMaterialBarcode.getCartonCode();
                    barcodeType = 3;
                }else if (StringUtils.isNotEmpty(wmsInnerMaterialBarcode.getColorBoxCode())) {
                    barcode = wmsInnerMaterialBarcode.getColorBoxCode();
                    barcodeType = 2;
                }else {
                    barcode = wmsInnerMaterialBarcode.getBarcode();
                }
                //按照最大条码单位进行分组，累计最大条码相同的sn码数量
                Integer qty = barcodeMap.get(barcodeType+","+barcode);
                if (StringUtils.isNotEmpty(qty)) {
                    barcodeMap.put(barcodeType+","+barcode,qty.intValue() + wmsInnerMaterialBarcodes.size());
                }else {
                    barcodeMap.put(barcodeType+","+barcode,wmsInnerMaterialBarcodes.size());
                }
            }
        }
        if (StringUtils.isNotEmpty(barcodeMap)) {
            //选循环最大条码集合，根据map查询最大包装数量下的sn码
            for (String barcode : barcodeMap.keySet()) {
                String[] split = barcode.split(",");
                barcode = split[1];
                //组合按照最大条码单位查询所有的sn码的查询条件
                if (1 == Integer.valueOf(split[0])) {
                    example.createCriteria().andEqualTo("barcode",barcode)
                            .andEqualTo("barcodeType",1);
                }else if (2 == Integer.valueOf(split[0])) {
                    example.createCriteria().andEqualTo("colorBoxCode",barcode)
                            .andEqualTo("barcodeType",1);
                }else if (3 == Integer.valueOf(split[0])) {
                    example.createCriteria().andEqualTo("cartonCode",barcode)
                            .andEqualTo("barcodeType",1);
                }else if (4 == Integer.valueOf(split[0])) {
                    example.createCriteria().andEqualTo("palletCode",barcode)
                            .andEqualTo("barcodeType",1);
                }
                List<WmsInnerMaterialBarcode> wmsInnerMaterialBarcodes = wmsInnerInventoryUtil.wmsInnerMaterialBarcodeMapper.selectByExample(example);


                example.clear();
                if (StringUtils.isNotEmpty(wmsInnerMaterialBarcodes)) {
                    //判断
                    barcode = split[0]+","+split[1];
                    if (wmsInnerMaterialBarcodes.size() != barcodeMap.get(barcode)) {
                        throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "出库需以最大单位进行，如需单独移动，请先进行拆分操作");
                    }
                }

            }
        }
    }

}
