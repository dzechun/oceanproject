package com.fantechs.provider.wms.out.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.storage.SmtStorageInventoryDetDto;
import com.fantechs.common.base.dto.storage.SmtStorageInventoryDto;
import com.fantechs.common.base.dto.storage.SmtStoragePalletDto;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageInventory;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageInventoryDet;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.storage.SmtStorageInventoryDet;
import com.fantechs.common.base.entity.storage.search.SearchSmtStoragePallet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutOtheroutDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutOtheroutDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrderPallet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutOtherout;
import com.fantechs.common.base.general.entity.wms.out.WmsOutOtheroutDet;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtOtherout;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutOtheroutDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.imes.storage.StorageInventoryFeignApi;
import com.fantechs.provider.wms.out.mapper.WmsOutDeliveryOrderPalletMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutHtOtheroutMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutOtheroutMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutShippingNoteDetMapper;
import com.fantechs.provider.wms.out.service.WmsOutOtheroutDetService;
import com.fantechs.provider.wms.out.service.WmsOutOtheroutService;
import org.springframework.beans.BeanUtils;
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
 * Created by leifengzhi on 2020/12/25.
 */
@Service
public class WmsOutOtheroutServiceImpl extends BaseService<WmsOutOtherout> implements WmsOutOtheroutService {

    @Resource
    private WmsOutOtheroutMapper wmsOutOtheroutMapper;
    @Resource
    private WmsOutHtOtheroutMapper wmsOutHtOtheroutMapper;
    @Resource
    private WmsOutOtheroutDetService wmsOutOtheroutDetService;
    @Resource
    private StorageInventoryFeignApi storageInventoryFeignApi;
    @Resource
    private WmsOutDeliveryOrderPalletMapper wmsOutDeliveryOrderPalletMapper;
    @Resource
    private WmsOutShippingNoteDetMapper wmsOutShippingNoteDetMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(WmsOutOtherout wmsOutOtherout) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        wmsOutOtherout.setOtheroutCode(CodeUtils.getId("OO-"));
        wmsOutOtherout.setCreateUserId(user.getUserId());
        wmsOutOtherout.setCreateTime(new Date());
        wmsOutOtherout.setModifiedUserId(user.getUserId());
        wmsOutOtherout.setModifiedTime(new Date());
        wmsOutOtherout.setStatus(StringUtils.isEmpty(wmsOutOtherout.getStatus())?1:wmsOutOtherout.getStatus());
        //新增其他出库单
        int i = wmsOutOtheroutMapper.insertUseGeneratedKeys(wmsOutOtherout);

        //新增其他出库单履历
        WmsOutHtOtherout wmsOutHtOtherout = new WmsOutHtOtherout();
        BeanUtils.copyProperties(wmsOutOtherout,wmsOutHtOtherout);
        wmsOutHtOtheroutMapper.insert(wmsOutHtOtherout);

