package com.fantechs.provider.om.service;

import com.fantechs.common.base.general.dto.om.OmHtOtherOutOrderDto;
import com.fantechs.common.base.general.dto.om.OmOtherOutOrderDto;
import com.fantechs.common.base.general.entity.om.OmOtherInOrder;
import com.fantechs.common.base.general.entity.om.OmOtherOutOrder;
import com.fantechs.common.base.general.entity.om.OmOtherOutOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by Mr.Lei on 2021/06/23.
 */

public interface OmOtherOutOrderService extends IService<OmOtherOutOrder> {
    List<OmOtherOutOrderDto> findList(Map<String, Object> map);

    int packageAutoOutOrder(OmOtherOutOrder omOtherOutOrder);

    int writeQty(OmOtherOutOrderDet omOtherOutOrderDet);

    List<OmHtOtherOutOrderDto> findHtList(Map<String ,Object> map);
}
