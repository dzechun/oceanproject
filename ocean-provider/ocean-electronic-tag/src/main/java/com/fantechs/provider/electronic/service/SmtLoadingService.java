package com.fantechs.provider.electronic.service;

import com.fantechs.common.base.electronic.entity.SmtLoading;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/09.
 */

public interface SmtLoadingService extends IService<SmtLoading> {

    List<SmtLoading> findList(Map<String, Object> map);

    List<SmtLoading> findHtList(Map<String, Object> map);

}
