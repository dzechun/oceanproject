package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.dto.basic.SmtPackingUnitDto;
import com.fantechs.common.base.dto.basic.SmtProcessCategoryDto;
import com.fantechs.common.base.entity.basic.SmtPackingUnit;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/11/03.
 */

public interface SmtPackingUnitService extends IService<SmtPackingUnit> {

    List<SmtPackingUnitDto> findList(Map<String,Object> map);
}
