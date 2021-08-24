package com.fantechs.provider.esop.mapper;

import com.fantechs.common.base.general.entity.esop.history.EsopHtIssueAttachment;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EsopHtIssueAttachmentMapper extends MyMapper<EsopHtIssueAttachment> {
    List<EsopHtIssueAttachment> findHtList(Map<String,Object> map);
}