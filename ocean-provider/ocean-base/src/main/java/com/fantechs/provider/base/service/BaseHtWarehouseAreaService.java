package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtWarehouseArea;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;


/**
 *
 * Created by leifengzhi on 2020/09/23.
 */

public interface BaseHtWarehouseAreaService extends IService<BaseHtWarehouseArea> {

    List<BaseHtWarehouseArea> findHtList(Map<String,Object> map);

}
