package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.entity.basic.SmtProcess;
import com.fantechs.common.base.entity.basic.search.SearchSmtProcess;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/09/25.
 */

public interface SmtProcessService extends IService<SmtProcess> {

    List<SmtProcess> findList(SearchSmtProcess searchSmtProcess);
    Map<String, Object> importExcel(List<SmtProcess> smtProcesses);
}
