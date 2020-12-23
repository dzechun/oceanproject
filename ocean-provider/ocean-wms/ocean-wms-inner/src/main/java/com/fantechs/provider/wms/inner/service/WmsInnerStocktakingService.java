package com.fantechs.provider.wms.inner.service;


import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStocktakingDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStocktaking;
import com.fantechs.common.base.support.IService;


import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/16.
 */

public interface WmsInnerStocktakingService extends IService<WmsInnerStocktaking> {

    List<WmsInnerStocktakingDto> findList(Map<String, Object> map);

    //导入盘点明细
    Map<String, Object> importStocktaking(List<WmsInnerStocktakingDto> wmsInnerStocktakings);
}
