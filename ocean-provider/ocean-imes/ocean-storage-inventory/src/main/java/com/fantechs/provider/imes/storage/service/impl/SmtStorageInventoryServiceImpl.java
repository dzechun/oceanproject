package com.fantechs.provider.imes.storage.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.storage.SmtStorageInventoryDto;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageInventory;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.storage.SmtStorageInventory;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
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

    @Override
    public List<SmtStorageInventoryDto> findList(Map<String,Object> map) {
        return smtStorageInventoryMapper.findList(map);
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
            storageInventory = smtStorageInventories.get(0);
            //累加库存
            quantity = BigDecimal.valueOf(storageInventory.getQuantity().intValue()+quantity.intValue());
            storageInventory.setQuantity(quantity);
            super.update(storageInventory);
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
