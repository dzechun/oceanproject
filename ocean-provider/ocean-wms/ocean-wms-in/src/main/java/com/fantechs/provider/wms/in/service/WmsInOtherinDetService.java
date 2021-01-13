package com.fantechs.provider.wms.in.service;

import com.fantechs.common.base.general.dto.wms.in.WmsInOtherinDetDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInOtherinDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;


/**
 *
 * Created by leifengzhi on 2021/01/12.
 */

public interface WmsInOtherinDetService extends IService<WmsInOtherinDet> {

    List<WmsInOtherinDetDto> findList(Map<String, Object> dynamicConditionByEntity);

}
