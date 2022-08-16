package com.fantechs.provider.om.service.ht;

import com.fantechs.common.base.general.dto.om.OmHtSalesOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmHtSalesOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/21.
 */

public interface OmHtSalesOrderDetService extends IService<OmHtSalesOrderDet> {
    List<OmHtSalesOrderDetDto> findList(Map<String, Object> map);
}
