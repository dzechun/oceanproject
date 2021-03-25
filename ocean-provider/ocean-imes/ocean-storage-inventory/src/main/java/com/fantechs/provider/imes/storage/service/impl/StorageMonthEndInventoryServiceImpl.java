package com.fantechs.provider.imes.storage.service.impl;

import com.fantechs.common.base.dto.storage.SmtStorageInventoryDetDto;
import com.fantechs.common.base.dto.storage.StorageMonthEndInventoryDto;
import com.fantechs.common.base.entity.storage.StorageMonthEndInventory;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.storage.mapper.SmtStorageInventoryDetMapper;
import com.fantechs.provider.imes.storage.mapper.StorageMonthEndInventoryMapper;
import com.fantechs.provider.imes.storage.service.StorageMonthEndInventoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StorageMonthEndInventoryServiceImpl extends BaseService<StorageMonthEndInventory> implements StorageMonthEndInventoryService {

    @Resource
    private StorageMonthEndInventoryMapper storageMonthEndInventoryMapper;
    @Resource
    private SmtStorageInventoryDetMapper smtStorageInventoryDetMapper;

    @Override
    public List<StorageMonthEndInventoryDto> findList(Map<String, Object> map) {
        List<StorageMonthEndInventoryDto> list = storageMonthEndInventoryMapper.findList(map);
        map.clear();
        for (StorageMonthEndInventoryDto storageMonthEndInventoryDto : list) {
            map.put("contractCode",storageMonthEndInventoryDto.getContractCode());
            List<SmtStorageInventoryDetDto> smtStorageInventoryDetDtos = smtStorageInventoryDetMapper.findList(map);
            storageMonthEndInventoryDto.setList(smtStorageInventoryDetDtos);
        }
        return list;
    }

    @Override
    public List<StorageMonthEndInventoryDto> findMonthEndList(Map<String, Object> map) {
        return storageMonthEndInventoryMapper.findMonthEndList(map);
    }

    @Override
    public int record() {
//        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
//        if (StringUtils.isEmpty(user)) {
//            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
//        }
        Map<String, Object> map = new HashMap<>();
//        List<StorageMonthEndInventory> storageMonthEndInventorys = new ArrayList<>();
//        List<SmtStorageInventoryDto> list = smtStorageInventoryMapper.findList(map);
        List<StorageMonthEndInventoryDto> list = storageMonthEndInventoryMapper.findList(map);
        for (StorageMonthEndInventoryDto storageMonthEndInventoryDto : list) {
            storageMonthEndInventoryDto.setCreateTime(new Date());
        }
//        for (SmtStorageInventoryDto smtStorageInventoryDto : list) {
//            if (StringUtils.isNotEmpty(smtStorageInventoryDto.getSmtStorageInventoryDets())) {
//                for (SmtStorageInventoryDet smtStorageInventoryDet : smtStorageInventoryDto.getSmtStorageInventoryDets()) {
//                    StorageMonthEndInventory storageMonthEndInventory = new StorageMonthEndInventory();
//                    storageMonthEndInventory.setStorageId(smtStorageInventoryDto.getStorageId());
//                    storageMonthEndInventory.setMaterialId(smtStorageInventoryDto.getMaterialId());
//                    storageMonthEndInventory.setTotal(smtStorageInventoryDet.getMaterialQuantity().intValue() <= 0 ? 0 : smtStorageInventoryDet.getMaterialQuantity().intValue());
//                    storageMonthEndInventory.setMaterialBarcodeCode(smtStorageInventoryDet.getMaterialBarcodeCode());
//                    storageMonthEndInventory.setMaterialTotal(smtStorageInventoryDto.getQuantity() == null ? 0 : smtStorageInventoryDto.getQuantity().intValue());
//                    storageMonthEndInventory.setCreateTime(new Date());
//                    storageMonthEndInventory.setModifiedTime(new Date());
//
//                    storageMonthEndInventory.setCreateUserId(user.getUserId());
//                    storageMonthEndInventory.setModifiedUserId(user.getUserId());
//                    storageMonthEndInventory.setOrganizationId(user.getOrganizationId());
//
//                    SearchSmtMaterial searchSmtMaterial = new SearchSmtMaterial();
//                    searchSmtMaterial.setMaterialId(smtStorageInventoryDto.getMaterialId());
//                    List<SmtMaterial> smtMaterialList = basicFeignApi.findSmtMaterialList(searchSmtMaterial).getData();
//
//                    if (StringUtils.isNotEmpty(smtMaterialList) && smtStorageInventoryDto.getQuantity().intValue() > 0) {
//                        if (smtMaterialList.get(0).getBaseTabDto() != null) {
//                            BigDecimal number = smtMaterialList.get(0).getBaseTabDto().getPackageSpecificationQuantity();
//                            if (StringUtils.isNotEmpty(number)) {
//                                Double ceil = Math.ceil(smtStorageInventoryDto.getQuantity().intValue() / number.intValue());
//                                storageMonthEndInventory.setBoxNumber(ceil.intValue());
//                            } else {
//                                storageMonthEndInventory.setBoxNumber(smtStorageInventoryDto.getQuantity().intValue());
//                            }
//                        } else {
//                            storageMonthEndInventory.setBoxNumber(smtStorageInventoryDto.getQuantity().intValue());
//                        }
//                    } else {
//                        storageMonthEndInventory.setBoxNumber(0);
//                    }
//                    storageMonthEndInventorys.add(storageMonthEndInventory);
//                }
//            }
//        }

        return storageMonthEndInventoryMapper.insertList(list);
    }
}
