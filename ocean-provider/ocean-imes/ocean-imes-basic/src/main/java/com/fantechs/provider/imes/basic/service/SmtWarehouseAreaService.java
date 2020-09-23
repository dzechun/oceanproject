package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.entity.basic.SmtWarehouseArea;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/09/23.
 */

public interface SmtWarehouseAreaService extends IService<SmtWarehouseArea> {

   List<SmtWarehouseArea> findList(Map<String,Object> map);
}
