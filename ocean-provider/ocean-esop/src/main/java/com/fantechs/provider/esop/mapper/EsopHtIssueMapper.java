package com.fantechs.provider.esop.mapper;

import com.fantechs.common.base.general.entity.esop.history.EsopHtIssue;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EsopHtIssueMapper extends MyMapper<EsopHtIssue> {
    List<EsopHtIssue> findHtList(Map<String,Object> map);
}