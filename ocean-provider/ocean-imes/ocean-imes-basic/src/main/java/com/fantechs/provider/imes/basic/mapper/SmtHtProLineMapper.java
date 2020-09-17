package com.fantechs.provider.imes.basic.mapper;



import com.fantechs.common.base.entity.basic.history.SmtHtProLine;
import com.fantechs.common.base.entity.basic.search.SearchSmtProLine;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtHtProLineMapper extends MyMapper<SmtHtProLine> {
    //int addBatchHtProLine(List<SmtHtProLine> list);

    List<SmtHtProLine> selectHtProLines(SearchSmtProLine searchSmtProLine);
}