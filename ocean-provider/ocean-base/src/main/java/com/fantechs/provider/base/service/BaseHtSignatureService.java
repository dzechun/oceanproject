package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtSignature;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSignature;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/09/24.
 */

public interface BaseHtSignatureService extends IService<BaseHtSignature> {

    List<BaseHtSignature> findHtList(Map<String, Object> map);
}
