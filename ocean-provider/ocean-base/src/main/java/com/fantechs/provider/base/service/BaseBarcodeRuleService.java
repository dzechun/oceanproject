package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleDto;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRule;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRule;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/10/26.
 */

public interface BaseBarcodeRuleService extends IService<BaseBarcodeRule> {

    List<BaseBarcodeRuleDto> findList(Map<String, Object> map);

    int preserve(BaseBarcodeRule baseBarcodeRule);

    /**
     * 根据标签类别ID集合获取规则列表
     * @param ids
     * @return
     */
    List<BaseBarcodeRule> findListByBarcodeRuleCategoryIds(List<Long> ids);
}
