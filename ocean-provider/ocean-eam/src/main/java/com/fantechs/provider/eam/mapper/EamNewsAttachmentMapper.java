package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamNewsAttachmentDto;
import com.fantechs.common.base.general.entity.eam.EamNewsAttachment;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamNewsAttachmentMapper extends MyMapper<EamNewsAttachment> {
    List<EamNewsAttachmentDto> findList(Map<String,Object> map);
}