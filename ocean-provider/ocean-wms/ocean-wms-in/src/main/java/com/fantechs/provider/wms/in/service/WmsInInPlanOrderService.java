package com.fantechs.provider.wms.in.service;

import com.fantechs.common.base.general.dto.wms.in.WmsInInPlanOrderDto;
import com.fantechs.common.base.general.dto.wms.in.imports.WmsInInPlanOrderImport;
import com.fantechs.common.base.general.entity.wms.in.WmsInInPlanOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/12/08.
 */

public interface WmsInInPlanOrderService extends IService<WmsInInPlanOrder> {
    List<WmsInInPlanOrderDto> findList(Map<String, Object> map);

    int save(WmsInInPlanOrderDto wmsInInPlanOrderDto);

    int update(WmsInInPlanOrderDto wmsInInPlanOrderDto);

    int close(String ids);

    Map<String, Object> importExcel(List<WmsInInPlanOrderImport> list);
}
