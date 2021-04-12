package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStorageInventoryDto;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStorageInventory;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStorageInventoryDet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.mapper.WmsInnerStorageInventoryDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerStorageInventoryMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerStorageInventoryService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/02.
 */
@Service
public class WmsInnerStorageInventoryServiceImpl extends BaseService<WmsInnerStorageInventory> implements WmsInnerStorageInventoryService {

    @Resource
    private WmsInnerStorageInventoryMapper wmsInnerStorageInventoryMapper;
    @Resource
    private WmsInnerStorageInventoryDetMapper wmsInnerStorageInventoryDetMapper;

    @Override
    public List<WmsInnerStorageInventoryDto> findList(Map<String,Object> map) {
        return wmsInnerStorageInventoryMapper.findList(map);
    }

    @Override
    public int out(WmsInnerStorageInventory wmsInnerStorageInventory) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example =new Example(WmsInnerStorageInventory.class);
        example.createCriteria().andEqualTo("storageId", wmsInnerStorageInventory.getStorageId())
                .andEqualTo("materialId", wmsInnerStorageInventory.getMaterialId());

        List<WmsInnerStorageInventory> smtStorageInventories = wmsInnerStorageInventoryMapper.selectByExample(example);

        if(StringUtils.isEmpty(smtStorageInventories)){
            throw new BizErrorException(ErrorCodeEnum.STO30012001);
        }

        //主表扣库存
        smtStorageInventories.get(0).setQuantity(smtStorageInventories.get(0).getQuantity().subtract(wmsInnerStorageInventory.getQuantity()));
        wmsInnerStorageInventoryMapper.updateByPrimaryKeySelective(smtStorageInventories.get(0));
        //子表扣库存 根据入库单号或栈板
        for (WmsInnerStorageInventoryDet smtStorageInventoryDet : wmsInnerStorageInventory.getSmtStorageInventoryDets()) {
            Example example1 = new Example(WmsInnerStorageInventoryDet.class);
            Example.Criteria criteria = example1.createCriteria();
            criteria.andEqualTo("storageInventoryId", wmsInnerStorageInventory.getStorageInventoryId());
            if(StringUtils.isNotEmpty(smtStorageInventoryDet.getGodownEntry())){
                criteria.andEqualTo("godownEntry",smtStorageInventoryDet.getGodownEntry());
            }
            if(StringUtils.isNotEmpty(smtStorageInventoryDet.getMaterialBarcodeCode())){
                criteria.andEqualTo("materialBarcodeCode",smtStorageInventoryDet.getMaterialBarcodeCode());
            }
            List<WmsInnerStorageInventoryDet> smtStorageInventoryDets = wmsInnerStorageInventoryDetMapper.selectByExample(example1);

            smtStorageInventoryDets.get(0).setMaterialQuantity(smtStorageInventoryDets.get(0).getMaterialQuantity().subtract(smtStorageInventoryDet.getMaterialQuantity()));
            wmsInnerStorageInventoryDetMapper.updateByPrimaryKeySelective(smtStorageInventoryDets.get(0));

            if(smtStorageInventoryDets.get(0).getMaterialQuantity().doubleValue() == 0){
                wmsInnerStorageInventoryDetMapper.deleteByPrimaryKey(smtStorageInventoryDets.get(0).getStorageInventoryDetId());
            }
        }

        return 0;
    }

    @Override
    public int save(WmsInnerStorageInventory storageInventory) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
//        if(StringUtils.isEmpty(user)){
//            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
//        }

        Example example =new Example(WmsInnerStorageInventory.class);
        example.createCriteria().andEqualTo("storageId",storageInventory.getStorageId())
                .andEqualTo("materialId",storageInventory.getMaterialId());

        List<WmsInnerStorageInventory> smtStorageInventories = wmsInnerStorageInventoryMapper.selectByExample(example);

        int i = 0;
        if(StringUtils.isNotEmpty(smtStorageInventories)){
            BigDecimal quantity = storageInventory.getQuantity();
            WmsInnerStorageInventory wmsInnerStorageInventory = smtStorageInventories.get(0);
            storageInventory.setStorageInventoryId(wmsInnerStorageInventory.getStorageInventoryId());
            //累加库存
            quantity = BigDecimal.valueOf(wmsInnerStorageInventory.getQuantity().intValue()+quantity.intValue());
            wmsInnerStorageInventory.setQuantity(quantity);
            super.update(wmsInnerStorageInventory);
        } else {
            example.createCriteria().andEqualTo("storageId",storageInventory.getStorageId());
            smtStorageInventories = wmsInnerStorageInventoryMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(smtStorageInventories)){
                throw new BizErrorException("该储位已存在物料，新增失败");
            }
            //新增库存
            storageInventory.setCreateTime(new Date());
            storageInventory.setModifiedTime(new Date());
            storageInventory.setStatus(StringUtils.isEmpty(storageInventory.getStatus())?1:storageInventory.getStatus());
            if (StringUtils.isNotEmpty(user)) {
                storageInventory.setCreateUserId(user.getUserId());
                storageInventory.setModifiedUserId(user.getUserId());
                storageInventory.setOrganizationId(user.getOrganizationId());
            }

            i = wmsInnerStorageInventoryMapper.insertUseGeneratedKeys(storageInventory);
        }

        return i;
    }
}
