package com.fantechs.provider.baseapi.esop.service;

import com.fantechs.common.base.general.entity.restapi.esop.EsopDept;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/21.
 */

public interface EsopDeptService extends IService<EsopDept> {
    int addDept (Map<String, Object> map);
}
