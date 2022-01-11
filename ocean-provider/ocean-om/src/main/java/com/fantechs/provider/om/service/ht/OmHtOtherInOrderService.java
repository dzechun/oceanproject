package com.fantechs.provider.om.service.ht;

import com.fantechs.common.base.general.dto.om.OmHtOtherInOrderDto;
import com.fantechs.common.base.general.entity.om.OmHtOtherInOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2022/01/10.
 */

public interface OmHtOtherInOrderService extends IService<OmHtOtherInOrder> {
    List<OmHtOtherInOrderDto> findList(Map<String, Object> map);
}
