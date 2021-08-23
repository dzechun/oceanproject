package com.fantechs.provider.esop.mapper;

import com.fantechs.common.base.general.dto.esop.EsopIssueDto;
import com.fantechs.common.base.general.entity.esop.EsopIssue;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EsopIssueMapper extends MyMapper<EsopIssue> {
    List<EsopIssueDto> findList(Map<String,Object> map);
}