package com.fantechs.provider.imes.storage.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.storage.SmtStorageInventoryDto;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.storage.SmtStorageInventory;
import com.fantechs.common.base.entity.storage.SmtStorageInventoryDet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.storage.mapper.SmtStorageInventoryDetMapper;
import com.fantechs.provider.imes.storage.mapper.SmtStorageInventoryMapper;
import com.fantechs.provider.imes.storage.service.SmtStorageInventoryService;
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
public class SmtStorageInventoryServiceImpl  extends BaseService<SmtStorageInventory> implements SmtStorageInventoryService {

    @Resource
    private SmtStorageInventoryMapper smtStorageInventoryMapper;
    @Resource
    private SmtStorageInventoryDetMapper smtStorageInventoryDetMapper;

    @Override
    public List<SmtStorageInventoryDto> findList(Map<String,Object> map) {
        return smtStorageInventoryMapper.findList(map);
    }

    @Override
    public int out(SmtStorageInventory smtStorageInventory) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example =new Example(SmtStorageInventory.class);
        example.createCriteria().andEqualTo("storageId",smtStorageInventory.getStorageId())
                .andEqualTo("materialId",smtStorageInventory.getMaterialId());

        List<SmtStorageInventory> smtStorageInventories = smtStorageInventoryMapper.selectByExample(example);

        if(StringUtils.isEmpty(smtStorageInventories)){
            throw new BizErrorException(ErrorCodeEnum.STO30012001);
        }

        //主表扣库存
        smtStorageInventories.get(0).setQuantity(smtStorageInventories.get(0).getQuantity().subtract(smtStorageInventory.getQuantity()));
        smtStorageInventoryMapper.updateByPrimaryKeySelective(smtStorageInventories.get(0));
        //子表扣库存
        for (SmtStorageInventoryDet smtStorageInventoryDet : smtStorageInventory.getSmtStorageInventoryDets()) {
            Example example1 = new Example(SmtStorageInventoryDet.class);
            example1.createCriteria().andEqualTo("storageInventoryId",smtStorageInventory.getStorageInventoryId()).andEqualTo("godownEntry",smtStorageInventoryDet.getGodownEntry());
            List<SmtStorageInventoryDet> smtStorageInventoryDets = smtStorageInventoryDetMapper.selectByExample(example1);

            smtStorageInventoryDets.get(0).setMaterialQuantity(smtStorageInventoryDets.get(0).getMaterialQuantity().subtract(smtStorageInventoryDet.getMaterialQuantity()));
            smtStorageInventoryDetMapper.updateByPrimaryKeySelective(smtStorageInventoryDets.get(0));
        }


        return 0;
    }

    @Override
    public int save(SmtStorageInventory storageInventory) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example =new Example(SmtStorageInventory.class);
        example.createCriteria().andEqualTo("storageId",storageInventory.getStorageId())
                .andEqualTo("materialId",storageInventory.getMaterialId());

        List<SmtStorageInventory> smtStorageInventories = smtStorageInventoryMapper.selectByExample(example);

        int i = 0;
        if(StringUtils.isNotEmpty(smtStorageInventories)){
            BigDecimal quantity = storageInventory.getQuantity();
            SmtStorageInventory smtStorageInventory = smtStorageInventories.get(0);
            storageInventory.setStorageInventoryId(smtStorageInventory.getStorageInventoryId());
            //累加库存
            quantity = BigDecimal.valueOf(smtStorageInventory.getQuantity().intValue()+quantity.intValue());
            smtStorageInventory.setQuantity(quantity);
            super.update(smtStorageInventory);
        } else {
            //新增库存
            storageInventory.setCreateTime(new Date());
            storageInventory.setCreateUserId(user.getUserId());
            storageInventory.setModifiedTime(new Date());
            storageInventory.setModifiedUserId(user.getUserId());
            storageInventory.setStatus(StringUtils.isEmpty(storageInventory.getStatus())?1:storageInventory.getStatus());
            storageInventory.setOrganizationId(user.getOrganizationId());

            i = smtStorageInventoryMapper.insertUseGeneratedKeys(storageInventory);
        }

        return i;
    }
}
