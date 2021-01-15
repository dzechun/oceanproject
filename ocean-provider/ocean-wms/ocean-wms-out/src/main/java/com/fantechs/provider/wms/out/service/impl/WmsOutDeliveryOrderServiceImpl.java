package com.fantechs.provider.wms.out.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.storage.SmtStorageInventoryDetDto;
import com.fantechs.common.base.dto.storage.SmtStorageInventoryDto;
import com.fantechs.common.base.dto.storage.SmtStoragePalletDto;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageInventory;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageInventoryDet;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.storage.SmtStorageInventory;
import com.fantechs.common.base.entity.storage.SmtStorageInventoryDet;
import com.fantechs.common.base.entity.storage.SmtStoragePallet;
import com.fantechs.common.base.entity.storage.search.SearchSmtStoragePallet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrder;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrderDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrderPallet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutShippingNoteDet;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtDeliveryOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.imes.storage.StorageInventoryFeignApi;
import com.fantechs.provider.wms.out.mapper.*;
import com.fantechs.provider.wms.out.service.WmsOutDeliveryOrderService;
import io.micrometer.core.instrument.search.Search;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/09.
 */
@Service
public class WmsOutDeliveryOrderServiceImpl  extends BaseService<WmsOutDeliveryOrder> implements WmsOutDeliveryOrderService {

    @Resource
    private WmsOutDeliveryOrderMapper wmsOutDeliveryOrderMapper;
    @Resource
    private WmsOutHtDeliveryOrderMapper wmsOutHtDeliveryOrderMapper;
    @Resource
    private WmsOutDeliveryOrderDetMapper wmsOutDeliveryOrderDetMapper;
    @Resource
    private StorageInventoryFeignApi storageInventoryFeignApi;
    @Resource
    private WmsOutDeliveryOrderPalletMapper wmsOutDeliveryOrderPalletMapper;
    @Resource
    private WmsOutShippingNoteDetMapper wmsOutShippingNoteDetMapper;

    @Override
    public List<WmsOutDeliveryOrderDto> findList(Map<String, Object> map) {
        return wmsOutDeliveryOrderMapper.findList(map);
    }

    @Override
    public List<WmsOutDeliveryOrderDto> findHtList(Map<String, Object> map) {
        return wmsOutHtDeliveryOrderMapper.findHtList(map);
    }

