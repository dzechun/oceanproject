package com.fantechs.provider.om.service;

import com.fantechs.common.base.general.dto.om.OmHtSalesReturnOrderDetDto;
import com.fantechs.common.base.general.dto.om.OmSalesReturnOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmHtSalesReturnOrderDet;
import com.fantechs.common.base.general.entity.om.OmSalesReturnOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by Mr.Lei on 2021/06/21.
 */

public interface OmSalesReturnOrderDetService extends IService<OmSalesReturnOrderDet> {
    List<OmSalesReturnOrderDetDto> findList(Map<String, Object> map);

    List<OmHtSalesReturnOrderDetDto> findHtList(Map<String,Object> map);
}
