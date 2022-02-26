package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseHtInAndOutRuleDto;
import com.fantechs.common.base.general.dto.basic.BaseInAndOutRuleDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseInAndOutRuleImport;
import com.fantechs.common.base.general.entity.basic.BaseInAndOutRule;
import com.fantechs.common.base.support.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
/**
 *
 * Created by mr.lei on 2021/12/30.
 */

public interface BaseInAndOutRuleService extends IService<BaseInAndOutRule> {
    List<BaseInAndOutRuleDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<BaseInAndOutRuleImport> list);

    List<BaseHtInAndOutRuleDto> findHtList(Map<String,Object> map);

    List<String> findView(Byte category);

    /**
     * 入库规则
     * @param warehouse
     * @param materialId
     * @param qty
     * @return
     */
    Long inRule(Long warehouseId, Long materialId, BigDecimal qty);

    /**
     * 出库规则
     * @param warehouseId
     * @param storageId
     * @param materialId
     * @param qty
     * @return
     */
    List<String> outRule(Long warehouseId,Long storageId,Long materialId,BigDecimal qty);
}
