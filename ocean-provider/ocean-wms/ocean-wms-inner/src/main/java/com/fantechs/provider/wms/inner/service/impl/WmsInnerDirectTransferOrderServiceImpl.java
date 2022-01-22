package com.fantechs.provider.wms.inner.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.*;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.general.entity.wms.inner.*;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.wms.inner.mapper.*;
import com.fantechs.provider.wms.inner.service.WmsInnerDirectTransferOrderService;
import com.fantechs.provider.wms.inner.service.WmsInnerMaterialBarcodeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by leifengzhi on 2021/12/21.
 */
@Service
public class WmsInnerDirectTransferOrderServiceImpl extends BaseService<WmsInnerDirectTransferOrder> implements WmsInnerDirectTransferOrderService {

    @Resource
    private WmsInnerDirectTransferOrderMapper wmsInnerDirectTransferOrderMapper;
    @Resource
    private WmsInnerDirectTransferOrderDetMapper wmsInnerDirectTransferOrderDetMapper;
    @Resource
    private WmsInnerInventoryDetMapper wmsInnerInventoryDetMapper;
    @Resource
    private WmsInnerInventoryMapper wmsInnerInventoryMapper;
    @Resource
    private WmsInnerMaterialBarcodeReOrderMapper wmsInnerMaterialBarcodeReOrderMapper;
    @Resource
    private WmsInnerMaterialBarcodeMapper wmsInnerMaterialBarcodeMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private WmsInnerMaterialBarcodeService wmsInnerMaterialBarcodeService;


