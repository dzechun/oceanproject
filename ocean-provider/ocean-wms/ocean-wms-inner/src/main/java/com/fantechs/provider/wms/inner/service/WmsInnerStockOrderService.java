package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStockOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by Mr.Lei on 2021/05/27.
 */

public interface WmsInnerStockOrderService extends IService<WmsInnerStockOrder> {
    List<WmsInnerStockOrderDto> findList(Map<String, Object> map);

    /**
     * 盘点单激活或作废
     * @param ids
     * @return
     */
    int activation(String ids,Byte btnType);

    /**
     * 盘点确认
     * @param ids
     * @return
     */
    int ascertained(String ids);

    /**
     * 差异处理
     * @param ids
     * @return
     */
    int difference(String ids);
}
