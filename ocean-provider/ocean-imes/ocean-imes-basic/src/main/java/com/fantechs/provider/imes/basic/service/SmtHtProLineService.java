package com.fantechs.provider.imes.basic.service;



import com.fantechs.common.base.entity.basic.history.SmtHtProLine;
import com.fantechs.common.base.entity.basic.search.SearchSmtProLine;

import java.util.List;

public interface SmtHtProLineService {
    List<SmtHtProLine> selectHtProLines(SearchSmtProLine searchSmtProLine);
}