    @Override
    public String checkPallet(String palletCode) {

        Example example = new Example(WmsOutDeliveryOrderPallet.class);
        example.createCriteria().andEqualTo("palletCode",palletCode);
        List<WmsOutDeliveryOrderPallet> wmsOutDeliveryOrderPallets = wmsOutDeliveryOrderPalletMapper.selectByExample(example);

        if(wmsOutDeliveryOrderPallets.size() > 0){
            return "false";
        }else{
            return "true";
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(WmsOutDeliveryOrder wmsOutDeliveryOrder) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        if(StringUtils.isEmpty(wmsOutDeliveryOrder.getWmsOutDeliveryOrderDetList())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }

        wmsOutDeliveryOrder.setDeliveryOrderCode(CodeUtils.getId("CK"));
        wmsOutDeliveryOrder.setOutStatus((byte)2);
        wmsOutDeliveryOrder.setStatus((byte)1);
        wmsOutDeliveryOrder.setIsDelete((byte)1);
        wmsOutDeliveryOrder.setCreateTime(new Date());
        wmsOutDeliveryOrder.setCreateUserId(user.getUserId());
        wmsOutDeliveryOrder.setOrganizationId(user.getOrganizationId());
        //新增出库单
        int result = wmsOutDeliveryOrderMapper.insertUseGeneratedKeys(wmsOutDeliveryOrder);

        //履历
        WmsOutHtDeliveryOrder wmsOutHtDeliveryOrder = new WmsOutHtDeliveryOrder();
        BeanUtils.copyProperties(wmsOutDeliveryOrder,wmsOutHtDeliveryOrder);
        wmsOutHtDeliveryOrderMapper.insertSelective(wmsOutHtDeliveryOrder);

        for (WmsOutDeliveryOrderDet wmsOutDeliveryOrderDet : wmsOutDeliveryOrder.getWmsOutDeliveryOrderDetList()) {
            wmsOutDeliveryOrderDet.setDeliveryOrderId(wmsOutDeliveryOrder.getDeliveryOrderId());
            wmsOutDeliveryOrderDet.setOutStatus((byte)2);
            wmsOutDeliveryOrderDet.setCreateTime(new Date());
            wmsOutDeliveryOrderDet.setCreateUserId(user.getUserId());
            //新增成品出库单明细
            wmsOutDeliveryOrderDetMapper.insertSelective(wmsOutDeliveryOrderDet);

            for (String s : wmsOutDeliveryOrderDet.getOutPalletList()) {

                SearchSmtStoragePallet searchSmtStoragePallet = new SearchSmtStoragePallet();
                searchSmtStoragePallet.setPalletCode(s);

                List<SmtStoragePalletDto> smtStoragePallets = storageInventoryFeignApi.findList(searchSmtStoragePallet).getData();
                if (smtStoragePallets.size() <= 0) {
                    throw new BizErrorException(ErrorCodeEnum.GL99990100);
                }
                //删除栈板与储位关系
                storageInventoryFeignApi.deleteSmtStoragePallet(String.valueOf(smtStoragePallets.get(0).getStoragePalletId()));

                //添加出库单明细与栈板关系表
                WmsOutDeliveryOrderPallet wmsOutDeliveryOrderPallet = new WmsOutDeliveryOrderPallet();
                wmsOutDeliveryOrderPallet.setDeliveryOrderDetId(wmsOutDeliveryOrderDet.getDeliveryOrderDetId());
                wmsOutDeliveryOrderPallet.setPalletCode(s);
                wmsOutDeliveryOrderPallet.setCreateTime(new Date());
                wmsOutDeliveryOrderPallet.setCreateUserId(user.getCreateUserId());
                wmsOutDeliveryOrderPallet.setOrganizationId(user.getOrganizationId());
                wmsOutDeliveryOrderPalletMapper.insertSelective(wmsOutDeliveryOrderPallet);

                //修改库存明细表库存数
                SearchSmtStorageInventoryDet searchSmtStorageInventoryDet = new SearchSmtStorageInventoryDet();
                searchSmtStorageInventoryDet.setMaterialBarcodeCode(s);
                List<SmtStorageInventoryDetDto> smtStorageInventoryDetDtos = storageInventoryFeignApi.findStorageInventoryDetList(searchSmtStorageInventoryDet).getData();
                SmtStorageInventoryDet smtStorageInventoryDet = new SmtStorageInventoryDet();
                smtStorageInventoryDet.setStorageInventoryDetId(smtStorageInventoryDetDtos.get(0).getStorageInventoryDetId());
                smtStorageInventoryDet.setMaterialQuantity(BigDecimal.valueOf(0));
                storageInventoryFeignApi.updateStorageInventoryDet(smtStorageInventoryDet);
            }
            //修改库存表
            SearchSmtStorageInventory searchSmtStorageInventory = new SearchSmtStorageInventory();
            searchSmtStorageInventory.setStorageId(wmsOutShippingNoteDetMapper.selectByPrimaryKey(wmsOutDeliveryOrderDet.getShippingNoteDetId()).getStorageId());//出货通知单明细 储位ID
            searchSmtStorageInventory.setMaterialId(wmsOutDeliveryOrderDet.getProductModelId());
            List<SmtStorageInventoryDto> smtStorageInventories = storageInventoryFeignApi.findList(searchSmtStorageInventory).getData();
            SmtStorageInventoryDto smtStorageInventoryDto = smtStorageInventories.get(0);
            smtStorageInventoryDto.setQuantity(smtStorageInventoryDto.getQuantity().subtract(wmsOutDeliveryOrderDet.getOutTotalQty()));
            storageInventoryFeignApi.update(smtStorageInventoryDto);
        }
        return result;
    }
}