        //新增其他出库单明细
        List<WmsOutOtheroutDet> wmsOutOtheroutDets = wmsOutOtherout.getWmsOutOtheroutDets();
        if (StringUtils.isNotEmpty(wmsOutOtheroutDets)){
            for (WmsOutOtheroutDet wmsOutOtheroutDet : wmsOutOtheroutDets) {
                if (StringUtils.isEmpty(wmsOutOtheroutDet.getMaterialId(),wmsOutOtherout.getOtheroutId())){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100);
                }
                wmsOutOtheroutDet.setOtheroutId(wmsOutOtherout.getOtheroutId());
                wmsOutOtheroutDetService.save(wmsOutOtheroutDet);

                for (String s : wmsOutOtheroutDet.getOutPalletList()) {
                    /*SearchSmtStoragePallet searchSmtStoragePallet = new SearchSmtStoragePallet();
                    searchSmtStoragePallet.setPalletCode(s);

                    List<SmtStoragePalletDto> smtStoragePallets = storageInventoryFeignApi.findList(searchSmtStoragePallet).getData();
                    if (smtStoragePallets.size() <= 0) {
                        throw new BizErrorException(ErrorCodeEnum.GL99990100);
                    }*/
                    //删除栈板与储位关系
                    //storageInventoryFeignApi.deleteSmtStoragePallet(String.valueOf(smtStoragePallets.get(0).getStoragePalletId()));

                    //添加杂出单明细与栈板关系表
                    WmsOutDeliveryOrderPallet wmsOutDeliveryOrderPallet = new WmsOutDeliveryOrderPallet();
                    wmsOutDeliveryOrderPallet.setDeliveryOrderDetId(wmsOutOtheroutDet.getOtheroutDetId());
                    wmsOutDeliveryOrderPallet.setPalletCode(s);
                    wmsOutDeliveryOrderPallet.setCreateTime(new Date());
                    wmsOutDeliveryOrderPallet.setCreateUserId(user.getCreateUserId());
                    wmsOutDeliveryOrderPallet.setOrganizationId(user.getOrganizationId());
                    wmsOutDeliveryOrderPalletMapper.insertSelective(wmsOutDeliveryOrderPallet);

                    //修改库存明细表库存数
                    SearchSmtStorageInventoryDet searchSmtStorageInventoryDet = new SearchSmtStorageInventoryDet();
                    searchSmtStorageInventoryDet.setMaterialBarcodeCode(s);

                    //根据物料条码编码查询储位库存明细
                    List<SmtStorageInventoryDetDto> smtStorageInventoryDetDtos = storageInventoryFeignApi.findStorageInventoryDetList(searchSmtStorageInventoryDet).getData();
                    SmtStorageInventoryDet smtStorageInventoryDet = new SmtStorageInventoryDet();
                    smtStorageInventoryDet.setStorageInventoryDetId(smtStorageInventoryDetDtos.get(0).getStorageInventoryDetId());
                    smtStorageInventoryDet.setMaterialQuantity(BigDecimal.valueOf(0));
                    storageInventoryFeignApi.updateStorageInventoryDet(smtStorageInventoryDet);
                }

                //修改库存表
                SearchSmtStorageInventory searchSmtStorageInventory = new SearchSmtStorageInventory();
                searchSmtStorageInventory.setStorageId(wmsOutOtheroutDet.getStorageId());
                searchSmtStorageInventory.setMaterialId(wmsOutOtheroutDet.getMaterialId());
                List<SmtStorageInventoryDto> smtStorageInventories = storageInventoryFeignApi.findList(searchSmtStorageInventory).getData();
                SmtStorageInventoryDto smtStorageInventoryDto = smtStorageInventories.get(0);
                System.out.println(wmsOutOtheroutDet.getRealityOutquantity());
                System.out.println(smtStorageInventoryDto.getQuantity());
                smtStorageInventoryDto.setQuantity(smtStorageInventoryDto.getQuantity().subtract(wmsOutOtheroutDet.getRealityOutquantity()));
                storageInventoryFeignApi.update(smtStorageInventoryDto);

            }
        }
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(WmsOutOtherout wmsOutOtherout) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        wmsOutOtherout.setModifiedTime(new Date());
        wmsOutOtherout.setModifiedUserId(user.getUserId());

        //删除原本保存的其他出库单明细
        Example example = new Example(WmsOutOtheroutDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("otheroutId",wmsOutOtherout.getOtheroutId());
        List<WmsOutOtheroutDet> wmsOutOtheroutDets1 = wmsOutOtheroutDetService.selectByExample(example);
        for (WmsOutOtheroutDet wmsOutOtheroutDet : wmsOutOtheroutDets1) {
            wmsOutOtheroutDetService.batchDelete(String.valueOf(wmsOutOtheroutDet.getOtheroutDetId()));
        }

        //新增其他出库单历史
        WmsOutHtOtherout wmsOutHtOtherout = new WmsOutHtOtherout();
        BeanUtils.copyProperties(wmsOutOtherout,wmsOutHtOtherout);
        wmsOutHtOtheroutMapper.insertSelective(wmsOutHtOtherout);

        //新增其他出库单明细
        List<WmsOutOtheroutDet> wmsOutOtheroutDets = wmsOutOtherout.getWmsOutOtheroutDets();
        if (StringUtils.isNotEmpty(wmsOutOtheroutDets)){
            for (WmsOutOtheroutDet wmsOutOtheroutDet : wmsOutOtheroutDets) {
                if (StringUtils.isEmpty(wmsOutOtheroutDet.getMaterialId(),wmsOutOtherout.getOtheroutId())){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100);
                }
                wmsOutOtheroutDet.setOtheroutId(wmsOutOtherout.getOtheroutId());
                wmsOutOtheroutDetService.save(wmsOutOtheroutDet);
            }
        }
        return wmsOutOtheroutMapper.updateByPrimaryKeySelective(wmsOutOtherout);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idArray = ids.split(",");
        Example example = new Example(WmsOutOtheroutDet.class);
        for (String id : idArray) {
            WmsOutOtherout wmsOutOtherout = wmsOutOtheroutMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(wmsOutOtherout)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("otheroutId",id);
            List<WmsOutOtheroutDet> wmsOutOtheroutDets = wmsOutOtheroutDetService.selectByExample(example);
            if (StringUtils.isNotEmpty(wmsOutOtheroutDets)){
                for (WmsOutOtheroutDet wmsOutOtheroutDet : wmsOutOtheroutDets) {
                    //删除其他出库单对应的明细
                    wmsOutOtheroutDetService.batchDelete(String.valueOf(wmsOutOtheroutDet.getOtheroutDetId()));
                }
            }

            //新增其他出库单历史
            WmsOutHtOtherout wmsOutHtOtherout = new WmsOutHtOtherout();
            BeanUtils.copyProperties(wmsOutOtherout,wmsOutHtOtherout);
            wmsOutHtOtheroutMapper.insert(wmsOutHtOtherout);
        }

        return wmsOutOtheroutMapper.deleteByIds(ids);
    }

    @Override
    public List<WmsOutOtheroutDto> findList(Map<String, Object> map) {
        List<WmsOutOtheroutDto> wmsOutOtheroutDtos = wmsOutOtheroutMapper.findList(map);

        for (WmsOutOtheroutDto wmsOutOtheroutDto : wmsOutOtheroutDtos) {
            SearchWmsOutOtheroutDet searchWmsOutOtheroutDet = new SearchWmsOutOtheroutDet();
            searchWmsOutOtheroutDet.setOtheroutId(wmsOutOtheroutDto.getOtheroutId());
            List<WmsOutOtheroutDetDto> list = wmsOutOtheroutDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutOtheroutDet));
            wmsOutOtheroutDto.setWmsOutOtheroutDetDtoList(list);
        }

        return wmsOutOtheroutDtos;
    }

    @Override
    public List<WmsOutHtOtherout> findHTList(Map<String, Object> map) {
        return wmsOutHtOtheroutMapper.findHTList(map);
    }
}
