package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.entity.basic.SmtStorageMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageMaterial;
import com.fantechs.common.base.support.IService;

import java.util.List;
/**
 *
 * Created by wcz on 2020/09/24.
 */

public interface SmtStorageMaterialService extends IService<SmtStorageMaterial> {

    int insert(SmtStorageMaterial smtStorageMaterial);

    int batchDel(String ids);

    int updateById(SmtStorageMaterial smtStorageMaterial);

    SmtStorageMaterial selectById(Long id);

    List<SmtStorageMaterial> findList(SearchSmtStorageMaterial searchSmtStorageMaterial);
}
