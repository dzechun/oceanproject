package com.fantechs.provider.imes.basic.service;



import com.fantechs.common.base.entity.sysmanage.SmtProLine;
import com.fantechs.common.base.entity.sysmanage.search.SearchSmtProLine;

import java.util.List;

public interface SmtProLineService {
    //根据条件查询生产线信息列表
    List<SmtProLine> findList(SearchSmtProLine searchSmtProLine);

    int insert(SmtProLine smtProLine);

    int updateById(SmtProLine smtProLine);

    int deleteByIds(List<String> proLineIds);

    List<SmtProLine> exportProLines(SearchSmtProLine searchSmtProLine);
}
