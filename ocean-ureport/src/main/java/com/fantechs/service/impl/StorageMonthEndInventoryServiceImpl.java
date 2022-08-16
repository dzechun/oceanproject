package com.fantechs.service.impl;

import com.fantechs.common.base.support.BaseService;
import com.fantechs.dto.StorageMonthEndInventoryDto;
import com.fantechs.entity.StorageMonthEndInventory;
import com.fantechs.mapper.StorageMonthEndInventoryMapper;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.service.StorageMonthEndInventoryService;
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
    private InnerFeignApi innerFeignAip;

    @Override
    public List<StorageMonthEndInventoryDto> findList(Map<String, Object> map) {
        List<StorageMonthEndInventoryDto> list = storageMonthEndInventoryMapper.findList(map);
        for (StorageMonthEndInventoryDto storageMonthEndInventoryDto : list) {
          /*  SearchWmsInnerStorageInventoryDet searchWmsInnerStorageInventoryDet = new SearchWmsInnerStorageInventoryDet();
            searchWmsInnerStorageInventoryDet.setContractCode(storageMonthEndInventoryDto.getContractCode());
            searchWmsInnerStorageInventoryDet.setStorageId(storageMonthEndInventoryDto.getStorageId());
            searchWmsInnerStorageInventoryDet.setIsBinding((byte)2);
            searchWmsInnerStorageInventoryDet.setMaterialId(storageMonthEndInventoryDto.getMaterialId());

            List<WmsInnerStorageInventoryDetDto> wmsInnerStorageInventoryDetDtos = innerFeignAip.findStorageInventoryDetList(searchWmsInnerStorageInventoryDet).getData();

            storageMonthEndInventoryDto.setList(wmsInnerStorageInventoryDetDtos);*/
        }
        return list;
    }

    @Override
    public List<StorageMonthEndInventoryDto> findMonthEndList(Map<String, Object> map) {
        return storageMonthEndInventoryMapper.findMonthEndList(map);
    }

    @Override
    public int record() {
        Map<String, Object> map = new HashMap<>();
        List<StorageMonthEndInventoryDto> list = storageMonthEndInventoryMapper.findList(map);
        for (StorageMonthEndInventoryDto storageMonthEndInventoryDto : list) {
            storageMonthEndInventoryDto.setCreateTime(new Date());
        }

        return storageMonthEndInventoryMapper.insertList(list);
    }
}
