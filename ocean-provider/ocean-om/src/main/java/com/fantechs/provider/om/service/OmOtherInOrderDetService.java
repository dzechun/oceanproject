package com.fantechs.provider.om.service;

import com.fantechs.common.base.general.dto.om.OmOtherInOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmOtherInOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/06/21.
 */

public interface OmOtherInOrderDetService extends IService<OmOtherInOrderDet> {
    List<OmOtherInOrderDetDto> findList(Map<String, Object> map);

    List<OmOtherInOrderDetDto> findHtList(Map<String,Object> map);
}
