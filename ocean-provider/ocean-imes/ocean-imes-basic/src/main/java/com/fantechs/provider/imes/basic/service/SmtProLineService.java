package com.fantechs.provider.imes.basic.service;



import com.fantechs.common.base.entity.basic.SmtProLine;
import com.fantechs.common.base.entity.basic.search.SearchSmtProLine;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

public interface SmtProLineService extends IService<SmtProLine> {
    //根据条件查询生产线信息列表
    List<SmtProLine> findList(SearchSmtProLine searchSmtProLine);
    Map<String, Object> importExcel(List<SmtProLine> smtProLines);
}
