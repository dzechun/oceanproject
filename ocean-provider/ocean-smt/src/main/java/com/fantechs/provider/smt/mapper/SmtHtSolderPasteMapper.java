package com.fantechs.provider.smt.mapper;

import com.fantechs.common.base.general.dto.smt.SmtSolderPasteDto;
import com.fantechs.common.base.general.entity.smt.SmtSolderPaste;
import com.fantechs.common.base.general.entity.smt.history.SmtHtSolderPaste;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmtHtSolderPasteMapper extends MyMapper<SmtHtSolderPaste> {
    List<SmtSolderPasteDto> findHtList(Map<String,Object>map);
}