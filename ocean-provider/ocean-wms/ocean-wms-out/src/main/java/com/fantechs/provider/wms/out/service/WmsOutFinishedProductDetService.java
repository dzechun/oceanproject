package com.fantechs.provider.wms.out.service;

import com.fantechs.common.base.general.dto.wms.out.WmsOutFinishedProductDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutFinishedProductDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/23.
 */

public interface WmsOutFinishedProductDetService extends IService<WmsOutFinishedProductDet> {

    List<WmsOutFinishedProductDetDto> findList(Map<String, Object> dynamicConditionByEntity);
}
