package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.entity.eam.history.EamHtIssue;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamHtIssueMapper extends MyMapper<EamHtIssue> {
    List<EamHtIssue> findHtList(Map<String,Object> map);
}