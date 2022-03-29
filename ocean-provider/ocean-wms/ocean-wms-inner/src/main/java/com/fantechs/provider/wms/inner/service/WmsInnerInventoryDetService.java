package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.InStorageMaterialDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryDet;
import com.fantechs.common.base.support.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
/**
 *
 * Created by Mr.Lei on 2021/06/02.
 */

public interface WmsInnerInventoryDetService extends IService<WmsInnerInventoryDet> {
    List<WmsInnerInventoryDetDto> findList(Map<String, Object> map);

    int add(List<WmsInnerInventoryDet> wmsInnerInventoryDets);

    int subtract(WmsInnerInventoryDet wmsInnerInventoryDet);

    WmsInnerInventoryDet findByOne(String barCode);


    /**
     * 20220104
     * 批量移位统计库存明细按物料分组
     * @param map
     * @return
     */
    List<InStorageMaterialDto> findInventoryDetByStorage(Map<String, Object> map);

    int lock(String ids);

    int unlock(String ids);
}
