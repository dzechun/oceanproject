package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderBomDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmExplainPlan;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderBom;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderBom;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/10/14.
 */

public interface SmtWorkOrderBomService extends IService<SmtWorkOrderBom> {

    List<SmtWorkOrderBomDto> findList(SearchSmtWorkOrderBom searchSmtWorkOrderBom);
    //新增或更新工单BOM
    int save(List<SmtWorkOrderBom> smtWorkOrderBomList);
    //动态条件删除对象
    int deleteByMap(Map<String,Object> map);
    //动态条件查询对象列表
    List<SmtWorkOrderBom> selectAll(Map<String,Object> map);
}
