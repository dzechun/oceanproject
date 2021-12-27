package com.fantechs.provider.om.service;

import com.fantechs.common.base.general.dto.om.OmHtTransferOrderDto;
import com.fantechs.common.base.general.dto.om.OmTransferOrderDetDto;
import com.fantechs.common.base.general.dto.om.OmTransferOrderDto;
import com.fantechs.common.base.general.entity.om.OmTransferOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by Mr.Lei on 2021/06/15.
 */

public interface OmTransferOrderService extends IService<OmTransferOrder> {
    List<OmTransferOrderDto> findList(Map<String, Object> map);

    int updateStatus(OmTransferOrder omTransferOrder);

    List<OmHtTransferOrderDto> findHtList(Map<String,Object> map);

    int pushDown(List<OmTransferOrderDetDto> omTransferOrderDetDtos);
}
