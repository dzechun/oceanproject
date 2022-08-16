package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleSpecDto;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSpec;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/11/07.
 */

public interface BaseBarcodeRuleSpecService extends IService<BaseBarcodeRuleSpec> {

    List<BaseBarcodeRuleSpecDto> findList(Map<String, Object> map);

    List<BaseBarcodeRuleSpec> findSpec(SearchBaseBarcodeRuleSpec searchBaseBarcodeRuleSpec);

    List<String> findFunction();

    String executeFunction(String functionName,String params);
}
