package com.fantechs.provider.baseapi.esop.service;

import com.fantechs.common.base.general.entity.basic.BaseDept;
import com.fantechs.common.base.general.entity.restapi.esop.EsopDept;
import com.fantechs.common.base.support.IService;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/21.
 */

public interface EsopDeptService extends IService<EsopDept> {
    List<BaseDept> addDept(Map<String, Object> map) throws ParseException;
}
