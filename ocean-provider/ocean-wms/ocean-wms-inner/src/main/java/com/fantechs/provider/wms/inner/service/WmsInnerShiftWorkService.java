package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.dto.wms.inner.export.WmsInnerJobOrderExport;
import com.fantechs.common.base.general.dto.wms.inner.imports.WmsInnerJobOrderImport;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;
import com.fantechs.common.base.support.IService;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by Mr.Lei on 2021/05/06.
 */

public interface WmsInnerShiftWorkService extends IService<WmsInnerJobOrder> {
    List<WmsInnerJobOrderDto> findList(SearchWmsInnerJobOrder searchWmsInnerJobOrder);

    /**
     * 手动分配
     * @param list
     * @return
     */
    int handDistribution(List<WmsInnerJobOrderDet> list);

    /**
     * 取消分配
     * @return
     */
    int cancelDistribution(String ids);

    /**
     * 关闭单据
     * @return
     */
    int closeWmsInnerJobOrder(String ids);


    /**
     * 按条码单一确认
     * @param wmsInnerJobOrderDet 明细
     * @param ids 条码ID 多个条码用逗号隔开
     * @return
     */
    int singleReceivingByBarcode(WmsInnerJobOrderDet wmsInnerJobOrderDet, String ids, Byte orderType);

    Map<String,Object> checkBarcode(String barCode, Long jobOrderDetId);

    /**
     * PDA激活关闭栈板
     * @param jobOrderId
     * @return
     */
    int activation(Long jobOrderId);

    /**
     * 移位单查询
     * @param map
     * @return
     */
    List<WmsInnerJobOrderDto> findShiftList(Map<String, Object> map);

    /**
     * 移位单批量删除
     * @param ids
     * @return
     */
    int batchDeleteByShiftWork(String ids);

    Map<String, Object> importExcel(List<WmsInnerJobOrderImport> wmsInnerJobOrderImportList) throws ParseException;

    WmsInnerJobOrderDto detail(Long id, String sourceSysOrderTypeCode);

    List<WmsInnerJobOrderExport> findExportList(Map<String, Object> map);
}
