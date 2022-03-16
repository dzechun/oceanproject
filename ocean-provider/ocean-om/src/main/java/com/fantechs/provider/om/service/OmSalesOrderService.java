package com.fantechs.provider.om.service;


import com.fantechs.common.base.general.dto.om.OmSalesOrderDto;
import com.fantechs.common.base.general.entity.om.OmSalesOrder;
import com.fantechs.common.base.support.IService;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/04/19.
 */

public interface OmSalesOrderService extends IService<OmSalesOrder> {
    List<OmSalesOrderDto> findList(Map<String, Object> map);
    List<OmSalesOrderDto> findAll();
    int saveDto(OmSalesOrderDto omSalesOrderDto);
    int updateDto(OmSalesOrderDto omSalesOrderDto);
    int issueWarehouse(Long id);
    int batchUpdate(List<OmSalesOrder> orders);

    /**
     * 万宝同步销售订单批量新增
     * @param list
     * @return
     */
    int syncBatchSave(List<OmSalesOrderDto> list);

    /**
     * 万宝同步销售订单批量修改
     * @param list
     * @return
     */
    int syncBatchUpdate(List<OmSalesOrderDto> list);
}
