package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.entity.basic.SmtProcess;
import com.fantechs.common.base.entity.basic.search.SearchSmtProcess;
import com.fantechs.common.base.support.IService;

import java.util.List;
/**
 *
 * Created by wcz on 2020/09/25.
 */

public interface SmtProcessService extends IService<SmtProcess> {

    int insert(SmtProcess smtProcess);

    int batchDel(String ids);

    int updateById(SmtProcess smtProcess);

    SmtProcess selectById(Long id);

    List<SmtProcess> findList(SearchSmtProcess searchSmtProcess);
}
