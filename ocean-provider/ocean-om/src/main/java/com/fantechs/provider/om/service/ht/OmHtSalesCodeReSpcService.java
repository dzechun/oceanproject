package com.fantechs.provider.om.service.ht;

import com.fantechs.common.base.general.dto.om.OmHtSalesCodeReSpcDto;
import com.fantechs.common.base.general.entity.om.OmHtSalesCodeReSpc;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/10/15.
 */

public interface OmHtSalesCodeReSpcService extends IService<OmHtSalesCodeReSpc> {
    List<OmHtSalesCodeReSpcDto> findList(Map<String, Object> map);
}
