package com.fantechs.provider.imes.basic.mapper;



import com.fantechs.common.base.entity.basic.SmtProLine;
import com.fantechs.common.base.entity.basic.search.SearchSmtProLine;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtProLineMapper extends MyMapper<SmtProLine> {

    List<SmtProLine> findList(SearchSmtProLine searchSmtProLine);
}