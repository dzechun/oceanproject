package com.fantechs.provider.om.service;

import com.fantechs.common.base.general.dto.om.OmTransferOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmTransferOrderDet;
import com.fantechs.common.base.support.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
/**
 *
 * Created by Mr.Lei on 2021/06/15.
 */

public interface OmTransferOrderDetService extends IService<OmTransferOrderDet> {
    List<OmTransferOrderDetDto> findList(Map<String, Object> map);

    int updatePutDownQty(Long detId, BigDecimal putawayQty);
}
