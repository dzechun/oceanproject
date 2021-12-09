package com.fantechs.provider.wms.out.service;

import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutTransferDeliveryOrderDto;
import com.fantechs.common.base.general.dto.wms.out.imports.WmsOutDeliveryOrderImport;
import com.fantechs.common.base.general.dto.wms.out.imports.WmsSamsungOutDeliveryOrderImport;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrder;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtDeliveryOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/07.
 */

public interface WmsOutDeliveryOrderService extends IService<WmsOutDeliveryOrder> {
    List<WmsOutDeliveryOrderDto> findList(Map<String, Object> map);

    List<WmsOutTransferDeliveryOrderDto> transferFindList(Map<String, Object> map);

    List<WmsOutTransferDeliveryOrderDto> transferFindHtList(Map<String, Object> map);

    List<WmsOutHtDeliveryOrder> findHtList(Map<String, Object> map);

    int createJobOrder(Long id,Long platformId);

    int forwardingStatus(Long deliverOrderId,Byte orderStatus);

    /**
     * 批量修改审核状态
     * @param ids
     * @return
     */
    int updateStatus(List<Long> ids);

    /**
     * create by: Dylan
     * description: 接口新增领料出库单
     * create time:
     * @return
     */
    int saveByApi(WmsOutDeliveryOrder wmsOutDeliveryOrder);

    int overIssue(Long deliveryOrderId);

    Map<String, Object> importExcel(List<WmsOutDeliveryOrderImport> wmsOutDeliveryOrderImports);

    //封单
    int sealOrder(Long outDeliveryOrderId);

    //三星导入
    Map<String, Object> importSamsungExcel(List<WmsSamsungOutDeliveryOrderImport> wmsSamsungOutDeliveryOrderImports);
}
