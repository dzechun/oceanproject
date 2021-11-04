package com.fantechs.provider.guest.eng.service;

import com.fantechs.common.base.general.dto.eng.EngContractQtyOrderAndPurOrderDto;
import com.fantechs.common.base.general.entity.eng.EngUserFollowContractQtyOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/03.
 */

public interface EngUserFollowContractQtyOrderService extends IService<EngUserFollowContractQtyOrder> {
    List<EngUserFollowContractQtyOrder> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<EngUserFollowContractQtyOrder> list);

    int follow(String contractQtyOrderIds);

    int cancelFollow(String contractQtyOrderIds);

    List<EngContractQtyOrderAndPurOrderDto> findFollowList(Map<String, Object> map);
}
