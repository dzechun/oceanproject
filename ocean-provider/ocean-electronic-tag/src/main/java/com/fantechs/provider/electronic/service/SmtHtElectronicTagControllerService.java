package com.fantechs.provider.electronic.service;

import com.fantechs.common.base.entity.history.SmtHtElectronicTagController;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/11/16.
 */

public interface SmtHtElectronicTagControllerService extends IService<SmtHtElectronicTagController> {

    List<SmtHtElectronicTagController> findHtList(Map<String,Object> map);
}
