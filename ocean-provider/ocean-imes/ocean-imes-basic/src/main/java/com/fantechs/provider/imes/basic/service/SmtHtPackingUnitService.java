package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.entity.basic.history.SmtHtPackingUnit;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2020/11/03.
 */

public interface SmtHtPackingUnitService extends IService<SmtHtPackingUnit> {

    List<SmtHtPackingUnit> findHtList(Map<String, Object> map);
}
