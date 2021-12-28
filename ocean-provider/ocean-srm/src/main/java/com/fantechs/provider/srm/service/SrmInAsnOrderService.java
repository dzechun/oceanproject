package com.fantechs.provider.srm.service;

import com.fantechs.common.base.general.dto.srm.SrmInAsnOrderDto;
import com.fantechs.common.base.general.entity.srm.SrmInAsnOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/25.
 */

public interface SrmInAsnOrderService extends IService<SrmInAsnOrder> {
    List<SrmInAsnOrderDto> findList(Map<String, Object> map);

    int save(SrmInAsnOrderDto srmInAsnOrderDto);

    int update(SrmInAsnOrderDto srmInAsnOrderDto);

    int batchUpdate(List<SrmInAsnOrderDto> srmInAsnOrderDtos);

    int send(List<SrmInAsnOrderDto> srmInAsnOrderDtos);

    SrmInAsnOrderDto detail(Long id);
}
