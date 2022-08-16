package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.NotOrderInStorage;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.support.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/07.
 */

public interface WmsInnerInventoryService extends IService<WmsInnerInventory> {
    List<WmsInnerInventoryDto> findList(Map<String, Object> map);

    int lock(Long id, BigDecimal quantity);

    int unlock(Long id, BigDecimal quantity);

    WmsInnerInventory selectOneByExample(Map<String,Object> map);

    int updateByPrimaryKeySelective(WmsInnerInventory wmsInnerInventory);

    int updateByExampleSelective(WmsInnerInventory wmsInnerInventory,Map<String,Object> map);

    int insertSelective(WmsInnerInventory wmsInnerInventory);

    int insertList(List<WmsInnerInventory> wmsInnerInventories);

    int batchUpdate(List<WmsInnerInventory> list);

    List<WmsInnerInventoryDto> findInvStorage(Map<String ,Object> map);

    /**
     * 万宝无单入库
     * @param notOrderInStorage
     * @return
     */
    int notOrderInStorage(NotOrderInStorage notOrderInStorage);
}
