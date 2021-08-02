package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamJigStandingBookAttachmentDto;
import com.fantechs.common.base.general.entity.eam.EamJigStandingBookAttachment;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamJigStandingBookAttachmentMapper extends MyMapper<EamJigStandingBookAttachment> {
    List<EamJigStandingBookAttachmentDto> findList(Map<String,Object> map);
}