package com.fantechs.provider.om.service;

import com.fantechs.common.base.general.dto.om.OmOtherInOrderDto;
import com.fantechs.common.base.general.entity.om.OmOtherInOrder;
import com.fantechs.common.base.general.entity.om.OmOtherInOrderDet;
import com.fantechs.common.base.support.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/06/21.
 */

public interface OmOtherInOrderService extends IService<OmOtherInOrder> {
    List<OmOtherInOrderDto> findList(Map<String, Object> map);

    int packageAutoOutOrder(OmOtherInOrder omOtherInOrder);

    /**
     * 数量反写
     * @param omOtherInOrderDet
     * @return
     */
    int writeQty(OmOtherInOrderDet omOtherInOrderDet);

    int pushDown(List<OmOtherInOrderDet> omOtherInOrderDets);

    int updateOtherInPutDownQty(Long otherInOrderDetId, BigDecimal putawayQty);
}
