package com.fantechs.provider.imes.storage.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.storage.SmtStorageInventoryDto;
import com.fantechs.common.base.dto.storage.StorageMonthEndInventoryDto;
import com.fantechs.common.base.entity.basic.SmtMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterial;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.storage.StorageMonthEndInventory;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseTab;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;
import com.fantechs.provider.imes.storage.mapper.SmtStorageInventoryMapper;
import com.fantechs.provider.imes.storage.mapper.StorageMonthEndInventoryMapper;
import com.fantechs.provider.imes.storage.service.StorageMonthEndInventoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
public class StorageMonthEndInventoryServiceImpl extends BaseService<StorageMonthEndInventory> implements StorageMonthEndInventoryService {

    @Resource
    private StorageMonthEndInventoryMapper storageMonthEndInventoryMapper;
    @Resource
    private SmtStorageInventoryMapper smtStorageInventoryMapper;
    @Resource
    private BasicFeignApi basicFeignApi;

    @Override
    public List<StorageMonthEndInventoryDto> findList(Map<String, Object> map) {
        return storageMonthEndInventoryMapper.findList(map);
    }

    @Override
    public int record() {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Map<String,Object> map = new HashMap<>();
        List<StorageMonthEndInventory> storageMonthEndInventorys = new ArrayList<>();
        List<SmtStorageInventoryDto> list = smtStorageInventoryMapper.findList(map);
        for (SmtStorageInventoryDto smtStorageInventoryDto : list) {
            StorageMonthEndInventory storageMonthEndInventory = new StorageMonthEndInventory();
            storageMonthEndInventory.setStorageId(smtStorageInventoryDto.getStorageId());
            storageMonthEndInventory.setMaterialId(smtStorageInventoryDto.getMaterialId());
            storageMonthEndInventory.setTotal(smtStorageInventoryDto.getQuantity().intValue() <=0?0:smtStorageInventoryDto.getQuantity().intValue());
            storageMonthEndInventory.setBoxNumber(1);
            storageMonthEndInventory.setCreateTime(new Date());
            storageMonthEndInventory.setModifiedTime(new Date());

            storageMonthEndInventory.setCreateUserId(user.getUserId());
            storageMonthEndInventory.setModifiedUserId(user.getUserId());
            storageMonthEndInventory.setOrganizationId(user.getOrganizationId());

            SearchSmtMaterial searchSmtMaterial = new SearchSmtMaterial();
            searchSmtMaterial.setMaterialId(smtStorageInventoryDto.getMaterialId());
            List<SmtMaterial> smtMaterialList = basicFeignApi.findSmtMaterialList(searchSmtMaterial).getData();

            if (StringUtils.isNotEmpty(smtMaterialList) && smtStorageInventoryDto.getQuantity().intValue() >0){
                if (smtMaterialList.get(0).getBaseTabDto() != null){
                    BigDecimal number = smtMaterialList.get(0).getBaseTabDto().getPackageSpecificationQuantity();
                    if (StringUtils.isNotEmpty(number)){
                        Double ceil = Math.ceil(smtStorageInventoryDto.getQuantity().intValue() / number.intValue());
                        storageMonthEndInventory.setBoxNumber(ceil.intValue());
                    }else{
                        storageMonthEndInventory.setBoxNumber(smtStorageInventoryDto.getQuantity().intValue());
                    }
                }else{
                    storageMonthEndInventory.setBoxNumber(smtStorageInventoryDto.getQuantity().intValue());
                }
            }else{
                storageMonthEndInventory.setBoxNumber(0);
            }
            storageMonthEndInventorys.add(storageMonthEndInventory);
        }

        return storageMonthEndInventoryMapper.insertList(storageMonthEndInventorys);
    }
}
