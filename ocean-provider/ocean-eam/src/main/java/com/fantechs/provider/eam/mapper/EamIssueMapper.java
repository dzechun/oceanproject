package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamIssueDto;
import com.fantechs.common.base.general.entity.eam.EamIssue;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamIssueMapper extends MyMapper<EamIssue> {
    List<EamIssueDto> findList(Map<String,Object> map);
}