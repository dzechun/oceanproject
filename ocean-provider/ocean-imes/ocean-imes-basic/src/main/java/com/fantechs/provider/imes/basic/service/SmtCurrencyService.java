package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.dto.basic.SmtCurrencyDto;
import com.fantechs.common.base.entity.basic.SmtCurrency;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/11/13.
 */

public interface SmtCurrencyService extends IService<SmtCurrency> {

    List<SmtCurrencyDto> findList(Map<String, Object> map);
}
