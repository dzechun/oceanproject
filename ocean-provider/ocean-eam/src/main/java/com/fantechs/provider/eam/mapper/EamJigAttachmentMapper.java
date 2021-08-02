package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamJigAttachmentDto;
import com.fantechs.common.base.general.entity.eam.EamJigAttachment;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamJigAttachmentMapper extends MyMapper<EamJigAttachment> {
    List<EamJigAttachmentDto> findList(Map<String,Object> map);
}