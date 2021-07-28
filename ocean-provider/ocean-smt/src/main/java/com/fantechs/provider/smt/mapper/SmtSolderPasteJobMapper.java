package com.fantechs.provider.smt.mapper;

import com.fantechs.common.base.general.dto.smt.SmtSolderPasteJobDto;
import com.fantechs.common.base.general.entity.smt.SmtSolderPasteJob;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmtSolderPasteJobMapper extends MyMapper<SmtSolderPasteJob> {
    List<SmtSolderPasteJobDto> findList(Map<String,Object> map);
}