    @Override
    public List<WmsInnerDirectTransferOrderDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return wmsInnerDirectTransferOrderMapper.findList(map);
    }

    @Override
    public WmsInnerDirectTransferOrderDto detail(Long id) {
        WmsInnerDirectTransferOrderDto dto = new WmsInnerDirectTransferOrderDto();
        Map map = new HashMap();
        map.put("directTransferOrderId", id);
        List<WmsInnerDirectTransferOrderDto> list = wmsInnerDirectTransferOrderMapper.findList(map);
        if (StringUtils.isNotEmpty(list)) {
            dto = list.get(0);
            List<Long> ids = new ArrayList<>();
            for (WmsInnerDirectTransferOrderDetDto det : dto.getWmsInnerDirectTransferOrderDetDtos()) {
                ids.add(det.getDirectTransferOrderDetId());
            }
            Map map1 = new HashMap();
            map1.put("orderDetIdList", ids);
            map1.put("orderTypeCode", "INNER-DTO");
            List<WmsInnerMaterialBarcodeReOrderDto> wmsInnerMaterialBarcodeReOrderDtos = wmsInnerMaterialBarcodeReOrderMapper.findList(map1);
            dto.setWmsInnerMaterialBarcodeReOrderDtos(wmsInnerMaterialBarcodeReOrderDtos);
        }
        return dto;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int save(List<PDAWmsInnerDirectTransferOrderDto> pdaWmsInnerDirectTransferOrderDtos) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(pdaWmsInnerDirectTransferOrderDtos))
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "提交参数不能为空");
        List<WmsInnerMaterialBarcodeReOrder> list = new ArrayList<>();
        List<WmsInnerMaterialBarcodeDto> wmsInnerMaterialBarcodeDtoList = new ArrayList<>();

        int i = 0;
        for (PDAWmsInnerDirectTransferOrderDto dto : pdaWmsInnerDirectTransferOrderDtos) {
            if (StringUtils.isEmpty(dto.getPdaWmsInnerDirectTransferOrderDetDtos(), dto.getMaterialId(), dto.getInStorageId(), dto.getOutStorageId()))
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "物料、移入库位、移出库位不能为空");
            if (dto.getInStorageId().equals(dto.getOutStorageId())) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(), "移入库位不能与移出库位相同");
            }

            WmsInnerDirectTransferOrder order = new WmsInnerDirectTransferOrder();
            order.setDirectTransferOrderCode(CodeUtils.getId("INNER-DTO"));
            order.setWorkerUserId(dto.getWorkerUserId());
            order.setOrderStatus((byte) 3);
            order.setStatus((byte) 1);
            order.setOrgId(user.getOrganizationId());
            order.setCreateUserId(user.getUserId());
            order.setCreateTime(new Date());
            order.setModifiedUserId(user.getUserId());
            order.setModifiedTime(new Date());
            wmsInnerDirectTransferOrderMapper.insertUseGeneratedKeys(order);
            for (PDAWmsInnerDirectTransferOrderDetDto det : dto.getPdaWmsInnerDirectTransferOrderDetDtos()) {

                //查询条码
                Example example2 = new Example(WmsInnerMaterialBarcode.class);
                Example.Criteria criteria2 = example2.createCriteria();
                criteria2.andEqualTo("materialId", dto.getMaterialId());
                //    criteria2.andEqualTo("materialBarcodeId", det.getMaterialBarcodeId());
                if (det.getBarcodeType() == 1) {
                    criteria2.andEqualTo("barcode", det.getBarcode());
                } else if (det.getBarcodeType() == 2) {
                    criteria2.andEqualTo("colorBoxCode", det.getBarcode());
                } else if (det.getBarcodeType() == 3) {
                    criteria2.andEqualTo("cartonCode", det.getBarcode());
                } else if (det.getBarcodeType() == 4) {
                    criteria2.andEqualTo("palletCode", det.getBarcode());
                } else {
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "未上传条码类型条码类型不正确");
                }
                List<WmsInnerMaterialBarcode> wmsInnerMaterialBarcodes = wmsInnerMaterialBarcodeMapper.selectByExample(example2);
                if (StringUtils.isEmpty(wmsInnerMaterialBarcodes)) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "未查询到对应的物料条码，条码为：" + det.getBarcode());
                }

                //查询库存
                Example example1 = new Example(WmsInnerInventory.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("storageId", dto.getOutStorageId())
                        .andEqualTo("jobStatus", (byte) 1)
                        .andEqualTo("stockLock", 0)
                        .andEqualTo("lockStatus", 0)
                        .andEqualTo("materialId", dto.getMaterialId());
                if (StringUtils.isNotEmpty(wmsInnerMaterialBarcodes.get(0).getBatchCode())) {
                    criteria1.andEqualTo("batchCode", wmsInnerMaterialBarcodes.get(0).getBatchCode());
                }
                List<WmsInnerInventory> wmsInnerInventorys = wmsInnerInventoryMapper.selectByExample(example1);
                if (StringUtils.isEmpty(wmsInnerInventorys)) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "未查询到移出库位");
                }
                WmsInnerInventory wmsInnerInventory = wmsInnerInventorys.get(0);
                BigDecimal qty =BigDecimal.ZERO;
                for(WmsInnerInventory w : wmsInnerInventorys){
                    if(StringUtils.isEmpty(w.getPackingQty()))
                        w.setPackingQty(BigDecimal.ZERO);
                    qty = qty.add(w.getPackingQty());
                }
                if (qty.compareTo(BigDecimal.ZERO) <= 0)
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "移出库位物料数量小于等于0");

                //保存明细表
                WmsInnerDirectTransferOrderDet orderDet = new WmsInnerDirectTransferOrderDet();
                orderDet.setDirectTransferOrderId(order.getDirectTransferOrderId());
                orderDet.setInStorageId(dto.getInStorageId());
                orderDet.setOutStorageId(dto.getOutStorageId());
                orderDet.setMaterialId(dto.getMaterialId());
                orderDet.setActualQty(det.getQty());
                orderDet.setStatus((byte) 1);
                orderDet.setOrgId(user.getOrganizationId());
                orderDet.setCreateUserId(user.getUserId());
                orderDet.setCreateTime(new Date());
                orderDet.setModifiedUserId(user.getUserId());
                orderDet.setModifiedTime(new Date());
                wmsInnerDirectTransferOrderDetMapper.insertUseGeneratedKeys(orderDet);

                for (WmsInnerMaterialBarcode barcode : wmsInnerMaterialBarcodes) {

                    //查询库位条码,变更数量
                    Example example = new Example(WmsInnerInventoryDet.class);
                    Example.Criteria criteria = example.createCriteria();
                    criteria.andEqualTo("storageId", dto.getOutStorageId());
                    criteria.andEqualTo("materialBarcodeId", det.getMaterialBarcodeId());
                    List<WmsInnerInventoryDet> wmsInnerInventoryDets = wmsInnerInventoryDetMapper.selectByExample(example);
                    if (StringUtils.isEmpty(wmsInnerInventoryDets)) {
                        throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "未查询到移出库位的物料条码，条码为：" + det.getBarcode());
                    }
                    WmsInnerInventoryDet wmsInnerInventoryDet = wmsInnerInventoryDets.get(0);
                    wmsInnerInventoryDet.setStorageId(dto.getInStorageId());
                    wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(wmsInnerInventoryDet);

                    if (det.getBarcodeType() == 1 && (StringUtils.isNotEmpty(barcode.getColorBoxCode()) || StringUtils.isNotEmpty(barcode.getCartonCode()) || StringUtils.isNotEmpty(barcode.getPalletCode())))
                        throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "调拨出库需以最大单位进行，如需单独移动SN码，请先进行拆分操作");
                    if (det.getBarcodeType() == 2 && (StringUtils.isNotEmpty(barcode.getCartonCode()) || StringUtils.isNotEmpty(barcode.getPalletCode())))
                        throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "调拨出库需以最大单位进行，如需单独移动彩盒码，请先进行拆分操作");
                    if (det.getBarcodeType() == 3 && (StringUtils.isNotEmpty(barcode.getPalletCode())))
                        throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "调拨出库需以最大单位进行，如需单独移动箱码，请先进行拆分操作");

                    WmsInnerMaterialBarcodeReOrder wmsInnerMaterialBarcodeReOrder = new WmsInnerMaterialBarcodeReOrder();
                    wmsInnerMaterialBarcodeReOrder.setOrderTypeCode("INNER-DTO");
                    wmsInnerMaterialBarcodeReOrder.setOrderCode(order.getDirectTransferOrderCode());
                    wmsInnerMaterialBarcodeReOrder.setOrderId(order.getDirectTransferOrderId());
                    wmsInnerMaterialBarcodeReOrder.setOrderDetId(orderDet.getDirectTransferOrderDetId());
                    wmsInnerMaterialBarcodeReOrder.setMaterialBarcodeId(barcode.getMaterialBarcodeId());
                    wmsInnerMaterialBarcodeReOrder.setStatus((byte) 1);
                    wmsInnerMaterialBarcodeReOrder.setOrgId(user.getOrganizationId());
                    wmsInnerMaterialBarcodeReOrder.setCreateUserId(user.getUserId());
                    wmsInnerMaterialBarcodeReOrder.setCreateTime(new Date());
                    wmsInnerMaterialBarcodeReOrder.setModifiedUserId(user.getUserId());
                    wmsInnerMaterialBarcodeReOrder.setModifiedTime(new Date());
                    list.add(wmsInnerMaterialBarcodeReOrder);

                    //库存原更新
                    wmsInnerInventory.setPackingQty(wmsInnerInventory.getPackingQty().subtract(barcode.getMaterialQty()));
                    wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventory);

                    WmsInnerInventory inWmsInnerInventory = new WmsInnerInventory();
                    Example example3 = new Example(WmsInnerInventory.class);
                    Example.Criteria criteria3 = example3.createCriteria();
                    criteria3.andEqualTo("storageId", dto.getInStorageId())
                            .andEqualTo("batchCode", barcode.getBatchCode())
                            .andEqualTo("materialId", dto.getMaterialId());
                    List<WmsInnerInventory> inWmsInnerInventorys = wmsInnerInventoryMapper.selectByExample(example3);
                    if (StringUtils.isEmpty(inWmsInnerInventorys)) {
                        BeanUtils.copyProperties(wmsInnerInventory, inWmsInnerInventory, new String[]{"inventoryId"});
                        inWmsInnerInventory.setStorageId(dto.getInStorageId());
                        SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
                        searchBaseStorage.setStorageId(dto.getInStorageId());
                        List<BaseStorage> baseStorage = baseFeignApi.findList(searchBaseStorage).getData();
                        if (StringUtils.isEmpty(baseStorage))
                            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "未查询到移入库位");
                        if (baseStorage.get(0).getStorageType() != 1)
                            throw new BizErrorException(ErrorCodeEnum.PDA40012037.getCode(), "库位类型错误，移入库位必须是存货库位");

                        inWmsInnerInventory.setWarehouseId(baseStorage.get(0).getWarehouseId());
                        inWmsInnerInventory.setJobStatus((byte) 1);
                        inWmsInnerInventory.setLockStatus((byte) 0);
                        inWmsInnerInventory.setStockLock((byte) 0);
                        inWmsInnerInventory.setStatus((byte) 1);
                        inWmsInnerInventory.setOrgId(user.getOrganizationId());
                        inWmsInnerInventory.setCreateUserId(user.getUserId());
                        inWmsInnerInventory.setCreateTime(new Date());
                        inWmsInnerInventory.setModifiedUserId(user.getUserId());
                        inWmsInnerInventory.setModifiedTime(new Date());
                        inWmsInnerInventory.setPackingQty(barcode.getMaterialQty());
                        i = wmsInnerInventoryMapper.insertSelective(inWmsInnerInventory);
                    } else {
                        inWmsInnerInventory = inWmsInnerInventorys.get(0);
                        inWmsInnerInventory.setPackingQty(inWmsInnerInventory.getPackingQty().add(barcode.getMaterialQty()));
                        i = wmsInnerInventoryMapper.updateByPrimaryKeySelective(inWmsInnerInventory);
                    }

                    //还原被标记的条码状态
                    WmsInnerMaterialBarcodeDto wmsInnerMaterialBarcodeDto = new WmsInnerMaterialBarcodeDto();
                    BeanUtils.copyProperties(barcode, wmsInnerMaterialBarcodeDto);
                    wmsInnerMaterialBarcodeDto.setIfScan((byte) 0);
                    wmsInnerMaterialBarcodeDto.setModifiedUserId(user.getUserId());
                    wmsInnerMaterialBarcodeDto.setModifiedTime(new Date());
                    wmsInnerMaterialBarcodeDtoList.add(wmsInnerMaterialBarcodeDto);
                }
            }
        }
        if (StringUtils.isNotEmpty(list))
            wmsInnerMaterialBarcodeReOrderMapper.insertList(list);
        if (StringUtils.isNotEmpty(wmsInnerMaterialBarcodeDtoList))
            wmsInnerMaterialBarcodeService.batchUpdate(wmsInnerMaterialBarcodeDtoList);
        return i;
    }

}
