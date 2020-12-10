package com.fantechs.provider.imes.basic.service;


import com.fantechs.common.base.entity.basic.SmtStorage;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorage;
import com.fantechs.common.base.support.IService;

import java.util.List;
/**
 *
 * Created by wcz on 2020/09/23.
 */

public interface SmtStorageService extends IService<SmtStorage> {

    List<SmtStorage> findList(SearchSmtStorage searchSmtStorage);

    //从qis获取储位信息
    int updateWarehouseAndStorageFromQis() throws Exception;

    //根据编码进行批量更新
    int batchUpdate(List<SmtStorage> smtStorages);
}
