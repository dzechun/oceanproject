package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamJigScrapOrderDto;
import com.fantechs.common.base.general.entity.eam.EamJigScrapOrder;
import com.fantechs.common.base.support.IService;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/19.
 */

public interface EamJigScrapOrderService extends IService<EamJigScrapOrder> {
    List<EamJigScrapOrderDto> findList(Map<String, Object> map);

    int save(EamJigScrapOrderDto record);

    int update(EamJigScrapOrderDto entity);

    int autoCreateOrder();
}
