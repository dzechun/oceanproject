package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamIssueAttachmentDto;
import com.fantechs.common.base.general.entity.eam.EamIssueAttachment;
import com.fantechs.common.base.mybatis.MyMapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamIssueAttachmentMapper extends MyMapper<EamIssueAttachment> {
    List<EamIssueAttachmentDto> findList(Map<String,Object> map);
}