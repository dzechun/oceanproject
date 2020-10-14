package com.fantechs.provider.imes.apply.service;


import com.fantechs.common.base.dto.apply.SmtOrderDto;
import com.fantechs.common.base.entity.apply.SmtOrder;
import com.fantechs.common.base.entity.apply.search.SearchSmtOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/10/13.
 */

public interface SmtOrderService extends IService<SmtOrder> {

    List<SmtOrderDto> findList(Map<String,Object> parm);
}
