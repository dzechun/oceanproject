package com.fantechs.provider.electronic.service;

import com.fantechs.common.base.entity.history.SmtHtElectronicTagStorage;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/11/17.
 */

public interface SmtHtElectronicTagStorageService extends IService<SmtHtElectronicTagStorage> {

    List<SmtHtElectronicTagStorage> findHtList(Map<String,Object> map);
}
