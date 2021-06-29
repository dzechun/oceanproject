package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamReturnOrderDetDto;
import com.fantechs.common.base.general.entity.eam.EamReturnOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;


/**
 *
 * Created by leifengzhi on 2021/06/29.
 */

public interface EamReturnOrderDetService extends IService<EamReturnOrderDet> {
    List<EamReturnOrderDetDto> findList(Map<String, Object> map);
}
