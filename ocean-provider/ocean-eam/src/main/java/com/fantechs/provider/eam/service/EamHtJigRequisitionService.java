package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.entity.eam.history.EamHtJigRequisition;
import com.fantechs.common.base.support.IService;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/30.
 */

public interface EamHtJigRequisitionService extends IService<EamHtJigRequisition> {
    List<EamHtJigRequisition> findHtList(Map<String, Object> map);
}
