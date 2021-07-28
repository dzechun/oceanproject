package com.fantechs.provider.smt.mapper;

import com.fantechs.common.base.general.dto.smt.SmtSolderPasteDto;
import com.fantechs.common.base.general.entity.smt.SmtSolderPaste;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmtSolderPasteMapper extends MyMapper<SmtSolderPaste> {
    List<SmtSolderPasteDto> findList(Map<String ,Object> map);

    Long findInvDet(@Param("barcode")String barcode);

    String findBatchCode(@Param("id")Long id);
}