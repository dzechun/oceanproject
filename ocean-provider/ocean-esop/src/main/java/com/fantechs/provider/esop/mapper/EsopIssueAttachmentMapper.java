package com.fantechs.provider.esop.mapper;

import com.fantechs.common.base.general.dto.esop.EsopIssueAttachmentDto;
import com.fantechs.common.base.general.entity.esop.EsopIssueAttachment;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EsopIssueAttachmentMapper extends MyMapper<EsopIssueAttachment> {
    List<EsopIssueAttachmentDto> findList(Map<String,Object> map);
}