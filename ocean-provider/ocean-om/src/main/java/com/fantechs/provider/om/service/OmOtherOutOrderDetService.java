package com.fantechs.provider.om.service;

import com.fantechs.common.base.general.dto.om.OmOtherOutOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmOtherOutOrderDet;
import com.fantechs.common.base.support.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
/**
 *
 * Created by Mr.Lei on 2021/06/23.
 */

public interface OmOtherOutOrderDetService extends IService<OmOtherOutOrderDet> {
    List<OmOtherOutOrderDetDto> findList(Map<String, Object> map);

    int updatePutDownQty(Long detId, BigDecimal putawayQty);
}
