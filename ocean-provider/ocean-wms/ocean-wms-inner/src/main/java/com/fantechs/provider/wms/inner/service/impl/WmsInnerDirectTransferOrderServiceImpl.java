package com.fantechs.provider.wms.inner.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.*;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.general.entity.wms.inner.*;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventoryDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.wms.inner.mapper.*;
import com.fantechs.provider.wms.inner.service.WmsInnerDirectTransferOrderService;
import com.fantechs.provider.wms.inner.service.WmsInnerInventoryDetService;
import com.fantechs.provider.wms.inner.service.WmsInnerMaterialBarcodeService;
import com.fantechs.provider.wms.inner.util.WmsInnerInventoryUtil;
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
    @Resource
    private WmsInnerInventoryDetService wmsInnerInventoryDetService;

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

            //查询移入库位
            SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
            searchBaseStorage.setStorageId(dto.getInStorageId());
            List<BaseStorage> baseStorage = baseFeignApi.findList(searchBaseStorage).getData();
            if (StringUtils.isEmpty(baseStorage))
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "未查询到移入库位");
            if (baseStorage.get(0).getStorageType() != 1)
                throw new BizErrorException(ErrorCodeEnum.PDA40012037.getCode(), "库位类型错误，移入库位必须是存货库位");

            //保存明细表
            WmsInnerDirectTransferOrderDet orderDet = new WmsInnerDirectTransferOrderDet();
            orderDet.setDirectTransferOrderId(order.getDirectTransferOrderId());
            orderDet.setInStorageId(dto.getInStorageId());
            orderDet.setOutStorageId(dto.getOutStorageId());
            orderDet.setMaterialId(dto.getMaterialId());
            orderDet.setActualQty(BigDecimal.ZERO);
            orderDet.setStatus((byte) 1);
            orderDet.setLineStatus((byte) 3);
            orderDet.setOrgId(user.getOrganizationId());
            orderDet.setCreateUserId(user.getUserId());
            orderDet.setCreateTime(new Date());
            orderDet.setModifiedUserId(user.getUserId());
            orderDet.setModifiedTime(new Date());
            wmsInnerDirectTransferOrderDetMapper.insertUseGeneratedKeys(orderDet);

            WmsInnerMaterialBarcode barcode = wmsInnerMaterialBarcodeMapper.selectByPrimaryKey(dto.getPdaWmsInnerDirectTransferOrderDetDtos().get(0).getMaterialBarcodeId());
            if (StringUtils.isEmpty(barcode)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "未再条码表中查询到对应的条码");
            }
            //查询库存
            Example example1 = new Example(WmsInnerInventory.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("storageId", dto.getOutStorageId())
                    .andEqualTo("jobStatus", (byte) 1)
                    .andEqualTo("stockLock", 0)
                    .andEqualTo("lockStatus", 0)
                    .andEqualTo("materialId", dto.getMaterialId())
                    .andGreaterThan("packingQty", 0);
            if (StringUtils.isNotEmpty(barcode.getBatchCode())) {
                criteria1.andEqualTo("batchCode", barcode.getBatchCode());
            }
            List<WmsInnerInventory> wmsInnerInventorys = wmsInnerInventoryMapper.selectByExample(example1);
            if (StringUtils.isEmpty(wmsInnerInventorys)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "未查询到移出库位");
            }
            WmsInnerInventory outWmsInnerInventory = wmsInnerInventorys.get(0);
            BigDecimal qty = BigDecimal.ZERO;
            for (WmsInnerInventory w : wmsInnerInventorys) {
                if (StringUtils.isEmpty(w.getPackingQty()))
                    w.setPackingQty(BigDecimal.ZERO);
                qty = qty.add(w.getPackingQty());
            }
            if (qty.compareTo(BigDecimal.ZERO) == -1)
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "移出库位物料数量小于等于0");

            for (PDAWmsInnerDirectTransferOrderDetDto det : dto.getPdaWmsInnerDirectTransferOrderDetDtos()) {

                //查询条码
                WmsInnerMaterialBarcode wmsInnerMaterialBarcode = wmsInnerMaterialBarcodeMapper.selectByPrimaryKey(det.getMaterialBarcodeId());
                if (StringUtils.isEmpty(wmsInnerMaterialBarcode)) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "未再条码表中查询到对应的条码");
                }

                //查询该条码下所有sn条码,更新所属库存
                SearchWmsInnerInventoryDet searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
                searchWmsInnerInventoryDet.setStorageId(dto.getOutStorageId());
                if (det.getBarcodeType() == 1)
                    searchWmsInnerInventoryDet.setBarcode(wmsInnerMaterialBarcode.getBarcode());
                else if (det.getBarcodeType() == 2)
                    searchWmsInnerInventoryDet.setColorBoxCode(wmsInnerMaterialBarcode.getColorBoxCode());
                else if (det.getBarcodeType() == 3)
                    searchWmsInnerInventoryDet.setCartonCode(wmsInnerMaterialBarcode.getCartonCode());
                else if (det.getBarcodeType() == 4)
                    searchWmsInnerInventoryDet.setPalletCode(wmsInnerMaterialBarcode.getPalletCode());
                List<WmsInnerInventoryDetDto> wmsInnerInventoryDetDtos = wmsInnerInventoryDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInventoryDet));

                if (StringUtils.isEmpty(wmsInnerInventoryDetDtos)) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "未查询到移出库位的物料条码，条码为：" + det.getBarcode());
                }
                Map<String, Byte> map = new HashMap();
                Set<String> set = new HashSet<>();
                for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : wmsInnerInventoryDetDtos) {
                    if (StringUtils.isNotEmpty(wmsInnerInventoryDetDto.getPalletCode())) {
                        map.put(wmsInnerInventoryDetDto.getPalletCode(), (byte) 4);
                        set.add(wmsInnerInventoryDetDto.getPalletCode());
                    }
                    if (StringUtils.isNotEmpty(wmsInnerInventoryDetDto.getCartonCode())) {
                        map.put(wmsInnerInventoryDetDto.getCartonCode(), (byte) 3);
                        set.add(wmsInnerInventoryDetDto.getCartonCode());
                    }
                    if (StringUtils.isNotEmpty(wmsInnerInventoryDetDto.getColorBoxCode())) {
                        map.put(wmsInnerInventoryDetDto.getColorBoxCode(), (byte) 2);
                        set.add(wmsInnerInventoryDetDto.getColorBoxCode());
                    }

                    wmsInnerInventoryDetDto.setStorageId(dto.getInStorageId());
                    wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(wmsInnerInventoryDetDto);

                    if (StringUtils.isNotEmpty(wmsInnerInventoryDetDto.getBarcode())) {
                        //保存所有sn码到条码中间表
                        WmsInnerMaterialBarcodeReOrder wmsInnerMaterialBarcodeReOrder = new WmsInnerMaterialBarcodeReOrder();
                        wmsInnerMaterialBarcodeReOrder.setOrderTypeCode("INNER-DTO");
                        wmsInnerMaterialBarcodeReOrder.setOrderCode(order.getDirectTransferOrderCode());
                        wmsInnerMaterialBarcodeReOrder.setOrderId(order.getDirectTransferOrderId());
                        wmsInnerMaterialBarcodeReOrder.setOrderDetId(orderDet.getDirectTransferOrderDetId());
                        wmsInnerMaterialBarcodeReOrder.setMaterialBarcodeId(wmsInnerInventoryDetDto.getMaterialBarcodeId());
                        wmsInnerMaterialBarcodeReOrder.setStatus((byte) 1);
                        wmsInnerMaterialBarcodeReOrder.setOrgId(user.getOrganizationId());
                        wmsInnerMaterialBarcodeReOrder.setCreateUserId(user.getUserId());
                        wmsInnerMaterialBarcodeReOrder.setCreateTime(new Date());
                        wmsInnerMaterialBarcodeReOrder.setModifiedUserId(user.getUserId());
                        wmsInnerMaterialBarcodeReOrder.setModifiedTime(new Date());
                        list.add(wmsInnerMaterialBarcodeReOrder);

                        //还原被标记的条码状态
                        WmsInnerMaterialBarcodeDto wmsInnerMaterialBarcodeDto = new WmsInnerMaterialBarcodeDto();
                        wmsInnerMaterialBarcodeReOrder.setMaterialBarcodeId(wmsInnerInventoryDetDto.getMaterialBarcodeId());
                        wmsInnerMaterialBarcodeDto.setIfScan((byte) 0);
                        wmsInnerMaterialBarcodeDto.setModifiedUserId(user.getUserId());
                        wmsInnerMaterialBarcodeDto.setModifiedTime(new Date());
                        wmsInnerMaterialBarcodeDtoList.add(wmsInnerMaterialBarcodeDto);

                    }

                }

                //更新库存明细
                Iterator<String> iterator = set.iterator();
                while (iterator.hasNext()) {
                    String code = iterator.next();
                    Byte type = map.get(code);
                    SearchWmsInnerInventoryDet searchWmsInnerInventoryDet1 = new SearchWmsInnerInventoryDet();
                    if (2 == type) {
                        searchWmsInnerInventoryDet1.setColorBoxCode(code);
                    } else if (3 == type) {
                        searchWmsInnerInventoryDet1.setCartonCode(code);
                    } else if (4 == type) {
                        searchWmsInnerInventoryDet1.setPalletCode(code);
                    } else {
                        continue;
                    }
                    //更新单独存在的彩盒码、箱码、栈板码
                    searchWmsInnerInventoryDet1.setOrgId(user.getOrganizationId());
                    searchWmsInnerInventoryDet1.setBarcodeType(type);
                    List<WmsInnerInventoryDetDto> list1 = wmsInnerInventoryDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInventoryDet1));
                    if(StringUtils.isNotEmpty(list1)){
                        WmsInnerInventoryDetDto innerInventoryDetDto1 = list1.get(0);
                        innerInventoryDetDto1.setStorageId(dto.getInStorageId());
                        innerInventoryDetDto1.setBarcodeStatus((byte) 1);
                        innerInventoryDetDto1.setModifiedTime(new Date());
                        innerInventoryDetDto1.setModifiedUserId(user.getUserId());
                        wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(innerInventoryDetDto1);
                    }
                    iterator.remove();
                }


            }

            //移出库存状态更新
            outWmsInnerInventory.setPackingQty(outWmsInnerInventory.getPackingQty().subtract(qty));
            wmsInnerInventoryMapper.updateByPrimaryKeySelective(outWmsInnerInventory);

            //新增移入库存状态
            WmsInnerInventory inWmsInnerInventory = new WmsInnerInventory();
            Example example3 = new Example(WmsInnerInventory.class);
            Example.Criteria criteria3 = example3.createCriteria();
            criteria3.andEqualTo("storageId", dto.getInStorageId())
                    .andEqualTo("batchCode", barcode.getBatchCode())
                    .andEqualTo("stockLock", 0)
                    .andEqualTo("lockStatus", 0)
                    .andEqualTo("materialId", dto.getMaterialId())
                    .andGreaterThan("packingQty", 0);
            List<WmsInnerInventory> inWmsInnerInventorys = wmsInnerInventoryMapper.selectByExample(example3);
            if (StringUtils.isEmpty(inWmsInnerInventorys)) {
                BeanUtils.copyProperties(outWmsInnerInventory, inWmsInnerInventory, new String[]{"inventoryId"});
                inWmsInnerInventory.setStorageId(dto.getInStorageId());
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
                inWmsInnerInventory.setPackingQty(qty);
                i = wmsInnerInventoryMapper.insertSelective(inWmsInnerInventory);
            } else {
                inWmsInnerInventory = inWmsInnerInventorys.get(0);
                inWmsInnerInventory.setPackingQty(inWmsInnerInventory.getPackingQty().add(qty));
                i = wmsInnerInventoryMapper.updateByPrimaryKeySelective(inWmsInnerInventory);
            }

            orderDet.setActualQty(qty);
            wmsInnerDirectTransferOrderDetMapper.updateByPrimaryKeySelective(orderDet);
        }
        if (StringUtils.isNotEmpty(list))
            wmsInnerMaterialBarcodeReOrderMapper.insertList(list);
        if (StringUtils.isNotEmpty(wmsInnerMaterialBarcodeDtoList))
            wmsInnerMaterialBarcodeService.batchUpdate(wmsInnerMaterialBarcodeDtoList);
        return i;
    }


    @Override
    public int check(List<PDAWmsInnerDirectTransferOrderDetDto> pdaWmsInnerDirectTransferOrderDetDtos) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        List<WmsInnerInventoryDetDto> newInventoryDetDtoList = new ArrayList<>();
        for (PDAWmsInnerDirectTransferOrderDetDto det : pdaWmsInnerDirectTransferOrderDetDtos) {
            Map map = new HashMap();
            map.put("orgId", user.getOrganizationId());
            map.put("materialBarcodeId", det.getMaterialBarcodeId());
            List<WmsInnerInventoryDetDto> list = wmsInnerInventoryDetMapper.findList(map);
            if (StringUtils.isEmpty(list))
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "未查询到物料条码");
            else
                newInventoryDetDtoList.add(list.get(0));
        }
        //校验是否整单发货
        WmsInnerInventoryUtil.isAllOutInventory(newInventoryDetDtoList);
        return 1;
    }

}
